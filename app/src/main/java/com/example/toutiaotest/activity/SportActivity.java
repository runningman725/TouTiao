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
import com.example.toutiaotest.adapter.SportAdapter;
import com.example.toutiaotest.db.Sport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */

public class SportActivity extends AppCompatActivity{
    private String URL = "http://v.juhe.cn/toutiao/index?type=tiyu&key=30af0fb406d2655e39d3ff8a119368eb";
    private ListView listView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        listView = (ListView) findViewById(R.id.lv_sport);
        new SportAsyncTask().execute(URL);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_sport);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<Sport> sports = getJsonData(URL);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SportAdapter sportAdapter = new SportAdapter(SportActivity.this,sports,listView);
                                listView.setAdapter(sportAdapter);
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });

    }

    //通过URL获取科技新闻的内容集合
    private List<Sport> getJsonData(String url) {
        try {
            List<Sport> sportList=new ArrayList<>();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            InputStream is=connection.getInputStream();
            String jsonString = readJSON(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            String obj1 = jsonObject.getString("reason");
            Sport sport=null;
            if ("成功的返回".equals(obj1)){
                JSONObject object = jsonObject.getJSONObject("result");
                JSONArray ary = object.getJSONArray("data");
                for(int i=0;i<ary.length();i++){
                    sport = new Sport();
                    JSONObject obj = ary.getJSONObject(i);
                    sport.sportTitle = obj.getString("title");
                    sport.sportDate = obj.getString("date");
                    sport.sportImgUrl = obj.getString("thumbnail_pic_s");
                    sport.sportWebUrl = obj.getString("url");
                    sport.sportAuthorName = obj.getString("author_name");
                    sportList.add(sport);
                }
                return sportList;
            }else{
                Toast.makeText(SportActivity.this, "数据返回失败", Toast.LENGTH_SHORT).show();
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readJSON(InputStream inputStream) {
        String line;
        String result = "";
        BufferedReader br=null;
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
            br = new BufferedReader(isr);
            while ((line=br.readLine())!=null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private class SportAsyncTask extends AsyncTask<String,Void,List<Sport>> {
        @Override
        protected List<Sport> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(final List<Sport> sports) {
            super.onPostExecute(sports);
            SportAdapter sportAdapter = new SportAdapter(SportActivity.this, sports, listView);
            listView.setAdapter(sportAdapter);
            swipeRefresh.setRefreshing(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String sportUrl = sports.get(position).sportWebUrl;
                    String sportTitle = sports.get(position).sportTitle;
                    Intent intent = new Intent(SportActivity.this, WebSportActivity.class);
                    intent.putExtra("spurl", sportUrl);
                    intent.putExtra("spTitle", sportTitle);
                    Log.d("TAG", "onItemClick: " + sportUrl);
                    startActivity(intent);
                }
            });
        }
    }
}
