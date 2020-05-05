package com.example.bladesaber.tvappvc1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.bladesaber.tvappvc1.AppManager.AppManager;
import com.example.bladesaber.tvappvc1.Global.GlobalClassManager;
import com.example.bladesaber.tvappvc1.Global.GlobalFunctionManager;
import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;
import com.example.bladesaber.tvappvc1.HttpClient.PostThread;
import com.example.bladesaber.tvappvc1.ViewPagerManager.PagerViewActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static Activity activity = null;

    //private ImageView imageView;
    //private TextView textView;

    private ImageView imageView;

    private NetworkListener networkListener;

    private final int GET_REGISTER_STATUS = 200;
    private final int GET_PICTURE_PATH = 201;
    private final int GET_JPUSH_ID = 202;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_JPUSH_ID:
                    if (GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext())!=null && !GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()).equals("")){
                        Get_RegisterStatus();
                    }else {
                        Message message = new Message();
                        message.what = GET_JPUSH_ID;
                        Looper.sendMessageDelayed(message, 1000);
                    }
                    break;

                case GET_REGISTER_STATUS:

                    System.out.println("MainActivity.Handler.GET_REGISTER_STATUS: " + (String)msg.obj);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, Object>>() {}.getType();
                    Map<String , Object> JsonMap = gson.fromJson((String)msg.obj, type);

                    String BackEnd_Reginster_State = (String) JsonMap.get("msg");
                    Check_RegisterStatus(BackEnd_Reginster_State,JsonMap);
                    break;
                case GET_PICTURE_PATH:
                    System.out.println("MainActivity.GET_PICTURE_PATH Resulf is: "+ (String)msg.obj);

                    Gson gson2 = new Gson();
                    Type type2 = new TypeToken<Map<String, Object>>() {}.getType();
                    Map<String , String> JsonMap2 = gson2.fromJson((String)msg.obj, type2);

                    Get_QR_Code((String)JsonMap2.get("url"));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);

        InitGlobal();

        InitView();

        //GlobalClassManager.sqlManager.RegistnerStaate_SQL_Reset();

        CheckPerMission(0);

        //TestArea();
    }

    //-------------  Test Area
    private void TestArea(){
        System.out.println("Register ID is : "+ GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()));
    }

    //-------------   Init Global
    private void InitGlobal(){
        GlobalClassManager.InitSqlManager(MainActivity.this);
        GlobalClassManager.InitJPushManager(getBaseContext());
        GlobalClassManager.InitUtils(getBaseContext());
    }

    //-------------   Init
    private void InitView(){
        //imageView = (ImageView) findViewById(R.id.ImageView_MainActivity);
        //textView = (TextView) findViewById(R.id.TextView_MainActivity);

        imageView = (ImageView) findViewById(R.id.QR_Code);

        activity = this;
        networkListener = new NetworkListener();
        InitNetworkListener();

        GlobalClassManager.sqlManager.Reginster_Init();

    }

    private void InitNetworkListener(){
        networkListener = new NetworkListener();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkListener, intentFilter);
    }

    //-------------    Step One
    String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    private void CheckPerMission(int Times){
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if ( !mPermissionList.isEmpty() ) {
            System.out.println("MainActivity.CheckPermission.Times: "+Times);
            if (Times==0) {
                String[] Need_permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(this, Need_permissions, 123);
            }else {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(MainActivity.this);
                mAlertDialog.setTitle("提醒");
                mAlertDialog.setMessage("请到设置(SETUP)->应用->相应App->权限 下开启权限 ");
                mAlertDialog.show();
            }
        }else {
            // Jump to StepTwo
            //Get_RegisterStatus();

            /*
            if (GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext())==null){
                System.out.println("MainActivity.GetJpushID is null ");
            }else if (GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()).equals("")){
                System.out.println("MainActivity.GetJpushID is nothing ");
            }
            */

            if (GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext())!=null && !GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()).equals("")){
                Get_RegisterStatus();
            }else {
                Message message = new Message();
                message.what = GET_JPUSH_ID;
                Looper.sendMessageDelayed(message, 1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("Here Jump into MainActivity.onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPerMission(1);
    }

    //----------------------  Step Two
    private void Get_RegisterStatus() {
        System.out.println("Jpush_id is: " + GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()));
        //System.out.println("IMEI is: " + AppManager.AppManager_GetIMEI(getBaseContext()));
        System.out.println("MAC is: " + AppManager.AppManager_GetMac(getBaseContext()));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("jpush_id", GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()));
            jsonObject.put("app_code",GlobalSignManager.App_Code);
            jsonObject.put("AppKey",GlobalClassManager.jPushManager.getAppKey(getBaseContext()));
            jsonObject.put("PackageName",getPackageName());
            jsonObject.put("UDid",GlobalClassManager.jPushManager.GetUDid(getBaseContext()));
            jsonObject.put("Pixel_Width",GlobalClassManager.utils.GetScreenWidth());
            jsonObject.put("Pixel_Height",GlobalClassManager.utils.GetScreenHeight());
            jsonObject.put("ScreenDesity",GlobalClassManager.utils.getScreenDensity());
            jsonObject.put("Android_VersionCode", Build.VERSION.RELEASE);
            jsonObject.put("name_of_board",Build.BOARD);
            jsonObject.put("Android_System_Company",Build.BRAND);
            jsonObject.put("Device",Build.DEVICE);
            jsonObject.put("Hardware_Compay",Build.HARDWARE);
            jsonObject.put("Product",Build.PRODUCT);
            jsonObject.put("Fingerprint",Build.FINGERPRINT);
            jsonObject.put("Phone_Model",Build.MODEL);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(GlobalSignManager.Reginster_Message_Url,jsonObject){
            public void Handle_Response(String Result){
                System.out.println("MainActivity.Result is: " + Result);
                Message message = new Message();
                message.what = GET_REGISTER_STATUS;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void Check_RegisterStatus(String BackEnd_Register_State,Map<String,Object> JsonMap){
        if (BackEnd_Register_State.equals(GlobalSignManager.Have_Registner)){
            // Jump into Next Acticity

            System.out.println("MainActivity: " + "BackEnd_Register_State == Have_Registner");

            GlobalClassManager.sqlManager.ReginsterState_Set(GlobalSignManager.Have_Registner);
            Map<String,Object> dataMap = (Map<String,Object>)JsonMap.get("data");
            GlobalClassManager.sqlManager.CustomerID_Set((int)((double)dataMap.get("xxx_id")));
            GlobalSignManager.xxx_id = (int)((double)dataMap.get("xxx_id"));

            Intent intent = new Intent(MainActivity.this, PagerViewActivity.class);
            startActivity(intent);
            this.finish();
            return;

        }else if (BackEnd_Register_State.equals(GlobalSignManager.No_Registner)){

            System.out.println("MainActivity: " + "BackEnd_Register_State == No_Registner");

            GlobalClassManager.sqlManager.ReginsterState_Set(GlobalSignManager.No_Registner);
            //Jump into Step Three
            Get_ImagePath();
            return;
        }
    }

    //--------------------------   Step Three
    private void Get_ImagePath(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread posThread = new PostThread(GlobalSignManager.Image_Path_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_PICTURE_PATH;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        posThread.start();
    }

    private void Get_QR_Code(String Url){
        Picasso.with(getBaseContext())
                .load(Url)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .error(R.drawable.error)
                .into(imageView);
    }

    //--------------------------
    @Override
    protected void onDestroy() {

        if (GlobalClassManager.sqlManager.ReginsterState_get().equals(GlobalSignManager.No_Registner)){
            GlobalClassManager.jPushManager.JPush_StopPush(getBaseContext());
        }

        Looper.removeCallbacksAndMessages(null);
        unregisterReceiver(networkListener);
        super.onDestroy();
        activity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("MainActivity.onResume.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            System.out.println("MainActivity.onResume and status change to OnLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("MainActivity.onStop.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            System.out.println("MainActivity.onStop and status change to OffLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }
    }

    private class NetworkListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isNetworkConnected_bool = isNetworkConnected(context);
            Log.d("isNetworkConnected_bool",""+isNetworkConnected_bool);

            if ( !isNetworkConnected_bool ) {
                Intent intent1 = new Intent(context, NetworkError.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }else {
                if (NetworkError.activity != null){
                    NetworkError.activity.finish();
                    CheckPerMission(0);
                }
            }

        }

        public boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
            return false;
        }

    }

}
