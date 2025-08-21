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
package com.feilong.context.codecreator;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.lib.lang3.StringUtils.defaultString;

import java.util.Date;

import com.feilong.core.date.DateUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.core.util.RandomUtil;
import com.feilong.json.JsonUtil;

/**
 * 订单编号生成器(要确保每个订单不编码不重复).
 * 
 * <p>
 * 不能100%的保证订单号唯一,各个商城按照需求 定制retry机制.
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.9
 */
@lombok.extern.slf4j.Slf4j
public class SimpleMultiSellerOrderCodeCreator implements MultiSellerOrderCodeCreator{

    /** The simple multi seller order code creator config. */
    private SimpleMultiSellerOrderCodeCreatorConfig simpleMultiSellerOrderCodeCreatorConfig;

    //---------------------------------------------------------------

    /**
     * 创建订单号.
     * 
     * @param buyerId
     *            买家id
     * @param sellerId
     *            卖家id
     * @return 订单号
     */
    @Override
    public String create(Long buyerId,Long sellerId){
        return generatorCode(now(), buyerId, sellerId, simpleMultiSellerOrderCodeCreatorConfig);
    }

    //---------------------------------------------------------------

    /**
     * 底层生成code 方法.
     *
     * @param specifiedDate
     *            the date
     * @param buyerId
     *            买家id
     * @param sellerId
     *            卖家id
     * @param simpleMultiSellerOrderCodeCreatorConfig
     *            the simple multi seller order code creator config
     * @return the string
     */
    private static String generatorCode(
                    Date specifiedDate,
                    Long buyerId,
                    Long sellerId,
                    SimpleMultiSellerOrderCodeCreatorConfig simpleMultiSellerOrderCodeCreatorConfig){
        String prefix = simpleMultiSellerOrderCodeCreatorConfig.getPrefix();
        int buyerIdLastLength = simpleMultiSellerOrderCodeCreatorConfig.getBuyerIdLastLength();
        int shopIdLastLength = simpleMultiSellerOrderCodeCreatorConfig.getShopIdLastLength();

        String debugSeparator = CodeCreatorHelper.debugSeparator(simpleMultiSellerOrderCodeCreatorConfig.getIsDebug());

        //---------------------------------------------------------------

        // 时间戳
        String yy = DateUtil.toString(specifiedDate, "yy");
        String hourOfYear = StringUtil.format("%04d", DateUtil.getHourOfYear(specifiedDate));
        String mmss = DateUtil.toString(specifiedDate, "mmss");

        //随机数
        String randomString = "" + RandomUtil.createRandomWithLength(simpleMultiSellerOrderCodeCreatorConfig.getRandomNumberLength());

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();

        sb.append(defaultString(prefix));
        sb.append(debugSeparator + yy);// 2
        sb.append(debugSeparator + CodeCreatorHelper.formatLastValue(sellerId, shopIdLastLength));// shopIdLastLength
        sb.append(debugSeparator + hourOfYear);// 4
        sb.append(debugSeparator + CodeCreatorHelper.formatLastValue(buyerId, buyerIdLastLength));// buyerIdLastLength
        sb.append(debugSeparator + mmss);// 4
        sb.append(debugSeparator + randomString);// randomNumberLength

        //---------------------------------------------------------------
        String result = sb.toString() + CodeCreatorHelper.debugLength(sb, simpleMultiSellerOrderCodeCreatorConfig.getIsDebug());
        if (log.isTraceEnabled()){
            log.trace(
                            "[{}]-->[{}],buyerId:[{}],sellerId:[{}],simpleMultiSellerOrderCodeCreatorConfig:[{}] ",
                            DateUtil.toString(specifiedDate, COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                            result,
                            buyerId,
                            sellerId,
                            JsonUtil.toString(simpleMultiSellerOrderCodeCreatorConfig));
        }
        return result;

    }

    //---------------------------------------------------------------

    /**
     * Sets the simple multi seller order code creator config.
     *
     * @param simpleMultiSellerOrderCodeCreatorConfig
     *            the simpleMultiSellerOrderCodeCreatorConfig to set
     */
    public void setSimpleMultiSellerOrderCodeCreatorConfig(SimpleMultiSellerOrderCodeCreatorConfig simpleMultiSellerOrderCodeCreatorConfig){
        this.simpleMultiSellerOrderCodeCreatorConfig = simpleMultiSellerOrderCodeCreatorConfig;
    }

}
