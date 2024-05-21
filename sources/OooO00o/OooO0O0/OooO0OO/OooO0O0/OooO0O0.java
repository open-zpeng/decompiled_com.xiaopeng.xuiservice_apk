package OooO00o.OooO0O0.OooO0OO.OooO0O0;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
/* loaded from: classes.dex */
public class OooO0O0 implements OooO00o {

    /* renamed from: OooO00o  reason: collision with root package name */
    public UsbDeviceConnection f241OooO00o;
    public UsbInterface OooO0O0;
    public int OooO0OO;
    public OooO0o OooO0Oo = new OooO();

    public OooO0O0(UsbInterface usbInterface) {
        this.OooO0O0 = usbInterface;
        this.OooO0OO = usbInterface.getId();
    }

    @Override // OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o
    public boolean OooO00o(UsbDevice usbDevice) {
        synchronized (OooO0O0.class) {
            try {
                try {
                    UsbDeviceConnection openDevice = OooO00o.OooO0O0.OooO0O0.OooO0OO.INTANCE.OooO00o().openDevice(usbDevice);
                    this.f241OooO00o = openDevice;
                    if (openDevice != null) {
                        if (openDevice.claimInterface(this.OooO0O0, true)) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                OooO00o();
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final byte[] OooO0O0() {
        byte[] bArr = {-1, 0, 0};
        byte[] bArr2 = new byte[3];
        ((OooO) this.OooO0Oo).OooO00o(this.f241OooO00o, this.OooO0OO, bArr, bArr2);
        byte[] bArr3 = {bArr2[0]};
        bArr[0] = (byte) (Math.random() * 128.0d);
        bArr[1] = 0;
        bArr[2] = 0;
        ((OooO) this.OooO0Oo).OooO00o(this.f241OooO00o, this.OooO0OO, bArr, bArr2);
        bArr3[0] = (byte) (-(bArr2[0] ^ (bArr[0] ^ bArr3[0])));
        return bArr3;
    }

    @Override // OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o
    public void OooO00o() {
        synchronized (OooO0O0.class) {
            UsbDeviceConnection usbDeviceConnection = this.f241OooO00o;
            if (usbDeviceConnection != null) {
                try {
                    usbDeviceConnection.releaseInterface(this.OooO0O0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    this.f241OooO00o.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            this.f241OooO00o = null;
        }
    }

    @Override // OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o
    public byte[] OooO00o(byte b, byte b2, byte b3) {
        synchronized (OooO0O0.class) {
            if (b == -1) {
                return OooO0O0();
            }
            byte[] bArr = new byte[3];
            byte[] bArr2 = {-1, 0, 0};
            ((OooO) this.OooO0Oo).OooO00o(this.f241OooO00o, this.OooO0OO, bArr2, bArr);
            byte[] bArr3 = {bArr[1], bArr[2]};
            bArr2[0] = b;
            bArr2[1] = b2;
            bArr2[2] = b3;
            ((OooO) this.OooO0Oo).OooO00o(this.f241OooO00o, this.OooO0OO, bArr2, bArr);
            if (b >= -16) {
                bArr3[0] = (byte) (bArr[1] ^ bArr3[0]);
                bArr3[1] = (byte) (bArr[2] ^ bArr3[1]);
            } else {
                bArr3[0] = bArr[1];
                bArr3[1] = bArr[2];
            }
            return bArr3;
        }
    }
}
