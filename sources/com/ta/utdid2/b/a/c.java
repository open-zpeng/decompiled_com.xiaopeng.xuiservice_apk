package com.ta.utdid2.b.a;

import android.annotation.TargetApi;
import android.util.Base64;
/* compiled from: Base64Helper.java */
/* loaded from: classes4.dex */
public class c {
    @TargetApi(8)
    public static String encodeToString(byte[] input, int flags) {
        return Base64.encodeToString(input, flags);
    }
}
