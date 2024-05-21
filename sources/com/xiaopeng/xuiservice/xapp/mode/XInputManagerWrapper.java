package com.xiaopeng.xuiservice.xapp.mode;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.eps.CarEpsManager;
import android.hardware.input.InputManager;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.xiaopeng.IXPKeyListener;
import com.xiaopeng.IXPMotionListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes5.dex */
public class XInputManagerWrapper {
    private static final String TAG = "XInputManagerWrapper";
    private static volatile XInputManagerWrapper sXInputManagerWrapper;
    private CopyOnWriteArrayList<KeyEventListener> mKeyEventListeners = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<MotionEventListener> mMotionEventListeners = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<CarEpsSteerEventListener> mCarEpsSteerEventListeners = new CopyOnWriteArrayList<>();
    private CarEpsManager.CarEpsEventCallback mCarEpsEventCallback = new CarEpsManager.CarEpsEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.mode.XInputManagerWrapper.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            if (carPropertyValue.getPropertyId() == 559948806) {
                XInputManagerWrapper.this.notifyEpsSteerEvent(carPropertyValue);
            }
        }

        public void onErrorEvent(int i, int i1) {
        }
    };

    /* loaded from: classes5.dex */
    public interface CarEpsSteerEventListener {
        void onEpsSteerEvent(CarPropertyValue carPropertyValue);
    }

    /* loaded from: classes5.dex */
    public interface KeyEventListener {
        void onKeyEvent(KeyEvent keyEvent);
    }

    /* loaded from: classes5.dex */
    public interface MotionEventListener {
        void onMotionEvent(MotionEvent motionEvent);
    }

    private XInputManagerWrapper() {
    }

    public static XInputManagerWrapper getInstance() {
        if (sXInputManagerWrapper == null) {
            synchronized (XInputManagerWrapper.class) {
                if (sXInputManagerWrapper == null) {
                    sXInputManagerWrapper = new XInputManagerWrapper();
                }
            }
        }
        return sXInputManagerWrapper;
    }

    public void addMotionEventListener(MotionEventListener listener) {
        if (this.mMotionEventListeners.isEmpty()) {
            InputManager.getInstance().registerMotionListener(new XPMotionListener(), "xp.key.gamemode.flag", true);
        }
        if (!this.mMotionEventListeners.contains(listener)) {
            this.mMotionEventListeners.add(listener);
        }
    }

    public void removeMotionEventListener(MotionEventListener listener) {
        if (this.mMotionEventListeners.contains(listener)) {
            this.mMotionEventListeners.remove(listener);
        }
        this.mMotionEventListeners.isEmpty();
    }

    public void addKeyEventListener(KeyEventListener listener) {
        LogUtil.d(TAG, "addKeyEventListener:" + listener);
        if (this.mKeyEventListeners.isEmpty()) {
            InputManager.getInstance().registerListener(new XPKeyListener(), "xp.key.gamemode.flag", true);
        }
        if (!this.mKeyEventListeners.contains(listener)) {
            this.mKeyEventListeners.add(listener);
        }
    }

    public void removeKeyEventListener(KeyEventListener listener) {
        LogUtil.d(TAG, "removeKeyEventListener:" + listener);
        if (this.mKeyEventListeners.contains(listener)) {
            this.mKeyEventListeners.remove(listener);
        }
        this.mKeyEventListeners.isEmpty();
    }

    public void addEpsSteerEventListener(CarEpsSteerEventListener listener) {
        if (this.mCarEpsSteerEventListeners.isEmpty()) {
            CarClientManager.getInstance().addEpsManagerListener(this.mCarEpsEventCallback);
        }
        if (!this.mCarEpsSteerEventListeners.contains(listener)) {
            this.mCarEpsSteerEventListeners.add(listener);
        }
    }

    public void removeEpsSteerEventListener(CarEpsSteerEventListener listener) {
        if (this.mCarEpsSteerEventListeners.contains(listener)) {
            this.mCarEpsSteerEventListeners.remove(listener);
        }
        if (this.mCarEpsSteerEventListeners.isEmpty()) {
            CarClientManager.getInstance().removeEpsManagerListener(this.mCarEpsEventCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyKeyEvent(KeyEvent keyEvent) {
        if (this.mKeyEventListeners.isEmpty()) {
            return;
        }
        Iterator<KeyEventListener> it = this.mKeyEventListeners.iterator();
        while (it.hasNext()) {
            KeyEventListener listener = it.next();
            listener.onKeyEvent(keyEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyJoystickMotionEvent(MotionEvent motionEvent) {
        if (this.mMotionEventListeners.isEmpty()) {
            return;
        }
        Iterator<MotionEventListener> it = this.mMotionEventListeners.iterator();
        while (it.hasNext()) {
            MotionEventListener listener = it.next();
            listener.onMotionEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEpsSteerEvent(CarPropertyValue carPropertyValue) {
        if (this.mCarEpsSteerEventListeners.isEmpty()) {
            return;
        }
        Iterator<CarEpsSteerEventListener> it = this.mCarEpsSteerEventListeners.iterator();
        while (it.hasNext()) {
            CarEpsSteerEventListener listener = it.next();
            listener.onEpsSteerEvent(carPropertyValue);
        }
    }

    /* loaded from: classes5.dex */
    private class XPKeyListener extends IXPKeyListener.Stub {
        private XPKeyListener() {
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
            WatchDog.getInstance().binderEnter("XPKeyListener", info);
            try {
                try {
                    boolean ret = super.onTransact(code, data, reply, flags);
                    return ret;
                } catch (RemoteException e) {
                    LogUtil.w(XInputManagerWrapper.TAG, "onTransact e=" + e + ",code=" + code);
                    throw e;
                }
            } finally {
                WatchDog.getInstance().binderLeave("XPKeyListener", info);
            }
        }

        public int notify(KeyEvent event, String extra) throws RemoteException {
            XInputManagerWrapper.this.notifyKeyEvent(event);
            return 0;
        }
    }

    /* loaded from: classes5.dex */
    private class XPMotionListener extends IXPMotionListener.Stub {
        private XPMotionListener() {
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
            WatchDog.getInstance().binderEnter("XPMotionListener", info);
            try {
                try {
                    boolean ret = super.onTransact(code, data, reply, flags);
                    return ret;
                } catch (RemoteException e) {
                    LogUtil.w(XInputManagerWrapper.TAG, "onTransact e=" + e + ",code=" + code);
                    throw e;
                }
            } finally {
                WatchDog.getInstance().binderLeave("XPMotionListener", info);
            }
        }

        public int notify(MotionEvent event, String extra) throws RemoteException {
            XInputManagerWrapper.this.notifyJoystickMotionEvent(event);
            return 0;
        }
    }
}
