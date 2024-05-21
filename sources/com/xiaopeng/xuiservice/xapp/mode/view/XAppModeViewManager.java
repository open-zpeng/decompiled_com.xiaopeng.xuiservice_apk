package com.xiaopeng.xuiservice.xapp.mode.view;

import android.app.ActivityThread;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.innerutils.DialogUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.xapp.mode.octopus.widget.JoystickQuickMenuView;
import com.xiaopeng.xuiservice.xapp.mode.octopus.widget.KeyConfigViewGroup;
import com.xiaopeng.xuiservice.xapp.mode.widget.GameDialog;
import com.xiaopeng.xuiservice.xapp.mode.widget.NavigationBar;
import com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel;
/* loaded from: classes5.dex */
public class XAppModeViewManager {
    private static final String TAG = "XAppModeViewManager";
    private static XAppModeViewManager sXAppModeViewManager;
    private SparseArray<NavigationBar> mBarSparseArray;
    private GameDialog mGameDialog;
    private JoystickQuickMenuView mJoystickQuickMenuView;
    private KeyConfigViewGroup mKeyConfigViewGroup;
    private String mPkgName;
    private QsPanel mQsPanel;
    private int delta = 20;
    private volatile transient boolean mQsPanelShow = false;
    private QsPanel.UserControlQsListener mUserControlQsListener = new QsPanel.UserControlQsListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.20
        @Override // com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.UserControlQsListener
        public void onUserControlQS(int displayId) {
            XAppModeViewManager.this.highlightNavigationBar(displayId);
        }
    };
    private DismissTask dismissTask = new DismissTask();
    private Context mContext = ActivityThread.currentActivityThread().getApplication();
    private LayoutInflater mLayoutInflater = LayoutInflater.from(this.mContext);
    private ClearDataDialogWrapper mClearDataDialogWrapper = new ClearDataDialogWrapper();
    private EffectDialogWrapper mEffectDialogWrapper = new EffectDialogWrapper();
    private AppTipsDialogWrapper mAppTipsDialogWrapper = new AppTipsDialogWrapper();

    public static XAppModeViewManager getInstance() {
        synchronized (XAppModeViewManager.class) {
            if (sXAppModeViewManager == null) {
                sXAppModeViewManager = new XAppModeViewManager();
            }
        }
        return sXAppModeViewManager;
    }

    private XAppModeViewManager() {
    }

    public void showNavigationBar(final int displayId, final xpPackageInfo xpPackageInfo) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.1
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d(XAppModeViewManager.TAG, "showNavigationBar displayId:" + displayId + " &xpPackageInfo:" + xpPackageInfo.toString());
                NavigationBar navigationBar = XAppModeViewManager.this.getNavigationBar(displayId);
                navigationBar.setDisplayId(displayId);
                navigationBar.updatePackageInfo(xpPackageInfo);
                navigationBar.addToWindow();
            }
        });
    }

    public void hideAllNavigationBar() {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.2
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d(XAppModeViewManager.TAG, "removeNavigationBar");
                if (XAppModeViewManager.this.mBarSparseArray != null) {
                    for (int i = 0; i < XAppModeViewManager.this.mBarSparseArray.size(); i++) {
                        NavigationBar bar = (NavigationBar) XAppModeViewManager.this.mBarSparseArray.valueAt(i);
                        if (bar != null) {
                            if (bar.isAttachedToWindow()) {
                                bar.cachePosition();
                            }
                            bar.setVisibility(4);
                        }
                    }
                }
            }
        });
    }

    public void removeNavigationBar(final int displayId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.3
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d(XAppModeViewManager.TAG, "hideNavigationBar displayId:" + displayId);
                NavigationBar navigationBar = XAppModeViewManager.this.getNavigationBar(displayId);
                navigationBar.removeFromWindow();
            }
        });
    }

    public void hideOrShowQsPanel(final xpPackageInfo packageInfo, final int displayId, final int x, final int y) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.4
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.initQsPanelView();
                XAppModeViewManager.this.mQsPanel.hideOrShow(packageInfo, displayId, x, y);
            }
        });
    }

    public void hideOrShowKaraokeEffectDialog(final String pkgName, final int displayId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.5
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mEffectDialogWrapper.show(pkgName, displayId);
            }
        });
    }

    public void showAppTipsStatusDialog(final String pkgName, final int displayId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.6
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mAppTipsDialogWrapper.show(pkgName, displayId);
            }
        });
    }

    public void dismissAppTipsStatusDialog(final String pkgName, final int displayId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.7
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mAppTipsDialogWrapper.dismiss(displayId, pkgName);
            }
        });
    }

    public void dismissKaraokeEffectDialog() {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.8
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mEffectDialogWrapper.dismiss();
            }
        });
    }

    public void dismissKaraokeEffectDialog(final int displayId, final String pkgName) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.9
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mEffectDialogWrapper.dismiss(displayId, pkgName);
            }
        });
    }

    public void showQsPanel(final xpPackageInfo packageInfo, final int displayId, final int x, final int y) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.10
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.initQsPanelView();
                if (XAppModeViewManager.this.mQsPanel != null) {
                    XAppModeViewManager.this.mQsPanel.addToWindowAtPosition(packageInfo, displayId, x, y);
                }
            }
        });
    }

    public void hideQsPanel(final int displayId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.11
            @Override // java.lang.Runnable
            public void run() {
                if (XAppModeViewManager.this.mQsPanel != null) {
                    XAppModeViewManager.this.mQsPanel.removeFromWindow(displayId);
                }
            }
        });
    }

    public void hideOrHideJoystickView(final int x, final int y) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.12
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.initJoystickView();
                XAppModeViewManager.this.mJoystickQuickMenuView.hideOrShow(x, y);
            }
        });
    }

    public void showClearAppDialog(final int displayId, final String pkgName) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.13
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mClearDataDialogWrapper.show(pkgName, displayId);
            }
        });
    }

    public void dismissClearAppDialog() {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.14
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mClearDataDialogWrapper.dismiss();
            }
        });
    }

    public void dismissClearAppDialog(final int displayId, final String pkgName) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.15
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mClearDataDialogWrapper.dismiss(displayId, pkgName);
            }
        });
    }

    public void dismissModuleDeniedDialog() {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.16
            @Override // java.lang.Runnable
            public void run() {
                DialogUtil.dismissModeDeniedDialog();
            }
        });
    }

    public void showGameDialog(final xpPackageInfo packageInfo) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.17
            @Override // java.lang.Runnable
            public void run() {
                if (XAppModeViewManager.this.mGameDialog == null) {
                    XAppModeViewManager.this.initGameDialog();
                }
                if (packageInfo != null) {
                    XAppModeViewManager.this.mGameDialog.setPackageInfo(packageInfo);
                }
                XAppModeViewManager.this.mGameDialog.setShowDetailStyle(false);
                XAppModeViewManager.this.mGameDialog.show();
            }
        });
    }

    public void showGameDetail() {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.18
            @Override // java.lang.Runnable
            public void run() {
                XAppModeViewManager.this.mGameDialog.setShowDetailStyle(true);
                XAppModeViewManager.this.mGameDialog.show();
            }
        });
    }

    public void dismissGameDialog() {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager.19
            @Override // java.lang.Runnable
            public void run() {
                if (XAppModeViewManager.this.mGameDialog != null && XAppModeViewManager.this.mGameDialog.isShowing()) {
                    XAppModeViewManager.this.mGameDialog.dismiss();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initGameDialog() {
        if (this.mGameDialog == null) {
            this.mGameDialog = new GameDialog(this.mContext, R.style.simpleDialogStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void initQsPanelView() {
        if (this.mQsPanel == null) {
            this.mQsPanel = (QsPanel) this.mLayoutInflater.inflate(R.layout.quick_settings_panel, (ViewGroup) null);
            this.mQsPanel.setUserControlQSListener(this.mUserControlQsListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void initJoystickView() {
        if (this.mJoystickQuickMenuView == null) {
            this.mJoystickQuickMenuView = (JoystickQuickMenuView) this.mLayoutInflater.inflate(R.layout.quick_menu_joystick, (ViewGroup) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NavigationBar getNavigationBar(int displayId) {
        if (this.mBarSparseArray == null) {
            this.mBarSparseArray = new SparseArray<>();
        }
        NavigationBar bar = this.mBarSparseArray.get(displayId);
        if (bar == null) {
            NavigationBar bar2 = (NavigationBar) this.mLayoutInflater.inflate(R.layout.layout_navigation_window, (ViewGroup) null);
            this.mBarSparseArray.put(displayId, bar2);
            return bar2;
        }
        return bar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void highlightNavigationBar(int displayId) {
        NavigationBar navigationBar = getNavigationBar(displayId);
        if (navigationBar != null) {
            navigationBar.onUserTouch();
        }
    }

    public void cancelDelayDismissTask(int displayId) {
        this.dismissTask.setDisplayId(displayId);
        UiHandler.getInstance().removeCallbacks(this.dismissTask);
    }

    public void addDelayDismissTask(int displayId) {
        this.dismissTask.setDisplayId(displayId);
        UiHandler.getInstance().postDelayed(this.dismissTask, 200L);
    }

    /* loaded from: classes5.dex */
    class DismissTask implements Runnable {
        private int displayId;

        DismissTask() {
        }

        public void setDisplayId(int displayId) {
            this.displayId = displayId;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (XAppModeViewManager.this.mQsPanel != null) {
                XAppModeViewManager.this.mQsPanel.removeFromWindow(this.displayId);
            }
        }
    }
}
