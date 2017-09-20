package com.biaoyuan.transfernet.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Title  :
 * Create : 2017/5/10
 * Author ：chen
 */

public class NewViewPager extends ViewPager {
    public NewViewPager(Context context) {
        super(context);
    }

    public NewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //解决滑动冲突
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (getCurrentItem()!=0){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
