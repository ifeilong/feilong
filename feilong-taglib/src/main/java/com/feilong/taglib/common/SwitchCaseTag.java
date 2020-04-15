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
package com.feilong.taglib.common;

import javax.servlet.jsp.tagext.Tag;

import com.feilong.taglib.BaseTag;

/**
 * case标签 配合switch标签使用.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.1
 */
public class SwitchCaseTag extends BaseTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3112940241411917867L;

    /** 值. */
    private String            value;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag(){
        Tag tag = getParent();
        SwitchTag switchTag = (SwitchTag) tag;
        // 判断是否可以执行本身.
        if (!switchTag.isExecuteTag()){
            // 如果当前的value=switch的value,
            // 通知父类.表示已经有了一个符合条件的. 否则,忽略标签体.
            String parentValue = switchTag.getValue();
            if (this.value.equals(parentValue)){
                switchTag.setExecuteTag();
                return EVAL_BODY_INCLUDE;
            }
        }
        return SKIP_BODY;
    }

    //---------------------------------------------------------------

    /**
     * Sets the 值.
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value){
        this.value = value;
    }
}
