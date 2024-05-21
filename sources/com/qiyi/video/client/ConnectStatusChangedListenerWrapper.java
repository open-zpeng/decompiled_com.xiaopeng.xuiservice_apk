package com.qiyi.video.client;

import com.qiyi.video.client.IQYSManager;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class ConnectStatusChangedListenerWrapper implements IQYSManager.ConnectStatusChangedListener {
    private final List<IQYSManager.ConnectStatusChangedListener> mList = new ArrayList();
    private final List<IQYSManager.ConnectStatusChangedListener> mProcessList = new ArrayList();

    public void addListener(IQYSManager.ConnectStatusChangedListener listener) {
        synchronized (this.mList) {
            this.mList.add(listener);
        }
    }

    public void removeListener(IQYSManager.ConnectStatusChangedListener listener) {
        synchronized (this.mList) {
            this.mList.remove(listener);
        }
    }

    @Override // com.qiyi.video.client.IQYSManager.ConnectStatusChangedListener
    public void onChanged(boolean isConnected) {
        synchronized (this.mList) {
            this.mProcessList.addAll(this.mList);
            for (IQYSManager.ConnectStatusChangedListener listener : this.mProcessList) {
                if (listener != null) {
                    listener.onChanged(isConnected);
                }
            }
            this.mProcessList.clear();
        }
    }
}
