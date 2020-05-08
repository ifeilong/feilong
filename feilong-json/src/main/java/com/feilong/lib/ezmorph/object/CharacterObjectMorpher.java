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

import com.feilong.lib.ezmorph.MorphException;

/**
 * Morphs to a Character.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class CharacterObjectMorpher extends AbstractObjectMorpher<Character>{

    /**
     * Instantiates a new character object morpher.
     */
    public CharacterObjectMorpher(){
        super();
    }

    /**
     * Instantiates a new character object morpher.
     *
     * @param defaultValue
     *            return value if the value to be morphed is null
     */
    public CharacterObjectMorpher(Character defaultValue){
        super(true);
        this.defaultValue = defaultValue;
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
            if (isUseDefault()){
                return defaultValue;
            }
            throw new MorphException("value is null");
        }

        if (value instanceof Character){
            return value;
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
        return Character.class;
    }
}