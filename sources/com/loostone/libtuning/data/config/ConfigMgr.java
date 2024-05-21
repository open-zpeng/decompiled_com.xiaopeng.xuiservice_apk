package com.loostone.libtuning.data.config;

import android.os.Build;
import android.text.TextUtils;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\u0017\u0010\u0004\u001a\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u0017\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\nJ\u0017\u0010\u000b\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u000b\u0010\u0005J\u000f\u0010\f\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\f\u0010\r¨\u0006\u0010"}, d2 = {"Lcom/loostone/libtuning/data/config/ConfigMgr;", "", "", "extraPath", "getConfigPath", "(Ljava/lang/String;)Ljava/lang/String;", "", "model", "", "setModel", "(I)V", "initConfig", "getModelName", "()Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public abstract class ConfigMgr {
    public static /* synthetic */ String getConfigPath$default(ConfigMgr configMgr, String str, int i, Object obj) {
        if (obj == null) {
            if ((i & 1) != 0) {
                str = "";
            }
            return configMgr.getConfigPath(str);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getConfigPath");
    }

    @NotNull
    public final String getConfigPath(@NotNull String extraPath) {
        Intrinsics.checkNotNullParameter(extraPath, "extraPath");
        if (!TextUtils.isEmpty(extraPath)) {
            File file = new File(extraPath);
            File[] listFiles = file.listFiles();
            if (!file.exists() || listFiles == null) {
                return "";
            }
            if (listFiles.length == 0) {
                return "";
            }
        }
        return initConfig(extraPath);
    }

    @NotNull
    public String getModelName() {
        String MODEL = Build.MODEL;
        Intrinsics.checkNotNullExpressionValue(MODEL, "MODEL");
        return MODEL;
    }

    @NotNull
    public abstract String initConfig(@NotNull String str);

    public void setModel(int i) {
    }
}
