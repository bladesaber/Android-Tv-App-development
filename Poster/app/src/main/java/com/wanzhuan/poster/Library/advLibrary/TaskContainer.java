package com.wanzhuan.poster.Library.advLibrary;

import android.os.Handler;
import android.os.Message;

/**
 * Created by bladesaber on 2018/6/27.
 */

public abstract class TaskContainer {

    private boolean open = false;

    private Handler TimeClicker = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ResloveTask();

            if (open){
                TimeClicker.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    abstract void ResloveTask();

    public void start(){
        if (!open) {
            open = true;
            TimeClicker.sendEmptyMessage(1);
        }
    }

    public void stop(){
        open = false;
    }

}
