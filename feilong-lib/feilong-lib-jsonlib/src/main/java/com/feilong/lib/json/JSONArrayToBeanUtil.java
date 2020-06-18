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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
class JSONArrayToBeanUtil{

    /** Don't let anyone instantiate this class. */
    private JSONArrayToBeanUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Returns a List or a Set taking generics into account.<br/>
     * Contributed by [Matt Small @ WaveMaker].
     *
     * @param jsonArray
     *            the json array
     * @param jsonConfig
     *            the json config
     * @return the collection
     */
    static Collection<?> toCollection(JSONArray jsonArray,JsonConfig jsonConfig){
        Collection collection = null;
        Class<?> collectionType = jsonConfig.getCollectionType();

        if (collectionType.isInterface()){
            if (collectionType.equals(List.class)){
                collection = new ArrayList<>();
            }else if (collectionType.equals(Set.class)){
                collection = new HashSet<>();
            }else{
                throw new JSONException("unknown interface: " + collectionType);
            }
        }else{
            try{
                collection = (Collection) collectionType.newInstance();
            }catch (InstantiationException e){
                throw new JSONException("", e);
            }catch (IllegalAccessException e){
                throw new JSONException("", e);
            }
        }

        //---------------------------------------------------------------
        Class<?> objectClass = jsonConfig.getRootClass();
        Map<String, Class<?>> classMap = jsonConfig.getClassMap();

        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);

            if (JSONUtils.isNull(value)){
                collection.add(null);
                continue;
            }
            Class<?> type = value.getClass();
            if (JSONArray.class.isAssignableFrom(value.getClass())){
                collection.add(toCollection((JSONArray) value, jsonConfig));
            }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                            || Character.class.isAssignableFrom(type)){

                if (objectClass != null && !objectClass.isAssignableFrom(type)){
                    value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                }
                collection.add(value);
            }else{
                if (objectClass != null){
                    JsonConfig jsc = jsonConfig.copy();
                    jsc.setRootClass(objectClass);
                    jsc.setClassMap(classMap);
                    collection.add(JSONObjectToBeanUtil.toBean((JSONObject) value, jsc));
                }else{
                    collection.add(JSONObjectToBeanUtil.toBean((JSONObject) value, null));
                }
            }
        }

        return collection;
    }

    //---------------------------------------------------------------

    /**
     * Returns a List or a Set taking generics into account.<br/>
     *
     * @param jsonArray
     *            the json array
     * @return the collection
     */
    public static Collection<?> toCollection(JSONArray jsonArray){
        return toCollection(jsonArray, new JsonConfig());
    }

    /**
     * Creates a java array from a JSONArray.<br>
     *
     * @param jsonArray
     *            the json array
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    static Object toArray(JSONArray jsonArray,JsonConfig jsonConfig){
        Class<?> rootClass = jsonConfig.getRootClass();
        int size = jsonArray.size();
        if (size == 0){
            return Array.newInstance(rootClass == null ? Object.class : rootClass, 0);
        }

        //---------------------------------------------------------------
        int[] dimensions = getDimensions(jsonArray);
        Object returnArray = Array.newInstance(rootClass == null ? Object.class : rootClass, dimensions);

        Map<String, Class<?>> classMap = jsonConfig.getClassMap();
        //---------------------------------------------------------------
        for (int i = 0; i < size; i++){
            Object element = jsonArray.get(i);
            if (JSONUtils.isNull(element)){
                Array.set(returnArray, i, null);
                continue;
            }
            //---------------------------------------------------------------
            Class<?> elementType = element.getClass();
            if (JSONArray.class.isAssignableFrom(elementType)){
                Array.set(returnArray, i, toArray((JSONArray) element, rootClass, classMap));
                continue;
            }

            if (String.class.isAssignableFrom(elementType) || Boolean.class.isAssignableFrom(elementType)
                            || Character.class.isAssignableFrom(elementType)){
                if (rootClass != null && !rootClass.isAssignableFrom(elementType)){
                    element = JSONUtils.getMorpherRegistry().morph(rootClass, element);
                }
                Array.set(returnArray, i, element);
                continue;
            }

            if (JSONUtils.isNumber(elementType)){
                if (rootClass != null && (Byte.class.isAssignableFrom(rootClass) || Byte.TYPE.isAssignableFrom(rootClass))){
                    Array.set(returnArray, i, Byte.valueOf(String.valueOf(element)));
                }else if (rootClass != null && (Short.class.isAssignableFrom(rootClass) || Short.TYPE.isAssignableFrom(rootClass))){
                    Array.set(returnArray, i, Short.valueOf(String.valueOf(element)));
                }else{
                    Array.set(returnArray, i, element);
                }
                continue;
            }

            if (rootClass != null){
                JsonConfig jsc = jsonConfig.copy();
                jsc.setRootClass(rootClass);
                jsc.setClassMap(classMap);

                Array.set(returnArray, i, JSONObjectToBeanUtil.toBean((JSONObject) element, jsc));
            }else{
                Array.set(returnArray, i, JSONObjectToBeanUtil.toBean((JSONObject) element, null));
            }
        }
        return returnArray;
    }

    /**
     * Creates a java array from a JSONArray.<br>
     * Any attribute is a JSONObject and matches a key in the classMap, it will
     * be converted to that target class.<br>
     * The classMap has the following conventions:
     * <ul>
     * <li>Every key must be an String.</li>
     * <li>Every value must be a Class.</li>
     * <li>A key may be a regular expression.</li>
     * </ul>
     *
     * @param jsonArray
     *            the json array
     * @param objectClass
     *            the object class
     * @param classMap
     *            the class map
     * @return the object
     */
    private static Object toArray(JSONArray jsonArray,Class<?> objectClass,Map<String, Class<?>> classMap){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toArray(jsonArray, jsonConfig);
    }

    //---------------------------------------------------------------

    /**
     * Returns the number of dimensions suited for a java array.
     *
     * @param jsonArray
     *            the json array
     * @return the dimensions
     * @deprecated maybe 可以删掉
     */
    @Deprecated
    private static int[] getDimensions(JSONArray jsonArray){
        // short circuit for empty arrays
        List<Integer> dims = new ArrayList<>();
        processArrayDimensions(jsonArray, dims, 0);
        int[] dimensions = new int[dims.size()];
        int j = 0;

        for (Iterator i = dims.iterator(); i.hasNext();){
            dimensions[j++] = ((Integer) i.next()).intValue();
        }
        return dimensions;
    }

    /**
     * Process array dimensions.
     *
     * @param jsonArray
     *            the json array
     * @param dims
     *            the dims
     * @param index
     *            the index
     * @deprecated maybe 可以删掉
     */
    @Deprecated
    private static void processArrayDimensions(JSONArray jsonArray,List<Integer> dims,int index){
        if (dims.size() <= index){
            dims.add(jsonArray.size());
        }else{
            int i = dims.get(index).intValue();
            if (jsonArray.size() > i){
                dims.set(index, jsonArray.size());
            }
        }

        //---------------------------------------------------------------
        for (Object element : jsonArray.elementList){
            if (element instanceof JSONArray){
                processArrayDimensions((JSONArray) element, dims, index + 1);
            }
        }
    }

}
