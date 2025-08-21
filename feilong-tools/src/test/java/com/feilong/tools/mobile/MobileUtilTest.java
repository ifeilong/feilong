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

import org.junit.Test;

import com.feilong.lib.lang3.StringUtils;
import com.feilong.test.AbstractTest;

/**
 * 手机相关测试.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@lombok.extern.slf4j.Slf4j
public class MobileUtilTest extends AbstractTest{

    /** The mobile number. */
    private final String mobileNumber = "15000001318";

    @Test
    public void testGetMobileNumberHided(){
        assertEquals("150****1318", MobileUtil.getMobileNumberHided(mobileNumber));

        log.debug(StringUtils.abbreviateMiddle(mobileNumber, "***", 10));
        //log.debug(MobileUtil.getMobileNumberHided(mobileNumber, 15));
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
