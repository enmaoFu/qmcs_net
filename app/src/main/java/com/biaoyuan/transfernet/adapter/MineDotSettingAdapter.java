package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.and.yzy.frame.adapter.NewBaseAdapter;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineDotSettingInfo;
import com.biaoyuan.transfernet.domain.NetInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class MineDotSettingAdapter extends NewBaseAdapter<NetInfo> {

    public NetInfo getMitem() {

        return mitem;
    }

    NetInfo mitem;

    public MineDotSettingAdapter(Context context, List<NetInfo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewNoHolder noHolder, NetInfo item, int positon) {
        int whether = item.getWhether();
        if(whether == 1){
            mitem = item;
            noHolder.setViewVisibility(R.id.lin,View.GONE);
        }else{
            noHolder.setViewVisibility(R.id.lin,View.VISIBLE);
            noHolder.setTextViewText(R.id.spot,item.getBasicName());
            noHolder.setTextViewText(R.id.phone,String.valueOf(item.getOutBasicTelphone()));
        }

    }

}
