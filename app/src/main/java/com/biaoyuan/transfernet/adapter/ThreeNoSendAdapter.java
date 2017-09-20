package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.ThreeNoSendInfo;

import java.util.List;

/**
 * Title  :三方代发
 * Create : 2017/5/10
 * Author ：chen
 */

public class ThreeNoSendAdapter extends BaseQuickAdapter<ThreeNoSendInfo, BaseViewHolder> {
    public ThreeNoSendAdapter(int layoutResId, List<ThreeNoSendInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThreeNoSendInfo item, int position) {
        helper.setTextViewText(R.id.code,item.getOrderTrackingCode());
        helper.setTextViewText(R.id.address,item.getOrderReceiveAddr().replace("|",""));
        helper.setTextViewText(R.id.date, DateTool.timesToStrTime(item.getOrderRequiredTime() + "", "yyyy.MM.dd-HH:mm:ss"));
    }
}
