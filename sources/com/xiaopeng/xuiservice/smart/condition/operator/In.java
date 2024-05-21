package com.xiaopeng.xuiservice.smart.condition.operator;

import com.xiaopeng.xuiservice.smart.condition.Conditions;
import java.util.Arrays;
import java.util.function.Predicate;
/* loaded from: classes5.dex */
public class In<T> implements OP<T[], T> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
    public /* bridge */ /* synthetic */ boolean isMatch(Object obj, Object obj2) {
        return isMatch((Object[][]) obj, (Object[]) obj2);
    }

    public boolean isMatch(T[] targetValue, final T currentValue) {
        return Arrays.stream(targetValue).anyMatch(new Predicate() { // from class: com.xiaopeng.xuiservice.smart.condition.operator.-$$Lambda$In$MQBz0hAUd_5oL7x2OjhkbhMUQeo
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean superEquals;
                superEquals = Conditions.superEquals(obj, currentValue);
                return superEquals;
            }
        });
    }
}
