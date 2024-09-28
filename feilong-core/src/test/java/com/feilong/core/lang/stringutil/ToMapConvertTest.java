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
package com.feilong.core.lang.stringutil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.util.CollectionsUtil.size;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.feilong.context.converter.StringToBeanConverter;
import com.feilong.core.lang.StringUtil;

public class ToMapConvertTest{

    StringToBeanConverter<String> valueStringToBeanConverter = (value) -> {
        return value;
    };

    @Test
    public void toMap(){
        assertEquals(emptyMap(), StringUtil.toMap("", String.class, null));
        assertEquals(emptyMap(), StringUtil.toMap(" ", String.class, null));
        assertEquals(emptyMap(), StringUtil.toMap(null, String.class, null));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void toMap1(){
        StringUtil.toMap("1=2", null, null);
    }

    @Test(expected = NullPointerException.class)
    public void toMap12(){
        StringUtil.toMap("1=2", String.class, (StringToBeanConverter) null);
    }

    //---------------------------------------------------------------

    @Test
    public void toMap123(){
        Map<String, String> map = StringUtil.toMap("1=2", String.class, valueStringToBeanConverter);
        assertThat(map, allOf(hasEntry("1", "2")));
    }

    @Test
    public void toMap1223(){
        Map<String, String> map = StringUtil.toMap("1=2;", String.class, valueStringToBeanConverter);
        assertThat(map, allOf(hasEntry("1", "2")));
    }

    @Test
    public void toMap1333223(){
        Map<Long, Long> map = StringUtil.toMap("1=2;", Long.class, (value) -> {
            return toLong(value);
        });

        assertThat(map, allOf(hasEntry(1L, 2L)));
    }

    @Test
    public void toMap1333223Integer(){
        Map<Integer, Integer> map = StringUtil.toMap("1=2;", Integer.class, (value) -> {
            return toInteger(value);
        });

        assertThat(map, allOf(hasEntry(1, 2)));
    }

    @Test
    public void toMap1333223Integer222(){
        Map<Integer, Integer> map = StringUtil.toMap("1=2;89=100;200=230", Integer.class, (value) -> {
            return toInteger(value);
        });

        assertThat(
                        map,
                        allOf(//
                                        hasEntry(1, 2),
                                        hasEntry(89, 100),
                                        hasEntry(200, 230)));
    }

    @Test
    public void toMap222(){
        String configString = "73034693=0-50;\n"//
                        + "11487680=0-43;\n"//
                        + "51099626=0-50;";

        Map<Long, TrackQueryExtendParam> map = StringUtil.toMap(configString, Long.class, (valueString) -> {
            String[] albumQueryParamsArray = StringUtil.tokenizeToStringArray(valueString, "-");
            if (isNullOrEmpty(albumQueryParamsArray)){
                return null;
            }
            try{
                //只有1个 
                if (1 == size(albumQueryParamsArray)){
                    //默认 0-x
                    Integer max = toInteger(albumQueryParamsArray[0]);
                    return new TrackQueryExtendParam(0, max);
                }

                Integer min = toInteger(albumQueryParamsArray[0]);
                Integer max = toInteger(albumQueryParamsArray[1]);
                return new TrackQueryExtendParam(min, max);

            }catch (Exception e){
                return null;
            }
        });

        assertThat(
                        map,
                        allOf(//
                                        hasEntry(73034693L, new TrackQueryExtendParam(0, 50)),
                                        hasEntry(11487680L, new TrackQueryExtendParam(0, 43)),
                                        hasEntry(51099626L, new TrackQueryExtendParam(0, 50))));
    }
}