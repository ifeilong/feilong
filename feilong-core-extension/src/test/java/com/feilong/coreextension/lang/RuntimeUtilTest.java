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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 */
public class RuntimeUtilTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeUtilTest.class);

    //---------------------------------------------------------------

    @Test
    public void test(){
        System.out.println(RuntimeUtil.execCmd("ll"));//TODO:remove;
    }

    @Test
    public void test12(){
        System.out.println(RuntimeUtil.execCmd("mvn -v"));//TODO:remove;
    }

    //---------------------------------------------------------------

    @Test
    public void test1(){
        String command = "/Users/feilong/workspace/feilong/feilong-incubator/feilong-test-incubator/src/test/resources/test.sh";
        command += " jinxin";

        String result = RuntimeUtil.execCmd(command);
        LOGGER.debug(result);
    }

}
