package com.mvivekanandji.emulatordetector.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.mvivekanandji.emulatordetector.model.DeviceProperty;
import com.mvivekanandji.emulatordetector.utils.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;

/**
 * Copyright 2019 Vivekanand Mishra.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by Vivekanand Mishra on 02/12/19.
 *
 * @author vivekanand
 * @version 1.0
 * <p>
 * <p>
 * Class to detect emulators
 */
public class EmulatorProperties {

    //region variables/constants
    /**
     * QEMU wasn’t made for hiding
     * <p>
     * REAL HARDWARE <-> Host <-> Android Client
     * <p>
     * As The “hardware” is inside the Host, so radio/gps/camera are communicated through QEMU pipes,
     * and pipes are not hidden
     */
    private String[] QEMU_PIPES = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"};

    private String[] QEMU_FILES = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props", "" +
            "/system/bin/qemud",
            "init.goldfish.rc"};

    private String[] QEMU_DRIVERS = {
            "goldfish"};

    /**
     * Known properties of QEMU environment.
     * Format {Property name, Value to seek]
     * If value to seek is null, the very existence of that property indicates the QEMU environment.
     * <p>
     * Default build(ro.build.type) is "user", other builds of android that are not production
     * usually have build types of "eng", "debug", etc... (but other values does not mean emulator,
     * it may just be poor manufacturing)
     */
    private DeviceProperty[] KNOWN_PROPS = {
            new DeviceProperty("init.svc.qemud", null),
            new DeviceProperty("init.svc.qemu-props", null),
            new DeviceProperty("qemu.hw.mainkeys", null),
            new DeviceProperty("qemu.sf.fake_camera", null),
            new DeviceProperty("qemu.sf.lcd_density", null),
            new DeviceProperty("ro.bootloader", "unknown"),
            new DeviceProperty("ro.build.type", "user"),
            new DeviceProperty("ro.bootmode", "unknown"),
            new DeviceProperty("ro.hardware", "goldfish"),
            new DeviceProperty("ro.hardware", "ranchu"),
            new DeviceProperty("ro.kernel.android.qemud", null),
            new DeviceProperty("ro.kernel.qemu.gles", null),
            new DeviceProperty("ro.kernel.qemu", "1"),
            new DeviceProperty("ro.product.device", "generic"),
            new DeviceProperty("ro.product.model", "sdk"),
            new DeviceProperty("ro.product.name", "sdk"),
            new DeviceProperty("ro.secure", "0"),
            new DeviceProperty("ro.serialno", null)};


    private String[] GENY_FILES = {
            "/dev/socket/genyd",
            "/dev/socket/baseband_genyd"};

    private String[] X86_FILES = {
            "ueventd.android_x86.rc",
            "x86.prop",
            "ueventd.ttVM_x86.rc",
            "init.ttVM_x86.rc",
            "fstab.ttVM_x86",
            "fstab.vbox86",
            "init.vbox86.rc",
            "ueventd.vbox86.rc"
    };

    private String[] ANDY_FILES = {
            "fstab.andy",
            "ueventd.andy.rc"
    };

    private String[] NOX_FILES = {
            "fstab.nox",
            "init.nox.rc",
            "ueventd.nox.rc"
    };

    private String[] PACKAGES = {
            "com.google.android.launcher.layouts.genymotion",
            "com.bluestacks",
            "com.bignox.app",
            "com.microvirt",
            "com.kaopu",
            "com.kop",
            "cn.itools",
            "me.haima",
            "com.nox.mopen.app",
            "com.vphone"
    };
    //endregion


    /**
     * Method will query specific system properties to identify emulator.
     *
     * @return boolean - {@code true} if  any of the properties found or {@code false} if not.
     */
    public boolean hasEmulatorProperties() {

        if (Build.PRODUCT.equalsIgnoreCase("sdk_x86_64") ||
                Build.PRODUCT.equalsIgnoreCase("sdk_google_phone_x86") ||
                Build.PRODUCT.equalsIgnoreCase("sdk_google_phone_x86_64") ||
                Build.PRODUCT.equalsIgnoreCase("sdk_google_phone_arm64") ||
                Build.PRODUCT.toLowerCase().contains("sdk") ||
                Build.PRODUCT.toLowerCase().contains("Andy") ||
                Build.PRODUCT.toLowerCase().contains("ttvm_hdragon") ||
                Build.PRODUCT.toLowerCase().contains("google_sdk") ||
                Build.PRODUCT.toLowerCase().contains("droid4x") ||
                Build.PRODUCT.toLowerCase().contains("itoolsavm") ||
                Build.PRODUCT.toLowerCase().contains("nox") ||
                Build.PRODUCT.toLowerCase().contains("sdk_x86") ||
                Build.PRODUCT.toLowerCase().contains("sdk_google") ||
                Build.PRODUCT.toLowerCase().contains("vbox86p")) {
            return true;
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("unknown") ||
                Build.MANUFACTURER.equalsIgnoreCase("Genymotion") ||
                Build.MANUFACTURER.toLowerCase().contains("Andy") ||
                Build.MANUFACTURER.toLowerCase().contains("MIT") ||
                Build.MANUFACTURER.toLowerCase().contains("nox") ||
                Build.MANUFACTURER.toLowerCase().contains("TiantianVM") ||
                Build.MANUFACTURER.startsWith("iToolsAVM")) {
            return true;
        }

        if (Build.BRAND.equals("generic") ||
                Build.BRAND.equalsIgnoreCase("android") ||
                Build.BRAND.equals("generic_arm64") ||
                Build.BRAND.equals("generic_x86") ||
                Build.BRAND.equals("generic_x86_64") ||
                Build.BRAND.equals("TTVM") ||
                Build.BRAND.toLowerCase().contains("andy")) {
            return true;
        }

        if (Build.DEVICE.equalsIgnoreCase("generic_arm64") ||
                Build.DEVICE.equalsIgnoreCase("generic_x86") ||
                Build.DEVICE.equalsIgnoreCase("generic_x86_64") ||
                Build.DEVICE.toLowerCase().contains("generic") ||
                Build.DEVICE.toLowerCase().contains("andy") ||
                Build.DEVICE.toLowerCase().contains("ttvm_hdragon") ||
                Build.DEVICE.toLowerCase().contains("droid4x") ||
                Build.DEVICE.toLowerCase().contains("nox") ||
                Build.DEVICE.toLowerCase().contains("vbox86p") ||
                Build.DEVICE.startsWith("iToolsAVM")) {
            return true;
        }

        if (Build.MODEL.equalsIgnoreCase("sdk") ||
                Build.MODEL.equalsIgnoreCase("Android SDK built for arm64") ||
                Build.MODEL.equalsIgnoreCase("Android SDK built for armv7") ||
                Build.MODEL.equalsIgnoreCase("Android SDK built for x86") ||
                Build.MODEL.equalsIgnoreCase("Android SDK built for x86_64") ||
                Build.MODEL.equalsIgnoreCase("google_sdk") ||
                Build.MODEL.toLowerCase().contains("google_sdk") ||
                Build.MODEL.toLowerCase().contains("droid4x") ||
                Build.MODEL.toLowerCase().contains("tiantianvm") ||
                Build.MODEL.toLowerCase().contains("andy") ||
                Build.MODEL.toLowerCase().contains("emulator") ||
                Build.MODEL.startsWith("iToolsAVM")) {
            return true;
        }

        if (Build.HARDWARE.equalsIgnoreCase("goldfish") ||
                Build.HARDWARE.equalsIgnoreCase("ranchu") ||
                Build.HARDWARE.toLowerCase().contains("vbox86") ||
                Build.HARDWARE.toLowerCase().contains("nox") ||
                Build.HARDWARE.toLowerCase().contains("ttVM_x86")) {
            return true;
        }

        if (Build.FINGERPRINT.toLowerCase().contains("generic/sdk/generic") ||
                Build.FINGERPRINT.toLowerCase().contains("sdk_google_phone_arm64") ||
                Build.FINGERPRINT.toLowerCase().contains("sdk_google_phone_armv7") ||
                Build.FINGERPRINT.toLowerCase().contains("generic_x86/sdk_x86/generic_x86") ||
                Build.FINGERPRINT.toLowerCase().contains("andy") ||
                Build.FINGERPRINT.toLowerCase().contains("ttvm_hdragon") ||
                Build.FINGERPRINT.toLowerCase().contains("generic_x86_64") ||
                Build.FINGERPRINT.toLowerCase().contains("generic/google_sdk/generic") ||
                Build.FINGERPRINT.toLowerCase().contains("vbox86p") ||
                Build.FINGERPRINT.toLowerCase().contains("generic/vbox86p/vbox86p") ||
                Build.FINGERPRINT.startsWith("generic")) {
            return true;
        }

        if (Build.BOARD.toLowerCase().contains("nox") ||
                Build.BOOTLOADER.toLowerCase().contains("nox") ||
                Build.HOST.toLowerCase().contains("droid4x-buildstation") ||
                Build.ID.toLowerCase().contains("frf91") ||
                Build.MANUFACTURER.toLowerCase().contains("unknown") ||
                Build.TAGS.toLowerCase().contains("test-keys") ||
                Build.USER.toLowerCase().contains("android-build"))
            return true;


        try {
            String openGl = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_RENDERER);
            if (openGl != null)
                if (openGl.toLowerCase().contains("bluestacks")
                        || openGl.toLowerCase().contains("translator"))
                    return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File sharedFolder = new File(Environment
                    .getExternalStorageDirectory().toString()
                    + File.separatorChar
                    + "windows"
                    + File.separatorChar
                    + "BstSharedFolder");

            if (sharedFolder.exists()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Method to check package names installed on the device with known package names
     *
     * @param context Application context
     * @return boolean - {@code true} if any of the package name is found or {@code false} if not.
     */
    public boolean checkPackageNames(Context context) {
        final PackageManager packageManager = context.getPackageManager();

        List<ApplicationInfo> packages =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages)
            for (String packageName : PACKAGES)
                if (applicationInfo.packageName.startsWith(packageName))
                    return true;
        return false;
    }


    /**
     * Method to read the driver file and check for the list of qemu drivers
     *
     * @return boolean - {@code true} if any driver is found to exist or {@code false} if not.
     */
    public boolean checkQEmuDrivers() {
        for (File drivers_file : new File[]{new File("/proc/tty/drivers"),
                new File("/proc/cpuinfo")})
            if (drivers_file.exists() && drivers_file.canRead()) {
                byte[] dataBuffer = new byte[1024];

                try (InputStream inputStream = new FileInputStream(drivers_file)) {
                    inputStream.read(dataBuffer);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                String driverData = new String(dataBuffer);

                for (String qemuDriver : QEMU_DRIVERS)
                    if (driverData.toLowerCase().contains(qemuDriver)) return true;
            }

        return false;
    }


    /**
     * Method to read the cpuinfo file and check for the existence to qemu cpuinfo
     *
     * @return boolean - {@code true} if any qemu cpuinfo is found to exist or {@code false} if not.
     */
    public boolean hasQemuCpuInfo() {
        try (BufferedReader cpuInfoReader =
                     new BufferedReader(new FileReader("/proc/cpuinfo"))) {
            String line;

            while ((line = cpuInfoReader.readLine()) != null)
                if (line.toLowerCase().toLowerCase().contains("goldfish")
                        || line.toLowerCase().toLowerCase().contains("ranchu"))
                    return true;
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * Method will query specific system properties to try and fingerprint a QEmu environment.
     * A minimum threshold must be met to prevent false positives.
     *
     * @param context Application context
     * @return boolean - {@code true} if  properties found crosses the threshold or {@code false} if not.
     */
    public boolean hasQemuProperties(Context context, boolean ignoreApiVersion) {
        final int MIN_PROPERTIES_THRESHOLD = 5;
        int foundProperties = 0;

        for (DeviceProperty deviceProperty : KNOWN_PROPS) {
            String propertyValue = Utilities.getProperty(context, deviceProperty.getPropertyName(),
                    ignoreApiVersion);

            if ((deviceProperty.getSeekValue() == null) && (propertyValue != null))
                foundProperties++;

            if ((deviceProperty.getSeekValue() != null)
                    && (propertyValue.toLowerCase().contains(deviceProperty.getSeekValue())))
                foundProperties++;

        }

        return foundProperties >= MIN_PROPERTIES_THRESHOLD;
    }


    /**
     * Method to check the existence of known pipes used by the Android QEmu environment.
     *
     * @return boolean - {@code true} if any pipes is found to exist or {@code false} if not.
     */
    public boolean hasPipes() {
        return checkFileExistence(QEMU_PIPES);
    }


    /**
     * Method to check the existence of known files used by the Android QEmu environment.
     *
     * @return {@code true} if any files is found to exist or {@code false} if not.
     */
    public boolean hasQEmuFiles() {
        return checkFileExistence(QEMU_FILES);
    }


    /**
     * Method to check the existence of known files used by the Genymotion environment.
     *
     * @return {@code true} if any files is found to exist or {@code false} if not.
     */
    public boolean hasGenyFiles() {
        return checkFileExistence(GENY_FILES);
    }


    /**
     * Method to check the existence of known files used by the Andy environment.
     *
     * @return {@code true} if any files is found to exist or {@code false} if not.
     */
    public boolean hasAndyFiles() {
        return checkFileExistence(ANDY_FILES);
    }


    /**
     * Method to check the existence of known files used by the Nox environment.
     *
     * @return {@code true} if any files is found to exist or {@code false} if not.
     */
    public boolean hasNoxFiles() {
        return checkFileExistence(NOX_FILES);
    }

    private boolean checkFileExistence(String[] fileList) {
        for (String file : fileList)
            if (new File(file).exists()) return true;
        return false;
    }


}
