package com.xiaopeng.xuiservice.smart.condition;

import androidx.annotation.NonNull;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.smart.condition.util.ConditionJsonUtil;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
public abstract class ConditionBase implements Condition {
    private final ArrayList<Condition.ConditionChangeListener> listeners = new ArrayList<>();
    private final AtomicBoolean isWatching = new AtomicBoolean(false);

    protected abstract void beforeWatch();

    protected abstract void onStartWatch();

    protected abstract void onStopWatch();

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public final void startWatch() {
        if (!this.isWatching.compareAndSet(false, true)) {
            Conditions.INFO("INVALID CALL, duplicated startWatch, " + this);
            return;
        }
        beforeWatch();
        Conditions.INFO("WATCH START: " + this);
        onStartWatch();
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public final void stopWatch() {
        if (!this.isWatching.compareAndSet(true, false)) {
            Conditions.INFO("INVALID CALL, duplicated stopWatch, " + this);
            return;
        }
        Conditions.INFO("WATCH STOP: " + this);
        onStopWatch();
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public final void addConditionChangeListener(Condition.ConditionChangeListener listener) {
        synchronized (this.listeners) {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public final void removeConditionChangeListener(Condition.ConditionChangeListener listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyConditionChange() {
        synchronized (this.listeners) {
            int size = this.listeners.size();
            Conditions.INFO("NOTIFY condition change to " + size + " listeners, " + this);
            this.listeners.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$ConditionBase$Ga0nPyWzxCdhIebXvi6R2otdhBY
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ConditionBase.this.lambda$notifyConditionChange$0$ConditionBase((Condition.ConditionChangeListener) obj);
                }
            });
        }
    }

    public /* synthetic */ void lambda$notifyConditionChange$0$ConditionBase(Condition.ConditionChangeListener listener) {
        listener.onConditionChange(this);
    }

    @NonNull
    public String toString() {
        return "Condition" + ConditionJsonUtil.condition2JSONObject(this) + "(" + Integer.toHexString(hashCode()) + ")";
    }
}
