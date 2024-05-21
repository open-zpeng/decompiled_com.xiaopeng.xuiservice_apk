package com.xiaopeng.xuiservice.xapp.karaoke.handler;

import android.app.ActivityThread;
import com.thunder.voiceinterface.OnVoiceBindListener;
import com.thunder.voiceinterface.VoiceServiceManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.Constants;
/* loaded from: classes5.dex */
public class ThunderKaraokeHandler implements IKaraokeHandler {
    private static final String TAG = "ThunderKaraokeHandler";
    private boolean mServiceBindSuccess = false;

    @Override // com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler
    public void searchSong(int displayId, final String artist, final String song) {
        SharedDisplayManager.getInstance().setSharedId(getPackageName(), displayId);
        boolean isConnected = VoiceServiceManager.getInstance().isConnected();
        if (isConnected) {
            VoiceServiceManager.getInstance().searchSong(artist, song);
        } else {
            VoiceServiceManager.getInstance().bindServer(ActivityThread.currentActivityThread().getApplication(), new OnVoiceBindListener() { // from class: com.xiaopeng.xuiservice.xapp.karaoke.handler.ThunderKaraokeHandler.1
                @Override // com.thunder.voiceinterface.OnVoiceBindListener
                public void onBindCompleted() {
                    VoiceServiceManager.getInstance().searchSong(artist, song);
                }
            });
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler
    public String getPackageName() {
        return Constants.PACKAGE_THUNDER;
    }
}
