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
 * 条件父类标签,需要条件控制的 例如logic equal请使用这个.
 * <p>
 * 用法:简单条件标签,仅需实现condition()方法即可.
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.servlet.jsp.jstl.core.ConditionalTagSupport
 * @since 1.0.1
 */
public abstract class AbstractConditionalTag extends BaseTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7583054141845571177L;

    /**
     * 标签开始.
     *
     * @return the int
     * @see javax.servlet.jsp.jstl.core.ConditionalTagSupport#doStartTag()
     */
    @Override
    public int doStartTag(){
        return condition() ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    //---------------------------------------------------------------

    /**
     * 自定义条件.
     *
     * @return 条件满足,返回true,页面会EVAL_BODY_INCLUDE,否则SKIP_BODY
     */
    protected abstract boolean condition();
}
