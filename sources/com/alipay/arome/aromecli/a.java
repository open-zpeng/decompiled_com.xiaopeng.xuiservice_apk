package com.alipay.arome.aromecli;

import android.util.Log;
/* compiled from: AromeCliLogger.java */
/* loaded from: classes4.dex */
final class a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(String msg) {
        return Log.i("AromeClient", msg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(String msg) {
        return Log.d("AromeClient", msg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(String msg) {
        return Log.e("AromeClient", msg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(String msg, Throwable tr) {
        return Log.e("AromeClient", msg, tr);
    }
}
