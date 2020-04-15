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
 * {@link Pager}(包含数据集) {@code &&} 解析的分页html 代码.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.4.0
 */
public class PagerAndContent<T> implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4989412502718346094L;

    /** 分页数据(包含数据集). */
    private Pager<T>          pager;

    /** 渲染出来的分页html代码. */
    private String            content;

    //---------------------------------------------------------------

    /**
     * Instantiates a new pager and content.
     */
    public PagerAndContent(){
        super();
    }

    /**
     * Instantiates a new pager and content.
     *
     * @param pager
     *            分页数据(包含数据集)
     * @param content
     *            渲染出来的分页html代码
     */
    public PagerAndContent(Pager<T> pager, String content){
        super();
        this.pager = pager;
        this.content = content;
    }

    //---------------------------------------------------------------

    /**
     * 获得 分页数据(包含数据集).
     *
     * @return the pager
     */
    public Pager<T> getPager(){
        return pager;
    }

    /**
     * 设置 分页数据(包含数据集).
     *
     * @param pager
     *            the pager to set
     */
    public void setPager(Pager<T> pager){
        this.pager = pager;
    }

    /**
     * 获得 渲染出来的分页html代码.
     *
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * 设置 渲染出来的分页html代码.
     *
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }

}
