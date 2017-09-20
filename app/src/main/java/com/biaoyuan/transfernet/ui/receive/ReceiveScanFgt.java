package com.biaoyuan.transfernet.ui.receive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.ui.AllScanAty;
import com.biaoyuan.transfernet.ui.InputCodeAty;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;

import butterknife.OnClick;

/**
 * Title  : 二维码扫描
 * Create : 2017/5/9
 * Author ：chen
 */
public class ReceiveScanFgt extends BaseFgt {


    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fgt_go_scan;
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.img_goto_check})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()) {
            case R.id.img_goto_check:
                Bundle bundle = new Bundle();
                bundle.putInt("type", InputCodeAty.TYPE_PACKAGE_TEST);

                startActivityForResult(AllScanAty.class, bundle, 1);
                break;

        }
    }

    @Override
    public void requestData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {

            final String str = data.getStringExtra("codeResult");

            if (!str.contains("QMWD")) {
                showErrorToast("打包码不匹配");
                return;
            }

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Bundle bundle = new Bundle();
                    bundle.putString("codeResult", str);
                    startActivityForResult(TestDetailAty.class, bundle, 2);
                }
            }, 300);


        } else if (requestCode == 2&&resultCode==getActivity().RESULT_OK) {
            //验收失败
            final RequestResultDialogHelper dialogHelperThree = new RequestResultDialogHelper(getActivity());

            dialogHelperThree.setType(RequestResultDialogHelper.TEST, false).setDismissClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogHelperThree.dismiss();

                }
            }).setCommitClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogHelperThree.dismiss();

                    Bundle bundle = new Bundle();
                    bundle.putInt("type", InputCodeAty.TYPE_PACKAGE_TEST);
                    startActivityForResult(AllScanAty.class, bundle, 1);
                }
            }).show();


        }
    }


}
