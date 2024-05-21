package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.GreatThan;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public abstract class ConditionGT<T extends Comparable<T>> extends ConditionSingleValue<T, T> {
    public ConditionGT(T targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<T, T> operator() {
        return new GreatThan();
    }
}
