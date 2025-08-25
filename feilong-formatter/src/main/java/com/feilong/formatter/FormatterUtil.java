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
package com.feilong.formatter;

import java.util.Map;

import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.lib.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 处理将对象格式化的工具类.
 * 
 * <p>
 * 提供静态的方法,方便调用或者方便使用 import static
 * </p>
 * 
 * <p>
 * 简单的table 会渲染标题 和分隔符,不包含 padding margin等设定,也不支持复杂的组合表格设置
 * </p>
 * 
 * <h3>初衷:</h3>
 * 
 * <blockquote>
 * <p>
 * 在做开发的时候,我们经常会记录一些日志,使用log,但是对于 list map ,bean的日志输出一直很难做得很好,为了格式化输出,我们可能会使用 json来输出,比如:
 * </p>
 * 
 * <pre class="code">
 * List{@code <Address>} list = toList(
 *                 new Address("china", "shanghai", "wenshui wanrong.lu 888", "216000"),
 *                 new Address("china", "beijing", "wenshui wanrong.lu 666", "216001"),
 *                 new Address("china", "nantong", "wenshui wanrong.lu 222", "216002"),
 *                 new Address("china", "tianjing", "wenshui wanrong.lu 999", "216600"));
 * 
 * log.debug(JsonUtil.format(list));
 * </pre>
 * 
 * 结果:
 * 
 * <pre class="code">
[{
            "zipCode": "wenshui wanrong.lu 888",
            "addr": "216000",
            "country": "china",
            "city": "shanghai"
        },
                {
            "zipCode": "wenshui wanrong.lu 666",
            "addr": "216001",
            "country": "china",
            "city": "beijing"
        },
                {
            "zipCode": "wenshui wanrong.lu 222",
            "addr": "216002",
            "country": "china",
            "city": "nantong"
        },
                {
            "zipCode": "wenshui wanrong.lu 999",
            "addr": "216600",
            "country": "china",
            "city": "tianjing"
}]
 * </pre>
 * 
 * <p>
 * 可以看出,结果难以阅读,如果list元素更多一些,那么更加难以阅读;
 * </p>
 * 
 * </blockquote>
 * 
 * 
 * <h3>解决方案:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 这个时候可以使用
 * </p>
 * 
 * <pre class="code">
 * List{@code <Address>} list = toList(
 *                 new Address("china", "shanghai", "wenshui wanrong.lu 888", "216000"),
 *                 new Address("china", "beijing", "wenshui wanrong.lu 666", "216001"),
 *                 new Address("china", "nantong", "wenshui wanrong.lu 222", "216002"),
 *                 new Address("china", "tianjing", "wenshui wanrong.lu 999", "216600"));
 * 
 * log.debug(FormatterUtil.formatToSimpleTable(list));
 * </pre>
 * 
 * 
 * 结果:
 * 
 * <pre class="code">
addr   city     country zipCode                
------ -------- ------- ---------------------- 
216000 shanghai china   wenshui wanrong.lu 888 
216001 beijing  china   wenshui wanrong.lu 666 
216002 nantong  china   wenshui wanrong.lu 222 
216600 tianjing china   wenshui wanrong.lu 999
 * </pre>
 * 
 * <p>
 * 可以看出,输出的结果会更加友好
 * </p>
 * 
 * </blockquote>
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>不建议format 太多的数据,以容易查看为原则</li>
 * <li>如果字段含有中文,显示可能会错位,你可以尝试将结果进行 replace(SPACE, "\u3000") 处理,参见
 * <a href="http://stackoverflow.com/questions/18961628/how-can-i-align-the-next-lines-in-java#answer-18962279">format Chinese
 * characters</a></li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see SimpleTableFormatter
 * @since 1.8.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterUtil{

    /** The Constant SIMPLE_TABLE_FORMATTER. */
    private static final SimpleTableFormatter SIMPLE_TABLE_FORMATTER = new SimpleTableFormatter();

    //---------------------------------------------------------------

    /**
     * 使用 {@link SimpleTableFormatter}来格式化.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>不建议format 太多的数据,以容易查看为原则</li>
     * <li>如果字段含有中文,显示可能会错位,你可以尝试将结果进行 replace(SPACE, "\u3000") 处理,参见
     * <a href="http://stackoverflow.com/questions/18961628/how-can-i-align-the-next-lines-in-java#answer-18962279">format Chinese
     * characters</a></li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User id12_age18 = new User(12L, 18);
     * User id1_age8 = new User(1L, 8);
     * User id2_age30 = new User(2L, 30);
     * User id2_age2 = new User(2L, 2);
     * User id2_age36 = new User(2L, 36);
     * List{@code <User>} list = toList(id12_age18, id2_age36, id2_age2, id2_age30, id1_age8);
     * 
     * log.debug(formatToSimpleTable(list));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    age attrMap date id loves money
    --- ------- ---- -- ----- -----
    18               12            
    36               2             
    2                2             
    30               2             
    8                1
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            支持entity,比如Member,也支持 Map
     * @param iterable
     *            the iterable
     * @return 如果 <code>iterable</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     */
    public static final <T> String formatToSimpleTable(Iterable<T> iterable){
        return SIMPLE_TABLE_FORMATTER.format(iterable);
    }

    /**
     * 使用 {@link SimpleTableFormatter}来格式化.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>不建议format 太多的数据,以容易查看为原则</li>
     * <li>如果字段含有中文,显示可能会错位,你可以尝试将结果进行 replace(SPACE, "\u3000") 处理,参见
     * <a href="http://stackoverflow.com/questions/18961628/how-can-i-align-the-next-lines-in-java#answer-18962279">format Chinese
     * characters</a></li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User id12_age18 = new User(12L, 18);
     * User id1_age8 = new User(1L, 8);
     * User id2_age30 = new User(2L, 30);
     * User id2_age2 = new User(2L, 2);
     * User id2_age36 = new User(2L, 36);
     * List{@code <User>} list = toList(id12_age18, id2_age36, id2_age2, id2_age30, id1_age8);
     * 
     * BeanFormatterConfig{@code <User>} beanFormatterConfig = new BeanFormatterConfig{@code <>}(User.class);
     * beanFormatterConfig.setIncludePropertyNames("id", "age");
     * beanFormatterConfig.setSorts("id", "age");
     * log.debug(formatToSimpleTable(list, beanFormatterConfig));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    id age 
    -- --- 
    12 18  
    2  36  
    2  2   
    2  30  
    1  8
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            支持entity,比如Member,也支持 Map
     * @param iterable
     *            the iterable
     * @param beanFormatterConfig
     *            the bean formatter config
     * @return 如果 <code>iterable</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     */
    public static final <T> String formatToSimpleTable(Iterable<T> iterable,BeanFormatterConfig beanFormatterConfig){
        return SIMPLE_TABLE_FORMATTER.format(iterable, beanFormatterConfig);
    }

    //---------------------------------------------------------------

    /**
     * 使用 {@link SimpleTableFormatter}以及指定特定的顺序字段来格式化.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>不建议format 太多的数据,以容易查看为原则</li>
     * <li>如果字段含有中文,显示可能会错位,你可以尝试将结果进行 replace(SPACE, "\u3000") 处理,参见
     * <a href="http://stackoverflow.com/questions/18961628/how-can-i-align-the-next-lines-in-java#answer-18962279">format Chinese
     * characters</a></li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * 已知有以下类
     * 
     * 
     * <pre class="code">
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     //setter/getter 省略
     * 
     * }
     * </pre>
     * 
     * 
     * <pre class="code">
     * 
     * Person person = new Person("feilong", now());
     * log.debug(formatToSimpleTable(toList(person)));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    dateAttr            name    
    ------------------- ------- 
    2018-03-15 15:23:15 feilong
     * </pre>
     * 
     * 此时的顺序是按照字段的自然顺序排序的,
     * 
     * 
     * <p>
     * 如果需要显示成 name在前, dateAttr在后,可以使用此方法
     * </p>
     * 
     * <pre>
     * Person person = new Person("feilong", now());
     * log.debug(formatToSimpleTable(toList(person), "name", "dateAttr"));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    name    dateAttr            
    ------- ------------------- 
    feilong 2018-03-15 15:27:03
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            支持entity,比如Member,也支持 Map
     * @param iterable
     *            the iterable
     * @param sorts
     *            the sorts
     * @return 如果 <code>iterable</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @since 1.11.0
     */
    public static final <T> String formatToSimpleTable(Iterable<T> iterable,String...sorts){
        BeanFormatterConfig beanFormatterConfig = new BeanFormatterConfig();
        beanFormatterConfig.setSorts(sorts);
        return SIMPLE_TABLE_FORMATTER.format(iterable, beanFormatterConfig);
    }

    //---------------------------------------------------------------

    /**
     * 格式化成简单的table格式.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>不建议format 太多的数据,以容易查看为原则</li>
     * <li>如果字段含有中文,显示可能会错位,你可以尝试将结果进行 replace(SPACE, "\u3000") 处理,参见
     * <a href="http://stackoverflow.com/questions/18961628/how-can-i-align-the-next-lines-in-java#answer-18962279">format Chinese
     * characters</a></li>
     * </ol>
     * </blockquote>
     * 
     * <h3>排序:</h3>
     * <blockquote>
     * <p>
     * 传入的map,内部会先进行排序,调用的是 {@link com.feilong.core.util.SortUtil#sortMapByKeyAsc(Map)}(不影响传入的原来的map)
     * </p>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map{@code <String, String>} map = toMap(//
     *                 Pair.of("Loading entityengine.xml from", "file:/opt/atlassian/jira/atlassian-jira/WEB-INF/classes/entityengine.xml"),
     *                 Pair.of("Entity model field type name", "postgres72"),
     *                 Pair.of("Entity model schema name", "public"),
     *                 Pair.of("Database Version", "PostgreSQL - 9.2.8"),
     *                 Pair.of("Database Driver", "PostgreSQL Native Driver - PostgreSQL 9.0 JDBC4 (build 801)"),
     *                 Pair.of("Database Version", "PostgreSQL - 9.2.8"),
     *                 Pair.of(<span style="color:red">(String) null</span>, "PostgreSQL - 9.2.8"),
     *                 Pair.of("Database URL", "jdbc:postgresql://127.0.0.1:5432/db_feilong_jira"),
     *                 Pair.of("Database JDBC config", "postgres72 jdbc:postgresql://127.0.0.1:5432/db_feilong_jira"));
     * 
     * log.debug(formatToSimpleTable(map));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
                              : PostgreSQL - 9.2.8                                                       
    Database Driver               : PostgreSQL Native Driver - PostgreSQL 9.0 JDBC4 (build 801)              
    Database JDBC config          : postgres72 jdbc:postgresql://127.0.0.1:5432/db_feilong_jira              
    Database URL                  : jdbc:postgresql://127.0.0.1:5432/db_feilong_jira                         
    Database Version              : PostgreSQL - 9.2.8                                                       
    Entity model field type name  : postgres72                                                               
    Entity model schema name      : public                                                                   
    Loading entityengine.xml from : file:/opt/atlassian/jira/atlassian-jira/WEB-INF/classes/entityengine.xml
     * </pre>
     * 
     * </blockquote>
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     * @return 如果 <code>map</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see com.feilong.formatter.AbstractFormatter#format(Map)
     * @see com.feilong.core.util.SortUtil#sortMapByKeyAsc(Map)
     */
    public static final <K, V> String formatToSimpleTable(Map<K, V> map){
        return SIMPLE_TABLE_FORMATTER.format(map);
    }

    //---------------------------------------------------------------

    /**
     * 格式化成简单的table格式.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>不建议format 太多的数据,以容易查看为原则</li>
     * <li>如果字段含有中文,显示可能会错位,你可以尝试将结果进行 replace(SPACE, "\u3000") 处理,参见
     * <a href="http://stackoverflow.com/questions/18961628/how-can-i-align-the-next-lines-in-java#answer-18962279">format Chinese
     * characters</a></li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * User user = new User();
     * user.setAge(15);
     * user.setId(88L);
     * user.setAttrMap(toMap("love", "sanguo"));
     * user.setDate(now());
     * user.setMoney(toBigDecimal(999));
     * user.setName("xinge");
     * user.setNickNames(toArray("jinxin", "feilong"));
     * log.debug(formatToSimpleTable(user));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    age              : 15                                
    attrMap          : {love=sanguo}                     
    class            : com.feilong.test.User             
    date             : Tue Jul 26 00:02:16 CST 2016      
    id               : 88                                
    loves            :                                   
    money            : 999                               
    name             : xinge                             
    nickNames        : jinxin                            
    password         :                                   
    userAddresseList :                                   
    userAddresses    :                                   
    userInfo         : com.feilong.test.UserInfo@1c21535
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @return 如果 <code>bean</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     */
    public static final <T> String formatToSimpleTable(T bean){
        return SIMPLE_TABLE_FORMATTER.format(bean);
    }
}
