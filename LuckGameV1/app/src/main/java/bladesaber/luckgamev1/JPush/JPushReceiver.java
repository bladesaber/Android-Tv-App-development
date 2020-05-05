package bladesaber.luckgamev1.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import bladesaber.luckgamev1.AppManager.AppManager;
import bladesaber.luckgamev1.ERCodeActivity.ERCodeActivity;
import bladesaber.luckgamev1.Global.GlobalSignManager;
import bladesaber.luckgamev1.Global.JsonType;
import bladesaber.luckgamev1.HttpClient.PostThread;
import bladesaber.luckgamev1.MainActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by bladesaber on 2018/4/9.
 */

public class JPushReceiver extends BroadcastReceiver {

    private final int GET_REGISTER_STATUS = 200;

    private Context Store_context;
    private Bundle Store_bundle;

    private JPushManager jPushManager = new JPushManager();

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
                        GlobalSignManager.xxx_id = (int)((double)dataMap.get("xxx_id"));
                        GlobalSignManager.Status = GlobalSignManager.Have_Registner;

                        StartERCodePager(Store_context,Store_bundle);
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

                break;

            case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                Log.d("JPushReveiver","NOTIFICATION_RECEIVED, 接受通知");
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
        Log.d("JPushReceiver","Receiver.dataJson:"+Json);
        Gson gson = new Gson();

        Map<String,Object> JsonMap = gson.fromJson(Json, HashMap.class);

        System.out.println("JPushReceiver.JPushReceiver_Message_Handle.Type: "+JsonMap.get("Type"));

        if (((String)JsonMap.get("Type")).equals(GlobalSignManager.Have_Registner)
                && jPushManager!=null && MainActivity.activity!=null){
            Get_RegisterStatus(context);
        }

        switch ((String) JsonMap.get("Type")){
            case JsonType.Tag_Add:
                Log.d("JPushReveiver","Tag_Add");
                if (jPushManager!=null) {
                    jPushManager.JPushManager_AddTag(context, (String) JsonMap.get("Data"));
                }
                break;
            case JsonType.Tag_Set:
                Log.d("JPushReveiver","Tag_Set");
                if (jPushManager!=null) {
                    jPushManager.JPushManager_SetTag(context, (String) JsonMap.get("Data"));
                }
                break;
            case JsonType.Tag_Del:
                Log.d("JPushReveiver","Tag_Del");
                if (jPushManager!=null) {
                    jPushManager.JPushManager_DeleteTag(context, (String) JsonMap.get("Data"));
                }
                break;
            case JsonType.Alia_Set:
                Log.d("JPushReveiver","Alia_Add_Set");
                if (jPushManager!=null) {
                    jPushManager.JPushManager_AddSetAlias(context, (String) JsonMap.get("Data"));
                }
                break;
            case JsonType.Alia_Del:
                Log.d("JPushReveiver","Alia_Del");
                if (jPushManager!=null) {
                    jPushManager.JPushManager_DeleteAlias(context);
                }
                break;
            case JsonType.Tag_Clean:
                Log.d("JPushReveiver","Tag_Clean");
                if (jPushManager!=null) {
                    jPushManager.JPushManager_CleanTag(context);
                }
                break;
            case JsonType.Remove_Register:
                Log.d("JPushReveiver","Remove_Register");
                if (jPushManager!=null && ERCodeActivity.activity!=null && GlobalSignManager.Status.equals(GlobalSignManager.Have_Registner)) {
                    jPushManager.JPushManager_RemoveRegister(context);
                    GlobalSignManager.Status = GlobalSignManager.No_Registner;

                    if (ERCodeActivity.activity != null) {
                        ERCodeActivity.activity.finish();
                    }
                    if (MainActivity.activity == null) {
                        Intent intent1 = new Intent(context, MainActivity.class);
                        intent1.putExtras(bundle);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                }
            default:
                Log.d("JPushOrder","Command Falied");
        }

    }

    private void Get_RegisterStatus(Context context) {
        System.out.println("JPushReceiver.Get_RegisterStatus Start");
        System.out.println("JPushReceiver.Get_RegisterStatus.JPushID: "+ jPushManager.JPush_GetReginsterID(context));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("jpush_id", jPushManager.JPush_GetReginsterID(context));
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread postThread = new PostThread(context,GlobalSignManager.Reginster_Message_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_REGISTER_STATUS;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void StartERCodePager(Context context,Bundle bundle){

        Intent intent1 = new Intent(context, ERCodeActivity.class);
        intent1.putExtras(bundle);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        MainActivity.activity.finish();
    }
}
