package com.xiaopeng.xuiservice.mediacenter.visualizer;

import android.app.AppGlobals;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.audio.xpAudioSessionInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes5.dex */
public class SessionUtil {
    private static final Comparator<xpAudioSessionInfo> mSortComparator = new Comparator<xpAudioSessionInfo>() { // from class: com.xiaopeng.xuiservice.mediacenter.visualizer.SessionUtil.1
        @Override // java.util.Comparator
        public int compare(xpAudioSessionInfo a, xpAudioSessionInfo b) {
            return Long.compare(a.getStartTime(), b.getStartTime());
        }
    };

    public static List<Integer> getRestoreSessionList(List<xpAudioSessionInfo> infoList) {
        List<Integer> result = new CopyOnWriteArrayList<>();
        if (infoList != null && infoList.size() > 0) {
            Iterator<xpAudioSessionInfo> iterator = infoList.iterator();
            while (iterator.hasNext()) {
                xpAudioSessionInfo info = iterator.next();
                if (!checkVisualizerEnable(info.getPackageName())) {
                    iterator.remove();
                }
            }
            if (infoList.size() > 1) {
                Collections.sort(infoList, mSortComparator);
            }
            if (infoList.size() > 0) {
                for (xpAudioSessionInfo xpAudioSessionInfo : infoList) {
                    result.add(Integer.valueOf(xpAudioSessionInfo.getSessionId()));
                }
            }
        }
        return result;
    }

    public static List<xpAudioSessionInfo> getRestoreSession(List<xpAudioSessionInfo> infoList, int displayId) {
        List<xpAudioSessionInfo> result = new CopyOnWriteArrayList<>();
        if (infoList != null && infoList.size() > 0) {
            Iterator<xpAudioSessionInfo> iterator = infoList.iterator();
            while (iterator.hasNext()) {
                xpAudioSessionInfo info = iterator.next();
                LogUtil.d("SessionUtil", "getRestoreSession info pkgName:" + info.getPackageName() + " &sessionId:" + info.getSessionId());
                if (info.getPackageName().contains(":")) {
                    String[] splitArray = info.getPackageName().split(":");
                    info.setPackageName(splitArray[0]);
                }
                if (!checkVisualizerEnable(info.getPackageName()) || !isDisplayMatch(info.getPackageName(), displayId)) {
                    iterator.remove();
                }
            }
            if (infoList.size() > 1) {
                Collections.sort(infoList, mSortComparator);
            }
        }
        result.addAll(infoList);
        return result;
    }

    private static boolean isDisplayMatch(String pkgName, int displayId) {
        if (SharedDisplayManager.hasSharedDisplayFeature() && PassengerBluetoothManager.getInstance().isDeviceConnected() && SharedDisplayManager.getInstance().getSharedId(pkgName) != displayId) {
            return false;
        }
        return true;
    }

    private static boolean checkVisualizerEnable(String packageName) {
        try {
            xpPackageInfo info = AppGlobals.getPackageManager().getXpPackageInfo(packageName);
            if (info != null) {
                if (info.enableVisualizer == 0) {
                    return false;
                }
                return true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
