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
package com.feilong.net.mail.entity;

/**
 * 邮件发送级别.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.3
 */
public enum Priority{

    /** 最高. */
    HIGHEST("1"),

    /** 高. */
    HIGH("2"),

    /** 一般. */
    NORMAL("3"),

    /** 低. */
    LOW("4"),

    /** 最低. */
    LOWEST("5");

    //---------------------------------------------------------------

    /** 级别. */
    private String levelValue;

    /**
     * Instantiates a new priority.
     * 
     * @param levelValue
     *            the level value
     */
    private Priority(String levelValue){
        this.levelValue = levelValue;
    }

    /**
     * Gets the 级别.
     * 
     * @return the levelValue
     */
    public String getLevelValue(){
        return levelValue;
    }

}