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
package com.feilong.coreextension.lang;

import static com.feilong.core.bean.ConvertUtil.toArray;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 */
public class MavenRuntimeUtilTest{

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(MavenRuntimeUtilTest.class);

    private static final String FOLDER           = "/Users/feilong/Development/DataCommon/Files/Java/Apache Maven";

    private static final String shellPath        = FOLDER + "/mvn-deploy-file.sh";

    private static final String SHELLPATH_SOURCE = FOLDER + "/mvn-deploy-file-source.sh";

    String[]                    params           = null;

    //    <dependency>
    //    <groupId>com.taobao</groupId>
    //    <artifactId>taobao-sdk-java-auto</artifactId>
    //    <version>20190603</version>
    //</dependency>
    @Test
    public void test667661(){
        params = toArray(
                        "com.taobao",
                        "taobao-sdk-java-auto",
                        "20190603", //
                        "/Users/feilong/Downloads/taobao-sdk-java-auto-20190603.jar");
    }

    @Test
    public void test6671(){
        params = toArray(
                        "jd",
                        "open-api-sdk",
                        "20190515-2", //
                        "/Users/feilong/Downloads/open-api-sdk-2.0-1.jar");
    }

    //    <groupId>taobao</groupId>
    //    <artifactId>taobao-sdk-java-online_standard</artifactId>
    //    <version>20190529</version>

    @Test
    public void test667777(){
        params = toArray(
                        "taobao",
                        "taobao-sdk-java-online_standard",
                        "20190529", //
                        "/Users/feilong/Downloads/taobao-sdk-java-auto_1557803250975-20190527.jar",
                        "/Users/feilong/Downloads/taobao-sdk-java-auto_1557803250975-20190527-source.jar");
    }

    @Test
    public void test667(){
        params = toArray(
                        "taobao",
                        "taobao-sdk-java-online_standard",
                        "20190510", //
                        "/Users/feilong/Downloads/taobao-sdk-java-auto_1557193685836-20190510.jar",
                        "/Users/feilong/Downloads/taobao-sdk-java-auto_1557193685836-20190510-source.jar");
    }

    @Test
    public void test66(){
        params = toArray(
                        "taobao",
                        "taobao-sdk-java-online_standard",
                        "20190507", //
                        "/Users/feilong/Downloads/taobao-sdk-java-auto_1557193685836-20190507.jar",
                        "/Users/feilong/Downloads/taobao-sdk-java-auto_1557193685836-20190507-source.jar");
    }

    @Test
    public void test5(){
        params = toArray(
                        "jd",
                        "open-api-sdk",
                        "20190404", //
                        "/Users/feilong/Downloads/open-api-sdk-2.0.jar");
    }

    //    <dependency>
    //    <groupId>jd</groupId>
    //    <artifactId>open-api-sdk</artifactId>
    //    <version>20190404</version>
    //   </dependency>

    @Test
    public void test6(){
        params = toArray(
                        "com.jumbo",
                        "jm-mq-msg-entity",
                        "4.3.0", //
                        "/Users/feilong/Downloads/jm-mq-msg-entity-4.3.0.jar");

    }

    @Test
    public void test4(){
        params = toArray(
                        "jd",
                        "open-api-sdk",
                        "20190318", //
                        "/Users/feilong/Downloads/open-api-sdk-2.01.jar");

    }

    //---------------------------------------------------------------

    @Test
    public void test2(){
        params = toArray(
                        //groupId
                        "com.lowagie",

                        //artifactId
                        "itextasian",

                        //version
                        "1.5.2",

                        "/Users/feilong/Downloads/itextasian-1.5.2.jar");

    }

    @Test
    public void loxia(){
        params = toArray(
                        //groupId
                        "loxia2",
                        //artifactId
                        "loxia2-core",
                        //version
                        "2.6.1.test",
                        "loxia2-core-2.6.1.jar");

    }

    //---------------------------------------------------------------
    @After
    public void after(){
        String file = params.length == 4 ? shellPath : SHELLPATH_SOURCE;
        String result = RuntimeUtil.execCmd(ArrayUtils.insert(0, params, file));
        LOGGER.debug(result);
    }
}
