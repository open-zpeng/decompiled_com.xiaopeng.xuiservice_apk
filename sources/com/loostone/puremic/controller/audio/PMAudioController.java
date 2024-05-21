package com.loostone.puremic.controller.audio;

import OooO00o.OooO0O0.OooO0o0.OooO0o.OooOO0;
import android.content.Context;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libbottom.util.JniUtil;
import com.loostone.libtuning.PuremicTuning;
import com.loostone.libtuning.channel.FullSceneSpeakMgr;
import com.loostone.libtuning.data.CarConstant;
import com.loostone.puremic.controller.audio.listener.IDongleListener;
import com.lzy.okgo.model.Progress;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.UByte;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class PMAudioController {
    private static final int BUF_SIZE_MILLISECOND = 192;
    private static final int BUF_SIZE_MILLISECOND_20 = 3840;
    private static final int CONCERT = 80;
    private static final int KTV = 30;
    private static final int LATENCY_SIZE = 92160;
    private static final int MUSIC_MAX = 100;
    private static final int STUDIO = 45;
    private static final String TAG = "PMAudioController";
    private static final int VOCAL_CONCERT = 60;
    private static boolean isInit = false;
    private static int mDeviceCard = -1;
    private static final Object recordLock = new Object();
    private static final Object trackLock = new Object();
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private FullSceneSpeakMgr fullSceneSpeakMgr;
    private int getMicDataAvailCount;
    private int getRecDataAvailCount;
    private IDongleListener iDongleListener;
    private int micReadCount;
    private int recReadCount;
    private int switchCount;
    private int totalSize;
    private byte[] trackBuf;
    private int trackGetAvailCount;
    private int trackGetLatencyCount;
    private int trackSampleRate;
    private int trackWriteCount;
    private long latencySize = 92160;
    private final byte[] micStereoBuffer = new byte[38400];
    private final byte[] micStereoBufferReal = new byte[38400];
    private int musicVolume = 100;
    private boolean playing = false;
    private boolean pausing = false;
    private final OooOO0 trackRingBuffer = new OooOO0(192000);
    private final OooOO0 recordRingBuffer = new OooOO0(192000);
    private final byte[] trackBuffer = new byte[192000];
    private final byte[] recordBuffer = new byte[192000];
    private final OooOO0 trackRingBuffer2 = new OooOO0(38400);
    private final OooOO0 micRingBuffer = new OooOO0(38400);
    private int micSampleRate = 44100;
    private int micChannel = 2;
    private int micSampleRateReal = 48000;
    private long latencyMicSize = 0;
    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService singleThreadExecutor2 = Executors.newSingleThreadExecutor();
    private final com.loostone.libtuning.inf.extern.IDongleListener dongleListener = new OooO00o();
    private final OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO micPowerListener = new OooO0O0();
    private final Runnable micRunnable = new OooO0OO();
    private final Runnable trackRunnable = new OooO0o();
    private final int maxCount = 3;

    /* loaded from: classes4.dex */
    public class OooO implements Runnable {
        public OooO() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = 0;
            while (i < 10) {
                if (PMAudioController.this.fullSceneSpeakMgr != null) {
                    PMAudioController.this.fullSceneSpeakMgr.setKaraokeMode(1);
                    PMAudioController.this.fullSceneSpeakMgr.startKaraoke();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("tryStartKaraoke count: ");
                i++;
                sb.append(i);
                String msg = sb.toString();
                Intrinsics.checkNotNullParameter(PMAudioController.TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", PMAudioController.TAG + " -> " + msg);
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    public class OooO00o implements com.loostone.libtuning.inf.extern.IDongleListener {
        public OooO00o() {
        }

        @Override // com.loostone.libtuning.inf.extern.IDongleListener
        public void onDongleAttach() {
            if (PMAudioController.this.iDongleListener != null) {
                Intrinsics.checkNotNullParameter(PMAudioController.TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter("dongle in", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", PMAudioController.TAG + " -> dongle in");
                }
                PMAudioController.this.iDongleListener.onDongleChanged(true);
            }
        }

        @Override // com.loostone.libtuning.inf.extern.IDongleListener
        public void onDongleDetach() {
            if (PMAudioController.this.iDongleListener != null) {
                Intrinsics.checkNotNullParameter(PMAudioController.TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter("dongle out", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", PMAudioController.TAG + " -> dongle out");
                }
                PMAudioController.this.iDongleListener.onDongleChanged(false);
            }
        }
    }

    /* loaded from: classes4.dex */
    public class OooO0O0 implements OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO {
        public OooO0O0() {
        }

        @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO
        public void OooO00o(int i) {
            if (i == 1) {
                if (PMAudioController.this.iDongleListener != null) {
                    Intrinsics.checkNotNullParameter(PMAudioController.TAG, Progress.TAG);
                    Intrinsics.checkNotNullParameter("mic on", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                        Log.i("LogTuning", PMAudioController.TAG + " -> mic on");
                    }
                    PMAudioController.this.iDongleListener.onMicPowerStatusChanged(true);
                }
            } else if (PMAudioController.this.iDongleListener != null) {
                Intrinsics.checkNotNullParameter(PMAudioController.TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter("mic off", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", PMAudioController.TAG + " -> mic off");
                }
                PMAudioController.this.iDongleListener.onMicPowerStatusChanged(false);
            }
        }
    }

    /* loaded from: classes4.dex */
    public class OooO0OO implements Runnable {
        public OooO0OO() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (PMAudioController.this.audioRecord != null) {
                try {
                    if (PMAudioController.this.micSampleRate != PMAudioController.this.micSampleRateReal) {
                        int resample = JniUtil.f600OooO00o.resample(PMAudioController.this.micStereoBufferReal, PMAudioController.this.micStereoBuffer, PMAudioController.this.micSampleRateReal, PMAudioController.this.micSampleRate, (PMAudioController.this.audioRecord.read(PMAudioController.this.micStereoBufferReal, 0, PMAudioController.BUF_SIZE_MILLISECOND_20) / 2) / 2, 2) * 2 * 2;
                        if (PMAudioController.this.playing) {
                            if (PMAudioController.this.latencyMicSize > PMAudioController.this.latencySize) {
                                PMAudioController.this.recordRingBuffer.OooO0O0(PMAudioController.this.micStereoBuffer, 0, resample);
                            } else {
                                PMAudioController.access$714(PMAudioController.this, resample);
                            }
                        }
                        if (PMAudioController.this.micChannel != 1) {
                            PMAudioController.this.micRingBuffer.OooO0O0(PMAudioController.this.micStereoBuffer, 0, resample);
                        } else {
                            for (int i = 0; i < resample / 2; i += 2) {
                                int i2 = i * 2;
                                PMAudioController.this.micStereoBuffer[i] = PMAudioController.this.micStereoBuffer[i2];
                                PMAudioController.this.micStereoBuffer[i + 1] = PMAudioController.this.micStereoBuffer[i2 + 1];
                            }
                            PMAudioController.this.micRingBuffer.OooO0O0(PMAudioController.this.micStereoBuffer, 0, resample / 2);
                        }
                    } else {
                        int read = PMAudioController.this.audioRecord.read(PMAudioController.this.micStereoBuffer, 0, PMAudioController.BUF_SIZE_MILLISECOND_20);
                        if (PMAudioController.this.playing) {
                            if (PMAudioController.this.latencyMicSize > PMAudioController.this.latencySize) {
                                PMAudioController.this.recordRingBuffer.OooO0O0(PMAudioController.this.micStereoBuffer, 0, read);
                            } else {
                                PMAudioController.access$714(PMAudioController.this, read);
                            }
                        }
                        if (PMAudioController.this.micChannel != 1) {
                            PMAudioController.this.micRingBuffer.OooO0O0(PMAudioController.this.micStereoBuffer, 0, read);
                        } else {
                            for (int i3 = 0; i3 < read / 2; i3 += 2) {
                                int i4 = i3 * 2;
                                PMAudioController.this.micStereoBuffer[i3] = PMAudioController.this.micStereoBuffer[i4];
                                PMAudioController.this.micStereoBuffer[i3 + 1] = PMAudioController.this.micStereoBuffer[i4 + 1];
                            }
                            PMAudioController.this.micRingBuffer.OooO0O0(PMAudioController.this.micStereoBuffer, 0, read / 2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    public class OooO0o implements Runnable {
        public OooO0o() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (PMAudioController.this.audioTrack != null) {
                try {
                    if (!PMAudioController.this.pausing) {
                        int OooO00o2 = PMAudioController.this.trackRingBuffer2.OooO00o(PMAudioController.this.trackBuf, 0, PMAudioController.this.trackBuf.length);
                        PMAudioController.volumeChange(PMAudioController.this.trackBuf, OooO00o2, PMAudioController.this.musicVolume, 100);
                        PMAudioController.this.audioTrack.write(PMAudioController.this.trackBuf, 0, OooO00o2);
                        PMAudioController.this.playing = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public static /* synthetic */ long access$714(PMAudioController pMAudioController, long j) {
        long j2 = pMAudioController.latencyMicSize + j;
        pMAudioController.latencyMicSize = j2;
        return j2;
    }

    private static int getChannelConfig(int i) {
        return i == 1 ? 4 : 12;
    }

    private int getDeviceCard(boolean z, String str) {
        if (z) {
            if (str.toUpperCase(Locale.getDefault()).contains(CarConstant.CAR_TYPE_P5) || str.toUpperCase(Locale.getDefault()).contains(CarConstant.CAR_TYPE_G9) || str.toUpperCase(Locale.getDefault()).contains(CarConstant.CAR_TYPE_P7A)) {
                return 9;
            }
            return (str.toUpperCase(Locale.getDefault()).contains(CarConstant.CAR_TYPE_G3) || str.toUpperCase(Locale.getDefault()).contains(CarConstant.CAR_TYPE_G3i) || str.toUpperCase(Locale.getDefault()).contains(CarConstant.CAR_TYPE_P7)) ? 17 : -1;
        }
        return 54;
    }

    private static void mix(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        for (int i2 = 0; i2 < i; i2 += 2) {
            int i3 = i2 + 1;
            int i4 = ((bArr2[i2] & UByte.MAX_VALUE) | (bArr2[i3] << 8)) + ((bArr3[i2] & UByte.MAX_VALUE) | (bArr3[i3] << 8));
            if (i4 > 32767) {
                i4 = 32767;
            } else if (i4 < -32768) {
                i4 = -32768;
            }
            bArr[i2] = (byte) i4;
            bArr[i3] = (byte) (i4 >> 8);
        }
    }

    private void tryStartKaraoke() {
        new Thread(new OooO()).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void volumeChange(byte[] bArr, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i; i4 += 2) {
            int i5 = i4 + 1;
            int i6 = (((bArr[i4] & 255) | (bArr[i5] << 8)) * i2) / i3;
            bArr[i4] = (byte) i6;
            bArr[i5] = (byte) (i6 >> 8);
        }
    }

    public int deinit() {
        synchronized (PuremicTuning.class) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("deinit:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> deinit:");
            }
            if (isInit) {
                this.fullSceneSpeakMgr.setMicPowerListener(null);
                this.fullSceneSpeakMgr.getLocalDongleManager().unRegisterDongleListener(this.dongleListener);
                this.fullSceneSpeakMgr = null;
                PuremicTuning.Companion.getInstance().release();
                JniUtil.f600OooO00o.resampleRelease();
                isInit = false;
            }
            this.iDongleListener = null;
        }
        return 0;
    }

    public int getHandShakeStatus() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("getHandShakeStatus: no implement", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 4) {
            Log.w("LogTuning", TAG + " -> getHandShakeStatus: no implement");
            return 1;
        }
        return 1;
    }

    public int getMicDataAvail() {
        int i = this.getMicDataAvailCount;
        if (i < 3) {
            this.getMicDataAvailCount = i + 1;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("getMicDataAvail:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> getMicDataAvail:");
            }
        }
        return this.micRingBuffer.OooO00o();
    }

    public int getMicPowerStatus() {
        int i;
        synchronized (PuremicTuning.class) {
            i = PuremicTuning.Companion.getInstance().getChannelMgr().getPowerStatus() == 3 ? 1 : 0;
            String msg = "getMicPowerStatus:" + i;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
        }
        return i;
    }

    public int getMicStatus() {
        int i;
        synchronized (PuremicTuning.class) {
            i = PuremicTuning.Companion.getInstance().getChannelMgr().hasDongle() ? 0 : -1;
            String msg = "getMicStatus:" + i;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
        }
        return i;
    }

    public int getRecDataAvail() {
        int i = this.getRecDataAvailCount;
        if (i < 3) {
            this.getRecDataAvailCount = i + 1;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("getRecDataAvail:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> getRecDataAvail:");
            }
        }
        return Math.min(this.recordRingBuffer.OooO00o(), this.trackRingBuffer.OooO00o());
    }

    public int getToken() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("getToken: no implement", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 4) {
            Log.w("LogTuning", TAG + " -> getToken: no implement");
            return 0;
        }
        return 0;
    }

    public int getVolume(int i) {
        synchronized (PuremicTuning.class) {
            String msg = "getVolume: type:" + i;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
            if (i == 0) {
                return PuremicTuning.Companion.getInstance().getVolumeAdjustment().getMusicVolume();
            } else if (i == 1) {
                return PuremicTuning.Companion.getInstance().getVolumeAdjustment().getMicVolume();
            } else if (i == 2) {
                return PuremicTuning.Companion.getInstance().getVolumeAdjustment().getAef2ReverbVolume();
            } else if (i == 3) {
                return PuremicTuning.Companion.getInstance().getVolumeAdjustment().getAef4ReverbVolume();
            } else if (i == 4) {
                return PuremicTuning.Companion.getInstance().getVolumeAdjustment().getAef4SceneEffectMode();
            } else if (i != 5) {
                return -1;
            } else {
                return PuremicTuning.Companion.getInstance().getVolumeAdjustment().getAef4BypassEnable();
            }
        }
    }

    public int init(Context context) {
        synchronized (PuremicTuning.class) {
            String msg = " will init, flag = " + isInit;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
            if (!isInit) {
                PuremicTuning.Companion companion = PuremicTuning.Companion;
                companion.getInstance().init(context, -1L, 1);
                FullSceneSpeakMgr fullSceneSpeakMgr = (FullSceneSpeakMgr) companion.getInstance().getChannelMgr();
                this.fullSceneSpeakMgr = fullSceneSpeakMgr;
                fullSceneSpeakMgr.setMicPowerListener(this.micPowerListener);
                this.fullSceneSpeakMgr.getLocalDongleManager().registerDongleListener(this.dongleListener);
                JniUtil.f600OooO00o.resampleInit();
                isInit = true;
                Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter(" init complete ", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", TAG + " ->  init complete ");
                }
            }
        }
        return 0;
    }

    public int micCreate(int i, int i2, int i3) {
        String msg = "micCreate: sampleRate:" + i + " channel:" + i2 + " bufSize:" + i3;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        this.getMicDataAvailCount = 0;
        this.micReadCount = 0;
        this.latencyMicSize = 0L;
        this.latencySize = PuremicTuning.Companion.getInstance().getVolumeAdjustment().getRecordVocalOffset() * 192;
        this.micRingBuffer.OooO0O0();
        this.micSampleRate = i;
        this.micChannel = i2;
        return 0;
    }

    public int micDestroy() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("micDestroy:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> micDestroy:");
        }
        this.micRingBuffer.OooO0O0();
        return 0;
    }

    public int micGetMinBuf(int i, int i2) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("micGetMinBuf:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> micGetMinBuf:");
        }
        return AudioRecord.getMinBufferSize(i, getChannelConfig(i2), 2);
    }

    public int pause() {
        synchronized (recordLock) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("pause:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> pause:");
            }
            FullSceneSpeakMgr fullSceneSpeakMgr = this.fullSceneSpeakMgr;
            if (fullSceneSpeakMgr != null) {
                fullSceneSpeakMgr.setKaraokeMode(0);
                this.fullSceneSpeakMgr.stopKaraoke();
            }
            AudioRecord audioRecord = this.audioRecord;
            if (audioRecord != null) {
                audioRecord.release();
                this.audioRecord = null;
                return 0;
            }
            return -1;
        }
    }

    public int pausePlay() {
        pause();
        synchronized (trackLock) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("pausePlay:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> pausePlay:");
            }
            this.pausing = true;
            AudioTrack audioTrack = this.audioTrack;
            if (audioTrack != null) {
                audioTrack.pause();
                return 0;
            }
            return -1;
        }
    }

    public int readMic(byte[] bArr) {
        int min;
        synchronized (recordLock) {
            min = Math.min(this.micRingBuffer.OooO00o(), bArr.length);
            this.micRingBuffer.OooO00o(bArr, 0, min);
            int i = this.micReadCount;
            if (i < 3) {
                this.micReadCount = i + 1;
                String msg = "readMic: buf.length:" + bArr.length + " size:" + min;
                Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", TAG + " -> " + msg);
                }
            }
        }
        return min;
    }

    public int readRec(byte[] bArr) {
        int min;
        synchronized (recordLock) {
            min = Math.min(this.recordRingBuffer.OooO00o(), this.trackRingBuffer.OooO00o());
            if (min > bArr.length) {
                min = bArr.length;
            }
            this.recordRingBuffer.OooO00o(this.recordBuffer, 0, min);
            this.trackRingBuffer.OooO00o(this.trackBuffer, 0, min);
            mix(bArr, this.recordBuffer, this.trackBuffer, min);
            int i = this.recReadCount;
            if (i < 3) {
                this.recReadCount = i + 1;
                String msg = "readRec: buf.length:" + bArr.length + " size:" + min;
                Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", TAG + " -> " + msg);
                }
            }
        }
        return min;
    }

    public int recCreate(int i, int i2, int i3) {
        String msg = "recCreate: sampleRate:" + i + " channel:" + i2 + " bufSize:" + i3;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        this.getRecDataAvailCount = 0;
        this.recReadCount = 0;
        this.recordRingBuffer.OooO0O0();
        return 0;
    }

    public int recDestroy() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("recDestroy:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> recDestroy:");
        }
        this.recordRingBuffer.OooO0O0();
        micDestroy();
        return 0;
    }

    public int recGetMinBuf(int i, int i2) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("recGetMinBuf:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> recGetMinBuf:");
        }
        return AudioRecord.getMinBufferSize(i, getChannelConfig(i2), 2);
    }

    public void recoveryFromJson() {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("recoveryFromJson:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> recoveryFromJson:");
        }
        PuremicTuning.Companion.getInstance().getVolumeAdjustment().recoveryFromJson();
    }

    public int resume() {
        synchronized (recordLock) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("resume:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> resume:");
            }
            tryStartKaraoke();
            if (this.audioRecord == null) {
                this.micSampleRateReal = this.micSampleRate;
                AudioRecord audioRecord = new AudioRecord(1, this.micSampleRateReal, getChannelConfig(2), 2, this.micStereoBufferReal.length);
                this.audioRecord = audioRecord;
                if (audioRecord.getState() == 1) {
                    this.audioRecord.startRecording();
                    this.singleThreadExecutor2.execute(this.micRunnable);
                    return 0;
                }
                throw new RuntimeException("Puremic Record create failed!");
            }
            return -1;
        }
    }

    public int resumePlay() {
        resume();
        synchronized (trackLock) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("resumePlay:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> resumePlay:");
            }
            this.pausing = false;
            AudioTrack audioTrack = this.audioTrack;
            if (audioTrack != null) {
                audioTrack.play();
                return 0;
            }
            return -1;
        }
    }

    public int setCommonData(int i, String str) {
        String msg = "setCommonData, type = " + i + ", msg = " + str;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        if (i != 0) {
            String msg2 = "setCommonData, type[" + i + "] is invalid";
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg2, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg2);
                return -1;
            }
            return -1;
        } else if (this.playing) {
            try {
                pausePlay();
                trackDestroy();
                return 0;
            } catch (Exception e) {
                Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter("local_ig_on pausePlay & trackDestroy", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", TAG + " -> local_ig_on pausePlay & trackDestroy");
                    return 0;
                }
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void setDongleListener(IDongleListener iDongleListener) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("setDongleListener:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> setDongleListener:");
        }
        this.iDongleListener = iDongleListener;
    }

    public int setSignedToken(String str) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("setSignedToken: no implement", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 4) {
            Log.w("LogTuning", TAG + " -> setSignedToken: no implement");
            return 0;
        }
        return 0;
    }

    public int setVolume(int i, int i2) {
        synchronized (PuremicTuning.class) {
            String msg = "setVolume: type:" + i + " vol:" + i2;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
            if (i == 0) {
                this.musicVolume = i2;
                PuremicTuning.Companion.getInstance().getVolumeAdjustment().setMusicVolume(i2);
                return 0;
            } else if (i == 1) {
                PuremicTuning.Companion.getInstance().getVolumeAdjustment().setMicVolume(i2);
                return 0;
            } else if (i != 2) {
                if (i == 3) {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4ReverbVolume(i2);
                    return 0;
                } else if (i == 4) {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4SceneEffectMode(i2);
                    return 0;
                } else if (i != 5) {
                    return -1;
                } else {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4BypassEnable(i2);
                    return 0;
                }
            } else {
                if (i2 == 30) {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4SceneEffectMode(0);
                } else if (i2 == 45) {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4SceneEffectMode(2);
                } else if (i2 == 60) {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4SceneEffectMode(3);
                } else if (i2 != 80) {
                    return -1;
                } else {
                    PuremicTuning.Companion.getInstance().getVolumeAdjustment().setAef4SceneEffectMode(1);
                }
                return 0;
            }
        }
    }

    public void switchPcmCardDevice(boolean z, String str) {
        String msg = "switchPcmCardDevice: isInCar:" + z + ", carType:" + str;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        int deviceCard = getDeviceCard(z, str);
        mDeviceCard = deviceCard;
        if (!isInit) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("switchPcmCardDevice: isInit = false", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> switchPcmCardDevice: isInit = false");
                return;
            }
            return;
        }
        if (!this.pausing && this.playing) {
            this.switchCount++;
        }
        if (deviceCard >= 0) {
            PuremicTuning.Companion.getInstance().getVolumeAdjustment().setDeviceCard(String.format(Locale.getDefault(), "%03d", Integer.valueOf(mDeviceCard)));
            mDeviceCard = -1;
            this.fullSceneSpeakMgr.dynamicAdjustCompPregain();
        }
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord == null || audioRecord.getRecordingState() != 3) {
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("resetKaraoke:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> resetKaraoke:");
        }
        FullSceneSpeakMgr fullSceneSpeakMgr = this.fullSceneSpeakMgr;
        if (fullSceneSpeakMgr != null) {
            fullSceneSpeakMgr.resetKaraoke();
        }
    }

    public int trackCreate(int i, int i2, int i3) {
        String msg = "trackCreate: mDeviceCard:" + mDeviceCard;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        if (mDeviceCard >= 0) {
            PuremicTuning.Companion.getInstance().getVolumeAdjustment().setDeviceCard(String.format(Locale.getDefault(), "%03d", Integer.valueOf(mDeviceCard)));
            mDeviceCard = -1;
        }
        this.trackSampleRate = i;
        synchronized (trackLock) {
            String msg2 = "trackCreate: sampleRate:" + i + " channel:" + i2 + " bufSize:" + i3;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg2, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> " + msg2);
            }
            AudioTrack audioTrack = this.audioTrack;
            if (audioTrack != null) {
                audioTrack.release();
                this.audioTrack = null;
            }
            this.trackGetAvailCount = 0;
            this.trackGetLatencyCount = 0;
            this.trackWriteCount = 0;
            this.trackRingBuffer.OooO0O0();
            this.trackBuf = new byte[i3];
            this.totalSize = 0;
            this.switchCount = 0;
            this.musicVolume = PuremicTuning.Companion.getInstance().getVolumeAdjustment().getMusicVolume();
            AudioTrack audioTrack2 = new AudioTrack(3, i, getChannelConfig(i2), 2, i3, 1);
            this.audioTrack = audioTrack2;
            if (audioTrack2.getState() == 1) {
                this.audioTrack.play();
                this.singleThreadExecutor.execute(this.trackRunnable);
            } else {
                throw new RuntimeException("Puremic Track create failed!");
            }
        }
        return 0;
    }

    public int trackDestroy() {
        synchronized (trackLock) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("trackDestroy:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> trackDestroy:");
            }
            this.trackRingBuffer.OooO0O0();
            this.trackRingBuffer2.OooO0O0();
            AudioTrack audioTrack = this.audioTrack;
            if (audioTrack != null) {
                audioTrack.release();
                this.audioTrack = null;
            }
            recDestroy();
            this.trackBuf = null;
            this.playing = false;
            this.pausing = false;
            this.totalSize = 0;
            this.switchCount = 0;
        }
        return 0;
    }

    public int trackGetAvail() {
        int i = this.trackGetAvailCount;
        if (i < 3) {
            this.trackGetAvailCount = i + 1;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("trackGetAvail:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 4) {
                Log.w("LogTuning", TAG + " -> trackGetAvail:");
            }
        }
        OooOO0 oooOO0 = this.trackRingBuffer2;
        return oooOO0.OooO0OO - (oooOO0.OooO0O0 - oooOO0.f568OooO00o);
    }

    public int trackGetLatency() {
        int i = this.trackGetLatencyCount;
        if (i < 3) {
            this.trackGetLatencyCount = i + 1;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("trackGetLatency:", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", TAG + " -> trackGetLatency:");
            }
        }
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            try {
                return (this.totalSize - ((audioTrack.getPlaybackHeadPosition() * 2) * 2)) - ((this.switchCount * this.trackSampleRate) / 5);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public int trackGetMinBuf(int i, int i2) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("trackGetMinBuf:", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> trackGetMinBuf:");
        }
        return AudioTrack.getMinBufferSize(i, getChannelConfig(i2), 2);
    }

    public int write(byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2 && this.audioTrack != null && !this.pausing) {
            int write = write(bArr, i3, i2 - i3, false);
            if (write >= 0) {
                i3 += write;
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return i3;
    }

    private int write(byte[] bArr, int i, int i2, boolean z) {
        int i3 = this.trackWriteCount;
        if (i3 < 3) {
            this.trackWriteCount = i3 + 1;
            String msg = "write: data.length:" + bArr.length + " size:" + i2;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
        }
        OooOO0 oooOO0 = this.trackRingBuffer2;
        if (oooOO0.OooO0OO - (oooOO0.OooO0O0 - oooOO0.f568OooO00o) >= i2) {
            this.trackRingBuffer.OooO0O0(bArr, 0, i2);
            this.trackRingBuffer2.OooO0O0(bArr, 0, i2);
            this.totalSize += i2;
            return i2;
        }
        return 0;
    }
}
