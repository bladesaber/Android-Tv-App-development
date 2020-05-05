package bladesaber.luckgamev1.ERCodeActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import bladesaber.luckgamev1.AppManager.AppManager;
import bladesaber.luckgamev1.AppUpdate.AppDownLoadService;
import bladesaber.luckgamev1.AppUpdate.AppJson;
import bladesaber.luckgamev1.AppUpdate.AppModel;
import bladesaber.luckgamev1.DataStructure.UserItem;
import bladesaber.luckgamev1.Global.GlobalFunctionManager;
import bladesaber.luckgamev1.Global.GlobalSignManager;
import bladesaber.luckgamev1.Global.JsonType;
import bladesaber.luckgamev1.HttpClient.PostThread;
import bladesaber.luckgamev1.JPush.JPushManager;
import bladesaber.luckgamev1.LuckPocker.LuckPockerActivity;
import bladesaber.luckgamev1.LuckTable.LuckTableActivity;
import bladesaber.luckgamev1.R;
import bladesaber.luckgamev1.SupportCompement.AnimatorImageView;
import bladesaber.luckgamev1.SupportCompement.LoopListView.ListViewItem;
import bladesaber.luckgamev1.SupportCompement.LoopRecycleView.MyRecycleAdapter;
import bladesaber.luckgamev1.SupportCompement.LoopRecycleView.TimeTaskScroll2;
import cn.jpush.android.api.JPushInterface;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

public class ERCodeActivity extends AppCompatActivity {

    public static Activity activity;

    //-----------------------------------------------
    private int Sign_FisrtOnResume = 0;

    //-----------------------------------------------
    private ImageView LuckTable_ERCode;
    //private ImageView LuckPocker_ERcodde;

    private AnimatorImageView animatorImageView;

    private JPushManager jPushManager = new JPushManager();

    //-----------------------------------------------
    private RecyclerView LoopUserList;
    private MyRecycleAdapter myRecycleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Timer timer;

    private List<UserItem> UserList;

    //-----------------------------------------------
    private final int GET_ER_CODE_PICTURE_PATH = 200;
    private final int GET_APP_UPDATE_MESSAGE = 201;
    private final int GET_USER_LIST = 202;
    private final int GET_JPUSH_ID = 203;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Gson gson = new Gson();
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

                case GET_ER_CODE_PICTURE_PATH:

                    System.out.println("ERCodeActivity.handler.GET_ER_CODE_PICTURE_PATH: "+(String)msg.obj);

                    Type type = new TypeToken<String>() {}.getType();
                    JsonObject responseObject = new JsonParser().parse((String) msg.obj).getAsJsonObject();
                    String url = gson.fromJson(responseObject.get("url"), type);

                    Picasso.with(getBaseContext())
                            .load(url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .into(LuckTable_ERCode);

                    /*  //  由于分离 App 所以封死
                    Picasso.with(getBaseContext())
                            .load(JsonMap.get("fp_url"))
                            .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .into(LuckPocker_ERcodde);
                    */

                    Get_UserList_Setting();

                    break;

                case GET_APP_UPDATE_MESSAGE:
                    CheckNestApp((String) msg.obj);
                    break;

                case GET_USER_LIST:
                    System.out.println("ERCodeActivity.handler.GET_USER_LIST: "+(String)msg.obj);

                    Type type2 = new TypeToken<List<UserItem>>() {}.getType();
                    JsonObject jsonObject = new JsonParser().parse((String)msg.obj).getAsJsonObject();
                    JsonElement jsonElement = jsonObject.get("data");

                    UserList = gson.fromJson(jsonElement,type2);

                    if (UserList != null && UserList.size()>0) {
                        InitUserList(UserList);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ercode);

        activity = this;
        jPushManager.JPush_Init(getBaseContext());
        jPushManager.JPush_ResumePush(getBaseContext());

        InitView();

        if (jPushManager.JPush_GetReginsterID(getBaseContext())!=null && !jPushManager.JPush_GetReginsterID(getBaseContext()).equals("")){
            Get_RegisterStatus();
        }else {
            Message message = new Message();
            message.what = GET_JPUSH_ID;
            Looper.sendMessageDelayed(message, 1000);
        }

        //Get_AppUpdate_Message();
        //Get_ER_Code_Picture();

    }

    //----------------------------------------------------------------------------------------------
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
                Gson gson = new Gson();

                JsonObject jsonObject1 = new JsonParser().parse(Result).getAsJsonObject().get("data").getAsJsonObject();
                Type int_type = new TypeToken<Integer>() {}.getType();

                GlobalSignManager.xxx_id = gson.fromJson(jsonObject1.get("xxx_id"),int_type);
                GlobalSignManager.Status = GlobalSignManager.Have_Registner;

                Get_ER_Code_Picture();
            }
        };
        postThread.start();
    }

    //----------------------------------------------------------------------------------------------

    private void InitView(){
        //LuckPocker_ERcodde = (ImageView) findViewById(R.id.ER_Code_LuckPocker);
        LuckTable_ERCode = (ImageView) findViewById(R.id.ER_Code_LuckTable);

        LoopUserList = (RecyclerView) findViewById(R.id.Recycler_User_List);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LoopUserList.setLayoutManager(mLayoutManager);

        animatorImageView = (AnimatorImageView) findViewById(R.id.carton_ercode);

    }

    //-------------    Step One
    private void Get_AppUpdate_Message() {
        System.out.println("MAC is: " + AppManager.AppManager_GetMac(getBaseContext()));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            //jsonObject.put("jpush_id", GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.AppUpdate_Url,jsonObject){
            public void Handle_Response(String Result){
                System.out.println("MainActivity.Result is: " + Result);
                Message message = new Message();
                message.what = GET_APP_UPDATE_MESSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void CheckNestApp(String Json){
        Gson gson = new Gson();
        final AppModel app_new = gson.fromJson(Json, AppJson.class).getData();
        if (AppManager.AppManager_GetVersionCode(getBaseContext())<app_new.getVersionCode()){
            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(ERCodeActivity.this);
            mAlertDialog.setTitle("更新提示");
            mAlertDialog.setMessage("检测到新版本下载");
            mAlertDialog.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ERCodeActivity.this, AppDownLoadService.class);
                    intent.putExtra("apkUrl", app_new.getUpdateUrl());
                    intent.putExtra("apkName", app_new.getApkName());
                    startService(intent);
                }
            });
            mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            mAlertDialog.create().show();
        }
    }

    //-------------------    Step Two
    private void Get_ER_Code_Picture() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.Table_Go_Image,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_ER_CODE_PICTURE_PATH;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    //----------------------  Step Three
    private void Get_UserList_Setting(){
        Sign_FisrtOnResume = 1;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.UserList,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_USER_LIST;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void InitUserList(List<UserItem> UserList){

        //  Resolve
        List<ListViewItem> UserViewList1 = new ArrayList<ListViewItem>();
        for (int i=0;i<UserList.size();i++){
            ListViewItem listViewItem = new ListViewItem();
            listViewItem.setName(UserList.get(i).getUser());
            listViewItem.setPresent(UserList.get(i).getPrize());
            UserViewList1.add(listViewItem);
        }

        myRecycleAdapter = new MyRecycleAdapter(UserViewList1);
        LoopUserList.setAdapter(myRecycleAdapter);

        if ( UserViewList1.size() > 3) {
            if (timer != null){
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            timer.schedule(new TimeTaskScroll2(this, myRecycleAdapter), 100, 2000);
        }

    }

    //---------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ERCodeActivity.onResume is run");

        if (animatorImageView != null){
            animatorImageView.Start();
        }

        if (Sign_FisrtOnResume==1){
            Get_UserList_Setting();
        }

        //GlobalSignManager.ActivityCount++;
        //if (GlobalSignManager.ActivityCount==1){
        //    System.out.println("In Forground");
        //}

        System.out.println("ERCodeActivity.onResume.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            System.out.println("ERCodeActivity.onResume and status change to OnLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //if (GlobalSignManager.xxx_id > 0) {
        //    jPushManager.JPush_StopPush(getBaseContext());
        //}

        /*
        if (GlobalSignManager.Status.equals(GlobalSignManager.Have_Registner)) {
            jPushManager.JPush_StopPush(getBaseContext());
        }
        */
        if (GlobalSignManager.Have_Registner.equals(GlobalSignManager.Status)){
            jPushManager.JPush_StopPush(getBaseContext());
        }

        if (LuckPockerActivity.activity != null){
            LuckPockerActivity.activity.finish();
        }
        if (LuckTableActivity.activity != null){
            LuckTableActivity.activity.finish();
        }

        if (timer != null) {
            timer.cancel();
        }

        animatorImageView.Destroy();

        activity = null;

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (animatorImageView!=null){
            animatorImageView.Stop();
        }

        //GlobalSignManager.ActivityCount--;
        //if (GlobalSignManager.ActivityCount==0){
        //    System.out.println("In Background");
        //}

        System.out.println("ERCodeActivity.onStop.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            System.out.println("ERCodeActivity.onStop and status change to OffLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }

    }

    //--------------------------------------------------------------------------------------------
    public static class ERCodeActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d("ERCodeActivity", "Jump into ERCodeActivityReceiver Receiver");
            switch (intent.getAction()) {
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Log.d("ERCodeActivity","MESSAGE_RECEIVED,接受自定义信息");
                    JPushReceiver_Message_Handle(context, bundle);
                    break;
            }
        }

        private void JPushReceiver_Message_Handle(final Context context, Bundle bundle) {

            String Json = bundle.getString(JPushInterface.EXTRA_EXTRA);

            System.out.println("ERCodeActivityReceiver.JPushInterface.EXTRA_EXTRA: "+Json);

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String , String> JsonMap = gson.fromJson(Json ,type);
            String name = JsonMap.get("Name");
            String avatar = JsonMap.get("Avatar");
            try {
                switch (JsonMap.get("Type")){
                    /*   //  由于分离 App 所以封死
                    case JsonType.Jump_Into_LuckPocker:
                        Intent intent1 = new Intent(context, LuckPockerActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                        break;
                        */

                    case JsonType.Jump_Into_LuckTable:
                        if (LuckTableActivity.activity == null && ERCodeActivity.activity!=null) {
                            System.out.println("ERCodeActivity.LuckTableActivity.activity != null");
                            Intent intent2 = new Intent(context, LuckTableActivity.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent2.putExtra("Name", name);
                            intent2.putExtra("Avatar", avatar);
                            context.startActivity(intent2);
                        }else {
                            System.out.println("ERCodeActivity.LuckTableActivity.activity == null");
                        }
                        break;
                    default:
                        break;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

    }

}
