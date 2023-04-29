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
package com.feilong.core.util.aggregateutil;

import com.feilong.store.member.User;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.5.0
 */
public class UserWithStringAge extends User{

    /**
     * string 类型的年龄
     */
    private String ageString;

    public UserWithStringAge(){
    }

    public UserWithStringAge(String name, String ageString){
        super();
        super.setName(name);
        this.ageString = ageString;
    }

    /**
     * @param ageString
     */
    public UserWithStringAge(String ageString){
        super();
        this.ageString = ageString;
    }

    /**
     * @return the ageString
     */
    public String getAgeString(){
        return ageString;
    }

    /**
     * @param ageString
     *            the ageString to set
     */
    public void setAgeString(String ageString){
        this.ageString = ageString;
    }

}
