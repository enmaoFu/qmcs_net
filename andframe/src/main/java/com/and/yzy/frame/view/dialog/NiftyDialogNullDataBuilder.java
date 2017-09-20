package com.and.yzy.frame.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.and.yzy.frame.R;


/**
 * Created by lee on
 */
public class NiftyDialogNullDataBuilder extends Dialog {

    private Context tmpContext;
    /**
     * 动画类型
     */
    private Effectstype type = null;
    /**
     * dialog布局
     */
    private View mDialogView;

    /**
     * 用来设置点击确认button之后，dialog是否消失，默认点击就消失
     */
    private boolean mIsClickDismiss = true;


    private LinearLayout mLinearLayoutView;

    private RelativeLayout mRelativeLayoutView;

    /**
     * 可以将一个view加到此布局中
     */
    private FrameLayout mFrameLayoutCustomView;

    /**
     * 动画持续时间
     */
    private int mDuration = 500;
    /**
     * 点击外边是否消失
     */
    private boolean isCancelable = true;

    public NiftyDialogNullDataBuilder(Context context) {

        this(context, R.style.dialog_untran);

    }

    public NiftyDialogNullDataBuilder(Context context, int theme) {
        super(context, R.style.dialog_untran);
        init(context);
        tmpContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(
                params);

    }

    protected void init(Context context) {

        mDialogView = View.inflate(context,
                R.layout.frame_dialog_null_data_layout, null);


        mLinearLayoutView = (LinearLayout) mDialogView
                .findViewById(R.id.parentPanel);
        mRelativeLayoutView = (RelativeLayout) mDialogView
                .findViewById(R.id.main);


        mFrameLayoutCustomView = (FrameLayout) mDialogView
                .findViewById(R.id.customPanel);


        // 默认点击dialog外，不让他消失
        setNd_IsOnTouchOutside(false);

        setContentView(mDialogView);

        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                mLinearLayoutView.setVisibility(View.VISIBLE);
                if (type == null) {
                    type = Effectstype.Fall;
                }
                start(type);

            }
        });
        mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable)
                    dismiss();
            }
        });
    }


    /**
     * 设置动画持续时间
     *
     * @param
     * @return
     */
    public NiftyDialogNullDataBuilder setND_Duration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * 设置动画的类型
     *
     * @param
     * @return
     */
    public NiftyDialogNullDataBuilder setND_Effect(Effectstype type) {
        this.type = type;
        return this;
    }


    /**
     * 在内容区域添加一个view
     *
     * @param
     * @return
     */
    public NiftyDialogNullDataBuilder setND_AddCustomView(int resId) {
        View customView = View.inflate(tmpContext, resId, null);
        if (mFrameLayoutCustomView.getChildCount() > 0) {
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(customView);
        return this;
    }

    /**
     * 在内容区域添加一个view
     *
     * @param
     * @return
     */
    public NiftyDialogNullDataBuilder setND_AddCustomView(View view) {
        if (mFrameLayoutCustomView.getChildCount() > 0) {
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(view);

        return this;
    }

    /**
     * 设置是否在对话框外点击消失
     *
     * @param
     * @return
     */
    public NiftyDialogNullDataBuilder setNd_IsOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }


    @Override
    public void show() {
        super.show();
    }

    /**
     * 启动动画
     */
    private void start(Effectstype type) {
        BaseEffects animator = type.getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayoutView);
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }


}
