package com.wanzhuan.poster.PushManager;

/**
 * Created by bladesaber on 2018/7/16.
 */

public interface PushListener {

    public void NextStep();

    public void ResolvePushMessage(String msg);

}
