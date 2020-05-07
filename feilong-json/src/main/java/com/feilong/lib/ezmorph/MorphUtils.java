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

package com.feilong.lib.ezmorph;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.feilong.lib.ezmorph.array.BooleanArrayMorpher;
import com.feilong.lib.ezmorph.array.ByteArrayMorpher;
import com.feilong.lib.ezmorph.array.CharArrayMorpher;
import com.feilong.lib.ezmorph.array.DoubleArrayMorpher;
import com.feilong.lib.ezmorph.array.FloatArrayMorpher;
import com.feilong.lib.ezmorph.array.IntArrayMorpher;
import com.feilong.lib.ezmorph.array.LongArrayMorpher;
import com.feilong.lib.ezmorph.array.ObjectArrayMorpher;
import com.feilong.lib.ezmorph.array.ShortArrayMorpher;
import com.feilong.lib.ezmorph.object.BooleanObjectMorpher;
import com.feilong.lib.ezmorph.object.CharacterObjectMorpher;
import com.feilong.lib.ezmorph.object.ClassMorpher;
import com.feilong.lib.ezmorph.object.NumberMorpher;
import com.feilong.lib.ezmorph.object.StringMorpher;
import com.feilong.lib.ezmorph.primitive.BooleanMorpher;
import com.feilong.lib.ezmorph.primitive.ByteMorpher;
import com.feilong.lib.ezmorph.primitive.CharMorpher;
import com.feilong.lib.ezmorph.primitive.DoubleMorpher;
import com.feilong.lib.ezmorph.primitive.FloatMorpher;
import com.feilong.lib.ezmorph.primitive.IntMorpher;
import com.feilong.lib.ezmorph.primitive.LongMorpher;
import com.feilong.lib.ezmorph.primitive.ShortMorpher;

/**
 * Covenient class for registering standard morphers to a ConvertRegistry.<br
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class MorphUtils{

    /**
     * Instantiates a new morph utils.
     */
    private MorphUtils(){

    }

    //---------------------------------------------------------------

    /**
     * Clears and registers all standard morpehrs.
     *
     * @param morpherRegistry
     *            the morpher registry
     */
    public static void registerStandardMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.clear();
        registerStandardPrimitiveMorphers(morpherRegistry);
        registerStandardPrimitiveArrayMorphers(morpherRegistry);
        registerStandardObjectMorphers(morpherRegistry);
        registerStandardObjectArrayMorphers(morpherRegistry);
    }

    //---------------------------------------------------------------

    /**
     * Registers morphers for arrays of wrappers and String with standard default values.<br>
     * <ul>
     * <li>Boolean - Boolean.FALSE</li>
     * <li>Character - new Character('\0')</li>
     * <li>Byte - new Byte( (byte)0 )</li>
     * <li>Short - new Short( (short)0 )</li>
     * <li>Integer - new Integer( 0 )</li>
     * <li>Long - new Long( 0 )</li>
     * <li>Float - new Float( 0 )</li>
     * <li>Double - new Double( 0 )</li>
     * <li>String - null</li>
     * <li>BigInteger - BigInteger.ZERO</li>
     * <li>BigDecimal - MorphUtils.BIGDECIMAL_ZERO</li>
     * </ul>
     *
     * @param morpherRegistry
     *            the morpher registry
     */
    private static void registerStandardObjectArrayMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new BooleanObjectMorpher(false)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new CharacterObjectMorpher('\0')));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(StringMorpher.getInstance()));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Byte.class, (byte) 0)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Short.class, (short) 0)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Integer.class, 0)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Long.class, 0L)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Float.class, 0f)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Double.class, 0d)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(BigInteger.class, BigInteger.ZERO)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(BigDecimal.class, BigDecimal.ZERO)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(ClassMorpher.getInstance()));
    }

    /**
     * Registers morphers for wrappers and String with standard default values.<br>
     * <ul>
     * <li>Boolean - Boolean.FALSE</li>
     * <li>Character - new Character('\0')</li>
     * <li>Byte - new Byte( (byte)0 )</li>
     * <li>Short - new Short( (short)0 )</li>
     * <li>Integer - new Integer( 0 )</li>
     * <li>Long - new Long( 0 )</li>
     * <li>Float - new Float( 0 )</li>
     * <li>Double - new Double( 0 )</li>
     * <li>String - null</li>
     * <li>BigInteger - BigInteger.ZERO</li>
     * <li>BigDecimal - MorphUtils.BIGDECIMAL_ZERO</li>
     * </ul>
     *
     * @param morpherRegistry
     *            the morpher registry
     */
    private static void registerStandardObjectMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new BooleanObjectMorpher(Boolean.FALSE));
        morpherRegistry.registerMorpher(new CharacterObjectMorpher('\0'));
        morpherRegistry.registerMorpher(StringMorpher.getInstance());
        morpherRegistry.registerMorpher(new NumberMorpher(Byte.class, (byte) 0));
        morpherRegistry.registerMorpher(new NumberMorpher(Short.class, (short) 0));
        morpherRegistry.registerMorpher(new NumberMorpher(Integer.class, 0));
        morpherRegistry.registerMorpher(new NumberMorpher(Long.class, 0L));
        morpherRegistry.registerMorpher(new NumberMorpher(Float.class, 0f));
        morpherRegistry.registerMorpher(new NumberMorpher(Double.class, 0d));
        morpherRegistry.registerMorpher(new NumberMorpher(BigInteger.class, BigInteger.ZERO));
        morpherRegistry.registerMorpher(new NumberMorpher(BigDecimal.class, BigDecimal.ZERO));
        morpherRegistry.registerMorpher(ClassMorpher.getInstance());
    }

    /**
     * Registers morphers for arrays of primitives with standard default values.<br>
     * <ul>
     * <li>boolean - false</li>
     * <li>char - '\0'</li>
     * <li>byte - 0</li>
     * <li>short - 0</li>
     * <li>int - 0</li>
     * <li>long - 0</li>
     * <li>float - 0</li>
     * <li>double - 0</li>
     * </ul>
     *
     * @param morpherRegistry
     *            the morpher registry
     */
    private static void registerStandardPrimitiveArrayMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new BooleanArrayMorpher(false));
        morpherRegistry.registerMorpher(new CharArrayMorpher('\0'));
        morpherRegistry.registerMorpher(new ByteArrayMorpher((byte) 0));
        morpherRegistry.registerMorpher(new ShortArrayMorpher((short) 0));
        morpherRegistry.registerMorpher(new IntArrayMorpher(0));
        morpherRegistry.registerMorpher(new LongArrayMorpher(0));
        morpherRegistry.registerMorpher(new FloatArrayMorpher(0));
        morpherRegistry.registerMorpher(new DoubleArrayMorpher(0));
    }

    /**
     * Registers morphers for primitives with standard default values.<br>
     * <ul>
     * <li>boolean - false</li>
     * <li>char - '\0'</li>
     * <li>byte - 0</li>
     * <li>short - 0</li>
     * <li>int - 0</li>
     * <li>long - 0</li>
     * <li>float - 0</li>
     * <li>double - 0</li>
     * </ul>
     *
     * @param morpherRegistry
     *            the morpher registry
     */
    private static void registerStandardPrimitiveMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new BooleanMorpher(false));
        morpherRegistry.registerMorpher(new CharMorpher('\0'));
        morpherRegistry.registerMorpher(new ByteMorpher((byte) 0));
        morpherRegistry.registerMorpher(new ShortMorpher((short) 0));
        morpherRegistry.registerMorpher(new IntMorpher(0));
        morpherRegistry.registerMorpher(new LongMorpher(0));
        morpherRegistry.registerMorpher(new FloatMorpher(0));
        morpherRegistry.registerMorpher(new DoubleMorpher(0));
    }

}