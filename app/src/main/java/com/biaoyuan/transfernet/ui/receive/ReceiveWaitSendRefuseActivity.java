package com.biaoyuan.transfernet.ui.receive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.and.yzy.frame.adapter.AdapterCallback;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.FormBotomDefaultDialogBuilder;
import com.and.yzy.frame.view.linearlistview.LinearListView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.AddPicAdapter;
import com.biaoyuan.transfernet.base.BasePhotoAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.http.Image;
import com.biaoyuan.transfernet.http.Receive;
import com.biaoyuan.transfernet.ui.ShowBigImgaeAty;
import com.biaoyuan.transfernet.ui.Three.ThreeSendDetailsActivity;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.EasyTransition;
import com.biaoyuan.transfernet.util.EasyTransitionOptions;
import com.biaoyuan.transfernet.util.UpImageUtils;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.bigkoo.pickerview.OptionsPickerView;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 代送快件详情->拒绝接收
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class ReceiveWaitSendRefuseActivity extends BasePhotoAty implements AdapterCallback {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.select_refuse_content)
    TextView selectRefuseConent;
    @Bind(R.id.describe)
    EditText describe;
    @Bind(R.id.add_pic)
    ImageView mAddPic;
    @Bind(R.id.lv_pic)
    LinearListView mLvPic;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.tv_progress)
    TextView mTvProgress;
    @Bind(R.id.tv_tip)
    TextView mTvTip;

    private String orderIdStr;
    private OptionsPickerView mDialogBuilderSelectRefuse;
    private FormBotomDefaultDialogBuilder mDefaultDialogBuilder;
    private AddPicAdapter mPicAdapter;
    private List<PicInfo> mPicList;
    private UpImageUtils mUtils;
    private String selectRefuseConentStrNew;

    private String key;

    @Override
    public int getLayoutId() {
        return R.layout.activity_receive_wait_send_refuse;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        orderIdStr = bundle.getString("orderIdStr");
        key = bundle.getString("key");
        initToolbar(mToolbar, "拒绝签收");
        initSelectRefuseDialog();

        /**
         * 照片的处理
         */
        mPicList = new ArrayList<>();
        mPicAdapter = new AddPicAdapter(this, mPicList, R.layout.item_add_pic, this);
        mLvPic.setAdapter(mPicAdapter);

        mLvPic.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                PicInfo picInfo = mPicAdapter.getItem(position);

                Intent intent = new Intent(ReceiveWaitSendRefuseActivity.this, ShowBigImgaeAty.class);
                intent.putExtra("url", picInfo.getPath());

                // ready for transition options
                EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(
                                ReceiveWaitSendRefuseActivity.this,
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
        //删除oos的垃圾图片
        doHttp(RetrofitUtils.createApi(Image.class).findKey(UpImageUtils.TAG_ORDER + "/" + orderIdStr), 2);

    }

    @OnClick({R.id.Rl_commit, R.id.add_pic, R.id.select_refuse})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.Rl_commit:

                final String selectRefuseConentStr = selectRefuseConent.getText().toString().trim();
                final String describeStr = describe.getText().toString().trim();
                if (selectRefuseConentStr.equals("请选择拒收原因")) {
                    showErrorToast("请选择拒收原因");
                    return;
                }
                if (mPicAdapter.findAll().size() == 0) {
                    showErrorToast("请至少上传一张图片");
                    return;
                }
                if (describeStr.length() == 0) {
                    showErrorToast("请填写其他描述");
                    return;
                }

                /**
                 * 拒绝签收原因：1违禁物品、2重量体积与实际不符、3收件地址无法送达、4货物破损/丢失、5描述与实际不符、6其他原因
                 */
                /*if(selectRefuseConentStr.equals("违禁物品")){
                    selectRefuseConentStrNew = "1";
                }else if(selectRefuseConentStr.equals("重量体积与实际不符")){
                    selectRefuseConentStrNew = "2";
                }else if(selectRefuseConentStr.equals("收件地址无法送达")){
                    selectRefuseConentStrNew = "3";
                }else */
                if (selectRefuseConentStr.equals("货物破损/丢失")) {
                    selectRefuseConentStrNew = "6";
                }
                if (selectRefuseConentStr.equals("其他原因")) {
                    selectRefuseConentStrNew = "7";
                }
                if (mUtils == null) {
                    mUtils = new UpImageUtils(this, UpImageUtils.TAG_ORDER, orderIdStr, new UpImageUtils.UpImageListener() {
                        @Override
                        public void onUpSuccess() {
                            Logger.v(mUtils.getImagePath());
                            doHttp(RetrofitUtils.createApi(Receive.class).rejectionOrder(describeStr, selectRefuseConentStrNew, mUtils.getImagePath(), orderIdStr), 1);
                        }

                        @Override
                        public void onUpFailure() {
                            dismissLoadingDialog();
                        }

                        @Override
                        public void onUpLoading(int progress) {
                            if (mProgressBar != null) {
                                mProgressBar.setProgress(progress);
                                mTvProgress.setText("正在请求(" + progress + "%)");
                            }
                        }
                    });
                }
                showLoadingDialog(null);
                mUtils.upPhoto(mPicAdapter.findAll());

                break;
            case R.id.add_pic:
                showPicDialog();
                break;
            case R.id.select_refuse:
                mDialogBuilderSelectRefuse.show();
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:

                //记录提交状态必须
                mUtils.isCommitSuccess = true;

                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.COMMIT, true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(key.equals("zs")){
                            //关闭上一个界面
                            AppManger.getInstance().killActivity(ThreeSendDetailsActivity.class);
                            dialogHelper.dismiss();
                            finish();
                        }else if(key.equals("ds")){
                            //关闭上一个界面
                            AppManger.getInstance().killActivity(ReceiveWaitSendPackageActivity.class);
                            dialogHelper.dismiss();
                            finish();
                        }
                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(key.equals("zs")){
                            //关闭上一个界面
                            AppManger.getInstance().killActivity(ThreeSendDetailsActivity.class);
                            dialogHelper.dismiss();
                            finish();
                        }else if(key.equals("ds")){
                            //关闭上一个界面
                            AppManger.getInstance().killActivity(ReceiveWaitSendPackageActivity.class);
                            dialogHelper.dismiss();
                            finish();
                        }
                    }
                }).show();

                break;
            case 2:
                //删除以前的垃圾图片
                List<String> objKey = AppJsonUtil.getArrayList(result, "listKey", String.class);
                new UpImageUtils().deleteOtherFile(objKey, UpImageUtils.TAG_ORDER);

                break;

        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        switch (what) {
            case 1:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.COMMIT, false).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                    }
                }).show();
                if (mUtils != null) {
                    mUtils.deleteFile();
                }
                if (mProgressBar != null) {
                    mProgressBar.setProgress(100);
                    mTvProgress.setText("提交申请");
                }
                break;

        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (mUtils != null) {
            mUtils.deleteFile();
            if (mProgressBar != null) {
                mProgressBar.setProgress(100);
                mTvProgress.setText("提交申请");
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUtils != null) {
            mUtils.isOnDestoryDoing();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mUtils != null) {
            mUtils.isOnBackPressedDoing();
            if (mProgressBar != null) {
                mProgressBar.setProgress(100);
                mTvProgress.setText("提交申请");
            }
        }
    }

    /**
     * 选择拒收原因
     */
    public void initSelectRefuseDialog() {

        /**
         * 拒绝签收原因：1违禁物品、2重量体积与实际不符、3收件地址无法送达、4货物破损/丢失、5描述与实际不符、6其他原因
         */
        final ArrayList<String> list = new ArrayList<>();
        list.add("货物破损/丢失");
        list.add("其他原因");

        mDialogBuilderSelectRefuse = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = list.get(options1);
                selectRefuseConent.setText(tx);
            }
        })
                .setTitleText("选择拒收原因")
                .setContentTextSize(14)
                .setOutSideCancelable(true)// default is true
                .build();

        mDialogBuilderSelectRefuse.setPicker(list);
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
                PicInfo picInfo = new PicInfo();
                //设置压缩路径到实体类
                picInfo.setPath(result.getImage().getCompressPath());
                //把一个图片实体类添加到适配器
                mPicAdapter.addInfo(picInfo);
                //        mPicAdapter.addInfo(picInfo);
                //当图片等于4的时候设置隐藏逻辑
                if (mPicAdapter.findAll().size() == 4) {
                    mTvTip.setVisibility(View.GONE);
                    mAddPic.setVisibility(View.GONE);
                }
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

    @Override
    public void adapterInfotoActiity(Object data, int what) {
        //当图片小于4的时候设置隐藏逻辑
        if (mPicAdapter.findAll().size() < 4) {
            mTvTip.setVisibility(View.VISIBLE);
            mAddPic.setVisibility(View.VISIBLE);
        }
    }
}
