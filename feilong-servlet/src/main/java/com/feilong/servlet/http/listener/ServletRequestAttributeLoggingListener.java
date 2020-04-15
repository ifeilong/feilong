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
package com.feilong.servlet.http.listener;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * request 属性的创建, 替换,删除的监听器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ServletRequestAttributeLoggingListener implements ServletRequestAttributeListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletRequestAttributeLoggingListener.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestAttributeListener#attributeAdded(javax.servlet.ServletRequestAttributeEvent)
     */
    @Override
    public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent){
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace(
                            "name:[{}],value:[{}] added to [servletRequest]",
                            servletRequestAttributeEvent.getName(),
                            servletRequestAttributeEvent.getValue());
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestAttributeListener#attributeRemoved(javax.servlet.ServletRequestAttributeEvent)
     */
    @Override
    public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "name:[{}],value:[{}] removed from [servletRequest]",
                            servletRequestAttributeEvent.getName(),
                            servletRequestAttributeEvent.getValue());
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestAttributeListener#attributeReplaced(javax.servlet.ServletRequestAttributeEvent)
     */
    @Override
    public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent){
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace(
                            "name:[{}],value:[{}] replaced to [servletRequest]",
                            servletRequestAttributeEvent.getName(),
                            servletRequestAttributeEvent.getValue());
        }
    }
}
