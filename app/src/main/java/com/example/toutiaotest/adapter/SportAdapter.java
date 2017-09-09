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
import com.example.toutiaotest.activity.ScienceActivity;
import com.example.toutiaotest.activity.SportActivity;
import com.example.toutiaotest.db.Sport;
import com.example.toutiaotest.util.ImageLoader;

import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */

public class SportAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<Sport> mSportList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public SportAdapter(SportActivity sportActivity, List<Sport> sportList, ListView listView){
        mSportList = sportList;
        mInflater = LayoutInflater.from(sportActivity);
        imageLoader = new ImageLoader(listView);
        URLS = new String[sportList.size()];
        for (int i = 0; i <URLS.length ; i++) {
            URLS[i] = sportList.get(i).sportImgUrl;
        }
        listView.setOnScrollListener(this);
        mFirstIn=true;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadSportImages(mStart,mEnd);
        }else {
            imageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if(mFirstIn && visibleItemCount>0){
            imageLoader.loadSportImages(mStart,mEnd);
            mFirstIn=false;
        }
    }

    @Override
    public int getCount() {
        return mSportList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSportList.get(position);
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
            convertView = mInflater.inflate(R.layout.sport_item_layout, parent, false);
            viewHolder.tvSportTitle = (TextView) convertView.findViewById(R.id.sport_title);
            viewHolder.tvSportDate = (TextView) convertView.findViewById(R.id.sport_date);
            viewHolder.tvSportAuthor = (TextView) convertView.findViewById(R.id.sport_author);
            viewHolder.ivSportImgUrl = (ImageView) convertView.findViewById(R.id.sport_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSportTitle.setText(mSportList.get(position).sportTitle);
        viewHolder.tvSportDate.setText(mSportList.get(position).sportDate);
        viewHolder.tvSportAuthor.setText("By:"+mSportList.get(position).sportAuthorName);
//        viewHolder.ivScienceImgUrl.setImageResource(R.mipmap.ic_launcher);
        String url = mSportList.get(position).sportImgUrl;
        viewHolder.ivSportImgUrl.setTag(url);
        imageLoader.showImageByAsyncTask(viewHolder.ivSportImgUrl,url);
        return convertView;
    }
    class ViewHolder{
        public TextView tvSportTitle;
        public TextView tvSportDate;
        public TextView tvSportAuthor;
        public ImageView ivSportImgUrl;
    }
}
