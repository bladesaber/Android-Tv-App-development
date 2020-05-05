package com.example.bladesaber.tvappvc1.Renderer;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class CartonManager {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;


    public final static String ALPHA = "ALPHA";
    public final static String SCALE = "SCALE";
    public final static String ROLL = "ROLL";
    public final static String DEEPPAGE = "DEEPPAGE";
    public final static String ZOOMOUTPAGE = "ZOOMOUTPAGE";

    public void CartonManager_SetCartonModel(String style,ViewPager viewPager){
        switch (style) {
            case ALPHA:
                // 渐变
                viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View page, float position) {

                        page.setRotationY(0);
                        page.setScaleX(1);
                        page.setScaleY(1);
                        page.setTranslationX(0);

                        float normalizedposition = Math.abs(Math.abs(position) - 1);
                        page.setAlpha(normalizedposition);
                    }
                });
                break;
            case SCALE:
                // 形状更换
                viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View page, float position) {

                        page.setAlpha(1);
                        page.setRotationY(0);
                        page.setTranslationX(0);

                        float normalizedposition2 = Math.abs(Math.abs(position) - 1);
                        page.setScaleX(normalizedposition2 / 2 + 0.5f);
                        page.setScaleY(normalizedposition2 / 2 + 0.5f);
                    }
                });
                break;
            case ROLL:
                // 旋转更换
                viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View page, float position) {
                        page.setAlpha(1);
                        page.setScaleX(1);
                        page.setScaleY(1);
                        page.setTranslationX(0);

                        page.setRotationY(position * 90);
                    }
                });
                break;
            case DEEPPAGE:
                viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View view, float position) {

                        view.setAlpha(1);
                        view.setScaleX(1);
                        view.setScaleY(1);
                        view.setRotationY(0);

                        int pageWidth = view.getWidth();

                        if (position < -1) { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            view.setAlpha(0);

                        } else if (position <= 0) { // [-1,0]
                            // Use the default slide transition when moving to the left page
                            view.setAlpha(1);
                            view.setTranslationX(0);
                            view.setScaleX(1);
                            view.setScaleY(1);

                        } else if (position <= 1) { // (0,1]
                            // Fade the page out.
                            view.setAlpha(1 - position);

                            // Counteract the default slide transition
                            view.setTranslationX(pageWidth * -position);

                            // Scale the page down (between MIN_SCALE and 1)
                            float scaleFactor = 0.75f + (1 - 0.75f) * (1 - Math.abs(position));
                            view.setScaleX(scaleFactor);
                            view.setScaleY(scaleFactor);

                        } else { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            view.setAlpha(0);
                        }
                    }
                });
                break;
            case ZOOMOUTPAGE:
                viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View view, float position) {
                        int pageWidth = view.getWidth();
                        int pageHeight = view.getHeight();

                        Log.e("TAG", view + " , " + position + "");

                        if (position < -1)
                        { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            view.setAlpha(0);

                        } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
                        { // [-1,1]
                            // Modify the default slide transition to shrink the page as well
                            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                            if (position < 0)
                            {
                                view.setTranslationX(horzMargin - vertMargin / 2);
                            } else
                            {
                                view.setTranslationX(-horzMargin + vertMargin / 2);
                            }

                            // Scale the page down (between MIN_SCALE and 1)
                            view.setScaleX(scaleFactor);
                            view.setScaleY(scaleFactor);

                            // Fade the page relative to its size.
                            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                        } else
                        { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            view.setAlpha(0);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

}
