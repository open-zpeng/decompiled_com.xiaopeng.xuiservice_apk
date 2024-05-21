package org.opencv.core;

import java.util.Arrays;
import java.util.List;
/* loaded from: classes5.dex */
public class MatOfByte extends Mat {
    private static final int _channels = 1;
    private static final int _depth = 0;

    public MatOfByte() {
    }

    protected MatOfByte(long addr) {
        super(addr);
        if (!empty() && checkVector(1, 0) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public static MatOfByte fromNativeAddr(long addr) {
        return new MatOfByte(addr);
    }

    public MatOfByte(Mat m) {
        super(m, Range.all());
        if (!empty() && checkVector(1, 0) < 0) {
            throw new IllegalArgumentException("Incompatible Mat");
        }
    }

    public MatOfByte(byte... a) {
        fromArray(a);
    }

    public MatOfByte(int offset, int length, byte... a) {
        fromArray(offset, length, a);
    }

    public void alloc(int elemNumber) {
        if (elemNumber > 0) {
            super.create(elemNumber, 1, CvType.makeType(0, 1));
        }
    }

    public void fromArray(byte... a) {
        if (a == null || a.length == 0) {
            return;
        }
        int num = a.length / 1;
        alloc(num);
        put(0, 0, a);
    }

    public void fromArray(int offset, int length, byte... a) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (a == null) {
            throw new NullPointerException();
        }
        if (length < 0 || length + offset > a.length) {
            throw new IllegalArgumentException("invalid 'length' parameter: " + Integer.toString(length));
        } else if (a.length == 0) {
        } else {
            int num = length / 1;
            alloc(num);
            put(0, 0, a, offset, length);
        }
    }

    public byte[] toArray() {
        int num = checkVector(1, 0);
        if (num < 0) {
            throw new RuntimeException("Native Mat has unexpected type or size: " + toString());
        }
        byte[] a = new byte[num * 1];
        if (num == 0) {
            return a;
        }
        get(0, 0, a);
        return a;
    }

    public void fromList(List<Byte> lb) {
        if (lb == null || lb.size() == 0) {
            return;
        }
        Byte[] ab = (Byte[]) lb.toArray(new Byte[0]);
        byte[] a = new byte[ab.length];
        for (int i = 0; i < ab.length; i++) {
            a[i] = ab[i].byteValue();
        }
        fromArray(a);
    }

    public List<Byte> toList() {
        byte[] a = toArray();
        Byte[] ab = new Byte[a.length];
        for (int i = 0; i < a.length; i++) {
            ab[i] = Byte.valueOf(a[i]);
        }
        return Arrays.asList(ab);
    }
}