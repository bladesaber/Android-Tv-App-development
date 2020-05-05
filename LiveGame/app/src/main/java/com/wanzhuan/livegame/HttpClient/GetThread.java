package com.wanzhuan.livegame.HttpClient;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class GetThread extends Thread{

    private String Url;
    private String Result;
    private Context context;


    public GetThread(Context myContext,String myUrl){
        this.context = myContext;
        Url = myUrl;
    }

    @Override
    public void run() {
        super.run();

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Url)
                .header("Accept","application/json")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Handle_Faile(999);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.code() == 200) {
                    InputStream inputStream = response.body().byteStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    Result = "";
                    String line = "";
                    if (bufferedReader != null) {
                        try {
                            while ((line = bufferedReader.readLine()) != null) {
                                Result += line;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("GetThread", "Result is " + Result);
                    Handle_Response(Result);
                }else {
                    Handle_Faile(response.code());
                }
            }
        });
    }

    public void Handle_Faile(int code){
        Looper.prepare();
        if (code==999){
            Toast.makeText(context,"网络连接不稳定",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,"错误码: "+code+" "+ Url + " 服务器请求异常",Toast.LENGTH_LONG).show();
        }
        Looper.loop();
    }

    public void Handle_Response(String Result){

    }



    public String getResult(){
        return Result;
    }

}
