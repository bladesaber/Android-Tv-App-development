package bladesaber.luckgame2v1.LuckPocker;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bladesaber.luckgame2v1.AppManager.AppManager;
import bladesaber.luckgame2v1.DataStructure.PockerItem;
import bladesaber.luckgame2v1.DataStructure.TestJson;
import bladesaber.luckgame2v1.Global.GlobalFunctionManager;
import bladesaber.luckgame2v1.Global.GlobalSignManager;
import bladesaber.luckgame2v1.Global.JsonType;
import bladesaber.luckgame2v1.HttpClient.PostThread;
import bladesaber.luckgame2v1.R;
import bladesaber.luckgame2v1.SupportCompement.DialogActivity;
import cn.jpush.android.api.JPushInterface;

public class LuckPockerActivity extends AppCompatActivity {

    public static Activity activity;

    //----------------------------------------------------------------------------------
    private MyLuckPocker card1;
    private MyLuckPocker card2;
    private MyLuckPocker card3;
    private MyLuckPocker card4;
    private MyLuckPocker card5;
    private MyLuckPocker card6;
    private MyLuckPocker card7;
    private MyLuckPocker card8;

    private TextView TimeClick_TextView;
    private TextView PlayerName;
    private ImageView PlayerImage;

    private static String prizeName;
    private static String WinType;

    public static double duration = 0.75;

    //-------------------------------------------------------------------------------
    private static int isPlayed = 0;

    //--------------------------------------------------------------------------------
    private static List<MyLuckPocker> pockers = new ArrayList<MyLuckPocker>();
    private static List<PockerItem> pockerItemList = new ArrayList<PockerItem>();

    private static int TimeOut = 60;
    public static int PrizeDuration = 10;
    private int GetReslt_Sign = 0;

    //----------------------------------------------------
    private final static int GET_LUCKPOCKER_MESSAGE = 200;
    private static final int TimeClick = 201;
    private static final int SHOW_POPUPWINDOWS = 202;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Gson gson = new Gson();

            switch (msg.what){
                case GET_LUCKPOCKER_MESSAGE:

                    System.out.println("LuckPockerActivity.handler.GET_LUCKPOCKER_MESSAGE: "+(String)msg.obj);

                    Type type = new TypeToken<List<PockerItem>>() {}.getType();
                    JsonObject responseJson1 = new JsonParser().parse((String) msg.obj).getAsJsonObject().get("data").getAsJsonObject();
                    JsonElement elementJson1 = responseJson1.get("prizes");

                    pockerItemList = gson.fromJson(elementJson1,type);

                    if (pockerItemList != null && pockerItemList.size()>0 && pockerItemList.size()<=8) {
                        InitLuckPocker(pockerItemList.size());
                    }

                    TimeStart();

                    break;
                case TimeClick :
                    TimeOut --;

                    //-----------------------------
                    String clock_time = Integer.toString(TimeOut);
                    if (TimeOut >= 0) {
                        TimeClick_TextView.setText(clock_time);
                    }
                    //-----------------------------
                    if (TimeOut==0){
                        HandleTimeOut();
                    }

                    Message message = new Message();
                    message.what = TimeClick;
                    Looper.sendMessageDelayed(message,1000);

                    break;

                case SHOW_POPUPWINDOWS:

                    TimeOut = PrizeDuration;
                    GetReslt_Sign = 1;

                    /*
                    if (tableItemList.get(ResultIndex).getType().equals(GlobalSignManager.Win)) {
                        ShowResult();
                    }
                    */
                    for (MyLuckPocker pocker:pockers){
                        pocker.Destroy();
                    }
                    //ShowResult();

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

        PlayerName = (TextView) findViewById(R.id.Player_Name);
        PlayerImage = (ImageView) findViewById(R.id.Player_Image);
        TimeClick_TextView = (TextView) findViewById(R.id.LuckTable_clock);

        PlayerName.setText(getIntent().getStringExtra("Name"));
        Picasso.with(getBaseContext())
                .load(getIntent().getStringExtra("Avatar"))
                .into(PlayerImage);

        Get_LuckPocker_Message();
    }

    //------------------   Step One
    private void Get_LuckPocker_Message(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.Table_Message,jsonObject){
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
    private void InitLuckPocker(int size){
        card1 = (MyLuckPocker) findViewById(R.id.card1);
        card2 = (MyLuckPocker) findViewById(R.id.card2);
        card3 = (MyLuckPocker) findViewById(R.id.card3);
        card4 = (MyLuckPocker) findViewById(R.id.card4);
        card5 = (MyLuckPocker) findViewById(R.id.card5);
        card6 = (MyLuckPocker) findViewById(R.id.card6);
        card7 = (MyLuckPocker) findViewById(R.id.card7);
        card8 = (MyLuckPocker) findViewById(R.id.card8);

        pockers.add(card1);pockers.add(card2);pockers.add(card3);pockers.add(card4);
        pockers.add(card5);pockers.add(card6);pockers.add(card7);pockers.add(card8);

        LuckPockerListener luckPockerListener = new LuckPockerListener() {
            @Override
            public void PockerGameStop() {
                //  do something
                Message message = new Message();
                message.what = SHOW_POPUPWINDOWS;
                Looper.sendMessage(message);
            }
        };

        if (size<8) {
            for (int i = 0; i < size; i++) {
                //pockers.get(i).SetImage(R.drawable.failer);
                pockers.get(i).setVisibility(View.VISIBLE);
            }
        }

        for (MyLuckPocker myLuckPocker:pockers){
            myLuckPocker.setListener(luckPockerListener);
        }
    }

    //-------------    Step  Three
    private void TimeStart(){
        Message message = new Message();
        message.what = TimeClick;
        Looper.sendMessage(message);
    }

    //----------------------------------------------------------------------------------------------
    private void HandleTimeOut(){
        if (isPlayed==0 ){
            System.out.println("超时未进行游戏");
            Toast.makeText(getBaseContext(),"超时未进行游戏",Toast.LENGTH_LONG).show();
            if (activity!=null){
                activity.finish();
            }
        }
        if (isPlayed==1 && GetReslt_Sign==1){
            System.out.println("游戏结束");

            if (DialogActivity.activity!=null){
                DialogActivity.activity.finish();
                TimeOut = 1;
            }

            if (activity !=null && DialogActivity.activity==null){
                Toast.makeText(getBaseContext(),"游戏结束",Toast.LENGTH_LONG).show();
                activity.finish();
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    private void ShowResult(){

        Intent intent = new Intent(LuckPockerActivity.this, DialogActivity.class);
        String Text = prizeName;
        String Result = WinType;
        if (Text!=null) {
            intent.putExtra("Prize", Text);
            intent.putExtra("Type",Result);
        }
        startActivity(intent);

    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Looper.removeCallbacksAndMessages(null);

        if (pockers.size()>0){
            for (MyLuckPocker pocker:pockers){
                pocker.Destroy();
            }
            pockers.clear();
        }
        if (pockerItemList.size()>0){
            pockerItemList.clear();
        }

        isPlayed = 0;
        TimeOut = 60;
        activity = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }
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
            String Json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            System.out.println("LuckPockerActivityReceiver.JPushReceiver_Message_Handle.Json: "+bundle.getString(JPushInterface.EXTRA_MESSAGE));

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String , Object> JsonMap = gson.fromJson(Json ,type);

            if ( ((String)JsonMap.get("Type")).equals(JsonType.Luckpocker_Stop) && LuckPockerActivity.activity!=null) {

                int Receice_Target = (int)((double)JsonMap.get("Id"));
                int Target = 0;
                for (int i=0;i<pockerItemList.size();i++){
                    if (pockerItemList.get(i).getId() == Receice_Target){
                        Target = i;
                    }
                }

                if (isPlayed==0 && pockers!=null && pockers.size()>0 && pockers.size()<=8) {
                    prizeName = (String) JsonMap.get("Name");
                    WinType = (String) JsonMap.get("Status");
                    pockers.get(Target).SetText(prizeName);
                    pockers.get(Target).Start();

                    isPlayed = 1;

                    //TimeOut = ?? + LuckPocker_Duration;
                    TimeOut = 2 + (int) (duration*2);
                }
            }
        }

    }

}
