package org.opencv.core;
/* loaded from: classes5.dex */
public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0.0d, 0.0d);
    }

    public Point(double[] vals) {
        this();
        set(vals);
    }

    public void set(double[] vals) {
        if (vals != null) {
            this.x = vals.length > 0 ? vals[0] : 0.0d;
            this.y = vals.length > 1 ? vals[1] : 0.0d;
            return;
        }
        this.x = 0.0d;
        this.y = 0.0d;
    }

    public Point clone() {
        return new Point(this.x, this.y);
    }

    public double dot(Point p) {
        return (this.x * p.x) + (this.y * p.y);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = (1 * 31) + ((int) ((temp >>> 32) ^ temp));
        long temp2 = Double.doubleToLongBits(this.y);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Point) {
            Point it = (Point) obj;
            return this.x == it.x && this.y == it.y;
        }
        return false;
    }

    public boolean inside(Rect r) {
        return r.contains(this);
    }

    public String toString() {
        return "{" + this.x + ", " + this.y + "}";
    }
}
