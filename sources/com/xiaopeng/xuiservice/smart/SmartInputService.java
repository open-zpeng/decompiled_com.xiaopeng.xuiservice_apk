package com.xiaopeng.xuiservice.smart;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.input.CarInputManager;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.SparseArray;
import android.view.KeyEvent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartInputService extends BaseSmartService {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.xp.input.logger", false);
    private static final int INVALID_KEYCODE = -1;
    public static final int KEYCODE_COMBINED_LOGGER = 1210;
    private static final int KEY_PRESSED = 1;
    private static final int KEY_TOUCHED = 1;
    private static final int MSG_SEND_LEFT_KEY_DOWN_EVENT = 2;
    private static final int MSG_SEND_LEFT_KEY_UP_EVENT = 1;
    private static final int MSG_SEND_RIGHT_KEY_DOWN_EVENT = 4;
    private static final int MSG_SEND_RIGHT_KEY_UP_EVENT = 3;
    private static final long POST_KEYEVENT_DELAY = 100;
    private int currentLeftTouchKeyCode;
    private int currentRightTouchKeyCode;
    private boolean[] keyStatus;
    private int latestLeftTouchKeyCode;
    private int latestRightTouchKeyCode;
    private CarInputManager.CarInputEventCallback mCarInputEventCallback;
    private KeyHandlerThread mKeyHandlerThread;
    private SparseArray<Integer> mRawDataKeyArray;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartInputService(Context context) {
        super(context);
        this.latestLeftTouchKeyCode = -1;
        this.latestRightTouchKeyCode = -1;
        this.currentLeftTouchKeyCode = -1;
        this.currentRightTouchKeyCode = -1;
        this.mRawDataKeyArray = new SparseArray<>();
        this.keyStatus = new boolean[8];
        this.mKeyHandlerThread = new KeyHandlerThread();
        initRawDataKeyArray();
        this.mKeyHandlerThread.start();
    }

    public static SmartInputService getInstance() {
        return InstanceHolder.sService;
    }

    private void initRawDataKeyArray() {
        this.mRawDataKeyArray.put(557851186, 1222);
        this.mRawDataKeyArray.put(557851187, 1223);
        this.mRawDataKeyArray.put(557851189, 1227);
        this.mRawDataKeyArray.put(557851188, 1226);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarInputEventCallback = new CarInputManager.CarInputEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartInputService.1
            public void onChangeEvent(CarPropertyValue value) {
                SmartInputService.this.handleInputEvent(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartInputService.this.TAG, "CarInputManager.CarInputEventCallback onErrorEvent");
            }
        };
        addInputManagerListener(this.mCarInputEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(this.TAG, "onCarManagerInited");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInputEvent(CarPropertyValue value) {
        LogUtil.d("SmartInputService", "handleInputEvent value:" + value.toString());
        int propertyId = value.getPropertyId();
        if (propertyId != 557851176) {
            switch (propertyId) {
                case 557851186:
                case 557851187:
                    int pressed = ((Integer) value.getValue()).intValue();
                    int keycode = this.mRawDataKeyArray.get(value.getPropertyId()).intValue();
                    if (pressed == 1) {
                        this.latestLeftTouchKeyCode = keycode;
                        postKeyEvent(keycode, 2, 100L);
                        return;
                    } else if (pressed == 0 && keycode == this.latestLeftTouchKeyCode) {
                        this.latestLeftTouchKeyCode = -1;
                        boolean isHasDownEvent = isQueueHaveDownEvent(2);
                        removeKeyEvent(2);
                        if (isHasDownEvent) {
                            sendKeyEvent(keycode, 0);
                        }
                        sendKeyEvent(keycode, 1);
                        return;
                    } else {
                        return;
                    }
                case 557851188:
                case 557851189:
                    int pressed2 = ((Integer) value.getValue()).intValue();
                    int keycode2 = this.mRawDataKeyArray.get(value.getPropertyId()).intValue();
                    if (pressed2 == 1) {
                        this.latestRightTouchKeyCode = keycode2;
                        postKeyEvent(keycode2, 4, 100L);
                        return;
                    } else if (pressed2 == 0 && keycode2 == this.latestRightTouchKeyCode) {
                        this.latestRightTouchKeyCode = -1;
                        boolean isHasDownEvent2 = isQueueHaveDownEvent(4);
                        removeKeyEvent(4);
                        if (isHasDownEvent2) {
                            sendKeyEvent(keycode2, 0);
                        }
                        sendKeyEvent(keycode2, 1);
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
        sendKeyEvent(KEYCODE_COMBINED_LOGGER, 0);
        sendKeyEvent(KEYCODE_COMBINED_LOGGER, 1);
    }

    private void postKeyEvent(int keycode, int what, long delay) {
        removeEvent(what);
        handleEvent(what, keycode, delay);
    }

    private void removeKeyEvent(int what) {
        removeEvent(what);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendKeyEvent(int keycode, int action) {
        injectInputEvent(keycode, action, SystemClock.uptimeMillis());
    }

    private void injectInputEvent(int keycode, int action, long time) {
        String str = this.TAG;
        LogUtil.d(str, "injectInputEvent really keycode:" + keycode + " &action:" + action);
        KeyEvent down = KeyEvent.obtain(time, time, action, keycode, 0, 0, -1, 0, 8, 257, null);
        InputManager.getInstance().injectInputEvent(down, 0);
        down.recycle();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        this.mKeyHandlerThread.quit();
        removeInputManagerListener(this.mCarInputEventCallback);
    }

    private void handleEvent(int what, int keyCode, long delayTime) {
        Message message = this.mKeyHandlerThread.getHandler().obtainMessage();
        message.what = what;
        message.obj = Integer.valueOf(keyCode);
        this.mKeyHandlerThread.getHandler().sendMessageDelayed(message, delayTime);
    }

    private void removeEvent(int what) {
        this.mKeyHandlerThread.getHandler().removeMessages(what);
    }

    private boolean isQueueHaveDownEvent(int what) {
        return this.mKeyHandlerThread.getHandler().hasMessages(what);
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartInputService sService = new SmartInputService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class KeyHandlerThread extends HandlerThread {
        private static final String TAG = "KeyHandlerThread";
        private Handler handler;

        public KeyHandlerThread() {
            super(TAG, 10);
        }

        @Override // android.os.HandlerThread
        @SuppressLint({"HandlerLeak"})
        protected void onLooperPrepared() {
            this.handler = new Handler() { // from class: com.xiaopeng.xuiservice.smart.SmartInputService.KeyHandlerThread.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    int i = msg.what;
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 3) {
                                if (i != 4) {
                                    return;
                                }
                            }
                        }
                        SmartInputService.this.sendKeyEvent(((Integer) msg.obj).intValue(), 0);
                        return;
                    }
                    SmartInputService.this.sendKeyEvent(((Integer) msg.obj).intValue(), 1);
                }
            };
        }

        public Handler getHandler() {
            return this.handler;
        }
    }
}
