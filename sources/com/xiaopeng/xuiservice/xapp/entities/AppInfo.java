package com.xiaopeng.xuiservice.xapp.entities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.os.UserHandle;
/* loaded from: classes5.dex */
public class AppInfo {
    public ComponentName componentName;
    public Intent intent;
    public CharSequence title;
    public UserHandle user;

    public AppInfo() {
    }

    public AppInfo(LauncherActivityInfo info) {
        this.componentName = info.getComponentName();
        this.user = info.getUser();
        this.title = info.getLabel();
        this.intent = makeLaunchIntent(info);
    }

    public void apply(LauncherActivityInfo info) {
        this.componentName = info.getComponentName();
        this.user = info.getUser();
        this.title = info.getLabel();
        this.intent = makeLaunchIntent(info);
    }

    public static Intent makeLaunchIntent(LauncherActivityInfo info) {
        return makeLaunchIntent(info.getComponentName());
    }

    public static Intent makeLaunchIntent(ComponentName cn) {
        return new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER").setComponent(cn).setFlags(270532608);
    }

    public String toString() {
        return "AppInfo{component=" + this.componentName + '}';
    }
}
