package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bladesaber.tvappvc1.AppManager.AppManager;
import com.example.bladesaber.tvappvc1.AppUpdate.AppDownLoadService;
import com.example.bladesaber.tvappvc1.AppUpdate.AppJson;
import com.example.bladesaber.tvappvc1.AppUpdate.AppModel;
import com.example.bladesaber.tvappvc1.FloopScreen.FloopScreenManager;
import com.example.bladesaber.tvappvc1.Global.GlobalClassManager;
import com.example.bladesaber.tvappvc1.Global.GlobalFunctionManager;
import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;
import com.example.bladesaber.tvappvc1.Global.JsonType;
import com.example.bladesaber.tvappvc1.HttpClient.GetThread;
import com.example.bladesaber.tvappvc1.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cn.jpush.android.api.JPushInterface;

import static com.example.bladesaber.tvappvc1.AppManager.AppManager.AppManager_GetVersionName;

public class PagerViewActivity extends AppCompatActivity {

    private static AlertDialog.Builder mAlertDialog;

    public static Activity activity = null;

    private ImageView background;
    private ImageView floopScreenImageView;
    private LinearLayout linearLayout;
    public static FloopScreenManager floopScreenManager;

    private ViewPager viewPager;
    private static ViewPagerManager3 ViewPagerManager;

    private AutoScrollTextView autoScrollTextView_Line1 ;
    private AutoScrollTextView autoScrollTextView_Line2 ;
    private AutoScrollTextView autoScrollTextView_Line3;
    private static AdvertingCarouselManager advertinrCarouselManager;

    private final int GET_UPDATE_MESSAGE = 200;
    private final int GET_TEXT_SETTING = 201;
    private final int GET_IMAGE_SETTING = 202;

    //------------------------------------------------------------------
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Gson gson = new Gson();
            switch (msg.what){
                case GET_UPDATE_MESSAGE:
                    AppModel appmodel_New=gson.fromJson((String) msg.obj,AppModel.class);
                    Check_Newest(GetAppInfo(),appmodel_New);
                    break;
                case GET_IMAGE_SETTING:
                    ViewPagerManager.ViewPagerManager_Get_UpdateViewList();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_view);

        Init();
        //Get_Update_Message();
        Get_ImageList();
        Get_Text();
    }

    //-------------------    Init
    private void Init(){
        activity = this;

        background = (ImageView) findViewById(R.id.ViewPager_Background);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_PageViewActivity);
        floopScreenImageView = (ImageView) findViewById(R.id.FloopScreenImagView);
        floopScreenManager = new FloopScreenManager(this,floopScreenImageView,linearLayout);

        viewPager = (ViewPager) findViewById(R.id.ViewPager_PagerViewActivity);
        ViewPagerManager = new ViewPagerManager3(getBaseContext(),viewPager,background);

        autoScrollTextView_Line1 = (AutoScrollTextView) findViewById(R.id.Advertist_Line1);
        autoScrollTextView_Line2 = (AutoScrollTextView) findViewById(R.id.Advertist_Line2);
        autoScrollTextView_Line3 = (AutoScrollTextView) findViewById(R.id.Advertist_Line3);

        advertinrCarouselManager = new AdvertingCarouselManager(getBaseContext(),
                autoScrollTextView_Line1,
                autoScrollTextView_Line2,
                autoScrollTextView_Line3,
                getWindowManager());

        ViewPagerManager.ViewPagerManager_InitPagerView();

        ViewPagerManager.ViewPagerManager_SpeedChange(GlobalSignManager.Duration);

        ViewPagerManager.PagerViewActivity_Start();

        advertinrCarouselManager.AdvertinrCarouselManager_Start();

        floopScreenManager.Get_FloopScreenImage();

    }

    //-------------------    Step One
    private void Get_Update_Message(){
        GetThread getThread = new GetThread(GlobalSignManager.Update_Apk_Message_Url){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_UPDATE_MESSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        getThread.start();
    }

    private void Check_Newest(AppModel appModel_old, final AppModel appModel_new){
        if (appModel_old.getVersionCode()<appModel_new.getVersionCode()){
            mAlertDialog = new AlertDialog.Builder(PagerViewActivity.this);
            mAlertDialog.setTitle("更新提示");
            mAlertDialog.setMessage("检测到新版本下载");
            mAlertDialog.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(PagerViewActivity.this, AppDownLoadService.class);
                    intent.putExtra("apkUrl", appModel_new.getUpdateUrl());
                    intent.putExtra("apkName", appModel_new.getApkName());
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

    private AppModel GetAppInfo(){

        AppModel appModel_old =new AppModel();
        appModel_old.setVersionName(AppManager_GetVersionName(PagerViewActivity.this));

        appModel_old.setVersionCode(AppManager.AppManager_GetVersionCode(PagerViewActivity.this));

        return appModel_old;
    }

    // -------------    Step Two
    private void Get_ImageList(){
        ViewPagerManager.Get_ImageSetting();
        Message message = new Message();
        message.what = GET_IMAGE_SETTING;
        Looper.sendMessageDelayed(message,2000);
    }

    private void Get_Text(){
        System.out.println("Here jump into PagerViewActivity.Get_Text()");
        advertinrCarouselManager.Get_Text_Setting();
    }

    //-------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (GlobalClassManager.sqlManager.ReginsterState_get().equals(GlobalSignManager.Have_Registner)){
            GlobalClassManager.jPushManager.JPush_StopPush(getBaseContext());
        }

        ViewPagerManager.Destroy();
        Looper.removeCallbacksAndMessages(null);

        activity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("PagerViewActivity.onResume.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            System.out.println("PagerViewActivity.onResume and status change to OnLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("PagerViewActivity.onStop.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            System.out.println("PagerViewActivity.onStop and status change to OffLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }
    }

    //--------------------------------
    public static class PagerViewReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d("PagerViewActivity", "Jump into PagerViewAvtivity Receiver");
            switch (intent.getAction()) {
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Log.d("PagerViewActivity","MESSAGE_RECEIVED,接受自定义信息");

                    JPushReceiver_Message_Handle(context, bundle);

                    break;

                case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                    Log.d("PagerViewActivity","NOTIFICATION_RECEIVED,接受通知");
                    break;

                case JPushInterface.ACTION_NOTIFICATION_OPENED:
                    Log.d("PagerViewActivity","NOTIFICATION_OPENED,用户点击通知");

                    JPushReceiver_Notification_Handle(context, bundle);

                    break;

                case JPushInterface.ACTION_RICHPUSH_CALLBACK:
                    Log.d("PagerViewActivity", "用户收到到RICH PUSH CALLBACK");
            }
        }

        private void JPushReceiver_Message_Handle(final Context context, Bundle bundle) {
            if (GlobalClassManager.sqlManager.ReginsterState_get().equals(GlobalSignManager.Have_Registner)) {

                String Json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Log.d("PagerViewActivity", "Receiver.dataJson:" + Json);
                Gson gson = new Gson();

                Type type = new TypeToken<String>() {}.getType();
                JsonObject jsonMessage = new JsonParser().parse(Json).getAsJsonObject();
                String data_Type = gson.fromJson(jsonMessage.get("Type"),type);

                //Map<String, Object> JsonMap = gson.fromJson(Json, HashMap.class);
                //String data_Type = (String) JsonMap.get("Type");

                switch (data_Type){
                    case JsonType.Update_Image:
                        ViewPagerManager.ViewPagerManager_Get_UpdateViewList();
                        break;
                    case JsonType.Update_Text_Setting:
                        advertinrCarouselManager.Get_Text_Setting();
                        break;
                    case JsonType.Update_Text_List:
                        advertinrCarouselManager.Get_TextList();
                        break;
                    case JsonType.Update_Image_Setting:
                        ViewPagerManager.Get_ImageSetting();
                        break;

                    case JsonType.Check_Screen_Floop_Status:
                        floopScreenManager.Get_FloopScreenImage();
                        break;

                    case JsonType.Start_Screen_Floop:
                        //floopScreenManager.GetPlayerMessage();
                        //floopScreenManager.Start_Screen_Floop(Json);
                        floopScreenManager.addUser(Json);
                        break;

                    default:
                        break;
                }

                if (data_Type.equals(JsonType.AppMessagePush)) {

                    AppModel appmodel_New = (gson.fromJson(Json, AppJson.class)).getData();
                    Check_Newest(GetAppInfo(),appmodel_New,context);

                }
            }
        }

        private void Check_Newest(AppModel appModel_old, final AppModel appModel_new, final Context context){
            if (appModel_old.getVersionCode()<appModel_new.getVersionCode()){
                mAlertDialog = new AlertDialog.Builder(PagerViewActivity.activity);
                mAlertDialog.setTitle("Update Message");
                mAlertDialog.setMessage("A Newest App Scan");
                mAlertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PagerViewActivity.activity, AppDownLoadService.class);
                        intent.putExtra("apkUrl", appModel_new.getUpdateUrl());
                        intent.putExtra("apkName", appModel_new.getApkName());
                        context.startService(intent);
                    }
                });

                mAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mAlertDialog.create().show();
            }
        }

        private AppModel GetAppInfo(){

            AppModel appModel_old =new AppModel();
            appModel_old.setVersionName(AppManager_GetVersionName(PagerViewActivity.activity));
            appModel_old.setVersionCode(AppManager.AppManager_GetVersionCode(PagerViewActivity.activity));

            return appModel_old;
        }

        private void JPushReceiver_Notification_Handle(Context context, Bundle bundle) {
        }
    }


}
