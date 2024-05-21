package com.xiaopeng.xuiservice.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes5.dex */
public class XuiWorkHandler extends Handler {
    private static final XuiWorkHandler sInstance;
    private final boolean BLOCK_DISABLE;
    private long WAIT_TIME;
    private static ReentrantLock mReentrantLock = new ReentrantLock();
    private static final String TAG = "XuiWorkHandler";
    private static final HandlerThread mHandlerThread = new HandlerThread(TAG);

    static {
        mHandlerThread.start();
        sInstance = new XuiWorkHandler();
        sInstance.post(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$XuiWorkHandler$SZJ8OSZWzW-rY2ODtsmrLfOpZCY
            @Override // java.lang.Runnable
            public final void run() {
                XuiWorkHandler.lambda$static$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0() {
        while (true) {
            try {
                Looper.loop();
            } catch (Throwable e) {
                LogUtil.w(TAG, "catch handler Exception=" + Log.getStackTraceString(e));
            }
        }
    }

    public XuiWorkHandler() {
        super(mHandlerThread.getLooper());
        this.WAIT_TIME = 50L;
        this.BLOCK_DISABLE = true;
    }

    public static XuiWorkHandler getInstance() {
        return sInstance;
    }

    @Override // android.os.Handler
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        ReentrantLock reentrantLock;
        boolean result = false;
        try {
            if (mReentrantLock.tryLock(this.WAIT_TIME, TimeUnit.MILLISECONDS)) {
                try {
                    result = super.sendMessageAtTime(msg, uptimeMillis);
                    reentrantLock = mReentrantLock;
                } catch (Exception e) {
                    reentrantLock = mReentrantLock;
                } catch (Throwable th) {
                    mReentrantLock.unlock();
                    throw th;
                }
                reentrantLock.unlock();
            } else {
                LogUtil.w(TAG, "sendMessageAtTime trylock fail lost msg=" + msg);
            }
        } catch (InterruptedException e2) {
            LogUtil.w(TAG, "sendMessageAtTime interrupted e=" + e2);
        }
        return result;
    }
}
