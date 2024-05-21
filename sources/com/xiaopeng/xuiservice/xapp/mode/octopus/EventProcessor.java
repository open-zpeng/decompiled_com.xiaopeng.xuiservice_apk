package com.xiaopeng.xuiservice.xapp.mode.octopus;

import android.app.Instrumentation;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.core.view.InputDeviceCompat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.EventMap;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigLoaderCallback;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfig;
import com.xiaopeng.xuiservice.xapp.mode.octopus.util.ParserUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes5.dex */
public class EventProcessor {
    private static final int DEFAULT_MOVE_DELTA = 30;
    private static final int MSG_CONFIG_UPDATED = 20;
    private static final int MSG_CONGIFS_CHANGED = 19;
    private static final int MSG_JOYSTICK_MOTION_EVENT = 18;
    private static final int MSG_KEY_EVENT = 17;
    private static final String TAG = "EventProcessor";
    private static volatile EventProcessor mInstance;
    private List<KeyConfig> mCurrentKeyConfigs;
    private String mCurrentPkgName;
    private boolean mJoystickEnable;
    private KeyConfigManager mKeyConfigManager;
    private Rect mNaviRect;
    private Handler mWorkHandler;
    private volatile boolean mJoystickDowned = false;
    private SparseArray<Point> mCurrentAppKeyArray = new SparseArray<>();
    private HashMap<String, SparseArray<Point>> mStringSparseArrayHashMap = new HashMap<>();
    private Instrumentation mInstrumentation = new Instrumentation();
    private HandlerThread mHandlerThread = new HandlerThread(TAG);

    private EventProcessor() {
        this.mHandlerThread.start();
        this.mWorkHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.EventProcessor.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 17:
                        EventProcessor.this.processKeyCodeInner(msg.arg1, (KeyEvent) msg.obj);
                        return;
                    case 18:
                        EventProcessor.this.processJoystickMotionEventInner((MotionEvent) msg.obj);
                        return;
                    case 19:
                        EventProcessor.this.processConfigsChanged((KeyConfigsWrapper) msg.obj);
                        return;
                    case 20:
                        EventProcessor.this.processConfigUpdated((KeyConfig) msg.obj);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public static EventProcessor getInstance() {
        if (mInstance == null) {
            synchronized (EventProcessor.class) {
                if (mInstance == null) {
                    mInstance = new EventProcessor();
                }
            }
        }
        return mInstance;
    }

    public void setupConfigManager(KeyConfigManager configManager) {
        this.mKeyConfigManager = configManager;
    }

    public void onOctopusAppEnter(final String pkgName) {
        LogUtil.d(TAG, "onOctopusAppEnter: " + pkgName);
        if (!pkgName.equals(this.mCurrentPkgName)) {
            this.mCurrentPkgName = pkgName;
            this.mKeyConfigManager.getKeyConfigs(pkgName, new ConfigLoaderCallback() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.EventProcessor.2
                @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigLoaderCallback
                public void onConfigLoaded(List<KeyConfig> configs) {
                    if (configs == null || configs.size() == 0) {
                        configs = EventProcessor.this.mKeyConfigManager.getDefaultKeyConfigList();
                    }
                    if (configs != null && configs.size() >= 1) {
                        Message.obtain(EventProcessor.this.mWorkHandler, 19, new KeyConfigsWrapper(pkgName, configs)).sendToTarget();
                    }
                }
            });
        }
    }

    public void onUserUpdateConfig(KeyConfig keyConfig) {
        Message.obtain(this.mWorkHandler, 20, keyConfig).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processConfigUpdated(KeyConfig keyConfig) {
        EventMap eventMap = ParserUtil.parseKeyConfig(keyConfig);
        this.mNaviRect = eventMap.getNaviTouchArea();
        this.mCurrentAppKeyArray = eventMap.getSelectKeyMap();
        for (KeyConfig config : this.mCurrentKeyConfigs) {
            if (config.configName.equals(keyConfig.configName)) {
                config.configInfo = keyConfig.configInfo;
                config.selected = 1;
            } else {
                config.selected = 0;
            }
            KeyConfigManager.getInstance().saveConfig(config);
        }
    }

    public void processKeyEvent(KeyEvent keyEvent) {
        if (isDirectionKeyEvent(keyEvent)) {
            return;
        }
        LogUtil.d(TAG, "processKeyEvent: " + keyEvent);
        if (!isDirectionKeyEvent(keyEvent) && keyEvent.getAction() != 0) {
            return;
        }
        Message message = Message.obtain();
        message.what = 17;
        message.arg1 = keyEvent.getKeyCode();
        message.obj = keyEvent;
        this.mWorkHandler.sendMessage(message);
    }

    public void processJoystickMotionEvent(MotionEvent motionEvent) {
        Message message = Message.obtain();
        message.what = 18;
        message.obj = motionEvent;
        this.mWorkHandler.sendMessage(message);
    }

    public List<KeyConfig> getCurrentKeyConfigs() {
        return this.mCurrentKeyConfigs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processConfigsChanged(KeyConfigsWrapper keyConfigsWrapper) {
        LogUtil.d(TAG, "processConfigsChanged mCurrentPkgName:" + this.mCurrentPkgName + "  &configPkgName:" + keyConfigsWrapper.mPkgName);
        if (this.mCurrentPkgName.equals(keyConfigsWrapper.mPkgName)) {
            this.mCurrentKeyConfigs = keyConfigsWrapper.mKeyConfigs;
            KeyConfig selectConfig = null;
            Iterator<KeyConfig> it = keyConfigsWrapper.mKeyConfigs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                KeyConfig config = it.next();
                if (config.selected == 1) {
                    selectConfig = config;
                    break;
                }
            }
            if (selectConfig == null && keyConfigsWrapper.mKeyConfigs.size() > 0) {
                KeyConfig selectConfig2 = keyConfigsWrapper.mKeyConfigs.get(0);
                selectConfig = selectConfig2;
            }
            processKeyConfig(selectConfig);
        }
    }

    private void processKeyConfig(KeyConfig keyConfig) {
        if (keyConfig != null) {
            EventMap eventMap = ParserUtil.parseKeyConfig(keyConfig);
            this.mNaviRect = eventMap.getNaviTouchArea();
            this.mCurrentAppKeyArray = eventMap.getSelectKeyMap();
            LogUtil.d(TAG, "processKeyEvent mNaviRect:" + this.mNaviRect.toString() + " &mCurrentAppKeyArray:" + this.mCurrentAppKeyArray.toString());
        }
    }

    private void processJoystickMotionEventAxis(MotionEvent event) {
        if ((event.getSource() & InputDeviceCompat.SOURCE_JOYSTICK) == 16777232 && event.getAction() == 2) {
            int id = event.getDeviceId();
            if (-1 != id) {
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    processJoystickInput(event, i);
                }
                processJoystickInput(event, -1);
            }
        }
    }

    private void processJoystickInput(MotionEvent event, int historyPos) {
        InputDevice inputDevice = event.getDevice();
        if (inputDevice == null) {
            return;
        }
        float x = getCenteredAxis(event, inputDevice, 0, historyPos);
        if (x == 0.0f) {
            x = getCenteredAxis(event, inputDevice, 15, historyPos);
        }
        if (x == 0.0f) {
            x = getCenteredAxis(event, inputDevice, 11, historyPos);
        }
        float y = getCenteredAxis(event, inputDevice, 1, historyPos);
        if (y == 0.0f) {
            y = getCenteredAxis(event, inputDevice, 16, historyPos);
        }
        if (y == 0.0f) {
            y = getCenteredAxis(event, inputDevice, 14, historyPos);
        }
        mockTouchEventWithAxis(x, y);
    }

    private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
        InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());
        if (range != null) {
            float flat = range.getFlat();
            float value = historyPos < 0 ? event.getAxisValue(axis) : event.getHistoricalAxisValue(axis, historyPos);
            if (Math.abs(value) > flat) {
                return value;
            }
            return 0.0f;
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processJoystickMotionEventInner(MotionEvent event) {
        processJoystickMotionEventAxis(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processKeyCodeInner(int keycode, KeyEvent keyEvent) {
        SparseArray<Point> sparseArray = this.mCurrentAppKeyArray;
        if (sparseArray != null) {
            Point point = sparseArray.get(keycode);
            if (point != null) {
                mockTouchEvent(point, keyEvent);
                return;
            }
            LogUtil.d(TAG, "the point is null with keycode: " + keycode);
            return;
        }
        LogUtil.d(TAG, "mCurrentAppKeyArray is null");
    }

    private void mockTouchEventWithAxis(float axisX, float axisY) {
        LogUtil.d(TAG, "mockTouchEventWithAxis axisX:" + axisX + " &axisY:" + axisY);
        Rect rect = this.mNaviRect;
        if (rect == null) {
            return;
        }
        int width = rect.width();
        int height = this.mNaviRect.height();
        int centerX = this.mNaviRect.centerX();
        int centerY = this.mNaviRect.centerY();
        int deltaX = (int) ((width * axisX) / 2.0f);
        int deltaY = (int) ((height * axisY) / 2.0f);
        int motionX = centerX + deltaX;
        int motionY = centerY + deltaY;
        if (axisX == 0.0f && axisX == 0.0f) {
            try {
                this.mJoystickDowned = false;
                this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, motionX, motionY, 0));
            } catch (Exception e) {
                LogUtil.d(TAG, "mockTouchEventWithAxis exception:" + e.toString());
                return;
            }
        }
        if (!this.mJoystickDowned) {
            this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, centerX + deltaX, centerY + deltaY, 0));
            this.mJoystickDowned = true;
        }
        this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 2, motionX, motionY, 0));
    }

    private void postMultiTouch(Point first, Point second) {
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        pointerCoords.x = first.x;
        pointerCoords.y = first.y;
        MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, 1, new int[]{0}, coords, 0, 0.0f, 0.0f, 0, 0, 0, 0);
        this.mInstrumentation.sendPointerSync(event);
        MotionEvent.PointerCoords pointerCoords2 = new MotionEvent.PointerCoords();
        pointerCoords2.x = second.x;
        pointerCoords2.y = second.y;
        MotionEvent.PointerCoords[] coords = {pointerCoords, pointerCoords2};
        MotionEvent event2 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 5, 2, new int[]{0, 1}, coords, 0, 0.0f, 0.0f, 0, 0, 0, 0);
        this.mInstrumentation.sendPointerSync(event2);
        int delta = ViewConfiguration.getTouchSlop();
        MotionEvent.PointerCoords pointerCoords3 = new MotionEvent.PointerCoords();
        pointerCoords3.x = first.x + delta;
        pointerCoords3.y = first.y + delta;
        coords[0] = pointerCoords3;
        MotionEvent event3 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 2, 1, new int[]{0}, coords, 0, 0.0f, 0.0f, 0, 0, 0, 0);
        this.mInstrumentation.sendPointerSync(event3);
    }

    private void mockTouchEvent(Point point, KeyEvent keyEvent) {
        if (isDirectionKeyEvent(keyEvent)) {
            mockDirectionTouchEvent(point, keyEvent);
        } else {
            mockNormalTouchEvent(point);
        }
    }

    private void mockNormalTouchEvent(Point point) {
        this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, point.x, point.y, 0));
        this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, point.x, point.y, 0));
    }

    private void mockDirectionTouchEvent(Point point, KeyEvent keyEvent) {
        LogUtil.i(TAG, "mockDirectionTouchEvent: " + keyEvent);
        int repeatCount = keyEvent.getRepeatCount();
        int action = keyEvent.getAction();
        int[] deltas = getMoveDeltaValue(keyEvent);
        if (action != 0) {
            if (action == 1) {
                this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, point.x, point.y, 0));
            }
        } else if (repeatCount == 0) {
            this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, point.x, point.y, 0));
            this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 2, point.x + deltas[0], point.y + deltas[1], 0));
        } else if (repeatCount > 0) {
            this.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 2, point.x + deltas[0], point.y + deltas[1], 0));
        }
    }

    public int[] getMoveDeltaValue(KeyEvent keyEvent) {
        int[] deltas = new int[2];
        switch (keyEvent.getKeyCode()) {
            case 19:
                deltas[0] = 0;
                deltas[1] = -30;
                break;
            case 20:
                deltas[0] = 0;
                deltas[1] = 30;
                break;
            case 21:
                deltas[0] = -30;
                deltas[1] = 0;
                break;
            case 22:
                deltas[0] = 30;
                deltas[1] = 0;
                break;
        }
        return deltas;
    }

    private boolean isDirectionKeyEvent(KeyEvent keyEvent) {
        int keycode = keyEvent.getKeyCode();
        return keycode == 21 || keycode == 22 || keycode == 19 || keycode == 20;
    }

    /* loaded from: classes5.dex */
    public class KeyConfigsWrapper {
        public List<KeyConfig> mKeyConfigs;
        public String mPkgName;

        public KeyConfigsWrapper(String pkgName, List<KeyConfig> configs) {
            this.mPkgName = pkgName;
            this.mKeyConfigs = configs;
        }
    }

    public boolean isJoystickEnable() {
        return this.mJoystickEnable;
    }

    public void setJoystickEnable(boolean enable) {
        this.mJoystickEnable = enable;
    }
}
