package com.wanzhuan.livegame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tencent.android.tpush.common.Constants;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanzhuan.livegame.ActivitySet.RollLottery.RollLotteryActivity;
import com.wanzhuan.livegame.AppManager.AppManager;
import com.wanzhuan.livegame.DataStructer.UserItem;
import com.wanzhuan.livegame.Global.GlobalSignManager;
import com.wanzhuan.livegame.HttpClient.PostThread;
import com.wanzhuan.livegame.ShowerWall.ShowerWallUtils;
import com.wanzhuan.livegame.Tool.CustomDialog;
import com.wanzhuan.livegame.Tool.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Type int_type = new TypeToken<Integer>() {}.getType();

    private Message m;

    private boolean First_Play = true;

    //----------------------------------------------------------------------------------------------
    private Type string_type = new TypeToken<String>() {}.getType();

    private static List<UserItem> userItemList = new ArrayList<>();

    private ShowerWallReceiver receiver;

    private ShowerWallUtils showerWallUtils;
    private RelativeLayout relativeLayout;

    private ImageView ercode;
    private ImageView ercode_background;

    private TextView number_of_player;
    private int number_player = 0;

    private TextView title;

    private Gson gson = new Gson();

    private String merchants_name;
    private String merchants_logo_url;

    private ImageView shower_wall_logo;

    private LoadingView loadingView;

    private TextView customer_name;

    //----------------------------------------------------------------------------------------------
    private final int TO_WALLSHOWER = 201;
    private final int GET_ERCODE = 202;
    private final int LOADING_END = 203;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            JsonObject jsonObject;

            switch (msg.what){
                case TO_WALLSHOWER:
                    jsonObject = new JsonParser().parse((String)msg.obj).getAsJsonObject();
                    int xxx_id = gson.fromJson(jsonObject.getAsJsonObject("data").get("xxx_id"),int_type);
                    GlobalSignManager.getInstance().setXxx_id(xxx_id);

                    if (First_Play) {
                        GlobalSignManager.getInstance().post_status(getBaseContext(), "WALL");
                        GlobalSignManager.getInstance().post_number(getBaseContext(),MainActivity.getUserItemList().size());
                        First_Play = false;
                    }

                    get_ERcode_url();

                    break;
                case GET_ERCODE:
                    jsonObject = new JsonParser().parse((String)msg.obj).getAsJsonObject();
                    String url = gson.fromJson(jsonObject.get("data").getAsJsonObject().get("qrcode_url"),string_type);
                    String title_text = gson.fromJson(jsonObject.get("data").getAsJsonObject().get("title"),string_type);
                    title.setText(title_text);

                    merchants_name = gson.fromJson(jsonObject.get("data").getAsJsonObject().get("studio_name"),string_type);
                    merchants_logo_url = gson.fromJson(jsonObject.get("data").getAsJsonObject().get("studio_logo"),string_type);

                    ercode_background.setVisibility(View.VISIBLE);
                    customer_name.setText(merchants_name);

                    Picasso.with(getBaseContext())
                            .load(url)
                            .config(Bitmap.Config.RGB_565)
                            .fit()
                            .into(ercode);

                    Picasso.with(getBaseContext())
                            .load(merchants_logo_url)
                            .config(Bitmap.Config.RGB_565)
                            .fit()
                            .into(shower_wall_logo);

                    break;
                case LOADING_END:
                    relativeLayout.removeView(loadingView);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower_wall);

        CrashReport.initCrashReport(getApplicationContext(), "2f98036e2f", false);

        relativeLayout = (RelativeLayout) findViewById(R.id.shower_wall);
        loadingView = new LoadingView(MainActivity.this,"信鸽推送注册中");
        relativeLayout.addView(loadingView);

        initXG();
        ShowerWallActivityInit();
    }

    private void ShowerWallActivityInit(){
        ercode = (ImageView) findViewById(R.id.ercode);
        number_of_player = (TextView) findViewById(R.id.shower_wall_number_of_player);

        showerWallUtils = new ShowerWallUtils(getBaseContext(),relativeLayout);

        title = (TextView) findViewById(R.id.shower_wall_title);

        shower_wall_logo = (ImageView) findViewById(R.id.shower_wall_logo);

        ercode_background = (ImageView) findViewById(R.id.ercode_background);

        customer_name = (TextView) findViewById(R.id.shower_wall_customer_name);

        initCustomPushNotificationBuilder(getBaseContext());

    }

    //----------------------------------------------------------------------------------------------
    private void initXG(){
        //开启信鸽的日志输出，线上版本不建议调用
        //XGPushConfig.enableDebug(this, true);
        XGPushConfig.getToken(this);
        // 1.获取设备Token
        Handler handler = new HandlerExtension(MainActivity.this);
        m = handler.obtainMessage();
        /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
        XGPushManager.registerPush(getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                        Toast.makeText(getBaseContext(),"信鸽推送注册成功",Toast.LENGTH_LONG).show();

                        // 获取token
                        GlobalSignManager.getInstance().setDevice_token(XGPushConfig.getToken(getApplicationContext()));
                        //System.out.println("token is: " + GlobalSignManager.getInstance().getDevice_token());
                        Get_RegisterStatus();

                        Looper.sendEmptyMessage(LOADING_END);
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag, "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg);
                        m.obj = "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                        Toast.makeText(getBaseContext(),"设备注册失败：errCode:" + errCode + ",msg:" + msg ,Toast.LENGTH_LONG).show();

                        Looper.sendEmptyMessage(LOADING_END);
                        reginster_failed("很抱歉，信鸽推送注册失败，请卸载后重新安装。" + "\n" + "errCode:" + errCode + "\n"+",msg:" + msg);
                    }
                });
    }

    private void reginster_failed(String text){

        CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
        builder.setMessage(text);
        builder.setTitle("信鸽推送注册信息");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    //-----------     Step Two
    private void Get_RegisterStatus() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("token", GlobalSignManager.getInstance().getDevice_token());
            jsonObject.put("app_code", GlobalSignManager.getInstance().getApp_code());
            jsonObject.put("version_code",AppManager.AppManager_GetVersionCode(getBaseContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.getInstance().get_check_url(),jsonObject){
            public void Handle_Response(String Result){
                //System.out.println("MainActivity.Get_RegisterStatus is: " + Result);
                Message message = new Message();
                message.what = TO_WALLSHOWER;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    //-----------------    Step Three
    private void get_ERcode_url(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("xxx_id", GlobalSignManager.getInstance().getXxx_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.getInstance().getShower_wall_ercode_url(),jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_ERCODE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);

        if (receiver==null) {
            receiver = new ShowerWallReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
            registerReceiver(receiver, filter);
        }

        if (!First_Play) {
            GlobalSignManager.getInstance().post_status(getBaseContext(), "WALL");
            GlobalSignManager.getInstance().post_number(getBaseContext(),MainActivity.getUserItemList().size());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);

        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        userItemList.clear();

        Looper.removeCallbacksAndMessages(null);
    }

    //----------------------------------------------------------------------------------------------
    private static class HandlerExtension extends Handler {
        WeakReference<MainActivity> mActivity;

        HandlerExtension(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity theActivity = mActivity.get();
            if (theActivity == null) {
                theActivity = new MainActivity();
            }
            if (msg != null) {
                Log.d("TPush", msg.obj.toString());
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    public static List<UserItem> getUserItemList(){
        return userItemList;
    }

    private void add_new_player(UserItem userItem){

        for (UserItem tem:getUserItemList()){
            if (tem.user_id == userItem.user_id){
                return;
            }
        }

        userItemList.add(userItem);
        showerWallUtils.add_user(userItem);
        number_player +=1;
        number_of_player.setText(number_player+"");

        GlobalSignManager.getInstance().post_number(getBaseContext(),MainActivity.getUserItemList().size());
    }

    //----------------------------------------------------------------------------------------------
    private void initCustomPushNotificationBuilder(Context context) {
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();

        int id = context.getResources().getIdentifier(
                "tixin", "raw", context.getPackageName());
        String uri = "android.resource://"
                + context.getPackageName() + "/" + id;
        build.setSound(Uri.parse(uri));
        // 设置自定义通知layout,通知背景等可以在layout里设置
        build.setLayoutId(R.layout.notification);
        // 设置自定义通知内容id
        build.setLayoutTextId(R.id.content);
        // 设置自定义通知标题id
        build.setLayoutTitleId(R.id.title);
        // 设置自定义通知图片id
        build.setLayoutIconId(R.id.icon);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.drawable.logo);
        // 设置状态栏的通知小图标
        //build.setbigContentView()
        build.setIcon(R.drawable.right);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源
        //build.setNotificationLargeIcon(R.drawable.ic_action_search);
        // 客户端保存build_id
        XGPushManager.setPushNotificationBuilder(this, 1, build);
        XGPushManager.setDefaultNotificationBuilder(this, build);
    }

    private class ShowerWallReceiver extends XGPushBaseReceiver {
        public static final String LogTag = "TPushReceiver";

        private void show(Context context, String text) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
            if (context == null || notifiShowedRlt == null) {
                return;
            }
            show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
        }

        @Override
        public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

            JsonObject jsonObject = new JsonParser().parse(xgPushTextMessage.getCustomContent()).getAsJsonObject();
            String type = gson.fromJson(jsonObject.get("type"),string_type);

            switch (type){
                case "LOTTERY_OPEN":
                    if (userItemList.size()>1) {
                        Intent intent = new Intent(MainActivity.this, RollLotteryActivity.class);
                        intent.putExtra("merchants_name", merchants_name);
                        intent.putExtra("merchants_logo_url", merchants_logo_url);
                        startActivity(intent);
                        GlobalSignManager.getInstance().post_status(context, "LOTTERY_OPEN");
                    }else {
                        Toast.makeText(getBaseContext(),"参与人数过少",Toast.LENGTH_LONG).show();
                    }
                    break;
                case "ONWALL" :
                    UserItem userItem = gson.fromJson(xgPushTextMessage.getCustomContent(),UserItem.class);
                    add_new_player(userItem);
                    break;
                case "UPDATE_INFO":
                    get_ERcode_url();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {}

        @Override
        public void onDeleteTagResult(Context context, int i, String s) {}

        @Override
        public void onSetTagResult(Context context, int i, String s) {}

        @Override
        public void onUnregisterResult(Context context, int i) {}

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {}
    }
}
