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
package com.feilong.taglib.display.httpconcat;

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.taglib.AbstractEndWriteContentTag;
import com.feilong.taglib.CacheTag;
import com.feilong.taglib.display.httpconcat.builder.DomainRebuilder;
import com.feilong.taglib.display.httpconcat.builder.HttpConcatParamBuilder;
import com.feilong.taglib.display.httpconcat.builder.VersionRebuilder;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * 根据 TENGINE_SUPPORT判断 将参数动态生成tengine插件的形式或者普通js/css的形式.
 * 
 * <p>
 * 你可以访问 wiki 查看更多 <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat">feilongDisplay-concat</a>
 * </p>
 * 
 * <p>
 * 作用:遵循Yahoo!前端优化准则第一条:减少HTTP请求发送次数<br>
 * 这一功能可以组合Javascript 以及 Css文件<br>
 * </p>
 * 
 * <h3>使用方法:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>以两个问号(??)激活combo</li>
 * <li>多文件之间用半角逗号(,)分开</li>
 * <li>用一个?来便是时间戳</li>
 * </ul>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.2
 */
public class HttpConcatTag extends AbstractEndWriteContentTag implements CacheTag{

    /** The Constant log. */
    private static final Logger LOGGER            = LoggerFactory.getLogger(HttpConcatTag.class);

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID  = -3447592871482978718L;

    //---------------------------------------------------------------

    /** 类型,是 css 还是 js. */
    private String              type;

    /** 版本号. */
    private String              version;

    /**
     * 根目录.
     * <p>
     * 如果设置root为'/script' 会拼成http://staging.nikestore.com.cn/script/??jquery/jquery-1.4.2.min.js?2013022801
     * </p>
     */
    private String              root;

    /** 域名,如果没有设置,将自动使用 {@link HttpServletRequest#getContextPath()}. */
    private String              domain;

    /** 是否支持 http concat(如果设置这个参数,本次渲染将会覆盖全局变量). */
    private Boolean             httpConcatSupport = null;

    //---------------------------------------------------------------

    /**
     * 加工后的版本号(not input param).
     * 
     * 
     * 
     * @since 1.11.1
     */
    private String              rebuildVersion;

    /**
     * 加工后的域名(not input param).
     * 
     * @since 1.11.1
     */
    private String              rebuildDomain;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractCommonTag#writeContent()
     */
    @Override
    protected Object buildContent(HttpServletRequest request){
        String bodyContentSrc = bodyContent.getString();

        //-------------------bodyContentSrc validate---------------------------
        if (isNullOrEmpty(bodyContentSrc)){
            LOGGER.warn("bodyContentSrc is null or empty, return empty");
            return EMPTY;
        }

        //---------------------------------------------------------------
        //重新赋值, 便于buildCacheTagKey 使用cache
        //注意 自定义标签是单例的, 不能直接使用domain来重新赋值
        rebuildDomain = DomainRebuilder.rebuild(domain, request);
        rebuildVersion = VersionRebuilder.rebuild(version, this.pageContext);

        HttpConcatParam httpConcatParam = HttpConcatParamBuilder
                        .build(bodyContentSrc, type, rebuildDomain, root, rebuildVersion, httpConcatSupport);
        return HttpConcatUtil.getWriteContent(httpConcatParam);
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.AbstractWriteContentTag#buildCacheTagKey()
     */
    @Override
    public String buildCacheTagKey(){
        //since 2.0.2 fix https://github.com/venusdrogon/feilong-taglib/issues/71
        return StringUtils.join(type, domain, root, version, httpConcatSupport, bodyContent.getString());
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.AbstractWriteContentTag#buildExtraKeyInfoToLog()
     */
    @Override
    protected String buildExtraKeyInfoToLog(){
        return "[type=" + type + "]";
    }

    //---------------------------------------------------------------

    /**
     * Sets the 类型css/js.
     * 
     * @param type
     *            the new 类型css/js
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Sets the 版本号.
     * 
     * @param version
     *            the new 版本号
     */
    public void setVersion(String version){
        this.version = version;
    }

    /**
     * Sets the 根目录.
     * <p>
     * 如果设置root为'/script' 会拼成http://staging.
     * </p>
     * 
     * @param root
     *            the new 根目录<br>
     *            如果设置root为'/script' 会拼成http://staging
     */
    public void setRoot(String root){
        this.root = root;
    }

    /**
     * 域名,如果没有设置,将自动使用 {@link HttpServletRequest#getContextPath()}.
     *
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain){
        this.domain = domain;
    }

    /**
     * 设置 是否支持 http concat(如果设置这个参数,本次渲染将会覆盖全局变量).
     *
     * @param httpConcatSupport
     *            the httpConcatSupport to set
     */
    public void setHttpConcatSupport(Boolean httpConcatSupport){
        this.httpConcatSupport = httpConcatSupport;
    }

}