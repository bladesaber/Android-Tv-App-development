package bladesaber.luckgamev1.Global;

/**
 * Created by bladesaber on 2018/4/9.
 */

public class JsonType {

    public static final String Jump_Into_LuckTable = "LOTTERY_ENTER";
    //{ Type:"..." }

    public static final String Jump_Into_LuckPocker = "LuckPocker_Jump";

    public static final String LuckTable_Start = "LOTTERY_START";

    public static final String LuckTable_End = "LOTTERY_STOP";

    public static final String Luckpocker_id = "LuckPocker_id";

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

    public static final String Remove_Register = "UNBIND";

    public class DataBag {
        private String Type;
        private Object Data;
    }
}
