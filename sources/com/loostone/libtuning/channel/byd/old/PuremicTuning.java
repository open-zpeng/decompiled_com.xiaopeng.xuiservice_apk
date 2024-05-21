package com.loostone.libtuning.channel.byd.old;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO;
import OooO00o.OooO0O0.OooO0o0.OooO0o.OooO00o;
import android.app.Application;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.channel.byd.data.BYDCache;
import com.loostone.libtuning.channel.byd.light.SampleTempoProcess;
import com.loostone.libtuning.data.config.vendor.EnvironmentalSoundItem;
import com.loostone.libtuning.inf.extern.IAudioFFTProcess;
import com.loostone.libtuning.inf.extern.IAudioTempoProcess;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class PuremicTuning {
    private static final String TAG = "PuremicTuningInf";
    private static volatile PuremicTuning instance;
    private IAudioTempoProcess audioTempoProcess;
    private ArrayList<EnvironmentalSoundItem> environmentalSoundItems;
    private IDongleListener localDongleListener;
    private OooO00o musicFFTControl;
    private IVolumeAdjustment volumeAdjustment = null;
    private final com.loostone.libtuning.inf.extern.IDongleListener dongleListener = new com.loostone.libtuning.inf.extern.IDongleListener() { // from class: com.loostone.libtuning.channel.byd.old.PuremicTuning.1
        @Override // com.loostone.libtuning.inf.extern.IDongleListener
        public void onDongleAttach() {
            if (PuremicTuning.this.localDongleListener != null) {
                PuremicTuning.this.localDongleListener.attach();
            }
        }

        @Override // com.loostone.libtuning.inf.extern.IDongleListener
        public void onDongleDetach() {
            PuremicTuning.this.getMusicAudioFFTControl().stop();
            PuremicTuning.this.getMusicAudioTempoControl().deinit();
            if (PuremicTuning.this.localDongleListener != null) {
                PuremicTuning.this.localDongleListener.detach();
            }
        }
    };
    private final com.loostone.libtuning.PuremicTuning realInstance = com.loostone.libtuning.PuremicTuning.Companion.getInstance();

    private PuremicTuning() {
    }

    public static synchronized PuremicTuning getInstance() {
        PuremicTuning puremicTuning;
        synchronized (PuremicTuning.class) {
            if (instance == null) {
                instance = new PuremicTuning();
            }
            puremicTuning = instance;
        }
        return puremicTuning;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0067  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isLoostoneMicPhone(android.content.Context r4) {
        /*
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o r0 = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o
            java.lang.String r1 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r1)
            java.lang.String r1 = "usb"
            java.lang.Object r4 = r4.getSystemService(r1)
            if (r4 == 0) goto L81
            android.hardware.usb.UsbManager r4 = (android.hardware.usb.UsbManager) r4
            java.util.HashMap r4 = r4.getDeviceList()
            if (r4 != 0) goto L18
            goto L44
        L18:
            java.util.Collection r4 = r4.values()
            java.util.Iterator r4 = r4.iterator()
        L20:
            boolean r1 = r4.hasNext()
            if (r1 == 0) goto L38
            java.lang.Object r1 = r4.next()
            android.hardware.usb.UsbDevice r1 = (android.hardware.usb.UsbDevice) r1
            java.lang.String r2 = "usbDevice"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r2)
            boolean r1 = r0.OooO00o(r1)
            if (r1 == 0) goto L20
            goto L42
        L38:
            java.lang.String r4 = "puremic"
            int r4 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(r4)
            r0 = -1
            if (r4 == r0) goto L44
        L42:
            r4 = 1
            goto L45
        L44:
            r4 = 0
        L45:
            java.lang.String r0 = "PuremicTuningInf"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "isLoostoneMicPhone() "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "tag"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r2)
            java.lang.String r2 = "msg"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r2)
            int r2 = OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0
            r3 = 3
            if (r2 > r3) goto L80
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            java.lang.String r0 = " -> "
            r2.append(r0)
            r2.append(r1)
            java.lang.String r0 = r2.toString()
            java.lang.String r1 = "LogTuning"
            android.util.Log.i(r1, r0)
        L80:
            return r4
        L81:
            java.lang.NullPointerException r4 = new java.lang.NullPointerException
            java.lang.String r0 = "null cannot be cast to non-null type android.hardware.usb.UsbManager"
            r4.<init>(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.byd.old.PuremicTuning.isLoostoneMicPhone(android.content.Context):boolean");
    }

    private void setFixGainEffect() {
        BYDCache bYDCache = BYDCache.INSTANCE;
        int intValue = bYDCache.getReverbVolume().OooO00o() == null ? 1 : bYDCache.getReverbVolume().OooO00o().intValue();
        int intValue2 = bYDCache.getKaraokeEffect().OooO00o() != null ? bYDCache.getKaraokeEffect().OooO00o().intValue() : 1;
        IVolumeAdjustment iVolumeAdjustment = this.volumeAdjustment;
        if (iVolumeAdjustment != null) {
            iVolumeAdjustment.setAef2ReverbVolume(intValue);
            this.volumeAdjustment.setEnvironmentalSound(intValue2, bYDCache.getReverbVolume().OooO00o() == null ? 1.0d : bYDCache.getReverbVolume().OooO00o().intValue() / 5.0d);
        }
    }

    public static void setNeedListenSystemVolume(boolean z) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("setNeedListenSystemVolume()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> setNeedListenSystemVolume()");
        }
    }

    public void deinit() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("deInit()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> deInit()");
        }
        this.realInstance.getDongleManager().unRegisterDongleListener(this.dongleListener);
        this.realInstance.release();
        this.localDongleListener = null;
        this.volumeAdjustment = null;
        this.musicFFTControl = null;
        this.audioTempoProcess = null;
    }

    public IAudioFFTProcess getMusicAudioFFTControl() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("getMusicAudioFFTControl()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> getMusicAudioFFTControl()");
        }
        return this.musicFFTControl;
    }

    public IAudioTempoProcess getMusicAudioTempoControl() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("getMusicAudioTempoControl()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> getMusicAudioTempoControl()");
        }
        return this.audioTempoProcess;
    }

    public void init(Application application, int i) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("init()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> init()");
        }
        this.localDongleListener = null;
        this.realInstance.init(application, 400000L, i);
        this.volumeAdjustment = this.realInstance.getVolumeAdjustment();
        this.realInstance.getDongleManager().registerDongleListener(this.dongleListener);
        if (this.environmentalSoundItems == null) {
            this.environmentalSoundItems = OooO0OO.OooO0O0;
        }
        this.musicFFTControl = new OooO00o(OooO00o.OooO0O0.MUSIC);
        this.audioTempoProcess = new SampleTempoProcess();
    }

    public void setDongleListener(IDongleListener iDongleListener) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("setDongleListener()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> setDongleListener()");
        }
        this.localDongleListener = iDongleListener;
    }

    public void setKaraokeEffect(int i) {
        String msg = "setKaraokeEffect(" + i + ")";
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        BYDCache.INSTANCE.getKaraokeEffect().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
        setFixGainEffect();
    }

    public void setMicPercentage(int i) {
        String msg = "setMicPercentage(" + i + ")";
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        IVolumeAdjustment iVolumeAdjustment = this.volumeAdjustment;
        if (iVolumeAdjustment != null) {
            iVolumeAdjustment.setMicVolume(i);
        }
    }

    public void setReverbPercentage(int i) {
        String msg = "setReverbPercentage(" + i + ")";
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        BYDCache.INSTANCE.getReverbVolume().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i / 10));
        setFixGainEffect();
    }
}
