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
package com.feilong.core.lang;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.convert;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toSet;
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.newLinkedHashSet;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newHashMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.feilong.context.converter.StringToBeanConverter;
import com.feilong.core.CharsetType;
import com.feilong.core.Validate;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.util.RegexUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.lib.lang3.text.StrSubstitutor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link String}工具类,可以查询,截取,format.
 * 
 * <h3>分隔(split)</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #split(String, String)}</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>分隔(tokenize)</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #tokenizeToStringArray(String, String)}</li>
 * <li>{@link #tokenizeToStringArray(String, String, boolean, boolean)}</li>
 * </ul>
 * 
 * <p>
 * 区别在于,split 使用的是 正则表达式 {@link Pattern#split(CharSequence)} 分隔(特别注意,一些特殊字符 $|()[{^?*+\\ 需要转义才能做分隔符),而 {@link StringTokenizer} 使用索引机制,在性能上
 * StringTokenizer更高<br>
 * 因此,在注重性能的场景,还是建议使用{@link StringTokenizer}
 * </p>
 * 
 * </blockquote>
 * 
 * <h3>查询(search)</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link StringUtils#countMatches(CharSequence, CharSequence)} 查询出现的次数</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>其他</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link StringUtils#capitalize(String)} 首字母大写</li>
 * <li>{@link StringUtils#uncapitalize(String)} 单词首字母小写</li>
 * <li>org.apache.commons.lang3.text.WordUtils#uncapitalize(String, char...) 如果要使用一段文字,每个单词首字母小写</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>{@link String#String(byte[] )} 和 {@link String#String(byte[], Charset)} 区别</h3>
 * 
 * <blockquote>
 * <p>
 * {@link String#String(byte[] )} 其实调用了{@link String#String(byte[], Charset)};<br>
 * 先使用 {@link Charset#defaultCharset()},如果有异常再用 ISO-8859-1, 具体参见 java.lang.StringCoding#decode(byte[], int, int)
 * </p>
 * </blockquote>
 * 
 * <h3>{@link StringBuffer} 和 {@link StringBuilder} 和 {@link String} 对比</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link StringBuffer} 字符串变量(线程安全)</li>
 * <li>{@link StringBuilder} 字符串变量(非线程安全)</li>
 * <li>{@link String} 字符串常量</li>
 * <li>在大部分情况下 {@link StringBuffer} {@code >} {@link String}</li>
 * <li>在大部分情况下 {@link StringBuilder} {@code >} {@link StringBuffer}</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>String s1 = new String("xyz"); 到底创建几个对象?</h3>
 * 
 * <blockquote>
 * <p>
 * 要看虚拟机的实现.而且要联系上下文
 * </p>
 * 
 * <ul>
 * <li>假设:HotSpot1.6,之前没有创建过"xyz" 则创建2个,之前创建过"xyz"则只创建1个</li>
 * <li>假设:HotSpot1.7,之前不管有没有创建过"xyz" 都创建1个</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>String s3 = s1 + s2; <br>
 * s3.intern() == s3 到底相不相等?</h3> <blockquote>
 * <p>
 * 要看虚拟机的实现
 * </p>
 * 
 * <ul>
 * <li>假设:HotSpot1.6,则false不相等</li>
 * <li>假设:HotSpot1.7,则在之前没有创建过"abcabc"时,true相等</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>{@link StringUtil#replace(String, String, String) replace} 和 {@link #replaceAll(CharSequence, String, String) replaceAll} 和
 * {@link String#replaceFirst(String, String) replaceFirst}区别:</h3>
 * 
 * <blockquote>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>{@link StringUtil#replace(String, String, String)}</td>
 * <td>将字符串中出现的target替换成replacement</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #replaceAll(CharSequence, String, String)}</td>
 * <td>regex是一个正则表达式,将字符串中匹配的子字符串替换为replacement</td>
 * </tr>
 * <tr valign="top">
 * <td>{@link String#replaceFirst(String, String)}</td>
 * <td>和{@link StringUtil#replace(String, String, String)}类似,只不过只替换第一个出现的地方。</td>
 * </tr>
 * </table>
 * 
 * <p>
 * 对比以下代码:
 * </p>
 * 
 * <pre class="code">
 * StringUtil.replaceAll("SH1265,SH5951", "([a-zA-Z]+[0-9]+)", "'$1'")  ='SH1265','SH5951'
 * StringUtil.replace("SH1265,SH5951", "([a-zA-Z]+[0-9]+)", "'$1'")     =SH1265,SH5951
 * "SH1265,SH5951".replaceFirst("([a-zA-Z]+[0-9]+)", "'$1'")            ='SH1265',SH5951
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see java.util.StringTokenizer
 * @see "org.springframework.util.StringUtils#tokenizeToStringArray(String, String)"
 * @see "org.springframework.beans.factory.xml.BeanDefinitionParserDelegate#MULTI_VALUE_ATTRIBUTE_DELIMITERS"
 * @see com.feilong.lib.lang3.StringUtils
 * @see "com.google.common.base.Strings"
 * @since 1.4.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil{

    /**
     * A String for a space character.
     *
     * @since 3.0.0
     */
    public static final String SPACE = " ";

    /**
     * The empty String {@code ""}.
     * 
     * @since 3.0.0
     */
    public static final String EMPTY = "";

    /**
     * A String for linefeed LF ("\n").
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
     *      for Character and String Literals</a>
     * @since 3.0.0
     */
    public static final String LF    = "\n";

    /**
     * A String for carriage return CR ("\r").
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
     *      for Character and String Literals</a>
     * @since 3.0.0
     */
    public static final String CR    = "\r";

    /**
     * 空格 (Non-Breaking SPace)<code>{@value}</code>.
     * <p>
     * 用"&nbsp;"来代替空格,一个"&nbsp;"相当于一个空格,多加几个"&nbsp;"就可以把空格拉大.<br>
     * 虽然"&nbsp;"可以当作空格用,但是"&nbsp;"其实和空格是不一样的,<br>
     * nbsp是英文Non-Breaking SPace的缩写,可以直接翻译成"不被折断的空格".
     * </p>
     * 
     * 比如下面这段html:
     * 
     * <pre class="code">
     * {@code
     * <h2>10 Most Sought-after Skills in Web Development</h2>
     * }
     * </pre>
     * 
     * <pre class="code">
     * 假设{@code<h2>}的宽度有限,只能容下"10 Most Sought-after Skills in Web", 
     * 由于Web Development之间用的是空格,"Development"就会被移到第二行. 
     * 
     * 因为Web Development是相关的两个词,所以如果可以把它们同时移到第二行,效果可能会更好一点.
     * 为了到达这个目的,我们可以在Web Development之间用"&nbsp;"来代替空格,这个样它们就会被连在一起.
     * </pre>
     * 
     * <pre class="code">
     * {@code
     * <h2>10 Most Sought-after Skills in Web&nbsp;Development</h2>
     * }
     * </pre>
     * 
     * @since 4.0.8
     */
    public static final String NBSP  = "&nbsp;";

    //---------------------------------------------------------------

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     * 
     * <p>
     * 默认使用 {@link CharsetType#UTF8}
     * </p>
     *
     * @param bytes
     *            The bytes to be decoded into characters, may be <code>null</code>
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset,
     *         or <code>null</code> if the input byte array was <code>null</code>.
     * @see String#String(byte[], String)
     * @see "org.apache.commons.lang3.StringUtils#toString(byte[], String)"
     * @see com.feilong.lib.lang3.StringUtils#toEncodedString(byte[], Charset)
     * @see "org.apache.commons.codec.binary.StringUtils#newString(byte[], String)"
     * @since 3.3.2
     */
    public static String newString(byte[] bytes){
        return newString(bytes, UTF8);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters, may be <code>null</code>
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset,
     *         or <code>null</code> if the input byte array was <code>null</code>.
     * @see String#String(byte[], String)
     * @see "org.apache.commons.lang3.StringUtils#toString(byte[], String)"
     * @see com.feilong.lib.lang3.StringUtils#toEncodedString(byte[], Charset)
     * @see "org.apache.commons.codec.binary.StringUtils#newString(byte[], String)"
     * @since 1.3.0
     */
    public static String newString(byte[] bytes,String charsetType){
        return StringUtils.toEncodedString(bytes, Charset.forName(charsetType));
    }
    // [start]toBytes

    //---------------------------------------------------------------
    /**
     * 字符串转换成byte数组.
     *
     * @param value
     *            字符串
     * @return 如果 <code>value</code> 是null,抛出 {@link NullPointerException}<br>
     * @see String#getBytes()
     * @since 1.3.0
     */
    public static byte[] getBytes(String value){
        Validate.notNull(value, "value can't be null!");
        return value.getBytes();
    }

    /**
     * 字符串 <code>value</code> 转换成byte数组.
     * 
     * @param value
     *            字符串
     * @param charsetName
     *            受支持的 charset 名称,比如 utf-8, {@link CharsetType}
     * @return 如果 <code>value</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see String#getBytes(String)
     * @since 1.3.0
     */
    public static byte[] getBytes(String value,String charsetName){
        Validate.notNull(value, "value can't be null!");
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            return value.getBytes(charsetName);
        }catch (UnsupportedEncodingException e){
            String pattern = "value:[{}],charsetName:[{}],suggest you use [{}] constants";
            String message = formatPattern(pattern, value, charsetName, CharsetType.class.getCanonicalName());
            throw new UncheckedIOException(message, e);
        }
    }

    // [end]
    // [start]replace

    //---------------------------------------------------------------
    /**
     * 将 <code>text</code> 中的 <code>searchString</code> 替换成 <code>replacement</code>.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     * </p>
     *
     * <pre>
     * StringUtil.replace(null, *, *) = null
     * StringUtil.replace("", *, *) = ""
     * StringUtil.replace("any", null, *) = "any"
     * StringUtil.replace("any", *, null) = "any"
     * StringUtil.replace("any", "", *) = "any"
     * StringUtil.replace("aba", "a", null) = "aba"
     * StringUtil.replace("aba", "a", "") = "b"
     * StringUtil.replace("aba", "a", "z") = "zbz"
     * 
     * StringUtil.replace("黑色/黄色/蓝色", "/", "_")         =   "黑色_黄色_蓝色"
     * StringUtil.replace(null, "/", "_")               =   null
     * StringUtil.replace("黑色/黄色/蓝色", "/", null)        =   "黑色/黄色/蓝色"
     * </pre>
     * 
     * 此外注意的是:
     * 
     * <pre class="code">
     * StringUtil.replace("SH1265,SH5951", "([a-zA-Z]+[0-9]+)", "'$1'") = SH1265,SH5951
     * </pre>
     * 
     * (注意和 {@link #replaceAll(CharSequence, String, String)} 的区别)
     * 
     * </blockquote>
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>该替换从字符串的开头朝末尾执行,例如,用 "b" 替换字符串 "aaa" 中的 "aa" 将生成 "ba" 而不是 "ab".</li>
     * <li>虽然底层调用了{@link java.util.regex.Matcher#replaceAll(String) Matcher.replaceAll()},但是使用了
     * {@link java.util.regex.Matcher#quoteReplacement(String) Matcher.quoteReplacement()} 处理了特殊字符</li>
     * </ol>
     * </blockquote>
     * 
     * @param text
     *            text to search and replace in, may be null
     * @param searchString
     *            the String to search for, may be null
     * @param replacement
     *            the String to replace it with, may be null
     * @return 如果 <code>text</code> 是null,返回 null<br>
     *         如果 <code>searchString</code> 是null,原样返回 <code>text</code><br>
     *         如果 <code>replacement</code> 是null,原样返回 <code>text</code><br>
     * @see java.lang.String#replace(CharSequence, CharSequence)
     * @see com.feilong.lib.lang3.StringUtils#replace(String, String, String)
     * @since jdk 1.5
     */
    public static String replace(final String text,final String searchString,final String replacement){
        return StringUtils.replace(text, searchString, replacement);
    }

    /**
     * 使用给定的<code>replacement</code>替换 <code>content</code> 此字符串所有匹配给定的正则表达式 <code>regex</code>的子字符串.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>
     * 此方法底层调用的是 {@link java.util.regex.Matcher#replaceAll(String)},相同于
     * <code>Pattern.compile(regex).matcher(str).replaceAll(repl)</code>
     * </li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * StringUtil.replaceAll("SH1265,SH5951,SH6766,SH7235,SH1265,SH5951,SH6766,SH7235", "([a-zA-Z]+[0-9]+)", "'$1'")
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 'SH1265','SH5951','SH6766','SH7235','SH1265','SH5951','SH6766','SH7235'
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>请注意:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 在替代字符串<code>replacement</code>中,使用 backslashes反斜杆(<tt>\</tt>)和 dollar signs美元符号 (<tt>$</tt>)与将其视为字面值替代字符串所得的结果可能不同.
     * <br>
     * 请参阅 {@link java.util.regex.Matcher#replaceAll Matcher.replaceAll};如有需要,可使用 {@link java.util.regex.Matcher#quoteReplacement
     * Matcher.quoteReplacement}取消这些字符的特殊含义
     * </p>
     * 
     * <p>
     * <tt>$</tt> may be treated as references to captured subsequences as described above,<tt>$</tt>这个特殊的字符,因为替换串使用这个引用正则表达式匹配的组,
     * $0代表匹配项,$1代表第1个匹配分组,$1代表第2个匹配分组
     * <br>
     * 并且 <tt>\</tt> are used to escape literal characters in the replacement string. 
     * </p>
     * </blockquote>
     * 
     * <h3>对于以下代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * <span style="color:green">//分隔字符串并添加单引号.</span>
     * public void splitAndAddYinHao(){
     *     String a = "12345,56789,1123456";
     *     String[] aStrings = a.split(",");
     *     StringBuilder sb = new StringBuilder();
     *     int size = aStrings.length;
     *     for (int i = 0; i {@code <} size; i++){
     *         sb.append("'" + aStrings[i] + "'");
     *         if (i != size - 1){
     *             sb.append(",");
     *         }
     *     }
     *     log.debug(sb.toString());
     * }
     * 
     * </pre>
     * 
     * 可以重构成:
     * 
     * <pre class="code">
     * StringUtil.replaceAll("12345,56789,1123456", "([0-9]+)", "'$1'")
     * </pre>
     * 
     * 结果都是:
     * 
     * <pre class="code">
     * '12345','56789','1123456'
     * </pre>
     * 
     * </blockquote>
     * 
     * @param content
     *            需要被替换的字符串
     * @param regex
     *            用来匹配此字符串的正则表达式,规则参见 RegexPattern 注释
     * @param replacement
     *            用来替换每个匹配项的字符串
     * @return 如果 <code>content</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>content</code> 中,没有 regex匹配的字符串或者格式,返回<code>content</code><br>
     * @see <a href="http://stamen.iteye.com/blog/2028256">String字符串替换的一个诡异问题</a>
     * @see <a href="https://blog.csdn.net/hadues/article/details/128002667">Java中String对象的replaceAll方法调用性能优化小技巧</a>
     * @see java.lang.String#replaceAll(String, String)
     * @since jdk 1.4
     */
    public static String replaceAll(CharSequence content,String regex,String replacement){
        //        return null == content ? EMPTY : content.toString().replaceAll(regex, replacement);
        if (null == content){
            return EMPTY;
        }
        Pattern pattern = RegexUtil.buildPattern(regex, 0);
        return pattern.matcher(content).replaceAll(replacement);
    }

    /**
     * 使用给定的字符串 <code>templateString</code> 作为模板,解析匹配的变量 .
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * String template = "/home/webuser/expressdelivery/${yearMonth}/${expressDeliveryType}/vipQuery_${fileName}.log";
     * Date date = now();
     * 
     * Map{@code <String, String>} valuesMap = newHashMap();
     * valuesMap.put("yearMonth", DateUtil.toString(date, DatePattern.YEAR_AND_MONTH));
     * valuesMap.put("expressDeliveryType", "sf");
     * valuesMap.put("fileName", DateUtil.toString(date, DatePattern.TIMESTAMP));
     * log.debug(StringUtil.replace(template, valuesMap));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * /home/webuser/expressdelivery/2016-06/sf/vipQuery_20160608214846.log
     * </pre>
     * 
     * </blockquote>
     * 
     * <p>
     * <span style="color:red">注意:此方法只能替换字符串,而不能像el表达式一样使用对象属性之类的来替换</span>
     * </p>
     * 
     * <h3>比如:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * Map{@code <String, Object>} valuesMap = newHashMap();
     * valuesMap.put("today", DateUtil.toString(now(), COMMON_DATE));
     * valuesMap.put("user", new User(1L));
     * log.debug(StringUtil.replace("${today}${today1}${user.id}${user}", valuesMap) + "");
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 2016-07-16${today1}${user.id}com.feilong.test.User@16f9a31
     * </pre>
     * 
     * </blockquote>
     *
     * @param <V>
     *            the value type
     * @param templateString
     *            the template string
     * @param valuesMap
     *            the values map
     * @return 如果 <code>templateString</code> 是 <code>StringUtils.isEmpty(templateString)</code>,返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>valuesMap</code> 是null或者empty,原样返回 <code>templateString</code><br>
     * @see com.feilong.lib.lang3.text.StrSubstitutor#replace(String)
     * @see com.feilong.lib.lang3.text.StrSubstitutor#replace(Object, Map)
     * @since 1.1.1
     */
    public static <V> String replace(CharSequence templateString,Map<String, V> valuesMap){
        return StringUtils.isEmpty(templateString) ? EMPTY : StrSubstitutor.replace(templateString, valuesMap);
    }

    // [end]

    // [start]substring

    //---------------------------------------------------------------
    /**
     * [截取]从指定索引处(<code>beginIndex</code>)的字符开始,直到此字符串末尾.
     * 
     * <p>
     * 如果 <code>beginIndex</code>是负数,那么表示倒过来截取,从结尾开始截取长度,此时等同于 {@link #substringLast(String, int)}
     * </p>
     *
     * <pre class="code">
     * StringUtil.substring(null, *)   = null
     * StringUtil.substring("", *)     = ""
     * StringUtil.substring("abc", 0)  = "abc"
     * StringUtil.substring("abc", 2)  = "c"
     * StringUtil.substring("abc", 4)  = ""
     * StringUtil.substring("abc", -2) = "bc"
     * StringUtil.substring("abc", -4) = "abc"
     * StringUtil.substring("jinxin.feilong",6)    =.feilong
     * </pre>
     * 
     * @param text
     *            内容 the String to get the substring from, may be null
     * @param beginIndex
     *            从指定索引处 the position to start from,negative means count back from the end of the String by this many characters
     * @return 如果 <code>text</code> 是null,返回 null<br>
     *         An empty ("") String 返回 "".<br>
     * @see com.feilong.lib.lang3.StringUtils#substring(String, int)
     * @see #substringLast(String, int)
     */
    public static String substring(final String text,final int beginIndex){
        return StringUtils.substring(text, beginIndex);
    }

    /**
     * [截取]从开始位置(<code>startIndex</code>),截取固定长度(<code>length</code>)字符串.
     * 
     * <pre class="code">
     * StringUtil.substring(null, 6, 8)                 =   null
     * StringUtil.substring("jinxin.feilong", 6, 2)     =   .f
     * </pre>
     *
     * @param text
     *            被截取文字
     * @param startIndex
     *            索引开始位置,0开始
     * @param length
     *            长度 {@code >=1}
     * @return 如果 <code>text</code> 是null,返回 null<br>
     *         如果 <code>startIndex + length</code> {@code >} <code>text.length</code>,那么截取 从 startIndex 开始截取,截取到最后
     * @see com.feilong.lib.lang3.StringUtils#substring(String, int, int)
     */
    public static String substring(final String text,int startIndex,int length){
        return StringUtils.substring(text, startIndex, startIndex + length);
    }

    /**
     * [截取]:获取文字最后位数 <code>lastLenth</code> 的字符串.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * StringUtil.substringLast("jinxin.feilong", 5) = ilong
     * </pre>
     * 
     * </blockquote>
     * 
     * @param text
     *            文字
     * @param lastLenth
     *            最后的位数
     * @return 如果 <code>text</code> 是null,返回 null<br>
     *         如果 {@code lastLenth<0},返回 {@link StringUtils#EMPTY}<br>
     *         如果 {@code text.length() <= lastLenth},返回text<br>
     *         否则返回<code> text.substring(text.length() - lastLenth)</code>
     * @see com.feilong.lib.lang3.StringUtils#right(String, int)
     */
    public static String substringLast(final String text,int lastLenth){
        return StringUtils.right(text, lastLenth);
    }

    /**
     * [截取]:去除最后几位 <code>lastLenth</code> .
     * 
     * <p>
     * 调用了 {@link java.lang.String#substring(int, int)}
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * StringUtil.substringWithoutLast("jinxin.feilong", 5) //jinxin.fe
     * </pre>
     * 
     * </blockquote>
     * 
     * @param text
     *            文字
     * @param lastLenth
     *            最后的位数
     * @return 如果 <code>text</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see java.lang.String#substring(int, int)
     * @see com.feilong.lib.lang3.StringUtils#left(String, int)
     */
    public static String substringWithoutLast(final String text,final int lastLenth){
        return null == text ? EMPTY : text.substring(0, text.length() - lastLenth);
    }

    /**
     * [截取]:去除最后的字符串 <code>lastString</code>.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * StringUtil.substringWithoutLast(null, "222")                     = ""
     * StringUtil.substringWithoutLast("jinxin.feilong", "ng")          = "jinxin.feilo"
     * StringUtil.substringWithoutLast("jinxin.feilong     ", "     ")  = "jinxin.feilong"
     * </pre>
     * 
     * </blockquote>
     *
     * @param text
     *            the text
     * @param lastString
     *            the last string
     * @return 如果 <code>text</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>lastString</code> 是null,返回 <code>text.toString()</code><br>
     * @since 1.4.0
     */
    public static String substringWithoutLast(final CharSequence text,final String lastString){
        if (null == text){
            return EMPTY;
        }
        String textString = text.toString();
        if (null == lastString){
            return textString;
        }
        return textString.endsWith(lastString) ? substringWithoutLast(textString, lastString.length()) : textString;
    }

    // [end]

    // [start]splitToT

    /**
     * 将字符串 <code>value</code> 使用分隔符 <code>regexSpliter</code> 分隔成 字符串数组.
     * 
     * <p>
     * 建议使用 {@link #tokenizeToStringArray(String, String)} 或者 {@link StringUtils#split(String)}
     * </p>
     *
     * @param value
     *            value
     * @param regexSpliter
     *            此处不是简单的分隔符,是正则表达式,<b>.$|()[{^?*+\\</b> 有特殊的含义,因此我们使用.的时候必须进行转义,<span style="color:red">"\"转义时要写成"\\\\"</span> <br>
     *            最终调用了 {@link java.util.regex.Pattern#split(CharSequence)}
     * @return 如果 <code>value</code> 是null或者empty,返回 {@link ArrayUtil#EMPTY_STRING_ARRAY}<br>
     * @see String#split(String)
     * @see String#split(String, int)
     * @see StringUtils#split(String)
     * @see java.util.regex.Pattern#split(CharSequence)
     */
    public static String[] split(String value,String regexSpliter){
        return isNullOrEmpty(value) ? EMPTY_STRING_ARRAY : value.split(regexSpliter);
    }

    // [end]

    // [start]tokenizeToStringArray

    /**
     * 使用<span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span> 分隔给定的字符串到字符串数组,去除 tokens的空格并且忽略empty tokens.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <p>
     * 调用了 {@link #tokenizeToStringArray(String, String, boolean, boolean)},<br>
     * 本方法默认使用参数 <span style="color:green">trimTokens = true;</span> <br>
     * <span style="color:green">ignoreEmptyTokens = true;</span>,<br>
     * <span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String str = "jin.xin  feilong ,jinxin;venusdrogon;jim ";
     * String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray(str);
     * log.info(JsonUtil.format(tokenizeToStringArray));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * "jin.xin",
     * "feilong",
     * "jinxin",
     * "venusdrogon",
     * "jim"
     * ]
     * </pre>
     * 
     * </blockquote>
     * 
     * @param str
     *            需要被分隔的字符串
     * @return 如果 <code>str</code> 是null,返回 {@link ArrayUtil#EMPTY_STRING_ARRAY}<br>
     *         如果 <code>str</code> 是blank,返回 {@link ArrayUtil#EMPTY_STRING_ARRAY}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * 
     * @see #tokenizeToStringArray(String, String, boolean, boolean)
     * @since 3.5.0
     */
    public static String[] tokenizeToStringArray(String str){
        String delimiters = ";, ";
        return tokenizeToStringArray(str, delimiters);
    }

    /**
     * 使用<span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span> 分隔给定的字符串,转成Set,去除 tokens的空格并且忽略empty tokens.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <p>
     * 调用了 {@link #tokenizeToStringArray(String, String, boolean, boolean)},<br>
     * 本方法默认使用参数 <span style="color:green">trimTokens = true;</span> <br>
     * <span style="color:green">ignoreEmptyTokens = true;</span>,<br>
     * <span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String str = "jin.xin  feilong ,jinxin;venusdrogon;jim ";
     * String[] set = StringUtil.tokenizeToSet(str);
     * log.info(JsonUtil.format(set));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * "jin.xin",
     * "feilong",
     * "jinxin",
     * "venusdrogon",
     * "jim"
     * ]
     * </pre>
     * 
     * </blockquote>
     * 
     * @param str
     *            需要被分隔的字符串
     * @return 如果 <code>str</code> 是null或者empty,返回 {@link Collections#emptySet()}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * 
     * @see #tokenizeToStringArray(String, String, boolean, boolean)
     * @since 3.5.0
     */
    public static Set<String> tokenizeToSet(String str){
        String[] stringArray = tokenizeToStringArray(str);
        return toSet(stringArray);
    }

    /**
     * 使用<span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span> 分隔给定的字符串,转成List,去除 tokens的空格并且忽略empty tokens.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <p>
     * 调用了 {@link #tokenizeToStringArray(String, String, boolean, boolean)},<br>
     * 本方法默认使用参数 <span style="color:green">trimTokens = true;</span> <br>
     * <span style="color:green">ignoreEmptyTokens = true;</span>,<br>
     * <span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String str = "jin.xin  feilong ,jinxin;venusdrogon;jim ";
     * String[] list = StringUtil.tokenizeToList(str);
     * log.info(JsonUtil.format(list));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * "jin.xin",
     * "feilong",
     * "jinxin",
     * "venusdrogon",
     * "jim"
     * ]
     * </pre>
     * 
     * </blockquote>
     * 
     * @param str
     *            需要被分隔的字符串
     * @return 如果 <code>str</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * 
     * @see #tokenizeToStringArray(String, String, boolean, boolean)
     * @since 3.5.0
     */
    public static List<String> tokenizeToList(String str){
        String[] stringArray = tokenizeToStringArray(str);
        return toList(stringArray);
    }

    /**
     * 使用<span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span> 分隔给定的字符串,转成Set,去除 tokens的空格并且忽略empty tokens, 然后将每个元素转换成指定的类型
     * <code>perElementConvertClass</code> 对应的set.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <p>
     * 调用了 {@link #tokenizeToStringArray(String, String, boolean, boolean)},<br>
     * 本方法默认使用参数 <span style="color:green">trimTokens = true;</span> <br>
     * <span style="color:green">ignoreEmptyTokens = true;</span>,<br>
     * <span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String str = "jin.xin  feilong ,jinxin;venusdrogon;jim ";
     * Set<String> set = StringUtil.tokenizeToSet(str, String.class);
     * log.info(JsonUtil.format(set));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * "jin.xin",
     * "feilong",
     * "jinxin",
     * "venusdrogon",
     * "jim"
     * ]
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     * @param str
     *            需要被分隔的字符串
     * @param perElementConvertClass
     *            单元素要转换成的类型
     * @return 如果 <code>str</code> 是null或者empty,返回 {@link Collections#emptySet()}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * 
     * @see #tokenizeToStringArray(String, String, boolean, boolean)
     * @since 4.3.1
     */
    public static <T> Set<T> tokenizeToSet(String str,Class<T> perElementConvertClass){
        String[] stringArray = tokenizeToStringArray(str);
        if (isNullOrEmpty(stringArray)){
            return emptySet();
        }
        //---------------------------------------------------------------
        Set<T> set = newLinkedHashSet();
        for (String perElement : stringArray){
            set.add(convert(perElement, perElementConvertClass));
        }
        return set;
    }

    /**
     * 使用<span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span> 分隔给定的字符串,转成List,去除 tokens的空格并且忽略empty tokens, 然后将每个元素转换成指定的类型
     * <code>perElementConvertClass</code> 对应的list.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <p>
     * 调用了 {@link #tokenizeToStringArray(String, String, boolean, boolean)},<br>
     * 本方法默认使用参数 <span style="color:green">trimTokens = true;</span> <br>
     * <span style="color:green">ignoreEmptyTokens = true;</span>,<br>
     * <span style="color:green">默认分隔符 ;, (逗号,分号和空格)</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String str = "jin.xin  feilong ,jinxin;venusdrogon;jim ";
     * List<String> list = StringUtil.tokenizeToList(str, String.class);
     * log.info(JsonUtil.format(list));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * "jin.xin",
     * "feilong",
     * "jinxin",
     * "venusdrogon",
     * "jim"
     * ]
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     * 
     * @param str
     *            需要被分隔的字符串
     * @param perElementConvertClass
     *            单元素要转换成的类型
     * @return 如果 <code>str</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * 
     * @see #tokenizeToStringArray(String, String, boolean, boolean)
     * @since 4.3.1
     */
    public static <T> List<T> tokenizeToList(String str,Class<T> perElementConvertClass){
        String[] stringArray = tokenizeToStringArray(str);
        if (isNullOrEmpty(stringArray)){
            return emptyList();
        }
        //---------------------------------------------------------------
        List<T> list = newArrayList();
        for (String perElement : stringArray){
            list.add(convert(perElement, perElementConvertClass));
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * 使用StringTokenizer分隔给定的字符串到字符串数组,去除 tokens的空格并且忽略empty tokens.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <p>
     * 调用了 {@link #tokenizeToStringArray(String, String, boolean, boolean)},<br>
     * 本方法默认使用参数 <span style="color:green">trimTokens = true;</span> <br>
     * <span style="color:green">ignoreEmptyTokens = true;</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * String str = "jin.xin  feilong ,jinxin;venusdrogon;jim ";
     * String delimiters = ";, .";
     * String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray(str, delimiters);
     * log.info(JsonUtil.format(tokenizeToStringArray));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     * "jin",
     * "xin",
     * "feilong",
     * "jinxin",
     * "venusdrogon",
     * "jim"
     * ]
     * </pre>
     * 
     * </blockquote>
     * 
     * 
     * 
     * @param str
     *            需要被分隔的字符串
     * @param delimiters
     *            delimiter characters, assembled as String<br>
     *            参数中的所有字符都是分隔标记的分隔符,比如这里可以设置成 ";, " ,spring就是使用这样的字符串来分隔数组/集合的
     * @return 如果 <code>str</code> 是null,返回 {@link ArrayUtil#EMPTY_STRING_ARRAY}<br>
     *         如果 <code>str</code> 是blank,返回 {@link ArrayUtil#EMPTY_STRING_ARRAY}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * 
     * @see #tokenizeToStringArray(String, String, boolean, boolean)
     * @since 1.0.7
     * @apiNote
     *          <h3>说明:</h3>
     * 
     *          <blockquote>
     *          <p>
     *          给定的delimiters字符串支持任意数量的分隔字符characters. <br>
     *          每一个characters可以用来分隔tokens.一个delimiter分隔符常常是一个single字符;<br>
     *          如果你要使用多字符 multi-character delimiters分隔, 你可以考虑使用<code>delimitedListToStringArray</code>
     *          </p>
     *          </blockquote>
     */
    public static String[] tokenizeToStringArray(String str,String delimiters){
        boolean trimTokens = true;
        boolean ignoreEmptyTokens = true;
        return tokenizeToStringArray(str, delimiters, trimTokens, ignoreEmptyTokens);
    }

    /**
     * 使用StringTokenizer分隔给定的字符串到字符串数组.
     * 
     * <p>
     * (此方法借鉴 "org.springframework.util.StringUtils#tokenizeToStringArray").
     * </p>
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * <p>
     * 给定的delimiters字符串支持任意数量的分隔字符characters. <br>
     * 每一个characters可以用来分隔tokens.一个delimiter分隔符常常是一个single字符;<br>
     * 如果你要使用多字符 multi-character delimiters分隔, 你可以考虑使用<code>delimitedListToStringArray</code>
     * </p>
     * </blockquote>
     * 
     * <h3>关于 {@link StringTokenizer}:</h3>
     * 
     * <blockquote>
     * 
     * {@link StringTokenizer} implements {@code Enumeration<Object>}<br>
     * 其在 Enumeration接口的基础上,定义了 hasMoreTokens nextToken两个方法<br>
     * 实现的Enumeration接口中的 hasMoreElements nextElement,调用了 hasMoreTokens nextToken<br>
     * 
     * </blockquote>
     * 
     * @param str
     *            需要被分隔的字符串
     * @param delimiters
     *            delimiter characters, assembled as String<br>
     *            参数中的所有字符都是分隔标记的分隔符,比如这里可以设置成 ";, " ,spring就是使用这样的字符串来分隔数组/集合的
     * @param trimTokens
     *            是否使用 {@link String#trim()}操作token
     * @param ignoreEmptyTokens
     *            是否忽视空白的token,如果为true,那么token必须长度 {@code >} 0;如果为false会包含长度=0 空白的字符<br>
     *            (仅仅用于那些 trim之后是empty的tokens,StringTokenizer不会考虑subsequent delimiters as token in the first place).
     * @return 如果 <code>str</code> 是null,返回 {@link ArrayUtil#EMPTY_STRING_ARRAY}<br>
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * @since 1.0.7
     */
    public static String[] tokenizeToStringArray(String str,String delimiters,boolean trimTokens,boolean ignoreEmptyTokens){
        if (null == str){
            return EMPTY_STRING_ARRAY;
        }

        //---------------------------------------------------------------
        List<String> tokenList = newArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(str, delimiters);
        while (stringTokenizer.hasMoreTokens()){
            String token = stringTokenizer.nextToken();
            token = trimTokens ? token.trim() : token;//去空

            if (!ignoreEmptyTokens || token.length() > 0){
                tokenList.add(token);
            }
        }
        return toArray(tokenList, String.class);
    }

    // [end]

    /**
     * 
     * 将配置类型的逗号分隔字符串分割转换成数组,然后自动转换成element一样的类型,判断是否包含.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * StringUtil.tokenizeToArrayContains((String) null, null) = false;
     * StringUtil.tokenizeToArrayContains(null, EMPTY) = false;
     * StringUtil.tokenizeToArrayContains(EMPTY, null) = false;
     * StringUtil.tokenizeToArrayContains(EMPTY, EMPTY) = false;
     * StringUtil.tokenizeToArrayContains("   ", EMPTY) = false;
     * 
     * StringUtil.tokenizeToArrayContains("1,2,2,,3,4", "3") = true;
     * StringUtil.tokenizeToArrayContains("1,2,2,,3,4", 3) = true;
     * StringUtil.tokenizeToArrayContains("1,2,2,3,4", "1") = true;
     * StringUtil.tokenizeToArrayContains("1,2,2,3,4", 1) = true;
     * StringUtil.tokenizeToArrayContains("1,2,2,3,4", 1L) = true;
     * StringUtil.tokenizeToArrayContains("1,2,2,3,4", toBigDecimal(1)) = true;
     * StringUtil.tokenizeToArrayContains("1,2,2,3,4", Double.valueOf(1)) = true;
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>重构:</h3>
     * 
     * <blockquote>
     * <p>
     * 对于以下代码:
     * </p>
     * 
     * <pre class="code">
     * String redMultiBookIds = staticConfig.getRedMultiBookIds();
     * if (isNullOrEmpty(redMultiBookIds)){
     *     return 0;
     * }
     * 
     * String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray(redMultiBookIds, ",");
     * boolean contains = Arrays.asList(tokenizeToStringArray).contains("" + entryId);
     * return contains ? 1 : 0;
     * </pre>
     * 
     * <b>可以重构成:</b>
     * 
     * <pre class="code">
     * String redMultiBookIds = staticConfig.getRedMultiBookIds();
     * if (isNullOrEmpty(redMultiBookIds)){
     *     return 0;
     * }
     * 
     * boolean contains = StringUtil.tokenizeToStringArrayContains(redMultiBookIds, entryId);
     * return contains ? 1 : 0;
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param config
     *            the config
     * @param element
     *            the element
     * @return 如果 <code>config</code> 是null或者empty,返回false<br>
     *         如果 <code>element</code> 是null,返回false<br>
     *         否则将config 使用逗号分隔成字符串数组,然后逐个转换成element类型,如果相等,返回true ,否则false
     * @since 3.3.7
     */
    public static <T> boolean tokenizeToArrayContains(String config,T element){
        return tokenizeToArrayContains(config, ",", element);
    }

    /**
     * 将配置类型的逗号分隔字符串分割转换成数组,然后自动转换成element一样的类型,判断是否包含.
     *
     * @param <T>
     *            the generic type
     * @param config
     *            the config
     * @param delimiters
     *            the delimiters
     * @param element
     *            the element
     * @return 如果 <code>config</code> 是null或者empty,返回false<br>
     *         如果 <code>element</code> 是null,返回false<br>
     *         否则将config 使用逗号分隔成字符串数组,然后逐个转换成element类型,如果相等,返回true ,否则false
     * @since 3.3.7
     */
    public static <T> boolean tokenizeToArrayContains(String config,String delimiters,T element){
        if (isNullOrEmpty(config)){
            return false;
        }
        if (null == element){
            return false;
        }
        //---------------------------------------------------------------
        String[] array = StringUtil.tokenizeToStringArray(config, delimiters);
        if (isNullOrEmpty(array)){
            return false;
        }
        //---------------------------------------------------------------
        Class<T> klass = (Class<T>) element.getClass();
        for (String str : array){
            T convertValue = ConvertUtil.convert(str, klass);
            if (Objects.equals(convertValue, element)){
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 将 189=988;200=455;这种格式的字符串转换成map , map的key 是189这种, value 是988,455 这种等号后面的值,使用逗号分隔成list.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map<String, String> map = StringUtil.toSingleValueMap("1=2;89=100;200=230");
     * 
     * assertThat(
     *                 map,
     *                 allOf(//
     *                                 hasEntry("1", "2"),
     *                                 hasEntry("89", "100"),
     *                                 hasEntry("200", "230")));
     * 
     * </pre>
     * 
     * 
     * </blockquote>
     *
     * @param configString
     *            189=988;200=455; 这种格式的配置字符串, 用分号分隔一组, 等号分隔key 和value
     * @return 如果 <code>configString</code> 是null,返回 emptyMap() <br>
     *         如果 <code>configString</code> 是empty,返回 emptyMap()<br>
     *         如果使用=号 分隔之后 value没值的 key-value 会被忽略
     * @since 3.3.4
     * @apiNote 自动去除空格,忽略空
     */
    public static Map<String, String> toSingleValueMap(String configString){
        return toSingleValueMap(configString, String.class, String.class);
    }

    /**
     * 将 189=988;200=455;这种格式的字符串转换成map , map的key 是189这种, value 是988,455 这种等号后面的值,使用逗号分隔成list.
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map<Integer, Integer> map = StringUtil.toSingleValueMap("1=2;89=100;200=230", Integer.class, Integer.class);
     * 
     * assertThat(
     *                 map,
     *                 allOf(//
     *                                 hasEntry(1, 2),
     *                                 hasEntry(89, 100),
     *                                 hasEntry(200, 230)));
     * 
     * </pre>
     * 
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <V>
     *            the value type
     * @param configString
     *            189=988;200=455; 这种格式的配置字符串, 用分号分隔一组, 等号分隔key 和value
     * @param keyClass
     *            key转换的类型,比如上面的背景中, 189 可以转换成String Long Integer
     * @param valueElementClass
     *            value转换的类型,比如上面的背景中, 455 可以转换成String Long Integer
     * @return 如果 <code>configString</code> 是null,返回 emptyMap() <br>
     *         如果 <code>configString</code> 是empty,返回 emptyMap()<br>
     *         如果 <code>keyClass</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>valueElementClass</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果使用=号 分隔之后 value没值的 key-value 会被忽略
     * @since 3.3.4
     * @apiNote 自动去除空格,忽略空
     */
    public static <T, V> Map<T, V> toSingleValueMap(String configString,Class<T> keyClass,Class<V> valueElementClass){
        return toMap(configString, keyClass, (valueString) -> {
            return convert(valueString, valueElementClass);
        });
    }

    //---------------------------------------------------------------

    /**
     * 将 73034693=0-50;11487680=0-43;51099626=0-50; 这种格式的字符串转换成map.
     * 
     * <p>
     * map的key 是73034693这种, value 可以TrackQueryExtendParam对象
     * 
     * <pre class="code">
     * public class TrackQueryExtendParam{
     * 
     *     // 单集编号 最小. 
     *     private Integer minEpisodeNumber;
     * 
     *     // 单集编号 最大. 
     *     private Integer maxEpisodeNumber;
     * 
     *     //setter/getter 省略
     * }
     * </pre>
     * </p>
     * 
     * <h3>解析代码示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * String configString = "73034693=0-50;\n"//
     *                 + "11487680=0-43;\n"//
     *                 + "51099626=0-50;";
     * 
     * Map{@code <Long, TrackQueryExtendParam>} map = StringUtil.toMap(configString, Long.class, (valueString) -> {
     *                                                   String[] albumQueryParamsArray = StringUtil.tokenizeToStringArray(valueString, "-");
     *                                                   if (isNullOrEmpty(albumQueryParamsArray)){
     *                                                       return null;
     *                                                   }
     *                                                   try{
     *                                                       //只有1个 
     *                                                       if (1 == size(albumQueryParamsArray)){
     *                                                           //默认 0-x
     *                                                           Integer max = toInteger(albumQueryParamsArray[0]);
     *                                                           return new TrackQueryExtendParam(0, max);
     *                                                       }
     * 
     *                                                       Integer min = toInteger(albumQueryParamsArray[0]);
     *                                                       Integer max = toInteger(albumQueryParamsArray[1]);
     *                                                       return new TrackQueryExtendParam(min, max);
     * 
     *                                                   }catch (Exception e){
     *                                                       return null;
     *                                                   }
     *                                               });
     * 
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <V>
     *            the value type
     * @param configString
     *            73034693=0-50;11487680=0-43;51099626=0-50; 这种格式的配置字符串, 用分号分隔一组, 等号分隔key 和value
     * @param keyClass
     *            key转换的类型,比如上面的背景中, 73034693 可以转换成String Long Integer
     * @param valueStringToBeanConverter
     *            value转换的类型
     * @return 如果 <code>configString</code> 是null,返回 emptyMap() <br>
     *         如果 <code>configString</code> 是empty,返回 emptyMap()<br>
     *         如果 <code>keyClass</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>stringToBeanConverter</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果使用=号 分隔之后 value没值的 key-value 会被忽略
     * @since 4.1.2
     * @apiNote 自动去除空格,忽略空
     */
    public static <T, V> Map<T, V> toMap(String configString,Class<T> keyClass,StringToBeanConverter<V> valueStringToBeanConverter){
        if (isNullOrEmpty(configString)){
            return emptyMap();
        }

        //---------------------------------------------------------------

        //entry之间的分隔符
        String entryDelimiters = ";";

        String[] entrys = StringUtil.tokenizeToStringArray(configString, entryDelimiters);
        if (isNullOrEmpty(entrys)){
            return emptyMap();
        }
        //---------------------------------------------------------------
        Validate.notNull(keyClass, "keyClass can't be null!");
        Validate.notNull(valueStringToBeanConverter, "stringToBeanConverter can't be null!");

        //---------------------------------------------------------------

        //key 和value之间的分隔符
        String keyAndValueDelimiters = "=";

        Map<T, V> map = newHashMap();
        for (String keyAndValueString : entrys){
            if (isNullOrEmpty(keyAndValueString)){
                continue;
            }
            String[] keyAndValues = StringUtil.tokenizeToStringArray(keyAndValueString, keyAndValueDelimiters);
            //如果使用=号 分隔之后 value没值的 key-value 会被忽略
            if (size(keyAndValues) != 2){
                continue;
            }
            //---------------------------------------------------------------
            T key = ConvertUtil.convert(keyAndValues[0], keyClass);
            V value = valueStringToBeanConverter.convert(keyAndValues[1]);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 将 asedeffrrfgg=张飞,关羽;asedcccccfgg=吕布;这种格式的字符串转换成map , map的key 是asedeffrrfgg,asedcccccfgg 这种, value 是张飞,关羽 吕布 这种等号后面的值,使用逗号分隔成list.
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map<String, List<String>> multiValueMap = StringUtil.toMultiValueMap("1=2;89=100 ,99;200=230, 999");
     * 
     * assertThat(
     *                 multiValueMap,
     *                 allOf(//
     *                                 hasEntry("1", toList("2")),
     *                                 hasEntry("89", toList("100", "99")),
     *                                 hasEntry("200", toList("230", "999"))));
     * 
     * </pre>
     * 
     * 
     * </blockquote>
     * 
     * @param configString
     *            asedeffrrfgg=张飞,关羽;asedcccccfgg=吕布; 这种格式的配置字符串, 用分号分隔一组, 等号分隔key 和value
     * @return 如果 <code>configString</code> 是null,返回 emptyMap() <br>
     *         如果 <code>configString</code> 是empty,返回 emptyMap()<br>
     *         如果使用=号 分隔之后 value没值的 key-value 会被忽略
     * @since 4.3.1
     * @apiNote 自动去除空格,忽略空
     */
    public static Map<String, List<String>> toMultiValueMap(String configString){
        return toMultiValueMap(configString, String.class, String.class);
    }

    /**
     * 将 asedeffrrfgg=张飞,关羽;asedcccccfgg=吕布;这种格式的字符串转换成map , map的key 是asedeffrrfgg,asedcccccfgg 这种, value 是张飞,关羽 吕布 这种等号后面的值,使用逗号分隔成list.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map<String, List<Integer>> multiValueMap = StringUtil.toMultiValueMap("1=2;89=100,99;200=230,999", Integer.class);
     * 
     * assertThat(
     *                 multiValueMap,
     *                 allOf(//
     *                                 hasEntry("1", toList(2)),
     *                                 hasEntry("89", toList(100, 99)),
     *                                 hasEntry("200", toList(230, 999))));
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <V>
     * 
     * @param configString
     *            asedeffrrfgg=张飞,关羽;asedcccccfgg=吕布; 这种格式的配置字符串, 用分号分隔一组, 等号分隔key 和value
     * @param valueElementClass
     *            value转换的类型,比如上面的背景中, 张飞,关羽 可以转换成String 或者其他类型
     * @return 如果 <code>configString</code> 是null,返回 emptyMap() <br>
     *         如果 <code>configString</code> 是empty,返回 emptyMap()<br>
     *         如果 <code>valueElementClass</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果使用=号 分隔之后 value没值的 key-value 会被忽略
     * @since 4.3.1
     * @apiNote 自动去除空格,忽略空
     */
    public static <V> Map<String, List<V>> toMultiValueMap(String configString,Class<V> valueElementClass){
        return toMultiValueMap(configString, String.class, valueElementClass);
    }

    /**
     * 将 189=988,900;200=455;这种格式的字符串转换成map , map的key 是189,200 这种, value 是988,900,455 这种等号后面的值,使用逗号分隔成list.
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map<Integer, List<Integer>> multiValueMap = StringUtil.toMultiValueMap("1=2;89=100,99;200=230,999", Integer.class, Integer.class);
     * 
     * assertThat(
     *                 multiValueMap,
     *                 allOf(//
     *                                 hasEntry(1, toList(2)),
     *                                 hasEntry(89, toList(100, 99)),
     *                                 hasEntry(200, toList(230, 999))));
     * 
     * </pre>
     * 
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param <V>
     *            the value type
     * @param configString
     *            189=988,900;200=455; 这种格式的配置字符串, 用分号分隔一组, 等号分隔key 和value
     * @param keyClass
     *            key转换的类型,比如上面的背景中, 189 可以转换成String Long Integer
     * @param valueElementClass
     *            value转换的类型,比如上面的背景中, 455 可以转换成String Long Integer
     * @return 如果 <code>configString</code> 是null,返回 emptyMap() <br>
     *         如果 <code>configString</code> 是empty,返回 emptyMap()<br>
     *         如果 <code>keyClass</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>valueElementClass</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果使用=号 分隔之后 value没值的 key-value 会被忽略
     * @since 3.3.4
     * @apiNote 自动去除空格,忽略空
     */
    public static <T, V> Map<T, List<V>> toMultiValueMap(String configString,Class<T> keyClass,Class<V> valueElementClass){
        return toMap(configString, keyClass, (valueString) -> {
            String[] values = StringUtil.tokenizeToStringArray(valueString, ",");
            return toList(toArray(values, valueElementClass));
        });
    }

    //---------------------------------------------------------------

    // [start]format

    /**
     * 将各类数据,使用{@link String#format(String, Object...)}格式化为字符串.
     * 
     * <h3>规则:</h3>
     * <blockquote>
     * 
     * <ol>
     * 
     * <li>
     * 
     * <p>
     * <b>对整数进行格式化:</b>
     * </p>
     * 
     * <p>
     * 由4部分组成:<span style="color:green">%[index$][标识][最小宽度]转换方式</span>
     * </p>
     * 
     * <p>
     * 此外,StringUtil.format("%03d", 1) 不能写成 StringUtil.format("%03d", "1")
     * </p>
     * </li>
     * 
     * <li>
     * <p>
     * <b>对浮点数进行格式化:</b>
     * </p>
     * 
     * <p>
     * <span style="color:green">%[index$][标识][最少宽度][.精度]转换方式</span>
     * </p>
     * 
     * </li>
     * 
     * <li>%index$开头,index从1开始取值,表示将第index个参数拿进来进行格式化.</li>
     * 
     * </ol>
     * 
     * </blockquote>
     * 
     * <p>
     * 转换符和标志的说明:
     * </p>
     * 
     * <h3>转换符</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">转换符</th>
     * <th align="left">说明</th>
     * <th align="left">示例</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%s</td>
     * <td>字符串类型</td>
     * <td>"mingrisoft"</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%c</td>
     * <td>字符类型</td>
     * <td>'m'</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%b</td>
     * <td>布尔类型</td>
     * <td>true</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%d</td>
     * <td>整数类型(十进制)</td>
     * <td>99</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%x</td>
     * <td>整数类型(十六进制)</td>
     * <td>FF</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%o</td>
     * <td>整数类型(八进制)</td>
     * <td>77</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%f</td>
     * <td>浮点类型</td>
     * <td>99.99</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%a</td>
     * <td>十六进制浮点类型</td>
     * <td>FF.35AE</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%e</td>
     * <td>指数类型</td>
     * <td>9.38e+5</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%g</td>
     * <td>通用浮点类型(f和e类型中较短的)</td>
     * <td></td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%h</td>
     * <td>散列码</td>
     * <td></td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%%</td>
     * <td>百分比类型</td>
     * <td>％</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>%n</td>
     * <td>换行符</td>
     * <td></td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>%tx</td>
     * <td>日期与时间类型(x代表不同的日期与时间转换符</td>
     * <td></td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * <h3>标志</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">标志</th>
     * <th align="left">说明</th>
     * <th align="left">示例</th>
     * <th align="left">结果</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>+</td>
     * <td>为正数或者负数添加符号</td>
     * <td>("%+d",15)</td>
     * <td>+15</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>-</td>
     * <td>左对齐(不可以与"用0填充"同时使用)</td>
     * <td>("%-5d",15)</td>
     * <td>|15 |</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>0</td>
     * <td>数字前面补0</td>
     * <td>("%04d", 99)</td>
     * <td>0099</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>空格</td>
     * <td>在整数之前添加指定数量的空格</td>
     * <td>("% 4d", 99)</td>
     * <td>| 99|</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>,</td>
     * <td>以","对数字分组</td>
     * <td>("%,f", 9999.99)</td>
     * <td>9,999.990000</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>(</td>
     * <td>使用括号包含负数</td>
     * <td>("%(f", -99.99)</td>
     * <td>(99.990000)</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>#</td>
     * <td>如果是浮点数则包含小数点,如果是16进制或8进制则添加0x或0</td>
     * <td>("%#x", 99) <br>
     * ("%#o", 99)</td>
     * <td>0x63<br>
     * 0143</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@code <}</td>
     * <td>格式化前一个转换符所描述的参数</td>
     * <td>("%f和%{@code <}3.2f", 99.45)</td>
     * <td>99.450000和99.45</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>$</td>
     * <td>被格式化的参数索引</td>
     * <td>("%1$d,%2$s", 99,"abc")</td>
     * <td>99,abc</td>
     * </tr>
     * 
     * </table>
     * </blockquote>
     * 
     * @param format
     *            the format
     * @param args
     *            the args
     * @return 如果 <code>format</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>format</code> 包含不需要转化的字符串,这些字符串是你写什么,最终就输出什么<br>
     *         否则返回 {@link String#format(String, Object...)}
     * @see java.util.Formatter
     * @see String#format(String, Object...)
     * @see String#format(java.util.Locale, String, Object...)
     * @since JDK 1.5
     */
    public static String format(String format,Object...args){
        return null == format ? EMPTY : String.format(format, args);
    }

    /**
     * 格式化字符串,此方法是抽取slf4j的核心方法.
     * 
     * <p>
     * 在java中,常会拼接字符串生成新的字符串值,在字符串拼接过程中容易写错或者位置写错<br>
     * <br>
     * slf4j的log支持格式化输出log,比如:<br>
     * </p>
     * 
     * <ul>
     * <li>log.debug("{}","feilong");</li>
     * <li>log.info("{},{}","feilong","hello");</li>
     * </ul>
     * 
     * 这些写法非常简洁且有效,不易出错
     * 
     * <br>
     * 因此,你可以在代码中出现这样的写法:
     * 
     * <pre class="code">
     * throw new IllegalArgumentException(StringUtil.formatPattern(
     *  "callbackUrl:[{}] ,length:[{}] can't {@code >}{}",
     *  callbackUrl,
     *  callbackUrlLength,
     *  callbackUrlMaxLength)
     * </pre>
     * 
     * 又或者
     * 
     * <pre class="code">
     * return StringUtil.formatPattern("{} [{}]", encode, encode.length());
     * </pre>
     * 
     * @param messagePattern
     *            message的格式,比如 callbackUrl:[{}] ,length:[{}]
     * @param args
     *            参数
     * @return 如果 <code>messagePattern</code> 是null,返回 null<br>
     *         如果 <code>args</code> 是null,返回 <code>messagePattern</code><br>
     * @see org.slf4j.helpers.FormattingTuple
     * @see org.slf4j.helpers.MessageFormatter#arrayFormat(String, Object[])
     * @see org.slf4j.helpers.FormattingTuple#getMessage()
     * @since 3.3.8
     */
    public static String formatPattern(String messagePattern,Object...args){
        if (null == messagePattern){
            return EMPTY;
        }
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(messagePattern, args);
        return formattingTuple.getMessage();
    }

    // [end]

    /**
     * 将两个字符串,去空格之后忽视大小写对比.
     * 
     * 
     * <pre class="code">
     * StringUtil.trimAndEqualsIgnoreCase(null, null)   = true
     * StringUtil.trimAndEqualsIgnoreCase(null, "")     = false
     * StringUtil.trimAndEqualsIgnoreCase("", "  ")     = true
     * StringUtil.trimAndEqualsIgnoreCase("", null)     = false
     * StringUtil.trimAndEqualsIgnoreCase("feilong  ", "   feilong")     = true
     * </pre>
     *
     * @param s1
     *            第1个字符串
     * @param s2
     *            第2个字符串
     * @return 如果 <code>s1, s2</code> 都是null,返回 true<br>
     * @since 3.1.1
     */
    public static boolean trimAndEqualsIgnoreCase(String s1,String s2){
        return StringUtils.equalsIgnoreCase(StringUtils.trim(s1), StringUtils.trim(s2));
    }

    /**
     * 清除charSequence 中一些特殊符号, 目前会清除 backspace (\b), 三个特殊空格 \u00A0,\u2007,\u202F.
     *
     * @param charSequence
     *            the char sequence
     * @return 如果 <code>charSequence</code> 是null,返回 {@link #EMPTY}<br>
     *         如果 <code>charSequence</code> 是empty,,返回 {@link #EMPTY}<br>
     * @since 3.3.8
     */
    public static String clean(CharSequence charSequence){
        if (isNullOrEmpty(charSequence)){
            return EMPTY;
        }

        //---------------------------------------------------------------
        String string = removeSpecialSpace(charSequence);

        //去空格
        string = StringUtils.trim(string);

        //        反斜杠（\）在字符串内有特殊含义，用来表示一些特殊字符，所以又称为转义符。
        //        \0:null(\u0000)
        //        \b ：后退键（\u0008）
        //        \f ：换页符（\u000C）
        //        n ：换行符（000A）
        //        r ：回车键（000D）
        //        \t ：制表符（\u0009）
        //        \v ：垂直制表符（\u000B）
        //        \' ：单引号（\u0027）
        //        \" ：双引号（\u0022）
        //        \\ ：反斜杠（\u005C）
        //        '\251' // "©"
        //        '\xA9' // "©"
        //        '\u00A9' // "©"

        //后退键 backspace see https://github.com/ifeilong/feilong/issues/55
        string = com.feilong.lib.lang3.StringUtils.remove(string, '\u0008');

        return string;
    }

    /**
     * 删除特殊空格.
     * 
     * <p>
     * \u00A0,\u2007,\u202F
     * </p>
     *
     * @param charSequence
     *            the char sequence
     * @return the string
     * @since 3.3.8
     */
    public static String removeSpecialSpace(CharSequence charSequence){
        String string = ConvertUtil.toString(charSequence);
        //不间断空格 as a space,but often not adjusted   see https://github.com/ifeilong/feilong/issues/56
        string = com.feilong.lib.lang3.StringUtils.remove(string, '\u00A0');

        //字符U+2007---U+200A和U+202F在Unicode标准中没有给它们分配精确的宽度，字符的显示实现可能会与预期的宽度有很大偏差。
        //此外，在出版软件中使用相同名称的概念时，比如“窄空格”，其含义可能会有很大的不同。
        //例如，
        //在 InDesign 软件中，“thin space窄空格”是1/8 em (即0.125 em，与建议的0.2 em 相反) ，
        //而“hair space发际空格”只有1/24 em (即大约0.042 em，而窄空格标志的宽度通常在0.1em和0.2 em之间变化)。
        //figure space
        string = com.feilong.lib.lang3.StringUtils.remove(string, '\u2007');

        //narrow no-break space
        //比不间断空格(或者空格)都窄
        string = com.feilong.lib.lang3.StringUtils.remove(string, '\u202F');

        return string;
    }
}