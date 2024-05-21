package org.opencv.objdetect;

import org.opencv.core.Algorithm;
/* loaded from: classes5.dex */
public class BaseCascadeClassifier extends Algorithm {
    private static native void delete(long j);

    protected BaseCascadeClassifier(long addr) {
        super(addr);
    }

    public static BaseCascadeClassifier __fromPtr__(long addr) {
        return new BaseCascadeClassifier(addr);
    }

    @Override // org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
