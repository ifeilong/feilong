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

package com.feilong.taglib;

import static com.feilong.core.Validator.isNullOrEmpty;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.Validate;

/**
 * Utility class for tag library related code, exposing functionality
 * such as translating {@link String Strings} to web scopes.
 * 
 * <p>
 * 借鉴了 org.springframework.web.util.TagUtils
 * </p>
 * 
 * <p>
 * <ul>
 * <li>{@code page} will be transformed to {@link javax.servlet.jsp.PageContext#PAGE_SCOPE PageContext.PAGE_SCOPE}
 * <li>{@code request} will be transformed to {@link javax.servlet.jsp.PageContext#REQUEST_SCOPE PageContext.REQUEST_SCOPE}
 * <li>{@code session} will be transformed to {@link javax.servlet.jsp.PageContext#SESSION_SCOPE PageContext.SESSION_SCOPE}
 * <li>{@code application} will be transformed to {@link javax.servlet.jsp.PageContext#APPLICATION_SCOPE PageContext.APPLICATION_SCOPE}
 * </ul>
 *
 * @author Alef Arendsen
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Rick Evans
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.springframework.web.util.TagUtils#getScope(String)"
 * @see javax.servlet.jsp.jstl.core.ConditionalTagSupport#setScope(String)
 * @since 1.11.1
 */
public final class TagUtils{

    /** Constant identifying the page scope. */
    public static final String SCOPE_PAGE        = "page";

    /** Constant identifying the request scope. */
    public static final String SCOPE_REQUEST     = "request";

    /** Constant identifying the session scope. */
    public static final String SCOPE_SESSION     = "session";

    /** Constant identifying the application scope. */
    public static final String SCOPE_APPLICATION = "application";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private TagUtils(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Determines the scope for a given input {@code String}.
     * 
     * <p>
     * If the {@code String} does not match 'request', 'session', 'page' or 'application', the method will return
     * {@link PageContext#PAGE_SCOPE}.
     * </p>
     * 
     * @param scope
     *            the {@code String} to inspect
     * @return the scope found, or {@link PageContext#PAGE_SCOPE} if no scope matched
     *         如果 <code>scope</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>scope</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static int getScope(String scope){
        Validate.notBlank(scope, "scope can't be blank!");

        if (scope.equalsIgnoreCase(SCOPE_REQUEST)){
            return PageContext.REQUEST_SCOPE;
        }

        if (scope.equalsIgnoreCase(SCOPE_SESSION)){
            return PageContext.SESSION_SCOPE;
        }

        if (scope.equalsIgnoreCase(SCOPE_APPLICATION)){
            return PageContext.APPLICATION_SCOPE;
        }

        return PageContext.PAGE_SCOPE;
    }

    //---------------------------------------------------------------

    /**
     * Find attribute value.
     * 
     * @param <T>
     *
     * @param pageContext
     *            the page context
     * @param attributeName
     *            the version name in scope
     * @param scope
     *            the version search scope
     * @return 如果 <code>pageContext</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>attributeName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>attributeName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>scope</code> 是null或者 blank,将会调用 {@link javax.servlet.jsp.JspContext#findAttribute(String)}<br>
     *         如果 <code>scope</code> 不是null或者 blank,将会调用 {@link javax.servlet.jsp.JspContext#getAttribute(String, int)}<br>
     * @since 1.12.8
     */
    @SuppressWarnings("unchecked")
    public static <T> T findAttributeValue(PageContext pageContext,String attributeName,String scope){
        Validate.notNull(pageContext, "pageContext can't be null!");
        Validate.notBlank(attributeName, "attributeName can't be blank!");

        //---------------------------------------------------------------

        //没有指定scope 那么从pageContext中查找
        if (isNullOrEmpty(scope)){
            return (T) pageContext.findAttribute(attributeName);
        }

        //如果指定了, 那么从指定的scope中获取
        return (T) pageContext.getAttribute(attributeName, TagUtils.getScope(scope));
    }
}
