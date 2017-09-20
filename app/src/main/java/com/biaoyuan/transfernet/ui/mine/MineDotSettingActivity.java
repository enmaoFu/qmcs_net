package com.biaoyuan.transfernet.ui.mine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.and.yzy.frame.adapter.NewBaseAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemChildClickListener;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.MatchStingUtil;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.MineDotSettingAdapter;
import com.biaoyuan.transfernet.adapter.SendTakeAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MessageEvent;
import com.biaoyuan.transfernet.domain.MessageEventOne;
import com.biaoyuan.transfernet.domain.MineDotSettingInfo;
import com.biaoyuan.transfernet.domain.NetInfo;
import com.biaoyuan.transfernet.domain.SendTakeInfo;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.ui.send.SendFgt;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendTakeItemDetailsActivity;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.biaoyuan.transfernet.ui.send.SendFgt.mList;

/**
 * @author :enmaoFu
 * @title :网点联系电话设置页面
 * @create :2017/5/9
 */
public class MineDotSettingActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.but)
    TextView but;
    @Bind(R.id.msg)
    EditText msg;
    @Bind(R.id.title_spot)
    TextView titleSpot;

    //适配器
    private MineDotSettingAdapter mineDotSettingAdapter;
    private String msgStr;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_dot_setting;
    }

    @Override
    public void initData() {

        initToolbar(mToolbar, "网点联系电话设置");
        titleSpot.setText(UserManger.getAddress());
        msg.setText(UserManger.getPhone());

        //实例化适配器
        mineDotSettingAdapter = new MineDotSettingAdapter(this,new ArrayList<NetInfo>(),R.layout.itme_mine_dot_setting);
        mineDotSettingAdapter.addAll(mList);
        //设置adapter
        listview.setAdapter(mineDotSettingAdapter);

    }

    @OnClick({R.id.but})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.but:
                String textStr = but.getText().toString().trim();
                msgStr = msg.getText().toString().trim();
                if(textStr.equals("编辑")){
                    msg.setFocusableInTouchMode(true);
                    msg.setFocusable(true);
                    msg.requestFocus();
                    msg.setSelection(msg.length());
                    showKeyboard();
                    but.setText("保存");
                    but.setTextColor(this.getResources().getColor(R.color.colorAccent));
                    Drawable drawable = this.getResources().getDrawable(R.drawable.icon_save);
                    // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    but.setCompoundDrawables(drawable,null,null,null);
                }else if(textStr.equals("保存")){
                    if(!MatchStingUtil.isMobile(msgStr)){
                        showErrorToast("请输入正确的手机号");
                    }else if(UserManger.getPhone().equals(msgStr)){
                        msg.setFocusableInTouchMode(false);
                        msg.setFocusable(false);
                        but.setText("编辑");
                        hintKbTwo();
                        but.setTextColor(this.getResources().getColor(R.color.font_black333));
                        Drawable drawable = this.getResources().getDrawable(R.drawable.icon_edit);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        but.setCompoundDrawables(drawable,null,null,null);
                    }else{
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Mine.class).updateAffiliateBasic(Long.parseLong(UserManger.getBaseId()),msgStr),1);
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                UserManger.setPhone(msgStr);
                msg.setFocusableInTouchMode(false);
                msg.setFocusable(false);
                but.setText("编辑");
                hintKbTwo();
                but.setTextColor(this.getResources().getColor(R.color.font_black333));
                Drawable drawable = this.getResources().getDrawable(R.drawable.icon_edit);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                but.setCompoundDrawables(drawable,null,null,null);
                EventBus.getDefault().post(new MessageEventOne(msgStr, titleSpot.getText().toString().trim()));
                showToast("设置网点电话号码成功");
                break;
        }
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {
    }

    /**
     * 弹出软键盘
     */
    private void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(msg, 0);
    }

    /**
     * 关闭软键盘
     */
    private void hintKbTwo() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
