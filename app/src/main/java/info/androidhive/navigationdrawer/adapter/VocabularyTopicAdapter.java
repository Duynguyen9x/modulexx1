package info.androidhive.navigationdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.model.WordInfo;
import info.androidhive.navigationdrawer.utils.ImageLoader;
import info.androidhive.navigationdrawer.utils.ImageUtils;

/**
 * Created by 8470p on 12/18/2016.
 */
public class VocabularyTopicAdapter extends ArrayAdapter<WordInfo> {

    private List<WordInfo> mWordList;
    private Context mContext;
    private ImageLoader mImageLoader;

    public VocabularyTopicAdapter(Context context, int resource, ArrayList<WordInfo> mWordList, ImageLoader imageLoader) {
        super(context, resource, mWordList);
        this.mContext = context;
        this.mWordList = mWordList;
        this.mImageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return ((null != mWordList) ? mWordList.size() : 0);
    }

    @Override
    public WordInfo getItem(int position) {
        return ((null != mWordList) ? mWordList.get(position) : null);
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
            convertView = layoutInflater.inflate(R.layout.word_item_layout, null);

            holder = new ViewHolder();

            holder.eng_word = (TextView) convertView.findViewById(R.id.eng_word);
            holder.viet_word = (TextView) convertView.findViewById(R.id.viet_word);
            holder.iconView = (ImageView) convertView.findViewById(R.id.app_icon);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (null != wordInfo) {
            holder.eng_word.setText(wordInfo.getEnglsih());
            holder.viet_word.setText(wordInfo.getVietnamese());
            // holder.iconView.setImageResource(mImageLoader.loadDrawableLocal(wordInfo.getEnglsih()));
            holder.iconView.setImageDrawable(ImageUtils.loadDrawableLocal(mContext, wordInfo.getEnglsih()));
            //mImageLoader.loadIcon(wordInfo.getEnglsih(), holder.iconView);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView eng_word;
        public TextView viet_word;
        public ImageView iconView;
    }

}
