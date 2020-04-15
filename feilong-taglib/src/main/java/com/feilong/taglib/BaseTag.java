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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 自定义标签的父类,所有自定义标签的基类,包含通用的方法.
 * 
 * <h3>可以使用的方法:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #getHttpServletRequest()}</li>
 * <li>{@link #getHttpServletResponse()}</li>
 * <li>{@link #getHttpSession()}</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>关于Return Code:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link javax.servlet.jsp.tagext.IterationTag#EVAL_BODY_AGAIN EVAL_BODY_AGAIN} 对标签体循环处理</li>
 * <li>{@link javax.servlet.jsp.tagext.Tag#EVAL_PAGE EVAL_PAGE} (6):处理标签后,继续处理JSP后面的内容</li>
 * <li>{@link javax.servlet.jsp.tagext.Tag#EVAL_BODY_INCLUDE EVAL_BODY_INCLUDE}
 * (1)将body的内容输出到存在的输出流中,表示需要处理标签体,但绕过setBodyContent()和doInitBody()方法</li>
 * 
 * <li>{@link javax.servlet.jsp.tagext.BodyTag#EVAL_BODY_BUFFERED EVAL_BODY_BUFFERED}(2) 表示需要处理标签体</li>
 * <li>{@link javax.servlet.jsp.tagext.Tag#SKIP_BODY SKIP_BODY} (0) : 表示不用处理标签体,直接调用doEndTag()方法 跳过了开始和结束标签之间的代码.</li>
 * <li>{@link javax.servlet.jsp.tagext.Tag#SKIP_PAGE SKIP_PAGE}(5): 忽略标签后面的JSP内容</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>{@link TagSupport}与{@link BodyTagSupport}:</h3>
 * 
 * <blockquote>
 * <p>
 * {@link TagSupport}与{@link BodyTagSupport}的区别,主要是标签处理类是否需要与标签体交互, 如果不需要交互的就用{@link TagSupport},否则如果需要交互就用{@link BodyTagSupport}. <br>
 * 交互就是标签处理类是否要读取标签体的内容和改变标签体返回的内容.<br>
 * </p>
 * {@link BodyTagSupport}继承了{@link TagSupport}.
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.servlet.jsp.tagext.BodyTagSupport
 * @see javax.servlet.jsp.tagext.TagSupport
 * 
 * @see javax.servlet.jsp.jstl.core.Config
 * @since 1.0.0
 */
public abstract class BaseTag extends BodyTagSupport implements TryCatchFinally{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = -5494214419937813707L;

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(BaseTag.class);

    //---------------------------------------------------------------

    /**
     * 获得HttpServletRequest.
     *
     * @return the http servlet request
     */
    protected final HttpServletRequest getHttpServletRequest(){
        return (HttpServletRequest) this.pageContext.getRequest();
    }

    /**
     * 获得 HttpSession.
     *
     * @return HttpSession
     */
    protected final HttpSession getHttpSession(){
        return this.pageContext.getSession();
    }

    /**
     * 获得HttpServletResponse.
     *
     * @return the http servlet response
     */
    protected final HttpServletResponse getHttpServletResponse(){
        return (HttpServletResponse) pageContext.getResponse();
    }

    //---------------------------------------------------------------

    /**
     * Log exception.
     *
     * @param e
     *            the e
     * @since 1.5.5
     */
    protected void logException(Exception e){
        if (LOGGER.isErrorEnabled()){
            RequestLogSwitch requestLogSwitch = RequestLogSwitch.NORMAL_WITH_IDENTITY_INCLUDE_FORWARD;
            Map<String, Object> map = RequestUtil.getRequestInfoMapForLog(getHttpServletRequest(), requestLogSwitch);

            //---------------------------------------------------------------
            String pattern = "tag:[{}],exception message:[{}],request info:{},but need render page,pls check!";
            LOGGER.error(Slf4jUtil.format(pattern, getClass().getSimpleName(), e.getMessage(), JsonUtil.format(map)), e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
     */
    @Override
    public void doCatch(Throwable t) throws Throwable{
        LOGGER.error("[" + getClass().getSimpleName() + "]", t);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally()
     */
    @Override
    public void doFinally(){

    }
}