package com.biaoyuan.transfernet.adapter;

import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseItemDraggableAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.SendMessageInfo;

import java.util.List;

/**
 * Title  : 消息中心适配器
 * Create : 2017/5/24
 * Author ：enmaoFu
 */
public class SendMessageAdapter extends BaseItemDraggableAdapter<SendMessageInfo,BaseViewHolder> {


    public SendMessageAdapter(int layoutResId, List<SendMessageInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SendMessageInfo item, int position) {

        /*if(item.getMessageType() == 1){
            helper.setViewVisibility(R.id.details_text, View.GONE);
            helper.setViewVisibility(R.id.details_img, View.GONE);
        }*/

        helper.setTextViewText(R.id.message_title,item.getMessageTitle());
        helper.setTextViewText(R.id.message_date, DateTool.timesToStrTime(item.getMessageTime() + "", "yyyy.MM.dd-HH:mm:ss"));
        helper.setTextViewText(R.id.message_content,item.getMessageContent());
    }
}
