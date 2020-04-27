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
package com.feilong.json.lib.json;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.feilong.json.lib.ezmorph.Morpher;
import com.feilong.json.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.json.lib.json.processors.JsonVerifier;
import com.feilong.json.lib.json.util.JSONTokener;
import com.feilong.json.lib.json.util.JSONUtils;

import net.sf.json.processors.JsonValueProcessor;

/**
 * A JSONArray is an ordered sequence of values. Its external text form is a
 * string wrapped in square brackets with commas separating the values. The
 * internal form is an object having <code>get</code> and <code>opt</code>
 * methods for accessing the values by index, and <code>element</code> methods
 * for adding or replacing values. The values can be any of these types:
 * <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>,
 * <code>Number</code>, <code>String</code>, or the
 * <code>JSONNull object</code>.
 * <p>
 * The constructor can convert a JSON text into a Java object. The
 * <code>toString</code> method converts to JSON text.
 * <p>
 * A <code>get</code> method returns a value if one can be found, and throws
 * an exception if one cannot be found. An <code>opt</code> method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an
 * object which you can cast or query for type. There are also typed
 * <code>get</code> and <code>opt</code> methods that do type checking and
 * type coersion for you.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * JSON syntax rules. The constructors are more forgiving in the texts they will
 * accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing bracket.</li>
 * <li>The <code>null</code> value will be inserted when there is
 * <code>,</code>&nbsp;<small>(comma)</small> elision.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a
 * quote or single quote, and if they do not contain leading or trailing spaces,
 * and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>,
 * or <code>null</code>.</li>
 * <li>Values can be separated by <code>;</code> <small>(semicolon)</small>
 * as well as by <code>,</code> <small>(comma)</small>.</li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 * <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Comments written in the slashshlash, slashstar, and hash conventions
 * will be ignored.</li>
 * </ul>
 *
 * @author JSON.org
 */
public final class JSONArray extends AbstractJSON implements JSON,List,Comparable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1663435868052425703L;

    /**
     * Creates a JSONArray.<br>
     * Inspects the object type to call the correct JSONArray factory method.
     * Accepts JSON formatted strings, arrays, Collections and Enums.
     *
     * @param object
     *            the object
     * @return the JSON array
     * @throws JSONException
     *             if the object can not be converted to a proper
     *             JSONArray.
     */
    public static JSONArray fromObject(Object object){
        return fromObject(object, new JsonConfig());
    }

    /**
     * Creates a JSONArray.<br>
     * Inspects the object type to call the correct JSONArray factory method.
     * Accepts JSON formatted strings, arrays, Collections and Enums.
     *
     * @param object
     *            the object
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     * @throws JSONException
     *             if the object can not be converted to a proper
     *             JSONArray.
     */
    public static JSONArray fromObject(Object object,JsonConfig jsonConfig){
        if (object instanceof JSONString){
            return _fromJSONString((JSONString) object, jsonConfig);
        }else if (object instanceof JSONArray){
            return _fromJSONArray((JSONArray) object, jsonConfig);
        }else if (object instanceof Collection){
            return _fromCollection((Collection) object, jsonConfig);
        }else if (object instanceof JSONTokener){
            return _fromJSONTokener((JSONTokener) object, jsonConfig);
        }else if (object instanceof String){
            return _fromString((String) object, jsonConfig);
        }else if (object != null && object.getClass().isArray()){
            Class type = object.getClass().getComponentType();
            if (!type.isPrimitive()){
                return _fromArray((Object[]) object, jsonConfig);
            }else{
                if (type == Boolean.TYPE){
                    return _fromArray((boolean[]) object, jsonConfig);
                }else if (type == Byte.TYPE){
                    return _fromArray((byte[]) object, jsonConfig);
                }else if (type == Short.TYPE){
                    return _fromArray((short[]) object, jsonConfig);
                }else if (type == Integer.TYPE){
                    return _fromArray((int[]) object, jsonConfig);
                }else if (type == Long.TYPE){
                    return _fromArray((long[]) object, jsonConfig);
                }else if (type == Float.TYPE){
                    return _fromArray((float[]) object, jsonConfig);
                }else if (type == Double.TYPE){
                    return _fromArray((double[]) object, jsonConfig);
                }else if (type == Character.TYPE){
                    return _fromArray((char[]) object, jsonConfig);
                }else{
                    throw new JSONException("Unsupported type");
                }
            }
        }else if (JSONUtils.isBoolean(object) || JSONUtils.isFunction(object) || JSONUtils.isNumber(object) || JSONUtils.isNull(object)
                        || JSONUtils.isString(object) || object instanceof JSON){
            fireArrayStartEvent(jsonConfig);
            JSONArray jsonArray = new JSONArray().element(object, jsonConfig);
            fireElementAddedEvent(0, jsonArray.get(0), jsonConfig);
            fireArrayStartEvent(jsonConfig);
            return jsonArray;
        }else if (object instanceof Enum){
            return _fromArray((Enum) object, jsonConfig);
        }else if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())){
            throw new JSONException("Unsupported type");
        }else if (JSONUtils.isObject(object)){
            fireArrayStartEvent(jsonConfig);
            JSONArray jsonArray = new JSONArray().element(JSONObject.fromObject(object, jsonConfig));
            fireElementAddedEvent(0, jsonArray.get(0), jsonConfig);
            fireArrayStartEvent(jsonConfig);
            return jsonArray;
        }else{
            throw new JSONException("Unsupported type");
        }
    }

    /**
     * Get the collection type from a getter or setter, or null if no type was
     * found.<br/>
     * Contributed by [Matt Small @ WaveMaker].
     *
     * @param pd
     *            the pd
     * @param useGetter
     *            the use getter
     * @return the collection type
     * @throws JSONException
     *             the JSON exception
     */
    public static Class[] getCollectionType(PropertyDescriptor pd,boolean useGetter) throws JSONException{

        Type type;
        if (useGetter){
            Method m = pd.getReadMethod();
            type = m.getGenericReturnType();
        }else{
            Method m = pd.getWriteMethod();
            Type[] gpts = m.getGenericParameterTypes();

            if (1 != gpts.length){
                throw new JSONException("method " + m + " is not a standard setter");
            }
            type = gpts[0];
        }

        if (!(type instanceof ParameterizedType)){
            return null;
            // throw new JSONException("type not instanceof ParameterizedType:
            // "+type.getClass());
        }

        ParameterizedType pType = (ParameterizedType) type;
        Type[] actualTypes = pType.getActualTypeArguments();

        Class[] ret = new Class[actualTypes.length];
        for (int i = 0; i < ret.length; i++){
            ret[i] = (Class) actualTypes[i];
        }

        return ret;
    }

    /**
     * Returns the number of dimensions suited for a java array.
     *
     * @param jsonArray
     *            the json array
     * @return the dimensions
     */
    public static int[] getDimensions(JSONArray jsonArray){
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
     * Creates a java array from a JSONArray.
     *
     * @param jsonArray
     *            the json array
     * @return the object
     */
    public static Object toArray(JSONArray jsonArray){
        return toArray(jsonArray, new JsonConfig());
    }

    /**
     * Creates a java array from a JSONArray.
     *
     * @param jsonArray
     *            the json array
     * @param objectClass
     *            the object class
     * @return the object
     */
    public static Object toArray(JSONArray jsonArray,Class objectClass){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toArray(jsonArray, jsonConfig);
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
    public static Object toArray(JSONArray jsonArray,Class objectClass,Map classMap){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toArray(jsonArray, jsonConfig);
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
    public static Object toArray(JSONArray jsonArray,JsonConfig jsonConfig){
        Class objectClass = jsonConfig.getRootClass();
        Map classMap = jsonConfig.getClassMap();

        if (jsonArray.size() == 0){
            return Array.newInstance(objectClass == null ? Object.class : objectClass, 0);
        }

        int[] dimensions = JSONArray.getDimensions(jsonArray);
        Object array = Array.newInstance(objectClass == null ? Object.class : objectClass, dimensions);
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                Array.set(array, i, null);
            }else{
                Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)){
                    Array.set(array, i, toArray((JSONArray) value, objectClass, classMap));
                }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)
                                || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                    if (objectClass != null && !objectClass.isAssignableFrom(type)){
                        value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                    }
                    Array.set(array, i, value);
                }else if (JSONUtils.isNumber(type)){
                    if (objectClass != null && (Byte.class.isAssignableFrom(objectClass) || Byte.TYPE.isAssignableFrom(objectClass))){
                        Array.set(array, i, Byte.valueOf(String.valueOf(value)));
                    }else if (objectClass != null
                                    && (Short.class.isAssignableFrom(objectClass) || Short.TYPE.isAssignableFrom(objectClass))){
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
        }
        return array;
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
    public static Object toArray(JSONArray jsonArray,Object root,JsonConfig jsonConfig){
        Class objectClass = root.getClass();
        if (jsonArray.size() == 0){
            return Array.newInstance(objectClass, 0);
        }

        int[] dimensions = JSONArray.getDimensions(jsonArray);
        Object array = Array.newInstance(objectClass == null ? Object.class : objectClass, dimensions);
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                Array.set(array, i, null);
            }else{
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
                        Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(root.getClass(), null);
                        Array.set(array, i, JSONObject.toBean((JSONObject) value, newRoot, jsonConfig));
                    }catch (JSONException jsone){
                        throw jsone;
                    }catch (Exception e){
                        throw new JSONException(e);
                    }
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
     * Returns a List or a Set taking generics into account.<br/>
     *
     * @param jsonArray
     *            the json array
     * @param objectClass
     *            the object class
     * @return the collection
     */
    public static Collection toCollection(JSONArray jsonArray,Class objectClass){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toCollection(jsonArray, jsonConfig);
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
    public static Collection toCollection(JSONArray jsonArray,JsonConfig jsonConfig){
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

        Class objectClass = jsonConfig.getRootClass();
        Map classMap = jsonConfig.getClassMap();

        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);

            if (JSONUtils.isNull(value)){
                collection.add(null);
            }else{
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
        }

        return collection;
    }
    /*
     * public static Collection toCollection( JSONArray jsonArray, JsonConfig jsonConfig ) {
     * Collection collection = null;
     * Class collectionType = jsonConfig.getCollectionType();
     * Class enclosedType = jsonConfig.getEnclosedType();
     * 
     * if( collectionType.isInterface() ){
     * if( collectionType.equals( List.class ) ){
     * collection = new ArrayList();
     * }else if( collectionType.equals( Set.class ) ){
     * collection = new HashSet();
     * }else{
     * throw new JSONException( "unknown interface: " + collectionType );
     * }
     * }else{
     * try{
     * collection = (Collection) collectionType.newInstance();
     * }catch( InstantiationException e ){
     * throw new JSONException( e );
     * }catch( IllegalAccessException e ){
     * throw new JSONException( e );
     * }
     * }
     * 
     * Class objectClass = jsonConfig.getRootClass();
     * Map classMap = jsonConfig.getClassMap();
     * 
     * int size = jsonArray.size();
     * for( int i = 0; i < size; i++ ){
     * Object value = jsonArray.get( i );
     * Class enclosedTypeE = enclosedType;
     * 
     * if( null == enclosedTypeE ){
     * enclosedTypeE = value.getClass();
     * }
     * 
     * if( JSONUtils.isNull( value ) ){
     * collection.add( null );
     * }else{
     * if( JSONArray.class.isAssignableFrom( value.getClass() ) ){
     * //throw new RuntimeException( "can't have nested collections" );
     * collection.add( toCollection( (JSONArray) value, jsonConfig ) );
     * }else if( String.class.isAssignableFrom( enclosedTypeE )
     * || Boolean.class.isAssignableFrom( enclosedTypeE )
     * || JSONUtils.isNumber( enclosedTypeE )
     * || Character.class.isAssignableFrom( enclosedTypeE )
     * || JSONFunction.class.isAssignableFrom( enclosedTypeE ) ){
     * 
     * if( !value.getClass()
     * .isAssignableFrom( enclosedTypeE ) ){
     * throw new JSONException( "can't assign value " + value + " of type "
     * + value.getClass() + " to Collection of type " + enclosedTypeE );
     * }
     * collection.add( value );
     * }else{
     * try{
     * if( JSON.class.isAssignableFrom( enclosedTypeE ) ){
     * ret.add( JSONObject.toBean( (JSONObject) value ) );
     * }else{
     * Object newRoot = enclosedTypeE.newInstance();
     * ret.add( JSONObject.toBean( (JSONObject) value, newRoot, jsonConfig ) );
     * }
     * }catch( JSONException jsone ){
     * throw jsone;
     * }catch( Exception e ){
     * throw new JSONException( e );
     * }
     * if( objectClass != null ){
     * JsonConfig jsc = jsonConfig.copy();
     * jsc.setRootClass( objectClass );
     * jsc.setClassMap( classMap );
     * collection.add( JSONObject.toBean( (JSONObject) value, jsc ) );
     * }else{
     * collection.add( JSONObject.toBean( (JSONObject) value ) );
     * }
     * }
     * }
     * }
     * 
     * return collection;
     * }
     */

    /**
     * Creates a List from a JSONArray.<br>
     *
     * @param jsonArray
     *            the json array
     * @return the list
     * @see #toCollection(JSONArray)
     * @deprecated replaced by toCollection
     */
    @Deprecated
    public static List toList(JSONArray jsonArray){
        return toList(jsonArray, new JsonConfig());
    }

    /**
     * Creates a List from a JSONArray.
     *
     * @param jsonArray
     *            the json array
     * @param objectClass
     *            the object class
     * @return the list
     * @see #toCollection(JSONArray,Class)
     * @deprecated replaced by toCollection
     */
    @Deprecated
    public static List toList(JSONArray jsonArray,Class objectClass){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        return toList(jsonArray, jsonConfig);
    }

    /**
     * Creates a List from a JSONArray.<br>
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
     * @return the list
     * @see #toCollection(JSONArray,Class,Map)
     * @deprecated replaced by toCollection
     */
    @Deprecated
    public static List toList(JSONArray jsonArray,Class objectClass,Map classMap){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(objectClass);
        jsonConfig.setClassMap(classMap);
        return toList(jsonArray, jsonConfig);
    }

    /**
     * Creates a List from a JSONArray.<br>
     *
     * @param jsonArray
     *            the json array
     * @param jsonConfig
     *            the json config
     * @return the list
     * @see #toCollection(JSONArray,JsonConfig)
     * @deprecated replaced by toCollection
     */
    @Deprecated
    public static List toList(JSONArray jsonArray,JsonConfig jsonConfig){
        if (jsonArray.size() == 0){
            return new ArrayList();
        }

        Class objectClass = jsonConfig.getRootClass();
        Map classMap = jsonConfig.getClassMap();

        List list = new ArrayList();
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                list.add(null);
            }else{
                Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)){
                    list.add(toList((JSONArray) value, objectClass, classMap));
                }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                                || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                    if (objectClass != null && !objectClass.isAssignableFrom(type)){
                        value = JSONUtils.getMorpherRegistry().morph(objectClass, value);
                    }
                    list.add(value);
                }else{
                    if (objectClass != null){
                        JsonConfig jsc = jsonConfig.copy();
                        jsc.setRootClass(objectClass);
                        jsc.setClassMap(classMap);
                        list.add(JSONObject.toBean((JSONObject) value, jsc));
                    }else{
                        list.add(JSONObject.toBean((JSONObject) value));
                    }
                }
            }
        }
        return list;
    }

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
    public static List toList(JSONArray jsonArray,Object root,JsonConfig jsonConfig){
        if (jsonArray.size() == 0 || root == null){
            return new ArrayList();
        }

        List list = new ArrayList();
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            Object value = jsonArray.get(i);
            if (JSONUtils.isNull(value)){
                list.add(null);
            }else{
                Class type = value.getClass();
                if (JSONArray.class.isAssignableFrom(type)){
                    list.add(toList((JSONArray) value, root, jsonConfig));
                }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                                || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                    list.add(value);
                }else{
                    try{
                        Object newRoot = jsonConfig.getNewBeanInstanceStrategy().newInstance(root.getClass(), null);
                        list.add(JSONObject.toBean((JSONObject) value, newRoot, jsonConfig));
                    }catch (JSONException jsone){
                        throw jsone;
                    }catch (Exception e){
                        throw new JSONException(e);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Construct a JSONArray from an boolean[].<br>
     *
     * @param array
     *            An boolean[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(boolean[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Boolean b = array[i] ? Boolean.TRUE : Boolean.FALSE;
            jsonArray.addValue(b, jsonConfig);
            fireElementAddedEvent(i, b, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an byte[].<br>
     *
     * @param array
     *            An byte[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(byte[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = JSONUtils.transformNumber(new Byte(array[i]));
            jsonArray.addValue(n, jsonConfig);
            fireElementAddedEvent(i, n, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an char[].<br>
     *
     * @param array
     *            An char[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(char[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Character c = new Character(array[i]);
            jsonArray.addValue(c, jsonConfig);
            fireElementAddedEvent(i, c, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an double[].<br>
     *
     * @param array
     *            An double[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(double[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        try{
            for (int i = 0; i < array.length; i++){
                Double d = new Double(array[i]);
                JSONUtils.testValidity(d);
                jsonArray.addValue(d, jsonConfig);
                fireElementAddedEvent(i, d, jsonConfig);
            }
        }catch (JSONException jsone){
            removeInstance(array);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an Enum value.
     *
     * @param e
     *            A enum value.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     * @throws JSONException
     *             If there is a syntax error.
     */
    private static JSONArray _fromArray(Enum e,JsonConfig jsonConfig){
        if (!addInstance(e)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(e);
            }catch (JSONException jsone){
                removeInstance(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException re){
                removeInstance(e);
                JSONException jsone = new JSONException(re);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        if (e != null){
            jsonArray.addValue(e, jsonConfig);
            fireElementAddedEvent(0, jsonArray.get(0), jsonConfig);
        }else{
            JSONException jsone = new JSONException("enum value is null");
            removeInstance(e);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }

        removeInstance(e);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an float[].<br>
     *
     * @param array
     *            An float[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(float[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        try{
            for (int i = 0; i < array.length; i++){
                Float f = new Float(array[i]);
                JSONUtils.testValidity(f);
                jsonArray.addValue(f, jsonConfig);
                fireElementAddedEvent(i, f, jsonConfig);
            }
        }catch (JSONException jsone){
            removeInstance(array);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an int[].<br>
     *
     * @param array
     *            An int[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(int[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = new Integer(array[i]);
            jsonArray.addValue(n, jsonConfig);
            fireElementAddedEvent(i, n, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an long[].<br>
     *
     * @param array
     *            An long[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(long[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = JSONUtils.transformNumber(new Long(array[i]));
            jsonArray.addValue(n, jsonConfig);
            fireElementAddedEvent(i, n, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    // ------------------------------------------------------

    /**
     * From array.
     *
     * @param array
     *            the array
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(Object[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        try{
            for (int i = 0; i < array.length; i++){
                Object element = array[i];
                jsonArray.addValue(element, jsonConfig);
                fireElementAddedEvent(i, jsonArray.get(i), jsonConfig);
            }
        }catch (JSONException jsone){
            removeInstance(array);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }catch (RuntimeException e){
            removeInstance(array);
            JSONException jsone = new JSONException(e);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an short[].<br>
     *
     * @param array
     *            An short[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(short[] array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = JSONUtils.transformNumber(new Short(array[i]));
            jsonArray.addValue(n, jsonConfig);
            fireElementAddedEvent(i, n, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * From collection.
     *
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromCollection(Collection collection,JsonConfig jsonConfig){
        if (!addInstance(collection)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(collection);
            }catch (JSONException jsone){
                removeInstance(collection);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(collection);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        try{
            int i = 0;
            for (Iterator elements = collection.iterator(); elements.hasNext();){
                Object element = elements.next();
                jsonArray.addValue(element, jsonConfig);
                fireElementAddedEvent(i, jsonArray.get(i++), jsonConfig);
            }
        }catch (JSONException jsone){
            removeInstance(collection);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }catch (RuntimeException e){
            removeInstance(collection);
            JSONException jsone = new JSONException(e);
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }

        removeInstance(collection);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * From JSON array.
     *
     * @param array
     *            the array
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromJSONArray(JSONArray array,JsonConfig jsonConfig){
        if (!addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (JSONException jsone){
                removeInstance(array);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(array);
                JSONException jsone = new JSONException(e);
                fireErrorEvent(jsone, jsonConfig);
                throw jsone;
            }
        }
        fireArrayStartEvent(jsonConfig);
        JSONArray jsonArray = new JSONArray();
        int index = 0;
        for (Iterator elements = array.iterator(); elements.hasNext();){
            Object element = elements.next();
            jsonArray.addValue(element, jsonConfig);
            fireElementAddedEvent(index++, element, jsonConfig);
        }

        removeInstance(array);
        fireArrayEndEvent(jsonConfig);
        return jsonArray;
    }

    /**
     * From JSON string.
     *
     * @param string
     *            the string
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromJSONString(JSONString string,JsonConfig jsonConfig){
        return _fromJSONTokener(new JSONTokener(string.toJSONString()), jsonConfig);
    }

    /**
     * From JSON tokener.
     *
     * @param tokener
     *            the tokener
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromJSONTokener(JSONTokener tokener,JsonConfig jsonConfig){

        JSONArray jsonArray = new JSONArray();
        int index = 0;

        try{
            if (tokener.nextClean() != '['){
                throw tokener.syntaxError("A JSONArray text must start with '['");
            }
            fireArrayStartEvent(jsonConfig);
            if (tokener.nextClean() == ']'){
                fireArrayEndEvent(jsonConfig);
                return jsonArray;
            }
            tokener.back();
            for (;;){
                if (tokener.nextClean() == ','){
                    tokener.back();
                    jsonArray.elements.add(JSONNull.getInstance());
                    fireElementAddedEvent(index, jsonArray.get(index++), jsonConfig);
                }else{
                    tokener.back();
                    Object v = tokener.nextValue(jsonConfig);
                    if (!JSONUtils.isFunctionHeader(v)){
                        if (v instanceof String && JSONUtils.mayBeJSON((String) v)){
                            jsonArray.addValue(JSONUtils.DOUBLE_QUOTE + v + JSONUtils.DOUBLE_QUOTE, jsonConfig);
                        }else{
                            jsonArray.addValue(v, jsonConfig);
                        }
                        fireElementAddedEvent(index, jsonArray.get(index++), jsonConfig);
                    }else{
                        // read params if any
                        String params = JSONUtils.getFunctionParams((String) v);
                        // read function text
                        int i = 0;
                        StringBuffer sb = new StringBuffer();
                        for (;;){
                            char ch = tokener.next();
                            if (ch == 0){
                                break;
                            }
                            if (ch == '{'){
                                i++;
                            }
                            if (ch == '}'){
                                i--;
                            }
                            sb.append(ch);
                            if (i == 0){
                                break;
                            }
                        }
                        if (i != 0){
                            throw tokener.syntaxError("Unbalanced '{' or '}' on prop: " + v);
                        }
                        // trim '{' at start and '}' at end
                        String text = sb.toString();
                        text = text.substring(1, text.length() - 1).trim();
                        jsonArray.addValue(new JSONFunction((params != null) ? StringUtils.split(params, ",") : null, text), jsonConfig);
                        fireElementAddedEvent(index, jsonArray.get(index++), jsonConfig);
                    }
                }
                switch (tokener.nextClean()) {
                    case ';':
                    case ',':
                        if (tokener.nextClean() == ']'){
                            fireArrayEndEvent(jsonConfig);
                            return jsonArray;
                        }
                        tokener.back();
                        break;
                    case ']':
                        fireArrayEndEvent(jsonConfig);
                        return jsonArray;
                    default:
                        throw tokener.syntaxError("Expected a ',' or ']'");
                }
            }
        }catch (JSONException jsone){
            fireErrorEvent(jsone, jsonConfig);
            throw jsone;
        }
    }

    /**
     * From string.
     *
     * @param string
     *            the string
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromString(String string,JsonConfig jsonConfig){
        return _fromJSONTokener(new JSONTokener(string), jsonConfig);
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

    // ------------------------------------------------------

    /**
     * The List where the JSONArray's properties are kept.
     */
    private final List elements;

    /**
     * A flag for XML processing.
     */
    private boolean    expandElements;

    /**
     * Construct an empty JSONArray.
     */
    public JSONArray(){
        this.elements = new ArrayList();
    }

    /**
     * 添加.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     */
    @Override
    public void add(int index,Object value){
        add(index, value, new JsonConfig());
    }

    /**
     * 添加.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     */
    public void add(int index,Object value,JsonConfig jsonConfig){
        this.elements.add(index, processValue(value, jsonConfig));
    }

    /**
     * 添加.
     *
     * @param value
     *            the value
     * @return true, if successful
     */
    @Override
    public boolean add(Object value){
        return add(value, new JsonConfig());
    }

    /**
     * 添加.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean add(Object value,JsonConfig jsonConfig){
        element(value, jsonConfig);
        return true;
    }

    /**
     * 添加 all.
     *
     * @param collection
     *            the collection
     * @return true, if successful
     */
    @Override
    public boolean addAll(Collection collection){
        return addAll(collection, new JsonConfig());
    }

    /**
     * 添加 all.
     *
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean addAll(Collection collection,JsonConfig jsonConfig){
        if (collection == null || collection.size() == 0){
            return false;
        }
        for (Iterator i = collection.iterator(); i.hasNext();){
            element(i.next(), jsonConfig);
        }
        return true;
    }

    /**
     * 添加 all.
     *
     * @param index
     *            the index
     * @param collection
     *            the collection
     * @return true, if successful
     */
    @Override
    public boolean addAll(int index,Collection collection){
        return addAll(index, collection, new JsonConfig());
    }

    /**
     * 添加 all.
     *
     * @param index
     *            the index
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean addAll(int index,Collection collection,JsonConfig jsonConfig){
        if (collection == null || collection.size() == 0){
            return false;
        }
        int offset = 0;
        for (Iterator i = collection.iterator(); i.hasNext();){
            this.elements.add(index + (offset++), processValue(i.next(), jsonConfig));
        }
        return true;
    }

    /**
     * 清除.
     */
    @Override
    public void clear(){
        elements.clear();
    }

    /**
     * Compare to.
     *
     * @param obj
     *            the obj
     * @return the int
     */
    @Override
    public int compareTo(Object obj){
        if (obj != null && (obj instanceof JSONArray)){
            JSONArray other = (JSONArray) obj;
            int size1 = size();
            int size2 = other.size();
            if (size1 < size2){
                return -1;
            }else if (size1 > size2){
                return 1;
            }else if (this.equals(other)){
                return 0;
            }
        }
        return -1;
    }

    /**
     * Contains.
     *
     * @param o
     *            the o
     * @return true, if successful
     */
    @Override
    public boolean contains(Object o){
        return contains(o, new JsonConfig());
    }

    /**
     * Contains.
     *
     * @param o
     *            the o
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean contains(Object o,JsonConfig jsonConfig){
        return elements.contains(processValue(o, jsonConfig));
    }

    /**
     * Contains all.
     *
     * @param collection
     *            the collection
     * @return true, if successful
     */
    @Override
    public boolean containsAll(Collection collection){
        return containsAll(collection, new JsonConfig());
    }

    /**
     * Contains all.
     *
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean containsAll(Collection collection,JsonConfig jsonConfig){
        return elements.containsAll(fromObject(collection, jsonConfig));
    }

    /**
     * Remove an element, if present.
     *
     * @param index
     *            the index of the element.
     * @return this.
     */
    public JSONArray discard(int index){
        elements.remove(index);
        return this;
    }

    /**
     * Remove an element, if present.
     *
     * @param o
     *            the o
     * @return this.
     */
    public JSONArray discard(Object o){
        elements.remove(o);
        return this;
    }

    /**
     * Append a boolean value. This increases the array's length by one.
     *
     * @param value
     *            A boolean value.
     * @return this.
     */
    public JSONArray element(boolean value){
        return element(value ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Append a value in the JSONArray, where the value will be a JSONArray which
     * is produced from a Collection.
     *
     * @param value
     *            A Collection value.
     * @return this.
     */
    public JSONArray element(Collection value){
        return element(value, new JsonConfig());
    }

    /**
     * Append a value in the JSONArray, where the value will be a JSONArray which
     * is produced from a Collection.
     *
     * @param value
     *            A Collection value.
     * @param jsonConfig
     *            the json config
     * @return this.
     */
    public JSONArray element(Collection value,JsonConfig jsonConfig){
        if (value instanceof JSONArray){
            elements.add(value);
            return this;
        }else{
            return element(_fromCollection(value, jsonConfig));
        }
    }

    /**
     * Append a double value. This increases the array's length by one.
     *
     * @param value
     *            A double value.
     * @return this.
     * @throws JSONException
     *             if the value is not finite.
     */
    public JSONArray element(double value){
        Double d = new Double(value);
        JSONUtils.testValidity(d);
        return element(d);
    }

    /**
     * Append an int value. This increases the array's length by one.
     *
     * @param value
     *            An int value.
     * @return this.
     */
    public JSONArray element(int value){
        return element(new Integer(value));
    }

    /**
     * Put or replace a boolean value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A boolean value.
     * @return this.
     * @throws JSONException
     *             If the index is negative.
     */
    public JSONArray element(int index,boolean value){
        return element(index, value ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONArray which is
     * produced from a Collection.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A Collection value.
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the value is not
     *             finite.
     */
    public JSONArray element(int index,Collection value){
        return element(index, value, new JsonConfig());
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONArray which is
     * produced from a Collection.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A Collection value.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the value is not
     *             finite.
     */
    public JSONArray element(int index,Collection value,JsonConfig jsonConfig){
        if (value instanceof JSONArray){
            if (index < 0){
                throw new JSONException("JSONArray[" + index + "] not found.");
            }
            if (index < size()){
                elements.set(index, value);
            }else{
                while (index != size()){
                    element(JSONNull.getInstance());
                }
                element(value, jsonConfig);
            }
            return this;
        }else{
            return element(index, _fromCollection(value, jsonConfig));
        }
    }

    /**
     * Put or replace a double value. If the index is greater than the length of
     * the JSONArray, then null elements will be added as necessary to pad it
     * out.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A double value.
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the value is not
     *             finite.
     */
    public JSONArray element(int index,double value){
        return element(index, new Double(value));
    }

    /**
     * Put or replace an int value. If the index is greater than the length of
     * the JSONArray, then null elements will be added as necessary to pad it
     * out.
     *
     * @param index
     *            The subscript.
     * @param value
     *            An int value.
     * @return this.
     * @throws JSONException
     *             If the index is negative.
     */
    public JSONArray element(int index,int value){
        return element(index, new Integer(value));
    }

    /**
     * Put or replace a long value. If the index is greater than the length of
     * the JSONArray, then null elements will be added as necessary to pad it
     * out.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A long value.
     * @return this.
     * @throws JSONException
     *             If the index is negative.
     */
    public JSONArray element(int index,long value){
        return element(index, new Long(value));
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param index
     *            The subscript.
     * @param value
     *            The Map value.
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the the value is an
     *             invalid number.
     */
    public JSONArray element(int index,Map value){
        return element(index, value, new JsonConfig());
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param index
     *            The subscript.
     * @param value
     *            The Map value.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the the value is an
     *             invalid number.
     */
    public JSONArray element(int index,Map value,JsonConfig jsonConfig){
        if (value instanceof JSONObject){
            if (index < 0){
                throw new JSONException("JSONArray[" + index + "] not found.");
            }
            if (index < size()){
                elements.set(index, value);
            }else{
                while (index != size()){
                    element(JSONNull.getInstance());
                }
                element(value, jsonConfig);
            }
            return this;
        }else{
            return element(index, JSONObject.fromObject(value, jsonConfig));
        }
    }

    /**
     * Put or replace an object value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     *
     * @param index
     *            The subscript.
     * @param value
     *            An object value. The value should be a Boolean, Double,
     *            Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *            JSONString or the JSONNull object.
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the the value is an
     *             invalid number.
     */
    public JSONArray element(int index,Object value){
        return element(index, value, new JsonConfig());
    }

    /**
     * Put or replace an object value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.
     *
     * @param index
     *            The subscript.
     * @param value
     *            An object value. The value should be a Boolean, Double,
     *            Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *            JSONString or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the the value is an
     *             invalid number.
     */
    public JSONArray element(int index,Object value,JsonConfig jsonConfig){
        JSONUtils.testValidity(value);
        if (index < 0){
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < size()){
            this.elements.set(index, processValue(value, jsonConfig));
        }else{
            while (index != size()){
                element(JSONNull.getInstance());
            }
            element(value, jsonConfig);
        }
        return this;
    }

    /**
     * Put or replace a String value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A String value.
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the the value is an
     *             invalid number.
     */
    public JSONArray element(int index,String value){
        return element(index, value, new JsonConfig());
    }

    /**
     * Put or replace a String value in the JSONArray. If the index is greater
     * than the length of the JSONArray, then null elements will be added as
     * necessary to pad it out.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param index
     *            The subscript.
     * @param value
     *            A String value.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the index is negative or if the the value is an
     *             invalid number.
     */
    public JSONArray element(int index,String value,JsonConfig jsonConfig){
        if (index < 0){
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < size()){
            if (value == null){
                this.elements.set(index, "");
            }else if (JSONUtils.mayBeJSON(value)){
                try{
                    this.elements.set(index, JSONSerializer.toJSON(value, jsonConfig));
                }catch (JSONException jsone){
                    this.elements.set(index, JSONUtils.stripQuotes(value));
                }
            }else{
                this.elements.set(index, JSONUtils.stripQuotes(value));
            }
        }else{
            while (index != size()){
                element(JSONNull.getInstance());
            }
            element(value, jsonConfig);
        }
        return this;
    }

    /**
     * Append an JSON value. This increases the array's length by one.
     *
     * @param value
     *            An JSON value.
     * @return this.
     */
    public JSONArray element(JSONNull value){
        this.elements.add(value);
        return this;
    }

    /**
     * Append an JSON value. This increases the array's length by one.
     *
     * @param value
     *            An JSON value.
     * @return this.
     */
    public JSONArray element(JSONObject value){
        this.elements.add(value);
        return this;
    }

    /**
     * Append an long value. This increases the array's length by one.
     *
     * @param value
     *            A long value.
     * @return this.
     */
    public JSONArray element(long value){
        return element(JSONUtils.transformNumber(new Long(value)));
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param value
     *            A Map value.
     * @return this.
     */
    public JSONArray element(Map value){
        return element(value, new JsonConfig());
    }

    /**
     * Put a value in the JSONArray, where the value will be a JSONObject which
     * is produced from a Map.
     *
     * @param value
     *            A Map value.
     * @param jsonConfig
     *            the json config
     * @return this.
     */
    public JSONArray element(Map value,JsonConfig jsonConfig){
        if (value instanceof JSONObject){
            elements.add(value);
            return this;
        }else{
            return element(JSONObject.fromObject(value, jsonConfig));
        }
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value
     *            An object value. The value should be a Boolean, Double,
     *            Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *            JSONString or the JSONNull object.
     * @return this.
     */
    public JSONArray element(Object value){
        return element(value, new JsonConfig());
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value
     *            An object value. The value should be a Boolean, Double,
     *            Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *            JSONString or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     */
    public JSONArray element(Object value,JsonConfig jsonConfig){
        return addValue(value, jsonConfig);
    }

    /**
     * Append a String value. This increases the array's length by one.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param value
     *            A String value.
     * @return this.
     */
    public JSONArray element(String value){
        return element(value, new JsonConfig());
    }

    /**
     * Append a String value. This increases the array's length by one.<br>
     * The string may be a valid JSON formatted string, in tha case, it will be
     * transformed to a JSONArray, JSONObject or JSONNull.
     *
     * @param value
     *            A String value.
     * @param jsonConfig
     *            the json config
     * @return this.
     */
    public JSONArray element(String value,JsonConfig jsonConfig){
        if (value == null){
            this.elements.add("");
        }else if (JSONUtils.hasQuotes(value)){
            this.elements.add(value);
        }else if (JSONNull.getInstance().equals(value)){
            this.elements.add(JSONNull.getInstance());
        }else if (JSONUtils.isJsonKeyword(value, jsonConfig)){
            if (jsonConfig.isJavascriptCompliant() && "undefined".equals(value)){
                this.elements.add(JSONNull.getInstance());
            }else{
                this.elements.add(value);
            }
        }else if (JSONUtils.mayBeJSON(value)){
            try{
                this.elements.add(JSONSerializer.toJSON(value, jsonConfig));
            }catch (JSONException jsone){
                this.elements.add(value);
            }
        }else{
            this.elements.add(value);
        }
        return this;
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
        if (obj == this){
            return true;
        }
        if (obj == null){
            return false;
        }

        if (!(obj instanceof JSONArray)){
            return false;
        }

        JSONArray other = (JSONArray) obj;

        if (other.size() != size()){
            return false;
        }

        int max = size();
        for (int i = 0; i < max; i++){
            Object o1 = get(i);
            Object o2 = other.get(i);

            // handle nulls
            if (JSONNull.getInstance().equals(o1)){
                if (JSONNull.getInstance().equals(o2)){
                    continue;
                }else{
                    return false;
                }
            }else{
                if (JSONNull.getInstance().equals(o2)){
                    return false;
                }
            }

            if (o1 instanceof JSONArray && o2 instanceof JSONArray){
                JSONArray e = (JSONArray) o1;
                JSONArray a = (JSONArray) o2;
                if (!a.equals(e)){
                    return false;
                }
            }else{
                if (o1 instanceof String && o2 instanceof JSONFunction){
                    if (!o1.equals(String.valueOf(o2))){
                        return false;
                    }
                }else if (o1 instanceof JSONFunction && o2 instanceof String){
                    if (!o2.equals(String.valueOf(o1))){
                        return false;
                    }
                }else if (o1 instanceof JSONObject && o2 instanceof JSONObject){
                    if (!o1.equals(o2)){
                        return false;
                    }
                }else if (o1 instanceof JSONArray && o2 instanceof JSONArray){
                    if (!o1.equals(o2)){
                        return false;
                    }
                }else if (o1 instanceof JSONFunction && o2 instanceof JSONFunction){
                    if (!o1.equals(o2)){
                        return false;
                    }
                }else{
                    if (o1 instanceof String){
                        if (!o1.equals(String.valueOf(o2))){
                            return false;
                        }
                    }else if (o2 instanceof String){
                        if (!o2.equals(String.valueOf(o1))){
                            return false;
                        }
                    }else{
                        Morpher m1 = JSONUtils.getMorpherRegistry().getMorpherFor(o1.getClass());
                        Morpher m2 = JSONUtils.getMorpherRegistry().getMorpherFor(o2.getClass());
                        if (m1 != null && m1 != IdentityObjectMorpher.getInstance()){
                            if (!o1.equals(JSONUtils.getMorpherRegistry().morph(o1.getClass(), o2))){
                                return false;
                            }
                        }else if (m2 != null && m2 != IdentityObjectMorpher.getInstance()){
                            if (!JSONUtils.getMorpherRegistry().morph(o1.getClass(), o1).equals(o2)){
                                return false;
                            }
                        }else{
                            if (!o1.equals(o2)){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Get the object value associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return An object value.
     */
    @Override
    public Object get(int index){
        /*
         * Object o = opt( index ); if( o == null ){ throw new JSONException(
         * "JSONArray[" + index + "] not found." ); } return o;
         */
        return this.elements.get(index);
    }

    /**
     * Get the boolean value associated with an index. The string values "true"
     * and "false" are converted to boolean.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The truth.
     * @throws JSONException
     *             If there is no value for the index or if the value
     *             is not convertable to boolean.
     */
    public boolean getBoolean(int index){
        Object o = get(index);
        if (o != null){
            if (o.equals(Boolean.FALSE) || (o instanceof String && ((String) o).equalsIgnoreCase("false"))){
                return false;
            }else if (o.equals(Boolean.TRUE) || (o instanceof String && ((String) o).equalsIgnoreCase("true"))){
                return true;
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a Boolean.");
    }

    /**
     * Get the double value associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The value.
     * @throws JSONException
     *             If the key is not found or if the value cannot be
     *             converted to a number.
     */
    public double getDouble(int index){
        Object o = get(index);
        if (o != null){
            try{
                return o instanceof Number ? ((Number) o).doubleValue() : Double.parseDouble((String) o);
            }catch (Exception e){
                throw new JSONException("JSONArray[" + index + "] is not a number.");
            }
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }

    /**
     * Get the int value associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The value.
     * @throws JSONException
     *             If the key is not found or if the value cannot be
     *             converted to a number. if the value cannot be converted to a
     *             number.
     */
    public int getInt(int index){
        Object o = get(index);
        if (o != null){
            return o instanceof Number ? ((Number) o).intValue() : (int) getDouble(index);
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }

    /**
     * Get the JSONArray associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return A JSONArray value.
     * @throws JSONException
     *             If there is no value for the index. or if the value
     *             is not a JSONArray
     */
    public JSONArray getJSONArray(int index){
        Object o = get(index);
        if (o != null && o instanceof JSONArray){
            return (JSONArray) o;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }

    /**
     * Get the JSONObject associated with an index.
     *
     * @param index
     *            subscript
     * @return A JSONObject value.
     * @throws JSONException
     *             If there is no value for the index or if the value
     *             is not a JSONObject
     */
    public JSONObject getJSONObject(int index){
        Object o = get(index);
        if (JSONNull.getInstance().equals(o)){
            return new JSONObject(true);
        }else if (o instanceof JSONObject){
            return (JSONObject) o;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }

    /**
     * Get the long value associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The value.
     * @throws JSONException
     *             If the key is not found or if the value cannot be
     *             converted to a number.
     */
    public long getLong(int index){
        Object o = get(index);
        if (o != null){
            return o instanceof Number ? ((Number) o).longValue() : (long) getDouble(index);
        }
        throw new JSONException("JSONArray[" + index + "] is not a number.");
    }

    /**
     * Get the string associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return A string value.
     * @throws JSONException
     *             If there is no value for the index.
     */
    public String getString(int index){
        Object o = get(index);
        if (o != null){
            return o.toString();
        }
        throw new JSONException("JSONArray[" + index + "] not found.");
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode(){
        int hashcode = 29;

        for (Iterator e = elements.iterator(); e.hasNext();){
            Object element = e.next();
            hashcode += JSONUtils.hashCode(element);
        }
        return hashcode;
    }

    /**
     * Index of.
     *
     * @param o
     *            the o
     * @return the int
     */
    @Override
    public int indexOf(Object o){
        return elements.indexOf(o);
    }

    /**
     * Checks if is array.
     *
     * @return true, if is array
     */
    @Override
    public boolean isArray(){
        return true;
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    @Override
    public boolean isEmpty(){
        return this.elements.isEmpty();
    }

    /**
     * Checks if is a flag for XML processing.
     *
     * @return the a flag for XML processing
     */
    public boolean isExpandElements(){
        return expandElements;
    }

    /**
     * Returns an Iterator for this JSONArray.
     *
     * @return the iterator
     */
    @Override
    public Iterator iterator(){
        return new JSONArrayListIterator();
    }

    /**
     * Make a string from the contents of this JSONArray. The
     * <code>separator</code> string is inserted between each element. Warning:
     * This method assumes that the data structure is acyclical.
     *
     * @param separator
     *            A string that will be inserted between the elements.
     * @return a string.
     * @throws JSONException
     *             If the array contains an invalid number.
     */
    public String join(String separator){
        return join(separator, false);
    }

    /**
     * Make a string from the contents of this JSONArray. The
     * <code>separator</code> string is inserted between each element. Warning:
     * This method assumes that the data structure is acyclical.
     *
     * @param separator
     *            A string that will be inserted between the elements.
     * @param stripQuotes
     *            the strip quotes
     * @return a string.
     * @throws JSONException
     *             If the array contains an invalid number.
     */
    public String join(String separator,boolean stripQuotes){
        int len = size();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i += 1){
            if (i > 0){
                sb.append(separator);
            }
            String value = JSONUtils.valueToString(this.elements.get(i));
            sb.append(stripQuotes ? JSONUtils.stripQuotes(value) : value);
        }
        return sb.toString();
    }

    /**
     * Last index of.
     *
     * @param o
     *            the o
     * @return the int
     */
    @Override
    public int lastIndexOf(Object o){
        return elements.lastIndexOf(o);
    }

    /**
     * List iterator.
     *
     * @return the list iterator
     */
    @Override
    public ListIterator listIterator(){
        return listIterator(0);
    }

    /**
     * List iterator.
     *
     * @param index
     *            the index
     * @return the list iterator
     */
    @Override
    public ListIterator listIterator(int index){
        if (index < 0 || index > size()){
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return new JSONArrayListIterator(index);
    }

    /**
     * Get the optional object value associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return An object value, or null if there is no object at that index.
     */
    public Object opt(int index){
        return (index < 0 || index >= size()) ? null : this.elements.get(index);
    }

    /**
     * Get the optional boolean value associated with an index. It returns false
     * if there is no value at that index, or if the value is not Boolean.TRUE or
     * the String "true".
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The truth.
     */
    public boolean optBoolean(int index){
        return optBoolean(index, false);
    }

    /**
     * Get the optional boolean value associated with an index. It returns the
     * defaultValue if there is no value at that index or if it is not a Boolean
     * or the String "true" or "false" (case insensitive).
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @param defaultValue
     *            A boolean default.
     * @return The truth.
     */
    public boolean optBoolean(int index,boolean defaultValue){
        try{
            return getBoolean(index);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * Get the optional double value associated with an index. NaN is returned if
     * there is no value for the index, or if the value is not a number and
     * cannot be converted to a number.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The value.
     */
    public double optDouble(int index){
        return optDouble(index, Double.NaN);
    }

    /**
     * Get the optional double value associated with an index. The defaultValue
     * is returned if there is no value for the index, or if the value is not a
     * number and cannot be converted to a number.
     *
     * @param index
     *            subscript
     * @param defaultValue
     *            The default value.
     * @return The value.
     */
    public double optDouble(int index,double defaultValue){
        try{
            return getDouble(index);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * Get the optional int value associated with an index. Zero is returned if
     * there is no value for the index, or if the value is not a number and
     * cannot be converted to a number.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The value.
     */
    public int optInt(int index){
        return optInt(index, 0);
    }

    /**
     * Get the optional int value associated with an index. The defaultValue is
     * returned if there is no value for the index, or if the value is not a
     * number and cannot be converted to a number.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @param defaultValue
     *            The default value.
     * @return The value.
     */
    public int optInt(int index,int defaultValue){
        try{
            return getInt(index);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * Get the optional JSONArray associated with an index.
     *
     * @param index
     *            subscript
     * @return A JSONArray value, or null if the index has no value, or if the
     *         value is not a JSONArray.
     */
    public JSONArray optJSONArray(int index){
        Object o = opt(index);
        return o instanceof JSONArray ? (JSONArray) o : null;
    }

    /**
     * Get the optional JSONObject associated with an index. Null is returned if
     * the key is not found, or null if the index has no value, or if the value
     * is not a JSONObject.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return A JSONObject value.
     */
    public JSONObject optJSONObject(int index){
        Object o = opt(index);
        return o instanceof JSONObject ? (JSONObject) o : null;
    }

    /**
     * Get the optional long value associated with an index. Zero is returned if
     * there is no value for the index, or if the value is not a number and
     * cannot be converted to a number.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return The value.
     */
    public long optLong(int index){
        return optLong(index, 0);
    }

    /**
     * Get the optional long value associated with an index. The defaultValue is
     * returned if there is no value for the index, or if the value is not a
     * number and cannot be converted to a number.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @param defaultValue
     *            The default value.
     * @return The value.
     */
    public long optLong(int index,long defaultValue){
        try{
            return getLong(index);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * Get the optional string value associated with an index. It returns an
     * empty string if there is no value at that index. If the value is not a
     * string and is not null, then it is coverted to a string.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return A String value.
     */
    public String optString(int index){
        return optString(index, "");
    }

    /**
     * Get the optional string associated with an index. The defaultValue is
     * returned if the key is not found.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @param defaultValue
     *            The default value.
     * @return A String value.
     */
    public String optString(int index,String defaultValue){
        Object o = opt(index);
        return o != null ? o.toString() : defaultValue;
    }

    /**
     * 删除.
     *
     * @param index
     *            the index
     * @return the object
     */
    @Override
    public Object remove(int index){
        return elements.remove(index);
    }

    /**
     * 删除.
     *
     * @param o
     *            the o
     * @return true, if successful
     */
    @Override
    public boolean remove(Object o){
        return elements.remove(o);
    }

    /**
     * 删除 all.
     *
     * @param collection
     *            the collection
     * @return true, if successful
     */
    @Override
    public boolean removeAll(Collection collection){
        return removeAll(collection, new JsonConfig());
    }

    /**
     * 删除 all.
     *
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean removeAll(Collection collection,JsonConfig jsonConfig){
        return elements.removeAll(fromObject(collection, jsonConfig));
    }

    /**
     * Retain all.
     *
     * @param collection
     *            the collection
     * @return true, if successful
     */
    @Override
    public boolean retainAll(Collection collection){
        return retainAll(collection, new JsonConfig());
    }

    /**
     * Retain all.
     *
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return true, if successful
     */
    public boolean retainAll(Collection collection,JsonConfig jsonConfig){
        return elements.retainAll(fromObject(collection, jsonConfig));
    }

    /**
     * 设置.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @return the object
     */
    @Override
    public Object set(int index,Object value){
        return set(index, value, new JsonConfig());
    }

    /**
     * 设置.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    public Object set(int index,Object value,JsonConfig jsonConfig){
        Object previous = get(index);
        element(index, value, jsonConfig);
        return previous;
    }

    /**
     * 设置 a flag for XML processing.
     *
     * @param expandElements
     *            the new a flag for XML processing
     */
    public void setExpandElements(boolean expandElements){
        this.expandElements = expandElements;
    }

    /**
     * Get the number of elements in the JSONArray, included nulls.
     *
     * @return The length (or size).
     */
    @Override
    public int size(){
        return this.elements.size();
    }

    /**
     * Sub list.
     *
     * @param fromIndex
     *            the from index
     * @param toIndex
     *            the to index
     * @return the list
     */
    @Override
    public List subList(int fromIndex,int toIndex){
        return elements.subList(fromIndex, toIndex);
    }

    /**
     * Produce an Object[] with the contents of this JSONArray.
     *
     * @return the object[]
     */
    @Override
    public Object[] toArray(){
        return this.elements.toArray();
    }

    /**
     * To array.
     *
     * @param array
     *            the array
     * @return the object[]
     */
    @Override
    public Object[] toArray(Object[] array){
        return elements.toArray(array);
    }

    /**
     * Produce a JSONObject by combining a JSONArray of names with the values of
     * this JSONArray.
     *
     * @param names
     *            A JSONArray containing a list of key strings. These will be
     *            paired with the values.
     * @return A JSONObject, or null if there are no names or if this JSONArray
     *         has no values.
     * @throws JSONException
     *             If any of the names are null.
     */
    public JSONObject toJSONObject(JSONArray names){
        if (names == null || names.size() == 0 || size() == 0){
            return null;
        }
        JSONObject jo = new JSONObject();
        for (int i = 0; i < names.size(); i++){
            jo.element(names.getString(i), this.opt(i));
        }
        return jo;
    }

    /**
     * Make a JSON text of this JSONArray. For compactness, no unnecessary
     * whitespace is added. If it is not possible to produce a syntactically
     * correct JSON text then null will be returned instead. This could occur if
     * the array contains an invalid number.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, transmittable representation of the
     *         array.
     */
    @Override
    public String toString(){
        try{
            return '[' + join(",") + ']';
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Make a prettyprinted JSON text of this JSONArray. Warning: This method
     * assumes that the data structure is acyclical.
     *
     * @param indentFactor
     *            The number of spaces to add to each level of
     *            indentation.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>[</code>&nbsp;<small>(left
     *         bracket)</small> and ending with <code>]</code>&nbsp;<small>(right
     *         bracket)</small>.
     * @throws JSONException
     *             the JSON exception
     */
    @Override
    public String toString(int indentFactor){
        if (indentFactor == 0){
            return this.toString();
        }
        return toString(indentFactor, 0);
    }

    /**
     * Make a prettyprinted JSON text of this JSONArray. Warning: This method
     * assumes that the data structure is acyclical.
     *
     * @param indentFactor
     *            The number of spaces to add to each level of
     *            indentation.
     * @param indent
     *            The indention of the top level.
     * @return a printable, displayable, transmittable representation of the
     *         array.
     * @throws JSONException
     *             the JSON exception
     */
    @Override
    public String toString(int indentFactor,int indent){
        int len = size();
        if (len == 0){
            return "[]";
        }
        if (indentFactor == 0){
            return this.toString();
        }
        int i;
        StringBuffer sb = new StringBuffer("[");
        if (len == 1){
            sb.append(JSONUtils.valueToString(this.elements.get(0), indentFactor, indent));
        }else{
            int newindent = indent + indentFactor;
            sb.append('\n');
            for (i = 0; i < len; i += 1){
                if (i > 0){
                    sb.append(",\n");
                }
                for (int j = 0; j < newindent; j += 1){
                    sb.append(' ');
                }
                sb.append(JSONUtils.valueToString(this.elements.get(i), indentFactor, newindent));
            }
            sb.append('\n');
            for (i = 0; i < indent; i += 1){
                sb.append(' ');
            }
            for (i = 0; i < indent; i += 1){
                sb.insert(0, ' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Write the contents of the JSONArray as JSON text to a writer. For
     * compactness, no whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param writer
     *            the writer
     * @return The writer.
     * @throws JSONException
     *             the JSON exception
     */
    @Override
    public Writer write(Writer writer){
        try{
            boolean b = false;
            int len = size();

            writer.write('[');

            for (int i = 0; i < len; i += 1){
                if (b){
                    writer.write(',');
                }
                Object v = this.elements.get(i);
                if (v instanceof JSONObject){
                    ((JSONObject) v).write(writer);
                }else if (v instanceof JSONArray){
                    ((JSONArray) v).write(writer);
                }else{
                    writer.write(JSONUtils.valueToString(v));
                }
                b = true;
            }
            writer.write(']');
            return writer;
        }catch (IOException e){
            throw new JSONException(e);
        }
    }

    /**
     * Adds a String without performing any conversion on it.
     *
     * @param str
     *            the str
     * @return the JSON array
     */
    protected JSONArray addString(String str){
        if (str != null){
            elements.add(str);
        }
        return this;
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value
     *            An object value. The value should be a Boolean, Double,
     *            Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *            JSONString or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     */
    private JSONArray _addValue(Object value,JsonConfig jsonConfig){
        this.elements.add(value);
        return this;
    }

    /**
     * Process value.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    @Override
    protected Object _processValue(Object value,JsonConfig jsonConfig){
        if (value instanceof JSONTokener){
            return _fromJSONTokener((JSONTokener) value, jsonConfig);
        }else if (value != null && Enum.class.isAssignableFrom(value.getClass())){
            return ((Enum) value).name();
        }else if (value instanceof Annotation || (value != null && value.getClass().isAnnotation())){
            throw new JSONException("Unsupported type");
        }
        return super._processValue(value, jsonConfig);
    }

    /**
     * Append an object value. This increases the array's length by one.
     *
     * @param value
     *            An object value. The value should be a Boolean, Double,
     *            Integer, JSONArray, JSONObject, JSONFunction, Long, String,
     *            JSONString or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     */
    private JSONArray addValue(Object value,JsonConfig jsonConfig){
        return _addValue(processValue(value, jsonConfig), jsonConfig);
    }

    /**
     * Process value.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    private Object processValue(Object value,JsonConfig jsonConfig){
        if (value != null){
            JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(value.getClass());
            if (jsonValueProcessor != null){
                value = jsonValueProcessor.processArrayValue(value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)){
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }
        return _processValue(value, jsonConfig);
    }

    /**
     * The Class JSONArrayListIterator.
     */
    private class JSONArrayListIterator implements ListIterator{

        /** The current index. */
        int currentIndex = 0;

        /** The last index. */
        int lastIndex    = -1;

        /**
         * Instantiates a new JSON array list iterator.
         */
        JSONArrayListIterator(){

        }

        /**
         * Instantiates a new JSON array list iterator.
         *
         * @param index
         *            the index
         */
        JSONArrayListIterator(int index){
            currentIndex = index;
        }

        /**
         * Checks for next.
         *
         * @return true, if successful
         */
        @Override
        public boolean hasNext(){
            return currentIndex != size();
        }

        /**
         * Next.
         *
         * @return the object
         */
        @Override
        public Object next(){
            try{
                Object next = get(currentIndex);
                lastIndex = currentIndex++;
                return next;
            }catch (IndexOutOfBoundsException e){
                throw new NoSuchElementException();
            }
        }

        /**
         * 删除.
         */
        @Override
        public void remove(){
            if (lastIndex == -1){
                throw new IllegalStateException();
            }
            try{
                JSONArray.this.remove(lastIndex);
                if (lastIndex < currentIndex){
                    currentIndex--;
                }
                lastIndex = -1;
            }catch (IndexOutOfBoundsException e){
                throw new ConcurrentModificationException();
            }
        }

        /**
         * Checks for previous.
         *
         * @return true, if successful
         */
        @Override
        public boolean hasPrevious(){
            return currentIndex != 0;
        }

        /**
         * Previous.
         *
         * @return the object
         */
        @Override
        public Object previous(){
            try{
                int index = currentIndex - 1;
                Object previous = get(index);
                lastIndex = currentIndex = index;
                return previous;
            }catch (IndexOutOfBoundsException e){
                throw new NoSuchElementException();
            }
        }

        /**
         * Next index.
         *
         * @return the int
         */
        @Override
        public int nextIndex(){
            return currentIndex;
        }

        /**
         * Previous index.
         *
         * @return the int
         */
        @Override
        public int previousIndex(){
            return currentIndex - 1;
        }

        /**
         * 设置.
         *
         * @param obj
         *            the obj
         */
        @Override
        public void set(Object obj){
            if (lastIndex == -1){
                throw new IllegalStateException();
            }

            try{
                JSONArray.this.set(lastIndex, obj);
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }

        /**
         * 添加.
         *
         * @param obj
         *            the obj
         */
        @Override
        public void add(Object obj){
            try{
                JSONArray.this.add(currentIndex++, obj);
                lastIndex = -1;
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }
    }
}
