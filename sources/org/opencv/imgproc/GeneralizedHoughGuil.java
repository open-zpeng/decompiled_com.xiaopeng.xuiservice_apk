package org.opencv.imgproc;
/* loaded from: classes5.dex */
public class GeneralizedHoughGuil extends GeneralizedHough {
    private static native void delete(long j);

    private static native double getAngleEpsilon_0(long j);

    private static native double getAngleStep_0(long j);

    private static native int getAngleThresh_0(long j);

    private static native int getLevels_0(long j);

    private static native double getMaxAngle_0(long j);

    private static native double getMaxScale_0(long j);

    private static native double getMinAngle_0(long j);

    private static native double getMinScale_0(long j);

    private static native int getPosThresh_0(long j);

    private static native double getScaleStep_0(long j);

    private static native int getScaleThresh_0(long j);

    private static native double getXi_0(long j);

    private static native void setAngleEpsilon_0(long j, double d);

    private static native void setAngleStep_0(long j, double d);

    private static native void setAngleThresh_0(long j, int i);

    private static native void setLevels_0(long j, int i);

    private static native void setMaxAngle_0(long j, double d);

    private static native void setMaxScale_0(long j, double d);

    private static native void setMinAngle_0(long j, double d);

    private static native void setMinScale_0(long j, double d);

    private static native void setPosThresh_0(long j, int i);

    private static native void setScaleStep_0(long j, double d);

    private static native void setScaleThresh_0(long j, int i);

    private static native void setXi_0(long j, double d);

    protected GeneralizedHoughGuil(long addr) {
        super(addr);
    }

    public static GeneralizedHoughGuil __fromPtr__(long addr) {
        return new GeneralizedHoughGuil(addr);
    }

    public double getAngleEpsilon() {
        return getAngleEpsilon_0(this.nativeObj);
    }

    public double getAngleStep() {
        return getAngleStep_0(this.nativeObj);
    }

    public double getMaxAngle() {
        return getMaxAngle_0(this.nativeObj);
    }

    public double getMaxScale() {
        return getMaxScale_0(this.nativeObj);
    }

    public double getMinAngle() {
        return getMinAngle_0(this.nativeObj);
    }

    public double getMinScale() {
        return getMinScale_0(this.nativeObj);
    }

    public double getScaleStep() {
        return getScaleStep_0(this.nativeObj);
    }

    public double getXi() {
        return getXi_0(this.nativeObj);
    }

    public int getAngleThresh() {
        return getAngleThresh_0(this.nativeObj);
    }

    public int getLevels() {
        return getLevels_0(this.nativeObj);
    }

    public int getPosThresh() {
        return getPosThresh_0(this.nativeObj);
    }

    public int getScaleThresh() {
        return getScaleThresh_0(this.nativeObj);
    }

    public void setAngleEpsilon(double angleEpsilon) {
        setAngleEpsilon_0(this.nativeObj, angleEpsilon);
    }

    public void setAngleStep(double angleStep) {
        setAngleStep_0(this.nativeObj, angleStep);
    }

    public void setAngleThresh(int angleThresh) {
        setAngleThresh_0(this.nativeObj, angleThresh);
    }

    public void setLevels(int levels) {
        setLevels_0(this.nativeObj, levels);
    }

    public void setMaxAngle(double maxAngle) {
        setMaxAngle_0(this.nativeObj, maxAngle);
    }

    public void setMaxScale(double maxScale) {
        setMaxScale_0(this.nativeObj, maxScale);
    }

    public void setMinAngle(double minAngle) {
        setMinAngle_0(this.nativeObj, minAngle);
    }

    public void setMinScale(double minScale) {
        setMinScale_0(this.nativeObj, minScale);
    }

    public void setPosThresh(int posThresh) {
        setPosThresh_0(this.nativeObj, posThresh);
    }

    public void setScaleStep(double scaleStep) {
        setScaleStep_0(this.nativeObj, scaleStep);
    }

    public void setScaleThresh(int scaleThresh) {
        setScaleThresh_0(this.nativeObj, scaleThresh);
    }

    public void setXi(double xi) {
        setXi_0(this.nativeObj, xi);
    }

    @Override // org.opencv.imgproc.GeneralizedHough, org.opencv.core.Algorithm
    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
