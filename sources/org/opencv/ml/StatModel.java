package org.opencv.ml;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
/* loaded from: classes5.dex */
public class StatModel extends Algorithm {
    public static final int COMPRESSED_INPUT = 2;
    public static final int PREPROCESSED_INPUT = 4;
    public static final int RAW_OUTPUT = 1;
    public static final int UPDATE_MODEL = 1;

    private static native float calcError_0(long j, long j2, boolean z, long j3);

    private static native void delete(long j);

    private static native boolean empty_0(long j);

    private static native int getVarCount_0(long j);

    private static native boolean isClassifier_0(long j);

    private static native boolean isTrained_0(long j);

    private static native float predict_0(long j, long j2, long j3, int i);

    private static native float predict_1(long j, long j2, long j3);

    private static native float predict_2(long j, long j2);

    private static native boolean train_0(long j, long j2, int i, long j3);

    private static native boolean train_1(long j, long j2, int i);

    private static native boolean train_2(long j, long j2);

    /* JADX INFO: Access modifiers changed from: protected */
    public StatModel(long addr) {
        super(addr);
    }

    public static StatModel __fromPtr__(long addr) {
        return new StatModel(addr);
    }

    @Override // org.opencv.core.Algorithm
    public boolean empty() {
        return empty_0(this.nativeObj);
    }

    public boolean isClassifier() {
        return isClassifier_0(this.nativeObj);
    }

    public boolean isTrained() {
        return isTrained_0(this.nativeObj);
    }

    public boolean train(Mat samples, int layout, Mat responses) {
        return train_0(this.nativeObj, samples.nativeObj, layout, responses.nativeObj);
    }

    public boolean train(TrainData trainData, int flags) {
        return train_1(this.nativeObj, trainData.getNativeObjAddr(), flags);
    }

    public boolean train(TrainData trainData) {
        return train_2(this.nativeObj, trainData.getNativeObjAddr());
    }

    public float calcError(TrainData data, boolean test, Mat resp) {
        return calcError_0(this.nativeObj, data.getNativeObjAddr(), test, resp.nativeObj);
    }

    public float predict(Mat samples, Mat results, int flags) {
        return predict_0(this.nativeObj, samples.nativeObj, results.nativeObj, flags);
    }

    public float predict(Mat samples, Mat results) {
        return predict_1(this.nativeObj, samples.nativeObj, results.nativeObj);
    }

    public float predict(Mat samples) {
        return predict_2(this.nativeObj, samples.nativeObj);
    }

    public int getVarCount() {
        return getVarCount_0(this.nativeObj);
    }

    @Override // org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
