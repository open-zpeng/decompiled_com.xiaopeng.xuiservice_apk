package com.xiaopeng.xuiservice.awareness;

import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.HalServiceBase;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class AwarenessHalService extends HalServiceBase {
    private static final boolean DBG = true;
    private static final String TAG = "AwarenessHalService";
    private AwarenessHalListener mListener;

    /* loaded from: classes5.dex */
    public interface AwarenessHalListener {
        void onError(int i, int i2);
    }

    public AwarenessHalService() {
        LogUtil.d(TAG, "started AwarenessHalService!");
    }

    public void setListener(AwarenessHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void init() {
        LogUtil.d(TAG, "init()");
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void release() {
        LogUtil.d(TAG, "release()");
        this.mListener = null;
    }

    public void handleHalEvents() {
        AwarenessHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(AwarenessHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void dump(PrintWriter writer) {
        writer.println("*Awareness HAL*");
    }
}
