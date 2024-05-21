package org.opencv.dnn;

import org.opencv.core.Mat;
/* loaded from: classes5.dex */
public class SegmentationModel extends Model {
    private static native long SegmentationModel_0(long j);

    private static native long SegmentationModel_1(String str, String str2);

    private static native long SegmentationModel_2(String str);

    private static native void delete(long j);

    private static native void segment_0(long j, long j2, long j3);

    protected SegmentationModel(long addr) {
        super(addr);
    }

    public static SegmentationModel __fromPtr__(long addr) {
        return new SegmentationModel(addr);
    }

    public SegmentationModel(Net network) {
        super(SegmentationModel_0(network.nativeObj));
    }

    public SegmentationModel(String model, String config) {
        super(SegmentationModel_1(model, config));
    }

    public SegmentationModel(String model) {
        super(SegmentationModel_2(model));
    }

    public void segment(Mat frame, Mat mask) {
        segment_0(this.nativeObj, frame.nativeObj, mask.nativeObj);
    }

    @Override // org.opencv.dnn.Model, org.opencv.dnn.Net
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
