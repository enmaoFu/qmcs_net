package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.MinePackageCollectAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MineCollectPackageInfo;
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
 * 我的待收包裹
 * Created by Administrator on 2017/5/11.
 */

public class MineNotReceivePackageAty extends BaseAty {

    //下拉刷新

    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private int targetPage = 1;

    private boolean isLoading = false;

    private MinePackageCollectAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_not_receive;
    }

    @Override
    public void initData() {

        initToolbar(mToolbar, "待收包裹");
        PtrInitHelper.initPtr(this, mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Mine.class).myPackageIsWaiting(Integer.parseInt(UserManger.pageSize),targetPage,UserManger.getBaseId()),1);
            }
        });

        //实例化布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mAdapter = new MinePackageCollectAdapter(R.layout.item_mine_not_receive_package,new ArrayList<MineCollectPackageInfo>());
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
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                String packageId = String.valueOf(mAdapter.getItem(position).getPackage_id());
                Bundle bundle = new Bundle();
                bundle.putString("packageId",packageId);
                bundle.putString("key","dsbg");
                bundle.putString("key1","dsbg");
                startActivity(MineSendPackageDetailAty.class,bundle);
            }
        });
        //设置没有数据的页面
        setEmptyView(mAdapter,null);
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
                        doHttp(RetrofitUtils.createApi(Mine.class).myPackageIsWaiting(Integer.parseInt(UserManger.pageSize),targetPage,UserManger.getBaseId()),2);
                    }
                });

            }
        }, mRecyclerView);

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).myPackageIsWaiting(Integer.parseInt(UserManger.pageSize),targetPage,UserManger.getBaseId()),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        isLoading = false;
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<MineCollectPackageInfo> mineCollectPackageInfoList = AppJsonUtil.getArrayList(result,MineCollectPackageInfo.class);
                if (mineCollectPackageInfoList != null) {
                    mAdapter.setNewData(mineCollectPackageInfoList);
                    if (mineCollectPackageInfoList.size()<Integer.parseInt(UserManger.pageSize)){
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
                List<MineCollectPackageInfo> mineCollectPackageInfoListTwo = AppJsonUtil.getArrayList(result,MineCollectPackageInfo.class);
                if (mineCollectPackageInfoListTwo != null && mineCollectPackageInfoListTwo.size() > 0) {
                    mAdapter.addDatas(mineCollectPackageInfoListTwo);
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
