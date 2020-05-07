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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.feilong.lib.ezmorph.MorphException;
import com.feilong.lib.ezmorph.primitive.ByteMorpher;
import com.feilong.lib.ezmorph.primitive.DoubleMorpher;
import com.feilong.lib.ezmorph.primitive.FloatMorpher;
import com.feilong.lib.ezmorph.primitive.IntMorpher;
import com.feilong.lib.ezmorph.primitive.LongMorpher;
import com.feilong.lib.ezmorph.primitive.ShortMorpher;

/**
 * Morphs to a subclass of Number.<br>
 * Supported types are - Byte, Short, Integer, Long, Float, BigInteger,
 * BigtDecimal.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class NumberMorpher extends AbstractObjectMorpher{

    /** The default value. */
    private Number         defaultValue;

    /** The type. */
    private final Class<?> type;

    //---------------------------------------------------------------

    /**
     * Creates a new morpher for the target type.
     *
     * @param type
     *            must be a primitive or wrapper type. BigDecimal and BigInteger
     *            are also supported.
     */
    public NumberMorpher(Class<?> type){
        super(false);

        if (type == null){
            throw new MorphException("Must specify a type");
        }

        if (type != Byte.TYPE && //
                        type != Short.TYPE && //
                        type != Integer.TYPE && //
                        type != Long.TYPE && //
                        type != Float.TYPE && //
                        type != Double.TYPE && //
                        !Byte.class.isAssignableFrom(type) && //
                        !Short.class.isAssignableFrom(type) && //
                        !Integer.class.isAssignableFrom(type) && //
                        !Long.class.isAssignableFrom(type) && //
                        !Float.class.isAssignableFrom(type) && //
                        !Double.class.isAssignableFrom(type) && //
                        !BigInteger.class.isAssignableFrom(type) && //
                        !BigDecimal.class.isAssignableFrom(type)){
            throw new MorphException("Must specify a Number subclass");
        }

        //---------------------------------------------------------------

        this.type = type;
    }

    /**
     * Creates a new morpher for the target type with a default value.<br>
     * The defaultValue should be of the same class as the target type.
     *
     * @param type
     *            must be a primitive or wrapper type. BigDecimal and BigInteger
     *            are also supported.
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public NumberMorpher(Class<?> type, Number defaultValue){
        super(true);

        if (type == null){
            throw new MorphException("Must specify a type");
        }

        //---------------------------------------------------------------

        if (type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE && type != Float.TYPE
                        && type != Double.TYPE && !Byte.class.isAssignableFrom(type) && !Short.class.isAssignableFrom(type)
                        && !Integer.class.isAssignableFrom(type) && !Long.class.isAssignableFrom(type)
                        && !Float.class.isAssignableFrom(type) && !Double.class.isAssignableFrom(type)
                        && !BigInteger.class.isAssignableFrom(type) && !BigDecimal.class.isAssignableFrom(type)){
            throw new MorphException("Must specify a Number subclass");
        }

        if (defaultValue != null && !type.isInstance(defaultValue)){
            throw new MorphException("Default value must be of type " + type);
        }

        //---------------------------------------------------------------

        this.type = type;
        setDefaultValue(defaultValue);
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

        if (!(obj instanceof NumberMorpher)){
            return false;
        }

        //---------------------------------------------------------------

        NumberMorpher other = (NumberMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(type, other.type);
        if (isUseDefault() && other.isUseDefault()){
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        if (!isUseDefault() && !other.isUseDefault()){
            return builder.isEquals();
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * Returns the default value for this Morpher.
     *
     * @return the default value
     */
    public Number getDefaultValue(){
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
        builder.append(type);
        if (isUseDefault()){
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
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
        if (value != null && type.isAssignableFrom(value.getClass())){
            // no conversion needed
            return value;
        }

        String str = String.valueOf(value).trim();

        if (!type.isPrimitive() && (value == null || str.length() == 0 || "null".equalsIgnoreCase(str))){
            // if empty string and class != primitive treat it like null
            return null;
        }

        if (isDecimalNumber(type)){
            if (Float.class.isAssignableFrom(type) || Float.TYPE == type){
                return morphToFloat(str);
            }else if (Double.class.isAssignableFrom(type) || Double.TYPE == type){
                return morphToDouble(str);
            }else{
                return morphToBigDecimal(str);
            }
        }
        if (Byte.class.isAssignableFrom(type) || Byte.TYPE == type){
            return morphToByte(str);
        }
        if (Short.class.isAssignableFrom(type) || Short.TYPE == type){
            return morphToShort(str);
        }
        if (Integer.class.isAssignableFrom(type) || Integer.TYPE == type){
            return morphToInteger(str);
        }
        if (Long.class.isAssignableFrom(type) || Long.TYPE == type){
            return morphToLong(str);
        }
        return morphToBigInteger(str);
    }

    //---------------------------------------------------------------

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return type;
    }

    /**
     * Sets the defaultValue to use if the value to be morphed is null.<br>
     * The defaultValue should be of the same class as the type this morpher
     * returns with <code>morphsTo()</code>.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public void setDefaultValue(Number defaultValue){
        if (defaultValue != null && !type.isInstance(defaultValue)){
            throw new MorphException("Default value must be of type " + type);
        }
        this.defaultValue = defaultValue;
    }

    /**
     * Checks if is decimal number.
     *
     * @param type
     *            the type
     * @return true, if is decimal number
     */
    private static boolean isDecimalNumber(Class<?> type){
        return (Double.class.isAssignableFrom(type) || //
                        Float.class.isAssignableFrom(type) || //
                        Double.TYPE == type || //
                        Float.TYPE == type || //
                        BigDecimal.class.isAssignableFrom(type));
    }

    /**
     * Morph to big decimal.
     *
     * @param str
     *            the str
     * @return the object
     */
    private Object morphToBigDecimal(String str){
        if (isUseDefault()){
            return new BigDecimalMorpher((BigDecimal) defaultValue).morph(str);
        }
        return new BigDecimal(str);
    }

    /**
     * Morph to big integer.
     *
     * @param str
     *            the str
     * @return the object
     */
    private Object morphToBigInteger(String str){
        if (isUseDefault()){
            return new BigIntegerMorpher((BigInteger) defaultValue).morph(str);
        }
        return new BigInteger(str);
    }

    /**
     * Morph to byte.
     *
     * @param str
     *            the str
     * @return the object
     */
    private Object morphToByte(String str){
        if (isUseDefault()){
            if (defaultValue == null){
                return null;
            }
            return new ByteMorpher(defaultValue.byteValue()).morph(str);
        }
        return new ByteMorpher().morph(str);
    }

    /**
     * Morph to double.
     *
     * @param str
     *            the str
     * @return the object
     */
    private Object morphToDouble(String str){
        if (isUseDefault()){
            if (defaultValue == null){
                return null;
            }
            return new DoubleMorpher(defaultValue.doubleValue()).morph(str);
        }
        return new DoubleMorpher().morph(str);

    }

    /**
     * Morph to float.
     *
     * @param str
     *            the str
     * @return the object
     */
    private Object morphToFloat(String str){
        if (isUseDefault()){
            if (defaultValue == null){
                return null;
            }
            return new FloatMorpher(defaultValue.floatValue()).morph(str);
        }
        return new FloatMorpher().morph(str);
    }

    /**
     * Morph to integer.
     *
     * @param str
     *            the str
     * @return the object
     */
    private Object morphToInteger(String str){
        if (isUseDefault()){
            if (defaultValue == null){
                return null;
            }
            return new IntMorpher(defaultValue.intValue()).morph(str);
        }
        return new IntMorpher().morph(str);
    }

    private Object morphToLong(String str){
        if (isUseDefault()){
            if (defaultValue == null){
                return null;
            }
            return new LongMorpher(defaultValue.longValue()).morph(str);
        }
        return new LongMorpher().morph(str);
    }

    private Object morphToShort(String str){
        if (isUseDefault()){
            if (defaultValue == null){
                return null;
            }
            return new ShortMorpher(defaultValue.shortValue()).morph(str);
        }
        return new ShortMorpher().morph(str);
    }
}