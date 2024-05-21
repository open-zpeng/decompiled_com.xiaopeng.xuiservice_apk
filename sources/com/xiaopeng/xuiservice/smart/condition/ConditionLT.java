package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.LessThan;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public abstract class ConditionLT<T extends Comparable<T>> extends ConditionSingleValue<T, T> {
    public ConditionLT(T targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<T, T> operator() {
        return new LessThan();
    }
}
