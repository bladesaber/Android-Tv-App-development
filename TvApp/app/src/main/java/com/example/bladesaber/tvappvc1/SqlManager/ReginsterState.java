package com.example.bladesaber.tvappvc1.SqlManager;

import io.realm.RealmObject;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class ReginsterState extends RealmObject {

    private String state;
    private int CustomerID;

    public String getState(){
        return state;
    }

    public void setState(String Current_state){
        state=Current_state;
    }

    public int getCustomerID(){
        return CustomerID;
    }

    public void setCustomerID(int myCustomerID){
        CustomerID = myCustomerID;
    }

}
