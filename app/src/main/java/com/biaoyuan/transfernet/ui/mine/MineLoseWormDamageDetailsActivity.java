package com.biaoyuan.transfernet.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.and.yzy.frame.view.linearlistview.LinearListView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.MineLoseWormDamageDetailsAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.ui.ShowBigImgaeAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.EasyTransition;
import com.biaoyuan.transfernet.util.EasyTransitionOptions;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author :enmaoFu
 * @title :破损快件
 * @create :2017/6/15
 */
public class MineLoseWormDamageDetailsActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.word_message)
    TextView wordMessage;
    @Bind(R.id.take_spot_name)
    TextView takeSpotName;
    @Bind(R.id.take_spot_phone)
    TextView takeSpotPhone;
    @Bind(R.id.angle_name)
    TextView angleName;
    @Bind(R.id.angle_phone)
    TextView angelePhone;
    @Bind(R.id.angle_date)
    TextView angleDate;
    @Bind(R.id.images)
    LinearListView linearListView;
    @Bind(R.id.angle)
    LinearLayout angle;

    private MineLoseWormDamageDetailsAdapter mineLoseWormDamageDetailsAdapter;
    private List<PicInfo> mineLoseWormDamageDetailsInfos;

    private String excepId;
    private String orderCode;
    private String ysDate;
    private int excepType;
    private int type = 0;
    private String orderNo;
    private int orderType;

    private String getTakeSpotPhone;
    private String getAnglePhone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_lose_worm_damage_details;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        excepId = bundle.getString("excepId");
        excepType = bundle.getInt("excepType");
        orderCode = bundle.getString("orderCode");
        ysDate = bundle.getString("ysDate");
        orderNo = bundle.getString("orderNo");
        orderType = bundle.getInt("orderType");

        initToolbar(mToolbar, "破损快件");
        code.setText("快件码：" + orderCode);
        date.setText(ysDate);

        mineLoseWormDamageDetailsAdapter = new MineLoseWormDamageDetailsAdapter(this, new ArrayList<PicInfo>(),
                R.layout.item_mine_lose_worm_damage_details);
        linearListView.setAdapter(mineLoseWormDamageDetailsAdapter);

        linearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {

                PicInfo picInfo = mineLoseWormDamageDetailsAdapter.getItem(position);

                Intent intent = new Intent(MineLoseWormDamageDetailsActivity.this, ShowBigImgaeAty.class);
                intent.putExtra("url", picInfo.getPath());

                // ready for transition options
                EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(
                                MineLoseWormDamageDetailsActivity.this,
                                view.findViewById(R.id.img));


                // start transition
                EasyTransition.startActivity(intent, options);


            }
        });

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        if (orderType == 3 || orderType == 5) {
            showLoadingContentDialog();
            doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpressBeDamagedDetail(excepId, String.valueOf(0)), 1);
        } else {
            showLoadingContentDialog();
            doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpressBeDamagedDetail(excepId, String.valueOf(orderType)), 1);
        }
    }

    @OnClick({R.id.query_details, R.id.take_spot_phone, R.id.angle_phone})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.query_details:
                Bundle bundle = new Bundle();
                bundle.putString("loseAndDamage", "damage");
                bundle.putString("excepId", excepId);
                bundle.putInt("excepType", excepType);
                bundle.putString("orderCode", orderCode);
                bundle.putString("ysDate", ysDate);
                bundle.putString("orderNo", orderNo);
                startActivity(MineLoseWormDetailsActivity.class, bundle);
                break;
            case R.id.take_spot_phone:
                type = 0;
                opCheckPermission();
                break;
            case R.id.angle_phone:
                type = 1;
                opCheckPermission();
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:

                //破损文字描述
                String getWordMessage = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "excep_reason")).toString();
                //取件网点名字
                String getTakeSpotName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "basic_name")).toString();
                //取件网点电话
                getTakeSpotPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "basic_telphone")).toString();

                //图片路径
                String getImages = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "excep_pic_url")).toString();
                if (getWordMessage.equals("") || getWordMessage.equals("null") || getWordMessage == null) {
                    wordMessage.setText("暂无描述");
                } else {
                    wordMessage.setText(getWordMessage);
                }
                takeSpotName.setText(getTakeSpotName);
                takeSpotPhone.setText(getTakeSpotPhone);
                if (getImages.equals("null")) {
                    return;
                } else {
                    String[] urlArray = getImages.split(",");
                    mineLoseWormDamageDetailsInfos = new ArrayList<>();
                    for (int i = 0; i < urlArray.length; i++) {
                        PicInfo picInfo = new PicInfo();
                        picInfo.setPath(urlArray[i]);
                        mineLoseWormDamageDetailsInfos.add(picInfo);
                    }
                    mineLoseWormDamageDetailsAdapter.addAll(mineLoseWormDamageDetailsInfos);
                }
                if (orderType == 3 || orderType == 5) {
                    angle.setVisibility(View.GONE);
                } else {
                    //传送天使名字
                    String getAngleName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "user_login_name")).toString();
                    //传送天使电话
                    getAnglePhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "user_telphone")).toString();
                    //传送天使接件时间
                    String getAngleDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "carry_pickup_time")
                            , "yyyy.MM.dd HH:mm")).toString();

                    angleName.setText(getAngleName);
                    angelePhone.setText(getAnglePhone);
                    angleDate.setText(getAngleDate + "接件");
                }


                /*String urls = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1498115334&di=9344f727596bfbf427dd4fa5e020243f&src=http://img3.duitang.com/uploads/item/201506/26/20150626122729_LzZP8.jpeg," +
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498125394390&di=e9f5fbe1df89ce4b33fee50bdf1d3ddb&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201509%2F21%2F20150921114703_wNVXT.jpeg," +
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498125394387&di=d17aeb9e97e482fff608c9153020a837&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201409%2F16%2F20140916123352_xBJCw.thumb.700_0.jpeg," +
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498125394385&di=8bd6db3faef184c51c4c5cbcab10c5b0&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201306%2F09%2F20130609201212_hZGzA.thumb.600_0.jpeg";
                String[] urlArray = urls.split(",");
                mineLoseWormDamageDetailsInfos = new ArrayList<>();
                MineLoseWormDamageDetailsInfo mineLoseWormDamageDetailsInfo;
                for (int i = 0; i < urlArray.length; i++) {
                    mineLoseWormDamageDetailsInfo = new MineLoseWormDamageDetailsInfo(urlArray[i]);
                    mineLoseWormDamageDetailsInfos.add(mineLoseWormDamageDetailsInfo);
                }
                mineLoseWormDamageDetailsAdapter.addAll(mineLoseWormDamageDetailsInfos);*/


                break;
        }
    }

    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.CALL_PHONE
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(MineLoseWormDamageDetailsActivity.this, rationale).show();
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


                if (type == 0) {
                    new MaterialDialog(MineLoseWormDamageDetailsActivity.this)
                            .setMDMessage("是否立即拨打取件网点电话?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getTakeSpotPhone));
                                    startActivity(intent);
                                }
                            }).show();
                } else {
                    new MaterialDialog(MineLoseWormDamageDetailsActivity.this)
                            .setMDMessage("是否立即拨打传送天使电话?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getAnglePhone));
                                    startActivity(intent);
                                }
                            }).show();
                }

            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // initMap();
            showErrorToast("未授权");

        }
    };

}
