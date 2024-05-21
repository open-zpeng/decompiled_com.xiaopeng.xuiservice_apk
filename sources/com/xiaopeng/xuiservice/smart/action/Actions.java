package com.xiaopeng.xuiservice.smart.action;

import android.util.Log;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/* loaded from: classes5.dex */
public class Actions {
    public static final String ACTION_ALL = "all";
    public static final String ACTION_AMBIENT = "ambient";
    public static final String ACTION_ANY = "any";
    public static final String ACTION_AUDIO = "audio";
    public static final String ACTION_AVAS = "avas";
    public static final String ACTION_DIALOG = "dialog";
    public static final String ACTION_IMAGE = "image";
    public static final String ACTION_LLU = "llu";
    public static final String ACTION_REF = "ref";
    public static final String ACTION_RESOURCE = "resource";
    public static final String ACTION_SEQUENCE = "sequence";
    public static final String ACTION_TOAST = "toast";
    public static final String ACTION_TTS = "tts";
    public static final String ACTION_VIDEO = "video";
    private static final boolean LOG_ENABLE = true;
    public static final String TAG = "egg";
    private static ScheduledExecutorService executor;

    public static void INFO(String msg) {
        Log.i("egg", msg);
    }

    public static void ERROR(String msg, Throwable t) {
        Log.e("egg", msg, t);
    }

    public static void ERROR(String msg) {
        Log.e("egg", msg);
    }

    public static Action parallelAll(Action... actions) {
        return new All(actions);
    }

    public static Action parallelAll(List<Action> actions) {
        return new All(actions);
    }

    public static Action parallelAny(Action... actions) {
        return new Any(actions);
    }

    public static Action parallelAny(List<Action> actions) {
        return new Any(actions);
    }

    public static Action sequence(Action... actions) {
        return new Sequence(actions);
    }

    public static Action sequence(List<Action> actions) {
        return new Sequence(actions);
    }

    public static Action delayedStart(Action action, long delay, TimeUnit unit) {
        return new DelayedStart(action, delay, unit);
    }

    public static Action delayedStop(Action action, long delay, TimeUnit unit) {
        return new DelayedStop(action, delay, unit);
    }

    public static Action immediately(Action action) {
        return new DelayedStop(action, 0L, TimeUnit.NANOSECONDS);
    }

    public static ScheduledExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(0);
        }
        return executor;
    }
}
