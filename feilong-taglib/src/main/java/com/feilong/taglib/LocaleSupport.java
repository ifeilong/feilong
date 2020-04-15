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

import java.util.Locale;

/**
 * 标识支持 locale 语言环境的标签 support.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport
 * @see javax.servlet.jsp.jstl.fmt.LocaleSupport
 * @since 1.7.2
 */
public interface LocaleSupport{

    /**
     * 设置{@link Locale} 环境, 支持 <code>java.util.Locale</code> 或 String 类型的实例 ,如果是null,将默认使用 <code>request.getLocale()</code>.
     *
     * @param locale
     *            the new locale
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#value
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#parseLocale(String, String)
     * @see org.apache.taglibs.standard.tag.common.fmt.ParseDateSupport#parseLocale
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseNumberTag#setParseLocale(Object)
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseDateTag#setParseLocale(Object)
     */
    //不能声明成 Serializable类型
    //Unable to convert string "zh_CN" to class "java.io.Serializable" for attribute "locale": Property Editor not registered with the PropertyEditorManager
    void setLocale(Object locale);

}
