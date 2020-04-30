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
package com.feilong.security.symmetric;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.StringUtil;
import com.feilong.json.JsonUtil;

class LogBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(LogBuilder.class);

    /** Don't let anyone instantiate this class. */
    private LogBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static void logEncrypt(String typeName,String original,String value,String algorithm,String keyString){
        if (LOGGER.isDebugEnabled()){
            Map<String, String> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("keyString", hided(keyString));

            map.put("original", original);

            map.put(typeName, value);
            map.put("valueLength", "" + value.length());
            LOGGER.debug(JsonUtil.format(map));
        }
    }

    static void logDecrypt(String typeName,String needDecryptValue,String original,String algorithm,String keyString){
        if (LOGGER.isDebugEnabled()){
            Map<String, String> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("keyString", hided(keyString));

            map.put(typeName, needDecryptValue);
            map.put("original", original);

            LOGGER.debug(JsonUtil.format(map));
        }
    }

    //---------------------------------------------------------------

    static String errorMessage(String typeName,String value,String algorithm,String keyString,String charsetName){
        Map<String, String> map = newLinkedHashMap();
        map.put("algorithm", algorithm);
        map.put("keyString", hided(keyString));
        map.put(typeName, value);
        map.put("charsetName", charsetName);
        return JsonUtil.format(map);
    }

    private static String hided(String keyString){
        int length = keyString.length();
        if (length <= 3){
            return "*";
        }
        if (length == 4){
            return StringUtil.substring(keyString, 0, 1) + StringUtils.repeat("*", length - 1);
        }
        if (length == 5){
            return StringUtil.substring(keyString, 0, 2) + StringUtils.repeat("*", length - 2);
        }

        return StringUtil.substring(keyString, 0, 1) //
                        + StringUtils.repeat("*", length - 3) //
                        + StringUtil.substringLast(keyString, 2);
    }

}
