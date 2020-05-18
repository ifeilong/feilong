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

import java.util.Map;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;

import com.feilong.core.bean.BeanUtil;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.io.FileUtil;
import com.feilong.lib.beanutils.ConvertUtils;
import com.feilong.lib.beanutils.converters.ArrayConverter;
import com.feilong.lib.beanutils.converters.StringConverter;
import com.feilong.lib.lang3.SystemUtils;
import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.test.AbstractTest;

public abstract class AbstractMailSenderTest extends AbstractTest{

    /** The Constant folder. */
    private static final String        folder  = SystemUtils.USER_HOME + "/Development/DataCommon/Files/Java/config/";

    //---------------------------------------------------------------

    /** The mail sender config. */
    protected MailSendRequest          mailSendRequest;

    protected MailSendConnectionConfig mailSendConnectionConfig;

    public static final String         toEmail = "venusdrogon@163.com";

    //---------------------------------------------------------------

    @Before
    public void before(){
        loadMailSenderConfig();
    }

    //---------------------------------------------------------------
    private void loadMailSenderConfig(){
        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(FileUtil.getFileInputStream(folder + getConfigFile()));
        Map<String, String> map = ResourceBundleUtil.toMap(resourceBundle);

        ArrayConverter arrayConverter = new ArrayConverter(String[].class, new StringConverter(), 2);
        char[] allowedChars = { '@' };
        arrayConverter.setAllowedChars(allowedChars);
        ConvertUtils.register(arrayConverter, String[].class);

        //---------------------------------------------------------------

        mailSendRequest = new MailSendRequest();
        mailSendConnectionConfig = new MailSendConnectionConfig();

        mailSendRequest = BeanUtil.populate(mailSendRequest, map);
        mailSendConnectionConfig = BeanUtil.populate(mailSendConnectionConfig, map);
    }

    //---------------------------------------------------------------

    protected String getConfigFile(){
        return "mail-feilongtestemail.properties";
    }

    //---------------------------------------------------------------

    @After
    public void after(){
        MailSender mailSender = new DefaultMailSender(mailSendConnectionConfig);
        mailSender.send(mailSendRequest);
    }

}
