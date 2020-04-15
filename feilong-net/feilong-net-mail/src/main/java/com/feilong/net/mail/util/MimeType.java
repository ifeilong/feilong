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
package com.feilong.net.mail.util;

/**
 * 多用途互联网邮件扩展(MIME,Multipurpose Internet Mail Extensions)是一个互联网标准,它扩展了电子邮件标准.
 * 
 * <blockquote>
 * <p>
 * 这个标准被定义在RFC 2045、RFC 2046、RFC 2047、RFC 2048、RFC 2049等RFC中.
 * </p>
 * </blockquote>
 * 
 * <h3>内容类型(Content-Type):</h3>
 * 
 * <blockquote>
 * <p>
 * 这个头部领域用于指定消息的类型.一般以下面的形式出现: <br>
 * Content-Type: [type]/[subtype]; parameter
 * </p>
 * </blockquote>
 * 
 * <h3>type有下面的形式:</h3>
 * 
 * <blockquote>
 * <p>
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>Text</td>
 * <td>用于标准化地表示的文本信息,文本消息可以是多种字符集和或者多种格式的;</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>Multipart</td>
 * <td>用于连接消息体的多个部分构成一个消息,这些部分可以是不同类型的数据;</td>
 * </tr>
 * <tr valign="top">
 * <td>Application</td>
 * <td>用于传输应用程序数据或者二进制数据;</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>Message</td>
 * <td>用于包装一个E-mail消息;</td>
 * </tr>
 * <tr valign="top">
 * <td>Image</td>
 * <td>用于传输静态图片数据;</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>Audio</td>
 * <td>用于传输音频或者音声数据;</td>
 * </tr>
 * <tr valign="top">
 * <td>Video</td>
 * <td>用于传输动态影像数据,可以是与音频编辑在一起的视频数据格式.</td>
 * </tr>
 * </table>
 * </blockquote>
 * </p>
 * </blockquote>
 * 
 * 
 * <h3>subtype用于指定type的详细形式:</h3>
 * 
 * <blockquote>
 * <p>
 * 
 * content-type/subtype配对的集合和与此相关的参数,将随着时间而增长.<br>
 * 
 * 为了确保这些值在一个有序而且公开的状态下开发,<br>
 * MIME使用Internet Assigned Numbers Authority (IANA)作为中心的注册机制来管理这些值.<br>
 * 
 * 常用的subtype值如下所示:<br>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>text/plain</td>
 * <td>纯文本</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>text/html</td>
 * <td>HTML文档</td>
 * </tr>
 * <tr valign="top">
 * <td>application/xhtml+xml</td>
 * <td>XHTML文档</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>image/gif</td>
 * <td>GIF图像</td>
 * </tr>
 * <tr valign="top">
 * <td>image/jpeg</td>
 * <td>JPEG图像【PHP中为:image/pjpeg】</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>image/png</td>
 * <td>PNG图像【PHP中为:image/x-png】</td>
 * </tr>
 * <tr valign="top">
 * <td>video/mpeg</td>
 * <td>MPEG动画</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>application/octet-stream</td>
 * <td>任意的二进制数据</td>
 * </tr>
 * <tr valign="top">
 * <td>application/pdf</td>
 * <td>PDF文档</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>application/msword</td>
 * <td>Microsoft Word文件</td>
 * </tr>
 * <tr valign="top">
 * <td>application/vnd.wap.xhtml+xml</td>
 * <td>wap1.0+</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>application/xhtml+xml</td>
 * <td>wap2.0+</td>
 * </tr>
 * <tr valign="top">
 * <td>message/rfc822</td>
 * <td>RFC 822形式</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>multipart/alternative</td>
 * <td>HTML邮件的HTML形式和纯文本形式,相同内容使用不同形式表示,alternative content, such as a message sent in both plain text and another format such as HTML
 * (multipart/alternative with the same content in text/plain and text/html forms)</td>
 * </tr>
 * <tr valign="top">
 * <td>application/x-www-form-urlencoded</td>
 * <td>使用HTTP的POST方法提交的表单</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>multipart/form-data</td>
 * <td>同上,但主要用于表单提交时伴随文件上传的场合</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 此外,尚未被接受为正式数据类型的subtype,可以使用x-开始的独立名称(例如application/x-gzip).<br>
 * vnd-开始的固有名称也可以使用(例:application/vnd.ms-excel).
 * 
 * </p>
 * </blockquote>
 * 
 * 
 * <h3>parameter可以用来指定附加的信息:</h3>
 * 
 * <blockquote>
 * <p>
 * 更多情况下是用于指定text/plain和text/htm等的文字编码方式的charset参数.<br>
 * 
 * MIME根据type制定了默认的subtype, 当客户端不能确定消息的subtype的情况下,消息被看作默认的subtype进行处理.<br>
 * 
 * Text默认是text/plain,<br>
 * Application默认是application/octet-stream<br>
 * 而Multipart默认情况下被看作multipart/mixed.<br>
 * </p>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.io.entity.MimeType
 * @since 1.2.0
 */
public final class MimeType{

    /** <code>{@value}</code>. */
    public static final String TEXT_ALL              = "text/*";

    /** <code>{@value}</code>. */
    public static final String TEXT_HTML             = com.feilong.io.entity.MimeType.HTML.getMime();

    //---------------------------------------------------------------

    /** <code>{@value}</code>. */
    public static final String MULTIPART_ALL         = "multipart/*";

    /** HTML邮件的HTML形式和纯文本形式,相同内容使用不同形式表示 <code>{@value}</code>. */
    public static final String MULTIPART_ALTERNATIVE = "multipart/alternative";

    /** <code>{@value}</code>. */
    public static final String MESSAGE_RFC822        = "message/rfc822";

    /**
     * 日历数据交换的标准<code>{@value}</code>.
     * 
     * @since 1.10.2
     * @see <a href="http://www.ibm.com/developerworks/cn/java/j-lo-ical4j/">iCalendar 编程基础：了解和使用 iCal4j</a>
     * @see <a href="https://en.wikipedia.org/wiki/ICalendar">ICalendar</a>
     * @see <a href="https://datatracker.ietf.org/doc/rfc5545/">Internet Calendaring and Scheduling Core Object Specification
     *      (iCalendar)</a>
     * @see <a href="https://rsync.tools.ietf.org/html/rfc5545">Internet Calendaring and Scheduling Core Object Specification(iCalendar)</a>
     */
    public static final String TYPE_ICS              = "text/calendar;method=REQUEST;charset=\"UTF-8\"";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private MimeType(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

}
