package OooO00o.OooO0O0.OooO0O0;

import android.hardware.usb.UsbManager;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public enum OooO0OO {
    INTANCE;
    
    public WeakReference<UsbManager> OooO0OO;

    public synchronized UsbManager OooO00o() {
        WeakReference<UsbManager> weakReference;
        UsbManager usbManager;
        if (this.OooO0OO == null && (usbManager = (UsbManager) OooO00o.OooO0O0.getApplicationContext().getSystemService("usb")) != null) {
            this.OooO0OO = new WeakReference<>(usbManager);
        }
        weakReference = this.OooO0OO;
        if (weakReference != null) {
        } else {
            throw new RuntimeException("UsbManager 获取失败");
        }
        return weakReference.get();
    }
}
