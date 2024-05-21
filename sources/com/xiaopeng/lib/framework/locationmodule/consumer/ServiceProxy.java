package com.xiaopeng.lib.framework.locationmodule.consumer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.xiaopeng.lib.framework.locationmodule.ILocationServiceCallback;
import com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface;
import com.xiaopeng.lib.framework.locationmodule.LocationImpl;
import com.xiaopeng.lib.framework.locationmodule.LocationServiceException;
import com.xiaopeng.lib.framework.locationmodule.common.Debug;
import com.xiaopeng.lib.framework.locationmodule.provider.ServiceProvider;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationServiceException;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.IStateEvent;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class ServiceProxy {
    private static final int DELAY_TO_BIND_SERVICE = 5000;
    private static final int DELAY_TO_POST_LAST_LOCATION = 3000;
    private static final String REMOTE_APP_PACKAGE = "com.xiaopeng.montecarlo";
    private static final String TAG = "LocationModule.Consumer.ServiceProxy";
    private final String mAssignedRemotePackageName;
    private Context mContext;
    private ILocation mCurrentDrLocation;
    private final Persistence mPersistence;
    private final Runnable mPostLastLocationTask;
    private ILocationServiceInterface mRemoteLocationService;
    private volatile boolean mBoundService = false;
    private InternalServiceConnection mServiceConnection = new InternalServiceConnection();
    private ILocationServiceCallback mCallback = new ILocationServiceCallback.Stub() { // from class: com.xiaopeng.lib.framework.locationmodule.consumer.ServiceProxy.1
        @Override // com.xiaopeng.lib.framework.locationmodule.ILocationServiceCallback
        public void locationChanged(LocationImpl location) {
            EventBus.getDefault().post(new LocationChangedEvent().location(location));
            ServiceProxy.this.updateCurrentLocation(location);
            ServiceProxy.this.mPersistence.write(location);
        }
    };

    public ServiceProxy(@NonNull Context context, String remoteAppPackage) {
        this.mContext = context;
        this.mAssignedRemotePackageName = remoteAppPackage == null ? "com.xiaopeng.montecarlo" : remoteAppPackage;
        this.mPersistence = new Persistence(context);
        updateCurrentLocation(this.mPersistence.read());
        this.mPostLastLocationTask = new Runnable() { // from class: com.xiaopeng.lib.framework.locationmodule.consumer.ServiceProxy.2
            @Override // java.lang.Runnable
            public void run() {
                if (ServiceProxy.this.mCurrentDrLocation != null) {
                    EventBus.getDefault().post(new LocationChangedEvent().location(ServiceProxy.this.mCurrentDrLocation));
                }
            }
        };
        ThreadUtils.postDelayed(0, this.mPostLastLocationTask, 3000L);
        startBindingTask();
    }

    public void bindToLocationService() {
        startBindingTask();
    }

    public boolean connected() {
        return this.mBoundService;
    }

    public void subscribe(ILocation.Category category) throws ILocationServiceException {
        if (!this.mBoundService) {
            throw new LocationServiceException(4);
        }
        try {
            this.mRemoteLocationService.subscribe(this.mCallback, category.ordinal());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void unsubscribe() throws ILocationServiceException {
        if (!this.mBoundService) {
            throw new LocationServiceException(4);
        }
        try {
            this.mRemoteLocationService.unsubscribe(this.mCallback);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    public ILocation getLastDrLocation() {
        return this.mCurrentDrLocation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindToLocationServiceInternal() {
        Debug.assertion(!this.mBoundService);
        if (this.mBoundService) {
            return;
        }
        LogUtils.d(TAG, "Try to bind location service.");
        Intent intent = new Intent(ServiceProvider.SERVICE_NAME);
        intent.setPackage(this.mContext.getPackageName());
        intent.setClassName(this.mAssignedRemotePackageName, ServiceProvider.SERVICE_NAME);
        boolean result = this.mContext.bindServiceAsUser(intent, this.mServiceConnection, 1073741824, UserHandle.CURRENT_OR_SELF);
        int timeDelay = result ? 20000 : 5000;
        checkAndStartBindingWithDelay(timeDelay);
    }

    private void startBindingTask() {
        if (this.mBoundService) {
            return;
        }
        bindToLocationServiceInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentLocation(ILocation location) {
        if (location != null && ILocation.Category.DR_LOCATION == location.category()) {
            this.mCurrentDrLocation = location;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndStartBindingWithDelay(int delay) {
        if (this.mBoundService) {
            return;
        }
        LogUtils.d(TAG, "Try to bind location service...");
        ThreadUtils.postDelayed(0, new Runnable() { // from class: com.xiaopeng.lib.framework.locationmodule.consumer.ServiceProxy.3
            @Override // java.lang.Runnable
            public void run() {
                ServiceProxy.this.bindToLocationServiceInternal();
            }
        }, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearPostingTask() {
        ThreadUtils.removeRunnable(this.mPostLastLocationTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class InternalServiceConnection implements ServiceConnection {
        private InternalServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ServiceProxy.this.mRemoteLocationService = ILocationServiceInterface.Stub.asInterface(binder);
            ServiceProxy.this.mBoundService = true;
            EventBus.getDefault().post(new ServiceStateEvent().type(IStateEvent.TYPE.BOUND));
            ServiceProxy.this.clearPostingTask();
            LogUtils.d(ServiceProxy.TAG, "Location service was connected!");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            ServiceProxy.this.mRemoteLocationService = null;
            ServiceProxy.this.mBoundService = false;
            EventBus.getDefault().post(new ServiceStateEvent().type(IStateEvent.TYPE.UNBOUND));
            ServiceProxy.this.checkAndStartBindingWithDelay(5000);
            LogUtils.d(ServiceProxy.TAG, "Location service was disconnected!");
        }
    }
}
