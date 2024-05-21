package OooO00o.OooO0O0.OooO0o0.OooO0o;
/* loaded from: classes.dex */
public class OooOO0 {

    /* renamed from: OooO00o  reason: collision with root package name */
    public int f568OooO00o;
    public int OooO0O0;
    public int OooO0OO;
    public byte[] OooO0Oo;

    public OooOO0(int i) {
        this.OooO0OO = i;
        this.OooO0Oo = new byte[i];
    }

    public int OooO00o(byte[] bArr, int i, int i2) {
        int i3 = this.f568OooO00o;
        int min = Math.min(Math.min(bArr.length - i, i2), this.OooO0O0 - i3);
        if (min <= 0) {
            return 0;
        }
        int i4 = this.OooO0OO;
        int i5 = i3 % i4;
        if (i5 + min > i4) {
            int i6 = i4 - i5;
            System.arraycopy(this.OooO0Oo, i5, bArr, i, i6);
            System.arraycopy(this.OooO0Oo, 0, bArr, i + i6, min - i6);
        } else {
            System.arraycopy(this.OooO0Oo, i5, bArr, i, min);
        }
        this.f568OooO00o += min;
        return min;
    }

    public int OooO0O0(byte[] bArr, int i, int i2) {
        int i3 = this.f568OooO00o;
        int i4 = this.OooO0O0;
        int min = Math.min(this.OooO0OO - (i4 - i3), Math.min(bArr.length - i, i2));
        if (min <= 0) {
            return 0;
        }
        int i5 = this.OooO0OO;
        int i6 = i4 % i5;
        if (i6 + min > i5) {
            System.arraycopy(bArr, i, this.OooO0Oo, i6, i5 - i6);
            int i7 = this.OooO0OO;
            System.arraycopy(bArr, (i + i7) - i6, this.OooO0Oo, 0, min - (i7 - i6));
        } else {
            System.arraycopy(bArr, i, this.OooO0Oo, i6, min);
        }
        this.OooO0O0 += min;
        return min;
    }

    public void OooO0O0() {
        this.f568OooO00o = 0;
        this.OooO0O0 = 0;
    }

    public int OooO00o() {
        return this.OooO0O0 - this.f568OooO00o;
    }
}
