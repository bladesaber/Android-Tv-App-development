package com.example.bladesaber.tvappvc1.HttpClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class PostThread extends Thread {

    private String Url;
    private JSONObject jsonObject;

    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public PostThread(String myUrl, JSONObject myJsonObject){
        Url = myUrl;
        jsonObject = myJsonObject;
    }

    @Override
    public void run() {
        super.run();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
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
                String result = "";
                String line = "";
                if (bufferedReader != null){
                    try {
                        while ((line = bufferedReader.readLine()) != null){
                            result += line;
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                Handle_Response(result);
            }
        });
    }

    public void Handle_Faile(){
    }

    public void Handle_Response(String JsonResult){

    }
}
