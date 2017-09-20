package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MinePackageWaitInfo;

import java.util.List;

/**
 * Title  :
 * Create : 2017/5/15
 * Author ：chen
 */

public class MinePackageWaitAdapter extends BaseQuickAdapter<MinePackageWaitInfo,BaseViewHolder> {

    public MinePackageWaitAdapter(int layoutResId, List<MinePackageWaitInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MinePackageWaitInfo item, int position) {
        helper.setTextViewText(R.id.code,item.getPackage_code());
        helper.setTextViewText(R.id.address,item.getBasic_name());
        helper.setTextViewText(R.id.phone,"传送天使："+item.getUser_telphone()+" 正在传送中");
    }
}
