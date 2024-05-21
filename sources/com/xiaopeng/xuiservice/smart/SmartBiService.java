package com.xiaopeng.xuiservice.smart;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import java.io.PrintWriter;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SmartBiService extends BaseSmartService {
    private static final String BI_UPLOADLOG_ACTION = "android.intent.action.BI_UPLOADLOG";
    private static final String BI_UPLOADLOG_EXTRA_LOG = "LogString";
    private static final int MSG_BILOG_UPGRADE = 0;
    private static final String SYS_MID = "system";
    private static final String SYS_PID = "P10069";
    private static final String TAG = "SmartBiService";
    private BiHandlerThread mBiHandlerThread;
    private final BroadcastReceiver mReceiver;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiLogData {
        public String buttonId;
        public String moduleId;
        public String pageId;
        public String params;

        public BiLogData(String moduleId, String pageId, String buttonId, String params) {
            this.moduleId = moduleId;
            this.pageId = pageId;
            this.buttonId = buttonId;
            this.params = params;
        }
    }

    public synchronized void handleBiUploadEvent(String jsonStr) {
        LogUtil.d(TAG, "handleBiUploadEvent: " + jsonStr);
        try {
            JSONObject obj = new JSONObject(jsonStr);
            String moduleId = obj.has("moduleId") ? obj.getString("moduleId") : "system";
            String pageId = obj.has("pageId") ? obj.getString("pageId") : SYS_PID;
            String buttonId = obj.getString("buttonId");
            String params = obj.getString("params");
            sendBiUploadEvent(new BiLogData(moduleId, pageId, buttonId, params));
        } catch (Exception e) {
            LogUtil.e(TAG, "handleBiUploadEvent failed " + e);
        }
    }

    private void sendBiUploadEvent(BiLogData data) {
        Message message = this.mBiHandlerThread.getHandler().obtainMessage();
        message.what = 0;
        message.obj = data;
        this.mBiHandlerThread.getHandler().sendMessage(message);
    }

    private void registerReceiver() {
        LogUtil.i(TAG, "registerReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BI_UPLOADLOG_ACTION);
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    public void unregisterReceiver() {
        LogUtil.i(TAG, "unregisterReceiver..");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    private SmartBiService(Context context) {
        super(context);
        this.mBiHandlerThread = new BiHandlerThread();
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.SmartBiService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                LogUtil.d(SmartBiService.TAG, "onReceive");
                String jsonStr = intent.getStringExtra(SmartBiService.BI_UPLOADLOG_EXTRA_LOG);
                SmartBiService.this.handleBiUploadEvent(jsonStr);
            }
        };
    }

    public static SmartBiService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartBiService sService = new SmartBiService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
        registerReceiver();
        this.mBiHandlerThread.start();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        this.mBiHandlerThread.quit();
        unregisterReceiver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiHandlerThread extends HandlerThread {
        private static final String TAG = "BiHandlerThread";
        private Handler handler;

        public BiHandlerThread() {
            super(TAG, 10);
        }

        @Override // android.os.HandlerThread
        @SuppressLint({"HandlerLeak"})
        protected void onLooperPrepared() {
            this.handler = new Handler() { // from class: com.xiaopeng.xuiservice.smart.SmartBiService.BiHandlerThread.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    LogUtil.i(BiHandlerThread.TAG, "handleMessage  msg:" + msg.what);
                    if (msg.what == 0) {
                        BiLogData data = (BiLogData) msg.obj;
                        DataLogUtils.sendSystemEventDataLog(data.moduleId, data.pageId, data.buttonId, data.params);
                        return;
                    }
                    LogUtil.e(BiHandlerThread.TAG, "Event type not handled?" + msg);
                }
            };
        }

        public Handler getHandler() {
            return this.handler;
        }
    }
}
