package com.xiaopeng.lib.framework.locationmodule.provider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.xiaopeng.lib.framework.locationmodule.ILocationServiceCallback;
import com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface;
import com.xiaopeng.lib.framework.locationmodule.LocationImpl;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public final class LocationService extends Service {
    private static final String TAG = "LocationModule.Provider.LocationService";

    /* loaded from: classes.dex */
    private static final class ServiceImplHolder {
        private static final LocationServiceImpl INSTANCE = new LocationServiceImpl();

        private ServiceImplHolder() {
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return ServiceImplHolder.INSTANCE;
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    /* loaded from: classes.dex */
    public static class LocationServiceImpl extends ILocationServiceInterface.Stub {
        private final CallbackList mCallbacks = new CallbackList();

        @Override // com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface
        public void subscribe(ILocationServiceCallback callback, int category) {
            LogUtils.d(LocationService.TAG, "Subscribe category: " + category + " with callback:" + callback.hashCode());
            this.mCallbacks.register(callback, category);
        }

        @Override // com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface
        public void unsubscribe(ILocationServiceCallback callback) {
            LogUtils.d(LocationService.TAG, "Unsubscribe for callback: " + callback.hashCode());
            this.mCallbacks.unregister(callback);
        }

        public void publishLocation(LocationImpl location) {
            this.mCallbacks.publishLocation(location);
        }
    }
}
