package com.biaoyuan.transfernet.ui.send.SendSealFgt;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.ui.MainAty;
import com.biaoyuan.transfernet.util.TabLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @title  :viewpager封包页面
 * @create :2017/5/9
 * @author :enmaoFu
 */
public class SendSealFgt extends BaseFgt{

    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private List<BaseFgt> mFragments;
    private List<String> mTabsString;

    public static ViewPager viewPager;
    @Override
    public int getLayoutId() {
        return R.layout.fgt_viewpager_send_seal_main;
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void initData() {
        viewPager=this.mViewPager;

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("待接单");
        mTabsString.add("已接单");

        mFragments.add(new SendSealIsWaitOrdersFgt());
        mFragments.add(new SendSealIsAlreadyOrdersFgt());

        mTab.post(new Runnable() {
            @Override
            public void run() {

                TabLayoutHelper.setIndicator(mTab,50,50);

            }
        });

        pageAdapter pageAdapter = new pageAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);



    }

    @Override
    public void requestData() {

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (MainAty.radioButtons.get(0).isChecked()){
            mFragments.get(mViewPager.getCurrentItem()).onUserVisible();
        }
    }



    class pageAdapter extends FragmentStatePagerAdapter {
        public pageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsString.get(position);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }


}
