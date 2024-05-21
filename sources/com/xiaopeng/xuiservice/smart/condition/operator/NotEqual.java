package com.xiaopeng.xuiservice.smart.condition.operator;
/* loaded from: classes5.dex */
public class NotEqual<T> implements OP<T, T> {
    private final Equal<T> equal = new Equal<>();

    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public boolean isMatch(T targetValue, T currentValue) {
        return !this.equal.isMatch(targetValue, currentValue);
    }
}
