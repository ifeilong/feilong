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
package com.feilong.taglib.display.pager.command;

import java.io.Serializable;

/**
 * 分页链接模板.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.5
 */
public class PagerUrlTemplate implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 288232184048495608L;

    /** 链接的模板,以便前端js 替换. */
    private String            href;

    /** 模板链接里面的分页号码. */
    private Integer           templateValue    = PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;

    /**
     * 获得 链接的模板,以便前端js 替换.
     * 
     * @return the href
     */
    public String getHref(){
        return href;
    }

    /**
     * 设置 链接的模板,以便前端js 替换.
     * 
     * @param href
     *            the href to set
     */
    public void setHref(String href){
        this.href = href;
    }

    /**
     * 获得 模板链接里面的分页号码.
     * 
     * @return the templateValue
     */
    public Integer getTemplateValue(){
        return templateValue;
    }

    /**
     * 设置 模板链接里面的分页号码.
     * 
     * @param templateValue
     *            the templateValue to set
     */
    public void setTemplateValue(Integer templateValue){
        this.templateValue = templateValue;
    }
}
