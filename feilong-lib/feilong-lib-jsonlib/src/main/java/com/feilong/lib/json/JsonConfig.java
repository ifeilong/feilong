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

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.feilong.lib.json.processors.DefaultValueProcessor;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.JavaIdentifierTransformer;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * Utility class that helps configuring the serialization process.
 * 
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class JsonConfig{

    //see net.sf.json.JsonConfig#DEFAULT_EXCLUDES
    //默认会过滤的几个key "class", "declaringClass","metaClass"  
    private static final String[]                      DEFAULT_EXCLUDES             = toArray("class", "declaringClass", "metaClass");

    private static final int                           MODE_LIST                    = 1;

    public static final int                            MODE_OBJECT_ARRAY            = 2;

    private static final int                           MODE_SET                     = 2;

    //---------------------------------------------------------------

    /** Array conversion mode. */
    private int                                        arrayMode                    = MODE_LIST;

    /** The excludes. */
    private String[]                                   excludes                     = EMPTY_STRING_ARRAY;

    /** The default value map. */
    private final Map<Class<?>, DefaultValueProcessor> defaultValueMap              = new HashMap<>();

    /** The java identifier transformer. */
    private JavaIdentifierTransformer                  javaIdentifierTransformer    = JavaIdentifierTransformer.NOOP;

    /** The java property filter. */
    private PropertyFilter                             javaPropertyFilter;
    //---------------------------------------------------------------

    /** The collection type. */
    private Class<?>                                   collectionType               = List.class;

    //---------------------------------------------------------------

    /** The java property name processor map. */
    private final Map<Class<?>, PropertyNameProcessor> javaPropertyNameProcessorMap = new HashMap<>();

    /** The key map. */
    private final Map<String, JsonValueProcessor>      keyMap                       = new HashMap<>();

    /** The type map. */
    private final Map<Class<?>, JsonValueProcessor>    typeMap                      = new HashMap<>();

    //---------------------------------------------------------------

    /** The json property filter. */
    private PropertyFilter                             jsonPropertyFilter;

    /** The json property name processor map. */
    private final Map<Class<?>, PropertyNameProcessor> jsonPropertyNameProcessorMap = new HashMap<>();

    //---------------------------------------------------------------

    /** Root class used when converting to an specific bean. */
    private Class<?>                                   rootClass;

    /** Map of attribute/class. */
    private Map<String, Class<?>>                      classMap;

    //---------------------------------------------------------------

    /**
     * Finds a JsonValueProcessor.<br>
     * It will search the registered JsonValueProcessors in the following order:
     * <ol>
     * <li>key</li>
     * <li>type</li>
     * </ol>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param propertyType
     *            the type of the property
     * @param key
     *            the name of the property which may belong to the target class
     * @return the json value processor
     */
    public JsonValueProcessor findJsonValueProcessor(Class<?> propertyType,String key){
        JsonValueProcessor jsonValueProcessor = keyMap.get(key);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }
        return typeMap.get(propertyType);
    }

    //---------------------------------------------------------------

    /**
     * Returns the current array mode conversion.<br>
     * [JSON -&gt; Java]
     * 
     * @return MODE_OBJECT_ARRAY, MODE_LIST or MODE_SET
     */
    public int getArrayMode(){
        return arrayMode;
    }

    /**
     * Returns the current attribute/class Map.<br>
     * [JSON -&gt; Java]
     * 
     * @return a Map of classes, every key identifies a property or a regexp
     */
    public Map<String, Class<?>> getClassMap(){
        return classMap;
    }

    /**
     * Returns the current collection type used for collection transformations.<br>
     * [JSON -&gt; Java]
     * 
     * @return the target collection class for conversion
     */
    public Class<?> getCollectionType(){
        return collectionType;
    }

    /**
     * Returns the configured properties for exclusion. <br>
     * [Java -&gt; JSON]
     *
     * @return the excludes
     */
    public String[] getExcludes(){
        return excludes;
    }

    /**
     * Returns the configured JavaIdentifierTransformer. <br>
     * Default value is JavaIdentifierTransformer.NOOP<br>
     * [JSON -&gt; Java]
     *
     * @return the java identifier transformer
     */
    public JavaIdentifierTransformer getJavaIdentifierTransformer(){
        return javaIdentifierTransformer;
    }

    /**
     * Returns the configured property filter when serializing to Java.<br>
     * [JSON -&gt; Java]
     *
     * @return the java property filter
     */
    public PropertyFilter getJavaPropertyFilter(){
        return javaPropertyFilter;
    }

    /**
     * Returns the configured property filter when serializing to JSON.<br>
     * [Java -&gt; JSON]
     *
     * @return the json property filter
     */
    public PropertyFilter getJsonPropertyFilter(){
        return jsonPropertyFilter;
    }

    /**
     * Returns a set of default excludes with user-defined excludes.<br>
     * [Java -&gt; JSON]
     *
     * @return the merged excludes
     */
    public Collection<String> getMergedExcludes(){
        Collection<String> exclusions = new HashSet<>();
        for (int i = 0; i < excludes.length; i++){
            String exclusion = excludes[i];
            if (!StringUtils.isBlank(exclusion)){
                exclusions.add(exclusion.trim());
            }
        }

        for (int i = 0; i < DEFAULT_EXCLUDES.length; i++){
            if (!exclusions.contains(DEFAULT_EXCLUDES[i])){
                exclusions.add(DEFAULT_EXCLUDES[i]);
            }
        }
        return exclusions;
    }

    //---------------------------------------------------------------

    /**
     * Returns the current root Class.<br>
     * [JSON -&gt; Java]
     * 
     * @return the target class for conversion
     */
    public Class<?> getRootClass(){
        return rootClass;
    }

    /**
     * Registers a DefaultValueProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param target
     *            the class to use as key
     * @param defaultValueProcessor
     *            the processor to register
     */
    public void registerDefaultValueProcessor(Class<?> target,DefaultValueProcessor defaultValueProcessor){
        if (target != null && defaultValueProcessor != null){
            defaultValueMap.put(target, defaultValueProcessor);
        }
    }

    /**
     * Registers a PropertyNameProcessor.<br>
     * [JSON -&gt; Java]
     * 
     * @param target
     *            the class to use as key
     * @param propertyNameProcessor
     *            the processor to register
     */
    public void registerJavaPropertyNameProcessor(Class<?> target,PropertyNameProcessor propertyNameProcessor){
        if (target != null && propertyNameProcessor != null){
            javaPropertyNameProcessorMap.put(target, propertyNameProcessor);
        }
    }

    /**
     * Registers a PropertyNameProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param target
     *            the class to use as key
     * @param propertyNameProcessor
     *            the processor to register
     */
    public void registerJsonPropertyNameProcessor(Class<?> target,PropertyNameProcessor propertyNameProcessor){
        if (target != null && propertyNameProcessor != null){
            jsonPropertyNameProcessorMap.put(target, propertyNameProcessor);
        }
    }

    /**
     * Registers a JsonValueProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param propertyType
     *            the property type to use as key
     * @param jsonValueProcessor
     *            the processor to register
     */
    public void registerJsonValueProcessor(Class<?> propertyType,JsonValueProcessor jsonValueProcessor){
        if (propertyType != null && jsonValueProcessor != null){
            typeMap.put(propertyType, jsonValueProcessor);
        }
    }

    /**
     * Registers a JsonValueProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param key
     *            the property name to use as key
     * @param jsonValueProcessor
     *            the processor to register
     */
    public void registerJsonValueProcessor(String key,JsonValueProcessor jsonValueProcessor){
        if (key != null && jsonValueProcessor != null){
            keyMap.put(key, jsonValueProcessor);
        }
    }

    //---------------------------------------------------------------

    /**
     * Sets the current array mode for conversion.<br>
     * If the value is not MODE_LIST, MODE_OBJECT_ARRAY nor MODE_SET, then MODE_LIST will be used.<br>
     * [JSON -&gt; Java]
     * 
     * @param arrayMode
     *            array mode for conversion
     */
    public void setArrayMode(int arrayMode){
        if (arrayMode == MODE_OBJECT_ARRAY){
            this.arrayMode = arrayMode;
        }else if (arrayMode == MODE_SET){
            this.arrayMode = arrayMode;
            this.collectionType = Set.class;
        }else{
            this.arrayMode = MODE_LIST;
        }
    }

    /**
     * Sets the current attribute/Class Map<br>
     * [JSON -&gt; Java].
     *
     * @param classMap
     *            a Map of classes, every key identifies a property or a regexp
     */
    public void setClassMap(Map<String, Class<?>> classMap){
        this.classMap = classMap;
    }

    /**
     * Sets the current collection type used for collection transformations.<br>
     * [JSON -&gt; Java]
     * 
     * @param collectionType
     *            the target collection class for conversion
     */
    public void setCollectionType(Class<?> collectionType){
        if (collectionType != null){
            if (!Collection.class.isAssignableFrom(collectionType)){
                throw new JSONException("The configured collectionType is not a Collection: " + collectionType.getName());
            }
            this.collectionType = collectionType;
        }else{
            collectionType = List.class;
        }
    }

    /**
     * Sets the excludes to use.<br>
     * Will set default value ([]) if null.<br>
     * [Java -&gt; JSON]
     *
     * @param excludes
     *            the new excludes
     */
    public void setExcludes(String[] excludes){
        this.excludes = excludes == null ? EMPTY_STRING_ARRAY : excludes;
    }

    /**
     * Sets the JavaIdentifierTransformer to use.<br>
     * Will set default value (JavaIdentifierTransformer.NOOP) if null.<br>
     * [JSON -&gt; Java]
     *
     * @param javaIdentifierTransformer
     *            the new java identifier transformer
     */
    public void setJavaIdentifierTransformer(JavaIdentifierTransformer javaIdentifierTransformer){
        this.javaIdentifierTransformer = javaIdentifierTransformer == null ? JavaIdentifierTransformer.NOOP : javaIdentifierTransformer;
    }

    /**
     * Sets a property filter used when serializing to Java.<br>
     * [JSON -&gt; Java]
     * 
     * @param javaPropertyFilter
     *            the property filter
     */
    public void setJavaPropertyFilter(PropertyFilter javaPropertyFilter){
        this.javaPropertyFilter = javaPropertyFilter;
    }

    /**
     * Sets a property filter used when serializing to JSON.<br>
     * [Java -&gt; JSON]
     * 
     * @param jsonPropertyFilter
     *            the property filter
     */
    public void setJsonPropertyFilter(PropertyFilter jsonPropertyFilter){
        this.jsonPropertyFilter = jsonPropertyFilter;
    }

    /**
     * Sets the current root Class.<br>
     * [JSON -&gt; Java]
     * 
     * @param rootClass
     *            the target class for conversion
     */
    public void setRootClass(Class<?> rootClass){
        this.rootClass = rootClass;
    }

    //---------------------------------------------------------------

    /**
     * Copy.
     *
     * @return the json config
     */
    public JsonConfig copy(){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.classMap = new HashMap<>();
        if (classMap != null){
            jsonConfig.classMap.putAll(classMap);
        }
        if (excludes != null){
            jsonConfig.excludes = new String[excludes.length];
            System.arraycopy(excludes, 0, jsonConfig.excludes, 0, excludes.length);
        }
        jsonConfig.javaIdentifierTransformer = javaIdentifierTransformer;
        jsonConfig.keyMap.putAll(keyMap);
        jsonConfig.rootClass = rootClass;
        jsonConfig.typeMap.putAll(typeMap);
        jsonConfig.jsonPropertyFilter = jsonPropertyFilter;
        jsonConfig.javaPropertyFilter = javaPropertyFilter;
        jsonConfig.defaultValueMap.putAll(defaultValueMap);
        jsonConfig.collectionType = collectionType;
        jsonConfig.javaPropertyNameProcessorMap.putAll(javaPropertyNameProcessorMap);
        jsonConfig.jsonPropertyNameProcessorMap.putAll(jsonPropertyNameProcessorMap);
        return jsonConfig;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    /**
     * @return the javaPropertyNameProcessorMap
     */
    public Map<Class<?>, PropertyNameProcessor> getJavaPropertyNameProcessorMap(){
        return javaPropertyNameProcessorMap;
    }

    /**
     * @return the defaultValueMap
     */
    public Map<Class<?>, DefaultValueProcessor> getDefaultValueMap(){
        return defaultValueMap;
    }

    /**
     * @return the jsonPropertyNameProcessorMap
     */
    public Map<Class<?>, PropertyNameProcessor> getJsonPropertyNameProcessorMap(){
        return jsonPropertyNameProcessorMap;
    }

    /**
     * @return the keyMap
     */
    public Map<String, JsonValueProcessor> getKeyMap(){
        return keyMap;
    }

    /**
     * @return the typeMap
     */
    public Map<Class<?>, JsonValueProcessor> getTypeMap(){
        return typeMap;
    }

}