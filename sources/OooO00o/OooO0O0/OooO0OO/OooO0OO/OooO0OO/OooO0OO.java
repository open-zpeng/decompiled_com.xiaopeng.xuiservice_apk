package OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO;

import OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o;
import OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo.OooO0o;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.irdeto.securesdk.core.SSUtils;
import com.loostone.libserver.version1.entity.request.BaseRequest;
import com.loostone.libserver.version1.net.NetParam;
import com.loostone.libserver.version1.net.NetRequest;
import com.xiaopeng.xuiservice.smart.condition.impl.time.CalendarUtil;
import java.util.HashMap;
import java.util.UUID;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public abstract class OooO0OO {

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final Long f256OooO00o;
    public final String OooO0OO;
    public final Context OooO0Oo;
    public final String OooO0O0 = getClass().getSimpleName();
    public Boolean OooO0o = Boolean.FALSE;
    public long OooO0oO = 0;
    public final OooO0o OooO0o0 = OooO00o();

    static {
        new Handler(Looper.getMainLooper());
        f256OooO00o = Long.valueOf((long) CalendarUtil.MS_OF_DAY);
    }

    public OooO0OO(Context context, String str) {
        this.OooO0Oo = context.getApplicationContext();
        this.OooO0OO = str;
    }

    public abstract OooO0o OooO00o();

    public void OooO00o(Boolean bool) {
        this.OooO0o = bool;
        this.OooO0oO = f256OooO00o.longValue();
    }

    public void OooO00o(Boolean bool, long j) {
        this.OooO0o = bool;
        this.OooO0oO = j;
    }

    public void OooO00o(String str, BaseRequest baseRequest, OooO00o oooO00o) {
        String str2;
        WifiManager wifiManager;
        WifiInfo connectionInfo;
        String string;
        String str3 = "";
        if (this.OooO0o.booleanValue() && (string = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0.getApplicationContext().getSharedPreferences(SSUtils.O00000o0, 0).getString(str, null)) != null && !string.equals("")) {
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O0 = (OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0) new Gson().fromJson(string, (Class<Object>) OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0.class);
            if (System.currentTimeMillis() - oooO0O0.OooO0Oo < this.OooO0oO && oooO00o != null && oooO0O0.OooO0O0) {
                oooO00o.OooO00o(((OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo.OooO00o) this.OooO0o0).OooO00o(oooO0O0.OooO0OO));
                return;
            }
        }
        baseRequest.set_time(System.currentTimeMillis() + "");
        baseRequest.setUuid(UUID.randomUUID().toString());
        Context context = this.OooO0Oo;
        String OooO0o0 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o0("sys/class/net/eth0/address");
        if (TextUtils.isEmpty(OooO0o0)) {
            OooO0o0 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o0("sys/class/net/eth0/wlan0");
        }
        if (TextUtils.isEmpty(OooO0o0) && (wifiManager = (WifiManager) context.getSystemService("wifi")) != null && (connectionInfo = wifiManager.getConnectionInfo()) != null) {
            str2 = connectionInfo.getMacAddress();
        } else {
            str2 = OooO0o0;
        }
        if (!TextUtils.isEmpty(str2)) {
            str3 = str2;
        } else {
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0o0.OooO0OO OooO00o2 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(new String[]{"busybox ifconfig"}, false, true);
            if (OooO00o2.f259OooO00o == 0) {
                String str4 = OooO00o2.OooO0O0;
                if (str4.length() > 0 && str4.contains("HWaddr")) {
                    try {
                        str3 = str4.substring(str4.indexOf("HWaddr") + 7, str4.indexOf("HWaddr") + 24);
                    } catch (Exception e) {
                    }
                }
            }
        }
        baseRequest.setMac(str3.trim());
        NetParam netParam = new NetParam();
        netParam.param = ((OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo.OooO00o) this.OooO0o0).OooO0O0(baseRequest);
        netParam.requestTag = this.OooO0OO;
        netParam.url = str + "?des=2";
        netParam.isRawRequest = true;
        ((OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo.OooO00o) this.OooO0o0).getClass();
        HashMap hashMap = new HashMap();
        hashMap.put("Content-Type", "text/plain");
        netParam.header = hashMap;
        netParam.retry = 3;
        netParam.timeout = 5000;
        OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O02 = new OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0();
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        oooO0O02.f257OooO00o = str;
        NetRequest.request(this.OooO0Oo, netParam, new OooO0O0(this, oooO00o, oooO0O02, baseRequest));
    }

    public void OooO00o(OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O0) {
        if (this.OooO0o.booleanValue()) {
            OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0, oooO0O0.f257OooO00o, new Gson().toJson(oooO0O0));
        }
    }
}
