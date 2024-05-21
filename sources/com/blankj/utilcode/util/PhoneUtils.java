package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: classes4.dex */
public final class PhoneUtils {
    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isPhone() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType() != 0;
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    @SuppressLint({"HardwareIds"})
    public static String getDeviceId() {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        TelephonyManager tm = getTelephonyManager();
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            if (Build.VERSION.SDK_INT >= 26) {
                String imei = tm.getImei();
                if (TextUtils.isEmpty(imei)) {
                    String meid = tm.getMeid();
                    return TextUtils.isEmpty(meid) ? "" : meid;
                }
                return imei;
            }
            return "";
        }
        return deviceId;
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    @SuppressLint({"HardwareIds"})
    public static String getSerial() {
        if (Build.VERSION.SDK_INT < 29) {
            return Build.VERSION.SDK_INT >= 26 ? Build.getSerial() : Build.SERIAL;
        }
        try {
            return Build.getSerial();
        } catch (SecurityException e) {
            e.printStackTrace();
            return "";
        }
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    public static String getIMEI() {
        return getImeiOrMeid(true);
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    public static String getMEID() {
        return getImeiOrMeid(false);
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    @SuppressLint({"HardwareIds"})
    public static String getImeiOrMeid(boolean isImei) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        TelephonyManager tm = getTelephonyManager();
        if (Build.VERSION.SDK_INT >= 26) {
            if (isImei) {
                return getMinOne(tm.getImei(0), tm.getImei(1));
            }
            return getMinOne(tm.getMeid(0), tm.getMeid(1));
        } else if (Build.VERSION.SDK_INT >= 21) {
            String ids = getSystemPropertyByReflect(isImei ? "ril.gsm.imei" : "ril.cdma.meid");
            if (!TextUtils.isEmpty(ids)) {
                String[] idArr = ids.split(",");
                if (idArr.length == 2) {
                    return getMinOne(idArr[0], idArr[1]);
                }
                return idArr[0];
            }
            String id0 = tm.getDeviceId();
            String id1 = "";
            try {
                Method method = tm.getClass().getMethod("getDeviceId", Integer.TYPE);
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf(isImei ? 1 : 2);
                id1 = (String) method.invoke(tm, objArr);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
            if (isImei) {
                if (id0 != null && id0.length() < 15) {
                    id0 = "";
                }
                if (id1 != null && id1.length() < 15) {
                    id1 = "";
                }
            } else {
                if (id0 != null && id0.length() == 14) {
                    id0 = "";
                }
                if (id1 != null && id1.length() == 14) {
                    id1 = "";
                }
            }
            return getMinOne(id0, id1);
        } else {
            String deviceId = tm.getDeviceId();
            if (isImei) {
                if (deviceId != null && deviceId.length() >= 15) {
                    return deviceId;
                }
            } else if (deviceId != null && deviceId.length() == 14) {
                return deviceId;
            }
            return "";
        }
    }

    private static String getMinOne(String s0, String s1) {
        boolean empty0 = TextUtils.isEmpty(s0);
        boolean empty1 = TextUtils.isEmpty(s1);
        if (empty0 && empty1) {
            return "";
        }
        if (empty0 || empty1) {
            return !empty0 ? s0 : s1;
        } else if (s0.compareTo(s1) <= 0) {
            return s0;
        } else {
            return s1;
        }
    }

    private static String getSystemPropertyByReflect(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String) getMethod.invoke(clz, key, "");
        } catch (Exception e) {
            return "";
        }
    }

    @RequiresPermission("android.permission.READ_PHONE_STATE")
    @SuppressLint({"HardwareIds"})
    public static String getIMSI() {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                getTelephonyManager().getSubscriberId();
            } catch (SecurityException e) {
                e.printStackTrace();
                return "";
            }
        }
        return getTelephonyManager().getSubscriberId();
    }

    public static int getPhoneType() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType();
    }

    public static boolean isSimCardReady() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimState() == 5;
    }

    public static String getSimOperatorName() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimOperatorName();
    }

    public static String getSimOperatorByMnc() {
        TelephonyManager tm = getTelephonyManager();
        String operator = tm.getSimOperator();
        if (operator == null) {
            return "";
        }
        char c = 65535;
        int hashCode = operator.hashCode();
        if (hashCode != 49679479) {
            if (hashCode != 49679502) {
                if (hashCode != 49679532) {
                    switch (hashCode) {
                        case 49679470:
                            if (operator.equals("46000")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 49679471:
                            if (operator.equals("46001")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 49679472:
                            if (operator.equals("46002")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 49679473:
                            if (operator.equals("46003")) {
                                c = 7;
                                break;
                            }
                            break;
                        default:
                            switch (hashCode) {
                                case 49679475:
                                    if (operator.equals("46005")) {
                                        c = '\b';
                                        break;
                                    }
                                    break;
                                case 49679476:
                                    if (operator.equals("46006")) {
                                        c = 5;
                                        break;
                                    }
                                    break;
                                case 49679477:
                                    if (operator.equals("46007")) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                            }
                    }
                } else if (operator.equals("46020")) {
                    c = 3;
                }
            } else if (operator.equals("46011")) {
                c = '\t';
            }
        } else if (operator.equals("46009")) {
            c = 6;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
                return "中国移动";
            case 4:
            case 5:
            case 6:
                return "中国联通";
            case 7:
            case '\b':
            case '\t':
                return "中国电信";
            default:
                return operator;
        }
    }

    public static void dial(@NonNull String phoneNumber) {
        if (phoneNumber == null) {
            throw new NullPointerException("Argument 'phoneNumber' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Utils.getApp().startActivity(UtilsBridge.getDialIntent(phoneNumber));
    }

    @RequiresPermission("android.permission.CALL_PHONE")
    public static void call(@NonNull String phoneNumber) {
        if (phoneNumber == null) {
            throw new NullPointerException("Argument 'phoneNumber' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Utils.getApp().startActivity(UtilsBridge.getCallIntent(phoneNumber));
    }

    public static void sendSms(@NonNull String phoneNumber, String content) {
        if (phoneNumber == null) {
            throw new NullPointerException("Argument 'phoneNumber' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Utils.getApp().startActivity(UtilsBridge.getSendSmsIntent(phoneNumber, content));
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) Utils.getApp().getSystemService("phone");
    }
}
