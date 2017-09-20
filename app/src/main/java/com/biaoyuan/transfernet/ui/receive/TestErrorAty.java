package com.biaoyuan.transfernet.ui.receive;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.adapter.AdapterCallback;
import com.and.yzy.frame.view.dialog.FormBotomDefaultDialogBuilder;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.and.yzy.frame.view.linearlistview.LinearListView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.AddPicAdapter;
import com.biaoyuan.transfernet.base.BasePhotoAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.domain.TestPackageListInfo;
import com.biaoyuan.transfernet.ui.ShowBigImgaeAty;
import com.biaoyuan.transfernet.util.EasyTransition;
import com.biaoyuan.transfernet.util.EasyTransitionOptions;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Title  :破损快件
 * Create : 2017/6/3
 * Author ：chen
 */

public class TestErrorAty extends BasePhotoAty implements AdapterCallback {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.uploda_photo)
    TextView mUplodaPhoto;
    @Bind(R.id.lv_pic)
    LinearListView mLvPic;
    @Bind(R.id.add_pic)
    ImageView mAddPic;
    @Bind(R.id.tv_tip)
    TextView mTvTip;
    @Bind(R.id.tv_code)
    TextView mTvCode;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.tv_cancle)
    TextView mTvCancle;
    @Bind(R.id.tv_commit)
    TextView mTvCommit;


    private long orderId;

    private AddPicAdapter mPicAdapter;

    private FormBotomDefaultDialogBuilder mDefaultDialogBuilder;


    private TestPackageListInfo mInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_error_file;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "破损快件");

        mInfo = getIntent().getParcelableExtra("data");
        orderId=mInfo.getOrderID();
        mTvCode.setText(mInfo.getOrderTrackingCode());

        List<PicInfo> mPicList = new ArrayList<>();
        mPicAdapter = new AddPicAdapter(this, mPicList, R.layout.item_add_pic, this);
        mLvPic.setAdapter(mPicAdapter);

        //判断是否已有数据
        if (mInfo.getPicInfos() != null) {

            //这里用new 对象
            for (PicInfo picInfo : mInfo.getPicInfos()) {

                PicInfo info=new PicInfo();
                info.setPath(picInfo.getPath());
                info.setId(orderId);
                mPicAdapter.addInfo(info);
            }
            if (!TextUtils.isEmpty(mInfo.getMsg())){
                mEtContent.setText(mInfo.getMsg());
            }

            mTvCancle.setVisibility(View.VISIBLE);
            mTvCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog(TestErrorAty.this).setMDMessage("是否确认该件未破损?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    mInfo.setPS(false);
                                    mInfo.setMsg(null);
                                    mInfo.setPicInfos(null);
                                    setResult(RESULT_OK,getIntent().putExtra("data",mInfo));
                                    finish();
                                }
                            }).show();
                }
            });
        } else {
            mTvCancle.setVisibility(View.GONE);
        }


        mLvPic.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                PicInfo picInfo=mPicAdapter.getItem(position);

                Intent intent = new Intent(TestErrorAty.this, ShowBigImgaeAty.class);
                intent.putExtra("url",picInfo.getPath());

                // ready for transition options
                EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(
                                TestErrorAty.this,
                                view.findViewById(R.id.img));


                // start transition
                EasyTransition.startActivity(intent, options);


            }
        });


    }
    @OnClick({R.id.add_pic,R.id.tv_commit})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {

            case R.id.add_pic:
                showPicDialog();
                break;
            case R.id.tv_commit:

                if (mPicAdapter.findAll().size()==0){
                    showErrorToast("请至少上传一张图片");
                    return;
                }
                if ( mInfo.getPicInfos()!=null){
                    mInfo.getPicInfos().clear();
                }


                mInfo.setPicInfos(mPicAdapter.findAll());
                if (!TextUtils.isEmpty(mEtContent.getText().toString().trim())){
                    mInfo.setMsg(mEtContent.getText().toString().trim());
                }else {
                    mInfo.setMsg(null);
                }


                //拼接图片地址
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(mInfo.getOrderID()+"/[");

                for (int i = 0; i < mPicAdapter.findAll().size(); i++) {
                    PicInfo picInfo=mPicAdapter.findAll().get(i);
                    File file=new File(picInfo.getPath());
                    stringBuffer.append(file.getName());

                    if (i==mPicAdapter.findAll().size()-1){
                        stringBuffer.append("]");
                    }else {
                        stringBuffer.append(",");
                    }
                }

                mInfo.setImagePath(stringBuffer.toString());

                mInfo.setPS(true);
                mInfo.setDS(false);
                setResult(RESULT_OK,getIntent().putExtra("data",mInfo));
                finish();

                break;
        }
    }

    @Override
    public void requestData() {

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
                picInfo.setId(orderId);
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
}
