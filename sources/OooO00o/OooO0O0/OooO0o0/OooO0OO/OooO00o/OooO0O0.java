package OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o;

import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.o0O0o000;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.irdeto.securesdk.core.SSUtils;
import com.loostone.libtuning.BuildConfig;
import com.loostone.libtuning.data.bean.EQItem;
import com.loostone.libtuning.data.config.ConfigMgr;
import com.loostone.libtuning.data.config.DefaultConfig;
import com.loostone.libtuning.data.config.load.effect.EffectModeConfig;
import com.loostone.libtuning.data.config.load.effect.EffectModeItem;
import com.loostone.libtuning.data.config.manager.XiaoPConfigMgr;
import com.loostone.libtuning.data.config.vendor.EnvironmentalSoundConfig;
import com.lzy.okgo.model.Progress;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0O0 {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0O0 f311OooO00o = new OooO0O0();
    @NotNull
    public static final Lazy OooO0O0 = LazyKt.lazy(OooO00o.f312OooO00o);
    public static int OooO0OO = -1;
    public static int OooO0Oo;

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<ConfigMgr> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f312OooO00o = new OooO00o();

        public OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public ConfigMgr invoke() {
            OooO0O0 oooO0O0 = OooO0O0.f311OooO00o;
            return new XiaoPConfigMgr();
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0O0$OooO0O0  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0013OooO0O0 implements OooO0O0.OooO00o {
        @Override // OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0.OooO00o
        public void OooO00o(int i) {
            OooO0O0 oooO0O0 = OooO0O0.f311OooO00o;
            OooO0O0.OooO0OO = i;
            OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO00o(OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO00o(-1), new OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0OO(), 100L, 0L, TimeUnit.MILLISECONDS);
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends OooO00o.OooO00o.OooO00o.OooO00o.OooOO0<String> {
        public OooO0OO() {
            super(null);
        }

        @Override // OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO
        public Object OooO00o() {
            EffectModeConfig effectModeConfig;
            Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter("LoadEffectMode initOnStart & TransportConfig transportSceneData", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InitConfig -> LoadEffectMode initOnStart & TransportConfig transportSceneData");
            }
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO00o oooO00o = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO00o.f550OooO00o;
            OooO oooO = OooO.f279OooO00o;
            Long OooO00o2 = oooO.OooO0O0().OooO00o();
            Intrinsics.checkNotNull(OooO00o2);
            long longValue = OooO00o2.longValue();
            Integer OooO00o3 = oooO.OooO0oO().OooO00o();
            Intrinsics.checkNotNull(OooO00o3);
            ArrayList<EffectModeItem> arrayList = null;
            try {
                effectModeConfig = (EffectModeConfig) new Gson().fromJson(oooO00o.OooO00o(longValue, OooO00o3.intValue()), (Class<Object>) EffectModeConfig.class);
            } catch (Exception e) {
                String msg = Intrinsics.stringPlus("load effect config error :: ", e);
                Intrinsics.checkNotNullParameter("LoadEffectMode", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", "LoadEffectMode -> " + msg);
                }
                effectModeConfig = null;
            }
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO00o.OooO0O0 = effectModeConfig == null ? 0 : effectModeConfig.getDefaultId();
            if (effectModeConfig != null) {
                arrayList = effectModeConfig.getEffectModeList();
            }
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO00o.OooO0OO = arrayList;
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0.f551OooO00o;
            oooO0O0.OooO00o();
            oooO0O0.OooO0O0();
            return "Execute method TransportConfig, transport scene data now.";
        }
    }

    public final void OooO00o(long j, int i, boolean z) {
        String OooO00o2 = OooO00o(j);
        if (!z) {
            OooO oooO = OooO.f279OooO00o;
            oooO.getClass();
            Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooO0o0.getValue()).OooO00o();
            boolean z2 = (num == null ? 0 : num.intValue()) >= 14;
            boolean areEqual = Intrinsics.areEqual(oooO.OooO0OO().OooO00o(), Boolean.TRUE);
            Lazy lazy = OooO.OooO0oO;
            String str = (String) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) lazy.getValue()).OooO00o();
            if (str == null) {
                str = "";
            }
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
            boolean areEqual2 = Intrinsics.areEqual(str, oooO00o.OooO00o(OooO00o2));
            Lazy lazy2 = OooO.OooO0o;
            String str2 = (String) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) lazy2.getValue()).OooO00o();
            if (str2 == null) {
                str2 = "";
            }
            boolean areEqual3 = Intrinsics.areEqual(str2, BuildConfig.VERSION_NAME);
            String msg = "version = " + z2 + ", init = " + areEqual + ", md5 = " + areEqual2 + ", versionName = " + areEqual3;
            Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InitConfig -> " + msg);
            }
            if (areEqual & z2 & areEqual2 & areEqual3) {
                Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context != null) {
                    String string = context.getApplicationContext().getSharedPreferences(SSUtils.O00000o0, 0).getString("init_config_path", null);
                    if (string == null) {
                        return;
                    }
                    String msg2 = Intrinsics.stringPlus("already init config, path = ", string);
                    Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
                    Intrinsics.checkNotNullParameter(msg2, "msg");
                    if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "InitConfig -> " + msg2);
                        return;
                    }
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("instance");
                throw null;
            }
            oooO.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Boolean>) Boolean.FALSE);
            ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) lazy2.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) BuildConfig.VERSION_NAME);
            oooO.OooO0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
            ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) lazy.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) oooO00o.OooO00o(OooO00o2));
        }
        OooO oooO2 = OooO.f279OooO00o;
        oooO2.getClass();
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooO0o0.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) 14);
        OooO00o(OooO00o(j, i));
        oooO2.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Boolean>) Boolean.TRUE);
    }

    public final void OooO0O0() {
        OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO00o(OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO00o(-1), new OooO0OO(), 0L, 0L, null);
    }

    @NotNull
    public final String OooO00o(long j, int i) {
        String OooO00o2 = OooO00o(j);
        if (new File(OooO00o2).exists()) {
            String msg = Intrinsics.stringPlus("外部路径或安装路径下配置-->path=", OooO00o2);
            Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InitConfig -> " + msg);
            }
            Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
            if (context != null) {
                OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(context, "init_config_path", OooO00o2);
                String OooO0Oo2 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0Oo(OooO00o2);
                Intrinsics.checkNotNullExpressionValue(OooO0Oo2, "{\n            LogTuning.i(TAG, \"外部路径或安装路径下配置-->path=$path\")\n            SharedPreferencesCacheUtil.saveData(FakeApplication.instance,Constant.CONFIG_PATH, path)\n\n            OldCommonUtil.getStringFromFile(path)\n        }");
                return OooO0Oo2;
            }
            Intrinsics.throwUninitializedPropertyAccessException("instance");
            throw null;
        }
        String msg2 = "  默认配置 channel: " + j + ", model: " + i;
        Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "InitConfig -> " + msg2);
        }
        String configPath$default = ConfigMgr.getConfigPath$default((ConfigMgr) OooO0O0.getValue(), null, 1, null);
        String msg3 = Intrinsics.stringPlus("  path: ", configPath$default);
        Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg3, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "InitConfig -> " + msg3);
        }
        Context context2 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
        if (context2 != null) {
            OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(context2, "init_config_path", configPath$default);
            String OooO00o3 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0O0.OooO00o(configPath$default);
            Intrinsics.checkNotNullExpressionValue(OooO00o3, "{\n            LogTuning.i(TAG, \"  默认配置 channel: $channel, model: $model\")\n            path = configMgr.getConfigPath()\n            LogTuning.i(TAG, \"  path: $path\")\n            SharedPreferencesCacheUtil.saveData(FakeApplication.instance,Constant.CONFIG_PATH, path)\n\n            ResourceUtils.readAssets2String(path)\n        }");
            return OooO00o3;
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    public final void OooO00o() {
        OooO oooO = OooO.f279OooO00o;
        if (Intrinsics.areEqual(oooO.OooO0OO().OooO00o(), Boolean.TRUE)) {
            OooOO0 oooOO0 = OooOO0.f398OooO00o;
            oooOO0.o0000o0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String>) oooOO0.o0000o0O().OooO00o());
            oooOO0.o0000O0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000O0O().OooO00o());
            oooOO0.o0000OoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000OoO().OooO00o());
            oooOO0.o0000o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000o0().OooO00o());
            Integer OooO00o2 = oooO.OooOO0O().OooO00o();
            boolean z = true;
            if ((OooO00o2 != null && OooO00o2.intValue() == 1) | (OooO00o2 != null && OooO00o2.intValue() == 3)) {
                oooOO0.o0000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000OOo().OooO00o());
            }
            if (OooO00o2 != null && OooO00o2.intValue() == 1) {
                oooOO0.o0000OOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000OOO().OooO00o());
                oooOO0.o0000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000OOo().OooO00o());
                oooOO0.o0000O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0000O0().OooO00o());
                oooOO0.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO00o().OooO00o());
                oooOO0.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0O0().OooO00o());
                oooOO0.OooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooOoo().OooO00o());
                Integer OooO0O02 = oooOO0.OooOO0O().OooO0O0();
                if (OooO0O02 != null) {
                    oooOO0.OooOO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O02.intValue()));
                }
                Integer OooO0O03 = oooOO0.OooOOO0().OooO0O0();
                if (OooO0O03 != null) {
                    oooOO0.OooOOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O03.intValue()));
                }
                Integer OooO0O04 = oooOO0.OooOOOO().OooO0O0();
                if (OooO0O04 != null) {
                    oooOO0.OooOOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O04.intValue()));
                }
                Integer OooO0O05 = oooOO0.OooOOo0().OooO0O0();
                if (OooO0O05 != null) {
                    oooOO0.OooOOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O05.intValue()));
                }
                Integer OooO0O06 = oooOO0.OooOOoo().OooO0O0();
                if (OooO0O06 != null) {
                    oooOO0.OooOOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O06.intValue()));
                }
                Integer OooO0O07 = oooOO0.OooOo0().OooO0O0();
                if (OooO0O07 != null) {
                    oooOO0.OooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O07.intValue()));
                }
                Integer OooO0O08 = oooOO0.OooOo0o().OooO0O0();
                if (OooO0O08 != null) {
                    oooOO0.OooOo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O08.intValue()));
                }
                Integer OooO0O09 = oooOO0.OooOoO0().OooO0O0();
                if (OooO0O09 != null) {
                    oooOO0.OooOoO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O09.intValue()));
                }
                Integer OooO0O010 = oooOO0.OooOoOO().OooO0O0();
                if (OooO0O010 != null) {
                    oooOO0.OooOoOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O010.intValue()));
                }
                Integer OooO0O011 = oooOO0.OooOO0o().OooO0O0();
                if (OooO0O011 != null) {
                    oooOO0.OooOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O011.intValue()));
                }
                Integer OooO0O012 = oooOO0.OooOOO().OooO0O0();
                if (OooO0O012 != null) {
                    oooOO0.OooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O012.intValue()));
                }
                Integer OooO0O013 = oooOO0.OooOOOo().OooO0O0();
                if (OooO0O013 != null) {
                    oooOO0.OooOOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O013.intValue()));
                }
                Integer OooO0O014 = oooOO0.OooOOo().OooO0O0();
                if (OooO0O014 != null) {
                    oooOO0.OooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O014.intValue()));
                }
                Integer OooO0O015 = oooOO0.OooOo00().OooO0O0();
                if (OooO0O015 != null) {
                    oooOO0.OooOo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O015.intValue()));
                }
                Integer OooO0O016 = oooOO0.OooOo0O().OooO0O0();
                if (OooO0O016 != null) {
                    oooOO0.OooOo0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O016.intValue()));
                }
                Integer OooO0O017 = oooOO0.OooOo().OooO0O0();
                if (OooO0O017 != null) {
                    oooOO0.OooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O017.intValue()));
                }
                Integer OooO0O018 = oooOO0.OooOoO().OooO0O0();
                if (OooO0O018 != null) {
                    oooOO0.OooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O018.intValue()));
                }
                Integer OooO0O019 = oooOO0.OooOoo0().OooO0O0();
                if (OooO0O019 != null) {
                    oooOO0.OooOoo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O019.intValue()));
                }
                Integer OooO0O020 = oooOO0.o00oO0o().OooO0O0();
                if (OooO0O020 != null) {
                    oooOO0.o00oO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O020.intValue()));
                }
                Integer OooO0O021 = oooOO0.o00oO0O().OooO0O0();
                if (OooO0O021 != null) {
                    oooOO0.o00oO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O021.intValue()));
                }
                Integer OooO0O022 = oooOO0.o0ooOO0().OooO0O0();
                if (OooO0O022 != null) {
                    oooOO0.o0ooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O022.intValue()));
                }
                Integer OooO0O023 = oooOO0.o0ooOOo().OooO0O0();
                if (OooO0O023 != null) {
                    oooOO0.o0ooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O023.intValue()));
                }
                Integer OooO0O024 = oooOO0.o0ooOoO().OooO0O0();
                if (OooO0O024 != null) {
                    oooOO0.o0ooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O024.intValue()));
                }
                Integer OooO0O025 = oooOO0.o0OOO0o().OooO0O0();
                if (OooO0O025 != null) {
                    oooOO0.o0OOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O025.intValue()));
                }
                Integer OooO0O026 = oooOO0.o0Oo0oo().OooO0O0();
                if (OooO0O026 != null) {
                    oooOO0.o0Oo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O026.intValue()));
                }
                Integer OooO0O027 = oooOO0.o0OO00O().OooO0O0();
                if (OooO0O027 != null) {
                    oooOO0.o0OO00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O027.intValue()));
                }
                Integer OooO0O028 = oooOO0.oo0o0Oo().OooO0O0();
                if (OooO0O028 != null) {
                    oooOO0.oo0o0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O028.intValue()));
                }
                Integer OooO0O029 = oooOO0.o0O0O00().OooO0O0();
                if (OooO0O029 != null) {
                    oooOO0.o0O0O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O029.intValue()));
                }
                Integer OooO0O030 = oooOO0.o000OOo().OooO0O0();
                if (OooO0O030 != null) {
                    oooOO0.o000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O030.intValue()));
                }
                Integer OooO0O031 = oooOO0.o000000().OooO0O0();
                if (OooO0O031 != null) {
                    oooOO0.o000000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O031.intValue()));
                }
                Integer OooO0O032 = oooOO0.o000000O().OooO0O0();
                if (OooO0O032 != null) {
                    oooOO0.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O032.intValue()));
                }
                Integer OooO0O033 = oooOO0.o000000o().OooO0O0();
                if (OooO0O033 != null) {
                    oooOO0.o000000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O033.intValue()));
                }
                Integer OooO0O034 = oooOO0.o00000().OooO0O0();
                if (OooO0O034 != null) {
                    oooOO0.o00000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O034.intValue()));
                }
                Integer OooO0O035 = oooOO0.o00000O0().OooO0O0();
                if (OooO0O035 != null) {
                    oooOO0.o00000O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O035.intValue()));
                }
                Integer OooO0O036 = oooOO0.o00000O().OooO0O0();
                if (OooO0O036 != null) {
                    oooOO0.o00000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O036.intValue()));
                }
                Integer OooO0O037 = oooOO0.o00000OO().OooO0O0();
                if (OooO0O037 != null) {
                    oooOO0.o00000OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O037.intValue()));
                }
                Integer OooO0O038 = oooOO0.Oooo0oO().OooO0O0();
                if (OooO0O038 != null) {
                    oooOO0.Oooo0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O038.intValue()));
                }
                Integer OooO0O039 = oooOO0.Oooo().OooO0O0();
                if (OooO0O039 != null) {
                    oooOO0.Oooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O039.intValue()));
                }
                Integer OooO0O040 = oooOO0.OoooO0().OooO0O0();
                if (OooO0O040 != null) {
                    oooOO0.OoooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O040.intValue()));
                }
                Integer OooO0O041 = oooOO0.OoooO().OooO0O0();
                if (OooO0O041 != null) {
                    oooOO0.OoooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O041.intValue()));
                }
                Integer OooO0O042 = oooOO0.o000oOoO().OooO0O0();
                if (OooO0O042 != null) {
                    oooOO0.o000oOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O042.intValue()));
                }
                Integer OooO0O043 = oooOO0.OoooOOo().OooO0O0();
                if (OooO0O043 != null) {
                    oooOO0.OoooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O043.intValue()));
                }
                Integer OooO0O044 = oooOO0.OoooOoO().OooO0O0();
                if (OooO0O044 != null) {
                    oooOO0.OoooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O044.intValue()));
                }
                Integer OooO0O045 = oooOO0.Ooooo00().OooO0O0();
                if (OooO0O045 != null) {
                    oooOO0.Ooooo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O045.intValue()));
                }
                Integer OooO0O046 = oooOO0.OooooO0().OooO0O0();
                if (OooO0O046 != null) {
                    oooOO0.OooooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O046.intValue()));
                }
                oooOO0.OooOooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooOooO().OooO00o());
                oooOO0.Oooo000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo000().OooO00o());
                oooOO0.Oooo00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo00o().OooO00o());
                oooOO0.Oooo00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo00O().OooO00o());
                oooOO0.OooOooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooOooo().OooO00o());
                oooOO0.Ooooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Ooooooo().OooO00o());
                oooOO0.o00O0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o00O0O().OooO00o());
                oooOO0.ooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.ooOO().OooO00o());
                oooOO0.o0OoOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o0OoOo0().OooO00o());
                oooOO0.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0Oo().OooO00o());
                oooOO0.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0oo().OooO00o());
                oooOO0.OooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooOO0().OooO00o());
                oooOO0.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0OO().OooO00o());
                oooOO0.OooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO().OooO00o());
                oooOO0.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0o0().OooO00o());
                Integer OooO00o3 = oooOO0.OooO0o().OooO00o();
                if ((OooO00o3 == null ? 0 : OooO00o3.intValue()) <= 0) {
                    Integer OooO00o4 = oooOO0.OooO0o0().OooO00o();
                    if ((OooO00o4 == null ? 0 : OooO00o4.intValue()) <= 80) {
                        oooOO0.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0o0().OooO00o());
                    } else {
                        oooOO0.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) 53);
                    }
                }
                Integer OooO00o5 = oooOO0.OooO0o0().OooO00o();
                int intValue = OooO00o5 == null ? 0 : OooO00o5.intValue();
                Integer OooO00o6 = oooOO0.OooO0oO().OooO00o();
                if (intValue >= (OooO00o6 != null ? OooO00o6.intValue() : 80)) {
                    oooOO0.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooO0o().OooO00o());
                }
                Integer OooO0O047 = oooOO0.OooO0oO().OooO0O0();
                if (OooO0O047 != null) {
                    int intValue2 = OooO0O047.intValue();
                    OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o0000O = oooOO0.o0000O();
                    Integer OooO00o7 = oooOO0.OooO0o0().OooO00o();
                    Intrinsics.checkNotNull(OooO00o7);
                    o0000O.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(intValue2 - OooO00o7.intValue()));
                }
                String msg = Intrinsics.stringPlus("carOutPregainAdd = ", oooOO0.o0000O().OooO00o());
                Intrinsics.checkNotNullParameter("PuremicConfig", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                    Log.i("LogTuning", "PuremicConfig -> " + msg);
                }
                Integer OooO0O048 = oooOO0.o0000oO().OooO0O0();
                if (OooO0O048 != null) {
                    oooOO0.o0000oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O048.intValue()));
                }
                Integer OooO0O049 = oooOO0.o0000oo().OooO0O0();
                if (OooO0O049 != null) {
                    oooOO0.o0000oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O049.intValue()));
                }
                Integer OooO0O050 = oooOO0.o0000Oo().OooO0O0();
                if (OooO0O050 != null) {
                    oooOO0.o0000Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO0O050.intValue()));
                }
                oooOO0.o00Oo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o00Oo0().OooO00o());
                oooOO0.o00Ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.o00Ooo().OooO00o());
                oooOO0.OooooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.OooooOo().OooO00o());
                oooOO0.Oooooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooooo0().OooO00o());
                oooOO0.Oooo0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo0O0().OooO00o());
                oooOO0.Oooo0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooOO0.Oooo0OO().OooO00o());
            }
            OooO0o oooO0o = OooO0o.f313OooO00o;
            oooO0o.getClass();
            Integer OooO00o8 = oooO.OooOO0O().OooO00o();
            boolean z2 = OooO00o8 == null || OooO00o8.intValue() != 2;
            if (OooO00o8 != null && OooO00o8.intValue() == 3) {
                z = false;
            }
            if (!(z2 & z)) {
                oooO0o.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.o000000O().OooO00o());
                oooO0o.o000000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.o000000o().OooO00o());
                oooO0o.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO00o().OooO00o());
                oooO0o.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0O0().OooO00o());
                oooO0o.OooOoOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooOoOO().OooO00o());
                oooO0o.OoooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OoooOo0().OooO00o());
                Integer OooO0O051 = oooO0o.OooO().OooO0O0();
                if (OooO0O051 != null) {
                    OooO0O051.intValue();
                    oooO0o.OooO().OooO00o();
                }
                Integer OooO0O052 = oooO0o.OooOO0O().OooO0O0();
                if (OooO0O052 != null) {
                    OooO0O052.intValue();
                    oooO0o.OooOO0O().OooO00o();
                }
                Integer OooO0O053 = oooO0o.OooOOO0().OooO0O0();
                if (OooO0O053 != null) {
                    OooO0O053.intValue();
                    oooO0o.OooOOO0().OooO00o();
                }
                Integer OooO0O054 = oooO0o.OooOOOO().OooO0O0();
                if (OooO0O054 != null) {
                    OooO0O054.intValue();
                    oooO0o.OooOOOO().OooO00o();
                }
                Integer OooO0O055 = oooO0o.OooOOo0().OooO0O0();
                if (OooO0O055 != null) {
                    OooO0O055.intValue();
                    oooO0o.OooOOo0().OooO00o();
                }
                Integer OooO0O056 = oooO0o.OooOOoo().OooO0O0();
                if (OooO0O056 != null) {
                    OooO0O056.intValue();
                    oooO0o.OooOOoo().OooO00o();
                }
                Integer OooO0O057 = oooO0o.OooOo0().OooO0O0();
                if (OooO0O057 != null) {
                    OooO0O057.intValue();
                    oooO0o.OooOo0().OooO00o();
                }
                Integer OooO0O058 = oooO0o.OooOo0o().OooO0O0();
                if (OooO0O058 != null) {
                    OooO0O058.intValue();
                    oooO0o.OooOo0o().OooO00o();
                }
                Integer OooO0O059 = oooO0o.OooOoO0().OooO0O0();
                if (OooO0O059 != null) {
                    OooO0O059.intValue();
                    oooO0o.OooOoO0().OooO00o();
                }
                Integer OooO0O060 = oooO0o.OooOO0().OooO0O0();
                if (OooO0O060 != null) {
                    OooO0O060.intValue();
                    oooO0o.OooOO0().OooO00o();
                }
                Integer OooO0O061 = oooO0o.OooOO0o().OooO0O0();
                if (OooO0O061 != null) {
                    OooO0O061.intValue();
                    oooO0o.OooOO0o().OooO00o();
                }
                Integer OooO0O062 = oooO0o.OooOOO().OooO0O0();
                if (OooO0O062 != null) {
                    OooO0O062.intValue();
                    oooO0o.OooOOO().OooO00o();
                }
                Integer OooO0O063 = oooO0o.OooOOOo().OooO0O0();
                if (OooO0O063 != null) {
                    OooO0O063.intValue();
                    oooO0o.OooOOOo().OooO00o();
                }
                Integer OooO0O064 = oooO0o.OooOOo().OooO0O0();
                if (OooO0O064 != null) {
                    OooO0O064.intValue();
                    oooO0o.OooOOo().OooO00o();
                }
                Integer OooO0O065 = oooO0o.OooOo00().OooO0O0();
                if (OooO0O065 != null) {
                    OooO0O065.intValue();
                    oooO0o.OooOo00().OooO00o();
                }
                Integer OooO0O066 = oooO0o.OooOo0O().OooO0O0();
                if (OooO0O066 != null) {
                    OooO0O066.intValue();
                    oooO0o.OooOo0O().OooO00o();
                }
                Integer OooO0O067 = oooO0o.OooOo().OooO0O0();
                if (OooO0O067 != null) {
                    OooO0O067.intValue();
                    oooO0o.OooOo().OooO00o();
                }
                Integer OooO0O068 = oooO0o.OooOoO().OooO0O0();
                if (OooO0O068 != null) {
                    OooO0O068.intValue();
                    oooO0o.OooOoO().OooO00o();
                }
                Integer OooO0O069 = oooO0o.Oooooo().OooO0O0();
                if (OooO0O069 != null) {
                    OooO0O069.intValue();
                    oooO0o.Oooooo().OooO00o();
                }
                Integer OooO0O070 = oooO0o.OoooooO().OooO0O0();
                if (OooO0O070 != null) {
                    OooO0O070.intValue();
                    oooO0o.OoooooO().OooO00o();
                }
                Integer OooO0O071 = oooO0o.Ooooooo().OooO0O0();
                if (OooO0O071 != null) {
                    OooO0O071.intValue();
                    oooO0o.Ooooooo().OooO00o();
                }
                Integer OooO0O072 = oooO0o.o0OoOo0().OooO0O0();
                if (OooO0O072 != null) {
                    OooO0O072.intValue();
                    oooO0o.o0OoOo0().OooO00o();
                }
                Integer OooO0O073 = oooO0o.ooOO().OooO0O0();
                if (OooO0O073 != null) {
                    OooO0O073.intValue();
                    oooO0o.ooOO().OooO00o();
                }
                Integer OooO0O074 = oooO0o.o00O0O().OooO0O0();
                if (OooO0O074 != null) {
                    OooO0O074.intValue();
                    oooO0o.o00O0O().OooO00o();
                }
                Integer OooO0O075 = oooO0o.o00Oo0().OooO0O0();
                if (OooO0O075 != null) {
                    OooO0O075.intValue();
                    oooO0o.o00Oo0().OooO00o();
                }
                Integer OooO0O076 = oooO0o.o00Ooo().OooO0O0();
                if (OooO0O076 != null) {
                    OooO0O076.intValue();
                    oooO0o.o00Ooo().OooO00o();
                }
                Integer OooO0O077 = oooO0o.o00o0O().OooO0O0();
                if (OooO0O077 != null) {
                    OooO0O077.intValue();
                    oooO0o.o00o0O().OooO00o();
                }
                Integer OooO0O078 = oooO0o.o00ooo().OooO0O0();
                if (OooO0O078 != null) {
                    OooO0O078.intValue();
                    oooO0o.o00ooo().OooO00o();
                }
                Integer OooO0O079 = oooO0o.oo000o().OooO0O0();
                if (OooO0O079 != null) {
                    OooO0O079.intValue();
                    oooO0o.oo000o().OooO00o();
                }
                Integer OooO0O080 = oooO0o.o00oO0o().OooO0O0();
                if (OooO0O080 != null) {
                    OooO0O080.intValue();
                    oooO0o.o00oO0o().OooO00o();
                }
                Integer OooO0O081 = oooO0o.o00oO0O().OooO0O0();
                if (OooO0O081 != null) {
                    OooO0O081.intValue();
                    oooO0o.o00oO0O().OooO00o();
                }
                Integer OooO0O082 = oooO0o.o0ooOO0().OooO0O0();
                if (OooO0O082 != null) {
                    OooO0O082.intValue();
                    oooO0o.o0ooOO0().OooO00o();
                }
                Integer OooO0O083 = oooO0o.o0ooOOo().OooO0O0();
                if (OooO0O083 != null) {
                    OooO0O083.intValue();
                    oooO0o.o0ooOOo().OooO00o();
                }
                Integer OooO0O084 = oooO0o.o0ooOoO().OooO0O0();
                if (OooO0O084 != null) {
                    OooO0O084.intValue();
                    oooO0o.o0ooOoO().OooO00o();
                }
                Integer OooO0O085 = oooO0o.o0OOO0o().OooO0O0();
                if (OooO0O085 != null) {
                    OooO0O085.intValue();
                    oooO0o.o0OOO0o().OooO00o();
                }
                Integer OooO0O086 = oooO0o.o0Oo0oo().OooO0O0();
                if (OooO0O086 != null) {
                    OooO0O086.intValue();
                    oooO0o.o0Oo0oo().OooO00o();
                }
                Integer OooO0O087 = oooO0o.Oooo00o().OooO0O0();
                if (OooO0O087 != null) {
                    OooO0O087.intValue();
                    oooO0o.Oooo00o().OooO00o();
                }
                Integer OooO0O088 = oooO0o.Oooo0O0().OooO0O0();
                if (OooO0O088 != null) {
                    OooO0O088.intValue();
                    oooO0o.Oooo0O0().OooO00o();
                }
                Integer OooO0O089 = oooO0o.Oooo0o0().OooO0O0();
                if (OooO0O089 != null) {
                    OooO0O089.intValue();
                    oooO0o.Oooo0o0().OooO00o();
                }
                Integer OooO0O090 = oooO0o.Oooo0oO().OooO0O0();
                if (OooO0O090 != null) {
                    OooO0O090.intValue();
                    oooO0o.Oooo0oO().OooO00o();
                }
                Integer OooO0O091 = oooO0o.Oooo().OooO0O0();
                if (OooO0O091 != null) {
                    OooO0O091.intValue();
                    oooO0o.Oooo().OooO00o();
                }
                Integer OooO0O092 = oooO0o.OoooO0().OooO0O0();
                if (OooO0O092 != null) {
                    OooO0O092.intValue();
                    oooO0o.OoooO0().OooO00o();
                }
                Integer OooO0O093 = oooO0o.OoooO().OooO0O0();
                if (OooO0O093 != null) {
                    OooO0O093.intValue();
                    oooO0o.OoooO().OooO00o();
                }
                Integer OooO0O094 = oooO0o.o000oOoO().OooO0O0();
                if (OooO0O094 != null) {
                    OooO0O094.intValue();
                    oooO0o.o000oOoO().OooO00o();
                }
                Integer OooO0O095 = oooO0o.OoooOOo().OooO0O0();
                if (OooO0O095 != null) {
                    OooO0O095.intValue();
                    oooO0o.OoooOOo().OooO00o();
                }
                oooO0o.OooOoo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooOoo0().OooO00o());
                oooO0o.OooOooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooOooO().OooO00o());
                oooO0o.Oooo000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.Oooo000().OooO00o());
                oooO0o.OooOooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooOooo().OooO00o());
                oooO0o.OooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooOoo().OooO00o());
                oooO0o.OoooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OoooOoO().OooO00o());
                oooO0o.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.Ooooo0o().OooO00o());
                oooO0o.Ooooo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.Ooooo00().OooO00o());
                oooO0o.OoooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OoooOoo().OooO00o());
                oooO0o.OooooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooooO0().OooO00o());
                oooO0o.o0O0O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.o0O0O00().OooO00o());
                oooO0o.o0OO00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.o0OO00O().OooO00o());
                oooO0o.oo0o0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.oo0o0Oo().OooO00o());
                oooO0o.o000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.o000OOo().OooO00o());
                oooO0o.OooooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooooOO().OooO00o());
                oooO0o.OooooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooooOo().OooO00o());
                oooO0o.Oooooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.Oooooo0().OooO00o());
                oooO0o.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0Oo().OooO00o());
                oooO0o.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0o().OooO00o());
                oooO0o.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0oo().OooO00o());
                oooO0o.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0OO().OooO00o());
                oooO0o.OooO0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0oO().OooO00o());
                oooO0o.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) oooO0o.OooO0o0().OooO00o());
            }
        }
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO oooO0OO = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.f553OooO00o;
        Long OooO00o9 = oooO.OooO0O0().OooO00o();
        Intrinsics.checkNotNull(OooO00o9);
        long longValue = OooO00o9.longValue();
        Integer OooO00o10 = oooO.OooO0oO().OooO00o();
        Intrinsics.checkNotNull(OooO00o10);
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0 = ((EnvironmentalSoundConfig) new Gson().fromJson(oooO0OO.OooO00o(longValue, OooO00o10.intValue()), (Class<Object>) EnvironmentalSoundConfig.class)).getEnvironmentalSoundList();
        String OooO0O096 = o0O0o000.f584OooO00o.OooO0O0();
        String msg2 = "initOnStart, transportScenePkgSize = " + OooO0OO + ", scenePkgSize = " + OooO0O096;
        Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "InitConfig -> " + msg2);
        }
        if (TextUtils.isEmpty(OooO0O096) || TextUtils.equals(OooO0O096, "0") || !TextUtils.equals(OooO0O096, String.valueOf(OooO0OO))) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0.f551OooO00o;
            C0013OooO0O0 listener = new C0013OooO0O0();
            Intrinsics.checkNotNullParameter(listener, "listener");
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0.OooO0OO = listener;
            OooO0O0();
        }
    }

    public final void OooO00o(@Nullable String str) {
        DefaultConfig defaultConfig = (DefaultConfig) new Gson().fromJson(str, (Class<Object>) DefaultConfig.class);
        if (defaultConfig == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("json.isNullOrEmpty() = ");
            sb.append(str == null || str.length() == 0);
            sb.append(",读取配置文件失败,加载默认配置");
            String msg = sb.toString();
            Intrinsics.checkNotNullParameter("InitConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InitConfig -> " + msg);
            }
            Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
            if (context != null) {
                OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(context, "init_config_path", "config.json");
                defaultConfig = (DefaultConfig) new Gson().fromJson(OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0O0.OooO00o("config.json"), (Class<Object>) DefaultConfig.class);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("instance");
                throw null;
            }
        }
        OooO oooO = OooO.f279OooO00o;
        Intrinsics.checkNotNullExpressionValue(defaultConfig, "defaultConfig");
        oooO.getClass();
        Intrinsics.checkNotNullParameter(defaultConfig, "defaultConfig");
        oooO.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Long>) Long.valueOf(defaultConfig.getChannel()));
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooO.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(defaultConfig.getAudioRecord()));
        oooO.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAudioTrack()));
        oooO.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getLogLevel()));
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOO0o.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(defaultConfig.getListenSystemVolume()));
        oooO.OooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getSysVolCurveSwitch()));
        oooO.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMicLevelFollowSysVol()));
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOOOO.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(defaultConfig.getListenMicElectricity()));
        oooO.OooOO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getWorkModeInt()));
        oooO.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMicVolumeStep()));
        oooO.OooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getReverbVolumeStep()));
        oooO.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMusicVolumeStep()));
        Integer OooO00o2 = oooO.OooO0o().OooO00o();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.f310OooO00o = OooO00o2 == null ? 5 : OooO00o2.intValue();
        Integer OooO00o3 = oooO.OooO().OooO00o();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0 = OooO00o3 == null ? 5 : OooO00o3.intValue();
        Integer OooO00o4 = oooO.OooO0oo().OooO00o();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0OO = OooO00o4 == null ? 5 : OooO00o4.intValue();
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOOoo.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(defaultConfig.getKeyMode()));
        double[] volumeCurveUI = defaultConfig.getVolumeCurveUI();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0Oo = volumeCurveUI;
        Intrinsics.checkNotNullExpressionValue(volumeCurveUI, "volumeCurveUI");
        if (volumeCurveUI.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0Oo = new double[]{0.0d, 0.2d, 0.4d, 0.6d, 0.8d, 1.0d};
        }
        String arrays = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0Oo);
        Intrinsics.checkNotNullExpressionValue(arrays, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOo00.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays);
        double[] volumeCurveSystem = defaultConfig.getVolumeCurveSystem();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o0 = volumeCurveSystem;
        Intrinsics.checkNotNullExpressionValue(volumeCurveSystem, "volumeCurveSystem");
        if (volumeCurveSystem.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o0 = new double[]{0.0d, 0.2d, 0.4d, 0.6d, 0.8d, 1.0d};
        }
        String arrays2 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o0);
        Intrinsics.checkNotNullExpressionValue(arrays2, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOo0.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays2);
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o = defaultConfig.getVolumeCurveMax();
        double[] volumeCurveSystem2 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o0;
        Intrinsics.checkNotNullExpressionValue(volumeCurveSystem2, "volumeCurveSystem");
        if (volumeCurveSystem2.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o = new int[]{256, 256, 256, 256, 256};
        }
        String arrays3 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0o);
        Intrinsics.checkNotNullExpressionValue(arrays3, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOo0O.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays3);
        double[] sysVolcurveX = defaultConfig.getSysVolcurveX();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oO = sysVolcurveX;
        Intrinsics.checkNotNullExpressionValue(sysVolcurveX, "sysVolcurveX");
        if (sysVolcurveX.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oO = new double[]{0.0d, 0.5d, 1.0d};
        }
        String arrays4 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oO);
        Intrinsics.checkNotNullExpressionValue(arrays4, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOo0o.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays4);
        double[] sysVolcurveY = defaultConfig.getSysVolcurveY();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oo = sysVolcurveY;
        Intrinsics.checkNotNullExpressionValue(sysVolcurveY, "sysVolcurveY");
        if (sysVolcurveY.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oo = new double[]{3.0d, 1.7d, 1.0d};
        }
        String arrays5 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oo);
        Intrinsics.checkNotNullExpressionValue(arrays5, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOo.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays5);
        double[] sysVolcurveSurX = defaultConfig.getSysVolcurveSurX();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO = sysVolcurveSurX;
        Intrinsics.checkNotNullExpressionValue(sysVolcurveSurX, "sysVolcurveSurX");
        if (sysVolcurveSurX.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oO;
        }
        String arrays6 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO);
        Intrinsics.checkNotNullExpressionValue(arrays6, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOoO0.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays6);
        double[] sysVolcurveSurY = defaultConfig.getSysVolcurveSurY();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0 = sysVolcurveSurY;
        Intrinsics.checkNotNullExpressionValue(sysVolcurveSurY, "sysVolcurveSurY");
        if (sysVolcurveSurY.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0oo;
        }
        String arrays7 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0);
        Intrinsics.checkNotNullExpressionValue(arrays7, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOoO.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays7);
        double[] micVolcurveX = defaultConfig.getMicVolcurveX();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0O = micVolcurveX;
        Intrinsics.checkNotNullExpressionValue(micVolcurveX, "micVolcurveX");
        if (micVolcurveX.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0O = new double[]{0.0d, 0.5d, 1.0d};
        }
        String arrays8 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0O);
        Intrinsics.checkNotNullExpressionValue(arrays8, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOoOO.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays8);
        double[] micVolcurveY = defaultConfig.getMicVolcurveY();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0o = micVolcurveY;
        Intrinsics.checkNotNullExpressionValue(micVolcurveY, "micVolcurveY");
        if (micVolcurveY.length == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0o = new double[]{0.0d, 0.7d, 1.0d};
        }
        String arrays9 = Arrays.toString(OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooOO0o);
        Intrinsics.checkNotNullExpressionValue(arrays9, "java.util.Arrays.toString(this)");
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOoo0.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) arrays9);
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOooO.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(defaultConfig.getHardwareMicVolume()));
        OooOO0 oooOO0 = OooOO0.f398OooO00o;
        oooOO0.getClass();
        Intrinsics.checkNotNullParameter(defaultConfig, "defaultConfig");
        oooOO0.o0000o0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String>) defaultConfig.getWorkMode());
        oooOO0.o0000O0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getBeta()));
        oooOO0.o0000OoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getVolSft()));
        oooOO0.o0000o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getVolStr()));
        Integer OooO00o5 = oooO.OooOO0O().OooO00o();
        if ((OooO00o5 != null && OooO00o5.intValue() == 1) | (OooO00o5 != null && OooO00o5.intValue() == 3)) {
            oooOO0.o0000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMusicVolume()));
        }
        if (OooO00o5 != null && OooO00o5.intValue() == 1) {
            oooOO0.o0000OOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMicVolume()));
            oooOO0.o0000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMusicVolume()));
            oooOO0.o0000O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getBalanceVolume()));
            oooOO0.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef1ReverbEnable()));
            oooOO0.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef1ReverbOutGain()));
            oooOO0.OooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EQEnable()));
            oooOO0.OooooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2MusicEQEnable()));
            oooOO0.OooOooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoEnable()));
            oooOO0.Oooo000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoGain()));
            oooOO0.Oooo00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoType()));
            oooOO0.Oooo00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoTime()));
            oooOO0.OooOooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoFeedback()));
            oooOO0.Ooooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2ReverbEnable()));
            oooOO0.o00O0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2ReverbVolume()));
            oooOO0.o0OoOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2ReverbTime()));
            oooOO0.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorEnable()));
            oooOO0.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorRatio()));
            oooOO0.OooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorThreshold()));
            oooOO0.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorAttack()));
            oooOO0.OooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorRelease()));
            oooOO0.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorPregain()));
            oooOO0.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorPregain()));
            oooOO0.o00Oo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2VoiceChangeEnable()));
            oooOO0.o00Ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2VoiceMode()));
            oooOO0.Oooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2FreqShiftChangeEnable()));
            Integer debugPlaySinEnable = defaultConfig.getDebugPlaySinEnable();
            if (debugPlaySinEnable != null) {
                int intValue = debugPlaySinEnable.intValue();
                oooOO0.getClass();
                ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooOO0.o000O000.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(intValue));
            }
            Integer aef2CompressorPregainOut = defaultConfig.getAef2CompressorPregainOut();
            if (aef2CompressorPregainOut != null) {
                int intValue2 = aef2CompressorPregainOut.intValue();
                oooOO0.OooO0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(intValue2));
                oooOO0.o0000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(intValue2 - defaultConfig.getAef2CompressorPregain()));
            }
            Integer aef4SurroundPregain = defaultConfig.getAef4SurroundPregain();
            if (aef4SurroundPregain != null) {
                oooOO0.o0000oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(aef4SurroundPregain.intValue()));
            }
            Integer aef4SurroundMusicVol = defaultConfig.getAef4SurroundMusicVol();
            if (aef4SurroundMusicVol != null) {
                oooOO0.o0000oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(aef4SurroundMusicVol.intValue()));
            }
            Integer recordVocalOffset = defaultConfig.getRecordVocalOffset();
            if (recordVocalOffset != null) {
                oooOO0.o0000Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(recordVocalOffset.intValue()));
            }
            oooOO0.OooooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2MusicFFTCalculateFrequency()));
            oooOO0.Oooooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2MusicFFTCount()));
            oooOO0.Oooo0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2MicFFTCalculateFrequency()));
            oooOO0.Oooo0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2MicFFTCount()));
        }
        OooO0o oooO0o = OooO0o.f313OooO00o;
        oooO0o.getClass();
        Intrinsics.checkNotNullParameter(defaultConfig, "defaultConfig");
        Integer OooO00o6 = oooO.OooOO0O().OooO00o();
        if (!((OooO00o6 == null || OooO00o6.intValue() != 3) & (OooO00o6 == null || OooO00o6.intValue() != 2))) {
            oooO0o.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getMicVolume()));
            oooO0o.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef1ReverbEnable()));
            oooO0o.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef1ReverbOutGain()));
            oooO0o.OooOoOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EQEnable()));
            oooO0o.OoooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2MusicEQEnable()));
            oooO0o.OooOoo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoEnable()));
            oooO0o.OooOooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoGain()));
            oooO0o.Oooo000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoType()));
            oooO0o.OooOooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoTime()));
            oooO0o.OooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2EchoFeedback()));
            oooO0o.OoooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2ReverbEnable()));
            oooO0o.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2ReverbVolume()));
            oooO0o.OoooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2ReverbTime()));
            oooO0o.OooooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2VoiceMode()));
            oooO0o.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorEnable()));
            oooO0o.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorRatio()));
            oooO0o.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorThreshold()));
            oooO0o.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorAttack()));
            oooO0o.OooO0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorRelease()));
            oooO0o.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(defaultConfig.getAef2CompressorPregain()));
        }
        List<EQItem> list = defaultConfig.getEqList();
        oooOO0.getClass();
        Intrinsics.checkNotNullParameter(list, "list");
        Integer OooO00o7 = oooO.OooOO0O().OooO00o();
        if (OooO00o7 != null && OooO00o7.intValue() == 1) {
            if (list.size() < 2) {
                Intrinsics.checkNotNullParameter("PuremicConfig", Progress.TAG);
                Intrinsics.checkNotNullParameter("  EQ初始化失败, json配置错误", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", "PuremicConfig ->   EQ初始化失败, json配置错误");
                }
            } else {
                EQItem eQItem = list.get(0);
                Integer frequency1 = eQItem.getFrequency1();
                if (frequency1 != null) {
                    oooOO0.OooOO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency1.intValue()));
                }
                Integer frequency2 = eQItem.getFrequency2();
                if (frequency2 != null) {
                    oooOO0.OooOOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency2.intValue()));
                }
                Integer frequency3 = eQItem.getFrequency3();
                if (frequency3 != null) {
                    oooOO0.OooOOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency3.intValue()));
                }
                Integer frequency4 = eQItem.getFrequency4();
                if (frequency4 != null) {
                    oooOO0.OooOOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency4.intValue()));
                }
                Integer frequency5 = eQItem.getFrequency5();
                if (frequency5 != null) {
                    oooOO0.OooOOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency5.intValue()));
                }
                Integer frequency6 = eQItem.getFrequency6();
                if (frequency6 != null) {
                    oooOO0.OooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency6.intValue()));
                }
                Integer frequency7 = eQItem.getFrequency7();
                if (frequency7 != null) {
                    oooOO0.OooOo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency7.intValue()));
                }
                Integer frequency8 = eQItem.getFrequency8();
                if (frequency8 != null) {
                    oooOO0.OooOoO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency8.intValue()));
                }
                Integer frequency9 = eQItem.getFrequency9();
                if (frequency9 != null) {
                    oooOO0.OooOoOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency9.intValue()));
                }
                Integer eq1 = eQItem.getEq1();
                if (eq1 != null) {
                    oooOO0.OooOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq1.intValue()));
                }
                Integer eq2 = eQItem.getEq2();
                if (eq2 != null) {
                    oooOO0.OooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq2.intValue()));
                }
                Integer eq3 = eQItem.getEq3();
                if (eq3 != null) {
                    oooOO0.OooOOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq3.intValue()));
                }
                Integer eq4 = eQItem.getEq4();
                if (eq4 != null) {
                    oooOO0.OooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq4.intValue()));
                }
                Integer eq5 = eQItem.getEq5();
                if (eq5 != null) {
                    oooOO0.OooOo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq5.intValue()));
                }
                Integer eq6 = eQItem.getEq6();
                if (eq6 != null) {
                    oooOO0.OooOo0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq6.intValue()));
                }
                Integer eq7 = eQItem.getEq7();
                if (eq7 != null) {
                    oooOO0.OooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq7.intValue()));
                }
                Integer eq8 = eQItem.getEq8();
                if (eq8 != null) {
                    oooOO0.OooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq8.intValue()));
                }
                Integer eq9 = eQItem.getEq9();
                if (eq9 != null) {
                    oooOO0.OooOoo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq9.intValue()));
                }
                Integer type1 = eQItem.getType1();
                if (type1 != null) {
                    oooOO0.o00oO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type1.intValue()));
                }
                Integer type2 = eQItem.getType2();
                if (type2 != null) {
                    oooOO0.o00oO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type2.intValue()));
                }
                Integer type3 = eQItem.getType3();
                if (type3 != null) {
                    oooOO0.o0ooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type3.intValue()));
                }
                Integer type4 = eQItem.getType4();
                if (type4 != null) {
                    oooOO0.o0ooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type4.intValue()));
                }
                Integer type5 = eQItem.getType5();
                if (type5 != null) {
                    oooOO0.o0ooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type5.intValue()));
                }
                Integer type6 = eQItem.getType6();
                if (type6 != null) {
                    oooOO0.o0OOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type6.intValue()));
                }
                Integer type7 = eQItem.getType7();
                if (type7 != null) {
                    oooOO0.o0Oo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type7.intValue()));
                }
                Integer type8 = eQItem.getType8();
                if (type8 != null) {
                    oooOO0.o0OO00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type8.intValue()));
                }
                Integer type9 = eQItem.getType9();
                if (type9 != null) {
                    oooOO0.oo0o0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(type9.intValue()));
                }
                Integer q1 = eQItem.getQ1();
                if (q1 != null) {
                    oooOO0.o0O0O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q1.intValue()));
                }
                Integer q2 = eQItem.getQ2();
                if (q2 != null) {
                    oooOO0.o000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q2.intValue()));
                }
                Integer q3 = eQItem.getQ3();
                if (q3 != null) {
                    oooOO0.o000000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q3.intValue()));
                }
                Integer q4 = eQItem.getQ4();
                if (q4 != null) {
                    oooOO0.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q4.intValue()));
                }
                Integer q5 = eQItem.getQ5();
                if (q5 != null) {
                    oooOO0.o000000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q5.intValue()));
                }
                Integer q6 = eQItem.getQ6();
                if (q6 != null) {
                    oooOO0.o00000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q6.intValue()));
                }
                Integer q7 = eQItem.getQ7();
                if (q7 != null) {
                    oooOO0.o00000O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q7.intValue()));
                }
                Integer q8 = eQItem.getQ8();
                if (q8 != null) {
                    oooOO0.o00000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q8.intValue()));
                }
                Integer q9 = eQItem.getQ9();
                if (q9 != null) {
                    oooOO0.o00000OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(q9.intValue()));
                }
                EQItem eQItem2 = list.get(1);
                Integer frequency12 = eQItem2.getFrequency1();
                if (frequency12 != null) {
                    oooOO0.Oooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency12.intValue()));
                }
                Integer frequency22 = eQItem2.getFrequency2();
                if (frequency22 != null) {
                    oooOO0.Oooo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency22.intValue()));
                }
                Integer frequency32 = eQItem2.getFrequency3();
                if (frequency32 != null) {
                    oooOO0.OoooO00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency32.intValue()));
                }
                Integer frequency42 = eQItem2.getFrequency4();
                if (frequency42 != null) {
                    oooOO0.OoooO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency42.intValue()));
                }
                Integer frequency52 = eQItem2.getFrequency5();
                if (frequency52 != null) {
                    oooOO0.OoooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency52.intValue()));
                }
                Integer frequency62 = eQItem2.getFrequency6();
                if (frequency62 != null) {
                    oooOO0.OoooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency62.intValue()));
                }
                Integer frequency72 = eQItem2.getFrequency7();
                if (frequency72 != null) {
                    oooOO0.OoooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency72.intValue()));
                }
                Integer frequency82 = eQItem2.getFrequency8();
                if (frequency82 != null) {
                    oooOO0.OoooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency82.intValue()));
                }
                Integer frequency92 = eQItem2.getFrequency9();
                if (frequency92 != null) {
                    oooOO0.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(frequency92.intValue()));
                }
                Integer eq12 = eQItem2.getEq1();
                if (eq12 != null) {
                    oooOO0.Oooo0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq12.intValue()));
                }
                Integer eq22 = eQItem2.getEq2();
                if (eq22 != null) {
                    oooOO0.Oooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq22.intValue()));
                }
                Integer eq32 = eQItem2.getEq3();
                if (eq32 != null) {
                    oooOO0.OoooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq32.intValue()));
                }
                Integer eq42 = eQItem2.getEq4();
                if (eq42 != null) {
                    oooOO0.OoooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq42.intValue()));
                }
                Integer eq52 = eQItem2.getEq5();
                if (eq52 != null) {
                    oooOO0.o000oOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq52.intValue()));
                }
                Integer eq62 = eQItem2.getEq6();
                if (eq62 != null) {
                    oooOO0.OoooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq62.intValue()));
                }
                Integer eq72 = eQItem2.getEq7();
                if (eq72 != null) {
                    oooOO0.OoooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq72.intValue()));
                }
                Integer eq82 = eQItem2.getEq8();
                if (eq82 != null) {
                    oooOO0.Ooooo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq82.intValue()));
                }
                Integer eq92 = eQItem2.getEq9();
                if (eq92 != null) {
                    oooOO0.OooooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(eq92.intValue()));
                }
            }
        }
        oooO0o.OooO00o(defaultConfig.getEqList());
    }

    public final String OooO00o(long j) {
        String str;
        if (j == 200000) {
            str = Intrinsics.stringPlus(OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO0O0("config"), File.separator);
        } else {
            str = j == 200001 ? "vendor/tvconfig/loostone/" : "";
        }
        String configPath = TextUtils.isEmpty(str) ? "" : ((ConfigMgr) OooO0O0.getValue()).getConfigPath(str);
        return (TextUtils.isEmpty(configPath) || !new File(configPath).exists()) ? OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO0O0("config.json") : configPath;
    }
}
