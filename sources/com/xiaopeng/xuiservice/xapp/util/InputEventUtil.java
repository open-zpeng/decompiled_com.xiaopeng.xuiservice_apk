package com.xiaopeng.xuiservice.xapp.util;

import android.app.Instrumentation;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.PointerIconCompat;
import com.xiaopeng.input.xpInputManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class InputEventUtil {
    public static final long INPUT_EVENT_DELAY = 50;
    public static final long INPUT_EVENT_SHORT_INTERVAL = 100;
    private static final String TAG = "InputEventUtil";
    private static Instrumentation mInstrumentation = new Instrumentation();

    public static void dispatchKey(int keyCode) {
        LogUtil.d(TAG, "dispatchKey:" + keyCode);
        sendDownAndUpKeyEvents(keyCode);
    }

    public static void dispatchKey(int keyCode, int displayId) {
        LogUtil.d(TAG, "dispatchKey:" + keyCode + " to display:" + displayId);
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            xpInputManager.setInputPolicy(displayId);
        }
        dispatchKey(keyCode);
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            xpInputManager.setInputPolicy(-1);
        }
    }

    public static void sendCarHomeKeyEvent(int displayId) {
        LogUtil.d(TAG, "sendCarHomeKeyEvent:" + displayId);
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            xpInputManager.setInputPolicy(-1);
        }
        long downTime = SystemClock.uptimeMillis();
        long upTime = SystemClock.uptimeMillis() + 100;
        int deviceId = displayId == 0 ? -2 : -3;
        KeyEvent downEvent = new KeyEvent(downTime, downTime + 50, 0, PointerIconCompat.TYPE_ZOOM_OUT, 0, 0, deviceId, 0, 64, 257);
        InputManager.getInstance().injectInputEvent(downEvent, 0);
        KeyEvent upEvent = new KeyEvent(upTime, upTime + 50, 1, PointerIconCompat.TYPE_ZOOM_OUT, 0, 0, deviceId, 0, 64, 257);
        InputManager.getInstance().injectInputEvent(upEvent, 0);
    }

    private static void sendDownAndUpKeyEvents(int keyCode) {
        long downTime = SystemClock.uptimeMillis();
        KeyEvent down = KeyEvent.obtain(downTime, downTime, 0, keyCode, 0, 0, -1, 0, 64, 257, null);
        InputManager.getInstance().injectInputEvent(down, 0);
        down.recycle();
        try {
            Thread.sleep(50L);
        } catch (Exception e) {
        }
        long upTime = SystemClock.uptimeMillis();
        KeyEvent up = KeyEvent.obtain(downTime, upTime, 1, keyCode, 0, 0, -1, 0, 64, 257, null);
        InputManager.getInstance().injectInputEvent(up, 0);
        up.recycle();
    }

    public static void dispatchTouchEvent(final int x, final int y) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.util.InputEventUtil.1
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d(InputEventUtil.TAG, "dispatchTouchEvent x:" + x + "&y:" + y);
                InputEventUtil.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float) x, (float) y, 0));
                InputEventUtil.mInstrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, (float) x, (float) y, 0));
            }
        });
    }

    public static void dispatchMotionEvent(float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent eventDown = MotionEvent.obtain(downTime, eventTime, 0, x, y, 0.0f, 0.0f, 0, x, y, -1, 0);
        eventDown.setSource(InputDeviceCompat.SOURCE_JOYSTICK);
        InputManager.getInstance().injectInputEvent(eventDown, 0);
        eventDown.recycle();
        long eventTime2 = SystemClock.uptimeMillis();
        MotionEvent eventMove = MotionEvent.obtain(downTime, eventTime2, 2, x, y, 0.0f, 0.0f, 0, x, y, -1, 0);
        eventMove.setSource(InputDeviceCompat.SOURCE_JOYSTICK);
        InputManager.getInstance().injectInputEvent(eventMove, 0);
        eventMove.recycle();
        long eventTime3 = SystemClock.uptimeMillis();
        MotionEvent eventUp = MotionEvent.obtain(downTime, eventTime3, 1, x, y, 0.0f, 0.0f, 0, x, y, -1, 0);
        eventUp.setSource(InputDeviceCompat.SOURCE_JOYSTICK);
        InputManager.getInstance().injectInputEvent(eventUp, 0);
        eventUp.recycle();
    }

    public static void dispatchJoystickMotionEvent(float axisX, float axisY) {
        MotionEvent.PointerProperties[] properties = {new MotionEvent.PointerProperties()};
        properties[0].id = -1;
        properties[0].toolType = 0;
        MotionEvent.PointerCoords[] pointerCoords = {new MotionEvent.PointerCoords()};
        pointerCoords[0].clear();
        pointerCoords[0].setAxisValue(0, axisX);
        pointerCoords[0].setAxisValue(1, axisY);
        MotionEvent event = MotionEvent.obtain(0L, SystemClock.uptimeMillis(), 2, 1, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, InputDeviceCompat.SOURCE_JOYSTICK, 0);
        InputManager.getInstance().injectInputEvent(event, 0);
        event.recycle();
    }
}
