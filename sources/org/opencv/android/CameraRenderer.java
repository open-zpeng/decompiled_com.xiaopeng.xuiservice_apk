package org.opencv.android;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.util.Log;
@TargetApi(15)
/* loaded from: classes5.dex */
public class CameraRenderer extends CameraGLRendererBase {
    public static final String LOGTAG = "CameraRenderer";
    private Camera mCamera;
    private boolean mPreviewStarted;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraRenderer(CameraGLSurfaceView view) {
        super(view);
        this.mPreviewStarted = false;
    }

    @Override // org.opencv.android.CameraGLRendererBase
    protected synchronized void closeCamera() {
        Log.i(LOGTAG, "closeCamera");
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.mPreviewStarted = false;
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00ec A[Catch: all -> 0x0193, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0011, B:6:0x0018, B:10:0x003a, B:12:0x003e, B:15:0x0044, B:17:0x004a, B:18:0x0065, B:24:0x0093, B:21:0x006e, B:56:0x0143, B:58:0x0147, B:61:0x0150, B:63:0x015c, B:65:0x0164, B:66:0x0169, B:67:0x016e, B:70:0x0177, B:9:0x0020, B:26:0x0098, B:28:0x009c, B:30:0x00a6, B:31:0x00b3, B:33:0x00b9, B:36:0x00c2, B:48:0x00ec, B:50:0x00f6, B:51:0x00fe, B:52:0x0119, B:55:0x0121, B:38:0x00c6, B:40:0x00ca, B:41:0x00d7, B:43:0x00dd, B:46:0x00e7), top: B:78:0x0001, inners: #0, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0147 A[Catch: all -> 0x0193, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0011, B:6:0x0018, B:10:0x003a, B:12:0x003e, B:15:0x0044, B:17:0x004a, B:18:0x0065, B:24:0x0093, B:21:0x006e, B:56:0x0143, B:58:0x0147, B:61:0x0150, B:63:0x015c, B:65:0x0164, B:66:0x0169, B:67:0x016e, B:70:0x0177, B:9:0x0020, B:26:0x0098, B:28:0x009c, B:30:0x00a6, B:31:0x00b3, B:33:0x00b9, B:36:0x00c2, B:48:0x00ec, B:50:0x00f6, B:51:0x00fe, B:52:0x0119, B:55:0x0121, B:38:0x00c6, B:40:0x00ca, B:41:0x00d7, B:43:0x00dd, B:46:0x00e7), top: B:78:0x0001, inners: #0, #2, #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0150 A[Catch: all -> 0x0193, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0011, B:6:0x0018, B:10:0x003a, B:12:0x003e, B:15:0x0044, B:17:0x004a, B:18:0x0065, B:24:0x0093, B:21:0x006e, B:56:0x0143, B:58:0x0147, B:61:0x0150, B:63:0x015c, B:65:0x0164, B:66:0x0169, B:67:0x016e, B:70:0x0177, B:9:0x0020, B:26:0x0098, B:28:0x009c, B:30:0x00a6, B:31:0x00b3, B:33:0x00b9, B:36:0x00c2, B:48:0x00ec, B:50:0x00f6, B:51:0x00fe, B:52:0x0119, B:55:0x0121, B:38:0x00c6, B:40:0x00ca, B:41:0x00d7, B:43:0x00dd, B:46:0x00e7), top: B:78:0x0001, inners: #0, #2, #3, #4 }] */
    @Override // org.opencv.android.CameraGLRendererBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected synchronized void openCamera(int r8) {
        /*
            Method dump skipped, instructions count: 406
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.opencv.android.CameraRenderer.openCamera(int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0100 A[Catch: all -> 0x0124, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0023, B:8:0x002c, B:10:0x0030, B:12:0x0034, B:13:0x0037, B:15:0x003b, B:17:0x003f, B:18:0x0042, B:20:0x0054, B:21:0x005b, B:23:0x0061, B:28:0x0091, B:36:0x00ad, B:38:0x00fc, B:40:0x0100, B:41:0x0107, B:37:0x00cc, B:42:0x010e), top: B:48:0x0001 }] */
    @Override // org.opencv.android.CameraGLRendererBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void setCameraPreviewSize(int r14, int r15) {
        /*
            Method dump skipped, instructions count: 295
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.opencv.android.CameraRenderer.setCameraPreviewSize(int, int):void");
    }
}
