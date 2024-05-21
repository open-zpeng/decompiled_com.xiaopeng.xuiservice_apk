package com.qiyi.video.client;

import android.content.Context;
/* loaded from: classes4.dex */
public class IQYSManager {
    private final IQYSNative mNative;

    /* loaded from: classes4.dex */
    public interface ConnectStatusChangedListener {
        void onChanged(boolean z);
    }

    /* loaded from: classes4.dex */
    public interface RegisterListener {
        void onRegisterFailure(int i, String str);

        void onRegisterSuccess(IQYSService iQYSService);
    }

    /* loaded from: classes4.dex */
    private static class SINGLE {
        public static final IQYSManager INSTANCE = new IQYSManager();

        private SINGLE() {
        }
    }

    public static IQYSManager getInstance() {
        return SINGLE.INSTANCE;
    }

    private IQYSManager() {
        this.mNative = new IQYSNative();
    }

    public void addConnectStatusChangedListener(ConnectStatusChangedListener connectStatusChangedListener) {
        this.mNative.addConnectStatusChangedListener(connectStatusChangedListener);
    }

    public void removeConnectStatusChangedListener(ConnectStatusChangedListener connectStatusChangedListener) {
        this.mNative.removeConnectStatusChangedListener(connectStatusChangedListener);
    }

    public boolean isConnected() {
        return this.mNative.isConnected();
    }

    public IQYSService getService() {
        return this.mNative.getService();
    }

    public void register(Context context, RegisterListener listener) {
        this.mNative.register(context, listener);
    }

    public void unRegister() {
        this.mNative.unRegister();
    }
}
