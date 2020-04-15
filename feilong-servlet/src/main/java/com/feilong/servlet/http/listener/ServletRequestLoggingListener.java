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

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * request的初始化以及销毁的监听器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.springframework.web.context.request.RequestContextListener"
 * @since 1.10.4
 */
public class ServletRequestLoggingListener implements ServletRequestListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletRequestLoggingListener.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestListener#requestInitialized(javax.servlet.ServletRequestEvent)
     */
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent){
        if (LOGGER.isTraceEnabled()){
            ServletRequest servletRequest = servletRequestEvent.getServletRequest();

            LOGGER.trace(
                            "servletRequest request [Initialized] info:[{}] ",
                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog((HttpServletRequest) servletRequest)));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestListener#requestDestroyed(javax.servlet.ServletRequestEvent)
     */
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent){
        if (LOGGER.isTraceEnabled()){
            ServletRequest servletRequest = servletRequestEvent.getServletRequest();
            LOGGER.trace(
                            "servletRequest request [Destroyed] info:[{}] ",
                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog((HttpServletRequest) servletRequest)));
        }
    }
}
