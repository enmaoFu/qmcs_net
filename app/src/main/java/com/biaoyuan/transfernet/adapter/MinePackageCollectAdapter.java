package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineCollectPackageInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class MinePackageCollectAdapter extends BaseQuickAdapter<MineCollectPackageInfo,BaseViewHolder> {

    public MinePackageCollectAdapter(int layoutResId, List<MineCollectPackageInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineCollectPackageInfo item, int position) {
        helper.setTextViewText(R.id.start_date,DateTool.timesToStrTime(item.getCarry_pickup_time() + "", "yyyy.MM.dd-HH:mm:ss"));
        helper.setTextViewText(R.id.address,item.getBasic_address());
        helper.setTextViewText(R.id.angle,"传送天使 " + item.getUser_login_name() + " 已接件");
    }
}
