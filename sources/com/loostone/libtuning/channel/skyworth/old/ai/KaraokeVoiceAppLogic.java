package com.loostone.libtuning.channel.skyworth.old.ai;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.xapp.Constants;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class KaraokeVoiceAppLogic {
    private static final String TAG = "KaraokeVoiceAppLogic";
    private static KaraokeVoiceAppLogic mInstance;
    private final String CHANGBA_PACKAGE = Constants.PACKAGE_CHANGBA;
    private Context mContext;
    private KaraokeApp mKaraokeApp;

    private KaraokeVoiceAppLogic() {
        KaraokeApp karaokeApp = new KaraokeApp();
        this.mKaraokeApp = karaokeApp;
        karaokeApp.setFirstPackage(Constants.PACKAGE_CHANGBA);
        this.mKaraokeApp.setType(KaraokeApp.MODE_DFT);
    }

    private String getInstalledPackage(List<String> list) {
        for (String str : list) {
            if (isAppInstalled(this.mContext, str)) {
                return str;
            }
        }
        return Constants.PACKAGE_CHANGBA;
    }

    public static synchronized KaraokeVoiceAppLogic getInstance() {
        KaraokeVoiceAppLogic karaokeVoiceAppLogic;
        synchronized (KaraokeVoiceAppLogic.class) {
            if (mInstance == null) {
                mInstance = new KaraokeVoiceAppLogic();
            }
            karaokeVoiceAppLogic = mInstance;
        }
        return karaokeVoiceAppLogic;
    }

    private boolean isAppInstalled(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void deinit() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("deinit", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> deinit");
        }
        this.mContext = null;
        this.mKaraokeApp = null;
        mInstance = null;
    }

    public String getPackageName() {
        KaraokeApp karaokeApp = this.mKaraokeApp;
        if (karaokeApp == null) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("dft package", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> dft package");
                return Constants.PACKAGE_CHANGBA;
            }
            return Constants.PACKAGE_CHANGBA;
        }
        String firstPackage = karaokeApp.getFirstPackage();
        if ((this.mKaraokeApp.getType() == KaraokeApp.MODE_DFT_INSTALL || this.mKaraokeApp.getType() == KaraokeApp.MODE_FIRST_INSTALL) && this.mKaraokeApp.getSupportList() != null && this.mKaraokeApp.getSupportList().size() > 0) {
            firstPackage = getInstalledPackage(this.mKaraokeApp.getSupportList());
            String msg = "installed package:" + firstPackage;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
        }
        return firstPackage;
    }

    public void init(Context context) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("init", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> init");
        }
        this.mContext = context.getApplicationContext();
    }

    public boolean isForceUsePackageName() {
        KaraokeApp karaokeApp = this.mKaraokeApp;
        if (karaokeApp == null) {
            return false;
        }
        return karaokeApp.getType() == KaraokeApp.MODE_FIRST || this.mKaraokeApp.getType() == KaraokeApp.MODE_FIRST_INSTALL;
    }
}
