package com.wanzhuan.poster.Library.easyLibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import com.wanzhuan.poster.ViewContainer.ViewContainer;

import java.util.List;

/**
 * Created by bladesaber on 2018/7/21.
 */

public class ModefyManager {

    private static ModefyManager modefyManager;

    public static ModefyManager getInstance(){
        if (modefyManager==null){
            modefyManager = new ModefyManager();
        }
        return modefyManager;
    }

    public void modefy(Context context,List<ViewContainer> viewContainerList, int viewContainer_id, int view_id, String changeText){
        for (ViewContainer viewContainer:viewContainerList){
            if (viewContainer.id == viewContainer_id){
                for (ViewModify_Model view_modifyModel_tem : viewContainer.getViewList()){
                    if (view_modifyModel_tem.viewModel.id == view_id){
                        if (view_modifyModel_tem.viewModel.type.equals("image")){

                            if (view_modifyModel_tem.view instanceof ImageView) {
                                Picasso.with(context)
                                        .load(changeText)
                                        .config(Bitmap.Config.RGB_565)
                                        .into((ImageView) view_modifyModel_tem.view);
                            }

                        }else if (view_modifyModel_tem.viewModel.type.equals("text")){

                            if (view_modifyModel_tem.view instanceof TextView) {
                                ((TextView) view_modifyModel_tem.view).setText(changeText);
                            }

                        }else if (view_modifyModel_tem.viewModel.type.equals("lottie")){

                            if (view_modifyModel_tem.view instanceof MyLottiesAnimationView){
                                MyLottiesAnimationView animationView = (MyLottiesAnimationView) view_modifyModel_tem.view;
                                animationView.change(changeText);
                            }

                        }
                    }
                }
            }
        }
    }

    public void modefy(Context context,List<ViewContainer> viewContainerList, int viewContainer_id, int view_id, ModifySet modifySet){
        for (ViewContainer viewContainer:viewContainerList){
            if (viewContainer.id == viewContainer_id){
                for (ViewModify_Model view_modifyModel_tem : viewContainer.getViewList()){
                    if (view_modifyModel_tem.viewModel.id == view_id){
                        if (view_modifyModel_tem.viewModel.type.equals("image")){

                            System.out.println("");

                        }else if (view_modifyModel_tem.viewModel.type.equals("text")){

                            System.out.println("");

                        }else if (view_modifyModel_tem.viewModel.type.equals("lottie")){

                            System.out.println("");

                        }
                    }
                }
            }
        }
    }

    public class ModifySet{
        public String commond;
        public Object object;
    }

}
