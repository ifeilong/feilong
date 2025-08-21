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

import com.feilong.test.AbstractTest;

@SuppressWarnings({
                    "squid:S2699", //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
                    "squid:S2187"//TestCases should contain tests
})
@Deprecated
public class ToolVelocityUtilTest extends AbstractTest{

    String templateInClassPath = "velocity/test_toolbox.vm";

    //    @Test
    //    public void testTool(){
    //        Map<String, Object> map = newHashMap();
    //        map.put("code", "SH123456");
    //        map.put("courseDate", "2015-06-02");
    //        map.put("courseTime", "14:00");
    //        map.put("DateUtil", DateUtil.class);
    //
    //        //        String toolboxPath = "/lib/toolbox.xml";
    //        //String toolboxPath = ConfigurationUtils.GENERIC_DEFAULTS_PATH;
    //
    //        String parseVMTemplate = ToolVelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader(templateInClassPath, map);
    //        log.debug(parseVMTemplate);
    //    }
}
