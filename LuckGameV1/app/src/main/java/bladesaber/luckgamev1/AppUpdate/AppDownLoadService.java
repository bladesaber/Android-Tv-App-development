package bladesaber.luckgamev1.AppUpdate;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by bladesaber on 2018/4/9.
 */

public class AppDownLoadService extends Service {

    public static final String Style3="UpdateJson";

    private DownloadManager mDownloadManager;
    private DownloadFinishReceiver mReceiver;

    private String ApkUrl;
    private String ApkName;
    private static String ApkPath = "";

    public AppDownLoadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mReceiver = new DownloadFinishReceiver();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ApkUrl = intent.getStringExtra("apkUrl");
        ApkName = intent.getStringExtra("apkName");

        StartDownLoad();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private long StartDownLoad(){
        clearApk(AppDownLoadService.this,ApkName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ApkUrl));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),ApkName);
        ApkPath = file.getAbsolutePath();

        request.setDestinationUri(Uri.fromFile(file));

        long downloadID = mDownloadManager.enqueue(request);
        return downloadID;
    }

    private File clearApk(Context context, String Apkname){
        File apkFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),Apkname);
        if (apkFile.exists()){
            apkFile.delete();
        }
        return apkFile;
    }

    private class DownloadFinishReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "DownLoad Complete", Toast.LENGTH_LONG).show();

            long DownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (!ApkPath.isEmpty()) {
                //Log.d("DownloadFinishReceiver", "Download Complete");
                installNormal(context, ApkPath);
            } else {
                Log.e("DownloadFinishReceiver", "apkPath is null");
            }
        }

        public void installNormal(Context context, String apkPath) {

        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {

            File file = new File(apkPath);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.example.chenfengyao.installapkdemo", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
        */

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            context.startActivity(intent);

        }
    }
}
