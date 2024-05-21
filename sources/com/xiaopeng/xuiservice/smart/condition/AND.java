package com.xiaopeng.xuiservice.smart.condition;

import java.util.List;
/* loaded from: classes5.dex */
public class AND extends Group {
    public static final String TYPE = "AND";

    /* JADX INFO: Access modifiers changed from: package-private */
    public AND(List<Condition> group) {
        super(group);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public boolean isMatch() {
        return getConditions().stream().allMatch($$Lambda$G3GToRzjDEWQgwRpWfWyi_cK88.INSTANCE);
    }
}
