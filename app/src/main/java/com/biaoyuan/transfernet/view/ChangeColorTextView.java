package com.biaoyuan.transfernet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.biaoyuan.transfernet.R;
import com.orhanobut.logger.Logger;

public class ChangeColorTextView extends View {


    /**
     * 颜色
     */
    private int mColor = 0xFF45C01A;
    /**
     * 透明度 0.0-1.0
     */
    private float mAlpha = 0f;
    /**
     * 文本
     */
    private String mText = "微信";
    private int mTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
    private Paint mTextPaint;
    private Rect mTextBound = new Rect();

    public ChangeColorTextView(Context context) {
        super(context);
    }

    /**
     * 初始化自定义属性值
     *
     * @param context
     * @param attrs
     */
    public ChangeColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取设置的图标
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ChangeColorView);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {

            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorView_color:
                    mColor = a.getColor(attr, 0x45C01A);
                    break;
                case R.styleable.ChangeColorView_text:
                    mText = a.getString(attr);
                    break;
            }
        }

        a.recycle();

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        // 得到text绘制范围
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

     int height = mTextBound.height() + getPaddingBottom() + getPaddingTop();
        int width = mTextBound.width() + getPaddingLeft() + getPaddingRight();
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int alpha = (int) Math.ceil((255 * mAlpha));

        drawSourceText(canvas, alpha,mAlpha);
        drawTargetText(canvas, alpha,mAlpha);


    }


    private void drawSourceText(Canvas canvas, int alpha,float mSize) {
        Logger.v("size="+16+4*mSize);
        //字体大小
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16+4*mSize, getResources().getDisplayMetrics());

        mTextPaint.setTextSize(size);
        mTextPaint.setColor(Color.parseColor("#bfe6fc"));
        mTextPaint.setAlpha(255 - alpha);

        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mText, getMeasuredWidth() / 2 - mTextBound.width() / 2, baseline, mTextPaint);

    }

    private void drawTargetText(Canvas canvas, int alpha,float mSize) {
        //字体大小
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16+4*mSize, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(size);
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mText, getMeasuredWidth() / 2 - mTextBound.width() / 2, baseline, mTextPaint);

    }


    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setIconColor(int color) {
        mColor = color;
    }


    private static final String INSTANCE_STATE = "instance_state";
    private static final String STATE_ALPHA = "state_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(STATE_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATE_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }

    }

}
