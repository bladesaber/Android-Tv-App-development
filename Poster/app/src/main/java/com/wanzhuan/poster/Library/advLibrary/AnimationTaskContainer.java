package com.wanzhuan.poster.Library.advLibrary;

import com.wanzhuan.poster.Library.advLibrary.Example_AnimationTask_List.AnimationTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/6/27.
 */

public class AnimationTaskContainer extends TaskContainer {

    private List<AnimationTask> tasks = new ArrayList<>();

    @Override
    void ResloveTask() {
        for (AnimationTask task:tasks){
            if (task.getDelayTime()<=0){
                task.start();
                task.setDelayTime(task.getReStartTime());
            }else{
                task.setDelayTime(task.getDelayTime()-1);
            }
        }
    }
}
