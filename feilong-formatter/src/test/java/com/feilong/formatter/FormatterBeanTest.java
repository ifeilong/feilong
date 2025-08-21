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
package com.feilong.formatter;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import org.junit.Test;

import com.feilong.store.member.User;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class FormatterBeanTest extends AbstractTest{

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public final void test(){
        User user = new User();
        user.setAge(15);
        user.setId(88L);
        user.setAttrMap(toMap("love", "sanguo"));
        user.setDate(now());
        user.setMoney(toBigDecimal(999));
        user.setName("xinge");
        user.setNickNames(toArray("jinxin", "feilong"));

        log.debug(formatToSimpleTable(user));

    }

}
