package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.Condition;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
public abstract class Group extends ConditionBase implements Condition.ConditionChangeListener {
    private final List<Condition> conditions;

    public Group(List<Condition> conditions) {
        this.conditions = conditions;
        conditions.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$Group$-xxYZqfVypu_1M-vNJexgVd_23s
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Group.this.lambda$new$0$Group((Condition) obj);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$Group(Condition condition) {
        condition.addConditionChangeListener(this);
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void beforeWatch() {
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        this.conditions.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$_pkHAaCLIFLBRsJS6K1w_rMI-c0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Condition) obj).startWatch();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        this.conditions.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$YBEFo6Il9Cq3S58M45IIh5uZyIE
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Condition) obj).stopWatch();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition.ConditionChangeListener
    public void onConditionChange(Condition condition) {
        notifyConditionChange();
    }
}
