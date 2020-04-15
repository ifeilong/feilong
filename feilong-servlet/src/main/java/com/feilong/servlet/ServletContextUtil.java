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
package com.feilong.servlet;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.ArrayUtils;

/**
 * {@link javax.servlet.ServletContext} 工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.2
 */
public final class ServletContextUtil{

    /** The Constant EXCLUDE_KEYS. */
    public static final String[] EXCLUDE_KEYS = {
                                                  "org.apache.catalina.jsp_classpath",
                                                  "org.apache.catalina.resources",
                                                  "org.apache.jasper.compiler.ELInterpreter",
                                                  "org.apache.jasper.compiler.TldLocationsCache",
                                                  "org.apache.jasper.runtime.JspApplicationContextImpl",
                                                  "org.apache.tomcat.InstanceManager",
                                                  "org.apache.tomcat.JarScanner",
                                                  "org.apache.tomcat.util.scan.MergedWebXml" };

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ServletContextUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
    //---------------------------------------------------------------

    /**
     * servletContext.log servletContext相关信息,一般启动时调用.
     *
     * @param servletContext
     *            the servlet context
     * @return the servlet context info map for log
     */
    public static Map<String, Object> getServletContextInfoMapForLog(ServletContext servletContext){
        Map<String, Object> map = newLinkedHashMap();
        // 返回servlet运行的servlet 容器的版本和名称.
        map.put("serverInfo", servletContext.getServerInfo());

        // 返回这个servlet容器支持的Java Servlet API的主要版本.所有符合2.5版本的实现,必须有这个方法返回的整数2.
        // 返回这个servlet容器支持的Servlet API的次要版本.所有符合2.5版本的实现,必须有这个方法返回整数5.
        map.put("version", servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());

        map.put("contextPath", servletContext.getContextPath());

        map.put("servletContextName", servletContext.getServletContextName());
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 遍历显示servletContext的 {@link javax.servlet.ServletContext#getAttributeNames() ServletContext.getAttributeNames()},将 name/attributeValue
     * 存入到map.
     * 
     * @param servletContext
     *            the servlet context
     * @return 如果{@link javax.servlet.ServletContext#getAttributeNames() ServletContext.getAttributeNames()} 是null或者empty,返回
     *         {@link Collections#emptyMap()}<br>
     */
    public static Map<String, Object> getAttributeMap(ServletContext servletContext){
        return getAttributeMap(servletContext, true);
    }

    /**
     * 遍历显示servletContext的 {@link javax.servlet.ServletContext#getAttributeNames() ServletContext.getAttributeNames()},将 name/attributeValue
     * 存入到map.
     *
     * @param servletContext
     *            the servlet context
     * @param ignoreContainerAttribute
     *            忽略容器自带的属性, 如果是true 那么将忽略
     * @return 如果{@link javax.servlet.ServletContext#getAttributeNames() ServletContext.getAttributeNames()} 是null或者empty,返回
     *         {@link Collections#emptyMap()}<br>
     * @since 1.10.4
     */
    public static Map<String, Object> getAttributeMap(ServletContext servletContext,boolean ignoreContainerAttribute){
        Enumeration<String> attributeNames = servletContext.getAttributeNames();
        if (isNullOrEmpty(attributeNames)){
            return emptyMap();
        }

        //---------------------------------------------------------------
        Map<String, Object> map = new TreeMap<>();
        while (attributeNames.hasMoreElements()){
            String name = attributeNames.nextElement();
            if (ignoreContainerAttribute && ArrayUtils.contains(EXCLUDE_KEYS, name)){
                continue;
            }
            map.put(name, servletContext.getAttribute(name));
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 遍历显示servletContext的 {@link ServletContext#getInitParameterNames()},将 name /attributeValue 存入到map返回.
     * 
     * @param servletContext
     *            the servlet context
     * @return 如果 {@link javax.servlet.ServletContext#getInitParameterNames() ServletContext.getInitParameterNames()} 是null或者empty,返回
     *         {@link Collections#emptyMap()}<br>
     * @see javax.servlet.ServletContext#getInitParameterNames()
     * @see "org.springframework.web.context.support#registerEnvironmentBeans(ConfigurableListableBeanFactory, ServletContext)"
     */
    public static Map<String, String> getInitParameterMap(ServletContext servletContext){
        Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
        if (isNullOrEmpty(initParameterNames)){
            return emptyMap();
        }

        //---------------------------------------------------------------

        Map<String, String> map = new TreeMap<>();
        while (initParameterNames.hasMoreElements()){
            String name = initParameterNames.nextElement();
            map.put(name, servletContext.getInitParameter(name));
        }
        return map;
    }
}
