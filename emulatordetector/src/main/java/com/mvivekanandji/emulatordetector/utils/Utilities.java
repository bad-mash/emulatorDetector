package com.mvivekanandji.emulatordetector.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

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
 * Common utilities used to detect emulator and/or root
 */
public class Utilities {
    /**
     * For Build.VERSION.SDK_INT <= 27 i.e. Oreo
     * Method to use reflection to invoke the SystemProperties.get command
     *
     * @param context          Application context
     * @param property         String
     * @param ignoreApiVersion boolean
     * @return String
     * @throws UnsupportedOperationException if Build version is greater than Oreo (API 27)
     *                                       and ignoreApiVersion is false
     */
    @Deprecated
    public static String getProperty(Context context, String property, boolean ignoreApiVersion)
            throws UnsupportedOperationException, NullPointerException {
        if (ignoreApiVersion || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                Class<?> systemProperties = classLoader.loadClass("android.os.SystemProperties");

                Method methodGet = systemProperties.getMethod("get", String.class);

                //Object[] params = {property};
                Object[] params = new Object[1];
                params[0] = property;

                return (String) methodGet.invoke(systemProperties, params);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception exception) {
                throw new NullPointerException(exception.getMessage());
            }
        } else throw new UnsupportedOperationException("Operation not supported for " +
                "Pie (API 28) and above.");
    }

    /**
     * Method to check whether given package name is installed on the device or not
     *
     * @param context     Application context
     * @param packageName String
     * @return boolean - {@code true} if package found
     * @see PackageManager#getInstallerPackageName(String)
     */
    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        try {
            packageManager.getInstallerPackageName(packageName);
            return true;
        } catch (IllegalArgumentException e) {
            //given package doesn't exists
            return false;
        }
    }

    /**
     * @param e     Exception thrown
     * @param debug boolean - true to log error
     * @param TAG   String to use while logging as Tag
     */
    public static void displayError(boolean debug, String TAG, Exception e) {
        if (debug) {
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param infoMessage String information to log
     * @param verbose     boolean - true to log information
     * @param TAG         String to use while logging as Tag
     */
    public static void displayInfo( boolean verbose, String TAG, String infoMessage) {
        if (verbose) Log.i(TAG, infoMessage);
    }

}
