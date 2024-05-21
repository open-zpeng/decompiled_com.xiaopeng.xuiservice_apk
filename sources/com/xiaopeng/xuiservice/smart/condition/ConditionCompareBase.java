package com.xiaopeng.xuiservice.smart.condition;

import com.xiaopeng.xuiservice.smart.condition.operator.Equal;
import com.xiaopeng.xuiservice.smart.condition.operator.GreatEqual;
import com.xiaopeng.xuiservice.smart.condition.operator.GreatThan;
import com.xiaopeng.xuiservice.smart.condition.operator.LessEqual;
import com.xiaopeng.xuiservice.smart.condition.operator.LessThan;
import com.xiaopeng.xuiservice.smart.condition.operator.NotEqual;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import com.xiaopeng.xuiservice.smart.condition.operator.Operators;
import java.lang.Comparable;
/* loaded from: classes5.dex */
public abstract class ConditionCompareBase<T extends Comparable<T>> extends ConditionSingleValue<T, T> {
    private final String compareOpName;

    public ConditionCompareBase(T targetValue, String compareOpName) {
        super(targetValue);
        this.compareOpName = compareOpName;
        if (compareOpName == null) {
            throw new IllegalArgumentException("opName is null");
        }
    }

    public String getCompareOpName() {
        return this.compareOpName;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<T, T> operator() {
        char c;
        String str = this.compareOpName;
        int hashCode = str.hashCode();
        if (hashCode == 3244) {
            if (str.equals(Operators.OP_EQ)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode == 3294) {
            if (str.equals(Operators.OP_GE)) {
                c = 3;
            }
            c = 65535;
        } else if (hashCode == 3309) {
            if (str.equals(Operators.OP_GT)) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode == 3449) {
            if (str.equals(Operators.OP_LE)) {
                c = 5;
            }
            c = 65535;
        } else if (hashCode != 3464) {
            if (hashCode == 108954 && str.equals(Operators.OP_NEQ)) {
                c = 1;
            }
            c = 65535;
        } else {
            if (str.equals(Operators.OP_LT)) {
                c = 4;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c != 3) {
                        if (c != 4) {
                            if (c == 5) {
                                return new LessEqual();
                            }
                            return null;
                        }
                        return new LessThan();
                    }
                    return new GreatEqual();
                }
                return new GreatThan();
            }
            return new NotEqual();
        }
        return new Equal();
    }
}
