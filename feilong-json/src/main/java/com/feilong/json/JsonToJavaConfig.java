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
package com.feilong.json;

import java.util.Map;

import com.feilong.lib.json.util.JavaIdentifierTransformer;
import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * 将 json转成java时候的配置.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.9.4
 */
public class JsonToJavaConfig extends AbstractConfig{

    /**
     * The root class.
     * 
     * @see com.feilong.lib.json.JsonConfig#setRootClass(Class)
     */
    private Class<?>                  rootClass;

    //---------------------------------------------------------------

    /**
     * 用来设置复杂自定义对象转换.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 有以下两个bean
     * 
     * <pre class="code">
     * 
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private Person person;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * 
     * }
     * 
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * }
     * 
     * 现在需要把这段json 转成MyBean
     * 
     * String json = "{'mybean':{'data':[{'name':'get'}]}}";
     * 
     * 代码写法如下:
     * 
     * Map{@code <String, Class<?>>} classMap = newHashMap();
     * classMap.put("data", Person.class);
     * 
     * JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(MyBean.class);
     * jsonToJavaConfig.setClassMap(classMap);
     * 
     * MyBean myBean = JsonUtil.toBean(json, jsonToJavaConfig);
     * </pre>
     * 
     * </blockquote>
     * 
     * @see com.feilong.lib.json.JsonConfig#setClassMap(Map)
     */
    private Map<String, Class<?>>     classMap;

    /**
     * java标识符号转换器.
     * 
     * <p>
     * <a href="http://json.org/">JSON 规范</a>指出一个对象的key是个字符串,并且该字符串是零个或多个Unicode字符的集合，用双引号括起来，使用反斜杠转义。<br>
     * 字符表示为单个字符串。
     * </p>
     * 
     * <p>
     * 这就意味着,当你从json格式转成java的时候,你可能有一个有效的 JSON key ,但是他是个无效的java 标识符.此时你可以设置 {@link JavaIdentifierTransformer}
     * </p>
     * 
     * <h3>实现:</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">实现</th>
     * <th align="left">说明</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer
     * SeparatorToCamelCaseJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>分隔符(下划线或者自定义分隔符)转驼峰 <br>
     * transformer 'member_no' {@code =>} 'memberNo'.</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link com.feilong.json.transformer.UncapitalizeJavaIdentifierTransformer UncapitalizeJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>首字母小写 <br>
     * transformer 'MemberNo' {@code =>} 'memberNo'.</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link com.feilong.json.transformer.CustomJavaIdentifierTransformer CustomJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>自定义转换.支持自定义map转换</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#NOOP}</td>
     * <td>json-lib自带</td>
     * <td>什么都不转换.<br>
     * (Noop transformer '@invalid' {@code =>} '@invalid')</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link JavaIdentifierTransformer#STRICT}</td>
     * <td>json-lib自带</td>
     * <td>抛出JSONException if a non JavaIdentifier character is found.<br>
     * ('@invalid' {@code =>} JSONException)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#CAMEL_CASE}</td>
     * <td>json-lib自带</td>
     * <td>将使用非Java标识符和空格字符作为词边界,新单词的第一个字符大写。<br>
     * ('camel case' {@code =>} 'camelCase')</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link JavaIdentifierTransformer#WHITESPACE}</td>
     * <td>json-lib自带</td>
     * <td>删除所有空格以及不符合java规范的字符.<br>
     * ('white space' {@code =>}'whitespace')</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#UNDERSCORE}</td>
     * <td>json-lib自带</td>
     * <td>将所有空格以及不符合java属性规范的字符转成 下划线'_'.<br>
     * ('under score' {@code =>} 'under_score')
     * </td>
     * </tr>
     * </table>
     * 
     * </blockquote>
     * 
     * @see com.feilong.lib.json.JsonConfig#setJavaIdentifierTransformer(JavaIdentifierTransformer)
     */
    private JavaIdentifierTransformer javaIdentifierTransformer;

    //---------------------------------------------------------------

    /**
     * Instantiates a new json to java config.
     */
    public JsonToJavaConfig(){
        super();
    }

    /**
     * Instantiates a new json to java config.
     *
     * @param rootClass
     *            the root class
     */
    public JsonToJavaConfig(Class<?> rootClass){
        super();
        this.rootClass = rootClass;
    }

    /**
     * Instantiates a new json to java config.
     *
     * @param rootClass
     *            the root class
     * @param javaIdentifierTransformer
     *            java标识符号转换器.
     * 
     *            <p>
     *            <a href="http://json.org/">JSON 规范</a>指出一个对象的key是个字符串,并且该字符串是零个或多个Unicode字符的集合，用双引号括起来，使用反斜杠转义。<br>
     *            字符表示为单个字符串。
     *            </p>
     * 
     *            <p>
     *            这就意味着,当你从json格式转成java的时候,你可能有一个有效的 JSON key ,但是他是个无效的java 标识符.此时你可以设置 {@link JavaIdentifierTransformer}
     *            </p>
     * 
     *            <h3>实现:</h3>
     * 
     *            <blockquote>
     *            <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     *            <tr style="background-color:#ccccff">
     *            <th align="left">字段</th>
     *            <th align="left">实现</th>
     *            <th align="left">说明</th>
     *            </tr>
     * 
     *            <tr valign="top">
     *            <td>{@link com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer
     *            SeparatorToCamelCaseJavaIdentifierTransformer}</td>
     *            <td>feilong</td>
     *            <td>分隔符(下划线或者自定义分隔符)转驼峰 <br>
     *            transformer 'member_no' {@code =>} 'memberNo'.</td>
     *            </tr>
     *            <tr valign="top" style="background-color:#eeeeff">
     *            <td>{@link com.feilong.json.transformer.UncapitalizeJavaIdentifierTransformer UncapitalizeJavaIdentifierTransformer}</td>
     *            <td>feilong</td>
     *            <td>首字母小写 <br>
     *            transformer 'MemberNo' {@code =>} 'memberNo'.</td>
     *            </tr>
     * 
     *            <tr valign="top">
     *            <td>{@link com.feilong.json.transformer.CustomJavaIdentifierTransformer CustomJavaIdentifierTransformer}</td>
     *            <td>feilong</td>
     *            <td>自定义转换.支持自定义map转换</td>
     *            </tr>
     * 
     *            <tr valign="top" style="background-color:#eeeeff">
     *            <td>{@link JavaIdentifierTransformer#NOOP}</td>
     *            <td>json-lib自带</td>
     *            <td>什么都不转换.<br>
     *            (Noop transformer '@invalid' {@code =>} '@invalid')</td>
     *            </tr>
     * 
     *            <tr valign="top">
     *            <td>{@link JavaIdentifierTransformer#STRICT}</td>
     *            <td>json-lib自带</td>
     *            <td>抛出JSONException if a non JavaIdentifier character is found.<br>
     *            ('@invalid' {@code =>} JSONException)</td>
     *            </tr>
     *            <tr valign="top" style="background-color:#eeeeff">
     *            <td>{@link JavaIdentifierTransformer#CAMEL_CASE}</td>
     *            <td>json-lib自带</td>
     *            <td>将使用非Java标识符和空格字符作为词边界,新单词的第一个字符大写。<br>
     *            ('camel case' {@code =>} 'camelCase')</td>
     *            </tr>
     * 
     *            <tr valign="top">
     *            <td>{@link JavaIdentifierTransformer#WHITESPACE}</td>
     *            <td>json-lib自带</td>
     *            <td>删除所有空格以及不符合java规范的字符.<br>
     *            ('white space' {@code =>}'whitespace')</td>
     *            </tr>
     *            <tr valign="top" style="background-color:#eeeeff">
     *            <td>{@link JavaIdentifierTransformer#UNDERSCORE}</td>
     *            <td>json-lib自带</td>
     *            <td>将所有空格以及不符合java属性规范的字符转成 下划线'_'.<br>
     *            ('under score' {@code =>} 'under_score')
     *            </td>
     *            </tr>
     *            </table>
     * 
     *            </blockquote>
     */
    public JsonToJavaConfig(Class<?> rootClass, JavaIdentifierTransformer javaIdentifierTransformer){
        super();
        this.rootClass = rootClass;
        this.javaIdentifierTransformer = javaIdentifierTransformer;
    }

    /**
     * Instantiates a new json to java config.
     *
     * @param rootClass
     *            the root class
     * @param classMap
     *            用来设置复杂自定义对象转换.
     * 
     *            <h3>示例:</h3>
     * 
     *            <blockquote>
     *            有以下两个bean
     * 
     *            <pre class="code">
     * 
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private Person person;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * 
     * }
     * 
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * }
     * 
     * 现在需要把这段json 转成MyBean
     * 
     * String json = "{'mybean':{'data':[{'name':'get'}]}}";
     * 
     * 代码写法如下:
     * 
     * Map{@code <String, Class<?>>} classMap = newHashMap();
     * classMap.put("data", Person.class);
     * 
     * JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(MyBean.class);
     * jsonToJavaConfig.setClassMap(classMap);
     * 
     * MyBean myBean = JsonUtil.toBean(json, jsonToJavaConfig);
     *            </pre>
     * 
     *            </blockquote>
     */
    public JsonToJavaConfig(Class<?> rootClass, Map<String, Class<?>> classMap){
        super();
        this.rootClass = rootClass;
        this.classMap = classMap;
    }

    //---------------------------------------------------------------

    /**
     * java标识符号转换器.
     * 
     * <p>
     * <a href="http://json.org/">JSON 规范</a>指出一个对象的key是个字符串,并且该字符串是零个或多个Unicode字符的集合，用双引号括起来，使用反斜杠转义。<br>
     * 字符表示为单个字符串。
     * </p>
     * 
     * <p>
     * 这就意味着,当你从json格式转成java的时候,你可能有一个有效的 JSON key ,但是他是个无效的java 标识符.此时你可以设置 {@link JavaIdentifierTransformer}
     * </p>
     * 
     * <h3>实现:</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">实现</th>
     * <th align="left">说明</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer
     * SeparatorToCamelCaseJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>分隔符(下划线或者自定义分隔符)转驼峰 <br>
     * transformer 'member_no' {@code =>} 'memberNo'.</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link com.feilong.json.transformer.UncapitalizeJavaIdentifierTransformer UncapitalizeJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>首字母小写 <br>
     * transformer 'MemberNo' {@code =>} 'memberNo'.</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link com.feilong.json.transformer.CustomJavaIdentifierTransformer CustomJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>自定义转换.支持自定义map转换</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#NOOP}</td>
     * <td>json-lib自带</td>
     * <td>什么都不转换.<br>
     * (Noop transformer '@invalid' {@code =>} '@invalid')</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link JavaIdentifierTransformer#STRICT}</td>
     * <td>json-lib自带</td>
     * <td>抛出JSONException if a non JavaIdentifier character is found.<br>
     * ('@invalid' {@code =>} JSONException)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#CAMEL_CASE}</td>
     * <td>json-lib自带</td>
     * <td>将使用非Java标识符和空格字符作为词边界,新单词的第一个字符大写。<br>
     * ('camel case' {@code =>} 'camelCase')</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link JavaIdentifierTransformer#WHITESPACE}</td>
     * <td>json-lib自带</td>
     * <td>删除所有空格以及不符合java规范的字符.<br>
     * ('white space' {@code =>}'whitespace')</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#UNDERSCORE}</td>
     * <td>json-lib自带</td>
     * <td>将所有空格以及不符合java属性规范的字符转成 下划线'_'.<br>
     * ('under score' {@code =>} 'under_score')
     * </td>
     * </tr>
     * </table>
     * 
     * </blockquote>
     *
     * @return java标识符号转换器
     * @see com.feilong.lib.json.JsonConfig#setJavaIdentifierTransformer(JavaIdentifierTransformer)
     */
    public JavaIdentifierTransformer getJavaIdentifierTransformer(){
        return javaIdentifierTransformer;
    }

    /**
     * java标识符号转换器.
     * 
     * <p>
     * <a href="http://json.org/">JSON 规范</a>指出一个对象的key是个字符串,并且该字符串是零个或多个Unicode字符的集合，用双引号括起来，使用反斜杠转义。<br>
     * 字符表示为单个字符串。
     * </p>
     * 
     * <p>
     * 这就意味着,当你从json格式转成java的时候,你可能有一个有效的 JSON key ,但是他是个无效的java 标识符.此时你可以设置 {@link JavaIdentifierTransformer}
     * </p>
     * 
     * <h3>实现:</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">实现</th>
     * <th align="left">说明</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer
     * SeparatorToCamelCaseJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>分隔符(下划线或者自定义分隔符)转驼峰 <br>
     * transformer 'member_no' {@code =>} 'memberNo'.</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link com.feilong.json.transformer.UncapitalizeJavaIdentifierTransformer UncapitalizeJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>首字母小写 <br>
     * transformer 'MemberNo' {@code =>} 'memberNo'.</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link com.feilong.json.transformer.CustomJavaIdentifierTransformer CustomJavaIdentifierTransformer}</td>
     * <td>feilong</td>
     * <td>自定义转换.支持自定义map转换</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#NOOP}</td>
     * <td>json-lib自带</td>
     * <td>什么都不转换.<br>
     * (Noop transformer '@invalid' {@code =>} '@invalid')</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link JavaIdentifierTransformer#STRICT}</td>
     * <td>json-lib自带</td>
     * <td>抛出JSONException if a non JavaIdentifier character is found.<br>
     * ('@invalid' {@code =>} JSONException)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#CAMEL_CASE}</td>
     * <td>json-lib自带</td>
     * <td>将使用非Java标识符和空格字符作为词边界,新单词的第一个字符大写。<br>
     * ('camel case' {@code =>} 'camelCase')</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link JavaIdentifierTransformer#WHITESPACE}</td>
     * <td>json-lib自带</td>
     * <td>删除所有空格以及不符合java规范的字符.<br>
     * ('white space' {@code =>}'whitespace')</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link JavaIdentifierTransformer#UNDERSCORE}</td>
     * <td>json-lib自带</td>
     * <td>将所有空格以及不符合java属性规范的字符转成 下划线'_'.<br>
     * ('under score' {@code =>} 'under_score')
     * </td>
     * </tr>
     * </table>
     * 
     * </blockquote>
     *
     * @param javaIdentifierTransformer
     *            java标识符号转换器
     * @see com.feilong.lib.json.JsonConfig#setJavaIdentifierTransformer(JavaIdentifierTransformer)
     */
    public void setJavaIdentifierTransformer(JavaIdentifierTransformer javaIdentifierTransformer){
        this.javaIdentifierTransformer = javaIdentifierTransformer;
    }

    /**
     * Gets the root class.
     *
     * @return the rootClass
     */
    public Class<?> getRootClass(){
        return rootClass;
    }

    /**
     * Sets the root class.
     *
     * @param rootClass
     *            the rootClass to set
     */
    public void setRootClass(Class<?> rootClass){
        this.rootClass = rootClass;
    }

    /**
     * 用来设置复杂自定义对象转换.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 有以下两个bean
     * 
     * <pre class="code">
     * 
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private Person person;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * 
     * }
     * 
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * }
     * 
     * 现在需要把这段json 转成MyBean
     * 
     * String json = "{'mybean':{'data':[{'name':'get'}]}}";
     * 
     * 代码写法如下:
     * 
     * Map{@code <String, Class<?>>} classMap = newHashMap();
     * classMap.put("data", Person.class);
     * 
     * JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(MyBean.class);
     * jsonToJavaConfig.setClassMap(classMap);
     * 
     * MyBean myBean = JsonUtil.toBean(json, jsonToJavaConfig);
     * </pre>
     * 
     * </blockquote>
     * 
     * @see com.feilong.lib.json.JsonConfig#setClassMap(Map)
     *
     * @return the classMap
     */
    public Map<String, Class<?>> getClassMap(){
        return classMap;
    }

    /**
     * 用来设置复杂自定义对象转换.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 有以下两个bean
     * 
     * <pre class="code">
     * 
     * public class MyBean{
     * 
     *     private Long id;
     * 
     *     private Person person;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * 
     * }
     * 
     * public class Person{
     * 
     *     private String name;
     * 
     *     private Date dateAttr;
     * 
     *     <span style="color:green">//setter/getter省略</span>
     * }
     * 
     * 现在需要把这段json 转成MyBean
     * 
     * String json = "{'mybean':{'data':[{'name':'get'}]}}";
     * 
     * 代码写法如下:
     * 
     * Map{@code <String, Class<?>>} classMap = newHashMap();
     * classMap.put("data", Person.class);
     * 
     * JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(MyBean.class);
     * jsonToJavaConfig.setClassMap(classMap);
     * 
     * MyBean myBean = JsonUtil.toBean(json, jsonToJavaConfig);
     * </pre>
     * 
     * </blockquote>
     * 
     * @see com.feilong.lib.json.JsonConfig#setClassMap(Map)
     *
     * @param classMap
     *            the classMap to set
     */
    public void setClassMap(Map<String, Class<?>> classMap){
        this.classMap = classMap;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
