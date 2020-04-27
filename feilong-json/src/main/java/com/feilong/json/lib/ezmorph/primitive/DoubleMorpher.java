/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feilong.json.lib.ezmorph.primitive;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.feilong.json.lib.ezmorph.MorphException;

/**
 * Morphs to a double.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class DoubleMorpher extends AbstractDecimalMorpher{

    private double defaultValue;

    public DoubleMorpher(){
        super();
    }

    /**
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public DoubleMorpher(double defaultValue){
        super(true);
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }

        if (!(obj instanceof DoubleMorpher)){
            return false;
        }

        DoubleMorpher other = (DoubleMorpher) obj;
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
     */
    public double getDefaultValue(){
        return defaultValue;
    }

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
     * @exception MorphException
     *                if conversion cannot be performed successfully
     */
    public double morph(Object value){
        if (value == null){
            if (isUseDefault()){
                return defaultValue;
            }else{
                throw new MorphException("value is null");
            }
        }

        if (value instanceof Number){
            return ((Number) value).doubleValue();
        }else{
            double i = 0;
            try{
                i = Double.parseDouble(String.valueOf(value));
                return i;
            }catch (NumberFormatException nfe){
                if (isUseDefault()){
                    return defaultValue;
                }else{
                    throw new MorphException(nfe);
                }
            }
        }
    }

    @Override
    public Class morphsTo(){
        return Double.TYPE;
    }
}