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

import static com.feilong.core.date.DateUtil.formatElapsedTime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 只需要实现 {@link ServletContextListener#contextDestroyed} 方法的 {@link ServletContextListener}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.14.3
 */
@lombok.extern.slf4j.Slf4j
public abstract class AbstractServletContextDestroyedListener implements ServletContextListener{

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){

    }

    //---------------------------------------------------------------

    /**
     * Receives notification that the ServletContext is about to be shut down.
     *
     * <p>
     * All servlets and filters will have been destroyed before any ServletContextListeners are notified of context destruction.
     * </p>
     * 
     * @param servletContextEvent
     *            the servlet context event
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){
        long beginTimeMillis = System.currentTimeMillis();

        destroyed(servletContextEvent);

        if (log.isInfoEnabled()){
            log.info("[{}] destroyed use time: [{}]", getClass().getName(), formatElapsedTime(beginTimeMillis));
        }
    }

    //---------------------------------------------------------------

    /**
     * Initialized.
     *
     * @param servletContextEvent
     *            the ServletContextEvent containing the ServletContext that is being destroyed
     */
    protected abstract void destroyed(ServletContextEvent servletContextEvent);

}
