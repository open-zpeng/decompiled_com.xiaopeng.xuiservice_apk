package com.xiaopeng.xuiservice.xapp;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.ToastUtil;
/* loaded from: classes5.dex */
public class ShowToastReceiver extends BroadcastReceiver {
    private static final String TAG = "ShowToastReceiver";
    private IntentFilter mIntentFilter = new IntentFilter();

    public ShowToastReceiver() {
        this.mIntentFilter.addAction(Constants.ACTION_SHOW_TOAST);
    }

    public void register() {
        ActivityThread.currentActivityThread().getApplication().registerReceiver(this, this.mIntentFilter);
    }

    public void unRegister() {
        ActivityThread.currentActivityThread().getApplication().unregisterReceiver(this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (Constants.ACTION_SHOW_TOAST.equals(intent.getAction())) {
            String text = intent.getStringExtra("text");
            int duration = intent.getIntExtra("duration", 0);
            int shareId = intent.getIntExtra("sharedId", 0);
            String caller = intent.getStringExtra("caller");
            LogUtil.d(TAG, "package:" + intent.getPackage() + " require show toast:" + text + " &duration:" + duration + " &sharedId:" + shareId);
            if (!TextUtils.isEmpty(caller) && !TextUtils.isEmpty(text)) {
                ToastUtil.showToast(ActivityThread.currentActivityThread().getApplication(), text, duration, shareId);
            }
        }
    }
}
