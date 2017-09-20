package com.and.yzy.frame.view.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * 抖动效果显示
 */
public class Shake extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, .10f, -25, .26f, 25, .42f, -25, .58f, 25, .74f, -25, .90f, 1, 0).setDuration(mDuration)

        );
    }
}
