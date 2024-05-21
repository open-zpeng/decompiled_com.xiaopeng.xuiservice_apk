package org.opencv.dnn;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
/* loaded from: classes5.dex */
public class KeypointsModel extends Model {
    private static native long KeypointsModel_0(long j);

    private static native long KeypointsModel_1(String str, String str2);

    private static native long KeypointsModel_2(String str);

    private static native void delete(long j);

    private static native long estimate_0(long j, long j2, float f);

    private static native long estimate_1(long j, long j2);

    protected KeypointsModel(long addr) {
        super(addr);
    }

    public static KeypointsModel __fromPtr__(long addr) {
        return new KeypointsModel(addr);
    }

    public KeypointsModel(Net network) {
        super(KeypointsModel_0(network.nativeObj));
    }

    public KeypointsModel(String model, String config) {
        super(KeypointsModel_1(model, config));
    }

    public KeypointsModel(String model) {
        super(KeypointsModel_2(model));
    }

    public MatOfPoint2f estimate(Mat frame, float thresh) {
        return MatOfPoint2f.fromNativeAddr(estimate_0(this.nativeObj, frame.nativeObj, thresh));
    }

    public MatOfPoint2f estimate(Mat frame) {
        return MatOfPoint2f.fromNativeAddr(estimate_1(this.nativeObj, frame.nativeObj));
    }

    @Override // org.opencv.dnn.Model, org.opencv.dnn.Net
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
