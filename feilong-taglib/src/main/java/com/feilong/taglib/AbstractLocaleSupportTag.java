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

import com.feilong.core.util.ResourceBundleUtil;

/**
 * 支持 locale 国际化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public abstract class AbstractLocaleSupportTag extends AbstractStartWriteContentTag implements LocaleSupport{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 828626739231668395L;

    /**
     * 配置文件的路径, 用于 {@link ResourceBundleUtil},比如如果在i18n文件下面有 edu-en.properties那么baseName就是去掉后缀,并且去掉语言的值:i18n/edu .
     */
    protected String          baseName;

    /**
     * 设置{@link Locale} 环境, 支持 <code>java.util.Locale</code> 或 String 类型的实例 ,如果是null,将默认使用 <code>request.getLocale()</code>.
     *
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#value
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#parseLocale(String, String)
     * @see org.apache.taglibs.standard.tag.common.fmt.ParseDateSupport#parseLocale
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseNumberTag#setParseLocale(Object)
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseDateTag#setParseLocale(Object)
     */
    protected Object          locale;

    //----------------------------------------------------------------------------------------------------

    /**
     * 配置文件的路径, 用于 {@link ResourceBundleUtil},比如如果在i18n文件下面有 edu-en.properties那么baseName就是去掉后缀,并且去掉语言的值:i18n/edu .
     * 
     * @param baseName
     *            the baseName to set
     */
    public void setBaseName(String baseName){
        this.baseName = baseName;
    }

    /**
     * 设置{@link Locale} 环境, 支持 java.util.Locale 或 String 类型的实例 ,如果是null,将默认使用 <code>request.getLocale()</code>.
     *
     * @param locale
     *            the locale to set
     */
    @Override
    public void setLocale(Object locale){
        this.locale = locale;
    }

}
