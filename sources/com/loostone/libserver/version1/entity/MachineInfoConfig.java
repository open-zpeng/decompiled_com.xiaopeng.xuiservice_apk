package com.loostone.libserver.version1.entity;

import android.os.Build;
/* loaded from: classes4.dex */
public class MachineInfoConfig {
    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getCpuAbi() {
        return Build.CPU_ABI;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getHardWare() {
        return Build.HARDWARE;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static String getSerial() {
        return Build.SERIAL;
    }
}
