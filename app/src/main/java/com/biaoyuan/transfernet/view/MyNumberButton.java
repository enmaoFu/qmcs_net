package com.biaoyuan.transfernet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaoyuan.transfernet.R;

import java.util.ArrayList;


/**
 * 加减numberButton
 *
 * @author yzy
 */

public class MyNumberButton extends LinearLayout {
    private ImageView btn_down;
    private ImageView btn_up;
    private TextView tv_content;


    //切换数据
    private ArrayList<String> data;


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

    public MyNumberButton(Context context) {
        super(context);
    }

    public MyNumberButton(Context context, AttributeSet attrs) {
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
                tv_content.setText(data.get(position));

            }
        });
        btn_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                position += 1;
                if (position > maxNumber) {
                    position = maxNumber;
                }
                tv_content.setText(data.get(position));

            }
        });

    }


    /**
     * 设置初始化显示的数据
     */
    public void setData(ArrayList<String> data) {


        this.data = data;
        //默认显示第一条
        tv_content.setText(data.get(0));
        maxNumber = data.size() - 1;
        position = 0;
    }


}
