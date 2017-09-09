package com.example.toutiaotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.toutiaotest.R;

/**
 * Created by Admin on 2017/9/9.
 */

public class WebFinanceActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_finance);
        //使用webview显示画面
        showWebContent();

    }

    private void showWebContent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent=getIntent();
                String url=intent.getStringExtra("fiurl");
                WebView webView = (WebView) findViewById(R.id.web_view_finance);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);
            }
        });
    }
}
