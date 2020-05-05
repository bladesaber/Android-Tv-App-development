package com.wanzhuan.poster.Tool;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;

/**
 * Created by bladesaber on 2018/7/21.
 */

public class TimerClicker {

    private Calendar calendar;

    private TimerClickerListener listener;

    private boolean open = false;

    private final int CHECK_TIME = 200;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case CHECK_TIME:

                    if (open){

                        System.out.println("TimerClicker.Handler");
                        if (listener!=null){
                            listener.HandleProgress(getDay(),getHour(),getMinute());
                        }

                        sendEmptyMessageDelayed(CHECK_TIME,300*1000);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private static TimerClicker timerClicker = null;

    public static TimerClicker getInstance(){
        if (timerClicker == null){
            timerClicker = new TimerClicker();
        }
        return timerClicker;
    }

    public void start(){
        open = true;
        Looper.sendEmptyMessage(CHECK_TIME);
    }

    public void stop(){
        Looper.removeCallbacksAndMessages(null);
        open = false;
    }

    public void setListener(TimerClickerListener listener){
        this.listener = listener;
    }

    public int getHour(){
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getDay(){
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMinute(){
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    private void TextExample(){
        calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        System.out.println("Calendar获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
    }

    public interface TimerClickerListener{
        public void HandleProgress(int day,int hour,int minutes);
    }

}
