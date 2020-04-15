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

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.Validate;

/**
 * 拿到标签体中间的内容,解析之后,输出内容显示.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.3.0
 */
public abstract class AbstractEndWriteContentTag extends AbstractWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3979342234682529223L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag(){
        //Request the creation of new buffer, a BodyContent on which to evaluate the body of this tag. 
        //Returned from doStartTag when it implements BodyTag. 
        //This is an illegal return value for doStartTag when the class does not implement BodyTag.
        return EVAL_BODY_BUFFERED;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException{
        try{
            //since 1.11.5 添加校验
            Validate.notNull(bodyContent, "EndWriteContentTag bodyContent can't be null!");

            //---------------------------------------------------------------

            execute();
        }catch (Exception e){// 默认处理异常,让页面正常执行,但是以错误log显示
            logException(e);
        }
        return EVAL_PAGE;
    }
}