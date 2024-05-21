package com.xiaopeng.xuiservice.download.download;

import android.content.Context;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import com.xiaopeng.xuimanager.download.IDownloadListenerInterface;
import com.xiaopeng.xuimanager.download.IDownloadServiceInterface;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.WatchDog;
import java.io.PrintWriter;
import java.util.Map;
/* loaded from: classes5.dex */
public class DownloadListenerBinder extends IDownloadServiceInterface.Stub implements XUIServiceBase {
    private static final String TAG = DownloadListenerBinder.class.getSimpleName();
    private final Context mContext;

    public DownloadListenerBinder(Context context) {
        this.mContext = context;
        DownloadListenerHelper.init(context);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("DownloadService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("DownloadService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        DownloadListenerHelper.destroy(this.mContext);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
    }

    public long enqueue(String uri) throws RemoteException {
        return DownloadListenerHelper.enqueue(uri);
    }

    public long enqueueWtihTitleDescription(String url, String title, String description, Map requestHeaders) {
        return DownloadListenerHelper.enqueueWithHeaders(url, title, description, requestHeaders);
    }

    public long enqueueWithHeader(String url, String title, String description, Map requestHeaders) {
        return DownloadListenerHelper.enqueueWithHeaders(url, title, description, requestHeaders);
    }

    public int[] getStatusListByPackageName(String packageName, String stateKey) {
        return DownloadListenerHelper.getDownloadStateIntByTitle(packageName, stateKey);
    }

    public String getState(long id, String stateKey) throws RemoteException {
        if (stateKey.equals("status")) {
            int downloadState = DownloadListenerHelper.getDownloadState(id);
            return String.valueOf(downloadState);
        }
        return DownloadListenerHelper.getDownloadStateStringByFields(id, stateKey)[0];
    }

    public void registerListener(IDownloadListenerInterface listener) throws RemoteException {
        String str = TAG;
        LogUtil.d(str, "registerListener instances is " + listener);
        if (DownloadListenerHelper.registerDownloadCallback(listener)) {
            listener.onRegisterSuccess(listener);
        } else {
            listener.onRegisterError(listener, "RemoteCallbackList is killed or RemoteException occurred");
        }
    }

    public void unRegisterListener(IDownloadListenerInterface listener) throws RemoteException {
        String str = TAG;
        LogUtil.d(str, "unRegisterListener instances is " + listener);
        if (DownloadListenerHelper.unRegisterDownloadCallback(listener)) {
            listener.onUnRegisterSuccess(listener);
        } else {
            listener.onUnRegisterError(listener, "RemoteCallbackList is killed or RemoteException occurred");
        }
    }
}
