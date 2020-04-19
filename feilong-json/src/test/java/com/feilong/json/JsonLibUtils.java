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
package com.feilong.json;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * json-lib utils.
 *
 * @author magic_yy
 * @see json-lib.sourceforge.net/
 * @see https://github.com/aalmiray/Json-lib
 */
public class JsonLibUtils{

    /** The config. */
    public static JsonConfig config = new JsonConfig();

    /**
     * array、map、Javabean convert to json string.
     *
     * @param obj
     *            the obj
     * @return the string
     */
    public static String object2json(Object obj){
        return JSONSerializer.toJSON(obj).toString();
    }

    /**
     * json string convert to map with javaBean.
     *
     * @param <T>
     *            the generic type
     * @param jsonStr
     *            the json str
     * @param klass
     *            the klass
     * @return the map< string, t>
     */
    public static <T> Map<String, T> json2map(String jsonStr,Class<T> klass){
        JSONObject jsonObj = JSONObject.fromObject(jsonStr);
        Map<String, T> map = newHashMap();
        Map<String, T> result = (Map<String, T>) JSONObject.toBean(jsonObj, Map.class, map);
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        Morpher dynaMorpher = new BeanMorpher(klass, morpherRegistry);
        morpherRegistry.registerMorpher(dynaMorpher);
        morpherRegistry.registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));
        for (Entry<String, T> entry : result.entrySet()){
            map.put(entry.getKey(), (T) morpherRegistry.morph(klass, entry.getValue()));
        }
        return map;
    }

    /**
     * json string convert to array.
     *
     * @param jsonString
     *            the json string
     * @return the object[]
     */
    public static Object[] json2arrays(String jsonString){
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsonString);
        //    	JSONArray jsonArray = JSONArray.fromObject(jsonString);  
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setArrayMode(JsonConfig.MODE_OBJECT_ARRAY);
        return (Object[]) JSONSerializer.toJava(jsonArray, jsonConfig);
    }

    /**
     * json string convert to list.
     *
     * @param <T>
     *            the generic type
     * @param jsonString
     *            the json string
     * @param pojoClass
     *            the pojo class
     * @return the list< t>
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public static <T> List<T> json2list(String jsonString,Class<T> pojoClass){
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        return JSONArray.toList(jsonArray, pojoClass);
    }
}