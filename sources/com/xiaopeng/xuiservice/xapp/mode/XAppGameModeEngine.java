package com.xiaopeng.xuiservice.xapp.mode;

import android.app.ActivityManager;
import android.car.hardware.CarPropertyValue;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import androidx.core.view.PointerIconCompat;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.xapp.Config;
import com.xiaopeng.xuiservice.xapp.Global;
import com.xiaopeng.xuiservice.xapp.mode.XAppModeController;
import com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper;
import com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager;
import com.xiaopeng.xuiservice.xapp.util.GsonUtil;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class XAppGameModeEngine extends XAppBaseModeEngine {
    private static final String TAG = "XAppGameModeEngine";
    private boolean isUserSelectWheelControl;
    private ActivityManager mActivityManager;
    XInputManagerWrapper.CarEpsSteerEventListener mCarEpsSteerEventListener;
    private boolean mCarKeyControlEnable;
    XAppModeController.GameControlListener mGameControlListener;
    private GameHandler mGameHandler;
    private boolean mGameModeEnable;
    XInputManagerWrapper.KeyEventListener mKeyEventListener;
    private volatile Map<Integer, Integer> mKeyMaps;
    private Map<String, Integer> mKeyNameCodeMap;
    private volatile Map<Integer, Point> mKeyPointMaps;
    private Map<String, String> mLastPackagePidMap;
    protected LayoutInflater mLayoutInflater;
    private boolean mSteeringWheelControlEnable;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class GameHandler extends Handler {
        static final int MSG_EPS_EVENT = 2;
        static final int MSG_INIT = 0;
        static final int MSG_KEY_EVENT = 1;

        GameHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 0) {
                if (i == 1) {
                    XAppGameModeEngine.this.handleKeyEvent((KeyEvent) msg.obj);
                } else if (i == 2) {
                    XAppGameModeEngine.this.handleEpsSteerEvent((CarPropertyValue) msg.obj);
                }
            }
        }
    }

    public XAppGameModeEngine(Context context, Handler handler) {
        super(context, handler);
        this.mLastPackagePidMap = new HashMap();
        this.mKeyMaps = new HashMap();
        this.mKeyPointMaps = new HashMap();
        this.isUserSelectWheelControl = false;
        this.mGameModeEnable = false;
        this.mSteeringWheelControlEnable = false;
        this.mCarKeyControlEnable = false;
        this.mKeyEventListener = new XInputManagerWrapper.KeyEventListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppGameModeEngine.1
            @Override // com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper.KeyEventListener
            public void onKeyEvent(KeyEvent keyEvent) {
                XAppGameModeEngine.this.notifyKeyEvent(keyEvent);
            }
        };
        this.mCarEpsSteerEventListener = new XInputManagerWrapper.CarEpsSteerEventListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppGameModeEngine.2
            @Override // com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper.CarEpsSteerEventListener
            public void onEpsSteerEvent(CarPropertyValue carPropertyValue) {
                XAppGameModeEngine.this.notifyEpsSteerEvent(carPropertyValue);
            }
        };
        this.mGameControlListener = new XAppModeController.GameControlListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppGameModeEngine.3
            @Override // com.xiaopeng.xuiservice.xapp.mode.XAppModeController.GameControlListener
            public void onControlWayChanged(boolean useWheelSteering) {
                XAppGameModeEngine.this.isUserSelectWheelControl = useWheelSteering;
            }
        };
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mGameHandler = new GameHandler(handler.getLooper());
        if (this.mActivityManager == null) {
            this.mActivityManager = (ActivityManager) this.mContext.getSystemService("activity");
        }
        initKeyNameCodeMap();
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected synchronized void handleAppInfoChange() {
        this.mGameModeEnable = isCurrentModeEnable();
        this.mSteeringWheelControlEnable = isSteeringWheelControlEnable();
        this.mCarKeyControlEnable = isCarKeyControlEnable();
        this.isUserSelectWheelControl = XAppModeController.getInstance().isWSControlEnable();
        setupEventListener();
        setupInputEventMode();
        showGamePrompt();
        loadKeyMaps();
    }

    private void initKeyNameCodeMap() {
        this.mKeyNameCodeMap = new HashMap();
        this.mKeyNameCodeMap.put("KEYCODE_ICM_UP", 1000);
        this.mKeyNameCodeMap.put("KEYCODE_ICM_DOWN", 1001);
        this.mKeyNameCodeMap.put("KEYCODE_ICM_LEFT", 1002);
        this.mKeyNameCodeMap.put("KEYCODE_ICM_RIGHT", 1003);
        this.mKeyNameCodeMap.put("KEYCODE_CAR_VOLUME_UP", Integer.valueOf((int) PointerIconCompat.TYPE_COPY));
        this.mKeyNameCodeMap.put("KEYCODE_CAR_VOLUME_DOWN", Integer.valueOf((int) PointerIconCompat.TYPE_NO_DROP));
        this.mKeyNameCodeMap.put("KEYCODE_CAR_MEDIA_PREVIOUS", Integer.valueOf((int) PointerIconCompat.TYPE_ALL_SCROLL));
        this.mKeyNameCodeMap.put("KEYCODE_CAR_MEDIA_NEXT", Integer.valueOf((int) PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW));
    }

    private void setupInputEventMode() {
        Global.setGameModeEnable(this.mGameModeEnable);
        setInputDispatchMode(this.mGameModeEnable);
    }

    private void showGamePrompt() {
        if (this.mGameModeEnable) {
            if (isNewApplication(this.mPkgName)) {
                XAppModeViewManager.getInstance().showGameDialog(this.mPackageInfo);
                return;
            }
            return;
        }
        XAppModeViewManager.getInstance().dismissGameDialog();
    }

    private void setupEventListener() {
        if (this.mGameModeEnable) {
            XAppModeController.getInstance().addControlChangedListener(this.mGameControlListener);
        } else {
            XAppModeController.getInstance().removeControlChangedListener(this.mGameControlListener);
        }
        if (this.mSteeringWheelControlEnable) {
            XInputManagerWrapper.getInstance().addEpsSteerEventListener(this.mCarEpsSteerEventListener);
        } else {
            XInputManagerWrapper.getInstance().removeEpsSteerEventListener(this.mCarEpsSteerEventListener);
        }
        if (this.mCarKeyControlEnable) {
            XInputManagerWrapper.getInstance().addKeyEventListener(this.mKeyEventListener);
        } else {
            XInputManagerWrapper.getInstance().removeKeyEventListener(this.mKeyEventListener);
        }
    }

    private void loadKeyMaps() {
        if (this.mCarKeyControlEnable && this.mPackageInfo != null && !TextUtils.isEmpty(this.mPackageInfo.keyEvent)) {
            this.mKeyMaps.clear();
            Map<String, Object> list = GsonUtil.fromJson2Map(this.mPackageInfo.keyEvent);
            for (String keyName : list.keySet()) {
                String mapCode = (String) list.get(keyName);
                if (mapCode.startsWith("{")) {
                    this.mKeyPointMaps.put(Integer.valueOf(getKeyCodeFromString(keyName)), getPointFromArrayString(mapCode));
                } else {
                    this.mKeyMaps.put(Integer.valueOf(getKeyCodeFromString(keyName)), Integer.valueOf(mapCode));
                }
            }
        }
    }

    private int getKeyCodeFromString(String keyName) {
        if (this.mKeyNameCodeMap.containsKey(keyName)) {
            return this.mKeyNameCodeMap.get(keyName).intValue();
        }
        return -1;
    }

    private Point getPointFromArrayString(String value) {
        String newValue = value.substring(1, value.length() - 1);
        String[] array = newValue.split(",");
        return new Point(Integer.valueOf(array[0]).intValue(), Integer.valueOf(array[1]).intValue());
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected boolean isCurrentModeEnable() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.versionCode < 14 ? this.mPackageInfo.executeMode == 1 : isSupportFlag(1) && isSupportFlag(2);
        }
        return false;
    }

    private boolean isSteeringWheelControlEnable() {
        if (this.mPackageInfo == null) {
            return false;
        }
        return this.mPackageInfo.versionCode < 14 ? this.mPackageInfo.executeMode == 1 : (this.mPackageInfo.controlFlags & 1) == 1;
    }

    private boolean isSupportFlag(int flag) {
        return (this.mPackageInfo.controlFlags & flag) != 0;
    }

    private boolean isCarKeyControlEnable() {
        if (this.mPackageInfo == null) {
            return false;
        }
        return this.mPackageInfo.versionCode < 14 ? this.mPackageInfo.executeMode == 1 : (this.mPackageInfo.controlFlags & 2) == 2;
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected void handleEnableChange() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyKeyEvent(KeyEvent keyEvent) {
        if (isNeedKeyEvent(keyEvent)) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = keyEvent;
            this.mGameHandler.sendMessage(msg);
        }
    }

    private boolean isNeedKeyEvent(KeyEvent keyEvent) {
        return this.mCarKeyControlEnable && this.isUserSelectWheelControl && isHasKeyMap(keyEvent.getKeyCode());
    }

    private boolean isHasKeyMap(int keyCode) {
        return this.mKeyMaps.containsKey(Integer.valueOf(keyCode)) || this.mKeyPointMaps.containsKey(Integer.valueOf(keyCode));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyEvent(KeyEvent keyEvent) {
        LogUtil.v(TAG, "old keyEvent : " + keyEvent);
        if ((keyEvent.getSource() & 2) != 0 && keyEvent.getDeviceId() == -1) {
            return;
        }
        int oldCode = keyEvent.getKeyCode();
        if (isDownAction(keyEvent)) {
            if (this.mKeyPointMaps.containsKey(Integer.valueOf(oldCode))) {
                Point point = this.mKeyPointMaps.get(Integer.valueOf(oldCode));
                InputEventUtil.dispatchTouchEvent(point.x, point.y);
            } else if (this.mKeyMaps.containsKey(Integer.valueOf(oldCode))) {
                int newCode = this.mKeyMaps.get(Integer.valueOf(oldCode)).intValue();
                InputEventUtil.dispatchKey(newCode);
                LogUtil.v(TAG, "new tranKey : " + newCode);
            }
        }
    }

    private boolean isDownAction(KeyEvent event) {
        if (event.getAction() == 0) {
            return true;
        }
        return false;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        XInputManagerWrapper.getInstance().removeEpsSteerEventListener(this.mCarEpsSteerEventListener);
        XInputManagerWrapper.getInstance().removeKeyEventListener(this.mKeyEventListener);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEpsSteerEvent(CarPropertyValue carPropertyValue) {
        if (isNeedEpsEvent() && carPropertyValue != null) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = carPropertyValue;
            this.mGameHandler.sendMessage(msg);
        }
    }

    private boolean isNeedEpsEvent() {
        return this.mSteeringWheelControlEnable && this.isUserSelectWheelControl;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEpsSteerEvent(CarPropertyValue carPropertyValue) {
        float angle = (((Float) carPropertyValue.getValue()).floatValue() / Config.GAME_ANGLE_M) * 90.0f;
        float x = 0.001f;
        try {
            int epsType = XUIConfig.getEpsType();
            if (epsType == 0) {
                x = (float) Math.cos(((90.0f + angle) * 3.141592653589793d) / 180.0d);
            } else if (epsType == 1) {
                x = (float) Math.sin((angle * 3.141592653589793d) / 180.0d);
            }
            InputEventUtil.dispatchMotionEvent(x, 0.001f);
            LogUtil.v(TAG, "handleMotionEvent angle: " + angle + ", x: " + x + ", y: 0.001");
        } catch (Exception e) {
        }
    }

    private void setInputDispatchMode(boolean enable) {
        xpPackageInfo.setXpGameModeEnable(enable);
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
}
