package com.example.toutiaotest;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/9/2.
 */

public class ViewPagerAdapter extends PagerAdapter{

    private List<View> list = new ArrayList<>();
    public ViewPagerAdapter(List<View> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewPager pViewPager = (ViewPager) container;
        pViewPager.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager pViewPager = (ViewPager) container;
        pViewPager.removeView(list.get(position));
    }
}
