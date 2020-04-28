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

package com.feilong.json.lib.ezmorph.array;

import java.lang.reflect.Array;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.feilong.json.lib.ezmorph.MorphException;
import com.feilong.json.lib.ezmorph.primitive.BooleanMorpher;

/**
 * Morphs an array to a boolean[].
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class BooleanArrayMorpher extends AbstractArrayMorpher{

    /** The Constant BOOLEAN_ARRAY_CLASS. */
    private static final Class BOOLEAN_ARRAY_CLASS = boolean[].class;

    /** The default value. */
    private boolean            defaultValue;

    /**
     * Instantiates a new boolean array morpher.
     */
    public BooleanArrayMorpher(){
        super(false);
    }

    /**
     * Instantiates a new boolean array morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public BooleanArrayMorpher(boolean defaultValue){
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

        if (!(obj instanceof BooleanArrayMorpher)){
            return false;
        }

        BooleanArrayMorpher other = (BooleanArrayMorpher) obj;
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
     * Gets the default value.
     *
     * @return the default value
     */
    public boolean getDefaultValue(){
        return defaultValue;
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
     * @param array
     *            the array
     * @return the object
     */
    @Override
    public Object morph(Object array){
        if (array == null){
            return null;
        }

        if (BOOLEAN_ARRAY_CLASS.isAssignableFrom(array.getClass())){
            // no conversion needed
            return array;
        }

        if (array.getClass().isArray()){
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(boolean.class, dimensions);
            BooleanMorpher morpher = isUseDefault() ? new BooleanMorpher(defaultValue) : new BooleanMorpher();
            if (dims == 1){
                for (int index = 0; index < length; index++){
                    Array.set(result, index, morpher.morph(Array.get(array, index)) ? Boolean.TRUE : Boolean.FALSE);
                }
            }else{
                for (int index = 0; index < length; index++){
                    Array.set(result, index, morph(Array.get(array, index)));
                }
            }
            return result;
        }else{
            throw new MorphException("argument is not an array: " + array.getClass());
        }
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class morphsTo(){
        return BOOLEAN_ARRAY_CLASS;
    }
}