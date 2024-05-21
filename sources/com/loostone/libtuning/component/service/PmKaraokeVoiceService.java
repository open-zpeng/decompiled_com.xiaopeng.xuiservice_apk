package com.loostone.libtuning.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.PuremicTuning;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.loostone.libtuning.channel.skyworth.old.ai.AiControlMgr;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000-\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\b\u0006*\u0001\u0011\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0015\u0010\u0004J\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004J\u0019\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016¢\u0006\u0004\b\b\u0010\tJ\u000f\u0010\n\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\n\u0010\u0004R\u001d\u0010\u0010\u001a\u00020\u000b8B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0014\u001a\u00020\u00118\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013¨\u0006\u0016"}, d2 = {"Lcom/loostone/libtuning/component/service/PmKaraokeVoiceService;", "Landroid/app/Service;", "", "onCreate", "()V", "Landroid/content/Intent;", "intent", "Landroid/os/IBinder;", "onBind", "(Landroid/content/Intent;)Landroid/os/IBinder;", "onDestroy", "Lcom/loostone/libtuning/channel/BaseChannelMgr;", "OooO00o", "Lkotlin/Lazy;", "getFullSceneSpeakMgr", "()Lcom/loostone/libtuning/channel/BaseChannelMgr;", "fullSceneSpeakMgr", "com/loostone/libtuning/component/service/PmKaraokeVoiceService$OooO00o", "OooO0O0", "Lcom/loostone/libtuning/component/service/PmKaraokeVoiceService$OooO00o;", "binder", "<init>", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class PmKaraokeVoiceService extends Service {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final Lazy f607OooO00o = LazyKt.lazy(OooO0O0.f609OooO00o);
    @NotNull
    public final OooO00o OooO0O0 = new OooO00o();

    /* loaded from: classes4.dex */
    public static final class OooO00o extends OooO00o.OooO0O0.OooO00o.OooO00o.OooO0O0 {
        public OooO00o() {
        }
    }

    /* loaded from: classes4.dex */
    public static final class OooO0O0 extends Lambda implements Function0<BaseChannelMgr> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f609OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public BaseChannelMgr invoke() {
            return PuremicTuning.Companion.getInstance().getChannelMgr();
        }
    }

    @Override // android.app.Service
    @NotNull
    public IBinder onBind(@Nullable Intent intent) {
        Intrinsics.checkNotNullParameter("PmKaraokeVoiceService", Progress.TAG);
        Intrinsics.checkNotNullParameter("  onBind", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "PmKaraokeVoiceService ->   onBind");
        }
        return this.OooO0O0;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        AiControlMgr.getInstance().create(getApplicationContext());
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        AiControlMgr.getInstance().destroy();
    }
}
