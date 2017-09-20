package com.biaoyuan.transfernet.adapter;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendMainDreaserLayoutRecyclerInfo;

import java.util.List;

/**
 * @title : 发送网点侧滑抽屉适配器
 * @author :enmaoFu
 * @create :2017/5/23
 */
public class SendMainDreaserLayoutRecyclerAdapter extends BaseQuickAdapter<SendMainDreaserLayoutRecyclerInfo,BaseViewHolder>{


    public SendMainDreaserLayoutRecyclerAdapter(int layoutResId, List<SendMainDreaserLayoutRecyclerInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendMainDreaserLayoutRecyclerInfo item, int position) {
        helper.setTextViewText(R.id.name,item.getName());
    }
}
