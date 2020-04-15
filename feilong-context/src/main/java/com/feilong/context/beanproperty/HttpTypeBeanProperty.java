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
 * http请求类型的bean属性.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.net.HttpMethodType
 * @since 1.11.0
 */
public interface HttpTypeBeanProperty extends BeanProperty{

    /**
     * 获得 表单提交地址.
     *
     * @return the uri
     * @since 1.18.0 change to uri
     */
    String getUri();

    /**
     * 设置 表单提交地址.
     *
     * @param uri
     *            the new uri
     * @since 1.18.0 change to uri
     */
    void setUri(String uri);

    //---------------------------------------------------------------

    /**
     * 获得 method.
     *
     * @return the method
     */
    String getMethod();

    /**
     * 设置 method.
     *
     * @param method
     *            the method to set
     */
    void setMethod(String method);
}
