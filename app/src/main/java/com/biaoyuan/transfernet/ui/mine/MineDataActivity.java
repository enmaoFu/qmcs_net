package com.biaoyuan.transfernet.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.Effectstype;
import com.and.yzy.frame.view.dialog.FormBotomDefaultDialogBuilder;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BasePhotoAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MessageEvent;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.ui.login.LoginAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;
import com.biaoyuan.transfernet.util.UpImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  :账号管理
 * Create : 2017/4/26
 * Author ：chen
 */

public class MineDataActivity extends BasePhotoAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.mine_img)
    SimpleDraweeView mMineImg;
    @Bind(R.id.phone)
    TextView phone;

    private String getMinePhone;
    private String getMineImge;
    private int version;
    private FormBotomDefaultDialogBuilder mDefaultDialogBuilder;
    private UpImageUtils mUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_data;
    }

    @Override
    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        getMinePhone = getIntent().getExtras().getString("getMinePhone");
        getMineImge = getIntent().getExtras().getString("getMineImge");
        initToolbar(mToolbar, "账号管理");
        phone.setText(MyNumberFormat.toPwdPhone(Long.parseLong(getMinePhone)));
        Uri uri = Uri.parse(getMineImge);
        mMineImg.setImageURI(uri);
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {
    }

    @OnClick({R.id.tv_pwd, R.id.ll_change_phone, R.id.tv_commit, R.id.head_img})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pwd:
                startActivity(MineUpdatePwdFristAty.class, null);
                break;
            case R.id.ll_change_phone:
                startActivity(UpdataPhoneOneActivity.class, null);
                break;
            case R.id.tv_commit:
                MaterialDialog dialog = new MaterialDialog(this);

                dialog.setMDMessage("是否立即退出当前账号?").setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                    @Override
                    public void dialogBtnOnClick() {


                        //退出登录
                        UserManger.setIsOpen("0");
                        AppManger.getInstance().killAllActivity();
                        setHasAnimiation(false);
                        UserManger.setIsLogin(false);
                        UserManger.setToken("");
                        UserManger.setUUid("");
                        Intent intent = new Intent(MineDataActivity.this, LoginAty.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    }
                }).setMDEffect(Effectstype.Shake).show();

                break;

            case R.id.head_img:
                showPicDialog();
                break;

        }
    }


    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                //数据版本号
                String getVersion = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffVersion")).toString();
                version = Integer.parseInt(getVersion);
                break;
            case 2:
                //记录提交状态必须
                mUtils.isCommitSuccess = true;
                String imageUrl = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffPortrait")).toString();
                UserManger.setImageURL(imageUrl);
                showToast("头像上传成功");
                //删除以前的图
                String rawStaffPortraitUrl = getIntent().getStringExtra("rawStaffPortraitUrl");
                if (!TextUtils.isEmpty(rawStaffPortraitUrl)) {
                    mUtils.deleteFile(rawStaffPortraitUrl);
                }
                MineFgt.isChangeImage = true;

                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        switch (what) {
            case 1:
                if (mUtils != null) {
                    mUtils.deleteFile();
                }
                showToast("头像上传失败");
                break;

        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (mUtils != null) {
            mUtils.deleteFile();
        }
        showToast("头像上传失败");
    }

    /**
     * 事件总线的回调函数
     * 注意：在接受页面注册了事件，必须写回调函数并且加注解
     *
     * @param event
     */
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        phone.setText(MyNumberFormat.toPwdPhone(Long.parseLong(event.getMesage())));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在结束界面中解除注册
        EventBus.getDefault().unregister(this);
        if (mUtils != null) {
            mUtils.isOnDestoryDoing();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mUtils != null) {
            mUtils.isOnBackPressedDoing();
        }
    }

    /**
     * 照片选择弹出框
     */
    private void showPicDialog() {

        if (mDefaultDialogBuilder == null) {

            mDefaultDialogBuilder = new FormBotomDefaultDialogBuilder(this);
            mDefaultDialogBuilder.setFBFirstBtnText("拍照");
            mDefaultDialogBuilder.setFBLastBtnText("相册");
            //压缩
            LubanOptions option = new LubanOptions.Builder()
                    .setMaxSize(UserManger.MAXSIZE)//设置压缩的最大大小，上传照片的最大值
                    .create();
            //进行压缩配置
            final CompressConfig config = CompressConfig.ofLuban(option);

            //进行剪裁配置
            final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).create();

            //点击进行拍照
            mDefaultDialogBuilder.setFBFirstBtnClick(new FormBotomDefaultDialogBuilder.DialogBtnCallBack() {
                @Override
                public void dialogBtnOnClick() {
                    //启用图片压缩,设置上面的压缩配置，不显示进度对话框
                    getTakePhoto().onEnableCompress(config, false);
                    //启用裁剪，设置裁剪路径，设置上面的裁剪配置
                    getTakePhoto().onPickFromCaptureWithCrop(getImageUri(), cropOptions);

                }
            });
            //点击进行相册选择
            mDefaultDialogBuilder.setFBLastBtnClick(new FormBotomDefaultDialogBuilder.DialogBtnCallBack() {
                @Override
                public void dialogBtnOnClick() {

                    getTakePhoto().onEnableCompress(config, false);
                    getTakePhoto().onPickFromGalleryWithCrop(getImageUri(), cropOptions);
                }
            });

        }

        mDefaultDialogBuilder.show();

    }

    /**
     * 成功
     *
     * @param result
     */
    @Override
    public void takeSuccess(final TResult result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logger.v("path==" + result.getImage().getCompressPath());
                mMineImg.setImageURI(Uri.parse("file://" + result.getImage().getCompressPath()));
                PicInfo picInfo = new PicInfo();
                //设置压缩路径到实体类
                picInfo.setPath(result.getImage().getCompressPath());
                List<PicInfo> picInfoList = new ArrayList<PicInfo>();
                picInfoList.add(picInfo);
                showLoadingDialog(null);
                if (mUtils == null) {
                    mUtils = new UpImageUtils(MineDataActivity.this, UpImageUtils.TAG_QMCS_NW_HEADER, UserManger.getStaffIdentityNo(), new UpImageUtils.UpImageListener() {
                        @Override
                        public void onUpSuccess() {
                            Logger.v(mUtils.getImagePath());
                            doHttp(RetrofitUtils.createApi(Mine.class).setStaffInformationNew("", "", mUtils.getImagePath(), "", version, ""), 2);
                        }

                        @Override
                        public void onUpFailure() {
                            dismissLoadingDialog();
                        }

                        @Override
                        public void onUpLoading(int progress) {
                        }
                    });
                }

                mUtils.upPhoto(picInfoList);
            }
        });
    }

    /**
     * 失败
     *
     * @param result
     * @param msg
     */
    @Override
    public void takeFail(TResult result, String msg) {

    }

    /**
     * 取消
     */
    @Override
    public void takeCancel() {

    }

}
