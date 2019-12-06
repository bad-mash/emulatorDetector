package com.mvivekanandji.emulatordetector.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;

public class EmulatorTelephony {

    /**
     * Default emulator phone numbers.
     * Android emulator allows total 16 instances to run concurrently
     * <p>
     * First emulator uses port 5554, subsequent instances use port numbers increasing by two.
     * Default number is  "15555215554"
     */
    private String[] NUMBERS = {
            "15555215554",
            "15555215556", "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572", "15555215574",
            "15555215576", "15555215578", "15555215580", "15555215582", "15555215584",};

    private String[] SUBSCRIBER_IDS = {

    };

    private String[] VOICE_MAIL_NUMBERS = {
            "15552175049"
    };

    /**
     * Each emulator device instance created is given an device/emulator id.
     * Default emulator id is "000000000000000"
     */
    private String[] DEVICE_IDS = {
            "000000000000000",
            "e21833235b6eef10",
            "012345678912345"};

    /**
     * IMSI - International mobile subscriber identity
     * see https://en.wikipedia.org/wiki/International_mobile_subscriber_identity
     * Default IMSI or subscriber Id is "310260000000000"
     */
    private String[] IMSI_IDS = {
            "310260000000000",
            "0000000000"
    };

    private String[] NETWORK_OPERATOR_NAMES = {
            "Android"
    };

    private String[] SIM_OPERATOR_NAMES = {
            "Android"
    };


    /**
     * Method to determine if device has sim or not.
     * CAUTION: This method does not differentiate between real and emulated sim.
     *
     * @param context Application context
     * @return boolean - {@code true} if device has SIM (emulated or real
     * {{@link #hasEmulatorTelephonyProperty(Context)}})
     * @throws NoSuchFieldException when unable to get network operator name
     */
    public boolean deviceHasSim(Context context) throws NoSuchFieldException {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager.getNetworkOperatorName() != null)
            return !telephonyManager.getNetworkOperatorName().isEmpty();
        else
            throw new NoSuchFieldException("Operator name absent");
    }

    /**
     * Method to detect emulator by checking default telephone properties of emulator
     *
     * @param context Application context
     * @return boolean - {@code true} if emulator default telephone properties are found
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public boolean hasEmulatorTelephonyProperty(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED && isSupportTelephony(context)) {

            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            return checkPhoneNumber(telephonyManager)
                    || checkVoiceMailNumber(telephonyManager)
                    || checkDeviceId(telephonyManager)
                    || checkImsi(telephonyManager)
                    || checkNetworkOperatorName(telephonyManager)
                    || checkSimOperatorName(telephonyManager);
        }
        return false;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private boolean checkPhoneNumber(TelephonyManager telephonyManager) {
        @SuppressLint("HardwareIds") String devicePhoneNumber = telephonyManager.getLine1Number();

        for (String number : NUMBERS)
            if (number.equalsIgnoreCase(devicePhoneNumber)) return true;
        return false;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private boolean checkVoiceMailNumber(TelephonyManager telephonyManager) {
        @SuppressLint("HardwareIds") String deviceVoiceMailNumber = telephonyManager.getVoiceMailNumber();

        for (String voiceMailNumber : VOICE_MAIL_NUMBERS)
            if (voiceMailNumber.equalsIgnoreCase(deviceVoiceMailNumber)) return true;
        return false;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private boolean checkDeviceId(TelephonyManager telephonyManager) {
        @SuppressLint("HardwareIds") String deviceId = telephonyManager.getDeviceId();

        for (String id : DEVICE_IDS)
            if (id.equalsIgnoreCase(deviceId)) return true;
        return false;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private boolean checkImsi(TelephonyManager telephonyManager) {
        @SuppressLint("HardwareIds") String deviceImsi = telephonyManager.getSubscriberId();

        for (String imsi : IMSI_IDS)
            if (imsi.equalsIgnoreCase(deviceImsi)) return true;
        return false;
    }

    private boolean checkNetworkOperatorName(TelephonyManager telephonyManager) {
        String deviceNetworkOperatorName = telephonyManager.getNetworkOperatorName();

        for (String networkOperatorName : NETWORK_OPERATOR_NAMES)
            if (networkOperatorName.equalsIgnoreCase(deviceNetworkOperatorName)) return true;
        return false;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private boolean checkSimOperatorName(TelephonyManager telephonyManager) {
        String deviceSimOperatorName = telephonyManager.getSimOperatorName();

        for (String simOperatorName : SIM_OPERATOR_NAMES)
            if (simOperatorName.equalsIgnoreCase(deviceSimOperatorName)) return true;
        return false;
    }

    private boolean isSupportTelephony(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

}
