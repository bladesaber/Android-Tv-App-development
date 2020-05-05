package com.example.bladesaber.tvappvc1.Renderer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class BlurTransformationPassico implements Transformation {

    RenderScript rs;

    public BlurTransformationPassico(Context context) {
        super();
        rs = RenderScript.create(context);
    }

    @SuppressLint("NewApi")
    @Override
    public Bitmap transform(Bitmap bitmap) {
        // 创建一个Bitmap作为最后处理的效果Bitmap
        Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        // 分配内存
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // 根据我们想使用的配置加载一个实例
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // 设置模糊半径
        script.setRadius(25);

        //开始操作
        script.forEach(output);

        // 将结果copy到blurredBitmap中
        output.copyTo(blurredBitmap);

        //释放资源
        bitmap.recycle();

        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur";
    }
}
