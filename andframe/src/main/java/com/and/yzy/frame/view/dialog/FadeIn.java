package com.and.yzy.frame.view.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * 正常显示
 */
public class FadeIn extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view,"alpha",0,1).setDuration(mDuration)

        );
    }
}