package com.mvivekanandji.emulatordetector.core;

import android.app.ActivityManager;

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
 * Optimistic class to detect if the use is monkey
 */
public class Monkey {

    /**
     * @return boolean - {@code true} if the user interface is currently being messed with by a monkey.
     * @see ActivityManager#isUserAMonkey()
     */
    public static boolean isUserAMonkey() {
        return ActivityManager.isUserAMonkey();
    }
}
