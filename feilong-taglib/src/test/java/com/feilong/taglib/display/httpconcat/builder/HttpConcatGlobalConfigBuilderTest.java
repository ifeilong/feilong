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
package com.feilong.taglib.display.httpconcat.builder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class HttpConcatGlobalConfigBuilderTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConcatGlobalConfigBuilderTest.class);

    @Test
    public void test(){

        for (int i = 0; i < 5; ++i){
            HttpConcatGlobalConfig a = HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;
            LOGGER.info("111");
        }
    }
}
