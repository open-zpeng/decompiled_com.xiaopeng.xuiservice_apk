package com.loostone.libtuning.channel.skyworth.old.ai;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.loostone.libtuning.PuremicTuning;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import com.loostone.libtuning.channel.skyworth.old.karaoke.IKaraokeControl;
import com.loostone.libtuning.channel.skyworth.old.karaoke.impl.ChangbaKaraokeControl;
import com.loostone.libtuning.channel.skyworth.old.karaoke.impl.NormalKaraokeControl;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.xapp.Constants;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class AiControlMgr {
    private static AiControlMgr mInstance;
    private final BaseChannelMgr fullSceneSpeakMgr;
    private Context mContext;
    private IKaraokeControl mKaraokeControl;
    private final IVolumeAdjustment volumeAdjustment;
    private final String TAG = "AiControlMgr";
    private final String CHANGBA_APP = Constants.PACKAGE_CHANGBA;

    private AiControlMgr() {
        PuremicTuning.Companion companion = PuremicTuning.Companion;
        this.fullSceneSpeakMgr = companion.getInstance().getChannelMgr();
        this.volumeAdjustment = companion.getInstance().getVolumeAdjustment();
    }

    private void adjustVolume(String str, String str2, int i) {
        if (AiCmd.INCREASE.equals(str2)) {
            str2 = "+" + i;
        } else if (AiCmd.REDUCE.equals(str2)) {
            str2 = "-" + i;
        }
        OooO0O0 oooO0O0 = OooO0O0.f225OooO00o;
        oooO0O0.OooO00o("AiControlMgr", "adjustVolume, cmd: " + str + ", value:" + str2);
        if (AiCmd.ADJUST_MUSIC.equals(str)) {
            if (this.mKaraokeControl == null || !AiControlFactory.getAiControl().isEnterApp()) {
                return;
            }
            this.mKaraokeControl.adjustMusic(str2);
        } else if (AiCmd.ADJUST_MIC.equals(str)) {
            if (!AiControlFactory.getAiControl().isEnterPlayer()) {
                oooO0O0.OooO00o("AiControlMgr", "self mic");
                this.volumeAdjustment.setMicVolume(parseVolume(str, str2));
            } else if (this.mKaraokeControl != null) {
                oooO0O0.OooO00o("AiControlMgr", "else mic");
                this.mKaraokeControl.adjustMic(str2);
            }
        } else if (AiCmd.ADJUST_ECHO.equals(str)) {
            if (!AiControlFactory.getAiControl().isEnterPlayer()) {
                oooO0O0.OooO00o("AiControlMgr", "self echo");
                this.volumeAdjustment.setAef1ReverbOutGain(parseVolume(str, str2));
                return;
            }
            IKaraokeControl iKaraokeControl = this.mKaraokeControl;
            if (iKaraokeControl != null) {
                if (Constants.PACKAGE_CHANGBA.equals(iKaraokeControl.getPackageName())) {
                    oooO0O0.OooO00o("AiControlMgr", "changba echo");
                    if ("唱将".equals(str2)) {
                        this.mKaraokeControl.adjustEcho("+0");
                        return;
                    } else if ("魔音".equals(str2)) {
                        this.mKaraokeControl.adjustEcho("+1");
                        return;
                    } else if ("专业".equals(str2)) {
                        this.mKaraokeControl.adjustEcho("+2");
                        return;
                    } else if ("歌神".equals(str2)) {
                        this.mKaraokeControl.adjustEcho("+3");
                        return;
                    } else {
                        this.mKaraokeControl.adjustEcho(str2);
                        return;
                    }
                }
                oooO0O0.OooO00o("AiControlMgr", "else echo");
                this.mKaraokeControl.adjustEcho(str2);
            }
        } else if (AiCmd.ADJUST_TONE.equals(str) && this.mKaraokeControl != null && AiControlFactory.getAiControl().isEnterApp()) {
            this.mKaraokeControl.adjustTone(str2);
        }
    }

    public static synchronized AiControlMgr getInstance() {
        AiControlMgr aiControlMgr;
        synchronized (AiControlMgr.class) {
            if (mInstance == null) {
                mInstance = new AiControlMgr();
            }
            aiControlMgr = mInstance;
        }
        return aiControlMgr;
    }

    private int getMicAvailable() {
        return this.fullSceneSpeakMgr.getVerifyStatus() == 2 ? 1 : 0;
    }

    private void initKaraokeControl(Cmd cmd) {
        String value;
        if (AiControlFactory.getAiControl().isEnterApp() && !AiCmd.OPEN_APP.equals(cmd.getCmd())) {
            value = AiControlFactory.getAiControl().getPackageName();
            String msg = "already enter karaoke app, package:" + value;
            Intrinsics.checkNotNullParameter("AiControlMgr", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "AiControlMgr -> " + msg);
            }
            if (TextUtils.isEmpty(value)) {
                value = KaraokeVoiceAppLogic.getInstance().getPackageName();
                String msg2 = "package name is empty, use dft," + value;
                Intrinsics.checkNotNullParameter("AiControlMgr", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg2, "msg");
                if (OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "AiControlMgr -> " + msg2);
                }
            }
        } else if (AiCmd.OPEN_APP.equals(cmd.getCmd()) || AiCmd.SEARCH.equals(cmd.getCmd())) {
            OooO0O0 oooO0O0 = OooO0O0.f225OooO00o;
            oooO0O0.OooO00o("AiControlMgr", "not enter karaoke app");
            if (KaraokeVoiceAppLogic.getInstance().isForceUsePackageName()) {
                value = KaraokeVoiceAppLogic.getInstance().getPackageName();
                oooO0O0.OooO00o("AiControlMgr", "ForceUsePackage, " + value);
            } else {
                value = cmd.getValue();
                oooO0O0.OooO00o("AiControlMgr", "extra," + value);
            }
            if (TextUtils.isEmpty(value)) {
                value = KaraokeVoiceAppLogic.getInstance().getPackageName();
                oooO0O0.OooO00o("AiControlMgr", "package name is empty, use dft," + value);
            }
        } else {
            value = "";
        }
        if (this.mKaraokeControl == null) {
            String msg3 = "karaokeControl is null, packageName:" + value;
            Intrinsics.checkNotNullParameter("AiControlMgr", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg3, "msg");
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "AiControlMgr -> " + msg3);
            }
            if (TextUtils.isEmpty(value)) {
                return;
            }
            if (Constants.PACKAGE_CHANGBA.equals(value)) {
                this.mKaraokeControl = new ChangbaKaraokeControl(this.mContext);
            } else {
                this.mKaraokeControl = new NormalKaraokeControl(this.mContext);
            }
            this.mKaraokeControl.setPackageName(value);
            return;
        }
        String msg4 = "karaokeControl not null, packageName:" + value + ", control.packageName:" + this.mKaraokeControl.getPackageName();
        Intrinsics.checkNotNullParameter("AiControlMgr", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg4, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "AiControlMgr -> " + msg4);
        }
        if (TextUtils.isEmpty(value) || value.equals(this.mKaraokeControl.getPackageName())) {
            return;
        }
        Intrinsics.checkNotNullParameter("AiControlMgr", Progress.TAG);
        Intrinsics.checkNotNullParameter("recreate karaokeControl", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "AiControlMgr -> recreate karaokeControl");
        }
        if (Constants.PACKAGE_CHANGBA.equals(value)) {
            this.mKaraokeControl = new ChangbaKaraokeControl(this.mContext);
        } else {
            this.mKaraokeControl = new NormalKaraokeControl(this.mContext);
        }
        this.mKaraokeControl.setPackageName(value);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int parseVolume(java.lang.String r7, java.lang.String r8) {
        /*
            r6 = this;
            com.loostone.libtuning.inf.extern.IVolumeAdjustment r0 = r6.volumeAdjustment
            int r0 = r0.getMicVolume()
            com.loostone.libtuning.inf.extern.IVolumeAdjustment r1 = r6.volumeAdjustment
            int r1 = r1.getAef1ReverbOutGain()
            boolean r2 = android.text.TextUtils.isEmpty(r8)
            java.lang.String r3 = "AdjustEcho"
            java.lang.String r4 = "AdjustMic"
            if (r2 == 0) goto L24
            boolean r2 = r4.equals(r7)
            if (r2 == 0) goto L1d
            return r0
        L1d:
            boolean r2 = r3.equals(r7)
            if (r2 == 0) goto L24
            return r1
        L24:
            java.lang.String r2 = "+"
            boolean r2 = r8.startsWith(r2)
            r5 = 0
            if (r2 == 0) goto L40
            boolean r8 = r4.equals(r7)
            if (r8 == 0) goto L37
            int r8 = r0 + 10
            goto L72
        L37:
            boolean r8 = r3.equals(r7)
            if (r8 == 0) goto L71
            int r8 = r1 + 10
            goto L72
        L40:
            java.lang.String r2 = "-"
            boolean r2 = r8.startsWith(r2)
            if (r2 == 0) goto L5b
            boolean r8 = r4.equals(r7)
            if (r8 == 0) goto L52
            int r8 = r0 + (-10)
            goto L72
        L52:
            boolean r8 = r3.equals(r7)
            if (r8 == 0) goto L71
            int r8 = r1 + (-10)
            goto L72
        L5b:
            int r8 = java.lang.Integer.parseInt(r8)     // Catch: java.lang.Exception -> L60
            goto L72
        L60:
            r8 = move-exception
            boolean r8 = r4.equals(r7)
            if (r8 == 0) goto L69
            r8 = r0
            goto L72
        L69:
            boolean r8 = r3.equals(r7)
            if (r8 == 0) goto L71
            r8 = r1
            goto L72
        L71:
            r8 = r5
        L72:
            boolean r2 = r4.equals(r7)
            r4 = 100
            if (r2 == 0) goto L7f
            if (r8 <= r4) goto L88
            r8 = r0
            goto L88
        L7f:
            boolean r7 = r3.equals(r7)
            if (r7 == 0) goto L88
            if (r8 <= r4) goto L88
            r8 = r1
        L88:
            if (r8 >= 0) goto L8b
            r8 = r5
        L8b:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.skyworth.old.ai.AiControlMgr.parseVolume(java.lang.String, java.lang.String):int");
    }

    public void create(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void destroy() {
        this.mContext = null;
        this.mKaraokeControl = null;
    }

    public int sendCmd(String str) {
        Cmd cmd;
        try {
            cmd = (Cmd) new Gson().fromJson(str, (Class<Object>) Cmd.class);
        } catch (Exception e) {
            e.printStackTrace();
            cmd = null;
        }
        char c = 65535;
        if (cmd == null) {
            Intrinsics.checkNotNullParameter("AiControlMgr", Progress.TAG);
            Intrinsics.checkNotNullParameter("sendCmd, cmd = null", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "AiControlMgr -> sendCmd, cmd = null");
            }
            return -1;
        }
        initKaraokeControl(cmd);
        String value = cmd.getValue();
        OooO0O0 oooO0O0 = OooO0O0.f225OooO00o;
        oooO0O0.OooO00o("AiControlMgr", "sendCmd, cmd:" + cmd.getCmd() + ", value:" + cmd.getValue());
        if (!AiCmd.ADJUST_MIC.equals(cmd.getCmd()) && !AiCmd.ADJUST_ECHO.equals(cmd.getCmd())) {
            if (this.mKaraokeControl == null) {
                return -1;
            }
            if (!AiControlFactory.getAiControl().isEnterApp() && !AiCmd.OPEN_APP.equals(cmd.getCmd()) && !AiCmd.SEARCH.equals(cmd.getCmd())) {
                oooO0O0.OooO00o("AiControlMgr", "send cmd, exit");
                return -1;
            }
            String cmd2 = cmd.getCmd();
            cmd2.hashCode();
            switch (cmd2.hashCode()) {
                case -1850657785:
                    if (cmd2.equals(AiCmd.REPLAY)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1822469688:
                    if (cmd2.equals(AiCmd.SEARCH)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1496068170:
                    if (cmd2.equals(AiCmd.ADJUST_MUSIC)) {
                        c = 2;
                        break;
                    }
                    break;
                case -536937088:
                    if (cmd2.equals(AiCmd.DELETE_SONG)) {
                        c = 3;
                        break;
                    }
                    break;
                case -509304930:
                    if (cmd2.equals(AiCmd.SWITCH_SCORE)) {
                        c = 4;
                        break;
                    }
                    break;
                case -506188927:
                    if (cmd2.equals(AiCmd.SWITCH_VOCAL)) {
                        c = 5;
                        break;
                    }
                    break;
                case -440128487:
                    if (cmd2.equals(AiCmd.OPEN_PAGE)) {
                        c = 6;
                        break;
                    }
                    break;
                case -325610892:
                    if (cmd2.equals(AiCmd.ADJUST_ECHO)) {
                        c = 7;
                        break;
                    }
                    break;
                case -325152319:
                    if (cmd2.equals(AiCmd.ADJUST_TONE)) {
                        c = '\b';
                        break;
                    }
                    break;
                case -287590376:
                    if (cmd2.equals(AiCmd.ADJUST_MIC)) {
                        c = '\t';
                        break;
                    }
                    break;
                case 2062599:
                    if (cmd2.equals(AiCmd.BACK)) {
                        c = '\n';
                        break;
                    }
                    break;
                case 2490196:
                    if (cmd2.equals(AiCmd.PLAY)) {
                        c = 11;
                        break;
                    }
                    break;
                case 2587682:
                    if (cmd2.equals(AiCmd.STOP)) {
                        c = '\f';
                        break;
                    }
                    break;
                case 76887510:
                    if (cmd2.equals(AiCmd.PAUSE)) {
                        c = '\r';
                        break;
                    }
                    break;
                case 401430359:
                    if (cmd2.equals(AiCmd.OPEN_APP)) {
                        c = 14;
                        break;
                    }
                    break;
                case 524750826:
                    if (cmd2.equals(AiCmd.TOP_SONG)) {
                        c = 15;
                        break;
                    }
                    break;
                case 1157440809:
                    if (cmd2.equals(AiCmd.CLOSE_APP)) {
                        c = 16;
                        break;
                    }
                    break;
                case 1345329074:
                    if (cmd2.equals(AiCmd.PRE_PAGE)) {
                        c = 17;
                        break;
                    }
                    break;
                case 1488917570:
                    if (cmd2.equals(AiCmd.NEXT_PAGE)) {
                        c = 18;
                        break;
                    }
                    break;
                case 1943221351:
                    if (cmd2.equals(AiCmd.PLAY_NEXT)) {
                        c = 19;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, replay");
                    this.mKaraokeControl.replay();
                    break;
                case 1:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, search");
                    if (getMicAvailable() == 1) {
                        if (AiControlFactory.getAiControl().isEnterApp() && this.mKaraokeControl.getPackageName() != null && this.mKaraokeControl.getPackageName().equals(AiControlFactory.getAiControl().getPackageName())) {
                            this.mKaraokeControl.search(cmd.getSong(), cmd.getSinger());
                        } else {
                            this.mKaraokeControl.openApp(cmd.getSong(), cmd.getSinger());
                        }
                    }
                    AiControlFactory.getAiControl().onMicExist(getMicAvailable());
                    break;
                case 2:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, adjust music");
                    adjustVolume(cmd.getCmd(), value, 10);
                    break;
                case 3:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, delete song");
                    this.mKaraokeControl.deleteSong(value);
                    break;
                case 4:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, switch score");
                    this.mKaraokeControl.switchScore(value);
                    break;
                case 5:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, switch vocal");
                    this.mKaraokeControl.switchVocal(value);
                    break;
                case 6:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, open page");
                    String page = cmd.getPage();
                    if (AiCmd.PAGE_TUNING.equals(page)) {
                        this.mKaraokeControl.openTuning();
                        break;
                    } else if (AiCmd.PAGE_SELECT_SONG.equals(page)) {
                        this.mKaraokeControl.openSelectedSong();
                        break;
                    }
                    break;
                case 7:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, adjust echo");
                    adjustVolume(cmd.getCmd(), value, 10);
                    break;
                case '\b':
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, adjust tone");
                    adjustVolume(cmd.getCmd(), value, 1);
                    break;
                case '\t':
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, adjust mic");
                    adjustVolume(cmd.getCmd(), value, 10);
                    break;
                case '\n':
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, back");
                    this.mKaraokeControl.back();
                    break;
                case 11:
                    if (TextUtils.isEmpty(value)) {
                        oooO0O0.OooO00o("AiControlMgr", "sendCmd, play");
                        this.mKaraokeControl.play();
                        break;
                    } else {
                        oooO0O0.OooO00o("AiControlMgr", "sendCmd, play, index:" + value);
                        try {
                            this.mKaraokeControl.play(Integer.parseInt(value));
                            break;
                        } catch (Exception e2) {
                            break;
                        }
                    }
                case '\f':
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, stop");
                    this.mKaraokeControl.stop();
                    break;
                case '\r':
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, pause");
                    this.mKaraokeControl.pause();
                    break;
                case 14:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, open app");
                    if (getMicAvailable() == 1) {
                        this.mKaraokeControl.openApp(null, null);
                    }
                    AiControlFactory.getAiControl().onMicExist(getMicAvailable());
                    break;
                case 15:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, top song");
                    this.mKaraokeControl.topSong(value);
                    break;
                case 16:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, close app");
                    this.mKaraokeControl.closeApp();
                    break;
                case 17:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, pre page");
                    this.mKaraokeControl.prePage();
                    break;
                case 18:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, next page");
                    this.mKaraokeControl.nextPage();
                    break;
                case 19:
                    oooO0O0.OooO00o("AiControlMgr", "sendCmd, play next");
                    this.mKaraokeControl.playNext();
                    break;
            }
            return 0;
        }
        AiCmd.ADJUST_MIC.equals(cmd.getCmd());
        adjustVolume(cmd.getCmd(), value, 10);
        return 0;
    }
}
