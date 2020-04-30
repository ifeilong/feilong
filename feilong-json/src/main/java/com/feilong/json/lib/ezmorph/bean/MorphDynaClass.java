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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.feilong.json.lib.ezmorph.MorphException;
import com.feilong.json.lib.ezmorph.MorphUtils;
import com.feilong.json.lib.ezmorph.MorpherRegistry;

/**
 * The Class MorphDynaClass.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public final class MorphDynaClass implements DynaClass,Serializable{

    /** The Constant dynaPropertyComparator. */
    private static final Comparator dynaPropertyComparator = new Comparator(){

                                                               @Override
                                                               public int compare(Object a,Object b){
                                                                   if (a instanceof DynaProperty && b instanceof DynaProperty){
                                                                       DynaProperty p1 = (DynaProperty) a;
                                                                       DynaProperty p2 = (DynaProperty) b;
                                                                       return p1.getName().compareTo(p2.getName());
                                                                   }
                                                                   return -1;
                                                               }
                                                           };

    /** The Constant serialVersionUID. */
    private static final long       serialVersionUID       = -613214016860871560L;

    /** The attributes. */
    private final Map               attributes;

    /** The bean class. */
    private Class                   beanClass;

    /** The dyna properties. */
    private DynaProperty[]          dynaProperties;

    /** The name. */
    private final String            name;

    /** The properties. */
    private final Map               properties             = new HashMap();

    /** The type. */
    private final Class             type;

    //---------------------------------------------------------------

    /**
     * Instantiates a new morph dyna class.
     *
     * @param attributes
     *            the attributes
     */
    public MorphDynaClass(Map attributes){
        this(null, null, attributes);
    }

    /**
     * Instantiates a new morph dyna class.
     *
     * @param attributes
     *            the attributes
     * @param exceptionOnEmptyAttributes
     *            the exception on empty attributes
     */
    public MorphDynaClass(Map attributes, boolean exceptionOnEmptyAttributes){
        this(null, null, attributes, exceptionOnEmptyAttributes);
    }

    /**
     * Instantiates a new morph dyna class.
     *
     * @param name
     *            the name
     * @param type
     *            the type
     * @param attributes
     *            the attributes
     */
    public MorphDynaClass(String name, Class type, Map attributes){
        this(name, type, attributes, false);
    }

    /**
     * Instantiates a new morph dyna class.
     *
     * @param name
     *            the name
     * @param type
     *            the type
     * @param attributes
     *            the attributes
     * @param exceptionOnEmptyAttributes
     *            the exception on empty attributes
     */
    public MorphDynaClass(String name, Class type, Map attributes, boolean exceptionOnEmptyAttributes){
        if (name == null){
            name = "MorphDynaClass";
        }
        if (type == null){
            type = MorphDynaBean.class;
        }
        if (!MorphDynaBean.class.isAssignableFrom(type)){
            throw new MorphException("MorphDynaBean is not assignable from " + type.getName());
        }
        if (attributes == null || attributes.isEmpty()){
            if (exceptionOnEmptyAttributes){
                throw new MorphException("Attributes map is null or empty.");
            }
            attributes = new HashMap();
        }
        this.name = name;
        this.type = type;
        this.attributes = attributes;
        process();
    }

    //---------------------------------------------------------------
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

        if (!(obj instanceof MorphDynaClass)){
            return false;
        }

        MorphDynaClass other = (MorphDynaClass) obj;
        EqualsBuilder builder = new EqualsBuilder().append(this.name, other.name).append(this.type, other.type);
        if (dynaProperties.length != other.dynaProperties.length){
            return false;
        }
        for (int i = 0; i < dynaProperties.length; i++){
            DynaProperty a = this.dynaProperties[i];
            DynaProperty b = other.dynaProperties[i];
            builder.append(a.getName(), b.getName());
            builder.append(a.getType(), b.getType());
        }
        return builder.isEquals();
    }

    /**
     * Gets the dyna properties.
     *
     * @return the dyna properties
     */
    @Override
    public DynaProperty[] getDynaProperties(){
        return dynaProperties;
    }

    /**
     * Gets the dyna property.
     *
     * @param propertyName
     *            the property name
     * @return the dyna property
     */
    @Override
    public DynaProperty getDynaProperty(String propertyName){
        if (propertyName == null){
            throw new MorphException("Unnespecified bean property name");

        }
        return (DynaProperty) properties.get(propertyName);
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName(){
        return this.name;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode(){
        HashCodeBuilder builder = new HashCodeBuilder().append(name).append(type);
        for (int i = 0; i < dynaProperties.length; i++){
            builder.append(this.dynaProperties[i].getName());
            builder.append(this.dynaProperties[i].getType());
        }
        return builder.toHashCode();
    }

    /**
     * New instance.
     *
     * @return the dyna bean
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InstantiationException
     *             the instantiation exception
     */
    @Override
    public DynaBean newInstance() throws IllegalAccessException,InstantiationException{
        return newInstance(null);
    }

    /**
     * New instance.
     *
     * @param morpherRegistry
     *            the morpher registry
     * @return the dyna bean
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InstantiationException
     *             the instantiation exception
     */
    public DynaBean newInstance(MorpherRegistry morpherRegistry) throws IllegalAccessException,InstantiationException{
        if (morpherRegistry == null){
            morpherRegistry = new MorpherRegistry();
            MorphUtils.registerStandardMorphers(morpherRegistry);
        }
        MorphDynaBean dynaBean = (MorphDynaBean) getBeanClass().newInstance();
        dynaBean.setDynaBeanClass(this);
        dynaBean.setMorpherRegistry(morpherRegistry);
        Iterator keys = attributes.keySet().iterator();
        while (keys.hasNext()){
            String key = (String) keys.next();
            dynaBean.set(key, null);
        }
        return dynaBean;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString(){
        return new ToStringBuilder(this).append("name", this.name).append("type", this.type).append("attributes", this.attributes)
                        .toString();
    }

    /**
     * Gets the bean class.
     *
     * @return the bean class
     */
    protected Class getBeanClass(){
        if (this.beanClass == null){
            process();
        }
        return this.beanClass;
    }

    /**
     * Process.
     */
    private void process(){
        this.beanClass = this.type;

        try{
            Iterator entries = attributes.entrySet().iterator();
            dynaProperties = new DynaProperty[attributes.size()];
            int i = 0;
            while (entries.hasNext()){
                Map.Entry entry = (Map.Entry) entries.next();
                String pname = (String) entry.getKey();
                Object pclass = entry.getValue();
                DynaProperty dynaProperty = null;
                if (pclass instanceof String){
                    Class klass = Class.forName((String) pclass);
                    if (klass.isArray() && klass.getComponentType().isArray()){
                        throw new MorphException("Multidimensional arrays are not supported");
                    }
                    dynaProperty = new DynaProperty(pname, klass);
                }else if (pclass instanceof Class){
                    Class klass = (Class) pclass;
                    if (klass.isArray() && klass.getComponentType().isArray()){
                        throw new MorphException("Multidimensional arrays are not supported");
                    }
                    dynaProperty = new DynaProperty(pname, klass);
                }else{
                    throw new MorphException("Type must be String or Class");
                }
                properties.put(dynaProperty.getName(), dynaProperty);
                dynaProperties[i++] = dynaProperty;
            }
        }catch (ClassNotFoundException cnfe){
            throw new MorphException(cnfe);
        }

        // keep properties sorted by name
        Arrays.sort(dynaProperties, 0, dynaProperties.length, dynaPropertyComparator);
    }
}