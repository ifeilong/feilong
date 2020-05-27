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

/**
 * 简单的JaxWs 请求类型的bean属性.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.3
 */
public class SimpleJaxWsTypeBeanProperty implements JaxWsTypeBeanProperty{

    /** The wsdl url. */
    private String wsdlUrl;

    /** The operation name. */
    private String operationName;

    //---------------------------------------------------------------

    /**
     * Instantiates a new simple jax ws type bean property.
     * 
     * @since 1.13.2
     */
    public SimpleJaxWsTypeBeanProperty(){
        super();
    }

    /**
     * Instantiates a new simple jax ws type bean property.
     *
     * @param wsdlUrl
     *            the wsdl url
     * @param operationName
     *            the operation name
     * @since 1.13.2
     */
    public SimpleJaxWsTypeBeanProperty(String wsdlUrl, String operationName){
        super();
        this.wsdlUrl = wsdlUrl;
        this.operationName = operationName;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.JaxWsTypeBeanProperty#getWsdlUrl()
     */
    @Override
    public String getWsdlUrl(){
        return wsdlUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.JaxWsTypeBeanProperty#setWsdlUrl(java.lang.String)
     */
    @Override
    public void setWsdlUrl(String wsdlUrl){
        this.wsdlUrl = wsdlUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.JaxWsTypeBeanProperty#getOperationName()
     */
    @Override
    public String getOperationName(){
        return operationName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.beanproperty.JaxWsTypeBeanProperty#setOperationName(java.lang.String)
     */
    @Override
    public void setOperationName(String operationName){
        this.operationName = operationName;
    }

}
