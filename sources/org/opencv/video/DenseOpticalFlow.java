package org.opencv.video;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
/* loaded from: classes5.dex */
public class DenseOpticalFlow extends Algorithm {
    private static native void calc_0(long j, long j2, long j3, long j4);

    private static native void collectGarbage_0(long j);

    private static native void delete(long j);

    /* JADX INFO: Access modifiers changed from: protected */
    public DenseOpticalFlow(long addr) {
        super(addr);
    }

    public static DenseOpticalFlow __fromPtr__(long addr) {
        return new DenseOpticalFlow(addr);
    }

    public void calc(Mat I0, Mat I1, Mat flow) {
        calc_0(this.nativeObj, I0.nativeObj, I1.nativeObj, flow.nativeObj);
    }

    public void collectGarbage() {
        collectGarbage_0(this.nativeObj);
    }

    @Override // org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
