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
package com.feilong.context.signer;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
public class DefaultMapSignerTest{

    private static final Logger       LOGGER = LoggerFactory.getLogger(DefaultMapSignerTest.class);

    private final Map<String, String> map    = toMap("name", "feilong", "age", "18");

    {
        map.put("_address", "江苏省通州市十六组1510号");
    }

    //---------------------------------------------------------------

    @Test
    public void test(){
        MapSigner mapSigner = new DefaultMapSigner(new MapSignConfig());
        LOGGER.info(mapSigner.sign(map));
    }

    @Test
    public void testsign(){
        MapSignConfig mapSignConfig = new MapSignConfig();
        mapSignConfig.setNoNeedSignParamNameList(toList("sign"));
        MapSigner mapSigner = new DefaultMapSigner(mapSignConfig);

        map.put("sign", "b0fb3a15d87f9e917b26980ecaec3264");
        LOGGER.info(mapSigner.sign(map));
    }

    @Test
    public void testUpperCase(){
        MapSignConfig mapSignConfig = new MapSignConfig();
        mapSignConfig.setIsResultUpperCase(true);
        MapSigner mapSigner = new DefaultMapSigner(mapSignConfig);
        LOGGER.info(mapSigner.sign(map));
    }

    @Test
    public void test2(){
        MapSignConfig mapSignConfig = new MapSignConfig();
        mapSignConfig.setAppendSecretParamName("key");
        mapSignConfig.setAppendSecretParamValue("88776655aa");
        MapSigner mapSigner = new DefaultMapSigner(mapSignConfig);
        LOGGER.info(mapSigner.sign(map));
    }

    @Test
    public void test23(){
        MapSignConfig mapSignConfig = new MapSignConfig("", "88776655aa");
        MapSigner mapSigner = new DefaultMapSigner(mapSignConfig);
        LOGGER.info(mapSigner.sign(map));
    }

    @Test
    public void test2322(){
        MapSigner mapSigner = new DefaultMapSigner(new MapSignConfig(null, "88776655aa"));
        LOGGER.info(mapSigner.sign(map));
    }

}
