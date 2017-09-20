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
import com.biaoyuan.transfernet.adapter.MineHaveSendFileAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MineHaveSendItem;
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
 * 签收快件
 * Created by Administrator on 2017/5/11.
 */

public class MineHaveSendFileAty extends BaseAty {

    //下拉刷新

    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private MineHaveSendFileAdapter mAdapter;

    private int targetPage = 1;

    //用来标记是否在加载
    private boolean isLoading = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_not_receive;
    }

    @Override
    public void initData() {

        initToolbar(mToolbar, "签收快件");
        PtrInitHelper.initPtr(this, mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Mine.class).mySignForExpress(Integer.parseInt(UserManger.pageSize),targetPage),1);
            }
        });

        //实例化布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mAdapter = new MineHaveSendFileAdapter(R.layout.item_mine_have_send_file,new ArrayList<MineHaveSendItem>());
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
                int orderId = mAdapter.getItem(position).getOrder_id();
                Bundle bundle = new Bundle();
                bundle.putString("orderId",String.valueOf(orderId));
                startActivity(MineHaveSendFileDetailsActivity.class,bundle);
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
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecyclerView==null){
                            return;
                        }
                        if (targetPage == 1) {
                            mAdapter.loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Mine.class).mySignForExpress(Integer.parseInt(UserManger.pageSize),targetPage),2);
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
        doHttp(RetrofitUtils.createApi(Mine.class).mySignForExpress(Integer.parseInt(UserManger.pageSize),targetPage),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        isLoading = false;
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<MineHaveSendItem> mineHaveSendItemList = AppJsonUtil.getArrayList(result,MineHaveSendItem.class);
                if (mineHaveSendItemList != null) {
                    mAdapter.setNewData(mineHaveSendItemList);
                    if (mineHaveSendItemList.size()<Integer.parseInt(UserManger.pageSize)){
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
                List<MineHaveSendItem> mineHaveSendItemListTweo = AppJsonUtil.getArrayList(result,MineHaveSendItem.class);
                if (mineHaveSendItemListTweo != null && mineHaveSendItemListTweo.size() > 0) {
                    mAdapter.addDatas(mineHaveSendItemListTweo);
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
