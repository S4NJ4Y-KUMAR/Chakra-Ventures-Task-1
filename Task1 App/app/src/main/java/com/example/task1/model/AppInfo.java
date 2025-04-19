package com.example.task1.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
    public String appName;
    public Drawable icon;

    public AppInfo(String name, String pkgName, Drawable icon) {
        this.appName = name;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + '\'' +
                '}';
    }
}