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
package com.feilong.context.converter;

import static com.feilong.core.bean.ConvertUtil.toList;

import java.util.List;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class JsonStringToListConverterTest extends AbstractTest{

    @Test
    public void test(){
        ItemDto itemDto = new ItemDto();
        itemDto.setCity("上海");

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setCity("北京");

        String json = JsonUtil.toString(toList(itemDto, itemDto2));
        JsonStringToListConverter<ItemDto> converter = new JsonStringToListConverter<>(ItemDto.class);

        List<ItemDto> convert = converter.convert(json);

        //---------------------------------------------------------------
        log.info(JsonUtil.format(convert));
        //        assertThat(convert, allOf(hasProperty("city", is("上海"))));
    }
}