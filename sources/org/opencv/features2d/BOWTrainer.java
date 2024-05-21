package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;
/* loaded from: classes5.dex */
public class BOWTrainer {
    protected final long nativeObj;

    private static native void add_0(long j, long j2);

    private static native void clear_0(long j);

    private static native long cluster_0(long j, long j2);

    private static native long cluster_1(long j);

    private static native void delete(long j);

    private static native int descriptorsCount_0(long j);

    private static native long getDescriptors_0(long j);

    /* JADX INFO: Access modifiers changed from: protected */
    public BOWTrainer(long addr) {
        this.nativeObj = addr;
    }

    public long getNativeObjAddr() {
        return this.nativeObj;
    }

    public static BOWTrainer __fromPtr__(long addr) {
        return new BOWTrainer(addr);
    }

    public Mat cluster(Mat descriptors) {
        return new Mat(cluster_0(this.nativeObj, descriptors.nativeObj));
    }

    public Mat cluster() {
        return new Mat(cluster_1(this.nativeObj));
    }

    public int descriptorsCount() {
        return descriptorsCount_0(this.nativeObj);
    }

    public List<Mat> getDescriptors() {
        List<Mat> retVal = new ArrayList<>();
        Mat retValMat = new Mat(getDescriptors_0(this.nativeObj));
        Converters.Mat_to_vector_Mat(retValMat, retVal);
        return retVal;
    }

    public void add(Mat descriptors) {
        add_0(this.nativeObj, descriptors.nativeObj);
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
