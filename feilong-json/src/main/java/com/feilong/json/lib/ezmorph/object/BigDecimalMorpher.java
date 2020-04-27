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

package com.feilong.json.lib.ezmorph.object;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.feilong.json.lib.ezmorph.MorphException;

/**
 * Morphs to a BigDecimal.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class BigDecimalMorpher extends AbstractObjectMorpher{

    private BigDecimal defaultValue;

    public BigDecimalMorpher(){
        super();
    }

    /**
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public BigDecimalMorpher(BigDecimal defaultValue){
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

        if (!(obj instanceof BigDecimalMorpher)){
            return false;
        }

        BigDecimalMorpher other = (BigDecimalMorpher) obj;
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
    public BigDecimal getDefaultValue(){
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

    @Override
    public Object morph(Object value){
        if (value instanceof BigDecimal){
            return value;
        }

        if (value == null){
            if (isUseDefault()){
                return defaultValue;
            }else{
                return null;
            }
        }

        if (value instanceof Number){
            if (value instanceof Float){
                Float f = ((Float) value);
                if (f.isInfinite() || f.isNaN()){
                    throw new MorphException("BigDecimal can not be infinite or NaN");
                }
            }else if (value instanceof Double){
                Double d = ((Double) value);
                if (d.isInfinite() || d.isNaN()){
                    throw new MorphException("BigDecimal can not be infinite or NaN");
                }
            }else if (value instanceof BigInteger){
                return new BigDecimal((BigInteger) value);
            }

            return new BigDecimal(((Number) value).doubleValue());
        }else{
            try{
                String str = String.valueOf(value).trim();
                if (str.length() == 0 || str.equalsIgnoreCase("null")){
                    return null;
                }else{
                    return new BigDecimal(str);
                }
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
        return BigDecimal.class;
    }
}