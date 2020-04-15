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
package com.feilong.taglib.display.httpconcat.command;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 封装解析http concat 用到的参数.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.4.0
 */
public final class HttpConcatParam implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID  = 288232184048495608L;

    //---------------------------------------------------------------

    /** 类型css/js. */
    private String            type;

    /** 版本号. */
    private String            version;

    /**
     * 根目录.
     * 
     * <p>
     * 如果设置root为'/script' 会拼成http://staging.nikestore.com.cn/script/??jquery/jquery-1.4.2.min.js?2013022801
     * </p>
     */
    private String            root;

    /** The domain. */
    private String            domain;

    /** 是否支持 http concat(如果设置这个参数,本次渲染,将会覆盖全局变量). */
    private Boolean           httpConcatSupport = null;

    //---------------------------------------------------------------

    /**
     * 内容(原样内容,内部解析).
     * 
     * @since 1.10.4
     */
    private String            content;

    //---------------------------------------------------------------

    /**
     * 获得 类型css/js.
     * 
     * @return the type
     */
    public String getType(){
        return type;
    }

    /**
     * 设置 类型css/js.
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * 获得 版本号.
     * 
     * @return the version
     */
    public String getVersion(){
        return version;
    }

    /**
     * 设置 版本号.
     * 
     * @param version
     *            the version to set
     */
    public void setVersion(String version){
        this.version = version;
    }

    /**
     * 根目录.
     * <p>
     * 如果设置root为'/script' 会拼成http://staging.nikestore.com.cn/script/??jquery/jquery-1.4.2.min.js?2013022801
     * </p>
     * 
     * @return the root
     */
    public String getRoot(){
        return root;
    }

    /**
     * 根目录.
     * <p>
     * 如果设置root为'/script' 会拼成http://staging.nikestore.com.cn/script/??jquery/jquery-1.4.2.min.js?2013022801
     * </p>
     * 
     * @param root
     *            the root to set
     */
    public void setRoot(String root){
        this.root = root;
    }

    /**
     * 获得 the domain.
     * 
     * @return the domain
     */
    public String getDomain(){
        return domain;
    }

    /**
     * 设置 the domain.
     * 
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain){
        this.domain = domain;
    }

    /**
     * 获得 是否支持 http concat(如果设置这个参数,本次渲染,将会覆盖全局变量).
     * 
     * @return the httpConcatSupport
     */
    public Boolean getHttpConcatSupport(){
        return httpConcatSupport;

    }

    /**
     * 设置 是否支持 http concat(如果设置这个参数,本次渲染,将会覆盖全局变量).
     * 
     * @param httpConcatSupport
     *            the httpConcatSupport to set
     */
    public void setHttpConcatSupport(Boolean httpConcatSupport){
        this.httpConcatSupport = httpConcatSupport;
    }

    /**
     * 获得 内容(原样内容,内部解析).
     *
     * @return the content
     * @since 1.10.4
     */
    public String getContent(){
        return content;
    }

    /**
     * 设置 内容(原样内容,内部解析).
     *
     * @param content
     *            the content to set
     * @since 1.10.4
     */
    public void setContent(String content){
        this.content = content;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        //返回该对象的哈希码值  支持此方法是为了提高哈希表(例如 java.util.Hashtable 提供的哈希表)的性能. 

        //当我们向一个集合中添加某个元素,集合会首先调用hashCode方法,这样就可以直接定位它所存储的位置,
        //若该处没有其他元素,则直接保存.

        //若该处已经有元素存在,
        //就调用equals方法来匹配这两个元素是否相同,相同则不存,
        //不同则散列到其他位置(具体情况请参考(Java提高篇()-----HashMap)).

        //这样处理,当我们存入大量元素时就可以大大减少调用equals()方法的次数,极大地提高了效率.

        //hashCode在上面扮演的角色为寻域(寻找某个对象在集合中区域位置)

        //---------------------------------------------------------------
        //可替代地,存在使用反射来确定测试中的字段的方法.
        //因为这些字段通常是私有的,该方法中,reflectionHashCode,使用AccessibleObject.setAccessible改变字段的可见性.
        //这点会在一个安全管理器失败,除非相应的权限设置是否正确.
        //它也比明确地测试速度较慢. 
        //HashCodeBuilder.reflectionHashCode(this)
        //---------------------------------------------------------------
        //你选择一个硬编码,随机选择,不为零,奇数 
        //理想情况下,每个类不同

        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 5);
        return hashCodeBuilder//
                        .append(this.domain)//
                        .append(this.httpConcatSupport)//
                        .append(this.root)//
                        .append(this.type)//
                        .append(this.version)//
                        .append(this.domain)//
                        .append(this.content)//
                        .toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        if (obj.getClass() != getClass()){
            return false;
        }

        //存在使用反射来确定测试中的字段的方法.因为这些字段通常是私有的,该方法中,reflectionEquals,使用AccessibleObject.setAccessible改变字段的可见性.
        //这点会在一个安全管理器失败,除非相应的权限设置是否正确.
        //它也比明确地测试速度较慢. 
        //EqualsBuilder.reflectionEquals(this, obj)

        HttpConcatParam t = (HttpConcatParam) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();

        return equalsBuilder //
                        .append(this.domain, t.getDomain())//
                        .append(this.httpConcatSupport, t.getHttpConcatSupport())//
                        .append(this.root, t.getRoot())//
                        .append(this.type, t.getType())//
                        .append(this.version, t.getVersion())//
                        .append(this.content, t.content)//
                        .isEquals();
    }

}
