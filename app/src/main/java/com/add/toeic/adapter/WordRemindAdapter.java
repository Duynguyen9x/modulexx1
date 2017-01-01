package com.add.toeic.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.model.Word;
import com.add.toeic.model.WordInfo;
import com.add.toeic.utils.ImageLoader;
import com.add.toeic.utils.ImageUtils;
import com.bumptech.glide.Glide;

/**
 * Created by 8470p on 12/25/2016.
 */
public class WordRemindAdapter extends ArrayAdapter<Word> {

    private List<Word> mWordList;
    private Context mContext;
    private ImageLoader mImageLoader;

    private OnRemindButtonClickListener mListener;
    private boolean mIsDelete;

    public void setOnRemindButtonClickListener(OnRemindButtonClickListener listener) {
        mListener = listener;
    }

    public WordRemindAdapter(Context context, int resource, ArrayList<Word> mWordList, boolean isDelete) {
        super(context, resource, mWordList);
        this.mContext = context;
        this.mWordList = mWordList;
        this.mIsDelete = isDelete;
    }

    @Override
    public int getCount() {
        return ((null != mWordList) ? mWordList.size() : 0);
    }

    @Override
    public Word getItem(int position) {
        return ((null != mWordList) ? mWordList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Word word = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.word_item_layout_for_remind, null);

            holder = new ViewHolder();

            holder.eng_word = (TextView) convertView.findViewById(R.id.eng_word);
            holder.viet_word = (TextView) convertView.findViewById(R.id.viet_word);
            holder.iconView = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.remindView = (ImageButton) convertView.findViewById(R.id.remind);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (null != word) {
            holder.eng_word.setText(word.getName());
            holder.viet_word.setText(word.getName_key());

            Glide.with(mContext).load(Uri.parse(ImageUtils.loadDrawableWord(word.getName())))
                    .centerCrop().into(holder.iconView);


        //    Glide.with(mContext).load(Uri.parse(ImageUtils.loadDrawableChild(groupPosition))).centerCrop().into(holder.iconView);

            // holder.remindView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_alarms));
            //mImageLoader.loadIcon(wordInfo.getEnglsih(), holder.iconView);
        }

        holder.remindView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onHandleRemindButtonClick();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public TextView eng_word;
        public TextView viet_word;
        public ImageView iconView;
        public ImageButton remindView;
    }

    // handle event click remind button
    public interface OnRemindButtonClickListener {
        // TODO: Update argument type and name
        void onHandleRemindButtonClick();
    }
}