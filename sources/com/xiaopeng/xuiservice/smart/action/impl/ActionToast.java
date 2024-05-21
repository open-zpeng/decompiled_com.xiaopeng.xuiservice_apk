package com.xiaopeng.xuiservice.smart.action.impl;

import android.app.ActivityThread;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.utils.ToastUtil;
/* loaded from: classes5.dex */
public class ActionToast extends ActionBase {
    private final String text;

    public ActionToast(String text) {
        this.text = text;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        ToastUtil.showToast(ActivityThread.currentActivityThread().getApplication(), this.text);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{msg='" + this.text + "'}";
    }
}
