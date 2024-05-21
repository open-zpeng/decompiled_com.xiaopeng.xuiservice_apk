package com.xiaopeng.lib.framework.locationmodule.provider;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.xiaopeng.lib.framework.locationmodule.ILocationServiceCallback;
import com.xiaopeng.lib.framework.locationmodule.LocationImpl;
import com.xiaopeng.lib.framework.locationmodule.common.Debug;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
/* loaded from: classes.dex */
public final class CallbackList {
    private static final int CATEGORY_COUNT = ILocation.Category.values().length;
    private Object[] mCallbacks = new Object[CATEGORY_COUNT];

    /* JADX INFO: Access modifiers changed from: package-private */
    public CallbackList() {
        for (int i = 0; i < CATEGORY_COUNT; i++) {
            this.mCallbacks[i] = new RemoteCallbackList();
        }
    }

    public void register(ILocationServiceCallback callback, int category) {
        Debug.assertion(category < CATEGORY_COUNT);
        ((RemoteCallbackList) this.mCallbacks[category]).register(callback);
    }

    public void unregister(ILocationServiceCallback callback) {
        unregister(callback, ILocation.Category.GPS_LOCATION.ordinal());
        unregister(callback, ILocation.Category.DR_LOCATION.ordinal());
    }

    public void publishLocation(LocationImpl location) {
        RemoteCallbackList<ILocationServiceCallback> callbacks = (RemoteCallbackList) this.mCallbacks[location.category().ordinal()];
        int count = callbacks.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                callbacks.getBroadcastItem(i).locationChanged(location);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbacks.finishBroadcast();
    }

    private void unregister(ILocationServiceCallback callback, int category) {
        ((RemoteCallbackList) this.mCallbacks[category]).unregister(callback);
    }
}
