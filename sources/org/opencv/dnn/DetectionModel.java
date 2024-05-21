package org.opencv.dnn;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
/* loaded from: classes5.dex */
public class DetectionModel extends Model {
    private static native long DetectionModel_0(long j);

    private static native long DetectionModel_1(String str, String str2);

    private static native long DetectionModel_2(String str);

    private static native void delete(long j);

    private static native void detect_0(long j, long j2, long j3, long j4, long j5, float f, float f2);

    private static native void detect_1(long j, long j2, long j3, long j4, long j5, float f);

    private static native void detect_2(long j, long j2, long j3, long j4, long j5);

    protected DetectionModel(long addr) {
        super(addr);
    }

    public static DetectionModel __fromPtr__(long addr) {
        return new DetectionModel(addr);
    }

    public DetectionModel(Net network) {
        super(DetectionModel_0(network.nativeObj));
    }

    public DetectionModel(String model, String config) {
        super(DetectionModel_1(model, config));
    }

    public DetectionModel(String model) {
        super(DetectionModel_2(model));
    }

    public void detect(Mat frame, MatOfInt classIds, MatOfFloat confidences, MatOfRect boxes, float confThreshold, float nmsThreshold) {
        detect_0(this.nativeObj, frame.nativeObj, classIds.nativeObj, confidences.nativeObj, boxes.nativeObj, confThreshold, nmsThreshold);
    }

    public void detect(Mat frame, MatOfInt classIds, MatOfFloat confidences, MatOfRect boxes, float confThreshold) {
        detect_1(this.nativeObj, frame.nativeObj, classIds.nativeObj, confidences.nativeObj, boxes.nativeObj, confThreshold);
    }

    public void detect(Mat frame, MatOfInt classIds, MatOfFloat confidences, MatOfRect boxes) {
        detect_2(this.nativeObj, frame.nativeObj, classIds.nativeObj, confidences.nativeObj, boxes.nativeObj);
    }

    @Override // org.opencv.dnn.Model, org.opencv.dnn.Net
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
