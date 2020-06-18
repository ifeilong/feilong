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

package com.feilong.lib.json.util;

import static com.feilong.lib.json.ToStringUtil.ARRAY_END;
import static com.feilong.lib.json.ToStringUtil.ARRAY_START;
import static com.feilong.lib.json.ToStringUtil.OBJECT_END;
import static com.feilong.lib.json.ToStringUtil.OBJECT_START;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.feilong.lib.ezmorph.MorphUtils;
import com.feilong.lib.ezmorph.MorpherRegistry;
import com.feilong.lib.ezmorph.bean.MorphDynaBean;
import com.feilong.lib.ezmorph.bean.MorphDynaClass;
import com.feilong.lib.json.JSON;
import com.feilong.lib.json.JSONArray;
import com.feilong.lib.json.JSONException;
import com.feilong.lib.json.JSONNull;
import com.feilong.lib.json.JSONObject;
import com.feilong.lib.json.JsonConfig;

/**
 * Provides useful methods on java objects and JSON values.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 * @version 7
 */
public final class JSONUtils{

    /** Constant for char ". */
    public static final String           DOUBLE_QUOTE    = "\"";

    /** Constant for char '. */
    private static final String          SINGLE_QUOTE    = "'";

    //---------------------------------------------------------------

    /** The Constant morpherRegistry. */
    private static final MorpherRegistry morpherRegistry = new MorpherRegistry();

    //---------------------------------------------------------------

    static{
        // register standard morphers
        MorphUtils.registerStandardMorphers(morpherRegistry);
    }

    //---------------------------------------------------------------

    /**
     * Returns the singleton MorpherRegistry.
     *
     * @return the morpher registry
     */
    public static MorpherRegistry getMorpherRegistry(){
        return morpherRegistry;
    }
    //---------------------------------------------------------------

    /**
     * Returns the inner-most component type of an Array.
     *
     * @param type
     *            the type
     * @return the inner component type
     */
    public static Class<?> getInnerComponentType(Class<?> type){
        if (!type.isArray()){
            return type;
        }
        return getInnerComponentType(type.getComponentType());
    }

    //---------------------------------------------------------------

    /**
     * Creates a Map with all the properties of the JSONObject.
     *
     * @param jsonObject
     *            the json object
     * @return the properties
     */
    public static Map<String, Class<?>> getKeyAndTypeMap(JSONObject jsonObject){
        Map<String, Class<?>> properties = new HashMap<>();

        Set<String> keys = jsonObject.keys();
        for (String key : keys){
            properties.put(key, getTypeClass(jsonObject.get(key)));
        }
        return properties;
    }

    //---------------------------------------------------------------

    /**
     * Returns the JSON type.<br>
     * Values are Object, String, Boolean, Number(subclasses) &amp; JSONFunction.
     *
     * @param obj
     *            the obj
     * @return the type class
     */
    private static Class<?> getTypeClass(Object obj){
        if (isNull(obj)){
            return Object.class;
        }
        if (isArray(obj)){
            return List.class;
        }
        if (isBoolean(obj)){
            return Boolean.class;
        }

        //---------------------------------------------------------------
        if (isNumber(obj)){
            Number n = (Number) obj;
            if (isInteger(n)){
                return Integer.class;
            }else if (isLong(n)){
                return Long.class;
            }else if (isFloat(n)){
                return Float.class;
            }else if (isBigInteger(n)){
                return BigInteger.class;
            }else if (isBigDecimal(n)){
                return BigDecimal.class;
            }else if (isDouble(n)){
                return Double.class;
            }else{
                throw new JSONException("Unsupported type");
            }
        }
        if (isString(obj)){
            return String.class;
        }
        if (isObject(obj)){
            return Object.class;
        }
        throw new JSONException("Unsupported type");
    }

    /**
     * Returns the hashcode of value.<br>
     * If null it will return JSONNull.getInstance().hashCode().<br>
     * If value is JSON, JSONFunction or String, value.hashCode is returned,
     * otherwise the value is transformed to a String an its hashcode is
     * returned.
     *
     * @param value
     *            the value
     * @return the int
     */
    public static int hashCode(Object value){
        if (value == null){
            return JSONNull.getInstance().hashCode();
        }
        if (value instanceof JSON || value instanceof String){
            return value.hashCode();
        }
        return String.valueOf(value).hashCode();
    }

    /**
     * Tests if a Class represents an array or Collection.
     *
     * @param clazz
     *            the clazz
     * @return true, if is array
     */
    public static boolean isArray(Class<?> clazz){
        return clazz != null && //
                        (clazz.isArray() || Collection.class.isAssignableFrom(clazz) || (JSONArray.class.isAssignableFrom(clazz)));
    }

    /**
     * Tests if obj is an array or Collection.
     *
     * @param obj
     *            the obj
     * @return true, if is array
     */
    public static boolean isArray(Object obj){
        return (obj != null && obj.getClass().isArray()) || //
                        obj instanceof Collection || //
                        obj instanceof JSONArray;

    }

    /**
     * Tests if Class represents a Boolean or primitive boolean.
     *
     * @param clazz
     *            the clazz
     * @return true, if is boolean
     */
    public static boolean isBoolean(Class<?> clazz){
        return clazz != null && //
                        (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
    }

    /**
     * Tests if obj is a Boolean or primitive boolean.
     *
     * @param obj
     *            the obj
     * @return true, if is boolean
     */
    public static boolean isBoolean(Object obj){
        return obj instanceof Boolean || //
                        (obj != null && obj.getClass() == Boolean.TYPE);
    }

    /**
     * Returns trus if str represents a valid Java identifier.
     *
     * @param str
     *            the str
     * @return true, if is java identifier
     */
    public static boolean isJavaIdentifier(String str){
        if (str.length() == 0 || !Character.isJavaIdentifierStart(str.charAt(0))){
            return false;
        }
        for (int i = 1; i < str.length(); i++){
            if (!Character.isJavaIdentifierPart(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if the obj is a javaScript null.
     *
     * @param obj
     *            the obj
     * @return true, if is null
     */
    public static boolean isNull(Object obj){
        if (obj instanceof JSONObject){
            return ((JSONObject) obj).isNullObject();
        }
        return JSONNull.getInstance().equals(obj);
    }

    /**
     * Tests if Class represents a primitive number or wrapper.<br>
     *
     * @param clazz
     *            the clazz
     * @return true, if is number
     */
    public static boolean isNumber(Class<?> clazz){
        return clazz != null && //
                        (Byte.TYPE.isAssignableFrom(clazz) //
                                        || Short.TYPE.isAssignableFrom(clazz)//
                                        || Integer.TYPE.isAssignableFrom(clazz)//
                                        || Long.TYPE.isAssignableFrom(clazz) //
                                        || Float.TYPE.isAssignableFrom(clazz)//
                                        || Double.TYPE.isAssignableFrom(clazz)//
                                        || Number.class.isAssignableFrom(clazz)
                        //
                        );
    }

    /**
     * Tests if obj is a primitive number or wrapper.<br>
     *
     * @param obj
     *            the obj
     * @return true, if is number
     */
    public static boolean isNumber(Object obj){
        if (null != obj){
            Class<? extends Object> class1 = obj.getClass();
            if (class1 == Byte.TYPE || //
                            class1 == Short.TYPE || //
                            class1 == Integer.TYPE || //
                            class1 == Long.TYPE || //
                            class1 == Float.TYPE || //
                            class1 == Double.TYPE){
                return true;
            }
        }
        return obj instanceof Number;
    }

    /**
     * Tests if obj is not a boolean, number, string or array.
     *
     * @param obj
     *            the obj
     * @return true, if is object
     */
    public static boolean isObject(Object obj){
        return !isNumber(obj) && //
                        !isString(obj) && //
                        !isBoolean(obj) && //
                        !isArray(obj) || isNull(obj);
    }

    /**
     * Tests if Class represents a String or a char.
     *
     * @param clazz
     *            the clazz
     * @return true, if is string
     */
    public static boolean isString(Class<?> clazz){
        return clazz != null && (String.class.isAssignableFrom(clazz)
                        || (Character.TYPE.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)));
    }

    /**
     * Tests if obj is a String or a char.
     *
     * @param obj
     *            the obj
     * @return true, if is string
     */
    public static boolean isString(Object obj){
        return obj instanceof String || //
                        obj instanceof Character || //
                        obj != null && (obj.getClass() == Character.TYPE || //
                                        String.class.isAssignableFrom(obj.getClass()));
    }

    /**
     * Tests if the String possibly represents a valid JSON String.<br>
     * Valid JSON strings are:
     * <ul>
     * <li>"null"</li>
     * <li>starts with "[" and ends with "]"</li>
     * <li>starts with "{" and ends with "}"</li>
     * </ul>
     *
     * @param string
     *            the string
     * @return true, if successful
     */
    public static boolean mayBeJSON(String string){
        return string != null && //
                        ("null".equals(string) || //
                                        (string.startsWith(OBJECT_START) && string.endsWith(OBJECT_END)) || //
                                        (string.startsWith(ARRAY_START) && string.endsWith(ARRAY_END)));
    }

    //---------------------------------------------------------------

    /**
     * Creates a new MorphDynaBean from a JSONObject. The MorphDynaBean will have
     * all the properties of the original JSONObject with the most accurate type.
     * Values of properties are not copied.
     *
     * @param jsonObject
     *            the json object
     * @param jsonConfig
     *            the json config
     * @return the dyna bean
     */
    public static DynaBean newDynaBean(JSONObject jsonObject,JsonConfig jsonConfig){
        Map<String, Class<?>> keyAndTypeMap = getKeyAndTypeMap(jsonObject);

        for (Map.Entry<String, Class<?>> entry : keyAndTypeMap.entrySet()){
            String key = entry.getKey();

            if (!isJavaIdentifier(key)){
                String parsedKey = jsonConfig.getJavaIdentifierTransformer().transformToJavaIdentifier(key);
                if (parsedKey.compareTo(key) != 0){
                    keyAndTypeMap.put(parsedKey, keyAndTypeMap.remove(key));
                }
            }
        }

        //---------------------------------------------------------------
        MorphDynaClass dynaClass = new MorphDynaClass(keyAndTypeMap);
        MorphDynaBean dynaBean = null;
        try{
            dynaBean = (MorphDynaBean) dynaClass.newInstance();
            dynaBean.setDynaBeanClass(dynaClass);
        }catch (Exception e){
            throw new JSONException("", e);
        }
        return dynaBean;
    }

    //---------------------------------------------------------------

    /**
     * Produce a string from a Number.
     *
     * @param n
     *            A Number
     * @return A String.
     * @throws JSONException
     *             If n is a non-finite number.
     */
    public static String numberToString(Number n){
        if (n == null){
            throw new JSONException("Null pointer");
        }
        testValidity(n);

        // Shave off trailing zeros and decimal point, if possible.
        String s = n.toString();
        if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0){
            while (s.endsWith("0")){
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")){
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * Strips any single-quotes or double-quotes from both sides of the string.
     *
     * @param input
     *            the input
     * @return the string
     */
    public static String stripQuotes(String input){
        if (input.length() < 2){
            return input;
        }
        if (input.startsWith(SINGLE_QUOTE) && input.endsWith(SINGLE_QUOTE)){
            return input.substring(1, input.length() - 1);
        }
        if (input.startsWith(DOUBLE_QUOTE) && input.endsWith(DOUBLE_QUOTE)){
            return input.substring(1, input.length() - 1);
        }
        return input;
    }

    /**
     * Returns true if the input has single-quotes or double-quotes at both sides.
     *
     * @param input
     *            the input
     * @return true, if successful
     */
    public static boolean hasQuotes(String input){
        if (input == null || input.length() < 2){
            return false;
        }
        return (input.startsWith(SINGLE_QUOTE) && input.endsWith(SINGLE_QUOTE))//
                        || (input.startsWith(DOUBLE_QUOTE) && input.endsWith(DOUBLE_QUOTE));
    }

    /**
     * Checks if is json keyword.
     *
     * @param input
     *            the input
     * @return true, if is json keyword
     */
    public static boolean isJsonKeyword(String input){
        if (input == null){
            return false;
        }
        return "null".equals(input) || "true".equals(input) || "false".equals(input);
    }

    /**
     * Throw an exception if the object is an NaN or infinite number.
     *
     * @param o
     *            The object to test.
     * @throws JSONException
     *             If o is a non-finite number.
     */
    public static void testValidity(Object o){
        if (o != null){
            if (o instanceof Double){
                if (((Double) o).isInfinite() || ((Double) o).isNaN()){
                    throw new JSONException("JSON does not allow non-finite numbers");
                }
            }
            if (o instanceof Float){
                if (((Float) o).isInfinite() || ((Float) o).isNaN()){
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
            if (o instanceof BigDecimal || o instanceof BigInteger){
                // ok
                return;
            }
        }
    }

    /**
     * Transforms a Number into a valid javascript number.<br>
     * Float gets promoted to Double.<br>
     * Byte and Short get promoted to Integer.<br>
     * Long gets downgraded to Integer if possible.<br>
     *
     * @param input
     *            the input
     * @return the number
     */
    public static Number transformNumber(Number input){
        if (input instanceof Float){
            return new Double(input.toString());
        }
        if (input instanceof Short){
            return input.intValue();
        }
        if (input instanceof Byte){
            return input.intValue();
        }
        if (input instanceof Long){
            Long max = new Long(Integer.MAX_VALUE);
            if (input.longValue() <= max.longValue() && input.longValue() >= Integer.MIN_VALUE){
                return input.intValue();
            }
        }

        return input;
    }

    //---------------------------------------------------------------

    /**
     * Finds out if n represents a BigInteger.
     *
     * @param n
     *            the n
     * @return true if n is instanceOf BigInteger or the literal value can be
     *         evaluated as a BigInteger
     */
    @SuppressWarnings("unused")
    private static boolean isBigDecimal(Number n){
        if (n instanceof BigDecimal){
            return true;
        }
        try{
            new BigDecimal(String.valueOf(n));
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Finds out if n represents a BigInteger.
     *
     * @param n
     *            the n
     * @return true if n is instanceOf BigInteger or the literal value can be
     *         evaluated as a BigInteger
     */
    @SuppressWarnings("unused")
    private static boolean isBigInteger(Number n){
        if (n instanceof BigInteger){
            return true;
        }
        try{
            new BigInteger(String.valueOf(n));
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Finds out if n represents a Double.
     *
     * @param n
     *            the n
     * @return true if n is instanceOf Double or the literal value can be
     *         evaluated as a Double.
     */
    private static boolean isDouble(Number n){
        if (n instanceof Double){
            return true;
        }
        try{
            double d = Double.parseDouble(String.valueOf(n));
            return !Double.isInfinite(d);
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Finds out if n represents a Float.
     *
     * @param n
     *            the n
     * @return true if n is instanceOf Float or the literal value can be
     *         evaluated as a Float.
     */
    private static boolean isFloat(Number n){
        if (n instanceof Float){
            return true;
        }
        try{
            float f = Float.parseFloat(String.valueOf(n));
            return !Float.isInfinite(f);
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Finds out if n represents an Integer.
     *
     * @param n
     *            the n
     * @return true if n is instanceOf Integer or the literal value can be
     *         evaluated as an Integer.
     */
    private static boolean isInteger(Number n){
        if (n instanceof Integer){
            return true;
        }
        try{
            Integer.parseInt(String.valueOf(n));
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Finds out if n represents a Long.
     *
     * @param n
     *            the n
     * @return true if n is instanceOf Long or the literal value can be evaluated
     *         as a Long.
     */
    private static boolean isLong(Number n){
        if (n instanceof Long){
            return true;
        }
        try{
            Long.parseLong(String.valueOf(n));
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    //---------------------------------------------------------------

    /**
     * Instantiates a new JSON utils.
     */
    private JSONUtils(){
        super();
    }
}