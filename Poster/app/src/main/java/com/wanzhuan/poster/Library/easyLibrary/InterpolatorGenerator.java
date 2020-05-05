package com.wanzhuan.poster.Library.easyLibrary;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by bladesaber on 2018/7/2.
 */

public class InterpolatorGenerator {

    private static InterpolatorGenerator interpolatorGenerator = null;

    public static InterpolatorGenerator getInstance(){
        if (interpolatorGenerator==null){
            interpolatorGenerator = new InterpolatorGenerator();
        }
        return interpolatorGenerator;
    }

    public BaseInterpolator createInterpolator(int id){
        switch (id){
            case 0:
                return new AccelerateDecelerateInterpolator();
            case 1:
                return new LinearInterpolator();
            case 2:
                return new AccelerateInterpolator();
            case 3:
                return new DecelerateInterpolator();
            case 4:
                return new AnticipateInterpolator();
            case 5:
                return new AnticipateOvershootInterpolator();
            case 6:
                return new BounceInterpolator();
            case 7:
                return new OvershootInterpolator();
            default:
                return new AccelerateDecelerateInterpolator();
        }
    }

}
