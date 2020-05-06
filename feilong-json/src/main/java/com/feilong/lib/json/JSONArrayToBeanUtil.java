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

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.lib.json.util.JSONExceptionUtil;
import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class JSONArrayToBeanUtil{

    /** Don't let anyone instantiate this class. */
    private JSONArrayToBeanUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

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
    static Collection toCollection(JSONArray jsonArray,JsonConfig jsonConfig){
        Collection collection = null;
        Class collectionType = jsonConfig.getCollectionType();

        if (collectionType.isInterface()){
            if (collectionType.equals(List.class)){
                collection = new ArrayList();
            }else if (collectionType.equals(Set.class)){
                collection = new HashSet();
            }else{
                throw new JSONException("unknown interface: " + collectionType);
            }
        }else{
            try{
                collection = (Collection) collectionType.newInstance();
            }catch (InstantiationException e){
                throw new JSONException(e);
            }catch (IllegalAccessException e){
                throw new JSONException(e);
            }
        }

        //---------------------------------------------------------------

        Class objectClass = jsonConfig.getRootClass();
        Map classMap = jsonConfig.getClassMap();

        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);

            if (JSONUtils.isNull(value)){
                collection.add(null);
                continue;
            }
            Class type = value.getClass();
            if (JSONArray.class.isAssignableFrom(value.getClass())){
                collection.add(toCollection((JSONArray) value, jsonConfig));
            }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                            || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){

                if (objectClass != null && !objectClass.isAssignableFrom(type)){
                    value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                }
                collection.add(value);
            }else{
                if (objectClass != null){
                    JsonConfig jsc = jsonConfig.copy();
                    jsc.setRootClass(objectClass);
                    jsc.setClassMap(classMap);
                    collection.add(JSONObject.toBean((JSONObject) value, jsc));
                }else{
                    collection.add(JSONObject.toBean((JSONObject) value));
                }
            }
        }

        return collection;
    }

    //---------------------------------------------------------------

    /**
     * Creates a List from a JSONArray.<br>
     *
     * @param jsonArray
     *            the json array
     * @param root
     *            the root
     * @param jsonConfig
     *            the json config
     * @return the list
     */
    static List toList(JSONArray jsonArray,Object root,JsonConfig jsonConfig){
        if (jsonArray.size() == 0 || root == null){
            return new ArrayList();
        }

        List list = new ArrayList();
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                list.add(null);
                continue;
            }
            Class type = value.getClass();
            if (JSONArray.class.isAssignableFrom(type)){
                list.add(toList((JSONArray) value, root, jsonConfig));
            }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                            || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                list.add(value);
            }else{
                try{
                    Object newRoot = ConstructorUtil.newInstance(root.getClass());
                    list.add(JSONObject.toBean((JSONObject) value, newRoot, jsonConfig));
                }catch (Exception e){
                    throw JSONExceptionUtil.build("", e);
                }
            }
        }
        return list;
    }

    /**
     * Creates a java array from a JSONArray.<br>
     *
     * @param jsonArray
     *            the json array
     * @param root
     *            the root
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    static Object toArray(JSONArray jsonArray,Object root,JsonConfig jsonConfig){
        Class objectClass = root.getClass();
        if (jsonArray.size() == 0){
            return Array.newInstance(objectClass, 0);
        }

        int[] dimensions = getDimensions(jsonArray);
        Object array = Array.newInstance(objectClass == null ? Object.class : objectClass, dimensions);
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                Array.set(array, i, null);
                continue;
            }
            Class type = value.getClass();
            if (JSONArray.class.isAssignableFrom(type)){
                Array.set(array, i, toArray((JSONArray) value, root, jsonConfig));
            }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                            || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                if (objectClass != null && !objectClass.isAssignableFrom(type)){
                    value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                }
                Array.set(array, i, value);
            }else{
                try{
                    Object newRoot = ConstructorUtil.newInstance(root.getClass());
                    Array.set(array, i, JSONObject.toBean((JSONObject) value, newRoot, jsonConfig));
                }catch (Exception e){
                    throw JSONExceptionUtil.build("", e);
                }
            }
        }
        return array;
    }

    /**
     * Returns a List or a Set taking generics into account.<br/>
     *
     * @param jsonArray
     *            the json array
     * @return the collection
     */
    public static Collection toCollection(JSONArray jsonArray){
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
        Class objectClass = jsonConfig.getRootClass();
        Map classMap = jsonConfig.getClassMap();

        if (jsonArray.size() == 0){
            return Array.newInstance(objectClass == null ? Object.class : objectClass, 0);
        }

        int[] dimensions = getDimensions(jsonArray);
        Object array = Array.newInstance(objectClass == null ? Object.class : objectClass, dimensions);
        int size = jsonArray.size();

        //---------------------------------------------------------------
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                Array.set(array, i, null);
                continue;
            }

            //---------------------------------------------------------------
            Class type = value.getClass();
            if (JSONArray.class.isAssignableFrom(type)){
                Array.set(array, i, toArray((JSONArray) value, objectClass, classMap));
            }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)
                            || JSONFunction.class.isAssignableFrom(type)){
                if (objectClass != null && !objectClass.isAssignableFrom(type)){
                    value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                }
                Array.set(array, i, value);
            }else if (JSONUtils.isNumber(type)){
                if (objectClass != null && (Byte.class.isAssignableFrom(objectClass) || Byte.TYPE.isAssignableFrom(objectClass))){
                    Array.set(array, i, Byte.valueOf(String.valueOf(value)));
                }else if (objectClass != null && (Short.class.isAssignableFrom(objectClass) || Short.TYPE.isAssignableFrom(objectClass))){
                    Array.set(array, i, Short.valueOf(String.valueOf(value)));
                }else{
                    Array.set(array, i, value);
                }
            }else{
                if (objectClass != null){
                    JsonConfig jsc = jsonConfig.copy();
                    jsc.setRootClass(objectClass);
                    jsc.setClassMap(classMap);
                    Array.set(array, i, JSONObject.toBean((JSONObject) value, jsc));
                }else{
                    Array.set(array, i, JSONObject.toBean((JSONObject) value));
                }
            }
        }
        return array;
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
    private static Object toArray(JSONArray jsonArray,Class objectClass,Map classMap){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return JSONArrayToBeanUtil.toArray(jsonArray, jsonConfig);
    }

    /**
     * Returns the number of dimensions suited for a java array.
     *
     * @param jsonArray
     *            the json array
     * @return the dimensions
     */
    static int[] getDimensions(JSONArray jsonArray){
        // short circuit for empty arrays
        if (jsonArray == null || jsonArray.isEmpty()){
            return new int[] { 0 };
        }

        List dims = new ArrayList();
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
     */
    private static void processArrayDimensions(JSONArray jsonArray,List dims,int index){
        if (dims.size() <= index){
            dims.add(new Integer(jsonArray.size()));
        }else{
            int i = ((Integer) dims.get(index)).intValue();
            if (jsonArray.size() > i){
                dims.set(index, new Integer(jsonArray.size()));
            }
        }
        for (Iterator i = jsonArray.iterator(); i.hasNext();){
            Object item = i.next();
            if (item instanceof JSONArray){
                processArrayDimensions((JSONArray) item, dims, index + 1);
            }
        }
    }

}
