package org.opencv.core;
/* loaded from: classes5.dex */
public class CvException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public CvException(String msg) {
        super(msg);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "CvException [" + super.toString() + "]";
    }
}
