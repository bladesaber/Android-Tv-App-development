package bladesaber.luckgamev1.SupportCompement;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import bladesaber.luckgamev1.R;

/**
 * Created by bladesaber on 2018/4/11.
 */

public class MyPopupWindow extends PopupWindow {

    private Context mContext;
    private float mShowAlpha = 0.88f;
    private Drawable mBackgroundDrawable;

    private TextView textView;
    private String PresentText = "";

    public MyPopupWindow(Context context) {
        this.mContext = context;
        initBasePopupWindow();
    }

    /**
     * 初始化BasePopupWindow的一些信息
     * */
    private void initBasePopupWindow() {
        setAnimationStyle(android.R.style.Animation_Dialog);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);  //默认设置outside点击无响应
        setFocusable(false);
    }

    @Override
    public void setContentView(View contentView) {
        if(contentView != null) {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            // Update View
            textView = (TextView) contentView.findViewById(R.id.PopupWindows_TextView);
            textView.setText(PresentText);

            super.setContentView(contentView);
        }
    }

    public void setPresentText(String myPresent){
        PresentText = myPresent;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        showAnimator().start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dismissAnimator().start();
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     * */
    private ValueAnimator showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mShowAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                //setWindowBackgroundAlpha(alpha);
                setWindowBackgroundAlpha((float)0.5);
            }
        });
        animator.setDuration(360);
        return animator;
    }

    /**
     * 窗口隐藏，窗口背景透明度渐变动画
     * */
    private ValueAnimator dismissAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(mShowAlpha, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                //setWindowBackgroundAlpha(alpha);
                setWindowBackgroundAlpha((float)0.5);
            }
        });
        animator.setDuration(360);
        return animator;
    }

    /**
     * 控制窗口背景的不透明度
     * */
    private void setWindowBackgroundAlpha(float alpha) {
        Window window = ((Activity)getContext()).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

    //  使用方法
    // popupWindow = new MyPopupWindow(this);
    // popupWindow.setPresentText(string)
    // popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.popup_item, null));
    // popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
    // View rootview = LayoutInflater.from(PopupWindowsTest.this).inflate(R.layout.activity_popup_windows_test, null);
    // popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
}
