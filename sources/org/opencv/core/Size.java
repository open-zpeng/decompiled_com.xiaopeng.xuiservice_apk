package org.opencv.core;
/* loaded from: classes5.dex */
public class Size {
    public double height;
    public double width;

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public Size() {
        this(0.0d, 0.0d);
    }

    public Size(Point p) {
        this.width = p.x;
        this.height = p.y;
    }

    public Size(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        if (vals != null) {
            this.width = vals.length > 0 ? vals[0] : 0.0d;
            this.height = vals.length > 1 ? vals[1] : 0.0d;
            return;
        }
        this.width = 0.0d;
        this.height = 0.0d;
    }

    public double area() {
        return this.width * this.height;
    }

    public boolean empty() {
        return this.width <= 0.0d || this.height <= 0.0d;
    }

    public Size clone() {
        return new Size(this.width, this.height);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.height);
        int result = (1 * 31) + ((int) ((temp >>> 32) ^ temp));
        long temp2 = Double.doubleToLongBits(this.width);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Size) {
            Size it = (Size) obj;
            return this.width == it.width && this.height == it.height;
        }
        return false;
    }

    public String toString() {
        return ((int) this.width) + "x" + ((int) this.height);
    }
}
