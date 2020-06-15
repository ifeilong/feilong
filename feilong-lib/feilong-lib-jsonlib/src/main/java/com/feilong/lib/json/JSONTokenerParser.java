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

import java.util.Collection;

import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.PropertyFilter;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.3
 */
public class JSONTokenerParser{

    static JSONObject toJSONObject(JSONTokener jsonTokener,JsonConfig jsonConfig){
        if (jsonTokener.nextClean() != '{'){
            throw jsonTokener.syntaxError("A JSONObject text must begin with '{'");
        }

        Collection<String> exclusions = jsonConfig.getMergedExcludes();
        PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

        //---------------------------------------------------------------

        JSONObject jsonObject = new JSONObject();

        String key;
        for (;;){
            char c = jsonTokener.nextClean();
            switch (c) {
                case 0:
                    throw jsonTokener.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return jsonObject;
                default:
                    jsonTokener.back();
                    key = jsonTokener.nextValue(jsonConfig).toString();
            }
            //---------------------------------------------------------------

            //The key is followed by ':'. 
            c = jsonTokener.nextClean();
            if (c != ':'){
                throw jsonTokener.syntaxError("Expected a ':' after a key");
            }

            //---------------------------------------------------------------
            char peek = jsonTokener.peek();

            if (exclusions.contains(key)){
                switch (jsonTokener.nextClean()) {
                    case ';':
                    case ',':
                        if (jsonTokener.nextClean() == '}'){
                            return jsonObject;
                        }
                        jsonTokener.back();
                        break;
                    case '}':
                        return jsonObject;
                    default:
                        throw jsonTokener.syntaxError("Expected a ',' or '}'");
                }
                continue;
            }

            //---------------------------------------------------------------
            boolean quoted = peek == '"' || peek == '\'';
            Object value = jsonTokener.nextValue(jsonConfig);
            if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(jsonTokener, key, value)){
                if (quoted && value instanceof String && (JSONUtils.mayBeJSON((String) value))){
                    value = JSONUtils.DOUBLE_QUOTE + value + JSONUtils.DOUBLE_QUOTE;
                }
                if (jsonObject.properties.containsKey(key)){
                    jsonObject.accumulate(key, value, jsonConfig);
                }else{
                    jsonObject.put(key, value, jsonConfig);
                }
            }
            //---------------------------------------------------------------

            // Pairs are separated by ','. We will also tolerate ';'.
            switch (jsonTokener.nextClean()) {
                case ';':
                case ',':
                    if (jsonTokener.nextClean() == '}'){
                        return jsonObject;
                    }
                    jsonTokener.back();
                    break;
                case '}':
                    return jsonObject;
                default:
                    throw jsonTokener.syntaxError("Expected a ',' or '}'");
            }
        }
    }

    /**
     * From JSON tokener.
     *
     * @param jsonTokener
     *            the tokener
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    static JSONArray toJSONArray(JSONTokener jsonTokener,JsonConfig jsonConfig){
        if (jsonTokener.nextClean() != '['){
            throw jsonTokener.syntaxError("A JSONArray text must start with '['");
        }
        if (jsonTokener.nextClean() == ']'){
            return new JSONArray();
        }

        //---------------------------------------------------------------
        JSONArray jsonArray = new JSONArray();
        jsonTokener.back();

        for (;;){
            if (jsonTokener.nextClean() == ','){
                jsonTokener.back();
                jsonArray.elementList.add(JSONNull.getInstance());
            }else{
                jsonTokener.back();
                Object v = jsonTokener.nextValue(jsonConfig);
                if (v instanceof String && JSONUtils.mayBeJSON((String) v)){
                    jsonArray.addValue(JSONUtils.DOUBLE_QUOTE + v + JSONUtils.DOUBLE_QUOTE, jsonConfig);
                }else{
                    jsonArray.addValue(v, jsonConfig);
                }

            }

            //---------------------------------------------------------------
            switch (jsonTokener.nextClean()) {
                case ';':
                case ',':
                    if (jsonTokener.nextClean() == ']'){
                        return jsonArray;
                    }
                    jsonTokener.back();
                    break;
                case ']':
                    return jsonArray;
                default:
                    throw jsonTokener.syntaxError("Expected a ',' or ']'");
            }
        }
    }
}
