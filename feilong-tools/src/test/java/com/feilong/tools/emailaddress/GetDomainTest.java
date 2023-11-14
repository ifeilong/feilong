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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * The Class GetDomainTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
public class GetDomainTest{

    /** The email. */
    private static final String EMAIL = "feilong@163.com";

    /**
     * Test get domain.
     */
    @Test
    public void testGetDomain(){
        assertEquals("163.com", EmailAddressUtil.getDomain(EMAIL));
    }

    //---------------------------------------------------------------

    /**
     * Test get domain test null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetDomainTestNull(){
        EmailAddressUtil.getDomain(null);
    }

    /**
     * Test get domain test empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDomainTestEmpty(){
        EmailAddressUtil.getDomain("");
    }

    /**
     * Test get domain test blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDomainTestBlank(){
        EmailAddressUtil.getDomain(" ");
    }
}
