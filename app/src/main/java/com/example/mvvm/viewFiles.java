package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class viewFiles extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);
        initViews();
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setBuiltInZoomControls(true);

        String filename= getIntent().getStringExtra("filename");
        String fileurl=getIntent().getStringExtra("fileurl");

        final ProgressDialog pd= new ProgressDialog(this);
        pd.setTitle(filename);
        pd.setMessage("Opening...");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url="";
        try{
            url= URLEncoder.encode(fileurl, "UTF-8");
        }catch (Exception e){

        }

        webView.loadUrl("https://docs.google.com/viewer?url=" + url);





    }

    private void initViews() {
        webView=findViewById(R.id.viewFiles);
    }
}