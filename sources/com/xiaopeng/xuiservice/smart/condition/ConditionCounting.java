package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionIG;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
/* loaded from: classes5.dex */
public class ConditionCounting extends ConditionSingleValue<Integer, Integer> {
    public static final String TYPE = "counting";
    private final Counting counting;
    private final String scope;

    /* loaded from: classes5.dex */
    public interface Counting {
        int getCounting();
    }

    public ConditionCounting(Integer targetValue, Counting counting, String scope) {
        super(targetValue);
        this.counting = counting;
        this.scope = scope;
        if (!ConditionIG.TYPE.equalsIgnoreCase(scope) && !Actions.ACTION_ALL.equalsIgnoreCase(scope)) {
            throw new IllegalArgumentException("Invalid scope: " + scope);
        }
    }

    public Counting getCounting() {
        return this.counting;
    }

    public String getScope() {
        return this.scope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        return null;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
    }

    public /* synthetic */ boolean lambda$operator$0$ConditionCounting(Integer maxCount, Integer _ignore) {
        return maxCount.intValue() > this.counting.getCounting();
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<Integer, Integer> operator() {
        return new OP() { // from class: com.xiaopeng.xuiservice.smart.condition.-$$Lambda$ConditionCounting$Gv0is3gsG22uKo5eZWW8vsaZkcM
            @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
            public final boolean isMatch(Object obj, Object obj2) {
                return ConditionCounting.this.lambda$operator$0$ConditionCounting((Integer) obj, (Integer) obj2);
            }
        };
    }
}
