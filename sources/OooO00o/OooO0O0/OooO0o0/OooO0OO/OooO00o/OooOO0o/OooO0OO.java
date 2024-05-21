package OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.data.config.vendor.EnvironmentalSoundItem;
import com.lzy.okgo.model.Progress;
import java.io.File;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0OO {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0OO f553OooO00o = new OooO0OO();
    @Nullable
    public static ArrayList<EnvironmentalSoundItem> OooO0O0;

    @NotNull
    public final String OooO00o(long j, int i) {
        int i2 = (j > 200001L ? 1 : (j == 200001L ? 0 : -1));
        String str = "environmental_sound.json";
        String OooO0O02 = i2 == 0 ? "vendor/tvconfig/loostone/environmental_sound.json" : OooO00o.f237OooO00o.OooO0O0("environmental_sound.json");
        if (new File(OooO0O02).exists()) {
            Intrinsics.checkNotNullParameter("EnvironmentalSound", Progress.TAG);
            Intrinsics.checkNotNullParameter("安装路径下配置环境音效", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "EnvironmentalSound -> 安装路径下配置环境音效");
            }
            String OooO0Oo = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0Oo(OooO0O02);
            Intrinsics.checkNotNullExpressionValue(OooO0Oo, "{\n            LogTuning.i(TAG, \"安装路径下配置环境音效\")\n\n            OldCommonUtil.getStringFromFile(path)\n        }");
            return OooO0Oo;
        }
        String msg = "channel: " + j + ", model: " + i;
        Intrinsics.checkNotNullParameter("EnvironmentalSound", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "EnvironmentalSound -> " + msg);
        }
        if (j == 400000) {
            str = "byd/scene_effect/environmental_sound.json";
        } else if (j == 200000) {
            str = "skyworth_a/scene_effect/environmental_sound.json";
        } else if (i2 == 0) {
            str = "skyworth_b/scene_effect/environmental_sound.json";
        } else if (j == 200002) {
            str = "skyworth_c/scene_effect/environmental_sound.json";
        }
        String OooO00o2 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0O0.OooO00o(str);
        Intrinsics.checkNotNullExpressionValue(OooO00o2, "{\n            LogTuning.i(TAG, \"channel: $channel, model: $model\")\n\n            path = when (channel) {\n                // 比亚迪渠道\n                Channel.BYD.BYD_A -> {\n                    \"byd/scene_effect/environmental_sound.json\"\n                }\n\n                // 创维KO方案-国内\n                Channel.SkyWorth.SKYWORTH_A -> {\n                    \"skyworth_a/scene_effect/environmental_sound.json\"\n                }\n\n                // 创维SO方案-海外\n                Channel.SkyWorth.SKYWORTH_B -> {\n                    \"skyworth_b/scene_effect/environmental_sound.json\"\n                }\n\n                // 创维酷沃智行 - SO方案\n                Channel.SkyWorth.SKYWORTH_C -> {\n                    \"skyworth_c/scene_effect/environmental_sound.json\"\n                }\n\n                else -> {\n                    \"environmental_sound.json\"\n                }\n            }\n\n            ResourceUtils.readAssets2String(path)\n        }");
        return OooO00o2;
    }
}
