package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.example.bladesaber.tvappvc1.AppManager.AppManager;
import com.example.bladesaber.tvappvc1.Global.GlobalClassManager;
import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;
import com.example.bladesaber.tvappvc1.HttpClient.PostThread;
import com.example.bladesaber.tvappvc1.R;
import com.example.bladesaber.tvappvc1.Renderer.BackgroundRenderer;
import com.example.bladesaber.tvappvc1.Renderer.CartonManager;
import com.example.bladesaber.tvappvc1.SqlManager.ImageItem;
import com.example.bladesaber.tvappvc1.SqlManager.ImageListJson;
import com.example.bladesaber.tvappvc1.SqlManager.ImageSetting;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/4/4.
 */

public class ViewPagerManager3 {

    //  因为安卓出现自身的进程处理紊乱，所以自己做缓冲区
    public static List<ImageItem> ImageList_Buffer = new ArrayList<ImageItem>();
    public static List<Integer> ImageDel_List_Buffer = new ArrayList<Integer>();
    private static List<ImageItem> Image_Item_List_Buffer = new ArrayList<ImageItem>();

    //-----------------------------------------------------------
    private ViewPager viewPager;
    private Context context;

    public static int Anto = 1;

    private ViewPagerScroller pagerScroller;
    public CartonManager cartonManager;

    public BackgroundRenderer backgroundRenderer;

    private ArrayList<ViewPagerItem> view_list_contrainer;
    private ViewPagerAdapter viewPagerAdapter_contrainer;

    public int currentPosition = 0;

    private long TIME_INTERVAL = 6000;

    private final int LoopRun = 200;
    private final int UpdateImage = 201;
    private final int UpdateImageSetting = 202;

    //----------------------  Use For Record Special Status
    private int inner_Change_Status = 0;

    //----------------------    Just For Test Delete After Testing
    //private static int[] pictures = { R.mipmap.ic_launcher,R.drawable.a1,R.mipmap.ic_launcher,R.drawable.a4,R.drawable.a5,R.mipmap.ic_launcher};
    //---------------------

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //System.out.println("ViewPagerManager.Handler");
            switch (msg.what) {
                case LoopRun:
                    if (Anto == 1) {
                        //System.out.println("ViewPagerManager.Handler.LoopRun");
                        PagerViewActivity_Show(currentPosition + 1);
                        Message message = new Message();
                        message.what = LoopRun;
                        Looper.sendMessageDelayed(message, TIME_INTERVAL);
                    }else {
                        Message message = new Message();
                        message.what = LoopRun;
                        Looper.sendMessageDelayed(message, TIME_INTERVAL);
                    }
                    break;
                case UpdateImage:
                    //System.out.println("ViewPagerManager.Handler.UpdateImage : "+(String)msg.obj);
                    String Json = (String) msg.obj;
                    ViewPagerManager_UpdateImageList(Json);
                    break;
                case UpdateImageSetting:
                    ViewPagerManager_UpdateImageSetting((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    ViewPagerManager3(Context mycontext, ViewPager myviewPager, ImageView mybackground){

        context = mycontext;
        viewPager = myviewPager;

        cartonManager = new CartonManager();

        backgroundRenderer = new BackgroundRenderer(context,mybackground);

        ArrayList<ViewPagerItem> view_list = new ArrayList<ViewPagerItem>();
        view_list_contrainer = view_list;

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(view_list);
        viewPagerAdapter_contrainer = viewPagerAdapter;

        pagerScroller = new ViewPagerScroller(context);

        viewPager.setAdapter(viewPagerAdapter_contrainer);

        cartonManager.CartonManager_SetCartonModel(CartonManager.ALPHA,viewPager);
    }

    public void PagerViewActivity_SetLoopTime(long time){
        TIME_INTERVAL = time;
        if (TIME_INTERVAL< GlobalSignManager.Duration){
            TIME_INTERVAL = 6000;
        }
    }

    public void PagerViewActivity_Start(){
        if (view_list_contrainer.size()>1) {
            viewPager.setCurrentItem(currentPosition);

            String Url = view_list_contrainer.get(currentPosition).getUrl();
            backgroundRenderer.StartChange(TIME_INTERVAL,Url);

            Message message = new Message();
            message.what = LoopRun;
            Looper.sendMessageDelayed(message,TIME_INTERVAL);
        }else {
            viewPager.setCurrentItem(0);

            Message message = new Message();
            message.what = LoopRun;
            Looper.sendMessageDelayed(message,TIME_INTERVAL);
        }
    }

    private void PagerViewActivity_Show(int position){
        if (view_list_contrainer.size()>1) {
            if (position < 0 ) {
                return;
            }else if (position >= view_list_contrainer.size()){
                viewPager.setCurrentItem(view_list_contrainer.size()-1);
            }else {
                viewPager.setCurrentItem(position);
            }
        }
    }

    public void ViewPagerManager_InitPagerView(){

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("ViewPagerManager","ViewPagerManager_InitPagerView.Position is :"+String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {
                //Log.d("ViewPagerManager","ViewPagerManager_InitPagerView.PageSelected is :"+position);
                currentPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //        若viewpager滑动未停止，直接返回
                if (state != ViewPager.SCROLL_STATE_IDLE) return;

                if (view_list_contrainer.size()>1) {
                    if (currentPosition == 0) {
                        //        若当前为第一张，设置页面为倒数第二张
                        viewPager.setCurrentItem(view_list_contrainer.size() - 2, false);

                    } else if (currentPosition == view_list_contrainer.size() - 1) {
                        //        若当前为倒数第一张，设置页面为第二张
                        viewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
    }

    public void ViewPagerManager_SpeedChange(int Duration){
        pagerScroller.setScrollDuration(Duration);//设置时间，时间越长，速度越慢
        pagerScroller.initViewPagerScroll(viewPager);
    }

    public void  ViewPagerManager_Get_UpdateViewList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id", GlobalClassManager.sqlManager.CustomerID_get());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread getThread = new PostThread(GlobalSignManager.Image_List_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = UpdateImage;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        getThread.start();
    }

    private void Image_List_Null(){

        System.out.println("Image_List_Null");

        if (view_list_contrainer.size()==0) {

            System.out.println("Image_List_Null-origin");

            ViewPagerItem viewPagerItem = new ViewPagerItem(context);
            viewPagerItem.SetImage(context, R.drawable.a1);
            viewPagerItem.setID(-99);
            view_list_contrainer.add(viewPagerItem);

            viewPagerAdapter_contrainer.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
            viewPager.setAdapter(viewPagerAdapter_contrainer);

        }else if (view_list_contrainer.size()==1){
            System.out.println("Image_List_Null from 1 to null");
            view_list_contrainer.get(0).SetImage(context,R.drawable.a1);
            view_list_contrainer.get(0).setID(-99);
            viewPagerAdapter_contrainer.notifyDataSetChanged();
        }

        /*
        if (view_list_contrainer.size()>0) {
            for (int i = 0; i < view_list_contrainer.size(); i++) {
                System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList.ViewList.Url: " + view_list_contrainer.get(i).getID() + ":" +view_list_contrainer.get(i).getUrl());
            }
        }
        */
    }

    private void Image_List_is1(List<ImageItem> image_List){

        System.out.println("Image_List_is1");

        if (view_list_contrainer.size()>1){
            System.out.println("Image_List_is1-sub");

            view_list_contrainer.clear();
            ViewPagerItem viewPagerItem = new ViewPagerItem(context);
            viewPagerItem.SetImage(context, image_List.get(0));
            view_list_contrainer.add(viewPagerItem);
            viewPagerAdapter_contrainer.notifyDataSetChanged();
            viewPager.setCurrentItem(0);

        }else if (view_list_contrainer.size()==1){
            System.out.println("Image_List_is1-add");
            view_list_contrainer.get(0).SetImage(context,image_List.get(0).getUrl());
            viewPagerAdapter_contrainer.notifyDataSetChanged();
            viewPager.setCurrentItem(0);

        }else if (view_list_contrainer.size()==0){
            ViewPagerItem viewPagerItem = new ViewPagerItem(context);
            viewPagerItem.SetImage(context, image_List.get(0));
            viewPagerItem.setID(-99);
            view_list_contrainer.add(viewPagerItem);
            viewPagerAdapter_contrainer.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
        }

        if (view_list_contrainer.size()>0) {
            for (int i = 0; i < view_list_contrainer.size(); i++) {
                System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList.ViewList.Url: " + view_list_contrainer.get(i).getID() + ":" +view_list_contrainer.get(i).getUrl());
            }
        }
    }

    private void Image_List_Bigger1(List<ImageItem> image_List){

        if (image_List.size()>(view_list_contrainer.size()-2)) {
            System.out.println("Image_List_Bigger1-add");
            Handle_Add(image_List);
        }
        if (image_List.size()<=(view_list_contrainer.size()-2)){
            System.out.println("Image_List_Bigger1-sub");

            Handle_Sub(image_List);
        }
        if (view_list_contrainer.size()>0) {
            for (int i = 0; i < view_list_contrainer.size(); i++) {
                System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList.ViewList.Url: " + view_list_contrainer.get(i).getID() + ":" +view_list_contrainer.get(i).getUrl());
            }
        }
    }

    private void Handle_Add(List<ImageItem> image_List){
        view_list_contrainer.clear();
        viewPagerAdapter_contrainer.notifyDataSetChanged();

        for (int i = 0; i < image_List.size(); i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(context);
            viewPagerItem.SetImage(context, image_List.get(i));
            view_list_contrainer.add(viewPagerItem);
        }

        if (view_list_contrainer.size() > 1) {
            String Url = view_list_contrainer.get(0).getUrl();
            ViewPagerItem viewPagerItem_Orignal = new ViewPagerItem(context);
            viewPagerItem_Orignal.SetImage(context, Url);
            viewPagerItem_Orignal.setID(-1);
            view_list_contrainer.add(viewPagerItem_Orignal);

            ViewPagerItem viewPagerItem_finial = new ViewPagerItem(context);
            viewPagerItem_finial.SetImage(context, image_List.get(image_List.size() - 1));
            viewPagerItem_finial.setID(-1);
            view_list_contrainer.add(0, viewPagerItem_finial);
        }

        viewPager.setAdapter(viewPagerAdapter_contrainer);
    }

    private void Handle_Sub(List<ImageItem> image_List){

        ArrayList<ViewPagerItem> view_list = new ArrayList<ViewPagerItem>();
        view_list_contrainer = view_list;

        for (int i = 0; i < image_List.size(); i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(context);
            viewPagerItem.SetImage(context, image_List.get(i));
            view_list.add(viewPagerItem);
        }

        if (view_list.size() > 1) {
            String Url = view_list.get(0).getUrl();
            ViewPagerItem viewPagerItem_Orignal = new ViewPagerItem(context);
            viewPagerItem_Orignal.SetImage(context, Url);
            viewPagerItem_Orignal.setID(-1);
            view_list.add(viewPagerItem_Orignal);

            ViewPagerItem viewPagerItem_finial = new ViewPagerItem(context);
            viewPagerItem_finial.SetImage(context, image_List.get(image_List.size() - 1));
            viewPagerItem_finial.setID(-1);
            view_list.add(0, viewPagerItem_finial);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(view_list);
        viewPagerAdapter_contrainer = viewPagerAdapter;

        viewPager.setAdapter(viewPagerAdapter_contrainer);
    }

    private void ViewPagerManager_UpdateImageList(String Json){

        System.out.println("ViewPagerManager_UpdateImageList");

        Gson gson = new Gson();
        List<ImageItem> imageItems = gson.fromJson(Json, ImageListJson.class).getData();

        if (imageItems.size()==0){
            Image_List_Null();
        }
        if (imageItems.size() == 1){
            Image_List_is1(imageItems);
        }
        if (imageItems.size()>1){
            Image_List_Bigger1(imageItems);
        }

    }

    public void ViewPagerManager_UpdateImageSetting(String Json){
        System.out.println("ViewPagerManager.ViewPagerManager_UpdateImageSetting: "+Json);
        Gson gson = new Gson();
        ImageSetting imageSetting = gson.fromJson(Json, ImageSetting.class);
        System.out.println("ViewPagerManager.ViewPagerManager_UpdateImageSetting runTime Test: "+imageSetting.getInterval());
        PagerViewActivity_SetLoopTime(imageSetting.getInterval()*1000);

        cartonManager.CartonManager_SetCartonModel(imageSetting.getAnimation(),viewPager);

    }

    public void Get_ImageSetting(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id",GlobalClassManager.sqlManager.CustomerID_get());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread postThread = new PostThread(GlobalSignManager.Image_Setting_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = UpdateImageSetting;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    public void Destroy(){
        Looper.removeCallbacksAndMessages(null);
    }

}
