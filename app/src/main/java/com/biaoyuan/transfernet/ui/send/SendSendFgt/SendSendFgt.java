package com.biaoyuan.transfernet.ui.send.SendSendFgt;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemChildClickListener;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.SendSendAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.SendSendHeadInfo;
import com.biaoyuan.transfernet.domain.SendSendHeadNewInfo;
import com.biaoyuan.transfernet.domain.SendSendItemInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.MainAty;
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
 * @author :enmaoFu
 * @title :viewpager发布页面
 * @create :2017/5/9
 */
public class SendSendFgt extends BaseFgt {

    @Bind(R.id.rv_data)
    RecyclerView mSendRecyclerview;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;


    private RecyclerView.LayoutManager mLayoutManager;
    private SendSendAdapter mSendSendAdapter;
    //页码
    private int targetPage = 1;
    //获取每次请求的页数
    private int pageSize = Integer.parseInt(UserManger.pageSize);

    //用来标记是否在加载
    private boolean isLoading = false;

    @Override
    public int getLayoutId() {
        return R.layout.fgt_send;
    }

    @Override
    public void initData() {

        PtrInitHelper.initPtr(getActivity(), mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Send.class).getIndexRelease(UserManger.getBaseId(), pageSize, targetPage), 1);

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mSendRecyclerview, header);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mSendSendAdapter = new SendSendAdapter(R.layout.item_viewpager_send_send_main_listview, R.layout.item_viewpager_send_send_main,
                new ArrayList<SendSendHeadInfo>());
        //设置布局管理器
        mSendRecyclerview.setLayoutManager(mLayoutManager);
        //设置间隔样式
        /*mTekeRecyclerview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor(getResources().getString(R.string.parseColor)))
                        .sizeResId(R.dimen.size_0_5p)
                        .build());*/
        //大小不受适配器影响
        mSendRecyclerview.setHasFixedSize(true);
        //设置加载动画类型
        //mSendSendAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mSendRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //设置item点击事件
        /*mSendRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(SendSendArriveAddressActivity.class,null);
            }
        });*/
        //设置item 子控件点击事件
        mSendRecyclerview.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.more:
                        String basicCode = mSendSendAdapter.getItem(position).getBasicCode();
                        String BasicName = mSendSendAdapter.getItem(position).getBasicName();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "SendSendFgt");
                        bundle.putString("basicCode", basicCode);
                        bundle.putString("BasicName", BasicName);
                        startActivity(SendSendArriveAddressActivity.class, bundle);
                        break;
                }
            }
        });
        //设置没有数据的页面
        setEmptyView(mSendSendAdapter, null);
        //设置adapter
        mSendRecyclerview.setAdapter(mSendSendAdapter);

        //上拉加载更多
        mSendSendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mSendRecyclerview != null)
                    mSendRecyclerview.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mSendRecyclerview == null) {
                                return;
                            }
                            //当数据没有填充完整个页面的时候，不去加载更多
                            if (targetPage == 1) {
                                mSendSendAdapter.loadMoreEnd();
                                return;
                            }
                            doHttp(RetrofitUtils.createApi(Send.class).getIndexRelease(UserManger.getBaseId(), pageSize, targetPage), 2);
                        }
                    });

            }
        }, mSendRecyclerview);


    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();

        isLoading = false;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();


        if (MainAty.radioButtons.get(0).isChecked() && !isLoading) {
            isLoading = true;
            //刷新界面
            targetPage = 1;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doHttp(RetrofitUtils.createApi(Send.class).getIndexRelease(UserManger.getBaseId(), pageSize, targetPage), 1);
                }
            }, 500);

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoadingContentDialog();
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        doHttp(RetrofitUtils.createApi(Send.class).getIndexRelease(UserManger.getBaseId(), pageSize, targetPage), 1);
    }


    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mSendSendAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mSendSendAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        isLoading = false;
        switch (what) {
            case 1:

                mPtrFrame.refreshComplete();

                mSendSendAdapter.removeAll();

                /**
                 * 设置recyclerview数据，是有分组的
                 * 先使用新建的实体类，接收对返回JSON的解析，然后再将新实体类的数据加入到分组必须的实体类集合
                 */
                //使用新建的实体类，接收对返回JSON的解析
                List<SendSendHeadNewInfo> SendSendHeadNewInfos = AppJsonUtil.getArrayList(result, SendSendHeadNewInfo.class);

                //分组必须的实体类集合
                List<SendSendHeadInfo> sendSendHeadInfos = new ArrayList<>();

                //遍历分组
                for (SendSendHeadNewInfo sshni : SendSendHeadNewInfos) {
                    //创建分组头部实例
                    SendSendHeadInfo sendSendHeadInfo = new SendSendHeadInfo(true, "");
                    //设置分组头部数据
                    sendSendHeadInfo.setBasicCode(sshni.getBasicCode());
                    sendSendHeadInfo.setBasicName(sshni.getBasicName());
                    //添加分组头部
                    sendSendHeadInfos.add(sendSendHeadInfo);

                    //得到每个分组下面的Item
                    List<SendSendHeadNewInfo.ChildListBean> scs = sshni.getChildList();
                    //遍历每个分组下面的Item
                    for (SendSendHeadNewInfo.ChildListBean sclb : scs) {
                        //创建Item实例
                        SendSendItemInfo sendSendItemInfo = new SendSendItemInfo();
                        //设置Item数据
                        sendSendItemInfo.setDeliveryUpdateTime(sclb.getDeliveryUpdateTime());
                        sendSendItemInfo.setOrderTrackingCode(sclb.getOrderTrackingCode());
                        sendSendItemInfo.setLast(false);
                        //添加Item到分组
                        SendSendHeadInfo sendSendHeadInfoTwo = new SendSendHeadInfo(sendSendItemInfo);
                        sendSendHeadInfos.add(sendSendHeadInfoTwo);
                    }
                    /**
                     * 对每个分组最后一个Item进行设置，以便在adapter里判断显示逻辑
                     * 注意：
                     * 因为此处是分组形式，所以总个数 = 头部 + Item
                     * 所以要拿到最后一个Item不能直接去拿Item的个数，而是要去拿这个分组的总个数
                     */
                    sendSendHeadInfos.get(sendSendHeadInfos.size() - 1).t.setLast(true);
                }

                if (sendSendHeadInfos != null) {
                    mSendSendAdapter.setNewData(sendSendHeadInfos);
                    if (sendSendHeadInfos.size() < pageSize) {
                        mSendSendAdapter.loadMoreEnd();
                    }

                } else {
                    mSendSendAdapter.loadMoreEnd();
                }

                //增加页码
                targetPage++;

                break;
            case 2:

                //加载更多

                /**
                 * 设置recyclerview数据，是有分组的
                 * 先使用新建的实体类，接收对返回JSON的解析，然后再将新实体类的数据加入到分组必须的实体类集合
                 */
                //使用新建的实体类，接收对返回JSON的解析
                List<SendSendHeadNewInfo> SendSendHeadNewInfoTwo = AppJsonUtil.getArrayList(result, SendSendHeadNewInfo.class);

                //分组必须的实体类集合
                List<SendSendHeadInfo> sendSendHeadInfosTwo = new ArrayList<>();

                //遍历分组
                for (SendSendHeadNewInfo sshni : SendSendHeadNewInfoTwo) {
                    //创建分组头部实例
                    SendSendHeadInfo sendSendHeadInfoTwo = new SendSendHeadInfo(true, "");
                    //设置分组头部数据
                    sendSendHeadInfoTwo.setBasicCode(sshni.getBasicCode());
                    sendSendHeadInfoTwo.setBasicName(sshni.getBasicName());
                    //添加分组头部
                    sendSendHeadInfosTwo.add(sendSendHeadInfoTwo);

                    //得到每个分组下面的Item
                    List<SendSendHeadNewInfo.ChildListBean> scs = sshni.getChildList();
                    //遍历每个分组下面的Item
                    for (SendSendHeadNewInfo.ChildListBean sclb : scs) {
                        //创建Item实例
                        SendSendItemInfo sendSendItemInfoTwo = new SendSendItemInfo();
                        //设置Item数据
                        sendSendItemInfoTwo.setDeliveryUpdateTime(sclb.getDeliveryUpdateTime());
                        sendSendItemInfoTwo.setOrderTrackingCode(sclb.getOrderTrackingCode());
                        sendSendItemInfoTwo.setLast(false);
                        //添加Item到分组
                        SendSendHeadInfo sendSendHeadInfoItemTwo = new SendSendHeadInfo(sendSendItemInfoTwo);
                        sendSendHeadInfosTwo.add(sendSendHeadInfoItemTwo);
                    }
                    //对每个分组最后一个Item进行设置，以便在adapter里判断显示逻辑
                    sendSendHeadInfosTwo.get(sendSendHeadInfosTwo.size() - 1).t.setLast(true);
                }

                //判断是否有新数据
                if (sendSendHeadInfosTwo != null && sendSendHeadInfosTwo.size() > 0) {
                    mSendSendAdapter.addDatas(sendSendHeadInfosTwo);
                    //加载完成
                    mSendSendAdapter.loadMoreComplete();
                } else {
                    //加载结束，没有新数据
                    mSendSendAdapter.loadMoreEnd();
                }

                //增加页码
                targetPage++;

                break;
        }
    }
}
