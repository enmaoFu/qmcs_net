package com.biaoyuan.transfernet.ui;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseMapActivity;
import com.biaoyuan.transfernet.util.maputil.MapToGuideUtil;
import com.orhanobut.logger.Logger;

/**
 * Title  : 导航基类
 * Create : 2016/12/8
 * Author ：chen
 */

public class NavigationActivity extends BaseMapActivity {

    private double startLatitude;//起点纬度
    private double startLongitude;//起点经度
    private double endLatitude;//终点纬度
    private double endLongitude;//终点经度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);

        ImageView imageView = (ImageView) findViewById(R.id.img_return);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_go = (TextView) findViewById(R.id.tv_go);
        if (MapToGuideUtil.haveGaodeMap()) {
            tv_go.setText("打开高德地图>>");
            tv_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(MapToGuideUtil.openGaodeMapToGuide(startLatitude, startLongitude, endLatitude, endLongitude));
                }
            });
        } else if (MapToGuideUtil.haveBaiduMap()) {
            tv_go.setText("打开百度地图>>");
            tv_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(MapToGuideUtil.openBaiduMapToGuide(endLatitude, endLongitude));
                }
            });
        } else {
            tv_go.setVisibility(View.GONE);
        }


        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        AppManger.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManger.getInstance().killActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openGPSSettings();
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (!checkGPSIsOpen()) {

            new MaterialDialog(this).setMDMessage("该功能需要打开GPS,是否前去设置?").setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                @Override
                public void dialogBtnOnClick() {
                    //跳转GPS设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);
                }
            }).show();

        }
    }

    @Override
    public void initWidgets() {

        startLatitude = getIntent().getDoubleExtra("startLat", 0);
        startLongitude = getIntent().getDoubleExtra("startLng", 0);
        endLatitude = getIntent().getDoubleExtra("endLat", 0);
        endLongitude = getIntent().getDoubleExtra("endLng", 0);
        mStartLatlng = new NaviLatLng(startLatitude, startLongitude);
        mEndLatlng = new NaviLatLng(endLatitude, endLongitude);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断导航类型
        Logger.v("type==" + getIntent().getIntExtra("type", 1));
        switch (getIntent().getIntExtra("type", 1)) {
            case 1:
                //开车
                mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
                break;
            case 2:
                //步行
                mAMapNavi.calculateWalkRoute(mStartLatlng, mEndLatlng);
                break;
            case 3:
                //骑行
                mAMapNavi.calculateRideRoute(mStartLatlng, mEndLatlng);
                break;
        }


    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void onPlayRing(int i) {

    }
}