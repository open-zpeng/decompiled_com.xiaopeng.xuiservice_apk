package com.xiaopeng.xuiservice.xapp.mode;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.WindowManager;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager;
/* loaded from: classes5.dex */
public class XAppNavigationModeEngine extends XAppBaseModeEngine {
    private static final String TAG = "XAppNavigationModeEngine";
    private WindowManager mWindowManager;
    private XAppModeViewManager mXAppModeViewManager;

    public XAppNavigationModeEngine(Context context, Handler handler) {
        super(context, handler);
        LogUtil.v(TAG, "started XAppNavigationModeEngine!");
        this.mXAppModeViewManager = XAppModeViewManager.getInstance();
        this.mWindowManager = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
        registerSharedListener();
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected synchronized void handleAppInfoChange() {
        LogUtil.d(TAG, "handleAppInfoChange:");
        this.mEnable = isCurrentModeEnable();
        int displayId = this.mWindowManager.getScreenId(this.mPkgName);
        boolean isPrimary = WindowManager.isPrimaryId(displayId);
        int screenId = isPrimary ? 0 : 1;
        LogUtil.d(TAG, "currentPackage : " + this.mPkgName + "&enable : " + this.mEnable + "&screenId:" + screenId);
        if (this.mEnable) {
            LogUtil.d(TAG, "handleAppInfoChange showNavigationBar:" + screenId);
            this.mXAppModeViewManager.showNavigationBar(screenId, this.mPackageInfo);
        } else {
            LogUtil.d(TAG, "handleAppInfoChange hideNavigationBar:" + screenId);
            this.mXAppModeViewManager.removeNavigationBar(screenId);
            this.mXAppModeViewManager.hideQsPanel(screenId);
        }
    }

    public void refreshNaviBar(final int screenId) {
        getWorkHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppNavigationModeEngine.1
            @Override // java.lang.Runnable
            public void run() {
                XAppNavigationModeEngine.this.refreshNaviBarWithScreenId(screenId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshNaviBar() {
        refreshNaviBarWithScreenId(0);
        refreshNaviBarWithScreenId(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideAllNaviBar() {
        XAppModeViewManager.getInstance().hideAllNavigationBar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideQsPanel(int displayId) {
        XAppModeViewManager.getInstance().hideQsPanel(displayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideDialog() {
        XAppModeViewManager.getInstance().dismissClearAppDialog();
        XAppModeViewManager.getInstance().dismissKaraokeEffectDialog();
    }

    private void hideDialog(int screenId, String pkgName) {
        XAppModeViewManager.getInstance().dismissClearAppDialog(screenId, pkgName);
        XAppModeViewManager.getInstance().dismissKaraokeEffectDialog(screenId, pkgName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshNaviBarWithScreenId(int screenId) {
        String activityName = this.mWindowManager.getTopActivity(0, screenId);
        String pkgName = "";
        if (!TextUtils.isEmpty(activityName)) {
            pkgName = getPackageName(activityName);
        }
        hideDialog(screenId, pkgName);
        xpPackageInfo packageInfo = getXpPackageInfo(pkgName);
        boolean isShowNaviBar = isShowNaviBar(packageInfo);
        LogUtil.i(TAG, "refreshNaviBarWithScreenId screenId:" + screenId + " &topActivity:" + activityName + " &isShowNaviBar:" + isShowNaviBar);
        if (isShowNaviBar) {
            this.mXAppModeViewManager.showNavigationBar(screenId, packageInfo);
            return;
        }
        this.mXAppModeViewManager.removeNavigationBar(screenId);
        this.mXAppModeViewManager.hideQsPanel(screenId);
    }

    private String getPackageName(String activityName) {
        ComponentName componentName = ComponentName.unflattenFromString(activityName);
        return componentName.getPackageName();
    }

    private xpPackageInfo getXpPackageInfo(String pkgName) {
        if (!TextUtils.isEmpty(pkgName) && !pkgName.startsWith("com.xiaopeng")) {
            try {
                return AppGlobals.getPackageManager().getXpPackageInfo(pkgName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private void registerSharedListener() {
        SharedDisplayManager.getInstance().registerListener(new SharedDisplayManager.ISharedDisplayEventListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppNavigationModeEngine.2
            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onPositionChanged(String pkgName, int event, final int fromSharedId, int toSharedId) {
                if (!"com.sinovoice.hcicloudinputvehicle".equals(pkgName)) {
                    if (event == 1) {
                        XAppNavigationModeEngine.this.getWorkHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppNavigationModeEngine.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                XAppNavigationModeEngine.this.hideAllNaviBar();
                                XAppNavigationModeEngine.this.hideQsPanel(fromSharedId);
                                XAppNavigationModeEngine.this.hideDialog();
                            }
                        });
                    } else if (event == 4) {
                        XAppNavigationModeEngine.this.getWorkHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppNavigationModeEngine.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                XAppNavigationModeEngine.this.refreshNaviBar();
                            }
                        });
                    }
                }
            }

            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onActivityChanged(int screenId, String property) {
                XAppNavigationModeEngine.this.getWorkHandler().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppNavigationModeEngine.2.3
                    @Override // java.lang.Runnable
                    public void run() {
                        XAppNavigationModeEngine.this.refreshNaviBar();
                    }
                });
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected boolean isCurrentModeEnable() {
        return this.mPackageInfo != null && this.mPackageInfo.navigationKey > 0;
    }

    protected boolean isShowNaviBar(xpPackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        LogUtil.d(TAG, "navigationKey:" + packageInfo.navigationKey);
        return packageInfo.navigationKey > 0;
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected void handleEnableChange() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }
}
