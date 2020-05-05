package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<ViewPagerItem> views;

    ViewPagerAdapter(ArrayList<ViewPagerItem> views){
        this.views=views;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        if(views!=null){
            return views.size();
        }
        return 0;
    }

    // 一个Page在切换完成后会调用该方法去加载下一个即将展示的Page
    @Override
    public Object instantiateItem(View view, int position) {
        //System.out.println("ViewPagerAdapter.createItem position is: "+position);
        ((ViewPager)view).addView(views.get(position),0);
        return views.get(position);
    }

    @Override
    public void destroyItem(View view, int position, Object arg2) {
        //System.out.println("ViewPagerAdapter.destroyItem position is: "+position);
        ((ViewPager) view).removeView(views.get(position));
    }

    public static void main(String[] args) {

    }

}
