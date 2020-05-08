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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.test.AbstractTest;

/**
 * The Class ItemDtoJsonConverterTest.
 */
public class ItemDtoJsonStringToBeanConverterTest extends AbstractTest{

    /**
     * Convert.
     */
    @Test
    public void test(){
        ItemDto itemDto = new ItemDto();
        itemDto.setCity("上海");

        String json = JsonUtil.format(itemDto);
        StringToBeanConverter<ItemDto> converter = new JsonStringToBeanConverter<>(ItemDto.class);

        ItemDto convert = converter.convert(json);

        assertThat(convert, allOf(hasProperty("city", is("上海"))));
    }
}