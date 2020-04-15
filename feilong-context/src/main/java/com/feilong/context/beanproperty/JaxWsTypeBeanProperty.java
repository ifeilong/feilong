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
 * JaxWs 请求类型的bean属性.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.3
 */
public interface JaxWsTypeBeanProperty extends BeanProperty{

    /**
     * Gets the wsdl url.
     *
     * @return the wsdlUrl
     */
    String getWsdlUrl();

    /**
     * Sets the wsdl url.
     *
     * @param wsdlUrl
     *            the wsdlUrl to set
     */
    void setWsdlUrl(String wsdlUrl);

    //---------------------------------------------------------------

    /**
     * Gets the operation name.
     *
     * @return the operationName
     */
    String getOperationName();

    /**
     * Sets the operation name.
     *
     * @param operationName
     *            the operationName to set
     */
    void setOperationName(String operationName);
}
