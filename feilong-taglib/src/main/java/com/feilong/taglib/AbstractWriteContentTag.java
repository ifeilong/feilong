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
package com.feilong.taglib;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.core.lang.ClassUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * 输出内容的标签.
 * 
 * <h3>可以使用的方法:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #print(Object)}</li>
 * <li>{@link #println(Object)}</li>
 * </ul>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.taglib.BaseTag
 * @since 1.0.0
 */
//默认修饰符号 限制访问
abstract class AbstractWriteContentTag extends BaseTag{

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(AbstractWriteContentTag.class);

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 8215127553271356734L;

    //---------------------------------------------------------------

    /**
     * Execute.
     */
    protected void execute(){
        Date beginDate = now();

        HttpServletRequest request = getHttpServletRequest();

        //---------------------------------------------------------------

        Object writeContent = buildContentIfUseCache(request);
        if (null != writeContent){
            print(writeContent);
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            String buildExtraKeyInfoToLog = buildExtraKeyInfoToLog();
            String tagLog = isNullOrEmpty(buildExtraKeyInfoToLog) ? "" : "," + buildExtraKeyInfoToLog;
            String useTime = formatDuration(beginDate);
            LOGGER.debug("[{}],[{}]{},use time:[{}]", getClass().getSimpleName(), RequestUtil.getRequestURL(request), tagLog, useTime);
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the content if use cache.
     *
     * @param request
     *            the request
     * @return the object
     * @since 1.10.5
     */
    protected Object buildContentIfUseCache(HttpServletRequest request){
        Class<? extends AbstractWriteContentTag> klass = getClass();

        //---------------------------------------------------------------
        boolean isCacheTag = ClassUtil.isAssignableFrom(CacheTag.class, klass);

        String cacheKey = !isCacheTag ? null : ((CacheTag) this).buildCacheTagKey();

        if (isCacheTag){//如果使用cache ,从cache 里面取
            Object contentFromCache = SimpleTagStringCacheManager.get(cacheKey);
            if (isNotNullOrEmpty(contentFromCache)){
                return contentFromCache;
            }
        }

        //---------------------------------------------------------------
        // 开始执行的部分
        Object writeContent = this.buildContent(request);
        if (isCacheTag && isNotNullOrEmpty(writeContent)){
            SimpleTagStringCacheManager.put(cacheKey, writeContent);
        }
        return writeContent;
    }

    //---------------------------------------------------------------

    /**
     * 标签体内容.
     *
     * @param request
     *            the request
     * @return the object
     */
    protected abstract Object buildContent(HttpServletRequest request);

    /**
     * 额外的关键的信息,用在log里面显示, 比如面包屑 可以放itemId=xxx.
     *
     * @return the string
     */
    protected String buildExtraKeyInfoToLog(){
        return EMPTY;
    }

    //---------------------------------------------------------------

    /**
     * 将文字输出到页面.
     *
     * @param object
     *            the object
     * @since 1.5.3 move from {@link BaseTag}
     */
    protected void print(Object object){
        JspWriter jspWriter = pageContext.getOut();
        try{
            jspWriter.print(object);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 将文字输出到页面.
     *
     * @param object
     *            the object
     * @since 1.5.3 move from {@link BaseTag}
     */
    protected void println(Object object){
        JspWriter jspWriter = pageContext.getOut();
        try{
            jspWriter.println(object.toString());
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}