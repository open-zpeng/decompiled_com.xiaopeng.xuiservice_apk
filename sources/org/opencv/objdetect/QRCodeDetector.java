package org.opencv.objdetect;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;
/* loaded from: classes5.dex */
public class QRCodeDetector {
    protected final long nativeObj;

    private static native long QRCodeDetector_0();

    private static native boolean decodeMulti_0(long j, long j2, long j3, List<String> list, long j4);

    private static native boolean decodeMulti_1(long j, long j2, long j3, List<String> list);

    private static native String decode_0(long j, long j2, long j3, long j4);

    private static native String decode_1(long j, long j2, long j3);

    private static native void delete(long j);

    private static native boolean detectAndDecodeMulti_0(long j, long j2, List<String> list, long j3, long j4);

    private static native boolean detectAndDecodeMulti_1(long j, long j2, List<String> list, long j3);

    private static native boolean detectAndDecodeMulti_2(long j, long j2, List<String> list);

    private static native String detectAndDecode_0(long j, long j2, long j3, long j4);

    private static native String detectAndDecode_1(long j, long j2, long j3);

    private static native String detectAndDecode_2(long j, long j2);

    private static native boolean detectMulti_0(long j, long j2, long j3);

    private static native boolean detect_0(long j, long j2, long j3);

    private static native void setEpsX_0(long j, double d);

    private static native void setEpsY_0(long j, double d);

    protected QRCodeDetector(long addr) {
        this.nativeObj = addr;
    }

    public long getNativeObjAddr() {
        return this.nativeObj;
    }

    public static QRCodeDetector __fromPtr__(long addr) {
        return new QRCodeDetector(addr);
    }

    public QRCodeDetector() {
        this.nativeObj = QRCodeDetector_0();
    }

    public boolean decodeMulti(Mat img, Mat points, List<String> decoded_info, List<Mat> straight_qrcode) {
        Mat straight_qrcode_mat = new Mat();
        boolean retVal = decodeMulti_0(this.nativeObj, img.nativeObj, points.nativeObj, decoded_info, straight_qrcode_mat.nativeObj);
        Converters.Mat_to_vector_Mat(straight_qrcode_mat, straight_qrcode);
        straight_qrcode_mat.release();
        return retVal;
    }

    public boolean decodeMulti(Mat img, Mat points, List<String> decoded_info) {
        return decodeMulti_1(this.nativeObj, img.nativeObj, points.nativeObj, decoded_info);
    }

    public boolean detect(Mat img, Mat points) {
        return detect_0(this.nativeObj, img.nativeObj, points.nativeObj);
    }

    public boolean detectAndDecodeMulti(Mat img, List<String> decoded_info, Mat points, List<Mat> straight_qrcode) {
        Mat straight_qrcode_mat = new Mat();
        boolean retVal = detectAndDecodeMulti_0(this.nativeObj, img.nativeObj, decoded_info, points.nativeObj, straight_qrcode_mat.nativeObj);
        Converters.Mat_to_vector_Mat(straight_qrcode_mat, straight_qrcode);
        straight_qrcode_mat.release();
        return retVal;
    }

    public boolean detectAndDecodeMulti(Mat img, List<String> decoded_info, Mat points) {
        return detectAndDecodeMulti_1(this.nativeObj, img.nativeObj, decoded_info, points.nativeObj);
    }

    public boolean detectAndDecodeMulti(Mat img, List<String> decoded_info) {
        return detectAndDecodeMulti_2(this.nativeObj, img.nativeObj, decoded_info);
    }

    public boolean detectMulti(Mat img, Mat points) {
        return detectMulti_0(this.nativeObj, img.nativeObj, points.nativeObj);
    }

    public String decode(Mat img, Mat points, Mat straight_qrcode) {
        return decode_0(this.nativeObj, img.nativeObj, points.nativeObj, straight_qrcode.nativeObj);
    }

    public String decode(Mat img, Mat points) {
        return decode_1(this.nativeObj, img.nativeObj, points.nativeObj);
    }

    public String detectAndDecode(Mat img, Mat points, Mat straight_qrcode) {
        return detectAndDecode_0(this.nativeObj, img.nativeObj, points.nativeObj, straight_qrcode.nativeObj);
    }

    public String detectAndDecode(Mat img, Mat points) {
        return detectAndDecode_1(this.nativeObj, img.nativeObj, points.nativeObj);
    }

    public String detectAndDecode(Mat img) {
        return detectAndDecode_2(this.nativeObj, img.nativeObj);
    }

    public void setEpsX(double epsX) {
        setEpsX_0(this.nativeObj, epsX);
    }

    public void setEpsY(double epsY) {
        setEpsY_0(this.nativeObj, epsY);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
