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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.net.mail.builder.AuthenticatorBuilderTest;
import com.feilong.net.mail.builder.MessageBuilderTest;
import com.feilong.net.mail.builder.SessionPropertiesBuilderTest;
import com.feilong.net.mail.builder.setter.BodySetterTest;
import com.feilong.net.mail.builder.setter.HeaderSetterTest;
import com.feilong.net.mail.setter.RecipientsSetterTest;
import com.feilong.net.mail.util.InternetAddressUtilTest;

/**
 * 邮件模块的测试集合.
 * 
 * <p>
 * <b>这里只放不依赖网络的用例</b>, 因此 <code>mvn -pl feilong-net/feilong-net-mail -am test</code> 可以直接跑通.<br>
 * 需要真实邮箱的用例(<code>HelloWorldTest</code>、<code>MailSenderTest</code>、<code>AttachSenderTest</code>、
 * <code>AttachSenderSimpleTest</code>、<code>EmojiSubjectTest</code>、<code>SSLTest</code>、<code>ics</code> 包下的用例等)
 * 依赖 <code>src/test/resources</code> 下的邮箱配置, 不放进 suite, 请手工在 IDE 里运行.
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@RunWith(Suite.class)
@SuiteClasses({ //

                // builder
                AuthenticatorBuilderTest.class,
                SessionPropertiesBuilderTest.class,
                MessageBuilderTest.class,

                // builder.setter
                HeaderSetterTest.class,
                BodySetterTest.class,

                // 核心
                SessionFactoryTest.class,
                DefaultMailSenderTest.class,

                // util / setter
                RecipientsSetterTest.class,
                InternetAddressUtilTest.class,
        //
})
public class SuiteTests{

}
