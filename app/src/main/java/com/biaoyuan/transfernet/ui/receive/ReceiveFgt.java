package com.biaoyuan.transfernet.ui.receive;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MessageEvent;
import com.biaoyuan.transfernet.domain.MessageEventOne;
import com.biaoyuan.transfernet.domain.NetInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.send.SendFgt;
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

import static com.biaoyuan.transfernet.ui.send.SendFgt.mList;

/**
 * Title  :达到接收网点
 * Create : 2017/5/9
 * Author ：chen
 */
public class ReceiveFgt extends BaseFgt {
    @Bind(R.id.rb_left)
    RadioButton mRbLeft;
    @Bind(R.id.rb_right)
    RadioButton mRbRight;
    @Bind(R.id.view_pager)
    CustomViewPager mViewPager;
    @Bind(R.id.rg_mian)
    RadioGroup mRgMian;

    @Bind(R.id.lv_data)
    ListViewForScrollView mLvData;
    @Bind(R.id.id_drawer_layout)
    DrawerLayout mDrawerLayout;
    private NetAdapter mNetAdapter;
    //网点名称
    @Bind(R.id.tv_address)
    TextView tvAddress;
    //当前网点
    @Bind(R.id.tv_city)
    TextView tvCity;

    private List<BaseFgt> mFgts;

    @Override
    public int getLayoutId() {
        return R.layout.fgt_receive;
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Subscribe
    @Override
    public void initData() {

        //注册EventBus
        EventBus.getDefault().register(this);
        mNetAdapter = new NetAdapter(getActivity(), new ArrayList<NetInfo>(), R.layout.item_net_choose);

        int is = UserManger.getStaffIsContractor();
        /**
         * 判断是否是负责人，如果不是则不显示三角
         */
        if (is == 0) {
            tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        tvAddress.setText(UserManger.getAddress());
        tvCity.setText(UserManger.getAddress());

        //关闭手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mLvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                mDrawerLayout.closeDrawer(Gravity.LEFT);

                Handler handler = new Handler();

                //开启延时防止卡顿
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String getItemMsg = mNetAdapter.getItem(position).getBasicName();
                        //刷新id
                        UserManger.setBaseId(mNetAdapter.getItem(position).getBasicId2() + "");
                        EventBus.getDefault().post(new MessageEvent(getItemMsg, position));

                        UserManger.setAddress(getItemMsg);
                        UserManger.setPhone(String.valueOf(mNetAdapter.getItem(position).getOutBasicTelphone()));
                    }
                }, 500);

            }
        });

        mLvData.setAdapter(mNetAdapter);

        mFgts = new ArrayList<>();

        mFgts.add(new ReceiveScanFgt());
        mFgts.add(new NotSendFgt());

        MainFragmentAdapter fragmentAdapter = new MainFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(fragmentAdapter);

        mRgMian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_left:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_right:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });


    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        tvAddress.setText(UserManger.getAddress());
        tvCity.setText(UserManger.getAddress());
    }

    @OnClick({R.id.tv_address, R.id.tv_city})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_address:
                mNetAdapter.removeAll();
                int is = UserManger.getStaffIsContractor();
                //判断是否是负责人，能否请求子网点
                if (is == 1) {
                    /**
                     * 此处判断SendFgt里的list是否为空
                     * 为空：说明已经获取过网点，直接使用这个List
                     * 不为空：说明还未获取过网点，直接网络获取
                     */
                    if (SendFgt.mList != null) {


                        boolean hasChose = false;
                        for (NetInfo netInfo : SendFgt.mList) {

                            if (netInfo.getBasicId2() == Integer.parseInt(UserManger.getBaseId())) {
                                hasChose = true;
                            }
                        }

                        if (hasChose) {

                            for (NetInfo netInfo : SendFgt.mList) {
                                if (netInfo.getBasicId2() == Integer.parseInt(UserManger.getBaseId())) {
                                    netInfo.setWhether(1);
                                } else {
                                    netInfo.setWhether(0);
                                }
                            }
                        }


                        mNetAdapter.addAll(mList);


                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Send.class).viewAffiliate(1), 1);
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
                break;
        }
    }


    @Override
    public void requestData() {

    }

    class MainFragmentAdapter extends FragmentPagerAdapter {
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFgts.get(position);
        }

        @Override
        public int getCount() {
            return mFgts.size();
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
        Logger.v("22222222222222222222222222222");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在结束界面中解除注册
        EventBus.getDefault().unregister(this);
    }

}
