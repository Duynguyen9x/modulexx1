package com.add.toeic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.add.toeic.R;
import com.add.toeic.fragment.NoteFragment;
import com.add.toeic.fragment.WordRemindFragment;
import com.add.toeic.fragment.WordVocabularyFragment;

/**
 * Created by DTA on 1/8/2017.
 */

public class MyAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    private static int item_cnt = 3 ;
    public Fragment[] fragments = new Fragment[item_cnt];

    public MyAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    /**
     * Return fragment with respect to Position .
     */
    @Override
    public Fragment getItem(int position) {
        Log.d("anhdt", "WordFragment, position = " + position );
        switch (position){
            case 0 : return WordVocabularyFragment.newInstance(null, null);
            case 1 : return WordRemindFragment.newInstance(null, null);
            case 2 : return NoteFragment.newInstance(null, null);
        }
        return null;
    }

    @Override
    public int getCount() {
        return item_cnt;
    }

    /**
     * This method returns the title of the tab according to the position.
     */
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0 :
                return mContext.getString(R.string.vocabulary);
            case 1 :
                return mContext.getString(R.string.remind);
            case 2 :
                return mContext.getString(R.string.notes);
        }
        return null;
    }
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    //This populates your Fragment reference array:
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        fragments[position]  = createdFragment;
        return createdFragment;
    }
}
