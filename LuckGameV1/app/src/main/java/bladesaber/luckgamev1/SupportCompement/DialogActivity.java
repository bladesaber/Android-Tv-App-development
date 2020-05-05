package bladesaber.luckgamev1.SupportCompement;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import bladesaber.luckgamev1.Global.GlobalSignManager;
import bladesaber.luckgamev1.LuckTable.LuckTableActivity;
import bladesaber.luckgamev1.R;

public class DialogActivity extends AppCompatActivity {

    public static Activity activity;

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        activity = this;

        textView = (TextView) findViewById(R.id.DialogText_DialogActivity);
        //System.out.println("DialogActivity.Prizes is: "+getIntent().getStringExtra("Prize"));
        if (getIntent().getStringExtra("Type").equals(GlobalSignManager.Win)) {
            textView.setText(getIntent().getStringExtra("Prize"));
        }

        imageView = (ImageView) findViewById(R.id.Dialoag_Guang);

        //Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.guang);
        //imageView.startAnimation(animation);

        //RotateAnimation rotateAnimation = new RotateAnimation(0f,5400f);
        //rotateAnimation.setDuration((LuckTableActivity.PrizeDuration-1)*1000);
        //imageView.startAnimation(rotateAnimation);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }
}
