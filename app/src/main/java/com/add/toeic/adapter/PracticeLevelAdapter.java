package com.add.toeic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            holder.tv_part_num = (TextView) convertView.findViewById(R.id.tv_part_num);
            holder.tv_part_description = (TextView) convertView.findViewById(R.id.tv_part_description);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_part_topic);

            if (position == 1) {
                holder.imageView.setBackground(mContext.getDrawable(R.drawable.all_circle_white_bg_practise_1));
            } else if (position == 2) {
                holder.imageView.setBackground(mContext.getDrawable(R.drawable.all_circle_white_bg_practise_2));
            } else if (position == 3) {
                holder.imageView.setBackground(mContext.getDrawable(R.drawable.all_circle_white_bg_practise_3));
            } else if (position == 4) {
                holder.imageView.setBackground(mContext.getDrawable(R.drawable.all_circle_white_bg_practise_4));
            } else if (position == 5) {
                holder.imageView.setBackground(mContext.getDrawable(R.drawable.all_circle_white_bg_practise_5));
            }

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(wordInfo != null){
            holder.tv_part_num.setText(wordInfo.getEnglsih());
            holder.tv_part_description.setText(wordInfo.getVietnamese());
            holder.imageView.setImageDrawable(wordInfo.getIcon());
            holder.imageView.setClipToOutline(true);
        }

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public TextView tv_part_num;
        public TextView tv_part_description;
    }
}
