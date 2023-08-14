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
package com.feilong.spring.web.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.feilong.core.Validate;

/**
 * {@link WebApplicationContextUtils} 工具类.
 * 
 * <p>
 * 当 Web应用集成 Spring容器后,代表 Spring 容器的 {@link WebApplicationContext} 对象将以{@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE}
 * 为键存放在 {@link ServletContext} 属性列表中,具体参见 {@link org.springframework.web.context.ContextLoader#initWebApplicationContext(ServletContext)}
 * </p>
 * 
 * <h3>{@link WebApplicationContextUtils#getWebApplicationContext(ServletContext)}VS
 * {@link RequestContextUtils#getWebApplicationContext(javax.servlet.ServletRequest)}:</h3>
 * <blockquote>
 * 
 * <p style="color:red">
 * 注意: {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE} 存放的是spring ApplicationContext而非 springmvc ApplicationContext
 * </p>
 * <p>
 * {@link RequestContextUtils#getWebApplicationContext(javax.servlet.ServletRequest)}可以取到 springmvc ApplicationContext,他的原理是,每次
 * 
 * {@link DispatcherServlet#doService(HttpServletRequest, HttpServletResponse)} 都会往request里面设置 key为
 * {@link DispatcherServlet#WEB_APPLICATION_CONTEXT_ATTRIBUTE} 的属性,而此时的值 WebApplicationContext是通过
 * {@link FrameworkServlet#initWebApplicationContext()} 初始化的; 具体参见 {@link FrameworkServlet#createWebApplicationContext(ApplicationContext)}
 * ,可以明显看出spring ApplicationContext是 springmvc ApplicationContext 的parent
 * </p>
 * </blockquote>
 * 
 * 
 * <h3>{@link WebApplicationContextUtils#getWebApplicationContext(ServletContext) getWebApplicationContext}VS
 * {@link WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext) getRequiredWebApplicationContext}:</h3>
 * <blockquote>
 * <p>
 * 当 ServletContext 属性列表中不存在 WebApplicationContext时:
 * <ol>
 * <li>{@link WebApplicationContextUtils#getWebApplicationContext(ServletContext)}方法不会抛出异常,它简单地返回 null, 如果后续代码直接访问返回的结果将引发一个
 * NullPointerException 异常.</li>
 * <li>而{@link WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext)}方法要求 ServletContext属性列表中一定要包含一个有效的
 * WebApplicationContext对象,否则马上抛出一个 异常 {@link java.lang.IllegalStateException}.</li>
 * </ol>
 * 我们推荐使用后者,因为它能提前发现错误的时间,强制开发者搭建好必备的基础设施.
 * </p>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
 * @see org.springframework.web.context.support.WebApplicationContextUtils#getWebApplicationContext(ServletContext)
 * @since 3.0.2 move from feilong-spring project
 */
public final class WebSpringUtil{

    /** Don't let anyone instantiate this class. */
    private WebSpringUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 request.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <p>
     * spring 3中可以通过 {@link RequestContextHolder} 得到 {@link HttpServletRequest},但是得不到 {@link HttpServletResponse},具体参见
     * {@link <a href="https://github.com/venusdrogon/feilong-spring/issues/6">WebSpringUtil.getResponse()方法获取到的response是null</a>}
     * </p>
     * </blockquote>
     * 
     * @return the request
     * @see <a href="http://www.cnblogs.com/softidea/p/6125087.html">Spring MVC的RequestContextHolder使用误区</a>
     * @see <a href="http://www.cnblogs.com/mikevictor07/p/3436393.html">springMVC 中几种获取request和response的方式</a>
     * @see <a href="http://www.programering.com/q/MDO3QjMwATY.html">How to obtain the HttpServletResponse Spring AOP?</a>
     * @see RequestContextHolder#getRequestAttributes()
     * @see ServletRequestAttributes#getRequest()
     * @see org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver
     * @see org.springframework.web.util.WebUtils#getNativeRequest(ServletRequest, Class)
     * @since 1.10.0
     */
    public static HttpServletRequest getRequest(){
        try{
            //the RequestAttributes currently bound to the thread, or null if none bound
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null == servletRequestAttributes){
                return null;
            }
            return servletRequestAttributes.getRequest();
        }catch (Throwable e){
            return null;
        }
    }

    //---------------------------------------------------------------
    /**
     * 普通类获得spring 注入的类方法.
     * 
     * <p>
     * 此方法底层调用的是 {@link RequestContextUtils#getWebApplicationContext(ServletRequest, ServletContext)} ,会从spingmvc 以及spring
     * ApplicationContext 查找bean
     * </p>
     * 
     * @param <T>
     *            the generic type
     * @param request
     *            request
     * @param beanName
     *            xml文件中配置的bean beanName
     * @return 先在servlet-specific {@link WebApplicationContext} 里面找 bean;<br>
     *         如果没有,会在 global context里面找;<br>
     *         如果在servlet-specific 或者 global context 都找不到,会抛出 {@link IllegalStateException}
     * @see #getWebApplicationContext(HttpServletRequest)
     * @see RequestContextUtils#getWebApplicationContext(ServletRequest, ServletContext)
     */
    public static <T> T getBean(HttpServletRequest request,String beanName){
        WebApplicationContext webApplicationContext = getWebApplicationContext(request);
        return getBean(webApplicationContext, beanName);
    }

    /**
     * Gets the bean.
     * 
     * <p>
     * 此方法底层调用的是 {@link RequestContextUtils#getWebApplicationContext(ServletRequest,
     * ServletContext)} ,会从spingmvc 以及spring ApplicationContext 查找bean
     * </p>
     * 
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param requiredType
     *            the required type
     * @return 先在servlet-specific {@link WebApplicationContext} 里面找 bean;<br>
     *         如果没有,会在 global context里面找;<br>
     *         如果在servlet-specific 或者 global context 都找不到,会抛出 {@link IllegalStateException}
     * @see #getWebApplicationContext(HttpServletRequest)
     * @see RequestContextUtils#getWebApplicationContext(ServletRequest, ServletContext)
     */
    public static <T> T getBean(HttpServletRequest request,Class<T> requiredType){
        WebApplicationContext webApplicationContext = getWebApplicationContext(request);
        return getBean(webApplicationContext, requiredType);
    }

    //---------------------------------------------------------------
    /**
     * 普通类获得spring 注入的类方法.
     * 
     * <p>
     * 注意:<b>(如果找不到bean,返回null)</b>.
     * </p>
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            servletContext
     * @param beanName
     *            xml文件中配置的bean beanName
     * @return 注入的bean
     * @see #getWebApplicationContext(ServletContext)
     */
    public static <T> T getBean(ServletContext servletContext,String beanName){
        WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
        return getBean(webApplicationContext, beanName);
    }

    /**
     * Gets the bean.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            the servlet context
     * @param requiredType
     *            the required type
     * @return the bean
     */
    public static <T> T getBean(ServletContext servletContext,Class<T> requiredType){
        WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
        return getBean(webApplicationContext, requiredType);
    }

    //---------------------------------------------------------------

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            the servlet context
     * @param beanName
     *            the bean name
     * @return the required bean
     * @see #getRequiredWebApplicationContext(ServletContext)
     */
    public static <T> T getRequiredBean(ServletContext servletContext,String beanName){
        WebApplicationContext webApplicationContext = getRequiredWebApplicationContext(servletContext);
        return getBean(webApplicationContext, beanName);
    }

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            the servlet context
     * @param requiredType
     *            the required type
     * @return the required bean
     * @see #getRequiredWebApplicationContext(ServletContext)
     */
    public static <T> T getRequiredBean(ServletContext servletContext,Class<T> requiredType){
        WebApplicationContext webApplicationContext = getRequiredWebApplicationContext(servletContext);
        return getBean(webApplicationContext, requiredType);
    }

    //---------------------------------------------------------------
    /**
     * Gets the bean.
     *
     * @param <T>
     *            the generic type
     * @param applicationContext
     *            the application context
     * @param beanName
     *            the bean name
     * @return NoSuchBeanDefinitionException - if there is no bean definition with the specified name
     */
    @SuppressWarnings("unchecked")
    private static <T> T getBean(ApplicationContext applicationContext,String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * Gets the bean.
     *
     * @param <T>
     *            the generic type
     * @param applicationContext
     *            the application context
     * @param requiredType
     *            the required type
     * @return the bean
     */
    private static <T> T getBean(ApplicationContext applicationContext,Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    //---------------------------------------------------------------

    /**
     * Find the root {@link WebApplicationContext} for this web app, typically loaded via
     * {@link org.springframework.web.context.ContextLoaderListener}.
     * <p>
     * Will rethrow an exception that happened on root context startup,
     * to differentiate between a failed context startup and no context at all.
     *
     * @param servletContext
     *            the servlet context
     * @return the root WebApplicationContext for this web app, or {@code null} if none
     * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
     * @see org.springframework.web.context.support.WebApplicationContextUtils#getWebApplicationContext(ServletContext)
     * @since 1.1.1
     */
    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext){
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    /**
     * 获得 web application context.
     *
     * @param servletContext
     *            the servlet context
     * @return the web application context
     * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
     * @see org.springframework.web.context.support.WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext)
     * @since 1.2.0
     */
    public static WebApplicationContext getRequiredWebApplicationContext(ServletContext servletContext){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

    /**
     * 获得 web application context.
     * 
     * <p>
     * 此方法可以得到springmvc 的bean
     * </p>
     *
     * @param request
     *            the request
     * @return 如果有 servlet-specific WebApplicationContext那么返回;<br>
     *         否则找 global context; <br>
     *         两个都没有 会抛出 IllegalStateException
     * @see org.springframework.web.servlet.support.RequestContextUtils#getWebApplicationContext(ServletRequest)
     * @since 1.5.3
     */
    public static WebApplicationContext getWebApplicationContext(HttpServletRequest request){
        Validate.notNull(request, "request can't be null!");

        //Gets the servlet context to which this ServletRequest was last dispatched.
        //since Servlet 3.0
        ServletContext servletContext = request.getServletContext();
        Validate.notNull(servletContext, "servletContext can't be null!,request class is:[%s]", request.getClass().getName());

        //内部调用了  WebApplicationContextUtils.getRequiredWebApplicationContext(ServletContext)
        //since spring 4.2.1  原 org.springframework.web.servlet.support.RequestContextUtils.getWebApplicationContext(ServletRequest, ServletContext) 方法
        return RequestContextUtils.findWebApplicationContext(request, servletContext);
    }
}
