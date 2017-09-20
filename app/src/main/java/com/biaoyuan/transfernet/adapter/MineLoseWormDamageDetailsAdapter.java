package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.net.Uri;

import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineLoseWormDamageDetailsInfo;
import com.biaoyuan.transfernet.domain.PicInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class MineLoseWormDamageDetailsAdapter extends CommonAdapter<PicInfo> {

    public MineLoseWormDamageDetailsAdapter(Context context, List<PicInfo> mList, int itemLayoutId) {
        super(context, mList, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, PicInfo item, int positon) {
        holder.setImageByUrl(R.id.img,item.getPath());
    }
}
