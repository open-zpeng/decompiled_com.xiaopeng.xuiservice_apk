package com.xiaopeng.xuiservice.iot.manager;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.hvac.CarHvacManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Intent;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.devices.FragranceDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.IServiceConn;
import com.xiaopeng.xuiservice.innerutils.datalog.PeripheralDeviceDataBi;
import com.xiaopeng.xuiservice.iot.manager.FragranceManager;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ConnectivityUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class FragranceManager extends BaseDeviceManager {
    private static final int CARSVC_STATE_CONNECTED = 1;
    private static final int CARSVC_STATE_DISCONNECTED = 0;
    private static final String IOT_PACKAGE = "com.xiaopeng.aiot";
    private static final int STATE_UNINIT = -1;
    private boolean bMonitorProp;
    private int mActiveChanel;
    private int mCOConcentration;
    private CarHvacManager.CarHvacEventCallback mCarHvacEventCallback;
    private volatile CarHvacManager mCarHvacManager;
    private int mCarServiceState;
    private int[] mChannelsType;
    private int mConcentration;
    private IServiceConn mConnListener;
    private IEventListener mDeviceEventListener;
    private FragranceDevice mFragranceDevice;
    private int mSwitchState;
    private static final ArrayList<BaseDevice> mDeviceList = new ArrayList<>();
    private static final String TAG = FragranceManager.class.getSimpleName() + "##";
    private static final String mFeatureCode = XUIConfig.getFeatureType(XUIConfig.PROPERTY_SFS_FEATURE);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class FragranceEventCallback implements CarHvacManager.CarHvacEventCallback {
        public FragranceEventCallback() {
        }

        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            Integer[] v;
            if (carPropertyValue != null) {
                String str = FragranceManager.TAG;
                LogUtil.d(str, "onChangeEvent: " + carPropertyValue);
                switch (carPropertyValue.getPropertyId()) {
                    case 557849143:
                        int value = ((Integer) carPropertyValue.getValue()).intValue();
                        String str2 = FragranceManager.TAG;
                        LogUtil.i(str2, "mSwitchState=" + FragranceManager.this.mSwitchState + ",value=" + value);
                        if (FragranceManager.this.mSwitchState != value) {
                            FragranceManager.this.mSwitchState = value;
                            if (FragranceManager.this.bMonitorProp) {
                                FragranceManager.this.mDeviceEventListener.onDeviceEvent(FragranceManager.this.mFragranceDevice.getDeviceId(), "switch_state", String.valueOf(value));
                                return;
                            }
                            return;
                        }
                        return;
                    case 557849145:
                        int value2 = ((Integer) carPropertyValue.getValue()).intValue();
                        if (FragranceManager.this.mActiveChanel != value2) {
                            String str3 = FragranceManager.TAG;
                            LogUtil.i(str3, "mActiveChanel:" + FragranceManager.this.mActiveChanel + ",value:" + value2);
                            FragranceManager.this.mActiveChanel = value2;
                            if (FragranceManager.this.bMonitorProp) {
                                FragranceManager.this.mDeviceEventListener.onDeviceEvent(FragranceManager.this.mFragranceDevice.getDeviceId(), "active", String.valueOf(value2));
                                return;
                            }
                            return;
                        }
                        return;
                    case 557849150:
                        int value3 = ((Integer) carPropertyValue.getValue()).intValue();
                        if (FragranceManager.this.mConcentration != value3) {
                            String str4 = FragranceManager.TAG;
                            LogUtil.i(str4, "mConcentration:" + FragranceManager.this.mConcentration + ",value:" + value3);
                            FragranceManager.this.mConcentration = value3;
                            if (FragranceManager.this.bMonitorProp) {
                                FragranceManager.this.mDeviceEventListener.onDeviceEvent(FragranceManager.this.mFragranceDevice.getDeviceId(), "concentration", String.valueOf(value3));
                                return;
                            }
                            return;
                        }
                        return;
                    case 557849151:
                        final int value4 = ((Integer) carPropertyValue.getValue()).intValue();
                        if (FragranceManager.this.mCOConcentration != value4) {
                            String str5 = FragranceManager.TAG;
                            LogUtil.i(str5, "mCOConcentration:" + FragranceManager.this.mCOConcentration + ",value:" + value4);
                            FragranceManager.this.mCOConcentration = value4;
                            if (FragranceManager.this.bMonitorProp) {
                                FragranceManager.this.mDeviceEventListener.onDeviceEvent(FragranceManager.this.mFragranceDevice.getDeviceId(), "co_concentration", String.valueOf(value4));
                            }
                            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FragranceManager$FragranceEventCallback$Se2IRP0fUZln0dnSR201J0co0Vc
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FragranceManager.FragranceEventCallback.this.lambda$onChangeEvent$1$FragranceManager$FragranceEventCallback(value4);
                                }
                            });
                            return;
                        }
                        return;
                    case 557914680:
                        if (!(carPropertyValue.getValue() instanceof Integer[]) || (v = (Integer[]) carPropertyValue.getValue()) == null) {
                            return;
                        }
                        boolean bChanged = false;
                        int length = FragranceManager.this.mChannelsType.length;
                        int i = 0;
                        while (i < v.length) {
                            if (i < length && FragranceManager.this.mChannelsType[i] != v[i].intValue()) {
                                bChanged = true;
                                int channelStatus = (1 == FragranceManager.this.mSwitchState && FragranceManager.this.mActiveChanel == i) ? 1 : 0;
                                if (FragranceManager.this.mChannelsType[i] == 0) {
                                    boolean isGearPLevel = FragranceManager.this.isGearInP();
                                    String str6 = FragranceManager.TAG;
                                    LogUtil.i(str6, "new channel:" + i + " inserted,gear in P=" + isGearPLevel);
                                    if (isGearPLevel) {
                                        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.-$$Lambda$FragranceManager$FragranceEventCallback$jDMx7AgdBP-FZBxsbvkLOrQoQ3o
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                FragranceManager.FragranceEventCallback.this.lambda$onChangeEvent$0$FragranceManager$FragranceEventCallback();
                                            }
                                        });
                                    }
                                    PeripheralDeviceDataBi.getInstance().handleIotDeviceStatus(FragranceManager.this.mFragranceDevice, String.valueOf(v[i]), String.valueOf(channelStatus), true);
                                } else if (-1 != FragranceManager.this.mChannelsType[i] && v[i].intValue() == 0) {
                                    PeripheralDeviceDataBi.getInstance().handleIotDeviceStatus(FragranceManager.this.mFragranceDevice, String.valueOf(FragranceManager.this.mChannelsType[i]), String.valueOf(channelStatus), false);
                                }
                                FragranceManager.this.mChannelsType[i] = v[i].intValue();
                            }
                            i++;
                        }
                        if (bChanged) {
                            String str7 = FragranceManager.TAG;
                            LogUtil.i(str7, "channel changed:" + FragranceManager.this.mChannelsType[0] + "," + FragranceManager.this.mChannelsType[1] + "," + FragranceManager.this.mChannelsType[2]);
                            if (FragranceManager.this.bMonitorProp) {
                                IEventListener iEventListener = FragranceManager.this.mDeviceEventListener;
                                String deviceId = FragranceManager.this.mFragranceDevice.getDeviceId();
                                FragranceManager fragranceManager = FragranceManager.this;
                                iEventListener.onDeviceEvent(deviceId, "channel_types", fragranceManager.formatChannelTypes(fragranceManager.mChannelsType));
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        public /* synthetic */ void lambda$onChangeEvent$0$FragranceManager$FragranceEventCallback() {
            FragranceManager.this.notifyEvent("notify_fragrance_insert");
        }

        public /* synthetic */ void lambda$onChangeEvent$1$FragranceManager$FragranceEventCallback(int value) {
            FragranceManager.this.notifyRawProp("co_concentration", String.valueOf(value));
        }

        public void onErrorEvent(int i, int i1) {
            String str = FragranceManager.TAG;
            LogUtil.e(str, "onErrorEvent i: " + i + " i1 : " + i1);
        }
    }

    /* loaded from: classes5.dex */
    private class FragranceServiceConn implements IServiceConn {
        private FragranceServiceConn() {
        }

        @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
        public void onConnectedCar() {
            LogUtil.d(FragranceManager.TAG, "onConnectedCar");
            FragranceManager.this.getCarHvacManager();
            FragranceManager.this.initChannelTypes();
        }

        @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
        public void onDisconnectCar() {
            LogUtil.d(FragranceManager.TAG, "onDisconnectCar");
            FragranceManager.this.mCarHvacManager = null;
            FragranceManager.this.mCarServiceState = 0;
        }

        @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
        public void onConnectXUI() {
            LogUtil.d(FragranceManager.TAG, "onConnectXUI");
        }

        @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
        public void onDisconnectXUI() {
            LogUtil.d(FragranceManager.TAG, "onDisconnectXUI");
        }
    }

    private FragranceManager() {
        this.mConnListener = null;
        this.mCarHvacEventCallback = null;
        this.mCarServiceState = -1;
        this.mSwitchState = -1;
        this.mChannelsType = new int[]{-1, -1, -1};
        this.mActiveChanel = -1;
        this.mConcentration = -1;
        this.mCOConcentration = -1;
        this.bMonitorProp = false;
    }

    /* loaded from: classes5.dex */
    private static class FragranceManagerHolder {
        private static final FragranceManager sInstance = new FragranceManager();

        private FragranceManagerHolder() {
        }
    }

    public static FragranceManager getInstance() {
        return FragranceManagerHolder.sInstance;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void init() {
        LogUtil.setModuleLogLevel(TAG, 1);
        this.mConnListener = new FragranceServiceConn();
        this.mCarHvacEventCallback = new FragranceEventCallback();
        String macId = ConnectivityUtil.getWifiMac();
        StringBuilder sb = new StringBuilder();
        sb.append("Fragrance_");
        sb.append(macId != null ? macId.substring(6) : "0");
        String deviceId = sb.toString();
        this.mFragranceDevice = new FragranceDevice("MyFragrance", deviceId, "Fragrance");
        mDeviceList.add(this.mFragranceDevice);
        CarClientManager.getInstance().addConnectionListener(this.mConnListener);
        CarClientManager.getInstance().addHvacManagerListener(this.mCarHvacEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public List<BaseDevice> getDevice() {
        selfGetPropertyMap();
        return mDeviceList;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void setPropertyMap(String deviceId, Map<String, String> propertyMap) {
        LogUtil.i(TAG, "deviceId:" + deviceId + ",setPropertyMap:" + propertyMap);
        int switchVal = -1;
        Iterator<String> it = propertyMap.keySet().iterator();
        while (true) {
            char c = 65535;
            if (!it.hasNext()) {
                break;
            }
            String prop = it.next();
            String propVal = propertyMap.get(prop);
            int hashCode = prop.hashCode();
            if (hashCode != -1422950650) {
                if (hashCode != -410557331) {
                    if (hashCode == 1585555398 && prop.equals("switch_state")) {
                        c = 0;
                    }
                } else if (prop.equals("concentration")) {
                    c = 2;
                }
            } else if (prop.equals("active")) {
                c = 1;
            }
            if (c == 0) {
                switchVal = Integer.parseInt(propVal);
            } else if (c == 1) {
                setActiveChannel(Integer.parseInt(propVal));
            } else if (c == 2) {
                setConcentration(Integer.parseInt(propVal));
            }
        }
        if (-1 != switchVal) {
            setSwitch(switchVal);
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public Map<String, String> getPropertyMap(String deviceId) {
        selfGetPropertyMap();
        return this.mFragranceDevice.getPropertyMap();
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void monitorDevice(String deviceId, boolean bMonitor) {
        if (deviceId.equals(this.mFragranceDevice.getDeviceId())) {
            this.bMonitorProp = bMonitor;
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void sendCommand(String deviceId, String cmd, String params) {
        char c;
        LogUtil.i(TAG, "sendCommand,id=" + deviceId + ",cmd=" + cmd + ",params=" + params);
        int hashCode = cmd.hashCode();
        if (hashCode == 591924790) {
            if (cmd.equals("setSwitch")) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 905242075) {
            if (hashCode == 935167787 && cmd.equals("setConcentration")) {
                c = 2;
            }
            c = 65535;
        } else {
            if (cmd.equals("setActiveChannel")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            setSwitch(Integer.parseInt(params));
        } else if (c == 1) {
            setActiveChannel(Integer.parseInt(params));
        } else if (c == 2) {
            setConcentration(Integer.parseInt(params));
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDeviceListener(IEventListener listener) {
        this.mDeviceEventListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void dump(PrintWriter pw, String[] args) {
        pw.println("dump " + TAG);
        pw.println("feature code=" + mFeatureCode);
        pw.println("property status:");
        pw.println("  switchStat=" + this.mSwitchState);
        pw.println("  channels count:" + this.mChannelsType);
        for (int i = 0; i < this.mChannelsType.length; i++) {
            pw.println("  channel " + i + ":" + this.mChannelsType[i]);
        }
        pw.println("  activeChannel=" + this.mActiveChanel);
        pw.println("  concentration=" + this.mConcentration);
        pw.println("  CO Concentration=" + this.mCOConcentration);
        pw.println("device=" + this.mFragranceDevice);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public boolean isEnabled() {
        return "1".equals(mFeatureCode);
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager
    public void addDevice(BaseDevice device) {
    }

    public int getSwitchStatus() {
        getCarHvacManager();
        if (this.mCarHvacManager == null) {
            return -1;
        }
        try {
            int status = this.mCarHvacManager.getSfsSwitchStatus();
            return status;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "getSwitchStatus fail:" + e.toString());
            return -1;
        }
    }

    public void setSwitch(int enable) {
        getCarHvacManager();
        if (this.mCarHvacManager != null) {
            try {
                this.mCarHvacManager.setSfsSwitch(enable);
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setSwitch fail:" + e.toString());
            }
        }
    }

    public int getActiveChannel() {
        getCarHvacManager();
        if (this.mCarHvacManager == null) {
            return -1;
        }
        try {
            int channel = this.mCarHvacManager.getSfsChannel();
            return channel;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "getActiveChannel fail:" + e.toString());
            return -1;
        }
    }

    public void setActiveChannel(int channel) {
        getCarHvacManager();
        if (this.mCarHvacManager != null) {
            try {
                this.mCarHvacManager.setSfsChannel(channel);
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setActiveChannel fail:" + e.toString());
            }
        }
    }

    public int getConcentration() {
        getCarHvacManager();
        if (this.mCarHvacManager == null) {
            return -1;
        }
        try {
            int concentration = this.mCarHvacManager.getSfsConcentrationStatus();
            return concentration;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "getConcentration fail:" + e.toString());
            return -1;
        }
    }

    public int getCOConcentration() {
        getCarHvacManager();
        if (this.mCarHvacManager == null) {
            return -1;
        }
        try {
            int coCOConcentration = this.mCarHvacManager.getCoConcentrationStatus();
            return coCOConcentration;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "get CO Concentration fail:" + e.toString());
            return -1;
        }
    }

    public void setConcentration(int value) {
        getCarHvacManager();
        if (this.mCarHvacManager != null) {
            try {
                this.mCarHvacManager.setSfsConcentration(value);
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "setConcentration fail:" + e.toString());
            }
        }
    }

    public int[] getTypeInChannels() {
        getCarHvacManager();
        if (this.mCarHvacManager == null) {
            return null;
        }
        try {
            return this.mCarHvacManager.getSfsTypeInChannels();
        } catch (Exception e) {
            LogUtil.e(TAG, "getTypeInChannels fail:" + e.toString());
            int[] channelValues = new int[3];
            for (int i : channelValues) {
            }
            return channelValues;
        }
    }

    public void selfGetPropertyMap() {
        int switchStat = getSwitchStatus();
        int[] channelTypes = getTypeInChannels();
        int activeChannel = getActiveChannel();
        int concentration = getConcentration();
        int coConcentration = getCOConcentration();
        Map<String, String> propertyMap = this.mFragranceDevice.getPropertyMap();
        if (propertyMap == null) {
            propertyMap = new HashMap<>();
            this.mFragranceDevice.setPropertyMap(propertyMap);
        }
        propertyMap.put("switch_state", String.valueOf(switchStat));
        propertyMap.put("active", String.valueOf(activeChannel));
        propertyMap.put("concentration", String.valueOf(concentration));
        propertyMap.put("co_concentration", String.valueOf(coConcentration));
        String formatStr = formatChannelTypes(channelTypes);
        propertyMap.put("channel_types", formatStr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initChannelTypes() {
        int[] channelsType = getTypeInChannels();
        if (channelsType != null) {
            int i = 0;
            while (true) {
                int[] iArr = this.mChannelsType;
                if (i < iArr.length) {
                    iArr[i] = channelsType[i];
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEvent(String event) {
        Intent i = new Intent("com.xiaopeng.intent.action.AIOT_NOTIFICATION");
        i.putExtra("bd_event_type", "type_event_stat_notify");
        i.putExtra("key_event_stat_notify", event);
        i.addFlags(20971520);
        i.setPackage(IOT_PACKAGE);
        BroadcastManager.getInstance().sendBroadcast(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyRawProp(String prop, String value) {
        Intent i = new Intent("com.xiaopeng.intent.action.AIOT_NOTIFICATION");
        i.putExtra("bd_event_type", "type_event_prop_raw");
        i.putExtra("extra_device_type", "Fragrance");
        i.putExtra("extra_device_property_name", prop);
        i.putExtra("extra_device_property_value", value);
        i.addFlags(20971520);
        i.setPackage(IOT_PACKAGE);
        BroadcastManager.getInstance().sendBroadcast(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatChannelTypes(int[] channelsType) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < channelsType.length; i++) {
            sb.append(channelsType[i]);
            if (i < channelsType.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCarHvacManager() {
        if (this.mCarHvacManager == null) {
            this.mCarHvacManager = CarClientManager.getInstance().getCarManager("hvac");
            if (this.mCarHvacManager != null) {
                this.mCarServiceState = 1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGearInP() {
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
}
