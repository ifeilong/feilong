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

package net.sf.ezmorph;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.ezmorph.array.BooleanArrayMorpher;
import net.sf.ezmorph.array.ByteArrayMorpher;
import net.sf.ezmorph.array.CharArrayMorpher;
import net.sf.ezmorph.array.DoubleArrayMorpher;
import net.sf.ezmorph.array.FloatArrayMorpher;
import net.sf.ezmorph.array.IntArrayMorpher;
import net.sf.ezmorph.array.LongArrayMorpher;
import net.sf.ezmorph.array.ObjectArrayMorpher;
import net.sf.ezmorph.array.ShortArrayMorpher;
import net.sf.ezmorph.object.BooleanObjectMorpher;
import net.sf.ezmorph.object.CharacterObjectMorpher;
import net.sf.ezmorph.object.ClassMorpher;
import net.sf.ezmorph.object.NumberMorpher;
import net.sf.ezmorph.object.StringMorpher;
import net.sf.ezmorph.primitive.BooleanMorpher;
import net.sf.ezmorph.primitive.ByteMorpher;
import net.sf.ezmorph.primitive.CharMorpher;
import net.sf.ezmorph.primitive.DoubleMorpher;
import net.sf.ezmorph.primitive.FloatMorpher;
import net.sf.ezmorph.primitive.IntMorpher;
import net.sf.ezmorph.primitive.LongMorpher;
import net.sf.ezmorph.primitive.ShortMorpher;

/**
 * Covenient class for registering standard morphers to a ConvertRegistry.<br
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MorphUtils{

    /** Constant value for BigDecimal(1) */
    public static final BigDecimal BIGDECIMAL_ONE  = new BigDecimal("1");

    /** Constant value for BigDecimal(0) */
    public static final BigDecimal BIGDECIMAL_ZERO = new BigDecimal("0");

    /**
     * Clears and registers all standard morpehrs.
     *
     * @param morpherRegistry
     */
    public static void registerStandardMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.clear();
        registerStandardPrimitiveMorphers(morpherRegistry);
        registerStandardPrimitiveArrayMorphers(morpherRegistry);
        registerStandardObjectMorphers(morpherRegistry);
        registerStandardObjectArrayMorphers(morpherRegistry);
    }

    /**
     * Registers morphers for arrays of wrappers and String with standard default
     * values.<br>
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
     */
    public static void registerStandardObjectArrayMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new BooleanObjectMorpher(Boolean.FALSE)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new CharacterObjectMorpher(new Character('\0'))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(StringMorpher.getInstance()));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Byte.class, new Byte((byte) 0))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Short.class, new Short((short) 0))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Integer.class, new Integer(0))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Long.class, new Long(0))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Float.class, new Float(0))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(Double.class, new Double(0))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(BigInteger.class, BigInteger.ZERO)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(BigDecimal.class, MorphUtils.BIGDECIMAL_ZERO)));
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
     */
    public static void registerStandardObjectMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new BooleanObjectMorpher(Boolean.FALSE));
        morpherRegistry.registerMorpher(new CharacterObjectMorpher(new Character('\0')));
        morpherRegistry.registerMorpher(StringMorpher.getInstance());
        morpherRegistry.registerMorpher(new NumberMorpher(Byte.class, new Byte((byte) 0)));
        morpherRegistry.registerMorpher(new NumberMorpher(Short.class, new Short((short) 0)));
        morpherRegistry.registerMorpher(new NumberMorpher(Integer.class, new Integer(0)));
        morpherRegistry.registerMorpher(new NumberMorpher(Long.class, new Long(0)));
        morpherRegistry.registerMorpher(new NumberMorpher(Float.class, new Float(0)));
        morpherRegistry.registerMorpher(new NumberMorpher(Double.class, new Double(0)));
        morpherRegistry.registerMorpher(new NumberMorpher(BigInteger.class, BigInteger.ZERO));
        morpherRegistry.registerMorpher(new NumberMorpher(BigDecimal.class, MorphUtils.BIGDECIMAL_ZERO));
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
     */
    public static void registerStandardPrimitiveArrayMorphers(MorpherRegistry morpherRegistry){
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
     */
    public static void registerStandardPrimitiveMorphers(MorpherRegistry morpherRegistry){
        morpherRegistry.registerMorpher(new BooleanMorpher(false));
        morpherRegistry.registerMorpher(new CharMorpher('\0'));
        morpherRegistry.registerMorpher(new ByteMorpher((byte) 0));
        morpherRegistry.registerMorpher(new ShortMorpher((short) 0));
        morpherRegistry.registerMorpher(new IntMorpher(0));
        morpherRegistry.registerMorpher(new LongMorpher(0));
        morpherRegistry.registerMorpher(new FloatMorpher(0));
        morpherRegistry.registerMorpher(new DoubleMorpher(0));
    }

    private MorphUtils(){

    }
}