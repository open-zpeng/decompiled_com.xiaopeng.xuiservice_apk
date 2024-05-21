package com.xiaopeng.xuiservice.smart.action;

import com.xiaopeng.xuiservice.smart.action.Action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
public abstract class ActionList extends ActionBase implements Action.ActionChangeListener {
    private final ArrayList<Action> actions;
    private final Set<Action> doneActions;
    private final Set<Action> runningActions;

    protected abstract void onChildrenActionDone(Action action);

    protected abstract void onChildrenActionStart(Action action);

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected abstract void onStart();

    public ActionList(List<Action> actions) {
        this.actions = new ArrayList<>(actions);
        int size = actions.size();
        this.runningActions = Collections.synchronizedSet(new HashSet(size));
        this.doneActions = Collections.synchronizedSet(new HashSet(size));
        actions.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$ActionList$BgX3F6UlQIUS7THR3a29whyQL7o
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActionList.this.lambda$new$0$ActionList((Action) obj);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$ActionList(Action action) {
        action.addActionChangeListener(this);
    }

    public ActionList(Action... actions) {
        this(Arrays.asList(actions));
    }

    public ArrayList<Action> getActions() {
        return this.actions;
    }

    public Set<Action> getRunningActions() {
        return this.runningActions;
    }

    public Set<Action> getDoneActions() {
        return this.doneActions;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public final void onActionStarted(Action action) {
        this.runningActions.add(action);
        onChildrenActionStart(action);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
    public final void onActionDone(Action action) {
        this.runningActions.remove(action);
        this.doneActions.add(action);
        onChildrenActionDone(action);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected final void onStop() {
        this.actions.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$Rt6Q5AhdUqwRtC3YAXq00Ww3XUg
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Action) obj).stop();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("[");
        int size = this.actions.size();
        sb.append("[");
        sb.append(size);
        sb.append("]");
        for (int i = 0; i < size; i++) {
            sb.append(this.actions.get(i));
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
