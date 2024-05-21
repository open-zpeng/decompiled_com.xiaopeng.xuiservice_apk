package com.loostone.libtuning.data.config.load.effect;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0012\u0010\u0013R2\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u0002j\b\u0012\u0004\u0012\u00020\u0003`\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\"\u0010\f\u001a\u00020\u000b8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006\u0014"}, d2 = {"Lcom/loostone/libtuning/data/config/load/effect/EffectModeConfig;", "", "Ljava/util/ArrayList;", "Lcom/loostone/libtuning/data/config/load/effect/EffectModeItem;", "Lkotlin/collections/ArrayList;", "effectModeList", "Ljava/util/ArrayList;", "getEffectModeList", "()Ljava/util/ArrayList;", "setEffectModeList", "(Ljava/util/ArrayList;)V", "", "defaultId", "I", "getDefaultId", "()I", "setDefaultId", "(I)V", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class EffectModeConfig {
    private int defaultId;
    @NotNull
    private ArrayList<EffectModeItem> effectModeList = new ArrayList<>();

    public final int getDefaultId() {
        return this.defaultId;
    }

    @NotNull
    public final ArrayList<EffectModeItem> getEffectModeList() {
        return this.effectModeList;
    }

    public final void setDefaultId(int i) {
        this.defaultId = i;
    }

    public final void setEffectModeList(@NotNull ArrayList<EffectModeItem> arrayList) {
        Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
        this.effectModeList = arrayList;
    }
}
