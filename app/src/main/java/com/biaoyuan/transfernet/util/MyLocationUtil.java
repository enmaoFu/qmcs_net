package com.biaoyuan.transfernet.util;

/**
 * 此工具来定位，获得经纬度
 *
 * @author HrcmChan
 */

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;


/**
 * @author Administrator
 */
public class MyLocationUtil {

    private MyLocationListener locationListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    public MyLocationUtil(Context context, MyLocationListener locationListener) {

        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(listener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setGpsFirst(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setGpsFirst(true);
        mLocationOption.setLocationCacheEnable(false);
        //        mLocationOption.setWifiActiveScan();
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        this.locationListener = locationListener;
        mlocationClient.startLocation();

    }

    AMapLocationListener listener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                Logger.i("定位colde===" + aMapLocation.getErrorCode() +"lat=="+aMapLocation.getLatitude()+"lon=="+"lat=="+aMapLocation.getLongitude());
                if (aMapLocation.getErrorCode() == 0) {
                    if (locationListener != null) {
                        locationListener.sussessLocation(aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation);
                    }
                } else {
                    locationListener.error();
                }
            } else {
                locationListener.error();
            }

        }
    };


    /**
     * 访问网络，开始点位
     */
    public void startLocation(){
        //启动定位
        mlocationClient.startLocation();
    }


    // 停止定位
    public void stopLocation() {
        //启动定位
        mlocationClient.stopLocation();
    }

    public interface MyLocationListener {


        void sussessLocation(double lat, double lon, AMapLocation location);

        void error();


    }


}
