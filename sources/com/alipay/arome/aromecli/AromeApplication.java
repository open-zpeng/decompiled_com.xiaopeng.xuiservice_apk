package com.alipay.arome.aromecli;

import android.app.Application;
import android.content.Context;
/* loaded from: classes4.dex */
public class AromeApplication extends Application {
    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        AromeInit.attachApplicationContext(context);
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        AromeInit.init(null);
    }
}
