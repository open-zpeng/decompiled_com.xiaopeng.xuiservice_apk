package com.android.volley.toolbox;

import android.os.Looper;
/* loaded from: classes4.dex */
public final class Threads {
    private Threads() {
    }

    public static void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Must be invoked from the main thread.");
        }
    }
}
