package com.wanzhuan.poster.Library.easyLibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.airbnb.lottie.LottieAnimationView;
import com.wanzhuan.poster.DataStructure.ViewModel;
import com.wanzhuan.poster.HttpClient.GetThread;

/**
 * Created by bladesaber on 2018/7/13.
 */

public class MyLottiesAnimationView extends LottieAnimationView {

    private Context context;

    private boolean downloaded = false;
    private LottieAnimationView lottieAnimationView;

    private ViewModel.LottieSetting lottieSetting;

    private boolean destroyed = false;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lottieAnimationView.setAlpha(1.0f);
            lottieAnimationView.playAnimation();
        }
    };

    public MyLottiesAnimationView(Context context, ViewModel.LottieSetting lottieSetting){
        super(context);
        this.context = context;
        lottieAnimationView = this;
        this.lottieSetting = lottieSetting;
    }

    public void start(){
        if (!downloaded) {
            GetThread getThread = new GetThread(context, lottieSetting.json_url) {
                @Override
                public void Handle_Response(String Result) {
                    super.Handle_Response(Result);
                    lottieAnimationView.setAnimationFromJson(Result);
                    downloaded = true;

                    if (lottieSetting.repetition){
                        lottieAnimationView.loop(true);
                    }

                    Looper.sendEmptyMessageDelayed(0,lottieSetting.delay_time);
                }
            };
            getThread.start();
        }else {
            Looper.sendEmptyMessageDelayed(0,lottieSetting.delay_time);
            if (lottieSetting.repetition){
                lottieAnimationView.loop(true);
            }
        }
        destroyed = false;
    }

    public void stop(){
        this.pauseAnimation();
    }

    public void change(String url){
        GetThread getThread = new GetThread(context, lottieSetting.json_url) {
            @Override
            public void Handle_Response(String Result) {
                super.Handle_Response(Result);
                lottieAnimationView.setAnimationFromJson(Result);
            }
        };
        getThread.start();
    }

    public void destroy(){
        if (destroyed=false) {
            this.cancelAnimation();
        }
        destroyed = true;
    }

}
