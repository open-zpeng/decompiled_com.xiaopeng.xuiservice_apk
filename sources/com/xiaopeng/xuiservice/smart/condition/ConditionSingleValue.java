package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.OP;
/* loaded from: classes5.dex */
public abstract class ConditionSingleValue<T, C> extends ConditionBase {
    private C currentValue;
    private OP<T, C> operator;
    private final T targetValue;

    protected abstract C fetchCurrentValue();

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected abstract void onStartWatch();

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected abstract void onStopWatch();

    protected abstract OP<T, C> operator();

    public ConditionSingleValue(T targetValue) {
        if (targetValue == null) {
            throw new NullPointerException("target value is null");
        }
        this.targetValue = targetValue;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void beforeWatch() {
        updateCurrentValue();
    }

    public T getTargetValue() {
        return this.targetValue;
    }

    public C getCurrentValue() {
        return this.currentValue;
    }

    public void updateCurrentValue() {
        updateCurrentValue(fetchCurrentValue());
    }

    public void updateCurrentValue(C newValue) {
        if (!Conditions.superEquals(this.currentValue, newValue)) {
            Conditions.INFO("UPDATE " + this + " current value: " + Conditions.valueToString(this.currentValue) + " => " + Conditions.valueToString(newValue));
            this.currentValue = newValue;
            notifyConditionChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateCurrentValueWithoutNotify(C newValue) {
        this.currentValue = newValue;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public final boolean isMatch() {
        if (this.operator == null) {
            this.operator = operator();
        }
        return this.operator.isMatch(this.targetValue, getCurrentValue());
    }
}
