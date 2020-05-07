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

package com.feilong.lib.ezmorph.object;

import com.feilong.lib.ezmorph.ObjectMorpher;

/**
 * The Class EnumMorpher.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class EnumMorpher implements ObjectMorpher{

    /** The enum class. */
    private final Class<?> enumClass;

    //---------------------------------------------------------------

    /**
     * Instantiates a new enum morpher.
     *
     * @param enumClass
     *            the enum class
     */
    public EnumMorpher(Class<?> enumClass){
        if (enumClass == null){
            throw new IllegalArgumentException("enumClass is null");
        }
        if (!Enum.class.isAssignableFrom(enumClass)){
            throw new IllegalArgumentException("enumClass is not an Enum class");
        }
        this.enumClass = enumClass;
    }

    //---------------------------------------------------------------

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
            return enumClass.cast(null);
        }
        return Enum.valueOf((Class) enumClass, String.valueOf(value));
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return enumClass;
    }

    //---------------------------------------------------------------

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class<?> clazz){
        return String.class.isAssignableFrom(clazz);
    }
}