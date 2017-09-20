package com.biaoyuan.transfernet.ui.send.SendTakeFgt;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.transformer.BGAPageTransformer;
import com.and.yzy.frame.transformer.TransitionEffect;
import com.and.yzy.frame.util.DensityUtils;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.SendTakeAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.AdInfo;
import com.biaoyuan.transfernet.domain.SendTakeInfo;
import com.biaoyuan.transfernet.http.Image;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.MainAty;
import com.biaoyuan.transfernet.ui.WebViewActivity;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.biaoyuan.transfernet.config.UserManger.pageSize;

/**
 * @author :enmaoFu
 * @title :viewpager取件页面
 * @create :2017/5/9
 */
public class SendTakeFgt extends BaseFgt {

    @Bind(R.id.rv_data)
    RecyclerView mTekeRecyclerview;

    //下拉刷新
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;
    @Bind(R.id.convenientBanner)
    ConvenientBanner mConvenientBanner;

    //布局管理器
    private RecyclerView.LayoutManager mLayoutManager;
    //适配器
    private SendTakeAdapter mSendTakeAdapter;

    private int targetPage = 1;

    //用来标记是否在加载
    private boolean isLoading = false;

    private List<AdInfo> mAdInfos;

    @Override
    public int getLayoutId() {
        return R.layout.fgt_take;
    }

    @Override
    public void initData() {


        PtrInitHelper.initPtr(getActivity(), mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Send.class).takeOrder(UserManger.getBaseId(), pageSize, targetPage + ""), 1);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mTekeRecyclerview, header);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mSendTakeAdapter = new SendTakeAdapter(R.layout.item_viewpager_send_take_main, new ArrayList<SendTakeInfo>());
        //设置布局管理器
        mTekeRecyclerview.setLayoutManager(mLayoutManager);
        //设置间隔样式
        /*mTekeRecyclerview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor(getResources().getString(R.string.parseColor)))
                        .sizeResId(R.dimen.size_0_5p)
                        .build());*/
        //大小不受适配器影响
        mTekeRecyclerview.setHasFixedSize(true);
        //设置加载动画类型
        //mSendTakeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mTekeRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //设置item点击事件
        mTekeRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int orderId = mSendTakeAdapter.getItem(position).getOrderID();
                int orderStatus = mSendTakeAdapter.getItem(position).getOrderStatus();
                String orderNo = mSendTakeAdapter.getItem(position).getOrderNo();
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", orderId);
                bundle.putString("orderNo", orderNo);
                bundle.putInt("orderStatus", orderStatus);
                startActivity(SendTakeItemDetailsActivity.class, bundle);
            }
        });
        //设置item 子控件点击事件
        /*mTekeRecyclerview.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.submit:
                        startActivity(SendTakeItemDetailsActivity.class, null);
                        break;
                }
            }
        });*/
        //设置没有数据的页面
        setEmptyView(mSendTakeAdapter, null);
        //设置adapter
        mTekeRecyclerview.setAdapter(mSendTakeAdapter);

        //上拉加载更多
        mSendTakeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mTekeRecyclerview != null)
                    mTekeRecyclerview.post(new Runnable() {
                        @Override
                        public void run() {

                            if (targetPage == 1) {
                                mSendTakeAdapter.loadMoreEnd();
                                return;
                            }
                            doHttp(RetrofitUtils.createApi(Send.class).takeOrder(UserManger.getBaseId(), pageSize, targetPage + ""), 2);
                        }
                    });

            }
        }, mTekeRecyclerview);


        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        mConvenientBanner.setPageTransformer(BGAPageTransformer.getPageTransformer(TransitionEffect.Accordion));


    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (mConvenientBanner != null && mAdInfos != null && mAdInfos.size() > 1) {
            mConvenientBanner.stopTurning();
        }
        isLoading = false;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mConvenientBanner != null && mAdInfos != null && mAdInfos.size() > 1) {
            mConvenientBanner.startTurning(5000);
        }
        //如果选中了第一个
        if (MainAty.radioButtons.get(0).isChecked() && !isLoading) {
            isLoading = true;
            //刷新界面
            targetPage = 1;
            Logger.v("baseId==" + UserManger.getBaseId());
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doHttp(RetrofitUtils.createApi(Send.class).takeOrder(UserManger.getBaseId(), pageSize, targetPage + ""), 1);
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
    public void requestData() {

        Logger.v("baseId==" + UserManger.getBaseId());
        doHttp(RetrofitUtils.createApi(Send.class).takeOrder(UserManger.getBaseId(), pageSize, targetPage + ""), 1);

        //请求广告页
        doHttp(RetrofitUtils.createApi(Image.class).listAdvertisement("1", "网点端首页"), 3);
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mSendTakeAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mSendTakeAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        isLoading = false;
        switch (what) {
            case 1:

                mPtrFrame.refreshComplete();
                mSendTakeAdapter.removeAll();
                List<SendTakeInfo> sendTakeInfos = AppJsonUtil.getArrayList(result, "orderList2", SendTakeInfo.class);
                if (sendTakeInfos != null) {
                    mSendTakeAdapter.setNewData(sendTakeInfos);
                    if (sendTakeInfos.size() < Integer.parseInt(pageSize)) {
                        mSendTakeAdapter.loadMoreEnd();
                    }
                } else {
                    mSendTakeAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
            case 2:

                //加载更多
                List<SendTakeInfo> sends = AppJsonUtil.getArrayList(result, "orderList2", SendTakeInfo.class);
                if (sends != null && sends.size() > 0) {
                    mSendTakeAdapter.addDatas(sends);
                    mSendTakeAdapter.loadMoreComplete();
                } else {
                    mSendTakeAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
            case 3:

                mAdInfos = AppJsonUtil.getArrayList(result, "advertisementList", AdInfo.class);


                if (mAdInfos == null && mAdInfos.size() == 0) {
                    return;
                }

                //设置尺寸
                String[] size = mAdInfos.get(0).getAdPicSize().split("\\*");

                int width = Integer.parseInt(size[0]);
                int height = Integer.parseInt(size[1]);
                //得到屏幕的宽度
                int screenWidth = DensityUtils.getScreenWidth(getActivity());
                int bannerHeight = screenWidth / (width / height);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mConvenientBanner.getLayoutParams();

                layoutParams.height = bannerHeight;


                mConvenientBanner.setLayoutParams(layoutParams);


                mConvenientBanner.setPages(
                        new CBViewHolderCreator<LocalImageHolderView>() {
                            @Override
                            public LocalImageHolderView createHolder() {
                                return new LocalImageHolderView();
                            }
                        }, mAdInfos).setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        AdInfo adInfo = mAdInfos.get(position);

                        if (!TextUtils.isEmpty(adInfo.getAdUrl())) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", WebViewActivity.TYPE_OTHER);
                            bundle.putString("url", adInfo.getAdUrl());
                            bundle.putString("title", adInfo.getAdDiscription());
                            startActivity(WebViewActivity.class, bundle);

                        }

                    }
                });

                if (mAdInfos.size() > 1) {
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    mConvenientBanner.setPageIndicator(new int[]{R.drawable.banner_unselected, R.drawable.banner_selected})
                            //设置指示器的方向
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                    mConvenientBanner.startTurning(5000);
                } else {
                    mConvenientBanner.setCanLoop(false);
                }
                break;

        }
    }

    class LocalImageHolderView implements Holder<AdInfo> {
        private SimpleDraweeView imageView;

        @Override
        public View createView(Context context) {
            imageView = new SimpleDraweeView(context);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, AdInfo data) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getResources());

            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setPlaceholderImage(R.drawable.placeholder_pic)
                    .setFailureImage(R.drawable.placeholder_pic)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setFailureImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            imageView.setHierarchy(hierarchy);
            imageView.setImageURI(Uri.parse(data.getAdPicUrl()));
        }
    }

}
