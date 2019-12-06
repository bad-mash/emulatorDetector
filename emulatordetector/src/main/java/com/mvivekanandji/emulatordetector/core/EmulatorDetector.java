package com.mvivekanandji.emulatordetector.core;

import android.Manifest;
import android.os.Build;

import androidx.annotation.RequiresPermission;

public class EmulatorDetector {

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceInfo() {
        return "Build.PRODUCT: " + Build.PRODUCT + "\n" +
                "Build.MANUFACTURER: " + Build.MANUFACTURER + "\n" +
                "Build.BRAND: " + Build.BRAND + "\n" +
                "Build.DEVICE: " + Build.DEVICE + "\n" +
                "Build.MODEL: " + Build.MODEL + "\n" +
                "Build.HARDWARE: " + Build.HARDWARE + "\n" +
                "Build.FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "Build.ID: " + Build.ID + "\n" +
                "Build.BOARD: " + Build.BOARD + "\n" +
                "Build.USER: " + Build.USER + "\n" +
                "Build.SERIAL: " + (Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? Build.SERIAL : Build.getSerial());
    }

}
