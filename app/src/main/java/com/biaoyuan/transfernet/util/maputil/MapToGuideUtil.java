package com.biaoyuan.transfernet.util.maputil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.biaoyuan.transfernet.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Title  :
 * Create : 2017/7/27
 * Author ：chen
 */

public class MapToGuideUtil {
    private static PackageManager mPackageManager = MyApplication.getApplicationCotext().getPackageManager();
    private static List<String> mPackageNames = new ArrayList<>();
    private static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    private static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";


    private static void initPackageManager(){

        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(0);

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                mPackageNames.add(packageInfos.get(i).packageName);
            }
        }
    }

    public static boolean haveGaodeMap(){
        initPackageManager();
        return mPackageNames.contains(GAODE_PACKAGE_NAME);
    }

    public static boolean haveBaiduMap(){
        initPackageManager();
        return mPackageNames.contains(BAIDU_PACKAGE_NAME);
    }


    public static Intent openGaodeMapToGuide(double sLat,double sLng,double eLat,double eLng) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String url = "androidamap://route?sourceApplication=amap&slat="+sLat+"&slon="+sLng
                +"&dlat="+eLat+"&dlon="+eLng+"&dname="+"目的点"+"&dev=0&t=2";
        Uri uri = Uri.parse(url);
        //将功能Scheme以URI的方式传入data
        intent.setData(uri);
        //启动该页面即可
        return intent;
    }


    public static Intent openBaiduMapToGuide(double eLat,double eLng) {
        Intent intent = new Intent();
        double[] location = GPSUtil.gcj02_To_Bd09(eLat , eLng);
        Uri uri = Uri.parse("baidumap://map/navi?location="+location[0]+","+location[1]);
        //将功能Scheme以URI的方式传入data
        intent.setData(uri);
        //启动该页面即可
       return intent;
    }
}
