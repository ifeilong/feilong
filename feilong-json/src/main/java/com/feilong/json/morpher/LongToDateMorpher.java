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

package com.feilong.json.morpher;

import java.util.Date;

import com.feilong.lib.ezmorph.MorphException;
import com.feilong.lib.ezmorph.object.AbstractObjectMorpher;

/**
 * Morphs a Long to a Date.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.2
 */
public final class LongToDateMorpher extends AbstractObjectMorpher<Date>{

    @Override
    public Object morph(Object timeMillisValue){
        if (timeMillisValue == null){
            return null;
        }

        if (Date.class.isAssignableFrom(timeMillisValue.getClass())){
            return timeMillisValue;
        }

        if (!supports(timeMillisValue.getClass())){
            throw new MorphException(timeMillisValue.getClass() + " is not supported");
        }

        //---------------------------------------------------------------
        return new Date((Long) timeMillisValue);
    }

    //---------------------------------------------------------------

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return Date.class;
    }

    //---------------------------------------------------------------

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class<?> clazz){
        return Long.class.isAssignableFrom(clazz);
    }
}