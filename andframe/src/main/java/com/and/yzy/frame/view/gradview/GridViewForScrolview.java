package com.and.yzy.frame.view.gradview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewForScrolview extends GridView {
	public GridViewForScrolview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridViewForScrolview(Context context) {
		super(context);
	}

	public GridViewForScrolview(Context context, AttributeSet attrs,
								int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
