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
package com.feilong.taglib.display.pager;

import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG;

import java.util.Locale;

import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;

import static com.feilong.core.CharsetType.UTF8;

/**
 * The Class BasePagerTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public abstract class BasePagerTest{

    // Locale.ENGLISH;
    /** The locale. */
    private final Locale locale            = Locale.SIMPLIFIED_CHINESE;

    /** The debug is not parse vm. */
    boolean              debugIsNotParseVM = false;

    /**
     * Gets the pager params.
     * 
     * @return the pager params
     */
    protected PagerParams getPagerParams(){
        int count = 1024;
        int currentPageNo = -1;
        int pageSize = 10;
        String skin = PagerConstants.DEFAULT_SKIN;
        //		String pageUrl = "http://localhost:8888/pager.htm?a=b&b=c&d=a&name=jinxin";
        String pageUrl = "http://item.blanja.com/items/search?oneNav=1190&priceTo=&twoNav=101706&priceFrom=100&pageNo=1&keyWords=Samsung";
        //		String pageUrl = "http://localhost:8888/pager.htm";

        // pageUrl =
        // "http://www.underarmour.cn/cmens-bottoms-pant/t-b-f-a-c-s-fLoose-p-g-e-i-o.htm?'\"--></style></script><script>netsparker(0x0000E1)</script>=";

        String pageParamName = "pageNo";
        String vmPath = PagerConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

        // LOGGER.debug("===================================================");

        PagerParams pagerParams = new PagerParams(count, pageUrl);
        pagerParams.setCurrentPageNo(currentPageNo);
        pagerParams.setPageSize(pageSize);
        pagerParams.setDynamicNavigationPageNumberConfig(DEFAULT_DYNAMIC_NAVIGATION_PAGE_NUMBER_CONFIG);
        pagerParams.setSkin(skin);
        pagerParams.setPageParamName(pageParamName);
        pagerParams.setVmPath(vmPath);
        pagerParams.setCharsetType(UTF8);

        pagerParams.setDebugIsNotParseVM(debugIsNotParseVM);

        pagerParams.setLocale(locale);
        return pagerParams;
    }
}