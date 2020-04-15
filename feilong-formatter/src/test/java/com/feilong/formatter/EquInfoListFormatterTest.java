package com.feilong.formatter;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.now;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.AggregateUtil;
import com.feilong.formatter.transformer.MoneyTransformer;
import com.feilong.json.jsonlib.JsonUtil;

public class EquInfoListFormatterTest{

    private final static Logger LOGGER = LoggerFactory.getLogger(EquInfoListFormatterTest.class);

    @Test
    public void testFindAllEquInfo(){
        List<EquInfo> equInfoList = buildEquInfoList();

        //---------------------------------------------------------------

        LOGGER.debug("equInfoList size:[{}],info:[{}]", equInfoList.size(), EquInfoListFormatter.format(equInfoList));

        Transformer<?, ?> transformer = MoneyTransformer.INSTANCE;
        Map<String, Transformer<Object, Object>> propertyValueAndTransformerMap = toMap(
                        "nonrestFloatShares",
                        (Transformer<Object, Object>) transformer,
                        "nonrestfloatA",
                        (Transformer<Object, Object>) transformer);
        propertyValueAndTransformerMap.put("totalShares", (Transformer<Object, Object>) transformer);

        //---------------------------------------------------------------

        Map<String, Map<Object, Integer>> resultGroupCount = AggregateUtil.groupCount(
                        equInfoList,
                        toArray("listStatusCd", "listSector", "nonrestFloatShares", "nonrestfloatA", "totalShares"),
                        propertyValueAndTransformerMap);

        LOGGER.debug("map size:[{}],info:[{}]", resultGroupCount.size(), JsonUtil.format(resultGroupCount));
    }

    //---------------------------------------------------------------

    private List<EquInfo> buildEquInfoList(){
        EquInfo equInfo = new EquInfo();

        Date date = now();
        equInfo.setCreateTime(date);
        equInfo.setDelistDate(null);
        equInfo.setEndDate(date);
        equInfo.setEquTypeCd("A");
        equInfo.setExchangeCd("A");
        equInfo.setExOuntryCd("");
        equInfo.setId(1049L);
        equInfo.setListDate(date);
        equInfo.setListSector("主板");
        equInfo.setListSectorCd(1);
        equInfo.setListStatusCd("L");
        equInfo.setNonrestfloatA(967400000d);
        equInfo.setNonrestFloatShares(967400000d);
        equInfo.setPartyId("488");
        equInfo.setSecShortName("佛塑科技");
        equInfo.setTicker("000973");
        equInfo.setTotalShares(967423200d);
        equInfo.setTshEquity(2407771296.75);
        equInfo.setUpdateTime(date);

        //---------------------------------------------------------------
        EquInfo equInfo1 = new EquInfo();

        equInfo1.setCreateTime(date);
        equInfo1.setDelistDate(null);
        equInfo1.setEndDate(date);
        equInfo1.setEquTypeCd("A");
        equInfo1.setExchangeCd("A");
        equInfo1.setExOuntryCd("");
        equInfo1.setId(1049L);
        equInfo1.setListDate(date);
        equInfo1.setListSector("主板");
        equInfo1.setListSectorCd(1);
        equInfo1.setListStatusCd("S");
        equInfo1.setNonrestfloatA(94000000d);
        equInfo1.setNonrestFloatShares(96740000d);
        equInfo1.setPartyId("488");
        equInfo1.setSecShortName("佛塑科技");
        equInfo1.setTicker("000973");
        equInfo1.setTotalShares(96740200d);
        equInfo1.setTshEquity(240770296.75);
        equInfo1.setUpdateTime(date);

        return toList(equInfo, equInfo1);
    }

}
