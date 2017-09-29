package com.example.toutiaotest.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.toutiaotest.R;
import com.example.toutiaotest.db.TouTiao;
import com.example.toutiaotest.adapter.TouTiaoAdapter;
import com.mob.tools.RxMob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//显示头条的Activity
public class MainActivity extends AppCompatActivity {

    public String Url = "http://v.juhe.cn/toutiao/index?type=top&key=30af0fb406d2655e39d3ff8a119368eb";
    private ListView lvTouTiao;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvTouTiao = (ListView) findViewById(R.id.tt_list_view);
        new TouTiaoAsyncTask().execute(Url);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        //刷新界面事件
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<TouTiao> touTiaos=getJsonData(Url);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TouTiaoAdapter adapter=new TouTiaoAdapter(MainActivity.this,touTiaos,lvTouTiao);
                                lvTouTiao.setAdapter(adapter);
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                }).start();


            }
        });
    }

    //通过url获取头条的数据
    private List<TouTiao> getJsonData(String param) {
        InputStream is = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(param).openConnection();
            is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = "";
            String line;
            while((line=br.readLine())!=null){
                result += line;
            }
            String jsonString = result;
            JSONObject obj = new JSONObject(jsonString);
            JSONObject obj1 = obj.getJSONObject("result");
            String obj2 = obj1.getString("stat");
            TouTiao toutiao;
            List<TouTiao> touTiaoList = new ArrayList<>();
            if("1".equals(obj2)){
                JSONArray ary = obj1.getJSONArray("data");
                for(int i=0;i<ary.length();i++){
                    JSONObject jsonObject = ary.getJSONObject(i);
                    toutiao = new TouTiao();
                    toutiao.toutiaoTitle = jsonObject.getString("title");
                    toutiao.toutiaoDate = jsonObject.getString("date");
                    toutiao.toutiaoImgUrl = jsonObject.getString("thumbnail_pic_s");
                    toutiao.toutiaoWebUrl = jsonObject.getString("url");
                    toutiao.toutiaoAuthor = jsonObject.getString("author_name");
//                    Log.d("TAG", "showImg"+toutiao.toutiaoImgUrl);
                    touTiaoList.add(toutiao);

                }
                return touTiaoList;
            }else {
                Toast.makeText(MainActivity.this,"json邀请状态出错",Toast.LENGTH_SHORT).show();
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    class TouTiaoAsyncTask extends AsyncTask<String,Void,List<TouTiao>>{

        @Override
        protected List<TouTiao> doInBackground(String... params) {

            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(final List<TouTiao> touTiaos) {
            super.onPostExecute(touTiaos);
            TouTiaoAdapter adapter=new TouTiaoAdapter(MainActivity.this,touTiaos,lvTouTiao);
            lvTouTiao.setAdapter(adapter);
            swipeRefresh.setRefreshing(false);
            //给头条每一项添加点击事件
            lvTouTiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String toutiao=touTiaos.get(position).toutiaoWebUrl;
                    String toutiaoTitle=touTiaos.get(position).toutiaoTitle;
                    String toutiaoImg=touTiaos.get(position).toutiaoImgUrl;
//                    Toast.makeText(MainActivity.this,toutiao,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,WebTouTiaoActivity.class);
                    intent.putExtra("tturl", toutiao);
                    intent.putExtra("ttTitle", toutiaoTitle);
                    intent.putExtra("ttImage", toutiaoImg);
                    startActivity(intent);
                }
            });

        }
    }


}
