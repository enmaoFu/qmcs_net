package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendSealAlreadyOrdersHeadInfo;
import com.biaoyuan.transfernet.util.MyNumberFormat;

import java.util.List;

/**
 * @title : 发件网点-》封包fragment recyclerview适配器
 * @author :enmaoFu
 * @create :2017/5/9
 */
public class SendSealAlreadyOrdersAdapter extends BaseQuickAdapter<SendSealAlreadyOrdersHeadInfo,BaseViewHolder> {


    public SendSealAlreadyOrdersAdapter(int layoutResId, List<SendSealAlreadyOrdersHeadInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendSealAlreadyOrdersHeadInfo item, int position) {
        helper.setTextViewText(R.id.title_content,"包裹  " + item.getPackage_weight() + "kg   最长边≤" + item.getPackage_size() + "cm");
        helper.setTextViewText(R.id.send_reach,"送至" + item.getBasic_name());
        helper.setTextViewText(R.id.price,"¥ " + MyNumberFormat.m2(item.getPackage_carrier_reward()));
        helper.setTextViewText(R.id.angel,String.valueOf(item.getUser_telphone()));
    }
}
