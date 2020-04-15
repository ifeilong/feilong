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
package com.feilong.tools.mobile;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.tools.mobile.MobileUtil;

/**
 * 手机相关测试.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class MobileUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER       = LoggerFactory.getLogger(MobileUtilTest.class);

    /** The mobile number. */
    private final String        mobileNumber = "15000001318";

    @Test
    public void testGetMobileNumberHided(){
        assertEquals("150****1318", MobileUtil.getMobileNumberHided(mobileNumber));

        LOGGER.debug(StringUtils.abbreviateMiddle(mobileNumber, "***", 10));
        //LOGGER.debug(MobileUtil.getMobileNumberHided(mobileNumber, 15));
        // assertEquals(mobileNumber, FeiLongMobile.getMobileNumberHided(mobileNumber,4));
    }

    /**
     * Test get mobile number number segment.
     */
    @Test
    public void testGetMobileNumberNumberSegment(){
        assertEquals("150", MobileUtil.getMobileNumberSegment(mobileNumber));
    }

}
