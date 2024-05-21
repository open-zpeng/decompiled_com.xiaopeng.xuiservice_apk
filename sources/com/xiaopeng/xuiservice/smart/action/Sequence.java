package com.xiaopeng.xuiservice.smart.action;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class Sequence extends ActionList {
    private final AtomicInteger next;

    public Sequence(List<Action> actions) {
        super(actions);
        this.next = new AtomicInteger(0);
    }

    public Sequence(Action... actions) {
        super(actions);
        this.next = new AtomicInteger(0);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList, com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        startNextChild();
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList
    protected void onChildrenActionStart(Action action) {
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList
    protected void onChildrenActionDone(Action action) {
        if (getDoneActions().size() == getActions().size()) {
            Actions.INFO("All actions are done, stop self. " + this);
            stop();
        } else if (!isDone()) {
            startNextChild();
        }
    }

    private void startNextChild() {
        int index = this.next.getAndIncrement();
        if (index < getActions().size()) {
            getActions().get(index).start();
            return;
        }
        Actions.ERROR("nextIndex not valid,  " + this);
    }
}
