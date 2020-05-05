package com.wanzhuan.livegame.ReceiverSet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.wanzhuan.livegame.Global.GlobalSignManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bladesaber on 2018/7/10.
 */

public class XGPExampleReceiver extends XGPushBaseReceiver {

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

    }

    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
            GlobalSignManager.getInstance().setDevice_token(token);
        } else {
            text = message + "注册失败错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {}

    @Override
    public void onSetTagResult(Context context, int i, String s) {}

    @Override
    public void onUnregisterResult(Context context, int i) {}

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {}
}
