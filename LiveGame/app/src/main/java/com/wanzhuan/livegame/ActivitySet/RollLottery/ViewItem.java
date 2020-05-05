package com.wanzhuan.livegame.ActivitySet.RollLottery;

/**
 * Created by bladesaber on 2018/4/16.
 */

public class ViewItem {

    private String Name;
    private String image_url;

    public String getName(){
        return Name;
    }

    public String getImage_url(){
        return image_url;
    }

    public void setName(String myName){
        Name = myName;
    }

    public void setImage_url(String image_url){
        this.image_url = image_url;
    }

    public ViewItem(String name,String imageUrl){
        this.Name = name;
        this.image_url = imageUrl;
    }

    public ViewItem(){}

}
