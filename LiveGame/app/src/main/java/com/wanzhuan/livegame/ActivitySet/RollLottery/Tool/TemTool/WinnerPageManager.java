package com.wanzhuan.livegame.ActivitySet.RollLottery.Tool.TemTool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import com.wanzhuan.livegame.ActivitySet.RollLottery.RollLotteryActivity;
import com.wanzhuan.livegame.ActivitySet.RollLottery.Tool.WinnerItem;
import com.wanzhuan.livegame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/7/9.
 */

public class WinnerPageManager {

    private ViewPager viewPager;
    private List<WinnerItem> listDatas = new ArrayList<>();//总的数据源

    private int totalPage; //总的页数
    private int mPageSize = 8; //每页显示的最大的数
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private MyGridViewAdpter mMyGridViewAdpter;
    private MyViewPagerAdapter mMyViewPagerAdapter;

    private boolean switcher = false;
    private int current_position = 0;
    private boolean direction_forward = true;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (switcher){
                System.out.println("WinnerPageManager.Looper go");
                viewPager.setCurrentItem(getPosition());
                sendEmptyMessageDelayed(0,5000);
            }
        }
    };

    public WinnerPageManager(ViewPager viewPager){
        this.viewPager = viewPager;
    }

    public void addWinner(Context context,WinnerItem winnerItem){

        listDatas.add(winnerItem);

        if(mMyViewPagerAdapter == null){
            totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
            viewPagerList = new ArrayList<View>();
            for(int i = 0; i < totalPage; i++){
                final GridView gridView = (GridView)View.inflate(context, R.layout.item_viewpager, null);
                mMyGridViewAdpter = new MyGridViewAdpter(context, listDatas, i, mPageSize);
                gridView.setAdapter(mMyGridViewAdpter);
                viewPagerList.add(gridView);
            }
            mMyViewPagerAdapter = new MyViewPagerAdapter(viewPagerList);
            viewPager.setAdapter(mMyViewPagerAdapter);
        }


        mMyGridViewAdpter.notifyDataSetChanged();

        int ceil = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        if(ceil > totalPage){
            totalPage = ceil;
            final GridView gridView = (GridView)View.inflate(context, R.layout.item_viewpager, null);
            mMyGridViewAdpter = new MyGridViewAdpter(context, listDatas, ceil-1, mPageSize);
            gridView.setAdapter(mMyGridViewAdpter);
            viewPagerList.add(gridView);
            mMyViewPagerAdapter.notifyDataSetChanged();
        }

        if (totalPage>1 && !switcher){
            current_position = 0;
            switcher = true;
            Looper.sendEmptyMessageDelayed(0,5000);
        }
    }

    public void stop(){
        switcher = false;
    }

    private int getPosition(){
        if (current_position == totalPage-1){
            direction_forward = false;
        }else {
            direction_forward = true;
        }

        if (direction_forward){
            current_position = current_position + 1;
            return current_position;
        }else {
            current_position = current_position - 1;
            return current_position;
        }
    }

    public void onDestroy(){
        Looper.removeCallbacksAndMessages(null);
    }

}
