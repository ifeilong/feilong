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
package com.feilong.csv;

import static com.feilong.core.CharsetType.GBK;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.nowTimestamp;
import static org.apache.commons.lang3.SystemUtils.USER_HOME;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.csv.CsvWrite;
import com.feilong.csv.DefaultCsvWrite;
import com.feilong.csv.entity.BeanCsvConfig;
import com.feilong.io.FileUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class CSVUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class CsvBeanWriteTest{

    private final CsvWrite        csvWrite      = new DefaultCsvWrite();

    private static Iterable<User> ITERABLE_DATA = null;

    private String                url;

    private BeanCsvConfig<User>   beanCsvConfig = null;

    //---------------------------------------------------------------

    @BeforeClass
    public static void buildTestData(){
        ITERABLE_DATA = toList(
                        new User("zhang\"san1", "张三1", 18),
                        new User("zhangsan5", "张三5", 28),
                        new User("zhang'san4", "张三4", 38),
                        new User("zhangsan6", "张三6", 48));
    }

    private String buildUrl(String name){
        return buildUrl("csv", name + nowTimestamp() + ".csv");

    }

    private String buildUrl(String type,String fileName){
        String pattern = "{}/feilong/{}/{}";
        return Slf4jUtil.format(pattern, USER_HOME, type, fileName);
    }

    @Test
    public void testWrite2(){
        url = buildUrl("writeBean-base");
    }

    @Test
    public void testWrite3(){
        url = buildUrl("writeBean-encode");

        beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setEncode(GBK);
    }

    @Test
    public void testWrite4(){
        url = buildUrl("writeBean-encode-noheader");

        beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setEncode(GBK);
        beanCsvConfig.setIsPrintHeaderLine(false);
    }

    @Test
    public void testWrite5(){
        url = buildUrl("writeBean-encode-noheader-exclude");

        beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setEncode(GBK);
        beanCsvConfig.setIsPrintHeaderLine(false);
        beanCsvConfig.setExcludePropertyNames("name", "age");
    }

    /**
     * afterCsvBeanWriteTest.
     */
    @After
    public void afterCsvBeanWriteTest(){
        csvWrite.write(url, ITERABLE_DATA, beanCsvConfig);
        DesktopUtil.open(FileUtil.getParent(url));
        DesktopUtil.open(url);
    }
}
