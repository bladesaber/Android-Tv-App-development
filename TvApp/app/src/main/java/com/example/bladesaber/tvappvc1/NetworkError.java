package com.example.bladesaber.tvappvc1;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class NetworkError extends AppCompatActivity {

    public static Activity activity = null;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);

        activity = NetworkError.this;

        textView = (TextView) findViewById(R.id.textView_NetworkError);

    }

    @Override
    protected void onDestroy() {
        activity = null;
        super.onDestroy();
    }

}
