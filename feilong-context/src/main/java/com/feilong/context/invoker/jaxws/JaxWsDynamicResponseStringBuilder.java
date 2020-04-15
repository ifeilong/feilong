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
package com.feilong.context.invoker.jaxws;

import com.feilong.context.beanproperty.JaxWsTypeBeanProperty;
import com.feilong.context.invoker.AbstractResponseStringBuilder;
import com.feilong.context.invoker.RequestArrayParamsBuilder;
import com.feilong.context.invoker.ResponseStringBuilder;
import com.feilong.net.cxf.JaxWsDynamicClientUtil;

/**
 * Http 类型的 {@link ResponseStringBuilder}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 */
public class JaxWsDynamicResponseStringBuilder<T> extends AbstractResponseStringBuilder<T>{

    /** The jax ws type bean property. */
    private JaxWsTypeBeanProperty        jaxWsTypeBeanProperty;

    //---------------------------------------------------------------
    /** 参数构造器. */
    private RequestArrayParamsBuilder<T> requestArrayParamsBuilder;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.invoker.AbstractResponseStringBuilder#handler(java.lang.Object)
     */
    @Override
    protected String handler(T request){
        Object[] params = requestArrayParamsBuilder.build(request);
        return JaxWsDynamicClientUtil.call(jaxWsTypeBeanProperty.getWsdlUrl(), jaxWsTypeBeanProperty.getOperationName(), params);
    }

    //---------------------------------------------------------------

    /**
     * Sets the jax ws type bean property.
     *
     * @param jaxWsTypeBeanProperty
     *            the jaxWsTypeBeanProperty to set
     */
    public void setJaxWsTypeBeanProperty(JaxWsTypeBeanProperty jaxWsTypeBeanProperty){
        this.jaxWsTypeBeanProperty = jaxWsTypeBeanProperty;
    }

    /**
     * 设置 参数构造器.
     *
     * @param requestArrayParamsBuilder
     *            the requestArrayParamsBuilder to set
     */
    public void setRequestArrayParamsBuilder(RequestArrayParamsBuilder<T> requestArrayParamsBuilder){
        this.requestArrayParamsBuilder = requestArrayParamsBuilder;
    }
}
