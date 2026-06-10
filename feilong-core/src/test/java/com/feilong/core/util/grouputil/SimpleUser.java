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
package com.feilong.core.util.grouputil;

import com.feilong.lib.lang3.builder.EqualsBuilder;
import com.feilong.lib.lang3.builder.HashCodeBuilder;

/**
 * The Class User.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2011-11-28 下午03:05:56
 */
public class SimpleUser{

    /** The name. */
    private String  name = "feilong";

    /** 年龄. */
    private Integer age;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param name
     *            the name
     * @param age
     *            the age
     */
    public SimpleUser(String name, Integer age){
        this.name = name;
        this.age = age;
    }

    /**
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return the age
     */
    public Integer getAge(){
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(Integer age){
        this.age = age;
    }

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