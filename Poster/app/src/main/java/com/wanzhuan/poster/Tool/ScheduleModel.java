package com.wanzhuan.poster.Tool;

import java.util.List;

/**
 * Created by bladesaber on 2018/7/23.
 */

public class ScheduleModel {

    public List<ScheduleItem> scheduleItemList;

    public class ScheduleItem{
        public int start_hour;
        public int start_minute;
        public int end_hour;
        public int end_minute;
        public List<Modelitem> lives;
        public int id;

        public class Modelitem{
            public int live_id;
        }

    }

}
