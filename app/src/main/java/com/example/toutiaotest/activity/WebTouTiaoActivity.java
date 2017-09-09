package com.example.toutiaotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.toutiaotest.R;


public class WebTouTiaoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_toutiao);
        //使用webview显示画面
        showWebContent();

    }

    private void showWebContent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent=getIntent();
                String url=intent.getStringExtra("tturl");
                WebView webView = (WebView) findViewById(R.id.web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);
            }
        });
    }

}
