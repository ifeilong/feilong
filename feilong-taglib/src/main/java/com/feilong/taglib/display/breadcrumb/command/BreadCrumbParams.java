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
import java.util.List;

/**
 * 面包屑参数.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.2.2
 */
public class BreadCrumbParams<T> implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long         serialVersionUID = 2706654282393984018L;

    /** The bread crumb entity list. */
    private List<BreadCrumbEntity<T>> breadCrumbEntityList;

    /** The current path. */
    private String                    currentPath;

    /** url前缀, 用来拼接 {@link BreadCrumbEntity#path}. */
    private String                    urlPrefix;

    /** The connector. */
    private String                    connector        = BreadCrumbConstants.DEFAULT_CONNECTOR;

    /** vm的路径. */
    private String                    vmPath           = BreadCrumbConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

    /**
     * 获得 the bread crumb entity list.
     *
     * @return the breadCrumbEntityList
     */
    public List<BreadCrumbEntity<T>> getBreadCrumbEntityList(){
        return breadCrumbEntityList;
    }

    /**
     * 设置 the bread crumb entity list.
     *
     * @param breadCrumbEntityList
     *            the breadCrumbEntityList to set
     */
    public void setBreadCrumbEntityList(List<BreadCrumbEntity<T>> breadCrumbEntityList){
        this.breadCrumbEntityList = breadCrumbEntityList;
    }

    /**
     * 获得 the connector.
     *
     * @return the connector
     */
    public String getConnector(){
        return connector;
    }

    /**
     * 设置 the connector.
     *
     * @param connector
     *            the connector to set
     */
    public void setConnector(String connector){
        this.connector = connector;
    }

    /**
     * 获得 the current path.
     *
     * @return the currentPath
     */
    public String getCurrentPath(){
        return currentPath;
    }

    /**
     * 设置 the current path.
     *
     * @param currentPath
     *            the currentPath to set
     */
    public void setCurrentPath(String currentPath){
        this.currentPath = currentPath;
    }

    /**
     * 获得 vm的路径.
     *
     * @return the vmPath
     */
    public String getVmPath(){
        return vmPath;
    }

    /**
     * 设置 vm的路径.
     *
     * @param vmPath
     *            the vmPath to set
     */
    public void setVmPath(String vmPath){
        this.vmPath = vmPath;
    }

    /**
     * 获得 url前缀, 用来拼接 {@link BreadCrumbEntity#path}.
     *
     * @return the urlPrefix
     */
    public String getUrlPrefix(){
        return urlPrefix;
    }

    /**
     * 设置 url前缀, 用来拼接 {@link BreadCrumbEntity#path}.
     *
     * @param urlPrefix
     *            the urlPrefix to set
     */
    public void setUrlPrefix(String urlPrefix){
        this.urlPrefix = urlPrefix;
    }

}
