package com.example.toutiaotest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.toutiaotest.R;
import com.example.toutiaotest.adapter.FinanceAdapter;
import com.example.toutiaotest.adapter.GuoJiAdapter;
import com.example.toutiaotest.adapter.ScienceAdapter;
import com.example.toutiaotest.adapter.SportAdapter;
import com.example.toutiaotest.adapter.TouTiaoAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 2017/8/14.
 */

public class ImageLoader {

    private LruCache<String,Bitmap> mCaches;
    private ListView mListView;
    private Set<NewsImageAsyncTask> mTask;

    public ImageLoader(){

    }

    public ImageLoader(ListView listview){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mListView=listview;
        mTask = new HashSet<>();
//        activity=mainActivity;
        //初始化LruCache
        mCaches=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    //把bitmap对象添加到缓存
    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null){
            mCaches.put(url, bitmap);
        }
    }
    //从缓存中获取图片
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    //获取图片
    public void showImageByAsyncTask(ImageView ivTitle, String url){
        //查看缓存中的图片
        Bitmap bitmap = getBitmapFromCache(url);
        if(bitmap==null){
//            new NewsImageAsyncTask(ivTitle,url).execute(url);
            //如果缓存中没有图片，就加载ic_launcher，把图片加载事件从getView()转移成滑动添加图片事件
            ivTitle.setImageResource(R.mipmap.ic_launcher);
        }else{
            ivTitle.setImageBitmap(bitmap);
        }
    }

    //获取头条新闻相应URL的集合的图片
    public void loadImages(int start, int end) {
        for (int i = start; i <end ; i++) {
            String url = TouTiaoAdapter.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
//            String bmString=sp.getString("bitmapString", "");
//            String url1 = sp.getString("url", "");

            //如果缓存中没有bitmap对象就通过AsyncTask异步加载
            if(bitmap==null){
                NewsImageAsyncTask task = new NewsImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView ivTitle = (ImageView) mListView.findViewWithTag(url);
                ivTitle.setImageBitmap(bitmap);
            }
//            if(bmString!=null){
//                ByteArrayInputStream bis = new ByteArrayInputStream(bmString.getBytes());
//                Bitmap bm = BitmapFactory.decodeStream(bis);
//                ImageView ivTitle1 = (ImageView) mListView.findViewWithTag(url1);
//                ivTitle1.setImageBitmap(bm);
//            }
        }
    }

    //获取科技新闻相应URL的集合的图片
    public void loadScienceImages(int start, int end) {
        for (int i = start; i <end ; i++) {
            String url = ScienceAdapter.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);

            if(bitmap==null){
                NewsImageAsyncTask task = new NewsImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView ivTitle = (ImageView) mListView.findViewWithTag(url);
                ivTitle.setImageBitmap(bitmap);
            }

        }
    }
    //获取体育新闻相应URL的集合的图片
    public void loadSportImages(int start, int end) {
        for (int i = start; i <end ; i++) {
            String url = SportAdapter.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);

            if(bitmap==null){
                NewsImageAsyncTask task = new NewsImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView ivTitle = (ImageView) mListView.findViewWithTag(url);
                ivTitle.setImageBitmap(bitmap);
            }

        }
    }
    //获取国际新闻相应URL的集合的图片
    public void loadGuoJiImages(int start, int end) {
        for (int i = start; i <end ; i++) {
            String url = GuoJiAdapter.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);

            if(bitmap==null){
                NewsImageAsyncTask task = new NewsImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView ivTitle = (ImageView) mListView.findViewWithTag(url);
                ivTitle.setImageBitmap(bitmap);
            }

        }
    }
    //获取财经新闻相应URL的集合的图片
    public void loadFinanceImages(int start, int end) {
        for (int i = start; i <end ; i++) {
            String url = FinanceAdapter.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);

            if(bitmap==null){
                NewsImageAsyncTask task = new NewsImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView ivTitle = (ImageView) mListView.findViewWithTag(url);
                ivTitle.setImageBitmap(bitmap);
            }

        }
    }

    //URL集合加载完了，取消异步加载
    public void cancelAllTasks() {
        if(mTask!=null){
            for (NewsImageAsyncTask task : mTask) {
                task.cancel(false);
            }
        }
    }

    private class NewsImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

//        private ImageView mImageView;
        private String mUrl;
        private Context mActivity;

        public NewsImageAsyncTask(String url) {
//            mImageView = ivTitle;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String url = params[0];
                Bitmap bitmap=getBitmapFromUrl(url);
                if(bitmap!=null){
                    addBitmapToCache(url,bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            if(mImageView.getTag().equals(mUrl)){
//                mImageView.setImageBitmap(bitmap);
//            }
            ImageView ivTitle = (ImageView) mListView.findViewWithTag(mUrl);
            if(bitmap!=null && ivTitle!=null){
                ivTitle.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }

    //从URL中获取bitmap
    public Bitmap getBitmapFromUrl(String param) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(param).openConnection();
        InputStream is = connection.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line=reader.readLine())!=null){
//            response.append(line);
//        }
//        String result = response.toString();
//        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(activity).edit();
//        editor.putString("urlString",result);
//        editor.putString("url", param);
//        editor.apply();

        Bitmap bitmap = BitmapFactory.decodeStream(is);
        connection.disconnect();
        is.close();
        return bitmap;
    }
}
