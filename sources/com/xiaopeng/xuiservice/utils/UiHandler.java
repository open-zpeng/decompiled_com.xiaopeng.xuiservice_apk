package com.xiaopeng.xuiservice.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes5.dex */
public class UiHandler extends Handler {
    private static final String TAG = "UiHandler";
    private final boolean BLOCK_DISABLE;
    private long WAIT_TIME;
    private static final UiHandler sInstance = new UiHandler(Looper.getMainLooper());
    private static ReentrantLock mReentrantLock = new ReentrantLock();

    static {
        sInstance.post(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$UiHandler$-SqIVqBb2aIZSThXoGn_NsqrR_I
            @Override // java.lang.Runnable
            public final void run() {
                UiHandler.lambda$static$0();
            }
        });
    }

    public UiHandler() {
        super(Looper.getMainLooper());
        this.WAIT_TIME = 50L;
        this.BLOCK_DISABLE = true;
    }

    private UiHandler(Looper looper) {
        super(looper);
        this.WAIT_TIME = 50L;
        this.BLOCK_DISABLE = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0() {
        while (true) {
            try {
                LogUtil.d(TAG, "looper loop enter");
                Looper.loop();
            } catch (IllegalStateException e) {
                LogUtil.w(TAG, "catch UiHandler Exception=" + e + ",trace=" + Log.getStackTraceString(e).replaceAll("[\\t\\n\\r]", "##"));
            }
        }
    }

    public static UiHandler getInstance() {
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
            e2.printStackTrace();
        }
        return result;
    }
}
