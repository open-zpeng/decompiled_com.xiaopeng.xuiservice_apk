package com.xiaopeng.xuiservice.smart;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.karaoke.KaraokeManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.Arrays;
/* loaded from: classes5.dex */
public class SmartRecordService extends BaseSmartService {
    private static final int MIC_POWER_OFF = 6;
    private static final int MIC_POWER_ON = 5;
    private static final int RECORD_START = 1;
    private static final int RECORD_STOP = 0;
    private static final String TAG = "SmartRecordService";
    private static final int UDB_DONGLE_OFF = 4;
    private static final int UDB_DONGLE_ON = 3;
    private static MicServiceCallBack mMicCallback;
    private static KaraokeManager mMicManager;
    private RecordHandlerThread mRecordHandlerThread;
    private static boolean mDongleStatus = false;
    private static boolean mMicStatus = false;
    private static int mRecordStatus = 0;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private void setProviderRecorder(int status) {
        LogUtil.d(TAG, "setProviderRecorder " + status);
        Settings.System.putInt(this.mContext.getContentResolver(), "mic_record", status);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getProviderRecorder() {
        int status = 0;
        try {
            status = Settings.System.getInt(this.mContext.getContentResolver(), "mic_record");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, "getProviderRecorder " + status);
        return status;
    }

    public void handleRecorderEvent(int status) {
        LogUtil.d(TAG, "handleRecorderEvent status=" + status + ", mRecordStatus=" + mRecordStatus);
        if (mMicManager != null && mRecordStatus != status) {
            mRecordStatus = status;
            Message message = this.mRecordHandlerThread.getHandler().obtainMessage();
            message.what = mRecordStatus;
            this.mRecordHandlerThread.getHandler().sendMessage(message);
        }
    }

    private void resetMicRecorder() {
        handleRecorderEvent(0);
    }

    private void micRecordReceiver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("mic_record"), true, new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.smart.SmartRecordService.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                SmartRecordService smartRecordService = SmartRecordService.this;
                smartRecordService.handleRecorderEvent(smartRecordService.getProviderRecorder());
            }
        });
    }

    /* loaded from: classes5.dex */
    private class MicServiceCallBack implements KaraokeManager.MicCallBack {
        private MicServiceCallBack() {
        }

        public void micServiceCallBack(int msg) {
            if (msg == 3) {
                boolean unused = SmartRecordService.mDongleStatus = true;
            } else if (msg == 4) {
                boolean unused2 = SmartRecordService.mDongleStatus = false;
            } else if (msg == 5) {
                boolean unused3 = SmartRecordService.mMicStatus = true;
            } else if (msg == 6) {
                boolean unused4 = SmartRecordService.mMicStatus = false;
            } else {
                return;
            }
            LogUtil.i(SmartRecordService.TAG, "micServiceCallBack msg=" + msg + ", mDongleStatus=" + SmartRecordService.mDongleStatus + ", mMicStatus=" + SmartRecordService.mMicStatus);
        }

        public void volumeEffectCallBack(int type, int value) {
        }

        public void onErrorEvent(int a, int b) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        super.initXUIManager();
        if (getXuiManager() == null) {
            LogUtil.e(TAG, "initXUIManager failed, xuimanager is null");
            return;
        }
        mMicCallback = new MicServiceCallBack();
        try {
            LogUtil.i(TAG, "init micManager");
            mMicManager = (KaraokeManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_KARAOKE);
            mMicManager.XMA_init("recorder");
            mMicManager.XMA_registerCallback(mMicCallback);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private SmartRecordService(Context context) {
        super(context);
        this.mRecordHandlerThread = new RecordHandlerThread();
        resetMicRecorder();
        micRecordReceiver();
    }

    public static SmartRecordService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartRecordService sService = new SmartRecordService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
        this.mRecordHandlerThread.start();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        this.mRecordHandlerThread.quit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startRecord() {
        try {
            mMicManager.XMA_resume();
            int recSize = mMicManager.XMA_recGetMinBuf(48000, 2);
            int trackSize = mMicManager.XMA_trackGetMinBuf(48000, 2);
            final byte[] recData = new byte[recSize];
            final byte[] trackData = new byte[trackSize];
            Arrays.fill(trackData, (byte) 0);
            LogUtil.i(TAG, "startRecord, recSize=" + recSize + ", trackSize=" + trackSize);
            mMicManager.XMA_recCreate(48000, 2, recSize);
            mMicManager.XMA_trackCreate(48000, 2, trackSize);
            mMicManager.XMA_resumePlay();
            new Thread(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartRecordService.2
                @Override // java.lang.Runnable
                public void run() {
                    while (SmartRecordService.mRecordStatus == 1) {
                        try {
                            if (SmartRecordService.mMicStatus && SmartRecordService.mMicManager.XMA_trackGetLatency() < 19200) {
                                LogUtil.d(SmartRecordService.TAG, "xma write trackData...");
                                SmartRecordService.mMicManager.XMA_write(trackData, 0, trackData.length);
                            }
                            Thread.sleep(10L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartRecordService.3
                @Override // java.lang.Runnable
                public void run() {
                    while (SmartRecordService.mRecordStatus == 1) {
                        try {
                            if (SmartRecordService.mMicStatus && SmartRecordService.mMicManager.XMA_recGetAvail() > recData.length) {
                                LogUtil.d(SmartRecordService.TAG, "xma read recData...");
                                SmartRecordService.mMicManager.XMA_readRec(recData);
                            }
                            Thread.sleep(10L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopRecord() {
        LogUtil.i(TAG, "stopRecord");
        try {
            mMicManager.XMA_pausePlay();
            mMicManager.XMA_pause();
            mMicManager.XMA_recDestroy();
            mMicManager.XMA_trackDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class RecordHandlerThread extends HandlerThread {
        private static final String TAG = "RecordHandlerThread";
        private Handler handler;

        public RecordHandlerThread() {
            super(TAG, 10);
        }

        @Override // android.os.HandlerThread
        @SuppressLint({"HandlerLeak"})
        protected void onLooperPrepared() {
            this.handler = new Handler() { // from class: com.xiaopeng.xuiservice.smart.SmartRecordService.RecordHandlerThread.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    LogUtil.i(RecordHandlerThread.TAG, "handleMessage  msg:" + msg.what);
                    int i = msg.what;
                    if (i == 0) {
                        SmartRecordService.this.stopRecord();
                    } else if (i == 1) {
                        SmartRecordService.this.startRecord();
                    } else {
                        LogUtil.e(RecordHandlerThread.TAG, "Event type not handled?" + msg);
                    }
                }
            };
        }

        public Handler getHandler() {
            return this.handler;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
