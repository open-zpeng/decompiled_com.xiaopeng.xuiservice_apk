package OooO00o.OooO0O0.OooO0o0.OooO00o.OooO0O0;

import android.accessibilityservice.AccessibilityService;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.inf.extern.IBaseKeyService;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.loostone.libtuning.inf.extern.IMicControl;
import com.loostone.libtuning.inf.extern.IMicInfoListener;
import com.lzy.okgo.model.Progress;
import java.util.Iterator;
import java.util.LinkedList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.opencv.imgproc.Imgproc;
/* loaded from: classes.dex */
public final class OooO0O0 extends AccessibilityService implements IBaseKeyService {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final C0011OooO0O0 f273OooO00o = new C0011OooO0O0();
    @NotNull
    public static final Lazy<LinkedList<IKeyListener>> OooO0O0 = LazyKt.lazy(OooO00o.f274OooO00o);
    @Nullable
    public UsbDevice OooO0OO;
    public boolean OooO0Oo = true;
    @NotNull
    public final Lazy OooO0o0 = LazyKt.lazy(new OooO0OO());

    /* loaded from: classes.dex */
    public static final class OooO00o extends Lambda implements Function0<LinkedList<IKeyListener>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO00o f274OooO00o = new OooO00o();

        public OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public LinkedList<IKeyListener> invoke() {
            return new LinkedList<>();
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO00o.OooO0O0.OooO0O0$OooO0O0  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0011OooO0O0 {
        static {
            Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(C0011OooO0O0.class), "callBackList", "getCallBackList()Ljava/util/LinkedList;"));
        }

        public static final LinkedList OooO00o(C0011OooO0O0 c0011OooO0O0) {
            c0011OooO0O0.getClass();
            return OooO0O0.OooO0O0.getValue();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0> {
        public OooO0OO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0 invoke() {
            return new OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0(OooO0O0.this.OooO0OO);
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    @NotNull
    public IMicControl getMicControl() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0) this.OooO0o0.getValue();
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(@Nullable AccessibilityEvent accessibilityEvent) {
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Intrinsics.checkNotNullParameter("MicKeyService", Progress.TAG);
        Intrinsics.checkNotNullParameter("  onCreate()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MicKeyService ->   onCreate()");
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
        Intrinsics.checkNotNullParameter("MicKeyService", Progress.TAG);
        Intrinsics.checkNotNullParameter("  onInterrupt()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MicKeyService ->   onInterrupt()");
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public boolean onKeyEvent(@Nullable KeyEvent keyEvent) {
        if (keyEvent == null) {
            return super.onKeyEvent(keyEvent);
        }
        if (!this.OooO0Oo) {
            return super.onKeyEvent(keyEvent);
        }
        try {
            Iterator it = C0011OooO0O0.OooO00o(f273OooO00o).iterator();
            while (it.hasNext()) {
                IKeyListener iKeyListener = (IKeyListener) it.next();
                switch (keyEvent.getKeyCode()) {
                    case 131:
                        if (keyEvent.getAction() != 0) {
                            break;
                        } else {
                            iKeyListener.onPower(0, true);
                            break;
                        }
                    case Imgproc.COLOR_BGR2YUV_YV12 /* 132 */:
                        if (keyEvent.getAction() != 0) {
                            break;
                        } else {
                            iKeyListener.onPower(0, false);
                            break;
                        }
                    case Imgproc.COLOR_RGBA2YUV_YV12 /* 133 */:
                        if (keyEvent.getAction() == 0) {
                            iKeyListener.onVoice(0, true);
                            break;
                        } else if (keyEvent.getAction() != 1) {
                            break;
                        } else {
                            iKeyListener.onVoice(0, false);
                            break;
                        }
                    case Imgproc.COLOR_BGRA2YUV_YV12 /* 134 */:
                        if (keyEvent.getAction() == 0) {
                            iKeyListener.onVolumeUp(0, true);
                            break;
                        } else if (keyEvent.getAction() != 1) {
                            break;
                        } else {
                            iKeyListener.onVolumeUp(0, false);
                            break;
                        }
                    case 135:
                        if (keyEvent.getAction() == 0) {
                            iKeyListener.onVolumeUp(0, true);
                            break;
                        } else if (keyEvent.getAction() != 1) {
                            break;
                        } else {
                            iKeyListener.onVolumeUp(0, false);
                            break;
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onKeyEvent(keyEvent);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void pause() {
        this.OooO0Oo = false;
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void registerKeyListener(@NotNull IKeyListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        C0011OooO0O0.OooO00o(f273OooO00o).add(listener);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void registerMicInfoListener(@NotNull IMicInfoListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void resume() {
        this.OooO0Oo = true;
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void setDevice(@Nullable UsbDevice usbDevice) {
        this.OooO0OO = usbDevice;
        ((OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0) this.OooO0o0.getValue()).OooO0O0 = usbDevice;
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void start() {
        Intrinsics.checkNotNullParameter("MicKeyService", Progress.TAG);
        Intrinsics.checkNotNullParameter("start()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MicKeyService -> start()");
        }
        OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO.f242OooO00o.OooO00o().OooO00o();
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void stop() {
        Intrinsics.checkNotNullParameter("MicKeyService", Progress.TAG);
        Intrinsics.checkNotNullParameter("stop()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "MicKeyService -> stop()");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void unRegisterKeyListener(@NotNull IKeyListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        C0011OooO0O0.OooO00o(f273OooO00o).remove(listener);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void unRegisterMicInfoListener(@NotNull IMicInfoListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
    }
}
