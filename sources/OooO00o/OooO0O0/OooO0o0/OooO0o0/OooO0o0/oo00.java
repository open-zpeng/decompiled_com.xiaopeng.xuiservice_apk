package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import OooO00o.OooO0O0.OooO0o0.OooO0o.OooOO0O;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class oo00 implements OooO0OO<Integer> {
    @Override // OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0OO
    public void OooO00o(Integer num) {
        Integer OooO00o2;
        int OooO0O0;
        String str;
        Integer OooO00o3;
        int OooO0O02;
        double[] dArr;
        double d;
        int i;
        double d2;
        int i2;
        int intValue = num.intValue();
        o0O0o00O o0o0o00o = o0O0o00O.f587OooO00o;
        OooO oooO = OooO.f279OooO00o;
        Integer OooO00o4 = oooO.OooO0o0().OooO00o();
        if ((OooO00o4 == null || OooO00o4.intValue() != 1) && ((OooO00o2 = oooO.OooOO0().OooO00o()) == null || OooO00o2.intValue() != 1)) {
            OooO0O0 = OooOO0O.f569OooO00o.OooO0O0(intValue);
            str = "DataSetting";
        } else {
            OooOO0O oooOO0O = OooOO0O.f569OooO00o;
            int OooO00o5 = oooOO0O.OooO00o(intValue);
            int OooO0O03 = oooOO0O.OooO0O0();
            Integer OooO00o6 = oooO.OooO0o0().OooO00o();
            if (OooO00o6 != null && OooO00o6.intValue() == 1) {
                Intrinsics.checkNotNullParameter("DataSetting", Progress.TAG);
                Intrinsics.checkNotNullParameter("micLevelFollowSysVol = 1", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "DataSetting -> micLevelFollowSysVol = 1");
                }
                OooO00o5 = (int) (((OooO00o5 * 1.0f) * OooO0O03) / 100.0f);
            }
            Integer OooO00o7 = oooO.OooOO0().OooO00o();
            if (OooO00o7 == null) {
                str = "DataSetting";
            } else if (OooO00o7.intValue() != 1) {
                str = "DataSetting";
            } else {
                Intrinsics.checkNotNullParameter("DataSetting", Progress.TAG);
                Intrinsics.checkNotNullParameter("sysVolCurveSwitch = 1", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "DataSetting -> sysVolCurveSwitch = 1");
                }
                double[] dArr2 = OooO00o.OooO0oO;
                double[] dArr3 = OooO00o.OooO0oo;
                if (OooO0O03 > 100) {
                    str = "DataSetting";
                    i2 = 1;
                    d2 = 1.0d;
                } else {
                    str = "DataSetting";
                    d2 = (OooO0O03 * 1.0d) / (100 * 1.0d);
                    i2 = 1;
                }
                while (d2 > dArr2[i2]) {
                    i2++;
                }
                int i3 = i2 - 1;
                double d3 = dArr2[i3];
                double d4 = dArr2[i2];
                double d5 = dArr3[i3];
                double d6 = ((((d2 - d3) / (d4 - d3)) * (dArr3[i2] - d5)) + d5) * OooO00o5;
                String msg = Intrinsics.stringPlus("sysvolcurve vol = ", Double.valueOf(d6));
                Intrinsics.checkNotNullParameter("VolumeCalculate", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "VolumeCalculate -> " + msg);
                }
                OooO00o5 = (int) d6;
            }
            OooO0O0 = (OooO00o5 * 256) / 100;
        }
        String msg2 = " micVolume(SO), oriVal = " + intValue + ", value = " + OooO0O0;
        String tag = str;
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", tag + " -> " + msg2);
        }
        o0O0o00O o0o0o00o2 = o0O0o00O.f587OooO00o;
        OooO oooO2 = OooO.f279OooO00o;
        Integer OooO00o8 = oooO2.OooO0o0().OooO00o();
        if ((OooO00o8 == null || OooO00o8.intValue() != 1) && ((OooO00o3 = oooO2.OooOO0().OooO00o()) == null || OooO00o3.intValue() != 1)) {
            OooO0O02 = OooOO0O.f569OooO00o.OooO0O0(intValue);
        } else {
            OooOO0O oooOO0O2 = OooOO0O.f569OooO00o;
            int OooO00o9 = oooOO0O2.OooO00o(intValue);
            int OooO0O04 = oooOO0O2.OooO0O0();
            Integer OooO00o10 = oooO2.OooO0o0().OooO00o();
            if (OooO00o10 != null && OooO00o10.intValue() == 1) {
                Intrinsics.checkNotNullParameter(tag, "tag");
                Intrinsics.checkNotNullParameter("micLevelFollowSysVol = 1", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", tag + " -> micLevelFollowSysVol = 1");
                }
                OooO00o9 = (int) (((OooO00o9 * 1.0f) * OooO0O04) / 100.0f);
            }
            Integer OooO00o11 = oooO2.OooOO0().OooO00o();
            if (OooO00o11 != null && OooO00o11.intValue() == 1) {
                Intrinsics.checkNotNullParameter(tag, "tag");
                Intrinsics.checkNotNullParameter("sysVolCurveSwitch = 1", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", tag + " -> sysVolCurveSwitch = 1");
                }
                double[] dArr4 = OooO00o.OooO;
                double[] dArr5 = OooO00o.OooOO0;
                if (OooO0O04 > 100) {
                    dArr = dArr4;
                    i = 1;
                    d = 1.0d;
                } else {
                    dArr = dArr4;
                    d = (OooO0O04 * 1.0d) / (100 * 1.0d);
                    i = 1;
                }
                while (d > dArr[i]) {
                    i++;
                }
                int i4 = i - 1;
                double d7 = dArr[i4];
                double d8 = dArr[i];
                double d9 = dArr5[i4];
                double d10 = ((((d - d7) / (d8 - d7)) * (dArr5[i] - d9)) + d9) * OooO00o9;
                String msg3 = Intrinsics.stringPlus("sysVolCurveSur vol = ", Double.valueOf(d10));
                Intrinsics.checkNotNullParameter("VolumeCalculate", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg3, "msg");
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "VolumeCalculate -> " + msg3);
                }
                OooO00o9 = (int) d10;
            }
            OooO0O02 = (OooO00o9 * 256) / 100;
        }
        String msg4 = " micVolume(SO), oriVal = " + intValue + ", surValue = " + OooO0O02;
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(msg4, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", tag + " -> " + msg4);
        }
        OooOO0.f398OooO00o.getClass();
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooOO0.o000O0.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(OooO0O02));
        o0O0o00O.f587OooO00o.OooO00o().OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(43).OooO0O0(0).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o(OooO0O0, 1).OooO00o());
    }
}
