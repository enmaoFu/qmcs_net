package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineHaveSendItem;

import java.util.List;

/**
 * Title  :我的已送达快件
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineHaveSendFileAdapter extends BaseQuickAdapter<MineHaveSendItem,BaseViewHolder> {

    public MineHaveSendFileAdapter(int layoutResId, List<MineHaveSendItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineHaveSendItem item, int position) {
        helper.setTextViewText(R.id.code,item.getOrder_tracking_code());
        helper.setTextViewText(R.id.address,item.getOrder_receive_addr().replace("|",""));
        helper.setTextViewText(R.id.success_date, DateTool.timesToStrTime(item.getDelivery_update_time() + "", "yyyy.MM.dd-HH:mm:ss") + "成功签收");
    }
}