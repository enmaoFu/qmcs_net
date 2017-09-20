package com.biaoyuan.transfernet.adapter;

import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineTakeRejectItem;
import com.biaoyuan.transfernet.util.JPushSetAliasUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Title  :取件拒收
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineTakeRejectAdapter extends BaseQuickAdapter<MineTakeRejectItem, BaseViewHolder> {

    public MineTakeRejectAdapter(int layoutResId, List<MineTakeRejectItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineTakeRejectItem item, int position) {
        helper.setTextViewText(R.id.order_id, item.getOrder_no());
        helper.setTextViewText(R.id.order_weight_size, "物品信息：最长边≤" + item.getOrder_goods_size() + "cm   " + item.getOrder_goods_weight() + "kg");
        helper.setTextViewText(R.id.order_send_address, "寄件地址：" + item.getOrder_send_addr().replace("|", ""));
        if(item.getDelivery_update_time() == 0){
            helper.setViewVisibility(R.id.order_date, View.GONE);
        }else{
            helper.setViewVisibility(R.id.order_date, View.VISIBLE);
            helper.setTextViewText(R.id.order_date, DateTool.timesToStrTime(item.getDelivery_update_time() + "", "yyyy.MM.dd HH:mm") + " 取件");
        }
        Logger.v(DateTool.timesToStrTime(item.getDelivery_update_time() + "", "yyyy.MM.dd HH:mm"));
        switch (item.getExcep_rejection_reason()) {
            case 1:
                helper.setTextViewText(R.id.result, "违禁物品");
                break;
            case 2:
                helper.setTextViewText(R.id.result, "重量体积与实际不符");
                break;
            case 3:
                helper.setTextViewText(R.id.result, "非本平台可接收快件");
                break;
            case 4:
                helper.setTextViewText(R.id.result, "寄送地址无法送达");
                break;
            case 5:
                helper.setTextViewText(R.id.result, "其他原因");
                break;
        }
    }
}
