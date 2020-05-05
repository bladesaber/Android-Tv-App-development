package com.example.bladesaber.tvappvc1.AppUpdate;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class AppModel {

    private String ApkName;
    private int VersionCode;
    private String VersionName;
    private String updateUrl;

    public void setVersionCode(int versionCode){
        VersionCode=versionCode;
    }

    public void setVersionName(String versionName){
        VersionName=VersionName;
    }

    public int getVersionCode(){
        return VersionCode;
    }

    public String getVersionName(){
        return VersionName;
    }

    public String getUpdateUrl(){
        return updateUrl;
    }

    public void setUpdateUrl(String url){
        updateUrl=url;
    }

    public void setApkName(String apkName){
        ApkName = apkName;
    }

    public String getApkName(){
        return ApkName;
    }
}