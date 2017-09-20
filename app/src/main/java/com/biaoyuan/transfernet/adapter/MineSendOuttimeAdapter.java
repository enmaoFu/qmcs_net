package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineSendOuttimeItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Title  :发出超时
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineSendOuttimeAdapter extends BaseQuickAdapter<MineSendOuttimeItem,BaseViewHolder> {


    public MineSendOuttimeAdapter(int layoutResId, List<MineSendOuttimeItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineSendOuttimeItem item, int position) {
        helper.setTextViewText(R.id.order_id,item.getOrder_tracking_code());
        helper.setTextViewText(R.id.order_weight_size,"物品信息：最长边≤" + item.getOrder_goods_size() + "cm   " + item.getOrder_goods_weight() + "kg");
        helper.setTextViewText(R.id.order_address,"寄件地址：" + item.getOrder_send_addr().replace("|",""));
        helper.setTextViewText(R.id.order_date, "取件时间：" + DateTool.timesToStrTime(item.getDelivery_update_time() + "", "yyyy.MM.dd HH:mm"));

        helper.setTextViewText(R.id.order_yq_date,"发出时间：" + DateTool.timesToStrTime(item.getExcep_record_time() + "", "yyyy.MM.dd HH:mm"));

        //获取取件时间后12小时
        long getDate_12 = item.getDelivery_update_time() + 60 * 60 * 12 * 1000;
        //发出时间
        long getExcepRecordTime = item.getExcep_record_time();
        if(getExcepRecordTime > getDate_12){
            try{
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String fromDate = simpleFormat.format(getExcepRecordTime);
                String toDate = simpleFormat.format(getDate_12);
                long from = simpleFormat.parse(fromDate).getTime();
                long to = simpleFormat.parse(toDate).getTime();
                int hours = (int) ((from - to)/(1000 * 60 * 60));
                helper.setTextViewText(R.id.order_cs_date,"(超时" + hours + "小时)");
                //Logger.v(DateTool.timesToStrTime(getDate_12 + "", "yyyy.MM.dd HH:mm") + "-------------------" + DateTool.timesToStrTime(getExcepRecordTime + "", "yyyy.MM.dd HH:mm") + "----------------0" + hours);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            helper.setTextViewText(R.id.order_cs_date,"()");
        }

    }
}
