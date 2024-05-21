package com.xiaopeng.xuiservice.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/* loaded from: classes5.dex */
public class ConnectivityUtil {
    private static String mWifiMac = null;

    public static String getWifiMac() {
        String str = mWifiMac;
        if (str != null) {
            return str;
        }
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            if (enumeration == null) {
                return null;
            }
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface.getName().equals("wlan0")) {
                    byte[] arrayOfByte = networkInterface.getHardwareAddress();
                    if (arrayOfByte != null && arrayOfByte.length != 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : arrayOfByte) {
                            sb.append(String.format("%02X", Byte.valueOf(b)));
                        }
                        mWifiMac = sb.toString();
                        return mWifiMac;
                    }
                    return null;
                }
            }
            return null;
        } catch (SocketException e) {
            return null;
        }
    }
}
