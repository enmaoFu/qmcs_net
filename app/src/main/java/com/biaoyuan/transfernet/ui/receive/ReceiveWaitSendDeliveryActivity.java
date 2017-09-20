package com.biaoyuan.transfernet.ui.receive;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.http.Receive;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 代送快件详情->投递点签收
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class ReceiveWaitSendDeliveryActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.input_spot_name)
    EditText inputSpotName;
    @Bind(R.id.input_context)
    EditText inputContext;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.tel)
    TextView tel;

    private String orderIdStr;

    //默认为没有同意
    private boolean isAgreeFlag = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_receive_wait_send_delivery;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        orderIdStr = bundle.getString("orderIdStr");
        initToolbar(mToolbar,"投递点签收");
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Receive.class).IdentityObject(),1);
    }

    @OnClick({R.id.send_sms,R.id.is_select})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.send_sms:
                if(isAgreeFlag){
                    String getInputSpotName = inputSpotName.getText().toString().trim();
                    String getInputContext = inputContext.getText().toString().trim();
                    if(getInputSpotName.length() == 0){
                        showErrorToast("请填写投递点名称");
                    }else if(getInputContext.length() == 0){
                        showErrorToast("请填写短信具体详情");
                    }else{
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Receive.class).deliveryOrder(orderIdStr,getInputSpotName,getInputContext,
                                UserManger.getLng(),UserManger.getLat()),2);
                    }
                }else{
                    showErrorToast("请选择信息是否输入无误");
                }
                break;
            case R.id.is_select:
                //判断是否点击信息输入无误
                if (!isAgreeFlag) {
                    img.setImageResource(R.drawable.btn_select_active);
                } else {
                    img.setImageResource(R.drawable.btn_select_normal);
                }
                isAgreeFlag = !isAgreeFlag;
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //派送员姓名
                String sendName = AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"staffName");
                //派送员电话
                String sendTel = AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"staffPhone");
                name.setText("派件员 " + sendName);
                tel.setText(sendTel);
                break;
            case 2:
                showToast("发送短信成功");
                AppManger.getInstance().killActivity(ReceiveWaitSendPackageActivity.class);
                finish();
                break;
        }
    }
}
