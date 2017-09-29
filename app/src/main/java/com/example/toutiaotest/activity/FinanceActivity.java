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
import com.example.toutiaotest.adapter.FinanceAdapter;
import com.example.toutiaotest.db.Finance;

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

public class FinanceActivity extends AppCompatActivity{
    private String URL = "http://v.juhe.cn/toutiao/index?type=caijing&key=30af0fb406d2655e39d3ff8a119368eb";
    private ListView listView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        listView = (ListView) findViewById(R.id.lv_finance);
        new FinanceAsyncTask().execute(URL);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_finance);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<Finance> finances = getJsonData(URL);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FinanceAdapter FinanceAdapter = new FinanceAdapter(FinanceActivity.this,finances,listView);
                                listView.setAdapter(FinanceAdapter);
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });

    }

    //通过URL获取科技新闻的内容集合
    private List<Finance> getJsonData(String url) {
        try {
            List<Finance> financeList=new ArrayList<>();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            InputStream is = connection.getInputStream();
            String jsonString = readJSON(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            String obj1 = jsonObject.getString("reason");
            Finance finance;
            if ("成功的返回".equals(obj1)){
                JSONObject object = jsonObject.getJSONObject("result");
                JSONArray ary = object.getJSONArray("data");
                for(int i=0;i<ary.length();i++){
                    finance = new Finance();
                    JSONObject obj = ary.getJSONObject(i);
                    finance.financeTitle = obj.getString("title");
                    finance.financeDate = obj.getString("date");
                    finance.financeImgUrl = obj.getString("thumbnail_pic_s");
                    finance.financeWebUrl = obj.getString("url");
                    finance.financeAuthorName = obj.getString("author_name");
                    financeList.add(finance);
                }
                return financeList;
            }else{
                Toast.makeText(FinanceActivity.this, "数据返回失败", Toast.LENGTH_SHORT).show();
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
        BufferedReader br = null;
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

    private class FinanceAsyncTask extends AsyncTask<String,Void,List<Finance>> {
        @Override
        protected List<Finance> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(final List<Finance> finances) {
            super.onPostExecute(finances);
            FinanceAdapter financeAdapter = new FinanceAdapter(FinanceActivity.this,finances,listView);
            listView.setAdapter(financeAdapter);
            swipeRefresh.setRefreshing(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String financeUrl = finances.get(position).financeWebUrl;
                    String financeTitle = finances.get(position).financeTitle;
                    Intent intent = new Intent(FinanceActivity.this, WebFinanceActivity.class);
                    intent.putExtra("fiurl",financeUrl);
                    intent.putExtra("fiTitle", financeTitle);
                    Log.d("TAG", "onItemClick: "+financeUrl);
                    startActivity(intent);
                }
            });
        }
    }
}
