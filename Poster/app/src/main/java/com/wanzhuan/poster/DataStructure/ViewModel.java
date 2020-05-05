package com.wanzhuan.poster.DataStructure;

import java.util.List;

/**
 * Created by bladesaber on 2018/6/29.
 */

public class ViewModel {

    public int id;

    public String type;
    public String image_url;
    public String text;

    public TextSetting text_setting;

    public int x;
    public int y;
    public int w;
    public int h;

    public List<AnimationSetting> animations;

    public LottieSetting lottieSetting;

    public class LottieSetting{
        public String json_url;
        public int delay_time;
        public boolean repetition;
    }

    public class AnimationSetting{
        public int type;
        public int delay_time;
        public int duration;
        public boolean repetition;

        public List<Float> translateX;
        public List<Float> translateY;

        public List<Float> rotationX;
        public List<Float> rotationY;
        public List<Float> rotation;

        public List<Float> scaleX;
        public List<Float> scaleY;

        public List<Float> alpha;

        public int Interpolator_id;
    }

    public class TextSetting{
        public float font_size;
        public int length;
        public String color;
        public String type;
        public String font_style;
        public Float line_space;
        public Float letter_space;
    }

}
