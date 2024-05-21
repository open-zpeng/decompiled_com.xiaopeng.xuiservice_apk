package com.loostone.libtuning.component.service;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.channel.skyworth.old.ai.AiControlFactory;
import com.loostone.libtuning.channel.skyworth.old.ai.IAiControl;
import com.lzy.okgo.model.Progress;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0014\u0010\u0004J\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004J\u0019\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016¢\u0006\u0004\b\b\u0010\tJ\u0019\u0010\u000b\u001a\u00020\n2\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016¢\u0006\u0004\b\u000b\u0010\fR\u0018\u0010\u0010\u001a\u0004\u0018\u00010\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0013\u001a\u00020\u00078\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012¨\u0006\u0015"}, d2 = {"Lcom/loostone/libtuning/component/service/PmKaraokeAppService;", "Landroid/app/Service;", "", "onCreate", "()V", "Landroid/content/Intent;", "intent", "Landroid/os/IBinder;", "onBind", "(Landroid/content/Intent;)Landroid/os/IBinder;", "", "onUnbind", "(Landroid/content/Intent;)Z", "Lcom/loostone/libtuning/channel/skyworth/old/ai/IAiControl;", "OooO00o", "Lcom/loostone/libtuning/channel/skyworth/old/ai/IAiControl;", "mAiControl", "OooO0O0", "Landroid/os/IBinder;", "mBinder", "<init>", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class PmKaraokeAppService extends Service {
    @Nullable

    /* renamed from: OooO00o  reason: collision with root package name */
    public IAiControl f605OooO00o;
    @NotNull
    public final IBinder OooO0O0 = new OooO00o();

    /* loaded from: classes4.dex */
    public static final class OooO00o extends OooO00o.OooO0O0.OooO00o.OooO00o.OooO00o {
        public OooO00o() {
        }
    }

    @Override // android.app.Service
    @NotNull
    public IBinder onBind(@Nullable Intent intent) {
        Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
        Intrinsics.checkNotNullParameter("onBind", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "PmKaraokeAppService -> onBind");
        }
        return this.OooO0O0;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
        Intrinsics.checkNotNullParameter("onCreate", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "PmKaraokeAppService -> onCreate");
        }
        this.f605OooO00o = AiControlFactory.getAiControl();
    }

    @Override // android.app.Service
    public boolean onUnbind(@Nullable Intent intent) {
        Intrinsics.checkNotNullParameter("PmKaraokeAppService", Progress.TAG);
        Intrinsics.checkNotNullParameter("onUnbind", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "PmKaraokeAppService -> onUnbind");
        }
        IAiControl iAiControl = this.f605OooO00o;
        if (iAiControl != null) {
            iAiControl.exitKaraokeApp();
        }
        return super.onUnbind(intent);
    }
}
