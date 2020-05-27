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
package com.feilong.tools.emailaddress;

import java.io.Serializable;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * 邮件服务商.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.3
 */
public class EmailProvider implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8681853481958208852L;

    //---------------------------------------------------------------

    /**
     * 邮箱domain部分, 如果邮箱是 feilong@163.com, 那么值是 163.com.
     * 
     * @see <a href="https://en.wikipedia.org/wiki/Email_address#Domain_part">Domain_part</a>
     */
    private String            domain;

    /** 邮箱名, 如果邮箱是 feilong@163.com, 那么值是 网易163邮箱. */
    private String            name;

    /** 网址, 如果邮箱是 feilong@163.com, 那么值是 http://mail.163.com/. */
    private String            webSite;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param domain
     *            邮箱domain部分, 如果 邮箱是 feilong@163.com, 那么值是 163.com.
     * @param name
     *            邮箱名, 如果 邮箱是 feilong@163.com, 那么值是 网易163邮箱
     * @param webSite
     *            网址, 如果 邮箱是 feilong@163.com, 那么值是 http://mail.163.com/
     */
    protected EmailProvider(String domain, String name, String webSite){
        super();
        this.domain = domain;
        this.name = name;
        this.webSite = webSite;
    }

    //---------------------------------------------------------------

    /**
     * 邮箱domain部分, 如果邮箱是 feilong@163.com, 那么值是 163.com.
     *
     * @return the domain
     * @see <a href="https://en.wikipedia.org/wiki/Email_address#Domain_part">Domain_part</a>
     */
    public String getDomain(){
        return domain;
    }

    /**
     * 邮箱domain部分, 如果邮箱是 feilong@163.com, 那么值是 163.com.
     *
     * @param domain
     *            the domain to set
     * @see <a href="https://en.wikipedia.org/wiki/Email_address#Domain_part">Domain_part</a>
     */
    public void setDomain(String domain){
        this.domain = domain;
    }

    /**
     * 邮箱名, 如果邮箱是 feilong@163.com, 那么值是 网易163邮箱
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * 邮箱名, 如果邮箱是 feilong@163.com, 那么值是 网易163邮箱
     *
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 网址, 如果邮箱是 feilong@163.com, 那么值是 http://mail.163.com/
     *
     * @return the webSite
     */
    public String getWebSite(){
        return webSite;
    }

    /**
     * 网址, 如果邮箱是 feilong@163.com, 那么值是 http://mail.163.com/
     *
     * @param webSite
     *            the webSite to set
     */
    public void setWebSite(String webSite){
        this.webSite = webSite;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
