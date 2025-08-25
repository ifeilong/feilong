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

import static com.feilong.core.util.MapUtil.get;
import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.feilong.lib.json.util.JSONUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 转成string
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToStringUtil{

    /**
     * json array 开始的字符 {@code ']'}.
     * 
     * @since 4.0.8
     */
    public static final String ARRAY_START_CHAR  = "[";

    /**
     * json array 结束的字符 {@code ']'}.
     * 
     * @since 4.0.8
     */
    public static final String ARRAY_END_CHAR    = "]";

    //---------------------------------------------------------------

    /**
     * json object 开始的字符 <code>'{'</code>.
     * 
     * @since 4.0.8
     */
    public static final String OBJECT_START_CHAR = "{";

    /**
     * json object 结束的字符 {@code '}'}.
     * 
     * @since 4.0.8
     */
    public static final String OBJECT_END_CHAR   = "}";

    //---------------------------------------------------------------

    static String toString(Map<String, Object> jsonKeyAndValueMap,int indentFactor,int indent){
        StringBuilder sb = new StringBuilder(OBJECT_START_CHAR);

        //---------------------------------------------------------------
        if (jsonKeyAndValueMap.size() == 1){

            Entry<String, Object> entry = get(jsonKeyAndValueMap, 0);
            sb.append(quote(entry.getKey()));
            sb.append(": ");
            sb.append(valueToString(entry.getValue(), indentFactor, indent));

        }else{
            //since 3.0.6
            Map<String, Object> useJsonKeyAndValueMap = sortMapByKeyAsc(jsonKeyAndValueMap);
            int newindent = indent + indentFactor;

            for (Map.Entry<String, Object> entry : useJsonKeyAndValueMap.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();

                if (sb.length() > 1){
                    sb.append(",\n");
                }else{
                    sb.append('\n');
                }
                for (int i = 0; i < newindent; i += 1){
                    sb.append(' ');
                }
                sb.append(quote(key));
                sb.append(": ");
                sb.append(valueToString(value, indentFactor, newindent));
            }
            if (sb.length() > 1){
                sb.append('\n');
                for (int i = 0; i < indent; i += 1){
                    sb.append(' ');
                }
            }
            for (int i = 0; i < indent; i += 1){
                sb.insert(0, ' ');
            }
        }

        //---------------------------------------------------------------
        sb.append(OBJECT_END_CHAR);
        return sb.toString();
    }

    static String toString(Map<String, Object> properties){
        StringBuilder sb = new StringBuilder(OBJECT_START_CHAR);

        Iterator<String> keys = properties.keySet().iterator();
        while (keys.hasNext()){
            if (sb.length() > 1){
                sb.append(',');
            }
            Object o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(':');

            sb.append(valueToString(properties.get(o)));
        }

        //---------------------------------------------------------------
        sb.append(OBJECT_END_CHAR);
        return sb.toString();
    }

    static String toString(List<Object> elements,int indentFactor,int indent){
        StringBuilder sb = new StringBuilder(ARRAY_START_CHAR);

        //---------------------------------------------------------------
        int len = elements.size();
        if (len == 1){
            sb.append(valueToString(elements.get(0), indentFactor, indent));
        }else{
            int newindent = indent + indentFactor;
            sb.append('\n');
            for (int i = 0; i < len; i += 1){
                if (i > 0){
                    sb.append(",\n");
                }
                for (int j = 0; j < newindent; j += 1){
                    sb.append(' ');
                }
                sb.append(valueToString(elements.get(i), indentFactor, newindent));
            }
            sb.append('\n');
            for (int i = 0; i < indent; i += 1){
                sb.append(' ');
            }
            for (int i = 0; i < indent; i += 1){
                sb.insert(0, ' ');
            }
        }

        //---------------------------------------------------------------
        sb.append(ARRAY_END_CHAR);
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Make a JSON text of this JSONArray.
     * 
     * For compactness, no unnecessary whitespace is added.
     * 
     * If it is not possible to produce a syntactically correct JSON text then null will be returned instead.
     * This could occur if the array contains an invalid number.
     *
     * @return a printable, displayable, transmittable representation of the array.
     */
    static String toString(List<Object> elements){
        StringBuilder sb = new StringBuilder(ARRAY_START_CHAR);

        int len = elements.size();
        for (int i = 0; i < len; i += 1){
            if (i > 0){
                sb.append(",");
            }
            sb.append(valueToString(elements.get(i)));
        }
        //---------------------------------------------------------------
        sb.append(ARRAY_END_CHAR);
        return sb.toString();
    }

    /**
     * Make a JSON text of an Object value. If the object has an value.toJSONString() method, then that method will be used to produce the
     * JSON text. The method is required to produce a strictly conforming text.
     * If the object does not contain a toJSONString method (which is the most common case), then a text will be produced by the rules.
     *
     * @param value
     *            The value to be serialized.
     * @return a printable, displayable, transmittable representation of the object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
     */
    private static String valueToString(Object value){
        if (value == null || JSONUtils.isNull(value)){
            return "null";
        }
        if (value instanceof Number){
            return JSONUtils.numberToString((Number) value);
        }
        if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray){
            return value.toString();
        }
        return quote(value.toString());
    }

    /**
     * Make a prettyprinted JSON text of an object value.
     *
     * @param value
     *            The value to be serialized.
     * @param indentFactor
     *            The number of spaces to add to each level of
     *            indentation.
     * @param indent
     *            The indentation of the top level.
     * @return a printable, displayable, transmittable representation of the object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
     */
    private static String valueToString(Object value,int indentFactor,int indent){
        if (value == null || JSONUtils.isNull(value)){
            return "null";
        }
        if (value instanceof Number){
            return JSONUtils.numberToString((Number) value);
        }
        if (value instanceof Boolean){
            return value.toString();
        }

        //---------------------------------------------------------------
        if (value instanceof JSONObject){
            return ((JSONObject) value).toString(indentFactor, indent);
        }
        if (value instanceof JSONArray){
            return ((JSONArray) value).toString(indentFactor, indent);
        }
        return quote(value.toString());
    }

    /**
     * Produce a string in double quotes with backslash sequences in all the right places. A backslash will be inserted within </, allowing
     * JSON text to be delivered in HTML. In JSON text, a string cannot contain a control character or an unescaped quote or backslash.<br>
     * <strong>CAUTION:</strong> if <code>string</code> represents a javascript function, translation of characters will not take place.
     * This
     * will produce a non-conformant JSON text.
     *
     * @param string
     *            A String
     * @return A String correctly formatted for insertion in a JSON text.
     */
    private static String quote(String string){
        if (string == null || string.length() == 0){
            return "\"\"";
        }

        //---------------------------------------------------------------

        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len * 2);
        String t;
        char[] chars = string.toCharArray();
        char[] buffer = new char[1030];
        int bufferIndex = 0;
        sb.append('"');
        for (i = 0; i < len; i += 1){
            if (bufferIndex > 1024){
                sb.append(buffer, 0, bufferIndex);
                bufferIndex = 0;
            }
            b = c;
            c = chars[i];
            switch (c) {
                case '\\':
                case '"':
                    buffer[bufferIndex++] = '\\';
                    buffer[bufferIndex++] = c;
                    break;
                case '/':
                    if (b == '<'){
                        buffer[bufferIndex++] = '\\';
                    }
                    buffer[bufferIndex++] = c;
                    break;
                default:
                    if (c < ' '){
                        switch (c) {
                            case '\b':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'b';
                                break;
                            case '\t':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 't';
                                break;
                            case '\n':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'n';
                                break;
                            case '\f':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'f';
                                break;
                            case '\r':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'r';
                                break;
                            default:
                                t = "000" + Integer.toHexString(c);
                                int tLength = t.length();
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'u';
                                buffer[bufferIndex++] = t.charAt(tLength - 4);
                                buffer[bufferIndex++] = t.charAt(tLength - 3);
                                buffer[bufferIndex++] = t.charAt(tLength - 2);
                                buffer[bufferIndex++] = t.charAt(tLength - 1);
                        }
                    }else{
                        buffer[bufferIndex++] = c;
                    }
            }
        }
        sb.append(buffer, 0, bufferIndex);
        sb.append('"');
        return sb.toString();
    }
}
