package com.wanzhuan.poster.Library.easyLibrary;

import android.animation.TypeEvaluator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class Customer_Glide_ViewAnimator extends BaseViewAnimator {

    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(

                /* Example
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                CustomerGlider.glide(5000,ObjectAnimator.ofFloat(target, "translationY", 0, 300),new CustomerEvaluator())
                */

        );
    }

    private class CustomerEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            return calclate(fraction,(float)startValue,(float)endValue);
        }
    }

    private float calclate(float fraction, float startValue, float endValue){
        /*  Example
            float startPoint = (float) startValue;
            float endPoint = (float) endValue;
            float y = startPoint + fraction % 0.2f  * (endPoint - startPoint);
            return Float.valueOf(y);
            */
        return 0.0f;
    }

}
