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
package com.feilong.coreextension.util;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.collection4.IterableUtils;

/**
 * 笛卡尔乘积.
 * 
 * <p>
 * 在数学中,两个集合X和Y的笛卡尓积(Cartesian product),又称直积,表示为X × Y,第一个对象是X的成员而第二个对象是Y的所有可能有序对的其中一个成员。<br>
 * 假设集合A={a, b},集合B={0, 1, 2},则两个集合的笛卡尔积为{(a, 0), (a, 1), (a, 2), (b, 0), (b, 1), (b, 2)}。<br>
 * 类似的例子有,如果A表示某学校学生的集合,B表示该学校所有课程的集合,则A与B的笛卡尔积表示所有可能的选课情况。
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.2
 */
public final class CartesianProductUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CartesianProductUtil.class);

    /** Don't let anyone instantiate this class. */
    private CartesianProductUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 笛卡尔乘积(数组数组参数形式).
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Integer[] array1 = { 1, 2, 3 };
     * Integer[] array2 = { 1, 2 };
     * Integer[] array3 = { 5 };
     * Integer[] array4 = { 4, 8 };
     * 
     * LOGGER.debug(JsonUtil.format(cartesianProduct(v1, v2, v3, v4), 0, 4));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8],[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8]]
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param arrays
     *            the arrays
     * @return the list
     * 
     * @see <a href="http://blog.chinaunix.net/uid-21125022-id-4392818.html">笛卡尔乘积及java算法实现 </a>
     * @see <a href="http://stackoverflow.com/questions/1719594/iterative-cartesian-product-in-java/1723050#1723050">Iterative Cartesian
     *      Product in Java</a>
     * @see <a href="http://baike.baidu.com/subview/348542/348542.htm#4_2">程序使用说明</a>
     * @see "com.google.common.collect.Sets#cartesianProduct(Set<? extends B>...)"
     * @see "com.google.common.collect.Lists#cartesianProduct(List<? extends B>...)"
     * @see #cartesianProduct(Iterable)
     * @since 1.7.2
     */
    @SafeVarargs
    public static <T> List<List<T>> cartesianProduct(T[]...arrays){
        List<Iterable<T>> list = newArrayList();
        for (T[] array : arrays){
            list.add(toList(array));
        }
        return cartesianProduct(list);
    }

    /**
     * 笛卡尔乘积(Iterable数组参数形式).
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * List{@code <List<Integer>>} result = cartesianProduct(toList(1, 2, 3), toList(1, 2), toList(5), toList(4, 8));
     * LOGGER.debug(JsonUtil.format(result, 0, 4));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8],[1,1,5,4],[2,2,5,8],[3,1,5,4],[1,2,5,8],[2,1,5,4],[3,2,5,8]]
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param iterables
     *            the iterables
     * @return the list
     * @see <a href="http://blog.chinaunix.net/uid-21125022-id-4392818.html">笛卡尔乘积及java算法实现 </a>
     * @see <a href="http://stackoverflow.com/questions/1719594/iterative-cartesian-product-in-java/1723050#1723050">Iterative Cartesian
     *      Product in Java</a>
     * @see <a href="http://baike.baidu.com/subview/348542/348542.htm#4_2">程序使用说明</a>
     * @see "com.google.common.collect.Sets#cartesianProduct(Set<? extends B>...)"
     * @see "com.google.common.collect.Lists#cartesianProduct(List<? extends B>...)"
     * @see #cartesianProduct(Iterable)
     * @since 1.7.2
     */
    @SafeVarargs
    public static <T> List<List<T>> cartesianProduct(Iterable<T>...iterables){
        List<Iterable<T>> list = toList(iterables);
        return cartesianProduct(list);
    }

    //---------------------------------------------------------------

    /**
     * Cartesian product.
     *
     * @param <T>
     *            the generic type
     * @param <I>
     *            the generic type
     * @param iterables
     *            the iterables
     * @return the list
     * @since 1.7.2
     */
    private static <T, I extends Iterable<T>> List<List<T>> cartesianProduct(Iterable<I> iterables){
        int length = 1;
        for (Iterable<T> iterable : iterables){
            length *= IterableUtils.size(iterable);
        }

        //---------------------------------------------------------------

        List<List<T>> returnList = new ArrayList<>(length);
        for (int i = 0; i < length; i++){
            returnList.add(buildList(iterables, i));
        }
        return returnList;
    }

    /**
     * Builds the list.
     *
     * @param <T>
     *            the generic type
     * @param <I>
     *            the generic type
     * @param iterables
     *            the iterables
     * @param i
     *            the i
     * @return the list
     * @since 1.8.2
     */
    private static <T, I extends Iterable<T>> List<T> buildList(Iterable<I> iterables,int i){
        //从不同的数组中取值
        List<T> list = newArrayList();
        for (Iterable<T> iterable : iterables){
            list.add(IterableUtils.get(iterable, i % IterableUtils.size(iterable)));
        }

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(list.toString());
        }
        return list;
    }

}
