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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.feilong.lib.beanutils.DynaClass;
import com.feilong.lib.beanutils.DynaProperty;
import com.feilong.lib.ezmorph.MorphException;
import com.feilong.lib.ezmorph.MorphUtils;
import com.feilong.lib.ezmorph.MorpherRegistry;
import com.feilong.lib.lang3.builder.EqualsBuilder;
import com.feilong.lib.lang3.builder.HashCodeBuilder;
import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * The Class MorphDynaBean.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class MorphDynaBean implements DynaBean,Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -605547389232706344L;

    /** The dyna class. */
    private MorphDynaClass    dynaClass;

    /** The dyna values. */
    private final Map         dynaValues       = new HashMap<>();

    /** The morpher registry. */
    private MorpherRegistry   morpherRegistry;

    //---------------------------------------------------------------

    /**
     * Instantiates a new morph dyna bean.
     */
    public MorphDynaBean(){
        this(null);
    }

    /**
     * Instantiates a new morph dyna bean.
     *
     * @param morpherRegistry
     *            the morpher registry
     */
    public MorphDynaBean(MorpherRegistry morpherRegistry){
        setMorpherRegistry(morpherRegistry);
    }

    //---------------------------------------------------------------

    /**
     * Contains.
     *
     * @param name
     *            the name
     * @param key
     *            the key
     * @return true, if successful
     */
    @Override
    public boolean contains(String name,String key){
        DynaProperty dynaProperty = getDynaProperty(name);

        Class<?> type = dynaProperty.getType();
        if (!Map.class.isAssignableFrom(type)){
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }

        //---------------------------------------------------------------
        Object value = dynaValues.get(name);
        if (value == null){
            value = new HashMap<>();
            dynaValues.put(name, value);
        }
        return ((Map) value).containsKey(key);
    }

    /**
     * Equals.
     *
     * @param obj
     *            the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (!(obj instanceof MorphDynaBean)){
            return false;
        }
        //---------------------------------------------------------------
        MorphDynaBean other = (MorphDynaBean) obj;
        EqualsBuilder builder = new EqualsBuilder().append(this.dynaClass, other.dynaClass);
        DynaProperty[] props = dynaClass.getDynaProperties();
        for (DynaProperty prop : props){
            builder.append(dynaValues.get(prop.getName()), dynaValues.get(prop.getName()));
        }
        return builder.isEquals();
    }

    /**
     * 获得.
     *
     * @param name
     *            the name
     * @return the object
     */
    @Override
    public Object get(String name){
        Object value = dynaValues.get(name);

        if (value != null){
            return value;
        }

        Class<?> type = getDynaProperty(name).getType();
        if (!type.isPrimitive()){
            return value;
        }
        return morpherRegistry.morph(type, value);
    }

    /**
     * 获得.
     *
     * @param name
     *            the name
     * @param index
     *            the index
     * @return the object
     */
    @Override
    public Object get(String name,int index){
        DynaProperty dynaProperty = getDynaProperty(name);

        Class<?> type = dynaProperty.getType();
        if (!type.isArray() && !List.class.isAssignableFrom(type)){
            throw new MorphException("Non-Indexed property name: " + name + " index: " + index);
        }

        Object value = dynaValues.get(name);
        if (value.getClass().isArray()){
            return Array.get(value, index);
        }else if (value instanceof List){
            return ((List) value).get(index);
        }

        return value;
    }

    /**
     * 获得.
     *
     * @param name
     *            the name
     * @param key
     *            the key
     * @return the object
     */
    @Override
    public Object get(String name,String key){
        DynaProperty dynaProperty = getDynaProperty(name);

        Class<?> type = dynaProperty.getType();
        if (!Map.class.isAssignableFrom(type)){
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }

        Object value = dynaValues.get(name);
        if (value == null){
            value = new HashMap<>();
            dynaValues.put(name, value);
        }
        return ((Map) value).get(key);
    }

    /**
     * Gets the dyna class.
     *
     * @return the dyna class
     */
    @Override
    public DynaClass getDynaClass(){
        return this.dynaClass;
    }

    /**
     * Gets the morpher registry.
     *
     * @return the morpher registry
     */
    public MorpherRegistry getMorpherRegistry(){
        return morpherRegistry;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode(){
        HashCodeBuilder builder = new HashCodeBuilder().append(dynaClass);
        DynaProperty[] props = dynaClass.getDynaProperties();
        for (DynaProperty prop : props){
            builder.append(dynaValues.get(prop.getName()));
        }
        return builder.toHashCode();
    }

    /**
     * 删除.
     *
     * @param name
     *            the name
     * @param key
     *            the key
     */
    @Override
    public void remove(String name,String key){
        DynaProperty dynaProperty = getDynaProperty(name);

        Class<?> type = dynaProperty.getType();
        if (!Map.class.isAssignableFrom(type)){
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }

        Object value = dynaValues.get(name);
        if (value == null){
            value = new HashMap<>();
            dynaValues.put(name, value);
        }
        ((Map) value).remove(key);
    }

    /**
     * 设置.
     *
     * @param name
     *            the name
     * @param index
     *            the index
     * @param value
     *            the value
     */
    @Override
    public void set(String name,int index,Object value){
        DynaProperty dynaProperty = getDynaProperty(name);

        Class<?> type = dynaProperty.getType();
        if (!type.isArray() && !List.class.isAssignableFrom(type)){
            throw new MorphException("Non-Indexed property name: " + name + " index: " + index);
        }

        //---------------------------------------------------------------

        Object prop = dynaValues.get(name);
        if (prop == null){
            if (List.class.isAssignableFrom(type)){
                prop = new ArrayList<>();
            }else{
                prop = Array.newInstance(type.getComponentType(), index + 1);
            }
            dynaValues.put(name, prop);
        }

        if (prop.getClass().isArray()){
            if (index >= Array.getLength(prop)){
                Object tmp = Array.newInstance(type.getComponentType(), index + 1);
                System.arraycopy(prop, 0, tmp, 0, Array.getLength(prop));
                prop = tmp;
                dynaValues.put(name, tmp);
            }
            Array.set(prop, index, value);
        }else if (prop instanceof List){
            List l = (List) prop;
            if (index >= l.size()){
                for (int i = l.size(); i <= index + 1; i++){
                    l.add(null);
                }
            }
            ((List) prop).set(index, value);
        }
    }

    /**
     * 设置.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    @Override
    public void set(String name,Object value){
        DynaProperty property = getDynaProperty(name);
        if (value == null || !isDynaAssignable(property.getType(), value.getClass())){
            value = morpherRegistry.morph(property.getType(), value);
        }
        dynaValues.put(name, value);
    }

    /**
     * 设置.
     *
     * @param name
     *            the name
     * @param key
     *            the key
     * @param value
     *            the value
     */
    @Override
    public void set(String name,String key,Object value){
        DynaProperty dynaProperty = getDynaProperty(name);

        Class<?> type = dynaProperty.getType();
        if (!Map.class.isAssignableFrom(type)){
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }

        Object prop = dynaValues.get(name);
        if (prop == null){
            prop = new HashMap<>();
            dynaValues.put(name, prop);
        }
        ((Map) prop).put(key, value);
    }

    /**
     * Sets the dyna bean class.
     *
     * @param dynaClass
     *            the new dyna bean class
     */
    public synchronized void setDynaBeanClass(MorphDynaClass dynaClass){
        if (this.dynaClass == null){
            this.dynaClass = dynaClass;
        }
    }

    /**
     * Sets the morpher registry.
     *
     * @param morpherRegistry
     *            the new morpher registry
     */
    public void setMorpherRegistry(MorpherRegistry morpherRegistry){
        if (morpherRegistry == null){
            this.morpherRegistry = new MorpherRegistry();
            MorphUtils.registerStandardMorphers(this.morpherRegistry);
        }else{
            this.morpherRegistry = morpherRegistry;
        }
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(dynaValues).toString();
    }

    /**
     * Gets the dyna property.
     *
     * @param name
     *            the name
     * @return the dyna property
     */
    protected DynaProperty getDynaProperty(String name){
        DynaProperty property = getDynaClass().getDynaProperty(name);
        if (property == null){
            throw new MorphException("Unspecified property for " + name);
        }
        return property;
    }

    /**
     * Checks if is dyna assignable.
     *
     * @param dest
     *            the dest
     * @param src
     *            the src
     * @return true, if is dyna assignable
     */
    private static boolean isDynaAssignable(Class<?> dest,Class<?> src){
        if (dest.isAssignableFrom(src)){
            return true;
        }

        //---------------------------------------------------------------
        boolean assignable = dest == Boolean.TYPE && src == Boolean.class;
        assignable = (dest == Byte.TYPE && src == Byte.class) ? true : assignable;
        assignable = (dest == Character.TYPE && src == Character.class) ? true : assignable;
        assignable = (dest == Short.TYPE && src == Short.class) ? true : assignable;
        assignable = (dest == Integer.TYPE && src == Integer.class) ? true : assignable;
        assignable = (dest == Long.TYPE && src == Long.class) ? true : assignable;
        assignable = (dest == Float.TYPE && src == Float.class) ? true : assignable;
        assignable = (dest == Double.TYPE && src == Double.class) ? true : assignable;

        if (src == Double.TYPE || Double.class.isAssignableFrom(src)){
            assignable = (isByte(dest) || isShort(dest) || isInteger(dest) || isLong(dest) || isFloat(dest)) ? true : assignable;
        }
        if (src == Float.TYPE || Float.class.isAssignableFrom(src)){
            assignable = (isByte(dest) || isShort(dest) || isInteger(dest) || isLong(dest)) ? true : assignable;
        }
        if (src == Long.TYPE || Long.class.isAssignableFrom(src)){
            assignable = (isByte(dest) || isShort(dest) || isInteger(dest)) ? true : assignable;
        }
        if (src == Integer.TYPE || Integer.class.isAssignableFrom(src)){
            assignable = (isByte(dest) || isShort(dest)) ? true : assignable;
        }
        if (src == Short.TYPE || Short.class.isAssignableFrom(src)){
            assignable = (isByte(dest)) ? true : assignable;
        }

        return assignable;
    }

    //---------------------------------------------------------------

    /**
     * Checks if is byte.
     *
     * @param clazz
     *            the clazz
     * @return true, if is byte
     */
    private static boolean isByte(Class<?> clazz){
        return Byte.class.isAssignableFrom(clazz) || clazz == Byte.TYPE;
    }

    /**
     * Checks if is float.
     *
     * @param clazz
     *            the clazz
     * @return true, if is float
     */
    private static boolean isFloat(Class<?> clazz){
        return Float.class.isAssignableFrom(clazz) || clazz == Float.TYPE;
    }

    /**
     * Checks if is integer.
     *
     * @param clazz
     *            the clazz
     * @return true, if is integer
     */
    private static boolean isInteger(Class<?> clazz){
        return Integer.class.isAssignableFrom(clazz) || clazz == Integer.TYPE;
    }

    /**
     * Checks if is long.
     *
     * @param clazz
     *            the clazz
     * @return true, if is long
     */
    private static boolean isLong(Class<?> clazz){
        return Long.class.isAssignableFrom(clazz) || clazz == Long.TYPE;
    }

    /**
     * Checks if is short.
     *
     * @param clazz
     *            the clazz
     * @return true, if is short
     */
    private static boolean isShort(Class<?> clazz){
        return Short.class.isAssignableFrom(clazz) || clazz == Short.TYPE;
    }
}