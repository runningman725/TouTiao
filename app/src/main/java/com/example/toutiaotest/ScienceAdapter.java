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
import java.util.zip.Inflater;

/**
 * Created by Admin on 2017/9/3.
 */

public class ScienceAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<Science> mScienceList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public ScienceAdapter(ScienceActivity scienceActivity, List<Science> scienceList, ListView listView){
        mScienceList = scienceList;
        mInflater = LayoutInflater.from(scienceActivity);
        imageLoader = new ImageLoader(listView);
        URLS = new String[scienceList.size()];
        for (int i = 0; i <URLS.length ; i++) {
            URLS[i] = scienceList.get(i).scienceImgUrl;
        }
        listView.setOnScrollListener(this);
        mFirstIn=true;
    }
    @Override
    public int getCount() {
        return mScienceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mScienceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.science_item_layout, parent, false);
            viewHolder.tvScienceTitle = (TextView) convertView.findViewById(R.id.science_title);
            viewHolder.tvSciencDate = (TextView) convertView.findViewById(R.id.science_date);
            viewHolder.tvSciencAuthor = (TextView) convertView.findViewById(R.id.science_author);
            viewHolder.ivScienceImgUrl = (ImageView) convertView.findViewById(R.id.science_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvScienceTitle.setText(mScienceList.get(position).scienceTitle);
        viewHolder.tvSciencDate.setText(mScienceList.get(position).scienceDate);
        viewHolder.tvSciencAuthor.setText("By:"+mScienceList.get(position).scienceAuthorName);
//        viewHolder.ivScienceImgUrl.setImageResource(R.mipmap.ic_launcher);
        String url = mScienceList.get(position).scienceImgUrl;
        viewHolder.ivScienceImgUrl.setTag(url);
        imageLoader.showImageByAsyncTask(viewHolder.ivScienceImgUrl,url);
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadScienceImages(mStart,mEnd);
        }else {
            imageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if(mFirstIn && visibleItemCount>0){
            imageLoader.loadScienceImages(mStart,mEnd);
            mFirstIn=false;
        }

    }

    class ViewHolder{
        public TextView tvScienceTitle;
        public TextView tvSciencDate;
        public TextView tvSciencAuthor;
        public ImageView ivScienceImgUrl;
    }
}
