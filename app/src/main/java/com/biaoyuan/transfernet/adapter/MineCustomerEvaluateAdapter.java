package com.biaoyuan.transfernet.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.BaseViewHolder;
import com.and.yzy.frame.util.DateTool;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.domain.MineCustomerEvaluateInfo;
import com.biaoyuan.transfernet.util.MyNumberFormat;
import com.orhanobut.logger.Logger;

import java.util.List;

import am.widget.drawableratingbar.DrawableRatingBar;

/**
 * @title  :我的客户评价适配器
 * @create :2017/5/22
 * @author :enmaoFu
 */
public class MineCustomerEvaluateAdapter extends BaseQuickAdapter<MineCustomerEvaluateInfo,BaseViewHolder> {

    /**
     * 1表示全部
     * 2表示好评 4-5颗星
     * 3表示中评 3颗星
     * 4表示差评 1-2颗星
     */

    public MineCustomerEvaluateAdapter(int layoutResId, List<MineCustomerEvaluateInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineCustomerEvaluateInfo item, int position) {

        helper.setTextViewText(R.id.phone, MyNumberFormat.toPwdPhone(item.getUserTelphone()));
        helper.setTextViewText(R.id.date, DateTool.timesToStrTime(item.getOcommentCreatTime() + "", "yyyy.MM.dd"));
        helper.setTextViewText(R.id.content,item.getOcommentContent());

        DrawableRatingBar drawableRatingBar = helper.getView(R.id.ratingbar);
        drawableRatingBar.setRatingDrawable(mContext.getResources().getDrawable(R.drawable.star_allmark),mContext.getResources().getDrawable(R.drawable.star_nomark));
        drawableRatingBar.setDrawablePadding(5);
        drawableRatingBar.setGravity(Gravity.LEFT);
        drawableRatingBar.setMax(5);
        drawableRatingBar.setMin(1);
        drawableRatingBar.setRating(item.getOcommentScore());
        drawableRatingBar.setManually(false);
        drawableRatingBar.setOnlyItemTouchable(false);
        //drawableRatingBar.setOnRatingChangeListener(OnRatingChangeListener);
    }

}
