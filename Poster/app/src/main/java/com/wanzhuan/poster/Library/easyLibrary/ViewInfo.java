package com.wanzhuan.poster.Library.easyLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/6/28.
 */

public class ViewInfo {

    public ViewInfo(){
        positionInfo = new PositionInfo();
        animatorInfos = new ArrayList<>();
    }

    PositionInfo positionInfo;
    List<AnimatorInfo> animatorInfos;

    public class PositionInfo {
        public int margin_left;
        public int margin_right;
        public int margin_top;
        public int margin_bottom;
        public int width;
        public int height;
    }

    public class AnimatorInfo{
        int delaytime;
        int duration;
        int animator_id;
    }

}
