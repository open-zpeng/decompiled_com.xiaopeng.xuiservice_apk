package com.xiaopeng.xuiservice.xapp.mode;

import android.app.ActivityManager;
import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Handler;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class XAppAudioFocusModeEngine extends XAppBaseModeEngine {
    private static final String PACKAGE_KYBC = "com.aligames.kuang.kybc.aligames";
    private static final String TAG = "XAppNavigationModeEngine";
    private ActivityManager mActivityManager;
    AudioManager.OnAudioFocusChangeListener mAudioFocusListener;
    private AudioFocusRequest mAudioFocusRequest;
    private AudioManager mAudioManager;

    public XAppAudioFocusModeEngine(Context context, Handler handler) {
        super(context, handler);
        this.mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.XAppAudioFocusModeEngine.1
            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int focusChange) {
            }
        };
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
            this.mAudioFocusRequest = new AudioFocusRequest.Builder(2).setOnAudioFocusChangeListener(this.mAudioFocusListener).build();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected synchronized void handleAppInfoChange() {
        requireOrReleaseAudioFocus();
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected boolean isCurrentModeEnable() {
        if (PACKAGE_KYBC.equals(this.mPkgName)) {
            return true;
        }
        return this.mPackageInfo != null && this.mPackageInfo.requestAudioFocus == 1;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    private void requireOrReleaseAudioFocus() {
        if (this.mAudioManager == null) {
            LogUtil.d(TAG, "mAudioManager is null");
        } else if (isCurrentModeEnable()) {
            LogUtil.d(TAG, "require audio focus");
            this.mAudioManager.requestAudioFocus(this.mAudioFocusRequest);
        } else {
            this.mAudioManager.abandonAudioFocusRequest(this.mAudioFocusRequest);
        }
    }
}
