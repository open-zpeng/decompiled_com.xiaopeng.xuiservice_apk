package com.xiaopeng.xuiservice.uvccamera.usb;

import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class UVCCamera {
    public static final int DEFAULT_PREVIEW_FPS = 30;
    public static final int DEFAULT_PREVIEW_FRAME_FORMAT = 7;
    public static final int DEFAULT_PREVIEW_HEIGHT = 480;
    public static final int DEFAULT_PREVIEW_WIDTH = 640;
    public static final int FRAME_FORMAT_MJPEG = 1;
    public static final int FRAME_FORMAT_YUYV = 0;
    public static final int PIXEL_FORMAT_BGR = 7;
    public static final int PIXEL_FORMAT_NV12 = 2;
    public static final int PIXEL_FORMAT_NV21 = 3;
    public static final int PIXEL_FORMAT_RAW = 0;
    public static final int PIXEL_FORMAT_RGB = 4;
    public static final int PIXEL_FORMAT_RGB565 = 5;
    public static final int PIXEL_FORMAT_RGBX = 6;
    public static final int PIXEL_FORMAT_YUV = 1;
    private static final String TAG = UVCCamera.class.getSimpleName();
    public static final int UVC_ERROR_BUSY = -6;
    public static final int UVC_QUIRK_FIX_BANDWIDTH = 128;
    public static final int UVC_VS_FORMAT_MJPEG = 6;
    public static final int UVC_VS_FORMAT_UNCOMPRESSED = 4;
    public static final int UVC_VS_FRAME_MJPEG = 7;
    public static final int UVC_VS_FRAME_UNCOMPRESSED = 5;
    private USBMonitor.UsbControlBlock mCtrlBlock;
    protected Size mCurrentSize;
    protected UVCParam mParam;
    protected List<Format> mSupportedFormatList;
    protected String mSupportedFormats;
    protected List<Size> mSupportedSizeList;
    private UVCControl mControl = null;
    protected long mNativePtr = nativeCreate();

    private native int nativeConnect(long j, int i, int i2);

    private native long nativeCreate();

    private native void nativeDestroy(long j);

    private native long nativeGetControl(long j);

    private native String nativeGetSupportedFormats(long j);

    private native int nativeRelease(long j);

    private native int nativeSetButtonCallback(long j, IButtonCallback iButtonCallback);

    private native int nativeSetCaptureDisplay(long j, Surface surface);

    private native int nativeSetFrameCallback(long j, IFrameCallback iFrameCallback, int i);

    private native int nativeSetPreviewDisplay(long j, Surface surface);

    private native int nativeSetPreviewSize(long j, int i, int i2, int i3, int i4);

    private native int nativeSetStatusCallback(long j, IStatusCallback iStatusCallback);

    private native int nativeStartPreview(long j);

    private native int nativeStopPreview(long j);

    static {
        System.loadLibrary("jpeg-turbo212");
        System.loadLibrary("usb1.0");
        System.loadLibrary("uvc");
        System.loadLibrary("UVCCamera");
    }

    public UVCCamera(UVCParam param) {
        this.mParam = param != null ? (UVCParam) param.clone() : new UVCParam();
    }

    public synchronized int open(USBMonitor.UsbControlBlock ctrlBlock) {
        int result;
        try {
            this.mCtrlBlock = ctrlBlock.m129clone();
            this.mCtrlBlock.open();
            result = nativeConnect(this.mNativePtr, this.mCtrlBlock.getFileDescriptor(), this.mParam.getQuirks());
        } catch (Exception e) {
            Log.w(TAG, e);
            result = -1;
        }
        if (result != 0) {
            return result;
        }
        updateSupportedFormats();
        Size size = this.mParam.getPreviewSize();
        if ((size == null || !checkSizeValid(size.width, size.height, size.type, size.fps)) && (size = getSupportedSizeOne()) == null) {
            size = new Size(7, DEFAULT_PREVIEW_WIDTH, 480, 30, new ArrayList(30));
        }
        int r = nativeSetPreviewSize(this.mNativePtr, size.width, size.height, size.type, size.fps);
        String str = TAG;
        LogUtil.d(str, "setPreviewSize:" + r + ":" + size);
        this.mCurrentSize = size;
        this.mControl = new UVCControl(nativeGetControl(this.mNativePtr));
        return result;
    }

    public void setQuirks(IStatusCallback callback) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetStatusCallback(j, callback);
        }
    }

    public void setStatusCallback(IStatusCallback callback) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetStatusCallback(j, callback);
        }
    }

    public void setButtonCallback(IButtonCallback callback) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetButtonCallback(j, callback);
        }
    }

    public synchronized void close() {
        close(false);
    }

    public synchronized void close(boolean isSilent) {
        LogUtil.v(TAG, HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
        stopPreview();
        if (this.mNativePtr != 0) {
            nativeRelease(this.mNativePtr);
        }
        if (this.mCtrlBlock != null) {
            this.mCtrlBlock.close(isSilent);
            this.mCtrlBlock = null;
        }
        this.mSupportedFormats = null;
        this.mSupportedFormatList = null;
        this.mSupportedSizeList = null;
        this.mCurrentSize = null;
        if (this.mControl != null) {
            this.mControl.release();
            this.mControl = null;
        }
    }

    public UsbDevice getDevice() {
        USBMonitor.UsbControlBlock usbControlBlock = this.mCtrlBlock;
        if (usbControlBlock != null) {
            return usbControlBlock.getDevice();
        }
        return null;
    }

    public String getDeviceName() {
        USBMonitor.UsbControlBlock usbControlBlock = this.mCtrlBlock;
        if (usbControlBlock != null) {
            return usbControlBlock.getDeviceName();
        }
        return null;
    }

    public USBMonitor.UsbControlBlock getUsbControlBlock() {
        return this.mCtrlBlock;
    }

    private void updateSupportedFormats() {
        long j = this.mNativePtr;
        if (j != 0) {
            this.mSupportedFormats = nativeGetSupportedFormats(j);
            this.mSupportedFormatList = parseSupportedFormats(this.mSupportedFormats);
            this.mSupportedSizeList = fetchSupportedSizeList(this.mSupportedFormatList);
        }
    }

    public synchronized String getSupportedSize() {
        if (TextUtils.isEmpty(this.mSupportedFormats)) {
            updateSupportedFormats();
        }
        return this.mSupportedFormats;
    }

    public List<Format> getSupportedFormatList() {
        List<Format> list = new ArrayList<>();
        List<Format> list2 = this.mSupportedFormatList;
        if (list2 != null) {
            for (Format format : list2) {
                list.add(format.m125clone());
            }
        }
        return list;
    }

    public List<Size> getSupportedSizeList() {
        List<Size> list = new ArrayList<>();
        List<Size> list2 = this.mSupportedSizeList;
        if (list2 != null) {
            for (Size size : list2) {
                list.add(size.m128clone());
            }
        }
        return list;
    }

    public Size getSupportedSizeOne() {
        Size maxSize = null;
        List<Size> sizeList = getSupportedSizeList();
        if (sizeList == null || sizeList.size() <= 0) {
            return null;
        }
        Collections.sort(sizeList, new Comparator() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$UVCCamera$tXx9FjnT8zO9XOpJYQ0x4BnNKNE
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return UVCCamera.lambda$getSupportedSizeOne$0((Size) obj, (Size) obj2);
            }
        });
        Iterator<Size> it = sizeList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Size size = it.next();
            if (size.type == 7) {
                maxSize = size;
                break;
            }
        }
        if (maxSize == null) {
            Size maxSize2 = sizeList.get(0);
            return maxSize2;
        }
        return maxSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$getSupportedSizeOne$0(Size o1, Size o2) {
        return (o2.width * o2.height) - (o1.width * o1.height);
    }

    private List<Format> parseSupportedFormats(String supportedFormats) {
        List<Format> formatList = new ArrayList<>();
        if (!TextUtils.isEmpty(supportedFormats)) {
            try {
                JSONObject json = new JSONObject(supportedFormats);
                JSONArray formatsJSON = json.getJSONArray("formats");
                for (int i = 0; i < formatsJSON.length(); i++) {
                    JSONObject formatJSON = formatsJSON.getJSONObject(i);
                    if (formatJSON.has("subType") && formatJSON.has("frameDescriptors")) {
                        int index = formatJSON.getInt("index");
                        int formatType = formatJSON.getInt("subType");
                        if (formatType == 6 || formatType == 4) {
                            List<Format.Descriptor> descriptorList = parseFrameDescriptors(formatJSON, index);
                            formatList.add(new Format(index, formatType, descriptorList));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return formatList;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:4|(5:6|(4:9|(2:11|12)(1:14)|13|7)|15|16|(8:18|19|20|21|22|23|25|26))(1:34)|33|19|20|21|22|23|25|26|2) */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00b0, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00b2, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b6, code lost:
        r0.printStackTrace();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<com.xiaopeng.xuiservice.uvccamera.usb.Format.Descriptor> parseFrameDescriptors(org.json.JSONObject r21, int r22) throws org.json.JSONException {
        /*
            r20 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r0
            java.lang.String r0 = "frameDescriptors"
            r2 = r21
            org.json.JSONArray r3 = r2.getJSONArray(r0)
            r0 = 0
            r4 = r0
        L10:
            int r0 = r3.length()
            if (r4 >= r0) goto Lc1
            org.json.JSONObject r5 = r3.getJSONObject(r4)
            java.lang.String r0 = "width"
            int r14 = r5.getInt(r0)
            java.lang.String r0 = "height"
            int r15 = r5.getInt(r0)
            java.lang.String r0 = "subType"
            int r16 = r5.getInt(r0)
            java.lang.String r0 = "defaultFps"
            int r0 = r5.getInt(r0)
            java.lang.String r6 = "defaultFrameInterval"
            int r6 = r5.getInt(r6)
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r13 = r7
            java.lang.String r7 = "intervals"
            org.json.JSONArray r12 = r5.getJSONArray(r7)
            int r7 = r12.length()
            if (r7 <= 0) goto L95
            r7 = 0
            r8 = 0
            r9 = 0
        L4d:
            int r10 = r12.length()
            if (r9 >= r10) goto L89
            org.json.JSONObject r10 = r12.getJSONObject(r9)
            com.xiaopeng.xuiservice.uvccamera.usb.Format$Interval r11 = new com.xiaopeng.xuiservice.uvccamera.usb.Format$Interval
            r17 = r0
            java.lang.String r0 = "index"
            int r0 = r10.getInt(r0)
            java.lang.String r2 = "value"
            int r2 = r10.getInt(r2)
            r18 = r3
            java.lang.String r3 = "fps"
            int r3 = r10.getInt(r3)
            r11.<init>(r0, r2, r3)
            r0 = r11
            r13.add(r0)
            int r2 = r0.fps
            if (r8 >= r2) goto L80
            int r2 = r0.value
            int r3 = r0.fps
            r7 = r2
            r8 = r3
        L80:
            int r9 = r9 + 1
            r2 = r21
            r0 = r17
            r3 = r18
            goto L4d
        L89:
            r17 = r0
            r18 = r3
            if (r8 <= 0) goto L99
            r0 = r7
            r2 = r8
            r17 = r2
            r2 = r0
            goto L9a
        L95:
            r17 = r0
            r18 = r3
        L99:
            r2 = r6
        L9a:
            com.xiaopeng.xuiservice.uvccamera.usb.Format$Descriptor r0 = new com.xiaopeng.xuiservice.uvccamera.usb.Format$Descriptor     // Catch: java.lang.Exception -> Lb2
            r6 = r0
            r7 = r22
            r8 = r16
            r9 = r14
            r10 = r15
            r11 = r17
            r3 = r12
            r12 = r2
            r19 = r13
            r6.<init>(r7, r8, r9, r10, r11, r12, r13)     // Catch: java.lang.Exception -> Lb0
            r1.add(r0)     // Catch: java.lang.Exception -> Lb0
            goto Lb9
        Lb0:
            r0 = move-exception
            goto Lb6
        Lb2:
            r0 = move-exception
            r3 = r12
            r19 = r13
        Lb6:
            r0.printStackTrace()
        Lb9:
            int r4 = r4 + 1
            r2 = r21
            r3 = r18
            goto L10
        Lc1:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.uvccamera.usb.UVCCamera.parseFrameDescriptors(org.json.JSONObject, int):java.util.List");
    }

    private List<Size> fetchSupportedSizeList(List<Format> formatList) {
        List<Size> sizeList = new ArrayList<>();
        for (Format format : formatList) {
            for (Format.Descriptor descriptor : format.frameDescriptors) {
                List<Integer> fpsList = new ArrayList<>();
                for (Format.Interval interval : descriptor.intervals) {
                    fpsList.add(Integer.valueOf(interval.fps));
                }
                sizeList.add(new Size(descriptor.type, descriptor.width, descriptor.height, descriptor.fps, fpsList));
            }
        }
        return sizeList;
    }

    private boolean checkSizeValid(int width, int height, int frameType, int fps) {
        if (this.mNativePtr != 0) {
            List<Size> sizeList = getSupportedSizeList();
            for (Size size : sizeList) {
                if (size.width == width && size.height == height && size.type == frameType && (size.fps == fps || size.fpsList.contains(Integer.valueOf(fps)))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public Size getPreviewSize() {
        return this.mCurrentSize;
    }

    public void setPreviewSize(int width, int height) {
        setPreviewSize(width, height, 7, 30);
    }

    public void setPreviewSize(int width, int height, int frameType) {
        setPreviewSize(width, height, frameType, 30);
    }

    public void setPreviewSize(int width, int height, int frameType, int fps) {
        setPreviewSize(new Size(frameType, width, height, fps, new ArrayList(fps)));
    }

    public void setPreviewSize(Size size) {
        if (size.width == 0 || size.height == 0) {
            throw new IllegalArgumentException("invalid preview size");
        }
        if (this.mNativePtr != 0) {
            if (!checkSizeValid(size.width, size.height, size.type, size.fps)) {
                throw new IllegalArgumentException("invalid preview size");
            }
            int result = nativeSetPreviewSize(this.mNativePtr, size.width, size.height, size.type, size.fps);
            if (result != 0) {
                result = nativeSetPreviewSize(this.mNativePtr, size.width, size.height, size.type, size.fps);
            }
            if (result != 0) {
                throw new IllegalArgumentException("Failed to set preview size");
            }
            this.mCurrentSize = size;
        }
    }

    public synchronized void setPreviewDisplay(SurfaceHolder holder) {
        nativeSetPreviewDisplay(this.mNativePtr, holder.getSurface());
    }

    public synchronized void setPreviewTexture(SurfaceTexture texture) {
        Surface surface = new Surface(texture);
        nativeSetPreviewDisplay(this.mNativePtr, surface);
    }

    public synchronized void setPreviewDisplay(Surface surface) {
        nativeSetPreviewDisplay(this.mNativePtr, surface);
    }

    public void setFrameCallback(IFrameCallback callback, int pixelFormat) {
        long j = this.mNativePtr;
        if (j != 0) {
            nativeSetFrameCallback(j, callback, pixelFormat);
        }
    }

    public synchronized void startPreview() {
        if (this.mCtrlBlock != null) {
            nativeStartPreview(this.mNativePtr);
        }
    }

    public synchronized void stopPreview() {
        if (this.mCtrlBlock != null) {
            nativeStopPreview(this.mNativePtr);
        }
    }

    public void startCapture(Surface surface) {
        if (this.mCtrlBlock != null && surface != null) {
            nativeSetCaptureDisplay(this.mNativePtr, surface);
            return;
        }
        throw new NullPointerException("startCapture");
    }

    public void stopCapture() {
        if (this.mCtrlBlock != null) {
            nativeSetCaptureDisplay(this.mNativePtr, null);
        }
    }

    public synchronized void destroy() {
        destroy(false);
    }

    public synchronized void destroy(boolean isSilent) {
        close(isSilent);
        if (this.mNativePtr != 0) {
            nativeDestroy(this.mNativePtr);
            this.mNativePtr = 0L;
        }
    }

    public UVCControl getControl() {
        return this.mControl;
    }

    public boolean isOpened() {
        return this.mControl != null;
    }
}
