package bladesaber.luckgamev1.LuckTable;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import bladesaber.luckgamev1.AppManager.AppManager;
import bladesaber.luckgamev1.DataStructure.TableItem;
import bladesaber.luckgamev1.Global.GlobalFunctionManager;
import bladesaber.luckgamev1.Global.GlobalSignManager;
import bladesaber.luckgamev1.Global.JsonType;
import bladesaber.luckgamev1.HttpClient.PostThread;
import bladesaber.luckgamev1.R;
import bladesaber.luckgamev1.SupportCompement.DialogActivity;
import bladesaber.luckgamev1.SupportCompement.MyPopupWindow;
import bladesaber.luckgamev1.Utils;
import cn.jpush.android.api.JPushInterface;

public class LuckTableActivity extends AppCompatActivity {

    public static Activity activity;

    private static MyLuckTurnTable luckTurnTable;

    //----------------------------------------------------------------
    private TextView TimeClick_TextView;
    private TextView PlayerName;
    private ImageView PlayerImage;

    //------------------------------------------------------------------
    private  static List<TableItem> tableItemList = new ArrayList<TableItem>();

    //------------------------------------------------------------------
    private int TimeOut = 25;
    public static int PrizeDuration = 10;
    private int GetReslt_Sign = 0;

    private MyPopupWindow popupWindow;
    private int ResultIndex = 0;

    //---------------------------------------------------------
    private static final int GET_LUCKTABLE_MESSAGE = 200;
    private static final int TimeClick = 201;
    private static final int GET_PLAYER_MESSAGE = 203;
    private static final int SHOW_POPUPWINDOWS = 204;


    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Gson gson = new Gson();
            switch (msg.what){
                case GET_LUCKTABLE_MESSAGE:

                    System.out.println("LuckTableActivity.handler.GET_LUCKTABLE_MESSAGE: " + (String)msg.obj);

                    Type type = new TypeToken<List<TableItem>>() {}.getType();
                    JsonObject responseJson1 = new JsonParser().parse((String) msg.obj).getAsJsonObject().get("data").getAsJsonObject();
                    JsonElement elementJson1 = responseJson1.get("prizes");

                    tableItemList = gson.fromJson(elementJson1,type);

                    if (tableItemList != null && tableItemList.size()>0) {
                        InitLuckTable(tableItemList);
                    }
                    TimeStart();
                    break;

                case GET_PLAYER_MESSAGE:

                    /*
                    if ( != null && .size()>0) {
                        InitPlayerSetting();
                    }
                    */
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

                    if (tableItemList.get(ResultIndex).getType().equals(GlobalSignManager.Win)) {
                        ShowResult();
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
        setContentView(R.layout.activity_luck_table);

        activity = this;

        luckTurnTable = (MyLuckTurnTable) findViewById(R.id.LuckTable);
        TimeClick_TextView = (TextView) findViewById(R.id.LuckTable_clock);

        PlayerName = (TextView) findViewById(R.id.Player_Name);
        PlayerImage = (ImageView) findViewById(R.id.Player_Image);

        PlayerName.setText(getIntent().getStringExtra("Name"));
        Picasso.with(getBaseContext())
                .load(getIntent().getStringExtra("Avatar"))
                .into(PlayerImage);

        Get_LuckTable_Setting();

    }

    //------------   Step One
    private void Get_LuckTable_Setting(){
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
                message.what = GET_LUCKTABLE_MESSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void InitLuckTable(List<TableItem> tableItemList){
        luckTurnTable.setItemCount(tableItemList.size());

        List<String> strings = new ArrayList<String>();
        for (TableItem tableItem:tableItemList){
            strings.add(tableItem.getPresent());
        }

        luckTurnTable.setStringList(strings);
        luckTurnTable.setListener(new LuckTurnTableListener() {
            @Override
            public void TableStop(int id) {
                System.out.println("LuckTableActivity.TableStop Present is: "+ id);
                //SendResult(id);

                ResultIndex = id;

                Message message = new Message();
                message.what = SHOW_POPUPWINDOWS;
                Looper.sendMessage(message);

            }
        });

        //  Jump into Step Two
        //Get_Player_Setting();
        //TimeStart();
    }

    //---------------   Step  Two
    private void Get_Player_Setting(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);
            jsonObject.put("app_code",GlobalSignManager.App_Code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.LuckTable_Setting_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_PLAYER_MESSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void InitPlayerSetting() {

        //  Resolve
        /*
        Picasso.with(getBaseContext())
                .load()
                .into(PlayerImage);

        PlayerName.setText();
        */

        //  Jump into Step Four
        TimeStart();
    }

    //---------------   Step Four
    private void TimeStart(){
        Message message = new Message();
        message.what = TimeClick;
        Looper.sendMessage(message);
    }

    //----------------------------------------------------------------------------------------------
    private void HandleTimeOut(){
        if (luckTurnTable.getPress_Start()==0){
            System.out.println("超时未开始");
            Toast.makeText(getBaseContext(),"超时未开始",Toast.LENGTH_LONG).show();

            if (activity!=null){
                activity.finish();
            }
        }else if (luckTurnTable.getPress_Start()==1 && luckTurnTable.getPress_End()==0){
            System.out.println("超时未结束");
            Toast.makeText(getBaseContext(),"超时未结束",Toast.LENGTH_LONG).show();

            if (activity!=null){
                activity.finish();
            }
        }else if (luckTurnTable.getPress_Start()==1 && luckTurnTable.getPress_End()==1 && GetReslt_Sign == 1 ){
            System.out.println("游戏结束");
            Toast.makeText(getBaseContext(),"游戏结束",Toast.LENGTH_LONG).show();

            if (DialogActivity.activity!=null){
                DialogActivity.activity.finish();
                TimeOut = 1;
            }

            if (activity!=null && DialogActivity.activity==null){
                activity.finish();
            }
        }else if (TimeOut == 0){
            activity.finish();
        }
    }

    //----------------------------------------------------------------------------------------------
    private void ShowResult(){

        Intent intent = new Intent(LuckTableActivity.this, DialogActivity.class);
        String Text = tableItemList.get(ResultIndex).getPresent();
        String Result = tableItemList.get(ResultIndex).getType();
        if (Text!=null) {
            intent.putExtra("Prize", Text);
            intent.putExtra("Type",Result);
        }
        luckTurnTable.StopDraw();
        startActivity(intent);

        /*
        popupWindow = new MyPopupWindow(this);
        String Text = tableItemList.get(ResultIndex).getPresent();
        popupWindow.setPresentText(Text);
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.popup_windows, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        luckTurnTable.setVisibility(View.INVISIBLE);

        View rootview = LayoutInflater.from(LuckTableActivity.this).inflate(R.layout.activity_luck_table, null);

        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        */

    }

    /*    由于不使用随机转盘，暂不做使用
    private void SendResult(int id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            //jsonObject.put("jpush_id", GlobalClassManager.jPushManager.JPush_GetReginsterID(getBaseContext()));
            jsonObject.put("result",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(GlobalSignManager.LuckTable_Result_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_LUCKTABLE_MESSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }
    */

    //-------------------------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Looper.removeCallbacksAndMessages(null);

        if (tableItemList.size()>0){
            tableItemList.clear();
        }

        if (popupWindow!=null){
            popupWindow.dismiss();
        }

        activity = null;
        TimeOut = 60;

    }

    @Override
    protected void onResume() {
        super.onResume();

        //GlobalSignManager.ActivityCount++;
        //if (GlobalSignManager.ActivityCount==1){
        //    System.out.println("In Forground");
        //}

        System.out.println("ERCodeActivity.onResume.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalSignManager.App_Background_Forground_Status.equals(GlobalSignManager.Background)
                && GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Forground)){
            System.out.println("LuckTableActivity.onResume and status change to OnLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Forground;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Forground);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        //GlobalSignManager.ActivityCount--;
        //if (GlobalSignManager.ActivityCount==0){
        //    System.out.println("In Background");
        //}

        System.out.println("ERCodeActivity.onStop.AppStatus is: "+GlobalFunctionManager.isBackground(getBaseContext()));
        if (GlobalFunctionManager.isBackground(getBaseContext()).equals(GlobalSignManager.Background)){
            System.out.println("LuckTableActivity.onResume and status change to OffLine");
            GlobalSignManager.App_Background_Forground_Status = GlobalSignManager.Background;
            GlobalFunctionManager.Post_AppStatus(getBaseContext(),AppManager.AppManager_GetMac(getBaseContext()),GlobalSignManager.Background);
        }

    }

    //-----------------------------------------------------------------------------------
    public static class LuckTableActivityReceiver extends BroadcastReceiver {

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
            System.out.println("LuckTableActivityReceiver.JPushReceiver_Message_Handle.Json: "+bundle.getString(JPushInterface.EXTRA_MESSAGE));

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String , Object> JsonMap = gson.fromJson(Json ,type);

            try {
                switch ((String)JsonMap.get("Type")){
                    case JsonType.LuckTable_Start:
                        if (tableItemList!=null && tableItemList.size()>1 && tableItemList.size()<=8 && LuckTableActivity.activity!=null) {
                            luckTurnTable.Start(1.0);
                        }
                        break;
                    case JsonType.LuckTable_End:
                        //luckTurnTable.End(-0.7);

                        if (tableItemList != null && tableItemList.size()>1 && tableItemList.size()<=8 && LuckTableActivity.activity!=null) {
                            int Receice_Target = (int) ((double) JsonMap.get("Id"));
                            int Target = 0;
                            for (int i = 0; i < tableItemList.size(); i++) {
                                if (tableItemList.get(i).getId() == Receice_Target) {
                                    Target = i;
                                }
                            }
                            luckTurnTable.End_Index(-0.5, Target + 1);
                            System.out.println("LuckTableActivity.LuckTableActivityReceiver.Press_End: " + luckTurnTable.getPress_End());
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
