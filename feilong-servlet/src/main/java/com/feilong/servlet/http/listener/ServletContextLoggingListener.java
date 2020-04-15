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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.ServletContextUtil;

/**
 * ServletContext 初始化以及销毁的监听器.
 * 
 * 
 * <h3>作用:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 可以在日志文件或者控制台输出如下信息:
 * </p>
 * 
 * <pre class="code">
 *14:39:36 INFO  (ServletContextLoggingListener.java:49) contextInitialized() - servletContext [Initialized],base info:[    {
        "serverInfo": "Apache Tomcat/7.0.47",
        "version": "3.0",
        "contextPath": "",
        "servletContextName": "Feilong Web Test,test css、js、customer tags、and so on"
    }],[attribute] info:[    {
        "base": "",
        "domainCSS": "http://rs.feilong.com:8888",
        "domainImage": "",
        "domainJS": "",
        "domainResource": "http://127.0.0.1:6666",
        "javax.servlet.context.tempdir": "/Users/feilong/workspace/feilong/feilong-test/feilong-web-springmvc-test/target/tomcat/work/Tomcat/localhost/_",
        "javax.websocket.server.ServerContainer": "org.apache.tomcat.websocket.server.WsServerContainer@3dfa5f07",
        "org.springframework.web.context.WebApplicationContext.ROOT": "Root WebApplicationContext: startup date [Sun May 20 14:39:36 CST 2018]; root of context hierarchy",
        "org.springframework.web.context.support.ServletContextScope": "org.springframework.web.context.support.ServletContextScope@74e45b"
    }],[initParameter] info:[    {
        "contextConfigLocation": "classpath*:applicationContext.xml\n\t\t\t\tclasspath*:spring.xml",
        "domainConfigLocation": "classpath:config/domain.properties"
    }]
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>参考配置:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * web.xml:
 * </p>
 * 
 * <pre class="code">
{@code 
    <listener>
        <listener-class>com.feilong.servlet.http.listener.ServletContextLoggingListener</listener-class>
    </listener>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ServletContextLoggingListener implements ServletContextListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletContextLoggingListener.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){
        if (LOGGER.isInfoEnabled()){
            ServletContext servletContext = servletContextEvent.getServletContext();

            LOGGER.info(
                            "servletContext [Initialized],base info:[{}],[attribute] info:[{}],[initParameter] info:[{}]",
                            JsonUtil.format(ServletContextUtil.getServletContextInfoMapForLog(servletContext)),
                            JsonUtil.formatSimpleMap(ServletContextUtil.getAttributeMap(servletContext)),
                            JsonUtil.format(ServletContextUtil.getInitParameterMap(servletContext)));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){
        if (LOGGER.isInfoEnabled()){
            ServletContext servletContext = servletContextEvent.getServletContext();

            LOGGER.info(
                            "servletContext [Destroyed] info:[{}] ",
                            JsonUtil.format(ServletContextUtil.getServletContextInfoMapForLog(servletContext)));
        }
    }
}
