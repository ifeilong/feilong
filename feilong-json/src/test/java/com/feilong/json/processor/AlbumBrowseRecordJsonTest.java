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
package com.feilong.json.processor;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.nowTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import java.util.Map;

import org.junit.Test;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.AlbumBrowseRecord;
import com.feilong.test.AbstractTest;

public class AlbumBrowseRecordJsonTest extends AbstractTest{

    @Test
    public void test(){
        long nowTime = nowTime();

        AlbumBrowseRecord command = new AlbumBrowseRecord();

        command.setAlbumId(888888899999L);
        command.setBrowsedAt(nowTime);
        command.setTrackId(null);

        //---------------------------------------------------------------

        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();

        Map<String, String> map = toMap("albumId", "album_id");
        map.put("browsedAt", "browse_at");
        javaToJsonConfig.setJsonTargetClassAndPropertyNameProcessorMap(
                        toMap(AlbumBrowseRecord.class, new SimpleMapPropertyNameProcessor(map)));

        //---------------------------------------------------------------

        String json = JsonUtil.toString(command, javaToJsonConfig);
        LOGGER.debug(json);

        assertThat(
                        json, //
                        allOf(//
                                        containsString("\"album_id\":888888899999"),
                                        containsString("\"browse_at\":" + nowTime + ""),
                                        containsString("\"trackId\":null")

                        ));
    }
}
