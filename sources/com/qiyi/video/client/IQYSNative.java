package com.qiyi.video.client;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.qiyi.video.client.IQYSConnection;
import com.qiyi.video.client.IQYSManager;
import com.qiyi.video.client.IQYSService;
import com.qiyi.video.qsrbase.QYSLog;
import com.qiyi.video.qys.IQYS;
import com.qiyi.video.qys.IQYSCallback;
import com.qiyi.video.qys.IQYSRequest;
import com.qiyi.video.qys.IQYSResponse;
import com.qiyi.video.qys.IQYSString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes4.dex */
class IQYSNative {
    private static final String TAG = "IQYSNative";
    private final Map<Object, List<IQYSManager.RegisterListener>> mListeners = new HashMap();
    private ConnectStatusChangedListenerWrapper mConnectStatusChangedListener = new ConnectStatusChangedListenerWrapper();
    private AtomicReference<IQYSConnection> mReference = null;
    private final IQYSService mDefault = new IQYSServiceImp(null);
    private volatile IQYSService mService = this.mDefault;

    public void addConnectStatusChangedListener(IQYSManager.ConnectStatusChangedListener connectStatusChangedListener) {
        this.mConnectStatusChangedListener.addListener(connectStatusChangedListener);
    }

    public void removeConnectStatusChangedListener(IQYSManager.ConnectStatusChangedListener connectStatusChangedListener) {
        this.mConnectStatusChangedListener.addListener(connectStatusChangedListener);
    }

    public IQYSService getService() {
        IQYSService service = this.mService;
        QYSLog.i(TAG, "getService: ref=" + this.mReference + ", service=" + service);
        return service;
    }

    public boolean isConnected() {
        IQYSConnection connection = ((IQYSServiceImp) this.mService).mConnection;
        return connection != null && connection.isValid();
    }

    public void register(Context context, IQYSManager.RegisterListener listener) {
        QYSLog.i(TAG, "register: ctx=" + context);
        IQYSManager.RegisterListener registerListener = listener == null ? new EmptyRegisterListener() : listener;
        AtomicReference<IQYSConnection> atomicReference = this.mReference;
        if (atomicReference == null) {
            final IQYSConnection connection = new IQYSConnection();
            final AtomicReference<IQYSConnection> reference = new AtomicReference<>();
            ArrayList<IQYSManager.RegisterListener> list = new ArrayList<>();
            list.add(registerListener);
            this.mListeners.put(reference, list);
            this.mReference = reference;
            connection.setListener(new IQYSConnection.QYSConnectionListener() { // from class: com.qiyi.video.client.IQYSNative.1
                @Override // com.qiyi.video.client.IQYSConnection.QYSConnectionListener
                public void onResult(IQYSResponse response) {
                    QYSLog.i(IQYSNative.TAG, "register: onResult ref=" + IQYSNative.this.refString(reference) + ",res=" + response.mCode);
                    if (IQYSNative.this.mReference != reference) {
                        connection.disConnect();
                        IQYSNative.this.callbackFailure(reference, -2, "service disconnected already");
                    } else if (response.mCode == 200) {
                        IQYSNative.this.mService = new IQYSServiceImp(connection);
                        reference.set(connection);
                        IQYSNative.this.callbackSuccess(reference);
                    } else {
                        IQYSNative.this.unRegister();
                        IQYSNative.this.callbackFailure(reference, response.mCode, response.mMsg.get());
                    }
                }
            });
            connection.setStatusListener(new IQYSConnection.QYSConnectionStatusListener() { // from class: com.qiyi.video.client.IQYSNative.2
                @Override // com.qiyi.video.client.IQYSConnection.QYSConnectionStatusListener
                public void onConnectedStatusChanged(IQYSConnection connection2, boolean isConnected) {
                    boolean isCur = IQYSNative.this.mReference == reference;
                    StringBuilder sb = new StringBuilder();
                    sb.append("register: onConnectedStatusChanged: ref=");
                    sb.append(IQYSNative.this.refString(reference));
                    sb.append(",mRef=");
                    IQYSNative iQYSNative = IQYSNative.this;
                    sb.append(iQYSNative.refString(iQYSNative.mReference));
                    sb.append(",cur=");
                    sb.append(isCur);
                    sb.append(", isConnected=");
                    sb.append(isConnected);
                    QYSLog.i(IQYSNative.TAG, sb.toString());
                    if (isCur) {
                        if (!isConnected) {
                            IQYSNative.this.mReference = null;
                            IQYSNative iQYSNative2 = IQYSNative.this;
                            iQYSNative2.mService = iQYSNative2.mDefault;
                        }
                        IQYSManager.ConnectStatusChangedListener changedListener = IQYSNative.this.mConnectStatusChangedListener;
                        if (changedListener != null) {
                            changedListener.onChanged(isConnected);
                        }
                    }
                }
            });
            connection.connect(context);
            QYSLog.i(TAG, "register: connection started, ref=" + refString(reference));
        } else if (atomicReference.get() != null) {
            registerListener.onRegisterSuccess(this.mService);
            QYSLog.i(TAG, "register: connected already");
        } else {
            QYSLog.i(TAG, "register: connecting ref=" + refString(this.mReference));
            List<IQYSManager.RegisterListener> registerListeners = this.mListeners.get(this.mReference);
            if (registerListeners != null) {
                registerListeners.add(registerListener);
            } else {
                registerListener.onRegisterFailure(-1, "connection created but listeners is null");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String refString(Object o) {
        if (o == null) {
            return "0x00";
        }
        return "0x" + Integer.toHexString(o.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackSuccess(Object obj) {
        IQYSService service = this.mService;
        QYSLog.i(TAG, "callbackSuccess: ref=" + refString(obj) + ",service=" + service);
        List<IQYSManager.RegisterListener> listeners = this.mListeners.get(obj);
        if (listeners != null) {
            for (IQYSManager.RegisterListener listener : listeners) {
                listener.onRegisterSuccess(service);
            }
            listeners.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackFailure(Object obj, int code, String msg) {
        QYSLog.i(TAG, "callbackFailure() called with: obj = [" + refString(obj) + "], code = [" + code + "], msg = [" + msg + "], ref=" + obj);
        List<IQYSManager.RegisterListener> listeners = this.mListeners.get(obj);
        if (listeners != null) {
            for (IQYSManager.RegisterListener listener : listeners) {
                listener.onRegisterFailure(code, msg);
            }
            listeners.clear();
        }
    }

    public void unRegister() {
        QYSLog.i(TAG, "unRegister: ref=" + refString(this.mReference));
        AtomicReference<IQYSConnection> connection = this.mReference;
        IQYSConnection con = connection == null ? null : connection.get();
        if (con != null) {
            con.disConnect();
        }
        this.mReference = null;
    }

    /* loaded from: classes4.dex */
    private static class IQYSServiceImp extends Handler implements IQYSService {
        private static final int MSG_CALLBACK = 100;
        private final IQYSConnection mConnection;

        private IQYSServiceImp(IQYSConnection connection) {
            super(Looper.getMainLooper());
            this.mConnection = connection;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                Object[] arg = (Object[]) msg.obj;
                IQYSService.Callback callback = (IQYSService.Callback) arg[0];
                IQYSResponse response = (IQYSResponse) arg[1];
                callback.onResponse(response);
            }
        }

        @Override // com.qiyi.video.client.IQYSService
        public void executeAsync(final IQYSRequest request, final IQYSService.Callback callback) {
            QYSLog.i(IQYSNative.TAG, "executeAsync called with: cmd=" + request.mCmd);
            IQYSConnection iQYSConnection = this.mConnection;
            IQYS service = (iQYSConnection == null || !iQYSConnection.isValid()) ? null : this.mConnection.getService();
            if (service == null) {
                if (callback != null) {
                    IQYSResponse response = createNoServiceResponse(request);
                    Message message = obtainMessage(100);
                    message.obj = new Object[]{callback, response};
                    sendMessage(message);
                    QYSLog.i(IQYSNative.TAG, "executeAsync onResponse() called with: code=" + response.mCode + ",msg=" + response.mMsg);
                    return;
                }
                return;
            }
            try {
                service.executeAsync(request, new IQYSCallback.Stub() { // from class: com.qiyi.video.client.IQYSNative.IQYSServiceImp.1
                    @Override // com.qiyi.video.qys.IQYSCallback
                    public void onResponse(IQYSResponse iqysResponse) throws RemoteException {
                        StringBuilder sb = new StringBuilder();
                        sb.append("executeAsync onResponse() called with: code=");
                        sb.append(iqysResponse == null ? "NULL" : Integer.valueOf(iqysResponse.mCode));
                        sb.append(",msg=");
                        sb.append(iqysResponse != null ? iqysResponse.mMsg : "NULL");
                        QYSLog.i(IQYSNative.TAG, sb.toString());
                        if (callback != null) {
                            if (iqysResponse != null) {
                                iqysResponse.mRequest = request;
                            }
                            Message message2 = IQYSServiceImp.this.obtainMessage(100);
                            message2.obj = new Object[]{callback, iqysResponse};
                            IQYSServiceImp.this.sendMessage(message2);
                        }
                    }
                });
            } catch (RemoteException e) {
                if (callback != null) {
                    IQYSResponse response2 = createRemoteFailureResponse(request, e.getMessage());
                    Message message2 = obtainMessage(100);
                    message2.obj = new Object[]{callback, response2};
                    sendMessage(message2);
                    QYSLog.i(IQYSNative.TAG, "executeAsync onResponse() called with: " + response2.mMsg);
                }
            }
        }

        @Override // com.qiyi.video.client.IQYSService
        public IQYSResponse executeSync(IQYSRequest request) {
            IQYSConnection iQYSConnection = this.mConnection;
            IQYS service = (iQYSConnection == null || !iQYSConnection.isValid()) ? null : this.mConnection.getService();
            if (service == null) {
                return createNoServiceResponse(request);
            }
            try {
                IQYSResponse response = service.executeSync(request);
                if (response != null) {
                    response.mRequest = request;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("executeSync onResponse() called with: code=");
                Object obj = "NULL";
                sb.append(response == null ? "NULL" : Integer.valueOf(response.mCode));
                sb.append(",msg=");
                if (response != null) {
                    obj = response.mMsg;
                }
                sb.append(obj);
                QYSLog.i(IQYSNative.TAG, sb.toString());
                return response;
            } catch (RemoteException e) {
                return createRemoteFailureResponse(request, e.getMessage());
            }
        }

        private IQYSResponse createNoServiceResponse(IQYSRequest message) {
            IQYSResponse response = new IQYSResponse();
            response.mCode = -3;
            response.mRequest = message;
            response.mMsg = new IQYSString("Service is disconnected, please retry register again and use the newest IQYSService instance.");
            return response;
        }

        private IQYSResponse createRemoteFailureResponse(IQYSRequest message, String eMessage) {
            IQYSResponse response = new IQYSResponse();
            response.mCode = -5;
            response.mRequest = message;
            response.mMsg = new IQYSString("Service connection is bad, msg=" + eMessage);
            return response;
        }
    }
}
