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
package com.feilong.net.bot;

import lombok.ToString;

/**
 * 名字和手机号码模型, 方便发消息的时候 at相关人员.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.5.3
 */
@ToString
public class UserNameAndMobileEntity{

    /** at 人员. */
    private String userName;

    /** 手机号码. */
    private String mobile;

    //---------------------------------------------------------------

    /**
     * Instantiates a new user name and mobile entity.
     */
    public UserNameAndMobileEntity(){
        super();
    }

    /**
     * Instantiates a new user name and mobile entity.
     *
     * @param userName
     *            the user name
     * @param mobile
     *            the mobile
     */
    public UserNameAndMobileEntity(String userName, String mobile){
        super();
        this.userName = userName;
        this.mobile = mobile;
    }

    //---------------------------------------------------------------

    /**
     * 获得 at 人员.
     *
     * @return the userName
     */
    public String getUserName(){
        return userName;
    }

    /**
     * 设置 at 人员.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * 获得 手机号码.
     *
     * @return the mobile
     */
    public String getMobile(){
        return mobile;
    }

    /**
     * 设置 手机号码.
     *
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

}
