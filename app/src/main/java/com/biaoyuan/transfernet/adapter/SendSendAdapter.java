package com.biaoyuan.transfernet.adapter;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.adapter.recyclerview.BaseSectionQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendSendHeadInfo;

import java.util.List;

/**
 * @author :enmaoFu
 * @title : 发件网点-》发布fragment recyclerview适配器
 * @create :2017/5/9
 */
public class SendSendAdapter extends BaseSectionQuickAdapter<SendSendHeadInfo, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SendSendAdapter(int layoutResId, int sectionHeadResId, List<SendSendHeadInfo> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    /**
     * 此方法设置分组头部
     * @param helper
     * @param item
     * @param position
     */
    @Override
    protected void convertHead(BaseViewHolder helper, SendSendHeadInfo item, int position) {
        helper.addOnClickListener(R.id.more);
        helper.setTextViewText(R.id.collect_address,item.getBasicName());
    }

    /**
     * 此方法设置分组item
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     * @param position
     */
    @Override
    protected void convert(BaseViewHolder helper, SendSendHeadInfo item, int position) {
        helper.setTextViewText(R.id.kj_number,item.t.getOrderTrackingCode());
        helper.setTextViewText(R.id.kj_date, "取件时间：" + DateTool.timesToStrTime(item.t.getDeliveryUpdateTime() + "", "yyyy.MM.dd-HH:mm:ss"));

        if (item.t.isLast()) {
            helper.setViewVisibility(R.id.img_shadow, View.VISIBLE);
            helper.setViewVisibility(R.id.v_divier, View.GONE);
            helper.setBackgroundDrawable(R.id.rl_item, mContext.getResources().getDrawable(R.drawable.shape_white_round4_bottom));
        } else {
            helper.setViewVisibility(R.id.img_shadow, View.GONE);
            helper.setViewVisibility(R.id.v_divier, View.VISIBLE);
            helper.setBackgroundColor(R.id.rl_item, mContext.getResources().getColor(R.color.white));
        }

    }

}
