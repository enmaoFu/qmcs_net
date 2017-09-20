package com.biaoyuan.transfernet.adapter;

import android.content.Context;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.PicInfo;

import java.util.List;

/**
 * Title  :
 * Create : 2017/6/5
 * Author ：chen
 */

public class TestPicAdapter extends CommonAdapter<PicInfo> {

    public TestPicAdapter(Context context, List<PicInfo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, PicInfo item, int positon) {

        holder.setImageByUrl(R.id.img,item.getPath());
    }
}
