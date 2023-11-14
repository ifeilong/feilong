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
package com.feilong.tools.emailaddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/**
 * The Class GetEmailProviderTest.
 */
public class GetEmailProviderTest{

    /** The email. */
    private static final String EMAIL = "feilong@163.com";

    /**
     * Test get email provider.
     */
    @Test
    public void testGetEmailProvider(){
        EmailProvider emailProvider = EmailAddressUtil.getEmailProvider(EMAIL);
        assertThat(emailProvider, allOf(//
                        hasProperty("webSite", is("http://mail.163.com/")),
                        hasProperty("name", is("网易163邮箱")),
                        hasProperty("domain", is("163.com"))));
    }

    /**
     * Test get email provider test null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetEmailProviderTestNull(){
        EmailAddressUtil.getEmailProvider(null);
    }

    /**
     * Test get email provider test empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEmailProviderTestEmpty(){
        EmailAddressUtil.getEmailProvider("");
    }

    /**
     * Test get email provider test blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEmailProviderTestBlank(){
        EmailAddressUtil.getEmailProvider("  ");
    }

}
