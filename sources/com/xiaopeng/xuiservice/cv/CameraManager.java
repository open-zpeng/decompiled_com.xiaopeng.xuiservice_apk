package com.xiaopeng.xuiservice.cv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.IOException;
import java.util.List;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class CameraManager implements Camera.PreviewCallback {
    private static final int IMAGE_CROP_SIZE = 300;
    private static final int MAGIC_TEXTURE_ID = 10;
    private static final int PREVIEW_FRAME_COUNT = 3;
    private static final String TAG = CameraManager.class.getName();
    private static volatile CameraManager sInstance;
    private Camera camera;
    private int cameraPosition;
    private boolean initialized;
    private Context mContext;
    private PreviewEventListener mListener;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceTexture mSurfaceTexture;
    private Camera.Parameters parameters;
    private boolean previewing;
    private int requestedCameraId = 0;
    private int numCameras = Camera.getNumberOfCameras();
    private Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    private int mPreviewCount = 0;

    /* loaded from: classes5.dex */
    public interface PreviewEventListener {
        void onPreviewCallBack(byte[] bArr, Camera camera);
    }

    public synchronized void registerListener(PreviewEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }
        this.mListener = listener;
    }

    public static CameraManager getInstance() {
        if (sInstance == null) {
            synchronized (CameraManager.class) {
                if (sInstance == null) {
                    sInstance = new CameraManager();
                }
            }
        }
        return sInstance;
    }

    private Camera open(int cameraId) {
        Camera camera;
        int i = this.numCameras;
        if (i <= 0) {
            LogUtil.e(TAG, "No cameras!");
            return null;
        }
        boolean explicitRequest = cameraId >= 0 && cameraId < i;
        int index = 0;
        if (!explicitRequest) {
            while (true) {
                if (index >= this.numCameras) {
                    break;
                }
                Camera.getCameraInfo(index, this.cameraInfo);
                cameraId = index;
                if (this.cameraInfo.facing == 0) {
                    this.cameraPosition = 1;
                    break;
                } else if (this.cameraInfo.facing == 1) {
                    this.cameraPosition = 0;
                } else {
                    index++;
                }
            }
        }
        if (index < this.numCameras) {
            String str = TAG;
            LogUtil.d(str, "Opeing camera #" + cameraId);
            camera = Camera.open(cameraId);
        } else {
            String str2 = TAG;
            LogUtil.d(str2, "Requested camera does not exist:" + cameraId);
            camera = null;
        }
        camera.setPreviewCallback(this);
        return camera;
    }

    public void setCameraView(int type) {
        Camera camera = this.camera;
        if (camera != null) {
            Camera.Parameters parameters = camera.getCmdControlParameters();
            parameters.setAvmDisplayMode(type);
            parameters.setAvmOverlayWorkSt(0);
            parameters.setAvmTransparentChassisWorkSt(0);
            this.camera.setCmdControlParameters(parameters);
        }
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        this.mSurfaceHolder = holder;
    }

    public void setPreviewDisplay() {
        SurfaceHolder surfaceHolder;
        Camera camera = this.camera;
        if (camera != null && (surfaceHolder = this.mSurfaceHolder) != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                LogUtil.e(TAG, "setPreviewDisplay error");
            }
        }
    }

    public void openCamera() throws IOException {
        if (this.camera == null) {
            Camera theCamera = open(this.requestedCameraId);
            if (theCamera == null) {
                throw new IOException();
            }
            this.camera = theCamera;
        }
        if (!this.initialized) {
            this.initialized = true;
            this.parameters = this.camera.getParameters();
            List<Camera.Size> previewSizes = this.parameters.getSupportedPreviewSizes();
            for (Camera.Size size : previewSizes) {
                LogUtil.d("TAG", "previewSizes width:" + size.width);
                LogUtil.d("TAG", "previewSizes height:" + size.height);
                if (size.width - Videoio.CAP_PVAPI <= 200 && size.width >= 800) {
                    int w = size.width;
                    int h = size.height;
                    return;
                }
            }
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        int i = this.mPreviewCount + 1;
        this.mPreviewCount = i;
        if (i % 3 != 0) {
            return;
        }
        this.mPreviewCount = 0;
        this.mListener.onPreviewCallBack(bytes, camera);
    }

    public synchronized boolean isOpen() {
        return this.camera != null;
    }

    public synchronized void closeCamera() {
        LogUtil.d(TAG, "closeCamera");
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
            this.mListener = null;
        }
    }

    public void setSurfaceTexture() {
        if (this.mSurfaceTexture == null) {
            this.mSurfaceTexture = new SurfaceTexture(10);
        }
        try {
            this.camera.setPreviewTexture(this.mSurfaceTexture);
        } catch (Exception e) {
            LogUtil.e(TAG, "setPreviewTexture error");
        }
    }

    public synchronized void startPreview() {
        LogUtil.d(TAG, "startPreview");
        Camera theCamera = this.camera;
        if (theCamera != null && !this.previewing) {
            theCamera.startPreview();
            this.previewing = true;
        }
    }

    public synchronized void stopPreview() {
        LogUtil.d(TAG, "stopPreview");
        if (this.camera != null && this.previewing) {
            this.camera.stopPreview();
            this.camera.setPreviewCallback(null);
            this.previewing = false;
        }
    }

    public synchronized void takePicture(Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback jpeg) {
        this.camera.takePicture(shutter, raw, jpeg);
    }

    private Bitmap reSize(byte[] data) {
        LogUtil.i(TAG, "myJpegCallback:onPictureTaken...");
        Bitmap cutMap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);
        Bitmap rotaBitmap = Bitmap.createBitmap(cutMap, 0, 0, cutMap.getWidth(), cutMap.getHeight(), matrix, false);
        Bitmap sizeBitmap = Bitmap.createScaledBitmap(rotaBitmap, Videoio.CAP_PROP_XI_BUFFER_POLICY, Videoio.CAP_PVAPI, true);
        Bitmap rectBitmap = Bitmap.createBitmap(sizeBitmap, 100, 200, 300, 300);
        return rectBitmap;
    }
}
