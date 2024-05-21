package com.xiaopeng.xuiservice.karaoke.utils;

import android.os.SystemProperties;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.MotionEventCompat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
/* loaded from: classes5.dex */
public class KaraokeAlgorithm {
    public static final boolean DBG = true;
    public static final int LOG_FFT_SIZE = 10;
    public static final int MAX_FFT_SIZE = 1024;
    public static final String TAG = "KaraokeAlgorithm";
    public static final int mCaptureSize = SystemProperties.getInt("persist.fft.capture.size", 1024);
    static int[] twiddle = {32768, -13139967, -26312702, -39485434, -52658166, -65830897, -79003626, -92176354, -105349081, -118521806, -131628994, -144801717, -157974439, -171081624, -184188807, -197361525, -210468706, -223575886, -236683064, -249790242, -262831882, -275939056, -288980694, -302022330, -315063965, -328105599, -341081696, -354123327, -367099422, -380075515, -393051606, -405962161, -418938250, -431848803, -444759354, -457604367, -470514916, -483359927, -496139401, -508984410, -521763882, -534543353, -547257286, -560036754, -572750685, -585399079, -598113007, -610695863, -623344253, -635927106, -648509958, -661092809, -673610122, -686061898, -698579210, -711030984, -723417221, -735803456, -748189691, -760510388, -772831084, -785086243, -797341401, -809596558, -821786178, -833910260, -846034342, -858158422, -870216965, -882275507, -894268512, -906195979, -918123446, -930050911, -941912840, -953709231, -965505621, -977236474, -988967326, -1000632641, -1012297955, -1023897731, -1035431971, -1046966210, -1058434911, -1069903611, -1081306775, -1092644401, -1103982026, -1115254114, -1126526201, -1137732751, -1148873764, -1159949240, -1171024715, -1182034653, -1193044590, -1203988989, -1214867852, -1225681178, -1236494503, -1247242290, -1257924541, -1268606791, -1279223504, -1289774679, -1300260318, -1310745956, -1321166057, -1331520620, -1341809647, -1352098673, -1362322162, -1372480114, -1382572529, -1392664943, -1402626284, -1412587624, -1422483428, -1432313694, -1442143959, -1451843152, -1461542344, -1471175998, -1480744116, -1490246697, -1499683741, -1509120784, -1518426754, -1527732724, -1536973156, -1546148052, -1555257411, -1564301233, -1573345054, -1582257802, -1591105014, -1599952224, -1608733898, -1617384499, -1626035099, -1634620163, -1643139689, -1651593679, -1659982132, -1668305048, -1676562427, -1684754270, -1692946112, -1701006881, -1709002113, -1716931809, -1724861504, -1732660126, -1740393211, -1748060760, -1755728308, -1763264783, -1770735722, -1778141124, -1785546525, -1792820853, -1800029645, -1807172900, -1814250619, -1821262801, -1828209446, -1835090554, -1841906126, -1848656161, -1855340660, -1861894086, -1868447511, -1874935400, -1881292216, -1887583496, -1893874775, -1900034981, -1906129651, -1912158784, -1918122381, -1924020441, -1929852965, -1935554416, -1941255866, -1946826244, -1952396622, -1957835927, -1963209695, -1968517927, -1973760622, -1978872245, -1983983868, -1988964418, -1993879431, -1998728908, -2003512849, -2008231253, -2012884121, -2017405916, -2021927711, -2026318434, -2030643620, -2034837733, -2039031847, -2043160424, -2047157928, -2051089896, -2054956328, -2058757223, -2062427047, -2066096869, -2069635620, -2073108834, -2076516511, -2079858653, -2083069722, -2086215255, -2089295251, -2092309712, -2095258636, -2098076487, -2100894339, -2103581118, -2106136825, -2108692531, -2111117166, -2113541800, -2115835362, -2117997852, -2120160341, -2122191759, -2124157640, -2126057985, -2127892794, -2129596531, -2131300267, -2132872932, -2134314524, -2135756116, -2137066636, -2138311620, -2139491067, -2140604979, -2141587819, -2142505122, -2143356889, -2144143121, -2144863816, -2145453439, -2145977526, -2146436077, -2146763556, -2147025499, -2147287442, -2147352777};
    static int hiNumber = 0;

    static int mult(int a, int b) {
        return ((((a >> 16) * (b >> 16)) + (((short) a) * ((short) b))) & SupportMenu.CATEGORY_MASK) | (((((a >> 16) * ((short) b)) - (((short) a) * (b >> 16))) >> 16) & 65535);
    }

    static int half(int a) {
        return ((a >> 1) & (-32769)) | (32768 & a);
    }

    static void fixed_fft(int n, int[] v) {
        int scale = 10;
        int r = 0;
        for (int i = 1; i < n; i++) {
            int p = n;
            while ((p & r) == 0) {
                p >>= 1;
                r ^= p;
            }
            if (i < r) {
                int t = v[i];
                v[i] = v[r];
                v[r] = t;
            }
        }
        for (int p2 = 1; p2 < n; p2 <<= 1) {
            scale--;
            for (int i2 = 0; i2 < n; i2 += p2 << 1) {
                int x = half(v[i2]);
                int y = half(v[i2 + p2]);
                v[i2] = x + y;
                v[i2 + p2] = x - y;
            }
            for (int r2 = 1; r2 < p2; r2++) {
                int w = 256 - (r2 << scale);
                int i3 = w >> 31;
                int w2 = twiddle[(w ^ i3) - i3] ^ (i3 << 16);
                for (int i4 = r2; i4 < n; i4 += p2 << 1) {
                    int x2 = half(v[i4]);
                    int y2 = mult(w2, v[i4 + p2]);
                    v[i4] = x2 - y2;
                    v[i4 + p2] = x2 + y2;
                }
            }
        }
    }

    static void fixed_fft_real(int n, int[] v) {
        int scale = 10;
        int m = n >> 1;
        fixed_fft(n, v);
        int i = 1;
        while (i <= n) {
            i <<= 1;
            scale--;
        }
        v[0] = mult(~v[0], UVCControl.PU_DIGITAL_MULTIPLIER_LIMIT_CONTROL);
        v[m] = half(v[m]);
        for (int i2 = 1; i2 < (n >> 1); i2++) {
            int x = half(v[i2]);
            int z = half(v[n - i2]);
            int x2 = half((z ^ 65535) + x);
            int y = mult(z - (x ^ 65535), twiddle[i2 << scale]);
            v[i2] = x2 - y;
            v[n - i2] = 65535 ^ (x2 + y);
        }
    }

    public static int doDataFFt(int[] data, byte[] fft) {
        int i;
        int length = data.length;
        int i2 = mCaptureSize;
        if (length < i2) {
            LogUtil.e(TAG, "doDataFFt data length:" + data.length);
            return -1;
        }
        int[] workspace = new int[i2 >> 1];
        int nonzero = 0;
        int i3 = 0;
        while (true) {
            i = mCaptureSize;
            if (i3 >= i) {
                break;
            }
            workspace[i3 >> 1] = (((data[i3] & 255) ^ 128) << 24) | (((data[i3 + 1] & 255) ^ 128) << 8);
            nonzero |= workspace[i3 >> 1];
            i3 += 2;
        }
        if (nonzero != 0) {
            fixed_fft_real(i >> 1, workspace);
        }
        for (int i4 = 0; i4 < mCaptureSize; i4 += 2) {
            short tmp = (short) (workspace[i4 >> 1] >> 21);
            while (true) {
                if (tmp <= 127 && tmp >= -128) {
                    break;
                }
                tmp = (short) (tmp >> 1);
            }
            fft[i4] = (byte) (tmp & 255);
            short tmp2 = (short) (workspace[i4 >> 1] & 65535);
            short tmp3 = (short) (tmp2 >> 5);
            while (true) {
                if (tmp3 > 127 || tmp3 < -128) {
                    tmp3 = (short) (tmp3 >> 1);
                }
            }
            fft[i4 + 1] = (byte) (tmp3 & 255);
        }
        return 0;
    }

    public static void dataCut4To1(byte[] fft, byte[] fft_ext) {
        if (fft_ext.length != fft.length / 4) {
            LogUtil.e(TAG, "dataCut4To1 fft length:" + fft.length + "  fft_ext length:" + fft_ext.length);
            return;
        }
        for (int i = 0; i < fft_ext.length; i++) {
            fft_ext[i] = fft[i * 4];
        }
    }

    private static int builtin_clz(int number) {
        int bitCount = 1;
        int number2 = number >> 1;
        while (number2 > 0) {
            bitCount++;
            number2 >>= 1;
        }
        int result = 32 - bitCount;
        if (hiNumber < number2) {
            hiNumber = number2;
        }
        return result;
    }

    public static void preFFt(byte[] data, int[] fft_ext) {
        int[] workspace = new int[data.length / 2];
        hiNumber = 0;
        for (int i = 0; i < workspace.length; i++) {
            workspace[i] = ((short) ((data[(i * 2) + 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | ((short) (data[i * 2] & 255));
        }
        int shift = 32;
        for (int smp : workspace) {
            if (smp < 0) {
                smp = (-smp) - 1;
            }
            int clz = builtin_clz(smp);
            if (shift > clz) {
                shift = clz;
            }
        }
        int shift2 = 25 - shift;
        if (shift2 < 3) {
            shift2 = 3;
        }
        int shift3 = shift2 + 1;
        int len = workspace.length / 2;
        for (int i2 = 0; i2 < len; i2++) {
            fft_ext[i2] = ((workspace[i2 * 2] + workspace[(i2 * 2) + 1]) >> shift3) ^ 128;
        }
    }

    private static void dataCompress(byte[] data, byte[] dataCompressed) {
        int multiple = data.length / dataCompressed.length;
        for (int i = 0; i < dataCompressed.length; i += 4) {
            dataCompressed[i] = data[i * multiple];
            dataCompressed[i + 1] = data[(i * multiple) + 1];
            dataCompressed[i + 2] = data[(i * multiple) + 2];
            dataCompressed[i + 3] = data[(i * multiple) + 3];
        }
    }

    public static void doFFt(byte[] data, byte[] fft) {
        int[] fft_ext = new int[fft.length];
        byte[] dataCompressed = new byte[fft.length * 4];
        dataCompress(data, dataCompressed);
        preFFt(dataCompressed, fft_ext);
        doDataFFt(fft_ext, fft);
    }
}
