package com.example.bladesaber.tvappvc1.ViewPagerManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.bladesaber.tvappvc1.R;
import com.example.bladesaber.tvappvc1.Renderer.PicassoRoundTransform;
import com.example.bladesaber.tvappvc1.Renderer.PicassoTransformation;
import com.example.bladesaber.tvappvc1.SqlManager.ImageItem;
import com.example.bladesaber.tvappvc1.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class ViewPagerItem extends PercentRelativeLayout {

    private int ID;
    private ImageView imageView;
    private String Url;

    ViewPagerItem(Context context){
        super(context);
    }

    ViewPagerItem(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public void SetImage(Context context,int id){
        View oneView = LayoutInflater.from(context).inflate(R.layout.viewpager_item,this);
        imageView = (ImageView) oneView.findViewById(R.id.imageView_ViewPagerItem);
        imageView.setImageResource(id);
    }

    public void SetImage(Context context, ImageItem imageItem){
        View oneView = LayoutInflater.from(context).inflate(R.layout.viewpager_item,this);
        imageView = (ImageView) oneView.findViewById(R.id.imageView_ViewPagerItem);

        Picasso.with(context)
                .load(imageItem.getUrl())
                //.config(Bitmap.Config.RGB_565)
                .error(R.drawable.error)
                //.networkPolicy(NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                //.transform(new PicassoRoundTransform())
                //.transform(new PicassoTransformation(context,2))
                .fit()
                .centerCrop()
                .into(imageView);

        Url = imageItem.getUrl();
        this.ID = imageItem.getID();
    }

    public void SetImage(Context context, String myUrl){
        View oneView = LayoutInflater.from(context).inflate(R.layout.viewpager_item,this);
        imageView = (ImageView) oneView.findViewById(R.id.imageView_ViewPagerItem);

        Picasso.with(context)
                .load(myUrl)
                //.config(Bitmap.Config.RGB_565)
                .error(R.drawable.error)
                //.networkPolicy(NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                //.transform(new PicassoRoundTransform())
                //.transform(new PicassoTransformation(context,2))
                .fit()
                .centerCrop()
                .into(imageView);

        Url = myUrl;
    }

    public String getUrl(){
        return Url;
    }

    public int getID(){
        return ID;
    }

    public void setID(int myid){
        ID = myid;
    }

}
