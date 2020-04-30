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
package com.feilong.context.beanproperty;

import com.feilong.net.http.HttpMethodType;

/**
 * 简单的 http请求类型的bean属性.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.net.http.HttpMethodType
 * @since 1.11.2
 */
public class SimpleHttpTypeBeanProperty implements HttpTypeBeanProperty{

    /**
     * 表单提交地址.
     * 
     * @since 1.11.4 change to uri
     */
    private String uri;

    /** The method. */
    private String method = HttpMethodType.GET.getMethod();

    //---------------------------------------------------------------

    /**
     * Instantiates a new simple http type bean property.
     * 
     * @since 1.13.2
     */
    public SimpleHttpTypeBeanProperty(){
        super();
    }

    /**
     * Instantiates a new simple http type bean property.
     *
     * @param uri
     *            the uri
     * @param method
     *            the method
     * @since 1.13.2
     */
    public SimpleHttpTypeBeanProperty(String uri, String method){
        super();
        this.uri = uri;
        this.method = method;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.HttpTypeBeanProperty#getUri()
     */
    @Override
    public String getUri(){
        return uri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.HttpTypeBeanProperty#setUri(java.lang.String)
     */
    @Override
    public void setUri(String gateway){
        this.uri = gateway;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.HttpTypeBeanProperty#getMethod()
     */
    @Override
    public String getMethod(){
        return method;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.HttpTypeBeanProperty#setMethod(java.lang.String)
     */
    @Override
    public void setMethod(String method){
        this.method = method;
    }
}
