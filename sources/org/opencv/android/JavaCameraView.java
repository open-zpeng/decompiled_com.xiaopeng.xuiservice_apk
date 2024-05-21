package org.opencv.android;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import org.opencv.BuildConfig;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/* loaded from: classes5.dex */
public class JavaCameraView extends CameraBridgeViewBase implements Camera.PreviewCallback {
    private static final int MAGIC_TEXTURE_ID = 10;
    private static final String TAG = "JavaCameraView";
    private byte[] mBuffer;
    protected Camera mCamera;
    protected JavaCameraFrame[] mCameraFrame;
    private boolean mCameraFrameReady;
    private int mChainIdx;
    private Mat[] mFrameChain;
    private int mPreviewFormat;
    private boolean mStopThread;
    private SurfaceTexture mSurfaceTexture;
    private Thread mThread;

    /* loaded from: classes5.dex */
    public static class JavaCameraSizeAccessor implements CameraBridgeViewBase.ListItemAccessor {
        @Override // org.opencv.android.CameraBridgeViewBase.ListItemAccessor
        public int getWidth(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.width;
        }

        @Override // org.opencv.android.CameraBridgeViewBase.ListItemAccessor
        public int getHeight(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.height;
        }
    }

    public JavaCameraView(Context context, int cameraId) {
        super(context, cameraId);
        this.mChainIdx = 0;
        this.mPreviewFormat = 17;
        this.mCameraFrameReady = false;
    }

    public JavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mChainIdx = 0;
        this.mPreviewFormat = 17;
        this.mCameraFrameReady = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x0287 A[Catch: Exception -> 0x0334, all -> 0x033b, TryCatch #4 {Exception -> 0x0334, blocks: (B:63:0x015f, B:65:0x0172, B:67:0x0185, B:69:0x018f, B:71:0x0199, B:73:0x01a3, B:75:0x01ad, B:77:0x01b7, B:79:0x01c1, B:81:0x01cb, B:84:0x01d6, B:86:0x01e2, B:88:0x0223, B:90:0x022d, B:91:0x0230, B:93:0x0236, B:95:0x023e, B:96:0x0243, B:98:0x0267, B:100:0x026f, B:102:0x0283, B:104:0x0287, B:105:0x0290, B:107:0x030e, B:109:0x0325, B:108:0x031f, B:101:0x0280, B:85:0x01dc), top: B:128:0x015f, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x030e A[Catch: Exception -> 0x0334, all -> 0x033b, TryCatch #4 {Exception -> 0x0334, blocks: (B:63:0x015f, B:65:0x0172, B:67:0x0185, B:69:0x018f, B:71:0x0199, B:73:0x01a3, B:75:0x01ad, B:77:0x01b7, B:79:0x01c1, B:81:0x01cb, B:84:0x01d6, B:86:0x01e2, B:88:0x0223, B:90:0x022d, B:91:0x0230, B:93:0x0236, B:95:0x023e, B:96:0x0243, B:98:0x0267, B:100:0x026f, B:102:0x0283, B:104:0x0287, B:105:0x0290, B:107:0x030e, B:109:0x0325, B:108:0x031f, B:101:0x0280, B:85:0x01dc), top: B:128:0x015f, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:108:0x031f A[Catch: Exception -> 0x0334, all -> 0x033b, TryCatch #4 {Exception -> 0x0334, blocks: (B:63:0x015f, B:65:0x0172, B:67:0x0185, B:69:0x018f, B:71:0x0199, B:73:0x01a3, B:75:0x01ad, B:77:0x01b7, B:79:0x01c1, B:81:0x01cb, B:84:0x01d6, B:86:0x01e2, B:88:0x0223, B:90:0x022d, B:91:0x0230, B:93:0x0236, B:95:0x023e, B:96:0x0243, B:98:0x0267, B:100:0x026f, B:102:0x0283, B:104:0x0287, B:105:0x0290, B:107:0x030e, B:109:0x0325, B:108:0x031f, B:101:0x0280, B:85:0x01dc), top: B:128:0x015f, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x015f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x015d A[Catch: all -> 0x033b, DONT_GENERATE, TRY_LEAVE, TryCatch #3 {, blocks: (B:5:0x0010, B:7:0x001b, B:8:0x0022, B:12:0x0044, B:14:0x0048, B:17:0x004f, B:19:0x0055, B:20:0x0074, B:26:0x00a3, B:23:0x007e, B:59:0x0159, B:61:0x015d, B:63:0x015f, B:65:0x0172, B:67:0x0185, B:69:0x018f, B:71:0x0199, B:73:0x01a3, B:75:0x01ad, B:77:0x01b7, B:79:0x01c1, B:81:0x01cb, B:84:0x01d6, B:86:0x01e2, B:88:0x0223, B:90:0x022d, B:91:0x0230, B:93:0x0236, B:95:0x023e, B:96:0x0243, B:98:0x0267, B:100:0x026f, B:102:0x0283, B:104:0x0287, B:105:0x0290, B:107:0x030e, B:109:0x0325, B:116:0x0339, B:108:0x031f, B:101:0x0280, B:85:0x01dc, B:115:0x0336, B:11:0x002a, B:28:0x00a8, B:30:0x00ac, B:32:0x00b6, B:33:0x00c3, B:35:0x00c9, B:38:0x00d2, B:51:0x00fe, B:53:0x0108, B:54:0x0110, B:55:0x012f, B:58:0x0137, B:40:0x00d7, B:42:0x00db, B:43:0x00e8, B:45:0x00ee, B:48:0x00f8), top: B:127:0x0010, inners: #0, #1, #2, #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean initializeCamera(int r17, int r18) {
        /*
            Method dump skipped, instructions count: 830
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.opencv.android.JavaCameraView.initializeCamera(int, int):boolean");
    }

    protected void releaseCamera() {
        synchronized (this) {
            if (this.mCamera != null) {
                this.mCamera.stopPreview();
                this.mCamera.setPreviewCallback(null);
                this.mCamera.release();
            }
            this.mCamera = null;
            if (this.mFrameChain != null) {
                this.mFrameChain[0].release();
                this.mFrameChain[1].release();
            }
            if (this.mCameraFrame != null) {
                this.mCameraFrame[0].release();
                this.mCameraFrame[1].release();
            }
        }
    }

    @Override // org.opencv.android.CameraBridgeViewBase
    protected boolean connectCamera(int width, int height) {
        Log.d(TAG, "Connecting to camera");
        if (initializeCamera(width, height)) {
            this.mCameraFrameReady = false;
            Log.d(TAG, "Starting processing thread");
            this.mStopThread = false;
            this.mThread = new Thread(new CameraWorker());
            this.mThread.start();
            return true;
        }
        return false;
    }

    @Override // org.opencv.android.CameraBridgeViewBase
    protected void disconnectCamera() {
        Log.d(TAG, "Disconnecting from camera");
        try {
            try {
                this.mStopThread = true;
                Log.d(TAG, "Notify thread");
                synchronized (this) {
                    notify();
                }
                Log.d(TAG, "Waiting for thread");
                if (this.mThread != null) {
                    this.mThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mThread = null;
            releaseCamera();
            this.mCameraFrameReady = false;
        } catch (Throwable th) {
            this.mThread = null;
            throw th;
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] frame, Camera arg1) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Preview Frame received. Frame size: " + frame.length);
        }
        synchronized (this) {
            this.mFrameChain[this.mChainIdx].put(0, 0, frame);
            this.mCameraFrameReady = true;
            notify();
        }
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.addCallbackBuffer(this.mBuffer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class JavaCameraFrame implements CameraBridgeViewBase.CvCameraViewFrame {
        private int mHeight;
        private Mat mRgba = new Mat();
        private int mWidth;
        private Mat mYuvFrameData;

        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
        public Mat gray() {
            return this.mYuvFrameData.submat(0, this.mHeight, 0, this.mWidth);
        }

        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
        public Mat rgba() {
            if (JavaCameraView.this.mPreviewFormat != 17) {
                if (JavaCameraView.this.mPreviewFormat == 842094169) {
                    Imgproc.cvtColor(this.mYuvFrameData, this.mRgba, 100, 4);
                } else {
                    throw new IllegalArgumentException("Preview Format can be NV21 or YV12");
                }
            } else {
                Imgproc.cvtColor(this.mYuvFrameData, this.mRgba, 96, 4);
            }
            return this.mRgba;
        }

        public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
            this.mYuvFrameData = Yuv420sp;
        }

        public void release() {
            this.mRgba.release();
        }
    }

    /* loaded from: classes5.dex */
    private class CameraWorker implements Runnable {
        private CameraWorker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            do {
                boolean hasFrame = false;
                synchronized (JavaCameraView.this) {
                    while (!JavaCameraView.this.mCameraFrameReady && !JavaCameraView.this.mStopThread) {
                        try {
                            JavaCameraView.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (JavaCameraView.this.mCameraFrameReady) {
                        JavaCameraView.this.mChainIdx = 1 - JavaCameraView.this.mChainIdx;
                        JavaCameraView.this.mCameraFrameReady = false;
                        hasFrame = true;
                    }
                }
                if (!JavaCameraView.this.mStopThread && hasFrame && !JavaCameraView.this.mFrameChain[1 - JavaCameraView.this.mChainIdx].empty()) {
                    JavaCameraView javaCameraView = JavaCameraView.this;
                    javaCameraView.deliverAndDrawFrame(javaCameraView.mCameraFrame[1 - JavaCameraView.this.mChainIdx]);
                }
            } while (!JavaCameraView.this.mStopThread);
            Log.d(JavaCameraView.TAG, "Finish processing thread");
        }
    }
}
