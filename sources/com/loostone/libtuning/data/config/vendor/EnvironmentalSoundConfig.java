package com.loostone.libtuning.data.config.vendor;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000b\u0010\fR2\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u0002j\b\u0012\u0004\u0012\u00020\u0003`\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\r"}, d2 = {"Lcom/loostone/libtuning/data/config/vendor/EnvironmentalSoundConfig;", "", "Ljava/util/ArrayList;", "Lcom/loostone/libtuning/data/config/vendor/EnvironmentalSoundItem;", "Lkotlin/collections/ArrayList;", "environmentalSoundList", "Ljava/util/ArrayList;", "getEnvironmentalSoundList", "()Ljava/util/ArrayList;", "setEnvironmentalSoundList", "(Ljava/util/ArrayList;)V", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class EnvironmentalSoundConfig {
    @NotNull
    private ArrayList<EnvironmentalSoundItem> environmentalSoundList = new ArrayList<>();

    @NotNull
    public final ArrayList<EnvironmentalSoundItem> getEnvironmentalSoundList() {
        return this.environmentalSoundList;
    }

    public final void setEnvironmentalSoundList(@NotNull ArrayList<EnvironmentalSoundItem> arrayList) {
        Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
        this.environmentalSoundList = arrayList;
    }
}
