package com.wanzhuan.poster.Library.easyLibrary;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BaseInterpolator;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;
import com.wanzhuan.poster.DataStructure.ViewModel;
import com.wanzhuan.poster.ViewContainer.ViewContainer;

import java.util.List;

/**
 * Created by bladesaber on 2018/6/30.
 */

public class ContainerPosterManager {

    private static ContainerPosterManager posterManager = null;

    private Context context = null;

    public static ContainerPosterManager getInstance(Context context){
        if (posterManager==null){
            posterManager = new ContainerPosterManager(context);
        }
        return posterManager;
    }

    private ContainerPosterManager(Context context){
        this.context = context;
    }

    public void start(ViewContainer container){
        for (ContainerAnimationTask task:container.getTasks()){
            task.start(container);
        }
    }

    public void add(Context context, List<ContainerAnimationTask> tasks, List<ViewModify_Model> view_modifyModels, ViewModel viewModel){

        View view = null;
        ViewInfo.PositionInfo positionInfo = null;

        if (viewModel.lottieSetting!=null){
            view= DataMaker.getInstance().create_LottieView(context,viewModel.w,viewModel.h,viewModel.x,viewModel.y,viewModel.lottieSetting);
        }else {
            if (viewModel.type.equals("image")) {
                positionInfo = DataMaker.getInstance().createPositionInfo(context, viewModel.w, viewModel.h, viewModel.x, viewModel.y);
                view = DataMaker.getInstance().createImageView(context, viewModel.w, viewModel.h, viewModel.x, viewModel.y, viewModel.image_url);
            } else {
                positionInfo = DataMaker.getInstance().createPositionInfo(context, viewModel.w, viewModel.h, viewModel.x, viewModel.y);
                view = DataMaker.getInstance().createTextView(context, viewModel.w, viewModel.h, viewModel.x, viewModel.y,
                        viewModel.text, viewModel.text_setting.color, viewModel.text_setting.font_size,
                        viewModel.text_setting.type,viewModel.text_setting.font_style,
                        viewModel.text_setting.line_space,viewModel.text_setting.letter_space);
            }
        }

        if (view != null) {
            view.setTag(false);

            ViewModify_Model view_modifyModel = new ViewModify_Model();
            view_modifyModel.viewModel = viewModel;
            view_modifyModel.view = view;
            view_modifyModels.add(view_modifyModel);

            if (!(view instanceof MyLottiesAnimationView)) {
                for (ViewModel.AnimationSetting animationSetting : viewModel.animations) {
                    ContainerAnimationTask animationTask = new ContainerAnimationTask();
                    animationTask.targetView = view;
                    animationTask.animationSetting = animationSetting;
                    animationTask.structure = positionInfo;
                    tasks.add(animationTask);
                }
            }else {
                ContainerAnimationTask animationTask = new ContainerAnimationTask();
                animationTask.targetView = view;
                tasks.add(animationTask);
            }
        }
    }

    public class ContainerAnimationTask{
        View targetView;
        ViewModel.AnimationSetting animationSetting;
        ViewInfo.PositionInfo structure;

        private YoYo.YoYoString rope = null;

        private boolean lottie_init = false;

        public void start(ViewGroup parent){
            if (targetView instanceof MyLottiesAnimationView){
                if (!lottie_init){
                    if (!(boolean) targetView.getTag()) {
                        parent.addView(targetView);
                        targetView.setTag(true);
                    }
                    lottie_init = true;
                    ((MyLottiesAnimationView)targetView).start();
                    setTransparent();
                }else {
                    ((MyLottiesAnimationView)targetView).destroy();
                    ((MyLottiesAnimationView)targetView).start();
                    setTransparent();
                }
            }else {
                if (targetView != null) {
                    if (rope == null) {

                        if (!(boolean) targetView.getTag()) {
                            parent.addView(targetView);
                            targetView.setTag(true);
                        }

                        rope = library_Load(context,
                                targetView,
                                animationSetting,
                                structure);
                        setTransparent();
                    } else {
                        if (rope.isRunning()) {
                            rope.stop();
                        }
                        rope = library_Load(context,
                                targetView,
                                animationSetting,
                                structure);
                        setTransparent();
                    }
                }
            }
        }

        public void stop(){
            if (targetView instanceof MyLottiesAnimationView){
                ((MyLottiesAnimationView)targetView).destroy();
            }else {
                if (rope!=null){
                    rope.stop();
                }
            }
        }

        public void setTransparent(){
            targetView.setAlpha(0.0f);
        }

    }

    private YoYo.YoYoString library_Load(Context context,View targetView, ViewModel.AnimationSetting animationSetting, ViewInfo.PositionInfo structure){

        BaseViewAnimator viewAnimator = null;
        if (animationSetting.type<900){
            viewAnimator = AnimatorGenerator.getInstance().createAnimator(animationSetting.type, structure);
        }else {
            viewAnimator = AnimatorGenerator.getInstance().createAnimator(context,targetView,animationSetting, structure);
        }

        BaseInterpolator interpolator = null;
        interpolator = InterpolatorGenerator.getInstance().createInterpolator(animationSetting.Interpolator_id);

        if (animationSetting.repetition) {
            YoYo.YoYoString rope = YoYo.with(viewAnimator)
                    .delay(animationSetting.delay_time)
                    .repeat(YoYo.INFINITE)
                    .duration(animationSetting.duration)
                    .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                    .interpolate(interpolator)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }).playOn(targetView);
            return rope;
        }else {
            YoYo.YoYoString rope = YoYo.with(viewAnimator)
                    .delay(animationSetting.delay_time)
                    .duration(animationSetting.duration)
                    .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                    .interpolate(interpolator)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }).playOn(targetView);
            return rope;
        }
    }

    public void onDestroy(){}
    
}
