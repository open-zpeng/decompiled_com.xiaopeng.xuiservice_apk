package com.xiaopeng.xuiservice.xapp.mode;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.WindowManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.xapp.XAppHalService;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class XAppModeController {
    private static final String GAME_CONTROL_TOUCH = "touch";
    private static final String GAME_CONTROL_WHEEL = "wheelSteering";
    private static final String KEY_GAME_CONTROL_WAY = "key_sp_game_control_way";
    private static final String TAG = "XAppModeController";
    private static XAppModeController mInstance;
    private List<GameControlListener> mControlListeners = new ArrayList();
    private ActivityManager mActivityManager = (ActivityManager) ActivityThread.currentActivityThread().getApplication().getSystemService("activity");
    private WindowManager mWindowManager = (WindowManager) ActivityThread.currentActivityThread().getApplication().getSystemService(ConditionWindowPos.TYPE);

    /* loaded from: classes5.dex */
    public interface GameControlListener {
        void onControlWayChanged(boolean z);
    }

    private XAppModeController() {
    }

    public static XAppModeController getInstance() {
        synchronized (XAppModeController.class) {
            if (mInstance == null) {
                mInstance = new XAppModeController();
            }
        }
        return mInstance;
    }

    public void exitCurrentApp(int displayId, String pkgName) {
        goSharedDesktop(displayId);
    }

    public void goHome(int displayId) {
        goSharedDesktop(displayId);
    }

    public void forceStopApp(String pkgName) {
        this.mActivityManager.forceStopPackage(pkgName);
    }

    private void refreshSecondNaviBar(final int displayId) {
        final XAppNavigationModeEngine engine = (XAppNavigationModeEngine) XAppHalService.getInstance().getModeEngineImpl(XAppNavigationModeEngine.class);
        engine.getWorkHandler().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppModeController.1
            @Override // java.lang.Runnable
            public void run() {
                String activityName = XAppModeController.this.mWindowManager.getTopActivity(0, displayId);
                LogUtil.d(XAppModeController.TAG, "refreshSecondNaviBar activityName:" + activityName);
                if (TextUtils.isEmpty(activityName)) {
                    engine.refreshNaviBar(displayId);
                }
            }
        }, 250L);
    }

    public void goSharedDesktop(int displayId) {
        SharedDisplayManager.getInstance().goSharedDesktop(displayId);
    }

    public void goHome() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addFlags(270532608);
            ActivityThread.currentActivityThread().getApplication().startActivity(intent);
        } catch (Exception e) {
            LogUtil.d(TAG, "goHome exception:" + e.getMessage());
        }
    }

    public void clearAppData(int displayId, String pkgName) {
        if (this.mActivityManager != null && !TextUtils.isEmpty(pkgName)) {
            exitCurrentApp(displayId, pkgName);
            this.mActivityManager.clearApplicationUserData(pkgName, null);
        }
    }

    public void goBack(Rect windowRect) {
        LogUtil.d(TAG, "goBack to do");
        if (windowRect != null && SharedDisplayManager.hasSharedDisplayFeature()) {
            InputEventUtil.dispatchTouchEvent(windowRect.centerX(), windowRect.top);
        }
        InputEventUtil.dispatchKey(4);
    }

    public void goBack(int displayId) {
        InputEventUtil.dispatchKey(4, displayId);
    }

    public void setWSControlStatus(boolean useWheelSteering) {
        notifyListeners(useWheelSteering);
        SharedPreferencesUtil.getInstance().put(KEY_GAME_CONTROL_WAY, useWheelSteering ? GAME_CONTROL_WHEEL : GAME_CONTROL_TOUCH);
    }

    private void notifyListeners(boolean useWheelSteering) {
        synchronized (this.mControlListeners) {
            for (GameControlListener listener : this.mControlListeners) {
                listener.onControlWayChanged(useWheelSteering);
            }
        }
    }

    public boolean isWSControlEnable() {
        String controlWay = SharedPreferencesUtil.getInstance().get(KEY_GAME_CONTROL_WAY, GAME_CONTROL_TOUCH);
        return TextUtils.equals(controlWay, GAME_CONTROL_WHEEL);
    }

    public void addControlChangedListener(GameControlListener listener) {
        synchronized (this.mControlListeners) {
            if (!this.mControlListeners.contains(listener)) {
                this.mControlListeners.add(listener);
            }
        }
    }

    public void removeControlChangedListener(GameControlListener listener) {
        synchronized (this.mControlListeners) {
            if (this.mControlListeners.contains(listener)) {
                this.mControlListeners.remove(listener);
            }
        }
    }
}
