package com.example.toutiaotest;

import android.app.ActivityManager;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LocalActivityManager localActivityManager;
    private TextView tab0;
    private TextView tab1;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        localActivityManager = new LocalActivityManager(this,true);
        localActivityManager.dispatchCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //给画面底部的tab初始化
        initTextView();
        //初始化viewpager,给viewpager添加Activity选项
        initViewerPager();

    }

    private void initTextView() {
        tab0 = (TextView) findViewById(R.id.tv_tab0);
        tab1 = (TextView) findViewById(R.id.tv_tab1);

        //给tab添加点击事件
        tab0.setOnClickListener(this);
        tab1.setOnClickListener(this);
    }

    private void initViewerPager() {

                viewPager = (ViewPager) findViewById(R.id.view_pager);
                List<View> list = new ArrayList<>();
                Intent intent0 = new Intent(ViewPagerActivity.this, MainActivity.class);
                list.add(getView("a",intent0));
                Intent intent1 = new Intent(ViewPagerActivity.this, ScienceActivity.class);
                list.add(getView("b",intent1));
                //设置画面加载viewpager时的默认显示
                viewPager.setCurrentItem(0);
                //给viewpager添加适配器
                viewPager.setAdapter(new ViewPagerAdapter(list));
                //viewpager添加滑动事件
                viewPager.addOnPageChangeListener(this);

    }

    private View getView(String a, Intent intent0) {
        return localActivityManager.startActivity(a, intent0).getDecorView();
    }

    //tab点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_tab0:
                tab0.setBackgroundColor(Color.GREEN);
                tab1.setBackgroundColor(Color.WHITE);
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab1:
                tab0.setBackgroundColor(Color.WHITE);
                tab1.setBackgroundColor(Color.GREEN);
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //画面被选中时
    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                tab0.setBackgroundColor(Color.GREEN);
                tab1.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                tab0.setBackgroundColor(Color.WHITE);
                tab1.setBackgroundColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
