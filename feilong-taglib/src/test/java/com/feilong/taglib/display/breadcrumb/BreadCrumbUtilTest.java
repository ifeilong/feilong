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
package com.feilong.taglib.display.breadcrumb;

import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.SortUtil.sortList;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbEntity;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbParams;
import com.feilong.taglib.display.breadcrumb.comparator.BreadCrumbEntityComparator;

/**
 * The Class SiteMapTagTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class BreadCrumbUtilTest{

    /** The Constant LOGGER. */
    private static final Logger                   LOGGER               = LoggerFactory.getLogger(BreadCrumbUtilTest.class);

    /** The site map entities. */
    private static List<BreadCrumbEntity<Number>> breadCrumbEntityList = newArrayList();

    static{
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(1, "Root1", "Root1_title", "/test1.htm", 0));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(2, "Root2", "Root2_title", "/test2.htm", 1));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(3, "Root3", "Root3_title", "/test3.htm", 1));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(4, "Root4", "Root4_title", "/test4.htm", 2));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(5, "Root5", "Root5_title", "/test5.htm", 2));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(6, "Root6", "Root6_title", "/test6.htm", 4));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(7, "Root7", "Root7_title", "/test7.htm", 6));
        breadCrumbEntityList.add(new BreadCrumbEntity<Number>(8, "Root8", "Root8_title", "/test8.htm", 7));
    }

    //---------------------------------------------------------------
    /**
     * TestBreadCrumbUtilTest.
     */
    @Test
    public void testBreadCrumbUtilTest(){
        List<BreadCrumbEntity<Number>> list = newArrayList();

        list.add(new BreadCrumbEntity<Number>(2, "Root2", "Root2_title", "/test2.htm", 99));
        list.add(new BreadCrumbEntity<Number>(8, "Root8", "Root8_title", "/test8.htm", 6));
        list.add(new BreadCrumbEntity<Number>(99, "Root1", "Root1_title", "/test1.htm", 100));
        list.add(new BreadCrumbEntity<Number>(6, "Root6", "Root6_title", "/test6.htm", 2));

        //TreeMap<Number, BreadCrumbEntity> treeMap = new TreeMap<>();

        sortList(list, new BreadCrumbEntityComparator());

        LOGGER.debug(JsonUtil.format(list));
    }

    /**
     * Test get bread crumb content.
     */
    @Test
    public void testGetBreadCrumbContent(){
        List<BreadCrumbEntity<Number>> list = newArrayList();
        list.add(new BreadCrumbEntity<Number>(1, "Root1", "Root1_title", "/test1.htm", 0));
        list.add(new BreadCrumbEntity<Number>(3, "Root3", "Root3_title", "/test3.htm", 1));
        list.add(new BreadCrumbEntity<Number>(4, "Root4", "Root4_title", "/test4.htm", 2));
        list.add(new BreadCrumbEntity<Number>(6, "Root6", "Root6_title", "/test6.htm", 4));
        list.add(new BreadCrumbEntity<Number>(7, "Root7", "Root7_title", "/test7.htm", 6));
        list.add(new BreadCrumbEntity<Number>(8, "Root8", "Root8_title", "/test8.htm", 7));

        BreadCrumbParams<Number> breadCrumbParams = new BreadCrumbParams<Number>();
        breadCrumbParams.setBreadCrumbEntityList(list);
        breadCrumbParams.setUrlPrefix("http://www.esprit.cn");

        //        breadCrumbParams.setConnector(breadCrumbEntityList);
        //        breadCrumbParams.setCurrentPath("/test8.htm");
        //        breadCrumbParams.setVmPath(null);

        LOGGER.debug(BreadCrumbUtil.getBreadCrumbContent(breadCrumbParams));
    }

    /**
     * Gets the all parent site map entity list.
     * 
     */
    @Test
    public void testGetAllParentSiteMapEntityList(){
        //        String path = "/test8.htm";
        //        BreadCrumbTag siteMapTag = new BreadCrumbTag();
        //        List<BreadCrumbEntity<Number>> allParentSiteMapEntityList = siteMapTag.getAllParentSiteMapEntityList(path, siteMapEntities);
        //        LOGGER.debug("show");
        //        if (null != allParentSiteMapEntityList){
        //            for (BreadCrumbEntity<Number> sme : allParentSiteMapEntityList){
        //                LOGGER.debug(sme.getName());
        //            }
        //        }else{
        //            LOGGER.debug("allParentSiteMapEntityList is null/empty");
        //        }
    }

    /**
     * Gets the site map entity by path.
     * 
     */
    @Test
    public void testGetSiteMapEntityByPath(){
        //        String path = "/test8.htm";
        //        BreadCrumbTag siteMapTag = new BreadCrumbTag();
        //        BreadCrumbEntity<Number> siteMapEntity = siteMapTag.getSiteMapEntityByPath(path, siteMapEntities);
        //        LOGGER.debug(siteMapEntity.getParentId() + "");
        //        LOGGER.debug(siteMapEntity.getName());
    }

    /**
     * Parses the.
     */
    @Test
    public void parse(){
        //        String path = "/test8.htm";
        //        BreadCrumbTag siteMapTag = new BreadCrumbTag();
        //        List<BreadCrumbEntity<Number>> allParentSiteMapEntityList = siteMapTag.getAllParentSiteMapEntityList(path, siteMapEntities);
        //        Map<String, Object> contextKeyValues =newHashMap();
        //        contextKeyValues.put("siteMapEntityList", allParentSiteMapEntityList);
        //        contextKeyValues.put("connector", ">");
        //        String siteMapString = new VelocityUtil().parseTemplateWithClasspathResourceLoader("velocity/sitemap.vm", contextKeyValues);
        //        LOGGER.debug(siteMapString);
    }
}
