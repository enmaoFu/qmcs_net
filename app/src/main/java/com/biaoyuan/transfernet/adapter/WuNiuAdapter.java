package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.WuNiuItem;
import com.biaoyuan.transfernet.domain.WuNiuItemNew;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Title  :
 * Create : 2017/5/18
 * Author ：chen
 */

public class WuNiuAdapter extends CommonAdapter<WuNiuItemNew> {
    public WuNiuAdapter(Context context, List<WuNiuItemNew> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, WuNiuItemNew item, int positon) {

        holder.setTextViewText(R.id.hm,item.getDate());
        holder.setTextViewText(R.id.year,item.getYear());
        holder.setTextViewText(R.id.tv_msg,item.getContent());

        if (positon == (mDatas.size()-1)){
            holder.setViewVisibility(R.id.v_bottom, View.INVISIBLE);
        }else {
            holder.setViewVisibility(R.id.v_bottom, View.VISIBLE);
        }
        if (positon==0){
            holder.setBackgroundDrawable(R.id.sopt,mContext.getResources().getDrawable(R.drawable.shape_circle_blue));
            holder.setTextViewTextColor(R.id.tv_msg,mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.setBackgroundDrawable(R.id.sopt,mContext.getResources().getDrawable(R.drawable.shape_circle_wuniu));
            holder.setTextViewTextColor(R.id.tv_msg,mContext.getResources().getColor(R.color.font_hint));
        }
    }
}
