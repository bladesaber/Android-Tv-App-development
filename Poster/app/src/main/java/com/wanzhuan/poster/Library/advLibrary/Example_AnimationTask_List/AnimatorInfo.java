package com.wanzhuan.poster.Library.advLibrary.Example_AnimationTask_List;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class AnimatorInfo {

    private int duration;
    private Object animator;
    private String sign;

    public void setSign(String sign){
        this.sign = sign;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setAnimator(Object animator){
        this.animator = animator;
    }

    public int getDuration(){
        return duration;
    }

    public Object getAnimator(){
        return animator;
    }

    public String getSign(){
        return sign;
    }

}
