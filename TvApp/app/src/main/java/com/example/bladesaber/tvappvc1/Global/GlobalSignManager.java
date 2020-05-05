package com.example.bladesaber.tvappvc1.Global;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class GlobalSignManager {

    public static final String No_Registner = "NOT_BIND";
    public static final String Have_Registner = "BIND";

    public static final String Reginster_Message_Url = "https://dc.3128play.com/api/tv/device/check";

    public static final String Image_Path_Url = "https://dc.3128play.com/api/tv/device/getCode";

    public static final String Update_Apk_Message_Url = "http://www.gdqpass.com/test/test2.php";

    public static final int Duration = 4000;

    public static final String Image_List_Url = "https://dc.3128play.com/api/tv/device/images";

    public static final String Image_Setting_Url = "https://dc.3128play.com/api/tv/device/image_setting";

    public static final String Text_List_Url = "https://dc.3128play.com/api/tv/device/notices";

    public static final String Text_Setting_Url = "https://dc.3128play.com/api/tv/device/notice_setting";

    public static final int App_Code = 1;

    public static int xxx_id = 0;

    //----------------------------------------------------------------------------------------------
    public static final String Background = "OFFLINE";

    public static final String Forground = "ONLINE";

    public static String App_Background_Forground_Status = Forground;

    public static final String App_Status_Url = "https://dc.3128play.com/api/tv/device/app/status";

    //----------------------------------------------------------------------------------------------
    public static final String Screen_Floop_Icon = "http://192.168.31.39/3128play/public/api/tv/onscreen/getCode";

    public static final String Open_Screen_Floop = "ON";

    public static final String Close_Screen_Floop = "OFF";

}
