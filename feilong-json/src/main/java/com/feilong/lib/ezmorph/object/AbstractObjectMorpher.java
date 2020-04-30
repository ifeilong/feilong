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

import com.feilong.lib.ezmorph.ObjectMorpher;

/**
 * Base class for ObjectMorpher implementations.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public abstract class AbstractObjectMorpher implements ObjectMorpher{

    /** The use default. */
    private boolean useDefault;

    //---------------------------------------------------------------

    /**
     * Instantiates a new abstract object morpher.
     */
    public AbstractObjectMorpher(){

    }

    /**
     * Instantiates a new abstract object morpher.
     *
     * @param useDefault
     *            if morph() should return a default value if the value to
     *            be morphed is null
     */
    public AbstractObjectMorpher(boolean useDefault){
        this.useDefault = useDefault;
    }

    /**
     * Returns if this morpher will use a default value.
     *
     * @return true, if is use default
     */
    public boolean isUseDefault(){
        return useDefault;
    }

    /**
     * Sets if this morpher will use a default value.
     *
     * @param useDefault
     *            the new use default
     */
    public void setUseDefault(boolean useDefault){
        this.useDefault = useDefault;
    }

    //---------------------------------------------------------------

    /**
     * Returns true if the Morpher supports conversion from this Class.<br>
     * Supports any type that is not an Array.
     *
     * @param clazz
     *            the source Class
     * @return true if clazz is supported by this morpher, false otherwise.
     */
    @Override
    public boolean supports(Class clazz){
        return !clazz.isArray();
    }
}