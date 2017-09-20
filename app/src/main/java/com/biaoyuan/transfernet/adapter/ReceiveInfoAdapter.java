package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.ReceiveInfo;

import java.util.List;

/**
 * Title  :
 * Create : 2017/5/9
 * Author ：chen
 */

public class ReceiveInfoAdapter extends BaseQuickAdapter<ReceiveInfo,BaseViewHolder> {


    public ReceiveInfoAdapter(int layoutResId, List<ReceiveInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceiveInfo item, int position) {

        helper.setTextViewText(R.id.code,item.getOrderTrackingCode());
        helper.setTextViewText(R.id.address,item.getAdr().replace("|",""));
        helper.setTextViewText(R.id.name,item.getOrderReceiverName());
        helper.setTextViewText(R.id.date,DateTool.timesToStrTime(item.getOrderUpdateTime() + "", "yyyy.MM.dd-HH:mm:ss"));
    }
}
