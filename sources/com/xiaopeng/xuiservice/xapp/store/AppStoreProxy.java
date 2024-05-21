package com.xiaopeng.xuiservice.xapp.store;

import android.content.Intent;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
/* loaded from: classes5.dex */
public class AppStoreProxy {
    private static final String ACTION_SHOW_DENIED_PROMPT = "com.xiaopeng.action.SHOW_DENIED_PROMPT";
    private static final String TAG = "AppStoreProxy";

    public static void showAppDeniedPrompt(String pkgName, int deniedType) {
        Intent intent = new Intent();
        intent.setAction(ACTION_SHOW_DENIED_PROMPT);
        intent.putExtra("pkgName", pkgName);
        intent.putExtra("deniedType", deniedType);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }
}
