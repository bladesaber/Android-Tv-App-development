package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import com.example.bladesaber.tvappvc1.AppManager.AppManager;
import com.example.bladesaber.tvappvc1.Global.GlobalClassManager;
import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;
import com.example.bladesaber.tvappvc1.HttpClient.PostThread;
import com.example.bladesaber.tvappvc1.SqlManager.TextListJson;
import com.example.bladesaber.tvappvc1.SqlManager.TextSetting;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class AdvertingCarouselManager {

    private Context context;
    private WindowManager windowManager;
    private TextSetting textSetting = new TextSetting();

    private AutoScrollTextView autoScrollTextView_Line1;
    private AutoScrollTextView autoScrollTextView_Line2;
    private AutoScrollTextView autoScrollTextView_Line3;

    private final int GET_TEXT_LIST = 201;
    private final int GET_TEXT_SETTING_LIST = 202;

    private final String TOP = "top";
    private final String MIDDLE = "middle";
    private final String BOTTOM = "bottom";

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_TEXT_LIST:
                    System.out.println("AdvertingCarouselManager.GET_TEXT_LIST " + (String)msg.obj);
                    Gson gson = new Gson();
                    AdvertingCarouselManager_UpDate(gson.fromJson((String)msg.obj,TextListJson.class));
                    break;
                case GET_TEXT_SETTING_LIST:
                    System.out.println("Here jump into Looper.AdvertingCarouselManager.Get_Text_Setting_List");
                    Gson gson1 = new Gson();
                    System.out.println("AdvertingCarouselManager.GET_TEXT_Setting " + (String)msg.obj);
                    textSetting = gson1.fromJson((String) msg.obj,TextSetting.class);
                    AdvertingCarouselManager_UpDate_Setting();
                    Get_TextList();
                    break;
                default:
                    break;
            }
        }
    };

    AdvertingCarouselManager(Context mycontext,
                              AutoScrollTextView my_autoScrollTextView_line1,
                              AutoScrollTextView my_autoScrollTextView_line2,
                              AutoScrollTextView my_autoScrollTextView_line3,
                              WindowManager mywindowManager){
        context = mycontext;
        autoScrollTextView_Line1 = my_autoScrollTextView_line1;
        autoScrollTextView_Line2 = my_autoScrollTextView_line2;
        autoScrollTextView_Line3 = my_autoScrollTextView_line3;
        windowManager = mywindowManager;

        autoScrollTextView_Line1.InitWindowManager(windowManager);
        autoScrollTextView_Line2.InitWindowManager(windowManager);
        autoScrollTextView_Line3.InitWindowManager(windowManager);

        String Tem = "";
        autoScrollTextView_Line1.InitText(Tem);
        autoScrollTextView_Line2.InitText(Tem);
        autoScrollTextView_Line3.InitText(Tem);

    }

    public void AdvertingCarouselManager_UpDate(TextListJson textListJson){
        System.out.println("Here jump into AdvertingCarouselManager.AdvertingCarouselManager_UpDate");
        switch (textSetting.getPosition()){
            case TOP:
                autoScrollTextView_Line1.Set_TextList_Buffer(textListJson.getData());
                autoScrollTextView_Line1.Show_Text_List_Buffer();

                autoScrollTextView_Line2.RemoveAllText();
                autoScrollTextView_Line3.RemoveAllText();

                break;
            case MIDDLE:
                autoScrollTextView_Line2.Set_TextList_Buffer(textListJson.getData());
                autoScrollTextView_Line2.Show_Text_List_Buffer();

                autoScrollTextView_Line1.RemoveAllText();
                autoScrollTextView_Line3.RemoveAllText();

                break;
            case BOTTOM:
                autoScrollTextView_Line3.Set_TextList_Buffer(textListJson.getData());
                autoScrollTextView_Line3.Show_Text_List_Buffer();

                autoScrollTextView_Line1.RemoveAllText();
                autoScrollTextView_Line2.RemoveAllText();

                break;
            default:
                Log.d("AdvertingCarouselManage","Out of Line");
                break;
        }
    }

    public void AdvertingCarouselManager_UpDate_Setting(){
        System.out.println("Here jump into AdvertingCarouselManager.AdvertingCarouselManager_UpDate_Setting()");
        switch (textSetting.getPosition()){
            case TOP:
                SettingChange(autoScrollTextView_Line1);
                break;
            case MIDDLE:
                SettingChange(autoScrollTextView_Line2);
                break;
            case BOTTOM:
                SettingChange(autoScrollTextView_Line3);
                break;
        }
    }

    private void SettingChange(AutoScrollTextView autoScrollTextView){

        System.out.println("AdvertingCarouselManager2.FontSize is : "+ textSetting.getFont_size());
        autoScrollTextView.Set_TextSize(textSetting.getFont_size());

        System.out.println("AdvertingCarouselManager2.colore is : "+ textSetting.getColor());
        autoScrollTextView.Set_TextColor(textSetting.getColor());

        System.out.println("AdvertingCarouselManager2.hidding is : "+ textSetting.getIs_hidden());
        autoScrollTextView.Set_Whether_visiable(textSetting.getIs_hidden());

        System.out.println("AdvertingCarouselManager2.speed is : "+ textSetting.getSpeed());
        autoScrollTextView.SetSpeed(textSetting.getSpeed());
    }

    public void AdvertinrCarouselManager_Start(){
        autoScrollTextView_Line1.startScroll();
        autoScrollTextView_Line2.startScroll();
        autoScrollTextView_Line3.startScroll();
    }

    public void AdvertinrCarouselManager_Stop(){
        autoScrollTextView_Line1.stopScroll();
        autoScrollTextView_Line2.stopScroll();
        autoScrollTextView_Line3.stopScroll();
    }

    public void Get_TextList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id",GlobalClassManager.sqlManager.CustomerID_get());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread getThread = new PostThread(GlobalSignManager.Text_List_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_TEXT_LIST;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        getThread.start();
    }

    public void Get_Text_Setting(){
        System.out.println("Here jump into AdvertingCarouselManager.Get_Text_Setting()");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id",GlobalClassManager.sqlManager.CustomerID_get());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread getThread = new PostThread(GlobalSignManager.Text_Setting_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_TEXT_SETTING_LIST;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        getThread.start();
    }

}