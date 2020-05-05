package com.wanzhuan.poster.Global;

/**
 * Created by bladesaber on 2018/6/29.
 */

public class GlobalSignManager {

    public static int App_Code = 5 ;

    public static final String Medel_Url = "https://poster.3128play.com/api/merchant-live";
    //"http://192.168.31.123/OwnTest/public/api/wtf";

    public static final String Reginster_Message_Url = "https://dc.3128play.com/api/tv/device/check";

    public static String Error_Layout_Check_Url = "";

    public static boolean Erro_Layout_Check = true;

    public static int xxx_id = -99;

    public static final String Protocal_Url = "";

    //----------------------------------------------------------------------------------------------
    private static GlobalSignManager globalSignManager = null;

    public static GlobalSignManager getInstance(){
        if (globalSignManager==null){
            globalSignManager = new GlobalSignManager();
        }
        return globalSignManager;
    }

    private String device_token = "";

    public void setDevice_token(String token){
        device_token = token;
    }

    public String getDevice_token(){
        return device_token;
    }

}
