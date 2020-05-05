package bladesaber.luckgamev1.LuckTable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import bladesaber.luckgamev1.R;

import static java.lang.Math.abs;
import static java.lang.Math.random;

/**
 * Created by bladesaber on 2018/4/9.
 */

public class MyLuckTurnTable extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    //----------------------------------------------------------------------------------------------
    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private int TimeGo = 0;

    //----------------------------------------------------------------------------------------------
    private boolean isRunning;

    //----------------------------------------------------------------------------------------------
    private List<String> stringList = new ArrayList<String>();
    private String[] mColors = new String[] { "#ffffff","#d2311c" };
    private String[] mTextColor = new String[]{"#DC3C22","#F1D23F"};

    private int mItemCount = 0;

    private RectF mRange = new RectF();

    private int mRadius = 640;
    private int Padding = 0;

    private int ResultIndex = 0;
    //----------------------------------------------------------------------------------------------
    //绘制盘快的画笔
    private Paint mArcPaint = new Paint();;

    //绘制文字的画笔
    private Paint mTextPaint1 = new Paint();
    private Paint mTextPaint2 = new Paint();

    private Thread thread;

    private int FPS = 80;

    //----------------------------------------------------------------------------------------------
    private double AngelsCount = 0;
    private int isPlaying = 0;
    private int Press_End = 0;
    private int Press_Start = 0;

    //滚动的速度
    private double mSpeed;
    private double mAcceleration = 0;
    private double MaxSpeed = 30.0;

    private volatile double startAngle = 270;
    private float singleAngle;
    private double pre_Angle = 45;

    private double BeginStopPosition = 0;
    private double StopDistance = 0;
    private double accelerationTem = 0;

    //----------------------------------------------------------------------------------------------
    private LuckTurnTableListener listener;

    //----------------------------------------------------------------------------------------------
    private Bitmap[] bitmaps = new Bitmap[]{
            BitmapFactory.decodeResource(getResources(), R.drawable.surface_background2),
            BitmapFactory.decodeResource(getResources(), R.drawable.surface_background3)
    };
    private Bitmap mBgBitmap = bitmaps[0];
    private int bitNumber = 0;

    //----------------------------------------------------------------------------------------------
    //控件的中心位置
    private int mCenter;

    //控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
    private int mPadding;

    //----------------------------------------------------------------------------------------------
    public MyLuckTurnTable(Context context){
        super(context);
    }

    public MyLuckTurnTable(Context context, AttributeSet attrs){
        super(context,attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);

        //setZOrderOnTop(true);// 设置永远位于顶层
        //mHolder.setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(false);
        setFocusableInTouchMode(false);
        this.setKeepScreenOn(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        Padding = (width-mRadius)/2;
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // 抗锯齿
        mArcPaint.setAntiAlias(true);
        // 防颤抖
        mArcPaint.setDither(true);

        //------------------------------------------------------------------------------------------
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mTextPaint1.setColor(Color.parseColor(mTextColor[0]));
        mTextPaint1.setTextSize(30);
        mTextPaint1.setTypeface(font);

        mTextPaint2.setColor(Color.parseColor(mTextColor[1]));
        mTextPaint2.setTextSize(30);
        mTextPaint2.setTypeface(font);
        //------------------------------------------------------------------------------------------

        mRange = new RectF(getPaddingLeft()+Padding, getPaddingLeft()+Padding, mRadius + getPaddingLeft()+Padding, mRadius + getPaddingLeft()+Padding);

        isRunning = true;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void run() {
        while (isRunning){
            long startTime = System.currentTimeMillis();
            draw();

            long endTime = System.currentTimeMillis();
            try {
                if (endTime-startTime < FPS){
                    Thread.sleep(FPS - (endTime - startTime));
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void draw(){
        try {
            mCanvas = mHolder.lockCanvas();

            drawBg();

            if (mItemCount>0) {
                float tmpAngle = (float) startAngle;
                for (int i = 0; i < mItemCount; i++) {
                    // 绘制块
                    /*
                    if (i==0){
                        mArcPaint.setColor(Color.parseColor("#FFCC99"));
                    }else {
                        mArcPaint.setColor(Color.parseColor(mColors[i % 2]));
                    }
                    */
                    mArcPaint.setColor(Color.parseColor(mColors[i % 2]));

                    // Canvas.drawArc 画圆弧
                    mCanvas.drawArc(mRange, tmpAngle, singleAngle, true, mArcPaint);

                    // 绘制文本
                    if (i%2==0) {
                        drawText(tmpAngle, singleAngle, stringList.get(i),mTextPaint1);
                    }else {
                        drawText(tmpAngle, singleAngle, stringList.get(i),mTextPaint2);
                    }

                    tmpAngle += singleAngle;
                }

                //--------   刷新速度
                if ((mSpeed < MaxSpeed) && (mAcceleration > 0)) {
                    mSpeed += mAcceleration;
                } else if (mSpeed > 0 && (mAcceleration < 0)) {
                    mSpeed += mAcceleration;
                }

                //------   速度修正
                if (mSpeed > MaxSpeed) {
                    mSpeed = MaxSpeed;
                } else if (mSpeed < 0) {
                    mSpeed = 0;
                }

            /*
            System.out.println(
                    "MyLuckTable.PressEnd: "+ Press_End+" BeginStopPosition-AngleCount: "+(BeginStopPosition-AngelsCount)+
                            " mSpeed: "+mSpeed + " mAcceleration: "+mAcceleration + " AngleCount: "+AngelsCount +" isPlaying: "+isPlaying +
                            " StartAngle: "+startAngle
            );
            */
                //-------  刷新位置与统计转动角度
                if (Press_End != 1) {
                    startAngle = (startAngle + mSpeed) % 360;
                    if (isPlaying == 1 && mSpeed > 0) {
                        AngelsCount = (AngelsCount + mSpeed) % 360;
                    } else if (isPlaying == 1 && mSpeed == 0) {
                        isPlaying = 0;
                        Trigger1();
                    }

                } else if (BeginStopPosition - AngelsCount >= 0 && BeginStopPosition - AngelsCount >= mSpeed && mAcceleration > 0) {
                    startAngle = (startAngle + mSpeed) % 360;
                    if (isPlaying == 1 && mSpeed > 0) {
                        AngelsCount = (AngelsCount + mSpeed) % 360;
                    } else if (isPlaying == 1 && mSpeed == 0) {
                        isPlaying = 0;
                        Trigger1();
                    }

                } else if (BeginStopPosition - AngelsCount >= 0 && BeginStopPosition - AngelsCount < mSpeed && mAcceleration > 0) {
                    startAngle = (startAngle + (BeginStopPosition - AngelsCount)) % 360;
                    if (isPlaying == 1 && mSpeed > 0) {
                        AngelsCount = (AngelsCount + BeginStopPosition - AngelsCount) % 360;
                    } else if (isPlaying == 1 && mSpeed == 0) {
                        isPlaying = 0;
                        Trigger1();
                    }

                    mAcceleration = accelerationTem;
                } else if (mAcceleration > 0 && BeginStopPosition - AngelsCount < 0) {
                    startAngle = (startAngle + mSpeed) % 360;
                    if (isPlaying == 1 && mSpeed > 0) {
                        AngelsCount = (AngelsCount + mSpeed) % 360;
                    } else if (isPlaying == 1 && mSpeed == 0) {
                        isPlaying = 0;
                        Trigger1();
                    }

                } else if (mAcceleration < 0) {
                    startAngle = (startAngle + mSpeed) % 360;
                    if (isPlaying == 1 && mSpeed > 0) {
                        AngelsCount = (AngelsCount + mSpeed) % 360;
                    } else if (isPlaying == 1 && mSpeed == 0) {
                        isPlaying = 0;
                        Trigger1();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawText(float startAngle, float sweepAngle, String string, Paint paint)
    {
        Path path = new Path();
        path.addArc(mRange, startAngle, sweepAngle);
        float textWidth = paint.measureText(string);

        // 利用水平偏移让文字居中
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);// 水平偏移
        float vOffset = mRadius / 2 / 6;// 垂直偏移
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, paint);
    }

    private void drawText(String text){

    }

    private void drawBg() {
        if (mCanvas!=null) {

            if (TimeGo<600){
                TimeGo+=FPS;
            }else {
                TimeGo=0;
                bitNumber++;
                mBgBitmap = bitmaps[bitNumber%2];
            }

            mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2,
                    mPadding / 2, getMeasuredWidth() - mPadding / 2,
                    getMeasuredWidth() - mPadding / 2), null);
        }
    }

    private double Get_BeginStopPosition(double targetPosition,double nowSpeed,double nowAcceleration){
        StopDistance = nowSpeed * ((nowSpeed-nowAcceleration)/nowAcceleration) / 2;
        if (targetPosition - StopDistance >= 0){
            return targetPosition-StopDistance;
        }else {
            int numScale = (int)(abs(targetPosition - StopDistance)/360);
            return 360*(numScale+1) + targetPosition - StopDistance;
        }
    }

    public void Start(double acceleration)
    {
        if (Press_Start == 0) {
            AngelsCount = 0;
            isPlaying = 1;
            Press_Start = 1;
            mAcceleration = acceleration;
        }
    }

    public void End(double acceleration)
    {
        if (Press_Start==1) {
            mAcceleration = acceleration;
            Press_End = 1;
        }
    }

    public void End_Index(double acceleration,int index)
    {
        if (Press_Start==1) {
            Press_End = 1;
            ResultIndex = index - 1;
            //double targetPosition = (index-1+Math.random())*(360/mItemCount)+pre_Angle;

            double random_number = random();
            if (random_number<0.1){
                random_number=0.1;
            }else if (random_number>0.9){
                random_number=0.9;
            }
            random_number = ((double)Math.round(random_number/0.1))*0.1;
            //System.out.println("MyLuckTable.End_Index.random_number: "+random_number);

            double targetPosition = 360 - (index - 1 + random_number) * (360 / mItemCount);

            double TemBeginStopPosition = Get_BeginStopPosition(targetPosition, mSpeed, -acceleration) + pre_Angle;
            if (TemBeginStopPosition > 360) {
                BeginStopPosition = TemBeginStopPosition - 360;
            } else {
                BeginStopPosition = TemBeginStopPosition;
            }

            accelerationTem = acceleration;

            System.out.println("MyLuckTable.TargetPosition: " + targetPosition);
            System.out.println("MyLuckTable.BeginStopPosition: " + BeginStopPosition);
            System.out.println("MyLuckTable.StopDistance: " + StopDistance);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    //----------------------------------------------------------------------------------------------
    private int FinishCursor(double Angle){
        // 角度计算，目前不做使用
        return 0;
    }

    //----------------------------------------------------------------------------------------------
    public void setListener(LuckTurnTableListener myListener){
        listener = myListener;
    }

    private void Trigger1(){
        listener.TableStop(ResultIndex);
    }

    //-------------------------   接口
    public void setMaxSpeed(double SpeedMax){
        MaxSpeed = SpeedMax;
    }

    public void setFPS(int myFps){
        FPS = myFps;
    }

    public void setStringList(List<String> myStringList){
        stringList = myStringList;
    }

    public void setTextSize(String color){
        mTextPaint1.setColor(Color.parseColor(color));
    }

    public void setTextSize(int Size){
        mTextPaint1.setTextSize(Size);
    }

    public void setItemCount(int myItem){
        mItemCount = myItem;
        singleAngle = (float) 360.0/mItemCount;
    }

    //-------------------------------------
    public int getPress_Start(){
        return Press_Start;
    }

    public int getPress_End(){
        return Press_End;
    }

    public void StopDraw(){
        isRunning = false;
    }

    //-------    使用方法
    // setStringList()
    // setItemCount()
    // Start()
}
