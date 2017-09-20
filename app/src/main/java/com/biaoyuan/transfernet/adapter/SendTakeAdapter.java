package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendTakeInfo;
import com.biaoyuan.transfernet.util.MyNumberFormat;

import java.util.List;

/**
 * @author :enmaoFu
 * @title : 发件网点-》取件fragment recyclerview适配器
 * @create :2017/5/9
 */
public class SendTakeAdapter extends BaseQuickAdapter<SendTakeInfo, BaseViewHolder> {

    public SendTakeAdapter(int layoutResId, List<SendTakeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendTakeInfo item, int position) {
        // helper.addOnClickListener(R.id.submit);
        helper.setTextViewText(R.id.tv_order_number, "订单编号:" + item.getOrderNo())
                .setTextViewText(R.id.tv_time, DateTool.timesToStrTime(item.getCreateTime() + "", "yyyy.MM.dd-HH:mm:ss"))
                .setTextViewText(R.id.tv_address, item.getSendAddress().replace("|", ""))
                .setTextViewText(R.id.tv_msg, "物品信息：" + item.getGoodsWeight() + "kg  最长边≤" + item.getGoodsSize() + "cm")
        ;

        String time = DateTool.getTimeType(item.getRequiredTime());
        if (time == null) {
            helper.setTextViewText(R.id.tv_take_time, DateTool.timesToStrTime(item.getRequiredTime() + "", "yyyy.MM.dd HH:mm") + "取件");
        } else {
            helper.setTextViewText(R.id.tv_take_time, time + DateTool.timesToStrTime(item.getRequiredTime() + "", "HH:mm") + "取件");
        }


        if (item.getPickupDistance() > 1000) {
            helper.setTextViewText(R.id.tv_distance, MyNumberFormat.m2(item.getPickupDistance() / 1000) + "km");
        } else {
            //helper.setTextViewText(R.id.tv_distance, item.getPickupDistance() + "km");
            helper.setTextViewText(R.id.tv_distance, MyNumberFormat.m2(item.getPickupDistance()) + "m");
        }


        if (item.getOrderStatus() == 1) {
            //未确认

            helper.setTextViewText(R.id.submit, "网点确认");

            helper.setTextViewText(R.id.tv_state, "等待网点确认").setTextViewTextColor(R.id.tv_state, mContext.getResources().getColor(R.color.colorAccent));

        } else {
            //代取件
            helper.setTextViewText(R.id.submit, "扫码取件");
            helper.setTextViewText(R.id.tv_state, item.getStaffPhone() + "已确认").setTextViewTextColor(R.id.tv_state, mContext.getResources().getColor(R.color.font_gray));

        }


    }

}
