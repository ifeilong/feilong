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
import com.feilong.lib.ezmorph.primitive.BooleanMorpher;

/**
 * Morphs an array to a Boolean[].
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class BooleanObjectArrayMorpher extends AbstractArrayMorpher{

    private static final Class<?> BOOLEAN_OBJECT_ARRAY_CLASS = Boolean[].class;

    /** The default value. */
    private Boolean               defaultValue;

    //---------------------------------------------------------------

    /**
     * Instantiates a new boolean object array morpher.
     */
    public BooleanObjectArrayMorpher(){
        super(false);
    }

    /**
     * Instantiates a new boolean object array morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public BooleanObjectArrayMorpher(Boolean defaultValue){
        super(true);
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the default value.
     *
     * @return the default value
     */
    public Boolean getDefaultValue(){
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

        if (BOOLEAN_OBJECT_ARRAY_CLASS.isAssignableFrom(array.getClass())){
            // no conversion needed
            return array;
        }

        if (array.getClass().isArray()){
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(Boolean.class, dimensions);
            if (dims == 1){
                BooleanMorpher morpher = null;
                if (isUseDefault()){
                    if (defaultValue == null){
                        for (int index = 0; index < length; index++){
                            Array.set(result, index, null);
                        }
                        return result;
                    }
                    morpher = new BooleanMorpher(defaultValue.booleanValue());
                }else{
                    morpher = new BooleanMorpher();
                }
                for (int index = 0; index < length; index++){
                    Array.set(result, index, morpher.morph(Array.get(array, index)) ? Boolean.TRUE : Boolean.FALSE);
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
        return BOOLEAN_OBJECT_ARRAY_CLASS;
    }
}