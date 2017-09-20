package com.biaoyuan.transfernet.ui.send;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.callback.ItemDragAndSwipeCallback;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemSwipeListener;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.Effectstype;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.SendMessageAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.NetInfo;
import com.biaoyuan.transfernet.domain.SendMessageInfo;
import com.biaoyuan.transfernet.domain.SendTakeInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.mine.MineSendOuttimeDetailsActivity;
import com.biaoyuan.transfernet.ui.mine.MineTakeOuttimeDetailsActivity;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendTakeItemDetailsActivity;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 消息中心页面
 * Create : 2017/5/24
 * Author ：enmaoFu
 */
public class SendMessageActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.ptr_frame)
    PtrFrameLayout ptrFrameLayout;

    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;
    private SendMessageAdapter indexMessageAdapter;

    //页码
    private int targetPage = 1;
    //获取每次请求的页数
    private String pageSize = UserManger.pageSize;

    private String messageId;
    private String contentNewOrder;
    private int orderStatus;

    @Override
    public int getLayoutId() {
        return R.layout.activity_index_message;
    }

    @Override
    public void initData() {

        initToolbar(mToolbar,"消息");

        PtrInitHelper.initPtr(this, ptrFrameLayout);

        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                //indexMessageAdapter.loadMoreComplete();
                doHttp(RetrofitUtils.createApi(Send.class).staMessage(pageSize,targetPage + ""),1);
            }
        });

        //实例化布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        indexMessageAdapter = new SendMessageAdapter(R.layout.item_index_message, new ArrayList<SendMessageInfo>());
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
        //indexMessageAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置item点击事件
        /*mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });*/

        // 开启滑动删除
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(indexMessageAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        indexMessageAdapter.enableSwipeItem();
        indexMessageAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                messageId = String.valueOf(indexMessageAdapter.getItem(pos).getMessageId());
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Send.class).deleteMessage(messageId),3);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });

        /*mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = indexMessageAdapter.getItem(position).getMessageType();
                String content = indexMessageAdapter.getItem(position).getMessageContent();
                Bundle bundle = new Bundle();
                switch (type){
                    case 2:
                        //新订单
                        contentNewOrder = content.substring(content.indexOf("【") + 1, content.lastIndexOf("】"));
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Send.class).queryOrderByOrderNumber(contentNewOrder),4);
                        break;
                    case 3:
                        //取件超时
                        String takeOrderOverTime = content.substring(content.indexOf("【") + 1, content.lastIndexOf("】"));
                        Log.v("print",takeOrderOverTime + "跳到取件超时详情");
                        bundle.putInt("excepType",3);
                        bundle.putString("orderNo",takeOrderOverTime);
                        startActivity(MineTakeOuttimeDetailsActivity.class,bundle);
                        break;
                    case 4:
                        //发件超时
                        String sendOrderOverTime = content.substring(content.indexOf("【") + 1, content.lastIndexOf("】"));
                        Log.v("print",sendOrderOverTime + "跳到发件超时详情");
                        bundle.putInt("excepType",4);
                        bundle.putString("orderNo",sendOrderOverTime);
                        startActivity(MineSendOuttimeDetailsActivity.class,bundle);
                        break;
                }
            }
        });*/

        //设置空数据页
        setEmptyView(indexMessageAdapter,null);
        //设置adapter
        mRecyclerView.setAdapter(indexMessageAdapter);

        //上拉加载更多
        indexMessageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        doHttp(RetrofitUtils.createApi(Send.class).staMessage(pageSize,targetPage + ""),2);
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
        doHttp(RetrofitUtils.createApi(Send.class).staMessage(pageSize,targetPage + ""),1);
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        if (ptrFrameLayout != null) {
            ptrFrameLayout.refreshComplete();
            indexMessageAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (ptrFrameLayout != null) {
            ptrFrameLayout.refreshComplete();
            indexMessageAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                ptrFrameLayout.refreshComplete();
                indexMessageAdapter.removeAll();
                List<SendMessageInfo> sendMessageInfos = AppJsonUtil.getArrayList(result,"staMessageeList",SendMessageInfo.class);
                if(sendMessageInfos != null){
                    indexMessageAdapter.setNewData(sendMessageInfos);
                }
                targetPage++;

                break;
            case 2:

                //加载更多
                List<SendMessageInfo> sendMessageInfosTwo = AppJsonUtil.getArrayList(result,"staMessageeList",SendMessageInfo.class);
                //判断是否有新数据
                if(sendMessageInfosTwo != null && sendMessageInfosTwo.size() > 0){
                    indexMessageAdapter.addDatas(sendMessageInfosTwo);
                    //加载完成
                    indexMessageAdapter.loadMoreComplete();
                }else{
                    //加载结束，没有新数据
                    indexMessageAdapter.loadMoreEnd();
                }

                //增加页码
                targetPage++;

                break;
            case 3:
                showToast("删除成功");
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Send.class).staMessage(pageSize,targetPage + ""),1);
                break;
            case 4:

                String getOrderStatus = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_status")).toString();
                String getOrderId = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_id")).toString();

                Bundle bundle = new Bundle();
                //Log.v("print",contentNewOrder + "跳到新订单详情" + "------------" + getOrderStatus + ".................." + getOrderId);
                bundle.putString("orderNo",contentNewOrder);
                if(getOrderStatus.equals("1")){
                    bundle.putInt("orderStatus",1);
                }else if(getOrderStatus.equals("2")){
                    bundle.putInt("orderStatus",2);
                }
                bundle.putInt("orderIdStr",Integer.parseInt(getOrderId));
                startActivity(SendTakeItemDetailsActivity.class,bundle);
                break;
        }
    }
}
