package com.add.toeic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.add.toeic.R;
import com.add.toeic.model.BookTheory;

/**
 * Created by 8470p on 12/28/2016.
 */
public class BookTheoryAdapter extends ArrayAdapter<BookTheory> {
    ArrayList<BookTheory> mArrayList;
    Context mContext;

    public BookTheoryAdapter(Context context, int resource, ArrayList<BookTheory> mArrayList) {
        super(context, resource, mArrayList);
        this.mContext = context;
        this.mArrayList = mArrayList;
    }

    @Override
    public int getCount() {
        return ((null != mArrayList) ? mArrayList.size() : 0);
    }

    @Override
    public BookTheory getItem(int position) {
        return ((null != mArrayList) ? mArrayList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BookTheory bookTheory = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.booktheory_item_layout, null);

            holder = new ViewHolder();

            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.content = (TextView) convertView.findViewById(R.id.contents);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (null != bookTheory) {
            holder.number.setText(bookTheory.getNumber());
            holder.content.setText(bookTheory.getContent());
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView number;
        public TextView content;
    }
}
