package com.wanzhuan.poster.Library.advLibrary;

import android.animation.Animator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class Loader {

    private static Loader loader;

    public static Loader getInstance(){
        if (loader==null){
            loader = new Loader();
        }
        return loader;
    }

    public Object load(View targetView,int duration,Object animator,int delay,String sign){
        switch (sign){
            case "library":
                return library_Loader(targetView,duration,(BaseViewAnimator)animator,delay);
            case "customer":
                return customer_Loader(targetView,duration,(CustomerViewAnimator)animator,delay);
            default:
                return null;
        }
    }

    private YoYo.YoYoString library_Loader(View targetView, int duration, BaseViewAnimator animator,int delay){
        YoYo.YoYoString rope = YoYo.with(animator)
                .delay(delay)
                .duration(duration)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {}

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                }).playOn(targetView);
        return rope;
    }

    private Object customer_Loader(View targetView,int duration,CustomerViewAnimator animator,int delay){
        animator.prepare(targetView);
        animator.setDuration(duration);
        animator.start(delay);
        return animator;
    }

}
