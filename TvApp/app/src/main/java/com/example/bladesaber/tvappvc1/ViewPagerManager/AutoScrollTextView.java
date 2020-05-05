package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bladesaber.tvappvc1.SqlManager.TextItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class AutoScrollTextView extends TextView {

    private float textLength = 0f;// 文本长度
    private float viewWidth = 0f; // View 的长度

    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标

    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量

    public boolean isStarting = false;//是否开始滚动

    private Paint paint = null;//绘图样式

    private String text = "";//文本内容

    private float speed = 1.0f;

    private WindowManager windowManager;

    private String textBuffer="";
    private List<TextItem> TextList_Buffer = new ArrayList<TextItem>();
    private int TextList_Buffer_ID = 0;

    /*
    Use Insitituion:

        1 , autoScrollTextView.InitWindowManager(getWindowManager());
        2 , String text1 = "";
        3 , autoScrollTextView.InitText(text1);
        4 , autoScrollTextView.startScroll();

     */

    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void InitWindowManager(WindowManager mywindowManager){
        windowManager = mywindowManager;
    }

    public void InitText(String mytext)
    {
        this.setText(mytext);
        paint = getPaint();
        text = getText().toString();

        textLength = paint.measureText(text);
        //Log.d("AutoScrollTextView","textLength is "+textLength);
        viewWidth = getWidth();
        //Log.d("AutoScrollTextView","viewLength_1 is "+viewWidth);

        if(viewWidth == 0)
        {
            if(windowManager != null)
            {
                Display display = windowManager.getDefaultDisplay();
                viewWidth = display.getWidth();
            }
        }
        //Log.d("AutoScrollTextView","viewLength_2 is "+viewWidth);

        step = textLength;
        //Log.d("AutoScrollTextView","step is "+step);

        temp_view_plus_text_length = viewWidth + textLength;
        //Log.d("AutoScrollTextView","temp_view_plus_text_length is "+temp_view_plus_text_length);

        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        //Log.d("AutoScrollTextView","temp_view_plus_two_text_length is "+temp_view_plus_two_text_length);

        //Log.d("AutoScrollTextView","PaddingTop is "+getPaddingTop());
        y = getTextSize() + getPaddingTop();
        //Log.d("AutoScrollTextView","y is "+y);
    }

    //  介绍Parcelable不得不先提一下Serializable接口,Serializable是Java为我们提供的一个标准化的序列化接口,那什么是序列化呢?
    //  简单来说就是将对象转换为可以传输的二进制流(二进制序列)的过程,这样我们就可以通过序列化,转化为可以在网络传输或者保存到本地的流(序列),从而进行传输数据
    //  那反序列化就是从二进制流(序列)转化为对象的过程.
    //  Parcelable是Android为我们提供的序列化的接口
    @Override
    public Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;

    }

    //--------------------------    接口
    public void Set_Whether_visiable(int tyle){
        switch (tyle){
            case 0:
                this.setVisibility(VISIBLE);
                break;
            case 1:
                this.setVisibility(INVISIBLE);
                break;
            default:
                Log.d("AutoScrollTextView","Set_Whether_visiable tyle Error");
        }
    }

    public void Set_TextSize(float size){
        System.out.println("AutoScrollTextView.setTextSize: "+size);
        this.setTextSize(size);
        y = getTextSize() + getPaddingTop();
    }

    public void Set_BackgroundColor(int red,int green,int blue){
        this.setBackgroundColor(Color.rgb(red,green,blue));
    }

    public void Set_TextColor(int red,int green,int blue){
        paint.setColor(Color.rgb(red,green,blue));
    }

    public void Set_TextColor(String color){
        paint.setColor(Color.parseColor(color));
    }

    public void SetSpeed(float myspeed){
        speed = myspeed;
    }

    public void SetText(String mytext){
        if ( (mytext != null) && ( !mytext.equals(""))) {
            //Log.d("AutoScrollTextView","TextBuffer Have Change");
            textBuffer = mytext;
        }
    }

    //------------------------------------------------------------------

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;

    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = null;
            in.readBooleanArray(b);
            if(b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }


    public void startScroll()
    {
        isStarting = true;

        // invalidate() 用于重绘View
        // 由此处会重新触发 View 的 onDraw 流程
        invalidate();
    }


    public void stopScroll()
    {
        isStarting = false;
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        //Log.d("AutoScrollTextView","onDraw");
        /*
         * text:要绘制的文字
         * x：绘制原点x坐标
         * y：绘制原点y坐标
         * paint:用来做画的画笔
        public void drawText(String text, float x, float y, Paint paint)
         */

        // temp_view_plus_text_length - step 是起始绘画点，当字幕不断向左走，这个值不断变小，
        // 当 temp_view_plus_text_length - step = -text_length 时 一段字幕完全走完
        // 绘画内容是 text
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if(!isStarting)
        {
            return;
        }
        step += speed;
        if(step > temp_view_plus_two_text_length) {

            //System.out.println("AutoScrollTextView.onDraw.TextList_Buffer.size: "+ TextList_Buffer.size());

            if (TextList_Buffer.size() > 0) {

                if (TextList_Buffer.get(TextList_Buffer_ID).getContent() != null) {
                    textBuffer = TextList_Buffer.get(TextList_Buffer_ID).getContent();
                }
                System.out.println("AntoScrollTextView.TextBuffer is : " + textBuffer);

                if (!textBuffer.equals("")) {
                    InitText(textBuffer);
                }

                Update_TextList_Id();

                step = textLength;
            }
        }
        invalidate();
    }

    public void Set_TextList_Buffer(List<TextItem> textList){
        TextList_Buffer.clear();
        TextList_Buffer.addAll(textList);
        TextList_Buffer_ID = 0;
    }

    private void Update_TextList_Id(){
        if (TextList_Buffer_ID == TextList_Buffer.size()-1){
            TextList_Buffer_ID = 0;
            return;
        }
        TextList_Buffer_ID += 1 ;
    };

    public void Show_Text_List_Buffer(){
        for (int i=0;i<TextList_Buffer.size();i++){
            System.out.println("AntoScrollTextView.Content is : "+TextList_Buffer.get(i).getContent());
        }
    }

    public void RemoveAllText(){
        TextList_Buffer.clear();
        textBuffer = "";
        TextList_Buffer_ID = 0;
        String mytext="";
        InitText(mytext);
    }

}

