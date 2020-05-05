package com.wanzhuan.livegame.TestActvity;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.wanzhuan.livegame.ActivitySet.RollLottery.MyRecycleAdapter;
import com.wanzhuan.livegame.ActivitySet.RollLottery.ViewItem;
import com.wanzhuan.livegame.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        add = (Button) findViewById(R.id.testActivity_Play);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}
