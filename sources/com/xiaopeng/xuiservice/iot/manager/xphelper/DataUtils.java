package com.xiaopeng.xuiservice.iot.manager.xphelper;

import android.os.SystemClock;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.ByteArrayOutputStream;
import kotlin.UByte;
/* loaded from: classes5.dex */
public class DataUtils {
    private static final String TAG = "Ble.DataUtils";
    private static int dataSize = 0;
    private static long assembleStartTick = 0;
    private static long lastUnit = 0;
    private static int unitLength = 20;
    private static ByteArrayOutputStream bos = null;
    private static boolean dataValid = false;
    private static boolean dataEnd = false;

    public static void reset() {
        dataSize = 0;
        assembleStartTick = 0L;
        lastUnit = 0L;
        unitLength = 20;
        dataValid = false;
        dataEnd = false;
        ByteArrayOutputStream byteArrayOutputStream = bos;
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.reset();
        }
    }

    public static String dataAssemble(byte[] data) {
        int totalUnit = ((data[0] & UByte.MAX_VALUE) << 8) | (data[1] & UByte.MAX_VALUE);
        int currentUnit = ((data[2] & UByte.MAX_VALUE) << 8) | (data[3] & UByte.MAX_VALUE);
        if (currentUnit == 0) {
            dataSize = 0;
            lastUnit = 0L;
            assembleStartTick = SystemClock.elapsedRealtime();
            unitLength = data.length;
            dataValid = true;
            dataEnd = false;
            ByteArrayOutputStream byteArrayOutputStream = bos;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.reset();
            }
            if (bos == null) {
                int data_length = (data.length - 4) * totalUnit;
                bos = new ByteArrayOutputStream(data_length);
            }
        } else {
            if (lastUnit + 1 != currentUnit) {
                LogUtil.w(TAG, "dataAssemble, discontinuous unit,expected:" + (lastUnit + 1) + ",but meet:" + currentUnit);
                dataValid = false;
            }
            lastUnit = currentUnit;
        }
        long transferTime = SystemClock.elapsedRealtime() - assembleStartTick;
        dataSize += data.length - 4;
        bos.write(data, 4, data.length - 4);
        if (currentUnit + 1 == totalUnit) {
            if (!dataValid) {
                LogUtil.w(TAG, "receive done but missed some units");
            } else {
                dataEnd = true;
            }
        }
        String info = "dataAssemble, total unit=" + totalUnit + ",current unit=" + currentUnit + ",current len=" + (data.length - 4) + ",received size=" + dataSize + ",transfer time=" + transferTime + "ms";
        LogUtil.d(TAG, info);
        if (dataEnd && dataValid) {
            return bos.toString();
        }
        return null;
    }
}
