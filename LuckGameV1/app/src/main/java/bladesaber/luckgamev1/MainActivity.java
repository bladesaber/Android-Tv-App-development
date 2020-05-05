package bladesaber.luckgamev1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bladesaber.luckgamev1.AppManager.AppManager;
import bladesaber.luckgamev1.ERCodeActivity.ERCodeActivity;
import bladesaber.luckgamev1.Global.GlobalFunctionManager;
import bladesaber.luckgamev1.Global.GlobalSignManager;
import bladesaber.luckgamev1.HttpClient.PostThread;
import bladesaber.luckgamev1.JPush.JPushManager;

public class MainActivity extends AppCompatActivity {

    private JPushManager jPushManager = new JPushManager();

    private ImageView imageView;

    public static Activity activity;

    //-----------------------------------------------
    private final int GET_REGISTER_STATUS = 200;
    private final int GET_PICTURE_PATH = 201;
    private final int GET_JPUSH_ID = 202;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_JPUSH_ID:
                    if (jPushManager.JPush_GetReginsterID(getBaseContext())!=null && !jPushManager.JPush_GetReginsterID(getBaseContext()).equals("") ){
                        Get_RegisterStatus();
                    }else {
                        Message message = new Message();
                        message.what = GET_JPUSH_ID;
                        Looper.sendMessageDelayed(message, 1000);
                    }
                    break;

                case GET_REGISTER_STATUS:

                    System.out.println("MainActivity.handler.GET_REGISTER_STATUS: " + (String)msg.obj);

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
        setContentView(R.layout.activity_main);

        CrashReport.initCrashReport(getApplicationContext(), "e948354ea8", false);

        activity = this;
        jPushManager.JPush_Init(getBaseContext());
        jPushManager.JPush_ResumePush(getBaseContext());

        InitView();
        CheckPerMission(0);

    }

    private void InitView(){

        imageView = (ImageView) findViewById(R.id.Reginster_QR_Code);

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
            if (jPushManager.JPush_GetReginsterID(getBaseContext())!=null && !jPushManager.JPush_GetReginsterID(getBaseContext()).equals("")){
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CheckPerMission(1);
    }

    //-------------------   Step Two
    private void Get_RegisterStatus() {
        System.out.println("MainActivity.Jpush_id is: " + jPushManager.JPush_GetReginsterID(getBaseContext()));
        System.out.println("MainActivity.MAC is: " + AppManager.AppManager_GetMac(getBaseContext()));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("jpush_id", jPushManager.JPush_GetReginsterID(getBaseContext()));
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.Reginster_Message_Url,jsonObject){
            public void Handle_Response(String Result){
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

            Map<String,Object> dataMap = (Map<String,Object>)JsonMap.get("data");
            GlobalSignManager.xxx_id = (int)((double)dataMap.get("xxx_id"));
            GlobalSignManager.Status = GlobalSignManager.Have_Registner;

            Intent intent = new Intent(MainActivity.this, ERCodeActivity.class);
            startActivity(intent);
            this.finish();
            return;

        }else if (BackEnd_Register_State.equals(GlobalSignManager.No_Registner)){

            System.out.println("MainActivity: " + "BackEnd_Register_State == No_Registner");

            GlobalSignManager.Status = GlobalSignManager.No_Registner;
            //Jump into Step Three
            Get_ImagePath();
            return;
        }
    }

    //-------------------    Step Three
    private void Get_ImagePath(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostThread posThread = new PostThread(getBaseContext(),GlobalSignManager.Image_Path_Url,jsonObject){
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

    //---------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {

        if (GlobalSignManager.Status!=null && GlobalSignManager.Status.equals(GlobalSignManager.No_Registner) ||
                GlobalSignManager.xxx_id<0){
            jPushManager.JPush_StopPush(getBaseContext());
        }

        super.onDestroy();
        activity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        GlobalSignManager.ActivityCount++;
        if (GlobalSignManager.ActivityCount==1){
            Post_AppStatus();
        }
        */

        System.out.println("ERCodeActivity.onResume.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            System.out.println("MainActivity.onResume and status change to OnLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
        GlobalSignManager.ActivityCount--;
        if (GlobalSignManager.ActivityCount==0){
            Post_AppStatus();
        }
        */

        System.out.println("ERCodeActivity.onStop.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            System.out.println("MainActivity.onResume and status change to OffLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }
    }

}
