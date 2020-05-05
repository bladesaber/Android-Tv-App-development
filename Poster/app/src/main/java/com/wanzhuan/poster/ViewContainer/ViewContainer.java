package com.wanzhuan.poster.ViewContainer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.wanzhuan.poster.Library.easyLibrary.ContainerPosterManager;
import com.wanzhuan.poster.Library.easyLibrary.ViewModify_Model;
import com.wanzhuan.poster.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/6/30.
 */

public class ViewContainer extends RelativeLayout{

    public int id;

    private ImageView background;

    private List<ContainerPosterManager.ContainerAnimationTask> tasks;
    private List<ViewModify_Model> viewList;

    private Context context;

    private int time = 600000;

    public ViewContainer(Context context,int id){
        super(context);
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.view_container,this);
        background = (ImageView) view.findViewById(R.id.background);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);

        tasks = new ArrayList<>();
        viewList = new ArrayList<>();

        this.id = id;

    }

    public void setBackground(String url){
        Picasso.with(context)
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .into(background);
    }

    public void remove(int id){
        tasks.remove(id);
    }

    public List<ContainerPosterManager.ContainerAnimationTask> getTasks(){
        return tasks;
    }

    public List<ViewModify_Model> getViewList(){
        return viewList;
    }

    public void setTime(int Time){
        this.time = Time;
    }

    public int getTime(){
        return time;
    }

    public void stop(){
        if (tasks!=null && tasks.size()>0){
            for (ContainerPosterManager.ContainerAnimationTask task:tasks){
                task.stop();
            }
        }
    }
}
