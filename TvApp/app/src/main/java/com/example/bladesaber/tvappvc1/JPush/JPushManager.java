package com.example.bladesaber.tvappvc1.JPush;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class JPushManager {

    public static String Style4 = "JPushOrderJson";

    public final static int TAG_ADD = 1;
    public final static int TAG_SET = 2;
    public final static int TAG_DEL = 3;

    public final static int ALIA_ADD_SET = 4;
    public final static int ALIA_DEL = 5;

    public final static int TAG_CLEAN = 6;

    private JPushHelper jPushHelper;

    public JPushManager(){
        jPushHelper = new JPushHelper();
    }

    public void JPush_Init(Context context){
        Log.d("JpushManager","InitPush");
        JPushInterface.init(context);
    }

    public String JPush_GetReginsterID(Context context){
        return JPushInterface.getRegistrationID(context);
    }

    public String GetUDid(Context context){
        return JPushInterface.getUdid(context);
    }

    public String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                appKey = metaData.getString("JPUSH_APPKEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }

    public void JPush_StopPush(Context context){
        Log.d("JpushManager","StopPush");
        JPushInterface.stopPush(context);
    }

    public void JPush_ResumePush(Context context){
        Log.d("JpushManager","ResumePush");
        JPushInterface.resumePush(context);
    }

    public void JPushManager_CleanTag(Context context){
        jPushHelper.handleAction(context,TAG_CLEAN);
    }

    public void JPushManager_AddTag(Context context,String TagSet){
        Log.d("JpushManager","Add Tag");
        Set<String> Tag = jPushHelper.getInputTag(context,TagSet);
        if (Tag == null){
            Toast.makeText(context,"Add Tag Faile",Toast.LENGTH_LONG).show();
            return;
        }
        jPushHelper.handleAction(context,TAG_ADD,Tag);
    }

    public void JPushManager_SetTag(Context context,String TagSet){
        Log.d("JpushManager","Set Tag");
        Set<String> Tag = jPushHelper.getInputTag(context,TagSet);
        if (Tag == null){
            Toast.makeText(context,"Set Tag Faile",Toast.LENGTH_LONG).show();
            return;
        }
        jPushHelper.handleAction(context,TAG_SET,Tag);
    }

    public void JPushManager_DeleteTag(Context context,String TagSet){
        Log.d("JpushManager","Delete Tag");
        Set<String> Tag = jPushHelper.getInputTag(context,TagSet);
        if (Tag == null){
            Toast.makeText(context,"Delete Tag Faile",Toast.LENGTH_LONG).show();
            return;
        }
        jPushHelper.handleAction(context,TAG_DEL,Tag);
    }

    public void JPushManager_AddSetAlias(Context context,String Alias){
        Log.d("JpushManager","Add and Set Alias");
        String Alia = jPushHelper.getInputAlia(context,Alias);
        if (Alia == null){
            Toast.makeText(context,"Add and Set Alia Faile",Toast.LENGTH_LONG).show();
            return;
        }
        jPushHelper.handleAction(context,ALIA_ADD_SET,Alia);
    }

    public void JPushManager_DeleteAlias(Context context){
        Log.d("JpushManager","Delete Alias");
        jPushHelper.handleAction(context,ALIA_DEL,"");
    }

    public void JPushManager_RemoveRegister(Context context){
        JPushManager_CleanTag(context);
        JPushManager_DeleteAlias(context);
    }

}
