package com.xiaopeng.xuiservice.cv;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.cv.CameraManager;
import com.xiaopeng.xuiservice.cv.algorithm.ImageCompare;
import com.xiaopeng.xuiservice.cv.utils.ImageUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
/* loaded from: classes5.dex */
public class SmartAvmManager {
    private static final String TAG = SmartAvmManager.class.getName();
    private static volatile SmartAvmManager sInstance = null;
    private static final String strLibraryName = "opencv_java4";
    public CameraManager mCamera;
    private SmartAvmEventListener mListener;
    private CameraManager.PreviewEventListener mPreviewEventListener;
    private int mType = 0;
    private Mat prevImage = new Mat();
    private int prevPosition = 0;

    /* loaded from: classes5.dex */
    public interface SmartAvmEventListener {
        void onMotionTrackingCallBack(int i);
    }

    static {
        try {
            System.loadLibrary(strLibraryName);
        } catch (Exception e) {
            LogUtil.e(TAG, "loadLibrary Exception: " + e);
        } catch (UnsatisfiedLinkError e2) {
            LogUtil.e(TAG, "Native code library failed to load.\n" + e2);
        }
    }

    public static SmartAvmManager getInstance() {
        if (sInstance == null) {
            synchronized (SmartAvmManager.class) {
                if (sInstance == null) {
                    sInstance = new SmartAvmManager();
                }
            }
        }
        return sInstance;
    }

    public void initSmartAvmManager(int type) throws IOException {
        this.mType = type;
        this.mPreviewEventListener = new CameraManager.PreviewEventListener() { // from class: com.xiaopeng.xuiservice.cv.SmartAvmManager.1
            @Override // com.xiaopeng.xuiservice.cv.CameraManager.PreviewEventListener
            public void onPreviewCallBack(byte[] bytes, Camera camera) {
                if (SmartAvmManager.this.mType == 1) {
                    SmartAvmManager.this.handleMotionTrackingEvent(bytes, camera);
                }
            }
        };
        this.mCamera = CameraManager.getInstance();
        this.mCamera.registerListener(this.mPreviewEventListener);
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        CameraManager cameraManager = this.mCamera;
        if (cameraManager != null) {
            cameraManager.setSurfaceHolder(holder);
        }
    }

    public void startSmartAvm() {
        LogUtil.d(TAG, "startSmartAvm");
        CameraManager cameraManager = this.mCamera;
        if (cameraManager != null) {
            try {
                cameraManager.openCamera();
                this.mCamera.setCameraView(16);
                this.mCamera.setSurfaceTexture();
                this.mCamera.startPreview();
            } catch (Exception e) {
                LogUtil.e(TAG, "startSmartAvm error");
            }
        }
    }

    public void stopSmartAvm() {
        LogUtil.d(TAG, "stopSmartAvm");
        this.mCamera.stopPreview();
        this.mCamera.closeCamera();
        onMotionTrackingEvent(-1);
        this.mType = 0;
        this.mListener = null;
    }

    public synchronized void registerListener(SmartAvmEventListener listener) {
        LogUtil.d(TAG, "register SmartAvmEventListener");
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }
        this.mListener = listener;
    }

    private void onMotionTrackingEvent(int position) {
        SmartAvmEventListener smartAvmEventListener = this.mListener;
        if (smartAvmEventListener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }
        smartAvmEventListener.onMotionTrackingCallBack(position);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMotionTrackingEvent(byte[] bytes, Camera camera) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        Bitmap bitmap = ImageUtils.ByteToBitmap(bytes, previewSize);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 300, 0, bitmap.getWidth() - (300 * 2), bitmap.getHeight(), (Matrix) null, true);
        if (bitmap2 != null) {
            Rect maxRect = new Rect();
            Mat inputFrame = new Mat();
            Utils.bitmapToMat(bitmap2, inputFrame);
            if (!bitmap2.isRecycled()) {
                bitmap2.recycle();
            }
            ArrayList<Rect> arraysRect = ImageCompare.CompareAndMarkDiff(this.prevImage, inputFrame);
            this.prevImage = inputFrame;
            if (arraysRect.size() > 0) {
                double maxRectArea = 0.0d;
                Iterator<Rect> it = arraysRect.iterator();
                while (it.hasNext()) {
                    Rect rect = it.next();
                    double tmp = rect.area();
                    if (tmp >= maxRectArea) {
                        maxRectArea = tmp;
                        maxRect = rect;
                    }
                }
                int range = previewSize.width - (300 * 2);
                int pos = ((maxRect.x + (maxRect.width / 2)) * 100) / range;
                String str = TAG;
                LogUtil.d(str, "position(0-100) is " + pos);
                if (Math.abs(pos - this.prevPosition) > 5) {
                    this.prevPosition = pos;
                    onMotionTrackingEvent(100 - pos);
                }
            }
        }
    }
}
