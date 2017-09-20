package com.biaoyuan.transfernet.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.adapter.AdapterCallback;
import com.and.yzy.frame.adapter.CommonAdapter;
import com.and.yzy.frame.adapter.ViewHolder;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.TestPackageListInfo;

import java.util.List;

/**
 * Title  :包裹详情里面的包裹
 * Create : 2017/6/5
 * Author ：chen
 */

public class TestPackageListAdapter extends CommonAdapter<TestPackageListInfo> {


    public TestPackageListAdapter(Context context, List<TestPackageListInfo> mDatas, int itemLayoutId, AdapterCallback adapterCallback) {
        super(context, mDatas, itemLayoutId, adapterCallback);
    }

    @Override
    public void convert(ViewHolder holder, final TestPackageListInfo item, int positon) {


        holder.setTextViewText(R.id.tv_number, positon + 1 + "")
                .setTextViewText(R.id.tv_code, item.getOrderTrackingCode())
        ;

        TextView tv_ds = holder.getView(R.id.tv_diushi);

        TextView tv_ps = holder.getView(R.id.tv_poshun);

        if (item.isDS()) {
            //丢失
            tv_ds.setTextColor(mContext.getResources().getColor(R.color.font_orange_red));

            tv_ds.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.lost_mark), null, null, null);

        } else {

            tv_ds.setTextColor(mContext.getResources().getColor(R.color.font_gray));
            tv_ds.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.lost_nor), null, null, null);

            //点击丢失
            tv_ds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.adapterInfotoActiity(item, 1);
                }
            });


        }

        if (item.isPS()) {
            //破损
            tv_ps.setTextColor(mContext.getResources().getColor(R.color.font_orange_red));
            tv_ps.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.damaged_mark), null, null, null);
        } else {
            tv_ps.setTextColor(mContext.getResources().getColor(R.color.font_gray));
            tv_ps.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.damaged_nor), null, null, null);
        }

        //点击丢失
        tv_ds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.adapterInfotoActiity(item, 1);
            }
        });


        tv_ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击破损
                adapterCallback.adapterInfotoActiity(item, 2);
            }
        });

    }
}
