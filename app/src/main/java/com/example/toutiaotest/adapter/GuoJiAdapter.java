package com.example.toutiaotest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toutiaotest.R;
import com.example.toutiaotest.activity.GuoJiActivity;
import com.example.toutiaotest.db.GuoJi;
import com.example.toutiaotest.util.ImageLoader;

import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */

public class GuoJiAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<GuoJi> mGuoJiList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public GuoJiAdapter(GuoJiActivity GuoJiActivity, List<GuoJi> GuoJiList, ListView listView){
        mGuoJiList = GuoJiList;
        mInflater = LayoutInflater.from(GuoJiActivity);
        imageLoader = new ImageLoader(listView);
        URLS = new String[GuoJiList.size()];
        for (int i = 0; i <URLS.length ; i++) {
            URLS[i] = GuoJiList.get(i).guojiImgUrl;
        }
        listView.setOnScrollListener(this);
        mFirstIn=true;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadGuoJiImages(mStart,mEnd);
        }else {
            imageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if(mFirstIn && visibleItemCount>0){
            imageLoader.loadGuoJiImages(mStart,mEnd);
            mFirstIn=false;
        }
    }

    @Override
    public int getCount() {
        return mGuoJiList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGuoJiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.guoji_item_layout, parent, false);
            viewHolder.tvGuoJiTitle = (TextView) convertView.findViewById(R.id.guoji_title);
            viewHolder.tvGuoJiDate = (TextView) convertView.findViewById(R.id.guoji_date);
            viewHolder.tvGuoJiAuthor = (TextView) convertView.findViewById(R.id.guoji_author);
            viewHolder.ivGuoJiImgUrl = (ImageView) convertView.findViewById(R.id.guoji_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvGuoJiTitle.setText(mGuoJiList.get(position).guojiTitle);
        viewHolder.tvGuoJiDate.setText(mGuoJiList.get(position).guojiDate);
        viewHolder.tvGuoJiAuthor.setText(mGuoJiList.get(position).guojiAuthorName);
//        viewHolder.ivScienceImgUrl.setImageResource(R.mipmap.ic_launcher);
        String url = mGuoJiList.get(position).guojiImgUrl;
        viewHolder.ivGuoJiImgUrl.setTag(url);
        imageLoader.showImageByAsyncTask(viewHolder.ivGuoJiImgUrl,url);
        return convertView;
    }
    class ViewHolder{
        public TextView tvGuoJiTitle;
        public TextView tvGuoJiDate;
        public TextView tvGuoJiAuthor;
        public ImageView ivGuoJiImgUrl;
    }
}
