package com.wanzhuan.poster.Library.easyLibrary;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class CustomerGlider  {

    public static ValueAnimator glide(long duration, ValueAnimator animator, TypeEvaluator t) {
        animator.setEvaluator(t);
        animator.setDuration(duration);
        return animator;
    }

}
