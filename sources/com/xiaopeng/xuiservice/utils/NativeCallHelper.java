package com.xiaopeng.xuiservice.utils;

import android.content.pm.IPackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ServiceManager;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class NativeCallHelper {
    public static final String GET_DOLBY_PKG_CHANNEL_COUNT = "getDolbyPkgChannelCount";
    private static final String TAG = NativeCallHelper.class.getSimpleName();
    private static final NativeCallHelper instance = new NativeCallHelper();
    private IPackageManager mPackageManager;

    private NativeCallHelper() {
        this.mPackageManager = null;
        IBinder b = ServiceManager.getService("package");
        this.mPackageManager = IPackageManager.Stub.asInterface(b);
    }

    public static NativeCallHelper getInstance() {
        return instance;
    }

    public void nativeCall(Parcel data, Parcel reply) {
        String method = data.readString();
        if (((method.hashCode() == 952964534 && method.equals(GET_DOLBY_PKG_CHANNEL_COUNT)) ? (char) 0 : (char) 65535) == 0) {
            int pid = data.readInt();
            String str = TAG;
            LogUtil.i(str, "onTransact, native call in,method=" + method + ",param pid=" + pid);
            getDolbyPkgChannelCount(pid, reply);
        } else {
            String str2 = TAG;
            LogUtil.w(str2, "unknown method:" + method);
        }
        reply.writeNoException();
    }

    private void getDolbyPkgChannelCount(int pid, Parcel reply) {
        String pkg = PackageUtils.getPackageName(pid);
        String str = TAG;
        LogUtil.d(str, "getDolbyPkgChannelCount,pid->" + pid + ",pkg->" + pkg);
        try {
            xpPackageInfo pkgInfo = this.mPackageManager.getXpPackageInfo(pkg);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getDolbyPkgChannelCount,app type=");
            sb.append(pkgInfo != null ? Integer.valueOf(pkgInfo.appType) : null);
            LogUtil.d(str2, sb.toString());
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "getDolbyPkgChannelCount,e=" + e);
        }
        reply.writeInt(12);
    }
}
