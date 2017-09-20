package com.and.yzy.frame.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.and.yzy.frame.R;
import com.and.yzy.frame.application.BaseApplication;

/**
 * Created by yzy on 2017/1/17.
 */
public class ToastUtil {


    private static Toast toast;


    public static void showSuccessToast(String content, int duration) {
        if (toast == null) {

            toast = new Toast(BaseApplication.getApplicationCotext());

        }

        View v = LayoutInflater.from(BaseApplication.getApplicationCotext()).inflate(R.layout.frame_toast_root, null);

        FrameLayout bg = (FrameLayout) v.findViewById(R.id.frame_toast_bg);

        bg.setBackgroundDrawable(BaseApplication.getApplicationCotext().getResources().getDrawable(R.drawable.frame_black_round_bg));

        TextView tv = (TextView) v.findViewById(R.id.textView1);
        tv.setText(content);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }

    public static void showErrorToast(String content, int duration) {
        if (toast == null) {
            toast = new Toast(BaseApplication.getApplicationCotext());
        }
        View v = LayoutInflater.from(BaseApplication.getApplicationCotext()).inflate(R.layout.frame_toast_root, null);
        FrameLayout bg = (FrameLayout) v.findViewById(R.id.frame_toast_bg);
        bg.setBackgroundDrawable(BaseApplication.getApplicationCotext().getResources().getDrawable(R.drawable.frame_yellow_round_bg));
        TextView tv = (TextView) v.findViewById(R.id.textView1);
        tv.setText(content);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }
}
