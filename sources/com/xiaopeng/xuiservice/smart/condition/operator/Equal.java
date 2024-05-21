package com.xiaopeng.xuiservice.smart.condition.operator;

import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class Equal<T> implements OP<T, T> {
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public boolean isMatch(T targetValue, T currentValue) {
        return Conditions.superEquals(targetValue, currentValue);
    }
}
