package com.wanzhuan.livegame.AppManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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

    public static String getMacAddress(Context context) {
        //SN号
        String SerialNumber = android.os.Build.SERIAL;
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }

            if (networkInterface == null) {
                macAddress = "020000000002";
            }else {
                try {
                    byte[] addr = networkInterface.getHardwareAddress();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    macAddress = buf.toString();
                } catch (Exception e) {
                    macAddress = "020000000002";
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            macAddress = "020000000002";
        }

        String android_id = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);

        if(!TextUtils.isEmpty(SerialNumber) && !TextUtils.equals("unknown",SerialNumber)){
            Log.d("SystemManager","SN号为 ： "+SerialNumber);
            return SerialNumber;
        } else if (!macAddress.equals("020000000002")) {
            Log.d("SystemManager","mac地址为 ： "+macAddress);
            return macAddress;
        }else {
            Log.d("SystemManager","Android id为 ： "+android_id);
            return android_id;
        }
    }

    public static String AppManager_GetMac(Context context){
        String macAddress = getMacAddress(context);
        if(macAddress.contains(":")){
            String[] strings = macAddress.split(":");
            StringBuilder buf = new StringBuilder();
            for (int i = 0;i < strings.length;i++){
                buf.append(strings[i]);
            }
            String s = buf.toString();
            return s;
        }else {
            return macAddress;
        }
    }

}
