package com.example.bladesaber.tvappvc1;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class Utils {

    WindowManager wm ;
    DisplayMetrics dm ;

    private double ScreenWidth;
    private double ScreenHeight;
    private float ScreenDensity;

    public Utils(Context context){
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        GetScreenInfo();
    }

    private void GetScreenInfo(){

        ScreenWidth = dm.widthPixels;         // 屏幕宽度（像素）

        ScreenHeight = dm.heightPixels;       // 屏幕高度（像素）

        ScreenDensity = dm.density;

    }

    public double GetScreenWidth(){
        return ScreenWidth;
    }

    public double GetScreenHeight(){
        return ScreenHeight;
    }

    public float getScreenDensity(){
        return ScreenDensity;
    }

}
