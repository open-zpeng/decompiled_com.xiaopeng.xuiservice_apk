package com.xiaopeng.xuiservice.smart.action;

import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
abstract class Parallel extends ActionList {
    public Parallel(List<Action> actions) {
        super(actions);
    }

    public Parallel(Action... actions) {
        super(actions);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionList, com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        getActions().forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.action.-$$Lambda$goENCmd3Q-DYhD3Td87PBIP_M2s
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Action) obj).start();
            }
        });
    }
}
