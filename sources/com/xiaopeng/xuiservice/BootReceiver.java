package com.xiaopeng.xuiservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
/* loaded from: classes5.dex */
public class BootReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        LogUtil.w(XUILog.TAG_SERVICE, "Starting...");
        BroadcastManager.getInstance().handleBroadcast(context, intent);
        Intent XUIServiceintent = new Intent();
        XUIServiceintent.setPackage(context.getPackageName());
        XUIServiceintent.setAction("com.xiaopeng.xuimanager.IXUIService");
        context.startServiceAsUser(XUIServiceintent, UserHandle.SYSTEM);
    }
}
