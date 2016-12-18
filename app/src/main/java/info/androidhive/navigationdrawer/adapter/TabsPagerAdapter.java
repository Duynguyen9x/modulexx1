package info.androidhive.navigationdrawer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.androidhive.navigationdrawer.fragment.TopicPracticeFragment;
import info.androidhive.navigationdrawer.fragment.TopicWordFragment;

/**
 * Created by 8470p on 12/18/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new TopicWordFragment();
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
}
