package com.xiaopeng.xuiservice.smart.action;
/* loaded from: classes5.dex */
public interface Action {

    /* loaded from: classes5.dex */
    public interface ActionChangeListener {
        void onActionDone(Action action);

        void onActionStarted(Action action);
    }

    void addActionChangeListener(ActionChangeListener actionChangeListener);

    boolean isDone();

    boolean isStarted();

    void removeActionChangeListener(ActionChangeListener actionChangeListener);

    void start();

    void stop();
}
