package com.xiaopeng.xuiservice.smart.condition.operator;
/* loaded from: classes5.dex */
public class NotIn<T> implements OP<T[], T> {
    private final In<T> in = new In<>();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public /* bridge */ /* synthetic */ boolean isMatch(Object obj, Object obj2) {
        return isMatch((Object[][]) obj, (Object[]) obj2);
    }

    public boolean isMatch(T[] targetValue, T currentValue) {
        return !this.in.isMatch((T[][]) targetValue, (T[]) currentValue);
    }
}
