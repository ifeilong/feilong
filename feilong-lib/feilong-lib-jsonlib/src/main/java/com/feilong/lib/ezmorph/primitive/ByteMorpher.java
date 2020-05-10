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

package com.feilong.lib.ezmorph.primitive;

import com.feilong.lib.ezmorph.IntegerValueUtil;
import com.feilong.lib.ezmorph.MorphException;

/**
 * Morphs to a byte.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class ByteMorpher extends AbstractPrimitiveMorpher<Byte>{

    /**
     * Instantiates a new byte morpher.
     */
    public ByteMorpher(){
        super();
    }

    /**
     * Instantiates a new byte morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public ByteMorpher(byte defaultValue){
        super(true);
        this.defaultValue = defaultValue;
    }

    //---------------------------------------------------------------

    /**
     * Morphs the input object into an output object of the supported type.
     *
     * @param value
     *            The input value to be morphed
     * @return the byte
     * @exception MorphException
     *                if conversion cannot be performed successfully
     */
    public byte morph(Object value){
        if (value == null){
            if (isUseDefault()){
                return defaultValue;
            }
            throw new MorphException("value is null");
        }

        //---------------------------------------------------------------

        if (value instanceof Number){
            return ((Number) value).byteValue();
        }
        byte i = 0;
        try{
            i = Byte.parseByte(IntegerValueUtil.getIntegerValue(value));
            return i;
        }catch (NumberFormatException nfe){
            if (isUseDefault()){
                return defaultValue;
            }
            throw new MorphException(nfe);
        }
    }

    //---------------------------------------------------------------

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return Byte.TYPE;
    }
}