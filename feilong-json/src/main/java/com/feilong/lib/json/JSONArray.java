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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.feilong.lib.ezmorph.Morpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.util.JSONUtils;

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
public final class JSONArray implements JSON{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1663435868052425703L;

    //---------------------------------------------------------------

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
        return JSONArrayBuilder.fromObject(object, jsonConfig);
    }

    //---------------------------------------------------------------

    /**
     * The List where the JSONArray's properties are kept.
     */
    final List elements;

    /**
     * Construct an empty JSONArray.
     */
    public JSONArray(){
        this.elements = new ArrayList<>();
    }

    /**
     * 添加.
     *
     * @param index
     *            the index
     * @param value
     *            the value
     */
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
    private void add(int index,Object value,JsonConfig jsonConfig){
        this.elements.add(index, ProcessValueUtil.processArrayValue(value, jsonConfig));
    }

    /**
     * 清除.
     */
    public void clear(){
        elements.clear();
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
    private JSONArray element(int index,Object value,JsonConfig jsonConfig){
        JSONUtils.testValidity(value);
        if (index < 0){
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < size()){
            this.elements.set(index, ProcessValueUtil.processArrayValue(value, jsonConfig));
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
    private JSONArray element(JSONNull value){
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
    JSONArray element(JSONObject value){
        this.elements.add(value);
        return this;
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
    JSONArray element(Object value){
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
    JSONArray element(Object value,JsonConfig jsonConfig){
        return addValue(value, jsonConfig);
    }

    /**
     * Get the object value associated with an index.
     *
     * @param index
     *            The index must be between 0 and size() - 1.
     * @return An object value.
     */
    public Object get(int index){
        return this.elements.get(index);
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
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty(){
        return this.elements.isEmpty();
    }

    /**
     * Returns an Iterator for this JSONArray.
     *
     * @return the iterator
     */
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
        int len = size();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i += 1){
            if (i > 0){
                sb.append(separator);
            }
            String value = JSONUtils.valueToString(this.elements.get(i));
            //sb.append(stripQuotes ? JSONUtils.stripQuotes(value) : value);
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 删除.
     *
     * @param index
     *            the index
     * @return the object
     */
    public Object remove(int index){
        return elements.remove(index);
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
    public Object set(int index,Object value){
        JsonConfig jsonConfig = new JsonConfig();
        Object previous = get(index);
        element(index, value, jsonConfig);
        return previous;
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

        List elements2 = this.elements;
        return ToStringUtil.toString(elements2, indentFactor, indent);
    }

    //---------------------------------------------------------------

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

    //---------------------------------------------------------------

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
    JSONArray addValue(Object value,JsonConfig jsonConfig){
        this.elements.add(ProcessValueUtil.processArrayValue(value, jsonConfig));
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
                }
                return false;
            }
            if (JSONNull.getInstance().equals(o2)){
                return false;
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
                        if (m1 != null && m1 != IdentityObjectMorpher.INSTANCE){
                            if (!o1.equals(JSONUtils.getMorpherRegistry().morph(o1.getClass(), o2))){
                                return false;
                            }
                        }else if (m2 != null && m2 != IdentityObjectMorpher.INSTANCE){
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
