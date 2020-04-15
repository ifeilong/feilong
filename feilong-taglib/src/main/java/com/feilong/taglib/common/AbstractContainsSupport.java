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
package com.feilong.taglib.common;

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Iterator;
import java.util.Objects;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.taglib.AbstractConditionalTag;

/**
 * 包含父类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.2
 */
public abstract class AbstractContainsSupport extends AbstractConditionalTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4014405690315059600L;

    /** 一个集合,将会被转成Iterator,可以为逗号隔开的字符串,会被分隔成Iterator. */
    protected Object          collection       = null;

    /** 任意类型的值,最终toString 判断比较. */
    protected Object          value            = null;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractConditionalTag#condition()
     */
    @Override
    public boolean condition(){
        return containsByStringValue();
    }

    //---------------------------------------------------------------

    /**
     * Contains by string value.
     *
     * @return true, if successful
     * @since 1.7.2
     */
    protected boolean containsByStringValue(){
        Iterator<?> iterator = ConvertUtil.toIterator(collection);
        return containsByStringValue(iterator, value);
    }

    //---------------------------------------------------------------

    /**
     * 迭代{@link Iterator},判断元素的字符串格式是否和传入的值<code>value</code>的字符串格式相等.
     * 
     * <p style="color:red">
     * 注意,比较的是{@link java.util.Objects#toString(Object, String)},常用于自定义标签或者el function
     * </p>
     * 
     * @param iterator
     *            iterator
     * @param value
     *            value
     * @return 如果iterator为null/empty,则返回false<br>
     *         否则迭代 <code>iterator</code>,将元素转成String,和传入参数 <code>value</code>的String值进行比较,如果相等直接返回true
     * @see "org.springframework.util.CollectionUtils#contains(Iterator, Object)"
     * @see org.apache.commons.collections4.IteratorUtils#contains(Iterator, Object)
     * @since 1.7.2
     */
    public static boolean containsByStringValue(Iterator<?> iterator,Object value){
        if (isNullOrEmpty(iterator)){
            return false;
        }

        //---------------------------------------------------------------
        while (iterator.hasNext()){
            Object object = iterator.next();
            //注意:如果null,java.util.Objects#toString(Object),返回 "null" 和  java.lang.String#valueOf(Object) 一样
            //而 org.apache.commons.lang3.ObjectUtils#toString(Object) 返回  ""  empty

            //如果发现有equals 的,那么就直接返回true
            if (Objects.toString(object, EMPTY).equals(Objects.toString(value, EMPTY))){
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * Sets the 一个集合,将会被转成Iterator,可以为逗号隔开的字符串,会被分隔成Iterator.
     * 
     * @param collection
     *            the collection to set
     */
    public void setCollection(Object collection){
        this.collection = collection;
    }

    /**
     * Sets the 一个值.
     * 
     * @param value
     *            the value to set
     */
    public void setValue(Object value){
        this.value = value;
    }
}
