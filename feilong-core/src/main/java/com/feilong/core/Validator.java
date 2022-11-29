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
package com.feilong.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.NumberUtil;
import com.feilong.lib.collection4.CollectionUtils;
import com.feilong.lib.lang3.StringUtils;

/**
 * 判断给定的对象是否是null或者empty.
 * 
 * <p>
 * 更多说明,参见 wiki <a href="https://github.com/venusdrogon/feilong-core/wiki/Validator">Validator wiki</a>
 * </p>
 * 
 * <ol>
 * <li>{@link #isNullOrEmpty(Object)} 判断对象是否是null或者empty</li>
 * <li>{@link #isNotNullOrEmpty(Object)} 判断对象是否不是null或者不是empty</li>
 * </ol>
 * 
 * <h3>对于empty的判断,使用以下逻辑:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>{@link CharSequence},支持子类有 {@link String},{@link StringBuffer},{@link StringBuilder},使用
 * {@link com.feilong.lib.lang3.StringUtils#isBlank(CharSequence)};</li>
 * <li>{@link Collection},使用其 {@link Collection#isEmpty()};</li>
 * <li>{@link Map},使用其 {@link Map#isEmpty()};</li>
 * <li>{@link Enumeration},使用 {@link Enumeration#hasMoreElements()};</li>
 * <li>{@link Iterator},使用 {@link Iterator#hasNext()};</li>
 * <li><code>数组</code>{@link java.lang.Class#isArray()},判断 {@link Array#getLength(Object)} ==0</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see String#trim()
 * @see Map#isEmpty()
 * @see Collection#isEmpty()
 * @see Enumeration#hasMoreElements()
 * @see Iterator#hasNext()
 * @see com.feilong.lib.lang3.Validate
 * @see com.feilong.lib.lang3.StringUtils#isBlank(CharSequence)
 * @see com.feilong.lib.lang3.StringUtils#isEmpty(CharSequence)
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(byte[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(boolean[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(char[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(int[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(long[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(short[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(float[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(double[])
 * @see com.feilong.lib.lang3.ArrayUtils#isEmpty(Object[])
 * @see com.feilong.lib.collection4.CollectionUtils#sizeIsEmpty(Object)
 * @see com.feilong.lib.collection4.CollectionUtils#isEmpty(Collection)
 * @see com.feilong.lib.collection4.CollectionUtils#isNotEmpty(Collection)
 * @see "org.apache.commons.collections4.MapUtils#isEmpty(Map)"
 * @see "org.apache.commons.collections4.MapUtils#isNotEmpty(Map)"
 * @since 1.0.0
 */
public final class Validator{

    /** Don't let anyone instantiate this class. */
    private Validator(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 判断内部任何对象 <code>values</code> 是不是 null或者empty.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * Validator.isAnyNullOrEmpty((String) null)    = true
     * Validator.isAnyNullOrEmpty((Object[]) null)  = false
     * 
     * Validator.isAnyNullOrEmpty(null, "foo")      = true
     * Validator.isAnyNullOrEmpty(null, null)       = true
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>对于empty的判断,使用以下逻辑:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>{@link CharSequence},支持子类有 {@link String},{@link StringBuffer},{@link StringBuilder},使用
     * {@link com.feilong.lib.lang3.StringUtils#isBlank(CharSequence)};</li>
     * <li>{@link Collection},使用其 {@link Collection#isEmpty()};</li>
     * <li>{@link Map},使用其 {@link Map#isEmpty()};</li>
     * <li>{@link Enumeration},使用 {@link Enumeration#hasMoreElements()};</li>
     * <li>{@link Iterator},使用 {@link Iterator#hasNext()};</li>
     * <li><code>数组</code>{@link java.lang.Class#isArray()},判断 {@link Array#getLength(Object)} ==0</li>
     * </ol>
     * </blockquote>
     * 
     * @param values
     *            元素可以是{@link Collection},{@link Map},{@link Enumeration},{@link Iterator},{@link Iterable},{@link CharSequence},
     *            以及所有数组类型(包括原始类型数组)
     * @return 如果values是null,返回false<br>
     *         如果values是empty也返回false<br>
     *         逻辑和 StringUtils#isAnyBlank(CharSequence...) 保持一致
     *         其他情况返回false<br>
     * @see com.feilong.lib.lang3.StringUtils#isAnyBlank(CharSequence...)
     * @since 3.2.0
     */
    public static boolean isAnyNullOrEmpty(Object...values){
        if (isNullOrEmpty(values)){
            return false;
        }
        for (Object value : values){
            //有一个是null或者empty 就返回true
            if (isNullOrEmpty(value)){
                return true;
            }
        }
        //表示所有元素都不是null 或者empty
        return false;
    }

    /**
     * 判断对象 <code>value</code> 是不是 null或者empty.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * <span style="color:green">// null</span>
     * Validator.isNullOrEmpty(null)                                            = true
     * 
     * <span style="color:green">//CharSequence</span>
     * Validator.isNullOrEmpty("")                                              = true
     * Validator.isNullOrEmpty("   ")                                           = true
     * 
     * Validator.isNullOrEmpty(new StringBuffer())                              = true
     * Validator.isNullOrEmpty(new StringBuffer(""))                            = true
     * Validator.isNullOrEmpty(new StringBuffer(" "))                           = true
     * 
     * Validator.isNullOrEmpty(new StringBuilder())                             = true
     * Validator.isNullOrEmpty(new StringBuilder(""))                           = true
     * Validator.isNullOrEmpty(new StringBuilder(" "))                          = true
     * 
     * <span style="color:green">//Collection</span>
     * Validator.isNullOrEmpty(new ArrayList{@code <String>}())                 = true
     * 
     * <span style="color:green">//Map</span>
     * Validator.isNullOrEmpty(new LinkedHashMap{@code <String, String>}())     = true
     * 
     * <span style="color:green">//Iterator</span>
     * Validator.isNullOrEmpty(new ArrayList{@code <String>}().iterator())      = true
     * 
     * <span style="color:green">//Enumeration</span>
     * Validator.isNullOrEmpty(toEnumeration(new ArrayList{@code <String>}()))  = true
     * 
     * <span style="color:green">//Array</span>
     * Validator.isNullOrEmpty(new String[] {})                                 = true
     * Validator.isNullOrEmpty(new Integer[][] {})                              = true
     * Validator.isNullOrEmpty(new User[] {})                                   = true
     * 
     * <span style="color:green">//Primitive Array</span>
     * Validator.isNullOrEmpty(new double[] {})                                 = true
     * Validator.isNullOrEmpty(new float[] {})                                  = true
     * Validator.isNullOrEmpty(new long[] {})                                   = true
     * Validator.isNullOrEmpty(new int[] {})                                    = true
     * Validator.isNullOrEmpty(new short[] {})                                  = true
     * Validator.isNullOrEmpty(new char[] {})                                   = true
     * Validator.isNullOrEmpty(new byte[] {})                                   = true
     * Validator.isNullOrEmpty(new boolean[] {})                                = true
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>对于empty的判断,使用以下逻辑:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>{@link CharSequence},支持子类有 {@link String},{@link StringBuffer},{@link StringBuilder},使用
     * {@link com.feilong.lib.lang3.StringUtils#isBlank(CharSequence)};</li>
     * <li>{@link Collection},使用其 {@link Collection#isEmpty()};</li>
     * <li>{@link Map},使用其 {@link Map#isEmpty()};</li>
     * <li>{@link Enumeration},使用 {@link Enumeration#hasMoreElements()};</li>
     * <li>{@link Iterator},使用 {@link Iterator#hasNext()};</li>
     * <li><code>数组</code>{@link java.lang.Class#isArray()},判断 {@link Array#getLength(Object)} ==0</li>
     * </ol>
     * </blockquote>
     * 
     * @param value
     *            可以是{@link Collection},{@link Map},{@link Enumeration},{@link Iterator},{@link Iterable},{@link CharSequence},
     *            以及所有数组类型(包括原始类型数组)
     * @return 如果是null,返回true<br>
     *         如果是empty也返回true<br>
     *         其他情况返回false<br>
     * @see com.feilong.lib.collection4.CollectionUtils#sizeIsEmpty(Object)
     * @see com.feilong.lib.lang3.StringUtils#isBlank(CharSequence)
     */
    public static boolean isNullOrEmpty(Object value){
        if (null == value){
            return true;
        }
        // 字符串
        if (value instanceof CharSequence){
            CharSequence charSequence = (CharSequence) value;
            boolean blank = StringUtils.isBlank(charSequence);
            if (blank){
                return true;
            }
            return handleSpace(charSequence);
        }

        //---------------------------------------------------------------

        // collections 支持的类型
        if (isCollectionsSupportType(value)){
            return CollectionUtils.sizeIsEmpty(value);
        }
        return false;
    }

    /**
     * 处理空格
     * 
     * @param charSequence
     * @return
     * @since 3.3.7
     */
    private static boolean handleSpace(CharSequence charSequence){
        String string = ConvertUtil.toString(charSequence);
        //不间断空格 as a space,but often not adjusted   see https://github.com/ifeilong/feilong/issues/56
        string = string.replace('\u00A0', ' ');

        //字符U+2007---U+200A和U+202F在Unicode标准中没有给它们分配精确的宽度，字符的显示实现可能会与预期的宽度有很大偏差。
        //此外，在出版软件中使用相同名称的概念时，比如“窄空格”，其含义可能会有很大的不同。
        //例如，
        //在 InDesign 软件中，“thin space窄空格”是1/8 em (即0.125 em，与建议的0.2 em 相反) ，
        //而“hair space发际空格”只有1/24 em (即大约0.042 em，而窄空格标志的宽度通常在0.1em和0.2 em之间变化)。
        //figure space
        string = string.replace('\u2007', ' ');

        //narrow no-break space
        //比不间断空格(或者空格)都窄
        string = string.replace('\u202F', ' ');

        return StringUtils.isBlank(string);
    }

    //---------------------------------------------------------------

    /**
     * 判断对象 <code>value</code> 是否不是null或者empty,调用 !{@link #isNullOrEmpty(Object)} 方法 .
     * 
     * <p>
     * 示例参考 {@link #isNullOrEmpty(Object)} 方法
     * </p>
     * 
     * <h3>对于empty的判断,使用以下逻辑:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>{@link CharSequence},支持子类有 {@link String},{@link StringBuffer},{@link StringBuilder},使用
     * {@link com.feilong.lib.lang3.StringUtils#isBlank(CharSequence)};</li>
     * <li>{@link Collection},使用其 {@link Collection#isEmpty()};</li>
     * <li>{@link Map},使用其 {@link Map#isEmpty()};</li>
     * <li>{@link Enumeration},使用 {@link Enumeration#hasMoreElements()};</li>
     * <li>{@link Iterator},使用 {@link Iterator#hasNext()};</li>
     * <li><code>数组</code>{@link java.lang.Class#isArray()},判断 {@link Array#getLength(Object)} ==0</li>
     * </ol>
     * </blockquote>
     * 
     * @param value
     *            可以是{@link Collection},{@link Map},{@link Enumeration},{@link Iterator},{@link Iterable},{@link CharSequence}
     *            ,以及所有数组类型(包括原始类型数组)
     * @return 如果是null,返回false<br>
     *         如果是空也返回false<br>
     *         其他情况返回true<br>
     * @see #isNullOrEmpty(Object)
     */
    public static boolean isNotNullOrEmpty(Object value){
        return !isNullOrEmpty(value);
    }

    /**
     * 判断对象 <code>value</code> 是否是null或者是0.
     * 
     * <h3>重构:</h3>
     * 
     * <blockquote>
     * <p>
     * 常用来做行数或者接口数据总数的判断,
     * </p>
     * <p>
     * 对于以下代码:
     * </p>
     * 
     * <pre class="code">
     * Long countTrack = bookVo.getCountTrack();//可能是0
     * if (Objects.equals(0L, countTrack)){
     *     return 0;
     * }
     * 
     * </pre>
     * 
     * <b>可以重构成:</b>
     * 
     * <pre class="code">
     * if (isNullOrZero(countTrack)){
     *     return 0;
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param value
     *            the value
     * @return 如果是null,返回true<br>
     *         如果是0也返回true<br>
     *         其他情况返回false<br>
     * @see #isNullOrEmpty(Object)
     * @since 3.3.3
     */
    public static boolean isNullOrZero(Number value){
        return null == value || NumberUtil.isEquals(value, 0);
    }

    /**
     * 判断对象 <code>value</code> 是否不是null且不是0.,调用 !{@link #isNullOrZero(Number)} 方法 .
     *
     * @param value
     *            the value
     * @return 如果是null,返回false<br>
     *         如果是0也返回false<br>
     *         其他情况返回true<br>
     * @see #isNullOrEmpty(Object)
     * @since 3.3.3
     */
    public static boolean isNotNullOrZero(Number value){
        return !isNullOrZero(value);
    }

    //---------------------------------------------------------------

    /**
     * Checks if is collections support type.
     *
     * @param value
     *            the value
     * @return true, if checks if is collections support type
     * @since 1.5.2
     */
    private static boolean isCollectionsSupportType(Object value){
        // 集合或者map
        boolean isCollectionOrMap = value instanceof Collection || value instanceof Map;

        // 枚举 或者是 Iterator迭代器
        boolean isEnumerationOrIterator = value instanceof Enumeration || value instanceof Iterator;

        return isCollectionOrMap//集合或者map
                        || isEnumerationOrIterator//枚举 或者是 Iterator迭代器
                        || value.getClass().isArray()//判断数组
        ;
    }
}