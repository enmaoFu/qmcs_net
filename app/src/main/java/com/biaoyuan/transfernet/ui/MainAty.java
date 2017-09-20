package com.biaoyuan.transfernet.ui;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocation;
import com.and.yzy.frame.base.BaseFrameAty;
import com.and.yzy.frame.base.BaseFrameLazyFgt;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.view.other.BottomMenuView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.event.PlayEvent;
import com.biaoyuan.transfernet.ui.Three.ThreeFgt;
import com.biaoyuan.transfernet.ui.login.LoginAty;
import com.biaoyuan.transfernet.ui.mine.MineFgt;
import com.biaoyuan.transfernet.ui.receive.ReceiveFgt;
import com.biaoyuan.transfernet.ui.send.SendFgt;
import com.biaoyuan.transfernet.util.AllLocationUtil;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.beta.Beta;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 主界面
 */
public class MainAty extends BaseFrameAty {

    @Bind(R.id.menu)
    BottomMenuView mMenu;

    public static List<RadioButton> radioButtons;

    AllLocationUtil mAllLocationUtil;

    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        MineFgt.isChangeImage=true;
    }

    @Subscribe
    public void onMessageEvent(PlayEvent event) {

        //当开启的时候才进行播报，0为开启
        if (UserManger.getIsPOpen().equals("0")){
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.sound);
            }

            Logger.v("onEventMainThread=====" + mMediaPlayer.isPlaying());
            //防止重复播放
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }

    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);


        //开启定位
        opCheckPermission();

        radioButtons = mMenu.getRadioButtons();

        AppManger.getInstance().killActivity(LoginAty.class);

        List<Drawable> drawables = new ArrayList<>();
        drawables.add(getResources().getDrawable(R.drawable.selector_tab_send));
        drawables.add(getResources().getDrawable(R.drawable.selector_tab_receive));
        drawables.add(getResources().getDrawable(R.drawable.selector_tab_three));
        drawables.add(getResources().getDrawable(R.drawable.selector_tab_mine));


        List<String> menus = new ArrayList<>();
        menus.add("发件网点");
        menus.add("送件网点");
        menus.add("直属服务");
        menus.add("我的");


        List<BaseFrameLazyFgt> fgts = new ArrayList<>();
        fgts.add(new SendFgt());
        fgts.add(new ReceiveFgt());
        fgts.add(new ThreeFgt());
        fgts.add(new MineFgt());

        mMenu.init(drawables, R.color.selector_main_rb_text, fgts, menus, getSupportFragmentManager());

        mMenu.getViewPager().setOffscreenPageLimit(4);
        mMenu.setListener(new BottomMenuView.CheckChangeListener() {
            @Override
            public void onCheckChanged(ViewPager pager, RadioGroup group, int checkedPosition) {
                switch (checkedPosition) {
                    case 0:
                        pager.setCurrentItem(0);
                        break;
                    case 1:
                        pager.setCurrentItem(1);
                        break;
                    case 2:
                        pager.setCurrentItem(2);
                        break;
                    case 3:
                        pager.setCurrentItem(3);
                        break;
                }
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Beta.checkUpgrade(false, false);
            }
        }, 3000);
    }

    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(MainAty.this, rationale).show();
                               }
                           }
                )
                .send();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 200) {

                if (mAllLocationUtil == null) {

                    mAllLocationUtil = new AllLocationUtil(MainAty.this, new AllLocationUtil.MyLocationListener() {
                        @Override
                        public void sussessLocation(double lat, double lon, AMapLocation location) {
                            UserManger.setLat(lat + "");
                            UserManger.setLng(lon + "");
                        }

                        @Override
                        public void error() {

                        }
                    });

                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {


        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void btnClick(View view) {

    }


    private long oldTime;

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        long newtime = System.currentTimeMillis();
        if (newtime - oldTime > 3000) {
            oldTime = newtime;
            showToast("连按两次返回桌面");
        } else {
            setHasAnimiation(false);
            AppManger.getInstance().AppExit(this);
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.aty_in, R.anim.aty_out);
    }


}
