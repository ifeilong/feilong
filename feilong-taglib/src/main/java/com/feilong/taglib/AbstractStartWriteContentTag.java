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

/**
 * start输出.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.3.0
 */
public abstract class AbstractStartWriteContentTag extends AbstractWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 20290570902030061L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag(){
        try{
            execute();
        }catch (Exception e){// 默认处理异常,让页面正常执行,但是以错误log显示
            logException(e);
        }
        return SKIP_BODY; //跳过开始和结束标签之间的代码.
    }
}