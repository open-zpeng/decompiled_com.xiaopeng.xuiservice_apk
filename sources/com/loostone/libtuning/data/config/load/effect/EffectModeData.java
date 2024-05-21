package com.loostone.libtuning.data.config.load.effect;

import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\b\u0012\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0013\u0010\u0014R.\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\"\u0010\n\u001a\u00020\u00038\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR.\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0010\u0010\u0005\u001a\u0004\b\u0011\u0010\u0007\"\u0004\b\u0012\u0010\t¨\u0006\u0015"}, d2 = {"Lcom/loostone/libtuning/data/config/load/effect/EffectModeData;", "", "", "", "effectScene", "Ljava/util/Map;", "getEffectScene", "()Ljava/util/Map;", "setEffectScene", "(Ljava/util/Map;)V", "effectId", "I", "getEffectId", "()I", "setEffectId", "(I)V", "effectSurround", "getEffectSurround", "setEffectSurround", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class EffectModeData {
    private int effectId;
    @NotNull
    private Map<Integer, Integer> effectScene = new HashMap();
    @NotNull
    private Map<Integer, Integer> effectSurround = new HashMap();

    public final int getEffectId() {
        return this.effectId;
    }

    @NotNull
    public final Map<Integer, Integer> getEffectScene() {
        return this.effectScene;
    }

    @NotNull
    public final Map<Integer, Integer> getEffectSurround() {
        return this.effectSurround;
    }

    public final void setEffectId(int i) {
        this.effectId = i;
    }

    public final void setEffectScene(@NotNull Map<Integer, Integer> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.effectScene = map;
    }

    public final void setEffectSurround(@NotNull Map<Integer, Integer> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.effectSurround = map;
    }
}
