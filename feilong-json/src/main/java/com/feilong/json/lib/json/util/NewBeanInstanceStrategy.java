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

package com.feilong.json.lib.json.util;

import java.lang.reflect.InvocationTargetException;

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.json.lib.json.JSONObject;

/**
 * Base class for creating Bean instances.<br>
 * <ul>
 * <li>DEFAULT - calls Class.newInstance().</li>
 * </ul>
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public abstract class NewBeanInstanceStrategy{

    /** Calls Class.newInstance() */
    public static final NewBeanInstanceStrategy DEFAULT = new DefaultNewBeanInstanceStrategy();

    /**
     * Creates a new instance.
     *
     * @param target
     *            the source class
     * @param source
     *            additional properties that may be needed to create the
     *            instance
     * @return the object
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws SecurityException
     *             the security exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    public abstract Object newInstance(Class target,JSONObject source)
                    throws InstantiationException,IllegalAccessException,SecurityException,NoSuchMethodException,InvocationTargetException;

    /**
     * The Class DefaultNewBeanInstanceStrategy.
     */
    private static final class DefaultNewBeanInstanceStrategy extends NewBeanInstanceStrategy{

        /**
         * New instance.
         *
         * @param target
         *            the target
         * @param source
         *            the source
         * @return the object
         * @throws InstantiationException
         *             the instantiation exception
         * @throws IllegalAccessException
         *             the illegal access exception
         * @throws SecurityException
         *             the security exception
         * @throws NoSuchMethodException
         *             the no such method exception
         * @throws InvocationTargetException
         *             the invocation target exception
         */
        @Override
        public Object newInstance(Class target,JSONObject source) throws InstantiationException,IllegalAccessException,SecurityException,
                        NoSuchMethodException,InvocationTargetException{
            return ConstructorUtil.newInstance(target);
        }
    }
}