package com.example.toutiaotest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 2017/8/14.
 */

public class TouTiaoAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private List<TouTiao> mTouTiao;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public TouTiaoAdapter(MainActivity mainActivity, List<TouTiao> touTiaos, ListView lvTouTiao){
        mTouTiao=touTiaos;
        mInflater = LayoutInflater.from(mainActivity);
        mImageLoader = new ImageLoader(lvTouTiao);
        URLS = new String[touTiaos.size()];
        for (int i = 0; i <URLS.length ; i++) {
            URLS[i] = touTiaos.get(i).toutiaoImgUrl;
        }
        //给listview添加滑动事件
        lvTouTiao.setOnScrollListener(this);
        mFirstIn=true;
    }
    @Override
    public int getCount() {
        return mTouTiao.size();
    }

    @Override
    public Object getItem(int position) {
        return mTouTiao.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.toutiao_item_layout, parent,false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title_text);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.date_text);
            viewHolder.ivTitle = (ImageView) convertView.findViewById(R.id.image_title);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(mTouTiao.get(position).toutiaoTitle);
        viewHolder.tvDate.setText(mTouTiao.get(position).toutiaoDate);
//        viewHolder.ivTitle.setImageResource(R.mipmap.ic_launcher);
        String url=mTouTiao.get(position).toutiaoImgUrl;
        viewHolder.ivTitle.setTag(url);
        mImageLoader.showImageByAsyncTask(viewHolder.ivTitle,url);
        return convertView;
    }

    //界面滑动，加载图片
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            //加载可见项
            mImageLoader.loadImages(mStart, mEnd);
        }else{
            //停止任务
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart=firstVisibleItem;
        mEnd=firstVisibleItem+visibleItemCount;
        //当画面第一次加载并显示item时，加载所见列表项目
        if(mFirstIn && visibleItemCount>0){
            mImageLoader.loadImages(mStart,mEnd);
            mFirstIn = false;
        }
    }

    class ViewHolder{
        public TextView tvTitle,tvDate;
        public ImageView ivTitle;
    }
}
