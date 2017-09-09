package com.example.toutiaotest.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.toutiaotest.R;
import com.example.toutiaotest.adapter.GuoJiAdapter;
import com.example.toutiaotest.db.GuoJi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */

public class GuoJiActivity extends AppCompatActivity{
    private String URL = "http://v.juhe.cn/toutiao/index?type=guoji&key=30af0fb406d2655e39d3ff8a119368eb";
    private ListView listView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guoji);
        listView = (ListView) findViewById(R.id.lv_guoji);
        new GuoJiAsyncTask().execute(URL);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_guoji);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<GuoJi> guojis = getJsonData(URL);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GuoJiAdapter guojiAdapter = new GuoJiAdapter(GuoJiActivity.this,guojis,listView);
                                listView.setAdapter(guojiAdapter);
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });

    }

    //通过URL获取科技新闻的内容集合
    private List<GuoJi> getJsonData(String url) {
        try {
            List<GuoJi> guojiList=new ArrayList<>();
            String jsonString = readJSON(new URL(url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);
            String obj1 = jsonObject.getString("reason");
            GuoJi guoji;
            if ("成功的返回".equals(obj1)){
                JSONObject object = jsonObject.getJSONObject("result");
                JSONArray ary = object.getJSONArray("data");
                for(int i=0;i<ary.length();i++){
                    guoji = new GuoJi();
                    JSONObject obj = ary.getJSONObject(i);
                    guoji.guojiTitle = obj.getString("title");
                    guoji.guojiDate = obj.getString("date");
                    guoji.guojiImgUrl = obj.getString("thumbnail_pic_s");
                    guoji.guojiWebUrl = obj.getString("url");
                    guoji.guojiAuthorName = obj.getString("author_name");
                    guojiList.add(guoji);
                }
                return guojiList;
            }else{
                Toast.makeText(GuoJiActivity.this, "数据返回失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readJSON(InputStream inputStream) {
        String line = "";
        String result = "";
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line=br.readLine())!=null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class GuoJiAsyncTask extends AsyncTask<String,Void,List<GuoJi>> {
        @Override
        protected List<GuoJi> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(final List<GuoJi> guojis) {
            super.onPostExecute(guojis);
            GuoJiAdapter guojiAdapter = new GuoJiAdapter(GuoJiActivity.this,guojis,listView);
            listView.setAdapter(guojiAdapter);
            swipeRefresh.setRefreshing(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String GuoJiUrl = guojis.get(position).guojiWebUrl;
                    Intent intent = new Intent(GuoJiActivity.this, WebGuoJiActivity.class);
                    intent.putExtra("gjcurl",GuoJiUrl);
                    Log.d("TAG", "onItemClick: "+GuoJiUrl);
                    startActivity(intent);
                }
            });
        }
    }
}
