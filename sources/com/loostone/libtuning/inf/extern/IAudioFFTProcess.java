package com.loostone.libtuning.inf.extern;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u000f\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0003\u0010\u0004J\u000f\u0010\u0005\u001a\u00020\u0002H&¢\u0006\u0004\b\u0005\u0010\u0004J\u000f\u0010\u0006\u001a\u00020\u0002H&¢\u0006\u0004\b\u0006\u0010\u0004J\u000f\u0010\u0007\u001a\u00020\u0002H&¢\u0006\u0004\b\u0007\u0010\u0004J\u000f\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\b\u0010\u0004J\u0017\u0010\u000b\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tH&¢\u0006\u0004\b\u000b\u0010\fJ\u000f\u0010\r\u001a\u00020\u0002H&¢\u0006\u0004\b\r\u0010\u0004¨\u0006\u000e"}, d2 = {"Lcom/loostone/libtuning/inf/extern/IAudioFFTProcess;", "", "", "start", "()V", "resume", "pause", "stop", "release", "Lcom/loostone/libtuning/inf/extern/IFFTReadListener;", "iFFTReadListener", "registerFFTReadListener", "(Lcom/loostone/libtuning/inf/extern/IFFTReadListener;)V", "unRegisterFFTReadListener", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public interface IAudioFFTProcess {
    void pause();

    void registerFFTReadListener(@NotNull IFFTReadListener iFFTReadListener);

    void release();

    void resume();

    void start();

    void stop();

    void unRegisterFFTReadListener();
}
