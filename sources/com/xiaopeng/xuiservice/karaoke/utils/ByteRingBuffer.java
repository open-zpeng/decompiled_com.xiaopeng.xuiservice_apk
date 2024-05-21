package com.xiaopeng.xuiservice.karaoke.utils;

import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class ByteRingBuffer {
    private static final String TAG = "ByteRingBuffer";
    private static Object rBufferLock = new Object();
    private byte[] rBuf;
    private int rBufPos;
    private int rBufSize;
    private int rBufUsed;

    public ByteRingBuffer(int size) {
        if (size <= 0) {
            LogUtil.e(TAG, "ByteRingBuffer error:" + size);
            return;
        }
        this.rBufSize = size;
        this.rBuf = new byte[this.rBufSize];
        this.rBufPos = 0;
        this.rBufUsed = 0;
    }

    public void resize(int newSize) {
        if (newSize <= 0) {
            LogUtil.e(TAG, "resize error:" + newSize);
            return;
        }
        int i = this.rBufUsed;
        if (newSize < i) {
            discard(i - newSize);
        }
        byte[] newBuf = new byte[newSize];
        int newBufUsed = read(newBuf, 0, newSize);
        this.rBuf = newBuf;
        this.rBufSize = newSize;
        this.rBufPos = 0;
        this.rBufUsed = newBufUsed;
    }

    public int getSize() {
        return this.rBufSize;
    }

    public int getFree() {
        return this.rBufSize - this.rBufUsed;
    }

    public int getUsed() {
        return this.rBufUsed;
    }

    public void clear() {
        synchronized (rBufferLock) {
            this.rBufPos = 0;
            this.rBufUsed = 0;
        }
    }

    public void discard(int len) {
        if (len < 0) {
            LogUtil.e(TAG, "discard error:" + len);
            return;
        }
        int trLen = Math.min(len, this.rBufUsed);
        this.rBufPos = clip(this.rBufPos + trLen);
        this.rBufUsed -= trLen;
    }

    public int write(byte[] buf, int pos, int len) {
        int ret;
        if (len < 0) {
            LogUtil.e(TAG, "write error pos:" + pos + " len:" + len);
            return 0;
        }
        synchronized (rBufferLock) {
            if (this.rBufUsed == 0) {
                this.rBufPos = 0;
            }
            int p1 = this.rBufPos + this.rBufUsed;
            if (p1 < this.rBufSize) {
                int trLen1 = Math.min(len, this.rBufSize - p1);
                append(buf, pos, trLen1);
                int trLen2 = Math.min(len - trLen1, this.rBufPos);
                append(buf, pos + trLen1, trLen2);
                ret = trLen1 + trLen2;
            } else {
                int ret2 = this.rBufSize;
                int trLen = Math.min(len, ret2 - this.rBufUsed);
                append(buf, pos, trLen);
                ret = trLen;
            }
        }
        return ret;
    }

    public int write(byte[] buf) {
        return write(buf, 0, buf.length);
    }

    private void append(byte[] buf, int pos, int len) {
        if (len == 0) {
            return;
        }
        if (len < 0) {
            LogUtil.e(TAG, "append error pos:" + pos + " len:" + len);
            return;
        }
        int p = clip(this.rBufPos + this.rBufUsed);
        if (p < 0) {
            LogUtil.e(TAG, "append error p:" + p);
            return;
        }
        try {
            System.arraycopy(buf, pos, this.rBuf, p, len);
        } catch (Exception e) {
            LogUtil.e(TAG, "append ERROR:" + e);
        }
        this.rBufUsed += len;
    }

    public int read(byte[] buf, int pos, int len) {
        int trLen1;
        int trLen2;
        if (len < 0) {
            LogUtil.e(TAG, "read error  pos:" + pos + " len:" + len);
            return -1;
        }
        synchronized (rBufferLock) {
            trLen1 = Math.min(len, Math.min(this.rBufUsed, this.rBufSize - this.rBufPos));
            remove(buf, pos, trLen1);
            trLen2 = Math.min(len - trLen1, this.rBufUsed);
            remove(buf, pos + trLen1, trLen2);
        }
        return trLen1 + trLen2;
    }

    public int read(byte[] buf) {
        return read(buf, 0, buf.length);
    }

    private void remove(byte[] buf, int pos, int len) {
        if (len == 0) {
            return;
        }
        if (len < 0) {
            LogUtil.e(TAG, "remove error  pos:" + pos + " len:" + len);
            return;
        }
        try {
            System.arraycopy(this.rBuf, this.rBufPos, buf, pos, len);
        } catch (Exception e) {
            LogUtil.e(TAG, "remove ERROR:" + e);
        }
        this.rBufPos = clip(this.rBufPos + len);
        this.rBufUsed -= len;
    }

    private int clip(int p) {
        int i = this.rBufSize;
        return p < i ? p : p - i;
    }
}
