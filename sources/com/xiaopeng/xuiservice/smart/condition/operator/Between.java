package com.xiaopeng.xuiservice.smart.condition.operator;

import android.util.Pair;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public class Between<T extends Comparable<T>> implements OP<Pair<T, T>, T> {
    private final GreatEqual<T> ge = new GreatEqual<>();
    private final LessEqual<T> le = new LessEqual<>();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public /* bridge */ /* synthetic */ boolean isMatch(Object obj, Object obj2) {
        return isMatch((Pair<Pair, Pair>) obj, (Pair) ((Comparable) obj2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isMatch(Pair<T, T> targetValue, T currentValue) {
        return this.ge.isMatch((Comparable) targetValue.first, (Comparable) currentValue) && this.le.isMatch((Comparable) targetValue.second, (Comparable) currentValue);
    }
}
