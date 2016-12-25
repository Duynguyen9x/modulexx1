package info.androidhive.navigationdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.model.WordInfo;
import info.androidhive.navigationdrawer.utils.ImageLoader;
import info.androidhive.navigationdrawer.utils.ImageUtils;

/**
 * Created by 8470p on 12/25/2016.
 */
public class VocabularyRemindAdapter  extends ArrayAdapter<WordInfo> {

    private List<WordInfo> mWordList;
    private Context mContext;
    private ImageLoader mImageLoader;

    private OnRemindButtonClickListener mListener;

    public void setOnRemindButtonClickListener(OnRemindButtonClickListener listener) {
        mListener = listener;
    }
    public VocabularyRemindAdapter(Context context, int resource, ArrayList<WordInfo> mWordList) {
        super(context, resource, mWordList);
        this.mContext = context;
        this.mWordList = mWordList;
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

        if (null != wordInfo) {
            holder.eng_word.setText(wordInfo.getEnglsih());
            holder.viet_word.setText(wordInfo.getVietnamese());
            // holder.iconView.setImageResource(mImageLoader.loadDrawableLocal(wordInfo.getEnglsih()));
            holder.iconView.setImageDrawable(ImageUtils.loadDrawableLocal(mContext, wordInfo.getEnglsih()));
            holder.remindView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_alarms));
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
