package com.xiaopeng.xuiservice.carcontrol;

import android.content.Context;
import android.os.Message;
import android.util.ArraySet;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.lang.ref.WeakReference;
import java.util.Collection;
/* loaded from: classes5.dex */
public class CommonMsgController {
    private static final int MSG_CONTEXTINFO_COMMON_EVENT = 0;
    public static final String TAG = "CommonMsgController";
    private static CommonMsgController mCommonMsgController;
    private final ArraySet<InternalCommonEventListener> mInternalCommonEventListeners = new ArraySet<>();
    private final EventCallbackHandler mHandler = new EventCallbackHandler(this);

    public CommonMsgController(Context context) {
    }

    public static CommonMsgController getInstance(Context context) {
        if (mCommonMsgController == null) {
            mCommonMsgController = new CommonMsgController(context);
        }
        return mCommonMsgController;
    }

    public synchronized void registerListener(InternalCommonEventListener listener) {
        this.mInternalCommonEventListeners.add(listener);
    }

    public synchronized void unregisterListener(InternalCommonEventListener listener) {
        LogUtil.d(TAG, "unregisterListener(InternalCommonEventListener listener)");
        this.mInternalCommonEventListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchCommonEvent(int eventType, int eventValue) {
        Collection<InternalCommonEventListener> commonEventListeners;
        synchronized (this) {
            commonEventListeners = this.mInternalCommonEventListeners;
        }
        if (!commonEventListeners.isEmpty()) {
            for (InternalCommonEventListener l : commonEventListeners) {
                try {
                    l.onInternalCommonEvent(eventType, eventValue);
                } catch (Exception e) {
                    LogUtil.e("test", "dispatchCommonEvent  " + e);
                }
            }
        }
    }

    /* loaded from: classes5.dex */
    public interface InternalCommonEventListener {
        default void onInternalCommonEvent(int eventType, int eventValue) {
        }
    }

    public void sendCommonEvent(int eventType, int eventValue) {
        handleCommonEvent(eventType, eventValue);
    }

    private void handleCommonEvent(int eventType, int eventValue) {
        Message message = this.mHandler.obtainMessage();
        message.what = 0;
        message.obj = Integer.valueOf(eventType);
        message.arg1 = eventValue;
        this.mHandler.sendMessage(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static final class EventCallbackHandler extends XuiWorkHandler {
        WeakReference<CommonMsgController> mMgr;

        EventCallbackHandler(CommonMsgController mgr) {
            this.mMgr = new WeakReference<>(mgr);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            CommonMsgController mgr = this.mMgr.get();
            if (msg.what == 0) {
                if (mgr != null) {
                    mgr.dispatchCommonEvent(((Integer) msg.obj).intValue(), msg.arg1);
                    return;
                }
                return;
            }
            LogUtil.e(CommonMsgController.TAG, "Event type not handled?" + msg);
        }
    }
}
