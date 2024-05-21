package com.xiaopeng.xuiservice.smart.condition;

import java.util.List;
/* loaded from: classes5.dex */
public class OR extends Group {
    public static final String TYPE = "OR";

    /* JADX INFO: Access modifiers changed from: package-private */
    public OR(List<Condition> group) {
        super(group);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition
    public boolean isMatch() {
        return getConditions().stream().anyMatch($$Lambda$G3GToRzjDEWQgwRpWfWyi_cK88.INSTANCE);
    }
}
