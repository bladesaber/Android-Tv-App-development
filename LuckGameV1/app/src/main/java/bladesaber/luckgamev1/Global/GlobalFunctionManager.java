package bladesaber.luckgamev1.Global;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bladesaber.luckgamev1.HttpClient.PostThread;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by bladesaber on 2018/4/17.
 */

public class GlobalFunctionManager {

    public static String isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        //System.out.println("BackGround_Sign is: "+ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND);
        //System.out.println("AppProcess.size is: "+appProcesses.size());

        //  IMPORTANCE_BACKGROUND = 400
        //  IMPORTANCE_EMPTY = 500
        //  IMPORTANCE_FOREGROUND = 100
        //  IMPORTANCE_SERVICE = 300
        //  IMPORTANCE_VISIBLE = 200

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

            //System.out.println("MainActivity.appProcesses.processName is: "+appProcess.processName);
            System.out.println("MainActivity.appProcesses.importance is: "+appProcess.importance);

            if (TextUtils.equals(appProcess.processName, context.getPackageName())) {
                System.out.println("GlobalFunction.AppStatus is: "+appProcess.importance);
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                    return GlobalSignManager.Forground;
                }else {
                    return GlobalSignManager.Background;
                }
            }
        }
        return "unKnow";
    }

    public static void Post_AppStatus(Context context,String Mac,String Status) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("app_code", GlobalSignManager.App_Code);
            jsonObject.put("device_code", Mac);
            jsonObject.put("status", Status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(context,GlobalSignManager.App_Status_Url,jsonObject);
        postThread.start();
    }

}
