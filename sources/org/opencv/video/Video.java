package org.opencv.video;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;
/* loaded from: classes5.dex */
public class Video {
    private static final int CV_LKFLOW_GET_MIN_EIGENVALS = 8;
    private static final int CV_LKFLOW_INITIAL_GUESSES = 4;
    public static final int MOTION_AFFINE = 2;
    public static final int MOTION_EUCLIDEAN = 1;
    public static final int MOTION_HOMOGRAPHY = 3;
    public static final int MOTION_TRANSLATION = 0;
    public static final int OPTFLOW_FARNEBACK_GAUSSIAN = 256;
    public static final int OPTFLOW_LK_GET_MIN_EIGENVALS = 8;
    public static final int OPTFLOW_USE_INITIAL_FLOW = 4;

    private static native double[] CamShift_0(long j, int i, int i2, int i3, int i4, double[] dArr, int i5, int i6, double d);

    private static native int buildOpticalFlowPyramid_0(long j, long j2, double d, double d2, int i, boolean z, int i2, int i3, boolean z2);

    private static native int buildOpticalFlowPyramid_1(long j, long j2, double d, double d2, int i, boolean z, int i2, int i3);

    private static native int buildOpticalFlowPyramid_2(long j, long j2, double d, double d2, int i, boolean z, int i2);

    private static native int buildOpticalFlowPyramid_3(long j, long j2, double d, double d2, int i, boolean z);

    private static native int buildOpticalFlowPyramid_4(long j, long j2, double d, double d2, int i);

    private static native void calcOpticalFlowFarneback_0(long j, long j2, long j3, double d, int i, int i2, int i3, int i4, double d2, int i5);

    private static native void calcOpticalFlowPyrLK_0(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i, int i2, int i3, double d3, int i4, double d4);

    private static native void calcOpticalFlowPyrLK_1(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i, int i2, int i3, double d3, int i4);

    private static native void calcOpticalFlowPyrLK_2(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i, int i2, int i3, double d3);

    private static native void calcOpticalFlowPyrLK_3(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i);

    private static native void calcOpticalFlowPyrLK_4(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2);

    private static native void calcOpticalFlowPyrLK_5(long j, long j2, long j3, long j4, long j5, long j6);

    private static native double computeECC_0(long j, long j2, long j3);

    private static native double computeECC_1(long j, long j2);

    private static native long createBackgroundSubtractorKNN_0(int i, double d, boolean z);

    private static native long createBackgroundSubtractorKNN_1(int i, double d);

    private static native long createBackgroundSubtractorKNN_2(int i);

    private static native long createBackgroundSubtractorKNN_3();

    private static native long createBackgroundSubtractorMOG2_0(int i, double d, boolean z);

    private static native long createBackgroundSubtractorMOG2_1(int i, double d);

    private static native long createBackgroundSubtractorMOG2_2(int i);

    private static native long createBackgroundSubtractorMOG2_3();

    private static native double findTransformECC_0(long j, long j2, long j3, int i, int i2, int i3, double d, long j4, int i4);

    private static native int meanShift_0(long j, int i, int i2, int i3, int i4, double[] dArr, int i5, int i6, double d);

    private static native long readOpticalFlow_0(String str);

    private static native boolean writeOpticalFlow_0(String str, long j);

    public static Mat readOpticalFlow(String path) {
        return new Mat(readOpticalFlow_0(path));
    }

    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history, double dist2Threshold, boolean detectShadows) {
        return BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_0(history, dist2Threshold, detectShadows));
    }

    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history, double dist2Threshold) {
        return BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_1(history, dist2Threshold));
    }

    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history) {
        return BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_2(history));
    }

    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN() {
        return BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_3());
    }

    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history, double varThreshold, boolean detectShadows) {
        return BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_0(history, varThreshold, detectShadows));
    }

    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history, double varThreshold) {
        return BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_1(history, varThreshold));
    }

    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history) {
        return BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_2(history));
    }

    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2() {
        return BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_3());
    }

    public static RotatedRect CamShift(Mat probImage, Rect window, TermCriteria criteria) {
        double[] window_out = new double[4];
        RotatedRect retVal = new RotatedRect(CamShift_0(probImage.nativeObj, window.x, window.y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon));
        window.x = (int) window_out[0];
        window.y = (int) window_out[1];
        window.width = (int) window_out[2];
        window.height = (int) window_out[3];
        return retVal;
    }

    public static boolean writeOpticalFlow(String path, Mat flow) {
        return writeOpticalFlow_0(path, flow.nativeObj);
    }

    public static double computeECC(Mat templateImage, Mat inputImage, Mat inputMask) {
        return computeECC_0(templateImage.nativeObj, inputImage.nativeObj, inputMask.nativeObj);
    }

    public static double computeECC(Mat templateImage, Mat inputImage) {
        return computeECC_1(templateImage.nativeObj, inputImage.nativeObj);
    }

    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix, int motionType, TermCriteria criteria, Mat inputMask, int gaussFiltSize) {
        return findTransformECC_0(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj, motionType, criteria.type, criteria.maxCount, criteria.epsilon, inputMask.nativeObj, gaussFiltSize);
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder, boolean tryReuseInputImage) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_0(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder, derivBorder, tryReuseInputImage);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_1(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder, derivBorder);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_2(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_3(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_4(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int meanShift(Mat probImage, Rect window, TermCriteria criteria) {
        double[] window_out = new double[4];
        int retVal = meanShift_0(probImage.nativeObj, window.x, window.y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon);
        window.x = (int) window_out[0];
        window.y = (int) window_out[1];
        window.width = (int) window_out[2];
        window.height = (int) window_out[3];
        return retVal;
    }

    public static void calcOpticalFlowFarneback(Mat prev, Mat next, Mat flow, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags) {
        calcOpticalFlowFarneback_0(prev.nativeObj, next.nativeObj, flow.nativeObj, pyr_scale, levels, winsize, iterations, poly_n, poly_sigma, flags);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria, int flags, double minEigThreshold) {
        calcOpticalFlowPyrLK_0(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon, flags, minEigThreshold);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria, int flags) {
        calcOpticalFlowPyrLK_1(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon, flags);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria) {
        calcOpticalFlowPyrLK_2(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel) {
        calcOpticalFlowPyrLK_3(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize) {
        calcOpticalFlowPyrLK_4(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err) {
        calcOpticalFlowPyrLK_5(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj);
    }
}
