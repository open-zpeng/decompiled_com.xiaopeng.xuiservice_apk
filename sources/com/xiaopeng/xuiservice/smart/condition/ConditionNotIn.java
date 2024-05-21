package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.NotIn;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
/* loaded from: classes5.dex */
public abstract class ConditionNotIn<T> extends ConditionSingleValue<T[], T> {
    public ConditionNotIn(T[] targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<T[], T> operator() {
        return new NotIn();
    }
}
