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
 * @title  :我的客户评价页面
 * @create :2017/5/22
 * @author :enmaoFu
 */
public class MineCustomerEvaluateActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private List<BaseFgt> mFragments;
    private List<String> mTabsString;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_customer_evaluate;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar,"客户评价");

        mFragments = new ArrayList<>();
        mTabsString = new ArrayList<>();
        mTabsString.add("全部评价");
        mTabsString.add("好评");
        mTabsString.add("中评");
        mTabsString.add("差评");

      addFgt(new MineCustomerEvaluateListFgt(),0);
      addFgt(new MineCustomerEvaluateListFgt(),1);
      addFgt(new MineCustomerEvaluateListFgt(),2);
      addFgt(new MineCustomerEvaluateListFgt(),3);

        //设置间隔
        /*LinearLayout linearLayout = (LinearLayout) mTab.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.tab_divider_vertical));
        linearLayout.setDividerPadding(DensityUtils.dp2px(this, 15));*/

        MineCustomerEvaluateActivity.pageAdapter pageAdapter = new MineCustomerEvaluateActivity.pageAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
        mTab.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void requestData() {

    }


    private void addFgt(BaseFgt fgt,int type){

        Bundle bundle = new Bundle();

        bundle.putInt("type",type);
        fgt.setArguments(bundle);
        mFragments.add(fgt);

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
