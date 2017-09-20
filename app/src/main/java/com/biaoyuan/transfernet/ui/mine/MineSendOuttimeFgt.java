package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.MineSendOuttimeAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MineSendOuttimeItem;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发件超时
 */
public class MineSendOuttimeFgt extends BaseFgt {
    @Bind(R.id.rv_data)
    RecyclerView mRvData;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private MineSendOuttimeAdapter mAdapter;

    private int targetPage = 1;

    //用来标记是否在加载
    private boolean isLoading = false;

    @Override
    public int getLayoutId() {
        return R.layout.ptr_recyclerview;
    }

    @Override
    public void initData() {
        PtrInitHelper.initPtr(getActivity(), mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,4),1);
            }
        });

        LinearLayoutManager mRecyclerViewlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new MineSendOuttimeAdapter(R.layout.item_mine_send_outtime,new ArrayList<MineSendOuttimeItem>());

        //设置布局管理器
        mRvData.setLayoutManager(mRecyclerViewlayoutManager);
        mRvData.setHasFixedSize(true);
        //mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRvData.setItemAnimator(new DefaultItemAnimator());
        ///设置item点击事件
        mRvData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int excepId = mAdapter.getItem(position).getExcep_id();
                int excepType = mAdapter.getItem(position).getExcep_type();
                String orderNo = mAdapter.getItem(position).getOrder_no();
                //快件码
                String code = mAdapter.getItem(position).getOrder_tracking_code();
                //取件时间
                /*TextView date = (TextView)view.findViewById(R.id.order_date);
                String qjDateNew = date.getText().toString().trim();*/
                //发出要求
                //获取取件时间后12小时
                /*long getDate_12 = mAdapter.getItem(position).getDelivery_update_time() + 60 * 60 * 12 * 1000;
                String yqDateNew = "要求时间：" + DateTool.timesToStrTime(getDate_12 + "", "yyyy.MM.dd HH:mm");*/
                //超过小时
                /*TextView cgDate = (TextView)view.findViewById(R.id.order_cs_date);
                String cgDateNew = cgDate.getText().toString().trim();*/
                Bundle bundle = new Bundle();
                bundle.putString("excepId",String.valueOf(excepId));
                bundle.putInt("excepType",excepType);
                bundle.putString("code",code);
                /*bundle.putString("qjDateNew",qjDateNew);
                bundle.putString("yqDateNew",yqDateNew);
                bundle.putString("cgDateNew",cgDateNew);*/
                bundle.putString("orderNo",orderNo);
                startActivity(MineSendOuttimeDetailsActivity.class,bundle);
            }
        });
        //设置没有数据的页面
        setEmptyView(mAdapter,null);
        //设置adapter
        mRvData.setAdapter(mAdapter);

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mRvData==null){
                    return;
                }
                mRvData.post(new Runnable() {
                    @Override
                    public void run() {

                        if (targetPage == 1) {
                            mAdapter.loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,4),2);
                    }
                });

            }
        }, mRvData);

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        isLoading = true;
        //刷新界面
        targetPage = 1;
        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,4),1);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoadingContentDialog();
    }

    @Override
    public void requestData() {
        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,4),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<MineSendOuttimeItem> mineSendOuttimeItemList = AppJsonUtil.getArrayList(result,MineSendOuttimeItem.class);
                if (mineSendOuttimeItemList != null) {
                    mAdapter.setNewData(mineSendOuttimeItemList);
                    if (mineSendOuttimeItemList.size()<Integer.parseInt(UserManger.pageSize)){
                        mAdapter.loadMoreEnd();
                    }
                }else {
                    mAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
            case 2:

                //加载更多
                List<MineSendOuttimeItem> mineSendOuttimeItemListTwo = AppJsonUtil.getArrayList(result,MineSendOuttimeItem.class);
                if (mineSendOuttimeItemListTwo != null && mineSendOuttimeItemListTwo.size() > 0) {
                    mAdapter.addDatas(mineSendOuttimeItemListTwo);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
    }
}
