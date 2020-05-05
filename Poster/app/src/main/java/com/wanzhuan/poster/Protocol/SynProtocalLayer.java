package com.wanzhuan.poster.Protocol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wanzhuan.poster.Global.GlobalSignManager;
import com.wanzhuan.poster.HttpClient.PostThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by bladesaber on 2018/7/3.
 */

public class SynProtocalLayer {

    private static SynProtocalLayer synProtocalLayer = null;

    private Context context;

    public int rate = 0;

    private Gson gson = new Gson();
    private Type int_type = new TypeToken<Integer>() {}.getType();

    public static SynProtocalLayer getInstance(){
        if (synProtocalLayer==null){
            synProtocalLayer = new SynProtocalLayer();
        }
        return synProtocalLayer;
    }

    public void setContext(Context context){
        this.context = context;
    }

    private final static int CHECK = 200;
    private final static int SEND = 201;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CHECK:
                    resolve((String)msg.obj);
                    break;
                case SEND:
                    send();
                    break;
                default:
                    Toast.makeText(context,"错误同步协议参数",Toast.LENGTH_LONG).show();
            }
        }
    };


    public void First_Start(int rate){
        this.rate = rate;
        send();
    }

    private void send(){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rate", rate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(context,GlobalSignManager.Protocal_Url,jsonObject){
            @Override
            public void Handle_Response(String JsonResult) {
                Message message = new Message();
                message.what = CHECK;
                message.obj = jsonObject;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void resolve(String json){
        JsonElement jsonElement = new JsonParser().parse(json).getAsJsonObject().get("status");
        int status = gson.fromJson(jsonElement,int_type);
        switch (status){
            case 0:
                Handle_Next();
                break;
            case 1:
                Looper.sendEmptyMessageDelayed(SEND,200);
                break;
            default:
                break;
        }
    }

    public void Handle_Next(){}

}
