package bladesaber.luckgame2v1.ERCoddeActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

import bladesaber.luckgame2v1.AppManager.AppManager;
import bladesaber.luckgame2v1.AppUpdate.AppDownLoadService;
import bladesaber.luckgame2v1.AppUpdate.AppJson;
import bladesaber.luckgame2v1.AppUpdate.AppModel;
import bladesaber.luckgame2v1.DataStructure.UserItem;
import bladesaber.luckgame2v1.Global.GlobalFunctionManager;
import bladesaber.luckgame2v1.Global.GlobalSignManager;
import bladesaber.luckgame2v1.Global.JsonType;
import bladesaber.luckgame2v1.HttpClient.PostThread;
import bladesaber.luckgame2v1.JPush.JPushManager;
import bladesaber.luckgame2v1.LuckPocker.LuckPockerActivity;
import bladesaber.luckgame2v1.R;
import bladesaber.luckgame2v1.SupportCompement.LoopRecycleView.ListViewItem;
import bladesaber.luckgame2v1.SupportCompement.LoopRecycleView.MyRecycleAdapter;
import bladesaber.luckgame2v1.SupportCompement.LoopRecycleView.TimeTaskScroll;
import cn.jpush.android.api.JPushInterface;

public class ERCodeActivity extends AppCompatActivity {

    public static Activity activity;

    //-----------------------------------------------
    private ImageView LuckPocker_ERcodde;

    private JPushManager jPushManager = new JPushManager();

    //-----------------------------------------------
    private int Sign_FisrtOnResume = 0;

    private RecyclerView LoopUserList;
    private MyRecycleAdapter myRecycleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Timer timer;

    private List<UserItem> UserList;

    //-----------------------------------------------
    private static final int GET_ER_CODE_PICTURE_PATH = 200;
    private static final int GET_APP_UPDATE_MESSAGE = 201;
    private static final int GET_USER_LIST = 202;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Gson gson = new Gson();

            switch (msg.what){
                case GET_ER_CODE_PICTURE_PATH:

                    System.out.println("ERCodeActivity.handler.GET_ER_CODE_PICTURE_PATH: "+(String)msg.obj);

                    Type type = new TypeToken<String>() {}.getType();
                    JsonObject responseObject = new JsonParser().parse((String) msg.obj).getAsJsonObject();
                    String url = gson.fromJson(responseObject.get("url"), type);

                    Picasso.with(getBaseContext())
                            .load(url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .into(LuckPocker_ERcodde);

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
                case GET_APP_UPDATE_MESSAGE:
                    CheckNestApp((String) msg.obj);
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

        //Get_AppUpdate_Message();
        Get_ER_Code_Picture();
        Get_UserList_Setting();

    }

    private void InitView(){
        LuckPocker_ERcodde = (ImageView) findViewById(R.id.ER_Code_LuckPocker);

        LoopUserList = (RecyclerView) findViewById(R.id.Recycler_User_List);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LoopUserList.setLayoutManager(mLayoutManager);

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

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.Reginster_Message_Url,jsonObject){
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

    //--------------------    Step Three
    private void Get_UserList_Setting(){

        Sign_FisrtOnResume = 1;

        /*
        System.out.println("ERCoceActivity.Mac: "+AppManager.AppManager_GetMac(getBaseContext()));
        System.out.println("ERCoceActivity.xxx_id: "+ GlobalSignManager.xxx_id);
        System.out.println("ERCoceActivity.app_code: "+ GlobalSignManager.App_Code);
        */

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
            timer.schedule(new TimeTaskScroll(this, myRecycleAdapter), 100, 2000);
        }

    }

    //---------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //if (GlobalSignManager.xxx_id>0) {
        //    jPushManager.JPush_StopPush(getBaseContext());
        //}

        if (GlobalSignManager.Have_Registner.equals(GlobalSignManager.Status)){
            jPushManager.JPush_StopPush(getBaseContext());
        }

        if (LuckPockerActivity.activity != null){
            LuckPockerActivity.activity.finish();
        }

        if (timer != null) {
            timer.cancel();
        }

        activity = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        //GlobalSignManager.ActivityCount--;
        //if (GlobalSignManager.ActivityCount==0){
        //    System.out.println("In Background");
        //}

        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Sign_FisrtOnResume==1){
            Get_UserList_Setting();
        }

        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }
    }

    //--------------------------------------------------------------------------------------------
    public static class ERCodeActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d("PagerViewActivity", "Jump into ERCodeActivityReceiver Receiver");
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
            try {
                switch (JsonMap.get("Type")){
                    case JsonType.Jump_Into_LuckPocker:
                        if (LuckPockerActivity.activity == null && ERCodeActivity.activity!=null) {
                            String name = JsonMap.get("Name");
                            String avatar = JsonMap.get("Avatar");
                            Intent intent1 = new Intent(context, LuckPockerActivity.class);
                            intent1.putExtra("Name", name);
                            intent1.putExtra("Avatar", avatar);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
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
