package com.loostone.libtuning.channel.byd.data;

import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO0O0;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\r\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R#\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058F@\u0006X\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\nR\u0016\u0010\f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\u0004R\u0016\u0010\r\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u0004R#\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058F@\u0006X\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\b\u001a\u0004\b\u000f\u0010\n¨\u0006\u0013"}, d2 = {"Lcom/loostone/libtuning/channel/byd/data/BYDCache;", "LOooO00o/OooO0O0/OooO0O0/OooO0o0/OooO0o0/OooO0O0;", "", "TAG", "Ljava/lang/String;", "LOooO00o/OooO0O0/OooO0O0/OooO0o0/OooO0o0/OooO00o;", "", "reverbVolume$delegate", "Lkotlin/Lazy;", "getReverbVolume", "()LOooO00o/OooO0O0/OooO0O0/OooO0o0/OooO0o0/OooO00o;", "reverbVolume", "REVERB_VOLUME", "KARAOKE_EFFECT", "karaokeEffect$delegate", "getKaraokeEffect", "karaokeEffect", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class BYDCache extends OooO0O0 {
    @NotNull
    private static final String KARAOKE_EFFECT = "karaoke_effect";
    @NotNull
    private static final String REVERB_VOLUME = "reverb_volume";
    @NotNull
    private static final String TAG = "BYDCache";
    @NotNull
    public static final BYDCache INSTANCE = new BYDCache();
    @NotNull
    private static final Lazy reverbVolume$delegate = LazyKt.lazy(new Function0<OooO00o<Integer>>() { // from class: com.loostone.libtuning.channel.byd.data.BYDCache$reverbVolume$2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final OooO00o<Integer> invoke() {
            return OooO0O0.intPref$default(BYDCache.INSTANCE, "reverb_volume", 0, 2, null);
        }
    });
    @NotNull
    private static final Lazy karaokeEffect$delegate = LazyKt.lazy(new Function0<OooO00o<Integer>>() { // from class: com.loostone.libtuning.channel.byd.data.BYDCache$karaokeEffect$2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final OooO00o<Integer> invoke() {
            return OooO0O0.intPref$default(BYDCache.INSTANCE, "karaoke_effect", 0, 2, null);
        }
    });

    private BYDCache() {
        super(TAG);
    }

    @NotNull
    public final OooO00o<Integer> getKaraokeEffect() {
        return (OooO00o) karaokeEffect$delegate.getValue();
    }

    @NotNull
    public final OooO00o<Integer> getReverbVolume() {
        return (OooO00o) reverbVolume$delegate.getValue();
    }
}
