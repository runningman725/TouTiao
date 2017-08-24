package com.example.toutiaotest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

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
    private Set<TouTiaoImageAsyncTask> mTask;

    public ImageLoader(ListView listview){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mListView=listview;
        mTask = new HashSet<>();
        mCaches=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null){
            mCaches.put(url, bitmap);
        }
    }
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    public void showImageByAsyncTask(ImageView ivTitle, String url){
        //查看缓存中的图片
        Bitmap bitmap = getBitmapFromCache(url);
        if(bitmap==null){
//            new TouTiaoImageAsyncTask(ivTitle,url).execute(url);
            ivTitle.setImageResource(R.mipmap.ic_launcher);
        }else{
            ivTitle.setImageBitmap(bitmap);
        }
    }

    public void loadImages(int start, int end) {
        for (int i = start; i <end ; i++) {
            String url = TouTiaoAdapter.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);
            if(bitmap==null){
                TouTiaoImageAsyncTask task = new TouTiaoImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else{
                ImageView ivTitle = (ImageView) mListView.findViewWithTag(url);
                ivTitle.setImageBitmap(bitmap);
            }
        }
    }

    public void cancelAllTasks() {
        if(mTask!=null){
            for (TouTiaoImageAsyncTask task : mTask) {
                task.cancel(false);
            }
        }
    }

    private class TouTiaoImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

//        private ImageView mImageView;
        private String mUrl;

        public TouTiaoImageAsyncTask(String url) {
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

    public Bitmap getBitmapFromUrl(String param) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(param).openConnection();
        InputStream is = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        connection.disconnect();
        is.close();
        return bitmap;
    }
}
