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
package com.feilong.net.mail.util;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 * {@link InternetAddressUtil} 的单元测试, 不依赖网络.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@lombok.extern.slf4j.Slf4j
public class InternetAddressUtilTest extends AbstractTest{

    @Test
    public void test() throws UnsupportedEncodingException{
        InternetAddress internetAddress = new InternetAddress("longxia@feilong.cn", "夏龙", UTF8);
        InternetAddress internetAddress1 = new InternetAddress("zhen.yao@feilong.cn", "姚真", UTF8);

        InternetAddress[] internetAddresses = toArray(internetAddress, internetAddress1);

        log.debug("\n{}", internetAddress.toUnicodeString());
        log.debug("\n{}", InternetAddress.toString(internetAddresses, 0));
    }

    //---------------------------------------------------------------

    @Test
    public void testToAddressArray() throws AddressException{
        Address[] addresses = InternetAddressUtil.toAddressArray(new String[] { "a@126.com", "b@126.com" });

        assertEquals(2, addresses.length);
        assertEquals("a@126.com", ((InternetAddress) addresses[0]).getAddress());
        assertEquals("b@126.com", ((InternetAddress) addresses[1]).getAddress());
    }

    @Test
    public void testToUnicodeStringList() throws UnsupportedEncodingException{
        InternetAddress[] internetAddresses = toArray(
                        new InternetAddress("longxia@feilong.cn", "夏龙", UTF8),
                        new InternetAddress("zhen.yao@feilong.cn", "姚真", UTF8));

        List<String> list = InternetAddressUtil.toUnicodeStringList(internetAddresses);

        assertEquals(2, list.size());
        assertTrue(list.get(0).contains("longxia@feilong.cn"));
        assertTrue(list.get(0).contains("夏龙"));
    }

    //---------------------------------------------------------------

    @Test
    public void testBuildFromAddress(){
        Address address = InternetAddressUtil.buildFromAddress("飞龙", "from@126.com");

        assertEquals("from@126.com", ((InternetAddress) address).getAddress());
    }

    /** personal 是 null 或者 empty 的时候, 只保留地址. */
    @Test
    public void testBuildFromAddressPersonalIsNullOrEmpty(){
        Address address = InternetAddressUtil.buildFromAddress("", "from@126.com");

        assertEquals("from@126.com", ((InternetAddress) address).getAddress());
    }

    @Test
    public void testBuildFromAddressBlank(){
        try{
            InternetAddressUtil.buildFromAddress(null, "");
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){
            assertEquals("fromAddress can't be blank!", e.getMessage());
        }
    }

}
