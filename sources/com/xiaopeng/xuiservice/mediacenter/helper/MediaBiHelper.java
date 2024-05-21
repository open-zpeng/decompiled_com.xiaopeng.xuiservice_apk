package com.xiaopeng.xuiservice.mediacenter.helper;

import android.os.SystemClock;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes5.dex */
public class MediaBiHelper {
    private static final String MODULE_PLAY_USE = "appstore";
    private static final String PLAY_USE_DATA_ID = "B003";
    private static final String PLAY_USE_PAGE_ID = "P10317";
    private static final String TAG = "MediaBiHelper";
    private static MediaBiHelper mInstance;
    private Map<String, Long> mStartTimeMap = new HashMap();

    private MediaBiHelper() {
    }

    public static MediaBiHelper getInstance() {
        if (mInstance == null) {
            synchronized (MediaBiHelper.class) {
                if (mInstance == null) {
                    mInstance = new MediaBiHelper();
                }
            }
        }
        return mInstance;
    }

    public void vendorStartAudioSession(final int usage, final String pkgName) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.helper.MediaBiHelper.1
            @Override // java.lang.Runnable
            public void run() {
                if (usage == 1) {
                    long startTime = SystemClock.uptimeMillis();
                    MediaBiHelper.this.mStartTimeMap.put(pkgName, Long.valueOf(startTime));
                }
            }
        });
    }

    public void vendorStopAudioSession(final String pkgName) {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.helper.MediaBiHelper.2
            @Override // java.lang.Runnable
            public void run() {
                long endTime = SystemClock.uptimeMillis();
                if (MediaBiHelper.this.mStartTimeMap.containsKey(pkgName)) {
                    long startTime = ((Long) MediaBiHelper.this.mStartTimeMap.get(pkgName)).longValue();
                    MediaBiHelper.this.sendMediaPlayBiData(pkgName, startTime, endTime);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMediaPlayBiData(String pkgName, long startTime, long endTime) {
        LogUtil.d(TAG, "pkgName:" + pkgName + " startTime:" + startTime + " &endTime:" + endTime);
        BiLog bilog = BiLogFactory.create(MODULE_PLAY_USE, PLAY_USE_PAGE_ID, "B003");
        bilog.push("start_time", String.valueOf(startTime));
        bilog.push("end_time", String.valueOf(endTime));
        bilog.push(RecommendBean.SHOW_TIME_RESULT, pkgName);
        BiLogTransmit.getInstance().submit(bilog);
    }
}
