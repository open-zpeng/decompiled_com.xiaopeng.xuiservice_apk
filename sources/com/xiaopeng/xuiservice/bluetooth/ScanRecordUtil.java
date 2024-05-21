package com.xiaopeng.xuiservice.bluetooth;

import android.os.ParcelUuid;
import android.util.SparseArray;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kotlin.UByte;
/* loaded from: classes5.dex */
public class ScanRecordUtil {
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA = 22;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecordUtil";
    private final int mAdvertiseFlags;
    private final byte[] mBytes;
    private final String mDeviceName;
    private final int mManufacturerId;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    private final List<ParcelUuid> mServiceUuids;
    private final int mTxPowerLevel;
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final ParcelUuid BASE_UUID = ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");

    static String toString(SparseArray<byte[]> array) {
        if (array == null) {
            return "null";
        }
        if (array.size() == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        for (int i = 0; i < array.size(); i++) {
            buffer.append(array.keyAt(i));
            buffer.append("=");
            buffer.append(Arrays.toString(array.valueAt(i)));
        }
        buffer.append('}');
        return buffer.toString();
    }

    public static String formatArrayData(SparseArray<byte[]> data) {
        if (data == null || data.size() < 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < data.size(); k++) {
            byte[] array = data.valueAt(k);
            if (array != null) {
                sb.append("key=0x" + Integer.toHexString(data.keyAt(k)) + ",data=0x");
                for (byte b : array) {
                    String hex = Integer.toHexString(b & UByte.MAX_VALUE);
                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    sb.append(hex);
                }
            }
        }
        return sb.toString().trim();
    }

    static <T> String toString(Map<T, byte[]> map) {
        if (map == null) {
            return "null";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        Iterator<Map.Entry<T, byte[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<T, byte[]> entry = it.next();
            Object key = entry.getKey();
            buffer.append(key);
            buffer.append("=");
            buffer.append(formatHexString(map.get(key)));
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    static String formatHexString(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        for (byte b : data) {
            String hex = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString().trim();
    }

    public int getAdvertiseFlags() {
        return this.mAdvertiseFlags;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return this.mManufacturerSpecificData.get(manufacturerId);
    }

    public int getManufacturerId() {
        return this.mManufacturerId;
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return this.mServiceData.get(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.mTxPowerLevel;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public String getManufacturerString() {
        SparseArray<byte[]> sparseArray = this.mManufacturerSpecificData;
        if (sparseArray == null || sparseArray.size() < 1) {
            return null;
        }
        return new String(this.mManufacturerSpecificData.valueAt(0));
    }

    private ScanRecordUtil(List<ParcelUuid> serviceUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, String localName, byte[] bytes, int manufacturerId) {
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mDeviceName = localName;
        this.mAdvertiseFlags = advertiseFlags;
        this.mTxPowerLevel = txPowerLevel;
        this.mBytes = bytes;
        this.mManufacturerId = manufacturerId;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:5|(4:(7:9|10|11|12|(8:52|53|54|55|(1:57)(1:63)|58|59|60)(5:14|15|16|(1:(1:19)(6:35|36|37|38|39|40))(3:46|47|48)|22)|6|7)|58|59|60)|70|71|54|55|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00d4, code lost:
        r18 = r2;
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00bd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.xiaopeng.xuiservice.bluetooth.ScanRecordUtil parseFromBytes(byte[] r20) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.bluetooth.ScanRecordUtil.parseFromBytes(byte[]):com.xiaopeng.xuiservice.bluetooth.ScanRecordUtil");
    }

    public String toString() {
        return "ScanRecord [mAdvertiseFlags=" + this.mAdvertiseFlags + ", mServiceUuids=" + this.mServiceUuids + ", mManufacturerSpecificData=" + formatArrayData(this.mManufacturerSpecificData) + ", mServiceData=" + toString(this.mServiceData) + ", mTxPowerLevel=" + this.mTxPowerLevel + ", mDeviceName=" + this.mDeviceName + ",manufacturerId=" + this.mManufacturerId + "]";
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & UByte.MAX_VALUE;
            char[] cArr = hexArray;
            hexChars[j * 2] = cArr[v >>> 4];
            hexChars[(j * 2) + 1] = cArr[v & 15];
        }
        return new String(hexChars);
    }

    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = extractBytes(scanRecord, currentPos, uuidLength);
            serviceUuids.add(parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }

    public static ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        long shortUuid;
        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length == 2 || length == 4 || length == 16) {
            if (length == 16) {
                ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
                long msb = buf.getLong(8);
                long lsb = buf.getLong(0);
                return new ParcelUuid(new UUID(msb, lsb));
            }
            if (length == 2) {
                long shortUuid2 = uuidBytes[0] & UByte.MAX_VALUE;
                shortUuid = shortUuid2 + ((uuidBytes[1] & UByte.MAX_VALUE) << 8);
            } else {
                long shortUuid3 = uuidBytes[0] & UByte.MAX_VALUE;
                shortUuid = ((uuidBytes[3] & UByte.MAX_VALUE) << 24) + shortUuid3 + ((uuidBytes[1] & UByte.MAX_VALUE) << 8) + ((uuidBytes[2] & UByte.MAX_VALUE) << 16);
            }
            long msb2 = BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32);
            long lsb2 = BASE_UUID.getUuid().getLeastSignificantBits();
            return new ParcelUuid(new UUID(msb2, lsb2));
        }
        throw new IllegalArgumentException("uuidBytes length invalid - " + length);
    }
}
