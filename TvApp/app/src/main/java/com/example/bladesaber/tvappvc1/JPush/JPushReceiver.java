package com.example.bladesaber.tvappvc1.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.bladesaber.tvappvc1.AppManager.AppManager;
import com.example.bladesaber.tvappvc1.Global.GlobalClassManager;
import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;
import com.example.bladesaber.tvappvc1.Global.JsonType;
import com.example.bladesaber.tvappvc1.HttpClient.PostThread;
import com.example.bladesaber.tvappvc1.MainActivity;
import com.example.bladesaber.tvappvc1.ViewPagerManager.PagerViewActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class JPushReceiver extends BroadcastReceiver {

    private final int GET_REGISTER_STATUS = 200;

    private Context Store_context;
    private Bundle Store_bundle;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_REGISTER_STATUS:

                    System.out.println("JPushReceiver.Handler.GET_REGISTER_STATUS: " + (String)msg.obj);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, Object>>() {}.getType();
                    Map<String , Object> JsonMap = gson.fromJson((String)msg.obj, type);
                    String BackEnd_Reginster_State = (String) JsonMap.get("msg");
                    if (BackEnd_Reginster_State.equals(GlobalSignManager.Have_Registner)){
                        Map<String,Object> dataMap = (Map<String,Object>)JsonMap.get("data");
                        GlobalClassManager.sqlManager.ReginsterState_Set(GlobalSignManager.Have_Registner);
                        GlobalClassManager.sqlManager.CustomerID_Set((int)((double)dataMap.get("xxx_id")));
                        GlobalSignManager.xxx_id = (int)((double)dataMap.get("xxx_id"));
                        StartViewPager(Store_context,Store_bundle);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {

        Store_bundle = intent.getExtras();
        Store_context = context;

        Bundle bundle = intent.getExtras();
        Log.d("JPushReveiver", "Jump into JPushReceiver");
        printBundle(bundle);

        switch (intent.getAction()) {
            case JPushInterface.ACTION_MESSAGE_RECEIVED:
                Log.d("JPushReveiver","MESSAGE_RECEIVED,接受自定义信息");

                JPushReceiver_Message_Handle(context,bundle);
                //JPushReceiver_Message_Jump(context,bundle);

                break;

            case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                Log.d("JPushReveiver","NOTIFICATION_RECEIVED, 接受通知");
                break;

            case JPushInterface.ACTION_NOTIFICATION_OPENED:
                Log.d("JPushReveiver","NOTIFICATION_OPENED,用户点击通知");

                JPushReceiver_Notification_Handle(context,bundle);

                break;

            case JPushInterface.ACTION_RICHPUSH_CALLBACK:
                Log.d("JPushReveiver", "用户收到到RICH PUSH CALLBACK");
                break;

            case JPushInterface.ACTION_REGISTRATION_ID:
                break;
        }
    }

    private void printBundle(Bundle bundle) {
        for (String key : bundle.keySet()) {
            Log.d("Test:" + key, String.valueOf(bundle.get(key)));
        }
    }

    private void JPushReceiver_Message_Handle(Context context,Bundle bundle){

        String Json = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d("PagerViewActivity","Receiver.dataJson:"+Json);
        Gson gson = new Gson();

        Map<String,Object> JsonMap = gson.fromJson(Json, HashMap.class);
        //****************************************************************************************************
        if (GlobalClassManager.sqlManager.ReginsterState_get().equals(GlobalSignManager.No_Registner)) {
            Log.d("JPushReveiver","JPushReveiver.EXTRA_MESSAGE"+bundle.getString(JPushInterface.EXTRA_EXTRA));

            if (((String)JsonMap.get("Type")).equals(GlobalSignManager.Have_Registner)){
                System.out.println("JPushReveiver");
                Get_RegisterStatus(context);

            }
        }

        String Type = (String) JsonMap.get("Type");
        switch (Type){
            case JsonType.Tag_Add:
                Log.d("JPushReveiver","Tag_Add");
                GlobalClassManager.jPushManager.JPushManager_AddTag(context,(String) JsonMap.get("Data"));
                break;
            case JsonType.Tag_Set:
                Log.d("JPushReveiver","Tag_Set");
                GlobalClassManager.jPushManager.JPushManager_SetTag(context,(String) JsonMap.get("Data"));
                break;
            case JsonType.Tag_Del:
                Log.d("JPushReveiver","Tag_Del");
                GlobalClassManager.jPushManager.JPushManager_DeleteTag(context,(String) JsonMap.get("Data"));
                break;
            case JsonType.Alia_Set:
                Log.d("JPushReveiver","Alia_Add_Set");
                GlobalClassManager.jPushManager.JPushManager_AddSetAlias(context,(String) JsonMap.get("Data"));
                break;
            case JsonType.Alia_Del:
                Log.d("JPushReveiver","Alia_Del");
                GlobalClassManager.jPushManager.JPushManager_DeleteAlias(context);
                break;
            case JsonType.Tag_Clean:
                Log.d("JPushReveiver","Tag_Clean");
                GlobalClassManager.jPushManager.JPushManager_CleanTag(context);
                break;
            case JsonType.Remove_Register:
                Log.d("JPushReveiver","Remove_Register");
                GlobalClassManager.jPushManager.JPushManager_RemoveRegister(context);
                GlobalClassManager.sqlManager.RegistnerStaate_SQL_Reset();

                if (PagerViewActivity.activity != null){
                    PagerViewActivity.activity.finish();
                }
                if (MainActivity.activity == null) {
                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.putExtras(bundle);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            default:
                Log.d("JPushOrder","Command Falied");
        }

    }

    private void Get_RegisterStatus(Context context) {
        System.out.println("JPushReceiver.Get_RegisterStatus Start");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("jpush_id", GlobalClassManager.jPushManager.JPush_GetReginsterID(context));
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread postThread = new PostThread(GlobalSignManager.Reginster_Message_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_REGISTER_STATUS;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void StartViewPager(Context context,Bundle bundle){

        Intent intent1 = new Intent(context, PagerViewActivity.class);
        intent1.putExtras(bundle);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        MainActivity.activity.finish();
    }

    private void JPushReceiver_Notification_Handle(Context context,Bundle bundle){

    }
}
