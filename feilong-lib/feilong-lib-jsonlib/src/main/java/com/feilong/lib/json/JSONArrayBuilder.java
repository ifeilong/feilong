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

import static com.feilong.core.lang.ObjectUtil.defaultIfNull;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.feilong.lib.json.util.CycleSetUtil;
import com.feilong.lib.json.util.JSONUtils;

/**
 * 用来生成 {@link JSONArray}.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class JSONArrayBuilder{

    /** Don't let anyone instantiate this class. */
    private JSONArrayBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * Creates a JSONArray.
     * 
     * <p>
     * Inspects the object type to call the correct JSONArray factory method.
     * </p>
     * 
     * <p>
     * Accepts JSON formatted strings, arrays, Collections and Enums.
     * </p>
     *
     * @param object
     *            the object
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    public static JSONArray build(Object object,JsonConfig jsonConfig){
        JsonConfig useJsonConfig = defaultIfNull(jsonConfig, new JsonConfig());

        if (object instanceof String){
            JSONTokener jsonTokener = new JSONTokener((String) object);
            return JSONTokenerParser.toJSONArray(jsonTokener, useJsonConfig);
        }
        if (object instanceof JSONTokener){
            return JSONTokenerParser.toJSONArray((JSONTokener) object, useJsonConfig);
        }

        //---------------------------------------------------------------
        if (object instanceof JSONArray){
            return fromJSONArray((JSONArray) object, useJsonConfig);
        }
        if (object instanceof Collection){
            return fromCollection((Collection<?>) object, useJsonConfig);
        }

        //---------------------------------------------------------------
        if (object != null && object.getClass().isArray()){
            Class<?> type = object.getClass().getComponentType();
            if (!type.isPrimitive()){
                return fromArray((Object[]) object, useJsonConfig);
            }
            //---------------------------------------------------------------
            if (type == Boolean.TYPE){
                return fromArray((boolean[]) object, useJsonConfig);
            }else if (type == Byte.TYPE){
                return fromArray((byte[]) object, useJsonConfig);
            }else if (type == Short.TYPE){
                return fromArray((short[]) object, useJsonConfig);
            }else if (type == Integer.TYPE){
                return fromArray((int[]) object, useJsonConfig);
            }else if (type == Long.TYPE){
                return fromArray((long[]) object, useJsonConfig);
            }else if (type == Float.TYPE){
                return fromArray((float[]) object, useJsonConfig);
            }else if (type == Double.TYPE){
                return fromArray((double[]) object, useJsonConfig);
            }else if (type == Character.TYPE){
                return fromArray((char[]) object, useJsonConfig);
            }

            throw new JSONException("Unsupported type");
        }

        //---------------------------------------------------------------
        if (JSONUtils.isBoolean(object) || //
                        JSONUtils.isNumber(object) || //
                        JSONUtils.isNull(object) || //
                        JSONUtils.isString(object) || //
                        object instanceof JSON){
            return new JSONArray().addValue(object, useJsonConfig);
        }

        //---------------------------------------------------------------
        if (object instanceof Enum){
            return fromArray((Enum) object, useJsonConfig);
        }
        if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())){
            throw new JSONException("Unsupported type");
        }

        //---------------------------------------------------------------
        if (JSONUtils.isObject(object)){
            return new JSONArray().addValue(JSONObjectBuilder.build(object, useJsonConfig));
        }
        throw new JSONException("Unsupported type");
    }

    //---------------------------------------------------------------

    /**
     * Construct a JSONArray from an boolean[].<br>
     *
     * @param array
     *            An boolean[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray fromArray(boolean[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    jsonArray.addValue(array[i], jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(byte[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    Number n = JSONUtils.transformNumber(array[i]);
                    jsonArray.addValue(n, jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(char[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    jsonArray.addValue(array[i], jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(double[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    Double d = array[i];
                    JSONUtils.testValidity(d);
                    jsonArray.addValue(d, jsonConfig);
                }
            }
        });
    }

    /**
     * Construct a JSONArray from an Enum value.
     *
     * @param enumType
     *            A enum value.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     * @throws JSONException
     *             If there is a syntax error.
     */
    private static JSONArray fromArray(Enum<?> enumType,JsonConfig jsonConfig){
        return build(enumType, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                if (enumType != null){
                    jsonArray.addValue(enumType, jsonConfig);
                }else{
                    throw new JSONException("enum value is null");
                }
            }
        });
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
    private static JSONArray fromArray(float[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    float f = array[i];
                    JSONUtils.testValidity(f);
                    jsonArray.addValue(f, jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(int[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    jsonArray.addValue(array[i], jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(long[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    jsonArray.addValue(JSONUtils.transformNumber(array[i]), jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(Object[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    jsonArray.addValue(array[i], jsonConfig);
                }
            }
        });
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
    private static JSONArray fromArray(short[] array,JsonConfig jsonConfig){
        return build(array, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (int i = 0; i < array.length; i++){
                    jsonArray.addValue(JSONUtils.transformNumber(array[i]), jsonConfig);
                }
            }
        });
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
    static JSONArray fromCollection(Collection<?> collection,JsonConfig jsonConfig){
        return build(collection, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (Object element : collection){
                    jsonArray.addValue(element, jsonConfig);
                }
            }
        });
    }

    /**
     * From JSON array.
     *
     * @param inputJsonArray
     *            the array
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray fromJSONArray(JSONArray inputJsonArray,JsonConfig jsonConfig){
        return build(inputJsonArray, jsonConfig, new JsonHook<JSONArray>(){

            @Override
            public void handle(JSONArray jsonArray){
                for (Object element : inputJsonArray.elementList){
                    jsonArray.addValue(element, jsonConfig);
                }
            }
        });
    }

    private static JSONArray build(Object array,JsonConfig jsonConfig,JsonHook<JSONArray> jsonHook) throws JSONException{
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw new JSONException("", e);
            }
        }

        //---------------------------------------------------------------
        JSONArray jsonArray = new JSONArray();
        try{
            jsonHook.handle(jsonArray);
            //---------------------------------------------------------------
            CycleSetUtil.removeInstance(array);
            return jsonArray;
        }catch (Exception e){
            CycleSetUtil.removeInstance(array);
            throw new JSONException("", e);
        }
    }

}
