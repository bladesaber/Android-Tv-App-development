package com.wanzhuan.poster.CustomerViewAnimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.wanzhuan.poster.DataStructure.ViewModel;
import com.wanzhuan.poster.Library.Utils;
import com.wanzhuan.poster.Library.easyLibrary.ViewInfo;

import java.util.List;

/**
 * Created by bladesaber on 2018/6/30.
 */

public class Cusomer_ViewAnimator_Utils {

    private static Cusomer_ViewAnimator_Utils customer_utils;

    public static Cusomer_ViewAnimator_Utils getInstance(){
        if (customer_utils==null){
            customer_utils = new Cusomer_ViewAnimator_Utils();
        }
        return customer_utils;
    }

    public Animator CreateTranlatinoX(Context context,View target, List<Float> vars, ViewInfo.PositionInfo positionInfo, ViewModel.AnimationSetting animationSetting){
        //float[] params = resolve_params(vars);

        float[] params = resolve_params_width(context,vars);

        return ObjectAnimator.ofFloat(target, "translationX", params);

    }

    public ObjectAnimator CreateTranlatinoY(Context context,View target, List<Float> vars, ViewInfo.PositionInfo positionInfo, ViewModel.AnimationSetting animationSetting){
        //float[] params = resolve_params(vars);

        float[] params = resolve_params_height(context,vars);

        return ObjectAnimator.ofFloat(target, "translationY", params);
    }

    /*
    public Animator CreateTranlatinoX_Left(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo, ViewModel.AnimationSetting animationSetting){
        float[] params = resolve_params_add(vars);
        params[0] = -(positionInfo.margin_left + positionInfo.width);
        return ObjectAnimator.ofFloat(target, "translationX", params);

    }

    public ObjectAnimator CreateTranlatinoX_Right(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo, ViewModel.AnimationSetting animationSetting){
        float[] params = resolve_params_add(vars);
        params[0] = positionInfo.margin_right + positionInfo.width;
        return ObjectAnimator.ofFloat(target, "translationX", params);
    }

    public ObjectAnimator CreateTranlatinoY_Top(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo, ViewModel.AnimationSetting animationSetting){
        float[] params = resolve_params_add(vars);
        params[0] = -(positionInfo.height + positionInfo.margin_top);
        return ObjectAnimator.ofFloat(target, "translationY", params);
    }

    public ObjectAnimator CreateTranlatinoY_Bottom(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo, ViewModel.AnimationSetting animationSetting){
        float[] params = resolve_params_add(vars);
        params[0] = positionInfo.height + positionInfo.margin_bottom;
        return ObjectAnimator.ofFloat(target, "translationY", params);
    }
    */

    public ObjectAnimator CreateRotationX(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo){
        float[] params = resolve_params(vars);
        return ObjectAnimator.ofFloat(target, "rotationX", params);
    }

    public ObjectAnimator CreateRotationY(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo){
        float[] params = resolve_params(vars);
        return ObjectAnimator.ofFloat(target, "rotationY", params);
    }

    public ObjectAnimator CreateRotationZ(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo){
        float[] params = resolve_params(vars);
        return ObjectAnimator.ofFloat(target, "rotation", params);
    }

    public ObjectAnimator CreateScaleX(Context context,View target, List<Float> vars, ViewInfo.PositionInfo positionInfo){
        //float[] params = resolve_params(vars);

        float[] params = resolve_params_width(context,vars);

        return ObjectAnimator.ofFloat(target, "ScaleX", params);
    }

    public ObjectAnimator CreateScaleY(Context context,View target, List<Float> vars, ViewInfo.PositionInfo positionInfo){
        //float[] params = resolve_params(vars);

        float[] params = resolve_params_height(context,vars);

        return ObjectAnimator.ofFloat(target, "ScaleY", params);
    }

    public ObjectAnimator CreateAlpha(View target, List<Float> vars, ViewInfo.PositionInfo positionInfo){
        float[] params = resolve_params(vars);
        return ObjectAnimator.ofFloat(target, "alpha", params);
    }

    private float[] resolve_params(List<Float> params){
        float[] new_params = new float[params.size()];
        for (int i=0;i<params.size();i++){
            new_params[i] = params.get(i);
        }
        return new_params;
    }

    private float[] resolve_params_width(Context context,List<Float> params){
        float[] new_params = new float[params.size()];
        for (int i=0;i<params.size();i++){
            new_params[i] = Utils.getInstance(context).calculate_width(params.get(i),1.5f,1920f);
        }
        return new_params;
    }

    private float[] resolve_params_height(Context context,List<Float> params){
        float[] new_params = new float[params.size()];
        for (int i=0;i<params.size();i++){
            new_params[i] = Utils.getInstance(context).calculate_height(params.get(i),1.5f,1080f);
        }
        return new_params;
    }
    
}
