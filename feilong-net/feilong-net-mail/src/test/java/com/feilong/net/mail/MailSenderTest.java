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
import static org.apache.commons.lang3.SystemUtils.USER_HOME;

import java.io.IOException;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;

public class MailSenderTest extends AbstractMailSenderTest{

    @Test
    public void sendMail1() throws IOException{
        String path = USER_HOME + "/feilong/train/1201单元测试/generalRegulation/generalRegulation-20141125194610.html";
        String textContent = IOReaderUtil.readToString(path, UTF8);
        mailSenderConfig.setContent(textContent);
    }

    //---------------------------------------------------------------

    @Test
    public void sendMail(){
        String textContent = "<html><body><hr/><div style='boder:1px #000 solid;color:red'>今天天气不错</div></body></html>";
        mailSenderConfig.setContent(textContent);
    }

    //---------------------------------------------------------------
    @Test
    public void testSendTextMail(){
        String textContent = "测试回执";
        mailSenderConfig.setContent(textContent);
    }
}