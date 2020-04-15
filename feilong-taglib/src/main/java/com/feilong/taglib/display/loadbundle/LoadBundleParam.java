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
package com.feilong.taglib.display.loadbundle;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.taglib.display.CacheParam;

/**
 * 用来处理国际化.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public class LoadBundleParam implements Serializable,CacheParam{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4955427699742568979L;

    //---------------------------------------------------------------

    /**
     * 配置文件的路径, 用于 {@link ResourceBundleUtil},比如如果在i18n文件下面有 edu-en.properties那么baseName就是去掉后缀,并且去掉语言的值:i18n/edu .
     */
    private String            baseName;

    /** 国际化当前语言,如果不传,那么使用默认的 {@link Locale#getDefault()}. */
    private Locale            locale;

    //---------------------------------------------------------------

    /**
     * Instantiates a new bundle param.
     */
    public LoadBundleParam(){
        super();
    }

    //---------------------------------------------------------------

    /**
     * 获得 配置文件的路径, 用于 {@link ResourceBundleUtil},比如如果在i18n文件下面有 edu-en.
     *
     * @return the baseName
     */
    public String getBaseName(){
        return baseName;
    }

    /**
     * 设置 配置文件的路径, 用于 {@link ResourceBundleUtil},比如如果在i18n文件下面有 edu-en.
     *
     * @param baseName
     *            the baseName to set
     */
    public void setBaseName(String baseName){
        this.baseName = baseName;
    }

    /**
     * 获得 国际化当前语言,如果不传,那么使用默认的 {@link Locale#getDefault()}.
     *
     * @return the locale
     */
    public Locale getLocale(){
        return locale;
    }

    /**
     * 设置 国际化当前语言,如果不传,那么使用默认的 {@link Locale#getDefault()}.
     *
     * @param locale
     *            the locale to set
     */
    public void setLocale(Locale locale){
        this.locale = locale;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj){
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}