package org.opencv.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
@TargetApi(21)
/* loaded from: classes5.dex */
public class JavaCamera2View extends CameraBridgeViewBase {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String LOGTAG = "JavaCamera2View";
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraDevice mCameraDevice;
    private String mCameraID;
    private CameraCaptureSession mCaptureSession;
    private ImageReader mImageReader;
    private int mPreviewFormat;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private Size mPreviewSize;
    private final CameraDevice.StateCallback mStateCallback;

    public JavaCamera2View(Context context, int cameraId) {
        super(context, cameraId);
        this.mPreviewFormat = 35;
        this.mPreviewSize = new Size(-1, -1);
        this.mStateCallback = new CameraDevice.StateCallback() { // from class: org.opencv.android.JavaCamera2View.1
            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onOpened(CameraDevice cameraDevice) {
                JavaCamera2View.this.mCameraDevice = cameraDevice;
                JavaCamera2View.this.createCameraPreviewSession();
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
                JavaCamera2View.this.mCameraDevice = null;
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onError(CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                JavaCamera2View.this.mCameraDevice = null;
            }
        };
    }

    public JavaCamera2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPreviewFormat = 35;
        this.mPreviewSize = new Size(-1, -1);
        this.mStateCallback = new CameraDevice.StateCallback() { // from class: org.opencv.android.JavaCamera2View.1
            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onOpened(CameraDevice cameraDevice) {
                JavaCamera2View.this.mCameraDevice = cameraDevice;
                JavaCamera2View.this.createCameraPreviewSession();
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
                JavaCamera2View.this.mCameraDevice = null;
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onError(CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                JavaCamera2View.this.mCameraDevice = null;
            }
        };
    }

    private void startBackgroundThread() {
        Log.i(LOGTAG, "startBackgroundThread");
        stopBackgroundThread();
        this.mBackgroundThread = new HandlerThread("OpenCVCameraBackground");
        this.mBackgroundThread.start();
        this.mBackgroundHandler = new Handler(this.mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        Log.i(LOGTAG, "stopBackgroundThread");
        HandlerThread handlerThread = this.mBackgroundThread;
        if (handlerThread == null) {
            return;
        }
        handlerThread.quitSafely();
        try {
            this.mBackgroundThread.join();
            this.mBackgroundThread = null;
            this.mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(LOGTAG, "stopBackgroundThread", e);
        }
    }

    protected boolean initializeCamera() {
        Log.i(LOGTAG, "initializeCamera");
        CameraManager manager = (CameraManager) getContext().getSystemService("camera");
        try {
            String[] camList = manager.getCameraIdList();
            if (camList.length == 0) {
                Log.e(LOGTAG, "Error: camera isn't detected.");
                return false;
            }
            if (this.mCameraIndex != -1) {
                for (String cameraID : camList) {
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
                    if ((this.mCameraIndex == 99 && ((Integer) characteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 1) || (this.mCameraIndex == 98 && ((Integer) characteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 0)) {
                        this.mCameraID = cameraID;
                        break;
                    }
                }
            } else {
                this.mCameraID = camList[0];
            }
            if (this.mCameraID != null) {
                Log.i(LOGTAG, "Opening camera: " + this.mCameraID);
                manager.openCamera(this.mCameraID, this.mStateCallback, this.mBackgroundHandler);
            } else {
                Log.i(LOGTAG, "Trying to open camera with the value (" + this.mCameraIndex + ")");
                if (this.mCameraIndex < camList.length) {
                    this.mCameraID = camList[this.mCameraIndex];
                    manager.openCamera(this.mCameraID, this.mStateCallback, this.mBackgroundHandler);
                } else {
                    throw new CameraAccessException(2);
                }
            }
            return true;
        } catch (CameraAccessException e) {
            Log.e(LOGTAG, "OpenCamera - Camera Access Exception", e);
            return false;
        } catch (IllegalArgumentException e2) {
            Log.e(LOGTAG, "OpenCamera - Illegal Argument Exception", e2);
            return false;
        } catch (SecurityException e3) {
            Log.e(LOGTAG, "OpenCamera - Security Exception", e3);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createCameraPreviewSession() {
        int w = this.mPreviewSize.getWidth();
        int h = this.mPreviewSize.getHeight();
        Log.i(LOGTAG, "createCameraPreviewSession(" + w + "x" + h + ")");
        if (w < 0 || h < 0) {
            return;
        }
        try {
            if (this.mCameraDevice == null) {
                Log.e(LOGTAG, "createCameraPreviewSession: camera isn't opened");
            } else if (this.mCaptureSession != null) {
                Log.e(LOGTAG, "createCameraPreviewSession: mCaptureSession is already started");
            } else {
                this.mImageReader = ImageReader.newInstance(w, h, this.mPreviewFormat, 2);
                this.mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { // from class: org.opencv.android.JavaCamera2View.2
                    static final /* synthetic */ boolean $assertionsDisabled = false;

                    @Override // android.media.ImageReader.OnImageAvailableListener
                    public void onImageAvailable(ImageReader reader) {
                        Image image = reader.acquireLatestImage();
                        if (image == null) {
                            return;
                        }
                        image.getPlanes();
                        JavaCamera2Frame tempFrame = new JavaCamera2Frame(image);
                        JavaCamera2View.this.deliverAndDrawFrame(tempFrame);
                        tempFrame.release();
                        image.close();
                    }
                }, this.mBackgroundHandler);
                Surface surface = this.mImageReader.getSurface();
                this.mPreviewRequestBuilder = this.mCameraDevice.createCaptureRequest(1);
                this.mPreviewRequestBuilder.addTarget(surface);
                this.mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() { // from class: org.opencv.android.JavaCamera2View.3
                    @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        Log.i(JavaCamera2View.LOGTAG, "createCaptureSession::onConfigured");
                        if (JavaCamera2View.this.mCameraDevice != null) {
                            JavaCamera2View.this.mCaptureSession = cameraCaptureSession;
                            try {
                                JavaCamera2View.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                                JavaCamera2View.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, 2);
                                JavaCamera2View.this.mCaptureSession.setRepeatingRequest(JavaCamera2View.this.mPreviewRequestBuilder.build(), null, JavaCamera2View.this.mBackgroundHandler);
                                Log.i(JavaCamera2View.LOGTAG, "CameraPreviewSession has been started");
                            } catch (Exception e) {
                                Log.e(JavaCamera2View.LOGTAG, "createCaptureSession failed", e);
                            }
                        }
                    }

                    @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        Log.e(JavaCamera2View.LOGTAG, "createCameraPreviewSession failed");
                    }
                }, null);
            }
        } catch (CameraAccessException e) {
            Log.e(LOGTAG, "createCameraPreviewSession", e);
        }
    }

    @Override // org.opencv.android.CameraBridgeViewBase
    protected void disconnectCamera() {
        Log.i(LOGTAG, "close camera");
        try {
            CameraDevice c = this.mCameraDevice;
            this.mCameraDevice = null;
            if (this.mCaptureSession != null) {
                this.mCaptureSession.close();
                this.mCaptureSession = null;
            }
            if (c != null) {
                c.close();
            }
            Log.i(LOGTAG, "camera closed!");
        } finally {
            stopBackgroundThread();
            ImageReader imageReader = this.mImageReader;
            if (imageReader != null) {
                imageReader.close();
                this.mImageReader = null;
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class JavaCameraSizeAccessor implements CameraBridgeViewBase.ListItemAccessor {
        @Override // org.opencv.android.CameraBridgeViewBase.ListItemAccessor
        public int getWidth(Object obj) {
            Size size = (Size) obj;
            return size.getWidth();
        }

        @Override // org.opencv.android.CameraBridgeViewBase.ListItemAccessor
        public int getHeight(Object obj) {
            Size size = (Size) obj;
            return size.getHeight();
        }
    }

    boolean calcPreviewSize(int width, int height) {
        Log.i(LOGTAG, "calcPreviewSize: " + width + "x" + height);
        if (this.mCameraID == null) {
            Log.e(LOGTAG, "Camera isn't initialized!");
            return false;
        }
        CameraManager manager = (CameraManager) getContext().getSystemService("camera");
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(this.mCameraID);
            StreamConfigurationMap map = (StreamConfigurationMap) characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(ImageReader.class);
            List<Size> sizes_list = Arrays.asList(sizes);
            org.opencv.core.Size frameSize = calculateCameraFrameSize(sizes_list, new JavaCameraSizeAccessor(), width, height);
            Log.i(LOGTAG, "Selected preview size to " + Integer.valueOf((int) frameSize.width) + "x" + Integer.valueOf((int) frameSize.height));
            if (this.mPreviewSize.getWidth() == frameSize.width && this.mPreviewSize.getHeight() == frameSize.height) {
                return false;
            }
            this.mPreviewSize = new Size((int) frameSize.width, (int) frameSize.height);
            return true;
        } catch (CameraAccessException e) {
            Log.e(LOGTAG, "calcPreviewSize - Camera Access Exception", e);
            return false;
        } catch (IllegalArgumentException e2) {
            Log.e(LOGTAG, "calcPreviewSize - Illegal Argument Exception", e2);
            return false;
        } catch (SecurityException e3) {
            Log.e(LOGTAG, "calcPreviewSize - Security Exception", e3);
            return false;
        }
    }

    @Override // org.opencv.android.CameraBridgeViewBase
    protected boolean connectCamera(int width, int height) {
        Log.i(LOGTAG, "setCameraPreviewSize(" + width + "x" + height + ")");
        startBackgroundThread();
        initializeCamera();
        try {
            boolean needReconfig = calcPreviewSize(width, height);
            this.mFrameWidth = this.mPreviewSize.getWidth();
            this.mFrameHeight = this.mPreviewSize.getHeight();
            if (getLayoutParams().width == -1 && getLayoutParams().height == -1) {
                this.mScale = Math.min(height / this.mFrameHeight, width / this.mFrameWidth);
            } else {
                this.mScale = 0.0f;
            }
            AllocateCache();
            if (needReconfig) {
                if (this.mCaptureSession != null) {
                    Log.d(LOGTAG, "closing existing previewSession");
                    this.mCaptureSession.close();
                    this.mCaptureSession = null;
                }
                createCameraPreviewSession();
                return true;
            }
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Interrupted while setCameraPreviewSize.", e);
        }
    }

    /* loaded from: classes5.dex */
    private class JavaCamera2Frame implements CameraBridgeViewBase.CvCameraViewFrame {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private Image mImage;
        private Mat mRgba = new Mat();
        private Mat mGray = new Mat();

        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
        public Mat gray() {
            Image.Plane[] planes = this.mImage.getPlanes();
            int w = this.mImage.getWidth();
            int h = this.mImage.getHeight();
            ByteBuffer y_plane = planes[0].getBuffer();
            int y_plane_step = planes[0].getRowStride();
            this.mGray = new Mat(h, w, CvType.CV_8UC1, y_plane, y_plane_step);
            return this.mGray;
        }

        /* JADX WARN: Incorrect condition in loop: B:32:0x0121 */
        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public org.opencv.core.Mat rgba() {
            /*
                Method dump skipped, instructions count: 342
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.opencv.android.JavaCamera2View.JavaCamera2Frame.rgba():org.opencv.core.Mat");
        }

        public JavaCamera2Frame(Image image) {
            this.mImage = image;
        }

        public void release() {
            this.mRgba.release();
            this.mGray.release();
        }
    }
}
