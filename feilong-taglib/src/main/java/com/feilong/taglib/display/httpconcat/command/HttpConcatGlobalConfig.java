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
package com.feilong.taglib.display.httpconcat.command;

/**
 * http concat 全局配置.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.0
 */
public class HttpConcatGlobalConfig{

    //----------------template-----------------------------------------------

    /** The template css. */
    private String  templateCss;

    /** The template js. */
    private String  templateJs;

    //---------------------------------------------------------------

    /** 是否支持 HTTP_CONCAT (全局参数). */
    private Boolean httpConcatSupport;

    //---------------cache------------------------------------------------

    /**
     * 设置缓存是否开启.
     * 
     * @since 1.12.8 change to boolean from Boolean
     */
    private boolean defaultCacheEnable;

    /**
     * cache size 限制,仅当 {@link #DEFAULT_CACHEENABLE}开启生效, 当cache数达到 {@link #DEFAULT_CACHESIZELIMIT},将不会再缓存结果
     * 经过测试
     * <ul>
     * <li>300000 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>304850 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>400000 size cache占用 内存 :8.36MB(非精准)</li>
     * </ul>
     * 
     * 对于一个正式项目而言,http concat的cache, size极限大小会是 <blockquote><i>页面总数(P)*页面concat标签数(C)*i18N数(I)*版本号(V)</i></blockquote><br>
     * 如果一个项目 页面有1000个,每个页面有5个concat块,一共有5种国际化语言,如果应用重启前支持5次版本更新,那么计算公式会是 <blockquote><i>1000*5*5*5=50000</i></blockquote>
     * <b>注意:此公式中的页面总数是指,VM/JSP的数量,除非参数不同导致VM/JSP渲染的JS也不同,另当别论</b>
     * 
     * @see org.apache.commons.collections4.map.LRUMap
     */
    private int     defaultCacheSizeLimit;

    //----------------version-----------------------------------------------

    /**
     * 
     * httpconcat version 作用域里面变量名字.
     * 
     * <p>
     * 
     * // # 如果没有配置或者是空,那么不会去作用域中查找<br>
     * // httpconcat.version.nameInScope=httpconcatVersion
     * </p>
     * 
     * @since 1.11.1
     */
    private String  versionNameInScope;

    /**
     * version 名字在哪个作用域.
     * 
     * <p>
     * // # 如果没有值那么会依次从系列作用域中去找值 page request session application<br>
     * // httpconcat.version.search.scope=
     * </p>
     * 
     * @since 1.11.1
     */
    private String  versionSearchScope;

    /**
     * version值的加密格式,值可以是 md5 或者sha1 (忽视大小写).
     * 
     * <p>
     * # 如果没有配置, 那么显示原样内容<br>
     * # 如果需要这个功能, 需要依赖 feilong-security jar<br>
     * httpconcat.version.encode=
     * </p>
     * 
     * @since 1.11.1
     */
    private String  versionEncode;

    /**
     * 当version 值是指定value的时候, 每次自动变.
     * 
     * <p>
     * httpconcat.version.autoRefresh.value
     * </p>
     * 
     * @since 1.11.1
     */
    private String  versionAutoRefreshValue;
    //---------------------------------------------------------------

    /**
     * 全局的域名.
     * 
     * @since 1.11.1
     */
    private String  domain;

    //----------------autoPartitionSize-----------------------------------------------

    /**
     * 自动分片大小.
     * 
     * <h3>功能及作用:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 如下的代码
     * </p>
     * 
     * <pre class="code">
    
    {@code 
    
    <feilongDisplay:httpConcat>
        <script type="text/javascript" src="/resources/libs/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="/resources/libs/iscroll/iscroll.min.js"></script>
        <script type="text/javascript" src="/resources/libs/spice/js/jquery.spice.min.js"></script>
    
        <script type="text/javascript" src="/resources/libs/jquery/jquery-ui-1.10.3.custom.js"></script>
        <script type="text/javascript" src="/resources/js/loxia2/jquery.loxiacore-2.js"></script>
    
        <script type="text/javascript" src="/resources/libs/spice/js/placeholder/jquery.placeholder.min.js"></script>
        <script type="text/javascript" src="/resources/libs/spice/js/jquery.spice.tools.min.js"></script>
        <script type="text/javascript" src="/resources/libs/spice/js/kvScroll/jquery.kvScroll.min.js"></script>
        <script type="text/javascript" src="/resources/libs/swiper/js/swiper.min.js"></script>
    
        <script type="text/javascript" src="/resources/libs/spice/js/tinyscrollbar/jquery.tinyscrollbar.min.js"></script>
        <script type="text/javascript" src="/resources/libs/spice/js/dropdown/jquery.dropdown.min.js"></script>
        <script type="text/javascript" src="/resources/libs/spice/js/lazyLoad/jquery.lazyLoad.min.js"></script>
    
    
        <script type="text/javascript" src="/resources/js/common/MathContext.js"></script>
        <script type="text/javascript" src="/resources/js/common/BigDecimal.js"></script>
        <script type="text/javascript" src="/resources/js/common/common.js"></script>
        <script type="text/javascript" src="/resources/js/common.js"></script>
        <script type="text/javascript" src="/resources/js/common/currency.js"></script>
        <script type="text/javascript" src="/resources/js/common/currencyforCoupon.js"></script>
        <script type="text/javascript" src="/resources/js/common/i18nUtil.js"></script>
        <script type="text/javascript" src="/resources/js/common/dateFormat.js"></script>
        <script type="text/javascript" src="/resources/js/common/ajax-extend.js"></script>
        <script type="text/javascript" src="/resources/js/common/ajax-abortOnRetry.js"></script>
        <script type="text/javascript" src="/resources/js/common/header.js"></script>
        <script type="text/javascript" src="/resources/js/scriptBridgenative.js"></script>
        <script type="text/javascript" src="/resources/js/app-bridge.js"></script>
    </feilongDisplay:httpConcat>
    }
     * </pre>
     * 
     * 直接拼接的结果会是 <b>??/resources/libs/jquery/jquery.min.js,/resources/js/common/MathContext.js,xxxxx</b> , concat数量会超过 nginx 默认的值 10个, 此时可以在
     * httpconcat.properties 将 httpconcat.autoPartitionSize=9 设置合理值, 之后页面渲染会自动分片成 多段
     * </blockquote>
     * 
     * @since 1.12.6
     */
    private Integer autoPartitionSize;

    //---------------------------------------------------------------

    /**
     * 获得 the template css.
     *
     * @return the templateCss
     */
    public String getTemplateCss(){
        return templateCss;
    }

    /**
     * 设置 the template css.
     *
     * @param templateCss
     *            the templateCss to set
     */
    public void setTemplateCss(String templateCss){
        this.templateCss = templateCss;
    }

    /**
     * 获得 the template js.
     *
     * @return the templateJs
     */
    public String getTemplateJs(){
        return templateJs;
    }

    /**
     * 设置 the template js.
     *
     * @param templateJs
     *            the templateJs to set
     */
    public void setTemplateJs(String templateJs){
        this.templateJs = templateJs;
    }

    /**
     * 获得 是否支持 HTTP_CONCAT (全局参数).
     *
     * @return the httpConcatSupport
     */
    public Boolean getHttpConcatSupport(){
        return httpConcatSupport;
    }

    /**
     * 设置 是否支持 HTTP_CONCAT (全局参数).
     *
     * @param httpConcatSupport
     *            the httpConcatSupport to set
     */
    public void setHttpConcatSupport(Boolean httpConcatSupport){
        this.httpConcatSupport = httpConcatSupport;
    }

    //---------------------------------------------------------------

    /**
     * 获得 default cache enable.
     *
     * @return the defaultCacheEnable
     * @since 1.12.8 change to boolean from Boolean
     */
    public boolean getDefaultCacheEnable(){
        return defaultCacheEnable;
    }

    /**
     * 设置 default cache enable.
     *
     * @param defaultCacheEnable
     *            the defaultCacheEnable to set
     * @since 1.12.8 change to boolean from Boolean
     */
    public void setDefaultCacheEnable(boolean defaultCacheEnable){
        this.defaultCacheEnable = defaultCacheEnable;
    }

    //---------------------------------------------------------------

    /**
     * cache size 限制,仅当 {@link #defaultCacheEnable}开启生效, 当cache数达到 {@link #defaultCacheEnable},将不会再缓存结果
     * 经过测试
     * <ul>
     * <li>300000 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>304850 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>400000 size cache占用 内存 :8.36MB(非精准)</li>
     * </ul>
     * 
     * 对于一个正式项目而言,http concat的cache, size极限大小会是 <blockquote><i>页面总数(P)*页面concat标签数(C)*i18N数(I)*版本号(V)</i></blockquote><br>
     * 如果一个项目 页面有1000个,每个页面有5个concat块,一共有5种国际化语言,如果应用重启前支持5次版本更新,那么计算公式会是 <blockquote><i>1000*5*5*5=50000</i></blockquote>
     * <b>注意:此公式中的页面总数是指,VM/JSP的数量,除非参数不同导致VM/JSP渲染的JS也不同,另当别论</b>.
     *
     * @return the defaultCacheSizeLimit
     */
    public int getDefaultCacheSizeLimit(){
        return defaultCacheSizeLimit;
    }

    /**
     * cache size 限制,仅当 {@link #defaultCacheEnable}开启生效, 当cache数达到 {@link #defaultCacheEnable},将不会再缓存结果
     * 经过测试
     * <ul>
     * <li>300000 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>304850 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>400000 size cache占用 内存 :8.36MB(非精准)</li>
     * </ul>
     * 
     * 对于一个正式项目而言,http concat的cache, size极限大小会是 <blockquote><i>页面总数(P)*页面concat标签数(C)*i18N数(I)*版本号(V)</i></blockquote><br>
     * 如果一个项目 页面有1000个,每个页面有5个concat块,一共有5种国际化语言,如果应用重启前支持5次版本更新,那么计算公式会是 <blockquote><i>1000*5*5*5=50000</i></blockquote>
     * <b>注意:此公式中的页面总数是指,VM/JSP的数量,除非参数不同导致VM/JSP渲染的JS也不同,另当别论</b>.
     *
     * @param defaultCacheSizeLimit
     *            the defaultCacheSizeLimit to set
     */
    public void setDefaultCacheSizeLimit(int defaultCacheSizeLimit){
        this.defaultCacheSizeLimit = defaultCacheSizeLimit;
    }

    /**
     * 
     * httpconcat version 作用域里面变量名字.
     * 
     * <p style="color:green">
     * # 如果没有配置或者是空,那么不会去作用域中查找<br>
     * httpconcat.version.nameInScope=httpconcatVersion
     * </p>
     * 
     * @return the versionNameInScope
     * @since 1.11.1
     */
    public String getVersionNameInScope(){
        return versionNameInScope;
    }

    /**
     * 
     * httpconcat version 作用域里面变量名字.
     * 
     * <p style="color:green">
     * # 如果没有配置或者是空,那么不会去作用域中查找<br>
     * httpconcat.version.nameInScope=httpconcatVersion
     * </p>
     * 
     * @param versionNameInScope
     *            the versionNameInScope to set
     * @since 1.11.1
     */
    public void setVersionNameInScope(String versionNameInScope){
        this.versionNameInScope = versionNameInScope;
    }

    /**
     * version 名字在哪个作用域.
     * 
     * <p style="color:green">
     * # 如果没有值那么会依次从系列作用域中去找值 page request session application<br>
     * httpconcat.version.search.scope=
     * </p>
     * 
     * @return the versionSearchScope
     * @since 1.11.1
     */
    public String getVersionSearchScope(){
        return versionSearchScope;
    }

    /**
     * version 名字在哪个作用域.
     * 
     * <p style="color:green">
     * # 如果没有值那么会依次从系列作用域中去找值 page request session application<br>
     * httpconcat.version.search.scope=
     * </p>
     * 
     * @param versionSearchScope
     *            the versionSearchScope to set
     * @since 1.11.1
     */
    public void setVersionSearchScope(String versionSearchScope){
        this.versionSearchScope = versionSearchScope;
    }

    /**
     * version值的加密格式,值可以是 md5 或者sha1 (忽视大小写).
     * 
     * <p style="color:green">
     * # 如果没有配置, 那么显示原样内容<br>
     * # 如果需要这个功能, 需要依赖 feilong-security jar<br>
     * httpconcat.version.encode=
     * </p>
     * 
     * @return the versionEncode
     * @since 1.11.1
     */
    public String getVersionEncode(){
        return versionEncode;
    }

    /**
     * version值的加密格式,值可以是 md5 或者sha1 (忽视大小写).
     * 
     * <p style="color:green">
     * # 如果没有配置, 那么显示原样内容<br>
     * # 如果需要这个功能, 需要依赖 feilong-security jar<br>
     * httpconcat.version.encode=
     * </p>
     * 
     * @param versionEncode
     *            the versionEncode to set
     * @since 1.11.1
     */
    public void setVersionEncode(String versionEncode){
        this.versionEncode = versionEncode;
    }

    /**
     * 获得 全局的域名.
     *
     * @return the domain
     * @since 1.11.1
     */
    public String getDomain(){
        return domain;
    }

    /**
     * 设置 全局的域名.
     *
     * @param domain
     *            the domain to set
     * @since 1.11.1
     */
    public void setDomain(String domain){
        this.domain = domain;
    }

    /**
     * 当version 值是指定value的时候, 每次自动变.
     * 
     * <p>
     * httpconcat.version.autoRefresh.value
     * </p>
     *
     * @return the versionAutoRefreshValue
     * @since 1.11.1
     */
    public String getVersionAutoRefreshValue(){
        return versionAutoRefreshValue;
    }

    /**
     * 当version 值是指定value的时候, 每次自动变.
     * 
     * <p>
     * httpconcat.version.autoRefresh.value
     * </p>
     *
     * @param versionAutoRefreshValue
     *            the versionAutoRefreshValue to set
     * @since 1.11.1
     */
    public void setVersionAutoRefreshValue(String versionAutoRefreshValue){
        this.versionAutoRefreshValue = versionAutoRefreshValue;
    }

    /**
     * 自动分片大小.
     * 
     * <h3>功能及作用:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 如下的代码
     * </p>
     * 
     * <pre class="code">
     *     
     *     {@code 
     *     
     *     <feilongDisplay:httpConcat>
     *         <script type="text/javascript" src="/resources/libs/jquery/jquery.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/iscroll/iscroll.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/jquery.spice.min.js"></script>
     *     
     *         <script type="text/javascript" src="/resources/libs/jquery/jquery-ui-1.10.3.custom.js"></script>
     *         <script type="text/javascript" src="/resources/js/loxia2/jquery.loxiacore-2.js"></script>
     *     
     *         <script type="text/javascript" src="/resources/libs/spice/js/placeholder/jquery.placeholder.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/jquery.spice.tools.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/kvScroll/jquery.kvScroll.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/swiper/js/swiper.min.js"></script>
     *     
     *         <script type="text/javascript" src="/resources/libs/spice/js/tinyscrollbar/jquery.tinyscrollbar.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/dropdown/jquery.dropdown.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/lazyLoad/jquery.lazyLoad.min.js"></script>
     *     
     *     
     *         <script type="text/javascript" src="/resources/js/common/MathContext.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/BigDecimal.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/common.js"></script>
     *         <script type="text/javascript" src="/resources/js/common.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/currency.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/currencyforCoupon.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/i18nUtil.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/dateFormat.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/ajax-extend.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/ajax-abortOnRetry.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/header.js"></script>
     *         <script type="text/javascript" src="/resources/js/scriptBridgenative.js"></script>
     *         <script type="text/javascript" src="/resources/js/app-bridge.js"></script>
     *     </feilongDisplay:httpConcat>
     *     }
     * </pre>
     * 
     * 直接拼接的结果会是 <b>??/resources/libs/jquery/jquery.min.js,/resources/js/common/MathContext.js,xxxxx</b> , concat数量会超过 nginx 默认的值 10个, 此时可以在
     * httpconcat.properties 将 httpconcat.autoPartitionSize=9 设置合理值, 之后页面渲染会自动分片成 多段
     * </blockquote>
     *
     * @return the 自动分片大小
     * @since 1.12.6
     */
    public Integer getAutoPartitionSize(){
        return autoPartitionSize;
    }

    /**
     * 自动分片大小.
     * 
     * <h3>功能及作用:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 如下的代码
     * </p>
     * 
     * <pre class="code">
     *     
     *     {@code 
     *     
     *     <feilongDisplay:httpConcat>
     *         <script type="text/javascript" src="/resources/libs/jquery/jquery.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/iscroll/iscroll.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/jquery.spice.min.js"></script>
     *     
     *         <script type="text/javascript" src="/resources/libs/jquery/jquery-ui-1.10.3.custom.js"></script>
     *         <script type="text/javascript" src="/resources/js/loxia2/jquery.loxiacore-2.js"></script>
     *     
     *         <script type="text/javascript" src="/resources/libs/spice/js/placeholder/jquery.placeholder.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/jquery.spice.tools.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/kvScroll/jquery.kvScroll.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/swiper/js/swiper.min.js"></script>
     *     
     *         <script type="text/javascript" src="/resources/libs/spice/js/tinyscrollbar/jquery.tinyscrollbar.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/dropdown/jquery.dropdown.min.js"></script>
     *         <script type="text/javascript" src="/resources/libs/spice/js/lazyLoad/jquery.lazyLoad.min.js"></script>
     *     
     *     
     *         <script type="text/javascript" src="/resources/js/common/MathContext.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/BigDecimal.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/common.js"></script>
     *         <script type="text/javascript" src="/resources/js/common.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/currency.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/currencyforCoupon.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/i18nUtil.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/dateFormat.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/ajax-extend.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/ajax-abortOnRetry.js"></script>
     *         <script type="text/javascript" src="/resources/js/common/header.js"></script>
     *         <script type="text/javascript" src="/resources/js/scriptBridgenative.js"></script>
     *         <script type="text/javascript" src="/resources/js/app-bridge.js"></script>
     *     </feilongDisplay:httpConcat>
     *     }
     * </pre>
     * 
     * 直接拼接的结果会是 <b>??/resources/libs/jquery/jquery.min.js,/resources/js/common/MathContext.js,xxxxx</b> , concat数量会超过 nginx 默认的值 10个, 此时可以在
     * httpconcat.properties 将 httpconcat.autoPartitionSize=9 设置合理值, 之后页面渲染会自动分片成 多段
     * </blockquote>
     *
     * @param autoPartitionSize
     *            the new 自动分片大小
     * @since 1.12.6
     */
    public void setAutoPartitionSize(Integer autoPartitionSize){
        this.autoPartitionSize = autoPartitionSize;
    }
}
