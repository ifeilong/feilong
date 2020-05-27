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
package com.feilong.xml.xstream;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * XStream相关配置.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.5.0 2015年10月29日 下午4:48:56
 * @since 1.5.0
 */
public final class XStreamConfig{

    /**
     * 是否格式化输出.
     * 
     * 默认 是true,使用 {@link PrettyPrintWriter},如果设置成false ,将使用 {@link CompactWriter}
     * 
     * @see PrettyPrintWriter
     * @see CompactWriter
     */
    private boolean                   isPrettyPrint            = true;

    /**
     * 哪个类的注解需要被激活.
     * 
     * @see com.thoughtworks.xstream.XStream#processAnnotations(Class[])
     */
    private Class<?>[]                processAnnotationsTypes;

    /**
     * Provides implementation of stream parsers and writers to XStream.
     * <p>
     * 默认是 {@link XppDriver}
     * </p>
     * 
     * @see com.thoughtworks.xstream.XStream#XStream(ReflectionProvider)
     * @since 1.10.7
     */
    private HierarchicalStreamDriver  hierarchicalStreamDriver;

    /**
     * 转换器.
     * 
     * @since 1.10.7
     */
    private List<? extends Converter> converterList;

    //---------------------------------------------------------------

    /**
     * 别名.
     * 
     * @see com.thoughtworks.xstream.XStream#alias(String, Class)
     */
    private Map<String, Class<?>>     aliasMap                 = newHashMap();

    /** 隐式集合 隐藏,隐藏,比如下面有list,泛型中的第二个参数 Class 是 ownerType. */
    private Map<String, Class<?>>     implicitCollectionMap    = newHashMap();

    /**
     * Associate a default implementation of a class with an object.
     * 
     * <p>
     * Whenever XStream encounters an instance of this type, it will use the default implementation instead.<br>
     * For example, java.util.ArrayList is the default implementation of java.util.List.
     * </p>
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
    {@code 
    <map class="linked-hash-map">
        <entry key="key1" value="value1"/>
        <entry key="key2" value="value2"/>
     </map>}
     * </pre>
     * 
     * 在XStream中，如果定义的字段是一个父类或接口，在序列化是会默认加入class属性以确定反序列化时用的类，<br>
     * 为了去掉这个class属性，可以定义默认的实现类来解决（虽然感觉这种解决方案不太好，但是目前还没有找到更好的解决方案）:
     * 
     * <pre class="code">
     * xstream.addDefaultImplementation(LinkedHashMap.class, Map.class);
     * </pre>
     * 
     * </blockquote>
     *
     * @see com.thoughtworks.xstream.mapper.ClassAliasingMapper#serializedClass(Class)
     * @see com.thoughtworks.xstream.XStream#addDefaultImplementation(Class, Class)
     * @see com.thoughtworks.xstream.mapper.DefaultImplementationsMapper#serializedClass(Class)
     * @since 1.10.7
     */
    private Map<Class<?>, Class<?>>   defaultImplementationMap = newHashMap();

    //---------------------------------------------------------------

    /**
     * Instantiates a new x stream config.
     */
    public XStreamConfig(){
        super();
    }

    /**
     * Instantiates a new x stream config.
     *
     * @param aliasMap
     *            别名,see {@link com.thoughtworks.xstream.XStream#alias(String, Class)}
     */
    public XStreamConfig(Map<String, Class<?>> aliasMap){
        this.aliasMap = aliasMap;
    }

    /**
     * Instantiates a new x stream config.
     *
     * @param aliasName
     *            the alias name
     * @param type
     *            the type
     */
    public XStreamConfig(String aliasName, Class<?> type){
        this.aliasMap.put(aliasName, type);
    }

    /**
     * Instantiates a new x stream config.
     *
     * @param processAnnotationsTypes
     *            哪个类的注解需要被激活.<br>
     *            see {@link com.thoughtworks.xstream.XStream#processAnnotations(Class[])}
     */
    public XStreamConfig(Class<?>...processAnnotationsTypes){
        this.processAnnotationsTypes = processAnnotationsTypes;
    }

    //---------------------------------------------------------------
    /**
     * 获得 别名.
     *
     * @return the aliasMap
     * @see com.thoughtworks.xstream.XStream#alias(String, Class)
     */
    public Map<String, Class<?>> getAliasMap(){
        return aliasMap;
    }

    /**
     * 设置 别名.
     *
     * @param aliasMap
     *            the aliasMap to set
     * @see com.thoughtworks.xstream.XStream#alias(String, Class)
     */
    public void setAliasMap(Map<String, Class<?>> aliasMap){
        this.aliasMap = aliasMap;
    }

    /**
     * 获得 隐式集合 隐藏,隐藏,比如下面有list,泛型中的第二个参数 Class 是 ownerType.
     *
     * @return the implicitCollectionMap
     */
    public Map<String, Class<?>> getImplicitCollectionMap(){
        return implicitCollectionMap;
    }

    /**
     * 设置 隐式集合 隐藏,隐藏,比如下面有list,泛型中的第二个参数 Class 是 ownerType.
     *
     * @param implicitCollectionMap
     *            the implicitCollectionMap to set
     */
    public void setImplicitCollectionMap(Map<String, Class<?>> implicitCollectionMap){
        this.implicitCollectionMap = implicitCollectionMap;
    }

    /**
     * Provides implementation of stream parsers and writers to XStream.
     * <p>
     * 默认是 {@link XppDriver}
     * </p>
     *
     * @return the provides implementation of stream parsers and writers to XStream
     * @see com.thoughtworks.xstream.XStream#XStream(ReflectionProvider)
     * @since 1.10.7
     */
    public HierarchicalStreamDriver getHierarchicalStreamDriver(){
        return hierarchicalStreamDriver;
    }

    /**
     * Provides implementation of stream parsers and writers to XStream.
     * <p>
     * 默认是 {@link XppDriver}
     * </p>
     *
     * @param hierarchicalStreamDriver
     *            the new provides implementation of stream parsers and writers to XStream
     * @see com.thoughtworks.xstream.XStream#XStream(ReflectionProvider)
     * @since 1.10.7
     */
    public void setHierarchicalStreamDriver(HierarchicalStreamDriver hierarchicalStreamDriver){
        this.hierarchicalStreamDriver = hierarchicalStreamDriver;
    }

    /**
     * 获得 转换器.
     *
     * @return the converterList
     * @since 1.10.7
     */
    public List<? extends Converter> getConverterList(){
        return converterList;
    }

    /**
     * 设置 转换器.
     *
     * @param converterList
     *            the converterList to set
     * @since 1.10.7
     */
    public void setConverterList(List<? extends Converter> converterList){
        this.converterList = converterList;
    }

    /**
     * 哪个类的注解需要被激活.
     *
     * @return 哪个类的注解需要被激活
     * @see com.thoughtworks.xstream.XStream#processAnnotations(Class[])
     */
    public Class<?>[] getProcessAnnotationsTypes(){
        return processAnnotationsTypes;
    }

    /**
     * 哪个类的注解需要被激活.
     *
     * @param processAnnotationsTypes
     *            哪个类的注解需要被激活
     * @see com.thoughtworks.xstream.XStream#processAnnotations(Class[])
     */
    public void setProcessAnnotationsTypes(Class<?>[] processAnnotationsTypes){
        this.processAnnotationsTypes = processAnnotationsTypes;
    }

    /**
     * 是否格式化输出.
     * 
     * 默认 是true,使用 {@link PrettyPrintWriter},如果设置成false ,将使用 {@link CompactWriter}
     *
     * @return the isPrettyPrint
     * @see PrettyPrintWriter
     * @see CompactWriter
     */
    public boolean getIsPrettyPrint(){
        return isPrettyPrint;
    }

    /**
     * 是否格式化输出.
     * 
     * 默认 是true,使用 {@link PrettyPrintWriter},如果设置成false ,将使用 {@link CompactWriter}
     *
     * @param isPrettyPrint
     *            the isPrettyPrint to set
     * @see PrettyPrintWriter
     * @see CompactWriter
     */
    public void setIsPrettyPrint(boolean isPrettyPrint){
        this.isPrettyPrint = isPrettyPrint;
    }

    /**
     * Associate a default implementation of a class with an object.
     * 
     * <p>
     * Whenever XStream encounters an instance of this type, it will use the default implementation instead.<br>
     * For example, java.util.ArrayList is the default implementation of java.util.List.
     * </p>
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
    {@code 
    <map class="linked-hash-map">
        <entry key="key1" value="value1"/>
        <entry key="key2" value="value2"/>
     </map>}
     * </pre>
     * 
     * 在XStream中，如果定义的字段是一个父类或接口，在序列化是会默认加入class属性以确定反序列化时用的类，<br>
     * 为了去掉这个class属性，可以定义默认的实现类来解决（虽然感觉这种解决方案不太好，但是目前还没有找到更好的解决方案）:
     * 
     * <pre class="code">
     * xstream.addDefaultImplementation(LinkedHashMap.class, Map.class);
     * </pre>
     * 
     * </blockquote>
     *
     * @return the defaultImplementationMap
     * @see com.thoughtworks.xstream.mapper.ClassAliasingMapper#serializedClass(Class)
     * @see com.thoughtworks.xstream.XStream#addDefaultImplementation(Class, Class)
     * @see com.thoughtworks.xstream.mapper.DefaultImplementationsMapper#serializedClass(Class)
     * @since 1.10.7
     */
    public Map<Class<?>, Class<?>> getDefaultImplementationMap(){
        return defaultImplementationMap;
    }

    /**
     * Associate a default implementation of a class with an object.
     * 
     * <p>
     * Whenever XStream encounters an instance of this type, it will use the default implementation instead.<br>
     * For example, java.util.ArrayList is the default implementation of java.util.List.
     * </p>
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
    {@code 
    <map class="linked-hash-map">
        <entry key="key1" value="value1"/>
        <entry key="key2" value="value2"/>
     </map>}
     * </pre>
     * 
     * 在XStream中，如果定义的字段是一个父类或接口，在序列化是会默认加入class属性以确定反序列化时用的类，<br>
     * 为了去掉这个class属性，可以定义默认的实现类来解决（虽然感觉这种解决方案不太好，但是目前还没有找到更好的解决方案）:
     * 
     * <pre class="code">
     * xstream.addDefaultImplementation(LinkedHashMap.class, Map.class);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param defaultImplementationMap
     *            the defaultImplementationMap to set
     * @see com.thoughtworks.xstream.mapper.ClassAliasingMapper#serializedClass(Class)
     * @see com.thoughtworks.xstream.XStream#addDefaultImplementation(Class, Class)
     * @see com.thoughtworks.xstream.mapper.DefaultImplementationsMapper#serializedClass(Class)
     * @since 1.10.7
     */
    public void setDefaultImplementationMap(Map<Class<?>, Class<?>> defaultImplementationMap){
        this.defaultImplementationMap = defaultImplementationMap;
    }

}