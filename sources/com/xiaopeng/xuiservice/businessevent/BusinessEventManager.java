package com.xiaopeng.xuiservice.businessevent;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.Map;
/* loaded from: classes5.dex */
public class BusinessEventManager {
    private static final String ACTION_EVENT_FILTER = "com.xiaopeng.xui.businessevent";
    private static final String TAG = BusinessEventManager.class.getSimpleName();
    private Context mContext;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class BusinessEventManagerHolder {
        private static final BusinessEventManager sInstance = new BusinessEventManager();

        private BusinessEventManagerHolder() {
        }
    }

    private BusinessEventManager() {
        this.mContext = ActivityThread.currentActivityThread().getApplication();
    }

    public static BusinessEventManager getInstance() {
        return BusinessEventManagerHolder.sInstance;
    }

    public void init() {
        AppStoreEventManager.getInstance().init();
        if (!XUIConfig.isXuiProcessHelpLaunch()) {
            LogUtil.i(TAG, "init, feature not configured");
            return;
        }
        LogUtil.d(TAG, "init, feature configured");
        CameraEventManager.getInstance().init();
        FactoryEventManager.getInstance().init();
        NetworkEventManager.getInstance().init();
        OtaEventManager.getInstance().init();
    }

    public void notifyApp(String pkgName, Map<String, String> eventMap) {
        Intent intent = new Intent();
        intent.setPackage(pkgName);
        intent.setAction(ACTION_EVENT_FILTER);
        intent.addFlags(20971520);
        if (eventMap != null) {
            for (String key : eventMap.keySet()) {
                intent.putExtra(key, eventMap.get(key));
            }
        }
        notifyAppAsync(intent);
    }

    private void notifyAppAsync(final Intent intent) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$BusinessEventManager$bbO1mEuLl0mrPT0-PwbL4M6SNfM
            @Override // java.lang.Runnable
            public final void run() {
                BusinessEventManager.lambda$notifyAppAsync$0(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$notifyAppAsync$0(Intent intent) {
        String str = TAG;
        LogUtil.i(str, "notify app,intent=" + intent);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }
}
