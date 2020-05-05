package com.example.bladesaber.tvappvc1.FloopScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bladesaber.tvappvc1.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by bladesaber on 2018/5/10.
 */

public class ScreenFloop2 extends RelativeLayout {

    private Context context;

    private ImageView Image;
    private ImageView Icon;
    private TextView Name;
    private TextView Show_Text;
    private TextView TimeClick;

    public ScreenFloop2(Context myContext){
        super(myContext);
        context = myContext;

        View oneView = LayoutInflater.from(context).inflate(R.layout.floop_screen2,this);
        Image = (ImageView) oneView.findViewById(R.id.screen_floop_image_Image);
        Icon = (ImageView) oneView.findViewById(R.id.screen_floop_image_playerIcon);
        Name = (TextView) oneView.findViewById(R.id.screen_floop_image_playername);
        Show_Text = (TextView) oneView.findViewById(R.id.floop_screen_image_TextView);
        TimeClick = (TextView) oneView.findViewById(R.id.screen_floop_TimeClick);

    }

    public ScreenFloop2(Context myContext, AttributeSet attrs){
        super(myContext,attrs);
        context = myContext;

        View oneView = LayoutInflater.from(context).inflate(R.layout.floop_screen2,this);
        Image = (ImageView) oneView.findViewById(R.id.screen_floop_image_Image);
        Icon = (ImageView) oneView.findViewById(R.id.screen_floop_image_playerIcon);
        Name = (TextView) oneView.findViewById(R.id.screen_floop_image_playername);
        Show_Text = (TextView) oneView.findViewById(R.id.floop_screen_image_TextView);
        TimeClick = (TextView) oneView.findViewById(R.id.screen_floop_TimeClick);

    }

    public void setImage(String url){
        Picasso.with(context)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .fit()
                .into(Image);
    }

    public void setIcon(String url){
        Picasso.with(context)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .fit()
                .into(Icon);
    }

    public void setName(String name){
        Name.setText(name);
    }

    public void setShow_Text(String Text){
        Show_Text.setText(Text);
    }

    public void setTimeClick(int time){
        TimeClick.setText(String.valueOf(time));
    }

}
