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
package com.feilong.core.bean.convertutil;

import static com.feilong.core.bean.ConvertUtil.convert;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import com.feilong.lib.beanutils.ConversionException;

/**
 * The Class ConvertUtilConvertClassTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class ConvertClassTest{

    //    @Test
    //    public void testConvert(){
    //        String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray("6", "_");
    //        LinkedList<Serializable> linkedList = new LinkedList<Serializable>();
    //        for (String string : tokenizeToStringArray){
    //            Serializable t = ConvertUtil.convert(string, Serializable.class);
    //            log.debug(t.getClass().getCanonicalName());
    //            linkedList.add(t);
    //        }
    //        Serializable l = 6L;
    //        log.debug("linkedList:{},contains:{},{}", linkedList, l, linkedList.contains(l));
    //    }

    /**
     * Test convert 2.
     */
    @Test

    public void testConvert2(){
        assertEquals(1, convert("1", Integer.class).intValue());
        assertEquals(1, convert("1", Long.class).intValue());
    }

    /**
     * Test convert 3.
     */
    @Test

    public void testConvert3(){
        assertEquals(null, convert("", Integer.class));
    }

    //---------------------------------------------------------------

    /**
     * Test to URL.
     */
    @Test(expected = ConversionException.class)
    public void testToURL(){
        String spec = "C:\\Users\\feilong\\feilong\\train\\新员工\\warmReminder\\20160704141057.html";
        convert(spec, URL.class); //异常

        //MalformedURLException ConversionException
    }

    /**
     * Test convert target type.
     */
    @Test(expected = NullPointerException.class)

    public void testConvertTargetType(){
        String spec = "C:\\Users\\feilong\\feilong\\train\\新员工\\warmReminder\\20160704141057.html";
        convert(spec, null);
    }
}
