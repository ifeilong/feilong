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

import java.util.Map;

import org.junit.Test;

import com.feilong.test.AbstractTest;

public class MacroTest extends AbstractTest{

    String templateInClassPath = "velocity/test_macro.vm";

    @Test
    public void parseVMTemplateWithClasspathResourceLoader(){
        // Properties properties = new Properties();
        // //设置模板的路径
        // properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "target/test-classes/scripts");

        Map<String, Object> contextKeyValues = null;
        String parseVMTemplate = TemplateUtil.parseTemplate(templateInClassPath, contextKeyValues);
        LOGGER.debug(parseVMTemplate);
    }
}
