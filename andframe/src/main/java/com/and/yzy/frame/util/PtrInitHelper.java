package com.and.yzy.frame.util;

import android.content.Context;

import com.and.yzy.frame.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by yzy on 2016/ic_01/15.
 */
public class PtrInitHelper {


    public static void initPtr(Context mContext, final PtrFrameLayout mPtrFrameLayout) {

      /*  MaterialHeader header = new MaterialHeader(mContext);
        int[] colors = mContext.getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(mContext, 20), 0, DensityUtils.dp2px(mContext, 20));
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(100);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.disableWhenHorizontalMove(true);*/
        StoreHouseHeader header = new StoreHouseHeader(mContext);
        header.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        //      header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(mContext, 20), 0, DensityUtils.dp2px(mContext, 10));
        header.initWithString("QMCS");
        mPtrFrameLayout.setDurationToCloseHeader(2500);
        mPtrFrameLayout.setLoadingMinTime(1200);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
    }


}
