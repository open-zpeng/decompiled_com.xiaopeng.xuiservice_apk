package org.opencv.features2d;
/* loaded from: classes5.dex */
public class SimpleBlobDetector extends Feature2D {
    private static native long create_0();

    private static native void delete(long j);

    private static native String getDefaultName_0(long j);

    protected SimpleBlobDetector(long addr) {
        super(addr);
    }

    public static SimpleBlobDetector __fromPtr__(long addr) {
        return new SimpleBlobDetector(addr);
    }

    public static SimpleBlobDetector create() {
        return __fromPtr__(create_0());
    }

    @Override // org.opencv.features2d.Feature2D, org.opencv.core.Algorithm
    public String getDefaultName() {
        return getDefaultName_0(this.nativeObj);
    }

    @Override // org.opencv.features2d.Feature2D, org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
