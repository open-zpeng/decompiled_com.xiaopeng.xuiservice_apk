package org.opencv.features2d;
/* loaded from: classes5.dex */
public class FlannBasedMatcher extends DescriptorMatcher {
    private static native long FlannBasedMatcher_0();

    private static native long create_0();

    private static native void delete(long j);

    protected FlannBasedMatcher(long addr) {
        super(addr);
    }

    public static FlannBasedMatcher __fromPtr__(long addr) {
        return new FlannBasedMatcher(addr);
    }

    public FlannBasedMatcher() {
        super(FlannBasedMatcher_0());
    }

    public static FlannBasedMatcher create() {
        return __fromPtr__(create_0());
    }

    @Override // org.opencv.features2d.DescriptorMatcher, org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
