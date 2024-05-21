package com.xiaopeng.xuiservice.smart.condition;

import android.util.Pair;
import com.xiaopeng.xuiservice.smart.condition.operator.NotBetween;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public abstract class ConditionNotBetween<T extends Comparable<T>> extends ConditionSingleValue<Pair<T, T>, T> {
    public ConditionNotBetween(Pair<T, T> targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<Pair<T, T>, T> operator() {
        return new NotBetween();
    }
}
