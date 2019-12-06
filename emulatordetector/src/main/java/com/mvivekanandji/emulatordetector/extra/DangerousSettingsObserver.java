package com.mvivekanandji.emulatordetector.extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;

import com.mvivekanandji.emulatordetector.utils.Utilities;


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
 * Created by Vivekanand Mishra on 03/12/19.
 *
 * @author vivekanand
 * @version 1.0
 * <p>
 * <p>
 * Class to continuously detect dangerous settings
 */
public class DangerousSettingsObserver {

    private static final String TAG = DangerousSettingsObserver.class.getSimpleName();

    private final Context context;
    private boolean verbose;
    private boolean debug;

    @SuppressLint("StaticFieldLeak") //application context will be used,
    private static DangerousSettingsObserver dangerousSettingsObserver;

    /**
     * private constructor
     */
    private DangerousSettingsObserver(Context context) {
        this.context = context;
        verbose = false;
        debug = false;
        Utilities.displayInfo(verbose, TAG,
                "Singleton Object created: " + dangerousSettingsObserver);
    }

    /**
     * @param context Activity Context
     * @return DangerousSettingsObserver
     */
    public static DangerousSettingsObserver with(Context context) {
        if (context == null)
            throw new IllegalArgumentException(TAG + ": Context must not be null.");

        if (dangerousSettingsObserver == null)
            dangerousSettingsObserver = new DangerousSettingsObserver(context.getApplicationContext());

        return dangerousSettingsObserver;
    }


    //later
    ContentObserver observer = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            int enabled = Settings.System.getInt(context.getContentResolver(),
                    Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    };

    //region getter/setter

    /**
     * Getter
     *
     * @return boolean
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Setter - to set debug (will log info messages when true)
     *
     * @param verbose boolean
     * @return this object
     */
    public DangerousSettingsObserver setVerbose(boolean verbose) {
        this.verbose = verbose;
        Utilities.displayInfo(verbose, TAG, "Verbose: " + verbose);
        return this;
    }

    /**
     * Getter
     *
     * @return boolean
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Setter - to set debug (will log error messages when true)
     *
     * @param debug boolean
     * @return this object
     */
    public DangerousSettingsObserver setDebug(boolean debug) {
        this.debug = debug;
        Utilities.displayInfo(verbose, TAG, "Verbose: " + verbose);
        return this;
    }
    //endregion
}
