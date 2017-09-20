package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.ThreeSendAlreadyInfo;
import com.biaoyuan.transfernet.domain.ThreeSendWaitInfo;

import java.util.List;

/**
 * Title  :三方已代发
 * Create : 2017/5/10
 * Author ：chen
 */

public class MineThreeSendAlreadyAdapter extends BaseQuickAdapter<ThreeSendAlreadyInfo,BaseViewHolder> {


    public MineThreeSendAlreadyAdapter(int layoutResId, List<ThreeSendAlreadyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThreeSendAlreadyInfo item, int position) {
        helper.setTextViewText(R.id.kd,item.getOrder_third_express_name());
        helper.setTextViewText(R.id.code,item.getOrder_third_express_code());
        helper.setTextViewText(R.id.textView2,item.getOrder_receive_addr().replace("|",""));
        helper.setTextViewText(R.id.fc_date, DateTool.timesToStrTime(item.getOrder_update_time() + "", "yyyy.MM.dd-HH:mm:ss"));
        helper.setTextViewText(R.id.qs_date, DateTool.timesToStrTime(item.getTime() + "", "yyyy.MM.dd-HH:mm:ss"));

    }
}
