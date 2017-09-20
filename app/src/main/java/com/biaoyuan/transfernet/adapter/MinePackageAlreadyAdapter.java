package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MinePackageAlreadyInfo;
import com.biaoyuan.transfernet.domain.MinePackageWaitInfo;

import java.util.List;

/**
 * Title  :
 * Create : 2017/5/15
 * Author ：chen
 */

public class MinePackageAlreadyAdapter extends BaseQuickAdapter<MinePackageAlreadyInfo,BaseViewHolder> {

    public MinePackageAlreadyAdapter(int layoutResId, List<MinePackageAlreadyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MinePackageAlreadyInfo item, int position) {
        helper.setTextViewText(R.id.code,item.getPackage_code());
        helper.setTextViewText(R.id.address,item.getBasic_name());
        helper.setTextViewText(R.id.phone,"送达时间："+ DateTool.timesToStrTime(item.getCarry_check_time() + "", "yyyy.MM.dd-HH:mm:ss"));
    }
}
