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
package com.feilong.lib.json;

import static com.feilong.core.util.MapUtil.newTreeMap;

import java.util.Map;

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.lib.json.util.JSONExceptionUtil;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.6
 */
public class BeanNewer{

    /** Don't let anyone instantiate this class. */
    private BeanNewer(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static Object newBean(Class<?> rootClass) throws JSONException{
        try{
            if (rootClass.isInterface()){
                if (!Map.class.isAssignableFrom(rootClass)){
                    throw new JSONException("beanClass is an interface. " + rootClass);
                }
                return newTreeMap();
            }
            return ConstructorUtil.newInstance(rootClass);
        }catch (Exception e){
            throw JSONExceptionUtil.build("rootClass:" + rootClass.getCanonicalName(), e);
        }
    }

}
