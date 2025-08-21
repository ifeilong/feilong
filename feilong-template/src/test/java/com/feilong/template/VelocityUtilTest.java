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
package com.feilong.template;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class VelocityUtilTest extends AbstractTest{

    @Test
    public void parseVMTemplateWithClasspathResourceLoader(){
        // Properties properties = new Properties();
        // //设置模板的路径
        // properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "target/test-classes/scripts");
        //
        List<Long> longs = newArrayList();
        for (int i = 0; i < 10; i++){
            longs.add(toLong(i));
        }

        Map<String, Object> map = newHashMap();
        map.put("memberId", "5");
        map.put("list", longs);
        map.put("channelIds", toList(1L, 2L, 8L, 3L));

        String templateInClassPath = "velocity/test.vm";
        String parseVMTemplate = TemplateUtil.parseTemplate(templateInClassPath, map);
        log.debug(parseVMTemplate);
    }

    @Test
    public void parseVMTemplateWithClasspathResourceLoader122(){
        Map<String, Object> map = newHashMap();
        String templateInClassPath = "velocity/test1.vm";

        String parseVMTemplate = TemplateUtil.parseTemplate(templateInClassPath, map);
        log.debug(parseVMTemplate);
    }

}
