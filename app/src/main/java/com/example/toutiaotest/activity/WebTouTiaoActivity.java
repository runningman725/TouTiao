package com.example.toutiaotest.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.toutiaotest.R;
import com.example.toutiaotest.util.SaveBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sharesdk.onekeyshare.OnekeyShare;


public class WebTouTiaoActivity extends AppCompatActivity {

    private String toutiaoTitle;
    private String url;
    private String toutiaoImg;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_toutiao);
        //使用webview显示画面
        showWebContent();
        toutiaoTitle = getIntent().getStringExtra("ttTitle");
//        toutiaoImg = getIntent().getStringExtra("ttImage");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bitmap=getBitmapFromUrl(toutiaoImg);
//                addBitmapToSDCard(bitmap,"toutiaoImage");
////                Bitmap bm=SaveBitmap.getImageFromSDCard("toutiaoImage");
//            }
//        }).start();

    }

//    private void addBitmapToSDCard(final Bitmap bitmap, final String toutiaoImage) {
//                try {
//                    SaveBitmap.saveImage(bitmap,toutiaoImage);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//    }

//    private Bitmap getBitmapFromUrl(final String toutiaoImg) {
//            Bitmap bitmap;
//
//                try {
//                    HttpURLConnection connection = (HttpURLConnection) new URL(toutiaoImg).openConnection();
//                    InputStream is=connection.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//                    return bitmap;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//
//    }

    private void showWebContent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent=getIntent();
                url=intent.getStringExtra("tturl");
                WebView webView = (WebView) findViewById(R.id.web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(url);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_btn:
//                Toast.makeText(this,"you clicked share btn",Toast.LENGTH_SHORT).show();
                showShare();
                break;
            default:
                break;
        }
        return true;
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("这是易新闻");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(toutiaoTitle+"\n"+url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath(Environment.getExternalStorageDirectory().getPath());//确保SDcard下面存在此张图片
//        oks.setImageUrl(toutiaoImg);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
