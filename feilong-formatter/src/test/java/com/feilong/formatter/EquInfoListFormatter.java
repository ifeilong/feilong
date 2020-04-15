/**
 * Copyright (c) 2017 TianYan All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TianYan.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with TianYan.
 *
 * TianYan MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. TianYan SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.feilong.formatter;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.transformer.NumberToOneHundredMillionTransformer;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class EquInfoListFormatter{

    private static final Logger LOGGER = LoggerFactory.getLogger(EquInfoListFormatter.class);

    public static String format(List<EquInfo> equInfoList){
        BeanFormatterConfig beanFormatterConfig = buildBeanFormatterConfig();

        beanFormatterConfig.setExcludePropertyNames("partyId" //机构内部ID
                        , "tshEquity" //所有者权益合计
                        , "equTypeCd" //股票分类编码，A或B
                        , "exchangeCd" //交易市场
                        , "exOuntryCd" //交易市场所属地区
                        , "listSectorCd" //上市板块编码
                        , "endDate" //财务报告日期
                        , "createTime" //创建时间
                        , "updateTime" //修改时间
        );

        //        beanFormatterConfig.setSorts("ticker", "secShortName", "listStatusCd", "exchangeCd");

        return FormatterUtil.formatToSimpleTable(equInfoList, beanFormatterConfig);
    }

    private static BeanFormatterConfig buildBeanFormatterConfig(){
        Map<String, Transformer<Object, String>> propertyNameAndConverterMap = newHashMap(5);

        //                        toMap("createTime", dateConverter, "endDate", dateConverter);
        //        propertyNameAndConverterMap.put("listDate", dateConverter);
        //        propertyNameAndConverterMap.put("updateTime", dateConverter);

        Transformer instance = NumberToOneHundredMillionTransformer.INSTANCE;
        propertyNameAndConverterMap.put("nonrestFloatShares", instance);//公司无限售流通股份合计(最新)
        propertyNameAndConverterMap.put("nonrestfloatA", instance);//无限售流通股本(最新)。如果为A股，该列为最新无限售流通A股股本数量；如果为B股，该列为最新流通B股股本数量
        propertyNameAndConverterMap.put("totalShares", instance);//总股本(最新)

        BeanFormatterConfig beanFormatterConfig = new BeanFormatterConfig();
        beanFormatterConfig.setPropertyNameAndTransformerMap(propertyNameAndConverterMap);
        return beanFormatterConfig;
    }
}
