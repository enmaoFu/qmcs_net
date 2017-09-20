package com.biaoyuan.transfernet.ui.mine;

import android.graphics.Color;
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
import com.biaoyuan.transfernet.adapter.MineCustomerEvaluateAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MineCustomerEvaluateInfo;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  :我的客户评价列表页面
 * @create :2017/5/22
 * @author :enmaoFu
 */
public class MineCustomerEvaluateListFgt extends BaseFgt{

    @Bind(R.id.rv_data)
    RecyclerView mRvData;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private MineCustomerEvaluateAdapter mineCustomerEvaluateAdapter;

    private int targetPage = 1;

    //用来标记是否在加载
    private boolean isLoading = false;

    int type = 0;
    @Override
    public int getLayoutId() {
        return R.layout.fgt_mine_customer_evaluate_list;
    }

    @Override
    public void initData() {

        PtrInitHelper.initPtr(getActivity(), mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Mine.class).viewComment(UserManger.pageSize,String.valueOf(targetPage),UserManger.getBaseId(),String.valueOf(type)),1);
            }
        });

        //实例化布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mineCustomerEvaluateAdapter = new MineCustomerEvaluateAdapter(R.layout.item_mine_customer_evaluate, new ArrayList<MineCustomerEvaluateInfo>());
        //设置布局管理器
        mRvData.setLayoutManager(layoutManager);
        //设置间隔样式
        /*mRvData.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor(getResources().getString(R.string.parseColor)))
                        .sizeResId(R.dimen.size_0_5p)
                        .build());
        //大小不受适配器影响*/
        mRvData.setHasFixedSize(true);
        //设置加载动画类型
  //      mineCustomerEvaluateAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mRvData.setItemAnimator(new DefaultItemAnimator());
        //设置item点击事件
        mRvData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int ocommentId = mineCustomerEvaluateAdapter.getItem(position).getOcommentId();
                long tel = mineCustomerEvaluateAdapter.getItem(position).getUserTelphone();
                long time = mineCustomerEvaluateAdapter.getItem(position).getOcommentCreatTime();
                String context = mineCustomerEvaluateAdapter.getItem(position).getOcommentContent();
                int score = mineCustomerEvaluateAdapter.getItem(position).getOcommentScore();
                Bundle bundle = new Bundle();
                bundle.putString("ocommentId",String.valueOf(ocommentId));
               bundle.putLong("tel",tel);
                bundle.putLong("time",time);
                bundle.putString("context",context);
                bundle.putInt("score",score);
                startActivity(MineCustomerEvaluateDetailsActivity.class,bundle);
            }
        });
        //设置没有数据的页面
        setEmptyView(mineCustomerEvaluateAdapter,null);
        //设置adapter
        mRvData.setAdapter(mineCustomerEvaluateAdapter);

        //上拉加载更多
        mineCustomerEvaluateAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRvData.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mRvData==null){
                            return;
                        }
                        if (targetPage == 1) {
                            mineCustomerEvaluateAdapter.loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Mine.class).viewComment(UserManger.pageSize,String.valueOf(targetPage),UserManger.getBaseId(),String.valueOf(type)),2);
                    }
                });

            }
        }, mRvData);

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        //如果选中了第一个
        isLoading = true;
        //刷新界面
        targetPage = 1;
        doHttp(RetrofitUtils.createApi(Mine.class).viewComment(UserManger.pageSize,String.valueOf(targetPage),UserManger.getBaseId(),String.valueOf(type)),1);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        type=getArguments().getInt("type",0);

        super.onActivityCreated(savedInstanceState);
        showLoadingContentDialog();
    }

    @Override
    public void requestData() {
        doHttp(RetrofitUtils.createApi(Mine.class).viewComment(UserManger.pageSize,String.valueOf(targetPage),UserManger.getBaseId(),String.valueOf(type)),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                mPtrFrame.refreshComplete();
                mineCustomerEvaluateAdapter.removeAll();
                List<MineCustomerEvaluateInfo> mineCustomerEvaluateInfoList = AppJsonUtil.getArrayList(result,"commentList",MineCustomerEvaluateInfo.class);
                if (mineCustomerEvaluateInfoList != null) {
                    mineCustomerEvaluateAdapter.setNewData(mineCustomerEvaluateInfoList);
                    if (mineCustomerEvaluateInfoList.size()<Integer.parseInt(UserManger.pageSize)){
                        mineCustomerEvaluateAdapter.loadMoreEnd();
                    }
                }else {
                    mineCustomerEvaluateAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
            case 2:

                //加载更多
                List<MineCustomerEvaluateInfo> mineCustomerEvaluateInfoListTwo = AppJsonUtil.getArrayList(result,"commentList",MineCustomerEvaluateInfo.class);
                if (mineCustomerEvaluateInfoListTwo != null && mineCustomerEvaluateInfoListTwo.size() > 0) {
                    mineCustomerEvaluateAdapter.addDatas(mineCustomerEvaluateInfoListTwo);
                    mineCustomerEvaluateAdapter.loadMoreComplete();
                } else {
                    mineCustomerEvaluateAdapter.loadMoreEnd();
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
            mineCustomerEvaluateAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mineCustomerEvaluateAdapter.loadMoreComplete();
        }
    }

}
