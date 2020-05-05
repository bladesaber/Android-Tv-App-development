package com.example.bladesaber.tvappvc1.FloopScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bladesaber.tvappvc1.AppManager.AppManager;
import com.example.bladesaber.tvappvc1.Global.GlobalSignManager;
import com.example.bladesaber.tvappvc1.HttpClient.PostThread;
import com.example.bladesaber.tvappvc1.R;
import com.example.bladesaber.tvappvc1.SqlManager.ScreenFloopUser;
import com.example.bladesaber.tvappvc1.ViewPagerManager.PagerViewActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/5/7.
 */

public class FloopScreenManager {

    private ImageView floopScreenImageView;
    private LinearLayout linearLayout;
    private Context context;

    // 空闲
    private boolean Working = false;

    private List<ScreenFloopUser> screenFloopUserList = new ArrayList<>();

    private final int GET_FLOOP_SCREEN_IMAGE = 200;
    private final int PLAYER_MASSAGE = 201;

    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Gson gson = new Gson();
            Type type = new TypeToken<String>() {}.getType();
            Type type2 = new TypeToken<Integer>() {}.getType();

            switch (msg.what){
                case GET_FLOOP_SCREEN_IMAGE:

                    System.out.println("FloopScreenManager.Handler.GET_FLOOP_SCREEN_IMAGE: "+(String)msg.obj);
                    JsonObject jsonMessage = new JsonParser().parse((String)msg.obj).getAsJsonObject();
                    if (gson.fromJson(jsonMessage.get("msg"),type).equals(GlobalSignManager.Open_Screen_Floop)){
                        String url = gson.fromJson(jsonMessage.get("url"), type);
                        Picasso.with(context)
                                .load(url)
                                .error(R.drawable.error)
                                .fit()
                                .into(floopScreenImageView);
                        linearLayout.setVisibility(View.VISIBLE);
                    }else {
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                    break;

                case PLAYER_MASSAGE:

                    JsonObject jsonMessage2 = new JsonParser().parse((String)msg.obj).getAsJsonObject();
                    String ImageUrl = gson.fromJson(jsonMessage2.get("Url"),type);
                    String Text = gson.fromJson(jsonMessage2.get("Content"),type);
                    String playerIcon_Url = gson.fromJson(jsonMessage2.get("Avatar"),type);
                    String playerName = gson.fromJson(jsonMessage2.get("Name"),type);
                    int Time = gson.fromJson(jsonMessage2.get("Seconds"),type2);

                    /*
                    // Jump into Screen Floop Activity
                    */

                    break;

                default:
                    break;
            }

        }
    };

    public FloopScreenManager(Context context,ImageView imageView,LinearLayout linearLayout){
        this.context = context;
        floopScreenImageView = imageView;
        this.linearLayout = linearLayout;
    }

    //------------------------------------------------------------------------------------------
    public void Get_FloopScreenImage(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);
            jsonObject.put("app_code",GlobalSignManager.App_Code);
        }catch (JSONException e){
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(GlobalSignManager.Screen_Floop_Icon ,jsonObject){
            @Override
            public void Handle_Response(String JsonResult) {
                Message message = new Message();
                message.obj = JsonResult;
                message.what = GET_FLOOP_SCREEN_IMAGE;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    //---------------------------------------------------------------------------
    public void GetPlayerMessage(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(context));
            jsonObject.put("xxx_id", GlobalSignManager.xxx_id);
            jsonObject.put("app_code",GlobalSignManager.App_Code);
        }catch (JSONException e){
            e.printStackTrace();
        }

        PostThread postThread = new PostThread("....",jsonObject){
            public void Handle_Response(String Result){
                System.out.println("FloopScreenActivity.Result is: " + Result);
                Message message = new Message();
                message.what = PLAYER_MASSAGE;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    //---------------------------------------------------------------------------
    public void Start_Screen_Floop(String JsonMessage){

        Gson gson = new Gson();
        Type type = new TypeToken<String>() {}.getType();
        Type type2 = new TypeToken<Integer>() {}.getType();

        JsonObject jsonMessage2 = new JsonParser().parse(JsonMessage).getAsJsonObject();
        String ImageUrl = gson.fromJson(jsonMessage2.get("Url"),type);
        String Text = gson.fromJson(jsonMessage2.get("Content"),type);
        String playerIcon_Url = gson.fromJson(jsonMessage2.get("Avatar"),type);
        String playerName = gson.fromJson(jsonMessage2.get("Name"),type);
        int Time = gson.fromJson(jsonMessage2.get("Seconds"),type2);

        /*
        *  Jump into Screen Floop Activity
        * */
    }

    //-------------------------------------------------------------------------------
    public void addUser(String JsonMessage){

        Gson gson = new Gson();
        Type type = new TypeToken<String>() {}.getType();
        Type type2 = new TypeToken<Integer>() {}.getType();

        JsonObject jsonMessage = new JsonParser().parse(JsonMessage).getAsJsonObject();
        String ImageUrl = gson.fromJson(jsonMessage.get("Url"),type);
        String Text = gson.fromJson(jsonMessage.get("Content"),type);
        String playerIcon_Url = gson.fromJson(jsonMessage.get("Avatar"),type);
        String playerName = gson.fromJson(jsonMessage.get("Name"),type);
        int Time = gson.fromJson(jsonMessage.get("Seconds"),type2);

        ScreenFloopUser screenFloopUser = new ScreenFloopUser();
        if (ImageUrl != null) {
            screenFloopUser.setImageUrl(ImageUrl);
            screenFloopUser.setType(1);
        }else {
            screenFloopUser.setImageUrl(null);
            screenFloopUser.setType(0);
        }
        screenFloopUser.setPlayerIcon_Url(playerIcon_Url);
        screenFloopUser.setPlayerName(playerName);
        screenFloopUser.setShowText(Text);
        screenFloopUser.setTime(Time);
        screenFloopUserList.add(screenFloopUser);

        System.out.println("FloopScreenManager.addUser.getWorking: " +getWorking());
        if (!Working){
            Run();
        }
    }

    private void Run(){
        ScreenFloopUser screenFloopUser = PagerViewActivity.floopScreenManager.getNext();
        if (screenFloopUser != null) {
            if (screenFloopUser.getType() == 1) {
                Intent intent = new Intent(context, ScreenFlop2Activity.class);
                intent.putExtra("PlayerName", screenFloopUser.getPlayerName());
                intent.putExtra("PlayerIconUrl", screenFloopUser.getPlayerIcon_Url());
                intent.putExtra("ShowText", screenFloopUser.getShowText());
                intent.putExtra("Time", screenFloopUser.getTime());
                intent.putExtra("Image", screenFloopUser.getImageUrl());
                Working = true;
                System.out.println("FloopScreenManager.Run.getWorking: " +getWorking());
                context.startActivity(intent);

            } else if (screenFloopUser.getType() == 0) {
                Intent intent = new Intent(context, ScreenFlop1Activity.class);
                intent.putExtra("PlayerName", screenFloopUser.getPlayerName());
                intent.putExtra("PlayerIconUrl", screenFloopUser.getPlayerIcon_Url());
                intent.putExtra("ShowText", screenFloopUser.getShowText());
                intent.putExtra("Time", screenFloopUser.getTime());
                Working = true;
                System.out.println("FloopScreenManager.Run.getWorking: " +getWorking());
                context.startActivity(intent);
            }
        }
    }

    public void setWorking(boolean ToF){
        Working = ToF;
    }

    public boolean getWorking(){
        return Working;
    }

    public ScreenFloopUser getNext(){
        if (screenFloopUserList.size()>0) {
            ScreenFloopUser screenFloopUser = screenFloopUserList.get(0);
            screenFloopUserList.remove(0);
            return screenFloopUser;
        }else {
            return null;
        }
    }

}
