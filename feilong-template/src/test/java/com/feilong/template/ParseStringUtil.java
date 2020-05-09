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
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.lang.StringUtil;
import com.feilong.template.VelocityUtil;
import com.feilong.test.AbstractTest;

public class ParseStringUtil extends AbstractTest{

    String templateInClassPath = "velocity/test_stringutil.vm";

    @Test
    public void parse(){
        Map<String, Object> map = newHashMap();
        map.put("StringUtil", StringUtil.class);
        map.put("code", "橘黄色/米黄色");

        String parseVMTemplate = VelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader(templateInClassPath, map);
        assertEquals("橘黄色_米黄色", parseVMTemplate);
    }
}
