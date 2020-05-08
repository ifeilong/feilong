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

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.feilong.lib.ezmorph.MorphException;

/**
 * Morphs to a BigDecimal.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class BigDecimalMorpher extends AbstractObjectMorpher<BigDecimal>{

    /**
     * Instantiates a new big decimal morpher.
     */
    public BigDecimalMorpher(){
        super();
    }

    /**
     * Instantiates a new big decimal morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public BigDecimalMorpher(BigDecimal defaultValue){
        super(true);
        this.defaultValue = defaultValue;
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
        if (value instanceof BigDecimal){
            return value;
        }

        if (value == null){
            if (isUseDefault()){
                return defaultValue;
            }
            return null;
        }

        //---------------------------------------------------------------

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
                return toBigDecimal(value);
            }

            return toBigDecimal(value).doubleValue();
        }

        //---------------------------------------------------------------

        try{
            String str = String.valueOf(value).trim();
            if (str.length() == 0 || str.equalsIgnoreCase("null")){
                return null;
            }
            return toBigDecimal(str);
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
        return BigDecimal.class;
    }
}