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
import com.biaoyuan.transfernet.adapter.MinePackageAlreadyAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MinePackageAlreadyInfo;
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
 * 我的发送的包裹-已送达
 * Created by Administrator on 2017/5/11.
 */

public class MineSendAlreadyPackageFgt extends BaseFgt {

    //下拉刷新

    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private MinePackageAlreadyAdapter mAdapter;

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
                doHttp(RetrofitUtils.createApi(Mine.class).myParcelIsSentByType(Integer.parseInt(UserManger.pageSize),targetPage,5,UserManger.getBaseId()),1);
            }
        });

        //实例化布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mAdapter = new MinePackageAlreadyAdapter(R.layout.item_mine_send_package,new ArrayList<MinePackageAlreadyInfo>());
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
        //设置adapter
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                String packageId = String.valueOf(mAdapter.getItem(position).getPackage_id());
                bundle.putString("packageId",packageId);
                bundle.putString("key","fcbg");
                bundle.putString("key1","already");
                startActivity(MineSendPackageDetailAty.class,bundle);
            }
        });

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
                        doHttp(RetrofitUtils.createApi(Mine.class).myParcelIsSentByType(Integer.parseInt(UserManger.pageSize),targetPage,5,UserManger.getBaseId()),2);
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
        doHttp(RetrofitUtils.createApi(Mine.class).myParcelIsSentByType(Integer.parseInt(UserManger.pageSize),targetPage,5,UserManger.getBaseId()),1);
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
        doHttp(RetrofitUtils.createApi(Mine.class).myParcelIsSentByType(Integer.parseInt(UserManger.pageSize),targetPage,5,UserManger.getBaseId()),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        isLoading = false;
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<MinePackageAlreadyInfo> minePackageAlreadyInfos = AppJsonUtil.getArrayList(result,MinePackageAlreadyInfo.class);
                if (minePackageAlreadyInfos != null) {
                    mAdapter.setNewData(minePackageAlreadyInfos);
                    if (minePackageAlreadyInfos.size()<Integer.parseInt(UserManger.pageSize)){
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
                List<MinePackageAlreadyInfo> minePackageAlreadyInfosTwo = AppJsonUtil.getArrayList(result,MinePackageAlreadyInfo.class);
                if (minePackageAlreadyInfosTwo != null && minePackageAlreadyInfosTwo.size() > 0) {
                    mAdapter.addDatas(minePackageAlreadyInfosTwo);
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
