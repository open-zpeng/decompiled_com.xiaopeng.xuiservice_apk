package com.xiaopeng.xuiservice.base.event;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class EventCenter {
    private static final String TAG = "EventCenter";
    private static volatile EventCenter mInstance;
    private List<MediaEventListener> mMediaEventListeners = new ArrayList();

    private EventCenter() {
    }

    public static EventCenter getInstance() {
        if (mInstance == null) {
            synchronized (EventCenter.class) {
                if (mInstance == null) {
                    mInstance = new EventCenter();
                }
            }
        }
        return mInstance;
    }

    public void registerMediaEventListener(MediaEventListener listener) {
        if (!this.mMediaEventListeners.contains(listener)) {
            this.mMediaEventListeners.add(listener);
        }
    }

    public void notifyNewMediaStream() {
        for (MediaEventListener listener : this.mMediaEventListeners) {
            listener.onPlayNewMediaStream();
        }
    }
}
