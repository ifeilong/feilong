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
package com.feilong.json.format;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.StoreLocatorErrorProperty;

/**
 * The Class FormatBeanErrorPropertyTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-json/issues/20">json format ,当字段名字叫 isO2O,但是get 方法叫 isO2O() 的时候会出现异常</a>
 * @since 1.12.0
 */
@lombok.extern.slf4j.Slf4j
public class FormatBeanErrorPropertyTest extends AbstractJsonTest{

    @Test
    public void test(){
        StoreLocatorErrorProperty storeLocator = new StoreLocatorErrorProperty();
        log.debug(JsonUtil.format(storeLocator));
    }
}
