package com.wanzhuan.livegame.ShowerWall;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wanzhuan.livegame.DataStructer.UserItem;
import com.wanzhuan.livegame.R;
import com.wanzhuan.livegame.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bladesaber on 2018/7/6.
 */

public class ShowerWallUtils {

    private RelativeLayout container;
    private Context context;

    private int left_margin = 140;
    private int top_margin = 160;

    private int col = 13;
    private int row = 7;

    private int scale_value = 100;
    private int leading = 10;

    private int count = col * row;

    private List<Integer> unused_container = new ArrayList<>();

    public ShowerWallUtils(Context context,RelativeLayout relativeLayout){
        for (int i=0;i<col*row;i++){
            unused_container.add(i);
        }
        container = relativeLayout;
        this.context = context;
    }

    public void add_user(UserItem userItem){
        Random random = new Random();
        int seat = random.nextInt(unused_container.size());
        unused_container.remove(seat);
        int seat_row = seat / col;
        int seat_col = seat % col;

        System.out.println(
                "seat is: "+seat + " seat_row: "+seat_row + " seat_col: "+seat_col
        );

        int top_distance = seat_row * leading + seat_row * scale_value + top_margin;
        int left_distance = seat_col * leading + seat_col * scale_value + left_margin;

        ImageView imageView = new ImageView(context);
        Picasso.with(context)
                .load(userItem.avatar)
                .config(Bitmap.Config.RGB_565)
                .fit()
                .into(imageView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(scale_value,scale_value);

        //params.leftMargin = left_distance;
        //params.topMargin = top_distance;
        params.leftMargin = left_margin;
        params.topMargin = top_margin;

        imageView.setLayoutParams(params);

        AnimatorSet animatorSet = createImageAnimator(imageView,scale_value,top_distance,left_distance);
        container.addView(imageView);
        animatorSet.start();

        //-------------------------------------------------------------------------
        TextView tem_textView = new TextView(context);
        tem_textView.setText(userItem.nick_name+" 签到成功");
        tem_textView.setTextSize(20);
        tem_textView.setGravity(Gravity.VERTICAL_GRAVITY_MASK);
        tem_textView.setTextColor(Color.parseColor("#000000"));
        tem_textView.setBackgroundResource(R.drawable.shower_wall_textbackground);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,100);

        //params2.leftMargin = left_distance + (int)(scale_value *1.2);
        //params2.topMargin = top_distance;

        params2.leftMargin = left_margin + (int)(scale_value*1.6);
        params2.topMargin = top_margin;

        tem_textView.setLayoutParams(params2);
        AnimatorSet animatorSet_text = createTextAnimator(tem_textView,400,left_distance+scale_value);
        container.addView(tem_textView);
        animatorSet_text.start();

    }

    private AnimatorSet createImageAnimator(View imageView,int scaleValue,int topDistance,int leftDistance){
        imageView.setAlpha(0.0f);

        final AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator alpha_objAnmimator = ObjectAnimator.ofFloat(imageView, "alpha", 0, 1,1,1,1);
        setCancel(alpha_objAnmimator);
        ObjectAnimator scaleX_objAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0, 1.8f,1.4f,1.6f);
        setCancel(scaleX_objAnimator);
        ObjectAnimator scaleY_objAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0, 1.8f,1.4f,1.6f);
        setCancel(scaleY_objAnimator);

        animatorSet.setDuration(1000);
        animatorSet.playTogether(alpha_objAnmimator,scaleX_objAnimator,scaleY_objAnimator);

        ObjectAnimator row_objAnimator = ObjectAnimator.ofFloat(imageView, "translationX",0,leftDistance-left_margin);
        setCancel(row_objAnimator);
        ObjectAnimator col_objAnimator = ObjectAnimator.ofFloat(imageView, "translationY",0,topDistance-top_margin);
        setCancel(col_objAnimator);
        ObjectAnimator scaleX_objAnimator2 = ObjectAnimator.ofFloat(imageView, "scaleX", 1.6f, 1f);
        setCancel(scaleX_objAnimator2);
        ObjectAnimator scaleY_objAnimator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1.6f, 1f);
        setCancel(scaleY_objAnimator2);

        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(1000);
        animatorSet2.playTogether(row_objAnimator,col_objAnimator,scaleX_objAnimator2,scaleY_objAnimator2);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        return animatorSet;
    }

    private AnimatorSet createTextAnimator(final View textview, int scaleValue, int leftDistance){
        textview.setAlpha(0.0f);

        ObjectAnimator row_objAnimator;
        row_objAnimator = ObjectAnimator.ofFloat(textview, "translationX", 800, 0);
        row_objAnimator.setDuration(1000);

        ObjectAnimator alpha_objAnmimator = ObjectAnimator.ofFloat(textview, "alpha", 1, 1);
        alpha_objAnmimator.setDuration(1000);

        ObjectAnimator alpha_objAnmimator2 = ObjectAnimator.ofFloat(textview, "alpha", 1, 0);
        alpha_objAnmimator2.setDuration(600);

        final AnimatorSet animatorSet = new AnimatorSet();

        setCancel(alpha_objAnmimator);
        setCancel(row_objAnimator);
        setCancel(alpha_objAnmimator2);

        animatorSet.play(alpha_objAnmimator).with(row_objAnimator).before(alpha_objAnmimator2);

        return animatorSet;
    }

    private Animator setCancel(Animator animator){
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.cancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        return animator;
    }

}
