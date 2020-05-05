package com.example.bladesaber.tvappvc1.SqlManager;

import android.content.Context;

import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class SqlManager {

    private Realm mRealm;

    public SqlManager(Context context){
        init(context);
    }

    private void init(Context context){
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
        RealmConfiguration myConfig=new RealmConfiguration.Builder()
                .name("myrealm.realm")
                .build();
    }

    public String ReginsterState_get(){
        RealmResults<ReginsterState> reginsterState_list=mRealm.where(ReginsterState.class).findAll();
        return reginsterState_list.get(0).getState();
    }

    public void Reginster_Init(){
        RealmResults<ReginsterState> reginsterState_list=mRealm.where(ReginsterState.class).findAll();
        if (reginsterState_list.isEmpty()) {
            mRealm.beginTransaction();
            ReginsterState reginsterState = mRealm.createObject(ReginsterState.class);
            reginsterState.setState(GlobalSignManager.No_Registner);
            reginsterState.setCustomerID(-1);
            mRealm.commitTransaction();
        }
    }

    public void ReginsterState_Set(final String state){
        final RealmResults<ReginsterState> reginsterState_list=mRealm.where(ReginsterState.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                reginsterState_list.get(0).setState(state);
            }
        });
    }

    public int CustomerID_get(){
        RealmResults<ReginsterState> reginsterStates_list = mRealm.where(ReginsterState.class).findAll();
        return reginsterStates_list.get(0).getCustomerID();
    }

    public void CustomerID_Set(final int ID){
        final RealmResults<ReginsterState> reginsterStates_list = mRealm.where(ReginsterState.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                reginsterStates_list.get(0).setCustomerID(ID);
            }
        });
    }

    /*
    public void Add_Example(String name,String Age){
        mRealm.beginTransaction();
        User user=mRealm.createObject(User.class);
        user.SetName(name);
        user.SetAge(Age);
        mRealm.commitTransaction();
    }

    public void Change_Example(final String name){
        final RealmResults<User> userlist=mRealm.where(User.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                userlist.get(0).SetName(name);
            }
        });
    }

    public void Del_Example(){
        final RealmResults<User> userlist=mRealm.where(User.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                userlist.get(0).deleteFromRealm();
            }
        });
    }

    public RealmResults<User> Search_Example(){
        RealmResults<User> userList = mRealm.where(User.class).equalTo("Name","blade").findAll();
        return userList;
    }
    */

    public void destroy(){
        mRealm.close();
    }

    public void RegistnerStaate_SQL_Reset(){
        ReginsterState_Set(GlobalSignManager.No_Registner);
        CustomerID_Set(-1);
    }

}
