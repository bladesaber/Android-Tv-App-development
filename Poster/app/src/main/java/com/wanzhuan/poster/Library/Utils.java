package com.wanzhuan.poster.Library;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class Utils {

    WindowManager wm ;
    DisplayMetrics dm ;

    private double ScreenWidth;
    private double ScreenHeight;
    private float ScreenDensity;

    private static Utils utils;

    public static Utils getInstance(Context context){
        if (utils==null){
            utils = new Utils(context);
        }
        return utils;
    }

    private Utils(Context context){
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        GetScreenInfo();
    }

    private void GetScreenInfo(){

        ScreenWidth = dm.widthPixels;         // 屏幕宽度（像素）

        ScreenHeight = dm.heightPixels;       // 屏幕高度（像素）

        ScreenDensity = dm.density;           // 像素密度

        System.out.println("Screen Width is: " + ScreenWidth);
        System.out.println("Screen Height is: " + ScreenHeight);
        System.out.println("Screen Density is: " + ScreenDensity);

    }

    public double getScreenWidth(){
        return ScreenWidth;
    }

    public double getScreenHeight(){
        return ScreenHeight;
    }

    public float getScreenDensity(){
        return ScreenDensity;
    }

    public float calculate_width(float distance,float standard_density,float standard_width){
        float ratio1 = getScreenDensity()/standard_density;
        float ratio2 = (float) getScreenWidth()/standard_width;
        return distance * ratio1 * ratio2;
    }

    public float calculate_height(float distance,float standard_density,float standard_height){
        float ratio1 = getScreenDensity()/standard_density;
        float ratio2 = (float) getScreenHeight()/standard_height;
        return distance * ratio1 * ratio2;
    }

}
