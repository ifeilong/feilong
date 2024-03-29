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
package com.feilong.json;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.now;

import com.feilong.store.member.User;
import com.feilong.store.member.UserAddress;
import com.feilong.store.member.UserInfo;
import com.feilong.test.AbstractTest;

public abstract class AbstractJsonTest extends AbstractTest{

    protected static final User   USER             = getUserForJsonTest();

    protected static final String USER_JSON_STRING = JsonUtil.toString(USER);

    //---------------------------------------------------------------

    /**
     * Gets the json string.
     * 
     * @return the json string
     */
    private static User getUserForJsonTest(){
        User user = new User();

        user.setPassword("123456");
        user.setId(8L);
        user.setName("feilong");
        user.setDate(now());
        user.setMoney(toBigDecimal("99999999.00"));

        user.setLoves(toArray("桔子", "香蕉"));
        user.setUserInfo(new UserInfo(10));

        UserAddress userAddress1 = new UserAddress("上海市地址1");
        UserAddress userAddress2 = new UserAddress("上海市地址2");

        user.setUserAddresses(toArray(userAddress1, userAddress2));
        user.setUserAddresseList(toList(userAddress1, userAddress2));
        return user;
    }
}
