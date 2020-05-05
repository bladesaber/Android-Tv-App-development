package com.wanzhuan.livegame.ActivitySet.RollLottery;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.wanzhuan.livegame.DataStructer.UserItem;
import com.wanzhuan.livegame.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bladesaber on 2018/7/6.
 */

public class RollLotteryManager {

    private Context context;

    private RecyclerView recyclerView;
    private List<ViewItem> viewItemList = new ArrayList<>();
    private MyRecycleAdapter adapter;
    private ScrollSpeedLinearLayoutManger linearLayoutManager;

    private int position = 0;
    private int duration_speed_init = 100;
    private int duration_speed ;

    // 最大递减速度，当到达此速度则活动停止
    // 此速度应相近于 view宽度 * 每像素滑移时间 ，这样才能保持不卡顿
    private int stop_speed = 500;

    // 每间隔 duration 时间，速度递减一次
    private int speed_down_duration = 1000;

    // 每次速度递减速率
    private int speed_down_pertime = 50;

    private boolean isPlaying;

    private PresentListener presentListener;

    private int mini_time = 1;

    private final int SPEED_DOWN = 201;
    private final int ROLL_NEXT = 202;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ROLL_NEXT:
                    if (duration_speed<stop_speed) {
                        position +=1;
                        recyclerView.smoothScrollToPosition(position);
                        System.out.println("duration_speed is: "+duration_speed+" position is: "+position);
                        Looper.sendEmptyMessageDelayed(ROLL_NEXT, duration_speed);
                    }else {
                        isPlaying = false;
                        int result = present_result();
                        presentListener.getResult(result);
                    }
                    break;
                case SPEED_DOWN:
                    if (duration_speed<stop_speed) {
                        duration_speed += speed_down_pertime;
                        Looper.sendEmptyMessageDelayed(SPEED_DOWN, speed_down_duration);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public RollLotteryManager(Context context,RecyclerView recyclerView,List<ViewItem> viewItems){
        this.context = context;
        this.recyclerView = recyclerView;
        this.viewItemList = viewItems;

        linearLayoutManager = new ScrollSpeedLinearLayoutManger(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyRecycleAdapter(context,viewItemList);
        recyclerView.setAdapter(adapter);

        recyclerView.getItemAnimator().setAddDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);

    }

    //----------------------------------------------------------------------------------------------
    public void start(){
        if (!isPlaying) {
            Random random = new Random();
            position = random.nextInt(viewItemList.size());
            recyclerView.scrollToPosition(position);
            duration_speed = duration_speed_init;
            isPlaying = true;
            Looper.sendEmptyMessage(ROLL_NEXT);

            Random random2 = new Random();
            int random_delay_time = random2.nextInt(50)*100;
            System.out.println("delay time is: "+random_delay_time);
            Looper.sendEmptyMessageDelayed(SPEED_DOWN,random_delay_time);
        }
    }

    public void stop(){
        if (isPlaying) {
            Looper.sendEmptyMessage(SPEED_DOWN);
        }
    }

    private int present_result(){

        //System.out.println("RollLotteryManager.viewItemList.size is: "+viewItemList.size());
        //System.out.println("RollLotteryManager.position is: "+position);
        // 点解要减1 ??
       int result = (position-1) % viewItemList.size();
        return result;
    }

    public void update_playerlist(){
        viewItemList.clear();
        for (UserItem userItem: MainActivity.getUserItemList()){
            ViewItem viewItem = new ViewItem(userItem.nick_name,userItem.avatar);
            viewItemList.add(viewItem);
        }
        adapter.notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    public void setDuration_speed_init(int duration_speed_init){
        this.duration_speed_init = duration_speed_init;
    }

    public void setSpeed_down_duration(int speed_down_suration){
        this.speed_down_duration = speed_down_suration;
    }

    public void setSpeed_down_pertime(int speed_down_pertime){
        this.speed_down_pertime = speed_down_pertime;
    }

    public void setStop_speed(int speed){
        this.stop_speed = speed;
    }

    public void setPresentListener(PresentListener listener){
        this.presentListener = listener;
    }

    //----------------------------------------------------------------------------------------------
    public void onDestroy(){
        Looper.removeCallbacksAndMessages(null);
    }

    //----------------------------------------------------------------------------------------------
    public void set_MILLISECONDS_PER_INCH(float speed){
        linearLayoutManager.setMILLISECONDS_PER_INCH(speed);
    }

    private class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {
        private float MILLISECONDS_PER_INCH = 1f;
        private Context contxt;

        public ScrollSpeedLinearLayoutManger(Context context) {
            super(context);
            this.contxt = context;
        }

        public void setMILLISECONDS_PER_INCH(float addication){
            MILLISECONDS_PER_INCH += addication;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return ScrollSpeedLinearLayoutManger.this.computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH / displayMetrics.density;
                            //返回滑动一个pixel需要多少毫秒
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }

        public void setSpeedSlow() {
            //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
            //0.3f是自己估摸的一个值，可以根据不同需求自己修改
            MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.3f;
        }

        public void setSpeedFast() {
            MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.03f;
        }
    }
}
