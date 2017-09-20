package com.biaoyuan.transfernet.ui.send;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.and.yzy.frame.view.other.CustomViewPager;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.NetAdapter;
import com.biaoyuan.transfernet.adapter.SendViewPagerAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MessageEvent;
import com.biaoyuan.transfernet.domain.MessageEventOne;
import com.biaoyuan.transfernet.domain.NetInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.MainAty;
import com.biaoyuan.transfernet.ui.send.SendSealFgt.SendSealFgt;
import com.biaoyuan.transfernet.ui.send.SendSendFgt.SendSendFgt;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendTakeFgt;
import com.biaoyuan.transfernet.util.AppJsonUtil;
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
 * Title  :发送网点
 * Create :2017/5/9
 * Author :chen
 */
public class SendFgt extends BaseFgt {

    //取件
    @Bind(R.id.qj)
    RadioButton mTake;

    //发布
    @Bind(R.id.fb)
    RadioButton mSend;

    //封包
    @Bind(R.id.fz)
    RadioButton mSeal;

    //网点名称
    @Bind(R.id.tv_address)
    TextView tvAddress;
    //当前网点
    @Bind(R.id.tv_city)
    TextView tvCity;

    //viewpager
    @Bind(R.id.viewpager)
    CustomViewPager mViewPager;
    @Bind(R.id.rg_mian)
    RadioGroup mRgMian;
    @Bind(R.id.lv_data)
    ListViewForScrollView mLvData;
    @Bind(R.id.id_drawer_layout)
    DrawerLayout mDrawerLayout;

    //页面集合
    private List<BaseFgt> mFragmentList;

    //三个页面
    private SendTakeFgt mSendTakeFgt;
    private SendSendFgt mSendSendFgt;
    private SendSealFgt mSendSealFgt;

    private NetAdapter mNetAdapter;

    public static List<NetInfo> mList;

    public static List<RadioButton> radioButtons = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fgt_send_main;
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Subscribe
    @Override
    public void initData() {
        radioButtons.add(mTake);
        radioButtons.add(mSend);
        radioButtons.add(mSeal);

        //注册EventBus
        EventBus.getDefault().register(this);
        initViewPager();
        tvAddress.setText(UserManger.getAddress());
        tvCity.setText(UserManger.getAddress());

    }

    /**
     * 第一次进入获取子网点信息
     */
    @Override
    public void requestData() {
        /**
         * 1表示是负责人
         * 判断是否是负责人
         * 是：则进行网络获取
         * 否：能选择进行网络获取子网点，并且设置三角不显示
         */
        int is = UserManger.getStaffIsContractor();
        if (is == 1) {
            //   showLoadingDialog(null);
            doHttp(RetrofitUtils.createApi(Send.class).viewAffiliate(1), 1);
        } else {
            tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        mFragmentList.get(mViewPager.getCurrentItem()).onUserInvisible();

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (MainAty.radioButtons.get(0).isChecked()) {
            mFragmentList.get(mViewPager.getCurrentItem()).onUserVisible();
        }

    }

    /**
     * 初始化viewpager
     */
    public void initViewPager() {

        mFragmentList = new ArrayList<>();
        mSendTakeFgt = new SendTakeFgt();
        mSendSendFgt = new SendSendFgt();
        mSendSealFgt = new SendSealFgt();

        mFragmentList.add(mSendTakeFgt);
        mFragmentList.add(mSendSendFgt);
        mFragmentList.add(mSendSealFgt);

        mViewPager.setAdapter(new SendViewPagerAdapter(getChildFragmentManager(), mFragmentList));

        mViewPager.setOffscreenPageLimit(3);

        mNetAdapter = new NetAdapter(getActivity(), new ArrayList<NetInfo>(), R.layout.item_net_choose);

        mLvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                mDrawerLayout.closeDrawer(Gravity.LEFT);


                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String getItemMsg = mNetAdapter.getItem(position).getBasicName();
                        //刷新id
                        UserManger.setBaseId(mNetAdapter.getItem(position).getBasicId2() + "");
                        //刷新当前选中的fgt
                        mFragmentList.get(mViewPager.getCurrentItem()).onUserVisible();

                        //发送一个事件
                        EventBus.getDefault().post(new MessageEvent(getItemMsg, position));

                        //保存
                        UserManger.setAddress(getItemMsg);
                        UserManger.setPhone(String.valueOf(mNetAdapter.getItem(position).getOutBasicTelphone()));
                    }
                }, 500);


            }
        });

        mLvData.setAdapter(mNetAdapter);


        //关闭手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


    }

    /**
     * 设置viewpager要显示的视图
     *
     * @param desTab
     */
    private void changeView(int desTab) {
        mViewPager.setCurrentItem(desTab, false);
        switch (desTab) {
            case 0:
                mTake.setChecked(true);
                break;
            case 1:
                mSend.setChecked(true);
                break;
            case 2:
                mSeal.setChecked(true);
                break;
        }
    }

    @OnClick({R.id.qj, R.id.fb, R.id.fz, R.id.tv_address, R.id.messgae, R.id.tv_city})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.qj:
                changeView(0);
                break;
            case R.id.fb:
                changeView(1);
                break;
            case R.id.fz:
                changeView(2);
                break;
            case R.id.messgae:
                startActivity(SendMessageActivity.class, null);
                break;
            case R.id.tv_address:
                int is = UserManger.getStaffIsContractor();
                //判断是否是负责人，如果是才能打开选择网点
                if (is == 1) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                String resultData = AppJsonUtil.getString(result, "data");
                Logger.v(resultData);
                mNetAdapter.removeAll();
                mList = AppJsonUtil.getArrayList(result, "affiliatelist", NetInfo.class);
                //设置默认选中
                if (mList != null && mList.size() > 0) {

                    boolean hasChose = false;
                    for (NetInfo netInfo : mList) {

                        if (netInfo.getBasicId2() == Integer.parseInt(UserManger.getBaseId())) {
                            hasChose = true;
                            break;
                        }
                    }

                    if (hasChose) {

                        for (NetInfo netInfo : mList) {
                            if (netInfo.getBasicId2() == Integer.parseInt(UserManger.getBaseId())) {
                                netInfo.setWhether(1);
                            } else {
                                netInfo.setWhether(0);
                            }
                        }
                    }
                    mNetAdapter.addAll(mList);
                }


                break;
        }
    }


    /**
     * 事件总线的回调函数
     * 注意：在接受页面注册了事件，必须写回调函数并且加注解
     *
     * @param event
     */
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        Logger.v("1111111111111111111111111111");
        String getItemMsg = mNetAdapter.getItem(event.getPosition()).getBasicName();
        tvAddress.setText(getItemMsg);
        tvCity.setText(getItemMsg);
        //将已选择的设为1，表示网点列表不再显示，只显示到当前网点
        mNetAdapter.getItem(event.getPosition()).setWhether(1);
        //将当前网点设为1，表示将当前网点重新显示到网点列表
        mNetAdapter.getMitem().setWhether(0);
        //更新
        mNetAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onMessageEventOne(MessageEventOne event) {
        List<NetInfo> netInfoList = mNetAdapter.findAll();
        for (int i = 0; i < netInfoList.size(); i++) {
            if (netInfoList.get(i).getBasicName().equals(event.getName())) {
                netInfoList.get(i).setOutBasicTelphone(Long.parseLong(event.getMesage()));
            }
        }
        //更新
        mNetAdapter.notifyDataSetChanged();
    }


  /*  @Subscribe
    public void onMessageEvent(PlayEvent event) {
        //刷新界面
        if (MainAty.radioButtons.get(0).isChecked()) {
            mFragmentList.get(mViewPager.getCurrentItem()).onUserVisible();
        }

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在结束界面中解除注册
        EventBus.getDefault().unregister(this);
        if (mList != null) {
            mList.clear();
        }
    }


}

