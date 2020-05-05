package com.example.bladesaber.tvappvc1.Global;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class JsonType {

    public static final String AppMessagePush = "AppMessagePush";
    // { Type:"AppMessagePush" , Data:{ ApkName:"..." , VersionCode:-1 , VersionName:"..." , updateUrl:"..." } }

    public static final String Remove_Register = "UNBIND";
    // { Type:"Remove_Register" }

    public static final String Tag_Add = "Tag_Add";
    // { Type:"Tag_Add" , Data:"...." }

    public static final String Tag_Set = "Tag_Set";
    // { Type:"Tag_Set" , Data:"...."}

    public static final String Tag_Del = "Tag_Del";
    // { Type:"Tag_Del" , Data:"...." }

    public static final String Tag_Clean = "Tag_Clean";
    // { Type:"Tag_Clean" }

    public static final String Alia_Set = "Alia_Set";
    // { Type:"Alia_Set" , Data:"...." }

    public static final String Alia_Del = "Alia_Del";
    // { Type:"Alia_Del"  }

    public static final String Update_Image = "IMAGE_CHANGE";
    // { Type:"Update_Image"  }

    public static final String Update_Image_Setting = "IMAGE_SETTING_CHANGE";

    public static final String Update_Text_Setting = "NOTICE_SETTING_CHANGE";

    public static final String Update_Text_List = "NOTICE_CHANGE";

    public static final String Check_Screen_Floop_Status = "ONSCREEN_CHANGE";

    public static final String Start_Screen_Floop ="ONSCREEN_ORDER" ;

    public class DataBag {
        private String Type;
        private Object Data;
    }
}
