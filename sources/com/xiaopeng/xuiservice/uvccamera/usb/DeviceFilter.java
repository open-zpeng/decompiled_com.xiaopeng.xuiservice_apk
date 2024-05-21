package com.xiaopeng.xuiservice.uvccamera.usb;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.text.TextUtils;
import android.util.Xml;
import com.alipay.mobile.aromeservice.RequestParams;
import com.android.volley.toolbox.JsonRequest;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes5.dex */
public final class DeviceFilter {
    private static final String TAG = "DeviceFilter";
    public final boolean isExclude;
    public final int mClass;
    public final String mManufacturerName;
    public final int mProductId;
    public final String mProductName;
    public final int mProtocol;
    public final String mSerialNumber;
    public final int mSubclass;
    public final int mVendorId;

    public DeviceFilter(int vid, int pid, int clasz, int subclass, int protocol, String manufacturer, String product, String serialNum) {
        this(vid, pid, clasz, subclass, protocol, manufacturer, product, serialNum, false);
    }

    public DeviceFilter(int vid, int pid, int clasz, int subclass, int protocol, String manufacturer, String product, String serialNum, boolean isExclude) {
        this.mVendorId = vid;
        this.mProductId = pid;
        this.mClass = clasz;
        this.mSubclass = subclass;
        this.mProtocol = protocol;
        this.mManufacturerName = manufacturer;
        this.mProductName = product;
        this.mSerialNumber = serialNum;
        this.isExclude = isExclude;
    }

    public DeviceFilter(UsbDevice device) {
        this(device, false);
    }

    public DeviceFilter(UsbDevice device, boolean isExclude) {
        this.mVendorId = device.getVendorId();
        this.mProductId = device.getProductId();
        this.mClass = device.getDeviceClass();
        this.mSubclass = device.getDeviceSubclass();
        this.mProtocol = device.getDeviceProtocol();
        this.mManufacturerName = null;
        this.mProductName = null;
        this.mSerialNumber = null;
        this.isExclude = isExclude;
    }

    public static List<DeviceFilter> getDeviceFilters(Context context, int deviceFilterXmlId) {
        XmlPullParser parser;
        List<DeviceFilter> deviceFilters = new ArrayList<>();
        try {
            File fileName = new File("/system/etc/xuiservice/device_filter.txt");
            if (!fileName.exists()) {
                LogUtil.w(TAG, "UVCCamera device_filter error on /system/etc/xuiservice/device_filter.txt");
                parser = context.getResources().getXml(deviceFilterXmlId);
            } else {
                FileInputStream fis = new FileInputStream("/system/etc/xuiservice/device_filter.txt");
                XmlPullParser parser2 = Xml.newPullParser();
                parser2.setInput(fis, JsonRequest.PROTOCOL_CHARSET);
                parser = parser2;
            }
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                if (eventType == 2) {
                    DeviceFilter deviceFilter = readEntryOne(context, parser);
                    if (deviceFilter != null) {
                        deviceFilters.add(deviceFilter);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LogUtil.d(TAG, "FileNotFoundException " + e);
        } catch (IOException e2) {
            LogUtil.d(TAG, "IOException" + e2);
        } catch (XmlPullParserException e3) {
            LogUtil.d(TAG, "XmlPullParserException" + e3);
        } catch (Exception e4) {
            LogUtil.d(TAG, "Exception" + e4);
        }
        return Collections.unmodifiableList(deviceFilters);
    }

    private static final int getAttributeInteger(Context context, XmlPullParser parser, String namespace, String name, int defaultValue) {
        int result = defaultValue;
        try {
            String v = parser.getAttributeValue(namespace, name);
            if (!TextUtils.isEmpty(v) && v.startsWith("@")) {
                String r = v.substring(1);
                int resId = context.getResources().getIdentifier(r, null, context.getPackageName());
                if (resId > 0) {
                    result = context.getResources().getInteger(resId);
                }
                return result;
            }
            int radix = 10;
            if (v != null && v.length() > 2 && v.charAt(0) == '0' && (v.charAt(1) == 'x' || v.charAt(1) == 'X')) {
                radix = 16;
                v = v.substring(2);
            }
            int result2 = Integer.parseInt(v, radix);
            return result2;
        } catch (Resources.NotFoundException e) {
            return defaultValue;
        } catch (NullPointerException e2) {
            return defaultValue;
        } catch (NumberFormatException e3) {
            return defaultValue;
        }
    }

    private static final boolean getAttributeBoolean(Context context, XmlPullParser parser, String namespace, String name, boolean defaultValue) {
        boolean result = defaultValue;
        try {
            String v = parser.getAttributeValue(namespace, name);
            if ("TRUE".equalsIgnoreCase(v)) {
                return true;
            }
            if ("FALSE".equalsIgnoreCase(v)) {
                return false;
            }
            if (!TextUtils.isEmpty(v) && v.startsWith("@")) {
                String r = v.substring(1);
                int resId = context.getResources().getIdentifier(r, null, context.getPackageName());
                if (resId > 0) {
                    result = context.getResources().getBoolean(resId);
                }
                return result;
            }
            int radix = 10;
            if (v != null && v.length() > 2 && v.charAt(0) == '0' && (v.charAt(1) == 'x' || v.charAt(1) == 'X')) {
                radix = 16;
                v = v.substring(2);
            }
            int val = Integer.parseInt(v, radix);
            boolean result2 = val != 0;
            return result2;
        } catch (Resources.NotFoundException e) {
            return defaultValue;
        } catch (NullPointerException e2) {
            return defaultValue;
        } catch (NumberFormatException e3) {
            return defaultValue;
        }
    }

    private static final String getAttributeString(Context context, XmlPullParser parser, String namespace, String name, String defaultValue) {
        try {
            String result = parser.getAttributeValue(namespace, name);
            if (result == null) {
                result = defaultValue;
            }
            if (!TextUtils.isEmpty(result) && result.startsWith("@")) {
                String r = result.substring(1);
                int resId = context.getResources().getIdentifier(r, null, context.getPackageName());
                if (resId > 0) {
                    return context.getResources().getString(resId);
                }
                return result;
            }
            return result;
        } catch (Resources.NotFoundException e) {
            return defaultValue;
        } catch (NullPointerException e2) {
            return defaultValue;
        } catch (NumberFormatException e3) {
            return defaultValue;
        }
    }

    public static DeviceFilter readEntryOne(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        int vendorId = -1;
        int productId = -1;
        int deviceClass = -1;
        int deviceSubclass = -1;
        int deviceProtocol = -1;
        boolean exclude = false;
        String serialNumber = null;
        String productName = null;
        String serialNumber2 = null;
        boolean hasValue = false;
        while (eventType != 1) {
            String tag = parser.getName();
            if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase("usb-device")) {
                if (eventType == 2) {
                    int vendorId2 = getAttributeInteger(context, parser, null, "vendor-id", -1);
                    if (vendorId2 == -1 && (vendorId2 = getAttributeInteger(context, parser, null, "vendorId", -1)) == -1) {
                        vendorId2 = getAttributeInteger(context, parser, null, "venderId", -1);
                    }
                    int productId2 = getAttributeInteger(context, parser, null, "product-id", -1);
                    if (productId2 == -1) {
                        productId2 = getAttributeInteger(context, parser, null, RequestParams.REQUEST_KEY_PRODUCT_ID, -1);
                    }
                    int deviceClass2 = getAttributeInteger(context, parser, null, "class", -1);
                    int deviceSubclass2 = getAttributeInteger(context, parser, null, "subclass", -1);
                    int deviceProtocol2 = getAttributeInteger(context, parser, null, "protocol", -1);
                    String manufacturerName = getAttributeString(context, parser, null, "manufacturer-name", null);
                    if (TextUtils.isEmpty(manufacturerName)) {
                        manufacturerName = getAttributeString(context, parser, null, "manufacture", null);
                    }
                    String productName2 = getAttributeString(context, parser, null, "product-name", null);
                    if (TextUtils.isEmpty(productName2)) {
                        productName2 = getAttributeString(context, parser, null, "product", null);
                    }
                    String serialNumber3 = getAttributeString(context, parser, null, "serial-number", null);
                    if (TextUtils.isEmpty(serialNumber3)) {
                        serialNumber3 = getAttributeString(context, parser, null, "serial", null);
                    }
                    hasValue = true;
                    exclude = getAttributeBoolean(context, parser, null, "exclude", false);
                    deviceProtocol = deviceProtocol2;
                    vendorId = vendorId2;
                    productId = productId2;
                    deviceClass = deviceClass2;
                    deviceSubclass = deviceSubclass2;
                    productName = productName2;
                    serialNumber2 = serialNumber3;
                    serialNumber = manufacturerName;
                } else if (eventType == 3 && hasValue) {
                    return new DeviceFilter(vendorId, productId, deviceClass, deviceSubclass, deviceProtocol, serialNumber, productName, serialNumber2, exclude);
                }
            }
            eventType = parser.next();
        }
        return null;
    }

    private boolean matches(int clasz, int subclass, int protocol) {
        int i;
        int i2;
        int i3 = this.mClass;
        return (i3 == -1 || clasz == i3) && ((i = this.mSubclass) == -1 || subclass == i) && ((i2 = this.mProtocol) == -1 || protocol == i2);
    }

    public boolean matches(UsbDevice device) {
        if (this.mVendorId == -1 || device.getVendorId() == this.mVendorId) {
            if (this.mProductId == -1 || device.getProductId() == this.mProductId) {
                if (matches(device.getDeviceClass(), device.getDeviceSubclass(), device.getDeviceProtocol())) {
                    return true;
                }
                int count = device.getInterfaceCount();
                for (int i = 0; i < count; i++) {
                    UsbInterface intf = device.getInterface(i);
                    if (matches(intf.getInterfaceClass(), intf.getInterfaceSubclass(), intf.getInterfaceProtocol())) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean isExclude(UsbDevice device) {
        return this.isExclude && matches(device);
    }

    public boolean matches(DeviceFilter f) {
        String str;
        String str2;
        String str3;
        if (this.isExclude != f.isExclude) {
            return false;
        }
        int i = this.mVendorId;
        if (i == -1 || f.mVendorId == i) {
            int i2 = this.mProductId;
            if (i2 == -1 || f.mProductId == i2) {
                if (f.mManufacturerName == null || this.mManufacturerName != null) {
                    if (f.mProductName == null || this.mProductName != null) {
                        if (f.mSerialNumber == null || this.mSerialNumber != null) {
                            String str4 = this.mManufacturerName;
                            if (str4 == null || (str3 = f.mManufacturerName) == null || str4.equals(str3)) {
                                String str5 = this.mProductName;
                                if (str5 == null || (str2 = f.mProductName) == null || str5.equals(str2)) {
                                    String str6 = this.mSerialNumber;
                                    if (str6 == null || (str = f.mSerialNumber) == null || str6.equals(str)) {
                                        return matches(f.mClass, f.mSubclass, f.mProtocol);
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean equals(Object obj) {
        int i;
        int i2;
        int i3;
        int i4;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        int i5 = this.mVendorId;
        if (i5 == -1 || (i = this.mProductId) == -1 || (i2 = this.mClass) == -1 || (i3 = this.mSubclass) == -1 || (i4 = this.mProtocol) == -1) {
            return false;
        }
        if (obj instanceof DeviceFilter) {
            DeviceFilter filter = (DeviceFilter) obj;
            if (filter.mVendorId == i5 && filter.mProductId == i && filter.mClass == i2 && filter.mSubclass == i3 && filter.mProtocol == i4) {
                if ((filter.mManufacturerName == null || this.mManufacturerName != null) && ((filter.mManufacturerName != null || this.mManufacturerName == null) && ((filter.mProductName == null || this.mProductName != null) && ((filter.mProductName != null || this.mProductName == null) && ((filter.mSerialNumber == null || this.mSerialNumber != null) && (filter.mSerialNumber != null || this.mSerialNumber == null)))))) {
                    String str6 = filter.mManufacturerName;
                    return (str6 == null || (str5 = this.mManufacturerName) == null || str5.equals(str6)) && ((str = filter.mProductName) == null || (str4 = this.mProductName) == null || str4.equals(str)) && (((str2 = filter.mSerialNumber) == null || (str3 = this.mSerialNumber) == null || str3.equals(str2)) && filter.isExclude != this.isExclude);
                }
                return false;
            }
            return false;
        } else if (obj instanceof UsbDevice) {
            UsbDevice device = (UsbDevice) obj;
            return !this.isExclude && device.getVendorId() == this.mVendorId && device.getProductId() == this.mProductId && device.getDeviceClass() == this.mClass && device.getDeviceSubclass() == this.mSubclass && device.getDeviceProtocol() == this.mProtocol;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ((this.mVendorId << 16) | this.mProductId) ^ (((this.mClass << 16) | (this.mSubclass << 8)) | this.mProtocol);
    }

    public String toString() {
        return "DeviceFilter[mVendorId=" + this.mVendorId + ",mProductId=" + this.mProductId + ",mClass=" + this.mClass + ",mSubclass=" + this.mSubclass + ",mProtocol=" + this.mProtocol + ",mManufacturerName=" + this.mManufacturerName + ",mProductName=" + this.mProductName + ",mSerialNumber=" + this.mSerialNumber + ",isExclude=" + this.isExclude + "]";
    }
}
