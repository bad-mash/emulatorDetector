package com.mvivekanandji.emulatordetector.core;

import android.os.Debug;

import com.mvivekanandji.emulatordetector.model.Tcp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
 * Class to detect android debugger
 */
public class Debugger {

    /**
     * Some packers still use this, being default
     *
     * @return boolean - {@code true} if debugger is connected
     * @see Debug#isDebuggerConnected()
     */
    public static boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected();
    }


    /**
     * Method to detect if someone/some application is  ptracing the application.
     * see https://man7.org/linux/man-pages/man2/ptrace.2.html
     * see https://en.wikipedia.org/wiki/Ptrace
     *
     * @return boolean - {@code true} if ptracing is detected.
     */
    public static boolean isBeingPTraced() {
        final String TRACE_PID = "TracerPid";
        final int TRACE_PID_LENGTH = TRACE_PID.length();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                (new FileInputStream("/proc/self/status")), 1000)) {
            String line;

            while ((line = bufferedReader.readLine()) != null)
                if (line.length() > TRACE_PID_LENGTH)
                    if (line.substring(0, TRACE_PID_LENGTH).equalsIgnoreCase(TRACE_PID)) {
                        if (Integer.decode(line.substring(TRACE_PID_LENGTH + 1).trim()) > 0)
                            return true;
                        break;
                    }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * Method to detect if adb is used to mask emulator
     * <p>
     * Adb always bounce to 0.0.0.0, even though the port can change,
     * real devices should be != 127.0.0.1
     *
     * @return boolean - {@code true} if adb is present
     */
    public static boolean hasAdbInEmulator() {
        boolean adbInEmulator = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader
                (new FileInputStream("/proc/net/tcp")), 1000)) {
            String line;
            ArrayList<Tcp> tcpList = new ArrayList<>();

            // to skip column names
            reader.readLine();

            while ((line = reader.readLine()) != null)
                tcpList.add(Tcp.create(line.split("\\W+")));

            int adbPort = -1;
            for (Tcp tcpItem : tcpList)
                if (tcpItem.getLocalIp() == 0) {
                    adbPort = tcpItem.getLocalPort();
                    break;
                }

            if (adbPort != -1)
                for (Tcp tcpItem : tcpList)
                    if ((tcpItem.getLocalIp() != 0) && (tcpItem.getLocalPort() == adbPort))
                        adbInEmulator = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return adbInEmulator;
    }


}
