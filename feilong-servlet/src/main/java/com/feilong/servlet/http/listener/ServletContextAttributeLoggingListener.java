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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.ServletContextUtil;

/**
 * ServletContext Attribute的创建/替换,删除的监听器.
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
 *
14:39:39 INFO  (ServletContextAttributeLoggingListener.java:62) attributeAdded() - name:[org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc],value:[WebApplicationContext for namespace 'springmvc-servlet': startup date [Sun May 20 14:39:36 CST 2018]; parent: Root WebApplicationContext] added to [servletContext],now servletContext attribute:[    {
        "base": "",
        "domainCSS": "http://rs.feilong.com:8888",
        "domainImage": "",
        "domainJS": "",
        "domainResource": "http://127.0.0.1:6666",
        "javax.servlet.context.tempdir": "/Users/feilong/workspace/feilong/feilong-test/feilong-web-springmvc-test/target/tomcat/work/Tomcat/localhost/_",
        "javax.websocket.server.ServerContainer": "org.apache.tomcat.websocket.server.WsServerContainer@3dfa5f07",
        "org.apache.tiles.CONTAINER": "org.apache.tiles.impl.BasicTilesContainer@701e0b2e",
        "org.apache.tiles.request.ApplicationContext.ATTRIBUTE": "org.springframework.web.servlet.view.tiles3.SpringWildcardServletTilesApplicationContext@3a9c60b5",
        "org.springframework.web.context.WebApplicationContext.ROOT": "Root WebApplicationContext: startup date [Sun May 20 14:39:36 CST 2018]; root of context hierarchy",
        "org.springframework.web.context.support.ServletContextScope": "org.springframework.web.context.support.ServletContextScope@5f08994b",
        "org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc": "WebApplicationContext for namespace 'springmvc-servlet': startup date [Sun May 20 14:39:36 CST 2018]; parent: Root WebApplicationContext"
    }]
 * 
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
        <listener-class>com.feilong.servlet.http.listener.ServletContextAttributeLoggingListener</listener-class>
    </listener>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ServletContextAttributeLoggingListener implements ServletContextAttributeListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletContextAttributeLoggingListener.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextAttributeListener#attributeAdded(javax.servlet.ServletContextAttributeEvent)
     */
    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent){
        if (LOGGER.isInfoEnabled()){
            String name = servletContextAttributeEvent.getName();

            if (isNotNullOrEmpty(name) && ArrayUtils.contains(ServletContextUtil.EXCLUDE_KEYS, name)){
                return;
            }

            //---------------------------------------------------------------

            LOGGER.info(
                            "name:[{}],value:[{}] added to [servletContext],now servletContext attribute:[{}] ",
                            name,
                            servletContextAttributeEvent.getValue(),
                            buildAttributesLogMessage(servletContextAttributeEvent.getServletContext()));
        }

    }

    //---------------------------------------------------------------

    /**
     * Builds the attributes log message.
     *
     * @param servletContext
     *            the servlet context
     * @return the string
     */
    private static String buildAttributesLogMessage(ServletContext servletContext){
        Map<String, Object> attributeMap = ServletContextUtil.getAttributeMap(servletContext);
        return JsonUtil.formatSimpleMap(attributeMap);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextAttributeListener#attributeRemoved(javax.servlet.ServletContextAttributeEvent)
     */
    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            "name:[{}],value:[{}] removed from [servletContext],now servletContext attribute:[{}] ",
                            servletContextAttributeEvent.getName(),
                            servletContextAttributeEvent.getValue(),
                            buildAttributesLogMessage(servletContextAttributeEvent.getServletContext()));

        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextAttributeListener#attributeReplaced(javax.servlet.ServletContextAttributeEvent)
     */
    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            "name:[{}],value:[{}] replaced to [servletContext],now servletContext attribute:[{}] ",
                            servletContextAttributeEvent.getName(),
                            servletContextAttributeEvent.getValue(),
                            buildAttributesLogMessage(servletContextAttributeEvent.getServletContext()));
        }
    }
}
