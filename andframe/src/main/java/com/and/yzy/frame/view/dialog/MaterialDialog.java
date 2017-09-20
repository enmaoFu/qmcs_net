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
import android.widget.TextView;

import com.and.yzy.frame.R;


/**
 * Material风格的dialog
 */
public class MaterialDialog extends Dialog implements DialogInterface,
        View.OnClickListener {

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
     * title显示的文字
     */
    private TextView mTitle;
    /**
     * 内容区域显示的文字
     */
    private TextView mMessage;

    /**
     * 取消按钮
     */
    private TextView mButton1;
    /**
     * 确定按钮
     */
    private TextView mButton2;
    /**
     * 用来设置点击确认button之后，dialog是否消失，默认点击就消失
     */
    private boolean mIsClickDismiss = true;


    private LinearLayout mLinearLayoutView;

    private RelativeLayout mRelativeLayoutView;

    private LinearLayout linerlyBtns;
    /**
     * 可以将一个view加到此布局中
     */
    private FrameLayout mFrameLayoutCustomView;
    /**
     * 动画持续时间
     */
    private int mDuration = 300;
    /**
     * 点击外边是否消失
     */
    private boolean isCancelable = true;

    public MaterialDialog(Context context) {

        this(context, R.style.dialog_untran);

    }

    public MaterialDialog(Context context, int theme) {
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
                R.layout.frame_materia_dialog_layout, null);


        mLinearLayoutView = (LinearLayout) mDialogView
                .findViewById(R.id.parentPanel);
        mRelativeLayoutView = (RelativeLayout) mDialogView
                .findViewById(R.id.main);

        mTitle = (TextView) mDialogView.findViewById(R.id.tv_dialog_title);
        mMessage = (TextView) mDialogView.findViewById(R.id.tv_dialog_message);
        linerlyBtns = (LinearLayout) mDialogView.findViewById(R.id.linerly_btns);
        mButton1 = (TextView) mDialogView.findViewById(R.id.btn_dialog_cancle);
        mButton2 = (TextView) mDialogView.findViewById(R.id.btn_dialog_confirm);
        mFrameLayoutCustomView = (FrameLayout) mDialogView
                .findViewById(R.id.customPanel);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);

        // 默认点击dialog外，不让他消失
        setMDOnTouchOutside(false);

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
     * 设置标题
     *
     * @param
     * @returnMD
     */
    public MaterialDialog setMDTitle(CharSequence title) {
        toggleView(mTitle, title);
        mTitle.setText(title);

        return this;
    }

    public MaterialDialog setMDNoButton(boolean isNoBtns) {
        if (isNoBtns)
            linerlyBtns.setVisibility(View.GONE);
        else
            linerlyBtns.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 隐藏标题
     *
     * @param
     * @return
     */
    public MaterialDialog setMDNoTitle(boolean isNoTitle) {

        if (isNoTitle) {

            mTitle.setVisibility(View.GONE);

        } else {
            mTitle.setVisibility(View.VISIBLE);

        }


        return this;
    }

    /**
     * 设置点击button之后，dialog是否消失，默认点击就消失
     *
     * @param
     * @return
     */
    public MaterialDialog setMDClickDismiss(boolean isClickDismiss) {

        this.mIsClickDismiss = isClickDismiss;

        return this;
    }


    /**
     * 设置内容
     *
     * @param
     * @return
     */
    public MaterialDialog setMDMessage(CharSequence msg) {
        toggleView(mMessage, msg);
        mMessage.setText(msg);

        return this;
    }


    private DialogBtnCallBack click_01;
    private DialogBtnCallBack click_02;

    /**
     * 设置取消的点击事件
     *
     * @param
     * @return
     */
    public MaterialDialog setMDCancelBtnClick(DialogBtnCallBack click) {
        if (click != null) {
            this.click_01 = click;
        }
        return this;
    }

    /**
     * 设置确定按钮的点击事件
     *
     * @param
     * @return
     */
    public MaterialDialog setMDConfirmBtnClick(DialogBtnCallBack click) {
        if (click != null) {
            this.click_02 = click;
        }

        return this;
    }

    /**
     * 设置动画持续时间
     *
     * @param
     * @return
     */
    public MaterialDialog setMDDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * 设置动画的类型
     *
     * @param
     * @return
     */
    public MaterialDialog setMDEffect(Effectstype type) {
        this.type = type;
        return this;
    }


    /**
     * 在内容区域添加一个view
     *
     * @param
     * @return
     */
    public MaterialDialog setMDCustomView(int resId) {
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
    public MaterialDialog setMDCustomView(View view) {
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
    public MaterialDialog setMDOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    /**
     * 判断是否显示view
     */
    private void toggleView(View view, Object obj) {
        if (obj == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {

        if (mTitle.getText().equals(""))
            mTitle.setVisibility(View.GONE);

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

    public interface DialogBtnCallBack {
        void dialogBtnOnClick();
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_dialog_cancle == v.getId()) {
            if (click_01 != null) {
                click_01.dialogBtnOnClick();
            }
            dismiss();

        } else if (R.id.btn_dialog_confirm == v.getId()) {
            if (click_02 != null) {
                click_02.dialogBtnOnClick();
            }

            if (mIsClickDismiss) {
                dismiss();
            }

        }

    }

}
