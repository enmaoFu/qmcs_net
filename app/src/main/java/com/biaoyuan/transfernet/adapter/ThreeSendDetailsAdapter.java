package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.ThreeSendDetailsInfo;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Title  : 第三方代发已代发详情 时光轴适配器
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class ThreeSendDetailsAdapter extends CommonAdapter<ThreeSendDetailsInfo> {

    public ThreeSendDetailsAdapter(Context context, List<ThreeSendDetailsInfo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, ThreeSendDetailsInfo item, int positon) {

        String date = DateTool.timesToStrTime(item.getFtime() + "","yyyy.MM.dd HH:mm");
        if("1970.01.01 08:00".equals(date)){
            holder.setViewVisibility(R.id.hm,View.GONE);
            holder.setViewVisibility(R.id.year,View.GONE);
            holder.setViewVisibility(R.id.tv_msg,View.GONE);
        }else{
            String year = date.substring(0,10);
            String hm = date.substring(date.length() - 6,date.length());
            holder.setTextViewText(R.id.hm,hm);
            holder.setTextViewText(R.id.year,year);
            holder.setTextViewText(R.id.tv_msg,item.getContext());
        }

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
