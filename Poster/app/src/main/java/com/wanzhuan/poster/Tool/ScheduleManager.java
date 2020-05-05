package com.wanzhuan.poster.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/7/23.
 */

public class ScheduleManager {

    private static ScheduleManager scheduleManager = null;

    private List<ScheduleModel.ScheduleItem> scheduleModelList = new ArrayList<>();
    private int current_schdule_id = -1;

    private Schedule_Listener listener;

    public static ScheduleManager getInstance(){
        if (scheduleManager == null){
            scheduleManager = new ScheduleManager();

            TimerClicker.getInstance().setListener(new TimerClicker.TimerClickerListener() {
                @Override
                public void HandleProgress(int day, int hour, int minutes) {
                    ScheduleManager.getInstance().change_containers(hour,minutes);
                }
            });

        }

        return scheduleManager;
    }

    public void setScheduleModelList(List<ScheduleModel.ScheduleItem> scheduleModelList){
        this.scheduleModelList = scheduleModelList;
    }

    public List<ScheduleModel.ScheduleItem> getScheduleModelList(){
        return scheduleModelList;
    }

    private void change_containers(int hour,int minute) {
        boolean non_in_other = false;
        for (ScheduleModel.ScheduleItem scheduleItem : scheduleModelList) {
            if (hour >= scheduleItem.start_hour && hour <= scheduleItem.end_hour) {

                if (minute >= scheduleItem.start_minute && hour == scheduleItem.start_hour ||
                        minute < scheduleItem.end_minute && hour == scheduleItem.end_hour ||
                        hour > scheduleItem.start_hour && hour < scheduleItem.end_hour) {
                    if (scheduleItem.id != current_schdule_id) {
                        // To Do Something

                        List<Integer> ids = new ArrayList<>();
                        for (ScheduleModel.ScheduleItem.Modelitem modelitem : scheduleItem.lives) {
                            ids.add(modelitem.live_id);
                        }

                        listener.Handle(ids);
                        current_schdule_id = scheduleItem.id;
                        non_in_other = true;
                    }
                }
            }
        }
    }

    public void stop(){
        TimerClicker.getInstance().stop();
    }

    public void start(){
        System.out.println("ScheduleManager.start");
        TimerClicker.getInstance().start();
    }

    public void setListener(Schedule_Listener listener){
        this.listener = listener;
    }

    public interface Schedule_Listener{
        public void Handle(List<Integer> ids);
    }

}
