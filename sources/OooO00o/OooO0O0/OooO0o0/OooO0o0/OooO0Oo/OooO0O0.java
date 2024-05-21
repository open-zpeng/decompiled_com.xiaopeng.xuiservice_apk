package OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO;

import OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import com.loostone.libtuning.inf.extern.IMicControl;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0O0 implements IMicControl {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final byte[] f277OooO00o = {0};
    @Nullable
    public UsbDevice OooO0O0;
    public long OooO0OO;
    @NotNull
    public final Lazy OooO0Oo = LazyKt.lazy(OooO00o.f278OooO00o);

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<OooO0OO> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f278OooO00o = new OooO00o();

        public OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO0OO invoke() {
            return OooO0OO.f242OooO00o.OooO00o();
        }
    }

    public OooO0O0(@Nullable UsbDevice usbDevice) {
        this.OooO0O0 = usbDevice;
    }

    public final OooO0OO OooO00o() {
        return (OooO0OO) this.OooO0Oo.getValue();
    }

    @NotNull
    public byte[] OooO0O0() {
        return OooO00o(this, new byte[]{-9, 0, 0}, (byte[]) null, 2);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getAudioEffect() {
        switch (OooO00o(new byte[]{-20, 2, 0}, false)) {
            case 1:
            default:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getBaseStatus(int i) {
        byte[] OooO00o2 = OooO00o(new byte[]{-20, 0, 0});
        if (OooO00o2 == null) {
            return 0;
        }
        byte b = OooO00o2[1];
        byte b2 = OooO00o2[2];
        return i != 0 ? i != 1 ? (i == 2 && b2 == 1) ? 1 : 0 : b == 1 ? 1 : 0 : (b == 1 || b2 == 1) ? 1 : 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public double getMicBattery(int i) {
        byte[] OooO00o2 = OooO00o(new byte[]{-16, 2, 0});
        if (OooO00o2 == null) {
            return 0.0d;
        }
        double d = (OooO00o2[1] * 2) / 100.0d;
        double d2 = (OooO00o2[2] * 2) / 100.0d;
        if (i != 1) {
            if (i != 2) {
                return 0.0d;
            }
            return d2;
        }
        return d;
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public double getMicBatteryPercentage(int i) {
        byte[] OooO00o2;
        OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0O0.f561OooO00o;
        double d = 0.0d;
        if (OooO00o(new byte[]{-16, 2, 0}) != null) {
            double d2 = (OooO00o2[1] * 2) / 100.0d;
            double d3 = (OooO00o2[2] * 2) / 100.0d;
            if (i == 1) {
                d = d2;
            } else if (i == 2) {
                d = d3;
            }
        }
        int length = ((double[]) OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0O0.OooO0O0.getValue()).length;
        int i2 = 0;
        if (length >= 0) {
            int i3 = 0;
            while (true) {
                int i4 = i3 + 1;
                if (d > ((double[]) OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0O0.OooO0O0.getValue())[i3]) {
                    i2 = i3;
                    break;
                } else if (i3 == length) {
                    break;
                } else {
                    i3 = i4;
                }
            }
        }
        return ((int[]) OooO00o.OooO0O0.OooO0o0.OooO0o.OooO0O0.OooO0OO.getValue())[i2] / 100.0d;
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ1() {
        return OooO00o(new byte[]{-20, 32, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ2() {
        return OooO00o(new byte[]{-20, 33, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ3() {
        return OooO00o(new byte[]{-20, 34, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ4() {
        return OooO00o(new byte[]{-20, 35, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ5() {
        return OooO00o(new byte[]{-20, 36, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ6() {
        return OooO00o(new byte[]{-20, 37, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ7() {
        return OooO00o(new byte[]{-20, 38, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ8() {
        return OooO00o(new byte[]{-20, 39, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicEQ9() {
        return OooO00o(new byte[]{-20, 40, 0}, false);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicShiftFrequency() {
        byte[] OooO00o2 = OooO00o(new byte[]{-17, 19, 0});
        if (OooO00o2 == null) {
            return 0;
        }
        String msg = Intrinsics.stringPlus("getMicShiftFrequency: ", Byte.valueOf(OooO00o2[0]));
        Intrinsics.checkNotNullParameter("MicInfo", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "MicInfo -> " + msg);
        }
        return OooO00o2[0];
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int getMicVolumeHW(int i) {
        byte[] OooO00o2 = OooO00o(new byte[]{-16, 3, 0});
        if (OooO00o2 == null) {
            return 0;
        }
        byte b = OooO00o2[1];
        byte b2 = OooO00o2[2];
        if (i != 1) {
            if (i != 2) {
                return 0;
            }
            return b2;
        }
        return b;
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setAudioEffect(int i) {
        byte b;
        switch (i) {
            case 1:
            default:
                b = 1;
                break;
            case 2:
                b = 2;
                break;
            case 3:
                b = 3;
                break;
            case 4:
                b = 4;
                break;
            case 5:
                b = 5;
                break;
            case 6:
                b = 6;
                break;
            case 7:
                b = 7;
                break;
            case 8:
                b = 8;
                break;
        }
        return OooO00o(new byte[]{-20, 1, b}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ1(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 16, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ2(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 17, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ3(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 18, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ4(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 19, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ5(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 20, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ6(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 21, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ7(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 22, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ8(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 23, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicEQ9(int i) {
        if (i < -12 || i > 12) {
            return 0;
        }
        return OooO00o(new byte[]{-20, 24, (byte) i}, true);
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicShiftFrequency(int i) {
        byte[] OooO00o2 = OooO00o(new byte[]{-17, 20, (byte) i});
        if (OooO00o2 == null) {
            return 0;
        }
        String msg = "setMicShiftFrequency:" + ((int) OooO00o2[0]) + ", 0:成功 1:失败";
        Intrinsics.checkNotNullParameter("MicInfo", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "MicInfo -> " + msg);
        }
        return OooO00o2[0];
    }

    @Override // com.loostone.libtuning.inf.extern.IMicControl
    public int setMicSwitch(int i) {
        return OooO00o(new byte[]{-20, TarConstants.LF_NORMAL, (byte) i}, true);
    }

    public static byte[] OooO00o(OooO0O0 oooO0O0, byte[] bArr, byte[] bArr2, int i) {
        byte[] bArr3 = (i & 2) != 0 ? f277OooO00o : null;
        if (oooO0O0.OooO0O0 != null && bArr.length >= 3) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = oooO0O0.OooO0OO;
            if (currentTimeMillis < j) {
                oooO0O0.OooO0OO = currentTimeMillis;
            } else if (currentTimeMillis - j >= 1000) {
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
                synchronized (OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.OooO0O0) {
                    if (!oooO0O0.OooO00o().OooO0OO) {
                        OooO0OO OooO00o2 = oooO0O0.OooO00o();
                        UsbDevice usbDevice = oooO0O0.OooO0O0;
                        Intrinsics.checkNotNull(usbDevice);
                        if (!OooO00o2.OooO0OO(usbDevice)) {
                            oooO0O0.OooO0O0 = null;
                            oooO0O0.OooO00o().OooO00o();
                        }
                    }
                    bArr3 = oooO0O0.OooO00o().OooO00o(bArr[0], bArr[1], bArr[2]);
                }
            }
        }
        return bArr3;
    }

    public static String OooO00o(OooO0O0 oooO0O0, byte[] bArr, String str, int i) {
        String str2 = (i & 2) != 0 ? "0000" : null;
        if (oooO0O0.OooO0O0 != null && bArr.length >= 3) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = oooO0O0.OooO0OO;
            if (currentTimeMillis < j) {
                oooO0O0.OooO0OO = currentTimeMillis;
            } else if (currentTimeMillis - j >= 1000) {
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
                synchronized (OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.OooO0O0) {
                    if (!oooO0O0.OooO00o().OooO0OO) {
                        OooO0OO OooO00o2 = oooO0O0.OooO00o();
                        UsbDevice usbDevice = oooO0O0.OooO0O0;
                        Intrinsics.checkNotNull(usbDevice);
                        if (!OooO00o2.OooO0OO(usbDevice)) {
                            oooO0O0.OooO0O0 = null;
                            oooO0O0.OooO00o().OooO00o();
                        }
                    }
                    str2 = oooO0O0.OooO00o().OooO0O0(bArr[0], bArr[1], bArr[2]);
                }
            }
        }
        return str2;
    }

    public final int OooO00o(byte[] bArr, boolean z) {
        byte[] OooO00o2 = OooO00o(bArr);
        if (OooO00o2 == null) {
            return 0;
        }
        String msg = Intrinsics.stringPlus("result-size", Integer.valueOf(OooO00o2.length));
        Intrinsics.checkNotNullParameter("MicInfo", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MicInfo -> " + msg);
        }
        if (OooO00o2.length < 2) {
            return 0;
        }
        byte b = OooO00o2[1];
        return z ? b == 1 ? 0 : 1 : b;
    }

    public final byte[] OooO00o(byte[] bArr) {
        if (this.OooO0O0 != null && bArr.length >= 3) {
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
            synchronized (OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.OooO0O0) {
                if (!OooO00o().OooO0OO) {
                    OooO0OO OooO00o2 = OooO00o();
                    UsbDevice usbDevice = this.OooO0O0;
                    Intrinsics.checkNotNull(usbDevice);
                    if (!OooO00o2.OooO0OO(usbDevice)) {
                        this.OooO0O0 = null;
                        OooO00o().OooO00o();
                        return null;
                    }
                }
                return OooO00o().OooO00o(bArr[0], bArr[1], bArr[2]);
            }
        }
        return null;
    }
}
