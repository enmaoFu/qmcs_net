package com.biaoyuan.transfernet.ui.send.SendTakeFgt;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.and.yzy.frame.util.AppManger;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.ui.NavigationActivity;
import com.biaoyuan.transfernet.view.overlay.DrivingRouteOverlay;
import com.biaoyuan.transfernet.view.overlay.RideRouteOverlay;
import com.biaoyuan.transfernet.view.overlay.WalkRouteOverlay;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.and.yzy.frame.application.BaseApplication.mContext;

/**
 * Title  :显示路径规划
 * Create : 2017/6/16
 * Author ：chen
 */

public class SendMapAty extends BaseAty implements RouteSearch.OnRouteSearchListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.map)
    MapView mMap;

    Bundle savedInstanceState;
    private AMap aMap;


    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;


    private RouteSearch mSearch;

    //1开车 2 步行 3骑行
    private int type = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "地图导航");

        startLat = getIntent().getDoubleExtra("startLat", startLat);
        startLng = getIntent().getDoubleExtra("startLng", startLng);
        endLat = getIntent().getDoubleExtra("endLat", endLat);
        endLng = getIntent().getDoubleExtra("endLng", endLng);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMap != null) {
            mMap.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMap != null) {
            mMap.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMap != null) {
            mMap.onSaveInstanceState(outState);
        }
    }

    @Override
    public void requestData() {

        opCheckPermission(200);
    }

    // 权限
    public void opCheckPermission(int requestcode) {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(requestcode)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(SendMapAty.this, rationale).show();
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
                initMap();
            } else if (requestCode == 300) {
                Bundle bun = new Bundle();
                bun.putDouble("startLat", startLat);
                bun.putDouble("endLat", endLat);
                bun.putDouble("startLng", startLng);
                bun.putDouble("endLng", endLng);
                bun.putInt("type", type);
                if(!AppManger.getInstance().isAddActivity(NavigationActivity.class))
                startActivity(NavigationActivity.class, bun);
            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // initMap();
            showErrorToast("定位失败");

            dismissLoadingDialog();
        }
    };


    @OnClick({R.id.tv_drive, R.id.tv_walk, R.id.tv_riding,R.id.btn_commit})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()) {
            case R.id.tv_drive:
                type = 1;
                showLoadingDialog(null);
                opCheckPermission(200);
                break;
            case R.id.tv_walk:
                type = 2;
                showLoadingDialog(null);
                opCheckPermission(200);
                break;
            case R.id.tv_riding:
                type = 3;
                showLoadingDialog(null);
                opCheckPermission(200);
                break;
            case R.id.btn_commit:
                //判断位置权限
                opCheckPermission(300);
                break;
        }
    }

    private void initMap() {

        if (aMap == null) {

            mMap.onCreate(savedInstanceState);

            aMap = mMap.getMap();
            aMap.getUiSettings().setZoomControlsEnabled(false);// 放大缩小开关
            aMap.getUiSettings().setScaleControlsEnabled(false);// 标尺开关
            aMap.getUiSettings().setCompassEnabled(false);// 指南针开关


            aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));


            mSearch = new RouteSearch(this);
            mSearch.setRouteSearchListener(this);


        }

        LatLonPoint s = new LatLonPoint(startLat, startLng);
        LatLonPoint e = new LatLonPoint(endLat, endLng);


        LatLng latLng = new LatLng(startLat, startLng);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(s, e);

        showLoadingDialog(null);
        switch (type) {
            case 1:
                //开车
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
                mSearch.calculateDriveRouteAsyn(query);
                break;
            case 2:

                //步行
                RouteSearch.WalkRouteQuery walk = new RouteSearch.WalkRouteQuery(fromAndTo);
                mSearch.calculateWalkRouteAsyn(walk);


                break;
            case 3:
                //骑行
                RouteSearch.RideRouteQuery ride = new RouteSearch.RideRouteQuery(fromAndTo);
                mSearch.calculateRideRouteAsyn(ride);


                break;
        }


    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }


    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dismissLoadingDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();


                } else if (result != null && result.getPaths() == null) {
                    showErrorToast("未找到相关路线");
                }

            } else {
                showErrorToast("未找到相关路线");
            }
        } else {
            showErrorToast("未找到相关路线");
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

        dismissLoadingDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    final WalkPath walkPath = result.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            result.getStartPos(),
                            result.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();

                } else if (result != null && result.getPaths() == null) {
                    showErrorToast("未找到相关路线");
                }
            } else {
                showErrorToast("未找到相关路线");
            }
        } else {
            showErrorToast("未找到相关路线");
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {
        dismissLoadingDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    RideRouteResult mRideRouteResult = result;
                    final RidePath ridePath = mRideRouteResult.getPaths()
                            .get(0);
                    RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                            this, aMap, ridePath,
                            mRideRouteResult.getStartPos(),
                            mRideRouteResult.getTargetPos());
                    rideRouteOverlay.removeFromMap();
                    rideRouteOverlay.addToMap();
                    rideRouteOverlay.zoomToSpan();

                } else if (result != null && result.getPaths() == null) {
                    showErrorToast("未找到相关路线");
                }
            } else {
                showErrorToast("未找到相关路线");
            }
        } else {
            showErrorToast("未找到相关路线");
        }
    }
}
