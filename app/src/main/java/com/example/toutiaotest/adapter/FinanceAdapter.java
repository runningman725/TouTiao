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
import com.example.toutiaotest.activity.FinanceActivity;
import com.example.toutiaotest.db.Finance;
import com.example.toutiaotest.util.ImageLoader;

import java.util.List;

/**
 * Created by Admin on 2017/9/9.
 */

public class FinanceAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<Finance> mFinanceList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public FinanceAdapter(FinanceActivity financeActivity, List<Finance> financeList, ListView listView){
        mFinanceList = financeList;
        mInflater = LayoutInflater.from(financeActivity);
        imageLoader = new ImageLoader(listView);
        URLS = new String[financeList.size()];
        for (int i = 0; i <URLS.length ; i++) {
            URLS[i] = financeList.get(i).financeImgUrl;
        }
        listView.setOnScrollListener(this);
        mFirstIn=true;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadFinanceImages(mStart,mEnd);
        }else {
            imageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if(mFirstIn && visibleItemCount>0){
            imageLoader.loadFinanceImages(mStart,mEnd);
            mFirstIn=false;
        }
    }

    @Override
    public int getCount() {
        return mFinanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFinanceList.get(position);
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
            convertView = mInflater.inflate(R.layout.finance_item_layout, parent, false);
            viewHolder.tvFinanceTitle = (TextView) convertView.findViewById(R.id.finance_title);
            viewHolder.tvFinanceDate = (TextView) convertView.findViewById(R.id.finance_date);
            viewHolder.tvFinanceAuthor = (TextView) convertView.findViewById(R.id.finance_author);
            viewHolder.ivFinanceImgUrl = (ImageView) convertView.findViewById(R.id.finance_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (FinanceAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvFinanceTitle.setText(mFinanceList.get(position).financeTitle);
        viewHolder.tvFinanceDate.setText(mFinanceList.get(position).financeDate);
        viewHolder.tvFinanceAuthor.setText("By:"+mFinanceList.get(position).financeAuthorName);
//        viewHolder.ivScienceImgUrl.setImageResource(R.mipmap.ic_launcher);
        String url = mFinanceList.get(position).financeImgUrl;
        viewHolder.ivFinanceImgUrl.setTag(url);
        imageLoader.showImageByAsyncTask(viewHolder.ivFinanceImgUrl,url);
        return convertView;
    }
    class ViewHolder{
        public TextView tvFinanceTitle;
        public TextView tvFinanceDate;
        public TextView tvFinanceAuthor;
        public ImageView ivFinanceImgUrl;
    }
}
