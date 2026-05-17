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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

import com.feilong.core.Validate;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.predicate.BeanPredicateUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 专注于group 分组
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
     * @see #group(Iterable, String, Predicate)
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
    public static <T, O> Map<T, List<O>> group(Iterable<O> beanIterable,final String propertyName,Predicate<O> includePredicate){
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
    public static <T, O> Map<T, List<O>> group(Iterable<O> beanIterable,Predicate<O> includePredicate,Transformer<O, T> keyTransformer){
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
     * 通过key提取函数分组，可包含过滤条件
     * 循环 <code>beanIterable</code>,以 <code>keyExtractor</code>提取器属性值为key,相同值的元素组成list作为value,封装成map返回.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     *
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
     *            注意,此处的T是属性值,Object类型,如果从excel中读取的类型是String,那么不能简简单单的使用Integer来接收,不能强制转换
     * @param <O>
     *            the generic type
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @param keyExtractor
     *            提取key
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>keyExtractor</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 4.5.2
     */
    public static <T, O> Map<T, List<O>> groupBy(
                    Iterable<O> beanIterable,
                    Function<O, T> keyExtractor,
                    java.util.function.Predicate<O> includePredicate){
        if (isNullOrEmpty(beanIterable)){
            return emptyMap();
        }

        Validate.notNull(keyExtractor, "keyExtractor can't be null!");

        // 使用Stream API和lambda表达式重写
        return toStream(beanIterable)//
                        .filter(obj -> includePredicate == null || includePredicate.test(obj))//
                        .collect(
                                        Collectors.groupingBy(
                                                        keyExtractor,
                                                        LinkedHashMap::new, // 保持插入顺序
                                                        Collectors.toList() // 收集到List
                                        ));
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
        return toStream(beanIterable).collect(
                        Collectors.toMap(
                                        keyExtractor, // keyMapper 键提取器
                                        Function.identity(), //  valueMapper 值提取器  对象本身,即Function.identity()。
                                        (existing,replacement) -> existing, // mergeFunction 当键重复时,保留第一个,即(existing, replacement) -> existing。
                                        LinkedHashMap::new // Supplier 使用 LinkedHashMap 保持插入顺序
                        ));
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
}
