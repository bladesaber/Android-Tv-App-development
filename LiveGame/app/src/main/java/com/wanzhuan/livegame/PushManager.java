package com.wanzhuan.livegame;

import android.content.Context;

/**
 * Created by bladesaber on 2018/7/5.
 */

public class PushManager {

    private static PushManager pushManager = null;

    private Context context = null;

    public PushManager getInstance(Context context){
        if (pushManager==null){
            pushManager = new PushManager(context);
        }
        return pushManager;
    }

    private PushManager(Context context){
        this.context = context;
    }

    public void start(){
        //TACMessagingService.getInstance().start(context);
    }

    public void stop(){
        //TACMessagingService.getInstance().stop(context);
    }

    public String getTolen(){
        //return TACMessagingService.getInstance().getToken().getTokenString();
        return "";
    }

}
