package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendSendArriveAddressInfo;

import java.util.List;

/**
 * @author :enmaoFu
 * @title : 发布-》发布到到达地址recyclerview适配器
 * @create :2017/5/12
 */
public class SendSednArriveAddressAdapter extends BaseQuickAdapter<SendSendArriveAddressInfo, BaseViewHolder> {

    public SendSednArriveAddressAdapter(int layoutResId, List<SendSendArriveAddressInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendSendArriveAddressInfo item, int position) {

        helper.setTextViewText(R.id.code,item.getOrder_tracking_code());
        helper.setTextViewText(R.id.address_content,item.getOrder_receive_addr().replace("|",""));
        helper.setTextViewText(R.id.date_content, DateTool.timesToStrTime(item.getDelivery_update_time() + "", "yyyy.MM.dd-HH:mm:ss"));

        if (item.isSelect()) {
            helper.setCheckBoxChecked(R.id.check, true);
        } else {
            helper.setCheckBoxChecked(R.id.check, false);
        }


    }
}
