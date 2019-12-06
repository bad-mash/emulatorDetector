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
 *
 * A simple model class (POJO if you may) to store property name and value to seek
 */
public class DeviceProperty {

    private String propertyName;
    private String seekValue;

    public DeviceProperty(String propertyName, String seekValue) {
        this.propertyName = propertyName;
        this.seekValue = seekValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSeekValue() {
        return seekValue;
    }

    public void setSeekValue(String seekValue) {
        this.seekValue = seekValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceProperty that = (DeviceProperty) o;
        return propertyName.equals(that.propertyName) &&
                Objects.equals(seekValue, that.seekValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, seekValue);
    }
}
