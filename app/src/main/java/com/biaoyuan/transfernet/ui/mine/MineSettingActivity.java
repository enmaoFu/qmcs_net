package com.biaoyuan.transfernet.ui.mine;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.and.yzy.frame.config.SavePath;
import com.and.yzy.frame.util.AppUtils;
import com.and.yzy.frame.util.FileUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.util.MyNumberFormat;
import com.suke.widget.SwitchButton;
import com.tencent.bugly.beta.Beta;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author :enmaoFu
 * @title :设置页面
 * @create :2017/5/9
 */
public class MineSettingActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.mine_swc)
    SwitchButton mMineSwc;


    @Bind(R.id.tv_size)
    TextView mTvSize;
    @Bind(R.id.rl_clear)
    RelativeLayout mRlClear;
    @Bind(R.id.tv_version)
    TextView mTvVersion;


    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_setting;
    }

    @Override
    public void initData() {

        initToolbar(mToolbar, "设置");

        mTvVersion.setText("v" + AppUtils.getVersionName(this));


        mMineSwc.isChecked();
        mMineSwc.toggle();     //switch state
        mMineSwc.toggle(true);//switch without animation
        mMineSwc.setShadowEffect(false);//disable shadow effect
        mMineSwc.setEnableEffect(true);//disable the switch animation
        if (UserManger.getIsPOpen().equals("0")) {
            mMineSwc.setChecked(true);
        } else {
            mMineSwc.setChecked(false);
        }
        mMineSwc.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    //开启语音播报
                    UserManger.setIsOpen("0");
                } else {
                    //关闭语音播报
                    UserManger.setIsOpen("1");
                }
            }
        });


        mTvSize.setText(MyNumberFormat.m2(FileUtils.getFileOrFilesSize(SavePath.SAVE_PATH, 3)) + "M");
        mRlClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.delFolder(SavePath.SAVE_PATH);
                mTvSize.setText("0.00M");
            }
        });

    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.dot_setting, R.id.about_we,R.id.rl_update})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.dot_setting:
                startActivity(MineDotSettingActivity.class, null);
                break;
            case R.id.about_we:
                startActivity(MineAboutWeActivity.class, null);
                break;
            case R.id.rl_update:
                Beta.checkUpgrade(true, false);
                break;
        }
    }
}
