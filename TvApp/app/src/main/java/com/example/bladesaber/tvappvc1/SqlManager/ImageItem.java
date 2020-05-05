package com.example.bladesaber.tvappvc1.SqlManager;

import io.realm.RealmObject;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class ImageItem extends RealmObject {

    private int id;
    private String image_url;
    private String title;
    private String subtitle;

    public int getID(){
        return id;
    }

    public String getUrl(){
        return image_url;
    }

}
