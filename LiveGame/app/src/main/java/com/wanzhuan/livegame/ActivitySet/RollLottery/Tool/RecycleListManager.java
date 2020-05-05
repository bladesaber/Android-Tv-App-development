package com.wanzhuan.livegame.ActivitySet.RollLottery.Tool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by bladesaber on 2018/7/6.
 */

public class RecycleListManager {

    private Context context;

    private RecyclerView recyclerView;
    private List<WinnerItem> winnerItems;
    private RecycleViewAdapter adapter;

    private boolean loopable = false;
    private int duration_time = 2000;

    private int thresold = 8;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (loopable){
                adapter.updateList();
                sendEmptyMessageDelayed(0,duration_time);
            }
        }
    };

    public RecycleListManager(Context context, RecyclerView myRecyclerView, List<WinnerItem> winnerItemList){
        this.context = context;
        this.recyclerView = myRecyclerView;
        this.winnerItems = winnerItemList;

        adapter = new RecycleViewAdapter(context,winnerItems);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);

    }

    public void start(){
        if (!loopable) {
            if (winnerItems.size()>thresold) {
                loopable = true;
                Looper.sendEmptyMessage(0);
            }
        }
    }

    public void stop(){
        loopable = false;
    }

    public void add(WinnerItem winnerItem){

        adapter.add_item(winnerItem);

        //start();
    }

    public void onDestroy(){
        Looper.removeCallbacksAndMessages(null);
    }

}
