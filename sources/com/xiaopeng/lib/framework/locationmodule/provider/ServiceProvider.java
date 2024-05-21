package com.xiaopeng.lib.framework.locationmodule.provider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import com.xiaopeng.lib.framework.locationmodule.LocationImpl;
import com.xiaopeng.lib.framework.locationmodule.provider.LocationService;
/* loaded from: classes.dex */
public final class ServiceProvider {
    public static final String SERVICE_NAME = LocationService.class.getName();
    private static final String TAG = "ServiceProvider";
    private boolean mBoundService;
    private LocationService.LocationServiceImpl mLocationService;
    private InternalServiceConnection mServiceConnection;

    /* loaded from: classes.dex */
    private static final class Holder {
        private static final ServiceProvider INSTANCE = new ServiceProvider();

        private Holder() {
        }
    }

    private ServiceProvider() {
        this.mServiceConnection = new InternalServiceConnection();
    }

    public static ServiceProvider getInstance() {
        return Holder.INSTANCE;
    }

    public boolean started() {
        return this.mBoundService;
    }

    public void startService(@NonNull Context context) {
        Intent serviceStartIntent = new Intent();
        serviceStartIntent.setComponent(new ComponentName(context.getPackageName(), SERVICE_NAME));
        Object service = context.startServiceAsUser(serviceStartIntent, UserHandle.CURRENT_OR_SELF);
        if (service == null) {
            throw new RuntimeException("Failed to launch " + SERVICE_NAME);
        }
        context.bindService(serviceStartIntent, this.mServiceConnection, 1);
    }

    public void publishLocation(LocationImpl location) {
        location.seal();
        this.mLocationService.publishLocation(location);
    }

    /* loaded from: classes.dex */
    private final class InternalServiceConnection implements ServiceConnection {
        private InternalServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ServiceProvider.this.mLocationService = (LocationService.LocationServiceImpl) binder;
            ServiceProvider.this.mBoundService = true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            ServiceProvider.this.mLocationService = null;
            ServiceProvider.this.mBoundService = false;
        }
    }
}
