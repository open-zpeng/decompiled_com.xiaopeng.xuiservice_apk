package org.opencv.imgproc;
/* loaded from: classes5.dex */
public class GeneralizedHoughBallard extends GeneralizedHough {
    private static native void delete(long j);

    private static native int getLevels_0(long j);

    private static native int getVotesThreshold_0(long j);

    private static native void setLevels_0(long j, int i);

    private static native void setVotesThreshold_0(long j, int i);

    protected GeneralizedHoughBallard(long addr) {
        super(addr);
    }

    public static GeneralizedHoughBallard __fromPtr__(long addr) {
        return new GeneralizedHoughBallard(addr);
    }

    public int getLevels() {
        return getLevels_0(this.nativeObj);
    }

    public int getVotesThreshold() {
        return getVotesThreshold_0(this.nativeObj);
    }

    public void setLevels(int levels) {
        setLevels_0(this.nativeObj, levels);
    }

    public void setVotesThreshold(int votesThreshold) {
        setVotesThreshold_0(this.nativeObj, votesThreshold);
    }

    @Override // org.opencv.imgproc.GeneralizedHough, org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
