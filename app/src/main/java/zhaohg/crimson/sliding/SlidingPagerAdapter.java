package zhaohg.crimson.sliding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SlidingPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<SlidingFragment> fragments;

    public SlidingPagerAdapter(FragmentManager manager, ArrayList<SlidingFragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int pos) {
        return fragments.get(pos).getTitle();
    }

}
