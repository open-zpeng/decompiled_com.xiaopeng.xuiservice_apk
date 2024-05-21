package com.xiaopeng.xuiservice.smart.action;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class All extends Parallel {
    public All(List<Action> actions) {
        super(actions);
    }

    public All(Action... actions) {
        super(actions);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList
    protected void onChildrenActionStart(Action action) {
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList
    protected void onChildrenActionDone(Action action) {
        if (getDoneActions().size() == getActions().size()) {
            stop();
        }
    }
}
