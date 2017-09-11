package com.example.toutiaotest.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.toutiaotest.R;
import com.example.toutiaotest.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LocalActivityManager localActivityManager;
    private ImageView tab0;
    private ImageView tab1;
    private ImageView tab2;
    private ImageView tab3;
    private ImageView tab4;
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
        tab0 = (ImageView) findViewById(R.id.iv_tab0);
        tab1 = (ImageView) findViewById(R.id.iv_tab1);
        tab2 = (ImageView) findViewById(R.id.iv_tab2);
        tab3 = (ImageView) findViewById(R.id.iv_tab3);
        tab4 = (ImageView) findViewById(R.id.iv_tab4);
        tab0.setImageResource(R.drawable.ic_toutiao);
        //给tab添加点击事件
        tab0.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
    }

    private void initViewerPager() {

                viewPager = (ViewPager) findViewById(R.id.view_pager);
                List<View> list = new ArrayList<>();
                Intent intent0 = new Intent(ViewPagerActivity.this, MainActivity.class);
                list.add(getView("a",intent0));
                Intent intent1 = new Intent(ViewPagerActivity.this, ScienceActivity.class);
                list.add(getView("b",intent1));
                Intent intent2 = new Intent(ViewPagerActivity.this, SportActivity.class);
                list.add(getView("c",intent2));
                Intent intent3 = new Intent(ViewPagerActivity.this, GuoJiActivity.class);
                list.add(getView("d",intent3));
                Intent intent4 = new Intent(ViewPagerActivity.this, FinanceActivity.class);
                list.add(getView("e",intent4));
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
            case R.id.iv_tab0:
                tab0.setImageResource(R.drawable.ic_toutiao);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                viewPager.setCurrentItem(0);
                break;
            case R.id.iv_tab1:
                tab1.setImageResource(R.drawable.ic_science);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                viewPager.setCurrentItem(1);
                break;
            case R.id.iv_tab2:
                tab2.setImageResource(R.drawable.ic_sport);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                viewPager.setCurrentItem(2);
                break;
            case R.id.iv_tab3:
                tab3.setImageResource(R.drawable.ic_guoji);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                viewPager.setCurrentItem(3);
                break;
            case R.id.iv_tab4:
                tab4.setImageResource(R.drawable.ic_finance);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                viewPager.setCurrentItem(4);
                break;
            default:
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
                tab0.setImageResource(R.drawable.ic_toutiao);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                break;
            case 1:
                tab1.setImageResource(R.drawable.ic_science);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                break;
            case 2:
                tab2.setImageResource(R.drawable.ic_sport);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                break;
            case 3:
                tab3.setImageResource(R.drawable.ic_guoji);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab4.setImageResource(R.drawable.ic_finance_black);
                break;
            case 4:
                tab4.setImageResource(R.drawable.ic_finance);
                tab0.setImageResource(R.drawable.ic_toutiao_black);
                tab1.setImageResource(R.drawable.ic_science_black);
                tab2.setImageResource(R.drawable.ic_sport_black);
                tab3.setImageResource(R.drawable.ic_guoji_black);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
