package org.opencv.photo;

import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;
/* loaded from: classes5.dex */
public class MergeExposures extends Algorithm {
    private static native void delete(long j);

    private static native void process_0(long j, long j2, long j3, long j4, long j5);

    /* JADX INFO: Access modifiers changed from: protected */
    public MergeExposures(long addr) {
        super(addr);
    }

    public static MergeExposures __fromPtr__(long addr) {
        return new MergeExposures(addr);
    }

    public void process(List<Mat> src, Mat dst, Mat times, Mat response) {
        Mat src_mat = Converters.vector_Mat_to_Mat(src);
        process_0(this.nativeObj, src_mat.nativeObj, dst.nativeObj, times.nativeObj, response.nativeObj);
    }

    @Override // org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
