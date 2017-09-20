package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.MineLoseWormAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MineLoseWormItem;
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
 * 丢失损坏
 */
public class MineLoseWormFgt extends BaseFgt {
    @Bind(R.id.rv_data)
    RecyclerView mRvData;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private MineLoseWormAdapter mAdapter;

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
                doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,5),1);
            }
        });

        final LinearLayoutManager mRecyclerViewlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new MineLoseWormAdapter(R.layout.item_mine_lose_worm, new ArrayList<MineLoseWormItem>());

        //设置布局管理器
        mRvData.setLayoutManager(mRecyclerViewlayoutManager);
        mRvData.setHasFixedSize(true);
        //mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRvData.setItemAnimator(new DefaultItemAnimator());
        ///设置item点击事件
        mRvData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mAdapter.getItem(position).getExcep_type() == 5){
                    //丢失
                    int excepId = mAdapter.getItem(position).getExcep_id();
                    int excepType = mAdapter.getItem(position).getExcep_type();
                    String orderNo = mAdapter.getItem(position).getOrder_no();
                    String orderCode = mAdapter.getItem(position).getOrder_tracking_code();
                    int orderType = mAdapter.getItem(position).getOrder_goods_type();
                    TextView date = (TextView) view.findViewById(R.id.date);
                    String ysDate = date.getText().toString().trim();
                    Bundle bundle = new Bundle();
                    bundle.putString("loseAndDamage","lose");
                    bundle.putString("excepId",String.valueOf(excepId));
                    bundle.putInt("excepType",excepType);
                    bundle.putInt("orderType",orderType);
                    bundle.putString("orderCode",orderCode);
                    bundle.putString("ysDate",ysDate);
                    bundle.putString("orderNo",orderNo);
                    startActivity(MineLoseWormDetailsActivity.class,bundle);
                }else{
                    //损坏
                    int excepId = mAdapter.getItem(position).getExcep_id();
                    int excepType = mAdapter.getItem(position).getExcep_type();
                    String orderNo = mAdapter.getItem(position).getOrder_no();
                    int orderType = mAdapter.getItem(position).getOrder_goods_type();
                    Bundle bundle = new Bundle();
                    String orderCode = mAdapter.getItem(position).getOrder_tracking_code();
                    TextView date = (TextView) view.findViewById(R.id.date);
                    String ysDate = date.getText().toString().trim();
                    bundle.putString("excepId",String.valueOf(excepId));
                    bundle.putInt("excepType",excepType);
                    bundle.putString("orderCode",orderCode);
                    bundle.putInt("orderType",orderType);
                    bundle.putString("ysDate",ysDate);
                    bundle.putString("orderNo",orderNo);
                    startActivity(MineLoseWormDamageDetailsActivity.class,bundle);
                }
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
                            mAdapter.loadMoreComplete();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,5),2);
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
        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,5),1);
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
        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpress(Integer.parseInt(UserManger.pageSize),targetPage,5),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<MineLoseWormItem> mineLoseWormItemList = AppJsonUtil.getArrayList(result,MineLoseWormItem.class);
                if (mineLoseWormItemList != null) {
                    mAdapter.setNewData(mineLoseWormItemList);
                    if (mineLoseWormItemList.size()<Integer.parseInt(UserManger.pageSize)){
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
                List<MineLoseWormItem> mineLoseWormItemListTweo = AppJsonUtil.getArrayList(result,MineLoseWormItem.class);
                if (mineLoseWormItemListTweo != null && mineLoseWormItemListTweo.size() > 0) {
                    mAdapter.addDatas(mineLoseWormItemListTweo);
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
