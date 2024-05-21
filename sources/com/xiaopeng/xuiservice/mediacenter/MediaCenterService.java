package com.xiaopeng.xuiservice.mediacenter;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.mediacenter.IAudioCaptureListener;
import com.xiaopeng.xuimanager.mediacenter.IBTStatusListener;
import com.xiaopeng.xuimanager.mediacenter.ILyricUpdateListener;
import com.xiaopeng.xuimanager.mediacenter.IMediaCenter;
import com.xiaopeng.xuimanager.mediacenter.IMediaCenterEventListener;
import com.xiaopeng.xuimanager.mediacenter.IModeChangedListener;
import com.xiaopeng.xuimanager.mediacenter.IPlaybackControlListener;
import com.xiaopeng.xuimanager.mediacenter.IPlaybackInfoListener;
import com.xiaopeng.xuimanager.mediacenter.IVisualizerViewEnableListener;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.AvrcpMeteData;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.IAvrcpEventListener;
import com.xiaopeng.xuimanager.mediacenter.lyric.ILyricInfoListener;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.mediacenter.utils.ParamsBuild;
import com.xiaopeng.xuimanager.mediacenter.visualizer.ISDVisualizerDataListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class MediaCenterService extends IMediaCenter.Stub implements XUIServiceBase, MediaCenterHalService.MediaCenterHalListener {
    public static final String TAG = "XUIManager.MEDIACENTER.MediaCenterService";
    private List<ListenerRecord<IPlaybackControlListener>> mControlListenerRecords = new ArrayList();
    private List<ListenerRecord<IAudioCaptureListener>> mAudioCaptureListenerRecords = new ArrayList();
    private List<ListenerRecord<IBTStatusListener>> mBtStatusListenerRecords = new ArrayList();
    private List<ListenerRecord<ILyricUpdateListener>> mLyricListenerRecords = new ArrayList();
    private List<ListenerRecord<IAvrcpEventListener>> mAvrcpListenerRecords = new ArrayList();
    private List<ListenerRecord<IModeChangedListener>> mModeListenerRecords = new ArrayList();
    private List<ListenerRecord<ILyricInfoListener>> mLyricInfoListenerRecords = new ArrayList();
    private List<PlaybackInfoListenerRecord> mPlaybackInfoListenerRecords = new ArrayList();
    private List<SDVisualizerListenerRecord> mSDVisualizerListenerRecords = new ArrayList();
    private MediaCenterHalService mMediaCenterHal = MediaCenterHalService.getInstance();

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("MediaCenterService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("MediaCenterService", info);
        }
    }

    public MediaCenterService(Context context) {
    }

    public void registerListener(IMediaCenterEventListener listener) throws RemoteException {
    }

    public void unregisterListener(IMediaCenterEventListener listener) throws RemoteException {
    }

    public void vendorRegister() throws RemoteException {
    }

    public void vendorUnRegister() throws RemoteException {
    }

    public void vendorUpdatePlaybackStatus(int status) throws RemoteException {
        LogUtil.i(TAG, "vendorUpdatePlaybackStatus status:" + status);
        this.mMediaCenterHal.vendorUpdatePlaybackStatus(status);
    }

    public void vendorUpdatePosition(long position, long duration) throws RemoteException {
        this.mMediaCenterHal.vendorUpdatePosition(position, duration);
    }

    public void vendorMediaInfoNotify(MediaInfo info) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("vendorMediaInfoNotify ");
        sb.append(info != null ? info.toString() : "");
        LogUtil.i(TAG, sb.toString());
        this.mMediaCenterHal.vendorMediaInfoNotify(info);
    }

    public void vendorSetControlListener(String pkgName, IPlaybackControlListener listener) throws RemoteException {
        LogUtil.d(TAG, "vendorSetControlListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mControlListenerRecords) {
            int index = findIndexOfListenerLocked(this.mControlListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "IPlaybackControlListener is already added, ignoring");
                return;
            }
            ListenerRecord record = new ListenerRecord(this.mControlListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mControlListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "PipeListener is dead, ignoring it");
            }
        }
    }

    public void vendorUnSetControlListener(String pkgName, IPlaybackControlListener listener) throws RemoteException {
        LogUtil.d(TAG, "vendorUnSetControlListener=" + listener);
        synchronized (this.mControlListenerRecords) {
            int index = findIndexOfListenerLocked(this.mControlListenerRecords, listener);
            if (index != -1) {
                ListenerRecord record = this.mControlListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public void vendorStartAudioSession(int audioSession, int usage, String pkgName) throws RemoteException {
        this.mMediaCenterHal.vendorStartAudioSession(audioSession, usage, pkgName);
    }

    public void vendorStopAudioSession(int audioSession, String pkgName) throws RemoteException {
        this.mMediaCenterHal.vendorStopAudioSession(audioSession, pkgName);
    }

    public void registerVisualizerListener(IAudioCaptureListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerVisualizerListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mAudioCaptureListenerRecords) {
            if (this.mAudioCaptureListenerRecords.isEmpty()) {
                this.mMediaCenterHal.startVisualizer();
            }
            int index = findIndexOfListenerLocked(this.mAudioCaptureListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "IAudioCaptureListener is already added, ignoring");
                return;
            }
            ListenerRecord record = new ListenerRecord(this.mAudioCaptureListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mAudioCaptureListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "PipeListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterVisualizerListener(IAudioCaptureListener listener) throws RemoteException {
        LogUtil.d(TAG, "unRegisterVisualizerListener=" + listener);
        synchronized (this.mAudioCaptureListenerRecords) {
            int index = findIndexOfListenerLocked(this.mAudioCaptureListenerRecords, listener);
            if (index != -1) {
                ListenerRecord record = this.mAudioCaptureListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
            if (this.mAudioCaptureListenerRecords.isEmpty()) {
                this.mMediaCenterHal.stopVisualizer();
            }
        }
    }

    public void registerVisualizerListenerWithDisplayId(int displayId, ISDVisualizerDataListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "registerVisualizerListenerWithDisplayId pid:" + pid + " &uid:" + uid + " &displayId:" + displayId);
        synchronized (this.mSDVisualizerListenerRecords) {
            int findTargetIndex = -1;
            int i = 0;
            while (true) {
                if (i >= this.mSDVisualizerListenerRecords.size()) {
                    break;
                }
                SDVisualizerListenerRecord record = this.mSDVisualizerListenerRecords.get(i);
                if (record.listener.asBinder() != listener.asBinder() || displayId != record.displayId) {
                    i++;
                } else {
                    findTargetIndex = i;
                    break;
                }
            }
            if (findTargetIndex == -1) {
                SDVisualizerListenerRecord newRecord = new SDVisualizerListenerRecord(displayId, "", listener, pid, uid);
                listener.asBinder().linkToDeath(newRecord, 0);
                this.mSDVisualizerListenerRecords.add(newRecord);
            }
            List<String> pkgNameList = new ArrayList<>();
            List<Integer> displayList = new ArrayList<>();
            for (SDVisualizerListenerRecord record2 : this.mSDVisualizerListenerRecords) {
                if (record2.displayId != -1) {
                    displayList.add(Integer.valueOf(record2.displayId));
                }
                if (!TextUtils.isEmpty(record2.pkgName)) {
                    pkgNameList.add(record2.pkgName);
                }
            }
            this.mMediaCenterHal.updateVisualizerRequest(displayList, pkgNameList);
        }
    }

    public void unRegisterVisualizerListenerWithDisplayId(int displayId, ISDVisualizerDataListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "unRegisterVisualizerListenerWithDisplayId pid:" + pid + " &uid:" + uid + " &displayId:" + displayId);
        synchronized (this.mSDVisualizerListenerRecords) {
            int findTargetIndex = -1;
            int i = 0;
            while (true) {
                if (i >= this.mSDVisualizerListenerRecords.size()) {
                    break;
                }
                SDVisualizerListenerRecord record = this.mSDVisualizerListenerRecords.get(i);
                if (record.listener.asBinder() != listener.asBinder() || displayId != record.displayId) {
                    i++;
                } else {
                    findTargetIndex = i;
                    break;
                }
            }
            if (findTargetIndex > -1) {
                this.mSDVisualizerListenerRecords.remove(findTargetIndex);
            }
            List<String> pkgNameList = new ArrayList<>();
            List<Integer> displayList = new ArrayList<>();
            for (SDVisualizerListenerRecord record2 : this.mSDVisualizerListenerRecords) {
                if (record2.displayId != -1) {
                    displayList.add(Integer.valueOf(record2.displayId));
                }
                if (!TextUtils.isEmpty(record2.pkgName)) {
                    pkgNameList.add(record2.pkgName);
                }
            }
            this.mMediaCenterHal.updateVisualizerRequest(displayList, pkgNameList);
        }
    }

    public void registerVisualizerListenerWithPackage(String pkgName, ISDVisualizerDataListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "registerVisualizerListenerWithDisplayId pid:" + pid + " &uid:" + uid + " &pkgName:" + pkgName);
        synchronized (this.mSDVisualizerListenerRecords) {
            int findTargetIndex = -1;
            int i = 0;
            while (true) {
                if (i >= this.mSDVisualizerListenerRecords.size()) {
                    break;
                }
                SDVisualizerListenerRecord record = this.mSDVisualizerListenerRecords.get(i);
                if (record.listener.asBinder() != listener.asBinder() || !pkgName.equals(record.pkgName)) {
                    i++;
                } else {
                    findTargetIndex = i;
                    break;
                }
            }
            if (findTargetIndex == -1) {
                SDVisualizerListenerRecord newRecord = new SDVisualizerListenerRecord(-1, pkgName, listener, pid, uid);
                listener.asBinder().linkToDeath(newRecord, 0);
                this.mSDVisualizerListenerRecords.add(newRecord);
            }
            List<String> pkgNameList = new ArrayList<>();
            List<Integer> displayList = new ArrayList<>();
            for (SDVisualizerListenerRecord record2 : this.mSDVisualizerListenerRecords) {
                if (record2.displayId != -1) {
                    displayList.add(Integer.valueOf(record2.displayId));
                }
                if (!TextUtils.isEmpty(record2.pkgName)) {
                    pkgNameList.add(record2.pkgName);
                }
            }
            this.mMediaCenterHal.updateVisualizerRequest(displayList, pkgNameList);
        }
    }

    public void unRegisterVisualizerListenerWithPackage(String pkgName, ISDVisualizerDataListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "unRegisterVisualizerListenerWithPackage pid:" + pid + " &uid:" + uid + " &pkgName:" + pkgName);
        synchronized (this.mSDVisualizerListenerRecords) {
            int findTargetIndex = -1;
            int i = 0;
            while (true) {
                if (i >= this.mSDVisualizerListenerRecords.size()) {
                    break;
                }
                SDVisualizerListenerRecord record = this.mSDVisualizerListenerRecords.get(i);
                if (record.listener.asBinder() != listener.asBinder() || !pkgName.equals(record.pkgName)) {
                    i++;
                } else {
                    findTargetIndex = i;
                    break;
                }
            }
            if (findTargetIndex > -1) {
                this.mSDVisualizerListenerRecords.remove(findTargetIndex);
            }
            List<String> pkgNameList = new ArrayList<>();
            List<Integer> displayList = new ArrayList<>();
            for (SDVisualizerListenerRecord record2 : this.mSDVisualizerListenerRecords) {
                if (record2.displayId != -1) {
                    displayList.add(Integer.valueOf(record2.displayId));
                }
                if (!TextUtils.isEmpty(record2.pkgName)) {
                    pkgNameList.add(record2.pkgName);
                }
            }
            if (this.mSDVisualizerListenerRecords.isEmpty()) {
                this.mMediaCenterHal.stopVisualizer();
            }
            this.mMediaCenterHal.updateVisualizerRequest(displayList, pkgNameList);
        }
    }

    public void registerPlaybackInfoListener(IPlaybackInfoListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerVisualizerListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mPlaybackInfoListenerRecords) {
            int index = findIndexOfPlaybackInfoListenerLocked(listener);
            if (index != -1) {
                LogUtil.w(TAG, "IPlaybackInfoListener is already added, ignoring");
                return;
            }
            PlaybackInfoListenerRecord record = new PlaybackInfoListenerRecord(listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mPlaybackInfoListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "IPlaybackInfoListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterPlaybackInfoListener(IPlaybackInfoListener listener) throws RemoteException {
        LogUtil.d(TAG, "unRegisterPlaybackInfoListener=" + listener);
        synchronized (this.mPlaybackInfoListenerRecords) {
            int index = findIndexOfPlaybackInfoListenerLocked(listener);
            if (index != -1) {
                PlaybackInfoListenerRecord record = this.mPlaybackInfoListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public int playbackControl(int displayId, int cmd, int param) throws RemoteException {
        return this.mMediaCenterHal.playbackControl(displayId, cmd, param);
    }

    public int switchSource(int source) throws RemoteException {
        return this.mMediaCenterHal.switchSource(source);
    }

    public int getCurrentPlayStatus(int displayId) throws RemoteException {
        return this.mMediaCenterHal.getCurrentPlayStatus(displayId);
    }

    public MediaInfo getCurrentMediaInfo(int displayId) throws RemoteException {
        return this.mMediaCenterHal.getCurrentMediaInfo(displayId);
    }

    public LyricInfo getCurrentLyricInfo(int displayId) throws RemoteException {
        return this.mMediaCenterHal.getCurrentLyricInfo(displayId);
    }

    public long[] getCurrentPosition(int displayId) throws RemoteException {
        return this.mMediaCenterHal.getCurrentPosition(displayId);
    }

    public void setVisualizerViewEnable(boolean enable) throws RemoteException {
        this.mMediaCenterHal.setVisualizerViewEnable(enable);
    }

    public void registerVisualizerViewEnableListener(IVisualizerViewEnableListener listener) throws RemoteException {
    }

    public void unRegisterVisualizerViewEnableListener(IVisualizerViewEnableListener listener) throws RemoteException {
    }

    public void notifyLyricUpdate(String lyric) throws RemoteException {
    }

    public void registerLyricUpdateListener(ILyricUpdateListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerLyricUpdateListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mLyricListenerRecords) {
            int index = findIndexOfListenerLocked(this.mLyricListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "ILyricUpdateListener is already added, ignoring");
                return;
            }
            ListenerRecord<ILyricUpdateListener> record = new ListenerRecord<>(this.mLyricListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mLyricListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "IPlaybackInfoListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterLyricUpdateListener(ILyricUpdateListener listener) throws RemoteException {
        LogUtil.d(TAG, "unRegisterLyricUpdateListener=" + listener);
        synchronized (this.mLyricListenerRecords) {
            int index = findIndexOfListenerLocked(this.mLyricListenerRecords, listener);
            if (index != -1) {
                ListenerRecord<ILyricUpdateListener> record = this.mLyricListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public void registerModeChangedListener(IModeChangedListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerModeChangedListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mModeListenerRecords) {
            int index = findIndexOfListenerLocked(this.mModeListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "IModeChangedListener is already added, ignoring");
                return;
            }
            ListenerRecord<IModeChangedListener> record = new ListenerRecord<>(this.mModeListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mModeListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "IPlaybackInfoListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterModeChangedListener(IModeChangedListener listener) throws RemoteException {
        LogUtil.d(TAG, "unRegisterModeChangedListener=" + listener);
        synchronized (this.mModeListenerRecords) {
            int index = findIndexOfListenerLocked(this.mModeListenerRecords, listener);
            if (index != -1) {
                ListenerRecord<IModeChangedListener> record = this.mModeListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public int getCurrentMode() throws RemoteException {
        return this.mMediaCenterHal.getCurrentMode();
    }

    public void registerBtStatusListener(IBTStatusListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerBtStatusListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mBtStatusListenerRecords) {
            int index = findIndexOfListenerLocked(this.mBtStatusListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "IBTStatusListener is already added, ignoring");
                return;
            }
            ListenerRecord<IBTStatusListener> record = new ListenerRecord<>(this.mBtStatusListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mBtStatusListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "IPlaybackInfoListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterBtStatusListener(IBTStatusListener listener) throws RemoteException {
        LogUtil.d(TAG, "unRegisterBtStatusListener=" + listener);
        synchronized (this.mBtStatusListenerRecords) {
            int index = findIndexOfListenerLocked(this.mBtStatusListenerRecords, listener);
            if (index != -1) {
                ListenerRecord<IBTStatusListener> record = this.mBtStatusListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public void playBtMedia() throws RemoteException {
        int callingPid = Binder.getCallingPid();
        LogUtil.d(TAG, "playBtMedia with callingPid:" + callingPid);
        this.mMediaCenterHal.playBtMedia(callingPid);
    }

    public void pauseBtMedia() throws RemoteException {
        int callingPid = Binder.getCallingPid();
        LogUtil.d(TAG, "pauseBtMedia with callingPid:" + callingPid);
        this.mMediaCenterHal.pauseBtMedia();
    }

    public int getBtStatus() throws RemoteException {
        return this.mMediaCenterHal.getBtStatus();
    }

    public int setFavorite(boolean favorite, String id) throws RemoteException {
        return this.mMediaCenterHal.setFavorite(favorite, id);
    }

    public void favorSong(Bundle extras) throws RemoteException {
        this.mMediaCenterHal.favorSong(extras);
    }

    public void requestMediaButton(boolean request, Bundle extras) throws RemoteException {
        int callingPid = Binder.getCallingPid();
        this.mMediaCenterHal.requestMediaButton(callingPid, request);
    }

    public void setBtVolume(float volume) throws RemoteException {
        int callingPid = Binder.getCallingPid();
        this.mMediaCenterHal.setBtVolume(callingPid, volume);
    }

    public void avrcpPlay() throws RemoteException {
        this.mMediaCenterHal.avrcpPlay();
    }

    public void avrcpPause() throws RemoteException {
        this.mMediaCenterHal.avrcpPause();
    }

    public void avrcpNext() throws RemoteException {
        this.mMediaCenterHal.avrcpNext();
    }

    public void avrcpPrevious() throws RemoteException {
        this.mMediaCenterHal.avrcpPrevious();
    }

    public AvrcpMeteData getAvrcpMeteData() throws RemoteException {
        return this.mMediaCenterHal.getAvrcpMeteData();
    }

    public long[] getAvrcpPosition() throws RemoteException {
        return this.mMediaCenterHal.getAvrcpPosition();
    }

    public int getAvrcpPlayStatus() throws RemoteException {
        return this.mMediaCenterHal.getAvrcpPlayStatus();
    }

    public void registerAvrcpEventListener(IAvrcpEventListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerAvrcpEventListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mAvrcpListenerRecords) {
            int index = findIndexOfListenerLocked(this.mAvrcpListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "IAvrcpEventListener is already added, ignoring");
                return;
            }
            ListenerRecord<IAvrcpEventListener> record = new ListenerRecord<>(this.mAvrcpListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mAvrcpListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "IAvrcpEventListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterAvrcpEventListener(IAvrcpEventListener listener) throws RemoteException {
        LogUtil.d(TAG, "unRegisterAvrcpEventListener=" + listener);
        synchronized (this.mAvrcpListenerRecords) {
            int index = findIndexOfListenerLocked(this.mAvrcpListenerRecords, listener);
            if (index != -1) {
                ListenerRecord<IAvrcpEventListener> record = this.mAvrcpListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public void executeCmd(String cmd, String params) throws RemoteException {
        char c;
        int pid = Binder.getCallingPid();
        int hashCode = cmd.hashCode();
        if (hashCode != -359886371) {
            if (hashCode == 1576019130 && cmd.equals("add_monitor_displayId")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (cmd.equals("remove_monitor_displayId")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            for (PlaybackInfoListenerRecord record : this.mPlaybackInfoListenerRecords) {
                if (record.pid == pid) {
                    record.addDisplayId(ParamsBuild.getDisplayId(params));
                }
            }
        } else if (c == 1) {
            for (PlaybackInfoListenerRecord record2 : this.mPlaybackInfoListenerRecords) {
                if (record2.pid == pid) {
                    record2.removeDisplayId(ParamsBuild.getDisplayId(params));
                }
            }
        }
    }

    public void setSDPosition(int displayId, String packageName) throws RemoteException {
        int pid = Binder.getCallingPid();
        LogUtil.d(TAG, "pid:" + pid + " set package:" + packageName + " to displayId:" + displayId);
        this.mMediaCenterHal.setSDPosition(displayId, packageName);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        this.mMediaCenterHal.setListener(this);
        this.mMediaCenterHal.init();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    private <T extends IInterface> int findIndexOfListenerLocked(List<ListenerRecord<T>> listenerRecords, T listener) {
        for (int i = listenerRecords.size() - 1; i >= 0; i--) {
            if (listenerRecords.get(i).listener.asBinder() == listener.asBinder()) {
                return i;
            }
        }
        return -1;
    }

    private int findIndexOfPlaybackInfoListenerLocked(IPlaybackInfoListener listener) {
        for (int i = this.mPlaybackInfoListenerRecords.size() - 1; i >= 0; i--) {
            if (this.mPlaybackInfoListenerRecords.get(i).listener.asBinder() == listener.asBinder()) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class ListenerRecord<T extends IInterface> implements IBinder.DeathRecipient {
        public final T listener;
        public final List<ListenerRecord<T>> listenerList;
        public final int pid;
        public final int uid;

        ListenerRecord(List<ListenerRecord<T>> listenerList, T listener, int pid, int uid) {
            this.listenerList = listenerList;
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (this.listenerList) {
                LogUtil.w(MediaCenterService.TAG, this.listener.getClass() + " binder died,pid=" + this.pid);
                this.listenerList.remove(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class PlaybackInfoListenerRecord implements IBinder.DeathRecipient {
        public List<Integer> displayIdList = new ArrayList();
        public final IPlaybackInfoListener listener;
        public final int pid;
        public final int uid;

        public PlaybackInfoListenerRecord(IPlaybackInfoListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (MediaCenterService.this.mPlaybackInfoListenerRecords) {
                LogUtil.w(MediaCenterService.TAG, this.listener.getClass() + " binder died,pid=" + this.pid);
                MediaCenterService.this.mPlaybackInfoListenerRecords.remove(this);
            }
        }

        public void addDisplayId(int displayId) {
            synchronized (this.displayIdList) {
                if (!this.displayIdList.contains(Integer.valueOf(displayId))) {
                    this.displayIdList.add(Integer.valueOf(displayId));
                }
            }
        }

        public void removeDisplayId(int displayId) {
            synchronized (this.displayIdList) {
                if (this.displayIdList.contains(Integer.valueOf(displayId))) {
                    this.displayIdList.remove(displayId);
                }
            }
        }

        public void onMediaInfoChanged(int displayId, MediaInfo mediaInfo) {
            List<Integer> list = this.displayIdList;
            if (list != null && !list.isEmpty() && this.displayIdList.contains(Integer.valueOf(displayId))) {
                try {
                    this.listener.OnMediaInfoNotify(displayId, mediaInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onPlayStatusChanged(int displayId, int playStatus) {
            List<Integer> list = this.displayIdList;
            if (list != null && !list.isEmpty() && this.displayIdList.contains(Integer.valueOf(displayId))) {
                try {
                    this.listener.OnPlaybackChanged(displayId, playStatus);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onPositionChanged(int displayId, long[] position) {
            List<Integer> list = this.displayIdList;
            if (list != null && !list.isEmpty() && this.displayIdList.contains(Integer.valueOf(displayId))) {
                try {
                    this.listener.OnUpdatePosition(displayId, position[0], position[1]);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onMediaInfoChanged(int displayId, MediaInfo mediaInfo) {
        notifyClientMediaInfoChanged(displayId, mediaInfo);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onPositionChanged(int displayId, long[] position) {
        notifyClientPositionChanged(displayId, position);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onPlayStatusChanged(int displayId, int status) {
        notifyClientStatusChanged(displayId, status);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onFftDataCapture(byte[] fft, int samplingRate) {
        notifyClientFftData(fft, samplingRate);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onRatioData(float ratio, float minRatio) {
        notifyClientRatioData(ratio, minRatio);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onFftDataCapture(int display, String pkgName, byte[] fft, int samplingRate) {
        notifyClientFftData(display, pkgName, fft, samplingRate);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onRatioData(int displayId, String pkgName, float ratio, float minRatio) {
        notifyClientRatioData(displayId, pkgName, ratio, minRatio);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onPlayControlIpc(int cmd, int params) {
        playControlListeners(cmd, params);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onSetFavorite(boolean favorite, String id) {
        setFavoriteListeners(favorite, id);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onSwitchSource(int source) {
        switchSourceListeners(source);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onBtStatusChanged(int status) {
        notifyBtStatusListener(status);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.MediaCenterHalListener
    public void onLyricUpdated(int displayId, LyricInfo info) {
        notifyClientLyricUpdated(displayId, info);
    }

    public void notifyLyricInfoUpdated(LyricInfo info) throws RemoteException {
        LogUtil.i(TAG, "notifyLyricInfoUpdated:" + info.toString());
        this.mMediaCenterHal.vendorNotifyUpdateLyric(info);
    }

    public void registerLyricInfoListener(ILyricInfoListener listener) throws RemoteException {
        LogUtil.d(TAG, "registerLyricInfoListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mLyricInfoListenerRecords) {
            int index = findIndexOfListenerLocked(this.mLyricInfoListenerRecords, listener);
            if (index != -1) {
                LogUtil.w(TAG, "ILyricInfoListener is already added, ignoring");
                return;
            }
            ListenerRecord<ILyricInfoListener> record = new ListenerRecord<>(this.mLyricInfoListenerRecords, listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mLyricInfoListenerRecords.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "ILyricInfoListener is dead, ignoring it");
            }
        }
    }

    public void unregisterLyricInfoListener(ILyricInfoListener listener) throws RemoteException {
        LogUtil.d(TAG, "unregisterLyricInfoListener=" + listener);
        synchronized (this.mLyricInfoListenerRecords) {
            int index = findIndexOfListenerLocked(this.mLyricInfoListenerRecords, listener);
            if (index != -1) {
                ListenerRecord<ILyricInfoListener> record = this.mLyricInfoListenerRecords.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    private void notifyClientStatusChanged(int displayId, int status) {
        LogUtil.i(TAG, "notifyClientStatusChanged displayId:" + displayId + " &status:" + status);
        synchronized (this.mPlaybackInfoListenerRecords) {
            if (!this.mPlaybackInfoListenerRecords.isEmpty()) {
                for (PlaybackInfoListenerRecord listenerRecord : this.mPlaybackInfoListenerRecords) {
                    listenerRecord.onPlayStatusChanged(displayId, status);
                }
            }
        }
    }

    private void notifyClientPositionChanged(int displayId, long[] position) {
        LogUtil.d(TAG, "notifyClientPositionChanged displayId:" + displayId + " &position:" + position[0] + " &duration:" + position[1]);
        synchronized (this.mPlaybackInfoListenerRecords) {
            if (!this.mPlaybackInfoListenerRecords.isEmpty()) {
                for (PlaybackInfoListenerRecord listenerRecord : this.mPlaybackInfoListenerRecords) {
                    listenerRecord.onPositionChanged(displayId, position);
                }
            }
        }
    }

    private void notifyClientMediaInfoChanged(int displayId, MediaInfo mediaInfo) {
        LogUtil.i(TAG, "notifyClientMediaInfoChanged displayId:" + displayId + " &info:" + mediaInfo.toString());
        synchronized (this.mPlaybackInfoListenerRecords) {
            if (!this.mPlaybackInfoListenerRecords.isEmpty()) {
                for (PlaybackInfoListenerRecord listenerRecord : this.mPlaybackInfoListenerRecords) {
                    listenerRecord.onMediaInfoChanged(displayId, mediaInfo);
                }
            }
        }
    }

    private void notifyClientFftData(byte[] fft, int samplingRate) {
        synchronized (this.mAudioCaptureListenerRecords) {
            for (ListenerRecord<IAudioCaptureListener> listenerRecord : this.mAudioCaptureListenerRecords) {
                try {
                    listenerRecord.listener.OnFftDataCapture(fft, samplingRate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyClientFftData(int displayId, String pkgName, byte[] fft, int samplingRate) {
        synchronized (this.mSDVisualizerListenerRecords) {
            for (SDVisualizerListenerRecord record : this.mSDVisualizerListenerRecords) {
                record.onFftDataCapture(displayId, pkgName, fft, samplingRate);
            }
        }
    }

    private void notifyClientRatioData(float ratio, float minRatio) {
        synchronized (this.mAudioCaptureListenerRecords) {
            for (ListenerRecord<IAudioCaptureListener> listenerRecord : this.mAudioCaptureListenerRecords) {
                try {
                    listenerRecord.listener.OnRatioData(ratio, minRatio);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyClientRatioData(int displayId, String pkgName, float ratio, float minRatio) {
        synchronized (this.mSDVisualizerListenerRecords) {
            for (SDVisualizerListenerRecord record : this.mSDVisualizerListenerRecords) {
                record.onRatioData(displayId, pkgName, ratio, minRatio);
            }
        }
    }

    private void playControlListeners(int cmd, int params) {
        LogUtil.d(TAG, "playControlListeners cmd:" + cmd + " &params:" + params);
        synchronized (this.mControlListenerRecords) {
            for (ListenerRecord<IPlaybackControlListener> listenerRecord : this.mControlListenerRecords) {
                try {
                    listenerRecord.listener.OnPlaybackControl(cmd, params);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void switchSourceListeners(int source) {
        LogUtil.d(TAG, "switchSourceListeners source:" + source);
        synchronized (this.mControlListenerRecords) {
            for (ListenerRecord<IPlaybackControlListener> listenerRecord : this.mControlListenerRecords) {
                try {
                    listenerRecord.listener.OnSwitchSource(source);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setFavoriteListeners(boolean favorite, String id) {
        synchronized (this.mControlListenerRecords) {
            for (ListenerRecord<IPlaybackControlListener> listenerRecord : this.mControlListenerRecords) {
                try {
                    listenerRecord.listener.OnSetFavorite(favorite, id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyBtStatusListener(int status) {
        synchronized (this.mBtStatusListenerRecords) {
            for (ListenerRecord<IBTStatusListener> listenerRecord : this.mBtStatusListenerRecords) {
                try {
                    listenerRecord.listener.onBtStatusChanged(status);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyClientLyricUpdated(int displayId, LyricInfo lyricInfo) {
        if (lyricInfo == null) {
            return;
        }
        LogUtil.i(TAG, "notifyClientLyricUpdated displayId:" + displayId + " &lyricInfo:" + lyricInfo.toString());
        synchronized (this.mLyricInfoListenerRecords) {
            LogUtil.d(TAG, "notifyClientLyricUpdated size: " + this.mLyricInfoListenerRecords.size());
            for (ListenerRecord<ILyricInfoListener> listenerRecord : this.mLyricInfoListenerRecords) {
                try {
                    listenerRecord.listener.onLyricInfoUpdated(displayId, lyricInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class SDVisualizerListenerRecord implements IBinder.DeathRecipient {
        public final int displayId;
        public final ISDVisualizerDataListener listener;
        public final int pid;
        public final String pkgName;
        public final int uid;

        SDVisualizerListenerRecord(int displayId, String pkgName, ISDVisualizerDataListener listener, int pid, int uid) {
            this.displayId = displayId;
            this.pkgName = pkgName;
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        public void onFftDataCapture(int display, String packageName, byte[] fft, int samplingRate) {
            if (this.displayId == display || this.pkgName.equals(packageName)) {
                try {
                    this.listener.OnFftDataCapture(display, packageName, fft, samplingRate);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onRatioData(int display, String packageName, float ratio, float minRatio) {
            if (this.displayId == display || this.pkgName.equals(packageName)) {
                try {
                    this.listener.OnRatioData(display, packageName, ratio, minRatio);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (MediaCenterService.this.mSDVisualizerListenerRecords) {
                MediaCenterService.this.mSDVisualizerListenerRecords.remove(this);
            }
        }
    }
}
