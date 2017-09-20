package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendSealWaitOrdersHeadInfo;
import com.biaoyuan.transfernet.util.MyNumberFormat;

import java.util.List;

/**
 * @title : 发件网点-》封包fragment recyclerview适配器
 * @author :enmaoFu
 * @create :2017/5/9
 */
public class SendSealWaitOrdersAdapter extends BaseQuickAdapter<SendSealWaitOrdersHeadInfo,BaseViewHolder> {


    public SendSealWaitOrdersAdapter(int layoutResId, List<SendSealWaitOrdersHeadInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendSealWaitOrdersHeadInfo item, int position) {
        helper.setTextViewText(R.id.title_content,"包裹  " + item.getPackage_weight() + "kg  最长边" + item.getPackage_size() + "cm");
        helper.setTextViewText(R.id.send,"送至："+item.getBasic_name());
        String xzDate = DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd");
        if (xzDate.equals(DateTool.timesToStrTime(item.getPublish_req_pickup_time() + "", "yyyy-MM-dd"))){
            helper.setTextViewText(R.id.date,"今日" + DateTool.timesToStrTime(item.getPublish_req_pickup_time() + "", "MM.dd-HH:mm") + "之前取件");
        }else{
            helper.setTextViewText(R.id.date,DateTool.timesToStrTime(item.getPublish_req_pickup_time() + "", "MM.dd-HH:mm") + "之前取件");
        }

        helper.setTextViewText(R.id.price,"¥ "+MyNumberFormat.m2(item.getPackage_carrier_reward()));
    }
}
