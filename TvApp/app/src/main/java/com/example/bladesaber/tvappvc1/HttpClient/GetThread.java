package com.example.bladesaber.tvappvc1.HttpClient;

import android.util.Log;

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


    public GetThread(String myUrl){
        Url = myUrl;
    }

    @Override
    public void run() {
        super.run();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Handle_Faile();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream = response.body().byteStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                Result= "";
                String line = "";
                if (bufferedReader != null){
                    try {
                        while ((line = bufferedReader.readLine()) != null){
                            Result += line;
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                Log.d("GetThread","Result is " + Result);
                Handle_Response(Result);
            }
        });
    }

    public void Handle_Faile(){
    }

    public void Handle_Response(String Result){

    }

    public String getResult(){
        return Result;
    }

}
