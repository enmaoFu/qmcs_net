package com.biaoyuan.transfernet.adapter;

import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineTakeOuttimeItem;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Title  :取件超时
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineTakeOuttimeAdapter extends BaseQuickAdapter<MineTakeOuttimeItem,BaseViewHolder> {


    public MineTakeOuttimeAdapter(int layoutResId, List<MineTakeOuttimeItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineTakeOuttimeItem item, int position) {
        helper.setTextViewText(R.id.order_id,item.getOrder_no());
        helper.setTextViewText(R.id.order_weight_size,"物品信息：最长边≤" + item.getOrder_goods_size() + "cm   " + item.getOrder_goods_weight() + "kg");
        helper.setTextViewText(R.id.order_address,"寄件地址：" + item.getOrder_send_addr().replace("|",""));
        helper.setTextViewText(R.id.order_date, DateTool.timesToStrTime(item.getDelivery_update_time() + "", "yyyy.MM.dd HH:mm") + " 取件");
        helper.setTextViewText(R.id.order_yq_date,"要求时间：" + DateTool.timesToStrTime(item.getOrder_required_time() + "", "yyyy.MM.dd HH:mm"));
        long getDeliveryUpdateTime = item.getDelivery_update_time();
        long getOrderRequiredTime = item.getOrder_required_time();

        /*//得到要求取件时间一小时后的时间
        long oneGetOrderRequiredTime = getOrderRequiredTime + 60 * 60 * 1 * 1000;

        //当实际取件时间大于要求取件时间的后一小时时，取件超时
        if(getDeliveryUpdateTime > oneGetOrderRequiredTime){
            try {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String sjDate = simpleFormat.format(item.getDelivery_update_time());
                String yqDate = simpleFormat.format(item.getOrder_required_time());
                long sjDateNew = simpleFormat.parse(sjDate).getTime();
                long yqDateNew = simpleFormat.parse(yqDate).getTime();
                int hours = (int) (sjDateNew - yqDateNew);
                if(hours < 1){
                    helper.setViewVisibility(R.id.order_cs_date, View.VISIBLE);
                    helper.setTextViewText(R.id.order_cs_date,"(超时1小时)");
                }else{
                    helper.setViewVisibility(R.id.order_cs_date, View.VISIBLE);
                    helper.setTextViewText(R.id.order_cs_date,"(超时" + hours + "小时)");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            helper.setViewVisibility(R.id.order_cs_date, View.GONE);
        }*/

        if(getDeliveryUpdateTime > getOrderRequiredTime){
            try{
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String fromDate = simpleFormat.format(getDeliveryUpdateTime);
                String toDate = simpleFormat.format(getOrderRequiredTime);
                long from = simpleFormat.parse(fromDate).getTime();
                long to = simpleFormat.parse(toDate).getTime();
                int hours = (int) ((from - to)/(1000 * 60 * 60));
                helper.setViewVisibility(R.id.order_cs_date, View.VISIBLE);
                helper.setTextViewText(R.id.order_cs_date,"(超时" + hours + "小时)");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            helper.setViewVisibility(R.id.order_cs_date, View.VISIBLE);
            helper.setTextViewText(R.id.order_cs_date,"(超时0.00小时)");
        }

        /*if(getDeliveryUpdateTime > getOrderRequiredTime){
            try{
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String fromDate = simpleFormat.format(item.getDelivery_update_time());
                String toDate = simpleFormat.format(item.getOrder_required_time());
                long from = simpleFormat.parse(fromDate).getTime();
                long to = simpleFormat.parse(toDate).getTime();
                int hours = (int) ((from - to)/(1000 * 60 * 60));
                helper.setTextViewText(R.id.order_cs_date,"(超时" + hours + "小时)");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            helper.setTextViewText(R.id.order_cs_date,"()");
        }*/


    }
}
