package com.wanzhuan.poster.Library.advLibrary;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.BaseInterpolator;

/**
 * Created by bladesaber on 2018/6/27.
 */

public abstract class CustomerViewAnimator {

    // 这里会导致多个 handler 到时会放弃这种做法
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Redraw();
        }
    };

    /*
    * User Guide
    * 1. prepare (target view)
    * 2, setVar()
    * 3, setDuration()
    * 4, start()
    * 5, cancel()
     */

    private Object currentValue;
    private Object startValue;
    private Object endValue;

    private View targetView;
    private ValueAnimator animator;
    private int duration = 0;

    public void prepare(View target) {
        targetView = target;
    }

    public void start(int delay){
        if (targetView!=null) {
            handler.sendEmptyMessageDelayed(1,delay);
        }
    }

    public void cancel(){
        if (animator!=null) {
            animator.cancel();
            animator = null;
        }
    }

    private void Redraw(){
        if (currentValue==null){
            startAnimationMotion();
        }else {
            draw();
        }
    }

    /*
    * Example
    * draw()
    *{
    *   this.targetView.setTranslationX(currentPoint.x);
    *   this.targetView.setTranslationY(currentPoint.y);
    * }
     */
    abstract void draw();

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setVar(Object startValue,Object endValue,Object currentValue){
        this.startValue = startValue;
        this.endValue = endValue;
        this.currentValue = currentValue;
    }

    private void startAnimationMotion() {

        if (duration<=0){
            return;
        }

        animator = ValueAnimator.ofObject(new Customer_Evaluator(), startValue, endValue);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = animation.getAnimatedValue();
                Redraw();
            }
        });
        //animator.setInterpolator(new LinearInterpolator());//设置插值器
        animator.setInterpolator(new Customer_Interpolator());
        animator.start();
    }

    public void ReStart(){
        reStart();
    }

    abstract void reStart();

    /**
     * sin弦等幅振荡
     */
    private class Customer_Evaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            return calcalute(fraction,startValue,endValue);
        }
    }

    /*
       Example
            PointF startPoint = (PointF) startValue;
            PointF endPoint = (PointF) endValue;
            float x = startPoint.x + fraction * (endPoint.x - startPoint.x);//x坐标线性变化
            float y = 120 * (float) (Math.sin(0.01 * Math.PI * x)) + 200.0f / 2;//y坐标取相对应函数值
            return new PointF(x, y);
     */
    abstract Object calcalute(float fraction, Object startValue, Object endValue);

    private class Customer_Interpolator extends BaseInterpolator {
        @Override
        public float getInterpolation(float fraction) {
            return fraction;
        }
    }

}
