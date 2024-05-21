package com.xiaopeng.xuiservice.smart.condition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import org.apache.commons.codec.binary.StringUtils;
/* loaded from: classes5.dex */
public class ConditionBroadcast extends ConditionBase {
    private final String action;
    private Bundle bundle;
    private final Context context;
    private boolean onReceived;
    private BroadcastReceiver receiver;

    public ConditionBroadcast(Context context, String action) {
        this.context = context;
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public boolean isMatch() {
        return this.onReceived;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void beforeWatch() {
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        this.receiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.condition.ConditionBroadcast.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (StringUtils.equals(ConditionBroadcast.this.action, intent.getAction())) {
                    Conditions.INFO("BroadcastReceiver onReceive " + ConditionBroadcast.this.action);
                    ConditionBroadcast.this.onReceived = true;
                    ConditionBroadcast.this.bundle = intent.getExtras();
                    ConditionBroadcast.this.notifyConditionChange();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(this.action);
        this.context.registerReceiver(this.receiver, intentFilter);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        this.context.unregisterReceiver(this.receiver);
        this.receiver = null;
    }
}
