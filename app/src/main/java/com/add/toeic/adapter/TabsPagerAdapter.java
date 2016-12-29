package com.add.toeic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.add.toeic.R;
import com.add.toeic.fragment.TopicPracticeFragment;
import com.add.toeic.fragment.TopicWordFragment;

/**
 * Created by 8470p on 12/18/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private int mLocation;
    private Context mContext;

    public TabsPagerAdapter(FragmentManager fm, Context context, int location) {
        super(fm);
        mContext = context;
        mLocation = location;
    }

    @Override
    public Fragment getItem(int index) {
        Log.i("duy.nq","getItem = " + index);
        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new TopicWordFragment(mLocation);
            case 1:
                // Games fragment activity
                return new TopicPracticeFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        Log.i("duy.nq","count="+2);
        // get item count - equal to number of tabs
        return 2;
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
                return mContext.getString(R.string.practice);
        }
        return null;
    }
}
