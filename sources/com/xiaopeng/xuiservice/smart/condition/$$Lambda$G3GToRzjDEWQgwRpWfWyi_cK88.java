package com.xiaopeng.xuiservice.smart.condition;

import java.util.function.Predicate;
/* compiled from: lambda */
/* renamed from: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$G3GTo-RzjDEWQgwRpWfWyi_cK88  reason: invalid class name */
/* loaded from: classes5.dex */
public final /* synthetic */ class $$Lambda$G3GToRzjDEWQgwRpWfWyi_cK88 implements Predicate {
    public static final /* synthetic */ $$Lambda$G3GToRzjDEWQgwRpWfWyi_cK88 INSTANCE = new $$Lambda$G3GToRzjDEWQgwRpWfWyi_cK88();

    private /* synthetic */ $$Lambda$G3GToRzjDEWQgwRpWfWyi_cK88() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((Condition) obj).isMatch();
    }
}
