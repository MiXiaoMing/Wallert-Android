package com.qiumi.app.wallet.wallet.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qiumi.app.support.AutoBaseFragment;

import java.util.ArrayList;

/**
 * Created by ThinkPad on 2019/9/27.
 */

public class MessageAdapter extends FragmentPagerAdapter {
    private ArrayList<AutoBaseFragment> mFragments;

    public MessageAdapter(FragmentManager fm, ArrayList<AutoBaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
