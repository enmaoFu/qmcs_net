package com.biaoyuan.transfernet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.biaoyuan.transfernet.base.BaseFgt;

import java.util.ArrayList;
import java.util.List;

/**
 * @title  :viewpager适配器
 * @create :2017/5/9
 * @author :enmaoFu
 */
public class SendViewPagerAdapter extends FragmentPagerAdapter {

	private List<BaseFgt> mFragments;

    public SendViewPagerAdapter(FragmentManager fm,List<BaseFgt> fragments) {
        super(fm);
        this.mFragments = fragments;
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
