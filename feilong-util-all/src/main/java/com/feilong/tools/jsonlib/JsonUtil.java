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
package com.feilong.tools.jsonlib;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.JsonToJavaConfig;
import com.feilong.json.jsonlib.processor.SensitiveWordsJsonValueProcessor;
import com.feilong.json.lib.json.JSONArray;
import com.feilong.json.lib.json.JSONObject;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.5
 * @deprecated 保留只为兼容,pls use {@link com.feilong.json.jsonlib.JsonUtil}
 */
@Deprecated
public final class JsonUtil{

    /** Don't let anyone instantiate this class. */
    private JsonUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    // [start] format

    /**
     * 将对象 <code>obj</code> 格式化成json字符串.
     * 
     * <p>
     * 该方法还可以格式化json字符串成缩进形式的格式
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     *     {"userAddresseList":[{"address":"上海市闸北区万荣路1188号H座109-118室"},{"address":"上海市闸北区阳城路280弄25号802室(阳城贵都)"}],"userAddresses":[{"address":"上海市闸北区万荣路1188号H座109-118室"},{"address":"上海市闸北区阳城路280弄25号802室(阳城贵都)"}],"date":"2016-06-09 17:40:28","password":"******","id":8,"nickName":[],"age":0,"name":"feilong","money":99999999,"attrMap":null,"userInfo":{"age":10},"loves":["桔子","香蕉"]}
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     *     {
     *             "userAddresseList":         [
     *                 {"address": "上海市闸北区万荣路1188号H座109-118室"},
     *                 {"address": "上海市闸北区阳城路280弄25号802室(阳城贵都)"}
     *             ],
     *             "userAddresses":         [
     *                 {"address": "上海市闸北区万荣路1188号H座109-118室"},
     *                 {"address": "上海市闸北区阳城路280弄25号802室(阳城贵都)"}
     *             ],
     *             "date": "2016-06-09 17:40:28",
     *             "password": "******",
     *             "id": 8,
     *             "nickName": [],
     *             "age": 0,
     *             "name": "feilong",
     *             "money": 99999999,
     *             "attrMap": null,
     *             "userInfo": {"age": 10},
     *             "loves":         [
     *                 "桔子",
     *                 "香蕉"
     *             ]
     *         }
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>关于 <code>indent</code>缩进:</h3>
     * 
     * <blockquote>
     * <p>
     * 默认使用 toString(4,4) 缩进
     * </p>
     * 
     * <p>
     * 如果不需要 <code>indent</code>缩进,你可以调用 {@link #format(Object, int, int)}或者 {@link #format(Object, JavaToJsonConfig, int, int)}
     * </p>
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @return 如果 <code>obj</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see #format(Object, JavaToJsonConfig)
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#format(Object)}
     */
    @Deprecated
    public static String format(Object obj){
        return com.feilong.json.jsonlib.JsonUtil.format(obj);
    }

    /**
     * 有些map 值很复杂,比如带有request信息, 这样的map转成json很可能由于一些级联关系而抛异常.
     * 
     * <h3>注意:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>此方法 将inputMap转成 simpleMap(<span style="color:red">原始inputMap不会变更</span>)</li>
     * <li>此方法转换的simpleMap是 {@link TreeMap}类型,转换的json key经过排序的</li>
     * </ul>
     * </blockquote>
     * 
     * <h3>转换规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>如果value 是isPrimitiveOrWrapper类型, 那么会直接取到值 设置到 新的simpleMap中</li>
     * <li>否则 使用 {@link String#valueOf(Object)} 转换到simpleMap中</li>
     * </ul>
     * </blockquote>.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param inputMap
     *            the input map
     * @return 如果 <code>inputMap</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @since 1.3.0
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#formatSimpleMap(Map)}
     */
    @Deprecated
    public static <K, V> String formatSimpleMap(Map<K, V> inputMap){
        return com.feilong.json.jsonlib.JsonUtil.formatSimpleMap(inputMap);
    }

    /**
     * 有些map 值很复杂,比如带有request信息, 这样的map转成json很可能由于一些级联关系而抛异常.
     * 
     * <h3>注意:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>此方法 将inputMap转成 simpleMap(<span style="color:red">原始inputMap不会变更</span>)</li>
     * <li>此方法转换的simpleMap是 {@link TreeMap}类型,转换的json key经过排序的</li>
     * </ul>
     * </blockquote>
     * 
     * <h3>转换规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>如果value是isPrimitiveOrWrapper类型,那么会直接取到值设置到新的simpleMap中</li>
     * <li>否则使用{@link String#valueOf(Object)}转换到simpleMap中</li>
     * </ul>
     * </blockquote>.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param inputMap
     *            the input map
     * @param allowFormatClassTypes
     *            除了基本类型,数组之外允许的类型,请确保该类型可以被json format输出
     * @return 如果 <code>inputMap</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @since 1.3.0
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use
     *             {@link com.feilong.json.jsonlib.JsonUtil#formatSimpleMap(Map, Class...)}
     */
    @Deprecated
    public static <K, V> String formatSimpleMap(Map<K, V> inputMap,Class<?>...allowFormatClassTypes){
        return com.feilong.json.jsonlib.JsonUtil.formatSimpleMap(inputMap, allowFormatClassTypes);
    }

    /**
     * 将对象格式化 成json字符串(<b>排除</b>指定名称的属性 <code>excludes</code>),并且 toString(4, 4) 输出.
     * 
     * <p>
     * 和该方法对应的有,仅仅<b>包含</b>某些属性,see {@link #formatWithIncludes(Object, String...)}
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User user = new User();
     * 
     * user.setPassword("123456");
     * user.setId(8L);
     * user.setName("feilong");
     * user.setDate(now());
     * user.setMoney(toBigDecimal("99999999.00"));
     * 
     * user.setLoves(toArray("桔子", "香蕉"));
     * user.setUserInfo(new UserInfo(10));
     * 
     * UserAddress userAddress1 = new UserAddress("上海市闸北区万荣路1188号H座109-118室");
     * UserAddress userAddress2 = new UserAddress("上海市闸北区阳城路280弄25号802室(阳城贵都)");
     * 
     * user.setUserAddresses(toArray(userAddress1, userAddress2));
     * user.setUserAddresseList(toList(userAddress1, userAddress2));
     * 
     * LOGGER.debug(JsonUtil.format(USER, toArray("name", "loves", "attrMap", "userInfo", "userAddresses")));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     *  {
        "userAddresseList":         [
            {"address": "上海市闸北区万荣路1188号H座109-118室"},
            {"address": "上海市闸北区阳城路280弄25号802室(阳城贵都)"}
        ],
        "date": "2016-07-17 16:04:35",
        "password": "******",
        "id": 8,
        "age": 0,
        "money": 99999999,
        "nickNames": []
    }
     * </pre>
     * 
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param excludes
     *            排除需要序列化成json的属性,如果 excludes isNotNullOrEmpty,那么不会setExcludes
     * @return 如果 <code>obj</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see <a href="http://feitianbenyue.iteye.com/blog/2046877">java.lang.ClassCastException: JSON keys must be strings</a>
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use
     *             {@link com.feilong.json.jsonlib.JsonUtil#format(Object, String[])}
     */
    @Deprecated
    public static String format(Object obj,String[] excludes){
        return com.feilong.json.jsonlib.JsonUtil.format(obj, excludes);
    }

    /**
     * 将对象格式化 成json字符串(<b>排除</b>指定名称的属性 <code>excludes</code>),并且按照指定的缩进(<code>indentFactor</code>和 <code>indent</code>) 输出.
     * 
     * <p>
     * 和该方法对应的有,仅仅<b>包含</b>某些属性,see {@link #formatWithIncludes(Object, String...)}
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User user = new User();
     * 
     * user.setPassword("123456");
     * user.setId(8L);
     * user.setName("feilong");
     * user.setDate(now());
     * user.setMoney(toBigDecimal("99999999.00"));
     * 
     * user.setLoves(toArray("桔子", "香蕉"));
     * user.setUserInfo(new UserInfo(10));
     * 
     * UserAddress userAddress1 = new UserAddress("上海市闸北区万荣路1188号H座109-118室");
     * UserAddress userAddress2 = new UserAddress("上海市闸北区阳城路280弄25号802室(阳城贵都)");
     * 
     * user.setUserAddresses(toArray(userAddress1, userAddress2));
     * user.setUserAddresseList(toList(userAddress1, userAddress2));
     * 
     * LOGGER.debug(JsonUtil.format(USER, toArray("name", "loves", "attrMap", "userInfo", "userAddresses"), 0, 0));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    {"userAddresseList":[{"address":"上海市闸北区万荣路1188号H座109-118室"},{"address":"上海市闸北区阳城路280弄25号802室(阳城贵都)"}],"date":"2016-07-17 16:05:34","password":"******","id":8,"age":0,"money":99999999,"nickNames":[]}
     * </pre>
     * 
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param excludes
     *            排除需要序列化成json的属性,如果 excludes isNotNullOrEmpty,那么不会setExcludes
     * @param indentFactor
     *            the indent factor
     * @param indent
     *            the indent
     * @return 如果 <code>obj</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see #format(Object, JavaToJsonConfig)
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#format(Object, String[],
     *             int, int)}
     */
    @Deprecated
    public static String format(Object obj,String[] excludes,int indentFactor,int indent){
        return com.feilong.json.jsonlib.JsonUtil.format(obj, excludes, indentFactor, indent);
    }

    /**
     * 将对象格式化成json字符串(<b>仅仅包含</b>指定名称的属性 <code>includes</code>).
     * 
     * <p>
     * 和该方法对应的有,仅仅<b>排除</b>某些属性,see {@link #format(Object, String[])}以及 {@link #format(Object, String[], int, int)}
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User user1 = new User("feilong1", 24);
     * user1.setNickNames(toArray("xin.jin", "shuai.ge"));
     * User user2 = new User("feilong2", 240);
     * user2.setNickNames(toArray("xin.jin", "shuai.ge"));
     * 
     * List{@code <User>} list = toList(user1, user2);
     * 
     * LOGGER.debug(JsonUtil.formatWithIncludes(list, "name", "age"));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    [{
                "age": 24,
                "name": "feilong1"
            },
                    {
                "age": 240,
                "name": "feilong2"
            }
        ]
     * </pre>
     * 
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param includes
     *            the includes
     * @return 如果 <code>obj</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see #format(Object, JavaToJsonConfig)
     * @since 1.0.8
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use
     *             {@link com.feilong.json.jsonlib.JsonUtil#formatWithIncludes(Object, String...)}
     */
    @Deprecated
    public static String formatWithIncludes(Object obj,final String...includes){
        return com.feilong.json.jsonlib.JsonUtil.formatWithIncludes(obj, includes);
    }

    /**
     * 将<code>obj</code>格式化成json字符串,并且按照指定的缩进(<code>indentFactor</code>和 <code>indent</code>) 输出.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * //查询{@code  >10} 的元素
     * Predicate{@code <Integer>} predicate = new ComparatorPredicate{@code <Integer>}(10, ComparatorUtils.{@code <Integer>} naturalComparator(), Criterion.LESS);
     * 
     * List{@code <Integer>} result = CollectionsUtil.select(toList(1, 5, 10, 30, 55, 88, 1, 12, 3), predicate);
     * LOGGER.debug(JsonUtil.format(result, 0, 0));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [30,55,88,12]
     * </pre>
     * 
     * <hr>
     * 
     * <pre class="code">
     * LOGGER.debug(JsonUtil.format(result, 4, 4));// = LOGGER.debug(JsonUtil.format(result))
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * 30,
     * 55,
     * 88,
     * 12
     * ]
     * </pre>
     * 
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param indentFactor
     *            the indent factor
     * @param indent
     *            the indent
     * @return the string
     * @since 1.2.2
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#format(Object, int,
     *             int)}
     */
    @Deprecated
    public static String format(Object obj,int indentFactor,int indent){
        return com.feilong.json.jsonlib.JsonUtil.format(obj, indentFactor, indent);
    }

    /**
     * 使用配置 <code>JavaToJsonConfig</code> 来格式化成json字符串.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User user = new User("feilong1", 24);
     * user.setPassword("123456");
     * user.setMoney(toBigDecimal("99999999.00"));
     * 
     * Map{@code <String, JsonValueProcessor>} propertyNameAndJsonValueProcessorMap = new HashMap{@code <>}();
     * propertyNameAndJsonValueProcessorMap.put("password", new SensitiveWordsJsonValueProcessor());
     * propertyNameAndJsonValueProcessorMap.put("money", new BigDecimalJsonValueProcessor());
     * 
     * JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
     * javaToJsonConfig.setPropertyNameAndJsonValueProcessorMap(propertyNameAndJsonValueProcessorMap);
     * 
     * LOGGER.debug(JsonUtil.format(user, javaToJsonConfig));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {
     * "userAddresseList": [],
     * "userAddresses": [],
     * "date": null,
     * "password": "******",
     * "id": 0,
     * "age": 24,
     * "name": "feilong1",
     * "money": "99999999.00",
     * "attrMap": null,
     * "userInfo": {"age": 0},
     * "nickNames": [],
     * "loves": []
     * }
     * </pre>
     * 
     * <p>
     * 当然上述示例中,<code>password</code>属性默认就是由 {@link SensitiveWordsJsonValueProcessor} 来处理
     * </p>
     * 
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param javaToJsonConfig
     *            the json format config
     * @return 如果 <code>obj</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @since 1.2.2
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#format(Object,
     *             JavaToJsonConfig)}
     */
    @Deprecated
    public static String format(Object obj,JavaToJsonConfig javaToJsonConfig){
        return com.feilong.json.jsonlib.JsonUtil.format(obj, javaToJsonConfig);
    }

    /**
     * Format.
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param javaToJsonConfig
     *            the json format config
     * @param indentFactor
     *            the indent factor
     * @param indent
     *            the indent
     * @return 如果 <code>obj</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @since 1.2.2
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#format(Object,
     *             JavaToJsonConfig, int, int)}
     */
    @Deprecated
    public static String format(Object obj,JavaToJsonConfig javaToJsonConfig,int indentFactor,int indent){
        return com.feilong.json.jsonlib.JsonUtil.format(obj, javaToJsonConfig, indentFactor, indent);
    }

    /**
     * since 1.10.7 change package 'com.feilong.json', pls use
     * {@link com.feilong.json.jsonlib.JsonUtil#formatObjectFieldsNameAndValueMap(Object)}.
     *
     * @param obj
     *            可以是Java bean
     * @return the string
     * @since 1.5.6
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use
     *             {@link com.feilong.json.jsonlib.JsonUtil#formatObjectFieldsNameAndValueMap(Object)}
     */
    @Deprecated
    public static String formatObjectFieldsNameAndValueMap(Object obj){
        return com.feilong.json.jsonlib.JsonUtil.formatObjectFieldsNameAndValueMap(obj);
    }

    // [end]

    //---------------------------------------------------------------

    // [start]toArray

    /**
     * 把一个json数组串转换成实体数组,数组元素的属性可以含有另外实例Bean.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * 比如有 <b>Person.class</b>,代码如下
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     // setter /getter 略
     * }
     * </pre>
     * 
     * 此时,
     * 
     * <pre class="code">
     * String json = "[{'name':'get'},{'name':'set'}]";
     * Person[] persons = JsonUtil.toArray(json, new JsonToJavaConfig(Person.class));
     * 
     * LOGGER.debug(JsonUtil.format(persons));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    [{
            "dateAttr": null,
            "name": "get"
        },
                {
            "dateAttr": null,
            "name": "set"
        }]
     * </pre>
     * 
     * 又如,又有<b>MyBean.class</b>
     * 
     * <pre class="code">
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private List{@code <Object>} data = new ArrayList{@code <>}();
     *     //setter /getter 略
     * }
     * </pre>
     * 
     * 下列的代码:
     * 
     * <pre class="code">
     * String json = "[{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]";
     * Map{@code <String, Class<?>>} classMap = new HashMap{@code <>}();
     * classMap.put("data", Person.class);
     * 
     * MyBean[] myBeans = JsonUtil.toArray(json, new JsonToJavaConfig(MyBean.class, classMap));
     * LOGGER.debug(JsonUtil.format(myBeans));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    [{
            "id": 0,
            "data": [{
                "dateAttr": null,
                "name": "get"
            }]
        },{
            "id": 0,
            "data": [{
                "dateAttr": null,
                "name": "set"
            }]
        }]
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            e.g. [{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]
     * @param jsonToJavaConfig
     *            the json to java config
     * @return 如果 <code>json</code> 是null,返回 null<br>
     *         如果 <code>jsonToJavaConfig</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>jsonToJavaConfig.getRootClass()</code> 是null,抛出 {@link NullPointerException}<br>
     * @see com.feilong.json.lib.json.JSONArray#fromObject(Object)
     * @see com.feilong.json.lib.json.JSONArray#getJSONObject(int)
     * @see #toBean(Object, JsonToJavaConfig)
     * @see java.lang.reflect.Array#newInstance(Class, int)
     * @since 1.9.4
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#toArray(Object,
     *             JsonToJavaConfig)}
     */
    @Deprecated
    public static <T> T[] toArray(Object json,JsonToJavaConfig jsonToJavaConfig){
        return com.feilong.json.jsonlib.JsonUtil.toArray(json, jsonToJavaConfig);
    }

    // [end]

    //---------------------------------------------------------------

    // [start]toList
    /**
     * 把一个json数组串转换成集合,集合里存放的为实例Bean.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * 如果有以下的bean <b>Person.class</b>:
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     *     //setter /getter 略
     * }
     * </pre>
     * 
     * 使用下列代码:
     * 
     * <pre class="code">
     * String json = "[{'name':'get'},{'name':'set'}]";
     * List{@code <Person>} list = JsonUtil.toList(json, Person.class);
     * 
     * LOGGER.info(JsonUtil.format(list));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
       [{
               "dateAttr": null,
               "name": "get"
           },
                   {
               "dateAttr": null,
               "name": "set"
           }
       ]
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            e.g. [{'name':'get'},{'name':'set'}]
     * @param rootClass
     * @return 如果<code>json</code> 是null,那么返回 null<br>
     *         如果 <code>rootClass()</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #toList(Object, JsonToJavaConfig)
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#toList(Object, Class) }
     */
    @Deprecated
    public static <T> List<T> toList(Object json,Class<T> rootClass){
        return com.feilong.json.jsonlib.JsonUtil.toList(json, rootClass);
    }

    /**
     * 把一个json数组串转换成集合,且集合里的对象的属性含有另外实例Bean.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * 比如有 <b>Person.class</b>,代码如下
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     // setter /getter 略
     * }
     * </pre>
     * 
     * 又有<b>MyBean.class</b>
     * 
     * <pre class="code">
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private List{@code <Object>} data = new ArrayList{@code <>}();
     *     //setter /getter 略
     * }
     * </pre>
     * 
     * 下列的代码:
     * 
     * <pre class="code">
     * String json = "[{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]";
     * Map{@code <String, Class<?>>} classMap = new HashMap{@code <>}();
     * classMap.put("data", Person.class);
     * 
     * List{@code <MyBean>} list = JsonUtil.toList(json, new JsonToJavaConfig(MyBean.class, classMap));
     * LOGGER.debug(JsonUtil.format(list));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     *      [{
     *              "id": 0,
     *              "data": [            {
     *                  "dateAttr": null,
     *                  "name": "get"
     *              }]
     *          },
     *                  {
     *              "id": 0,
     *              "data": [            {
     *                  "dateAttr": null,
     *                  "name": "set"
     *              }]
     *       }]
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            e.g. [{'data':[{'name':'get'}]},{'data':[{'name':'set'}]}]
     * @param jsonToJavaConfig
     *            the json to java config
     * @return 如果 <code>json</code> 是null,返回 null<br>
     *         如果 <code>jsonToJavaConfig</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>jsonToJavaConfig.getRootClass()</code> 是null,抛出 {@link NullPointerException}<br>
     * 
     * @see com.feilong.json.lib.json.JSONArray#getJSONObject(int)
     * @see com.feilong.json.lib.json.JSONArray#fromObject(Object)
     * @see #toBean(Object, JsonToJavaConfig)
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#toList(Object,
     *             JsonToJavaConfig)}
     */
    @Deprecated
    public static <T> List<T> toList(Object json,JsonToJavaConfig jsonToJavaConfig){
        return com.feilong.json.jsonlib.JsonUtil.toList(json, jsonToJavaConfig);
    }

    // [end]

    //---------------------------------------------------------------

    // [start]toMap

    /**
     * 将简单的json字符串转成<code>map</code>.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String json = "{'brandCode':'UA'}";
     * Map{@code <String, Object>} map = JsonUtil.toMap(json);
     * LOGGER.info(JsonUtil.format(map));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * key是 brandCode,value 是 UA 的map
     * </pre>
     * 
     * <hr>
     * 
     * <pre class="code">
     * Map{@code <String, Integer>} map = JsonUtil.toMap("{'brandCode':55555}");
     * LOGGER.info(JsonUtil.format(map));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {"brandCode": 55555}
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ol>
     * <li>返回的map是 {@link LinkedHashMap}</li>
     * 
     * <li>
     * <p>
     * 由于泛型 unchecked,所以可能返回的结果,泛型里面有其他类型的值
     * </p>
     * 
     * <pre class="code">
     * Map{@code <String, Long>} map = JsonUtil.toMap("{'brandCode':55.555}");
     * LOGGER.debug(JsonUtil.format(map));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {"brandCode": 55.555}
     * </pre>
     * 
     * </li>
     * 
     * </ol>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            the json
     * @return 如果 <code>json</code> 是null或者empty,返回 {@link Collections#emptyMap()}
     * @see #toMap(Object, JsonToJavaConfig)
     * @since 1.5.0
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#toMap(Object)}
     */
    @Deprecated
    public static <T> Map<String, T> toMap(Object json){
        return com.feilong.json.jsonlib.JsonUtil.toMap(json);
    }

    /**
     * 把json对象串转换成map对象,map对象里可以存放的其他实体Bean还含有另外实体Bean.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * 比如有 Person.class,代码如下
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     // setter /getter 略
     * }
     * </pre>
     * 
     * 此时,
     * 
     * <pre class="code">
     * String json = "{'data1':{'name':'get'},'data2':{'name':'set'}}";
     * Map{@code <String, Person>} map = JsonUtil.toMap(json, new JsonToJavaConfig(Person.class));
     * 
     * LOGGER.debug(JsonUtil.format(map));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    {
        "data1":         {
            "dateAttr": null,
            "name": "get"
        },
        "data2":         {
            "dateAttr": null,
            "name": "set"
        }
    }
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            e.g. {'data1':{'name':'get'},'data2':{'name':'set'}}
     * @param jsonToJavaConfig
     *            the json to java config
     * @return 如果 <code>json</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     *         如果 <code>rootClass</code> 是null,那么直接将json里面的value 作为map 的value
     * @see com.feilong.json.lib.json.JSONObject#keys()
     * @see #toBean(Object, JsonToJavaConfig)
     * @since 1.9.2 use LinkedHashMap instead of HashMap
     * @since 1.9.4
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#toMap(Object,
     *             JsonToJavaConfig)}
     */
    @Deprecated
    public static <T> Map<String, T> toMap(Object json,JsonToJavaConfig jsonToJavaConfig){
        return com.feilong.json.jsonlib.JsonUtil.toMap(json, jsonToJavaConfig);
    }

    // [end]

    //---------------------------------------------------------------
    // [start]toBean

    /**
     * json串,转换成实体对象.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * 比如有 Person.class,代码如下
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     // setter /getter 略
     * }
     * </pre>
     * 
     * 此时,
     * 
     * <pre class="code">
     * String json = "{'name':'get','dateAttr':'2009-11-12'}";
     * LOGGER.debug(JsonUtil.format(JsonUtil.toBean(json, Person.class)));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {
     * "dateAttr": "2009-11-12 00:00:00",
     * "name": "get"
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            e.g. {'name':'get','dateAttr':'2009-11-12'}<br>
     *            可以是 json字符串,也可以是JSONObject<br>
     *            Accepts JSON formatted strings, Maps, DynaBeans and JavaBeans. <br>
     * @return 如果<code>json</code> 是null,那么返回 null <br>
     *         如果 <code>rootClass</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #toBean(Object, JsonToJavaConfig)
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use {@link com.feilong.json.jsonlib.JsonUtil#toBean(Object, Class) }
     */
    @Deprecated
    public static <T> T toBean(Object json,Class<T> rootClass){
        return com.feilong.json.jsonlib.JsonUtil.toBean(json, rootClass);
    }

    /**
     * 从json串转换成 javabean 实体对象,支持实体集合属性可能存有另外实体Bean.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * 如果有以下的bean:
     * 
     * <pre class="code">
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private List{@code <Object>} data = new ArrayList{@code <>}();
     *     //setter /getter 略
     * }
     * </pre>
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     *     //setter /getter 略
     * }
     * </pre>
     * 
     * 使用以下的代码:
     * 
     * <pre class="code">
     * String json = "{'data':[{'name':'get'},{'name':'set'}],'id':5}";
     * Map{@code <String, Class<?>>} classMap = new HashMap{@code <>}();
     * classMap.put("data", Person.class);
     * 
     * JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(MyBean.class);
     * jsonToJavaConfig.setClassMap(classMap);
     * 
     * MyBean myBean = JsonUtil.toBean(json, jsonToJavaConfig);
     * LOGGER.debug(JsonUtil.format(myBean));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {
     *              "id": 5,
     *              "data":[{
     *                      "dateAttr": null,
     *                      "name": "get"
     *                  },{
     *                      "dateAttr": null,
     *                      "name": "set"
     *                  }
     *              ]
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param json
     *            e.g. {'data':[{'name':'get'},{'name':'set'}]}
     * @param jsonToJavaConfig
     *            the json to java config
     * @return 如果<code>json</code> 是null,那么返回 null<br>
     *         如果 <code>jsonToJavaConfig</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>jsonToJavaConfig.getRootClass()</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.9.4
     * @deprecated since 1.10.7 change package 'com.feilong.json', pls use
     *             {@link com.feilong.json.jsonlib.JsonUtil#toBean(Object, JsonToJavaConfig)}
     */
    @Deprecated
    public static <T> T toBean(Object json,JsonToJavaConfig jsonToJavaConfig){
        return com.feilong.json.jsonlib.JsonUtil.toBean(json, jsonToJavaConfig);
    }

    // [end]
}