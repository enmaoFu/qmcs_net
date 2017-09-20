package com.biaoyuan.transfernet.ui.send.SendSealFgt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.and.yzy.frame.adapter.AdapterCallback;
import com.and.yzy.frame.adapter.ViewHolder;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.Effectstype;
import com.and.yzy.frame.view.dialog.FormBotomDefaultDialogBuilder;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.and.yzy.frame.view.linearlistview.LinearListView;
import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.AddPicAdapter;
import com.biaoyuan.transfernet.adapter.SendSealPackingScanningAdapter;
import com.biaoyuan.transfernet.base.BasePhotoAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.domain.SendSealPackingScanningInfo;
import com.biaoyuan.transfernet.http.Image;
import com.biaoyuan.transfernet.http.Seal;
import com.biaoyuan.transfernet.ui.AllScanAty;
import com.biaoyuan.transfernet.ui.InputCodeAty;
import com.biaoyuan.transfernet.ui.ShowBigImgaeAty;
import com.biaoyuan.transfernet.ui.send.SendSendFgt.SendSendArriveAddressActivity;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.EasyTransition;
import com.biaoyuan.transfernet.util.EasyTransitionOptions;
import com.biaoyuan.transfernet.util.UpImageUtils;
import com.biaoyuan.transfernet.view.MyAutoCompleteTextView;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author :enmaoFu
 * @title :封包-》已接单-》封装扫描页面
 * @create :2017/5/17
 */
public class SendSealPackingScanningActivity extends BasePhotoAty implements AdapterCallback {

    @Bind(R.id.scrolistview)
    ListViewForScrollView mListview;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.add_pic)
    ImageView mAddPic;
    @Bind(R.id.lv_pic)
    LinearListView mLvPic;
    @Bind(R.id.tv_tip)
    TextView mTvTip;
    @Bind(R.id.tv_number)
    TextView mTvNumber;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.et_big_code)
    MyAutoCompleteTextView mEtBigCode;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.tv_progress)
    TextView mTvProgress;

    private List<PicInfo> mPicList;

    private AddPicAdapter mPicAdapter;

    private FormBotomDefaultDialogBuilder mDefaultDialogBuilder;


    //适配器
    private SendSealPackingScanningAdapter mPackingScanningAdapter;


    private String packageId;
    private String basicCode;
    private String basicName;


    private UpImageUtils mUtils;
    private String mBigCode;
    private String mContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_seal_packing_scanning;
    }

    @Override
    public void initData() {


        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"QMWD"});
        mEtBigCode.setAdapter(arrayAdapter);
        mEtBigCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEtBigCode, InputMethodManager.SHOW_FORCED);
            }
        });


        Bundle bundle = getIntent().getExtras();
        packageId = bundle.getString("packageId");
        basicCode = bundle.getString("basicCode");
        basicName = bundle.getString("basicName");

        initToolbar(mToolbar, "封包扫描");

        mPackingScanningAdapter = new SendSealPackingScanningAdapter(this, new ArrayList<SendSealPackingScanningInfo>(), R.layout.item_send_seal_packing_scanning);

        mPackingScanningAdapter.setOntalkItemOnclick(new SendSealPackingScanningAdapter.talkItemOnclick() {
            @Override
            public void talkItemOnclickListener(final int postion, ViewHolder vh, int swtich) {
                switch (swtich) {
                    case 0:

                        //判断至少得保留一个

                        int delete = 0;
                        for (SendSealPackingScanningInfo scanningInfo : mPackingScanningAdapter.findAll()) {
                            if (scanningInfo.isDelete()) {
                                delete++;
                            }
                        }


                        if (mPackingScanningAdapter.findAll().size() - delete == 1) {
                            showErrorToast("封包至少需要一个快件");
                            return;
                        }


                        MaterialDialog dialog = new MaterialDialog(SendSealPackingScanningActivity.this);
                        dialog.setMDMessage("是否删除该快件？").setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                            @Override
                            public void dialogBtnOnClick() {

                                SendSealPackingScanningInfo scanningInfo = mPackingScanningAdapter.findAll().get(postion);
                                scanningInfo.setDelete(true);
                                mPackingScanningAdapter.notifyDataSetChanged();
                                setNumber();

                            }
                        }).setMDEffect(Effectstype.Shake).show();

                        break;
                }
            }
        });

        mListview.setAdapter(mPackingScanningAdapter);

        mPicList = new ArrayList<>();
        mPicAdapter = new AddPicAdapter(this, mPicList, R.layout.item_add_pic, this);

        mLvPic.setAdapter(mPicAdapter);

        mLvPic.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                PicInfo picInfo = mPicAdapter.getItem(position);

                Intent intent = new Intent(SendSealPackingScanningActivity.this, ShowBigImgaeAty.class);
                intent.putExtra("url", picInfo.getPath());

                // ready for transition options
                EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(
                                SendSealPackingScanningActivity.this,
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
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Seal.class).packetScanning(packageId), 1);

        //删除oos的垃圾图片
        doHttp(RetrofitUtils.createApi(Image.class).findKey(UpImageUtils.TAG_PACKAGE + "/" + packageId), 4);
    }

    @OnClick({R.id.tv_addMore, R.id.add_pic, R.id.Rl_commit, R.id.tv_scan_small, R.id.img_big_scan})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addMore:
                Bundle bundle = new Bundle();
                bundle.putString("key", "SendSealPackingScanningActivity");
                bundle.putString("basicCode", basicCode);
                bundle.putString("basicName", basicName);
                startActivityForResult(SendSendArriveAddressActivity.class, bundle, 1);
                break;
            case R.id.Rl_commit:
                if (mPackingScanningAdapter.findAll().size() == 0) {
                    showErrorToast("封包至少需要一个快件");
                    return;
                }


                if (getNumber() == 0) {

                    mBigCode = mEtBigCode.getText().toString().trim();
                    mContent = mEtContent.getText().toString().trim();
                    if (TextUtils.isEmpty(mContent)) {
                        mContent = null;
                    }

                    if (TextUtils.isEmpty(mBigCode)) {
                        showErrorToast("请填写打包码");
                        return;
                    }
                    if (!mBigCode.contains("QMWD") || mBigCode.length() < 16) {
                        showErrorToast("请填写正确的打包码");
                        return;
                    }

                    if (mPicAdapter.findAll().size() == 0) {
                        showErrorToast("请至少上传一张图片");
                        return;
                    }

                    //先检验打包码是否正确在上传图片
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Seal.class).validatePackageCode(mBigCode), 2);


                } else {

                    showErrorToast("您还有" + getNumber() + "个快件未确认");

                }


                break;
            case R.id.add_pic:

                showPicDialog();

                break;
            case R.id.tv_scan_small:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", InputCodeAty.TYPE_PACKAGE_SMALL);

                startActivityForResult(AllScanAty.class, bundle1, 2);


                break;
            case R.id.img_big_scan:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("type", InputCodeAty.TYPE_PACKAGE_BIG);
                startActivityForResult(AllScanAty.class, bundle2, 3);


                break;
        }
    }


    /**
     * 得到字符串里面的数字
     *
     * @param a
     * @return
     */
    private String getNumber(String a) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);

        return m.replaceAll("").trim();
    }

    private void showPicDialog() {

        if (mDefaultDialogBuilder == null) {

            mDefaultDialogBuilder = new FormBotomDefaultDialogBuilder(this);
            mDefaultDialogBuilder.setFBFirstBtnText("拍照");
            mDefaultDialogBuilder.setFBLastBtnText("相册");
            LubanOptions option = new LubanOptions.Builder()
                    .setMaxSize(UserManger.MAXSIZE)
                    .create();
            final CompressConfig config = CompressConfig.ofLuban(option);

            final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).create();
            mDefaultDialogBuilder.setFBFirstBtnClick(new FormBotomDefaultDialogBuilder.DialogBtnCallBack() {
                @Override
                public void dialogBtnOnClick() {
                    getTakePhoto().onEnableCompress(config, false);
                    getTakePhoto().onPickFromCaptureWithCrop(getImageUri(), cropOptions);

                }
            });
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


    @Override
    public void takeSuccess(final TResult result) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logger.v("path==" + result.getImage().getCompressPath());
                PicInfo picInfo = new PicInfo();
                picInfo.setPath(result.getImage().getCompressPath());
                mPicAdapter.addInfo(picInfo);
                //        mPicAdapter.addInfo(picInfo);
                if (mPicAdapter.findAll().size() == 4) {
                    mTvTip.setVisibility(View.GONE);
                    mAddPic.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void adapterInfotoActiity(Object data, int what) {
        if (mPicAdapter.findAll().size() < 4) {
            mTvTip.setVisibility(View.VISIBLE);
            mAddPic.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        switch (what) {
            case 3:
                if (mUtils != null) {
                    mUtils.deleteFile();
                }
                if (mProgressBar != null) {
                    mProgressBar.setProgress(100);
                    mTvProgress.setText("封包");
                }
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.CLOSE_PACKAGE, false).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭上一个界面
                        AppManger.getInstance().killActivity(SendSealReceivingOrdersActivity.class);
                        dialogHelper.dismiss();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭上一个界面
                        AppManger.getInstance().killActivity(SendSealReceivingOrdersActivity.class);
                        dialogHelper.dismiss();
                    }
                }).show();
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
                mTvProgress.setText("封包");
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
                mTvProgress.setText("封包");
            }
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {

        switch (what) {
            case 1:
                super.onSuccess(result, call, response, what);
                mPackingScanningAdapter.removeAll();
                List<SendSealPackingScanningInfo> sendSealPackingScanningInfos = AppJsonUtil.getArrayList(result, SendSealPackingScanningInfo.class);
                if (sendSealPackingScanningInfos != null) {

                    for (SendSealPackingScanningInfo info : sendSealPackingScanningInfos) {
                        info.setType(1);
                    }
                    mPackingScanningAdapter.addAll(sendSealPackingScanningInfos);

                    setNumber();

                }

                break;

            case 2:

                isInitRequestData = false;

                if (mUtils == null) {
                    mUtils = new UpImageUtils(this, UpImageUtils.TAG_PACKAGE, packageId, new UpImageUtils.UpImageListener() {
                        @Override
                        public void onUpSuccess() {


                            //包裹中的订单id列表,以逗号隔开的字符串
                            StringBuffer orderIds = new StringBuffer();
                            //新增加的ids

                            StringBuffer addOrderIds = new StringBuffer();

                            //删除包裹中的订单id列表(手动添加进来的不用添加)
                            StringBuffer deletePackageOrderIds = new StringBuffer();

                            for (SendSealPackingScanningInfo scanningInfo : mPackingScanningAdapter.findAll()) {

                                //找到没有被删除，isScan=true的数据
                                if (!scanningInfo.isDelete() && scanningInfo.isScan()) {

                                    //添加全部
                                    orderIds.append(scanningInfo.getP_o_order_id() + ",");
                                    if (scanningInfo.getType() != 1) {
                                        //手动添加的
                                        addOrderIds.append(scanningInfo.getP_o_order_id() + ",");
                                    }


                                } else {
                                    if (scanningInfo.getType() == 1) {
                                        deletePackageOrderIds.append(scanningInfo.getP_o_id() + ",");
                                    }

                                }


                            }


                            String chooseId = orderIds.toString().substring(0, orderIds.toString().length() - 1);

                            String deleteId = null;
                            if (deletePackageOrderIds.toString().length() > 0) {
                                deleteId = deletePackageOrderIds.toString().substring(0, deletePackageOrderIds.toString().length() - 1);
                            }
                            String addId = null;
                            if (addOrderIds.toString().length() > 0) {
                                addId = addOrderIds.toString().substring(0, addOrderIds.toString().length() - 1);
                            }

                            Logger.v("imagePath==" + mUtils.getImagePath());

                            doHttp(RetrofitUtils.createApi(Seal.class).packet(packageId, mBigCode, mContent, chooseId, Double.parseDouble(UserManger.getLng()), Double.parseDouble(UserManger.getLat()), deleteId,
                                    addId, mUtils.getImagePath()
                            ), 3);


                        }

                        @Override
                        public void onUpFailure() {

                            mTvProgress.setText("封包");
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
                mUtils.upPhoto(mPicAdapter.findAll());


                break;
            case 3:
                super.onSuccess(result, call, response, what);
                //封包操作成功

                //记录提交状态必须
                mUtils.isCommitSuccess = true;

                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.CLOSE_PACKAGE, true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭上一个界面
                        AppManger.getInstance().killActivity(SendSealReceivingOrdersActivity.class);

                        dialogHelper.dismiss();
                        finish();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭上一个界面
                        AppManger.getInstance().killActivity(SendSealReceivingOrdersActivity.class);
                        dialogHelper.dismiss();
                        finish();
                    }
                }).show();

                break;
            case 4:
                //删除以前的垃圾图片
                List<String> objKey = AppJsonUtil.getArrayList(result, "listKey", String.class);
                new UpImageUtils().deleteOtherFile(objKey, UpImageUtils.TAG_PACKAGE);

                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:
                    ArrayList<SendSealPackingScanningInfo> list = data.getParcelableArrayListExtra("data");
                    //出去重复的

                    List<SendSealPackingScanningInfo> temp = new ArrayList<>();
                    for (SendSealPackingScanningInfo scanningInfo : list) {
                        scanningInfo.setDelete(false);
                        Logger.v("getP_o_id==" + scanningInfo.getP_o_id());
                        Logger.v("getP_o_order_id==" + scanningInfo.getP_o_order_id());
                        for (SendSealPackingScanningInfo sendSealPackingScanningInfo : mPackingScanningAdapter.findAll()) {
                            if (scanningInfo.getP_o_order_id() == sendSealPackingScanningInfo.getP_o_order_id()) {
                                sendSealPackingScanningInfo.setDelete(false);
                                temp.add(scanningInfo);
                            }
                        }
                    }

                    list.removeAll(temp);
                    mPackingScanningAdapter.addAll(list);
                    setNumber();


                    break;
                case 2:
                    //扫描小包码
                    String code = data.getStringExtra("codeResult");
                    if (code.contains("QMCS")) {

                        boolean hava = false;
                        for (SendSealPackingScanningInfo scanningInfo : mPackingScanningAdapter.findAll()) {

                            if (scanningInfo.getOrder_tracking_code().equals(code)) {
                                scanningInfo.setScan(true);
                                hava = true;
                                break;
                            }

                        }

                        if (hava) {
                            mPackingScanningAdapter.notifyDataSetChanged();
                            setNumber();
                        } else {
                            showErrorToast("快件码不匹配");
                        }


                    } else {
                        showErrorToast("快件码不匹配");
                    }


                    break;
                case 3:

                    //扫描打包码
                    String bigCode = data.getStringExtra("codeResult");
                    if (bigCode.contains("QMWD")) {

                        mEtBigCode.setText(bigCode);

                    } else {
                        showErrorToast("打包码不匹配");
                    }


                    break;


            }
        }
    }

    private void setNumber() {

        int scan = 0;
        int size = 0;
        for (SendSealPackingScanningInfo scanningInfo : mPackingScanningAdapter.findAll()) {
            if (!scanningInfo.isDelete()) {
                size++;
                if (scanningInfo.isScan()) {
                    scan++;
                }
            }
        }
        String number = "已确认" + scan + "个快件，还有" + (size - scan) + "个未确认";
        mTvNumber.setText(number);


    }

    private int getNumber() {

        int scan = 0;
        int size = 0;
        for (SendSealPackingScanningInfo scanningInfo : mPackingScanningAdapter.findAll()) {
            if (!scanningInfo.isDelete()) {
                size++;
                if (scanningInfo.isScan()) {
                    scan++;
                }
            }
        }

        return (size - scan);
    }


}
