package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.GreatEqual;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public abstract class ConditionGE<T extends Comparable<T>> extends ConditionSingleValue<T, T> {
    public ConditionGE(T targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<T, T> operator() {
        return new GreatEqual();
    }
}
