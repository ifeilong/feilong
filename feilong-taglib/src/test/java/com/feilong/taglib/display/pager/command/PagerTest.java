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
package com.feilong.taglib.display.pager.command;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.store.member.User;
import com.feilong.test.AbstractTest;

/**
 * The Class PagerTest.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@lombok.extern.slf4j.Slf4j
public class PagerTest extends AbstractTest{

    @Test
    public void test(){
        Pager<User> pager = new Pager<>(2, 10, 10000);
        pager.setMaxShowPageNo(-2);
        log.debug(JsonUtil.format(pager));
    }
}