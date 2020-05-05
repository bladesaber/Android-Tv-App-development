package com.wanzhuan.poster.PushManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanzhuan.poster.R;

/**
 * Created by bladesaber on 2018/7/12.
 */

public class LoadingView extends RelativeLayout {

    private ProgressBar progressBar;
    private TextView textView;

    public LoadingView(Context context, String text){
        super(context);

        View layout = LayoutInflater.from(context).inflate(R.layout.loading_view,this);
        progressBar = (ProgressBar) layout.findViewById(R.id.loading_view_progress);
        textView = (TextView) layout.findViewById(R.id.loading_view_textview);
        textView.setText(text);

        /*
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(params);
        */
    }

}
