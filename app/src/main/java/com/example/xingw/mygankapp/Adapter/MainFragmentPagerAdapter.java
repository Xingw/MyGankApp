package com.example.xingw.mygankapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2015/11/30.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter{
    private static List<android.support.v4.app.Fragment> mFragments = new ArrayList<>();
    private static List<String> mFragmentTitles = new ArrayList<>();

    public MainFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    public void MainFragmentPagerAdapter(android.support.v4.app.Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void addFragment(Fragment fragment,String title)
    {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
