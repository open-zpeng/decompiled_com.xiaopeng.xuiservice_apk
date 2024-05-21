package com.xiaopeng.xuiservice.mediacenter;

import android.os.SystemProperties;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes5.dex */
public class Config {
    public static final boolean DBG = SystemProperties.getBoolean("persist.media.center.logger", false);
    public static final int CAPTURE_SIZE = SystemProperties.getInt("persist.fft.capture.size", 1024);
    public static final int DynamicExtremebufferLen = SystemProperties.getInt("persist.dynamic.extreme.BufLen", 10);
    public static final int fftTimeGap = SystemProperties.getInt("persist.fft.timeGap", 40);
    public static final float fftaverageGap = (float) SystemProperties.getLong("persist.fft.averageGap", 20);
    public static final float fftkaraokeGap = (float) SystemProperties.getLong("persist.karaoke.fft.minGap", 20);
    public static final boolean A2dpAutoSwitch = SystemProperties.getBoolean("persist.sys.xp.a2dp.switch", true);
    public static final boolean MuteAutoSwitch = SystemProperties.getBoolean("persist.sys.xp.mute.switch", true);
    public static final boolean ShowSourceLLULimit = SystemProperties.getBoolean("persist.autoshow.llu.limit", false);
    public static final boolean SendXpMusicInfoToIcm = SystemProperties.getBoolean("persist.sys.send.xpmusic.toicm", true);
    public static final int rateSpeed = SystemProperties.getInt("persist.visualizer.rate", 1);
    public static final boolean MediaButtonControl = SystemProperties.getBoolean("persist.sys.media.button.control", true);
    public static final int MediaLaunchPolicy = FeatureOption.FO_MEDIA_LAUNCHER_POLICY;
    public static final boolean Load_Media_Lyric = SystemProperties.getBoolean("persist.sys.load.media.lyric", true);
    public static final int Fft_Print_Size = SystemProperties.getInt("persist.sys.media.fft.size", 80);
    public static final boolean Show_Seize_Dialog = SystemProperties.getBoolean("persist.sys.media.seize.dialog", true);
    public static final boolean XP_Music_Session_Control = SystemProperties.getBoolean("persist.sys.xp.music.session", false);
}
