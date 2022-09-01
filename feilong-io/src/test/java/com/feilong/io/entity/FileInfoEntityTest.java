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
package com.feilong.io.entity;

import static com.feilong.core.date.DateUtil.getTime;
import static com.feilong.core.date.DateUtil.toDate;
import static com.feilong.io.entity.FileType.FILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import java.util.Date;

import org.junit.Test;

import com.feilong.core.DatePattern;
import com.feilong.json.JsonUtil;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.6
 */
public class FileInfoEntityTest{

    @Test
    public void test(){
        Date now = toDate("2020-06-21 00:00:00", DatePattern.COMMON_DATE_AND_TIME);

        FileInfoEntity fileInfoEntity = new FileInfoEntity("feilongstore_china_cancel20130910.csv", FILE, 25655L, getTime(now));
        String format = JsonUtil.toString(fileInfoEntity);

        assertThat(
                        format,
                        allOf(
                                        containsString("\"formatSize\":\"25.5KB\""), //
                                        containsString("\"fileType\":\"FILE\""),
                                        containsString("\"formatLastModified\":\"06-21 00:00\"")

                        //
                        ));
    }

}
