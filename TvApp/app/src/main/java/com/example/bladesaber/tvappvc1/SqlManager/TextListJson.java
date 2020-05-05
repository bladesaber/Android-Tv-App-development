package com.example.bladesaber.tvappvc1.SqlManager;

import java.util.List;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class TextListJson {

    private List<TextItem> data;

    public List<TextItem> getData(){
        return data;
    }

    public String getText(int id){
        return data.get(id).getContent();
    }

    public int getId(int id){
        return data.get(id).getId();
    }

}
