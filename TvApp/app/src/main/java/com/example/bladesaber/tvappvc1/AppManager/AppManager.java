package com.example.bladesaber.tvappvc1.AppManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class AppManager {

    public static String AppManager_GetIMEI(Context context){
        String ret = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            ret = telephonyManager.getDeviceId();
            Log.d("AppManager","GetIMEI:"+ret);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    public static String AppManager_GetVersionName(Context context){
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            Log.d("AppManager","GetVersionName:"+manager.versionName);
            return manager.versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return "unKnow";
        }
    }

    public static int AppManager_GetVersionCode(Context context){
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            Log.d("AppManager","GetVersionCode:"+String.valueOf(manager.versionCode));
            return manager.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return -1;
        }
    }

    public static String AppManager_GetMac(Context context){

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "020000000002";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "020000000002";
        }
        return macAddress.replace(":","");

    }

}
