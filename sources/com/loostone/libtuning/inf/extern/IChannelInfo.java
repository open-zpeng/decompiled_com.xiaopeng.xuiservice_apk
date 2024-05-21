package com.loostone.libtuning.inf.extern;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u000f\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0003\u0010\u0004J\u000f\u0010\u0005\u001a\u00020\u0002H&¢\u0006\u0004\b\u0005\u0010\u0004J\u000f\u0010\u0007\u001a\u00020\u0006H&¢\u0006\u0004\b\u0007\u0010\bJ\u0011\u0010\n\u001a\u0004\u0018\u00010\tH&¢\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\r\u001a\u00020\fH&¢\u0006\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lcom/loostone/libtuning/inf/extern/IChannelInfo;", "", "", "getPowerStatus", "()I", "getVerifyStatus", "Lcom/loostone/libtuning/inf/extern/IBaseKeyService;", "getMicService", "()Lcom/loostone/libtuning/inf/extern/IBaseKeyService;", "Lcom/loostone/libtuning/inf/extern/IMicControl;", "getMicControlService", "()Lcom/loostone/libtuning/inf/extern/IMicControl;", "", "hasDongle", "()Z", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public interface IChannelInfo {
    @Nullable
    IMicControl getMicControlService();

    @NotNull
    IBaseKeyService getMicService();

    int getPowerStatus();

    int getVerifyStatus();

    boolean hasDongle();
}
