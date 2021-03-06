package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineTakeAlreadyInfo;
import com.biaoyuan.transfernet.domain.MineTakeWaitInfo;
import com.biaoyuan.transfernet.util.MyNumberFormat;

import java.util.List;

/**
 * Title  :取消快件
 * Create : 2017/5/12
 * Author ：chen
 */

public class MineTakeCancleAdapter extends BaseQuickAdapter<MineTakeAlreadyInfo,BaseViewHolder> {


    public MineTakeCancleAdapter(int layoutResId, List<MineTakeAlreadyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineTakeAlreadyInfo item, int position) {
        helper.setTextViewText(R.id.tv_order_number,"订单编号:"+item.getOrder_no());
        helper.setTextViewText(R.id.tv_time, DateTool.timesToStrTime(item.getOrder_time() + "", "yyyy.MM.dd-HH:mm:ss"));
        helper.setTextViewText(R.id.tv_msg,"物品信息："+ item.getOrder_goods_weight() + "kg   " + item.getOrder_goods_size() + "cm");
        helper.setTextViewText(R.id.tv_address,item.getOrder_send_addr().replace("|",""));
        String time = DateTool.getTimeType(item.getOrder_required_time());
        if (time == null) {
            helper.setTextViewText(R.id.tv_take_time, DateTool.timesToStrTime(item.getOrder_required_time() + "", "yyyy.MM.dd HH:mm") + "取件");
        } else {
            helper.setTextViewText(R.id.tv_take_time, time + DateTool.timesToStrTime(item.getOrder_required_time() + "", "HH:mm") + "取件");
        }

        if (item.getOrder_goods_distance() > 1000) {
            helper.setTextViewText(R.id.tv_distance, MyNumberFormat.m2(item.getOrder_goods_distance() / 1000) + "km");
        } else {
            //helper.setTextViewText(R.id.tv_distance, item.getPickupDistance() + "km");
            helper.setTextViewText(R.id.tv_distance, MyNumberFormat.m2(item.getOrder_goods_distance()) + "m");
        }

    }
}
