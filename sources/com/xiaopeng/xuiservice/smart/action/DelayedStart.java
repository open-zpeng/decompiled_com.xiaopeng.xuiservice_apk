package com.xiaopeng.xuiservice.smart.action;

import com.xiaopeng.xuiservice.smart.action.Action;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class DelayedStart extends ActionBase implements Action.ActionChangeListener {
    private final long delay;
    private ScheduledFuture<?> futureForCancel;
    private final Action original;
    private final TimeUnit unit;

    public DelayedStart(Action original, long delay, TimeUnit unit) {
        this.original = original;
        this.delay = delay;
        this.unit = unit;
        original.addActionChangeListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        ScheduledExecutorService executor = Actions.getExecutor();
        final Action action = this.original;
        Objects.requireNonNull(action);
        this.futureForCancel = executor.schedule(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$ugmuCTpo5TbTSg7WGALGuEX7kOE
            @Override // java.lang.Runnable
            public final void run() {
                Action.this.start();
            }
        }, this.delay, this.unit);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        ScheduledFuture<?> future = this.futureForCancel;
        if (future != null) {
            if (!future.isDone()) {
                future.cancel(false);
            }
            this.futureForCancel = null;
        }
        this.original.stop();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public void onActionStarted(Action action) {
        this.futureForCancel = null;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public void onActionDone(Action action) {
        stop();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return getClass().getSimpleName() + "[" + this.delay + " " + this.unit + "]" + this.original;
    }
}
