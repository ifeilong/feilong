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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.feilong.lib.ezmorph.MorphException;

/**
 * Morphs to a char.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class CharMorpher extends AbstractPrimitiveMorpher{

    /** The default value. */
    private char defaultValue;

    //---------------------------------------------------------------

    /**
     * Instantiates a new char morpher.
     */
    public CharMorpher(){
        super();
    }

    /**
     * Instantiates a new char morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public CharMorpher(char defaultValue){
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

        if (!(obj instanceof CharMorpher)){
            return false;
        }

        CharMorpher other = (CharMorpher) obj;
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
    public char getDefaultValue(){
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
     * Morphs the input object into an output object of the supported type.
     *
     * @param value
     *            The input value to be morphed
     * @return the char
     * @exception MorphException
     *                if conversion cannot be performed successfully
     */
    public char morph(Object value){
        if (value == null){
            if (isUseDefault()){
                return defaultValue;
            }
            throw new MorphException("value is null");
        }

        //---------------------------------------------------------------

        if (value instanceof Character){
            return ((Character) value).charValue();
        }
        String s = String.valueOf(value);
        if (s.length() > 0){
            return s.charAt(0);
        }
        if (isUseDefault()){
            return defaultValue;
        }
        throw new MorphException("Can't morph value: " + value);
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return Character.TYPE;
    }
}