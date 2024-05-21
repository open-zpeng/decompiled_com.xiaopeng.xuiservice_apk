package com.xiaopeng.xuiservice.xapp.mode.octopus;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class SyntheticJoystickHandler {
    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;
    private static final int KEY_REPEAT_DELAY = 50;
    private static final String TAG = "SyntheticJoystickHandler";
    private static volatile SyntheticJoystickHandler mSyntheticJoystickHandler;
    private HandlerThread mHandlerThread = new HandlerThread("JoystickHandlerThread");
    private JoystickHandler mJoystickHandler;
    private OnJoystickNavigationEvent mOnJoystickNavigationEventListener;

    /* loaded from: classes5.dex */
    public interface OnJoystickNavigationEvent {
        void onJoystickNavigationEvent(KeyEvent keyEvent);
    }

    private SyntheticJoystickHandler() {
        this.mHandlerThread.start();
        this.mJoystickHandler = new JoystickHandler(this.mHandlerThread.getLooper());
    }

    public static SyntheticJoystickHandler getInstance() {
        synchronized (SyntheticJoystickHandler.class) {
            if (mSyntheticJoystickHandler == null) {
                mSyntheticJoystickHandler = new SyntheticJoystickHandler();
            }
        }
        return mSyntheticJoystickHandler;
    }

    public void setJoystickNavigationEventListener(OnJoystickNavigationEvent listener) {
        this.mOnJoystickNavigationEventListener = listener;
    }

    public void processMotionEvent(MotionEvent event) {
        LogUtil.d(TAG, "event:" + event.toString());
        JoystickHandler joystickHandler = this.mJoystickHandler;
        if (joystickHandler != null) {
            joystickHandler.process(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class JoystickHandler extends Handler {
        private static final int MSG_ENQUEUE_X_AXIS_KEY_REPEAT = 1;
        private static final int MSG_ENQUEUE_Y_AXIS_KEY_REPEAT = 2;
        private final SparseArray<KeyEvent> mDeviceKeyEvents;
        private final JoystickAxesState mJoystickAxesState;

        public JoystickHandler(Looper looper) {
            super(looper);
            this.mJoystickAxesState = new JoystickAxesState();
            this.mDeviceKeyEvents = new SparseArray<>();
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1 || i == 2) {
                KeyEvent oldEvent = (KeyEvent) msg.obj;
                KeyEvent e = KeyEvent.changeTimeRepeat(oldEvent, SystemClock.uptimeMillis(), oldEvent.getRepeatCount() + 1);
                LogUtil.w(SyntheticJoystickHandler.TAG, "handle message & sendEvent: " + e);
                Message m = obtainMessage(msg.what, e);
                m.setAsynchronous(true);
                sendMessageDelayed(m, (long) ViewConfiguration.getKeyRepeatDelay());
            }
        }

        public void process(MotionEvent event) {
            int actionMasked = event.getActionMasked();
            if (actionMasked == 2) {
                update(event);
            } else if (actionMasked == 3) {
                cancel();
            } else {
                LogUtil.w(SyntheticJoystickHandler.TAG, "Unexpected action: " + event.getActionMasked());
            }
        }

        private void cancel() {
            removeMessages(1);
            removeMessages(2);
            for (int i = 0; i < this.mDeviceKeyEvents.size(); i++) {
                KeyEvent keyEvent = this.mDeviceKeyEvents.valueAt(i);
                if (keyEvent != null) {
                    KeyEvent.changeTimeRepeat(keyEvent, SystemClock.uptimeMillis(), 0);
                    if (SyntheticJoystickHandler.this.mOnJoystickNavigationEventListener != null) {
                        SyntheticJoystickHandler.this.mOnJoystickNavigationEventListener.onJoystickNavigationEvent(keyEvent);
                    }
                    LogUtil.d(SyntheticJoystickHandler.TAG, "cancel send keyevent:" + keyEvent);
                }
            }
            this.mDeviceKeyEvents.clear();
            this.mJoystickAxesState.resetState();
        }

        private void update(MotionEvent event) {
            int historySize = event.getHistorySize();
            for (int h = 0; h < historySize; h++) {
                long time = event.getHistoricalEventTime(h);
                this.mJoystickAxesState.updateStateForAxis(event, time, 0, event.getHistoricalAxisValue(0, 0, h));
                this.mJoystickAxesState.updateStateForAxis(event, time, 1, event.getHistoricalAxisValue(1, 0, h));
                this.mJoystickAxesState.updateStateForAxis(event, time, 15, event.getHistoricalAxisValue(15, 0, h));
                this.mJoystickAxesState.updateStateForAxis(event, time, 16, event.getHistoricalAxisValue(16, 0, h));
            }
            long time2 = event.getEventTime();
            this.mJoystickAxesState.updateStateForAxis(event, time2, 0, event.getAxisValue(0));
            this.mJoystickAxesState.updateStateForAxis(event, time2, 1, event.getAxisValue(1));
            this.mJoystickAxesState.updateStateForAxis(event, time2, 15, event.getAxisValue(15));
            this.mJoystickAxesState.updateStateForAxis(event, time2, 16, event.getAxisValue(16));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes5.dex */
        public final class JoystickAxesState {
            private static final int STATE_DOWN_OR_RIGHT = 1;
            private static final int STATE_NEUTRAL = 0;
            private static final int STATE_UP_OR_LEFT = -1;
            final int[] mAxisStatesHat = {0, 0};
            final int[] mAxisStatesStick = {0, 0};

            JoystickAxesState() {
            }

            void resetState() {
                int[] iArr = this.mAxisStatesHat;
                iArr[0] = 0;
                iArr[1] = 0;
                int[] iArr2 = this.mAxisStatesStick;
                iArr2[0] = 0;
                iArr2[1] = 0;
            }

            void updateStateForAxis(MotionEvent event, long time, int axis, float value) {
                int axisStateIndex;
                int repeatMessage;
                int currentState;
                int keyCode;
                if (isXAxis(axis)) {
                    axisStateIndex = 0;
                    repeatMessage = 1;
                } else if (isYAxis(axis)) {
                    axisStateIndex = 1;
                    repeatMessage = 2;
                } else {
                    LogUtil.e(SyntheticJoystickHandler.TAG, "Unexpected axis " + axis + " in updateStateForAxis!");
                    return;
                }
                int newState = joystickAxisValueToState(value);
                if (axis == 0 || axis == 1) {
                    currentState = this.mAxisStatesStick[axisStateIndex];
                } else {
                    currentState = this.mAxisStatesHat[axisStateIndex];
                }
                if (currentState == newState) {
                    return;
                }
                int metaState = event.getMetaState();
                int deviceId = event.getDeviceId();
                int source = event.getSource();
                if (currentState == 1 || currentState == -1) {
                    int keyCode2 = joystickAxisAndStateToKeycode(axis, currentState);
                    if (keyCode2 != 0) {
                        KeyEvent keyEvent = new KeyEvent(time, time, 1, keyCode2, 0, metaState, deviceId, 0, 1024, source);
                        if (SyntheticJoystickHandler.this.mOnJoystickNavigationEventListener != null) {
                            SyntheticJoystickHandler.this.mOnJoystickNavigationEventListener.onJoystickNavigationEvent(keyEvent);
                        }
                        deviceId = deviceId;
                        JoystickHandler.this.mDeviceKeyEvents.put(deviceId, null);
                    }
                    JoystickHandler.this.removeMessages(repeatMessage);
                }
                if ((newState == 1 || newState == -1) && (keyCode = joystickAxisAndStateToKeycode(axis, newState)) != 0) {
                    int deviceId2 = deviceId;
                    KeyEvent keyEvent2 = new KeyEvent(time, time, 0, keyCode, 0, metaState, deviceId2, 0, 1024, source);
                    if (SyntheticJoystickHandler.this.mOnJoystickNavigationEventListener != null) {
                        SyntheticJoystickHandler.this.mOnJoystickNavigationEventListener.onJoystickNavigationEvent(keyEvent2);
                    }
                    Message m = JoystickHandler.this.obtainMessage(repeatMessage, keyEvent2);
                    m.setAsynchronous(true);
                    JoystickHandler.this.sendMessageDelayed(m, ViewConfiguration.getKeyRepeatTimeout());
                    JoystickHandler.this.mDeviceKeyEvents.put(deviceId2, new KeyEvent(time, time, 1, keyCode, 0, metaState, deviceId2, 0, 1056, source));
                }
                if (axis == 0 || axis == 1) {
                    this.mAxisStatesStick[axisStateIndex] = newState;
                } else {
                    this.mAxisStatesHat[axisStateIndex] = newState;
                }
            }

            private boolean isXAxis(int axis) {
                return axis == 0 || axis == 15;
            }

            private boolean isYAxis(int axis) {
                return axis == 1 || axis == 16;
            }

            private int joystickAxisAndStateToKeycode(int axis, int state) {
                if (isXAxis(axis) && state == -1) {
                    return 21;
                }
                if (isXAxis(axis) && state == 1) {
                    return 22;
                }
                if (isYAxis(axis) && state == -1) {
                    return 19;
                }
                if (isYAxis(axis) && state == 1) {
                    return 20;
                }
                LogUtil.e(SyntheticJoystickHandler.TAG, "Unknown axis " + axis + " or direction " + state);
                return 0;
            }

            private int joystickAxisValueToState(float value) {
                if (value >= 0.5f) {
                    return 1;
                }
                if (value <= -0.5f) {
                    return -1;
                }
                return 0;
            }
        }
    }
}
