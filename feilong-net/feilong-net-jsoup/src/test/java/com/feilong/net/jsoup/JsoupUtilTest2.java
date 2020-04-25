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
package com.feilong.net.jsoup;

import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.test.AbstractTest;

/**
 * The Class JsoupUtilTest.
 */
public class JsoupUtilTest2 extends AbstractTest{

    @Test
    public void getDocument1() throws JsoupUtilException{
        String urlString = "http://data.10jqka.com.cn/financial/yjgg/date/2017-03-31/ajax/1/";
        Elements elements = JsoupUtil.getElementsBySelect(urlString, "tbody tr");

        List<Map<String, Object>> list = newArrayList();

        for (Element trElement : elements){
            list.add(buildTdValueMap(trElement));
        }

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(list));
        }

    }

    /**
     * @param tdElements
     * @param i
     * @return
     * @since 1.10.4
     */
    private Map<String, Object> buildTdValueMap(Element element){
        LOGGER.debug("" + element);

        Elements tdElements = element.select("td");

        Map<String, Object> map = newLinkedHashMap();
        int i = 0;
        for (Element tdElement : tdElements){
            map.put("code" + i, tdElement.text());
            i++;
        }
        return map;
    }

}