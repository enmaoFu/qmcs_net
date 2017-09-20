package com.and.yzy.frame.view.dialog;

import android.app.Dialog;
import android.content.Context;

import com.and.yzy.frame.R;


/**
 * Created by Administrator on 2016/4/12.
 */
public  class BaseDialog extends Dialog {


    public BaseDialog(Context context) {

        this(context, R.style.dialog_untran);

        setCanceledOnTouchOutside(false);

    }




    public BaseDialog(Context context, int theme) {
        super(context, R.style.dialog_untran);

        setCanceledOnTouchOutside(false);


    }




}
