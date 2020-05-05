package bladesaber.luckgamev1.SupportCompement;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by bladesaber on 2018/4/21.
 */

public class AnimatorImageView extends ImageView {

    AnimatorSet animatorSet ;

    public AnimatorImageView(Context context){
        super(context);
    }

    public AnimatorImageView(Context context, AttributeSet attrs){
        super(context,attrs);
        Start();
    }

    public AnimatorImageView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    private void InitAnimator(){

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "rotation",0,1080);
        objectAnimator1.setDuration(3000);

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this,"rotation",0,-270);
        objectAnimator2.setDuration(6000);

        animatorSet.playSequentially(objectAnimator1,objectAnimator2);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                System.out.println("AnimatorImageView New Start");
                if (animatorSet!=null) {
                    animatorSet.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void Start(){
        if (animatorSet != null) {
            if (!animatorSet.isStarted()) {
                animatorSet.start();
            }
        }else {
            animatorSet = new AnimatorSet();
            InitAnimator();
            animatorSet.start();
        }
    }

    public void Stop(){
        if(animatorSet != null){
            animatorSet.cancel();
            animatorSet = null;
        }
    }

    public void Destroy(){
        if (animatorSet!=null){
            animatorSet.removeAllListeners();
            animatorSet.cancel();
            animatorSet = null;
        }
    }

}
