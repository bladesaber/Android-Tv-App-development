package com.example.bladesaber.tvappvc1.Global;

import android.content.Context;

import com.example.bladesaber.tvappvc1.JPush.JPushManager;
import com.example.bladesaber.tvappvc1.SqlManager.SqlManager;
import com.example.bladesaber.tvappvc1.Utils;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class GlobalClassManager {

    public static JPushManager jPushManager;
    public static SqlManager sqlManager;
    public static Utils utils;

    public static void InitJPushManager(Context context){
        jPushManager = new JPushManager();
        jPushManager.JPush_Init(context);
        jPushManager.JPush_ResumePush(context);
    }

    public static void InitSqlManager(Context context){
        sqlManager = new SqlManager(context);
    }

    public static void InitUtils(Context context){
        utils = new Utils(context);
    }

}
