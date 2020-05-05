package bladesaber.luckgame2v1.HttpClient;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

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
 * Created by bladesaber on 2018/4/12.
 */

public class PostThread extends Thread {

    private String Url;
    private JSONObject jsonObject;
    private Context context;

    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public PostThread(Context myContent,String myUrl, JSONObject myJsonObject){
        this.context = myContent;
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
                .header("Accept","application/json")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Handle_Faile(999);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //System.out.println("PostThread.Response.code is : "+response.code());
                if (response.code() == 200) {
                    InputStream inputStream = response.body().byteStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String result = "";
                    String line = "";
                    if (bufferedReader != null) {
                        try {
                            while ((line = bufferedReader.readLine()) != null) {
                                result += line;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Handle_Response(result);
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
            Toast.makeText(context,"错误码: "+code+" 服务器请求异常",Toast.LENGTH_LONG).show();
        }
        Looper.loop();
    }

    public void Handle_Response(String JsonResult){

    }
}
