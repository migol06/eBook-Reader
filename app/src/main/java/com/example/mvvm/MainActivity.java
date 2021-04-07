package com.example.mvvm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST= 1;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FloatingActionButton fab;

//    private ListView listView;
//    private DatabaseReference databaseReference;
//    private List<uploadFile> uploadPDFs;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();;
    private CollectionReference notebookRef=db.collection("Uploads");

    private shelfAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, addnoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });

        setUpRecyclerView();

//        uploadPDFs =new ArrayList<>();
//
//        viewAllFiles();

        //Previous work

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//
//        final NoteAdapter adapter=new NoteAdapter();
//        recyclerView.setAdapter(adapter);
//
//        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
//        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
//            @Override
//            public void onChanged(List<Note> notes) {
//                adapter.setNotes(notes);
//
//            }
//        });
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                noteViewModel.delete(adapter.getNotesAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(recyclerView);


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                uploadFile uploadFile = uploadPDFs.get(i);
//                Intent intent= new Intent();
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(uploadFile.getUrl()));
//                startActivity(intent);
//            }
//        });





        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.shelftwo);





    }

    private void setUpRecyclerView() {
        FirebaseRecyclerOptions<uploadFile> options1= new
                FirebaseRecyclerOptions.Builder<uploadFile>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Uploads"), uploadFile.class)
                .build();

        adapter=new shelfAdapter(options1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

//    private void viewAllFiles() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    uploadFile uploadFile=dataSnapshot1.getValue(com.example.mvvm.uploadFile.class);
//                    uploadPDFs.add(uploadFile);
//                }
//                String [] uploads= new String[uploadPDFs.size()];
//
//                for (int i=0; i<uploads.length;i++){
//                    uploads[i]= uploadPDFs.get(i).getName();
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                        android.R.layout.simple_list_item_1,uploads){
//                    @NonNull
//                    @Override
//                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                        View view=super.getView(position,convertView,parent);
//                        TextView myText=view.findViewById(android.R.id.text1);
//                        myText.setTextColor(Color.BLACK);
//
//
//
//                        return view;
//                    }
//                };
//                listView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    private void initViews() {
        recyclerView=findViewById(R.id.recycler_view);
        toolbar=findViewById(R.id.tool_bar);
        fab=findViewById(R.id.fab);
//        listView=findViewById(R.id.recycler_view);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater=getMenuInflater();
//        menuInflater.inflate(R.menu.side_menu,menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.side_menu,menu);

        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });
        return true;
    }

    private void processSearch(String s) {
        FirebaseRecyclerOptions<uploadFile> options1= new
                FirebaseRecyclerOptions.Builder<uploadFile>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Uploads").orderByChild("name").startAt(s).endAt(s + "\uf8ff"), uploadFile.class)
                .build();

        adapter=new shelfAdapter(options1);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                System.exit(0);
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}