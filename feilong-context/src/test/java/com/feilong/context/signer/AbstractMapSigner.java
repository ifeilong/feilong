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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.security.symmetric.LogBuilder.hided;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.core.net.ParamUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.security.oneway.OnewayEncryption;
import com.feilong.security.oneway.OnewayType;

/**
 * // 合作方实现接口签名生成算法
 * // 有些接口需要合作方按照规范要求实现，比如专辑/声音上下架推送接口，这些接口的签名生成算法如下：
 * //
 * // (1) 将除了sig以外的所有其他请求参数的原始值按照参数名的字典序排序
 * // (2) 将排序后的参数键值对用&拼接，即拼接成key1=val1&key2=val2这样的形式
 * // (3) 将上一步得到的字符串后拼上app_secret，比如app_secret为abc，那么现在就得到key1=val1&key2=val2&app_secret=abc
 * // (4) 对上一步得到的字符串进行MD5运算得到32位小写字符串，即为sig
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
public abstract class AbstractMapSigner implements MapSigner{

    private static final Logger   LOGGER = LoggerFactory.getLogger(AbstractMapSigner.class);

    /**
     * 加密参数配置
     */
    protected final MapSignConfig mapSignConfig;

    //---------------------------------------------------------------

    /**
     * @param mapSignConfig
     */
    public AbstractMapSigner(MapSignConfig mapSignConfig){
        Validate.notNull(mapSignConfig, "mapSignConfig can't be null!");

        OnewayType onewayType = mapSignConfig.getOnewayType();
        Validate.notNull(onewayType, "onewayType can't be null!");

        this.mapSignConfig = mapSignConfig;
    }

    //---------------------------------------------------------------

    @Override
    public String sign(Map<String, String> map){
        Validate.notEmpty(map, "map can't be null/empty!");

        AtomicInteger counter = new AtomicInteger(0);

        //---------------------------------------------------------------
        Map<String, String> tobeSignMap = handleTobeSignMap(map, counter);

        String signResult = handleSign(tobeSignMap, counter);
        Validate.notBlank(signResult, "signResult can't be blank!,map:%s", map);

        //---------------------------------------------------------------
        //是否转大写
        if (mapSignConfig.getIsResultUpperCase()){
            return signResult.toUpperCase();
        }
        return signResult;
    }

    //---------------------------------------------------------------

    protected String handleSign(Map<String, String> tobeSignMap,AtomicInteger counter){
        //生成 待签名string
        String toBeSignString = createToBeSignString(tobeSignMap, counter);

        // 加签
        return encodeString(toBeSignString, counter);
    }

    //---------------------------------------------------------------
    /**
     * 生成 待签名string
     * 
     * @return
     */
    protected String createToBeSignString(Map<String, String> tobeSignMap,AtomicInteger counter){
        //(1) 将除了sig以外的所有其他请求参数的原始值按照参数名的字典序排序
        //(2) 将排序后的参数键值对用&拼接，即拼接成key1=val1&key2=val2这样的形式
        String naturalOrderingQueryString = ParamUtil.toNaturalOrderingQueryString(tobeSignMap);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(signLog(counter, "[参数排序],结果: {}", naturalOrderingQueryString));
        }

        //---------------------------------------------------------------
        String appendSecretParamName = mapSignConfig.getAppendSecretParamName();
        if (isNullOrEmpty(appendSecretParamName)){
            return naturalOrderingQueryString;
        }

        //---------------------------------------------------------------
        String appendSecretParamValue = mapSignConfig.getAppendSecretParamValue();
        String toBeSignString = formatPattern("{}&{}={}", naturalOrderingQueryString, appendSecretParamName, appendSecretParamValue);
        if (LOGGER.isDebugEnabled()){
            String log = signLog(
                            counter,
                            "待MD5字符串(追加{}参数): {}(安全参数值日志掩码处理)",
                            appendSecretParamName,
                            formatPattern("{}&{}={}", naturalOrderingQueryString, appendSecretParamName, hided(appendSecretParamValue)));
            LOGGER.debug(log);
        }
        return toBeSignString;
    }

    //---------------------------------------------------------------

    /**
     * 加签
     * 
     * @param toBeSignString
     * @param counter
     *            计数器
     * @return
     */
    protected String encodeString(String toBeSignString,AtomicInteger counter){
        OnewayType onewayType = mapSignConfig.getOnewayType();
        String encodeResult = OnewayEncryption.encode(onewayType, toBeSignString, UTF8);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(signLog(counter, "{} 最终结果: {}", onewayType, encodeResult));
        }
        return encodeResult;
    }

    protected String encodeBytes(byte[] hmacSha1,AtomicInteger counter){
        OnewayType onewayType = mapSignConfig.getOnewayType();
        String encodeResult = OnewayEncryption.encode(onewayType, hmacSha1);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(signLog(counter, "{} 最终结果: {}", onewayType, encodeResult));
        }
        return encodeResult;
    }

    protected static String signLog(AtomicInteger counter,String messagePattern,Object...args){
        return formatPattern(
                        "[第 {} 步] {}", //
                        counter.addAndGet(1),
                        formatPattern(messagePattern, args));
    }

    /**
     * 支持从map中排除掉不需要签名的签署
     * 
     * @param map
     * @return
     */
    private Map<String, String> handleTobeSignMap(Map<String, String> map,AtomicInteger counter){
        List<String> noNeedSignParamNameList = mapSignConfig.getNoNeedSignParamNameList();
        if (isNullOrEmpty(noNeedSignParamNameList)){
            return map;
        }
        //---------------------------------------------------------------
        Map<String, String> resultMap = MapUtil.getSubMapExcludeKeys(map, noNeedSignParamNameList);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(signLog(counter, "[处理待签名Map],inputMap:{},去掉不需要参与签名的参数:[{}],结果:[{}]", map, noNeedSignParamNameList, resultMap));
        }
        return resultMap;
    }

}
