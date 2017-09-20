package com.biaoyuan.transfernet.ui.send;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.NetAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.NetInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Title  :选择网点
 * Create : 2017/5/26
 * Author ：chen
 */

public class SendNetAty extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_city)
    TextView mTvCity;
    @Bind(R.id.lv_data)
    ListViewForScrollView mLvData;

    private NetAdapter mNetAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_net;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar,"选择网点");

        List<NetInfo> netInfos=new ArrayList<>();
        netInfos.add(new NetInfo());
        netInfos.add(new NetInfo());
        netInfos.add(new NetInfo());
        netInfos.add(new NetInfo());
        mNetAdapter=new NetAdapter(this,netInfos,R.layout.item_net_choose);
        mLvData.setAdapter(mNetAdapter);
    }

    @Override
    public void requestData() {

    }
}
