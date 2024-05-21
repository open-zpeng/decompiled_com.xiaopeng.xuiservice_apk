package com.xiaopeng.xuiservice.smart.action;

import android.os.SystemClock;
import com.xiaopeng.xuiservice.smart.action.Action;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
public abstract class ActionBase implements Action {
    private static final int STATE_CREATED = 0;
    private static final int STATE_DONE = 2;
    private static final int STATE_RUNNING = 1;
    private long startTime;
    private final AtomicInteger state = new AtomicInteger(0);
    private final ArrayList<Action.ActionChangeListener> actionChangeListeners = new ArrayList<>();

    protected abstract void onStart();

    protected abstract void onStop();

    private static String nameOfState(int state) {
        if (state != 0) {
            if (state != 1) {
                if (state == 2) {
                    return "DONE";
                }
                return "UNKNOWN STATE";
            }
            return "RUNNING";
        }
        return "CREATED";
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action
    public final void start() {
        if (this.state.compareAndSet(0, 1)) {
            this.startTime = SystemClock.elapsedRealtime();
            Actions.INFO("ACTION START: " + this);
            boolean started = false;
            try {
                onStart();
                started = true;
            } catch (Throwable t) {
                Actions.ERROR("start action error: " + this, t);
            }
            if (started) {
                try {
                    notifyStarted();
                    return;
                } catch (Throwable t2) {
                    Actions.ERROR("notifyStarted error: " + this, t2);
                    return;
                }
            }
            return;
        }
        Actions.INFO("WARNING try to start " + this + " ignored.");
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action
    public final void stop() {
        if (this.state.compareAndSet(1, 2)) {
            Actions.INFO("ACTION STOP: " + this + " use: " + (SystemClock.elapsedRealtime() - this.startTime) + "ms");
            boolean stopped = false;
            try {
                onStop();
                stopped = true;
            } catch (Throwable t) {
                Actions.ERROR("stop action error: " + this, t);
            }
            if (stopped) {
                try {
                    notifyDone();
                } catch (Throwable t2) {
                    Actions.ERROR("notifyDone error: " + this, t2);
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action
    public final boolean isStarted() {
        return this.state.get() == 1;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action
    public final boolean isDone() {
        return this.state.get() == 2;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action
    public void addActionChangeListener(Action.ActionChangeListener listener) {
        synchronized (this.actionChangeListeners) {
            if (listener != null) {
                if (!this.actionChangeListeners.contains(listener)) {
                    this.actionChangeListeners.add(listener);
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action
    public void removeActionChangeListener(Action.ActionChangeListener listener) {
        synchronized (this.actionChangeListeners) {
            if (listener != null) {
                this.actionChangeListeners.remove(listener);
            }
        }
    }

    private void notifyStarted() {
        synchronized (this.actionChangeListeners) {
            int size = this.actionChangeListeners.size();
            Actions.INFO("NOTIFY ACTION START: " + this + " LISTENER=" + size);
            this.actionChangeListeners.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$ActionBase$jsMVHJrLE4YdjSwfqx5cMS7oshI
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ActionBase.this.lambda$notifyStarted$0$ActionBase((Action.ActionChangeListener) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$notifyStarted$0$ActionBase(Action.ActionChangeListener listener) {
        listener.onActionStarted(this);
    }

    private void notifyDone() {
        synchronized (this.actionChangeListeners) {
            int size = this.actionChangeListeners.size();
            Actions.INFO("NOTIFY ACTION DONE: " + this + " LISTENER=" + size);
            this.actionChangeListeners.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$ActionBase$bWIXvl4vm9569urXaMvmjfwtVUw
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ActionBase.this.lambda$notifyDone$1$ActionBase((Action.ActionChangeListener) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$notifyDone$1$ActionBase(Action.ActionChangeListener listener) {
        listener.onActionDone(this);
    }

    public String toString() {
        return getClass().getSimpleName() + "[state=" + nameOfState(this.state.get()) + "]";
    }
}
