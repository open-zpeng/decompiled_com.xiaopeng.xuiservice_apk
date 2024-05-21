package com.xiaopeng.xuiservice.smart.action;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class Any extends Parallel {
    public Any(List<Action> actions) {
        super(actions);
    }

    public Any(Action... actions) {
        super(actions);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList
    protected void onChildrenActionStart(Action action) {
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList
    protected void onChildrenActionDone(Action action) {
        if (!isDone()) {
            stop();
        }
    }
}
