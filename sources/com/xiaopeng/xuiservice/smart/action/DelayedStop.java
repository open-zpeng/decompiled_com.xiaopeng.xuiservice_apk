package com.xiaopeng.xuiservice.smart.action;

import com.xiaopeng.xuiservice.smart.action.Action;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class DelayedStop extends ActionBase implements Action.ActionChangeListener {
    private final long delay;
    private ScheduledFuture<?> futureForCancel;
    private final Action original;
    private final TimeUnit unit;

    public DelayedStop(Action original, long delay, TimeUnit unit) {
        this.original = original;
        this.delay = delay;
        this.unit = unit;
        original.addActionChangeListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        this.original.start();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        ScheduledFuture<?> future = this.futureForCancel;
        if (future != null) {
            future.cancel(false);
            this.futureForCancel = null;
        }
        this.original.stop();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public void onActionStarted(Action action) {
        ScheduledExecutorService executor = Actions.getExecutor();
        final Action action2 = this.original;
        Objects.requireNonNull(action2);
        this.futureForCancel = executor.schedule(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$lo5LiohUZmmch6zgPTwAu99F1XY
            @Override // java.lang.Runnable
            public final void run() {
                Action.this.stop();
            }
        }, this.delay, this.unit);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public void onActionDone(Action action) {
        ScheduledFuture<?> future = this.futureForCancel;
        if (future != null) {
            if (!future.isDone()) {
                future.cancel(false);
            }
            this.futureForCancel = null;
        }
        stop();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return getClass().getSimpleName() + "[" + this.delay + " " + this.unit + "]" + this.original;
    }
}
