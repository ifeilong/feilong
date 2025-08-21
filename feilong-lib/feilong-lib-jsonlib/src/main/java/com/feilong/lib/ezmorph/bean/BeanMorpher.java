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

package com.feilong.lib.ezmorph.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.lib.beanutils.DynaProperty;
import com.feilong.lib.beanutils.PropertyUtils;
import com.feilong.lib.ezmorph.MorphException;
import com.feilong.lib.ezmorph.MorpherRegistry;
import com.feilong.lib.ezmorph.ObjectMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;

/**
 * Converts a JavaBean into another JavaBean or DynaBean.<br>
 * This Morpher will try to match every property from the target JavaBean's
 * class to the properties of the source JavaBean. If any target property
 * differs in type from the source property, it will try to morph it. If a
 * Morpher is not found for that type, the conversion will be aborted with a
 * MorphException; this may be changed by setting the Morpher to be lenient, in
 * that way it will ignore the property (the resulting value will be null).
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
@lombok.extern.slf4j.Slf4j
public final class BeanMorpher implements ObjectMorpher{

    /** The bean class. */
    private final Class<?>        beanClass;

    /** The morpher registry. */
    private final MorpherRegistry morpherRegistry;

    //---------------------------------------------------------------

    /**
     * Instantiates a new bean morpher.
     *
     * @param beanClass
     *            the target class to morph to
     * @param morpherRegistry
     *            a registry of morphers
     */
    public BeanMorpher(Class<?> beanClass, MorpherRegistry morpherRegistry){
        validateClass(beanClass);
        if (morpherRegistry == null){
            throw new MorphException("morpherRegistry is null");
        }
        this.beanClass = beanClass;
        this.morpherRegistry = morpherRegistry;
    }

    //---------------------------------------------------------------

    /**
     * Morph.
     *
     * @param sourceBean
     *            the source bean
     * @return the object
     */
    @Override
    public Object morph(Object sourceBean){
        if (sourceBean == null){
            return null;
        }
        if (!supports(sourceBean.getClass())){
            throw new MorphException("unsupported class: " + sourceBean.getClass().getName());
        }

        //---------------------------------------------------------------
        try{
            Object targetBean = beanClass.newInstance();

            PropertyDescriptor[] targetPropertyDescriptors = PropertyUtil.getPropertyDescriptors(beanClass);
            for (PropertyDescriptor targetPropertyDescriptor : targetPropertyDescriptors){
                String name = targetPropertyDescriptor.getName();
                if (targetPropertyDescriptor.getWriteMethod() == null){
                    log.info("Property '{}.{}' has no write method. SKIPPED.", beanClass.getName(), name);
                    continue;
                }

                //---------------------------------------------------------------
                Class<?> sourceType = null;
                if (sourceBean instanceof DynaBean){
                    DynaBean dynaBean = (DynaBean) sourceBean;
                    DynaProperty dynaProperty = dynaBean.getDynaClass().getDynaProperty(name);
                    if (dynaProperty == null){
                        log.warn("DynaProperty '{}' does not exist. SKIPPED.", name);
                        continue;
                    }
                    sourceType = dynaProperty.getType();
                }else{
                    PropertyDescriptor sourcePropertyDescriptor = PropertyUtils.getPropertyDescriptor(sourceBean, name);
                    if (sourcePropertyDescriptor == null){
                        log.warn("Property '{}.{}' does not exist. SKIPPED.", sourceBean.getClass().getName(), name);
                        continue;
                    }else if (sourcePropertyDescriptor.getReadMethod() == null){
                        log.warn("Property '{}.{}' has no read method. SKIPPED.", sourceBean.getClass().getName(), name);
                        continue;
                    }
                    sourceType = sourcePropertyDescriptor.getPropertyType();
                }

                //---------------------------------------------------------------

                Class<?> targetType = targetPropertyDescriptor.getPropertyType();
                Object value = PropertyUtils.getProperty(sourceBean, name);
                setProperty(targetBean, name, sourceType, targetType, value);
            }
            return targetBean;
        }catch (MorphException me){
            throw me;
        }catch (Exception e){
            throw new MorphException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class<?> morphsTo(){
        return beanClass;
    }

    /**
     * Supports.
     *
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    @Override
    public boolean supports(Class<?> clazz){
        return !clazz.isArray();
    }

    //---------------------------------------------------------------

    /**
     * 设置 property.
     *
     * @param targetBean
     *            the target bean
     * @param name
     *            the name
     * @param sourceType
     *            the source type
     * @param targetType
     *            the target type
     * @param value
     *            the value
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    private void setProperty(Object targetBean,String name,Class<?> sourceType,Class<?> targetType,Object value) throws Exception{
        if (targetType.isAssignableFrom(sourceType)){
            if (value == null && targetType.isPrimitive()){
                value = morpherRegistry.morph(targetType, value);
            }
            PropertyUtils.setProperty(targetBean, name, value);
            return;
        }

        //---------------------------------------------------------------
        if (targetType.equals(Object.class)){
            // no conversion
            PropertyUtils.setProperty(targetBean, name, value);
            return;
        }

        //---------------------------------------------------------------
        if (value == null){
            if (targetType.isPrimitive()){
                PropertyUtils.setProperty(targetBean, name, morpherRegistry.morph(targetType, value));
            }
            return;
        }

        if (IdentityObjectMorpher.INSTANCE == morpherRegistry.getMorpherFor(targetType)){
            throw new MorphException("Can't find a morpher for target class " + targetType.getName() + " (" + name + ")");
        }
        PropertyUtils.setProperty(targetBean, name, morpherRegistry.morph(targetType, value));
    }

    private static void validateClass(Class<?> clazz){
        if (clazz == null){
            throw new MorphException("target class is null");
        }
        if (clazz.isPrimitive()){
            throw new MorphException("target class is a primitive");
        }
        if (clazz.isArray()){
            throw new MorphException("target class is an array");
        }
        if (clazz.isInterface()){
            throw new MorphException("target class is an interface");
        }
        if (DynaBean.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a DynaBean");
        }
        if (Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a wrapper");
        }
        if (String.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a String");
        }
        if (Collection.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a Collection");
        }
        if (Map.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a Map");
        }
    }
}