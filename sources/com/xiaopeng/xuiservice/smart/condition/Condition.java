package com.xiaopeng.xuiservice.smart.condition;
/* loaded from: classes5.dex */
public interface Condition {

    /* loaded from: classes5.dex */
    public interface ConditionChangeListener {
        void onConditionChange(Condition condition);
    }

    void addConditionChangeListener(ConditionChangeListener conditionChangeListener);

    boolean isMatch();

    void removeConditionChangeListener(ConditionChangeListener conditionChangeListener);

    void startWatch();

    void stopWatch();
}
