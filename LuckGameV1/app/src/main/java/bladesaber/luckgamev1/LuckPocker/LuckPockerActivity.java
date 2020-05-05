package bladesaber.luckgamev1.LuckPocker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bladesaber.luckgamev1.AppManager.AppManager;
import bladesaber.luckgamev1.DataStructure.TestJson;
import bladesaber.luckgamev1.Global.GlobalSignManager;
import bladesaber.luckgamev1.Global.JsonType;
import bladesaber.luckgamev1.HttpClient.PostThread;
import bladesaber.luckgamev1.R;
import cn.jpush.android.api.JPushInterface;

public class LuckPockerActivity extends AppCompatActivity {

    public static Activity activity;

    private MyLuckPocker card1;
    private MyLuckPocker card2;
    private MyLuckPocker card3;
    private MyLuckPocker card4;
    private MyLuckPocker card5;
    private MyLuckPocker card6;
    private MyLuckPocker card7;
    private MyLuckPocker card8;

    private static int isPlayed = 0;

    private static List<MyLuckPocker> pockers = new ArrayList<MyLuckPocker>();

    private static int TimeOut = 20;

    //----------------------------------------------------
    private final static int GET_LUCKPOCKER_MESSAGE = 200;
    private static final int TimeClick = 201;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_LUCKPOCKER_MESSAGE:

                    System.out.println("LuckPockerActivity.handler.GET_LUCKPOCKER_MESSAGE: "+(String)msg.obj);

                    Gson gson = new Gson();
                    List<String> strings = gson.fromJson((String)msg.obj, TestJson.class).getData();
                    if (strings != null && strings.size()>0) {
                        InitLuckPocker(strings);
                    }
                    break;
                case TimeClick :
                    TimeOut --;

                    if (TimeOut==0){
                        HandleTimeOut();
                    }

                    Message message = new Message();
                    message.what = TimeClick;
                    Looper.sendMessageDelayed(message,1000);

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_pocker);

        activity = this;

        Get_LuckPocker_Message();
    }

    //------------------   Step One
    private void Get_LuckPocker_Message(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.LuckPocker_Setting_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_LUCKPOCKER_MESSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    //-------------------   Step Two
    private void InitLuckPocker(List<String> strings){
        card1 = (MyLuckPocker) findViewById(R.id.card1);
        card2 = (MyLuckPocker) findViewById(R.id.card2);
        card3 = (MyLuckPocker) findViewById(R.id.card3);
        card4 = (MyLuckPocker) findViewById(R.id.card4);
        card5 = (MyLuckPocker) findViewById(R.id.card5);
        card6 = (MyLuckPocker) findViewById(R.id.card6);
        card7 = (MyLuckPocker) findViewById(R.id.card7);
        card8 = (MyLuckPocker) findViewById(R.id.card8);

        pockers.add(card1);pockers.add(card2);pockers.add(card3);pockers.add(card4);
        pockers.add(card5);pockers.add(card6);pockers.add(card6);pockers.add(card7);

        for (int i=0;i<strings.size();i++){
            pockers.get(i).SetText(strings.get(i));
        }

        //  Jump into Step Three
        //TimeStart();
    }

    //-------------    Step  Three
    private void TimeStart(){
        Message message = new Message();
        message.what = TimeClick;
        Looper.sendMessage(message);
    }

    //----------------------------------------------------------------------------------------------
    private void HandleTimeOut(){
        if (isPlayed==0){
            System.out.println("超时未进行游戏");
        }
        if (isPlayed==1){
            System.out.println("游戏结束");
        }
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Looper.removeCallbacksAndMessages(null);

        isPlayed = 0;
        TimeOut = 20;
        activity = null;
    }

    //----------------------------------------------------------------------------------------------
    public static class LuckPockerActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d("PagerViewActivity", "Jump into MainAvtivity Receiver");
            switch (intent.getAction()) {
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Log.d("PagerViewActivity","MESSAGE_RECEIVED,接受自定义信息");
                    JPushReceiver_Message_Handle(context, bundle);
                    break;
            }
        }

        private void JPushReceiver_Message_Handle(final Context context, Bundle bundle) {
            /*
            //String Json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String Json = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            System.out.println("LuckPockerActivityReceiver.JPushReceiver_Message_Handle.Json: "+bundle.getString(JPushInterface.EXTRA_MESSAGE));

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String , Object> JsonMap = gson.fromJson(Json ,type);

            if ( ((String)JsonMap.get("Type")).equals(JsonType.Luckpocker_id) ) {
                int id = (int) ((double)JsonMap.get("id"));
                pockers.get(id).Start();
                isPlayed = 1;
                //TimeOut = ?? + LuckPocker_Duration;
            }
            */
        }

    }

}
