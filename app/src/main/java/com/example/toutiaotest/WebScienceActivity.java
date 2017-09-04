package com.example.toutiaotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by Admin on 2017/9/3.
 */

public class WebScienceActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_science);
        //使用webview显示画面
        showWebScience();
    }

    private void showWebScience() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent=getIntent();
                String url=intent.getStringExtra("url");
                WebView webView = (WebView) findViewById(R.id.web_view_science);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);
            }
        });

    }
}
