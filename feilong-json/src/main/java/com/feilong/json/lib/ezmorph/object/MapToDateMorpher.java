/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feilong.json.lib.ezmorph.object;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.feilong.json.lib.ezmorph.MorphException;

/**
 * Morphs a Map into a Date.<br>
 * The Map should have at least one of the following keys
 * [yer,month,day,hour,minutes,seconds,milliseconds] and the values should be
 * instances of Number. Any key that is not defined will have zero (0) assigned
 * as its value.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class MapToDateMorpher extends AbstractObjectMorpher{

    /** The default value. */
    private Date defaultValue;

    /**
     * Instantiates a new map to date morpher.
     */
    public MapToDateMorpher(){
        super();
    }

    /**
     * Instantiates a new map to date morpher.
     *
     * @param defaultValue
     *            the default value
     */
    public MapToDateMorpher(Date defaultValue){
        super(true);
        this.defaultValue = defaultValue;
    }

    /**
     * Equals.
     *
     * @param obj
     *            the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }

        if (!(obj instanceof MapToDateMorpher)){
            return false;
        }

        MapToDateMorpher other = (MapToDateMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        if (isUseDefault() && other.isUseDefault()){
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }else if (!isUseDefault() && !other.isUseDefault()){
            return builder.isEquals();
        }else{
            return false;
        }
    }

    /**
     * Returns the default value for this Morpher.
     *
     * @return the default value
     */
    public Date getDefaultValue(){
        return (Date) defaultValue.clone();
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode(){
        HashCodeBuilder builder = new HashCodeBuilder();
        if (isUseDefault()){
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    /**
     * Morph.
     *
     * @param value
     *            the value
     * @return the object
     */
    @Override
    public Object morph(Object value){
        if (value == null){
            return null;
        }

        if (Date.class.isAssignableFrom(value.getClass())){
            return value;
        }

        if (!supports(value.getClass())){
            throw new MorphException(value.getClass() + " is not supported");
        }

        Map map = (Map) value;
        if (map.isEmpty()){
            if (isUseDefault()){
                return defaultValue;
            }else{
                throw new MorphException("Unable to parse the date " + value);
            }
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, getValue(map, "year"));
        c.set(Calendar.MONTH, getValue(map, "month"));
        c.set(Calendar.DATE, getValue(map, "day"));
        c.set(Calendar.HOUR_OF_DAY, getValue(map, "hour"));
        c.set(Calendar.MINUTE, getValue(map, "minutes"));
        c.set(Calendar.SECOND, getValue(map, "seconds"));
        c.set(Calendar.MILLISECOND, getValue(map, "milliseconds"));
        return c.getTime();
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class morphsTo(){
        return Date.class;
    }

    /**
     * Sets the defaultValue to use if the value to be morphed is null.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public void setDefaultValue(Date defaultValue){
        this.defaultValue = (Date) defaultValue.clone();
    }

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class clazz){
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

    /**
     * Gets the value.
     *
     * @param map
     *            the map
     * @param key
     *            the key
     * @return the value
     */
    private int getValue(Map map,String key){
        Object value = map.get(key);
        if (value == null || !(value instanceof Number)){
            return 0;
        }

        Number n = (Number) value;
        return n.intValue();
    }
}