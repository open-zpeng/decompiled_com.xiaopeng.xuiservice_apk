package com.xiaopeng.xuiservice.uvccamera.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
/* loaded from: classes5.dex */
public class HandlerThreadHandler extends Handler {
    private static final String TAG = HandlerThreadHandler.class.getSimpleName();

    public static HandlerThreadHandler createHandler() {
        return createHandler(TAG);
    }

    public static HandlerThreadHandler createHandler(String name) {
        HandlerThread thread = new HandlerThread(name);
        thread.start();
        return new HandlerThreadHandler(thread.getLooper());
    }

    public static HandlerThreadHandler createHandler(Handler.Callback callback) {
        return createHandler(TAG, callback);
    }

    public static HandlerThreadHandler createHandler(String name, Handler.Callback callback) {
        HandlerThread thread = new HandlerThread(name, -1);
        thread.start();
        return new HandlerThreadHandler(thread.getLooper(), callback);
    }

    private HandlerThreadHandler(Looper looper) {
        super(looper);
    }

    private HandlerThreadHandler(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }
}
