package com.wanzhuan.poster.Library.easyLibrary;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;
import com.wanzhuan.poster.DataStructure.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class PosterManager {

    private static PosterManager posterManager = null;

    private ViewGroup parent;

    private boolean open = false;
    private int reStartTime = 1000000;

    private List<AnimationTask> tasks = new ArrayList<>();

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    public static PosterManager getInstance(){
        if (posterManager==null){
            posterManager = new PosterManager();
        }
        return posterManager;
    }

    public void setParent(ViewGroup parent){
        this.parent = parent;
    }

    private Runnable timeTask = new Runnable() {
        @Override
        public void run() {
            if (open) {
                start();
                Looper.postDelayed(timeTask,reStartTime);
            }
        }
    };

    private void start(){
        for (AnimationTask task:tasks){
            task.start();
        }
    }

    public List<AnimationTask> getTasks(){
        return tasks;
    }

    public void start(int reStartTime){
        if (!open) {
            open = true;
            start();
            this.reStartTime = reStartTime;
            Looper.postDelayed(timeTask, reStartTime);
        }
    }

    public void add(int animator_id, View targetView, int delayTime, int duration, ViewInfo.PositionInfo structure,boolean restart){
        AnimationTask animationTask = new AnimationTask();
        animationTask.animator_id = animator_id;
        animationTask.targetView = targetView;
        animationTask.delayTime = delayTime;
        animationTask.duration = duration;
        animationTask.structure = structure;
        animationTask.restart = restart;
        tasks.add(animationTask);
    }

    public void add(Context context,ViewModel viewModel){

        View view = null;
        ViewInfo.PositionInfo positionInfo = null;
        if (viewModel.type.equals("image")){
            positionInfo = DataMaker.getInstance().createPositionInfo(context,viewModel.w,viewModel.h,viewModel.x,viewModel.y);
            view = DataMaker.getInstance().createImageView(context,viewModel.w,viewModel.h,viewModel.x,viewModel.y,viewModel.image_url);
        }else {
            positionInfo = DataMaker.getInstance().createPositionInfo(context,viewModel.w,viewModel.h,viewModel.x,viewModel.y);
            view = DataMaker.getInstance().createTextView(context,viewModel.w,viewModel.h,viewModel.x,viewModel.y,
                    viewModel.text,viewModel.text_setting.color,viewModel.text_setting.font_size,
                    viewModel.text_setting.type,viewModel.text_setting.font_style,
                    viewModel.text_setting.line_space,viewModel.text_setting.letter_space);
        }
        view.setTag(false);

        for (ViewModel.AnimationSetting animationSetting:viewModel.animations){
            AnimationTask animationTask = new AnimationTask();
            animationTask.animator_id = animationSetting.type;
            animationTask.targetView = view;
            animationTask.duration = animationSetting.duration;
            animationTask.delayTime = animationSetting.delay_time;
            animationTask.restart = animationSetting.repetition;
            animationTask.structure = positionInfo;
            tasks.add(animationTask);
        }
    }

    public void remove(int id){
        tasks.remove(id);
    }

    private void stop(){
        open = false;
    }

    public class AnimationTask{
        int animator_id;
        View targetView;
        int delayTime;
        int duration;
        ViewInfo.PositionInfo structure;
        boolean restart;

        private YoYo.YoYoString rope = null;

        public void start(){
            if (targetView!=null) {
                if (rope == null) {

                    if (!(boolean) targetView.getTag()) {
                        parent.addView(targetView);
                        targetView.setTag(true);
                    }

                    rope = library_Load(targetView,
                            duration,
                            animator_id,
                            delayTime,
                            structure,
                            restart);
                    setTransparent();
                } else {
                    rope.stop();
                    rope = library_Load(targetView,
                            duration,
                            animator_id,
                            delayTime,
                            structure,
                            restart);
                    setTransparent();
                }
            }
        }

        public void setTransparent(){
            targetView.setAlpha(0.0f);
        }

    }

    private YoYo.YoYoString library_Load(View targetView, int duration, int animator_id, int delay, ViewInfo.PositionInfo structure,boolean restart){
        if (restart) {
            BaseViewAnimator viewAnimator = null;
            viewAnimator = AnimatorGenerator.getInstance().createAnimator(animator_id, structure);
            YoYo.YoYoString rope = YoYo.with(viewAnimator)
                    .delay(delay)
                    .repeat(YoYo.INFINITE)
                    .duration(duration)
                    .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                    .interpolate(new AccelerateDecelerateInterpolator())
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
            BaseViewAnimator viewAnimator = null;
            viewAnimator = AnimatorGenerator.getInstance().createAnimator(animator_id, structure);
            YoYo.YoYoString rope = YoYo.with(viewAnimator)
                    .delay(delay)
                    .duration(duration)
                    .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                    .interpolate(new AccelerateDecelerateInterpolator())
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

    public void onDestroy(){
        open = false;
        Looper.removeCallbacks(timeTask);
    }

}
