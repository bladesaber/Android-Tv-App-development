package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
 * Created by bladesaber on 2018/4/3.
 */

public class ViewPagerManager {

    //  因为安卓出现自身的进程处理紊乱，所以自己做缓冲区
    public static List<ImageItem> ImageList_Buffer = new ArrayList<ImageItem>();
    public static List<Integer> ImageDel_List_Buffer = new ArrayList<Integer>();
    private static List<ImageItem> Image_Item_List_Buffer = new ArrayList<ImageItem>();

    //-----------------------------------------------------------
    private ViewPager viewPager;
    private Context context;

    public static int Anto = 1;

    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<ViewPagerItem> view_list;
    private ViewPagerScroller pagerScroller;
    public CartonManager cartonManager;

    public BackgroundRenderer backgroundRenderer;

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
            System.out.println("ViewPagerManager.Handler");
            switch (msg.what) {
                case LoopRun:
                    if (Anto == 1) {
                        System.out.println("ViewPagerManager.Handler.LoopRun");
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
                    System.out.println("ViewPagerManager.Handler.UpdateImage : "+(String)msg.obj);
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

    ViewPagerManager(Context mycontext, ViewPager myviewPager, ImageView mybackground){

        context = mycontext;
        viewPager = myviewPager;

        cartonManager = new CartonManager();

        backgroundRenderer = new BackgroundRenderer(context,mybackground);

        view_list = new ArrayList<ViewPagerItem>();

        viewPagerAdapter = new ViewPagerAdapter(view_list);
        pagerScroller = new ViewPagerScroller(context);

        viewPager.setAdapter(viewPagerAdapter);

        cartonManager.CartonManager_SetCartonModel(CartonManager.ALPHA,viewPager);
    }

    public void PagerViewActivity_SetLoopTime(long time){
        TIME_INTERVAL = time;
        if (TIME_INTERVAL< GlobalSignManager.Duration){
            TIME_INTERVAL = 6000;
        }
    }

    public void PagerViewActivity_Start(){
        if (view_list.size()>1) {
            viewPager.setCurrentItem(currentPosition);

            String Url = view_list.get(currentPosition).getUrl();
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
        if (view_list.size()>1) {
            if (position < 0 ) {
                return;
            }else if (position >= view_list.size()){
                viewPager.setCurrentItem(view_list.size()-1);
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
                Log.d("ViewPagerManager","ViewPagerManager_InitPagerView.PageSelected is :"+position);
                currentPosition = position;

                if (ImageList_Buffer.size() != 0) {
                    if ((currentPosition != view_list.size() - 1) && (currentPosition != 0)) {
                        ViewPagerManager_AddViewList(ImageList_Buffer);
                        ImageList_Buffer.removeAll(ImageList_Buffer);
                        if (ImageList_Buffer.size() == 0) {
                            System.out.println("ImageList_Buffer is Zero");
                        }
                    }
                }

                //IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
                if ( (ImageDel_List_Buffer.size() != 0) && ( currentPosition != view_list.size()-1 ) && ( currentPosition != 0 ) ){
                    ViewPagerManager_DelViewList(ImageDel_List_Buffer);
                    ImageDel_List_Buffer.removeAll(ImageDel_List_Buffer);
                    if (ImageDel_List_Buffer.size() == 0){
                        System.out.println("ImageList_Buffer is Zero");
                    }
                }

                System.out.println("ViewPagerManager_InitPagerView.Image_Item_List_Buffer.size: "+Image_Item_List_Buffer.size());
                System.out.println("ViewPagerManager_InitPagerView.currentPosition: "+currentPosition);
                if ( ( Image_Item_List_Buffer.size() != 0 ) && ( currentPosition == 0 ) ){
                    System.out.println("ViewPagerManager_InitPagerView.Image_Item_List_Buffer Solution ");
                    Anto = 0;
                    ViewPagerManager_UpdateViewList(Image_Item_List_Buffer);
                    Image_Item_List_Buffer.removeAll(Image_Item_List_Buffer);
                    if (Image_Item_List_Buffer.size() == 0){
                        System.out.println("Image_Item_List_Buffer is Zero");
                        Anto = 1;
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //        若viewpager滑动未停止，直接返回
                if (state != ViewPager.SCROLL_STATE_IDLE) return;

                if (view_list.size()>1) {
                    if (currentPosition == 0) {
                        //        若当前为第一张，设置页面为倒数第二张
                        viewPager.setCurrentItem(view_list.size() - 2, false);

                    } else if (currentPosition == view_list.size() - 1) {
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

    public void ViewPagerManager_AddViewList(List<ImageItem> image_List){

        Log.d("ViewPagerManager","ViewPagerManager.AddViewList");

        if (!image_List.isEmpty()) {
            if (view_list.size()>1) {
                view_list.remove(0);
                view_list.remove(view_list.size() - 1);
            }

            for (int i = 0; i < image_List.size(); i++) {
                ViewPagerItem viewPagerItem = new ViewPagerItem(context);
                viewPagerItem.SetImage(context, image_List.get(i));
                view_list.add(viewPagerItem);
            }

            if (view_list.size()>1) {
                String Url = view_list.get(0).getUrl();
                ViewPagerItem viewPagerItem_Orignal= new ViewPagerItem(context);
                viewPagerItem_Orignal.SetImage(context,Url);
                viewPagerItem_Orignal.setID(-1);
                view_list.add(viewPagerItem_Orignal);

                ViewPagerItem viewPagerItem_finial = new ViewPagerItem(context);
                viewPagerItem_finial.SetImage(context, image_List.get(image_List.size() - 1));
                viewPagerItem_finial.setID(-1);
                view_list.add(0, viewPagerItem_finial);
            }

            //viewPagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    public void ViewPagerManager_DelViewList(List<Integer> ID_list){

        if (ID_list.size()> 0) {

            if ((view_list.size() - 2 - ID_list.size()) > 1) {

                viewPager.setCurrentItem(1,false);

                if (view_list.size() > 1) {
                    view_list.remove(0);
                    view_list.remove(view_list.size() - 1);
                }

                for (int id : ID_list) {
                    for (int j = 0; j < view_list.size(); j++) {
                        if (view_list.get(j).getID() == id) {
                            view_list.remove(j);
                        }
                    }
                }

                if (view_list.size() > 1) {
                    String Url_Last = view_list.get(0).getUrl();
                    String Url_Origin = view_list.get(view_list.size() - 1).getUrl();

                    ViewPagerItem viewPagerItem_Orignal = new ViewPagerItem(context);
                    viewPagerItem_Orignal.SetImage(context, Url_Last);
                    viewPagerItem_Orignal.setID(-1);
                    view_list.add(viewPagerItem_Orignal);

                    ViewPagerItem viewPagerItem_Finial = new ViewPagerItem(context);
                    viewPagerItem_Finial.SetImage(context, Url_Origin);
                    viewPagerItem_Finial.setID(-1);
                    view_list.add(0, viewPagerItem_Finial);
                }

                viewPager.setAdapter(viewPagerAdapter);

            }else {
                if (view_list.size() > 1) {
                    view_list.remove(0);
                    view_list.remove(view_list.size() - 1);
                }

                for (int id : ID_list) {
                    for (int j = 0; j < view_list.size(); j++) {
                        if (view_list.get(j).getID() == id) {
                            view_list.remove(j);
                        }
                    }
                }

                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
            }
        }
    }

    public void  ViewPagerManager_Get_UpdateViewList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id",GlobalClassManager.sqlManager.CustomerID_get());
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

    public void ViewPagerManager_UpdateViewList(List<ImageItem> image_List){

        Log.d("ViewPagerManager","ViewPagerManager_UpdateViewList");

        if (image_List.isEmpty()){
            System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList "+"Method 3");

            if (view_list.size()==0) {

                ViewPagerItem viewPagerItem = new ViewPagerItem(context);
                viewPagerItem.SetImage(context, R.drawable.logo);
                viewPagerItem.setID(-99);
                view_list.add(viewPagerItem);

                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
                viewPager.setAdapter(viewPagerAdapter);
            }

            //   from one to null
            if (view_list.size() == 1){
                view_list.get(0).SetImage(context,R.drawable.logo);
                view_list.get(0).setID(-99);
            }
        }

        int from_null_to_one = 0;
        if (!image_List.isEmpty()) {

            for (int i=0;i<view_list.size();i++){
                if (view_list.get(i).getID() == -99){
                    from_null_to_one = 1;
                }
            }

            view_list.clear();
            viewPagerAdapter.notifyDataSetChanged();
            System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList.view_list.size is "+view_list.size());
            System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList.from_null_to_one is "+from_null_to_one);

            for (int i = 0; i < image_List.size(); i++) {
                ViewPagerItem viewPagerItem = new ViewPagerItem(context);
                viewPagerItem.SetImage(context, image_List.get(i));
                view_list.add(viewPagerItem);
            }

            if (view_list.size()>1) {
                String Url = view_list.get(0).getUrl();
                ViewPagerItem viewPagerItem_Orignal= new ViewPagerItem(context);
                viewPagerItem_Orignal.SetImage(context,Url);
                viewPagerItem_Orignal.setID(-1);
                view_list.add(viewPagerItem_Orignal);

                ViewPagerItem viewPagerItem_finial = new ViewPagerItem(context);
                viewPagerItem_finial.SetImage(context, image_List.get(image_List.size() - 1));
                viewPagerItem_finial.setID(-1);
                view_list.add(0, viewPagerItem_finial);
            }

            if (view_list.size()>1) {
                System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList "+"Method 1");
                viewPager.setAdapter(viewPagerAdapter);
            }else {
                if (from_null_to_one == 0) {
                    System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList " + "Method 2");
                    viewPagerAdapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(0);
                }else {
                    System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList "+"Method 4");
                    viewPager.setAdapter(viewPagerAdapter);
                    //viewPager.setCurrentItem(0);
                    from_null_to_one = 0;
                }
            }
        }

        if (view_list.size()>0) {
            for (int i = 0; i < view_list.size(); i++) {
                System.out.println("ViewPagerManager.ViewPagerManager_UpdateViewList.ViewList.Url: " + view_list.get(i).getID() + ":" +view_list.get(i).getUrl());
            }
        }
    }

    private void ViewPagerManager_UpdateImageList(String Json){

        System.out.println("ViewPagerManager_UpdateImageList");

        Gson gson = new Gson();
        List<ImageItem> imageItems = gson.fromJson(Json, ImageListJson.class).getData();
        if (view_list.size()>1) {
            System.out.println("ViewPagerManager_UpdateImageList Add Buffer ");
            Image_Item_List_Buffer.addAll(imageItems);
            viewPager.setCurrentItem(0);
        }else if (view_list.size()==0){
            System.out.println("ViewPagerManager_UpdateImageList Init ");
            ViewPagerManager_UpdateViewList(imageItems);
        }else if (view_list.size()==1){
            System.out.println("ViewPagerManager_UpdateImageList viewSize == 1 ");
            ViewPagerManager_UpdateViewList(imageItems);
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
