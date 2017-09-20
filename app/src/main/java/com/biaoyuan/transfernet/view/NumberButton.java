package com.biaoyuan.transfernet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.and.yzy.frame.util.DensityUtils;
import com.biaoyuan.transfernet.R;
import com.orhanobut.logger.Logger;


/**
 * 加减numberButton
 *
 * @author yzy
 */

public class NumberButton extends LinearLayout {
    private ImageView btn_down;
    private ImageView btn_up;
    private TextView tv_content;


    //切换数据
    private String[] data={};

    private talkItemOnclick onclick;

    public int getPosition() {
        return position;
    }

    private int position = 0;

    private int maxNumber = 0;

    public ImageView getBtn_down() {
        return btn_down;
    }

    public void setBtn_down(ImageView btn_down) {
        this.btn_down = btn_down;
    }

    public ImageView getBtn_up() {
        return btn_up;
    }

    public void setBtn_up(ImageView btn_up) {
        this.btn_up = btn_up;
    }

    public TextView getTv_content() {
        return tv_content;
    }

    public NumberButton(Context context) {
        super(context);
    }

    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_number_layout, this);
        initView();

    }

    private void initView() {

        btn_down = (ImageView) findViewById(R.id.btn_select_num_down);
        btn_up = (ImageView) findViewById(R.id.btn_select_num_up);
        tv_content = (TextView) findViewById(R.id.tv_select_num);
        btn_down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                position -= 1;
                if (position < 0) {
                    position = 0;
                }
                tv_content.setText(data[position]);

                if(onclick != null){
                    onclick.talkItemOnclickListener(position, 1);
                }
            }
        });
        btn_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                position += 1;
                if (position > maxNumber) {
                    position = maxNumber;
                }
                tv_content.setText(data[position]);

                if (onclick != null) {
                    onclick.talkItemOnclickListener(position, 0);
                }
            }
        });
    }

    public void setOntalkItemOnclick(talkItemOnclick onclick){
        this.onclick = onclick;
    }

    public interface talkItemOnclick{
        public void talkItemOnclickListener(int postion,int number);
    }

    public void setTextColor(int color){
        tv_content.setTextColor(getResources().getColor(color));
    }

    public void setTextSize(int size){
        tv_content.setTextSize(size);
    }


    /**
     * 设置初始化显示的数据
     *
     */
    public void setData(String[] data) {
        this.data = data;
        //默认显示第一条
        tv_content.setText(data[0]);
        maxNumber = data.length-1;
    }

}
