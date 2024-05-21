package com.xiaopeng.xuiservice.xapp.mode;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
/* loaded from: classes5.dex */
public abstract class XAppBaseModeEngine extends BaseCarListener {
    public static final boolean DBG = false;
    public static final int MODE_GAME = 1;
    public static final int MODE_NAVI_VIEW = 2;
    private static final String TAG = "XAppBaseModeEngine";
    protected boolean mEnable;
    protected int mMode;
    protected xpPackageInfo mPackageInfo;
    protected String mPkgName;
    protected int mSubMode;
    private WorkHandler mWorkHandler;

    /* loaded from: classes5.dex */
    private class WorkHandler extends Handler {
        static final int MSG_APP_INFO_CHANGE = 2;
        static final int MSG_APP_SHARED_INFO_CHANGED = 3;
        static final int MSG_ENABLE = 1;

        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                XAppBaseModeEngine.this.handleEnableChange();
            } else if (i == 2) {
                XAppBaseModeEngine.this.handleAppInfoChange();
            } else if (i == 3) {
                xpPackageInfo[] packageInfos = (xpPackageInfo[]) msg.obj;
                XAppBaseModeEngine.this.handleSharedPackageInfoChange(packageInfos);
            }
        }
    }

    public XAppBaseModeEngine(Context context, Handler handler) {
        super(context);
        this.mWorkHandler = new WorkHandler(handler.getLooper());
        this.mWorkHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine.1
            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        LogUtil.w(XAppBaseModeEngine.TAG, "catch UncaughtException:" + e.getMessage());
                    }
                }
            }
        });
    }

    protected void setEnable(String pkgName, int mode, int subMode) {
        this.mPkgName = pkgName;
        this.mMode = mode;
        this.mSubMode = subMode;
        this.mWorkHandler.sendEmptyMessage(1);
    }

    public synchronized void onAppInfoChange(String pkgName, xpPackageInfo info) {
        this.mPkgName = pkgName;
        this.mPackageInfo = info;
        Message message = this.mWorkHandler.obtainMessage(2);
        message.setAsynchronous(true);
        message.sendToTarget();
    }

    public synchronized void onSharedPackageInfoChanged(xpPackageInfo[] packageInfos) {
        Message message = this.mWorkHandler.obtainMessage(3, packageInfos);
        message.setAsynchronous(true);
        message.sendToTarget();
    }

    public Handler getWorkHandler() {
        return this.mWorkHandler;
    }

    protected boolean isModeEnable(int mode) {
        return (this.mMode & mode) == mode && !TextUtils.isEmpty(this.mPkgName);
    }

    protected boolean isCurrentModeEnable() {
        return false;
    }

    protected void handleEnableChange() {
    }

    protected synchronized void handleAppInfoChange() {
    }

    protected synchronized void handleSharedPackageInfoChange(xpPackageInfo[] packageInfos) {
    }
}
