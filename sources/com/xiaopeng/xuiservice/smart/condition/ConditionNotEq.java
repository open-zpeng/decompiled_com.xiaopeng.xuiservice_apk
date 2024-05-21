package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.NotEqual;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
/* loaded from: classes5.dex */
public abstract class ConditionNotEq<T> extends ConditionSingleValue<T, T> {
    public ConditionNotEq(T targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<T, T> operator() {
        return new NotEqual();
    }
}
