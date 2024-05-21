package com.xiaopeng.xuiservice.userscenario;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.icm.CarIcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.WindowManager;
import androidx.core.app.NotificationCompat;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xuimanager.userscenario.IUserScenario;
import com.xiaopeng.xuimanager.userscenario.IUserScenarioListener;
import com.xiaopeng.xuimanager.userscenario.UserScenarioManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.userscenario.UserScenarioConfiger;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.XAppStartManager;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class UserScenarioService extends IUserScenario.Stub implements XUIServiceBase {
    private static final String ACTION_SECOND_BT_CONNECTION = "xiaopeng.bluetooth.a2dp.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private static final String BUTTON_ID = "B001";
    private static final int CHECK_OPERATION_TIMEOUT_MS = 4000;
    private static final String EXTRA_VOLCHANGE_PACKAGENAME = "android.media.vol_change.PACKAGE_NAME";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private static final int MSG_SCENARIO_ENTER = 1;
    private static final int MSG_SCENARIO_ENTER_CHECK = 3;
    private static final int MSG_SCENARIO_ENTER_INNER = 7;
    private static final int MSG_SCENARIO_EXIT = 2;
    private static final int MSG_SCENARIO_EXIT_CHECK = 4;
    private static final int MSG_SCENARIO_EXIT_INNER = 6;
    private static final int MSG_SCENARIO_STATUS_NOTIFY = 5;
    private static final int MSG_SCENARIO_VOLUME_CHANGED = 8;
    private static final int MSG_SCENARIO_VOLUME_PASSENGER_BT_CHANGED = 9;
    private static final String OP_ENTER = "enter";
    private static final String OP_EXIT = "exit";
    private static final String OP_REPORT = "report";
    private static final String TRIGGER_BY_EVENT = "2";
    private static final String TRIGGER_BY_USER = "1";
    private static final String TYPE_ENTER = "1";
    private static final String TYPE_EXIT = "2";
    private static SimpleDateFormat formatter;
    private static IBinder[] mAppBinders;
    private static DeathObserver[] mAppDeathRecipients;
    private static int[] mAppPids;
    private static AudioManager mAudioManager;
    private final Context mContext;
    private XDialog mDialog;
    private static final String TAG = UserScenarioService.class.getSimpleName();
    private static UserScenarioService mUserScenarioService = null;
    private static List<UserScenarioConfiger> mConfigerList = new CopyOnWriteArrayList();
    private static UserScenarioConfiger[][] mConfigGroup = new UserScenarioConfiger[1];
    private static volatile UserScenarioConfiger[] mRunningConfigers = new UserScenarioConfiger[1];
    private static UserScenarioHandler mUserScenarioHandler = null;
    private static ActivityManager mActivityManager = (ActivityManager) ActivityThread.currentActivityThread().getApplication().getSystemService("activity");
    private static boolean debugMode = false;
    private static int[] mStatusToBe = {-1};
    private static int lastMusicVolume = -1;
    private static int lastPassengerBtVolume = -1;
    private static boolean passengerbluetoothConnected = false;
    private static ArrayList<String> mVolumeIgnorePkgList = new ArrayList<String>() { // from class: com.xiaopeng.xuiservice.userscenario.UserScenarioService.1
        {
            add("com.xiaopeng.xuiservice");
        }
    };
    private static final HashMap<Integer, String> msgMap = new HashMap<>();
    private final Map<IBinder, ListenerRecord> mListenerMap = new HashMap();
    private final HashMap<String, UserScenarioConfiger> mConfigMap = new HashMap<>();
    private WindowManager mWindowManager = (WindowManager) ActivityThread.currentActivityThread().getApplication().getSystemService(ConditionWindowPos.TYPE);
    private boolean mVolumeMonitor = false;
    private BroadcastReceiver mVolumeReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.userscenario.UserScenarioService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            UserScenarioService.this.dispatchBroadcast(intent);
        }
    };
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.userscenario.UserScenarioService.3
        public void onChangeEvent(CarPropertyValue value) {
            if (value.getPropertyId() == 557847045) {
                int gear = ((Integer) value.getValue()).intValue();
                String str = UserScenarioService.TAG;
                LogUtil.i(str, "gear changed to:" + gear);
                UserScenarioService.mUserScenarioService.onGearChanged(gear);
            }
        }

        public void onErrorEvent(int propertyId, int zone) {
        }
    };
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.userscenario.UserScenarioService.4
        public void onChangeEvent(CarPropertyValue value) {
            if (value.getPropertyId() == 557915161) {
                try {
                    Integer[] array = (Integer[]) value.getValue();
                    if (array != null) {
                        UserScenarioService.mUserScenarioService.onDoorStatChanged(array);
                    }
                } catch (Exception e) {
                    String str = UserScenarioService.TAG;
                    LogUtil.w(str, "bcm onChangeEvent,e=" + e);
                }
            }
        }

        public void onErrorEvent(int propertyId, int zone) {
        }
    };
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.userscenario.UserScenarioService.5
        public void onChangeEvent(CarPropertyValue value) {
            UserScenarioService.this.handlePropertyChange(value);
        }

        public void onErrorEvent(int propertyId, int zone) {
        }
    };

    static {
        msgMap.put(1, "MSG_SCENARIO_ENTER");
        msgMap.put(2, "MSG_SCENARIO_EXIT");
        msgMap.put(3, "MSG_SCENARIO_ENTER_CHECK");
        msgMap.put(4, "MSG_SCENARIO_EXIT_CHECK");
        msgMap.put(5, "MSG_SCENARIO_STATUS_NOTIFY");
        msgMap.put(6, "MSG_SCENARIO_EXIT_INNER");
        msgMap.put(7, "MSG_SCENARIO_ENTER_INNER");
        msgMap.put(8, "MSG_SCENARIO_VOLUME_CHANGED");
        msgMap.put(9, "MSG_SCENARIO_VOLUME_PASSENGER_BT_CHANGED");
        mAppPids = new int[1];
        mAppBinders = new IBinder[1];
        mAppDeathRecipients = new DeathObserver[1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class ListenerRecord implements IBinder.DeathRecipient {
        private final String TAG = UserScenarioService.class.getSimpleName();
        public final IUserScenarioListener listener;
        public final int pid;
        public final int uid;

        ListenerRecord(IUserScenarioListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = this.TAG;
            LogUtil.w(str, "binder died,pid=" + this.pid);
            release();
            synchronized (UserScenarioService.this.mListenerMap) {
                UserScenarioService.this.mListenerMap.remove(this.listener.asBinder());
            }
        }

        public void release() {
            try {
                this.listener.asBinder().unlinkToDeath(this, 0);
            } catch (Exception e) {
                String str = this.TAG;
                LogUtil.w(str, "ListenerRecord release e=" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class UserScenarioHandler extends Handler {
        private final String TAG;

        public UserScenarioHandler(Looper looper) {
            super(looper);
            this.TAG = UserScenarioService.class.getSimpleName();
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            UserScenarioConfiger lastConfiger;
            String str = this.TAG;
            LogUtil.i(str, "handle msg:" + ((String) UserScenarioService.msgMap.get(Integer.valueOf(msg.what))));
            switch (msg.what) {
                case 1:
                    UserScenarioConfiger configer = (UserScenarioConfiger) msg.obj;
                    UserScenarioService.mRunningConfigers[configer.getGroupIndex()] = configer;
                    if (configer.getCurrentFriend() != null && (lastConfiger = (UserScenarioConfiger) UserScenarioService.this.mConfigMap.get(configer.getCurrentFriend())) != null) {
                        UserScenarioService.this.lastConfigerExitInner(configer, lastConfiger);
                    }
                    configer.enter();
                    return;
                case 2:
                    UserScenarioConfiger configer2 = (UserScenarioConfiger) msg.obj;
                    int groupIdx = configer2.getGroupIndex();
                    try {
                        if (UserScenarioService.mAppBinders[groupIdx] != null) {
                            UserScenarioService.mAppBinders[groupIdx].unlinkToDeath(UserScenarioService.mAppDeathRecipients[groupIdx], 0);
                            UserScenarioService.mAppBinders[groupIdx] = null;
                            UserScenarioService.mAppDeathRecipients[groupIdx] = null;
                        }
                    } catch (Exception e) {
                        String str2 = this.TAG;
                        LogUtil.w(str2, "MSG_SCENARIO_EXIT e=" + e);
                    }
                    configer2.voiceOnOff(false);
                    configer2.exit();
                    return;
                case 3:
                    UserScenarioConfiger configer3 = (UserScenarioConfiger) msg.obj;
                    if (UserScenarioService.mAppBinders[configer3.getGroupIndex()] == null) {
                        LogUtil.w(this.TAG, "enter check,app observer is null");
                        UserScenarioService.mUserScenarioHandler.obtainMessage(6, msg.obj).sendToTarget();
                        return;
                    } else if (1 == configer3.getStatus()) {
                        configer3.setStatus(2);
                        UserScenarioService.this.notifyScenarioStatus(configer3.getScenarioName(), 2);
                        return;
                    } else {
                        return;
                    }
                case 4:
                    UserScenarioConfiger configer4 = (UserScenarioConfiger) msg.obj;
                    if (configer4.getStatus() == 0) {
                        synchronized (UserScenarioService.mUserScenarioService) {
                            UserScenarioService.mRunningConfigers[configer4.getGroupIndex()] = null;
                        }
                        return;
                    } else if (3 == configer4.getStatus()) {
                        configer4.setStatus(0);
                        synchronized (UserScenarioService.mUserScenarioService) {
                            UserScenarioService.mRunningConfigers[configer4.getGroupIndex()] = null;
                        }
                        UserScenarioService.this.notifyScenarioStatus(configer4.getScenarioName(), 0);
                        return;
                    } else {
                        return;
                    }
                case 5:
                    int status = msg.arg1;
                    UserScenarioConfiger configer5 = (UserScenarioConfiger) msg.obj;
                    configer5.configVolume(2 == status);
                    UserScenarioService.this.notifyScenarioStatus(configer5.getScenarioName(), status);
                    return;
                case 6:
                    UserScenarioConfiger configer6 = (UserScenarioConfiger) msg.obj;
                    if (configer6.getStatus() == 0) {
                        synchronized (UserScenarioService.mUserScenarioService) {
                            UserScenarioService.mRunningConfigers[configer6.getGroupIndex()] = null;
                        }
                        return;
                    }
                    configer6.voiceOnOff(false);
                    configer6.configVolume(false);
                    configer6.exitInner();
                    synchronized (UserScenarioService.mUserScenarioService) {
                        UserScenarioService.mRunningConfigers[configer6.getGroupIndex()] = null;
                    }
                    UserScenarioService.this.notifyScenarioStatus(configer6.getScenarioName(), 0);
                    return;
                case 7:
                    UserScenarioConfiger configer7 = (UserScenarioConfiger) msg.obj;
                    configer7.enterInner();
                    configer7.voiceOnOff(true);
                    configer7.configVolume(true);
                    if (configer7.shouldMonitorVolume()) {
                        UserScenarioService.this.volumeMonitor();
                    }
                    if (1 == configer7.getStatus()) {
                        UserScenarioService.this.notifyScenarioStatus(configer7.getScenarioName(), 2);
                        return;
                    }
                    return;
                case 8:
                    int targetVolume = msg.arg1;
                    UserScenarioConfiger configer8 = (UserScenarioConfiger) msg.obj;
                    if (targetVolume != configer8.getVolumeDynamic()) {
                        configer8.setVolumeDynamic(targetVolume);
                        configer8.saveData();
                        return;
                    }
                    return;
                case 9:
                    int targetVolume2 = msg.arg1;
                    UserScenarioConfiger configer9 = (UserScenarioConfiger) msg.obj;
                    if (targetVolume2 != configer9.getVolumeBtDynamic()) {
                        configer9.setVolumeBtDynamic(targetVolume2);
                        configer9.saveData();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class DeathObserver implements IBinder.DeathRecipient {
        private int appPid;
        private int groupIdx;
        private String scenario;

        public DeathObserver(int groupIdx, int appPid, String scenario) {
            this.groupIdx = groupIdx;
            this.appPid = appPid;
            this.scenario = scenario;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = UserScenarioService.TAG;
            LogUtil.w(str, "launch app died, pid=" + this.appPid + ",scenario:" + this.scenario + ",group index:" + this.groupIdx);
            UserScenarioService.mAppBinders[this.groupIdx] = null;
            if (UserScenarioService.mRunningConfigers[this.groupIdx] != null) {
                try {
                    UserScenarioService.mUserScenarioHandler.removeMessages(3);
                    UserScenarioService.mUserScenarioHandler.removeMessages(4);
                    UserScenarioService.mRunningConfigers[this.groupIdx].setStatus(3);
                    UserScenarioService.mUserScenarioHandler.obtainMessage(6, UserScenarioService.mRunningConfigers[this.groupIdx]).sendToTarget();
                } catch (Exception e) {
                    String str2 = UserScenarioService.TAG;
                    LogUtil.w(str2, "DeathRecipient e=" + e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class OpRecord {
        public int configerState;
        public long opTick;
        public String operation;
        public String ret;
        public String source;

        private OpRecord() {
        }

        public String toString() {
            if (UserScenarioService.formatter == null) {
                SimpleDateFormat unused = UserScenarioService.formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            }
            StringBuilder sb = new StringBuilder();
            Date curDate = new Date(this.opTick);
            sb.append(UserScenarioService.formatter.format(curDate));
            sb.append(",op:");
            sb.append(this.operation);
            if (-1 == this.configerState) {
                sb.append(",stat:-1");
            } else {
                sb.append(",stat:0x");
                sb.append(Integer.toHexString(this.configerState));
            }
            sb.append(",source:");
            sb.append(this.source);
            sb.append(",ret:");
            sb.append(this.ret);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class OpRecordManager {
        public static final int MAX_RECORD_COUNT = 64;
        public static OpRecord[] recordArray = new OpRecord[64];
        public static int pointer = 0;

        private OpRecordManager() {
        }

        public static synchronized void add(OpRecord record) {
            synchronized (OpRecordManager.class) {
                if (pointer >= 64) {
                    pointer = 0;
                }
                OpRecord[] opRecordArr = recordArray;
                int i = pointer;
                pointer = i + 1;
                opRecordArr[i] = record;
            }
        }
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("UserScenarioService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("UserScenarioService", info);
        }
    }

    public UserScenarioService(Context context) {
        this.mContext = context;
        mUserScenarioService = this;
        DumpDispatcher.registerDump("userscenario", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$u8eSdsm-kIXSUFSxhCbsj8FVw8s
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                UserScenarioService.this.lambda$new$0$UserScenarioService(printWriter, strArr);
            }
        });
        mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
    }

    public static UserScenarioService getInstance() {
        return mUserScenarioService;
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void init() {
        CarClientManager.getInstance().addVcuManagerListener(this.mCarVcuEventCallback);
        CarClientManager.getInstance().addBcmManagerListener(this.mCarBcmEventCallback);
        CarClientManager.getInstance().addMcuManagerListener(this.mCarMcuEventCallback);
        mUserScenarioHandler = new UserScenarioHandler(XuiWorkHandler.getInstance().getLooper());
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$KQR2okMmGxl09bpZI9JxRTc51iY
            @Override // java.lang.Runnable
            public final void run() {
                UserScenarioService.this.lambda$init$1$UserScenarioService();
            }
        });
        registerBroadcast();
    }

    public /* synthetic */ void lambda$init$1$UserScenarioService() {
        mConfigGroup = UserScenarioParser.parseUserScenarioConfig();
        UserScenarioConfiger[][] userScenarioConfigerArr = mConfigGroup;
        if (userScenarioConfigerArr != null) {
            mRunningConfigers = new UserScenarioConfiger[userScenarioConfigerArr.length];
            mStatusToBe = new int[userScenarioConfigerArr.length];
            mAppPids = new int[userScenarioConfigerArr.length];
            mAppBinders = new IBinder[userScenarioConfigerArr.length];
            mAppDeathRecipients = new DeathObserver[userScenarioConfigerArr.length];
            int i = 0;
            while (true) {
                UserScenarioConfiger[][] userScenarioConfigerArr2 = mConfigGroup;
                if (i >= userScenarioConfigerArr2.length) {
                    break;
                }
                if (userScenarioConfigerArr2[i] != null) {
                    int j = 0;
                    while (true) {
                        UserScenarioConfiger[][] userScenarioConfigerArr3 = mConfigGroup;
                        if (j < userScenarioConfigerArr3[i].length) {
                            if (userScenarioConfigerArr3[i][j] != null) {
                                mConfigerList.add(userScenarioConfigerArr3[i][j]);
                            } else {
                                String str = TAG;
                                LogUtil.w(str, "wtf, null configer for data group:" + i + ",idx:" + j);
                            }
                            j++;
                        }
                    }
                } else {
                    String str2 = TAG;
                    LogUtil.w(str2, "init, no configer data of group:" + i);
                }
                i++;
            }
        }
        if (!mConfigerList.isEmpty()) {
            for (UserScenarioConfiger configer : mConfigerList) {
                String str3 = TAG;
                LogUtil.i(str3, "find userscenario config=" + configer);
                this.mConfigMap.put(configer.getScenarioName(), configer);
            }
            int groupLen = mConfigGroup.length;
            for (int i2 = 0; i2 < groupLen; i2++) {
                restoreStatus(i2);
            }
        }
    }

    private void restoreStatus(int groupIdx) {
        String[] array;
        String val = SystemProperties.get(UserScenarioConfiger.PROP_CONFIGER_STATUS + groupIdx);
        if (!TextUtils.isEmpty(val) && (array = val.split("#")) != null && array.length > 1) {
            mRunningConfigers[groupIdx] = this.mConfigMap.get(array[0]);
            int status = Integer.parseInt(array[1]);
            String str = TAG;
            LogUtil.i(str, "restoreStatus,name=" + array[0] + ",stat=" + status);
            if (mRunningConfigers[groupIdx] != null) {
                if (status != 0) {
                    if (status != 1 && status != 2) {
                        if (status != 3) {
                            return;
                        }
                    } else {
                        mRunningConfigers[groupIdx].setStatus(2);
                        mUserScenarioHandler.obtainMessage(7, mRunningConfigers[groupIdx]).sendToTarget();
                        return;
                    }
                }
                mRunningConfigers[groupIdx].setStatus(3);
                mUserScenarioHandler.obtainMessage(6, mRunningConfigers[groupIdx]).sendToTarget();
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void release() {
        synchronized (this.mListenerMap) {
            for (ListenerRecord record : this.mListenerMap.values()) {
                record.release();
            }
            this.mListenerMap.clear();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter pw) {
        pw.println("dump-" + TAG);
    }

    /* renamed from: dump */
    public void lambda$new$0$UserScenarioService(PrintWriter pw, String[] args) {
        OpRecord[] opRecordArr;
        pw.println("dump-" + TAG);
        boolean dumpAll = false;
        if (args == null) {
            pw.println("please input cmds");
            return;
        }
        int i = 0;
        while (true) {
            if (i < args.length) {
                String str = args[i];
                char c = 65535;
                switch (str.hashCode()) {
                    case -451623671:
                        if (str.equals("-debugMode")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -139990669:
                        if (str.equals("-active")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 1492:
                        if (str.equals("-a")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 1503:
                        if (str.equals("-l")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 44686027:
                        if (str.equals("-exit")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1384979147:
                        if (str.equals("-enter")) {
                            c = 4;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    dumpAll = true;
                } else if (c == 1) {
                    i++;
                    if (i < args.length) {
                        if ("0".equals(args[i])) {
                            debugMode = false;
                            pw.println("disable debugMode");
                        } else {
                            debugMode = true;
                            pw.println("enable debugMode");
                        }
                    } else {
                        pw.println("please add params 0/1!");
                    }
                } else if (c == 2) {
                    for (int j = 0; j < mConfigGroup.length; j++) {
                        if (mRunningConfigers[j] == null) {
                            pw.println("exit current scenario,no working scenario");
                        } else {
                            String scenarioName = mRunningConfigers[j].getScenarioName();
                            pw.println("exit current scenario:" + scenarioName);
                            exitUserScenario(scenarioName);
                        }
                    }
                } else if (c != 3) {
                    if (c != 4) {
                        if (c == 5) {
                            for (Map.Entry<String, UserScenarioConfiger> configerEntry : this.mConfigMap.entrySet()) {
                                UserScenarioConfiger configer = configerEntry.getValue();
                                pw.println("  scenario:" + configer.getScenarioName() + ",status:" + configer.getStatus() + ",group:" + configer.getGroupIndex());
                            }
                        }
                    } else if (Build.TYPE.equals(XAppStartManager.SHARED_FROM_USER)) {
                        pw.println("release version not support!");
                    } else if (i + 1 < args.length) {
                        String scenarioName2 = null;
                        String ret = null;
                        try {
                            int index = Integer.parseInt(args[i + 1]);
                            if (index >= 0 && index < mConfigerList.size()) {
                                ret = enterUserScenario(mConfigerList.get(index).getScenarioName(), NotificationCompat.CATEGORY_SERVICE);
                            }
                        } catch (NumberFormatException e) {
                            scenarioName2 = args[i + 1];
                            ret = enterUserScenario(scenarioName2, NotificationCompat.CATEGORY_SERVICE);
                        }
                        pw.println("try to enter :" + scenarioName2 + ",ret=" + ret);
                    }
                } else if (Build.TYPE.equals(XAppStartManager.SHARED_FROM_USER)) {
                    pw.println("release version not support!");
                } else if (i + 1 < args.length) {
                    try {
                        int scenarioIndex = Integer.parseInt(args[i + 1]);
                        if (scenarioIndex < 0) {
                            pw.println("invalid scenario state:" + scenarioIndex);
                        } else if (scenarioIndex < mConfigerList.size()) {
                            UserScenarioConfiger configer2 = mConfigerList.get(scenarioIndex);
                            configer2.setStatus(2);
                            mRunningConfigers[configer2.getGroupIndex()] = configer2;
                            pw.println("set current scenario as:" + configer2.getScenarioName());
                        } else {
                            pw.println("scenario index overlap");
                        }
                    } catch (NumberFormatException e2) {
                        String scenario = args[i + 1];
                        UserScenarioConfiger configer3 = this.mConfigMap.get(scenario);
                        if (configer3 == null) {
                            pw.println("invalid scenario:" + scenario);
                        } else {
                            configer3.setStatus(2);
                            mRunningConfigers[configer3.getGroupIndex()] = configer3;
                            pw.println("active scenario:" + scenario);
                        }
                    }
                } else {
                    pw.println("lack scenario index...");
                }
                i++;
            } else {
                if (dumpAll) {
                    pw.println("debugMode:" + debugMode);
                    if (!mConfigerList.isEmpty()) {
                        pw.println("version:" + UserScenarioParser.getVersionCode());
                        pw.println("configers:");
                        for (UserScenarioConfiger configer4 : mConfigerList) {
                            pw.println(configer4);
                        }
                    } else {
                        pw.println("mConfigerList is empty!");
                    }
                    pw.println("operation history,op pointer:" + OpRecordManager.pointer + ",capacity:64");
                    if (OpRecordManager.recordArray[63] == null) {
                        for (OpRecord record : OpRecordManager.recordArray) {
                            if (record != null) {
                                pw.println(record);
                            }
                        }
                    } else {
                        for (int i2 = OpRecordManager.pointer; i2 < 64; i2++) {
                            pw.println(OpRecordManager.recordArray[i2]);
                        }
                        for (int i3 = 0; i3 < OpRecordManager.pointer; i3++) {
                            pw.println(OpRecordManager.recordArray[i3]);
                        }
                    }
                }
                pw.println("dump-" + TAG + ",end");
                return;
            }
        }
    }

    public synchronized String enterUserScenario(String scenario, String source) {
        OpRecord record = new OpRecord();
        record.opTick = System.currentTimeMillis();
        int pid = Binder.getCallingPid();
        String packageName = getProcessName(pid);
        record.source = scenario + "#" + packageName;
        record.operation = "enter";
        int groupIdx = getScenarioGroup(scenario);
        String str = TAG;
        LogUtil.i(str, "enterUserScenario,scenario=" + scenario + ",src=" + source + ",call pid=" + pid + ",caller=" + packageName + ",groupIdx=" + groupIdx);
        if (groupIdx < 0) {
            return "scenarioInvalid";
        }
        record.configerState = mRunningConfigers[groupIdx] != null ? mRunningConfigers[groupIdx].getStatus() : 0;
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        String retVal = enterScenarioInnerCheck(configer, false, null);
        if (retVal != null) {
            record.ret = retVal;
            OpRecordManager.add(record);
            if (!"scenarioInvalid".equals(retVal)) {
                sendBILog(scenario, packageName, retVal, "1", "1");
            }
            return retVal;
        }
        enterScenarioTrigger(configer, packageName, source, null);
        record.ret = ResponseParams.RESPONSE_KEY_SUCCESS;
        OpRecordManager.add(record);
        return ResponseParams.RESPONSE_KEY_SUCCESS;
    }

    public synchronized String enterUserScenarioWithExtra(String scenario, String source, String extra) {
        OpRecord record = new OpRecord();
        record.opTick = System.currentTimeMillis();
        int pid = Binder.getCallingPid();
        String packageName = getProcessName(pid);
        record.source = scenario + "#" + packageName;
        record.operation = "enter";
        int groupIdx = getScenarioGroup(scenario);
        String str = TAG;
        LogUtil.i(str, "enterUserScenarioWithExtra,scenario=" + scenario + ",src=" + source + ",extra=" + extra + ",call pid=" + pid + ",caller=" + packageName + ",groupIdx=" + groupIdx);
        if (groupIdx < 0) {
            return "scenarioInvalid";
        }
        record.configerState = mRunningConfigers[groupIdx] != null ? mRunningConfigers[groupIdx].getStatus() : 0;
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        String retVal = enterScenarioInnerCheck(configer, false, extra);
        if (retVal != null) {
            record.ret = retVal;
            OpRecordManager.add(record);
            if (!"scenarioInvalid".equals(retVal)) {
                sendBILog(scenario, packageName, retVal, "1", "1");
            }
            return retVal;
        }
        enterScenarioTrigger(configer, packageName, source, extra);
        record.ret = ResponseParams.RESPONSE_KEY_SUCCESS;
        OpRecordManager.add(record);
        return ResponseParams.RESPONSE_KEY_SUCCESS;
    }

    public String checkEnterUserScenario(String scenario, String source) {
        int pid = Binder.getCallingPid();
        String packageName = getProcessName(pid);
        String str = TAG;
        LogUtil.i(str, "checkEnterUserScenario,scenario=" + scenario + ",src=" + source + ",call pid=" + pid + ",caller=" + packageName);
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        String retVal = enterScenarioInnerCheck(configer, true, null);
        if (retVal == null) {
            return ResponseParams.RESPONSE_KEY_SUCCESS;
        }
        return retVal;
    }

    public synchronized String setParameters(String scenario, String jsonStr) {
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        if (configer == null) {
            return "scenarioInvalid";
        }
        boolean match = false;
        int i = 0;
        while (true) {
            if (i >= mConfigGroup.length) {
                break;
            } else if (configer != mRunningConfigers[i]) {
                i++;
            } else {
                match = true;
                break;
            }
        }
        if (!match) {
            String str = TAG;
            LogUtil.w(str, "setParameters invalid,scenario=" + scenario);
            return "scenarioInvalid";
        }
        handleScenarioParameters(configer, jsonStr);
        return ResponseParams.RESPONSE_KEY_SUCCESS;
    }

    public synchronized String exitUserScenario(String scenario) {
        String exitReason = null;
        if (scenario != null) {
            String[] array = scenario.split("#");
            scenario = array[0];
            if (array.length > 1) {
                exitReason = array[1];
                LogUtil.i(TAG, "exit reason=" + array[1]);
            }
        }
        OpRecord record = new OpRecord();
        record.opTick = System.currentTimeMillis();
        int pid = Binder.getCallingPid();
        String packageName = getProcessName(pid);
        record.source = scenario + "#" + packageName;
        record.operation = OP_EXIT;
        int groupIdx = getScenarioGroup(scenario);
        LogUtil.i(TAG, "exitUserScenario,scenario=" + scenario + ",call pid=" + pid + ",caller=" + packageName + ",groupIdx=" + groupIdx);
        if (groupIdx < 0) {
            return "scenarioInvalid";
        }
        UserScenarioConfiger configer = mRunningConfigers[groupIdx];
        record.configerState = configer != null ? configer.getStatus() : -1;
        if (configer == null) {
            LogUtil.w(TAG, "exitUserScenario,no working scenario");
            record.ret = "scenarioInvalid";
            OpRecordManager.add(record);
            return "scenarioInvalid";
        } else if (!configer.getScenarioName().equals(scenario) && !"scenario_all".equals(scenario)) {
            LogUtil.w(TAG, "exitUserScenario, scenario not match!");
            String ret = "conflict#" + configer.getScenarioName();
            record.ret = ret;
            OpRecordManager.add(record);
            return ret;
        } else if (1 != configer.getStatus() && 2 != configer.getStatus()) {
            LogUtil.w(TAG, "exitUserScenario,invalid status:" + configer.getStatus());
            record.ret = "scenarioInvalid";
            OpRecordManager.add(record);
            return "scenarioInvalid";
        } else if (1 == configer.getExitDialogConfirm()) {
            LogUtil.i(TAG, "exitUserScenario with exit window confirm");
            record.ret = "dialogConfirm";
            OpRecordManager.add(record);
            showExitWindow(configer);
            return "dialogConfirm";
        } else {
            try {
                if (exitReason != null) {
                    sendBILog(scenario, configer.getLastLaunchPackage(), exitReason, "2", "1");
                } else {
                    sendBILog(scenario, packageName, ResponseParams.RESPONSE_KEY_SUCCESS, "2", "1");
                }
                configer.setStatus(3);
                mStatusToBe[groupIdx] = 0;
                configer.setExitReason("rAppRequest");
                mUserScenarioHandler.obtainMessage(2, configer).sendToTarget();
                mUserScenarioHandler.removeMessages(4);
                Message checkMsg = mUserScenarioHandler.obtainMessage(4, configer);
                mUserScenarioHandler.sendMessageDelayed(checkMsg, 4000L);
            } catch (Exception e) {
                LogUtil.w(TAG, "exitUserScenario,e=" + e);
            }
            record.ret = ResponseParams.RESPONSE_KEY_SUCCESS;
            OpRecordManager.add(record);
            return ResponseParams.RESPONSE_KEY_SUCCESS;
        }
    }

    public String getCurrentUserScenario() {
        String scenarioName = "normal_mode";
        if (mRunningConfigers[0] != null) {
            scenarioName = mRunningConfigers[0].getScenarioName();
        }
        String str = TAG;
        LogUtil.i(str, "getCurrentUserScenario=" + scenarioName);
        return scenarioName;
    }

    public String[] getCurrentUserScenarios() {
        int count = mConfigGroup.length;
        String[] scenarios = new String[count];
        for (int i = 0; i < count; i++) {
            if (mRunningConfigers[i] == null) {
                scenarios[i] = "normal_mode";
            } else {
                scenarios[i] = mRunningConfigers[i].getScenarioName();
            }
        }
        return scenarios;
    }

    public int getUserScenarioStatus(String scenario) {
        String str = TAG;
        LogUtil.d(str, "getUserScenarioStatus, scenario=" + scenario);
        if (scenario == null || "normal_mode".equals(scenario)) {
            LogUtil.w(TAG, "getUserScenarioStatus, scenario error");
            return -1;
        }
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        if (configer == null) {
            LogUtil.w(TAG, "getUserScenarioStatus, configer null");
            return -1;
        }
        return configer.getStatus();
    }

    public void registerListener(IUserScenarioListener listener) {
        if (listener == null) {
            LogUtil.w(TAG, "registerListener null!");
            return;
        }
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        IBinder binder = listener.asBinder();
        String str = TAG;
        LogUtil.i(str, "registerListener,binder-" + binder + ",pid-" + pid + ",uid" + uid + ",listeners-" + this.mListenerMap.size());
        synchronized (this.mListenerMap) {
            if (this.mListenerMap.containsKey(listener.asBinder())) {
                String str2 = TAG;
                LogUtil.w(str2, "repeat listener-" + binder);
                return;
            }
            ListenerRecord record = new ListenerRecord(listener, pid, uid);
            try {
                binder.linkToDeath(record, 0);
                synchronized (this.mListenerMap) {
                    this.mListenerMap.put(binder, record);
                }
            } catch (RemoteException e) {
                String str3 = TAG;
                LogUtil.w(str3, "registerListener e=" + e);
            }
        }
    }

    public void unregisterListener(IUserScenarioListener listener) {
        if (listener == null) {
            LogUtil.w(TAG, "unregisterListener null!");
            return;
        }
        IBinder binder = listener.asBinder();
        String str = TAG;
        LogUtil.i(str, "unregisterListener,binder-" + binder + ",listeners-" + this.mListenerMap.size());
        ListenerRecord record = this.mListenerMap.get(binder);
        if (record == null) {
            String str2 = TAG;
            LogUtil.w(str2, "no registed record for binder-" + binder);
            return;
        }
        record.release();
        synchronized (this.mListenerMap) {
            this.mListenerMap.remove(binder);
        }
    }

    public synchronized void reportStatus(String scenario, int status) {
        OpRecord record = new OpRecord();
        record.opTick = System.currentTimeMillis();
        int pid = Binder.getCallingPid();
        String packageName = getProcessName(pid);
        record.source = scenario + "#" + packageName;
        record.operation = OP_REPORT;
        int groupIdx = getScenarioGroup(scenario);
        if (groupIdx < 0) {
            LogUtil.w(TAG, "reportStatus,invalid scenario:" + scenario);
            return;
        }
        record.configerState = mRunningConfigers[groupIdx] != null ? mRunningConfigers[groupIdx].getStatus() : -1;
        record.configerState |= status << 8;
        record.ret = ResponseParams.RESPONSE_KEY_SUCCESS;
        OpRecordManager.add(record);
        LogUtil.i(TAG, "reportStatus,scenario-" + scenario + ",status-" + status + ",call pid-" + pid + ",caller=" + packageName);
        if (mRunningConfigers[groupIdx] == null) {
            if (mAppBinders[groupIdx] != null && mAppPids[groupIdx] == pid && 2 == status) {
                LogUtil.w(TAG, "reportStatus, app may start very slow,restart scenario state machine");
                mStatusToBe[groupIdx] = 2;
                mRunningConfigers[groupIdx] = this.mConfigMap.get(scenario);
                if (mRunningConfigers[groupIdx] == null) {
                    LogUtil.w(TAG, "reportStatus, reget configer null");
                    return;
                }
                mRunningConfigers[groupIdx].setStatus(1);
            } else {
                LogUtil.w(TAG, "reportStatus, no configer working for " + scenario + ",rp status:" + status);
                return;
            }
        }
        UserScenarioConfiger configer = mRunningConfigers[groupIdx];
        if (!configer.getScenarioName().equals(scenario)) {
            LogUtil.w(TAG, "reportStatus, scenario not match!");
        } else if (2 != status && status != 0) {
            LogUtil.w(TAG, "reportStatus, invalid status:" + status);
        } else if (status != mStatusToBe[groupIdx]) {
            LogUtil.w(TAG, "reportStatus, status conflict,tobe=" + mStatusToBe[groupIdx] + ",report=" + status);
        } else {
            mUserScenarioHandler.removeMessages(3);
            mUserScenarioHandler.removeMessages(4);
            if (2 == status) {
                try {
                    configer.voiceOnOff(true);
                    sendBILog(scenario, configer.getLastLaunchPackage(), ResponseParams.RESPONSE_KEY_SUCCESS, "1", "1");
                } catch (Exception e) {
                    LogUtil.w(TAG, "reportStatus,e=" + e);
                }
            }
            configer.setStatus(status);
            configer.setSubName(null);
            if (status == 0) {
                mRunningConfigers[groupIdx] = null;
            }
            mUserScenarioHandler.obtainMessage(5, status, 0, configer).sendToTarget();
        }
    }

    public synchronized void registerBinderObserver(IBinder binder) {
        if (mAppBinders[0] != null) {
            try {
                mAppBinders[0].unlinkToDeath(mAppDeathRecipients[0], 0);
                mAppDeathRecipients[0] = null;
            } catch (Exception e2) {
                String str = TAG;
                LogUtil.w(str, "registerBinderObserver, unlink e=" + e2);
            }
        }
        mAppBinders[0] = binder;
        mAppPids[0] = Binder.getCallingPid();
        String str2 = TAG;
        LogUtil.i(str2, "registerBinderObserver,pid=" + mAppPids[0] + ",binder=" + binder);
        if (mAppDeathRecipients[0] == null) {
            mAppDeathRecipients[0] = new DeathObserver(0, mAppPids[0], mRunningConfigers[0] != null ? mRunningConfigers[0].getScenarioName() : "unknown");
        }
        try {
            binder.linkToDeath(mAppDeathRecipients[0], 0);
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "registerBinderObserver e=" + e);
        }
    }

    public void registerBinderObserverWithData(IBinder binder, String scenario) throws RemoteException {
        int callPid = Binder.getCallingPid();
        String str = TAG;
        LogUtil.i(str, "registerBinderObserverWithData,pid=" + callPid + ",binder=" + binder + ",scenario=" + scenario);
        int groupIdx = getScenarioGroup(scenario);
        if (groupIdx < 0) {
            String str2 = TAG;
            LogUtil.w(str2, "registerBinderObserverWithData,no configer for:" + scenario);
            return;
        }
        IBinder[] iBinderArr = mAppBinders;
        if (iBinderArr[groupIdx] != null) {
            try {
                iBinderArr[groupIdx].unlinkToDeath(mAppDeathRecipients[groupIdx], 0);
                mAppDeathRecipients[groupIdx] = null;
            } catch (Exception e2) {
                String str3 = TAG;
                LogUtil.w(str3, "registerBinderObserverWithData, unlink e=" + e2);
            }
        }
        mAppBinders[groupIdx] = binder;
        mAppPids[groupIdx] = callPid;
        DeathObserver[] deathObserverArr = mAppDeathRecipients;
        if (deathObserverArr[groupIdx] == null) {
            deathObserverArr[groupIdx] = new DeathObserver(groupIdx, callPid, scenario);
        }
        try {
            binder.linkToDeath(mAppDeathRecipients[groupIdx], 0);
        } catch (Exception e) {
            String str4 = TAG;
            LogUtil.w(str4, "registerBinderObserverWithData e=" + e);
        }
    }

    public static Boolean isPassengerbluetoothConnected() {
        return Boolean.valueOf(passengerbluetoothConnected);
    }

    public boolean canAppLaunch(String pkg) {
        if (mRunningConfigers[0] != null) {
            return mRunningConfigers[0].canAppLaunch(pkg);
        }
        return true;
    }

    public boolean canAppLaunch(String pkg, int displayId) {
        int result;
        int count = mConfigGroup.length;
        for (int i = 0; i < count && 2 != (result = mRunningConfigers[i].canAppLaunchWithDisplay(pkg, displayId)); i++) {
            if (3 == result) {
                return false;
            }
        }
        return true;
    }

    public boolean canAppMove(String pkg) {
        boolean ret;
        for (int k = 0; k < mConfigGroup.length; k++) {
            if (mRunningConfigers[k] != null && !(ret = mRunningConfigers[k].canAppMove(pkg))) {
                return ret;
            }
        }
        return true;
    }

    public int getPanelCover(String scenario) {
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        if (configer != null) {
            return configer.getPanelCover();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static UserScenarioConfiger[] getRunningConfigers() {
        return mRunningConfigers;
    }

    private synchronized String exitUserScenarioWithReason(String scenario, String reason) {
        OpRecord record = new OpRecord();
        record.opTick = System.currentTimeMillis();
        int pid = Binder.getCallingPid();
        String packageName = getProcessName(pid);
        record.source = scenario + "#" + packageName;
        record.operation = OP_EXIT;
        int groupIdx = getScenarioGroup(scenario);
        LogUtil.i(TAG, "exitUserScenarioWithReason,scenario=" + scenario + ",call pid=" + pid + ",caller=" + packageName + ",groupIdx=" + groupIdx);
        if (groupIdx < 0) {
            return "scenarioInvalid";
        }
        UserScenarioConfiger currentConfiger = mRunningConfigers[groupIdx];
        record.configerState = currentConfiger != null ? currentConfiger.getStatus() : -1;
        if (currentConfiger == null) {
            LogUtil.w(TAG, "exitUserScenarioWithReason,no working scenario");
            record.ret = "scenarioInvalid";
            OpRecordManager.add(record);
            return "scenarioInvalid";
        } else if (!currentConfiger.getScenarioName().equals(scenario)) {
            LogUtil.w(TAG, "exitUserScenarioWithReason, scenario not match!");
            String ret = "conflict#" + currentConfiger.getScenarioName();
            record.ret = ret;
            OpRecordManager.add(record);
            return ret;
        } else if (1 != currentConfiger.getStatus() && 2 != currentConfiger.getStatus()) {
            LogUtil.w(TAG, "exitUserScenarioWithReason,invalid status:" + currentConfiger.getStatus());
            record.ret = "scenarioInvalid";
            OpRecordManager.add(record);
            return "scenarioInvalid";
        } else {
            try {
                sendBILog(scenario, packageName, reason, "2", "2");
                currentConfiger.setStatus(3);
                mStatusToBe[groupIdx] = 0;
                currentConfiger.setExitReason(reason);
                mUserScenarioHandler.obtainMessage(2, currentConfiger).sendToTarget();
                mUserScenarioHandler.removeMessages(4);
                Message checkMsg = mUserScenarioHandler.obtainMessage(4, currentConfiger);
                mUserScenarioHandler.sendMessageDelayed(checkMsg, 4000L);
            } catch (Exception e) {
                LogUtil.w(TAG, "exitUserScenarioWithReason,e=" + e);
            }
            record.ret = ResponseParams.RESPONSE_KEY_SUCCESS;
            OpRecordManager.add(record);
            return ResponseParams.RESPONSE_KEY_SUCCESS;
        }
    }

    private void notifyStatusToIcm(String scenario, int status) {
        CarIcmManager manager = CarClientManager.getInstance().getCarManager("xp_icm");
        try {
            int scenarioValue = UserScenarioManager.getScenarioNameValue(scenario);
            String str = TAG;
            LogUtil.i(str, "notifyStatusToIcm,name val=" + scenarioValue + ",status=" + status);
            manager.setModeInfo(scenarioValue, status);
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "notifyStatusToIcm e=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScenarioStatus(String scenario, int status) {
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        notifyToCanBus(2 == status, configer);
        seatWelcomeCheck(2 == status, configer);
        if (status == 0) {
            volumeUnMonitor();
        } else if (status == 2) {
            if (configer != null && configer.shouldMonitorVolume()) {
                volumeMonitor();
            } else {
                String str = TAG;
                LogUtil.d(str, "no dynamic volume set for:" + scenario);
            }
        }
        if (-2 != configer.getAppFlyLimit()) {
            String str2 = TAG;
            LogUtil.i(str2, "notifyScenarioStatus,do app fly limit->" + configer.getAppFlyLimit() + ",status->" + status);
            if (2 == status) {
                this.mWindowManager.setModeEvent(configer.getAppFlyLimit(), UserScenarioManager.getScenarioNameValue(scenario), null);
            } else if (status == 0) {
                this.mWindowManager.setModeEvent(configer.getAppFlyLimit(), -1, null);
            }
        }
        if (!this.mListenerMap.isEmpty()) {
            String str3 = TAG;
            LogUtil.i(str3, "notifyScenaroStatus,scenario:" + scenario + ",status:" + status);
            for (ListenerRecord record : this.mListenerMap.values()) {
                try {
                    record.listener.onUserScenarioStateChanged(scenario, status);
                } catch (Exception e) {
                    String str4 = TAG;
                    LogUtil.w(str4, "notifyScenaroStatus e:" + e + ",l:" + record.listener.asBinder());
                }
            }
        }
        if (configer.getNotifyIcm()) {
            notifyStatusToIcm(scenario, status);
        } else {
            LogUtil.d(TAG, "notifyScenarioStatus,notify icm false");
        }
    }

    private boolean isGearInP() {
        if (debugMode) {
            return true;
        }
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager == null) {
            return false;
        }
        try {
            boolean isP = manager.getDisplayGearLevel() == 4;
            return isP;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "isGearInP e=" + e);
            return false;
        }
    }

    private boolean isDoorCloseStateMatch(int doorState) {
        boolean z = true;
        if (debugMode) {
            return true;
        }
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager == null) {
            return false;
        }
        try {
            int[] stats = manager.getDoorsState();
            if (stats == null) {
                LogUtil.w(TAG, "isDoorCloseStateMatch getDoorsState null");
                return false;
            }
            int i = 0;
            int curState = 0;
            for (int stat : stats) {
                if (stat == 0) {
                    curState += 1 << i;
                }
                i++;
            }
            if ((doorState & curState) == 0) {
                z = false;
            }
            boolean isMatch = z;
            LogUtil.i(TAG, "isDoorCloseStateMatch,compared=" + doorState + ",cur=" + curState + ",match=" + isMatch);
            return isMatch;
        } catch (Exception e) {
            LogUtil.w(TAG, "isDoorCloseStateMatch e=" + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGearChanged(int gear) {
        if (4 != gear) {
            for (int i = 0; i < mConfigGroup.length; i++) {
                UserScenarioConfiger configer = mRunningConfigers[i];
                if (configer != null) {
                    try {
                        if (configer.exitOnGear(gear) && (1 == configer.getStatus() || 2 == configer.getStatus())) {
                            exitUserScenarioWithReason(configer.getScenarioName(), "rGearNotP");
                        }
                    } catch (Exception e) {
                        String str = TAG;
                        LogUtil.w(str, "onGearChanged,e=" + e);
                    }
                }
            }
        }
    }

    private boolean exitPreCheck() {
        for (int i = 0; i < mConfigGroup.length; i++) {
            UserScenarioConfiger configer = mRunningConfigers[i];
            if (configer != null && (1 == configer.getStatus() || 2 == configer.getStatus())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDoorStatChanged(Integer[] statArray) {
        String str = TAG;
        LogUtil.d(str, "onDoorStatChanged:" + Arrays.toString(statArray));
        if (!exitPreCheck()) {
            return;
        }
        for (int k = 0; k < mConfigGroup.length; k++) {
            UserScenarioConfiger configer = mRunningConfigers[k];
            if (configer != null && configer.getExitDoor() > 0) {
                int i = 0;
                int length = statArray.length;
                int i2 = 0;
                while (true) {
                    if (i2 < length) {
                        Integer status = statArray[i2];
                        if (1 == status.intValue()) {
                            int bitValue = 1 << i;
                            int compareValue = configer.getExitDoor() & bitValue;
                            if (compareValue > 0) {
                                String str2 = TAG;
                                LogUtil.i(str2, "door open hit,bit:" + bitValue + ",config:" + configer.getExitDoor() + ",scenario:" + configer.getScenarioName());
                                try {
                                    exitUserScenarioWithReason(configer.getScenarioName(), "rDoorOpen");
                                    break;
                                } catch (Exception e) {
                                    String str3 = TAG;
                                    LogUtil.w(str3, "handle door open,e=" + e);
                                }
                            }
                        }
                        i++;
                        i2++;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        if (value.getPropertyId() == 557847561) {
            handleIgStatus(value);
        }
    }

    private void handleIgStatus(CarPropertyValue value) {
        int status = ((Integer) value.getValue()).intValue();
        String str = TAG;
        LogUtil.d(str, "handleIgStatus:" + status);
        if (status == 0 && exitPreCheck()) {
            for (int i = 0; i < mConfigGroup.length; i++) {
                try {
                    if (mRunningConfigers[i] != null && (mRunningConfigers[i].getStatus() == 1 || mRunningConfigers[i].getStatus() == 2)) {
                        exitUserScenarioWithReason(mRunningConfigers[i].getScenarioName(), "rIgOff");
                    }
                } catch (Exception e) {
                    String str2 = TAG;
                    LogUtil.w(str2, "handle ig off,e=" + e);
                }
            }
        }
    }

    private void registerBroadcast() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add(BroadcastManager.ACTION_VOICE_ACTIVE);
        filter.add(BroadcastManager.ACTION_SCREEN_STATUS_CHANGE);
        BroadcastManager.getInstance().registerListener(new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$0T_If8HE1LJJ16A8K9UTI13GoHs
            @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
            public final void onReceive(Context context, Intent intent) {
                UserScenarioService.this.lambda$registerBroadcast$2$UserScenarioService(context, intent);
            }
        }, filter);
    }

    public /* synthetic */ void lambda$registerBroadcast$2$UserScenarioService(Context context, Intent intent) {
        dispatchBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void dispatchBroadcast(Intent intent) {
        char c;
        LogUtil.i(TAG, "get broadcast:" + intent);
        String action = intent.getAction();
        switch (action.hashCode()) {
            case -1940635523:
                if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -391260225:
                if (action.equals(BroadcastManager.ACTION_VOICE_ACTIVE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 279629229:
                if (action.equals(ACTION_SECOND_BT_CONNECTION)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1979690354:
                if (action.equals(BroadcastManager.ACTION_SCREEN_STATUS_CHANGE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            handleVoiceActive();
        } else if (c != 1) {
            if (c == 2) {
                handleVolumeEvent(intent);
            } else if (c == 3) {
                handlePassengerBluetoothStatus(intent);
            }
        } else {
            boolean screenOn = intent.getBooleanExtra("status", false);
            LogUtil.i(TAG, "screenOn:" + screenOn);
            if (screenOn) {
                handleScreenOn();
            }
        }
    }

    private void handleVoiceActive() {
        if (!exitPreCheck()) {
            return;
        }
        for (int i = 0; i < mConfigGroup.length; i++) {
            UserScenarioConfiger configer = mRunningConfigers[i];
            if (configer != null && configer.getExitVoice() > 0) {
                try {
                    exitUserScenarioWithReason(configer.getScenarioName(), "rVoiceActive");
                } catch (Exception e) {
                    String str = TAG;
                    LogUtil.w(str, "handleVoiceActive e=" + e);
                }
            }
        }
    }

    private void handleScreenOn() {
        if (!exitPreCheck()) {
            return;
        }
        for (int i = 0; i < mConfigGroup.length; i++) {
            UserScenarioConfiger configer = mRunningConfigers[i];
            if (configer != null && configer.getExitScreenOn() > 0) {
                try {
                    exitUserScenarioWithReason(configer.getScenarioName(), "rScreenOn");
                } catch (Exception e) {
                    String str = TAG;
                    LogUtil.w(str, "handleScreenOn e=" + e);
                }
            }
        }
    }

    private String getProcessName(int pid) {
        List<ActivityManager.RunningAppProcessInfo> runningApps = mActivityManager.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return "default";
    }

    private void sendBILog(String name, String source, String result, String type, String trigger) {
        BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, "B001");
        bilog.push("name", name);
        bilog.push("source", source);
        bilog.push(RecommendBean.SHOW_TIME_RESULT, result);
        bilog.push(SpeechConstants.KEY_COMMAND_TYPE, type);
        bilog.push("trigger", trigger);
        String str = TAG;
        LogUtil.i(str, "sendBILog, scenario=" + name + ",source=" + source + ",result=" + result + ",type=" + type + ",trigger=" + trigger);
        BiLogTransmit.getInstance().submit(bilog);
    }

    private String enterScenarioInnerCheck(UserScenarioConfiger configer, boolean onlyCheck, String extra) {
        int groupIdx = configer.getGroupIndex();
        UserScenarioConfiger runningConfiger = mRunningConfigers[groupIdx];
        if (runningConfiger != null) {
            if (runningConfiger == configer) {
                if (1 == runningConfiger.getStatus() || 2 == runningConfiger.getStatus()) {
                    LogUtil.w(TAG, "enterScenarioInnerCheck, already started:" + runningConfiger.getScenarioName());
                } else {
                    LogUtil.w(TAG, "enterScenarioInnerCheck,scenario:" + runningConfiger.getScenarioName() + ",invalid status:" + runningConfiger.getStatus());
                }
                return null;
            }
            String ret = "conflict#" + runningConfiger.getScenarioName();
            if (configer.isFriend(runningConfiger.getScenarioName())) {
                LogUtil.i(TAG, "target scenario:" + configer.getScenarioName() + " is current friend:" + runningConfiger.getScenarioName());
                if (!onlyCheck) {
                    configer.setCurrentFriend(runningConfiger.getScenarioName());
                }
            } else {
                LogUtil.w(TAG, ret);
                if (!onlyCheck) {
                    final String toastStr = runningConfiger.getConflictHintRes();
                    int screenId = 0;
                    if (extra != null) {
                        screenId = getScreenId(extra);
                    }
                    final int displayId = screenId;
                    UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$JZJmdXED5-a0SaddkfAXipXQ4Gk
                        @Override // java.lang.Runnable
                        public final void run() {
                            UserScenarioService.this.lambda$enterScenarioInnerCheck$3$UserScenarioService(toastStr, displayId);
                        }
                    });
                }
                return ret;
            }
        }
        UserScenarioConfiger.PanelCheckResult checkResult = configer.enterPanelsCheck();
        int panelsCheck = checkResult.result;
        if (panelsCheck != 0) {
            if (!onlyCheck && 1 == configer.getConflictHintFlag()) {
                int panelIndex = (panelsCheck & 1) != 0 ? 0 : 1;
                Integer[] winType = checkResult.winType[panelIndex];
                final String toastStr2 = UserScenarioParser.getWindowConflictString(winType, panelIndex);
                int screenId2 = 0;
                if (extra != null) {
                    screenId2 = getScreenId(extra);
                }
                final int displayId2 = screenId2;
                UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$eKKVOC4kgMzjJqoVKwcyb4rgFo4
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserScenarioService.this.lambda$enterScenarioInnerCheck$4$UserScenarioService(toastStr2, displayId2);
                    }
                });
            }
            return "panelInvalid#" + panelsCheck;
        } else if (configer.getEnterDoor() > 0 && !isDoorCloseStateMatch(configer.getEnterDoor())) {
            LogUtil.w(TAG, "enterScenarioInnerCheck,door open");
            if (!onlyCheck) {
                int screenId3 = 0;
                if (extra != null) {
                    screenId3 = getScreenId(extra);
                }
                final int displayId3 = screenId3;
                final String toastStr3 = this.mContext.getString(R.string.scenario_door_check_fail);
                UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$rrAf26o1DI8XmuPLlzHd-dBGm1w
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserScenarioService.this.lambda$enterScenarioInnerCheck$5$UserScenarioService(toastStr3, displayId3);
                    }
                });
                return "doorOpen";
            }
            return "doorOpen";
        } else if (1 != configer.getEnterGear() || isGearInP()) {
            return null;
        } else {
            LogUtil.w(TAG, "enterScenarioInnerCheck,gear not P");
            if (!onlyCheck) {
                int screenId4 = 0;
                if (extra != null) {
                    screenId4 = getScreenId(extra);
                }
                final int displayId4 = screenId4;
                final String toastStr4 = this.mContext.getString(R.string.scenario_gear_check_fail);
                UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$sEhOHS34JTZvdoNJLEmv0GNhX_c
                    @Override // java.lang.Runnable
                    public final void run() {
                        UserScenarioService.this.lambda$enterScenarioInnerCheck$6$UserScenarioService(toastStr4, displayId4);
                    }
                });
                return "gearNotP";
            }
            return "gearNotP";
        }
    }

    public /* synthetic */ void lambda$enterScenarioInnerCheck$3$UserScenarioService(String toastStr, int displayId) {
        String str = TAG;
        LogUtil.d(str, "show conflict str:" + toastStr);
        ToastUtil.showToast(this.mContext, toastStr, 0, displayId);
    }

    public /* synthetic */ void lambda$enterScenarioInnerCheck$4$UserScenarioService(String toastStr, int displayId) {
        String str = TAG;
        LogUtil.i(str, "show window conflict str:" + toastStr);
        ToastUtil.showToast(this.mContext, toastStr, 0, displayId);
    }

    public /* synthetic */ void lambda$enterScenarioInnerCheck$5$UserScenarioService(String toastStr, int displayId) {
        String str = TAG;
        LogUtil.i(str, "show door check str:" + toastStr);
        ToastUtil.showToast(this.mContext, toastStr, 0, displayId);
    }

    public /* synthetic */ void lambda$enterScenarioInnerCheck$6$UserScenarioService(String toastStr, int displayId) {
        String str = TAG;
        LogUtil.i(str, "show gear check str:" + toastStr);
        ToastUtil.showToast(this.mContext, toastStr, 0, displayId);
    }

    private void enterScenarioTrigger(UserScenarioConfiger configer, String packageName, String source, String extra) {
        int groupIdx = configer.getGroupIndex();
        mRunningConfigers[groupIdx] = configer;
        configer.setStatus(1);
        configer.setLastLaunchPackage(packageName);
        mStatusToBe[groupIdx] = 2;
        configer.setLaunchSource(source);
        configer.setLaunchExtra(extra);
        mUserScenarioHandler.obtainMessage(1, configer).sendToTarget();
        mUserScenarioHandler.removeMessages(3);
        Message checkMsg = mUserScenarioHandler.obtainMessage(3, configer);
        mUserScenarioHandler.sendMessageDelayed(checkMsg, 4000L);
    }

    private void seatWelcomeCheck(boolean enter, UserScenarioConfiger configer) {
        if (configer.getSeatWelcomeDisable() != 1) {
            LogUtil.d(TAG, "seatWelcomeCheck,needn't handle");
            return;
        }
        CarVcuManager vcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        CarClientManager.getInstance().getCarManager("xp_bcm");
        try {
            String scenario = configer.getScenarioName();
            String str = TAG;
            LogUtil.i(str, "seatWelcomeCheck,scenario:" + scenario + ",enter:" + enter);
            if (enter) {
                vcuManager.setSpecialCarbinModeSwitch(1);
            } else {
                vcuManager.setSpecialCarbinModeSwitch(0);
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "seatWelcomeCheck e=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void volumeMonitor() {
        if (!this.mVolumeMonitor) {
            this.mVolumeMonitor = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.media.VOLUME_CHANGED_ACTION");
            if (XUIConfig.hasPassengerBluetooth()) {
                int k = 0;
                while (true) {
                    if (k >= mConfigGroup.length) {
                        break;
                    }
                    UserScenarioConfiger configer = mRunningConfigers[k];
                    if (configer == null || configer.getVolumeBtDynamic() < 0) {
                        k++;
                    } else {
                        filter.addAction(ACTION_SECOND_BT_CONNECTION);
                        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                        if (adapter != null && adapter.isDeviceConnected(1)) {
                            passengerbluetoothConnected = true;
                            String str = TAG;
                            LogUtil.i(str, "volumeMonitor,passengerbluetoothConnected:" + passengerbluetoothConnected);
                        }
                    }
                }
            } else {
                LogUtil.i(TAG, "volumeMonitor,no PassengerBluetooth feature");
            }
            this.mContext.registerReceiver(this.mVolumeReceiver, filter);
            return;
        }
        LogUtil.w(TAG, "already monitor volume");
    }

    private void volumeUnMonitor() {
        if (this.mVolumeMonitor) {
            this.mVolumeMonitor = false;
            passengerbluetoothConnected = false;
            this.mContext.unregisterReceiver(this.mVolumeReceiver);
            return;
        }
        LogUtil.w(TAG, "already un monitor volume");
    }

    private void handleVolumeEventWithConfiger(UserScenarioConfiger configer, int streamType, String volumePackage) {
        if (3 == streamType && !passengerbluetoothConnected) {
            if (!mVolumeIgnorePkgList.contains(volumePackage) && configer != null && configer.getVolumeDynamic() > 0) {
                lastMusicVolume = mAudioManager.getStreamVolume(3);
                if (lastMusicVolume < 1) {
                    String str = TAG;
                    LogUtil.i(str, "handleVolumeEvent, volume invalid:" + lastMusicVolume);
                    return;
                }
                mUserScenarioHandler.removeMessages(8);
                Message msg = mUserScenarioHandler.obtainMessage(8, lastMusicVolume, 0, configer);
                mUserScenarioHandler.sendMessageDelayed(msg, 500L);
            }
        } else if (13 == streamType && passengerbluetoothConnected && configer != null && configer.getVolumeBtDynamic() > 0) {
            lastPassengerBtVolume = mAudioManager.getStreamVolume(13);
            if (lastPassengerBtVolume < 1) {
                String str2 = TAG;
                LogUtil.i(str2, "handleVolumeEvent, passenger bt volume invalid:" + lastPassengerBtVolume);
                return;
            }
            mUserScenarioHandler.removeMessages(9);
            Message msg2 = mUserScenarioHandler.obtainMessage(9, lastPassengerBtVolume, 0, configer);
            mUserScenarioHandler.sendMessageDelayed(msg2, 500L);
        }
    }

    private void handleVolumeEvent(Intent intent) {
        int streamType = intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1);
        String volumePackage = intent.getStringExtra(EXTRA_VOLCHANGE_PACKAGENAME);
        for (int i = 0; i < mConfigGroup.length; i++) {
            UserScenarioConfiger configer = mRunningConfigers[i];
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("handleVolumeEvent,pkg=");
            sb.append(volumePackage);
            sb.append(",streamType=");
            sb.append(streamType);
            sb.append(",current scenario:");
            sb.append(configer != null ? configer.getScenarioName() + ",vol dynamic:" + configer.getVolumeDynamic() : "idle");
            sb.append(",passengerbtConnected:");
            sb.append(passengerbluetoothConnected);
            LogUtil.i(str, sb.toString());
            if (configer != null) {
                handleVolumeEventWithConfiger(configer, streamType, volumePackage);
            }
        }
    }

    private void handlePassengerBluetoothStatus(Intent intent) {
        int state = intent.getIntExtra("state", 0);
        String str = TAG;
        LogUtil.i(str, "handlePassengerBluetoothStatus:" + state);
        if (2 == state) {
            passengerbluetoothConnected = true;
            for (int k = 0; k < mConfigGroup.length; k++) {
                UserScenarioConfiger configer = mRunningConfigers[k];
                if (configer != null && configer.getVolumeBtDynamic() > 0) {
                    configer.configMediaStreamVolume(false);
                    configer.configPassengerBtVolume(true);
                    configer.saveData();
                }
            }
        } else if (passengerbluetoothConnected) {
            passengerbluetoothConnected = false;
            for (int k2 = 0; k2 < mConfigGroup.length; k2++) {
                UserScenarioConfiger configer2 = mRunningConfigers[k2];
                if (configer2 != null && configer2.getVolumeBtDynamic() > 0) {
                    configer2.configPassengerBtVolume(false);
                    configer2.configMediaStreamVolume(true);
                    configer2.saveData();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lastConfigerExitInner(UserScenarioConfiger current, UserScenarioConfiger lastConfiger) {
        int lastGroup = lastConfiger.getGroupIndex();
        IBinder[] iBinderArr = mAppBinders;
        if (iBinderArr[lastGroup] != null) {
            try {
                if (iBinderArr[lastGroup] != null) {
                    iBinderArr[lastGroup].unlinkToDeath(mAppDeathRecipients[lastGroup], 0);
                    mAppBinders[lastGroup] = null;
                    mAppDeathRecipients[lastGroup] = null;
                }
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "lastConfigerExitInner e=" + e);
            }
        }
        lastConfiger.setCurrentFriend(current.getScenarioName());
        lastConfiger.setExitReason("rScenarioSwitch");
        lastConfiger.configVolume(false);
        lastConfiger.voiceOnOff(false);
        lastConfiger.exit();
        sendBILog(lastConfiger.getScenarioName(), this.mContext.getPackageName(), "rScenarioSwitch", "2", "2");
        lastConfiger.setStatus(0);
        lastConfiger.setSubName(null);
        notifyScenarioStatus(lastConfiger.getScenarioName(), 0);
    }

    private int getScreenId(String extra) {
        try {
            JSONObject obj = new JSONObject(extra);
            int screenId = obj.optInt("screenId", 0);
            return screenId;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "getDisPlayId e=" + e + ",extra=" + extra);
            return 0;
        }
    }

    private void handleScenarioParameters(UserScenarioConfiger configer, String jsonStr) {
        try {
            JSONObject jsonData = new JSONObject(jsonStr);
            Iterator iterator = jsonData.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                char c = 65535;
                if (key.hashCode() == 1594376939 && key.equals("pScreenUse")) {
                    c = 0;
                }
                if (c == 0) {
                    int panelCover = jsonData.optInt("pScreenUse", 0);
                    configer.setPanelCover(panelCover);
                }
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "handleScenarioParameters,e=" + e);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void notifyToCanBus(boolean enter, UserScenarioConfiger configer) {
        CarBcmManager bcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        try {
            String scenario = configer.getScenarioName();
            boolean z = false;
            int status = enter ? 1 : 0;
            LogUtil.i(TAG, "notifyToCanBus,scenario:" + scenario + ",enter:" + enter + ",status=" + status);
            switch (scenario.hashCode()) {
                case -631527828:
                    if (scenario.equals("spacecapsule_mode_movie")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case -626092525:
                    if (scenario.equals("spacecapsule_mode_sleep")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 885092324:
                    if (scenario.equals("meditation_mode")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 1909445825:
                    if (scenario.equals("5d_cinema_mode")) {
                        break;
                    }
                    z = true;
                    break;
                default:
                    z = true;
                    break;
            }
            if (!z) {
                bcmManager.setX5dCinemaModeStatus(status);
            } else if (z) {
                bcmManager.setXmeditationModeStatus(status);
            } else if (z) {
                bcmManager.setXmovieModeStatus(status);
            } else if (z) {
                bcmManager.setXsleepModeStatus(status);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "notifyToCanBus e=" + e);
        }
    }

    private int getScenarioGroup(String scenario) {
        UserScenarioConfiger configer = this.mConfigMap.get(scenario);
        if (configer == null) {
            String str = TAG;
            LogUtil.w(str, "getScenarioGroup,no configer for scenario:" + scenario);
            return -1;
        }
        return configer.getGroupIndex();
    }

    private void showExitWindow(final UserScenarioConfiger configer) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$laqzb9zNuxhaD1hYarXhFag9y2c
            @Override // java.lang.Runnable
            public final void run() {
                UserScenarioService.this.lambda$showExitWindow$10$UserScenarioService(configer);
            }
        });
    }

    public /* synthetic */ void lambda$showExitWindow$10$UserScenarioService(final UserScenarioConfiger configer) {
        if (this.mDialog == null) {
            LogUtil.d(TAG, "showExitWindow,create scenario Dialog");
            this.mDialog = new XDialog(this.mContext);
        }
        if (this.mDialog.isShowing()) {
            LogUtil.i(TAG, "showExitWindow, close last window first");
            this.mDialog.dismiss();
        }
        String resId = "mode_exit_" + configer.getScenarioName();
        String exitStr = LocaleStrings.getInstance().getString(resId);
        if (exitStr == null) {
            exitStr = "Confirm exit " + configer.getScenarioName() + " ?";
        }
        this.mDialog.setMessage(exitStr).setPositiveButtonInterceptDismiss(true).setPositiveButton(this.mContext.getText(R.string.dialog_cofirm), new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$VAUPpdQ3qSmMhEHSbCRq-tQVJeE
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public final void onClick(XDialog xDialog, int i) {
                UserScenarioService.this.lambda$showExitWindow$7$UserScenarioService(configer, xDialog, i);
            }
        }).setNegativeButtonInterceptDismiss(true).setNegativeButton(this.mContext.getText(R.string.dialog_cancel), new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$UuwHjX-CG9soJtLURQhzy_s7l24
            @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
            public final void onClick(XDialog xDialog, int i) {
                UserScenarioService.this.lambda$showExitWindow$8$UserScenarioService(configer, xDialog, i);
            }
        });
        this.mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.xiaopeng.xuiservice.userscenario.-$$Lambda$UserScenarioService$R8DBY_TlAHS0ngsoguxbqHnZZgg
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                LogUtil.i(UserScenarioService.TAG, "showExitWindow, window closed");
            }
        });
        this.mDialog.setSystemDialog(2008);
        this.mDialog.show();
    }

    public /* synthetic */ void lambda$showExitWindow$7$UserScenarioService(UserScenarioConfiger configer, XDialog dialog, int which) {
        String str = TAG;
        LogUtil.i(str, "showExitWindow,on click yes,configer:" + configer);
        this.mDialog.dismiss();
        exitScenarioDirect(configer);
    }

    public /* synthetic */ void lambda$showExitWindow$8$UserScenarioService(UserScenarioConfiger configer, XDialog dialog, int which) {
        String str = TAG;
        LogUtil.i(str, "showExitWindow,on click no,configer:" + configer);
        this.mDialog.dismiss();
    }

    private void exitScenarioDirect(UserScenarioConfiger configer) {
        try {
            String scenario = configer.getScenarioName();
            sendBILog(scenario, this.mContext.getPackageName(), ResponseParams.RESPONSE_KEY_SUCCESS, "2", "1");
            configer.setStatus(3);
            mStatusToBe[configer.getGroupIndex()] = 0;
            configer.setExitReason("rAppRequest");
            mUserScenarioHandler.obtainMessage(2, configer).sendToTarget();
            mUserScenarioHandler.removeMessages(4);
            Message checkMsg = mUserScenarioHandler.obtainMessage(4, configer);
            mUserScenarioHandler.sendMessageDelayed(checkMsg, 4000L);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "exitScenarioDirect,e=" + e);
        }
    }
}
