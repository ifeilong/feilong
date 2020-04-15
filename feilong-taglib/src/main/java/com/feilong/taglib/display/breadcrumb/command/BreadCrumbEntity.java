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
package com.feilong.taglib.display.breadcrumb.command;

import java.io.Serializable;

/**
 * 面包屑所需要的字段 封装.
 * 
 * <p>
 * 其中泛型中的T 是 id 主键的类型,可以是Number String 或者其他类型.
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * 
 * @since 1.2.2
 */
public class BreadCrumbEntity<T> implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2739340747216481761L;

    /** current id, 可以是Number String 或者其他类型.. */
    private T                 id;

    /** name,用于拼接. */
    private String            name;

    /** title. */
    private String            title;

    /** 匹配的路径,可以是相对路径或者绝对路径. */
    private String            path;

    /** parent id ,可以是Number String 或者其他类型.. */
    private T                 parentId;

    /**
     * The Constructor.
     */
    public BreadCrumbEntity(){
        super();
    }

    /**
     * The Constructor.
     *
     * @param id
     *            the id
     * @param name
     *            the name
     * @param title
     *            the title
     * @param path
     *            the path
     * @param parentId
     *            the parent id
     */
    public BreadCrumbEntity(T id, String name, String title, String path, T parentId){
        super();
        this.id = id;
        this.name = name;
        this.title = title;
        this.path = path;
        this.parentId = parentId;
    }

    /**
     * Gets the name,用于拼接.
     * 
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name,用于拼接.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Gets the 匹配的路径.
     * 
     * @return the requestMapping
     */
    public String getPath(){
        return path;
    }

    /**
     * Sets the 匹配的路径.
     * 
     * @param path
     *            the requestMapping to set
     */
    public void setPath(String path){
        this.path = path;
    }

    /**
     * 获得 current id, 可以是Number String 或者其他类型.
     * 
     * @return the id
     */
    public T getId(){
        return id;
    }

    /**
     * 设置 current id, 可以是Number String 或者其他类型.
     * 
     * @param id
     *            the id to set
     */
    public void setId(T id){
        this.id = id;
    }

    /**
     * 设置 parent id ,可以是Number String 或者其他类型.
     * 
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(T parentId){
        this.parentId = parentId;
    }

    /**
     * 获得 parent id ,可以是Number String 或者其他类型.
     * 
     * @return the parentId
     */
    public T getParentId(){
        return parentId;
    }
}
