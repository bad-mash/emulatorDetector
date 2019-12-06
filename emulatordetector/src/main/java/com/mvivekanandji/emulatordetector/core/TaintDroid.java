package com.mvivekanandji.emulatordetector.core;


import android.content.Context;

import com.mvivekanandji.emulatordetector.utils.Utilities;

import java.io.FileDescriptor;

import javax.crypto.Cipher;

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
 * Class to detect TaintDroid presence
 * see http://www.appanalysis.org/
 */
public class TaintDroid {

    final private static String TAINTDROID_CLASS_NAME = "dalvik.system.Taint";
    final private static String TAINTDROID_PACKAGE_NAME = "org.appanalysis";

    /**
     * Checks if class specific to TaintDroid is present in the system
     *
     * @return boolean - {@code true} if class found else false
     */
    public static boolean hasTaintClass() {
        try {
            Class.forName(TAINTDROID_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    /**
     * Check if specific member variables injected by TaintDroid exist
     *
     * @return boolean - {@code true} if any of the taint member variable detected
     */
    public static boolean hasTaintMemberVariables() {
        boolean taintDetected = false;
        Class<FileDescriptor> fileDescriptorClass = FileDescriptor.class;

        try {
            fileDescriptorClass.getField("name");
            taintDetected = true;
        } catch (NoSuchFieldException e) {
            // unable to get field name
        }

        Class cipher = Cipher.class;
        try {
            cipher.getField("key");
            taintDetected = true;
        } catch (NoSuchFieldException nsfe) {
            // unable to get field key
        }

        return taintDetected;
    }

    /**
     * Detects if the package of TaintAndroid is present
     *
     * @param context Application context
     * @return boolean - {@code true} if the package name exists on the device
     */
    public static boolean hasAppAnalysisPackage(Context context) {
        return Utilities.isPackageInstalled(context, TAINTDROID_PACKAGE_NAME);
    }

}
