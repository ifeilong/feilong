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

package com.feilong.json.lib.ezmorph.array;

import java.lang.reflect.Array;

import com.feilong.json.lib.ezmorph.ObjectMorpher;

/**
 * Base class for array Morphers.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public abstract class AbstractArrayMorpher implements ObjectMorpher{

    /** The use default. */
    private boolean useDefault = false;

    //---------------------------------------------------------------

    /**
     * Instantiates a new abstract array morpher.
     */
    public AbstractArrayMorpher(){
    }

    /**
     * Instantiates a new abstract array morpher.
     *
     * @param useDefault
     *            if morph() should return a default value if the value to
     *            be morphed is null
     */
    public AbstractArrayMorpher(boolean useDefault){
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
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class clazz){
        return clazz.isArray();
    }

    /**
     * Creates an array representing the dimensions for comversion.
     *
     * @param length
     *            the length
     * @param initial
     *            the initial
     * @return the int[]
     */
    protected int[] createDimensions(int length,int initial){
        Object dims = Array.newInstance(int.class, length);
        Array.set(dims, 0, new Integer(initial));
        return (int[]) dims;
    }

    /**
     * Returns the number of dimensions in an array class.
     *
     * @param arrayClass
     *            the array class
     * @return the dimensions
     */
    protected int getDimensions(Class arrayClass){
        if (arrayClass == null || !arrayClass.isArray()){
            return 0;
        }

        return 1 + getDimensions(arrayClass.getComponentType());
    }
}