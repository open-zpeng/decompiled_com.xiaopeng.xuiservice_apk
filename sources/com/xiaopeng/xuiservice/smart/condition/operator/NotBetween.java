package com.xiaopeng.xuiservice.smart.condition.operator;

import android.util.Pair;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public class NotBetween<T extends Comparable<T>> implements OP<Pair<T, T>, T> {
    private final Between<T> between = new Between<>();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public /* bridge */ /* synthetic */ boolean isMatch(Object obj, Object obj2) {
        return isMatch((Pair<Pair, Pair>) obj, (Pair) ((Comparable) obj2));
    }

    public boolean isMatch(Pair<T, T> targetValue, T currentValue) {
        return !this.between.isMatch((Pair<Pair<T, T>, Pair<T, T>>) targetValue, (Pair<T, T>) currentValue);
    }
}
