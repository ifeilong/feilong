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

import com.feilong.json.lib.ezmorph.MorphException;
import com.feilong.json.lib.ezmorph.ObjectMorpher;

/**
 * Morphs to a Class.<br>
 * This morpher is a singleton.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class ClassMorpher implements ObjectMorpher{

    /** The Constant INSTANCE. */
    private static final ClassMorpher INSTANCE = new ClassMorpher();

    //---------------------------------------------------------------

    /**
     * Returns the singleton instance.
     *
     * @return single instance of ClassMorpher
     */
    public static ClassMorpher getInstance(){
        return INSTANCE;
    }

    /**
     * Instantiates a new class morpher.
     */
    private ClassMorpher(){
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
        return INSTANCE == obj;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode(){
        return 42 + getClass().hashCode();
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
        if (value == null){
            return null;
        }

        if (value instanceof Class){
            return value;
        }

        if ("null".equals(value)){
            return null;
        }

        try{
            return Class.forName(value.toString());
        }catch (Exception e){
            throw new MorphException(e);
        }
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class morphsTo(){
        return Class.class;
    }

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class clazz){
        return true;
    }
}