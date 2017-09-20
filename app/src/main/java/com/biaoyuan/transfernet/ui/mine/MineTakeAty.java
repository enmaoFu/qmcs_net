package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.and.yzy.frame.util.DensityUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.base.BaseFgt;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Title  :我的代发快件
 * Create : 2017/4/25
 * Author ：chen
 */

public class MineTakeAty extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private List<BaseFgt> mFragments;
    private List<String> mTabsString;

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.tab_viewpager_layout;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "我的取件");

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("待取件");
        mTabsString.add("已取消");


        mFragments.add(new MineTakeFgt());
        mFragments.add(new MineTakeCancleFgt());


        //设置间隔
        /*LinearLayout linearLayout = (LinearLayout) mTab.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.tab_divider_vertical));
        linearLayout.setDividerPadding(DensityUtils.dp2px(this, 15));*/

        pageAdapter pageAdapter = new pageAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public void requestData() {

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
