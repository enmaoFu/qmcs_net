package com.biaoyuan.transfernet.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.and.yzy.frame.util.AppManger;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.util.EasyTransition;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Title  :
 * Create : 2017/7/6
 * Author ：chen
 */

public class ShowBigImgaeAty extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bigimage);
        // onCreate
        EasyTransition.enter(this);

        AppManger.getInstance().addActivity(this);

        PhotoDraweeView photoDraweeView = (PhotoDraweeView) findViewById(R.id.img);


        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
        String url = getIntent().getStringExtra("url");

        //判断是网络图还是本地图

        if (url.contains("http") && url.length() > 20) {
            photoDraweeView.setPhotoUri(Uri.parse(url));
        } else {
            photoDraweeView.setPhotoUri(Uri.parse("file://" + url));

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManger.getInstance().killActivity(this);
    }
}
