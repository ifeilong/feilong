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
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import com.feilong.lib.json.processors.DefaultDefaultValueProcessor;
import com.feilong.lib.json.processors.DefaultValueProcessor;
import com.feilong.lib.json.processors.DefaultValueProcessorMatcher;
import com.feilong.lib.json.processors.JsonBeanProcessor;
import com.feilong.lib.json.processors.JsonBeanProcessorMatcher;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonValueProcessorMatcher;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.processors.PropertyNameProcessorMatcher;
import com.feilong.lib.json.util.CycleDetectionStrategy;
import com.feilong.lib.json.util.JavaIdentifierTransformer;
import com.feilong.lib.json.util.PropertyFilter;

/**
 * Utility class that helps configuring the serialization process.
 * 
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class JsonConfig{

    /** The Constant DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER. */
    public static final DefaultValueProcessorMatcher DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER = DefaultValueProcessorMatcher.DEFAULT;

    /** The Constant DEFAULT_JSON_BEAN_PROCESSOR_MATCHER. */
    public static final JsonBeanProcessorMatcher     DEFAULT_JSON_BEAN_PROCESSOR_MATCHER     = JsonBeanProcessorMatcher.DEFAULT;

    /** The Constant DEFAULT_JSON_VALUE_PROCESSOR_MATCHER. */
    public static final JsonValueProcessorMatcher    DEFAULT_JSON_VALUE_PROCESSOR_MATCHER    = JsonValueProcessorMatcher.DEFAULT;

    /** The Constant DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER. */
    public static final PropertyNameProcessorMatcher DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER = PropertyNameProcessorMatcher.DEFAULT;

    //---------------------------------------------------------------

    /** The Constant MODE_LIST. */
    public static final int                          MODE_LIST                               = 1;

    /** The Constant MODE_OBJECT_ARRAY. */
    public static final int                          MODE_OBJECT_ARRAY                       = 2;

    /** The Constant MODE_SET. */
    public static final int                          MODE_SET                                = 2;

    //---------------------------------------------------------------

    /** The Constant DEFAULT_COLLECTION_TYPE. */
    private static final Class                       DEFAULT_COLLECTION_TYPE                 = List.class;

    /** The Constant DEFAULT_CYCLE_DETECTION_STRATEGY 排除,避免循环引用 There is a cycle in the hierarchy! Returns empty array and null object. */
    private static final CycleDetectionStrategy      DEFAULT_CYCLE_DETECTION_STRATEGY        = CycleDetectionStrategy.LENIENT;

    /** The Constant DEFAULT_EXCLUDES. */
    private static final String[]                    DEFAULT_EXCLUDES                        = toArray(
                    "class",
                    "declaringClass",
                    "metaClass");

    /** The Constant DEFAULT_JAVA_IDENTIFIER_TRANSFORMER. */
    private static final JavaIdentifierTransformer   DEFAULT_JAVA_IDENTIFIER_TRANSFORMER     = JavaIdentifierTransformer.NOOP;

    /** The Constant DEFAULT_VALUE_PROCESSOR. */
    private static final DefaultValueProcessor       DEFAULT_VALUE_PROCESSOR                 = new DefaultDefaultValueProcessor();

    /** The Constant EMPTY_EXCLUDES. */
    private static final String[]                    EMPTY_EXCLUDES                          = new String[0];

    //---------------------------------------------------------------

    /** Array conversion mode. */
    private int                                      arrayMode                               = MODE_LIST;

    /** The bean key map. */
    private final MultiKeyMap                        beanKeyMap                              = new MultiKeyMap();

    /** The bean processor map. */
    private final Map                                beanProcessorMap                        = new HashMap();

    /** The bean type map. */
    private final MultiKeyMap                        beanTypeMap                             = new MultiKeyMap();

    //---------------------------------------------------------------

    /** Map of attribute/class. */
    private Map                                      classMap;

    /** The collection type. */
    private Class                                    collectionType                          = DEFAULT_COLLECTION_TYPE;

    /** The cycle detection strategy. */
    private CycleDetectionStrategy                   cycleDetectionStrategy                  = DEFAULT_CYCLE_DETECTION_STRATEGY;

    /** The default value map. */
    private final Map                                defaultValueMap                         = new HashMap();

    /** The default value processor matcher. */
    private DefaultValueProcessorMatcher             defaultValueProcessorMatcher            = DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER;

    /** The enclosed type. */
    private Class                                    enclosedType;

    /** The excludes. */
    private String[]                                 excludes                                = EMPTY_EXCLUDES;

    /** The exclusion map. */
    private final Map                                exclusionMap                            = new HashMap();

    //---------------------------------------------------------------

    /** The ignore default excludes. */
    private boolean                                  ignoreDefaultExcludes;

    /** The ignore transient fields. */
    private boolean                                  ignoreTransientFields;

    //---------------------------------------------------------------

    /** The java identifier transformer. */
    private JavaIdentifierTransformer                javaIdentifierTransformer               = DEFAULT_JAVA_IDENTIFIER_TRANSFORMER;

    /** The java property filter. */
    private PropertyFilter                           javaPropertyFilter;

    /** The java property name processor map. */
    private final Map                                javaPropertyNameProcessorMap            = new HashMap();

    /** The java property name processor matcher. */
    private PropertyNameProcessorMatcher             javaPropertyNameProcessorMatcher        = DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;

    /** The json bean processor matcher. */
    private JsonBeanProcessorMatcher                 jsonBeanProcessorMatcher                = DEFAULT_JSON_BEAN_PROCESSOR_MATCHER;

    /** The json property filter. */
    private PropertyFilter                           jsonPropertyFilter;

    /** The json property name processor map. */
    private final Map                                jsonPropertyNameProcessorMap            = new HashMap();

    /** The json property name processor matcher. */
    private PropertyNameProcessorMatcher             jsonPropertyNameProcessorMatcher        = DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;

    /** The json value processor matcher. */
    private JsonValueProcessorMatcher                jsonValueProcessorMatcher               = DEFAULT_JSON_VALUE_PROCESSOR_MATCHER;

    /** The key map. */
    private final Map                                keyMap                                  = new HashMap();

    /** Root class used when converting to an specific bean. */
    private Class                                    rootClass;

    //---------------------------------------------------------------

    /** The type map. */
    private final Map                                typeMap                                 = new HashMap();

    /** The allow non string keys. */
    private boolean                                  allowNonStringKeys                      = false;

    //---------------------------------------------------------------

    /**
     * Instantiates a new json config.
     */
    public JsonConfig(){
    }

    //---------------------------------------------------------------

    /**
     * Finds a DefaultValueProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param target
     *            a class used for searching a DefaultValueProcessor.
     * @return the default value processor
     */
    public DefaultValueProcessor findDefaultValueProcessor(Class target){
        if (!defaultValueMap.isEmpty()){
            Object key = defaultValueProcessorMatcher.getMatch(target, defaultValueMap.keySet());
            DefaultValueProcessor processor = (DefaultValueProcessor) defaultValueMap.get(key);
            if (processor != null){
                return processor;
            }
        }
        return DEFAULT_VALUE_PROCESSOR;
    }

    /**
     * Finds a PropertyNameProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [JSON -&gt; Java]
     *
     * @param beanClass
     *            the bean class
     * @return the property name processor
     */
    public PropertyNameProcessor findJavaPropertyNameProcessor(Class beanClass){
        if (!javaPropertyNameProcessorMap.isEmpty()){
            Object key = javaPropertyNameProcessorMatcher.getMatch(beanClass, javaPropertyNameProcessorMap.keySet());
            return (PropertyNameProcessor) javaPropertyNameProcessorMap.get(key);

        }
        return null;
    }

    /**
     * Finds a JsonBeanProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param target
     *            a class used for searching a JsonBeanProcessor.
     * @return the json bean processor
     */
    public JsonBeanProcessor findJsonBeanProcessor(Class target){
        if (!beanProcessorMap.isEmpty()){
            Object key = jsonBeanProcessorMatcher.getMatch(target, beanProcessorMap.keySet());
            return (JsonBeanProcessor) beanProcessorMap.get(key);
        }
        return null;
    }

    /**
     * Finds a PropertyNameProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param beanClass
     *            the bean class
     * @return the property name processor
     */
    public PropertyNameProcessor findJsonPropertyNameProcessor(Class beanClass){
        if (!jsonPropertyNameProcessorMap.isEmpty()){
            Object key = jsonPropertyNameProcessorMatcher.getMatch(beanClass, jsonPropertyNameProcessorMap.keySet());
            return (PropertyNameProcessor) jsonPropertyNameProcessorMap.get(key);

        }
        return null;
    }

    /**
     * Finds a JsonValueProcessor registered to the target type.<br>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param propertyType
     *            a class used for searching a JsonValueProcessor.
     * @return the json value processor
     */
    public JsonValueProcessor findJsonValueProcessor(Class propertyType){
        if (!typeMap.isEmpty()){
            Object key = jsonValueProcessorMatcher.getMatch(propertyType, typeMap.keySet());
            return (JsonValueProcessor) typeMap.get(key);

        }
        return null;
    }

    /**
     * Finds a JsonValueProcessor.<br>
     * It will search the registered JsonValueProcessors in the following order:
     * <ol>
     * <li>beanClass, key</li>
     * <li>beanClass, type</li>
     * <li>key</li>
     * <li>type</li>
     * </ol>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param beanClass
     *            the class to which the property may belong
     * @param propertyType
     *            the type of the property
     * @param key
     *            the name of the property which may belong to the target class
     * @return the json value processor
     */
    public JsonValueProcessor findJsonValueProcessor(Class beanClass,Class propertyType,String key){
        JsonValueProcessor jsonValueProcessor = null;
        jsonValueProcessor = (JsonValueProcessor) beanKeyMap.get(beanClass, key);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }

        jsonValueProcessor = (JsonValueProcessor) beanTypeMap.get(beanClass, propertyType);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }

        jsonValueProcessor = (JsonValueProcessor) keyMap.get(key);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }

        Object tkey = jsonValueProcessorMatcher.getMatch(propertyType, typeMap.keySet());
        jsonValueProcessor = (JsonValueProcessor) typeMap.get(tkey);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }

        return null;
    }

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
    public JsonValueProcessor findJsonValueProcessor(Class propertyType,String key){
        JsonValueProcessor jsonValueProcessor = null;
        jsonValueProcessor = (JsonValueProcessor) keyMap.get(key);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }

        Object tkey = jsonValueProcessorMatcher.getMatch(propertyType, typeMap.keySet());
        jsonValueProcessor = (JsonValueProcessor) typeMap.get(tkey);
        if (jsonValueProcessor != null){
            return jsonValueProcessor;
        }

        return null;
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
    public Map getClassMap(){
        return classMap;
    }

    /**
     * Returns the current collection type used for collection transformations.<br>
     * [JSON -&gt; Java]
     * 
     * @return the target collection class for conversion
     */
    public Class getCollectionType(){
        return collectionType;
    }

    /**
     * Returns the configured CycleDetectionStrategy.<br>
     * Default value is CycleDetectionStrategy.STRICT<br>
     * [Java -&gt; JSON]
     *
     * @return the cycle detection strategy
     */
    public CycleDetectionStrategy getCycleDetectionStrategy(){
        return cycleDetectionStrategy;
    }

    /**
     * Returns the current enclosed type for generic collection transformations.<br>
     * [JSON -&gt; Java]
     * 
     * @return the target type for conversion
     */
    public Class getEnclosedType(){
        return enclosedType;
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
     * Returns the configured PropertyNameProcessorMatcher.<br>
     * Default value is PropertyNameProcessorMatcher.DEFAULT<br>
     * [JSON -&gt; Java]
     *
     * @return the java property name processor matcher
     */
    public PropertyNameProcessorMatcher getJavaPropertyNameProcessorMatcher(){
        return javaPropertyNameProcessorMatcher;
    }

    /**
     * Returns the configured JsonBeanProcessorMatcher.<br>
     * Default value is JsonBeanProcessorMatcher.DEFAULT<br>
     * [JSON -&gt; Java]
     *
     * @return the json bean processor matcher
     */
    public JsonBeanProcessorMatcher getJsonBeanProcessorMatcher(){
        return jsonBeanProcessorMatcher;
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
     * Returns the configured PropertyNameProcessorMatcher.<br>
     * Default value is PropertyNameProcessorMatcher.DEFAULT<br>
     * [Java -&gt; JSON]
     *
     * @return the json property name processor matcher
     */
    public PropertyNameProcessorMatcher getJsonPropertyNameProcessorMatcher(){
        return javaPropertyNameProcessorMatcher;
    }

    /**
     * Returns the configured JsonValueProcessorMatcher.<br>
     * Default value is JsonValueProcessorMatcher.DEFAULT<br>
     * [Java -&gt; JSON]
     *
     * @return the json value processor matcher
     */
    public JsonValueProcessorMatcher getJsonValueProcessorMatcher(){
        return jsonValueProcessorMatcher;
    }

    /**
     * Returns a set of default excludes with user-defined excludes.<br>
     * [Java -&gt; JSON]
     *
     * @return the merged excludes
     */
    public Collection getMergedExcludes(){
        Collection exclusions = new HashSet();
        for (int i = 0; i < excludes.length; i++){
            String exclusion = excludes[i];
            if (!StringUtils.isBlank(excludes[i])){
                exclusions.add(exclusion.trim());
            }
        }

        if (!ignoreDefaultExcludes){
            for (int i = 0; i < DEFAULT_EXCLUDES.length; i++){
                if (!exclusions.contains(DEFAULT_EXCLUDES[i])){
                    exclusions.add(DEFAULT_EXCLUDES[i]);
                }
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
    public Class getRootClass(){
        return rootClass;
    }

    /**
     * Returns true if non-String keys are allowed on JSONObject.<br>
     * Default value is false<br>
     * [Java -&gt; JSON]
     *
     * @return true, if is allow non string keys
     */
    public boolean isAllowNonStringKeys(){
        return allowNonStringKeys;
    }

    /**
     * Returns true if default excludes will not be used.<br>
     * Default value is false.<br>
     * [Java -&gt; JSON]
     *
     * @return true, if is ignore default excludes
     */
    public boolean isIgnoreDefaultExcludes(){
        return ignoreDefaultExcludes;
    }

    /**
     * Returns true if transient fields of a bean will be ignored.<br>
     * Default value is false.<br>
     * [Java -&gt; JSON]
     *
     * @return true, if is ignore transient fields
     */
    public boolean isIgnoreTransientFields(){
        return ignoreTransientFields;
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
    public void registerDefaultValueProcessor(Class target,DefaultValueProcessor defaultValueProcessor){
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
    public void registerJavaPropertyNameProcessor(Class target,PropertyNameProcessor propertyNameProcessor){
        if (target != null && propertyNameProcessor != null){
            javaPropertyNameProcessorMap.put(target, propertyNameProcessor);
        }
    }

    /**
     * Registers a JsonBeanProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param target
     *            the class to use as key
     * @param jsonBeanProcessor
     *            the processor to register
     */
    public void registerJsonBeanProcessor(Class target,JsonBeanProcessor jsonBeanProcessor){
        if (target != null && jsonBeanProcessor != null){
            beanProcessorMap.put(target, jsonBeanProcessor);
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
    public void registerJsonPropertyNameProcessor(Class target,PropertyNameProcessor propertyNameProcessor){
        if (target != null && propertyNameProcessor != null){
            jsonPropertyNameProcessorMap.put(target, propertyNameProcessor);
        }
    }

    /**
     * Registers a JsonValueProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param beanClass
     *            the class to use as key
     * @param propertyType
     *            the property type to use as key
     * @param jsonValueProcessor
     *            the processor to register
     */
    public void registerJsonValueProcessor(Class beanClass,Class propertyType,JsonValueProcessor jsonValueProcessor){
        if (beanClass != null && propertyType != null && jsonValueProcessor != null){
            beanTypeMap.put(beanClass, propertyType, jsonValueProcessor);
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
    public void registerJsonValueProcessor(Class propertyType,JsonValueProcessor jsonValueProcessor){
        if (propertyType != null && jsonValueProcessor != null){
            typeMap.put(propertyType, jsonValueProcessor);
        }
    }

    /**
     * Registers a JsonValueProcessor.<br>
     * [Java -&gt; JSON]
     * 
     * @param beanClass
     *            the class to use as key
     * @param key
     *            the property name to use as key
     * @param jsonValueProcessor
     *            the processor to register
     */
    public void registerJsonValueProcessor(Class beanClass,String key,JsonValueProcessor jsonValueProcessor){
        if (beanClass != null && key != null && jsonValueProcessor != null){
            beanKeyMap.put(beanClass, key, jsonValueProcessor);
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

    /**
     * Registers exclusions for a target class.<br>
     * [Java -&gt; JSON]
     * 
     * @param target
     *            the class to use as key
     * @param properties
     *            the properties to be excluded
     */
    public void registerPropertyExclusions(Class target,String...properties){
        if (target != null && properties != null && properties.length > 0){
            Set set = (Set) exclusionMap.get(target);
            if (set == null){
                set = new HashSet();
                exclusionMap.put(target, set);
            }
            for (int i = 0; i < properties.length; i++){
                if (!set.contains(properties[i])){
                    set.add(properties[i]);
                }
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Sets if non-String keys are allowed on JSONObject.<br>
     * [Java -&gt; JSON]
     *
     * @param allowNonStringKeys
     *            the new allow non string keys
     */
    public void setAllowNonStringKeys(boolean allowNonStringKeys){
        this.allowNonStringKeys = allowNonStringKeys;
    }

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
            this.enclosedType = DEFAULT_COLLECTION_TYPE;
        }
    }

    /**
     * Sets the current attribute/Class Map<br>
     * [JSON -&gt; Java].
     *
     * @param classMap
     *            a Map of classes, every key identifies a property or a regexp
     */
    public void setClassMap(Map classMap){
        this.classMap = classMap;
    }

    /**
     * Sets the current collection type used for collection transformations.<br>
     * [JSON -&gt; Java]
     * 
     * @param collectionType
     *            the target collection class for conversion
     */
    public void setCollectionType(Class collectionType){
        if (collectionType != null){
            if (!Collection.class.isAssignableFrom(collectionType)){
                throw new JSONException("The configured collectionType is not a Collection: " + collectionType.getName());
            }
            this.collectionType = collectionType;
        }else{
            collectionType = DEFAULT_COLLECTION_TYPE;
        }
    }

    /**
     * Sets a CycleDetectionStrategy to use.<br>
     * Will set default value (CycleDetectionStrategy.STRICT) if null.<br>
     * [Java -&gt; JSON]
     *
     * @param cycleDetectionStrategy
     *            the new cycle detection strategy
     */
    public void setCycleDetectionStrategy(CycleDetectionStrategy cycleDetectionStrategy){
        this.cycleDetectionStrategy = defaultIfNull(cycleDetectionStrategy, DEFAULT_CYCLE_DETECTION_STRATEGY);
    }

    /**
     * Sets a DefaultValueProcessorMatcher to use.<br>
     * Will set default value (DefaultValueProcessorMatcher.DEFAULT) if null.<br>
     * [Java -&gt; JSON]
     *
     * @param defaultValueProcessorMatcher
     *            the new default value processor matcher
     */
    public void setDefaultValueProcessorMatcher(DefaultValueProcessorMatcher defaultValueProcessorMatcher){
        this.defaultValueProcessorMatcher = defaultValueProcessorMatcher == null ? DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER
                        : defaultValueProcessorMatcher;
    }

    /**
     * Sets the current enclosed type for generic collection transformations.<br>
     * [JSON -&gt; Java]
     * 
     * @param enclosedType
     *            the target type for conversion
     */
    public void setEnclosedType(Class enclosedType){
        this.enclosedType = enclosedType;
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
        this.excludes = excludes == null ? EMPTY_EXCLUDES : excludes;
    }

    /**
     * Sets if default excludes would be skipped when building.<br>
     * [Java -&gt; JSON]
     *
     * @param ignoreDefaultExcludes
     *            the new ignore default excludes
     */
    public void setIgnoreDefaultExcludes(boolean ignoreDefaultExcludes){
        this.ignoreDefaultExcludes = ignoreDefaultExcludes;
    }

    /**
     * Sets if transient fields would be skipped when building.<br>
     * [Java -&gt; JSON]
     *
     * @param ignoreTransientFields
     *            the new ignore transient fields
     */
    public void setIgnoreTransientFields(boolean ignoreTransientFields){
        this.ignoreTransientFields = ignoreTransientFields;
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
        this.javaIdentifierTransformer = javaIdentifierTransformer == null ? DEFAULT_JAVA_IDENTIFIER_TRANSFORMER
                        : javaIdentifierTransformer;
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
     * Sets a PropertyNameProcessorMatcher to use.<br>
     * Will set default value (PropertyNameProcessorMatcher.DEFAULT) if null.<br>
     * [JSON -&gt; Java]
     *
     * @param propertyNameProcessorMatcher
     *            the new java property name processor matcher
     */
    public void setJavaPropertyNameProcessorMatcher(PropertyNameProcessorMatcher propertyNameProcessorMatcher){
        this.javaPropertyNameProcessorMatcher = propertyNameProcessorMatcher == null ? DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER
                        : propertyNameProcessorMatcher;
    }

    /**
     * Sets a JsonBeanProcessorMatcher to use.<br>
     * Will set default value (JsonBeanProcessorMatcher.DEFAULT) if null.<br>
     * [Java -&gt; JSON]
     *
     * @param jsonBeanProcessorMatcher
     *            the new json bean processor matcher
     */
    public void setJsonBeanProcessorMatcher(JsonBeanProcessorMatcher jsonBeanProcessorMatcher){
        this.jsonBeanProcessorMatcher = jsonBeanProcessorMatcher == null ? DEFAULT_JSON_BEAN_PROCESSOR_MATCHER : jsonBeanProcessorMatcher;
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
     * Sets a PropertyNameProcessorMatcher to use.<br>
     * Will set default value (PropertyNameProcessorMatcher.DEFAULT) if null.<br>
     * [Java -&gt; JSON]
     *
     * @param propertyNameProcessorMatcher
     *            the new json property name processor matcher
     */
    public void setJsonPropertyNameProcessorMatcher(PropertyNameProcessorMatcher propertyNameProcessorMatcher){
        this.jsonPropertyNameProcessorMatcher = propertyNameProcessorMatcher == null ? DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER
                        : propertyNameProcessorMatcher;
    }

    /**
     * Sets a JsonValueProcessorMatcher to use.<br>
     * Will set default value (JsonValueProcessorMatcher.DEFAULT) if null.<br>
     * [Java -&gt; JSON]
     *
     * @param jsonValueProcessorMatcher
     *            the new json value processor matcher
     */
    public void setJsonValueProcessorMatcher(JsonValueProcessorMatcher jsonValueProcessorMatcher){
        this.jsonValueProcessorMatcher = jsonValueProcessorMatcher == null ? DEFAULT_JSON_VALUE_PROCESSOR_MATCHER
                        : jsonValueProcessorMatcher;
    }

    /**
     * Sets the current root Class.<br>
     * [JSON -&gt; Java]
     * 
     * @param rootClass
     *            the target class for conversion
     */
    public void setRootClass(Class rootClass){
        this.rootClass = rootClass;
    }

    /**
     * Resets all values to its default state.
     */
    public void reset(){
        excludes = EMPTY_EXCLUDES;
        ignoreDefaultExcludes = false;
        ignoreTransientFields = false;
        javaIdentifierTransformer = DEFAULT_JAVA_IDENTIFIER_TRANSFORMER;
        cycleDetectionStrategy = DEFAULT_CYCLE_DETECTION_STRATEGY;
        arrayMode = MODE_LIST;
        rootClass = null;
        classMap = null;
        keyMap.clear();
        typeMap.clear();
        beanKeyMap.clear();
        beanTypeMap.clear();
        jsonPropertyFilter = null;
        javaPropertyFilter = null;
        jsonBeanProcessorMatcher = DEFAULT_JSON_BEAN_PROCESSOR_MATCHER;
        defaultValueProcessorMatcher = DEFAULT_DEFAULT_VALUE_PROCESSOR_MATCHER;
        defaultValueMap.clear();
        //ignoreJPATransient = false;
        collectionType = DEFAULT_COLLECTION_TYPE;
        enclosedType = null;
        jsonValueProcessorMatcher = DEFAULT_JSON_VALUE_PROCESSOR_MATCHER;
        javaPropertyNameProcessorMap.clear();
        javaPropertyNameProcessorMatcher = DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
        jsonPropertyNameProcessorMap.clear();
        jsonPropertyNameProcessorMatcher = DEFAULT_PROPERTY_NAME_PROCESSOR_MATCHER;
        beanProcessorMap.clear();
        exclusionMap.clear();
        allowNonStringKeys = false;
    }

    //---------------------------------------------------------------

    /**
     * Copy.
     *
     * @return the json config
     */
    public JsonConfig copy(){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.beanKeyMap.putAll(beanKeyMap);
        jsonConfig.beanTypeMap.putAll(beanTypeMap);
        jsonConfig.classMap = new HashMap();
        if (classMap != null){
            jsonConfig.classMap.putAll(classMap);
        }
        jsonConfig.cycleDetectionStrategy = cycleDetectionStrategy;
        if (excludes != null){
            jsonConfig.excludes = new String[excludes.length];
            System.arraycopy(excludes, 0, jsonConfig.excludes, 0, excludes.length);
        }
        jsonConfig.ignoreDefaultExcludes = ignoreDefaultExcludes;
        jsonConfig.ignoreTransientFields = ignoreTransientFields;
        jsonConfig.javaIdentifierTransformer = javaIdentifierTransformer;
        jsonConfig.keyMap.putAll(keyMap);
        jsonConfig.beanProcessorMap.putAll(beanProcessorMap);
        jsonConfig.rootClass = rootClass;
        jsonConfig.typeMap.putAll(typeMap);
        jsonConfig.jsonPropertyFilter = jsonPropertyFilter;
        jsonConfig.javaPropertyFilter = javaPropertyFilter;
        jsonConfig.jsonBeanProcessorMatcher = jsonBeanProcessorMatcher;
        jsonConfig.defaultValueProcessorMatcher = defaultValueProcessorMatcher;
        jsonConfig.defaultValueMap.putAll(defaultValueMap);
        jsonConfig.collectionType = collectionType;
        jsonConfig.enclosedType = enclosedType;
        jsonConfig.jsonValueProcessorMatcher = jsonValueProcessorMatcher;
        jsonConfig.javaPropertyNameProcessorMatcher = javaPropertyNameProcessorMatcher;
        jsonConfig.javaPropertyNameProcessorMap.putAll(javaPropertyNameProcessorMap);
        jsonConfig.jsonPropertyNameProcessorMatcher = jsonPropertyNameProcessorMatcher;
        jsonConfig.jsonPropertyNameProcessorMap.putAll(jsonPropertyNameProcessorMap);
        jsonConfig.exclusionMap.putAll(exclusionMap);
        jsonConfig.allowNonStringKeys = allowNonStringKeys;
        return jsonConfig;
    }

}