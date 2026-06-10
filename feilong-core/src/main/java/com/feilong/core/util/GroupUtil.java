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
package com.feilong.core.util;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.CollectionsUtil.toStream;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Transformer;

import com.feilong.core.Validate;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.predicate.BeanPredicateUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 专注于 group 分组的工具类, 提供多种分组方式, 支持按属性名, 按 key 提取函数, 带过滤条件, 去重等功能.
 * <p>
 * 该类封装了常见的"分组 + 过滤 + 去重 + 收集"模式, 使用传统的 for 循环或 Stream API 实现,
 * 旨在提供可读性强, 性能良好的分组操作.
 * </p>
 *
 * <h3>核心方法概览</h3>
 * <ul>
 * <li>{@link #group(Iterable, String)} - 按属性名分组, 每组为 List</li>
 * <li>{@link #group(Iterable, String, org.apache.commons.collections4.Predicate)} - 按属性名分组, 支持过滤</li>
 * <li>{@link #group(Iterable, Transformer)} - 按 Transformer 分组, 每组为 List</li>
 * <li>{@link #group(Iterable, org.apache.commons.collections4.Predicate, Transformer)} - 按 Transformer 分组, 支持过滤</li>
 * <li>{@link #groupBy(Iterable, Function)} - 按 Function 分组, 每组为 List</li>
 * <li>{@link #groupBy(Iterable, Function, Predicate)} - 按 Function 分组, 支持过滤</li>
 * <li>{@link #groupByToSet(Iterable, Function, Predicate)} - 按 Function 分组, 每组为 Set (基于元素 equals 去重)</li>
 * <li>{@link #groupByToSet(Iterable, Function, Function, Predicate)} - 按 Function 分组, 每组内按指定 key 去重</li>
 * <li>{@link #groupOne(Iterable, Function)} - 按 Function 分组, 每组只保留第一个元素</li>
 * <li>{@link #groupOne(Iterable, String)} - 按属性名分组, 每组只保留第一个元素</li>
 * </ul>
 *
 * <h3>设计原则:</h3>
 * <ul>
 * <li>优先使用传统的 for 循环, 避免 Stream 的 Collector 嵌套, 提高可读性和调试便利性.</li>
 * <li>返回的 Map 均为 {@link LinkedHashMap}, 保持元素的插入顺序.</li>
 * <li>支持分组 key 为 null 的情况 (基于 LinkedHashMap 的特性).</li>
 * <li>所有方法均对 null/empty 输入返回空 Map, 不做额外处理.</li>
 * </ul>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.2
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupUtil{

    /**
     * 循环 <code>beanIterable</code>,以 元素的 <code>propertyName</code>属性值为key,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     *
     * <li>
     * 返回的{@link LinkedHashMap},key是 <code>beanIterable</code>中的元素对象中 <code>propertyName</code>的值,value是<code>beanIterable</code>
     * 中的元素对象;
     * </li>
     *
     * <li>顺序是 <code>beanIterable</code> <code>propertyName</code>的值顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}</li>
     *
     * <li>属性<code>propertyName</code>值相同的元素,组成集合 list</li>
     * <li>如果value只需要单值的话,可以调用 {@link #groupOne(Iterable, String)}方法</li>
     * </ol>
     * </blockquote>
     *
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <pre class="code">
     * List{@code <User>} list = toList(
     *                 new User("张飞", 23),
     *                 new User("刘备", 25),
     *                 new User("刘备", 30));
     *
     * Map{@code <String, List<User>>} map = GroupUtil.group(list, "name");
     * log.debug(JsonUtil.format(map));
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
    {
        "张飞": [ {
            "age": 23,
            "name": "张飞",
        }],
        "刘备": [
            {
                "age": 25,
                "name": "刘备",
            },
            {
                "age": 30,
                "name": "刘备",
            }
        ]
    }
     * </pre>
     *
     * </blockquote>
     *
     * @param <T>
     *            注意,此处的T是属性值,Object类型,如果从excel中读取的类型是String,那么不能简简单单的使用Integer来接收,不能强制转换
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param propertyName
     *            泛型O对象指定的属性名称,Possibly indexed and/or nested name of the property to be modified,参见
     *            <a href="../bean/BeanUtil.html#propertyName">propertyName</a>
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>propertyName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>propertyName</code> 是blank,抛出 {@link IllegalArgumentException}
     * @see #group(Iterable, String, org.apache.commons.collections4.Predicate)
     * @since 1.0.8
     */
    public static <T, O> Map<T, List<O>> group(Iterable<O> beanIterable,String propertyName){
        return group(beanIterable, propertyName, null);
    }

    /**
     * 循环 <code>beanIterable</code>,找到符合条件的 <code>includePredicate</code>的元素,以元素的 <code>propertyName</code>
     * 属性值为key,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     *
     * <li>
     * 返回的{@link LinkedHashMap},key是 <code>beanIterable</code>中的元素对象中 <code>propertyName</code>的值,value是<code>beanIterable</code>
     * 中的元素对象;
     * </li>
     *
     * <li>
     * 顺序是 <code>beanIterable</code> <code>propertyName</code>的值顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}
     * </li>
     *
     * </ol>
     * </blockquote>
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <p>
     * <b>场景:</b> 将age {@code >} 20的User,按照name 进行 group
     * </p>
     *
     * <pre class="code">
        List{@code <User>} list = new ArrayList{@code <>}();
        list.add(new User("张飞", 10));
        list.add(new User("张飞", 28));
        list.add(new User("刘备", 32));
        list.add(new User("刘备", 30));
        list.add(new User("刘备", 10));
    
        Map{@code <String, List<User>>} map = GroupUtil.group(list, "name", new Predicate{@code <User>}(){
    
            {@code @Override}
            public boolean evaluate(User user){
                return user.getAge() {@code >} 20;
            }
        });
        log.info(JsonUtil.format(map));
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
    {
        "张飞": [{
            "age": 28,
            "name": "张飞"
        }],
        "刘备": [{
                "age": 32,
                "name": "刘备"
            },{
                "age": 30,
                "name": "刘备"
            }
        ]
    }
     * </pre>
     *
     * <p>
     * 当然,对于上述代码,你还可以优化成:
     * </p>
     *
     * <pre class="code">
     * Predicate{@code <User>} comparatorPredicate = BeanPredicateUtil.comparatorPredicate("age", 20, Criterion.LESS);
     * Map{@code <String, List<User>>} map = GroupUtil.group(list, "name", comparatorPredicate);
     * </pre>
     *
     * 参见
     * {@link BeanPredicateUtil#comparatorPredicate(String, Comparable, com.feilong.lib.collection4.functors.ComparatorPredicate.Criterion)}
     *
     * </blockquote>
     *
     * @param <T>
     *            注意,此处的T是属性值,Object类型,如果从excel中读取的类型是String,那么不能简简单单的使用Integer来接收,不能强制转换
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param propertyName
     *            泛型O对象指定的属性名称,Possibly indexed and/or nested name of the property to be modified,参见
     *            <a href="../bean/BeanUtil.html#propertyName">propertyName</a>
     * @param includePredicate
     *            the include predicate
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>propertyName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>propertyName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果没有任何element match <code>includePredicate</code>,返回 empty {@link LinkedHashMap}<br>
     *         如果 <code>includePredicate</code> 是null,那么以所有的元素进行分组
     * @see PropertyUtil#getProperty(Object, String)
     * @see #groupOne(Iterable, String)
     * @since 1.5.5
     */
    public static <T, O> Map<T, List<O>> group(
                    Iterable<O> beanIterable,
                    final String propertyName,
                    org.apache.commons.collections4.Predicate<O> includePredicate){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }
        Validate.notBlank(propertyName, "propertyName can't be null/empty!");

        //---------------------------------------------------------------
        //org.apache.commons.beanutils.BeanToPropertyValueTransformer 但是实现的是 commons-collection3
        return group(
                        beanIterable,
                        includePredicate, //
                        input -> PropertyUtil.getProperty(input, propertyName));
    }

    /**
     * 循环 <code>beanIterable</code>,将元素使用<code>keyTransformer</code>转成key,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>
     * 返回的{@link LinkedHashMap},key是 <code>beanIterable</code>中的元素 使用<code>keyTransformer</code>转换的值,value是
     * <code>beanIterable</code>中的元素对象(相同key值,组成list);
     * </li>
     *
     * <li>
     * 返回的{@link LinkedHashMap}顺序,是 <code>beanIterable</code> 元素顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}
     * </li>
     *
     * </ol>
     * </blockquote>
     *
     *
     * <h3>示例:</h3>
     *
     * <blockquote>
     *
     * <p>
     * <b>场景:</b> 从user list中,提取user的姓名的姓为key,user组成list,返回map
     * </p>
     *
     * <pre class="code">
     *
     * User mateng55 = new User("马腾", 55);
     * User machao28 = new User("马超", 28);
     * User madai27 = new User("马岱", 27);
     * User maxiu25 = new User("马休", 25);
     * User zhangfei28 = new User("张飞", 28);
     * User liubei32 = new User("刘备", 32);
     * User guanyu50 = new User("关羽", 50);
     * User guanping32 = new User("关平", 32);
     * User guansuo31 = new User("关索", 31);
     * User guanxing20 = new User("关兴", 18);
     *
     * <span style="color:green">//---------------------------------------------------------------</span>
     * List{@code <User>} list = toList(mateng55, machao28, madai27, maxiu25, zhangfei28, liubei32, guanyu50, guanping32, guansuo31, guanxing20);
     *
     * <span style="color:green">//---------------------------------------------------------------</span>
     *
     * Map{@code <String, List<User>>} map = GroupUtil.group(list,new Transformer{@code <User, String>}(){
     *
     *     &#64;Override
     *     public String transform(User user){
     *         <span style="color:green">//提取名字 的姓</span>
     *         return user.getName().substring(0, 1);
     *     }
     * });
     *
     * log.debug(JsonUtil.format(map));
     *
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
    {
        "马":[{
                "age": 55,
                "name": "马腾",
            },{
                "age": 28,
                "name": "马超",
            },{
                "age": 27,
                "name": "马岱",
            },{
                "age": 25,
                "name": "马休",
            }
        ],
        "张": [{
            "age": 28,
            "name": "张飞",
        }],
        "刘": [{
            "age": 32,
            "name": "刘备",
        }],
        "关": [{
                "age": 50,
                "name": "关羽",
            },{
                "age": 32,
                "name": "关平",
            },{
                "age": 31,
                "name": "关索",
            },{
                "age": 18,
                "name": "关兴",
            }
        ]
    }
     * </pre>
     *
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param keyTransformer
     *            返回的map,key转换器
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>keyTransformer</code> 是null,抛出 {@link NullPointerException}<br>
     * @see <a href="https://github.com/venusdrogon/feilong-core/issues/270">List to Map 实现类似矩阵的逻辑 by ananbeike</a>
     * @since 1.8.8
     */
    public static <T, O> Map<T, List<O>> group(Iterable<O> beanIterable,Transformer<O, T> keyTransformer){
        return group(beanIterable, null, keyTransformer);
    }

    /**
     * 循环 <code>beanIterable</code>,找到符合条件的 <code>includePredicate</code>的元素,将元素使用<code>keyTransformer</code>转成key
     * ,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>
     * 返回的{@link LinkedHashMap},key是 <code>beanIterable</code>中的元素 使用<code>keyTransformer</code>转换的值,value是
     * <code>beanIterable</code>中的元素对象(相同key值,组成list);
     * </li>
     *
     * <li>
     * 返回的{@link LinkedHashMap}顺序,是 <code>beanIterable</code> 元素顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}
     * </li>
     *
     * </ol>
     * </blockquote>
     *
     *
     * <h3>示例:</h3>
     *
     * <blockquote>
     *
     * <p>
     * <b>场景:</b> 从user list中,提取 年龄 大于20的user,user的姓名的姓为key,user组成list,返回map
     * </p>
     *
     * <pre class="code">
     *
     * User mateng55 = new User("马腾", 55);
     * User machao28 = new User("马超", 28);
     * User madai27 = new User("马岱", 27);
     * User maxiu25 = new User("马休", 25);
     * User zhangfei28 = new User("张飞", 28);
     * User liubei32 = new User("刘备", 32);
     * User guanyu50 = new User("关羽", 50);
     * User guanping32 = new User("关平", 32);
     * User guansuo31 = new User("关索", 31);
     * User guanxing20 = new User("关兴", 18);
     *
     * <span style="color:green">//---------------------------------------------------------------</span>
     * List{@code <User>} list = toList(mateng55, machao28, madai27, maxiu25, zhangfei28, liubei32, guanyu50, guanping32, guansuo31, guanxing20);
     *
     * <span style="color:green">//---------------------------------------------------------------</span>
     *
     * Predicate{@code <User>} comparatorPredicate = BeanPredicateUtil.comparatorPredicate("age", 20, Criterion.LESS);
     * Map{@code <String, List<User>>} map = GroupUtil.group(list, comparatorPredicate, new Transformer{@code <User, String>}(){
     *
     *     &#64;Override
     *     public String transform(User user){
     *         <span style="color:green">//提取名字 的姓</span>
     *         return user.getName().substring(0, 1);
     *     }
     * });
     *
     * log.debug(JsonUtil.format(map));
     *
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
    {
            "马":[{
                    "age": 55,
                    "name": "马腾",
                },{
                    "age": 28,
                    "name": "马超",
                },{
                    "age": 27,
                    "name": "马岱",
                },{
                    "age": 25,
                    "name": "马休"
                }],
            "张": [{
                "age": 28,
                "name": "张飞"
            }],
            "刘": [{
                "age": 32,
                "name": "刘备"
            }],
            "关": [{
                    "age": 50,
                    "name": "关羽"
                },{
                    "age": 32,
                    "name": "关平"
                },{
                    "age": 31,
                    "name": "关索"
                }]
        }
     * </pre>
     *
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param includePredicate
     *            the include predicate
     * @param keyTransformer
     *            返回的map,key转换器
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>keyTransformer</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>includePredicate</code> 是null,那么以所有的元素进行分组<br>
     *         如果没有任何element match <code>includePredicate</code>,返回 empty {@link LinkedHashMap}<br>
     * @see org.apache.commons.collections4.Transformer#transform(Object)
     * @see <a href="https://github.com/venusdrogon/feilong-core/issues/270">List to Map 实现类似矩阵的逻辑 by ananbeike</a>
     * @since 1.8.8
     */
    public static <T, O> Map<T, List<O>> group(
                    Iterable<O> beanIterable,
                    org.apache.commons.collections4.Predicate<O> includePredicate,
                    Transformer<O, T> keyTransformer){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }
        Validate.notNull(keyTransformer, "keyTransformer can't be null!");
        //---------------------------------------------------------------

        Map<T, List<O>> map = newLinkedHashMap(size(beanIterable));
        for (O obj : beanIterable){
            if (null != includePredicate && !includePredicate.evaluate(obj)){
                continue;
            }
            MapUtil.putMultiValue(map, keyTransformer.transform(obj), obj);
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 
     * 通过key提取函数分组，
     * 循环 <code>beanIterable</code>,以 <code>keyExtractor</code>提取器属性值为key,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     *
     * <li>
     * 返回的{@link LinkedHashMap},key是 <code>beanIterable</code>中的元素对象中 <code>keyExtractor</code>提取的值,value是<code>beanIterable</code>
     * 中的元素对象;
     * </li>
     *
     * <li>顺序是 <code>beanIterable</code> <code>propertyName</code>的值顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}</li>
     *
     * <li>属性<code>keyExtractor</code>提取值相同的元素,组成集合 list</li>
     * <li>如果value只需要单值的话,可以调用 {@link #groupOne(Iterable, Function)}方法</li>
     * </ol>
     * </blockquote>
     *
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <pre class="code">
     * List{@code <User>} list = toList(
     *                 new User("张飞", 23),
     *                 new User("刘备", 25),
     *                 new User("刘备", 30));
     *
     * Map{@code <String, List<User>>} map = GroupUtil.groupBy(list, "name");
     * log.debug(JsonUtil.format(map));
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
    {
        "张飞": [ {
            "age": 23,
            "name": "张飞",
        }],
        "刘备": [
            {
                "age": 25,
                "name": "刘备",
            },
            {
                "age": 30,
                "name": "刘备",
            }
        ]
    }
     * </pre>
     *
     * </blockquote>
     * 
     * <p>
     * 由于重载方法:
     * 
     * <ul>
     * <li><code>public static <T, O> Map<T, List<O>> group(Iterable<O> beanIterable, Transformer<O, T> keyTransformer)</code></li>
     * <li><code>public static <T, O> Map<T, List<O>> group(Iterable<O> beanIterable, Function<O, T> keyExtractor)</code></li>
     * </ul>
     * 
     * 
     * 调用 <code>Map<String, List<User>> map = GroupUtil.group(list, User::getName);</code>
     * 
     * 会出现提示 is ambiguous for the type GroupUtil , 编译有歧义问题,
     * 
     * <span style="color:red">故新方法,重新命名为 groupBy</span>
     * </p>
     * 
     * 
     *
     * @param <T>
     *            注意,此处的T是属性值,Object类型,如果从excel中读取的类型是String,那么不能简简单单的使用Integer来接收,不能强制转换
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param keyExtractor
     *            提取key 的提取器
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>keyExtractor</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 4.5.2
     */
    public static <T, O> Map<T, List<O>> groupBy(Iterable<O> beanIterable,Function<O, T> keyExtractor){
        return groupBy(beanIterable, keyExtractor, null);
    }

    /**
     * 循环 <code>beanIterable</code>,以 <code>keyExtractor</code>提取器属性值为key,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>
     * 返回的{@link LinkedHashMap},key是 <code>beanIterable</code>中的元素对象中 <code>keyExtractor</code>提取的值,value是<code>beanIterable</code>
     * 中的元素对象;
     * </li>
     *
     * <li>顺序是 <code>beanIterable</code> <code>propertyName</code>的值顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}</li>
     *
     * <li>属性<code>keyExtractor</code>提取值相同的元素,组成集合 list</li>
     * </ol>
     * </blockquote>
     *
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <pre class="code">
     * List{@code <User>} list = toList(
     *                 new User("张飞", 23),
     *                 new User("刘备", 25),
     *                 new User("刘备", 30));
     *
     * Map{@code <String, List<User>>} map = GroupUtil.groupBy(list, "name");
     * log.debug(JsonUtil.format(map));
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
    {
        "张飞": [ {
            "age": 23,
            "name": "张飞",
        }],
        "刘备": [
            {
                "age": 25,
                "name": "刘备",
            },
            {
                "age": 30,
                "name": "刘备",
            }
        ]
    }
     * </pre>
     *
     * </blockquote>
     *
     * @param <T>
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param keyExtractor
     *            提取key
     * @param includePredicate
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>keyExtractor</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 4.5.2
     */
    public static <T, O> Map<T, List<O>> groupBy(Iterable<O> beanIterable,Function<O, T> keyExtractor,Predicate<O> includePredicate){
        // 使用Stream API和lambda表达式重写
        Collector<O, ?, List<O>> collection = Collectors.toList();
        return filterAndCollect(beanIterable, keyExtractor, includePredicate, collection);
    }

    /**
     * 对集合进行分组, 每组内元素收集到 {@link Set} 中 <span style="color:red">(基于元素自身的 {@code equals/hashCode} 去重)</span>, 并保留元素的插入顺序 (使用
     * {@link LinkedHashSet}).
     * 
     * <p>
     * 与 {@link #groupByToSet(Iterable, Function, Function, Predicate)} 不同, 此方法没有显式的去重 key,
     * 去重逻辑完全依赖于元素类型的 {@code equals()} 和 {@code hashCode()} 实现.
     * 如果元素本身没有正确的 equals/hashCode 实现, 则无法达到预期的去重效果.
     * </p>
     *
     * <h3>实现说明</h3>
     * <ul>
     * <li>使用传统的 for 循环 + 临时 {@code Map} 实现, 避免 Stream 的 Collector 嵌套, 提高可读性.</li>
     * <li>每组使用 {@link LinkedHashSet} 作为值容器, 保持插入顺序并自动去重.</li>
     * <li>支持可选的 {@code includePredicate} 过滤条件.</li>
     * <li>支持分组 key 为 null 的情况 (不同于基于 Stream 的 {@link Collectors#groupingBy} 实现).</li>
     * </ul>
     *
     * <h3>使用示例</h3>
     * <pre>{@code
     * // 假设 User 类已正确实现 equals/hashCode (基于 id 或 name+age)
     * List<User> users = Arrays.asList(
     *     new User("张飞", 28),
     *     new User("刘备", 32),
     *     new User("刘备", 30),
     *     new User("张飞", 28),   // 与第一个张飞相同 (根据 equals)
     *     new User("关羽", 35)
     * );
     *
     * // 按姓名分组, 每组内去重 (基于 User 的 equals)
     * Map<String, Set<User>> result = GroupUtil.groupByToSet(
     *     users,
     *     User::getName,
     *     null // 不过滤
     * );
     *
     * // 结果:
     * // "张飞" -> [User("张飞",28)]  (重复的被去重)
     * // "刘备" -> [User("刘备",32), User("刘备",30)]
     * // "关羽" -> [User("关羽",35)]
     *
     * // 带过滤的例子: 只保留年龄 >= 30 的用户
     * Map<String, Set<User>> filtered = GroupUtil.groupByToSet(
     *     users,
     *     User::getName,
     *     u -> u.getAge() != null && u.getAge() >= 30
     * );
     * // 结果中不包含 "张飞" 分组
     * }</pre>
     *
     * @param <T>
     *            分组 key 的类型
     * @param <O>
     *            元素类型 (必须正确实现 {@code equals()} 和 {@code hashCode()})
     * @param beanIterable
     *            待处理的集合, 不能为 {@code null} (但允许为空, 返回空 Map)
     * @param keyExtractor
     *            分组 key 提取函数, 不能为 {@code null}
     * @param includePredicate
     *            可选过滤条件, 为 {@code null} 时不过滤
     * @return 分组后的 Map, key 为分组 key, value 为每组内去重后的 Set (保持插入顺序)
     * @throws NullPointerException
     *             如果 {@code keyExtractor} 为 {@code null}
     * @since 4.5.3
     */
    public static <T, O> Map<T, Set<O>> groupByToSet(Iterable<O> beanIterable,Function<O, T> keyExtractor,Predicate<O> includePredicate){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }
        Validate.notNull(keyExtractor, "keyExtractor can't be null!");

        // 使用 LinkedHashMap 保持分组顺序, 每组使用 LinkedHashSet 保持插入顺序并去重
        Map<T, Set<O>> result = new LinkedHashMap<>();

        for (O bean : beanIterable){
            // 1. 过滤
            if (includePredicate != null && !includePredicate.test(bean)){
                continue;
            }

            // 2. 提取分组 key (允许为 null)
            T groupKey = keyExtractor.apply(bean);

            // 3. 获取该分组的 Set (不存在则创建)
            Set<O> groupSet = result.computeIfAbsent(groupKey, k -> new LinkedHashSet<>());

            // 4. 添加元素 (Set 自动基于 equals/hashCode 去重)
            groupSet.add(bean);
        }

        return result;

        //下面代码 如果有元素的keyExtractor 值是null 会报错 比如

        //        User noNameUser = new User((String) null, 20);
        //        List<User> users = Arrays.asList(zhangFei28, noNameUser);
        //
        //        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, null);

        //底层 在 java.util.stream.Collectors.groupingBy(Function<? super T, ? extends K>, Supplier<M>, Collector<? super T, A, D>) 会校验
        //---------------------------------------------------------------
        //        
        //        
        //        // 使用Stream API和lambda表达式重写
        //        // 使用LinkedHashSet保持插入顺序
        //        Collector<O, ?, Set<O>> collection = Collectors.toCollection(LinkedHashSet::new);
        //        return filterAndCollect(beanIterable, keyExtractor, includePredicate, collection);
    }

    /**
     * 对集合进行分组，并在每组内根据指定的去重 key 进行去重，最终返回 {@code Map<T, Set<O>>}.
     * <p>
     * 与 {@link #groupBy(Iterable, Function)} 不同，此方法不仅按 {@code keyExtractor} 分组，
     * 还会在每组内根据 {@code distinctByFunction} 提取的唯一标识进行去重，
     * 保留每组中第一个出现的元素（基于遍历顺序）.
     * 此外，可通过 {@code includePredicate} 过滤掉不需要的元素.
     * </p>
     *
     * <h3>实现原理</h3>
     * <ul>
     * <li>使用传统的 for 循环 + 临时 {@code Map} 实现，避免 Stream 的 Collector 嵌套，提高可读性.</li>
     * <li>外层 Map 的 key 为分组 key，value 为内层 Map；内层 Map 的 key 为去重 key，value 为元素本身.</li>
     * <li>内层 Map 使用 {@link LinkedHashMap} 保持去重 key 的首次出现顺序.</li>
     * <li>最终将内层 Map 的 values 转为 {@link LinkedHashSet}，保留顺序并去重.</li>
     * </ul>
     *
     * <h3>性能说明</h3>
     * <ul>
     * <li>时间复杂度 O(n)，每组内去重操作 O(1)（基于 {@link Map#putIfAbsent}）.</li>
     * <li>空间复杂度 O(n)，需要额外存储内层 Map 的 key-value 映射.</li>
     * <li>如果直接用 {@code Map<T, Set<O>>} 并通过遍历 Set 判断重复，则每组内去重复杂度为 O(m²)（m 为该组元素个数），
     * 本实现通过内层 Map 将去重降至 O(1)，显著提升性能.</li>
     * </ul>
     *
     * <h3>与 Stream 版本对比</h3>
     * <ul>
     * <li>可读性更高，易于调试和维护.</li>
     * <li>性能相近，无额外抽象开销.</li>
     * <li>不支持并行流，但在此场景下通常不需要.</li>
     * </ul>
     *
     * <h3>使用示例</h3>
     * <pre>{@code
     * // 假设有以下实体类
     * class Order {
     *     Long orderId;
     *     Long userId;
     *     String product;
     *     LocalDateTime createTime;
     *     // getters...
     * }
     *
     * List<Order> orders = Arrays.asList(
     *     new Order(1L, 100L, "手机", now()),
     *     new Order(2L, 100L, "手机", now().plusHours(1)), // 同一用户买了同一商品，但不同订单
     *     new Order(3L, 100L, "耳机", now()),
     *     new Order(4L, 200L, "手机", now()),
     *     new Order(5L, 200L, "手机", now()) // 重复的商品，但用户不同
     * );
     *
     * // 需求：按用户分组，每组内根据商品去重（每个用户每种商品只保留第一个订单）
     * Map<Long, Set<Order>> result = groupByToSet(
     *     orders,
     *     Order::getUserId,           // 分组 key：用户ID
     *     Order::getProduct,          // 去重 key：商品名称
     *     order -> order.getCreateTime().isAfter(someTime) // 可选过滤
     * );
     *
     * // 结果示例：
     * // {
     * //   100: [Order(1L,100,"手机"), Order(3L,100,"耳机")],
     * //   200: [Order(4L,200,"手机")]
     * // }
     * }</pre>
     *
     * <h3>Stream 版本对比（仅供了解）</h3>
     * <pre>{@code
     * public static <T, O, K> Map<T, Set<O>> groupByToSetStream(
     *         Iterable<O> beanIterable,
     *         Function<O, T> keyExtractor,
     *         Function<O, K> distinctByFunction,
     *         Predicate<O> includePredicate) {
     *
     *     if (isNullOrEmpty(beanIterable)) return emptyMap();
     *     Validate.notNull(keyExtractor, "keyExtractor can't be null!");
     *     Validate.notNull(distinctByFunction, "distinctByFunction can't be null!");
     *
     *     Collector<O, ?, LinkedHashMap<K, O>> downstream = Collectors.toMap(
     *         distinctByFunction,
     *         Function.identity(),
     *         (existing, replacement) -> existing,
     *         LinkedHashMap::new
     *     );
     *     Collector<O, ?, Set<O>> collectingAndThen = Collectors.collectingAndThen(
     *         downstream,
     *         map -> new LinkedHashSet<>(map.values())
     *     );
     *     return toStream(beanIterable)
     *         .filter(obj -> includePredicate == null || includePredicate.test(obj))
     *         .collect(Collectors.groupingBy(keyExtractor, LinkedHashMap::new, collectingAndThen));
     * }
     * }</pre>
     *
     * @param <T>
     *            分组 key 的类型
     * @param <O>
     *            元素类型
     * @param <K>
     *            去重 key 的类型（必须正确实现 {@code equals()} 和 {@code hashCode()}）
     * @param beanIterable
     *            待处理的集合，不能为 {@code null}（但允许为空，此时返回空 Map）
     * @param keyExtractor
     *            分组 key 提取函数，不能为 {@code null}
     * @param distinctByFunction
     *            去重 key 提取函数，不能为 {@code null}
     * @param includePredicate
     *            可选过滤条件，为 {@code null} 时不过滤
     * @return 分组后的 Map，key 为分组 key，value 为每组内基于 distinctByFunction 去重后的 Set（保持首次出现顺序）
     * @throws NullPointerException
     *             如果 {@code keyExtractor} 或 {@code distinctByFunction} 为 {@code null}
     * @since 4.5.3
     */
    public static <T, O, K> Map<T, Set<O>> groupByToSet(
                    Iterable<O> beanIterable,
                    Function<O, T> keyExtractor,
                    Function<O, K> distinctByFunction,
                    Predicate<O> includePredicate){

        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }

        //---------------------------------------------------------------

        Validate.notNull(keyExtractor, "keyExtractor can't be null!");
        Validate.notNull(distinctByFunction, "distinctBy can't be null!");

        //---------------------------------------------------------------
        // 外层 Map：分组 key → 
        // 内层 Map（去重 key → 元素）

        //如果直接用 Map<T, Set<O>> temp = new LinkedHashMap<>(); 需要自己循环判断Set元素是否已经存在, 性能要差 性能差：每次添加都要遍历整个 Set，O(n²) 复杂度
        Map<T, Map<K, O>> temp = new LinkedHashMap<>();
        for (O bean : beanIterable){
            // 1. 过滤
            if (includePredicate != null && !includePredicate.test(bean)){
                continue;
            }

            //---------------------------------------------------------------
            // 2. 提取分组 key 和去重 key
            T groupKey = keyExtractor.apply(bean);
            K distinctKey = distinctByFunction.apply(bean);

            // 3. 获取该分组的内层 Map（不存在则创建） , 借助map来实现 distinctByFunction 去重效果
            Map<K, O> innerMap = temp.computeIfAbsent(
                            groupKey, //
                            k -> new LinkedHashMap<>());

            // 4. 去重：如果 distinctKey 已存在，保留第一个（不覆盖）
            innerMap.putIfAbsent(distinctKey, bean);
        }

        //---------------------------------------------------------------
        // 5. 将内层 Map 的值转为 Set<O>
        Map<T, Set<O>> result = new LinkedHashMap<>();
        for (Map.Entry<T, Map<K, O>> entry : temp.entrySet()){
            result.put(entry.getKey(), new LinkedHashSet<>(entry.getValue().values()));
        }
        return result;
    }

    //---------------------------------------------------------------

    /**
     * 对集合进行过滤, 分组并收集到指定容器中 (内部方法).
     * <p>
     * 此方法封装了常见的 "过滤 + 分组 + 收集" 模式:
     * <ol>
     * <li>如果集合为空, 返回空 Map.</li>
     * <li>校验 keyExtractor 非空.</li>
     * <li>使用 {@link Collectors#groupingBy} 按 keyExtractor 分组, 结果容器使用 {@link LinkedHashMap} 保持顺序.</li>
     * <li>调用 {@link #filterAndCollect(Iterable, Predicate, Collector)} 执行过滤和收集.</li>
     * </ol>
     *
     * @param <O>
     *            元素类型
     * @param <T>
     *            分组 key 的类型
     * @param <M>
     *            每组收集到的结果类型 (如 Set, List 等)
     * @param beanIterable
     *            待处理的集合
     * @param keyExtractor
     *            分组 key 提取函数
     * @param includePredicate
     *            可选过滤条件
     * @param collection
     *            每组的收集器 (downstream collector)
     * @return 分组后的 Map, key 为分组 key, value 为收集结果
     * @since 4.5.3
     */
    private static <O, T, M> Map<T, M> filterAndCollect(
                    Iterable<O> beanIterable,
                    Function<O, T> keyExtractor,
                    Predicate<O> includePredicate,
                    Collector<O, ?, M> collection){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }

        Validate.notNull(keyExtractor, "keyExtractor can't be null!");
        //Collectors.groupingBy底层使用 HashMap作为默认的 Map 实现，而 HashMap不允许 key 为 null。虽然在你的 filterAndCollect方法中指定了 LinkedHashMap::new作为 Map 工厂，但 groupingBy在内部处理时仍然会对 null key 进行检查并抛出 NPE。
        Collector<O, ?, LinkedHashMap<T, M>> collector = Collectors.groupingBy(keyExtractor, LinkedHashMap::new, collection);
        return filterAndCollect(beanIterable, includePredicate, collector);
    }

    //---------------------------------------------------------------

    /**
     * 使用 Lambda 和 Stream 实现分组,保留重复键的第一个元素,提高了类型安全性和性能,避免了反射开销.
     *
     * <p>
     * 接受一个Iterable, 使用一个函数来提取键分组时,如果键重复,只保留第一个对象
     * </p>
     * 
     * <h3>对比 {@link #groupOne(Iterable, String)} 原方法的优势:</h3>
     * <blockquote>
     * <ol>
     * <li>代码简洁性: 去显式迭代和反射代码，改用声明式 Stream 操作.</li>
     * <li>类型安全: 编译时检查键提取函数，避免运行时反射错误.</li>
     * <li>易扩展性: 支持任意键提取逻辑 (如嵌套属性 user -> user.getDepartment().getId())</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>返回的{@link LinkedHashMap},key是 <code>iterable</code>中的元素对象中 <code>propertyName</code>的值,value是
     * <code>beanIterable</code>中的元素对象;</li>
     *
     * <li>顺序是 <code>beanIterable</code> <code>propertyName</code>的值 顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}</li>
     *
     * <li>间接的可以做到基于某个属性值去重的效果</li>
     * <li>如果value需要是集合的话,可以调用 {@link #group(Iterable, String)}方法</li>
     * </ol>
     * </blockquote>
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <pre class="code">
     * List{@code <User>} list = new ArrayList{@code <>}();
     * list.add(new User("张飞", 23));
     * list.add(new User("刘备", 25));
     * list.add(new User("刘备", 30));
     *
     * Map{@code <String, User>} map = GroupUtil.groupOne(list, User::getName);
     * log.info(JsonUtil.format(map));
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
     * {
        "张飞":         {
            "age": 23,
            "name": "张飞"
        },
        "刘备":         {
            "age": 25,
            "name": "刘备"
        }
    }
     * </pre>
     *
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param keyExtractor
     *            键提取函数 (例如：User::getId 或 obj -> obj.getProperty());在Lambda版本中, 为了更好的类型安全和性能,应该使用Function<O, T>而不是反射
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>keyExtractor</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #group(Iterable, String)
     * @since 4.5.0
     */
    public static <T, O> Map<T, O> groupOne(Iterable<O> beanIterable,Function<O, T> keyExtractor){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }
        Validate.notNull(keyExtractor, "keyExtractor can't be null/empty!");
        //---------------------------------------------------------------
        // 使用 Stream 处理分组和去重 将一个 Spliterator<T>转换并封装成一个 Stream<T>对象
        Collector<O, ?, LinkedHashMap<T, O>> collector = Collectors.toMap(
                        keyExtractor, // keyMapper 键提取器
                        Function.identity(), //  valueMapper 值提取器  对象本身,即Function.identity().
                        (existing,replacement) -> existing, // mergeFunction 当键重复时,保留第一个,即(existing, replacement) -> existing.
                        LinkedHashMap::new // Supplier 使用 LinkedHashMap 保持插入顺序
        );
        return filterAndCollect(beanIterable, null, collector);
    }

    /**
     * 循环 <code>iterable</code>,以元素的 <code>propertyName</code>属性值为key,元素为value,封装成map返回(map只put第一个匹配的元素,<b>后面出现相同的元素将会忽略</b>).
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>返回的{@link LinkedHashMap},key是 <code>iterable</code>中的元素对象中 <code>propertyName</code>的值,value是
     * <code>beanIterable</code>中的元素对象;</li>
     *
     * <li>顺序是 <code>beanIterable</code> <code>propertyName</code>的值 顺序,如果需要排序,可自行调用 {@link SortUtil#sortMapByKeyAsc(Map)},
     * {@link SortUtil#sortMapByKeyDesc(Map)}, {@link SortUtil#sortMapByValueAsc(Map)}, {@link SortUtil#sortMapByValueDesc(Map)}或者,
     * {@link SortUtil#sortMap(Map, java.util.Comparator)}</li>
     *
     * <li>间接的可以做到基于某个属性值去重的效果</li>
     * <li>如果value需要是集合的话,可以调用 {@link #group(Iterable, String)}方法</li>
     * </ol>
     * </blockquote>
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <pre class="code">
     * List{@code <User>} list = new ArrayList{@code <>}();
     * list.add(new User("张飞", 23));
     * list.add(new User("刘备", 25));
     * list.add(new User("刘备", 30));
     *
     * Map{@code <String, User>} map = GroupUtil.groupOne(list, "name");
     * log.info(JsonUtil.format(map));
     * </pre>
     *
     * <b>返回:</b>
     *
     * <pre class="code">
     * {
        "张飞":         {
            "age": 23,
            "name": "张飞"
        },
        "刘备":         {
            "age": 25,
            "name": "刘备"
        }
    }
     * </pre>
     *
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param propertyName
     *            泛型O对象指定的属性名称,Possibly indexed and/or nested name of the property to be modified,参见
     *            <a href="../bean/BeanUtil.html#propertyName">propertyName</a>
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>propertyName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>propertyName</code> 是blank,抛出 {@link IllegalArgumentException}
     * @see #group(Iterable, String)
     * @since 1.0.8
     */
    public static <T, O> Map<T, O> groupOne(Iterable<O> beanIterable,String propertyName){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }
        Validate.notBlank(propertyName, "propertyName can't be null/empty!");
        //---------------------------------------------------------------
        Map<T, O> map = newLinkedHashMap(size(beanIterable));
        for (O o : beanIterable){
            T key = PropertyUtil.getProperty(o, propertyName);
            if (!map.containsKey(key)){
                map.put(key, o);
            }else{
                if (log.isTraceEnabled()){
                    log.trace("map:[{}] already has the key:[{}],ignore!", map.keySet(), key);
                }
            }
        }
        return map;
    }
    //---------------------------------------------------------------

    /**
     * 对集合进行过滤并收集到指定 Collector 中 (最底层方法).
     * <p>
     * 此方法执行实际的 Stream 操作:
     * <ul>
     * <li>将 Iterable 转为 Stream.</li>
     * <li>应用可选的过滤条件 (如果 includePredicate 为 null 则不过滤).</li>
     * <li>使用传入的 Collector 进行收集.</li>
     * </ul>
     *
     * @param <O>
     *            元素类型
     * @param <M>
     *            中间累加器类型 (由 Collector 决定)
     * @param <T>
     *            最终结果类型 (Map 的 value 类型)
     * @param beanIterable
     *            待处理的集合
     * @param includePredicate
     *            可选过滤条件, 为 null 时不过滤
     * @param collector
     *            收集器 (必须产生 {@code LinkedHashMap<T, M>} 类型的结果)
     * @return 收集后的 Map
     * @since 4.5.3
     */
    private static <O, M, T> Map<T, M> filterAndCollect(
                    Iterable<O> beanIterable,
                    Predicate<O> includePredicate,
                    Collector<O, ?, LinkedHashMap<T, M>> collector){

        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }

        return toStream(beanIterable)//
                        .filter(obj -> includePredicate == null || includePredicate.test(obj))//
                        .collect(collector);
    }
}
