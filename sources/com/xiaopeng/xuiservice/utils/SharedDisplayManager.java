package com.xiaopeng.xuiservice.utils;

import android.app.ActivityThread;
import android.os.RemoteException;
import android.view.WindowManager;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.view.SharedDisplayListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.xapp.XAppStartManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class SharedDisplayManager {
    private static final String TAG = "SharedDisplayManager";
    private static SharedDisplayManager mInstance;
    private List<ISharedDisplayEventListener> mEventListeners = new ArrayList();
    private WindowManager mWindowManager = (WindowManager) ActivityThread.currentActivityThread().getApplication().getSystemService(ConditionWindowPos.TYPE);
    private SharedDisplayListenerImpl mSharedDisplayListenerImpl = new SharedDisplayListenerImpl();

    private SharedDisplayManager() {
    }

    public static SharedDisplayManager getInstance() {
        if (mInstance == null) {
            synchronized (SharedDisplayManager.class) {
                if (mInstance == null) {
                    mInstance = new SharedDisplayManager();
                }
            }
        }
        return mInstance;
    }

    public void confirmSharedEvent(int targetScreen) {
        LogUtil.d(TAG, "confirmSharedEvent targetScreen:" + targetScreen);
        this.mWindowManager.setSharedEvent(0, targetScreen, XAppStartManager.SHARED_FROM_USER);
    }

    public void setSharedId(String packageName, int sharedId) {
        LogUtil.d(TAG, "setSharedId pkgName:" + packageName + " &sharedId:" + sharedId);
        this.mWindowManager.setScreenId(packageName, sharedId);
    }

    public void setSelfSharedId(int displayId) {
        setSharedId(ActivityThread.currentActivityThread().getApplication().getPackageName(), displayId);
    }

    public static boolean hasSharedDisplayFeature() {
        return FeatureOption.FO_SHARED_DISPLAY_ENABLED;
    }

    public List<String> getPrimaryFilterPackages() {
        return this.mWindowManager.getFilterPackages(0);
    }

    public List<String> getFilterPackages(int displayId) {
        return this.mWindowManager.getFilterPackages(displayId);
    }

    public boolean isSupportSharedDisplay(String pkgName) {
        List<String> unSupportList = this.mWindowManager.getFilterPackages(-1);
        if (unSupportList == null || unSupportList.isEmpty()) {
            return true;
        }
        return true ^ unSupportList.contains(pkgName);
    }

    public void startSharedDisplay(int toDisplayId) {
        this.mWindowManager.setSharedEvent(0, toDisplayId);
    }

    public List<String> getSecondFilterPackages() {
        return this.mWindowManager.getFilterPackages(1);
    }

    public List<String> getSharedPackages() {
        return this.mWindowManager.getSharedPackages();
    }

    public int getSharedId(String pkgName) {
        return this.mWindowManager.getScreenId(pkgName);
    }

    public void goSharedDesktop(int displayId) {
        this.mWindowManager.setSharedEvent(3, displayId, "");
    }

    public String getNapaDialogString() {
        return this.mWindowManager.getTopWindow();
    }

    public synchronized void registerListener(ISharedDisplayEventListener listener) {
        if (this.mEventListeners.isEmpty()) {
            this.mWindowManager.registerSharedListener(this.mSharedDisplayListenerImpl);
        }
        if (!this.mEventListeners.contains(listener)) {
            this.mEventListeners.add(listener);
        }
    }

    public synchronized void unregisterListener(ISharedDisplayEventListener listener) {
        if (this.mEventListeners.contains(listener)) {
            this.mEventListeners.remove(listener);
        }
        if (this.mEventListeners.isEmpty()) {
            this.mWindowManager.unregisterSharedListener(this.mSharedDisplayListenerImpl);
        }
    }

    /* loaded from: classes5.dex */
    private final class SharedDisplayListenerImpl extends SharedDisplayListener {
        private SharedDisplayListenerImpl() {
        }

        public void onChanged(String pkgName, int sharedId) throws RemoteException {
            LogUtil.i(SharedDisplayManager.TAG, "onChanged packageName=" + pkgName + " sharedId=" + sharedId);
            for (ISharedDisplayEventListener listener : SharedDisplayManager.this.mEventListeners) {
                listener.onChanged(pkgName, sharedId);
            }
        }

        public void onPositionChanged(String pkgName, int event, int fromSharedId, int toSharedId) throws RemoteException {
            LogUtil.i(SharedDisplayManager.TAG, "onPositionChanged packageName=" + pkgName + " event=" + event + " from=" + fromSharedId + " to=" + toSharedId);
            for (ISharedDisplayEventListener listener : SharedDisplayManager.this.mEventListeners) {
                listener.onPositionChanged(pkgName, event, fromSharedId, toSharedId);
            }
        }

        public void onActivityChanged(int screenId, String property) throws RemoteException {
            LogUtil.i(SharedDisplayManager.TAG, "onActivityChanged screenId=" + screenId + "&property=" + property);
            for (ISharedDisplayEventListener listener : SharedDisplayManager.this.mEventListeners) {
                listener.onActivityChanged(screenId, property);
            }
        }
    }

    /* loaded from: classes5.dex */
    public interface ISharedDisplayEventListener {
        default void onChanged(String pkgName, int sharedId) {
        }

        default void onPositionChanged(String pkgName, int event, int fromSharedId, int toSharedId) {
        }

        default void onActivityChanged(int screenId, String property) {
        }
    }
}
