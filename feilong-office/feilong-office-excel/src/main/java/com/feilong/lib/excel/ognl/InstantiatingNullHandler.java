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
package com.feilong.lib.excel.ognl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ognl.NullHandler;
import ognl.Ognl;
import ognl.OgnlRuntime;

/**
 * The Class InstantiatingNullHandler.
 */
public class InstantiatingNullHandler implements NullHandler{

    /** The Constant log. */
    private static final Logger LOGGER                   = LoggerFactory.getLogger(InstantiatingNullHandler.class);

    //---------------------------------------------------------------

    static final String         USING_LOXIA_NULL_HANDLER = "loxia.useingLoxiaNullHandler";

    /** The ignore list. */
    private final List<String>  ignoreList               = new ArrayList<>();

    /** The handler wrapper. */
    private final NullHandler   handlerWrapper;

    //---------------------------------------------------------------

    public InstantiatingNullHandler(NullHandler nullHandler, List<String> ignoreList){
        this.handlerWrapper = nullHandler;
        this.ignoreList.addAll(ignoreList);
    }

    //---------------------------------------------------------------

    @Override
    public Object nullMethodResult(Map context,Object target,String methodName,Object[] args){
        Boolean flag = (Boolean) context.get(USING_LOXIA_NULL_HANDLER);
        if (handlerWrapper != null && ((flag == null) || !flag)){
            return handlerWrapper.nullMethodResult(context, target, methodName, args);
        }
        LOGGER.debug("Entering nullMethodResult ");
        return null;
    }

    @Override
    public Object nullPropertyValue(Map context,Object target,Object property){
        Boolean flag = (Boolean) context.get(USING_LOXIA_NULL_HANDLER);
        if (handlerWrapper != null && ((flag == null) || !flag)){
            return handlerWrapper.nullPropertyValue(context, target, property);
        }
        LOGGER.trace("Entering nullPropertyValue [target={}, property={}]", target, property);

        //---------------------------------------------------------------
        if ((target == null) || property == null){
            return null;
        }
        //---------------------------------------------------------------

        try{
            String propName = property.toString();
            Class<?> clazz = null;

            PropertyDescriptor pd = OgnlRuntime.getPropertyDescriptor(target.getClass(), propName);
            if (pd == null){
                return null;
            }
            clazz = pd.getPropertyType();

            if (clazz == null){
                // can't do much here!
                return null;
            }

            //---------------------------------------------------------------

            //ignore those class in ignored package
            if (ignoreClass(clazz)){
                return null;
            }

            Object param = createObject(clazz);
            Ognl.setValue(propName, context, target, param);
            return param;
        }catch (Exception e){
            LOGGER.error("Could not create and/or set value back on to object", e);
        }
        return null;
    }

    //---------------------------------------------------------------

    private boolean ignoreClass(Class<?> clazz){
        if (clazz.isPrimitive() || clazz.isArray()){
            return true;
        }
        String clazzFullName = clazz.getName();
        for (String name : ignoreList){
            if (clazzFullName.startsWith(name)){
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 创建 object.
     *
     * @param clazz
     *            the clazz
     * @return the object
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws Exception
     *             the exception
     */
    private static Object createObject(Class<?> clazz) throws InstantiationException,IllegalAccessException{
        if (Collection.class.isAssignableFrom(clazz)){
            return new ArrayList<>();
        }else if (clazz == Map.class){
            return new HashMap<>();
        }
        return clazz.newInstance();
    }
}
