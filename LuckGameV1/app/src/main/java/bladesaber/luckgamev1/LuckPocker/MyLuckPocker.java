package bladesaber.luckgamev1.LuckPocker;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import bladesaber.luckgamev1.R;

/**
 * Created by bladesaber on 2018/4/9.
 */

public class MyLuckPocker extends PercentRelativeLayout {

    private Animation FrontAnimation;
    private Animation BackAnimation;

    private View oneView;
    private ImageView imageView;
    private TextView textView;

    private int[] Card_Picture_List = new int[]{
            R.drawable.card1,R.drawable.card2,R.drawable.card3,R.drawable.card4,
            R.drawable.card5,R.drawable.card6,R.drawable.card7,R.drawable.card8
    };

    public MyLuckPocker(Context context){
        super(context);
    }

    public MyLuckPocker(Context context, AttributeSet attrs){
        super(context,attrs);

        FrontAnimation = AnimationUtils.loadAnimation(context,R.anim.first_rotate);
        FrontAnimation.setDuration(3000);
        BackAnimation = AnimationUtils.loadAnimation(context,R.anim.last_rotate);
        BackAnimation.setDuration(3000);

        Init();

        oneView = LayoutInflater.from(context).inflate(R.layout.pocker_item,this);
        imageView = (ImageView) oneView.findViewById(R.id.Image_PockerItem);
        textView = (TextView) oneView.findViewById(R.id.Text_PockerItem);

        System.out.println("MyLuckPocker.card is: "+attrs.getAttributeIntValue("http://bladesaber/myLuckPocker","image_set",R.drawable.error));
        int id_position = attrs.getAttributeIntValue("http://bladesaber/myLuckPocker","image_set",R.drawable.error);
        if (id_position != R.drawable.error) {
            imageView.setImageResource(Card_Picture_List[id_position]);
        }
    }

    public void Init(){
        FrontAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageResource(R.drawable.bang);
                textView.setVisibility(VISIBLE);
                startAnimation(BackAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void Start(){
        startAnimation(FrontAnimation);
    }

    //-------------------     接口
    public void SetDuration(int myDuration){
        FrontAnimation.setDuration(myDuration);
        BackAnimation.setDuration(myDuration);
    }

    public void SetText(String myText){
        textView.setText(myText);
    }

    public void SetImage(int id){
        imageView.setImageResource(id);
    }

    //------------    使用方法
    // Init()
    // setDuration()

}

