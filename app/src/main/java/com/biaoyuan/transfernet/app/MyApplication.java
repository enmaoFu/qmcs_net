package com.biaoyuan.transfernet.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.and.yzy.frame.application.BaseApplication;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.util.JPushSetAliasUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Title  :
 * Create : 2017/4/24
 * Author ：chen
 */

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        JPushInterface.setDebugMode(false);
        if (UserManger.isLogin()) {
            JPushSetAliasUtil jPushSetAliasUtil = new JPushSetAliasUtil(this);
            jPushSetAliasUtil.setAlias();
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
