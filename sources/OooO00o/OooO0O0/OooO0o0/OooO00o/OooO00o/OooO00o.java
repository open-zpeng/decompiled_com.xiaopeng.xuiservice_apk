package OooO00o.OooO0O0.OooO0o0.OooO00o.OooO00o;

import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.PuremicTuning;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o extends BroadcastReceiver {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final Context f260OooO00o;
    @NotNull
    public final Lazy OooO0O0;
    @NotNull
    public final Lazy OooO0OO;
    @NotNull
    public final Lazy OooO0Oo;
    @NotNull
    public final Handler OooO0o0;

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO00o.OooO00o.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0009OooO00o extends Lambda implements Function0<AudioManager> {
        public C0009OooO00o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public AudioManager invoke() {
            Object systemService = OooO00o.this.f260OooO00o.getSystemService("audio");
            if (systemService != null) {
                return (AudioManager) systemService;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<ArrayList<OooO0o>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f262OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public ArrayList<OooO0o> invoke() {
            return new ArrayList<>();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends Lambda implements Function0<IVolumeAdjustment> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0OO f263OooO00o = new OooO0OO();

        public OooO0OO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public IVolumeAdjustment invoke() {
            return PuremicTuning.Companion.getInstance().getVolumeAdjustment();
        }
    }

    public OooO00o(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.f260OooO00o = context;
        this.OooO0O0 = LazyKt.lazy(OooO0O0.f262OooO00o);
        this.OooO0OO = LazyKt.lazy(new C0009OooO00o());
        this.OooO0Oo = LazyKt.lazy(OooO0OO.f263OooO00o);
        this.OooO0o0 = new Handler(Looper.getMainLooper());
    }

    public static final void OooO00o(OooO00o this$0) {
        String str;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.getClass();
        Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
        Intrinsics.checkNotNullParameter("  U盘接入", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "InfoReceiver ->   U盘接入");
        }
        ArrayList arrayList = new ArrayList();
        StorageManager storageManager = (StorageManager) OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0.getSystemService("storage");
        if (storageManager != null) {
            int i = 0;
            if (Build.VERSION.SDK_INT >= 24) {
                List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
                try {
                    Method method = StorageVolume.class.getMethod("getPath", new Class[0]);
                    for (StorageVolume storageVolume : storageVolumes) {
                        arrayList.add(new OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0OO((String) method.invoke(storageVolume, new Object[0]), storageVolume.getState(), storageVolume.isRemovable()));
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Class<?> cls = Class.forName("android.os.storage.StorageVolume");
                    Method method2 = cls.getMethod("getPath", new Class[0]);
                    Method method3 = cls.getMethod("isRemovable", new Class[0]);
                    Method method4 = StorageManager.class.getMethod("getVolumeState", String.class);
                    Object invoke = StorageManager.class.getMethod("getVolumeList", new Class[0]).invoke(storageManager, new Object[0]);
                    int length = Array.getLength(invoke);
                    int i2 = 0;
                    while (i2 < length) {
                        Object obj = Array.get(invoke, i2);
                        String str2 = (String) method2.invoke(obj, new Object[i]);
                        Object obj2 = invoke;
                        arrayList.add(new OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0OO(str2, (String) method4.invoke(storageManager, str2), ((Boolean) method3.invoke(obj, new Object[i])).booleanValue()));
                        i2++;
                        i = 0;
                        invoke = obj2;
                    }
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                str = null;
                break;
            }
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0OO oooO0OO = (OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0OO) it.next();
            String msg = "  path: " + ((Object) oooO0OO.f238OooO00o) + " info: " + oooO0OO + ' ';
            Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InfoReceiver -> " + msg);
            }
            if (oooO0OO.OooO0OO && new File(Intrinsics.stringPlus(oooO0OO.f238OooO00o, "/tuning_config.json")).exists()) {
                str = Intrinsics.stringPlus(oooO0OO.f238OooO00o, "/tuning_config.json");
                break;
            }
        }
        if (str == null) {
            Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
            Intrinsics.checkNotNullParameter("U盘中没有配置文件", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InfoReceiver -> U盘中没有配置文件");
                return;
            }
            return;
        }
        String msg2 = Intrinsics.stringPlus("通过U盘文件更新配置 jsonFilePath = ", str);
        Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "InfoReceiver -> " + msg2);
        }
        String OooO0Oo = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0Oo(str);
        String msg3 = Intrinsics.stringPlus("通过U盘文件更新配置 config = ", OooO0Oo);
        Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg3, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "InfoReceiver -> " + msg3);
        }
        if (TextUtils.isEmpty(OooO0Oo)) {
            return;
        }
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0O0.f311OooO00o.OooO00o(OooO0Oo);
    }

    public final IVolumeAdjustment OooO0O0() {
        return (IVolumeAdjustment) this.OooO0Oo.getValue();
    }

    public final void OooO0OO() {
        int streamVolume = ((AudioManager) this.OooO0OO.getValue()).getStreamVolume(3);
        OooO.f279OooO00o.getClass();
        Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOO0o.getValue()).OooO00o();
        if (num != null && num.intValue() == 1) {
            OooO0O0().setSystemVolume((int) ((streamVolume * 100.0f) / ((AudioManager) this.OooO0OO.getValue()).getStreamMaxVolume(3)));
            Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
            Intrinsics.checkNotNullParameter("  收到广播，设置系统音量", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "InfoReceiver ->   收到广播，设置系统音量");
            }
        }
        OooO0O0().setMicVolume(OooO0O0().getMicVolume());
        synchronized (OooO00o()) {
            for (OooO0o oooO0o : OooO00o()) {
                oooO0o.onVolumeChange(streamVolume);
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(@Nullable Context context, @Nullable Intent intent) {
        String action;
        if (intent == null || (action = intent.getAction()) == null) {
            return;
        }
        switch (action.hashCode()) {
            case -2128145023:
                if (action.equals("android.intent.action.SCREEN_OFF")) {
                    synchronized (OooO00o()) {
                        for (OooO0o oooO0o : OooO00o()) {
                            oooO0o.onScreenChange(false);
                        }
                    }
                    return;
                }
                return;
            case -2114103349:
                if (action.equals(USBMonitor.ACTION_USB_DEVICE_ATTACHED)) {
                    this.OooO0o0.postDelayed(new Runnable() { // from class: OooO00o.OooO0O0.OooO0o0.OooO00o.OooO00o.-$$Lambda$LpekMLC_PsBoMssRrrlbP6VeP1c
                        @Override // java.lang.Runnable
                        public final void run() {
                            OooO00o.OooO00o(OooO00o.this);
                        }
                    }, 3000L);
                    return;
                }
                return;
            case -1940635523:
                if (!action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                    return;
                }
                break;
            case -1454123155:
                if (action.equals("android.intent.action.SCREEN_ON")) {
                    synchronized (OooO00o()) {
                        for (OooO0o oooO0o2 : OooO00o()) {
                            oooO0o2.onScreenChange(true);
                        }
                    }
                    return;
                }
                return;
            case -1132153869:
                if (action.equals("com.android.sky.SendHotKey")) {
                    int intExtra = intent.getIntExtra("specialKey", 0);
                    if (intExtra == 744) {
                        OooO0O0().setMicVolumeEnable(false);
                    } else if (intExtra == 758) {
                        OooO0O0().setMicVolumeEnable(true);
                    }
                    synchronized (OooO00o()) {
                        for (OooO0o oooO0o3 : OooO00o()) {
                            oooO0o3.onSkyworthKey(intExtra);
                        }
                    }
                    return;
                }
                return;
            case -189218414:
                if (!action.equals("android.media.MASTER_VOLUME_CHANGED_ACTION")) {
                    return;
                }
                break;
            case 622839499:
                if (!action.equals("android.media.EXTRA_MASTER_VOLUME_MUTED")) {
                    return;
                }
                break;
            case 630548177:
                if (!action.equals("android.media.EXTRA_MASTER_VOLUME_VALUE")) {
                    return;
                }
                break;
            case 1170999219:
                if (!action.equals("android.media.MASTER_MUTE_CHANGED_ACTION")) {
                    return;
                }
                break;
            case 1920758225:
                if (!action.equals("android.media.STREAM_MUTE_CHANGED_ACTION")) {
                    return;
                }
                break;
            default:
                return;
        }
        Context context2 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
        if (context2 != null) {
            Object systemService = context2.getSystemService("audio");
            if (systemService != null) {
                int streamVolume = ((AudioManager) systemService).getStreamVolume(3);
                OooO.f279OooO00o.getClass();
                Lazy lazy = OooO.OooOoo;
                Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) lazy.getValue()).OooO00o();
                if (num != null && streamVolume == num.intValue()) {
                    return;
                }
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o oooO00o = (OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) lazy.getValue();
                Context context3 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context3 != null) {
                    Object systemService2 = context3.getSystemService("audio");
                    if (systemService2 != null) {
                        oooO00o.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(((AudioManager) systemService2).getStreamVolume(3)));
                        OooO0OO();
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
                }
                Intrinsics.throwUninitializedPropertyAccessException("instance");
                throw null;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    public final synchronized void OooO0O0(@NotNull OooO0o callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        OooO00o().remove(callback);
    }

    public final List<OooO0o> OooO00o() {
        return (List) this.OooO0O0.getValue();
    }

    public final synchronized void OooO00o(@NotNull OooO0o callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        OooO00o().add(callback);
    }
}
