package com.biaoyuan.transfernet.ui.Three;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.and.yzy.frame.view.other.CustomViewPager;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.ui.MainAty;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Title  :三方快递代发
 * Create : 2017/5/9
 * Author ：chen
 */

public class ThreeFgt extends BaseFgt {
    @Bind(R.id.rb_left)
    RadioButton mRbLeft;
    @Bind(R.id.rb_right)
    RadioButton mRbRight;
    @Bind(R.id.view_pager)
    CustomViewPager mViewPager;
    @Bind(R.id.rg_mian)
    RadioGroup mRgMian;

    public static List<RadioButton> radioButtons=new ArrayList<>();
    private List<BaseFgt> mFgts;

    @Override
    public int getLayoutId() {
        return R.layout.fgt_three;
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Subscribe
    @Override
    public void initData() {

        radioButtons.add(mRbLeft);
        radioButtons.add(mRbRight);

        mFgts = new ArrayList<>();

        mFgts.add(new ThreeNotSendFgt());
        mFgts.add(new ThreeSendFgt());

        MainFragmentAdapter fragmentAdapter = new MainFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(fragmentAdapter);

        mRgMian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_left:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_right:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }


    @Override
    public void requestData() {

    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        mFgts.get(mViewPager.getCurrentItem()).onUserInvisible();

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (MainAty.radioButtons.get(2).isChecked()){
            mFgts.get(mViewPager.getCurrentItem()).onUserVisible();
        }

    }

    class MainFragmentAdapter extends FragmentPagerAdapter {
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFgts.get(position);
        }

        @Override
        public int getCount() {
            return mFgts.size();
        }
    }


}
