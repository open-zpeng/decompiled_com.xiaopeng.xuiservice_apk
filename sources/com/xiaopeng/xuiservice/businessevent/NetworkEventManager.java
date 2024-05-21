package com.xiaopeng.xuiservice.businessevent;

import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.alibaba.mtl.appmonitor.AppMonitorDelegate;
import com.xiaopeng.lib.framework.configuration.ConfigurationModuleEntry;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.configurationmodule.ConfigurationChangeEvent;
import com.xiaopeng.lib.framework.moduleinterface.configurationmodule.IConfiguration;
import com.xiaopeng.lib.framework.moduleinterface.configurationmodule.IConfigurationData;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/* loaded from: classes5.dex */
public class NetworkEventManager {
    private static final String APPLICATION_ID = "com.xiaopeng.xuiservice";
    private static final String KEY_PROBE_CONFIG = "probe_config";
    private static final String TAG = NetworkEventManager.class.getSimpleName();
    private static boolean configurationInited = false;
    private static final String notifyPackageName = "com.xiaopeng.networkmonitor";
    private Application mApplication;
    private boolean moduleInit;

    /* loaded from: classes5.dex */
    private static class NetworkEventManagerHolder {
        private static final NetworkEventManager sInstance = new NetworkEventManager();

        private NetworkEventManagerHolder() {
        }
    }

    private NetworkEventManager() {
        this.moduleInit = false;
        DumpDispatcher.registerDump("networkevent", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$NetworkEventManager$-oAUM9V-QnOxuLOd3Bukbba4tFk
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                NetworkEventManager.this.lambda$new$0$NetworkEventManager(printWriter, strArr);
            }
        });
    }

    public static NetworkEventManager getInstance() {
        return NetworkEventManagerHolder.sInstance;
    }

    public void init() {
        this.mApplication = ActivityThread.currentActivityThread().getApplication();
        BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$NetworkEventManager$8FKZQOkHEamNSrzudSH7LIlCccw
            @Override // java.lang.Runnable
            public final void run() {
                NetworkEventManager.this.lambda$init$1$NetworkEventManager();
            }
        });
        if (BroadcastManager.isBootComplete()) {
            lambda$init$1$NetworkEventManager();
        }
        ArrayList<String> filter = new ArrayList<>();
        filter.add("android.net.conn.CONNECTIVITY_CHANGE");
        BroadcastManager.getInstance().registerListener(new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$NetworkEventManager$TjTXLXC1he9b8OZ5x3R5ca1HZ-k
            @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
            public final void onReceive(Context context, Intent intent) {
                NetworkEventManager.this.lambda$init$3$NetworkEventManager(context, intent);
            }
        }, filter);
    }

    public /* synthetic */ void lambda$init$3$NetworkEventManager(Context context, Intent intent) {
        String str = TAG;
        LogUtil.i(str, "get broadcast=" + intent);
        String action = intent.getAction();
        if (((action.hashCode() == -1172645946 && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) ? (char) 0 : (char) 65535) == 0) {
            if (!configurationInited) {
                LogUtil.i(TAG, "network changed but configuration not ready");
            } else {
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$NetworkEventManager$es0ZK4mT120u4mS51UuQRyqNkq8
                    @Override // java.lang.Runnable
                    public final void run() {
                        NetworkEventManager.this.lambda$init$2$NetworkEventManager();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: getConfigAndNotify */
    public void lambda$initConfigurationModuleInner$5$NetworkEventManager() {
        try {
            String configurationValue = getConfigInterface().getConfiguration(KEY_PROBE_CONFIG, null);
            String str = TAG;
            LogUtil.i(str, "getConfigAndNotify,get configurationValue=" + configurationValue + ",configurationInited=" + configurationInited);
            if (configurationValue != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put(KEY_PROBE_CONFIG, configurationValue);
                BusinessEventManager.getInstance().notifyApp(notifyPackageName, map);
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "getConfigAndNotify,e=" + e);
        }
    }

    /* renamed from: dump */
    public void lambda$new$0$NetworkEventManager(PrintWriter pw, String[] args) {
        String str = TAG;
        LogUtil.i(str, "dump " + TAG);
        String configurationValue = getConfigInterface().getConfiguration(KEY_PROBE_CONFIG, AppMonitorDelegate.DEFAULT_VALUE);
        pw.println("get config=" + configurationValue);
    }

    /* loaded from: classes5.dex */
    private class TestData implements IConfigurationData {
        private TestData() {
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.configurationmodule.IConfigurationData
        public String getKey() {
            return "testData";
        }

        @Override // com.xiaopeng.lib.framework.moduleinterface.configurationmodule.IConfigurationData
        public String getValue() {
            return "testData";
        }
    }

    private void postTest() {
        IConfigurationData data = new TestData();
        List<IConfigurationData> dataList = new ArrayList<>();
        dataList.add(data);
        ConfigurationChangeEvent event = new ConfigurationChangeEvent();
        event.setChangeList(dataList);
        EventBus.getDefault().post(event);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onConfigurationChanged(ConfigurationChangeEvent event) {
        List<IConfigurationData> data = event.getChangeList();
        if (data != null && data.size() > 0) {
            LogUtil.d(TAG, "onConfigurationChanged()");
            for (IConfigurationData config : data) {
                String key = config.getKey();
                String str = TAG;
                LogUtil.i(str, "get key=" + key);
                if (KEY_PROBE_CONFIG.equals(key)) {
                    LogUtil.i(TAG, "KEY_PROBE_CONFIG hit");
                    Object value = config.getValue();
                    if (value instanceof String) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(KEY_PROBE_CONFIG, (String) value);
                        BusinessEventManager.getInstance().notifyApp(notifyPackageName, map);
                    } else {
                        String str2 = TAG;
                        LogUtil.w(str2, "onConfigurationChanged,value class=" + value.getClass());
                    }
                }
            }
        }
    }

    private IConfiguration getConfigInterface() {
        return (IConfiguration) Module.get(ConfigurationModuleEntry.class).get(IConfiguration.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: initConfigurationModule */
    public synchronized void lambda$init$1$NetworkEventManager() {
        if (this.moduleInit) {
            return;
        }
        this.moduleInit = true;
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$NetworkEventManager$Kzq7oW-lN9m2ao6XkS9n0F1T8Uo
            @Override // java.lang.Runnable
            public final void run() {
                NetworkEventManager.this.lambda$initConfigurationModule$4$NetworkEventManager();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: initConfigurationModuleInner */
    public void lambda$initConfigurationModule$4$NetworkEventManager() {
        LogUtil.d(TAG, "initConfigurationModule");
        if (!EventBus.getDefault().isRegistered(this)) {
            LogUtil.d(TAG, "register listener");
            EventBus.getDefault().register(this);
        }
        Module.register(ConfigurationModuleEntry.class, new ConfigurationModuleEntry());
        getConfigInterface().init(this.mApplication, APPLICATION_ID);
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$NetworkEventManager$uQdKB3ZFeZh-nqPwYButNAGSzQI
            @Override // java.lang.Runnable
            public final void run() {
                NetworkEventManager.this.lambda$initConfigurationModuleInner$5$NetworkEventManager();
            }
        }, 5000L);
        configurationInited = true;
    }
}
