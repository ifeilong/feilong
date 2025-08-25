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
package com.feilong.template;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 解析模板.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TemplateUtil{

    /** The Constant velocityUtil. */
    private static final VelocityUtil velocityUtil = new VelocityUtil();

    //---------------------------------------------------------------

    /**
     * Parses the string.
     *
     * @param vmContent
     *            the vm content
     * @param contextKeyValues
     *            the context key values
     * @return the string
     */
    public static String parseString(String vmContent,Map<String, ?> contextKeyValues){
        return velocityUtil.parseString(vmContent, contextKeyValues);
    }

    //---------------------------------------------------------------

    /**
     * Parses the template.
     *
     * @param templateInClassPath
     *            the template in class path
     * @return the string
     */
    public static String parseTemplate(String templateInClassPath){
        return velocityUtil.parseTemplateWithClasspathResourceLoader(templateInClassPath);
    }

    /**
     * Parses the template.
     *
     * @param templateInClassPath
     *            the template in class path
     * @param contextKeyValues
     *            the context key values
     * @return the string
     */
    public static String parseTemplate(String templateInClassPath,Map<String, ?> contextKeyValues){
        return velocityUtil.parseTemplateWithClasspathResourceLoader(templateInClassPath, contextKeyValues);
    }
}
