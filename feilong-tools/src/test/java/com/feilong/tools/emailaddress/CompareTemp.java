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
package com.feilong.tools.emailaddress;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.bean.ConvertUtil.toProperties;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.feilong.core.util.MapUtil;
import com.feilong.core.util.SortUtil;
import com.feilong.io.FileUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.SystemUtils;
import com.feilong.test.AbstractTest;

public class CompareTemp extends AbstractTest{

    private static Map<String, String> EMAIL_ADDRESS_MAP = toMap(getResourceBundle("email-address"));

    private static Map<String, String> ERROR_MAP         = toMap(getResourceBundle("error"));

    @Test
    public void testCompareTest() throws IOException{
        MapUtil.removeKeys(EMAIL_ADDRESS_MAP, toArray(ERROR_MAP.keySet(), String.class));

        String[] array = toArray(EmailAddressUtil.EMAIL_PROVIDER_MAP.keySet(), String.class);
        Map<String, String> subMapExcludeKeys = MapUtil.getSubMapExcludeKeys(EMAIL_ADDRESS_MAP, array);

        Map<String, Integer> map3 = toMap(subMapExcludeKeys, String.class, Integer.class);
        LOGGER.debug("{},size:[{}]", JsonUtil.format(map3), map3.size());

        Properties properties = toProperties(toMap(map3, String.class));
        properties.store(FileUtil.getFileOutputStream(SystemUtils.USER_HOME + "/work/a.properties"), "");

        Map<String, Integer> sortMapByValueDesc = SortUtil.sortMapByValueDesc(map3);
        LOGGER.debug("{},size:[{}]", JsonUtil.format(sortMapByValueDesc), sortMapByValueDesc.size());
    }
}
