package com.biaoyuan.transfernet.adapter;

import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineLoseWormItem;

import java.util.List;

/**
 * Title  :丢失破损
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineLoseWormAdapter extends BaseQuickAdapter<MineLoseWormItem,BaseViewHolder> {

    public MineLoseWormAdapter(int layoutResId, List<MineLoseWormItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineLoseWormItem item, int position) {
        helper.setTextViewText(R.id.code,item.getOrder_tracking_code());
        helper.setTextViewText(R.id.weight_size,"最长边≤"+item.getOrder_goods_size() + "cm    " + item.getOrder_goods_weight() + "kg");
        helper.setTextViewText(R.id.address,"寄件地址：" + item.getOrder_send_addr().replace("|",""));

        if(item.getCarry_check_time().equals("null") || item.getCarry_check_time().equals("") || item.getCarry_check_time() == null){
            //helper.setTextViewText(R.id.date,"提交时间：" + DateTool.timesToStrTime(0 + "", "yyyy.MM.dd HH:mm"));
            helper.setViewVisibility(R.id.date, View.GONE);
        }else{
            helper.setTextViewText(R.id.date,"提交时间：" + DateTool.timesToStrTime(item.getCarry_check_time() + "", "yyyy.MM.dd HH:mm"));
        }

        if(item.getExcep_type() == 5){
            helper.setTextViewText(R.id.type,"快件丢失");
        }else{
            helper.setTextViewText(R.id.type,"快件破损");
        }
    }
}
