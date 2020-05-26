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
package com.feilong.net.mail;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.lib.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;

@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
public class MailSenderTest extends AbstractMailSenderTest{

    @Test
    public void sendMail1(){
        String textContent = IOReaderUtil.readToString(CLASSPATH_URL_PREFIX + "参数解析能手 - ParamUtil.html", UTF8);
        mailSendRequest.setContent(textContent);
    }

    //---------------------------------------------------------------

    @Test
    public void sendMail(){
        String textContent = "<html><body><hr/><div style='boder:1px #000 solid;color:red'>今天天气不错</div></body></html>";
        mailSendRequest.setContent(textContent);
    }

    //---------------------------------------------------------------
    @Test
    public void testSendTextMail(){
        String textContent = "测试回执";
        mailSendRequest.setContent(textContent);
    }
}