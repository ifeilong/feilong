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

import com.feilong.core.lang.StringUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("squid:S1192") //String literals should not be duplicated
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogBuilder{

    static void logEncrypt(String typeName,String original,String value,String algorithm,String keyString){
        if (log.isDebugEnabled()){
            Map<String, String> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("keyString", hided(keyString));

            map.put("original", original);

            map.put(typeName, value);
            map.put("valueLength", "" + value.length());
            log.debug(JsonUtil.toString(map));
        }
    }

    static void logDecrypt(String typeName,String needDecryptValue,String original,String algorithm,String keyString){
        if (log.isDebugEnabled()){
            Map<String, String> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("keyString", hided(keyString));

            map.put(typeName, needDecryptValue);
            map.put("original", original);

            log.debug(JsonUtil.toString(map));
        }
    }

    //---------------------------------------------------------------

    static String errorMessage(String typeName,String value,String algorithm,String keyString,String charsetName){
        Map<String, String> map = newLinkedHashMap();
        map.put("algorithm", algorithm);
        map.put("keyString", hided(keyString));
        map.put(typeName, value);
        map.put("charsetName", charsetName);
        return JsonUtil.toString(map);
    }

    public static String hided(String keyString){
        if (null == keyString){
            return null;
        }
        //---------------------------------------------------------------
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
