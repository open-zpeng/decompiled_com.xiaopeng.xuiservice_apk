package org.opencv.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.List;
import org.opencv.BuildConfig;
import org.opencv.R;
import org.opencv.core.Mat;
import org.opencv.core.Size;
/* loaded from: classes5.dex */
public abstract class CameraBridgeViewBase extends SurfaceView implements SurfaceHolder.Callback {
    public static final int CAMERA_ID_ANY = -1;
    public static final int CAMERA_ID_BACK = 99;
    public static final int CAMERA_ID_FRONT = 98;
    public static final int GRAY = 2;
    protected static final int MAX_UNSPECIFIED = -1;
    public static final int RGBA = 1;
    private static final int STARTED = 1;
    private static final int STOPPED = 0;
    private static final String TAG = "CameraBridge";
    private Bitmap mCacheBitmap;
    protected int mCameraIndex;
    protected boolean mCameraPermissionGranted;
    protected boolean mEnabled;
    protected FpsMeter mFpsMeter;
    protected int mFrameHeight;
    protected int mFrameWidth;
    private CvCameraViewListener2 mListener;
    protected int mMaxHeight;
    protected int mMaxWidth;
    protected int mPreviewFormat;
    protected float mScale;
    private int mState;
    private boolean mSurfaceExist;
    private final Object mSyncObject;

    /* loaded from: classes5.dex */
    public interface CvCameraViewFrame {
        Mat gray();

        Mat rgba();
    }

    /* loaded from: classes5.dex */
    public interface CvCameraViewListener {
        Mat onCameraFrame(Mat mat);

        void onCameraViewStarted(int i, int i2);

        void onCameraViewStopped();
    }

    /* loaded from: classes5.dex */
    public interface CvCameraViewListener2 {
        Mat onCameraFrame(CvCameraViewFrame cvCameraViewFrame);

        void onCameraViewStarted(int i, int i2);

        void onCameraViewStopped();
    }

    /* loaded from: classes5.dex */
    public interface ListItemAccessor {
        int getHeight(Object obj);

        int getWidth(Object obj);
    }

    protected abstract boolean connectCamera(int i, int i2);

    protected abstract void disconnectCamera();

    public CameraBridgeViewBase(Context context, int cameraId) {
        super(context);
        this.mState = 0;
        this.mSyncObject = new Object();
        this.mScale = 0.0f;
        this.mPreviewFormat = 1;
        this.mCameraIndex = -1;
        this.mCameraPermissionGranted = false;
        this.mFpsMeter = null;
        this.mCameraIndex = cameraId;
        getHolder().addCallback(this);
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
    }

    public CameraBridgeViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mState = 0;
        this.mSyncObject = new Object();
        this.mScale = 0.0f;
        this.mPreviewFormat = 1;
        this.mCameraIndex = -1;
        this.mCameraPermissionGranted = false;
        this.mFpsMeter = null;
        int count = attrs.getAttributeCount();
        Log.d(TAG, "Attr count: " + Integer.valueOf(count));
        TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CameraBridgeViewBase);
        if (styledAttrs.getBoolean(R.styleable.CameraBridgeViewBase_show_fps, false)) {
            enableFpsMeter();
        }
        this.mCameraIndex = styledAttrs.getInt(R.styleable.CameraBridgeViewBase_camera_id, -1);
        getHolder().addCallback(this);
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
        styledAttrs.recycle();
    }

    public void setCameraIndex(int cameraIndex) {
        this.mCameraIndex = cameraIndex;
    }

    /* loaded from: classes5.dex */
    protected class CvCameraViewListenerAdapter implements CvCameraViewListener2 {
        private CvCameraViewListener mOldStyleListener;
        private int mPreviewFormat = 1;

        public CvCameraViewListenerAdapter(CvCameraViewListener oldStypeListener) {
            this.mOldStyleListener = oldStypeListener;
        }

        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
        public void onCameraViewStarted(int width, int height) {
            this.mOldStyleListener.onCameraViewStarted(width, height);
        }

        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
        public void onCameraViewStopped() {
            this.mOldStyleListener.onCameraViewStopped();
        }

        @Override // org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
        public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
            int i = this.mPreviewFormat;
            if (i == 1) {
                Mat result = this.mOldStyleListener.onCameraFrame(inputFrame.rgba());
                return result;
            } else if (i == 2) {
                Mat result2 = this.mOldStyleListener.onCameraFrame(inputFrame.gray());
                return result2;
            } else {
                Log.e(CameraBridgeViewBase.TAG, "Invalid frame format! Only RGBA and Gray Scale are supported!");
                return null;
            }
        }

        public void setFrameFormat(int format) {
            this.mPreviewFormat = format;
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d(TAG, "call surfaceChanged event");
        synchronized (this.mSyncObject) {
            if (!this.mSurfaceExist) {
                this.mSurfaceExist = true;
                checkCurrentState();
            } else {
                this.mSurfaceExist = false;
                checkCurrentState();
                this.mSurfaceExist = true;
                checkCurrentState();
            }
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this.mSyncObject) {
            this.mSurfaceExist = false;
            checkCurrentState();
        }
    }

    public void setCameraPermissionGranted() {
        synchronized (this.mSyncObject) {
            this.mCameraPermissionGranted = true;
            checkCurrentState();
        }
    }

    public void enableView() {
        synchronized (this.mSyncObject) {
            this.mEnabled = true;
            checkCurrentState();
        }
    }

    public void disableView() {
        synchronized (this.mSyncObject) {
            this.mEnabled = false;
            checkCurrentState();
        }
    }

    public void enableFpsMeter() {
        if (this.mFpsMeter == null) {
            this.mFpsMeter = new FpsMeter();
            this.mFpsMeter.setResolution(this.mFrameWidth, this.mFrameHeight);
        }
    }

    public void disableFpsMeter() {
        this.mFpsMeter = null;
    }

    public void setCvCameraViewListener(CvCameraViewListener2 listener) {
        this.mListener = listener;
    }

    public void setCvCameraViewListener(CvCameraViewListener listener) {
        CvCameraViewListenerAdapter adapter = new CvCameraViewListenerAdapter(listener);
        adapter.setFrameFormat(this.mPreviewFormat);
        this.mListener = adapter;
    }

    public void setMaxFrameSize(int maxWidth, int maxHeight) {
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
    }

    public void SetCaptureFormat(int format) {
        this.mPreviewFormat = format;
        CvCameraViewListener2 cvCameraViewListener2 = this.mListener;
        if (cvCameraViewListener2 instanceof CvCameraViewListenerAdapter) {
            CvCameraViewListenerAdapter adapter = (CvCameraViewListenerAdapter) cvCameraViewListener2;
            adapter.setFrameFormat(this.mPreviewFormat);
        }
    }

    private void checkCurrentState() {
        int targetState;
        Log.d(TAG, "call checkCurrentState");
        if (this.mEnabled && this.mCameraPermissionGranted && this.mSurfaceExist && getVisibility() == 0) {
            targetState = 1;
        } else {
            targetState = 0;
        }
        int i = this.mState;
        if (targetState != i) {
            processExitState(i);
            this.mState = targetState;
            processEnterState(this.mState);
        }
    }

    private void processEnterState(int state) {
        Log.d(TAG, "call processEnterState: " + state);
        if (state == 0) {
            onEnterStoppedState();
            CvCameraViewListener2 cvCameraViewListener2 = this.mListener;
            if (cvCameraViewListener2 != null) {
                cvCameraViewListener2.onCameraViewStopped();
            }
        } else if (state == 1) {
            onEnterStartedState();
            CvCameraViewListener2 cvCameraViewListener22 = this.mListener;
            if (cvCameraViewListener22 != null) {
                cvCameraViewListener22.onCameraViewStarted(this.mFrameWidth, this.mFrameHeight);
            }
        }
    }

    private void processExitState(int state) {
        Log.d(TAG, "call processExitState: " + state);
        if (state == 0) {
            onExitStoppedState();
        } else if (state == 1) {
            onExitStartedState();
        }
    }

    private void onEnterStoppedState() {
    }

    private void onExitStoppedState() {
    }

    private void onEnterStartedState() {
        Log.d(TAG, "call onEnterStartedState");
        if (!connectCamera(getWidth(), getHeight())) {
            AlertDialog ad = new AlertDialog.Builder(getContext()).create();
            ad.setCancelable(false);
            ad.setMessage("It seems that you device does not support camera (or it is locked). Application will be closed.");
            ad.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: org.opencv.android.CameraBridgeViewBase.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((Activity) CameraBridgeViewBase.this.getContext()).finish();
                }
            });
            ad.show();
        }
    }

    private void onExitStartedState() {
        disconnectCamera();
        Bitmap bitmap = this.mCacheBitmap;
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void deliverAndDrawFrame(CvCameraViewFrame frame) {
        Mat modified;
        Canvas canvas;
        CvCameraViewListener2 cvCameraViewListener2 = this.mListener;
        if (cvCameraViewListener2 != null) {
            modified = cvCameraViewListener2.onCameraFrame(frame);
        } else {
            modified = frame.rgba();
        }
        boolean bmpValid = true;
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, this.mCacheBitmap);
            } catch (Exception e) {
                Log.e(TAG, "Mat type: " + modified);
                Log.e(TAG, "Bitmap type: " + this.mCacheBitmap.getWidth() + "*" + this.mCacheBitmap.getHeight());
                StringBuilder sb = new StringBuilder();
                sb.append("Utils.matToBitmap() throws an exception: ");
                sb.append(e.getMessage());
                Log.e(TAG, sb.toString());
                bmpValid = false;
            }
        }
        if (bmpValid && this.mCacheBitmap != null && (canvas = getHolder().lockCanvas()) != null) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "mStretch value: " + this.mScale);
            }
            if (this.mScale != 0.0f) {
                Bitmap bitmap = this.mCacheBitmap;
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), this.mCacheBitmap.getHeight()), new Rect((int) ((canvas.getWidth() - (this.mScale * this.mCacheBitmap.getWidth())) / 2.0f), (int) ((canvas.getHeight() - (this.mScale * this.mCacheBitmap.getHeight())) / 2.0f), (int) (((canvas.getWidth() - (this.mScale * this.mCacheBitmap.getWidth())) / 2.0f) + (this.mScale * this.mCacheBitmap.getWidth())), (int) (((canvas.getHeight() - (this.mScale * this.mCacheBitmap.getHeight())) / 2.0f) + (this.mScale * this.mCacheBitmap.getHeight()))), (Paint) null);
            } else {
                Bitmap bitmap2 = this.mCacheBitmap;
                canvas.drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), this.mCacheBitmap.getHeight()), new Rect((canvas.getWidth() - this.mCacheBitmap.getWidth()) / 2, (canvas.getHeight() - this.mCacheBitmap.getHeight()) / 2, ((canvas.getWidth() - this.mCacheBitmap.getWidth()) / 2) + this.mCacheBitmap.getWidth(), ((canvas.getHeight() - this.mCacheBitmap.getHeight()) / 2) + this.mCacheBitmap.getHeight()), (Paint) null);
            }
            FpsMeter fpsMeter = this.mFpsMeter;
            if (fpsMeter != null) {
                fpsMeter.measure();
                this.mFpsMeter.draw(canvas, 20.0f, 30.0f);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void AllocateCache() {
        this.mCacheBitmap = Bitmap.createBitmap(this.mFrameWidth, this.mFrameHeight, Bitmap.Config.ARGB_8888);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor, int surfaceWidth, int surfaceHeight) {
        int calcWidth = 0;
        int calcHeight = 0;
        int maxAllowedWidth = this.mMaxWidth;
        if (maxAllowedWidth == -1 || maxAllowedWidth >= surfaceWidth) {
            maxAllowedWidth = surfaceWidth;
        }
        int i = this.mMaxHeight;
        if (i == -1 || i >= surfaceHeight) {
            i = surfaceHeight;
        }
        int maxAllowedHeight = i;
        for (Object size : supportedSizes) {
            int width = accessor.getWidth(size);
            int height = accessor.getHeight(size);
            Log.d(TAG, "trying size: " + width + "x" + height);
            if (width <= maxAllowedWidth && height <= maxAllowedHeight && width >= calcWidth && height >= calcHeight) {
                calcWidth = width;
                calcHeight = height;
            }
        }
        if ((calcWidth == 0 || calcHeight == 0) && supportedSizes.size() > 0) {
            Log.i(TAG, "fallback to the first frame size");
            Object size2 = supportedSizes.get(0);
            calcWidth = accessor.getWidth(size2);
            calcHeight = accessor.getHeight(size2);
        }
        return new Size(calcWidth, calcHeight);
    }
}
