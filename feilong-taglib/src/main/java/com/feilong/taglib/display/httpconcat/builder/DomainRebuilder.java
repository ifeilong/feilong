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
package com.feilong.taglib.display.httpconcat.builder;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * domain 再加工.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class DomainRebuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainRebuilder.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private DomainRebuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 如果domain 是null或者 empty 那么会使用 {@link javax.servlet.http.HttpServletRequest#getContextPath()}.
     *
     * @param domain
     *            the domain
     * @param request
     *            the request
     * @return the string
     */
    public static String rebuild(String domain,HttpServletRequest request){
        //如果不是空, 那么返回 设置的domain
        if (isNotNullOrEmpty(domain)){
            return domain;
        }

        //---------------------------------------------------------------
        String globalDomain = GLOBAL_CONFIG.getDomain();
        if (isNullOrEmpty(globalDomain)){
            String contextPath = request.getContextPath();
            LOGGER.debug("domain is null or empty, use request contextPath:[{}]", contextPath);
            return contextPath;
        }

        //---------------------------------------------------------------
        LOGGER.trace("domain is null or empty, use globalDomain:[{}]", globalDomain);
        return globalDomain;
    }
}
