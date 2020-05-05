package com.wanzhuan.poster.LayoutCheck;

import android.content.Context;
import android.os.Message;

import com.wanzhuan.poster.Global.GlobalSignManager;
import com.wanzhuan.poster.HttpClient.PostThread;
import com.wanzhuan.poster.Library.Utils;

import org.json.JSONObject;

/**
 * Created by bladesaber on 2018/6/29.
 */

public class LayoutCheckManager {

    private static LayoutCheckManager manager = null;

    public static LayoutCheckManager getInstance(){
        if (manager==null){
            manager = new LayoutCheckManager();
        }
        return manager;
    }

    public boolean check_Height(Context context,int distance){
        if (distance> Utils.getInstance(context).getScreenHeight()){
            return false;
        }
        return true;
    }

    public boolean check_Width(Context context,int distance){
        if (distance> Utils.getInstance(context).getScreenWidth()){
            return false;
        }
        return true;
    }

    public boolean check_Zero(int distance){
        if (distance<0){
            return false;
        }
        return true;
    }

    public void send_Error(Context context, JSONObject jsonObject){
        //PostThread postThread = new PostThread(context, GlobalSignManager.Error_Layout_Check_Url,jsonObject);
        //postThread.start();
    }

}
