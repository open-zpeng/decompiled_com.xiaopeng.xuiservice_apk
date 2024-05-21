package com.xiaopeng.xuiservice.smart.condition.operator;

import java.lang.Comparable;
/* loaded from: classes5.dex */
public class LessEqual<T extends Comparable<T>> implements OP<T, T> {
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public boolean isMatch(T targetValue, T currentValue) {
        return currentValue != null && currentValue.compareTo(targetValue) <= 0;
    }
}
