package com.example.bladesaber.tvappvc1.FloopScreen;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bladesaber.tvappvc1.R;
import com.example.bladesaber.tvappvc1.Renderer.PicassoCircleTransformation;
import com.example.bladesaber.tvappvc1.SqlManager.ScreenFloopUser;
import com.example.bladesaber.tvappvc1.ViewPagerManager.PagerViewActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ScreenFlop1Activity extends AppCompatActivity {

    private String playerName;
    private String playerIcon_Url;
    private String show_Text;
    private int Time;
    private int Lock = 0;
    private int Last = 0;

    private TextView ShowText;
    private ImageView Icon;
    private TextView Name;
    private TextView TimeClick;

    private final int TIME_CLICK = 200;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_CLICK:

                    if (Time==0){
                        if (Lock==0) {
                            Intent intent = GetNext();
                            if (intent != null) {
                                System.out.println("ScreenFlop1Activity.getWorking: " + PagerViewActivity.floopScreenManager.getWorking());
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            } else {
                                Last = 1;
                            }
                            Lock = 1;
                        }

                        if (Last==1) {
                            PagerViewActivity.floopScreenManager.setWorking(false);
                        }

                        finish();
                    }

                    if (Time>0){
                        Time--;
                        TimeClick.setText(String.valueOf(Time));
                    }

                    Message message = new Message();
                    message.what = TIME_CLICK;
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
        setContentView(R.layout.floop_screen);

        playerName = getIntent().getStringExtra("PlayerName");
        playerIcon_Url = getIntent().getStringExtra("PlayerIconUrl");
        show_Text = getIntent().getStringExtra("ShowText");
        Time = getIntent().getIntExtra("Time",10);

        Init();

    }

    private void Init(){
        ShowText = (TextView) findViewById(R.id.floop_screen_Text);
        ShowText.setText(show_Text);

        Icon = (ImageView) findViewById(R.id.screen_floop_playericon);
        Picasso.with(getBaseContext())
                .load(playerIcon_Url)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .fit()
                .transform(new PicassoCircleTransformation())
                .into(Icon);

        Name = (TextView) findViewById(R.id.screen_floop_playername);
        Name.setText(playerName);

        TimeClick = (TextView) findViewById(R.id.screen_floop_TimeClick);
        TimeClick.setText(String.valueOf(Time));

        TimeStart();
    }

    private Intent GetNext(){
        ScreenFloopUser screenFloopUser = PagerViewActivity.floopScreenManager.getNext();
        if (screenFloopUser != null){
            if (screenFloopUser.getType()==1){
                Intent intent = new Intent(this,ScreenFlop2Activity.class);
                intent.putExtra("PlayerName",screenFloopUser.getPlayerName());
                intent.putExtra("PlayerIconUrl",screenFloopUser.getPlayerIcon_Url());
                intent.putExtra("ShowText",screenFloopUser.getShowText());
                intent.putExtra("Time",screenFloopUser.getTime());
                intent.putExtra("Image",screenFloopUser.getImageUrl());
                return intent;

            }else if (screenFloopUser.getType()==0){
                Intent intent = new Intent(this,ScreenFlop1Activity.class);
                intent.putExtra("PlayerName",screenFloopUser.getPlayerName());
                intent.putExtra("PlayerIconUrl",screenFloopUser.getPlayerIcon_Url());
                intent.putExtra("ShowText",screenFloopUser.getShowText());
                intent.putExtra("Time",screenFloopUser.getTime());
                return intent;
            }

        }
        return null;
    }

    private void TimeStart(){
        Message message = new Message();
        message.what = TIME_CLICK;
        Looper.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        Looper.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
