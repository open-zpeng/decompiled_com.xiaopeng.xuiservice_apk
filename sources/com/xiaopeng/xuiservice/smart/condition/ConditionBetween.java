package com.xiaopeng.xuiservice.smart.condition;

import android.util.Pair;
import com.xiaopeng.xuiservice.smart.condition.operator.Between;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public abstract class ConditionBetween<T extends Comparable<T>> extends ConditionSingleValue<Pair<T, T>, T> {
    public ConditionBetween(Pair<T, T> targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<Pair<T, T>, T> operator() {
        return new Between();
    }
}
