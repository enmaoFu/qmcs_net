package com.biaoyuan.transfernet.adapter;

import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineSendWaitInfo;
import com.biaoyuan.transfernet.domain.ThreeSendWaitInfo;

import java.util.List;

/**
 * Title  :三方已代发
 * Create : 2017/5/10
 * Author ：chen
 */

public class MineThreeSendWaitAdapter extends BaseQuickAdapter<MineSendWaitInfo,BaseViewHolder> {

    public MineThreeSendWaitAdapter(int layoutResId, List<MineSendWaitInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineSendWaitInfo item, int position) {
        if(item.getOrder_third_express_name() == null || item.getOrder_third_express_name().equals("null") || item.getOrder_third_express_name().equals("")){
            helper.setTextViewText(R.id.kd,"暂无");
        }else{
            helper.setTextViewText(R.id.kd,item.getOrder_third_express_name());
        }
        if(item.getOrder_third_express_code() == null || item.getOrder_third_express_code().equals("null") || item.getOrder_third_express_code().equals("")){
            helper.setTextViewText(R.id.code,"暂无");
        }else{
            helper.setTextViewText(R.id.code,item.getOrder_third_express_code());
        }

        helper.setTextViewText(R.id.textView2,item.getOrder_receive_addr().replace("|",""));
        helper.setTextViewText(R.id.date,DateTool.timesToStrTime(item.getOrder_update_time() + "", "yyyy.MM.dd-HH:mm:ss"));

        if("".equals(item.getContext()) || "null".equals(item.getContext()) || null == item.getContext()){
            helper.setViewVisibility(R.id.address, View.GONE);
        }else {
            helper.setTextViewText(R.id.address, "快件已到达：" + item.getContext());
        }
    }
}
