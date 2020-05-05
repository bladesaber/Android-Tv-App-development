package com.wanzhuan.livegame.Global;

import android.content.Context;
import android.os.Message;

import com.wanzhuan.livegame.AppManager.AppManager;
import com.wanzhuan.livegame.HttpClient.PostThread;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bladesaber on 2018/7/5.
 */

public class GlobalSignManager {

    private static GlobalSignManager globalSignManager = null;

    public static GlobalSignManager getInstance(){
        if (globalSignManager==null){
            globalSignManager = new GlobalSignManager();
        }
        return globalSignManager;
    }

    //------------------------------------------------------------------------------------
    private int app_code = 6;

    public int getApp_code(){
        return app_code;
    }

    //--------------------------------------------------------------------------------------
    // 上传抽奖结果
    private String Post_Result_Url = "https://dc.3128play.com/api/tv/wall/winner";
            //"http://192.168.31.39/3128play/public/api/tv/wall/winner";

    public String getPost_Result_Url(){
        return Post_Result_Url;
    }

    //-------------------------------------------------------------------------------------------
    private String check_url = "https://dc.3128play.com/api/tv/device/check";
            //"http://192.168.31.39/3128play/public/api/tv/device/check";

    private String device_token = "";

    public void setDevice_token(String token){
        device_token = token;
    }

    public String getDevice_token(){
        return device_token;
    }

    public String get_check_url(){
        return check_url;
    }

    //------------------------------------------------------------------------------------------
    private int xxx_id = -1;

    public void setXxx_id(int id){
        xxx_id = id;
    }

    public int getXxx_id(){
        return xxx_id;
    }

    //--------------------------------------------------------------------------------------------
    public String shower_wall_ercode_url = "https://dc.3128play.com/api/tv/wall/info";
            //"http://192.168.31.39/3128play/public/api/tv/wall/info";

    public String getShower_wall_ercode_url(){
        return shower_wall_ercode_url;
    }

    //---------------------------------------------------------------------------------------------
    public String status_url = "https://dc.3128play.com/api/tv/wall/status";
            //"http://192.168.31.39/3128play/public/api/tv/wall/status";

    public void post_status(Context context,String status){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id", xxx_id);
            jsonObject.put("status", status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(context,status_url,jsonObject);
        postThread.start();
    }

    //----------------------------------------------------------------------------------------------
    public String number_url = "https://dc.3128play.com/api/tv/wall/number";

    public void post_number(Context context,int number){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id", xxx_id);
            jsonObject.put("number", number);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(context,number_url,jsonObject);
        postThread.start();
    }
}
