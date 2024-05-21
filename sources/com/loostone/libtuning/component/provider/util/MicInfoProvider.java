package com.loostone.libtuning.component.provider.util;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import com.loostone.libtuning.PuremicTuning;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0016\u0010\u0017J\u0015\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u001d\u0010\b\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002¢\u0006\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\u000bR\u001d\u0010\u0012\u001a\u00020\r8B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0014\u001a\u00020\u00138\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0014\u0010\u0015¨\u0006\u0018"}, d2 = {"Lcom/loostone/libtuning/component/provider/util/MicInfoProvider;", "", "", "keyIndex", "Landroid/database/Cursor;", "getValue", "(I)Landroid/database/Cursor;", "value", "setValue", "(II)I", "GET_MIC_STATUS", "I", "GET_DONGLE_STATUS", "Lcom/loostone/libtuning/channel/BaseChannelMgr;", "fullSceneSpeakMgr$delegate", "Lkotlin/Lazy;", "getFullSceneSpeakMgr", "()Lcom/loostone/libtuning/channel/BaseChannelMgr;", "fullSceneSpeakMgr", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class MicInfoProvider {
    private static final int GET_DONGLE_STATUS = 101;
    private static final int GET_MIC_STATUS = 100;
    @NotNull
    private static final String TAG = "MicInfoProvider";
    @NotNull
    public static final MicInfoProvider INSTANCE = new MicInfoProvider();
    @NotNull
    private static final Lazy fullSceneSpeakMgr$delegate = LazyKt.lazy(new Function0<BaseChannelMgr>() { // from class: com.loostone.libtuning.component.provider.util.MicInfoProvider$fullSceneSpeakMgr$2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final BaseChannelMgr invoke() {
            return PuremicTuning.Companion.getInstance().getChannelMgr();
        }
    });

    private MicInfoProvider() {
    }

    private final BaseChannelMgr getFullSceneSpeakMgr() {
        return (BaseChannelMgr) fullSceneSpeakMgr$delegate.getValue();
    }

    @NotNull
    public final Cursor getValue(int i) {
        int i2;
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"key", "value"});
        MatrixCursor.RowBuilder newRow = matrixCursor.newRow();
        if (i == 100) {
            i2 = getFullSceneSpeakMgr().getVerifyStatus();
            if (i2 == 2) {
                i2 = getFullSceneSpeakMgr().getPowerStatus();
            }
        } else {
            i2 = 0;
        }
        newRow.add("key", Integer.valueOf(i));
        newRow.add("value", String.valueOf(i2));
        String msg = "get  keyIndex: " + i + " value: " + i2;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        return matrixCursor;
    }

    public final int setValue(int i, int i2) {
        String msg = "set  keyIndex: " + i + " value: " + i2;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
            return 1;
        }
        return 1;
    }
}
