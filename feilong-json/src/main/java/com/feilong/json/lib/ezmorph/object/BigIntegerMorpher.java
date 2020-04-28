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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.feilong.json.lib.ezmorph.MorphException;

/**
 * Morphs to a BigInteger.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class BigIntegerMorpher extends AbstractObjectMorpher{

    /** The default value. */
    private BigInteger defaultValue;

    /**
     * Instantiates a new big integer morpher.
     */
    public BigIntegerMorpher(){
        super();
    }

    /**
     * Instantiates a new big integer morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public BigIntegerMorpher(BigInteger defaultValue){
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

        if (!(obj instanceof BigIntegerMorpher)){
            return false;
        }

        BigIntegerMorpher other = (BigIntegerMorpher) obj;
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
    public BigInteger getDefaultValue(){
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
     * @param value
     *            the value
     * @return the object
     */
    @Override
    public Object morph(Object value){
        if (value instanceof BigInteger){
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
                    throw new MorphException("BigInteger can not be infinite or NaN");
                }
            }else if (value instanceof Double){
                Double d = ((Double) value);
                if (d.isInfinite() || d.isNaN()){
                    throw new MorphException("BigInteger can not be infinite or NaN");
                }
            }else if (value instanceof BigDecimal){
                return ((BigDecimal) value).toBigInteger();
            }
            return BigInteger.valueOf(((Number) value).longValue());
        }else{
            try{
                String str = getIntegerValue(value);
                if (str.length() == 0 || str.equalsIgnoreCase("null")){
                    return null;
                }else{
                    return new BigInteger(str);
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

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class morphsTo(){
        return BigInteger.class;
    }

    /**
     * Trims the String from the begining to the first "."
     *
     * @param obj
     *            the obj
     * @return the integer value
     */
    protected String getIntegerValue(Object obj){
        // use en_US Locale
        Locale defaultLocale = Locale.getDefault();
        String str = null;
        try{
            Locale.setDefault(Locale.US);
            str = String.valueOf(obj).trim();
        }finally{
            Locale.setDefault(defaultLocale);
        }

        int index = str.indexOf(".");
        if (index != -1){
            str = str.substring(0, index);
        }
        return str;
    }
}