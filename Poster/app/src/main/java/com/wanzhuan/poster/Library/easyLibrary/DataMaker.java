package com.wanzhuan.poster.Library.easyLibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import com.wanzhuan.poster.AppManager.AppManager;
import com.wanzhuan.poster.DataStructure.ViewModel;
import com.wanzhuan.poster.Global.GlobalSignManager;
import com.wanzhuan.poster.LayoutCheck.LayoutCheckManager;
import com.wanzhuan.poster.Library.Utils;
import com.wanzhuan.poster.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bladesaber on 2018/6/28.
 */

public class DataMaker {

    private static DataMaker dataMaker;

    public static DataMaker getInstance(){
        if (dataMaker==null){
            dataMaker = new DataMaker();
        }
        return dataMaker;
    }

    public ViewInfo.PositionInfo createPositionInfo(Context context,
                                        int width, int height,
                                        int margin_left,int margin_top){
        ViewInfo viewInfo = new ViewInfo();

        viewInfo.positionInfo.width = (int) Utils.getInstance(context).calculate_width(width,1.5f,1920f);
        viewInfo.positionInfo.height = (int)Utils.getInstance(context).calculate_height(height,1.5f,1080f);
        viewInfo.positionInfo.margin_left = (int)Utils.getInstance(context).calculate_width(margin_left,1.5f,1920f);
        viewInfo.positionInfo.margin_right = (int)(Utils.getInstance(context).getScreenWidth() - viewInfo.positionInfo.margin_left - viewInfo.positionInfo.width);
        viewInfo.positionInfo.margin_top = (int)Utils.getInstance(context).calculate_height(margin_top,1.5f,1080f);
        viewInfo.positionInfo.margin_bottom = (int)(Utils.getInstance(context).getScreenHeight() - viewInfo.positionInfo.margin_top - viewInfo.positionInfo.height);

        /*
        viewInfo.positionInfo.width = width;
        viewInfo.positionInfo.height = height;
        viewInfo.positionInfo.margin_left = margin_left;
        viewInfo.positionInfo.margin_right = (int)Utils.getInstance(context).getScreenWidth() - margin_left - width;
        viewInfo.positionInfo.margin_top = margin_top;
        viewInfo.positionInfo.margin_bottom = (int)Utils.getInstance(context).getScreenHeight() - margin_top;
        */

        return viewInfo.positionInfo;
    }

    public int getRestartTime(){
        return 15000;
    }

    public ImageView createImageView(Context context,
                            int width, int height,
                            int margin_left, int margin_top, int Resource_id){
        ImageView imageView = new ImageView(context);

        int caluate_width = (int)Utils.getInstance(context).calculate_width(width,1.5f,1920f);
        int caluate_height = (int)Utils.getInstance(context).calculate_height(height,1.5f,1080f);
        int caluate_left_margin = (int)Utils.getInstance(context).calculate_width(margin_left,1.5f,1920f);
        int caluate_top_margin = (int)Utils.getInstance(context).calculate_height(margin_top,1.5f,1080f);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(caluate_width,caluate_height);
        params.leftMargin = caluate_left_margin;
        params.topMargin = caluate_top_margin;

        /*
        System.out.println("caluate width: " + caluate_width);
        System.out.println("caluate height: " + caluate_height);
        System.out.println("caluate left_margin: " + caluate_left_margin);
        System.out.println("caluate top_margin: " + caluate_top_margin);
        */

        imageView.setImageResource(Resource_id);
        imageView.setLayoutParams(params);
        return imageView;
    }

    public ImageView createImageView(Context context,
                                     int width, int height,
                                     int margin_left, int margin_top, String url){
        ImageView imageView = new ImageView(context);

        int caluate_width = (int)Utils.getInstance(context).calculate_width(width,1.5f,1920f);
        int caluate_height = (int)Utils.getInstance(context).calculate_height(height,1.5f,1080f);
        int caluate_left_margin = (int)Utils.getInstance(context).calculate_width(margin_left,1.5f,1920f);
        int caluate_top_margin = (int)Utils.getInstance(context).calculate_height(margin_top,1.5f,1080f);

        if (LayoutCheckManager.getInstance().check_Height(context,caluate_height+caluate_top_margin) ||
                LayoutCheckManager.getInstance().check_Width(context,caluate_left_margin+caluate_width) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_height) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_width) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_left_margin) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_top_margin)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(caluate_width, caluate_height);
            params.leftMargin = caluate_left_margin;
            params.topMargin = caluate_top_margin;

        /*
        System.out.println("caluate width: " + caluate_width);
        System.out.println("caluate height: " + caluate_height);
        System.out.println("caluate left_margin: " + caluate_left_margin);
        System.out.println("caluate top_margin: " + caluate_top_margin);
        */

            Picasso.with(context)
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    .into(imageView);

            imageView.setLayoutParams(params);
            return imageView;
        }else {
            GlobalSignManager.Erro_Layout_Check = false;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("error_view_url", url );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LayoutCheckManager.getInstance().send_Error(context,jsonObject);
            Toast.makeText(context, "模板布局不适配", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public TextView createTextView(Context context,
                                     int width, int height,
                                     int margin_left, int margin_top,
                                   String text,String color,float textSize){
        TextView textView = new TextView(context);

        int caluate_width = (int)Utils.getInstance(context).calculate_width(width,1.5f,1920f);
        int caluate_height = (int)Utils.getInstance(context).calculate_height(height,1.5f,1080f);
        int caluate_left_margin = (int)Utils.getInstance(context).calculate_width(margin_left,1.5f,1920f);
        int caluate_top_margin = (int)Utils.getInstance(context).calculate_height(margin_top,1.5f,1080f);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(caluate_width,caluate_height);
        params.leftMargin = caluate_left_margin;
        params.topMargin = caluate_top_margin;

        /*
        System.out.println("caluate width: " + caluate_width);
        System.out.println("caluate height: " + caluate_height);
        System.out.println("caluate left_margin: " + caluate_left_margin);
        System.out.println("caluate top_margin: " + caluate_top_margin);
        */

        textView.setText(text);
        textView.setTextColor(Color.parseColor(color));
        textView.setTextSize(textSize);
        textView.setLayoutParams(params);
        return textView;
    }

    public TextView createTextView(Context context,
                                   int width, int height,
                                   int margin_left, int margin_top,
                                   String text,String color,float textSize,
                                   String type,String front_style,Float line_space,Float letter_space){
        TextView textView = new TextView(context);

        if (line_space!=null){
            textView.setLineSpacing(0,line_space);
        }
        if (letter_space!=null){
            textView.setLetterSpacing(letter_space);
        }

        int caluate_width = (int)Utils.getInstance(context).calculate_width(width,1.5f,1920f);
        int caluate_height = (int)Utils.getInstance(context).calculate_height(height,1.5f,1080f);
        int caluate_left_margin = (int)Utils.getInstance(context).calculate_width(margin_left,1.5f,1920f);
        int caluate_top_margin = (int)Utils.getInstance(context).calculate_height(margin_top,1.5f,1080f);

        if (LayoutCheckManager.getInstance().check_Height(context,caluate_height+caluate_top_margin) ||
                LayoutCheckManager.getInstance().check_Width(context,caluate_left_margin+caluate_width) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_height) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_width) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_left_margin) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_top_margin)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(caluate_width, caluate_height);
            params.leftMargin = caluate_left_margin;
            params.topMargin = caluate_top_margin;

            /*
            System.out.println("caluate width: " + caluate_width);
            System.out.println("caluate height: " + caluate_height);
            System.out.println("caluate left_margin: " + caluate_left_margin);
            System.out.println("caluate top_margin: " + caluate_top_margin);
            */

            Typeface typeface = null;
            if (front_style!=null && !front_style.equals("")){
                typeface = TypefaceGenerator.getInstance().createTypeface(context,front_style);
            }
            if (type.equals("normal")) {
                textView.setTypeface(typeface, Typeface.NORMAL);
            } else if (type.equals("bold")) {
                textView.setTypeface(typeface, Typeface.BOLD);
            } else if (type.equals("italic")) {
                textView.setTypeface(typeface, Typeface.ITALIC);
            }

            textView.setText(text);
            textView.setTextColor(Color.parseColor(color));
            textView.setTextSize(textSize);
            textView.setLayoutParams(params);
            return textView;
        }else {
            GlobalSignManager.Erro_Layout_Check = false;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("error_view_text", text );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LayoutCheckManager.getInstance().send_Error(context,jsonObject);
            Toast.makeText(context, "模板布局不适配", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public LottieAnimationView create_LottieView(Context context,
                                                 int width, int height,
                                                 int margin_left, int margin_top, ViewModel.LottieSetting lottieSetting){
        MyLottiesAnimationView animationView = new MyLottiesAnimationView(context,lottieSetting);

        int caluate_width = (int)Utils.getInstance(context).calculate_width(width,1.5f,1920f);
        int caluate_height = (int)Utils.getInstance(context).calculate_height(height,1.5f,1080f);
        int caluate_left_margin = (int)Utils.getInstance(context).calculate_width(margin_left,1.5f,1920f);
        int caluate_top_margin = (int)Utils.getInstance(context).calculate_height(margin_top,1.5f,1080f);

        if (LayoutCheckManager.getInstance().check_Height(context,caluate_height+caluate_top_margin) ||
                LayoutCheckManager.getInstance().check_Width(context,caluate_left_margin+caluate_width) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_height) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_width) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_left_margin) ||
                LayoutCheckManager.getInstance().check_Zero(caluate_top_margin)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(caluate_width, caluate_height);
            params.leftMargin = caluate_left_margin;
            params.topMargin = caluate_top_margin;

            animationView.setLayoutParams(params);
            return animationView;
        }else {
            GlobalSignManager.Erro_Layout_Check = false;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("error_view_url","lottie file");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LayoutCheckManager.getInstance().send_Error(context,jsonObject);
            Toast.makeText(context, "模板布局不适配", Toast.LENGTH_LONG).show();
            return null;
        }
    }

}
