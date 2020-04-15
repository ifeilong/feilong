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

public class MailAttachFileSenderSimpleTest extends AbstractMailSenderTest{

    @Test
    public void sendMailWithAttach(){
        mailSenderConfig.setContent("hello world");
        mailSenderConfig.setAttachFilePaths("/Users/feilong/Downloads/20200326165841.zip");
    }

    @Test
    public void sendMailWithAttach1(){
        mailSenderConfig.setContent("hello world");
        //        另外，如果要做内嵌或发送图片，你应该使用信用较高的邮箱帐户，否则会报错：
        //        554 DT:SPM 发送的邮件内容包含了未被许可的信息，或被系统识别为垃圾邮件。请检查是否有用户发送病毒或者垃圾邮件

        mailSenderConfig.setAttachFilePaths("/Users/feilong/DataFixed/Material/头像avatar/飞龙.png");
    }
}