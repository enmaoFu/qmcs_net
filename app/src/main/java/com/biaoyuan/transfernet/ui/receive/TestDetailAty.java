package com.biaoyuan.transfernet.ui.receive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.and.yzy.frame.adapter.AdapterCallback;
import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.and.yzy.frame.view.linearlistview.LinearListView;
import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.TestPackageListAdapter;
import com.biaoyuan.transfernet.adapter.TestPicAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.domain.TestCommitInfo;
import com.biaoyuan.transfernet.domain.TestPackageListInfo;
import com.biaoyuan.transfernet.domain.TestResultInfo;
import com.biaoyuan.transfernet.http.Image;
import com.biaoyuan.transfernet.http.Receive;
import com.biaoyuan.transfernet.ui.ShowBigImgaeAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.EasyTransition;
import com.biaoyuan.transfernet.util.EasyTransitionOptions;
import com.biaoyuan.transfernet.util.UpImageTestUtils;
import com.biaoyuan.transfernet.util.UpImageUtils;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  :扫码包裹详情
 * Create : 2017/6/5
 * Author ：chen
 */

public class TestDetailAty extends BaseAty implements AdapterCallback {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.lv_pic)
    LinearListView mLvPic;
    @Bind(R.id.lv_data)
    ListViewForScrollView mLvData;
    @Bind(R.id.tv_day)
    TextView mTvDay;
    @Bind(R.id.tv_month)
    TextView mTvMonth;
    @Bind(R.id.tv_send_address)
    TextView mTvSendAddress;
    @Bind(R.id.tv_receive_address)
    TextView mTvReceiveAddress;
    @Bind(R.id.tv_size)
    TextView mTvSize;
    @Bind(R.id.tv_code)
    TextView mTvCode;
    @Bind(R.id.tv_number)
    TextView mTvNumber;

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.tv_progress)
    TextView mTvProgress;

    private TestPicAdapter mPicAdapter;
    private TestPackageListAdapter mListAdapter;

    private String packageId;


    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_detail;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "包裹详情");

        List<PicInfo> picInfos = new ArrayList<>();


        mPicAdapter = new TestPicAdapter(this, picInfos, R.layout.item_list_pic);
        mLvPic.setAdapter(mPicAdapter);

        List<TestPackageListInfo> listInfos = new ArrayList<>();

        mListAdapter = new TestPackageListAdapter(this, listInfos, R.layout.item_test_package_list, this);
        mLvData.setAdapter(mListAdapter);

        mLvPic.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                PicInfo picInfo = mPicAdapter.getItem(position);

                Intent intent = new Intent(TestDetailAty.this, ShowBigImgaeAty.class);
                intent.putExtra("url", picInfo.getPath());

                // ready for transition options
                EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(
                                TestDetailAty.this,
                                view.findViewById(R.id.img));


                // start transition
                EasyTransition.startActivity(intent, options);


            }
        });

    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Receive.class).getIndexReleaseDetailList(getIntent().getStringExtra("codeResult"), UserManger.getBaseId()), 1);


    }


    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                TestResultInfo resultInfo = AppJsonUtil.getObject(result, TestResultInfo.class);
                packageId = resultInfo.getPackagePublish().getPackageId() + "";
                long time = resultInfo.getPackagePublish().getPublishReqDelivTime();


                mTvDay.setText(DateTool.timesToStrTime(time + "", "dd"));
                String str = formatInteger(Integer.parseInt(DateTool.timesToStrTime(time + "", "MM")));
                mTvMonth.setText(str + "月");
                //DateTool.timesToStrTime(time + "", "MM") + "月"


                String sendAddress[] = resultInfo.getPackagePublish().getOutBasicAreaName().split(",");

                mTvSendAddress.setText(sendAddress[1] + sendAddress[2] + resultInfo.getPackagePublish().getOutBasicName());

                String receiveAddress[] = resultInfo.getPackagePublish().getEntBasicAreaName().split(",");

                if (receiveAddress.length > 2) {
                    mTvReceiveAddress.setText(receiveAddress[1] + receiveAddress[2] + resultInfo.getPackagePublish().getEntBasicName());
                } else {
                    mTvReceiveAddress.setText(resultInfo.getPackagePublish().getEntBasicAreaName() + resultInfo.getPackagePublish().getEntBasicName());
                }


                mTvSize.setText("最长边≤" + resultInfo.getPackagePublish().getPackageSize() + "cm   " + resultInfo.getPackagePublish().getPackageWeight() + "kg");

                mTvCode.setText(resultInfo.getPackagePublish().getPackageCode());

                mTvNumber.setText(resultInfo.getListOrderSize() + "个");


                mListAdapter.removeAll();

                mListAdapter.addAll(resultInfo.getListOrder());


                StringBuffer orderIds = new StringBuffer();
                //拼接id
                for (int i = 0; i < resultInfo.getListOrder().size(); i++) {
                    orderIds.append("order/" + resultInfo.getListOrder().get(i).getOrderID());
                    if (i != resultInfo.getListOrder().size() - 1) {
                        orderIds.append(",");
                    }
                }
                //删除oos的垃圾图片
                doHttp(RetrofitUtils.createApi(Image.class).findKey(UpImageUtils.TAG_ORDER + "/" + orderIds.toString()), 3);


                //添加图片
                String url = resultInfo.getPackagePublish().getPublishPicsUrl();


                if (url != null && url.length() > 0) {

                    String[] paths = url.split(",");

                    for (int i = 0; i < paths.length; i++) {
                        PicInfo picInfo = new PicInfo();

                        picInfo.setPath(paths[i]);
                        mPicAdapter.addInfo(picInfo);
                    }

                }


                break;
            case 2:
                UpImageTestUtils.isCommitSuccess = true;
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.TEST, true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        dialogHelper.dismiss();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        dialogHelper.dismiss();
                    }
                }).show();

                break;
            case 3:
                //删除以前的垃圾图片
                List<String> objKey = AppJsonUtil.getArrayList(result, "listKey", String.class);
                new UpImageTestUtils().deleteOtherFile(objKey, UpImageUtils.TAG_ORDER);
                break;
        }

    }

    @Override
    public void adapterInfotoActiity(Object data, int what) {


        final TestPackageListInfo mInfo = (TestPackageListInfo) data;

        switch (what) {


            case 1:

                if (mInfo.isDS()) {
                    //丢失状态的时候点击

                    mInfo.setDS(false);
                    mListAdapter.notifyDataSetChanged();
                } else {

                    //未丢失的时候点击
                    new MaterialDialog(this).setMDMessage("是否确认该件已丢失?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    mInfo.setDS(true);
                                    mInfo.setPS(false);
                                    mInfo.setMsg(null);
                                    mInfo.setPicInfos(null);
                                    mListAdapter.notifyDataSetChanged();
                                }
                            }).show();

                }


                break;
            case 2:
                //破损

                Bundle bundle = new Bundle();
                bundle.putParcelable("data", mInfo);
                startActivityForResult(TestErrorAty.class, bundle, 1);

                break;
        }

    }

    private UpImageTestUtils mUtils;

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        switch (what) {
            case 2:
                if (mUtils != null) {
                    mUtils.deleteFile();
                }
                if (mProgressBar != null) {
                    mProgressBar.setProgress(100);
                    mTvProgress.setText("确认");
                }
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.TEST, false).setDismissClick(new View.OnClickListener() {
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
                break;

            case 1:
                setResult(RESULT_OK);
                finish();
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
                mTvProgress.setText("确认");
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
                mTvProgress.setText("确认");
            }
        }
    }

    @OnClick({R.id.Rl_commit})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()) {
            case R.id.Rl_commit:

                //上传图片
                //  判断有图片没得

                List<PicInfo> picInfos = new ArrayList<>();


                for (TestPackageListInfo listInfo : mListAdapter.findAll()) {


                    if (listInfo.getPicInfos() != null && listInfo.getPicInfos().size() > 0) {
                        picInfos.addAll(listInfo.getPicInfos());
                    }
                }


                if (picInfos.size() > 0) {

                    if (mUtils == null) {

                        mUtils = new UpImageTestUtils(TestDetailAty.this, UpImageTestUtils.TAG_ORDER, new UpImageTestUtils.UpImageListener() {
                            @Override
                            public void onUpSuccess() {


                                //组装json
                                List<TestCommitInfo> commitInfoList = new ArrayList<>();
                                //验收
                                for (TestPackageListInfo listInfo : mListAdapter.findAll()) {

                                    TestCommitInfo commitInfo = new TestCommitInfo();
                                    commitInfo.setOrderId(listInfo.getOrderID() + "");

                                    if (listInfo.isDS()) {

                                        //丢失
                                        commitInfo.setOrderStauts("1");
                                    } else if (listInfo.isPS()) {
                                        //破损
                                        commitInfo.setOrderStauts("2");
                                        commitInfo.setFileUrl(mUtils.getPathRoot() + "/" + listInfo.getImagePath());
                                        Logger.v("path==" + commitInfo.getFileUrl());
                                        commitInfo.setOrderText(listInfo.getMsg());
                                    } else {
                                        //正常
                                        commitInfo.setOrderStauts("3");

                                    }
                                    commitInfoList.add(commitInfo);
                                }


                                Logger.v("json==" + JSON.toJSONString(commitInfoList));
                                doHttp(RetrofitUtils.createApi(Receive.class).poAcceptance(packageId, JSON.toJSONString(commitInfoList), UserManger.getBaseId()), 2);


                            }

                            @Override
                            public void onUpFailure() {


                                dismissLoadingDialog();
                                mTvProgress.setText("确认");
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
                    mUtils.upPhoto(picInfos);


                } else {

                    //无破损无需上传图片
                    showLoadingDialog(null);

                    //验收

                    //组装json
                    List<TestCommitInfo> commitInfoList = new ArrayList<>();
                    //验收
                    for (TestPackageListInfo listInfo : mListAdapter.findAll()) {

                        TestCommitInfo commitInfo = new TestCommitInfo();
                        commitInfo.setOrderId(listInfo.getOrderID() + "");

                        if (listInfo.isDS()) {

                            commitInfo.setOrderStauts("1");
                        } else {
                            commitInfo.setOrderStauts("3");

                        }

                        commitInfoList.add(commitInfo);

                    }

                    Logger.v("json==" + JSON.toJSONString(commitInfoList));
                    doHttp(RetrofitUtils.createApi(Receive.class).poAcceptance(packageId, JSON.toJSONString(commitInfoList), UserManger.getBaseId()), 2);


                }


                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            TestPackageListInfo mInfo = data.getParcelableExtra("data");

            for (TestPackageListInfo testPackageListInfo : mListAdapter.findAll()) {
                if (mInfo.getOrderID() == testPackageListInfo.getOrderID()) {
                    testPackageListInfo.setMsg(mInfo.getMsg());
                    testPackageListInfo.setDS(mInfo.isDS());
                    testPackageListInfo.setPS(mInfo.isPS());
                    testPackageListInfo.setOrderID(mInfo.getOrderID());
                    testPackageListInfo.setImagePath(mInfo.getImagePath());
                    testPackageListInfo.setOrderNo(mInfo.getOrderNo());
                    testPackageListInfo.setOrderTrackingCode(mInfo.getOrderTrackingCode());
                    testPackageListInfo.setPicInfos(mInfo.getPicInfos());
                    break;
                }
            }

            mListAdapter.notifyDataSetChanged();
        }
    }

    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};
    char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    public String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }

}
