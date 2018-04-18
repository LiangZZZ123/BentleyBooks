package com.example.chaochin.bentleybooks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.content.Intent;

public class BookInformation extends AppCompatActivity  {

    WebView webView;
    String Url;
    Button confirmPost;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookinformation);
        webView = (WebView)findViewById(R.id.webView);
        confirmPost = (Button)findViewById(R.id.confirmPost);
        cancel = (Button)findViewById(R.id.cancel);


        confirmPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                Intent i1 = new Intent();
                i1.setClass(BookInformation.this, ManagePost.class);
                startActivity(i1);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                Intent i1 = new Intent();
                i1.setClass(BookInformation.this, ManagePost.class);
                startActivity(i1);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

}
