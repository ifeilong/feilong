/**
 * 从原来的 bs tengineConnect css js请求合并标签移植.
 * 
 * <p>
 * 目前适用于 :
 * tengine ngx_http_concat_module
 * apache mod_concat
 * </p>
 * 
 * <br>
 * 请求参数需要用两个问号('??')例如:http://example.com/??style1.css,style2.css,foo/style3.css
 * <br>
 * 参数中某位置只包含一个‘?’,则'?'后表示文件的版本,例如:http://example.com/??style1.css,style2.css,foo/style3.css?v=102234
 * 
 * 作用:遵循Yahoo!前端优化准则第一条:减少HTTP请求发送次数<br>
 * 这一功能可以组合Javascript 以及 Css文件<br>
 * 使用方法:<br>
 * a)以两个问号(??)激活combo<br>
 * b)多文件之间用半角逗号(,)分开<br>
 * c)用一个?来便是时间戳<br>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.5
 */
package com.feilong.taglib.display.httpconcat;