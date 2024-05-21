package com.xiaopeng.xuiservice.cv.algorithm;

import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/* loaded from: classes5.dex */
public class ImageCompare {
    private static final String TAG = ImageCompare.class.getName();

    public static ArrayList<Rect> CompareAndMarkDiff(Mat img1, Mat img2) {
        Mat img = new Mat();
        Mat erodeImg = new Mat();
        Mat dilateImg = new Mat();
        Mat threshImg = new Mat();
        Mat hierarchy = new Mat();
        Mat img1_copy = img1.clone();
        Mat img2_copy = img2.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        if (img1.rows() != 0 && img1.cols() != 0) {
            img1.convertTo(img1, CvType.CV_8UC1);
            img2.convertTo(img2, CvType.CV_8UC1);
            Mat gray1 = new Mat();
            Imgproc.cvtColor(img1, gray1, 6);
            Mat gray2 = new Mat();
            Imgproc.cvtColor(img2, gray2, 6);
            gray1.convertTo(gray1, 5);
            gray2.convertTo(gray2, 5);
            double result = Imgproc.compareHist(gray1, gray2, 0);
            String str = TAG;
            LogUtil.d(str, "similarity: " + result);
            Core.absdiff(img1_copy, img2_copy, img);
            Mat kernel = Imgproc.getStructuringElement(2, new Size(3.0d, 3.0d));
            Imgproc.erode(img, erodeImg, kernel, new Point(-1.0d, -1.0d), 5);
            Mat kernel1 = Imgproc.getStructuringElement(2, new Size(3.0d, 3.0d));
            Imgproc.dilate(erodeImg, dilateImg, kernel1);
            Imgproc.threshold(dilateImg, threshImg, 40.0d, 255.0d, 0);
            Imgproc.cvtColor(threshImg, threshImg, 6);
            Imgproc.findContours(threshImg, contours, hierarchy, 3, 2, new Point(0.0d, 0.0d));
            ArrayList<Rect> boundRect = new ArrayList<>(contours.size());
            for (int i = 0; i < contours.size(); i++) {
                Rect rect = Imgproc.boundingRect(contours.get(i));
                boundRect.add(rect);
            }
            return boundRect;
        }
        return new ArrayList<>();
    }
}
