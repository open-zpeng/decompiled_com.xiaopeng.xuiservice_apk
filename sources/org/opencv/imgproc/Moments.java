package org.opencv.imgproc;
/* loaded from: classes5.dex */
public class Moments {
    public double m00;
    public double m01;
    public double m02;
    public double m03;
    public double m10;
    public double m11;
    public double m12;
    public double m20;
    public double m21;
    public double m30;
    public double mu02;
    public double mu03;
    public double mu11;
    public double mu12;
    public double mu20;
    public double mu21;
    public double mu30;
    public double nu02;
    public double nu03;
    public double nu11;
    public double nu12;
    public double nu20;
    public double nu21;
    public double nu30;

    public Moments(double m00, double m10, double m01, double m20, double m11, double m02, double m30, double m21, double m12, double m03) {
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m20 = m20;
        this.m11 = m11;
        this.m02 = m02;
        this.m30 = m30;
        this.m21 = m21;
        this.m12 = m12;
        this.m03 = m03;
        completeState();
    }

    public Moments() {
        this(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    public Moments(double[] vals) {
        set(vals);
    }

    public void set(double[] vals) {
        if (vals != null) {
            this.m00 = vals.length > 0 ? vals[0] : 0.0d;
            this.m10 = vals.length > 1 ? vals[1] : 0.0d;
            this.m01 = vals.length > 2 ? vals[2] : 0.0d;
            this.m20 = vals.length > 3 ? vals[3] : 0.0d;
            this.m11 = vals.length > 4 ? vals[4] : 0.0d;
            this.m02 = vals.length > 5 ? vals[5] : 0.0d;
            this.m30 = vals.length > 6 ? vals[6] : 0.0d;
            this.m21 = vals.length > 7 ? vals[7] : 0.0d;
            this.m12 = vals.length > 8 ? vals[8] : 0.0d;
            this.m03 = vals.length > 9 ? vals[9] : 0.0d;
            completeState();
            return;
        }
        this.m00 = 0.0d;
        this.m10 = 0.0d;
        this.m01 = 0.0d;
        this.m20 = 0.0d;
        this.m11 = 0.0d;
        this.m02 = 0.0d;
        this.m30 = 0.0d;
        this.m21 = 0.0d;
        this.m12 = 0.0d;
        this.m03 = 0.0d;
        this.mu20 = 0.0d;
        this.mu11 = 0.0d;
        this.mu02 = 0.0d;
        this.mu30 = 0.0d;
        this.mu21 = 0.0d;
        this.mu12 = 0.0d;
        this.mu03 = 0.0d;
        this.nu20 = 0.0d;
        this.nu11 = 0.0d;
        this.nu02 = 0.0d;
        this.nu30 = 0.0d;
        this.nu21 = 0.0d;
        this.nu12 = 0.0d;
        this.nu03 = 0.0d;
    }

    public String toString() {
        return "Moments [ \nm00=" + this.m00 + ", \nm10=" + this.m10 + ", m01=" + this.m01 + ", \nm20=" + this.m20 + ", m11=" + this.m11 + ", m02=" + this.m02 + ", \nm30=" + this.m30 + ", m21=" + this.m21 + ", m12=" + this.m12 + ", m03=" + this.m03 + ", \nmu20=" + this.mu20 + ", mu11=" + this.mu11 + ", mu02=" + this.mu02 + ", \nmu30=" + this.mu30 + ", mu21=" + this.mu21 + ", mu12=" + this.mu12 + ", mu03=" + this.mu03 + ", \nnu20=" + this.nu20 + ", nu11=" + this.nu11 + ", nu02=" + this.nu02 + ", \nnu30=" + this.nu30 + ", nu21=" + this.nu21 + ", nu12=" + this.nu12 + ", nu03=" + this.nu03 + ", \n]";
    }

    protected void completeState() {
        double cx = 0.0d;
        double cy = 0.0d;
        double inv_m00 = 0.0d;
        if (Math.abs(this.m00) > 1.0E-8d) {
            inv_m00 = 1.0d / this.m00;
            cx = this.m10 * inv_m00;
            cy = this.m01 * inv_m00;
        }
        double d = this.m20;
        double d2 = this.m10;
        double mu20 = d - (d2 * cx);
        double mu11 = this.m11 - (d2 * cy);
        double d3 = this.m02;
        double inv_m002 = inv_m00;
        double inv_m003 = this.m01;
        double mu02 = d3 - (inv_m003 * cy);
        this.mu20 = mu20;
        this.mu11 = mu11;
        this.mu02 = mu02;
        this.mu30 = this.m30 - (((mu20 * 3.0d) + (cx * d2)) * cx);
        double mu112 = mu11 + mu11;
        this.mu21 = (this.m21 - ((mu112 + (cx * inv_m003)) * cx)) - (cy * mu20);
        this.mu12 = (this.m12 - (((d2 * cy) + mu112) * cy)) - (cx * mu02);
        this.mu03 = this.m03 - (((mu02 * 3.0d) + (inv_m003 * cy)) * cy);
        double inv_sqrt_m00 = Math.sqrt(Math.abs(inv_m002));
        double s2 = inv_m002 * inv_m002;
        double s3 = s2 * inv_sqrt_m00;
        double cx2 = this.mu20;
        this.nu20 = cx2 * s2;
        this.nu11 = this.mu11 * s2;
        this.nu02 = this.mu02 * s2;
        this.nu30 = this.mu30 * s3;
        this.nu21 = this.mu21 * s3;
        this.nu12 = this.mu12 * s3;
        this.nu03 = this.mu03 * s3;
    }

    public double get_m00() {
        return this.m00;
    }

    public double get_m10() {
        return this.m10;
    }

    public double get_m01() {
        return this.m01;
    }

    public double get_m20() {
        return this.m20;
    }

    public double get_m11() {
        return this.m11;
    }

    public double get_m02() {
        return this.m02;
    }

    public double get_m30() {
        return this.m30;
    }

    public double get_m21() {
        return this.m21;
    }

    public double get_m12() {
        return this.m12;
    }

    public double get_m03() {
        return this.m03;
    }

    public double get_mu20() {
        return this.mu20;
    }

    public double get_mu11() {
        return this.mu11;
    }

    public double get_mu02() {
        return this.mu02;
    }

    public double get_mu30() {
        return this.mu30;
    }

    public double get_mu21() {
        return this.mu21;
    }

    public double get_mu12() {
        return this.mu12;
    }

    public double get_mu03() {
        return this.mu03;
    }

    public double get_nu20() {
        return this.nu20;
    }

    public double get_nu11() {
        return this.nu11;
    }

    public double get_nu02() {
        return this.nu02;
    }

    public double get_nu30() {
        return this.nu30;
    }

    public double get_nu21() {
        return this.nu21;
    }

    public double get_nu12() {
        return this.nu12;
    }

    public double get_nu03() {
        return this.nu03;
    }

    public void set_m00(double m00) {
        this.m00 = m00;
    }

    public void set_m10(double m10) {
        this.m10 = m10;
    }

    public void set_m01(double m01) {
        this.m01 = m01;
    }

    public void set_m20(double m20) {
        this.m20 = m20;
    }

    public void set_m11(double m11) {
        this.m11 = m11;
    }

    public void set_m02(double m02) {
        this.m02 = m02;
    }

    public void set_m30(double m30) {
        this.m30 = m30;
    }

    public void set_m21(double m21) {
        this.m21 = m21;
    }

    public void set_m12(double m12) {
        this.m12 = m12;
    }

    public void set_m03(double m03) {
        this.m03 = m03;
    }

    public void set_mu20(double mu20) {
        this.mu20 = mu20;
    }

    public void set_mu11(double mu11) {
        this.mu11 = mu11;
    }

    public void set_mu02(double mu02) {
        this.mu02 = mu02;
    }

    public void set_mu30(double mu30) {
        this.mu30 = mu30;
    }

    public void set_mu21(double mu21) {
        this.mu21 = mu21;
    }

    public void set_mu12(double mu12) {
        this.mu12 = mu12;
    }

    public void set_mu03(double mu03) {
        this.mu03 = mu03;
    }

    public void set_nu20(double nu20) {
        this.nu20 = nu20;
    }

    public void set_nu11(double nu11) {
        this.nu11 = nu11;
    }

    public void set_nu02(double nu02) {
        this.nu02 = nu02;
    }

    public void set_nu30(double nu30) {
        this.nu30 = nu30;
    }

    public void set_nu21(double nu21) {
        this.nu21 = nu21;
    }

    public void set_nu12(double nu12) {
        this.nu12 = nu12;
    }

    public void set_nu03(double nu03) {
        this.nu03 = nu03;
    }
}