package OooO00o.OooO0O0.OooO0OO.OooO0O0;

import android.hardware.usb.UsbDeviceConnection;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.alibaba.fastjson.asm.Opcodes;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO implements OooO0o {
    public void OooO00o(@Nullable UsbDeviceConnection usbDeviceConnection, int i, @NotNull byte[] inByte, @NotNull byte[] outByte) {
        String str;
        String str2;
        String str3;
        String str4;
        Intrinsics.checkNotNullParameter(inByte, "inByte");
        Intrinsics.checkNotNullParameter(outByte, "outByte");
        byte[] message = new byte[4];
        byte[] mCmd = {5, inByte[0], inByte[1], inByte[2]};
        Intrinsics.checkNotNullParameter(mCmd, "mCmd");
        String str5 = "LogTuning";
        if (usbDeviceConnection == null) {
            str = "TransferNew";
            str2 = Progress.TAG;
            str3 = NotificationCompat.CATEGORY_MESSAGE;
            str4 = " -> ";
        } else {
            str = "TransferNew";
            str2 = Progress.TAG;
            str3 = NotificationCompat.CATEGORY_MESSAGE;
            str4 = " -> ";
            if (usbDeviceConnection.controlTransfer(33, 9, 517, i, mCmd, 4, 1000) >= 0) {
                str5 = "LogTuning";
            } else {
                Intrinsics.checkNotNullParameter(str, str2);
                Intrinsics.checkNotNullParameter(" send command fail", str3);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 > 5) {
                    str5 = "LogTuning";
                } else {
                    str5 = "LogTuning";
                    Log.e(str5, str + str4 + " send command fail");
                }
            }
        }
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intrinsics.checkNotNullParameter(message, "message");
        if (usbDeviceConnection != null) {
            String str6 = str5;
            if (usbDeviceConnection.controlTransfer(Opcodes.IF_ICMPLT, 1, 261, i, message, 4, 1000) < 0) {
                Intrinsics.checkNotNullParameter(str, str2);
                Intrinsics.checkNotNullParameter(" receive command fail", str3);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e(str6, str + str4 + " receive command fail");
                }
            }
        }
        outByte[0] = message[1];
        outByte[1] = message[2];
        outByte[2] = message[3];
    }
}
