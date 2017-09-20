package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.MineTakeCancleAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MineTakeAlreadyInfo;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendTakeItemDetailsActivity;
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
 * 我的取件-已取消
 * Created by Administrator on 2017/5/11.
 */

public class MineTakeCancleFgt extends BaseFgt {

    //下拉刷新

    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private MineTakeCancleAdapter mAdapter;

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
                doHttp(RetrofitUtils.createApi(Mine.class).getMyPickUp(Integer.parseInt(UserManger.pageSize),targetPage,17),1);
            }
        });

        //实例化布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mAdapter = new MineTakeCancleAdapter(R.layout.item_mine_take_cancle, new ArrayList<MineTakeAlreadyInfo>());
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置间隔样式
        /*mTekeRecyclerview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor(getResources().getString(R.string.parseColor)))
                        .sizeResId(R.dimen.size_0_5p)
                        .build());*/
        //大小不受适配器影响
        mRecyclerView.setHasFixedSize(true);
        //设置加载动画类型
        //mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置没有数据的页面
        setEmptyView(mAdapter,null);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int orderId = mAdapter.getItem(position).getOrder_id();
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", orderId);
                bundle.putInt("staffId", 1);
                startActivity(MineTakeCancleDetalseActivity.class, bundle);
            }
        });
        //设置adapter
        mRecyclerView.setAdapter(mAdapter);

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mRecyclerView==null){
                    return;
                }
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (targetPage == 1) {
                            mAdapter.loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Mine.class).getMyPickUp(Integer.parseInt(UserManger.pageSize),targetPage,17),2);
                    }
                });

            }
        }, mRecyclerView);

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        //如果选中了第一个
        isLoading = true;
        //刷新界面
        targetPage = 1;
        doHttp(RetrofitUtils.createApi(Mine.class).getMyPickUp(Integer.parseInt(UserManger.pageSize),targetPage,17),1);
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
        doHttp(RetrofitUtils.createApi(Mine.class).getMyPickUp(Integer.parseInt(UserManger.pageSize),targetPage,17),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<MineTakeAlreadyInfo> mineTakeAlreadyInfos = AppJsonUtil.getArrayList(result,MineTakeAlreadyInfo.class);
                if (mineTakeAlreadyInfos != null) {
                    mAdapter.setNewData(mineTakeAlreadyInfos);
                    if (mineTakeAlreadyInfos.size()<Integer.parseInt(UserManger.pageSize)){
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
                List<MineTakeAlreadyInfo> mineTakeAlreadyInfosTwo = AppJsonUtil.getArrayList(result,MineTakeAlreadyInfo.class);
                if (mineTakeAlreadyInfosTwo != null && mineTakeAlreadyInfosTwo.size() > 0) {
                    mAdapter.addDatas(mineTakeAlreadyInfosTwo);
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
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
    }

}
