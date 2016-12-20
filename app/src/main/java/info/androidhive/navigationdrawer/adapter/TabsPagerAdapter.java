package info.androidhive.navigationdrawer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.fragment.TopicPracticeFragment;
import info.androidhive.navigationdrawer.fragment.TopicWordFragment;

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
