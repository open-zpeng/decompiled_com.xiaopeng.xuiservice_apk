package com.xiaopeng.xuiservice;

import com.xiaopeng.xuimanager.karaoke.IKaraokeEventListener;
/* loaded from: classes5.dex */
public interface IXiaoPengMicVendor {
    int create(String str, int i, String str2);

    int destroy();

    int getHandShakeStatus();

    int getMicPowerStatus();

    int getMicStatus();

    int getToken();

    int getVolume(int i);

    int micCreate(int i, int i2, int i3);

    int micDestroy();

    int micGetAvail();

    int micGetMinBuf(int i, int i2);

    int micRead(byte[] bArr, int i);

    int pause();

    int pausePlay();

    int read(byte[] bArr, int i);

    int recCreate(int i, int i2, int i3);

    int recDestroy();

    int recGetAvail();

    int recGetMinBuf(int i, int i2);

    int recStart();

    int recStop();

    int registCallback(IKaraokeEventListener iKaraokeEventListener);

    int releaseKaraokeResource(String str);

    int requestKaraokeResource(String str, boolean z);

    int resume();

    int resumePlay();

    int saveRec(int i, String str, String str2);

    int setBypassFilter(int i, boolean z, boolean z2, boolean z3);

    int setCommonData(int i, String str);

    void setOutPutPath(boolean z, String str);

    int setRecMode(int i);

    int setSignedToken(String str);

    int setVolume(int i, int i2);

    int stopSaveRec();

    int trackCreate(int i, int i2, int i3);

    int trackDestroy();

    int trackGetAvail();

    int trackGetLatency();

    int trackGetMinBuf(int i, int i2);

    int unRegistCallback();

    int write(byte[] bArr, int i, int i2);
}
