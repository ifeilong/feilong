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

package com.feilong.json.lib.ezmorph.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.lib.ezmorph.MorphException;
import com.feilong.json.lib.ezmorph.MorpherRegistry;
import com.feilong.json.lib.ezmorph.ObjectMorpher;
import com.feilong.json.lib.ezmorph.object.IdentityObjectMorpher;

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
public final class BeanMorpher implements ObjectMorpher{

    /** The Constant log. */
    private static final Logger   LOGGER = LoggerFactory.getLogger(BeanMorpher.class);

    //---------------------------------------------------------------

    /** The bean class. */
    private final Class           beanClass;

    /** The lenient. */
    private final boolean         lenient;

    /** The morpher registry. */
    private final MorpherRegistry morpherRegistry;

    /**
     * Instantiates a new bean morpher.
     *
     * @param beanClass
     *            the target class to morph to
     * @param morpherRegistry
     *            a registry of morphers
     */
    public BeanMorpher(Class beanClass, MorpherRegistry morpherRegistry){
        this(beanClass, morpherRegistry, false);
    }

    /**
     * Instantiates a new bean morpher.
     *
     * @param beanClass
     *            the target class to morph to
     * @param morpherRegistry
     *            a registry of morphers
     * @param lenient
     *            if an exception should be raised if no morpher is found for
     *            a target property
     */
    public BeanMorpher(Class beanClass, MorpherRegistry morpherRegistry, boolean lenient){
        validateClass(beanClass);
        if (morpherRegistry == null){
            throw new MorphException("morpherRegistry is null");
        }
        this.beanClass = beanClass;
        this.morpherRegistry = morpherRegistry;
        this.lenient = lenient;
    }

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

        Object targetBean = null;

        try{
            targetBean = beanClass.newInstance();
            PropertyDescriptor[] targetPds = PropertyUtils.getPropertyDescriptors(beanClass);
            for (int i = 0; i < targetPds.length; i++){
                PropertyDescriptor targetPd = targetPds[i];
                String name = targetPd.getName();
                if (targetPd.getWriteMethod() == null){
                    LOGGER.info("Property '" + beanClass.getName() + "." + name + "' has no write method. SKIPPED.");
                    continue;
                }

                Class sourceType = null;
                if (sourceBean instanceof DynaBean){
                    DynaBean dynaBean = (DynaBean) sourceBean;
                    DynaProperty dynaProperty = dynaBean.getDynaClass().getDynaProperty(name);
                    if (dynaProperty == null){
                        LOGGER.warn("DynaProperty '" + name + "' does not exist. SKIPPED.");
                        continue;
                    }
                    sourceType = dynaProperty.getType();
                }else{
                    PropertyDescriptor sourcePd = PropertyUtils.getPropertyDescriptor(sourceBean, name);
                    if (sourcePd == null){
                        LOGGER.warn("Property '" + sourceBean.getClass().getName() + "." + name + "' does not exist. SKIPPED.");
                        continue;
                    }else if (sourcePd.getReadMethod() == null){
                        LOGGER.warn("Property '" + sourceBean.getClass().getName() + "." + name + "' has no read method. SKIPPED.");
                        continue;
                    }
                    sourceType = sourcePd.getPropertyType();
                }

                Class targetType = targetPd.getPropertyType();
                Object value = PropertyUtils.getProperty(sourceBean, name);
                setProperty(targetBean, name, sourceType, targetType, value);
            }
        }catch (MorphException me){
            throw me;
        }catch (Exception e){
            throw new MorphException(e);
        }

        return targetBean;
    }

    /**
     * Morphs to.
     *
     * @return the class
     */
    @Override
    public Class morphsTo(){
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
    public boolean supports(Class clazz){
        return !clazz.isArray();
    }

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
    private void setProperty(Object targetBean,String name,Class sourceType,Class targetType,Object value)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        if (targetType.isAssignableFrom(sourceType)){
            if (value == null && targetType.isPrimitive()){
                value = morpherRegistry.morph(targetType, value);
            }
            PropertyUtils.setProperty(targetBean, name, value);
        }else{
            if (targetType.equals(Object.class)){
                // no conversion
                PropertyUtils.setProperty(targetBean, name, value);
            }else{
                if (value == null){
                    if (targetType.isPrimitive()){
                        PropertyUtils.setProperty(targetBean, name, morpherRegistry.morph(targetType, value));
                    }
                }else{
                    if (IdentityObjectMorpher.getInstance() == morpherRegistry.getMorpherFor(targetType)){
                        if (!lenient){
                            throw new MorphException("Can't find a morpher for target class " + targetType.getName() + " (" + name + ")");
                        }else{
                            LOGGER.info("Can't find a morpher for target class " + targetType.getName() + " (" + name + ") SKIPPED");
                        }
                    }else{
                        PropertyUtils.setProperty(targetBean, name, morpherRegistry.morph(targetType, value));
                    }
                }
            }
        }
    }

    /**
     * Validate class.
     *
     * @param clazz
     *            the clazz
     */
    private void validateClass(Class clazz){
        if (clazz == null){
            throw new MorphException("target class is null");
        }else if (clazz.isPrimitive()){
            throw new MorphException("target class is a primitive");
        }else if (clazz.isArray()){
            throw new MorphException("target class is an array");
        }else if (clazz.isInterface()){
            throw new MorphException("target class is an interface");
        }else if (DynaBean.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a DynaBean");
        }else if (Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a wrapper");
        }else if (String.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a String");
        }else if (Collection.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a Collection");
        }else if (Map.class.isAssignableFrom(clazz)){
            throw new MorphException("target class is a Map");
        }
    }
}