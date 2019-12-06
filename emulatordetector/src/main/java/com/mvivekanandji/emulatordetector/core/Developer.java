package com.mvivekanandji.emulatordetector.core;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

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
 * Class to detect developer options, usb and adb
 */
public class Developer {

    /**
     * @param context application or activity context
     * @return boolean - {@code true} if developer options is enabled
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDeveloperOptionEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }

    /**
     * @param context Application context
     * @return boolean - {@code true} if usb debugging is enabled
     */
    public static boolean isUsbDebuggingEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0) != 0;
    }

    /**
     * @param context Application context
     * @return boolean - {@code true} if connected to usb
     */
    public static boolean isConnectedToUsb(Context context) {
        Intent intent = context.registerReceiver(null,
                new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
    }

}
