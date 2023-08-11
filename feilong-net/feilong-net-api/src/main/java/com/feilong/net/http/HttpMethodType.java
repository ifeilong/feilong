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
package com.feilong.net.http;

import com.feilong.core.lang.EnumUtil;

/**
 * http请求方法.
 * 
 * <h3>get {@code &&} post区别:</h3>
 * 
 * <blockquote>
 * 
 * <h4>本质区别:</h4>
 * 
 * <p>
 * GET的语义是请求获取指定的资源。<br>
 * GET方法是安全、幂等、可缓存的（除非有 Cache-Control Header的约束）,GET方法的报文主体没有任何语义。
 * </p>
 * 
 * <p>
 * POST的语义是根据请求负荷（报文主体）对指定的资源做出处理，具体的处理方式视资源类型而不同。<br>
 * POST不安全，不幂等，（大部分实现）不可缓存。
 * </p>
 * 
 * <p>
 * 举一个通俗栗子吧，在微博这个场景里，GET的语义会被用在「看看我的Timeline上最新的20条微博」这样的场景，而POST的语义会被用在「发微博、评论、点赞」这样的场景中。
 * </p>
 * 
 * <h4>表现形式:</h4>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">get</th>
 * <th align="left">post</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>后退按钮/刷新</td>
 * <td>无害</td>
 * <td>数据会被重新提交（浏览器应该告知用户数据会被重新提交)</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>行为</td>
 * <td>Get是用来从服务器上获得数据，</td>
 * <td>而Post是用来向服务器上传递数据</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>书签</td>
 * <td>可收藏为书签</td>
 * <td>不可收藏为书签</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>缓存</td>
 * <td>能被缓存</td>
 * <td>不能缓存</td>
 * </tr>
 * 
 * <tr valign="top" >
 * <td>编码类型</td>
 * <td>application/x-www-form-urlencoded</td>
 * <td>application/x-www-form-urlencoded 或multipart/form-data。为二进制数据使用多重编码</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>历史</td>
 * <td>参数保留在浏览器历史中</td>
 * <td>参数不会保存在浏览器历史中</td>
 * </tr>
 * 
 * <tr valign="top" >
 * <td>对数据长度的限制</td>
 * <td>当发送数据时，<s>GET URL 的长度是受限制的（URL 的最大长度是 2048个字符)</s><br>
 * <span style="color:red">HTTP协议并没有限制URI的长度，具体的长度是由浏览器和系统来约束的</span></td>
 * <td>无限制</td>
 * </tr>
 * <tr valign="top"style="background-color:#eeeeff">
 * <td>对数据类型的限制</td>
 * <td>只允许 ASCII 字符</td>
 * <td>没有限制。也允许二进制数据</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>可见性</td>
 * <td>GET的数据在 URL 中对所有人都是可见的</td>
 * <td>POST的数据不会显示在 URL 中</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>安全性</td>
 * <td>与 POST 相比，GET 的安全性较差，所发送的数据是 URL 的一部分。<br>
 * 在发送密码或其他敏感信息时绝不要使用 GET ！<br>
 * 
 * <p>
 * 因为在传输过程，如今现有的很多服务器、代理服务器或者用户代理都会将请求URL记录到日志文件中，然后放在某个地方，<br>
 * 这样就可能会有一些隐私的信息被第三方看到。
 * </p>
 * 
 * <p>
 * 另外，用户也可以在浏览器上直接看到提交的数据，一些系统内部消息将会一同显示在用户面前。
 * </p>
 * Post的所有操作对用户来说都是不可见的。</td>
 * <td>POST 比 GET更安全，因为参数不会被保存在浏览器历史或 web 服务器日志中</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see "org.springframework.http.HttpMethod"
 * @see <a href="http://www.w3school.com.cn/tags/html_ref_httpmethods.asp">HTTP 方法：GET 对比 POST</a>
 * @since 1.7.3 move from feilong-core
 */
public enum HttpMethodType{

    /**
     * get方式,求获取Request-URI所标识的资源.
     * 
     * @since http0.9
     */
    GET("get"),

    /**
     * post方式,向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。
     * POST请求可能会导致新的资源的建立和/或已有资源的修改.
     * 
     * @since http1.0
     */
    POST("post"),

    //---------------------------------------------------------------

    /**
     * put方式,向指定资源位置上传其最新内容（全部更新，操作幂等）.
     * 
     * <p>
     * The PUT method requests that the enclosed entity be stored under the supplied Request-URI. <br>
     * If the Request-URI refers to an already existing resource, the enclosed entity SHOULD be considered as a modified version of the one
     * residing on the origin server.
     * </p>
     * 
     * <p>
     * PUT方法请求服务器去把请求里的实体存储在请求URI（Request-URI）标识下。
     * </p>
     * 
     * <ul>
     * <li>如果请求URI（Request-URI）指定的的资源已经在源服务器上存在，那么此请求里的实体应该被当作是源服务器此URI所指定资源实体的修改版本</li>
     * <li>如果请求URI（Request-URI）指定的资源不存在，并且此URI被用户代理（user agent，译注：用户代理可认为是客户浏览器）定义为一个新资源，那么源服务器就应该根据请求里的实体创建一个此URI所标识下的资源。</li>
     * <li>如果一个新的资源被创建了，源服务器必须能向用户代理（user agent）发送201（已创建）响应。</li>
     * <li>如果已存在的资源被改变了，那么源服务器应该发送200（Ok）或者204（无内容）响应。</li>
     * <li>如果资源不能根据请求URI创建或者改变，一个合适的错误响应应该给出以反应问题的性质</li>
     * <li>实体的接收者不能忽略任何它不理解的Content-*（如：Content-Range）头域，并且必须返回501（没有被实现）响应</li>
     * </ul>
     * 
     * <p>
     * 如果请求穿过一个缓存（cache），并且此请求URI（Request-URI）指示了一个或多个当前缓存的实体，那么这些实体应该被看作是旧的。<span style="color:red">PUT方法的响应不应该被缓存</span>。
     * </p>
     * 
     * <h3>POST方法和PUT方法请求最根本的区别:</h3>
     * 
     * <blockquote>
     * <p>
     * POST方法和PUT方法请求最根本的区别:<span style="color:green">请求URI（Request-URI）的含义不同</span>。
     * </p>
     * 
     * <p>
     * POST请求里的URI指示一个能处理请求实体的资源（译注：此资源可能是一段程序，如jsp里的servlet）<br>
     * 此资源可能是一个数据接收过程，一个网关（gateway，译注：网关和代理服务器的区别是：网关可以进行协议转换，而代理服务器不能，只是起代理的作用，比如缓存服务器其实就是一个代理服务器），或者一个单独接收注释的实体。<br>
     * 而PUT方法请求里有一个实体一一用户代理知道URI意指什么，并且服务器不能把此请求应用于其他URI指定的资源。<br>
     * 如果服务器期望请求被应用于一个不同的URI，那么它必须发送301（永久移动了）响应；用户代理可以自己决定是否重定向请求。
     * 一个独立的资源可能会被许多不同的URI指定。<br>
     * 
     * 如：一篇文章可能会有一个URI指定当前版本，此URI区别于其文章其他特殊版本的URI。<br>
     * 这种情况下，一个通用URI的PUT请求可能会导致其资源的其他URI被源服务器定义。
     * 
     * </p>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>HTTP/1.1没有定义PUT方法对源服务器的状态影响</li>
     * <li>PUT请求必须遵循8.2节中的消息传输要求</li>
     * <li>除非特别指出，PUT方法请求里的实体头域应该被用于资源的创建或修改</li>
     * </ol>
     * </blockquote>
     * 
     * @since 1.12.5
     * @since http1.1
     */
    PUT("put"),

    /**
     * PATCH方式,PATCH方法用于资源的部分内容的更新.
     * 
     * <p>
     * 而PUT方法已经被定义为用一个请求体去修改一个完整的资源。并且不能重复做部分更改，否则代理和缓存、甚至服务器或者客户端都会得到有问题的操作结果。
     * 至此，PATCH方法有了被完全定义的必要。
     * PATCH在请求中定义了一个描述修改的实体集合，如果被请求修改的资源不存在，服务器可能会创建一个新的资源。
     * </p>
     * 
     * <p>
     * The PATCH method requests that a set of changes described in the request entity be applied to the resource identified by the Request-
     * URI. The set of changes is represented in a format called a “patch document” identified by a media type. If the Request-URI does not
     * point to an existing resource, the server MAY create a new resource, depending on the patch document type (whether it can logically
     * modify a null resource) and permissions, etc.The difference between the PUT and PATCH requests is reflected in the way the server
     * processes the enclosed entity to modify the resource identified by the Request-URI. In a PUT request, the enclosed entity is
     * considered to be a modified version of the resource stored on the origin server, and the client is requesting that the stored version
     * be replaced. With PATCH, however, the enclosed entity contains a set of instructions describing how a resource currently residing on
     * the origin server should be modified to produce a new version. The PATCH method affects the resource identified by the Request-URI,
     * and it also MAY have side effects on other resources; i.e., new resources may be created, or existing ones modified, by the
     * application of a PATCH.
     * </p>
     * 
     * <p>
     * PUT与PATCH的区别反映在服务器内部修改资源的方式中。
     * The difference between the PUT and PATCH requests is reflected in the way the server processes the enclosed entity to modify the
     * resource
     * identified by the Request-URI. In a PUT request, the enclosed entity is considered to be a modified version of the resource stored on
     * the
     * origin server, and the client is requesting that the stored version be replaced. With PATCH, however, the enclosed entity contains a
     * set
     * of instructions describing how a resource currently residing on the origin server should be modified to produce a new version. The
     * PATCH
     * method affects the resource identified by the Request-URI, and it also MAY have side effects on other resources; i.e., new resources
     * may be created, or existing ones modified, by the application of a PATCH.
     * </p>
     * 
     * <p>
     * 简单来说就是：
     * 
     * PATCH方法用于资源的部分内容的更新；
     * PUT用于更新某个较为完整的资源；
     * </p>
     * 
     * HTTP Patch 的主要目的是对现有资源进行部分更新，而不是完全替换或删除它们。
     * 
     * 与 HTTP PUT 方法不同，HTTP Patch 允许客户端发送仅包含要更新的部分数据的请求，而不需要提供整个资源的内容。这在一些情况下非常有用，例如：
     * 
     * <ul>
     * <li>部分更新： 当你只想更新资源的特定属性或字段时，而不想影响其他属性。</li>
     * <li>避免冲突： 如果多个客户端正在并发更新同一资源，使用部分更新可以减少冲突的可能性，因为不需要完全替换资源。</li>
     * <li>节省带宽： 如果资源很大，只需要更新其中的一小部分，可以减少数据传输量。</li>
     * <li>原子操作： 使用部分更新可以实现原子操作，确保多个属性在一次请求中更新，从而避免不一致状态。</li>
     * </ul>
     * <p>
     * 在 HTTP Patch 请求中，客户端通常发送一个包含要应用于资源的更改的表示形式的请求。服务器会解释这些更改，并尝试将它们应用于资源。
     * 需要注意的是，HTTP Patch 方法的确切行为取决于服务器的实现，因此在使用时需要查阅相关文档或规范来了解服务器如何处理部分更新。
     * </p>
     * 
     * 
     * @since 3.5.1
     * @since http1.1
     */
    PATCH("patch");

    //---------------------------------------------------------------

    /** The method. */
    private String method;

    //---------------------------------------------------------------

    /**
     * Instantiates a new http method type.
     * 
     * @param method
     *            the method
     */
    private HttpMethodType(String method){
        this.method = method;
    }

    /**
     * Gets the method.
     * 
     * @return the method
     */
    public String getMethod(){
        return method;
    }

    //---------------------------------------------------------------

    /**
     * 传入一个字符串的 HTTP method,比如 get,得到 {@link HttpMethodType#GET}.
     *
     * @param methodValue
     *            <span style="color:red">不区分大小写</span>, 比如get,Get,GET都可以,但是需要对应 {@link HttpMethodType}的支持的枚举值
     * @return 如果遍历所有枚举值,找不到枚举值的属性method的值是 methodValue,那么返回null
     * @see EnumUtil#getEnumByPropertyValueIgnoreCase(Class, String, Object)
     * @since 1.0.8
     */
    public static HttpMethodType getByMethodValueIgnoreCase(String methodValue){
        String propertyName = "method";
        return EnumUtil.getEnumByPropertyValueIgnoreCase(HttpMethodType.class, propertyName, methodValue);
    }
}
