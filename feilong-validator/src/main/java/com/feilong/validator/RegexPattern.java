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
package com.feilong.validator;

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;

import java.util.Map;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 常用的正则表达式模式.
 * 
 * <h3>开始结束</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>\</td>
 * <td>将下一个字符标记为一个特殊字符、或一个原义字符、或一个 向后引用、或一个八进制转义符.<br>
 * 例如,'n' 匹配字符 "n".'\n' 匹配一个换行符.序列 '\\' 匹配 "\" 而 "\(" 则匹配 "(".</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>^</td>
 * <td>匹配输入字符串的开始位置.如果设置了 RegExp 对象的 Multiline 属性,^ 也匹配 '\n' 或 '\r' 之后的位置.</td>
 * </tr>
 * <tr valign="top">
 * <td>$</td>
 * <td>匹配输入字符串的结束位置.如果设置了RegExp 对象的 Multiline 属性,$ 也匹配 '\n' 或 '\r' 之前的位置.</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>次数</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>*</td>
 * <td>匹配前面的子表达式 <b>零次</b> 或 <b>多次</b>.<br>
 * 例如,zo* 能匹配 "z" 以及 "zoo".* 等价于{0,}.</td>
 * </tr>
 * <tr valign="top">
 * <td>+</td>
 * <td>匹配前面的子表达式 <b>一次</b> 或 <b>多次</b>.<br>
 * 例如,'zo+' 能匹配 "zo" 以及 "zoo",但不能匹配 "z".+ 等价于 {1,}.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>?</td>
 * <td>匹配前面的子表达式<b> 零次</b> 或 <b>一次</b>.<br>
 * 例如,"do(es)?" 可以匹配 "do" 或 "does" 中的"do" .? 等价于 {0,1}.</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>简写</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>\d</td>
 * <td>匹配一个数字字符.等价于 [0-9].</td>
 * </tr>
 * <tr valign="top">
 * <td>\D</td>
 * <td>匹配一个非数字字符.等价于 [^0-9].</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>\s</td>
 * <td>匹配任何空白字符,包括空格、制表符、换页符等等.等价于 [ \f\n\r\t\v].</td>
 * </tr>
 * <tr valign="top">
 * <td>\S</td>
 * <td>匹配任何非空白字符.等价于 [^ \f\n\r\t\v].</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>\w</td>
 * <td>匹配包括下划线的任何单词字符.等价于'[A-Za-z0-9_]'.</td>
 * </tr>
 * <tr valign="top">
 * <td>\W</td>
 * <td>匹配任何非单词字符.等价于 '[^A-Za-z0-9_]'.</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>特殊</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>\n</td>
 * <td>匹配一个换行符.等价于 \x0a 和 \cJ.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>\r</td>
 * <td>匹配一个回车符.等价于 \x0d 和 \cM.</td>
 * </tr>
 * <tr valign="top">
 * <td>\t</td>
 * <td>匹配一个制表符.等价于 \x09 和 \cI.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>\v</td>
 * <td>匹配一个垂直制表符.等价于 \x0b 和 \cK.</td>
 * </tr>
 * <tr valign="top">
 * <td>\f</td>
 * <td>匹配一个换页符.等价于 \x0c 和 \cL.</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>全部符号解释</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{n}</td>
 * <td>n 是一个非负整数.匹配确定的 n 次.例如,'o{2}' 不能匹配 "Bob" 中的 'o',但是能匹配 "food" 中的两个 o.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{n,}</td>
 * <td>n 是一个非负整数.至少匹配n 次.例如,'o{2,}' 不能匹配 "Bob" 中的 'o',但能匹配 "foooood" 中的所有 o.'o{1,}' 等价于 'o+'.'o{0,}' 则等价于 'o*'.</td>
 * </tr>
 * <tr valign="top">
 * <td>{n,m}</td>
 * <td>m 和 n 均为非负整数,其中 {@code n <= m}.最少匹配 n 次且最多匹配 m 次.例如,"o{1,3}" 将匹配 "fooooood" 中的前三个 o.'o{0,1}' 等价于 'o?'.请注意在逗号和两个数之间不能有空格.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>?</td>
 * <td>当该字符紧跟在任何一个其他限制符 (*, +, ?, {n}, {n,}, {n,m}) 后面时,匹配模式是非贪婪的.非贪婪模式尽可能少的匹配所搜索的字符串,而默认的贪婪模式则尽可能多的匹配所搜索的字符串.例如,对于字符串 "oooo",'o+?' 将匹配单个
 * "o",而 'o+' 将匹配所有 'o'.</td>
 * </tr>
 * <tr valign="top">
 * <td>.</td>
 * <td>匹配除 "\n" 之外的任何单个字符.要匹配包括 '\n' 在内的任何字符,请使用象 '[.\n]' 的模式.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>(pattern)</td>
 * <td>匹配 pattern 并获取这一匹配.所获取的匹配可以从产生的 Matches 集合得到,在VBScript 中使用 SubMatches 集合,在JScript 中则使用 $0…$9 属性.要匹配圆括号字符,请使用 '\(' 或 '\)'.</td>
 * </tr>
 * <tr valign="top">
 * <td>(?:pattern)</td>
 * <td>匹配 pattern 但不获取匹配结果,也就是说这是一个非获取匹配,不进行存储供以后使用.这在使用 "或" 字符 (|) 来组合一个模式的各个部分是很有用.例如, 'industr(?:y|ies) 就是一个比 'industry|industries'
 * 更简略的表达式.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>(?=pattern)</td>
 * <td>正向预查,在任何匹配 pattern 的字符串开始处匹配查找字符串.这是一个非获取匹配,也就是说,该匹配不需要获取供以后使用.例如,'Windows (?=95|98|NT|2000)' 能匹配 "Windows 2000" 中的 "Windows" ,但不能匹配
 * "Windows 3.1" 中的 "Windows".预查不消耗字符,也就是说,在一个匹配发生后,在最后一次匹配之后立即开始下一次匹配的搜索,而不是从包含预查的字符之后开始.</td>
 * </tr>
 * <tr valign="top">
 * <td>(?!pattern)</td>
 * <td>负向预查,在任何不匹配 pattern 的字符串开始处匹配查找字符串.这是一个非获取匹配,也就是说,该匹配不需要获取供以后使用.例如'Windows (?!95|98|NT|2000)' 能匹配 "Windows 3.1" 中的 "Windows",但不能匹配
 * "Windows 2000" 中的 "Windows".预查不消耗字符,也就是说,在一个匹配发生后,在最后一次匹配之后立即开始下一次匹配的搜索,而不是从包含预查的字符之后开始</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>x|y</td>
 * <td>匹配 x 或 y.例如,'z|food' 能匹配 "z" 或 "food".'(z|f)ood' 则匹配 "zood" 或 "food".</td>
 * </tr>
 * <tr valign="top">
 * <td>[xyz]</td>
 * <td>字符集合.匹配所包含的任意一个字符.例如, '[abc]' 可以匹配 "plain" 中的 'a'.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>[^xyz]</td>
 * <td>负值字符集合.匹配未包含的任意字符.例如, '[^abc]' 可以匹配 "plain" 中的'p'.</td>
 * </tr>
 * <tr valign="top">
 * <td>[a-z]</td>
 * <td>字符范围.匹配指定范围内的任意字符.例如,'[a-z]' 可以匹配 'a' 到 'z' 范围内的任意小写字母字符.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>[^a-z]</td>
 * <td>负值字符范围.匹配任何不在指定范围内的任意字符.例如,'[^a-z]' 可以匹配任何不在 'a' 到 'z' 范围内的任意字符.</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>\b</td>
 * <td>匹配一个单词边界,也就是指单词和空格间的位置.例如, 'er\b' 可以匹配"never" 中的 'er',但不能匹配 "verb" 中的 'er'.</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>\B</td>
 * <td>匹配非单词边界.'er\B' 能匹配 "verb" 中的 'er',但不能匹配 "never" 中的 'er'.</td>
 * </tr>
 * <tr valign="top">
 * <td>\cx</td>
 * <td>匹配由 x 指明的控制字符.例如, \cM 匹配一个 Control-M 或回车符.x 的值必须为 A-Z 或 a-z 之一.否则,将 c 视为一个原义的 'c' 字符.</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * <pre class="code">
 * {@code
 * 1.字符类是可选自符的集合,用‘[’封装,比如[Jj],[0-9],[A-Za-z]或[^0-9].这里的-表示范围(Unicode落在两个边界之间的所有字符),^表示求补(指定字符外的所有字符 
 * 2.有许多预定以的字符类,像\d(数字)或\p&#123;Sc&#125;(Unicode货币符号),见表12-8和12-9. 
 * 3大多数字符与它们自身匹配,像上例中的java字符. 
 * 4.符号.匹配任何字符(可能行终止符(line terminators)除外,这依赖于标识设置(flag settings)) 
 * 5.\用作转义符,比如\.匹配一个句点,\\匹配一个反斜杠. 
 * 6.^和$分别匹配行头和行尾 
 * 7.如果X和Y都是正则表达式,则XY表示"X的匹配后面跟着Y的匹配".X|Y表示"任何X或Y的匹配" 
 * 8.可以将量词(quantifier)用到表达式中,X+ 表示X重复1次或多次,X* 表示X重复0次或多次,X? 表示X重复0次或1次 
 * 9.默认地,一个量词总是与使总体成功匹配的最长的可能重复匹配.可以加上后缀？(称为reluctant或stingy 匹配,用以匹配最小的重复数),和+(称为possessive或贪婪匹配,用以即使在总体匹配失败的情况下也匹配最大的重复数)来更改这种属性. 
 * }
 * </pre>
 * 
 * <h3>不建议使用自定义正则的地方</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * 
 * <tr style="background-color:#ccccff">
 * <th align="left">内容</th>
 * <th align="left">原正则</th>
 * <th align="left">替代方法</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>网址Url 链接</td>
 * <td>{@code http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?}</td>
 * <td>commons-validator<br>
 * org.apache.commons.validator.routines.DomainValidator.getInstance().isValid(emailString)</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>所有都是字母</td>
 * <td>^[A-Za-z]+$</td>
 * <td>{@link com.feilong.lib.lang3.StringUtils#isAlpha(CharSequence)}</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>大写字母</td>
 * <td>^[A-Z]+$</td>
 * <td>{@link com.feilong.lib.lang3.StringUtils#isAllUpperCase(CharSequence)}</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>小写字母</td>
 * <td>^[a-z]+$</td>
 * <td>{@link com.feilong.lib.lang3.StringUtils#isAllLowerCase(CharSequence)}</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>IP</td>
 * <td>^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.
 * (25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)</td>
 * <td>commons-validator<br>
 * org.apache.commons.validator.routines.InetAddressValidator.getInstance().isValid(emailString)</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>纯数字</td>
 * <td>^[0-9]*$</td>
 * <td>{@link com.feilong.lib.lang3.StringUtils#isNumeric(CharSequence)}</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>字母和数字 (alpha numeric)</td>
 * <td>^[0-9a-zA-Z]+$</td>
 * <td>{@link com.feilong.lib.lang3.StringUtils#isAlphanumeric(CharSequence)}</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>字母和数字和空格(alpha numeric space)</td>
 * <td>^[0-9a-zA-Z ]+$</td>
 * <td>{@link com.feilong.lib.lang3.StringUtils#isAlphanumericSpace(CharSequence)}</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see Pattern
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexPattern{

    /**
     * 配置的正则表达式.
     * 
     * @since 1.10.6
     */
    private static final Map<String, String> REGEX_PATTERN_MAP       = toMap(getResourceBundle("config/feilong-validator-regex"));

    //---------------------------------------------------------------

    /**
     * 大陆的电话号码 <code>{@value}</code>.
     * 
     * <p>
     * 支持 (3-4位)区号(option)+(6-8位)直播号码+(1-6位)分机号(option) 的组合
     * </p>
     * 
     * <h3>有效的:</h3>
     * 
     * <blockquote>
     * 
     * <ul>
     * <li>86771588</li>
     * <li>021-86771588</li>
     * <li>021-867715</li>
     * <li>86771588-888</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * <h3>无效的:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * "",
     * "   ",
     * "02021-86771588-888", //区号3-4位 太长了
     * "020-86771588888", //直播号码6-8位 太长了
     * "021-86775" //直播号码 需要 6-8位
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>关于分机号(extension number):</h3>
     * 
     * <blockquote>
     * <p>
     * 分机号是相对总机号说的,一般公司或企事业单位为了节省外线电话,会购置一台集团电话或电话交换机,外线或叫直线电话接到交换机上,可以设一部总机,外线的电话全部接入总机,通过总机代转,<br>
     * 一般一个电话交换机能接入外线和分出的分机是由交换机的型号决定,各分机可以设1-6位分机号,各分机之间可通过分机号任打电话,接打外线由电话交换机自动调配线路。
     * </p>
     * 
     * <p>
     * 分机号比较理想的位数为4位,第一位可以设置成部门识别号,其他的可以根据需要设置。
     * 千万别用三位的紧急服务号码来做分机号,如果有人用直线电话误操作的话,会带来不必要的麻烦
     * </p>
     * </blockquote>
     * 
     * @see <a href="https://en.wikipedia.org/wiki/Extension_(telephone)">Extension_(telephone)</a>
     * @see <a href="http://regexlib.com/Search.aspx?k=phone+number&c=7&m=5&ps=20">regexlib</a>
     */
    public static final String               TELEPHONE               = defaultIfNullOrEmpty(
                    REGEX_PATTERN_MAP.get("regex_pattern_telephone"),
                    "^(\\d{3,4}-)?\\d{6,8}(-\\d{1,6})?$");

    /**
     * 大陆的电话号码(必须要有区号) <code>{@value}</code>.
     * 
     * <p>
     * 支持 (3-4位)区号(must)+(6-8位)直播号码+(1-6位)分机号(option) 的组合
     * </p>
     * 
     * <h3>有效的:</h3>
     * 
     * <blockquote>
     * 
     * <ul>
     * <li>021-86771588</li>
     * <li>021-867715</li>
     * <li>021-86771588-888</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * <h3>无效的:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * "",
     * "   ",
     * "86771588", //没有区号
     * "02021-86771588-888", //区号3-4位 太长了
     * "86771588-888", //没有区号
     * "020-86771588888", //直播号码6-8位 太长了
     * "021-86775" //直播号码 需要 6-8位
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>区号</h3>
     * 
     * <blockquote>
     * <p>
     * 是指世界各大城市所属行政区域常用电话区划号码,这些号码主要用于国内、国际长途电话接入。<br>
     * 比如,中国大陆国际区号86,北京区号010、广州区号020等。<br>
     * 而在使用国内长途电话时,区号前要加拨0。<br>
     * 值得一提的是,人们往往容易混淆概念,误以为区号本身前面有一个0。也就是说,由于0是唯一的国内长途接入码,经常和后面的区号并列使用,所以形成了习惯。<br>
     * 实际上在境外打电话回境内某城市时,当地国际长途电话接入码加在中国国家号86之后,该城市区号前没有0。比如,成都区号应是28,而非028。
     * </p>
     * </blockquote>
     * 
     * @see <a href="http://baike.baidu.com/view/103379.htm">区号</a>
     * @since 1.7.1
     */
    public static final String               TELEPHONE_MUST_AREACODE = defaultIfNullOrEmpty(
                    REGEX_PATTERN_MAP.get("regex_pattern_telephone_must_areacode"),
                    "^\\d{3,4}-\\d{6,8}(-\\d{1,6})?$");

    //---------------------------------------------------------------

    /**
     * 手机号码 <code>{@value}</code>.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <p>
     * 目前严格校验的话,有 <a href="http://blog.csdn.net/skychenjh/article/details/49923345">2015年最新手机号码正则表达式</a>
     * </p>
     * 
     * <pre class="code">
     * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0-9] 
     * 移动号段: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705 
     * 联通号段: 130,131,132,155,156,185,186,145,176,1709 
     * 电信号段: 133,153,180,181,189,177,1700
     * </pre>
     * 
     * <p>
     * 最近逐渐出现的145 147段,以后会不会有148 149段?不好说,难道每次都需要更新?<br>
     * 所以这里使用稍微宽松的写法,<span style="color:red">只严格校验前两位</span>
     * </p>
     * 
     * </blockquote>
     * 
     * <h3>182段</h3>
     * 
     * <blockquote>
     * <p>
     * 北京移动正式启用182新号段 昨天,记者从北京移动获悉,从今年1月开始,北京移动启动了182新号段.<br>
     * 其中动感地带号段为18210000000到18210369999,数量为37万,<br>
     * 神州行畅听卡号段为18210370000到18210649999,数量为25万,<br>
     * 神州行家园卡号段为18210650000到18210899999,数量为25万.
     * </p>
     * </blockquote>
     * 
     * <h3><a href="http://www.dvbcn.com/2014/09/23-113699.html">176、177、178段</a></h3>
     * 
     * <blockquote>
     * <p>
     * 工信部为了鼓励民间资本进去电信行业,专门发放电信拍照.然后第三方电信上商又从三大运营商买的号码 <br>
     * 按照工信部的批复,三大运营商4G新用户均将使用17开头的号段. <br>
     * 中国电信,称177号段为中国电信4G专属号段,并在7月15日向在上海等16个城市实行177号段放号.<br>
     * 中国移动,则在今年已使用178号段并对多个城市进行放号.<br>
     * </p>
     * </blockquote>
     * 
     * @see <a href="http://blog.csdn.net/skychenjh/article/details/49923345">2015年最新手机号码正则表达式</a>
     * @see <a href="https://www.ithome.com/html/it/319951.htm?mp_sourceid=0.1.1">新号段时代来临：工信部新批电信199/移动198/联通166</a>
     */
    public static final String               MOBILEPHONE             = defaultIfNullOrEmpty(
                    REGEX_PATTERN_MAP.get("regex_pattern_mobile"),
                    "^1[3456789]\\d{9}$");
    //---------------------------------------------------------------

    /** 邮政编码 <code>{@value}</code>. */
    public static final String               ZIPCODE                 = defaultIfNullOrEmpty(
                    REGEX_PATTERN_MAP.get("regex_pattern_telephone_zipcode"),
                    "^\\d{6}$");

    //---------------------------------------------------------------
    /**
     * 两位数小数 <code>{@value}</code>
     * 
     * <p>
     * 可以是200 也可以是200.00 不可以是 200.0
     * </p>
     */
    public static final String               DECIMAL_TWO_DIGIT       = "^[0-9]+(.[0-9]{2})?$";

}