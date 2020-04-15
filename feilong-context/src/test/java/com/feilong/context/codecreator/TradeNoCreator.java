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

import com.feilong.core.lang.StringUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class TradeNoCreator{

    /**
     * 创建支付交易号码.
     * 
     * @param tradeId
     *            所属交易
     * @param countTradeNo
     *            数据库中这笔交易存在的交易号码数量
     * @return 需要创建的交易号码
     */
    public static String createTradeNo(Long tradeId,Integer countTradeNo){
        Long a = 1000000 + tradeId;
        Integer b = countTradeNo + 1;
        return StringUtil.format("%08d", a) + "" + StringUtil.format("%04d", b);
    }
}
