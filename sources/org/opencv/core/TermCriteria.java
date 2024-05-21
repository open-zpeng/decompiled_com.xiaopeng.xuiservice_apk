package org.opencv.core;
/* loaded from: classes5.dex */
public class TermCriteria {
    public static final int COUNT = 1;
    public static final int EPS = 2;
    public static final int MAX_ITER = 1;
    public double epsilon;
    public int maxCount;
    public int type;

    public TermCriteria(int type, int maxCount, double epsilon) {
        this.type = type;
        this.maxCount = maxCount;
        this.epsilon = epsilon;
    }

    public TermCriteria() {
        this(0, 0, 0.0d);
    }

    public TermCriteria(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        if (vals != null) {
            this.type = vals.length > 0 ? (int) vals[0] : 0;
            this.maxCount = vals.length > 1 ? (int) vals[1] : 0;
            this.epsilon = vals.length > 2 ? vals[2] : 0.0d;
            return;
        }
        this.type = 0;
        this.maxCount = 0;
        this.epsilon = 0.0d;
    }

    public TermCriteria clone() {
        return new TermCriteria(this.type, this.maxCount, this.epsilon);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.type);
        int result = (1 * 31) + ((int) ((temp >>> 32) ^ temp));
        int result2 = this.maxCount;
        long temp2 = Double.doubleToLongBits(result2);
        int result3 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.epsilon);
        return (result3 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TermCriteria) {
            TermCriteria it = (TermCriteria) obj;
            return this.type == it.type && this.maxCount == it.maxCount && this.epsilon == it.epsilon;
        }
        return false;
    }

    public String toString() {
        return "{ type: " + this.type + ", maxCount: " + this.maxCount + ", epsilon: " + this.epsilon + "}";
    }
}
