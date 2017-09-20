package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.view.View;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendSealPackingScanningInfo;

import java.util.List;

/**
 * @title :封包-》已接单-》封装扫描页面 listview 适配器
 * @author :enmaoFu
 * @create :2017/5/17
 */
public class SendSealPackingScanningAdapter extends CommonAdapter<SendSealPackingScanningInfo> {

    public SendSealPackingScanningAdapter(Context context, List<SendSealPackingScanningInfo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(final ViewHolder holder, SendSealPackingScanningInfo item, final int positon) {
        holder.setTextViewText(R.id.code,item.getOrder_tracking_code());
        holder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclick!=null) {
                    onclick.talkItemOnclickListener(positon, holder, 0);
                }
            }
        });

        if (item.isDelete()){
            holder.setViewVisibility(R.id.rl_data,View.GONE);
        }else {
            holder.setViewVisibility(R.id.rl_data,View.VISIBLE);

            //设置选中

            if (item.isScan()){
                holder.setImageByResource(R.id.img,R.drawable.select_fill);
            }else {
                holder.setImageByResource(R.id.img,R.drawable.select_dot);
            }

        }







    }

    private talkItemOnclick onclick;

    public void setOntalkItemOnclick(talkItemOnclick onclick){
        this.onclick=onclick;
    }

    public interface talkItemOnclick{
         void talkItemOnclickListener(int postion,ViewHolder vh,int swtich);
    }

}
