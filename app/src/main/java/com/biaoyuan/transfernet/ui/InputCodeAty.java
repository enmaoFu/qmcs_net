package com.biaoyuan.transfernet.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.view.MyAutoCompleteTextView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Title  :手动输入
 * Create : 2017/6/3
 * Author ：chen
 */

public class InputCodeAty extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_tip)
    TextView mTvTip;

    /**
     * 取件
     */
    public static final int TYPE_TAKE = 0;
    /**
     * 封包扫描快件码
     */
    public static final int TYPE_PACKAGE_SMALL = 1;
    /**
     * 封包扫描打包码
     */
    public static final int TYPE_PACKAGE_BIG = 2;
    /**
     * 验收
     */
    public static final int TYPE_PACKAGE_TEST = 3;
    @Bind(R.id.et_content)
    MyAutoCompleteTextView mEtContent;
    private int mType;

    private ArrayAdapter<String> arrayAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_input_code;
    }



    @Override
    public void initData() {

        initToolbar(mToolbar, "手动输入");

        mType = getIntent().getIntExtra("type", 0);

        String qmcs = getResources().getString(R.string.input_digits_QMCS);
        String qmwd = getResources().getString(R.string.input_digits_QMWD);

        String [] arr=new String[1];


        switch (mType) {
            case TYPE_TAKE:
                mEtContent.setHint("请输入快件码");
                mTvTip.setText("快件码");
                mEtContent.setKeyListener(DigitsKeyListener.getInstance(qmcs));

                arr[0]="QMCS";
                break;
            case TYPE_PACKAGE_SMALL:
                mEtContent.setHint("请输入快件码");
                mTvTip.setText("快件码");

                mEtContent.setKeyListener(DigitsKeyListener.getInstance(qmcs));
                arr[0]="QMCS";
                break;
            case TYPE_PACKAGE_BIG:
                mEtContent.setHint("请输入打包码");
                mTvTip.setText("打包码");
                mEtContent.setKeyListener(DigitsKeyListener.getInstance(qmwd));
                arr[0]="QMWD";
                break;
            case TYPE_PACKAGE_TEST:
                mEtContent.setHint("请输入打包码");
                mTvTip.setText("打包码");
                mEtContent.setKeyListener(DigitsKeyListener.getInstance(qmwd));
                arr[0]="QMWD";
                break;
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arr);
        mEtContent.setAdapter(arrayAdapter);
        mEtContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEtContent,InputMethodManager.SHOW_FORCED);
            }
        });

    }

    @OnClick({R.id.btn_commit})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()) {
            case R.id.btn_commit:
               String mm=mEtContent.getText().toString().trim();

                if (TextUtils.isEmpty(mm)){

                    showErrorToast("未填写内容");
                    return;
                }

                switch (mType) {
                    case TYPE_TAKE:
                    case TYPE_PACKAGE_SMALL:
                        if (!mm.contains("QMCS") || mm.length() < 16){
                            showErrorToast("请填写正确的快件码");
                            return;
                        }

                        break;
                    case TYPE_PACKAGE_BIG:
                    case TYPE_PACKAGE_TEST:

                        if (!mm.contains("QMWD") || mm.length() < 16){
                            showErrorToast("请填写正确的打包码");
                            return;
                        }
                        break;
                }

                Intent intent=getIntent();
                intent.putExtra("codeResult",mm);
                setResult(RESULT_OK,intent);
                finish();
                break;

        }

    }

    @Override
    public void requestData() {

    }
}
