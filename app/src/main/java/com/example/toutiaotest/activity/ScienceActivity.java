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
import com.example.toutiaotest.adapter.ScienceAdapter;
import com.example.toutiaotest.db.Science;

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

public class ScienceActivity extends AppCompatActivity {

    private String URL = "http://v.juhe.cn/toutiao/index?type=keji&key=30af0fb406d2655e39d3ff8a119368eb";
    private ListView listView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science);
        listView = (ListView) findViewById(R.id.lv_science);
        new ScienceAsyncTask().execute(URL);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_science);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<Science> sciences = getJsonData(URL);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ScienceAdapter scienceAdapter = new ScienceAdapter(ScienceActivity.this,sciences,listView);
                                listView.setAdapter(scienceAdapter);
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });

    }

    //通过URL获取科技新闻的内容集合
    private List<Science> getJsonData(String url) {
        try {
            List<Science> scienceList=new ArrayList<>();
            String jsonString = readJSON(new URL(url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);
            String obj1 = jsonObject.getString("reason");
            Science science;
            if ("成功的返回".equals(obj1)){
                JSONObject object = jsonObject.getJSONObject("result");
                JSONArray ary = object.getJSONArray("data");
                for(int i=0;i<ary.length();i++){
                    science = new Science();
                    JSONObject obj = ary.getJSONObject(i);
                    science.scienceTitle = obj.getString("title");
                    science.scienceDate = obj.getString("date");
                    science.scienceImgUrl = obj.getString("thumbnail_pic_s");
                    science.scienceWebUrl = obj.getString("url");
                    science.scienceAuthorName = obj.getString("author_name");
                    scienceList.add(science);
                }
                return scienceList;
            }else{
                Toast.makeText(ScienceActivity.this, "数据返回失败", Toast.LENGTH_SHORT).show();
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

    private class ScienceAsyncTask extends AsyncTask<String,Void,List<Science>>{
        @Override
        protected List<Science> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(final List<Science> sciences) {
            super.onPostExecute(sciences);
            ScienceAdapter scienceAdapter = new ScienceAdapter(ScienceActivity.this,sciences,listView);
            listView.setAdapter(scienceAdapter);
            swipeRefresh.setRefreshing(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String scienceUrl = sciences.get(position).scienceWebUrl;
                    Intent intent = new Intent(ScienceActivity.this, WebScienceActivity.class);
                    intent.putExtra("scurl",scienceUrl);
                    Log.d("TAG", "onItemClick: "+scienceUrl);
                    startActivity(intent);
                }
            });
        }
    }



}
