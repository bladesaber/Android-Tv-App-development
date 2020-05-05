package com.wanzhuan.livegame.ActivitySet.RollLottery;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.wanzhuan.livegame.ActivitySet.RollLottery.Tool.RecycleListManager;
import com.wanzhuan.livegame.ActivitySet.RollLottery.Tool.WinnerItem;
import com.wanzhuan.livegame.AppManager.AppManager;
import com.wanzhuan.livegame.DataStructer.UserItem;
import com.wanzhuan.livegame.Global.GlobalSignManager;
import com.wanzhuan.livegame.HttpClient.PostThread;
import com.wanzhuan.livegame.MainActivity;
import com.wanzhuan.livegame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RollLotteryActivity extends Activity {

    private RollLotteryReceiver receiver = null;

    private RollLotteryManager rollLotteryManager;
    private RecyclerView recyclerView;
    private List<ViewItem> viewItemList = new ArrayList<>();

    private Type int_type = new TypeToken<Integer>() {}.getType();
    private Type string_type = new TypeToken<String>() {}.getType();

    private RecyclerView recyclerView_winnerlist;
    private RecycleListManager recycleListManager;
    private List<WinnerItem> winnerItemList = new ArrayList<>();

    private TextView number_of_player;
    private TextView number_of_winner;
    private int winner_number = 0;

    private TextView present_name_textview;
    private TextView winner_name_textview;

    private TextView text1;
    private TextView roll_lottery_name;
    private ImageView roll_lottery_logo;

    //private ViewPager viewPager;
    //private WinnerPageManager winnerPageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_lottery);

        //viewPager = (ViewPager)findViewById(R.id.viewpager);
        //winnerPageManager = new WinnerPageManager(viewPager);

        number_of_player = (TextView) findViewById(R.id.roll_lottery_number_of_player);
        number_of_player.setText("" + MainActivity.getUserItemList().size());
        number_of_winner = (TextView) findViewById(R.id.roll_lottery_winner_number);
        number_of_winner.setText(""+winner_number);

        present_name_textview = (TextView) findViewById(R.id.roll_lottery_present_text);
        winner_name_textview = (TextView) findViewById(R.id.roll_lottery_winner_name);

        recyclerView = (RecyclerView) findViewById(R.id.rollLettery_recycleview);
        init_playerlist(viewItemList);
        rollLotteryManager = new RollLotteryManager(getBaseContext(),recyclerView,viewItemList);
        rollLotteryManager.setPresentListener(new PresentListener() {
            @Override
            public void getResult(int result) {

                System.out.println("RollLotteryActivity.result is: " + result);

                UserItem userItem = MainActivity.getUserItemList().get(result);
                PostResult(userItem.user_id,receiver.getActivity_id());

                System.out.println("winner name is: "+userItem.nick_name + " user_id is: "+userItem.user_id);

                WinnerItem winnerItem = new WinnerItem();
                winnerItem.name = viewItemList.get(result).getName();
                winnerItem.present = receiver.getPresent();
                winnerItem.icon_url = viewItemList.get(result).getImage_url();

                recycleListManager.add(winnerItem);

                //winnerPageManager.addWinner(getBaseContext(),winnerItem);

                winner_number +=1;
                number_of_winner.setText(""+winner_number);

                winner_name_textview.setText(userItem.nick_name);

            }
        });

        recyclerView_winnerlist = (RecyclerView) findViewById(R.id.rollLettery_winnerlist);
        recycleListManager = new RecycleListManager(getBaseContext(),recyclerView_winnerlist,winnerItemList);

        text1 = (TextView) findViewById(R.id.roll_lottery_text1);
        roll_lottery_name = (TextView) findViewById(R.id.roll_lottery_merchants_name);
        roll_lottery_name.setText(getIntent().getStringExtra("merchants_name"));

        roll_lottery_logo = (ImageView) findViewById(R.id.roll_lottery_merchants_logo);
        Picasso.with(getBaseContext())
                .load(getIntent().getStringExtra("merchants_logo_url"))
                .config(Bitmap.Config.RGB_565)
                .into(roll_lottery_logo);

    }

    public void init_playerlist(List<ViewItem> viewItems){
        viewItems.clear();
        for (UserItem userItem: MainActivity.getUserItemList()){
            ViewItem viewItem = new ViewItem(userItem.nick_name,userItem.avatar);
            viewItems.add(viewItem);
        }
    }

    private void PostResult(int user_id,int id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("app_code", GlobalSignManager.getInstance().getApp_code());
            jsonObject.put("version_code",AppManager.AppManager_GetVersionCode(getBaseContext()));
            jsonObject.put("user_id",user_id);
            jsonObject.put("id",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.getInstance().getPost_Result_Url(),jsonObject);
        postThread.start();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();

        if (receiver==null) {
            receiver = new RollLotteryReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
            registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver!=null) {
            unregisterReceiver(receiver);
        }
        if (rollLotteryManager!=null){
            rollLotteryManager.onDestroy();
        }

        if (recycleListManager!=null){
            recycleListManager.onDestroy();
        }

        /*
        if (winnerPageManager!=null){
            winnerPageManager.onDestroy();
        }
        */
    }

    //----------------------------------------------------------------------------------------------
    private class RollLotteryReceiver extends XGPushBaseReceiver {

        public static final String LogTag = "TPushReceiver";

        private Gson gson = new Gson();

        private int activity_id;
        private String present;

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
                // StartGame
                case "LOTTERY_START":
                    text1.setVisibility(View.VISIBLE);
                    present = gson.fromJson(jsonObject.get("name"),string_type);
                    present_name_textview.setText(present);
                    activity_id = gson.fromJson(jsonObject.get("id"),int_type);
                    rollLotteryManager.start();
                    GlobalSignManager.getInstance().post_status(context,"LOTTERY_START");
                    break;
                // OutGame
                case "LOTTERY_END":
                    GlobalSignManager.getInstance().post_status(context,"WALL");
                    finish();
                    break;
                default:
                    break;
            }
        }

        public int getActivity_id(){
            return activity_id;
        }

        public String getPresent(){
            return present;
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
