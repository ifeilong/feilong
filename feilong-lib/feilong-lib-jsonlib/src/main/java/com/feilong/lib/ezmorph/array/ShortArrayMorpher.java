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

package com.feilong.lib.ezmorph.array;

import java.lang.reflect.Array;

import com.feilong.lib.ezmorph.MorphException;
import com.feilong.lib.ezmorph.primitive.ShortMorpher;

/**
 * Morphs an array to a short[].
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class ShortArrayMorpher extends AbstractArrayMorpher{

    private static final Class<?> SHORT_ARRAY_CLASS = short[].class;

    /** The default value. */
    private short                 defaultValue;

    //---------------------------------------------------------------

    /**
     * Instantiates a new short array morpher.
     */
    public ShortArrayMorpher(){
        super(false);
    }

    /**
     * Instantiates a new short array morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public ShortArrayMorpher(short defaultValue){
        super(true);
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value for this Morpher.
     *
     * @return the default value
     */
    public short getDefaultValue(){
        return defaultValue;
    }

    /**
     * Morph.
     *
     * @param array
     *            the array
     * @return the object
     */
    @Override
    public Object morph(Object array){
        if (array == null){
            return null;
        }

        if (SHORT_ARRAY_CLASS.isAssignableFrom(array.getClass())){
            // no conversion needed
            return array;
        }

        if (array.getClass().isArray()){
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(short.class, dimensions);
            ShortMorpher morpher = isUseDefault() ? new ShortMorpher(defaultValue) : new ShortMorpher();
            if (dims == 1){
                for (int index = 0; index < length; index++){
                    Array.set(result, index, morpher.morph(Array.get(array, index)));
                }
            }else{
                for (int index = 0; index < length; index++){
                    Array.set(result, index, morph(Array.get(array, index)));
                }
            }
            return result;
        }
        throw new MorphException("argument is not an array: " + array.getClass());
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return SHORT_ARRAY_CLASS;
    }
}