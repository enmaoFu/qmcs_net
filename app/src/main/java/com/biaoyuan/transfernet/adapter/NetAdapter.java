package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.view.View;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.NetInfo;

import java.util.List;

/**
 * Title  :
 * Create : 2017/5/26
 * Author ：chen
 */

public class NetAdapter extends CommonAdapter<NetInfo> {

    public NetInfo getMitem() {

        return mitem;
    }

    NetInfo mitem;
    public NetAdapter(Context context, List<NetInfo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, NetInfo item, int positon) {
        int whether = item.getWhether();
        if(whether == 1){
            mitem = item;
            holder.setViewVisibility(R.id.tv_name,View.GONE);
        }else{
            holder.setViewVisibility(R.id.tv_name,View.VISIBLE);
            holder.setTextViewText(R.id.tv_name,item.getBasicName());
        }
    }

}
