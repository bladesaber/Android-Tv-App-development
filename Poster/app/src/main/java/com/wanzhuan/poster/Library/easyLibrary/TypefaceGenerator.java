package com.wanzhuan.poster.Library.easyLibrary;

/**
 * Created by bladesaber on 2018/7/13.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

public class TypefaceGenerator {

    private static TypefaceGenerator typefaceGenerator;

    public static TypefaceGenerator getInstance(){
        if (typefaceGenerator==null){
            typefaceGenerator = new TypefaceGenerator();
        }
        return typefaceGenerator;
    }

    public Typeface createTypeface(Context context,String type_style){
        try {
            Typeface textFont = Typeface.createFromAsset(context.getAssets(), "fonts/"+type_style);
            return textFont;
        }catch (Exception e){
            Toast.makeText(context,"未找到该字体类型",Toast.LENGTH_LONG).show();
            return null;
        }
    }

}
