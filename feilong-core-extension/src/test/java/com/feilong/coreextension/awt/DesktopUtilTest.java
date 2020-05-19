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
package com.feilong.coreextension.awt;

import org.junit.Test;

import com.feilong.lib.lang3.SystemUtils;

public class DesktopUtilTest{

    /** The test file. */
    private final String testFile = SystemUtils.USER_HOME + "/.m2/settings.xml";

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testBrowse1(){
        DesktopUtil.browse("http://101.95.128.146/payment/paymentChannel?s={}&id={}", 14, "中国");

    }

    //---------------------------------------------------------------
    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testPrint(){
        DesktopUtil.print(testFile);
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testOpen(){
        DesktopUtil.open(testFile);
    }

    //---------------------------------------------------------------
    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testMail(){
        DesktopUtil.mail(
                        "mailto:{}?subject={}&body={}", //
                        "feilong@163.com",
                        "你好",
                        "我是飞天奔月</br>哈哈哈哈");
    }

}
