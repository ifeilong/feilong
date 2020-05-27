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
package com.feilong.net.mail.builder;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.feilong.net.mail.entity.SessionConfig;

/**
 * 用来生成 {@link Authenticator}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.2
 */
public class AuthenticatorBuilder{

    /** Don't let anyone instantiate this class. */
    private AuthenticatorBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 判断是否需要身份认证,如果需要身份认证,则创建一个密码验证器.
     *
     * @param sessionConfig
     *            the base config
     * @return the authenticator
     * @see javax.mail.PasswordAuthentication#PasswordAuthentication(String, String)
     */
    public static Authenticator build(final SessionConfig sessionConfig){
        // 判断是否需要身份认证
        if (sessionConfig.getIsValidate()){
            return new Authenticator(){// 如果需要身份认证,则创建一个密码验证器

                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(sessionConfig.getUserName(), sessionConfig.getPassword());
                }
            };
        }
        return null;
    }
}
