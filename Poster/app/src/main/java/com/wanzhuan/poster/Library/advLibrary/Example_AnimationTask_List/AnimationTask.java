package com.wanzhuan.poster.Library.advLibrary.Example_AnimationTask_List;

import android.view.View;

import com.daimajia.androidanimations.library.YoYo;
import com.wanzhuan.poster.Library.advLibrary.CustomerViewAnimator;
import com.wanzhuan.poster.Library.advLibrary.Loader;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class AnimationTask {

    private AnimatorInfo animator;
    private View targetView;
    private int delayTime;
    private int reStartTime;

    private Object rope = null;

    public void start(){
        if (rope==null) {
            rope = Loader.getInstance().load(targetView,
                    animator.getDuration(),
                    animator.getAnimator(),
                    delayTime,
                    animator.getSign());
        }else if (animator.getSign().equals("library")){
            ((YoYo.YoYoString)rope).stop();
            rope = Loader.getInstance().load(targetView,
                    animator.getDuration(),
                    animator.getAnimator(),
                    delayTime,
                    animator.getSign());
        }else if (animator.getSign().equals("customer")){
            ((CustomerViewAnimator)rope).ReStart();
        }
    }

    public void setAnimation_index(AnimatorInfo animator){
        this.animator = animator;
    }

    public void setTargetView(View view){
        targetView = view;
    }

    public void setDelayTime(int delay){
        delayTime = delay;
    }

    public void setReStartTime(int time){
        reStartTime = time;
    }

    public AnimatorInfo getAnimation_index(){
        return animator;
    }

    public int getDelayTime(){
        return delayTime;
    }

    public int getReStartTime(){
        return reStartTime;
    }

    public View getTargetView(){
        return targetView;
    }

}
