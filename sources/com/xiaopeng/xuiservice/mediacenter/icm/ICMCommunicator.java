package com.xiaopeng.xuiservice.mediacenter.icm;

import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class ICMCommunicator {
    private static final String TAG = "ICMCommunicator";
    private ICMHandler mICMHandler;

    public ICMCommunicator() {
        if (isLegacyIcmMediaSyncEnable()) {
            this.mICMHandler = new LegacyICMHandler();
        } else {
            this.mICMHandler = new NewIcmHandler();
        }
    }

    private boolean isLegacyIcmMediaSyncEnable() {
        return XUIConfig.getIcmType() == 0;
    }

    public void sendMediaInfoToIcm(final MediaInfo info, final int status, final long[] positions, final boolean withImage) {
        if (info != null) {
            if (info.isXpMusic() && !Config.SendXpMusicInfoToIcm) {
                return;
            }
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.icm.ICMCommunicator.1
                @Override // java.lang.Runnable
                public void run() {
                    ICMCommunicator.this.mICMHandler.sendIcmMediaInfoData(info, status, positions, withImage);
                }
            });
        }
    }
}
