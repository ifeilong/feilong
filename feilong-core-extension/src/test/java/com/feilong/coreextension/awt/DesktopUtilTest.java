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
package com.feilong.coreextension.awt;

import org.junit.Test;

import com.feilong.tools.slf4j.Slf4jUtil;

public class DesktopUtilTest{

    /** The test file. */
    private final String testFile = "E:\\DataCommon\\test\\test.txt";

    /**
     * Test browse1.
     */
    @Test
    public void testBrowse1(){
        int id = 14;
        String s = "中国";
        DesktopUtil.browse("http://101.95.128.146/payment/paymentChannel?s=" + s + "&id=" + id);
    }

    /**
     * Test print.
     */
    @Test
    public void testPrint(){
        DesktopUtil.print(testFile);
    }

    /**
     * Test edit.
     */
    @Test
    public void testEdit(){
        DesktopUtil.edit(testFile);
    }

    @Test
    public void testOpen(){
        DesktopUtil.open(testFile);
    }

    @Test
    public void testMail(){
        String a = "mailto:{}?subject={}&body={}";
        String format = Slf4jUtil.format(a, "feilong@163.com", "你好", "我是飞天奔月</br>哈哈哈哈");

        DesktopUtil.mail(format);
    }

    @Test
    public void testBrowse(){
        String[] strings = { "2RMD217-4", "ARHE041-1", "CRDF013-3" };
        for (String string : strings){
            DesktopUtil.browse("http://list.tmall.com/search_product.htm?q=" + string + "&type=p&cat=all&userBucket=5&userBucketCell=25");
        }
    }
}
