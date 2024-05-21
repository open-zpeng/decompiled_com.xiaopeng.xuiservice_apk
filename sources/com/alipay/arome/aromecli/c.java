package com.alipay.arome.aromecli;

import android.content.Context;
/* compiled from: Utils.java */
/* loaded from: classes4.dex */
final class c {
    private static Boolean a = false;

    public static boolean a(Context context) {
        Boolean bool = a;
        if (bool == null) {
            try {
                Boolean valueOf = Boolean.valueOf((context.getApplicationInfo().flags & 2) != 0);
                a = valueOf;
                return valueOf.booleanValue();
            } catch (Throwable th) {
                return false;
            }
        }
        return bool.booleanValue();
    }
}
