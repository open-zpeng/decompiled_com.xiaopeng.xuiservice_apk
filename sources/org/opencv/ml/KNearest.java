package org.opencv.ml;

import org.opencv.core.Mat;
/* loaded from: classes5.dex */
public class KNearest extends StatModel {
    public static final int BRUTE_FORCE = 1;
    public static final int KDTREE = 2;

    private static native long create_0();

    private static native void delete(long j);

    private static native float findNearest_0(long j, long j2, int i, long j3, long j4, long j5);

    private static native float findNearest_1(long j, long j2, int i, long j3, long j4);

    private static native float findNearest_2(long j, long j2, int i, long j3);

    private static native int getAlgorithmType_0(long j);

    private static native int getDefaultK_0(long j);

    private static native int getEmax_0(long j);

    private static native boolean getIsClassifier_0(long j);

    private static native long load_0(String str);

    private static native void setAlgorithmType_0(long j, int i);

    private static native void setDefaultK_0(long j, int i);

    private static native void setEmax_0(long j, int i);

    private static native void setIsClassifier_0(long j, boolean z);

    protected KNearest(long addr) {
        super(addr);
    }

    public static KNearest __fromPtr__(long addr) {
        return new KNearest(addr);
    }

    public static KNearest create() {
        return __fromPtr__(create_0());
    }

    public static KNearest load(String filepath) {
        return __fromPtr__(load_0(filepath));
    }

    public boolean getIsClassifier() {
        return getIsClassifier_0(this.nativeObj);
    }

    public float findNearest(Mat samples, int k, Mat results, Mat neighborResponses, Mat dist) {
        return findNearest_0(this.nativeObj, samples.nativeObj, k, results.nativeObj, neighborResponses.nativeObj, dist.nativeObj);
    }

    public float findNearest(Mat samples, int k, Mat results, Mat neighborResponses) {
        return findNearest_1(this.nativeObj, samples.nativeObj, k, results.nativeObj, neighborResponses.nativeObj);
    }

    public float findNearest(Mat samples, int k, Mat results) {
        return findNearest_2(this.nativeObj, samples.nativeObj, k, results.nativeObj);
    }

    public int getAlgorithmType() {
        return getAlgorithmType_0(this.nativeObj);
    }

    public int getDefaultK() {
        return getDefaultK_0(this.nativeObj);
    }

    public int getEmax() {
        return getEmax_0(this.nativeObj);
    }

    public void setAlgorithmType(int val) {
        setAlgorithmType_0(this.nativeObj, val);
    }

    public void setDefaultK(int val) {
        setDefaultK_0(this.nativeObj, val);
    }

    public void setEmax(int val) {
        setEmax_0(this.nativeObj, val);
    }

    public void setIsClassifier(boolean val) {
        setIsClassifier_0(this.nativeObj, val);
    }

    @Override // org.opencv.ml.StatModel, org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
