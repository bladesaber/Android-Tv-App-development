package com.example.bladesaber.tvappvc1.SqlManager;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class ImageSetting {

    private ImageDataItem data;

    private class ImageDataItem {

        private int id;
        private int interval;
        private String animation;

        public int getInterval() {
            return interval;
        }

        public String getAnimation() {
            return animation;
        }
    }

    public int getInterval(){
        return data.getInterval();
    }

    public String getAnimation() {
        return data.getAnimation();
    }

}
