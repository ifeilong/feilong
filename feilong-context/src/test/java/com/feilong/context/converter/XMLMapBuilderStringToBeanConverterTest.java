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

import static com.feilong.core.DatePattern.TO_STRING_STYLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.feilong.core.bean.ConvertUtil;

public class XMLMapBuilderStringToBeanConverterTest{

    @Test
    @SuppressWarnings("static-method")
    public void convert(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<item>" + "<categoryId></categoryId>"
                        + "<title><![CDATA[test pro<duct name]]></title>" + "<price>0</price>" + "<listPrice>200000</listPrice>"
                        + "<state>10</state>" + "<city>1010</city>" + "<district>101004</district>" + "<weight>1.2</weight>"
                        + "<length>23</length>" + "<width>56</width>" + "<height>5</height>" + "<buyerObtainPoint>20</buyerObtainPoint>"
                        + "<freightPayer>buyer</freightPayer>" + "<postageId>-1</postageId>" + "<expressFee>5</expressFee>"
                        + "<description>test product</description>" + "<properties>1004:2586,12536:15248,58746:2546</properties>"
                        + "<supportPOD>false</supportPOD>" + "<podFee></podFee>" + "<timing></timing>" + "<listTime></listTime>"
                        + "</item>";

        ConvertUtil.registerSimpleDateLocaleConverter(TO_STRING_STYLE);

        XMLMapBuilderStringToBeanConverter<ItemDto> converter = new XMLMapBuilderStringToBeanConverter<>("/item/*");
        converter.setBeanClass(ItemDto.class);

        ItemDto itemDto = converter.convert(xml);

        assertThat(
                        itemDto,
                        allOf(//
                                        hasProperty("buyerObtainPoint", is(20)),
                                        hasProperty("district", is("101004")),
                                        hasProperty("freightPayer", is("buyer")),
                                        hasProperty("properties", is("1004:2586,12536:15248,58746:2546")),
                                        hasProperty("title", is("test pro<duct name")),
                                        hasProperty("city", is("1010"))

                        ));
    }
}