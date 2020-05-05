package com.wanzhuan.poster.TestManager;

import android.content.Context;
import android.os.Message;

import com.wanzhuan.poster.AppManager.AppManager;
import com.wanzhuan.poster.Global.GlobalSignManager;
import com.wanzhuan.poster.HttpClient.GetThread;
import com.wanzhuan.poster.HttpClient.PostThread;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bladesaber on 2018/6/29.
 */

public class TestClass {

    private static TestClass testClass = null;

    public static TestClass getInstance(){
        if (testClass==null){
            testClass = new TestClass();
        }
        return testClass;
    }

    public void TestGet(Context context){
        GetThread getThread = new GetThread(context,"https://www.baidu.com"){
            @Override
            public void Handle_Response(String Result) {
                super.Handle_Response(Result);
                System.out.println("network test is: "+Result);
            }
        };
        getThread.start();
    }

    public void TestPost(Context context){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wtf", "wtf");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(context,"http://192.168.31.123/OwnTest/public/api/wtf",jsonObject){
            public void Handle_Response(String Result){
                System.out.println("network test is: "+Result);
            }
        };
        postThread.start();
    }

    private void test_non_params(int... param){
        int[] var = new int[param.length+1];
    }

}
