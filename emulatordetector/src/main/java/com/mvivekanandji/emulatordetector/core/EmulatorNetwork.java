package com.mvivekanandji.emulatordetector.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class EmulatorNetwork {

    /**
     * Method to check for "eth0" interface on the device
     *
     * @return boolean - {@code true} if "eth0" interface is found (which occurs only on emulators)
     */
    public static boolean checkEth0Interface() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaceEnumeration =
                 NetworkInterface.getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements(); ) {

                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();

                if (networkInterface.getName().equals("eth0")) return true;
            }
        } catch (SocketException ex) {
        }
        return false;
    }

    /**
     * Method to check for the known emulator ip on the device.
     * Default ip is "10.0.2.15"
     *
     * @param context Application context
     * @return boolean - {@code true} if the known ip is found on the device
     * (which occurs only on emulators)
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    private boolean checkIp(Context context) {
        boolean ipDetected = false;
        final String IP = "10.0.2.15";

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            String[] args = {"/system/bin/netcfg"};
            StringBuilder stringBuilder = new StringBuilder();

            try (InputStream inputStream = new ProcessBuilder(args)
                    .directory((new File("/system/bin/")))
                    .redirectErrorStream(true)
                    .start()
                    .getInputStream()) {

                byte[] readBuffer = new byte[1024];
                while (inputStream.read(readBuffer) != -1)
                    stringBuilder.append(new String(readBuffer));

            } catch (Exception ex) {
            }

            String netData = stringBuilder.toString();

            if (!TextUtils.isEmpty(netData)) {
                String[] array = netData.split("\n");

                for (String lan : array)
                    if ((lan.contains("wlan0") || lan.contains("tunl0") || lan.contains("eth0"))
                            && lan.contains(IP)) {
                        ipDetected = true;
                        break;
                    }
            }
        }
        return ipDetected;
    }
}
