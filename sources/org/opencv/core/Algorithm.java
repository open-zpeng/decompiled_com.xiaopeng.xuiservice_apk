package org.opencv.core;
/* loaded from: classes5.dex */
public class Algorithm {
    protected final long nativeObj;

    private static native void clear_0(long j);

    private static native void delete(long j);

    private static native boolean empty_0(long j);

    private static native String getDefaultName_0(long j);

    private static native void save_0(long j, String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public Algorithm(long addr) {
        this.nativeObj = addr;
    }

    public long getNativeObjAddr() {
        return this.nativeObj;
    }

    public static Algorithm __fromPtr__(long addr) {
        return new Algorithm(addr);
    }

    public String getDefaultName() {
        return getDefaultName_0(this.nativeObj);
    }

    public boolean empty() {
        return empty_0(this.nativeObj);
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public void save(String filename) {
        save_0(this.nativeObj, filename);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}