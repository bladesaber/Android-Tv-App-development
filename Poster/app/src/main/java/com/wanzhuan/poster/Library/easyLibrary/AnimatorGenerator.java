package com.wanzhuan.poster.Library.easyLibrary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.wanzhuan.poster.CustomerViewAnimator.Cusomer_ViewAnimator_Utils;
import com.wanzhuan.poster.DataStructure.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class AnimatorGenerator {

    private static AnimatorGenerator generator;

    public static AnimatorGenerator getInstance(){
        if (generator==null){
            generator = new AnimatorGenerator();
        }
        return generator;
    }

    public BaseViewAnimator createAnimator(int id,final ViewInfo.PositionInfo structure){
        BaseViewAnimator animator = null;
        switch (id){
            case 1:
                // DropOutAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1),
                                Glider.glide(Skill.BounceEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "translationY",
                                        -(structure.height+structure.margin_top), 0))
                        );
                    }
                };
                return animator;
            case 2:
                // PulseAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1),
                                ObjectAnimator.ofFloat(target, "scaleY", 1, 1.1f, 1),
                                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.1f, 1)
                        );
                    }
                };
                return animator;
            case 3:
                // RubberBand
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1),
                                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.25f, 0.75f, 1.15f, 1),
                                ObjectAnimator.ofFloat(target, "scaleY", 1, 0.75f, 1.25f, 0.85f, 1)
                        );
                    }
                };
                return animator;
            case 4:
                // BounceAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1),
                                ObjectAnimator.ofFloat(target, "translationY", 0, 0, -30, 0, -15, 0, 0)
                        );
                    }
                };
                return animator;
            case 5:
                // TadaAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1),
                                ObjectAnimator.ofFloat(target, "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),
                                ObjectAnimator.ofFloat(target, "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1),
                                ObjectAnimator.ofFloat(target, "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0)
                        );
                    }
                };
                return animator;
            case 6:
                // RollInAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                                ObjectAnimator.ofFloat(target, "translationX", -structure.width, 0),
                                ObjectAnimator.ofFloat(target, "rotation", -120, 0)
                        );
                    }
                };
                return animator;
            case 7:
                // BounceInAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1),
                                ObjectAnimator.ofFloat(target, "scaleX", 0.3f, 1.05f, 0.9f, 1),
                                ObjectAnimator.ofFloat(target, "scaleY", 0.3f, 1.05f, 0.9f, 1)
                        );
                    }
                };
                return animator;
            case 8:
                // BounceInDownAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1),
                                ObjectAnimator.ofFloat(target, "translationY", -structure.height, 30, -10, 0)
                        );
                    }
                };
                return animator;
            case 9:
                // BounceLeftAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "translationX", -structure.width, 30, -10, 0),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1)
                        );
                    }
                };
                return animator;
            case 10:
                // BounceRightAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "translationX", structure.width + structure.margin_right, -30, 10, 0),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1)
                        );
                    }
                };
                return animator;
            case 11:
                // BounceUpAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "translationY", structure.height, -30, 10, 0),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1)
                        );
                    }
                };
                return animator;
            case 12:
                // FlipInXAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "rotationX", 90, -15, 15, 0),
                                //ObjectAnimator.ofFloat(target, "alpha", 0.25f, 0.5f, 0.75f, 1)
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1)
                        );
                    }
                };
                return animator;
            case 13:
                // FlipInYAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "rotationY", 90, -15, 15, 0),
                                //ObjectAnimator.ofFloat(target, "alpha", 0.25f, 0.5f, 0.75f, 1)
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1)
                        );
                    }
                };
                return animator;
            case 14:
                // RouteInDownLeftAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        float x = target.getPaddingLeft();
                        float y = target.getHeight() - target.getPaddingBottom();
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "rotation", -90, 0),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                                ObjectAnimator.ofFloat(target, "pivotX", x, x),
                                ObjectAnimator.ofFloat(target, "pivotY", y, y)
                        );
                    }
                };
                return animator;
            case 15:
                // RouteInDownRightAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        float x = target.getWidth() - target.getPaddingRight();
                        float y = target.getHeight() - target.getPaddingBottom();
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "rotation", 90, 0),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                                ObjectAnimator.ofFloat(target, "pivotX", x, x),
                                ObjectAnimator.ofFloat(target, "pivotY", y, y)
                        );
                    }
                };
                return animator;
            case 16:
                // DropOutAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1),
                                Glider.glide(Skill.BounceEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "translationY",
                                        structure.height+structure.margin_bottom, 0))
                        );
                    }
                };
                return animator;
            case 17:
                // DropOutAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "scaleX", 0.45f, 1),
                                ObjectAnimator.ofFloat(target, "scaleY", 0.45f, 1),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1)
                        );
                    }
                };
                return animator;
            case 18:
                // PluseAnimator
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "scaleY", 1, 1.2f, 1),
                                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.2f, 1),
                                ObjectAnimator.ofFloat(target, "alpha", 1, 1)
                        );
                    }
                };
                return animator;
            case 19:
                // 烟雾上腾
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "scaleY", 0, 1f),
                                ObjectAnimator.ofFloat(target, "translationY", 0 ,-structure.height/2.0f),
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1 ,0)
                        );
                    }
                };
                return animator;
            case 20:
                // 测试
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 1f ,1f),
                                ObjectAnimator.ofFloat(target, "translationY", structure.height + structure.margin_bottom,0f )
                        );
                    }
                };
                return animator;
            default:
                animator = new BaseViewAnimator() {
                    @Override
                    protected void prepare(View target) {
                        getAnimatorAgent().playTogether(
                                ObjectAnimator.ofFloat(target, "alpha", 0, 1)
                        );
                    }
                };
                return animator;
        }
    }

    public BaseViewAnimator createAnimator(Context context,View target, ViewModel.AnimationSetting animationSetting, final ViewInfo.PositionInfo structure){

        final List<Animator> objectAnimators = new ArrayList<>();

        if (animationSetting.alpha!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateAlpha(target,animationSetting.alpha,structure));
        }

        if (animationSetting.translateX!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateTranlatinoX(context,target,animationSetting.translateX,structure,animationSetting));
        }

        if (animationSetting.translateY!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateTranlatinoY(context,target,animationSetting.translateY,structure,animationSetting));
        }

        if (animationSetting.rotation!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateRotationZ(target,animationSetting.rotation,structure));
        }else if (animationSetting.rotationX!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateRotationX(target,animationSetting.rotationX,structure));
        }else if (animationSetting.rotationY!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateRotationY(target,animationSetting.rotationY,structure));
        }

        if (animationSetting.scaleX!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateScaleX(context,target,animationSetting.scaleX,structure));
        }

        if (animationSetting.scaleY!=null){
            objectAnimators.add(Cusomer_ViewAnimator_Utils.getInstance().CreateScaleY(context,target,animationSetting.scaleY,structure));
        }

        BaseViewAnimator animator = null;
        final Animator[] animators = create_objectAnimators(objectAnimators);
        if (animators.length>0) {
            animator = new BaseViewAnimator() {
                @Override
                protected void prepare(View target) {
                    getAnimatorAgent().playTogether(animators);
                }
            };
        }

        if (animator == null) {
            animator = new BaseViewAnimator() {
                @Override
                protected void prepare(View target) {
                    getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "alpha", 0f, 1f));
                }
            };
        }
        return animator;

    }

    private Animator[] create_objectAnimators(List<Animator> params){
        Animator[] new_params = new Animator[params.size()];
        for (int i=0;i<params.size();i++){
            new_params[i] = params.get(i);
        }
        return new_params;
    }

}
