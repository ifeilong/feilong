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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.List;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.store.member.Address;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class FormatterUtilTest extends AbstractTest{

    @Test

    public final void test(){
        List<Address> list = toList(
                        new Address("china", "shanghai", "216000", "wenshui wanrong.lu 888"),
                        new Address("china", "beijing", "216001", "wenshui wanrong.lu 666"),
                        new Address("china", "nantong", "216002", "wenshui wanrong.lu 222"),
                        new Address("china", "tianjing", "216600", "wenshui wanrong.lu 999"));

        //---------------------------------------------------------------
        log.debug(JsonUtil.format(list));

        log.debug(formatToSimpleTable(list));
    }
}
