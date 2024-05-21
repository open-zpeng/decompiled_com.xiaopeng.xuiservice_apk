package org.opencv.video;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
/* loaded from: classes5.dex */
public class SparseOpticalFlow extends Algorithm {
    private static native void calc_0(long j, long j2, long j3, long j4, long j5, long j6, long j7);

    private static native void calc_1(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void delete(long j);

    /* JADX INFO: Access modifiers changed from: protected */
    public SparseOpticalFlow(long addr) {
        super(addr);
    }

    public static SparseOpticalFlow __fromPtr__(long addr) {
        return new SparseOpticalFlow(addr);
    }

    public void calc(Mat prevImg, Mat nextImg, Mat prevPts, Mat nextPts, Mat status, Mat err) {
        calc_0(this.nativeObj, prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj);
    }

    public void calc(Mat prevImg, Mat nextImg, Mat prevPts, Mat nextPts, Mat status) {
        calc_1(this.nativeObj, prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj);
    }

    @Override // org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
