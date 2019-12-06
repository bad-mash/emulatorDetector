package com.mvivekanandji.emulatordetector.model;


import java.util.Objects;

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
 * A simple model class (POJO if you may) to represent tcp entry
 */
public class Tcp {

    private  int id;
    private long localIp;
    private int localPort;

    /**
     * Constructor
     *
     * @param id String
     * @param localIp String
     * @param localPort String
     * @param remoteIp String
     * @param remotePort String
     * @param state String
     * @param tx_queue String
     * @param rx_queue String
     * @param tr String
     * @param tm_when String
     * @param retrnsmt String
     * @param uid String
     * @param timeout String
     * @param inode String
     */
    private Tcp(String id, String localIp, String localPort, String remoteIp, String remotePort,
               String state, String tx_queue, String rx_queue, String tr, String tm_when,
               String retrnsmt, String uid, String timeout, String inode) {
        this.id = Integer.parseInt(id, 16);
        this.localIp = Long.parseLong(localIp, 16);
        this.localPort = Integer.parseInt(localPort, 16);
    }

    public static Tcp create(String[] params) {
        return new Tcp(params[1], params[2], params[3], params[4], params[5], params[6], params[7],
                params[8], params[9], params[10], params[11], params[12], params[13], params[14]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLocalIp() {
        return localIp;
    }

    public void setLocalIp(long localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tcp tcp = (Tcp) o;
        return id == tcp.id &&
                localIp == tcp.localIp &&
                localPort == tcp.localPort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, localIp, localPort);
    }
}
