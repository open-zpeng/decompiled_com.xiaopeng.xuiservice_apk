package com.xiaopeng.xuiservice.uvccamera.usb;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import com.xiaopeng.xuiservice.uvccamera.utils.HandlerThreadHandler;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.UByte;
import org.opencv.ml.DTrees;
/* loaded from: classes5.dex */
public final class USBMonitor {
    public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_PERMISSION_BASE = "com.serenegiant.USB_PERMISSION.";
    private static final int CHECK_DEVICE_RUNNABLE_DELAY = 150;
    private static final String TAG = USBMonitor.class.getSimpleName();
    private static final int USB_DIR_IN = 128;
    private static final int USB_DIR_OUT = 0;
    private static final int USB_DT_BOS = 15;
    private static final int USB_DT_CONFIG = 2;
    private static final int USB_DT_CS_CONFIG = 34;
    private static final int USB_DT_CS_DEVICE = 33;
    private static final int USB_DT_CS_ENDPOINT = 37;
    private static final int USB_DT_CS_INTERFACE = 36;
    private static final int USB_DT_CS_RADIO_CONTROL = 35;
    private static final int USB_DT_CS_STRING = 35;
    private static final int USB_DT_DEBUG = 10;
    private static final int USB_DT_DEVICE = 1;
    private static final int USB_DT_DEVICE_CAPABILITY = 16;
    private static final int USB_DT_DEVICE_QUALIFIER = 6;
    private static final int USB_DT_DEVICE_SIZE = 18;
    private static final int USB_DT_ENCRYPTION_TYPE = 14;
    private static final int USB_DT_ENDPOINT = 5;
    private static final int USB_DT_INTERFACE = 4;
    private static final int USB_DT_INTERFACE_ASSOCIATION = 11;
    private static final int USB_DT_INTERFACE_POWER = 8;
    private static final int USB_DT_KEY = 13;
    private static final int USB_DT_OTG = 9;
    private static final int USB_DT_OTHER_SPEED_CONFIG = 7;
    private static final int USB_DT_PIPE_USAGE = 36;
    private static final int USB_DT_RPIPE = 34;
    private static final int USB_DT_SECURITY = 12;
    private static final int USB_DT_SS_ENDPOINT_COMP = 48;
    private static final int USB_DT_STRING = 3;
    private static final int USB_DT_WIRELESS_ENDPOINT_COMP = 17;
    private static final int USB_DT_WIRE_ADAPTER = 33;
    private static final int USB_RECIP_DEVICE = 0;
    private static final int USB_RECIP_ENDPOINT = 2;
    private static final int USB_RECIP_INTERFACE = 1;
    private static final int USB_RECIP_MASK = 31;
    private static final int USB_RECIP_OTHER = 3;
    private static final int USB_RECIP_PORT = 4;
    private static final int USB_RECIP_RPIPE = 5;
    private static final int USB_REQ_CLEAR_FEATURE = 1;
    private static final int USB_REQ_CS_DEVICE_GET = 160;
    private static final int USB_REQ_CS_DEVICE_SET = 32;
    private static final int USB_REQ_CS_ENDPOINT_GET = 162;
    private static final int USB_REQ_CS_ENDPOINT_SET = 34;
    private static final int USB_REQ_CS_INTERFACE_GET = 161;
    private static final int USB_REQ_CS_INTERFACE_SET = 33;
    private static final int USB_REQ_GET_CONFIGURATION = 8;
    private static final int USB_REQ_GET_DESCRIPTOR = 6;
    private static final int USB_REQ_GET_ENCRYPTION = 14;
    private static final int USB_REQ_GET_HANDSHAKE = 16;
    private static final int USB_REQ_GET_INTERFACE = 10;
    private static final int USB_REQ_GET_SECURITY_DATA = 19;
    private static final int USB_REQ_GET_STATUS = 0;
    private static final int USB_REQ_LOOPBACK_DATA_READ = 22;
    private static final int USB_REQ_LOOPBACK_DATA_WRITE = 21;
    private static final int USB_REQ_RPIPE_ABORT = 14;
    private static final int USB_REQ_RPIPE_RESET = 15;
    private static final int USB_REQ_SET_ADDRESS = 5;
    private static final int USB_REQ_SET_CONFIGURATION = 9;
    private static final int USB_REQ_SET_CONNECTION = 17;
    private static final int USB_REQ_SET_DESCRIPTOR = 7;
    private static final int USB_REQ_SET_ENCRYPTION = 13;
    private static final int USB_REQ_SET_FEATURE = 3;
    private static final int USB_REQ_SET_HANDSHAKE = 15;
    private static final int USB_REQ_SET_INTERFACE = 11;
    private static final int USB_REQ_SET_INTERFACE_DS = 23;
    private static final int USB_REQ_SET_ISOCH_DELAY = 49;
    private static final int USB_REQ_SET_SECURITY_DATA = 18;
    private static final int USB_REQ_SET_SEL = 48;
    private static final int USB_REQ_SET_WUSB_DATA = 20;
    private static final int USB_REQ_STANDARD_DEVICE_GET = 128;
    private static final int USB_REQ_STANDARD_DEVICE_SET = 0;
    private static final int USB_REQ_STANDARD_ENDPOINT_GET = 130;
    private static final int USB_REQ_STANDARD_ENDPOINT_SET = 2;
    private static final int USB_REQ_STANDARD_INTERFACE_GET = 129;
    private static final int USB_REQ_STANDARD_INTERFACE_SET = 1;
    private static final int USB_REQ_SYNCH_FRAME = 12;
    private static final int USB_REQ_VENDER_DEVICE_GET = 160;
    private static final int USB_REQ_VENDER_DEVICE_SET = 32;
    private static final int USB_REQ_VENDER_ENDPOINT_GET = 162;
    private static final int USB_REQ_VENDER_ENDPOINT_SET = 34;
    private static final int USB_REQ_VENDER_INTERFACE_GET = 161;
    private static final int USB_REQ_VENDER_INTERFACE_SET = 33;
    private static final int USB_TYPE_CLASS = 32;
    private static final int USB_TYPE_MASK = 96;
    private static final int USB_TYPE_RESERVED = 96;
    private static final int USB_TYPE_STANDARD = 0;
    private static final int USB_TYPE_VENDOR = 64;
    private final String ACTION_USB_PERMISSION;
    private final Handler mAsyncHandler;
    private volatile boolean mDestroyed;
    private HashSet<String> mDetectedDeviceKeys;
    private final Runnable mDeviceCheckRunnable;
    private List<DeviceFilter> mDeviceFilters;
    private HashSet<String> mHasPermissionDeviceKeys;
    private final Handler mListenerHandler;
    private final OnDeviceConnectListener mOnDeviceConnectListener;
    private final HashMap<UsbDevice, UsbControlBlock> mOpenedCtrlBlocks;
    private PendingIntent mPermissionIntent;
    private final UsbManager mUsbManager;
    private final BroadcastReceiver mUsbReceiver;
    private final WeakReference<Context> mWeakContext;

    /* loaded from: classes5.dex */
    public interface OnDeviceConnectListener {
        void onAttach(UsbDevice usbDevice);

        void onCancel(UsbDevice usbDevice);

        void onDetach(UsbDevice usbDevice);

        void onDeviceClose(UsbDevice usbDevice, UsbControlBlock usbControlBlock);

        void onDeviceOpen(UsbDevice usbDevice, UsbControlBlock usbControlBlock, boolean z);
    }

    public USBMonitor(Context context, OnDeviceConnectListener listener, Handler handler) {
        this.ACTION_USB_PERMISSION = ACTION_USB_PERMISSION_BASE + hashCode();
        this.mOpenedCtrlBlocks = new HashMap<>();
        this.mHasPermissionDeviceKeys = new HashSet<>();
        this.mDetectedDeviceKeys = new HashSet<>();
        this.mPermissionIntent = null;
        this.mDeviceFilters = new ArrayList();
        this.mUsbReceiver = new AnonymousClass1();
        this.mDeviceCheckRunnable = new AnonymousClass2();
        LogUtil.v(TAG, "USBMonitor:Constructor");
        if (listener == null) {
            throw new IllegalArgumentException("OnDeviceConnectListener should not null.");
        }
        this.mWeakContext = new WeakReference<>(context);
        this.mUsbManager = (UsbManager) context.getSystemService("usb");
        this.mOnDeviceConnectListener = listener;
        this.mListenerHandler = handler;
        this.mAsyncHandler = HandlerThreadHandler.createHandler(TAG);
        this.mDestroyed = false;
        String str = TAG;
        LogUtil.v(str, "USBMonitor:mUsbManager=" + this.mUsbManager);
    }

    public USBMonitor(Context context, OnDeviceConnectListener listener) {
        this(context, listener, new Handler(Looper.getMainLooper()));
    }

    public void destroy() {
        LogUtil.i(TAG, "destroy:");
        unregister();
        if (!this.mDestroyed) {
            this.mDestroyed = true;
            synchronized (this.mOpenedCtrlBlocks) {
                Set<UsbDevice> keys = this.mOpenedCtrlBlocks.keySet();
                if (keys != null) {
                    try {
                        for (UsbDevice key : keys) {
                            UsbControlBlock ctrlBlock = this.mOpenedCtrlBlocks.remove(key);
                            if (ctrlBlock != null) {
                                ctrlBlock.close();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "destroy:", e);
                    }
                }
                this.mOpenedCtrlBlocks.clear();
            }
            try {
                this.mAsyncHandler.getLooper().quit();
            } catch (Exception e2) {
                Log.e(TAG, "destroy:", e2);
            }
        }
    }

    public synchronized void register() throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        if (this.mPermissionIntent == null) {
            LogUtil.i(TAG, "register:");
            Context context = this.mWeakContext.get();
            if (context != null) {
                int flags = 0;
                if (Build.VERSION.SDK_INT >= 31) {
                    flags = 33554432;
                }
                this.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(this.ACTION_USB_PERMISSION), flags);
                IntentFilter filter = new IntentFilter(this.ACTION_USB_PERMISSION);
                filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                context.registerReceiver(this.mUsbReceiver, filter);
            }
            this.mDetectedDeviceKeys.clear();
            this.mAsyncHandler.postDelayed(this.mDeviceCheckRunnable, 150L);
        }
    }

    public synchronized void unregister() throws IllegalStateException {
        this.mDetectedDeviceKeys.clear();
        if (!this.mDestroyed) {
            this.mAsyncHandler.removeCallbacks(this.mDeviceCheckRunnable);
        }
        if (this.mPermissionIntent != null) {
            Context context = this.mWeakContext.get();
            if (context != null) {
                try {
                    context.unregisterReceiver(this.mUsbReceiver);
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
            this.mPermissionIntent = null;
        }
    }

    public synchronized boolean isRegistered() {
        boolean z;
        if (!this.mDestroyed) {
            z = this.mPermissionIntent != null;
        }
        return z;
    }

    public void setDeviceFilter(DeviceFilter filter) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.clear();
        this.mDeviceFilters.add(filter);
    }

    public void addDeviceFilter(DeviceFilter filter) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.add(filter);
    }

    public void removeDeviceFilter(DeviceFilter filter) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.remove(filter);
    }

    public void setDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.clear();
        this.mDeviceFilters.addAll(filters);
    }

    public void addDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.addAll(filters);
    }

    public void removeDeviceFilter(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.removeAll(filters);
    }

    public boolean isAvailableDevice(UsbDevice device) {
        if (device == null) {
            return false;
        }
        List<DeviceFilter> list = this.mDeviceFilters;
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (DeviceFilter filter : this.mDeviceFilters) {
            if (filter != null && filter.matches(device)) {
                return !filter.isExclude;
            }
        }
        return false;
    }

    public int getDeviceCount() throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return getDeviceList().size();
    }

    public List<UsbDevice> getDeviceList() throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return getDeviceList(this.mDeviceFilters);
    }

    public List<UsbDevice> getDeviceList(List<DeviceFilter> filters) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        List<UsbDevice> result = new ArrayList<>();
        if (deviceList != null) {
            if (filters == null || filters.isEmpty()) {
                result.addAll(deviceList.values());
            } else {
                for (UsbDevice device : deviceList.values()) {
                    Iterator<DeviceFilter> it = filters.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        DeviceFilter filter = it.next();
                        if (filter != null && filter.matches(device)) {
                            if (!filter.isExclude) {
                                result.add(device);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<UsbDevice> getDeviceList(DeviceFilter filter) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        List<UsbDevice> result = new ArrayList<>();
        if (deviceList != null) {
            for (UsbDevice device : deviceList.values()) {
                if (filter == null || (filter.matches(device) && !filter.isExclude)) {
                    result.add(device);
                }
            }
        }
        return result;
    }

    public Iterator<UsbDevice> getDevices() throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> list = this.mUsbManager.getDeviceList();
        if (list == null) {
            return null;
        }
        Iterator<UsbDevice> iterator = list.values().iterator();
        return iterator;
    }

    public final void dumpDevices() {
        HashMap<String, UsbDevice> list = this.mUsbManager.getDeviceList();
        if (list == null) {
            Log.i(TAG, "no device");
            return;
        }
        Set<String> keys = list.keySet();
        if (keys == null || keys.size() <= 0) {
            Log.i(TAG, "no device");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            UsbDevice device = list.get(key);
            int num_interface = device != null ? device.getInterfaceCount() : 0;
            sb.setLength(0);
            for (int i = 0; i < num_interface; i++) {
                sb.append(String.format(Locale.US, "interface%d:%s", Integer.valueOf(i), device.getInterface(i).toString()));
            }
            String str = TAG;
            Log.i(str, "key=" + key + ":" + device + ":" + sb.toString());
        }
    }

    public final boolean hasPermission(UsbDevice device) throws IllegalStateException {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return device != null && this.mUsbManager.hasPermission(device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceKeys(UsbDevice device, boolean hasPermission) {
        String deviceKey = getDeviceKey(device);
        updateDeviceKeys(deviceKey, hasPermission);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceKeys(String deviceKey, boolean hasPermission) {
        synchronized (this) {
            this.mDetectedDeviceKeys.add(deviceKey);
            if (hasPermission) {
                this.mHasPermissionDeviceKeys.add(deviceKey);
            } else {
                this.mHasPermissionDeviceKeys.remove(deviceKey);
            }
        }
    }

    public void requestPermission(UsbDevice device) {
        String str = TAG;
        LogUtil.v(str, "requestPermission:device=" + device.getDeviceName());
        synchronized (USBMonitor.class) {
            if (isRegistered()) {
                if (this.mUsbManager.hasPermission(device)) {
                    processOpenDevice(device);
                } else {
                    try {
                        this.mUsbManager.requestPermission(device, this.mPermissionIntent);
                    } catch (Exception e) {
                        Log.w(TAG, e);
                        processCancel(device);
                    }
                }
            } else {
                processCancel(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            USBMonitor.this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$1$dQKyLEK6ac1khnWcyXL8VYYm-pw
                @Override // java.lang.Runnable
                public final void run() {
                    USBMonitor.AnonymousClass1.this.lambda$onReceive$0$USBMonitor$1(intent);
                }
            });
        }

        public /* synthetic */ void lambda$onReceive$0$USBMonitor$1(Intent intent) {
            UsbDevice device;
            if (USBMonitor.this.mDestroyed) {
                return;
            }
            String action = intent.getAction();
            if (USBMonitor.this.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (USBMonitor.this) {
                    UsbDevice device2 = USBMonitor.this.getExtraDevice(intent);
                    if (device2 != null) {
                        if (intent.getBooleanExtra("permission", false)) {
                            USBMonitor.this.processOpenDevice(device2);
                        } else {
                            USBMonitor.this.processCancel(device2);
                        }
                    }
                }
            } else if (USBMonitor.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device3 = USBMonitor.this.getExtraDevice(intent);
                if (device3 != null) {
                    USBMonitor uSBMonitor = USBMonitor.this;
                    uSBMonitor.updateDeviceKeys(device3, uSBMonitor.hasPermission(device3));
                    USBMonitor.this.processAttach(device3);
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) && (device = USBMonitor.this.getExtraDevice(intent)) != null) {
                synchronized (USBMonitor.this.mOpenedCtrlBlocks) {
                    UsbControlBlock ctrlBlock = (UsbControlBlock) USBMonitor.this.mOpenedCtrlBlocks.remove(device);
                    if (ctrlBlock != null) {
                        ctrlBlock.close();
                    }
                }
                USBMonitor.this.processDetach(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UsbDevice getExtraDevice(Intent intent) {
        UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
        if (isAvailableDevice(device)) {
            return device;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor$2  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (USBMonitor.this.mDestroyed) {
                return;
            }
            List<UsbDevice> devices = USBMonitor.this.getDeviceList();
            final HashSet<UsbDevice> needNotifyDevices = new HashSet<>();
            synchronized (this) {
                HashSet<String> oldHasPermissionKeys = USBMonitor.this.mHasPermissionDeviceKeys;
                HashSet<String> oldDetectedKeys = USBMonitor.this.mDetectedDeviceKeys;
                USBMonitor.this.mHasPermissionDeviceKeys = new HashSet();
                USBMonitor.this.mDetectedDeviceKeys = new HashSet();
                for (UsbDevice device : devices) {
                    boolean hasPermission = USBMonitor.this.hasPermission(device);
                    String deviceKey = USBMonitor.getDeviceKey(device);
                    if (!oldDetectedKeys.contains(deviceKey) || (hasPermission && !oldHasPermissionKeys.contains(deviceKey))) {
                        needNotifyDevices.add(device);
                    }
                    USBMonitor.this.updateDeviceKeys(deviceKey, hasPermission);
                }
            }
            if (USBMonitor.this.mOnDeviceConnectListener != null) {
                USBMonitor.this.mListenerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$2$Q6lWg5RLWLnM1__o7rhaYxT2k5I
                    @Override // java.lang.Runnable
                    public final void run() {
                        USBMonitor.AnonymousClass2.this.lambda$run$0$USBMonitor$2(needNotifyDevices);
                    }
                });
            }
            USBMonitor.this.mAsyncHandler.postDelayed(this, 150L);
        }

        public /* synthetic */ void lambda$run$0$USBMonitor$2(HashSet needNotifyDevices) {
            Iterator it = needNotifyDevices.iterator();
            while (it.hasNext()) {
                UsbDevice device = (UsbDevice) it.next();
                String str = USBMonitor.TAG;
                Log.d(str, "DeviceCheckRunnable onAttach:device=" + device.getDeviceName());
                USBMonitor.this.mOnDeviceConnectListener.onAttach(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processOpenDevice(final UsbDevice device) {
        final UsbControlBlock ctrlBlock;
        final boolean createNew;
        if (this.mDestroyed) {
            return;
        }
        updateDeviceKeys(device, true);
        String str = TAG;
        LogUtil.v(str, "processOpenDevice:device=" + device.getDeviceName());
        synchronized (this.mOpenedCtrlBlocks) {
            if (!this.mOpenedCtrlBlocks.containsKey(device)) {
                ctrlBlock = new UsbControlBlock(this, device, null);
                ctrlBlock.open();
                this.mOpenedCtrlBlocks.put(device, ctrlBlock);
                createNew = true;
            } else {
                ctrlBlock = this.mOpenedCtrlBlocks.get(device);
                createNew = false;
            }
        }
        if (this.mOnDeviceConnectListener != null) {
            this.mListenerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$0UV98TXSMfv3twWWofSoa_gCwts
                @Override // java.lang.Runnable
                public final void run() {
                    USBMonitor.this.lambda$processOpenDevice$0$USBMonitor(device, ctrlBlock, createNew);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processOpenDevice$0$USBMonitor(UsbDevice device, UsbControlBlock ctrlBlock, boolean createNew) {
        this.mOnDeviceConnectListener.onDeviceOpen(device, ctrlBlock, createNew);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCancel(final UsbDevice device) {
        if (this.mDestroyed) {
            return;
        }
        LogUtil.v(TAG, "processCancel:");
        updateDeviceKeys(device, false);
        if (this.mOnDeviceConnectListener != null) {
            this.mListenerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$BPJ1cyxyVd9v1bkdwfHk8Tp8d1Q
                @Override // java.lang.Runnable
                public final void run() {
                    USBMonitor.this.lambda$processCancel$1$USBMonitor(device);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processCancel$1$USBMonitor(UsbDevice device) {
        this.mOnDeviceConnectListener.onCancel(device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAttach(final UsbDevice device) {
        if (this.mDestroyed) {
            return;
        }
        LogUtil.v(TAG, "processAttach:");
        if (this.mOnDeviceConnectListener != null) {
            this.mListenerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$Bg8ku9gtczlivzw_R-Q_zbTYkfc
                @Override // java.lang.Runnable
                public final void run() {
                    USBMonitor.this.lambda$processAttach$2$USBMonitor(device);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processAttach$2$USBMonitor(UsbDevice device) {
        this.mOnDeviceConnectListener.onAttach(device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDetach(final UsbDevice device) {
        if (this.mDestroyed) {
            return;
        }
        LogUtil.v(TAG, "processDetach:");
        if (this.mOnDeviceConnectListener != null) {
            this.mListenerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$ku9lfrFl-3t1ZZNUCMDyzNmFKJw
                @Override // java.lang.Runnable
                public final void run() {
                    USBMonitor.this.lambda$processDetach$3$USBMonitor(device);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processDetach$3$USBMonitor(UsbDevice device) {
        this.mOnDeviceConnectListener.onDetach(device);
    }

    @SuppressLint({"NewApi"})
    public static String getDeviceKey(UsbDevice device) {
        if (device == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(device.getDeviceName());
        sb.append("#");
        sb.append(device.getVendorId());
        sb.append("#");
        sb.append(device.getProductId());
        sb.append("#");
        sb.append(device.getDeviceClass());
        sb.append("#");
        sb.append(device.getDeviceSubclass());
        sb.append("#");
        sb.append(device.getDeviceProtocol());
        if (Build.VERSION.SDK_INT >= 21) {
            sb.append("#");
            if (Build.VERSION.SDK_INT < 29) {
                sb.append(device.getSerialNumber());
                sb.append("#");
            }
            sb.append(device.getManufacturerName());
            sb.append("#");
            sb.append(device.getConfigurationCount());
            sb.append("#");
            if (Build.VERSION.SDK_INT >= 23) {
                sb.append(device.getVersion());
                sb.append("#");
            }
        }
        return sb.toString();
    }

    public static String getProductKey(UsbDevice device) {
        if (device == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(device.getVendorId());
        sb.append("#");
        sb.append(device.getProductId());
        sb.append("#");
        sb.append(device.getDeviceClass());
        sb.append("#");
        sb.append(device.getDeviceSubclass());
        sb.append("#");
        sb.append(device.getDeviceProtocol());
        if (Build.VERSION.SDK_INT >= 21) {
            sb.append("#");
            if (Build.VERSION.SDK_INT < 29) {
                sb.append(device.getSerialNumber());
                sb.append("#");
            }
            sb.append(device.getManufacturerName());
            sb.append("#");
            sb.append(device.getConfigurationCount());
            sb.append("#");
            if (Build.VERSION.SDK_INT >= 23) {
                sb.append(device.getVersion());
                sb.append("#");
            }
        }
        return sb.toString();
    }

    /* loaded from: classes5.dex */
    public static class UsbDeviceInfo {
        public String manufacturer;
        public String product;
        public String serial;
        public String usb_version;
        public String version;

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.serial = null;
            this.version = null;
            this.product = null;
            this.manufacturer = null;
            this.usb_version = null;
        }

        public String toString() {
            Object[] objArr = new Object[5];
            String str = this.usb_version;
            if (str == null) {
                str = "";
            }
            objArr[0] = str;
            String str2 = this.manufacturer;
            if (str2 == null) {
                str2 = "";
            }
            objArr[1] = str2;
            String str3 = this.product;
            if (str3 == null) {
                str3 = "";
            }
            objArr[2] = str3;
            String str4 = this.version;
            if (str4 == null) {
                str4 = "";
            }
            objArr[3] = str4;
            String str5 = this.serial;
            objArr[4] = str5 != null ? str5 : "";
            return String.format("UsbDevice:usb_version=%s,manufacturer=%s,product=%s,version=%s,serial=%s", objArr);
        }
    }

    private static String getString(UsbDeviceConnection connection, int id, int languageCount, byte[] languages) {
        byte[] work = new byte[256];
        String result = null;
        for (int i = 1; i <= languageCount; i++) {
            int ret = connection.controlTransfer(128, 6, id | DTrees.PREDICT_MASK, languages[i], work, 256, 0);
            if (ret > 2 && work[0] == ret && work[1] == 3) {
                try {
                    result = new String(work, 2, ret - 2, "UTF-16LE");
                    if (!"Љ".equals(result)) {
                        break;
                    }
                    result = null;
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return result;
    }

    public UsbDeviceInfo getDeviceInfo(UsbDevice device) {
        return updateDeviceInfo(this.mUsbManager, device, null);
    }

    public static UsbDeviceInfo getDeviceInfo(Context context, UsbDevice device) {
        return updateDeviceInfo((UsbManager) context.getSystemService("usb"), device, new UsbDeviceInfo());
    }

    public static UsbDeviceInfo updateDeviceInfo(UsbManager manager, UsbDevice device, UsbDeviceInfo _info) {
        UsbDeviceInfo info = _info != null ? _info : new UsbDeviceInfo();
        info.clear();
        if (device != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                info.manufacturer = device.getManufacturerName();
                info.product = device.getProductName();
                if (Build.VERSION.SDK_INT < 29 || (manager != null && manager.hasPermission(device))) {
                    info.serial = device.getSerialNumber();
                }
            }
            if (Build.VERSION.SDK_INT >= 23) {
                info.usb_version = device.getVersion();
            }
            if (manager != null && manager.hasPermission(device)) {
                UsbDeviceConnection connection = null;
                try {
                    try {
                        try {
                            connection = manager.openDevice(device);
                            byte[] desc = connection.getRawDescriptors();
                            if (TextUtils.isEmpty(info.usb_version) && desc != null) {
                                info.usb_version = String.format("%x.%02x", Integer.valueOf(desc[3] & UByte.MAX_VALUE), Integer.valueOf(desc[2] & UByte.MAX_VALUE));
                            }
                            if (TextUtils.isEmpty(info.version) && desc != null) {
                                info.version = String.format("%x.%02x", Integer.valueOf(desc[13] & UByte.MAX_VALUE), Integer.valueOf(desc[12] & UByte.MAX_VALUE));
                            }
                            if (TextUtils.isEmpty(info.serial)) {
                                info.serial = connection.getSerial();
                            }
                            byte[] languages = new byte[256];
                            int languageCount = 0;
                            int result = connection.controlTransfer(128, 6, DTrees.PREDICT_MASK, 0, languages, 256, 0);
                            if (result > 0) {
                                languageCount = (result - 2) / 2;
                            }
                            if (languageCount > 0 && desc != null) {
                                if (TextUtils.isEmpty(info.manufacturer)) {
                                    info.manufacturer = getString(connection, desc[14], languageCount, languages);
                                }
                                if (TextUtils.isEmpty(info.product)) {
                                    info.product = getString(connection, desc[15], languageCount, languages);
                                }
                                if (TextUtils.isEmpty(info.serial)) {
                                    info.serial = getString(connection, desc[16], languageCount, languages);
                                }
                            }
                            connection.close();
                        } catch (Throwable e) {
                            if (connection != null) {
                                try {
                                    connection.close();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                            throw e;
                        }
                    } catch (Exception e3) {
                        Log.e(TAG, e3.getLocalizedMessage(), e3);
                        if (connection != null) {
                            connection.close();
                        }
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(info.manufacturer)) {
                info.manufacturer = USBVendorId.vendorName(device.getVendorId());
            }
            if (TextUtils.isEmpty(info.manufacturer)) {
                info.manufacturer = String.format("%04x", Integer.valueOf(device.getVendorId()));
            }
            if (TextUtils.isEmpty(info.product)) {
                info.product = String.format("%04x", Integer.valueOf(device.getProductId()));
            }
        }
        return info;
    }

    /* loaded from: classes5.dex */
    public static class UsbControlBlock implements Cloneable {
        private int mBusNum;
        protected UsbDeviceConnection mConnection;
        private int mDevNum;
        protected UsbDeviceInfo mInfo;
        private final SparseArray<SparseArray<UsbInterface>> mInterfaces;
        private final WeakReference<UsbDevice> mWeakDevice;
        private final WeakReference<USBMonitor> mWeakMonitor;

        /* synthetic */ UsbControlBlock(USBMonitor x0, UsbDevice x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private UsbControlBlock(USBMonitor monitor, UsbDevice device) {
            this.mInterfaces = new SparseArray<>();
            LogUtil.i(USBMonitor.TAG, "UsbControlBlock:constructor");
            this.mWeakMonitor = new WeakReference<>(monitor);
            this.mWeakDevice = new WeakReference<>(device);
        }

        public synchronized void open() {
            LogUtil.i(USBMonitor.TAG, "UsbControlBlock#open:");
            USBMonitor monitor = this.mWeakMonitor.get();
            UsbDevice device = this.mWeakDevice.get();
            this.mConnection = monitor.mUsbManager.openDevice(device);
            this.mInfo = USBMonitor.updateDeviceInfo(monitor.mUsbManager, device, null);
            String name = device.getDeviceName();
            String[] v = TextUtils.isEmpty(name) ? null : name.split("/");
            int busnum = 0;
            int devnum = 0;
            if (v != null) {
                busnum = Integer.parseInt(v[v.length - 2]);
                devnum = Integer.parseInt(v[v.length - 1]);
            }
            this.mBusNum = busnum;
            this.mDevNum = devnum;
            if (this.mConnection == null) {
                String str = USBMonitor.TAG;
                Log.e(str, "could not connect to device " + name);
            } else {
                int desc = this.mConnection.getFileDescriptor();
                byte[] rawDesc = this.mConnection.getRawDescriptors();
                String str2 = USBMonitor.TAG;
                Log.i(str2, String.format(Locale.US, "name=%s,desc=%d,busnum=%d,devnum=%d,rawDesc=", name, Integer.valueOf(desc), Integer.valueOf(busnum), Integer.valueOf(devnum)) + rawDesc);
            }
        }

        public synchronized void close() {
            close(false);
        }

        public synchronized void close(boolean isSilent) {
            LogUtil.i(USBMonitor.TAG, "UsbControlBlock#close:");
            if (this.mConnection != null) {
                int n = this.mInterfaces.size();
                for (int i = 0; i < n; i++) {
                    SparseArray<UsbInterface> intfs = this.mInterfaces.valueAt(i);
                    if (intfs != null) {
                        int m = intfs.size();
                        for (int j = 0; j < m; j++) {
                            UsbInterface intf = intfs.valueAt(j);
                            this.mConnection.releaseInterface(intf);
                        }
                        intfs.clear();
                    }
                }
                this.mInterfaces.clear();
                this.mConnection.close();
                this.mConnection = null;
                final USBMonitor monitor = this.mWeakMonitor.get();
                if (monitor != null) {
                    if (!isSilent && monitor.mOnDeviceConnectListener != null) {
                        monitor.mListenerHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.-$$Lambda$USBMonitor$UsbControlBlock$u8Lbhb-HyPTG_gKJMWEqkLPFOpM
                            @Override // java.lang.Runnable
                            public final void run() {
                                USBMonitor.UsbControlBlock.this.lambda$close$0$USBMonitor$UsbControlBlock(monitor);
                            }
                        });
                    }
                    synchronized (monitor.mOpenedCtrlBlocks) {
                        monitor.mOpenedCtrlBlocks.remove(getDevice());
                    }
                }
            }
        }

        public /* synthetic */ void lambda$close$0$USBMonitor$UsbControlBlock(USBMonitor monitor) {
            monitor.mOnDeviceConnectListener.onDeviceClose(this.mWeakDevice.get(), this);
        }

        /* renamed from: clone */
        public UsbControlBlock m129clone() throws CloneNotSupportedException {
            try {
                UsbControlBlock ctrlblock = new UsbControlBlock(this.mWeakMonitor.get(), this.mWeakDevice.get());
                return ctrlblock;
            } catch (IllegalStateException e) {
                throw new CloneNotSupportedException(e.getMessage());
            }
        }

        public USBMonitor getUSBMonitor() {
            return this.mWeakMonitor.get();
        }

        public final UsbDevice getDevice() {
            return this.mWeakDevice.get();
        }

        public String getDeviceName() {
            UsbDevice device = this.mWeakDevice.get();
            return device != null ? device.getDeviceName() : "";
        }

        public int getDeviceId() {
            UsbDevice device = this.mWeakDevice.get();
            if (device != null) {
                return device.getDeviceId();
            }
            return 0;
        }

        public synchronized UsbDeviceConnection getConnection() {
            return this.mConnection;
        }

        public synchronized int getFileDescriptor() throws IllegalStateException {
            checkConnection();
            return this.mConnection.getFileDescriptor();
        }

        public synchronized byte[] getRawDescriptors() throws IllegalStateException {
            checkConnection();
            return this.mConnection.getRawDescriptors();
        }

        public int getVendorId() {
            UsbDevice device = this.mWeakDevice.get();
            if (device != null) {
                return device.getVendorId();
            }
            return 0;
        }

        public int getProductId() {
            UsbDevice device = this.mWeakDevice.get();
            if (device != null) {
                return device.getProductId();
            }
            return 0;
        }

        public String getUsbVersion() {
            return this.mInfo.usb_version;
        }

        public String getManufacture() {
            return this.mInfo.manufacturer;
        }

        public String getProductName() {
            return this.mInfo.product;
        }

        public String getVersion() {
            return this.mInfo.version;
        }

        public String getSerial() {
            return this.mInfo.serial;
        }

        public int getBusNum() {
            return this.mBusNum;
        }

        public int getDevNum() {
            return this.mDevNum;
        }

        public synchronized UsbInterface getInterface(int interface_id) throws IllegalStateException {
            return getInterface(interface_id, 0);
        }

        public synchronized UsbInterface getInterface(int interface_id, int altsetting) throws IllegalStateException {
            UsbInterface intf;
            checkConnection();
            SparseArray<UsbInterface> intfs = this.mInterfaces.get(interface_id);
            if (intfs == null) {
                intfs = new SparseArray<>();
                this.mInterfaces.put(interface_id, intfs);
            }
            intf = intfs.get(altsetting);
            if (intf == null) {
                UsbDevice device = this.mWeakDevice.get();
                int n = device.getInterfaceCount();
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    }
                    UsbInterface temp = device.getInterface(i);
                    if (temp.getId() != interface_id || temp.getAlternateSetting() != altsetting) {
                        i++;
                    } else {
                        intf = temp;
                        break;
                    }
                }
                if (intf != null) {
                    intfs.append(altsetting, intf);
                }
            }
            return intf;
        }

        public synchronized void claimInterface(UsbInterface intf) {
            claimInterface(intf, true);
        }

        public synchronized void claimInterface(UsbInterface intf, boolean force) {
            checkConnection();
            this.mConnection.claimInterface(intf, force);
        }

        public synchronized void releaseInterface(UsbInterface intf) throws IllegalStateException {
            checkConnection();
            SparseArray<UsbInterface> intfs = this.mInterfaces.get(intf.getId());
            if (intfs != null) {
                int index = intfs.indexOfValue(intf);
                intfs.removeAt(index);
                if (intfs.size() == 0) {
                    this.mInterfaces.remove(intf.getId());
                }
            }
            this.mConnection.releaseInterface(intf);
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof UsbControlBlock) {
                UsbDevice device = ((UsbControlBlock) o).getDevice();
                if (device == null) {
                    return this.mWeakDevice.get() == null;
                }
                return device.equals(this.mWeakDevice.get());
            } else if (o instanceof UsbDevice) {
                return o.equals(this.mWeakDevice.get());
            } else {
                return super.equals(o);
            }
        }

        private synchronized void checkConnection() throws IllegalStateException {
            if (this.mConnection == null) {
                throw new IllegalStateException("already closed");
            }
        }
    }
}
