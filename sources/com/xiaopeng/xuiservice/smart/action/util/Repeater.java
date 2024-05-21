package com.xiaopeng.xuiservice.smart.action.util;

import com.xiaopeng.xuiservice.smart.action.Action;
import com.xiaopeng.xuiservice.smart.action.Actions;
/* loaded from: classes5.dex */
public class Repeater {
    private final Action action;
    private int done;
    private final int repeat;

    public Repeater(int repeat, Action action) {
        this.repeat = repeat;
        this.action = action;
    }

    public synchronized void repeat(Runnable runnable) {
        this.done++;
        StringBuilder sb = new StringBuilder();
        sb.append("REPEAT ");
        sb.append(this.action);
        sb.append(" (");
        sb.append(this.done);
        sb.append("/");
        sb.append(this.repeat);
        sb.append(")");
        sb.append(this.repeat <= 0 ? " INFINITE" : "");
        Actions.INFO(sb.toString());
        if (this.repeat > 0 && this.done >= this.repeat) {
            this.action.stop();
        }
        if (!this.action.isDone()) {
            runnable.run();
        }
    }

    public String toString() {
        return "Repeater{repeat=" + this.repeat + '}';
    }
}
