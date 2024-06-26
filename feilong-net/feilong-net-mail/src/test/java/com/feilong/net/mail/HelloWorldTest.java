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

import org.junit.Test;

import com.feilong.template.TemplateUtil;

public class HelloWorldTest extends AbstractMailSenderTest{

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void test(){
        mailSendRequest.setSubject("hello world");
        mailSendRequest.setContent(TemplateUtil.parseTemplate("hello world.vm"));
    }

    //---------------------------------------------------------------

    //    @Override
    //    protected String getConfigFile(){
    //        return "mail-feilongtestemail.properties";
    //    }
    @Override
    protected String getConfigFile(){
        return "mail-ximalayaopenapi.properties";
    }
}
