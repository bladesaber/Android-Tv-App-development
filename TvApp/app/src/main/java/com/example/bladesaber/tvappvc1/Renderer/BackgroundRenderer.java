package com.example.bladesaber.tvappvc1.Renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.bladesaber.tvappvc1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class BackgroundRenderer {

    public static final int PAPER_DISAPPEAR = 1;
    public static final int PAPER_APPEAR = 2;

    private Context context;
    private ImageView imageView;

    private long Innerduration;
    private int Innerid;
    private String InnerUrl;

    public BackgroundRenderer(Context mycontext,ImageView background){
        context = mycontext;
        imageView = background;

    }

    public void StartChange(long duration, final String Url){

        Innerduration = duration;
        InnerUrl = Url;

        //Log.d("BackgroundRender","Here jump into StartChange");

        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.05f);
        alphaAnimation.setDuration(Innerduration/15);
        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Picasso.with(context)
                        .load(Url)
                        .error(R.drawable.a1)
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .transform(new BlurTransformationPassico(context))
                        .into(mTarget);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(alphaAnimation);
    }

    public void StartChange(long duration, final int id){

        Innerduration = duration;
        Innerid = id;

        Log.d("BackgroundRender","Here jump into StartChange");

        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(Innerduration/15);
        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(alphaAnimation);
    }

    final com.squareup.picasso.Target mTarget = new com.squareup.picasso.Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //Log.d("BackgroundRender","onBitmapLoaded call");

            AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.05f,1.0f);
            alphaAnimation1.setDuration(Innerduration/15);
            imageView.setImageBitmap(bitmap);
            imageView.startAnimation(alphaAnimation1);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public void Destroy(){
    }
}
