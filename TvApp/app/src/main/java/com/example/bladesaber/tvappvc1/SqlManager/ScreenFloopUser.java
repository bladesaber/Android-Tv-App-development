package com.example.bladesaber.tvappvc1.SqlManager;

/**
 * Created by bladesaber on 2018/5/10.
 */

public class ScreenFloopUser {

    private String ImageUrl;
    private String PlayerName;
    private String PlayerIcon_Url;
    private String ShowText;
    private int time;

    private int Type;

    public void setImageUrl(String url){
        ImageUrl = url;
    }

    public void setPlayerName(String Name){
        PlayerName = Name;
    }

    public void setPlayerIcon_Url(String url){
        PlayerIcon_Url = url;
    }

    public void setShowText(String Text){
        ShowText = Text;
    }

    public void setTime(int Time){
        time = Time;
    }

    public void setType(int type){
        Type = type;
    }

    public String getImageUrl(){
        return ImageUrl;
    }

    public String getPlayerName(){
        return PlayerName;
    }

    public String getPlayerIcon_Url(){
        return PlayerIcon_Url;
    }

    public String getShowText(){
        return ShowText;
    }

    public int getTime(){
        return time;
    }

    public int getType(){
        return Type;
    }

}
