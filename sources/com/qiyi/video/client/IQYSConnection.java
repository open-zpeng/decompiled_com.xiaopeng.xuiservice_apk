package com.qiyi.video.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import com.qiyi.video.qsrbase.QYSLog;
import com.qiyi.video.qys.IQYS;
import com.qiyi.video.qys.IQYSResponse;
import com.qiyi.video.qys.IQYSString;
import java.util.List;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public class IQYSConnection {
    private static final int CONNECT_TIMEOUT_CHECK_DELAY_MILLIS = 3000;
    private static final int RETRY_CONNECT_DELAY_MILLIS = 3000;
    private Context mContext;
    private QYSConnectionListener mListener;
    private QYSConnectionStatusListener mQYSConnectionStatusListener;
    private IQYS mService;
    private ServiceConnectionInner mServiceConnectionInner;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public interface QYSConnectionListener {
        void onResult(IQYSResponse iQYSResponse);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public interface QYSConnectionStatusListener {
        void onConnectedStatusChanged(IQYSConnection iQYSConnection, boolean z);
    }

    public void setListener(QYSConnectionListener listener) {
        this.mListener = listener;
    }

    public void setStatusListener(QYSConnectionStatusListener QYSConnectionStatusListener2) {
        this.mQYSConnectionStatusListener = QYSConnectionStatusListener2;
    }

    public void connect(Context context) {
        this.mContext = context.getApplicationContext();
        connectAndTryLaunchProcess(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectAndTryLaunchProcess(boolean needRetryOnTimeOut) {
        try {
            Context context = this.mContext;
            if (context != null) {
                connectService(context, needRetryOnTimeOut);
            } else {
                QYSConnectionListener listener = this.mListener;
                if (listener != null) {
                    IQYSResponse response = new IQYSResponse();
                    response.mCode = IQYSConstants.RESPONSE_CODE_PARAMS_ERROR;
                    response.mMsg = new IQYSString("connect failure, context is null");
                    listener.onResult(response);
                }
            }
        } catch (Exception e) {
            QYSConnectionListener listener2 = this.mListener;
            if (listener2 != null) {
                IQYSResponse response2 = new IQYSResponse();
                response2.mCode = -6;
                response2.mMsg = new IQYSString("connect failure, msg=" + e.getMessage());
                listener2.onResult(response2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startProcess(Context context) {
        Intent intent = new Intent();
        Uri parse = Uri.parse("iqys://com.qiyi.video.iv/launch");
        intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        intent.setAction("android.intent.action.VIEW");
        intent.setData(parse);
        context.startActivity(intent);
    }

    private void connectService(Context context, boolean needRetryOnTimeOut) {
        try {
            Intent intent = new Intent("com.qiyi.video.iqys.connection");
            convertImplicitIntentToExplicitIntent(intent, context);
            this.mServiceConnectionInner = new ServiceConnectionInner(needRetryOnTimeOut);
            this.mServiceConnectionInner.startConnectedTimeOutCheck();
            context.bindService(intent, this.mServiceConnectionInner, 1);
        } catch (Exception e) {
            ServiceConnectionInner inner = this.mServiceConnectionInner;
            if (inner != null) {
                inner.stopConnectedTimeOutCheck();
            }
            throw e;
        }
    }

    private void convertImplicitIntentToExplicitIntent(Intent intent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(intent, 0);
        if (resolveInfoList == null || resolveInfoList.isEmpty()) {
            throw new IllegalStateException("No host service found");
        }
        if (resolveInfoList.size() > 1) {
            throw new IllegalStateException("More than one host service found");
        }
        ResolveInfo resolveInfo = resolveInfoList.get(0);
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        ComponentName component = new ComponentName(serviceInfo.packageName, serviceInfo.name);
        intent.setComponent(component);
    }

    public void disConnect() {
        ServiceConnectionInner serviceConnectionInner = this.mServiceConnectionInner;
        disConnect(serviceConnectionInner);
        clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disConnect(ServiceConnectionInner serviceConnectionInner) {
        if (serviceConnectionInner != null) {
            try {
                Context context = this.mContext;
                if (context != null) {
                    context.unbindService(serviceConnectionInner);
                }
            } catch (Exception e) {
            }
        }
    }

    private void clear() {
        this.mListener = null;
        this.mQYSConnectionStatusListener = null;
        this.mServiceConnectionInner = null;
        this.mService = null;
    }

    public boolean isValid() {
        ServiceConnectionInner serviceConnectionInner = this.mServiceConnectionInner;
        return serviceConnectionInner != null && serviceConnectionInner.isValid();
    }

    public IQYS getService() {
        return this.mService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ServiceConnectionInner implements ServiceConnection {
        volatile boolean mConnected;
        private final boolean mNeedRetryOnTimeOut;
        final Handler mHandler = new Handler(Looper.getMainLooper());
        final String TAG = "IQYSConnection@" + Integer.toHexString(hashCode());
        final Runnable mRetryConnect = new Runnable() { // from class: com.qiyi.video.client.IQYSConnection.ServiceConnectionInner.1
            @Override // java.lang.Runnable
            public void run() {
                String str = ServiceConnectionInner.this.TAG;
                Log.i(str, "onServiceConnected retry: isDirty=" + ServiceConnectionInner.this.isDirty());
                if (!ServiceConnectionInner.this.isDirty()) {
                    IQYSConnection.this.connectAndTryLaunchProcess(false);
                }
            }
        };
        final Runnable mConnectTimeOutCheck = new Runnable() { // from class: com.qiyi.video.client.IQYSConnection.ServiceConnectionInner.2
            @Override // java.lang.Runnable
            public void run() {
                QYSConnectionListener listener;
                Log.i(ServiceConnectionInner.this.TAG, "onServiceConnected time out: isDirty=" + ServiceConnectionInner.this.isDirty());
                ServiceConnectionInner.this.stopConnectedTimeOutCheck();
                if (!ServiceConnectionInner.this.isDirty()) {
                    boolean needCallbackFailure = !ServiceConnectionInner.this.mNeedRetryOnTimeOut;
                    if (ServiceConnectionInner.this.mNeedRetryOnTimeOut) {
                        IQYSConnection.this.disConnect(ServiceConnectionInner.this);
                        try {
                            IQYSConnection.this.startProcess(IQYSConnection.this.mContext);
                            ServiceConnectionInner.this.mHandler.postDelayed(ServiceConnectionInner.this.mRetryConnect, 3000L);
                        } catch (Exception e) {
                            needCallbackFailure = true;
                        }
                    }
                    if (needCallbackFailure && (listener = IQYSConnection.this.mListener) != null) {
                        IQYSResponse response = new IQYSResponse();
                        response.mCode = -8;
                        response.mMsg = new IQYSString("connect failure, connect time out");
                        listener.onResult(response);
                    }
                }
            }
        };

        ServiceConnectionInner(boolean needRetryOnTimeOut) {
            this.mNeedRetryOnTimeOut = needRetryOnTimeOut;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            String str = this.TAG;
            QYSLog.i(str, "onServiceConnected: isDirty=" + isDirty());
            if (isDirty()) {
                IQYSConnection.this.mContext.unbindService(this);
            } else {
                IQYSConnection.this.mService = IQYS.Stub.asInterface(service);
                changeConnectedStatus(true);
                QYSConnectionListener listener = IQYSConnection.this.mListener;
                if (listener != null) {
                    IQYSResponse iqysResponse = new IQYSResponse();
                    iqysResponse.mCode = 200;
                    iqysResponse.mMsg = new IQYSString("SUCCESS");
                    listener.onResult(iqysResponse);
                }
            }
            stopConnectedTimeOutCheck();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            String str = this.TAG;
            QYSLog.i(str, "onServiceDisconnected: isDirty=" + isDirty());
            changeConnectedStatus(false);
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName name) {
            String str = this.TAG;
            QYSLog.i(str, "onBindingDied: isDirty=" + isDirty());
            changeConnectedStatus(false);
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName name) {
            String str = this.TAG;
            QYSLog.i(str, "onNullBinding: isDirty=" + isDirty());
            IQYSConnection.this.disConnect(this);
            changeConnectedStatus(false);
        }

        boolean isValid() {
            return !isDirty() && this.mConnected;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDirty() {
            return IQYSConnection.this.mServiceConnectionInner != this;
        }

        private void changeConnectedStatus(boolean isConnected) {
            boolean connected = this.mConnected;
            this.mConnected = isConnected;
            QYSConnectionStatusListener listener = IQYSConnection.this.mQYSConnectionStatusListener;
            if (connected != isConnected && listener != null && !isDirty()) {
                listener.onConnectedStatusChanged(IQYSConnection.this, isConnected);
            }
        }

        void startConnectedTimeOutCheck() {
            this.mHandler.postDelayed(this.mConnectTimeOutCheck, 3000L);
        }

        void stopConnectedTimeOutCheck() {
            this.mHandler.removeCallbacks(this.mConnectTimeOutCheck);
        }
    }
}
