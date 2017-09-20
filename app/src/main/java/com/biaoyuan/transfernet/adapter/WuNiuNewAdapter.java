package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.view.View;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.WuNiuItemNew;
import com.biaoyuan.transfernet.domain.WuNiuItemNewOne;

import java.util.List;

/**
 * Title  :
 * Create : 2017/5/18
 * Author ：chen
 */

public class WuNiuNewAdapter extends CommonAdapter<WuNiuItemNewOne> {
    public WuNiuNewAdapter(Context context, List<WuNiuItemNewOne> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, WuNiuItemNewOne item, int positon) {

        String date = DateTool.timesToStrTime(item.getRecordTime(),"yyyy.MM.dd HH:mm");
        String year = date.substring(0,10);
        String hm = date.substring(date.length() - 6,date.length());

        holder.setTextViewText(R.id.hm,hm);
        holder.setTextViewText(R.id.year,year);
        holder.setTextViewText(R.id.tv_msg,"【" + item.getUserLoginName() + "】" + item.getRecordValue() + "，联系电话：" + item.getUserTelphone());

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
