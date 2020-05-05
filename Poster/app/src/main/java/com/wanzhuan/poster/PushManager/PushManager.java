package com.wanzhuan.poster.PushManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
import com.wanzhuan.poster.Global.GlobalSignManager;
import com.wanzhuan.poster.MainActivity;
import com.wanzhuan.poster.R;
import com.wanzhuan.poster.Tool.CustomDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * Created by bladesaber on 2018/7/16.
 */

public class PushManager {

    private Type int_type = new TypeToken<Integer>() {}.getType();
    private Type string_type = new TypeToken<String>() {}.getType();

    private Message m;

    private Gson gson = new Gson();

    private PushListener listener;

    private PushReceiver receiver;

    private LoadingView loadingView;
    private ViewGroup viewGroup;

    private final int LOADING_END = 200;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING_END:
                    viewGroup.removeView(loadingView);
                    break;
                default:
                    break;
            }
        }
    };

    public PushManager(Activity activity){
        if (receiver == null){
            receiver = new PushReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
            activity.registerReceiver(receiver, filter);
        }
        if (loadingView==null){
            loadingView = new LoadingView(activity,"推送注册中");
        }
        initCustomPushNotificationBuilder(activity,activity);
    }

    public void onDestroy(Activity activity){
        if (receiver!=null){
            activity.unregisterReceiver(receiver);
        }
    }

    public void setListener(PushListener listener){
        this.listener = listener;
    }

    public void set_loadingvIew(ViewGroup viewGroup){
        this.viewGroup = viewGroup;
        this.viewGroup.addView(loadingView);
    }

    public void initXG(final Activity activity){
        //开启信鸽的日志输出，线上版本不建议调用
        //XGPushConfig.enableDebug(this, true);
        XGPushConfig.getToken(activity);
        // 1.获取设备Token
        Handler handler = new HandlerExtension(activity);
        m = handler.obtainMessage();
        /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
        XGPushManager.registerPush(activity,
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                        Toast.makeText(activity,"信鸽推送注册成功",Toast.LENGTH_LONG).show();

                        // 获取token
                        GlobalSignManager.getInstance().setDevice_token(XGPushConfig.getToken(activity));
                        //System.out.println("token is: " + GlobalSignManager.getInstance().getDevice_token());

                        Looper.sendEmptyMessage(LOADING_END);

                        listener.NextStep();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag, "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg);
                        m.obj = "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                        Toast.makeText(activity,"设备注册失败：errCode:" + errCode + ",msg:" + msg ,Toast.LENGTH_LONG).show();

                        Looper.sendEmptyMessage(LOADING_END);

                        reginster_failed("很抱歉，信鸽推送注册失败，请卸载后重新安装。" + "\n" + "errCode:" + errCode + "\n"+",msg:" + msg,activity);
                    }
                });
    }

    private static class HandlerExtension extends Handler {
        WeakReference<Activity> mActivity;

        HandlerExtension(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity theActivity = mActivity.get();
            if (theActivity == null) {
                theActivity = new MainActivity();
            }
            if (msg != null) {
                Log.d("TPush", msg.obj.toString());
            }
        }
    }

    private void reginster_failed(String text, final Activity activity){

        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setMessage(text);
        builder.setTitle("推送注册信息");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });

        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private class PushReceiver extends XGPushBaseReceiver {
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

            System.out.println("Receive message is: "+xgPushTextMessage.getCustomContent());
            JsonObject jsonObject = new JsonParser().parse(xgPushTextMessage.getCustomContent()).getAsJsonObject();
            String type = gson.fromJson(jsonObject.get("type"),string_type);

            listener.ResolvePushMessage(type);
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

    private void initCustomPushNotificationBuilder(Context context,Activity activity) {
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
        XGPushManager.setPushNotificationBuilder(activity, 1, build);
        XGPushManager.setDefaultNotificationBuilder(activity, build);
    }

}
