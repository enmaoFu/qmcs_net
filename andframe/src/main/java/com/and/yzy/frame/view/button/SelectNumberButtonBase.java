package com.and.yzy.frame.view.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.and.yzy.frame.R;


/**
 * 加减numberButton
 * 控件使用方法
 * 建立button对用布局，并对布局明明为<font color="#FF0000">R.layout.select_num_button</font><br/>
 * 减按钮命名为<font color="#FF0000">btn_select_num_down</font><br/>
 * 加按钮按钮命名为<font color="#FF0000">btn_select_num_up</font><br/>
 * 中间数字显示命名为<font color="#FF0000">tv_select_num</font><br/>
 * <p>
 * numberButton 监听 {@link NumberButtonListener}   public void btnNumber(String num);返回中间数量
 *
 * @author yzy
 */

public abstract class SelectNumberButtonBase extends LinearLayout {
    private TextView btn_down;
    private TextView btn_up;
    TextView tv_num;
    private String num = "1";
    private int maxNumber = 999;
    private int minNumber = 1;
    private NumberButtonListener buttonListener;
    private TextClickListener mTextClickListener;

    public TextView getBtn_down() {
        return btn_down;
    }

    public TextView getBtn_up() {
        return btn_up;
    }



    public void setTextClickListener(TextClickListener textClickListener) {
        mTextClickListener = textClickListener;
    }

    public SelectNumberButtonBase(Context context) {
        super(context);

    }

    public void setMinNumber(int min){

        this.minNumber=min;
    }

    public void setNumberButtonListener(NumberButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public SelectNumberButtonBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(setLayoutId(), this);
        initView();

    }


    public abstract int setLayoutId();


    public void setBtn_downVisibility(boolean isShow){

        if (isShow){
            btn_down.setVisibility(View.VISIBLE);
            tv_num.setVisibility(View.VISIBLE);
        }else {

            btn_down.setVisibility(View.INVISIBLE);
            tv_num.setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {

        btn_down = (TextView) findViewById(R.id.btn_select_num_down);
        btn_up = (TextView) findViewById(R.id.btn_select_num_up);
        tv_num = (TextView) findViewById(R.id.tv_select_num);


        tv_num.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextClickListener!=null){
                    mTextClickListener.textClick(tv_num.getText().toString());
                }
            }
        });


        btn_down.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                num = tv_num.getText().toString();
                int tempNum = Integer.parseInt(num) - 1;

                if (tempNum <= minNumber) {
                    tempNum = minNumber;
                 /*   if (minNumber==0){

                        btn_down.setVisibility(View.INVISIBLE);
                        tv_num.setVisibility(View.INVISIBLE);
                    }*/

                }
                tv_num.setText(tempNum + "");

                if (buttonListener != null) {
                    buttonListener.btnNumber(getNum(),false);
                }

            }
        });
        btn_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                num = tv_num.getText().toString();

                int tempNum = Integer.parseInt(num) + 1;

                if (tv_num.getVisibility()==View.INVISIBLE){
                    btn_down.setVisibility(View.VISIBLE);
                    tv_num.setVisibility(View.VISIBLE);
                }

                if (tempNum >= maxNumber) {
                    tempNum = maxNumber;
                }

                tv_num.setText(tempNum + "");

                if (buttonListener != null) {
                    buttonListener.btnNumber(getNum(),true);
                }
            }
        });

    }

    public String getNum() {
        return tv_num.getText().toString();
    }

    public void setNum(String num) {
        this.num = num;
        tv_num.setText(num);
    }

    public void setMaxNumber(String num) {
        this.maxNumber = Integer.parseInt(num);

    }

    public interface NumberButtonListener {
         void btnNumber(String num,boolean isAdd);
    }
    public interface TextClickListener {
        void textClick(String num);
    }

}
