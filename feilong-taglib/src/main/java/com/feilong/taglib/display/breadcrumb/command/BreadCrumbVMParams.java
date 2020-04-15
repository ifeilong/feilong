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
 * 面包屑VM参数.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.2.2
 */
public class BreadCrumbVMParams<T> implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long         serialVersionUID = 2706654282393984018L;

    /** The bread crumb entity list. */
    private List<BreadCrumbEntity<T>> breadCrumbEntityList;

    /** The connector. */
    private String                    connector;

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

}
