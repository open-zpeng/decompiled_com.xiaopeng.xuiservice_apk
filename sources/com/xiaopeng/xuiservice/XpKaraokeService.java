package com.xiaopeng.xuiservice;

import com.xiaopeng.xuimanager.karaoke.IKaraokeEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.innerutils.SpeechUtils;
/* loaded from: classes5.dex */
public class XpKaraokeService implements IXiaoPengMicVendor {
    public static final boolean DBG = true;
    public static final String TAG = "XpKaraokeService";
    private static IXiaoPengMicVendor mMicVendor;
    private static volatile XpKaraokeService xpKaraokeService;
    IKaraokeEventListener mMicSDKCallBack;

    private XpKaraokeService() {
        LogUtil.i(TAG, "XpKaraokeService init");
    }

    public static XpKaraokeService getInstance() {
        if (xpKaraokeService == null) {
            synchronized (XpKaraokeService.class) {
                if (xpKaraokeService == null) {
                    xpKaraokeService = new XpKaraokeService();
                }
            }
        }
        return xpKaraokeService;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int create(String pkgName, int flag, String midware) {
        LogUtil.i(TAG, "create() pkgName=" + pkgName + "  flag=" + flag + " midware=" + midware);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int destroy() {
        LogUtil.i(TAG, "destroy() ");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int registCallback(IKaraokeEventListener callBackFunc) {
        LogUtil.i(TAG, "registCallback()");
        this.mMicSDKCallBack = callBackFunc;
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int unRegistCallback() {
        LogUtil.i(TAG, "unRegistCallback()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getToken() {
        LogUtil.i(TAG, "getToken()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setSignedToken(String sToken) {
        LogUtil.i(TAG, "setSignedToken()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getHandShakeStatus() {
        LogUtil.d(TAG, "getHandShakeStatus ");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getMicStatus() {
        LogUtil.i(TAG, "getMicStatus() ");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getMicPowerStatus() {
        LogUtil.i(TAG, "getMicPowerStatus() ");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setVolume(int type, int vol) {
        LogUtil.i(TAG, "setVolume() type=" + type + " vol=" + vol);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getVolume(int type) {
        LogUtil.i(TAG, "getVolume() type=" + type);
        return 0;
    }

    public int setEcho(int echo) {
        LogUtil.i(TAG, "setEcho() echo=" + echo);
        return 0;
    }

    public int getEcho() {
        LogUtil.i(TAG, "getEcho()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackGetMinBuf(int sampleRate, int channel) {
        LogUtil.i(TAG, "trackGetMinBuf() sampleRate=" + sampleRate + " channel=" + channel);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackCreate(int sampleRate, int channel, int bufSize) {
        LogUtil.i(TAG, "trackCreate() sampleRate=" + sampleRate + " channel=" + channel + " bufSize=" + bufSize);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackGetLatency() {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int write(byte[] data, int off, int size) {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackGetAvail() {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackDestroy() {
        LogUtil.i(TAG, "trackDestroy()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int pause() {
        LogUtil.i(TAG, "pause()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recStop() {
        LogUtil.i(TAG, "recStop()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recStart() {
        LogUtil.i(TAG, "recStart()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int resume() {
        LogUtil.i(TAG, "resume()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int resumePlay() {
        LogUtil.i(TAG, "resumePlay()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int pausePlay() {
        LogUtil.i(TAG, "pausePlay()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int saveRec(int mode, String micPath, String mixPath) {
        LogUtil.i(TAG, "saveRec() mode" + mode + " micPath" + micPath + " mixPath" + mixPath);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int stopSaveRec() {
        LogUtil.i(TAG, "stopSaveRec()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recGetMinBuf(int sampleRate, int channel) {
        LogUtil.i(TAG, "recGetMinBuf() sampleRate=" + sampleRate + " channel=" + channel);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recCreate(int sampleRate, int channel, int bufSize) {
        LogUtil.i(TAG, "recCreate() sampleRate=" + sampleRate + " channel=" + channel + " bufSize" + bufSize);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recGetAvail() {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setRecMode(int mode) {
        LogUtil.i(TAG, "setRecMode() mode=" + mode);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int read(byte[] data, int size) {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recDestroy() {
        LogUtil.i(TAG, "recDestroy()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micGetAvail() {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micGetMinBuf(int sampleRate, int channel) {
        LogUtil.d(TAG, "micGetMinBuf " + sampleRate + " " + channel);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micCreate(int sampleRate, int channel, int bufSize) {
        LogUtil.i(TAG, "micCreate " + sampleRate + " " + channel + " " + bufSize);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micRead(byte[] data, int size) {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micDestroy() {
        LogUtil.i(TAG, "micDestroy");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public void setOutPutPath(boolean inCar, String productId) {
        LogUtil.d(TAG, "WMM_SetOutPutPath " + inCar + " " + productId);
    }

    public int aiWakeUp() {
        LogUtil.i(TAG, "aiWakeUp");
        SpeechUtils.getInstance().wakeupSpeech("changba");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setCommonData(int type, String message) {
        LogUtil.i(TAG, "setCommonData() type: " + type + ",message:" + message);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setBypassFilter(int audioSatus, boolean isKaraokeApp, boolean isResumed, boolean isAudioStackEmpty) {
        LogUtil.d(TAG, "setBypassFilter() ");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int requestKaraokeResource(String pkgName, boolean isAudioStackEmpty) {
        LogUtil.i(TAG, "requestKaraokeResource() ");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int releaseKaraokeResource(String pkgName) {
        LogUtil.i(TAG, "releaseKaraokeResource() ");
        return 0;
    }
}
