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
package com.feilong.core.lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 * 泛型.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0
 * @deprecated 要加入到suite里
 */
@lombok.extern.slf4j.Slf4j
@Deprecated
public class GenericTest extends AbstractTest{

    /**
     * Test generic test.
     */
    @Test
    public void testGenericTest(){
        log.debug((String) getValue("jinxin", String.class));
        log.debug((String) getValue("jinxin", Integer.class));
    }

    /**
     * Gets the value.
     *
     * @param <T>
     *            the generic type
     * @param a
     *            the a
     * @param klass
     *            the klass
     * @return the value
     */
    public <T> T getValue(String a,Class<?> klass){
        try{
            Method method = GenericTest.class.getMethod("getValue", String.class, Class.class);
            TypeVariable<?> typeVariable = (TypeVariable<?>) method.getGenericReturnType();

            log.debug(typeVariable.toString());
            log.debug(typeVariable.getName());
            log.debug("" + typeVariable.getBounds()[0]);
            log.debug(typeVariable.getGenericDeclaration().toString());
            log.debug(method.toGenericString());
            log.debug(method.toString());
        }catch (SecurityException | NoSuchMethodException e){
            log.error(e.getClass().getName(), e);
        }
        return null;
    }

}