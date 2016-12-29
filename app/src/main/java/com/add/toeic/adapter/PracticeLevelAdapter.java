package com.add.toeic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.add.toeic.R;
import com.add.toeic.model.WordInfo;

import java.util.ArrayList;

/**
 * Created by 8470p on 12/29/2016.
 */
public class PracticeLevelAdapter extends ArrayAdapter<WordInfo> {
    ArrayList<WordInfo> mWordInfoArrayList;
    Context mContext;

    public PracticeLevelAdapter(Context context, int resource, ArrayList<WordInfo> mArrayList) {
        super(context, resource, mArrayList);
        this.mWordInfoArrayList = mArrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mWordInfoArrayList.size();
    }

    @Override
    public WordInfo getItem(int position) {
        return (mWordInfoArrayList != null) ? mWordInfoArrayList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        WordInfo wordInfo = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.practice_lv_item, null);

            holder = new ViewHolder();
            holder.topic = (TextView) convertView.findViewById(R.id.tv_practice_item);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(wordInfo != null){
            holder.topic.setText(wordInfo.getVietnamese());
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView topic;
    }
}
