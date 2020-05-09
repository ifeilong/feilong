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

import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.feilong.template.VelocityUtil;
import com.feilong.test.AbstractTest;

public class TestFalse extends AbstractTest{

    String templateInClassPath = "velocity/test_false.vm";

    @Test
    public void test(){
        Map<String, Object> map = newHashMap();
        map.put("effective", false);

        String parseVMTemplate = VelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader(templateInClassPath, map);
        assertTrue(parseVMTemplate.contains(":effective false 进"));
    }
}
