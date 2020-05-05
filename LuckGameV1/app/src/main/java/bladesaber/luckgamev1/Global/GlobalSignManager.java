package bladesaber.luckgamev1.Global;

import bladesaber.luckgamev1.DataStructure.SigUser;

/**
 * Created by bladesaber on 2018/4/9.
 */

public class GlobalSignManager {

    public static String Landing_Message_Url = "http://www.gdqpass.com/test/test4.php";

    public static String LuckTable_Setting_Url = "http://www.gdqpass.com/test/test5.php";

    public static String LuckTable_Result_Url = "http://...";

    public static String LuckPocker_Setting_Url = "http://www.gdqpass.com/test/test5.php";

    public static String AppUpdate_Url = "http://...";

    //------------------------------------------------------------------------------------------
    public static final String No_Registner = "NOT_BIND";

    public static final String Have_Registner = "BIND";

    public static final String Reginster_Message_Url = "https://dc.3128play.com/api/tv/device/check";

    public static final String Image_Path_Url = "https://dc.3128play.com//api/tv/device/getCode";

    //--------------------------------------------------------------------------------------------
    public static final String Table_Message = "https://dc.3128play.com/api/tv/lottery/info";

    public static final String Table_Go_Image = "https://dc.3128play.com/api/tv/lottery/getCode";

    public static final String UserList = "https://dc.3128play.com/api/tv/lottery/record";

    //----------------------------------------------------------------------------------------------
    public static int xxx_id = -1;

    public static final String GameType = "dial";

    public static final int App_Code = 2;

    public static String Status = Have_Registner;

    //----------------------------------------------------------------------------------------------
    public static final String Win = "WIN";

    public static final String No_Win = "NOT_WIN";

    //----------------------------------------------------------------------------------------------
    public static int ActivityCount = 0;

    public static final String Background = "OFFLINE";

    public static final String Forground = "ONLINE";

    public static String App_Background_Forground_Status = Forground;

    public static final String App_Status_Url = "https://dc.3128play.com/api/tv/device/app/status";

}
