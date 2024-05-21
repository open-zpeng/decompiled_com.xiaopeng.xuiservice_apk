package com.loostone.libtuning.inf.extern;

import android.hardware.usb.UsbDevice;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H&¢\u0006\u0004\b\u0005\u0010\u0006J\u000f\u0010\u0007\u001a\u00020\u0004H&¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\t\u001a\u00020\u0004H&¢\u0006\u0004\b\t\u0010\bJ\u000f\u0010\n\u001a\u00020\u0004H&¢\u0006\u0004\b\n\u0010\bJ\u000f\u0010\u000b\u001a\u00020\u0004H&¢\u0006\u0004\b\u000b\u0010\bJ\u0011\u0010\r\u001a\u0004\u0018\u00010\fH&¢\u0006\u0004\b\r\u0010\u000eJ\u0017\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fH&¢\u0006\u0004\b\u0011\u0010\u0012J\u0017\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fH&¢\u0006\u0004\b\u0013\u0010\u0012J\u0017\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0014H&¢\u0006\u0004\b\u0015\u0010\u0016J\u0017\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0014H&¢\u0006\u0004\b\u0017\u0010\u0016¨\u0006\u0018"}, d2 = {"Lcom/loostone/libtuning/inf/extern/IBaseKeyService;", "", "Landroid/hardware/usb/UsbDevice;", "usbDevice", "", "setDevice", "(Landroid/hardware/usb/UsbDevice;)V", "start", "()V", "resume", "pause", "stop", "Lcom/loostone/libtuning/inf/extern/IMicControl;", "getMicControl", "()Lcom/loostone/libtuning/inf/extern/IMicControl;", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "listener", "registerKeyListener", "(Lcom/loostone/libtuning/inf/extern/IKeyListener;)V", "unRegisterKeyListener", "Lcom/loostone/libtuning/inf/extern/IMicInfoListener;", "registerMicInfoListener", "(Lcom/loostone/libtuning/inf/extern/IMicInfoListener;)V", "unRegisterMicInfoListener", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public interface IBaseKeyService {
    @Nullable
    IMicControl getMicControl();

    void pause();

    void registerKeyListener(@NotNull IKeyListener iKeyListener);

    void registerMicInfoListener(@NotNull IMicInfoListener iMicInfoListener);

    void resume();

    void setDevice(@Nullable UsbDevice usbDevice);

    void start();

    void stop();

    void unRegisterKeyListener(@NotNull IKeyListener iKeyListener);

    void unRegisterMicInfoListener(@NotNull IMicInfoListener iMicInfoListener);
}
