package com.xiaopeng.xuiservice.xapp.mode.octopus;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine;
import com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper;
import com.xiaopeng.xuiservice.xapp.mode.octopus.HidDeviceManager;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class XAppOctopusModeEngine extends XAppBaseModeEngine {
    private static final String TAG = "XAppOctopusModeEngine";
    private ActivityManager mActivityManager;
    private ConfigRepository mConfigRepository;
    private EventProcessor mEventProcessor;
    HidDeviceManager.HidDeviceStateListener mHidDeviceStateListener;
    private KeyConfigManager mKeyConfigManager;
    XInputManagerWrapper.KeyEventListener mKeyEventListener;
    private XDialog mKeyMapDialog;
    private Map<String, String> mLastPackagePidMap;
    XInputManagerWrapper.MotionEventListener mMotionEventListener;

    public XAppOctopusModeEngine(Context context, Handler handler) {
        super(context, handler);
        this.mLastPackagePidMap = new HashMap();
        this.mKeyEventListener = new XInputManagerWrapper.KeyEventListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.XAppOctopusModeEngine.2
            @Override // com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper.KeyEventListener
            public void onKeyEvent(KeyEvent keyEvent) {
                XAppOctopusModeEngine.this.handleKeyEvent(keyEvent);
            }
        };
        this.mMotionEventListener = new XInputManagerWrapper.MotionEventListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.XAppOctopusModeEngine.3
            @Override // com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper.MotionEventListener
            public void onMotionEvent(MotionEvent motionEvent) {
                XAppOctopusModeEngine.this.handleMotionEvent(motionEvent);
            }
        };
        this.mHidDeviceStateListener = new HidDeviceManager.HidDeviceStateListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.XAppOctopusModeEngine.4
            @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.HidDeviceManager.HidDeviceStateListener
            public void onHidDeviceStateChanged(int oldState, int newState) {
                if (newState == 2) {
                    ToastUtil.showToast(XAppOctopusModeEngine.this.mContext, (int) R.string.hid_device_connected);
                } else if (newState == 0) {
                    ToastUtil.showToast(XAppOctopusModeEngine.this.mContext, (int) R.string.hid_device_disconnected);
                }
            }
        };
        if (XUIConfig.isFlyGameModeEnabled()) {
            this.mConfigRepository = ConfigRepository.getInstance(context);
            this.mKeyConfigManager = KeyConfigManager.getInstance();
            this.mKeyConfigManager.setupWithConfigRepository(this.mConfigRepository);
            this.mEventProcessor = EventProcessor.getInstance();
            this.mEventProcessor.setupConfigManager(this.mKeyConfigManager);
            HidDeviceManager.getInstance().init(context);
        }
        if (this.mActivityManager == null) {
            this.mActivityManager = (ActivityManager) this.mContext.getSystemService("activity");
        }
    }

    private String getPid(String pkgName) {
        List<ActivityManager.RunningAppProcessInfo> processInfos = this.mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.processName.equals(pkgName)) {
                int pid = processInfo.pid;
                return String.valueOf(pid);
            }
        }
        return String.valueOf(-1);
    }

    private boolean isNewApplication(String packageName) {
        String mLastPid = this.mLastPackagePidMap.get(packageName);
        String mCurrentPid = getPid(packageName);
        LogUtil.d(TAG, "packageName:" + packageName + "mLastPid:" + mLastPid + " &mCurrentPid:" + mCurrentPid);
        if (!TextUtils.isEmpty(mLastPid) && mCurrentPid.equals(mLastPid)) {
            return false;
        }
        this.mLastPackagePidMap.put(packageName, mCurrentPid);
        return true;
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected synchronized void handleAppInfoChange() {
        this.mEnable = isCurrentModeEnable();
        if (this.mEnable) {
            if (isNewApplication(this.mPkgName)) {
                showKeyMapDialog();
            }
        } else {
            dismissKeyMapDialog();
        }
        addOrRemoveEventListener(this.mEnable);
        if (this.mEnable && this.mEventProcessor != null) {
            this.mEventProcessor.onOctopusAppEnter(this.mPkgName);
        }
    }

    private void showHidDevicePrompt() {
    }

    private void addOrRemoveEventListener(boolean enable) {
        if (enable) {
            addEventListener();
        } else {
            removeEventListener();
        }
    }

    private void addEventListener() {
        HidDeviceManager.getInstance().registerListener(this.mHidDeviceStateListener);
        XInputManagerWrapper.getInstance().addKeyEventListener(this.mKeyEventListener);
        XInputManagerWrapper.getInstance().addMotionEventListener(this.mMotionEventListener);
    }

    private void removeEventListener() {
        HidDeviceManager.getInstance().unregisterListener(this.mHidDeviceStateListener);
        XInputManagerWrapper.getInstance().removeKeyEventListener(this.mKeyEventListener);
        XInputManagerWrapper.getInstance().removeMotionEventListener(this.mMotionEventListener);
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected boolean isCurrentModeEnable() {
        if (this.mPackageInfo == null) {
            return false;
        }
        LogUtil.d(TAG, "packageName:" + this.mPkgName + " &mPackageInfo.controlFlags:" + this.mPackageInfo.controlFlags);
        return (this.mPackageInfo.controlFlags & 4) != 0;
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected void handleEnableChange() {
    }

    public void onAppConfigUpload(String pkgName, String config) {
        KeyConfigManager keyConfigManager = this.mKeyConfigManager;
        if (keyConfigManager != null) {
            keyConfigManager.onAppConfigUpload(pkgName, config);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMotionEvent(MotionEvent motionEvent) {
        EventProcessor eventProcessor;
        if (this.mEnable && (eventProcessor = this.mEventProcessor) != null) {
            eventProcessor.processJoystickMotionEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyEvent(KeyEvent keyEvent) {
        EventProcessor eventProcessor;
        if (this.mEnable && (eventProcessor = this.mEventProcessor) != null) {
            eventProcessor.processKeyEvent(keyEvent);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeEventListener();
    }

    private void showKeyMapDialog() {
        if (this.mKeyMapDialog == null) {
            initKeyMapDialog();
        }
        XDialog xDialog = this.mKeyMapDialog;
        if (xDialog != null) {
            xDialog.show();
        }
    }

    private void dismissKeyMapDialog() {
        XDialog xDialog = this.mKeyMapDialog;
        if (xDialog != null) {
            xDialog.dismiss();
        }
    }

    private void initKeyMapDialog() {
        this.mKeyMapDialog = new XDialog(this.mContext, 2131690147).setTitle(R.string.hid_device_connected).setCustomView(R.layout.view_for_dialog_keymap, false).setPositiveButton(R.string.begin_game, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.XAppOctopusModeEngine.1
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public void onClick(XDialog dialog, int which) {
                dialog.dismiss();
            }
        });
        this.mKeyMapDialog.setSystemDialog(2008);
        this.mKeyMapDialog.getContentView().setBackgroundResource(R.drawable.bg_keymap_prompt);
    }
}
