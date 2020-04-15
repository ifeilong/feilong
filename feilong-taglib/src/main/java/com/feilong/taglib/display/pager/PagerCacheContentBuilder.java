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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;
import static com.feilong.core.util.SortUtil.sortMapByKeyDesc;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_NAVIGATION_PAGE_NUMBER;
import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;
import static com.feilong.taglib.display.pager.command.PagerConstants.I18N_FEILONG_PAGER;
import static com.feilong.taglib.display.pager.command.PagerConstants.VM_KEY_I18NMAP;
import static com.feilong.taglib.display.pager.command.PagerConstants.VM_KEY_PAGERVMPARAM;
import static com.feilong.taglib.display.pager.command.PagerType.NO_REDIRECT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.ParamUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.display.CacheContentBuilder;
import com.feilong.taglib.display.pager.command.Pager;
import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;
import com.feilong.taglib.display.pager.command.PagerType;
import com.feilong.taglib.display.pager.command.PagerUrlTemplate;
import com.feilong.taglib.display.pager.command.PagerVMParam;
import com.feilong.velocity.VelocityUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 */
public class PagerCacheContentBuilder implements CacheContentBuilder<PagerParams, String>{

    private static final Logger                                  LOGGER   = LoggerFactory.getLogger(PagerCacheContentBuilder.class);

    /** Static instance. */
    // the static instance works for all types
    public static final CacheContentBuilder<PagerParams, String> INSTANCE = new PagerCacheContentBuilder();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.display.CacheContentBuilder#build(com.feilong.taglib.display.CacheParam)
     */
    @Override
    public String build(PagerParams pagerParams){
        if (pagerParams.getDebugIsNotParseVM()){
            LOGGER.debug("param [debugIsNotParseVM] is [true],return empty~");
            return EMPTY;
        }

        //-----------------------设置变量参数----------------------------------------

        Map<String, Object> vmParamMap = newHashMap();
        vmParamMap.put(VM_KEY_PAGERVMPARAM, buildPagerVMParam(pagerParams));
        vmParamMap.put(VM_KEY_I18NMAP, toMap(getResourceBundle(I18N_FEILONG_PAGER, pagerParams.getLocale())));

        String content = VelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader(pagerParams.getVmPath(), vmParamMap);

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("parse:[{}],use vmParamMap:{},content result:{}", pagerParams.getVmPath(), JsonUtil.format(vmParamMap), content);
        }
        return content;
    }

    //---------------------------------------------------------------

    /**
     * Builds the pager vm param.
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @return the pager vm param
     */
    private static <T> PagerVMParam buildPagerVMParam(PagerParams pagerParams){
        Pager<T> pager = buildPager(pagerParams);

        int allPageNo = pager.getAllPageNo();
        int currentPageNo = pager.getCurrentPageNo();
        // 最多显示多少个导航页码
        Integer maxNavigationPageNumbers = buildMaxNavigationPageNumbers(currentPageNo, pagerParams.getDynamicNavigationPageNumberConfig());

        //---------------------------------------------------------------
        Pair<Integer, Integer> startAndEndIndexPair = buildStartAndEndIndexPair(allPageNo, currentPageNo, maxNavigationPageNumbers); //获得开始和结束的索引

        //---------------------------------------------------------------
        // 获得所有页码的连接.
        Map<Integer, String> allUseIndexAndHrefMap = buildAllUseIndexAndHrefMap(pagerParams, pager, startAndEndIndexPair);

        //---------------------------------------------------------------
        int prePageNo = pager.getPrePageNo();
        int nextPageNo = pager.getNextPageNo();

        //---------------------------------------------------------------
        PagerVMParam pagerVMParam = new PagerVMParam();
        pagerVMParam.setSkin(pagerParams.getSkin());// 皮肤
        pagerVMParam.setPagerType(pagerParams.getPagerType());//分页类型

        //---------------------------------------------------------------
        pagerVMParam.setTotalCount(pagerParams.getTotalCount());// 总行数,总结果数

        pagerVMParam.setCurrentPageNo(currentPageNo);// 当前页
        pagerVMParam.setAllPageNo(allPageNo);// 总页数
        pagerVMParam.setPrePageNo(prePageNo);
        pagerVMParam.setNextPageNo(nextPageNo);

        pagerVMParam.setStartIteratorIndex(startAndEndIndexPair.getLeft());// 导航开始页码
        pagerVMParam.setEndIteratorIndex(startAndEndIndexPair.getRight());// 导航结束页码

        //---------------------------------------------------------------
        pagerVMParam.setPreUrl(allUseIndexAndHrefMap.get(prePageNo)); // 上一页链接
        pagerVMParam.setNextUrl(allUseIndexAndHrefMap.get(nextPageNo));// 下一页链接
        pagerVMParam.setFirstUrl(allUseIndexAndHrefMap.get(1));// firstPageNo 第一页的链接
        pagerVMParam.setLastUrl(allUseIndexAndHrefMap.get(pager.getAllPageNo()));//lastPageNo 最后一页的链接
        //---------------------------------------------------------------

        pagerVMParam.setPagerUrlTemplate(buildPagerUrlTemplate(allUseIndexAndHrefMap));
        pagerVMParam.setPageParamName(pagerParams.getPageParamName());
        pagerVMParam.setIteratorIndexMap(
                        getIteratorIndexAndHrefMap(allUseIndexAndHrefMap, startAndEndIndexPair.getLeft(), startAndEndIndexPair.getRight()));
        return pagerVMParam;
    }

    //---------------------------------------------------------------

    /**
     * Builds the pager.
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @return the pager
     */
    static <T> Pager<T> buildPager(PagerParams pagerParams){
        int totalCount = pagerParams.getTotalCount();
        int currentPageNo = detectCurrentPageNo(pagerParams);
        int pageSize = pagerParams.getPageSize();

        Pager<T> pager = new Pager<>(currentPageNo, pageSize, totalCount);
        pager.setMaxShowPageNo(pagerParams.getMaxShowPageNo());
        return pager;
    }

    /**
     * Builds the pager url template.
     *
     * @param indexAndHrefMap
     *            the index and href map
     * @return the pager url template
     * @since 1.4.0
     */
    private static PagerUrlTemplate buildPagerUrlTemplate(Map<Integer, String> indexAndHrefMap){
        Integer defaultTemplatePageNo = PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;

        PagerUrlTemplate pagerUrlTemplate = new PagerUrlTemplate();
        pagerUrlTemplate.setTemplateValue(defaultTemplatePageNo);
        pagerUrlTemplate.setHref(indexAndHrefMap.get(defaultTemplatePageNo));// 模板链接
        return pagerUrlTemplate;
    }

    //---------------------------------------------------------------

    /**
     * 要循环的 index和 end 索引 =href map.
     *
     * @param indexAndHrefMap
     *            the index and href map
     * @param startIteratorIndex
     *            开始迭代索引编号
     * @param endIteratorIndex
     *            结束迭代索引编号
     * @return the iterator index and href map
     */
    private static Map<Integer, String> getIteratorIndexAndHrefMap(
                    Map<Integer, String> indexAndHrefMap,
                    int startIteratorIndex,
                    int endIteratorIndex){
        Map<Integer, String> map = newLinkedHashMap(endIteratorIndex - startIteratorIndex + 1);
        for (int i = startIteratorIndex; i <= endIteratorIndex; ++i){
            map.put(i, indexAndHrefMap.get(i));
        }
        return map;
    }

    /**
     * 获得当前的页码.
     * 
     * <p>
     * 对于 {@code <} 1的情况做 返回1特殊处理.
     * </p>
     * 
     * @param pagerParams
     *            the pager params
     * @return 如果 {@link PagerParams#getCurrentPageNo()} {@code <} 1, 返回 1 <br>
     *         否则返回 {@link PagerParams#getCurrentPageNo()}
     */
    private static int detectCurrentPageNo(PagerParams pagerParams){
        Integer currentPageNo = pagerParams.getCurrentPageNo();
        if (null == currentPageNo || currentPageNo < 1){
            return 1;// 解决可能出现界面上负数的情况
        }
        return currentPageNo;
    }

    /**
     * 获得所有页码的连接.
     * 
     * <p>
     * 注:(key={@link #DEFAULT_TEMPLATE_PAGE_NO} 为模板链接,可用户前端解析 {@link PagerVMParam#getHrefUrlTemplate()}.
     * </p>
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @param pager
     *            the pager
     * @param startAndEndIndexPair
     *            the start and end index pair
     * @return key是分页页码,value是解析之后的链接
     * @since 1.4.0
     */
    private static <T> Map<Integer, String> buildAllUseIndexAndHrefMap(
                    PagerParams pagerParams,
                    Pager<T> pager,
                    Pair<Integer, Integer> startAndEndIndexPair){
        String pageParamName = pagerParams.getPageParamName();
        PagerType pagerType = pagerParams.getPagerType();

        //*********************这种替换的性能要高***********************************************
        CharSequence targetForReplace = pageParamName + "=" + DEFAULT_TEMPLATE_PAGE_NO;

        String templateEncodedUrl = getTemplateEncodedUrl(pagerParams, pageParamName, pagerType);
        //---------------------------------------------------------------
        Set<Integer> indexSet = buildAllUseIndexSet(pager, startAndEndIndexPair.getLeft(), startAndEndIndexPair.getRight());
        Map<Integer, String> returnMap = newHashMap();
        for (Integer index : indexSet){
            String link = pagerType == NO_REDIRECT ? templateEncodedUrl
                            : templateEncodedUrl.replace(targetForReplace, pageParamName + "=" + index);
            returnMap.put(index, link);
        }
        return returnMap;
    }

    /**
     * Gets the template encoded url.
     *
     * @param pagerParams
     *            the pager params
     * @param pageParamName
     *            the page param name
     * @param pagerType
     *            the pager type
     * @return the template encoded url
     * @since 1.6.1
     */
    private static String getTemplateEncodedUrl(PagerParams pagerParams,String pageParamName,PagerType pagerType){
        boolean isNoRedirect = NO_REDIRECT == pagerType;
        if (isNoRedirect){
            return "javascript:void(0);";//ajaxLink
        }
        String defaultTemplatePageNo = "" + PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;
        return ParamUtil.addParameter(pagerParams.getPageUrl(), pageParamName, defaultTemplatePageNo, pagerParams.getCharsetType());
    }

    /**
     * 获得所有需要使用的链接的索引号.
     * 
     * <h3>包含:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>{@link PagerConstants#DEFAULT_TEMPLATE_PAGE_NO}</li>
     * <li><code>prePageNo</code></li>
     * <li><code>nextPageNo</code></li>
     * <li><code>firstPageNo</code></li>
     * <li><code>lastPageNo</code></li>
     * <li>以及迭代的 <code>startIteratorIndexAndEndIteratorIndexs</code></li>
     * </ul>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param pager
     *            the pager
     * @param startIteratorIndex
     *            开始迭代索引编号
     * @param endIteratorIndex
     *            结束迭代索引编号
     * @return the index set
     */
    private static <T> Set<Integer> buildAllUseIndexSet(Pager<T> pager,int startIteratorIndex,int endIteratorIndex){
        Set<Integer> indexSet = new HashSet<>();// 所有需要生成url 的 index值
        indexSet.add(PagerConstants.DEFAULT_TEMPLATE_PAGE_NO);// 模板链接 用于前端操作
        indexSet.add(pager.getPrePageNo());//prePageNo
        indexSet.add(pager.getNextPageNo());//nextPageNo
        indexSet.add(1);//firstPageNo
        indexSet.add(pager.getAllPageNo());//lastPageNo

        for (int i = startIteratorIndex; i <= endIteratorIndex; ++i){
            indexSet.add(i);
        }
        return indexSet;
    }

    //---------------------------------------------------------------

    /**
     * 获得开始和结束的索引.
     *
     * @param allPageNo
     *            总页码
     * @param currentPageNo
     *            当前页面
     * @param maxIndexPages
     *            最大显示页码数量
     * @return 获得开始和结束的索引
     */
    private static Pair<Integer, Integer> buildStartAndEndIndexPair(int allPageNo,int currentPageNo,Integer maxIndexPages){
        if (allPageNo <= maxIndexPages){
            return Pair.of(1, allPageNo);
        }

        //**********总页数大于最大导航页数******************************************************************
        // 当前页导航两边总数和
        int fenTwo = maxIndexPages - 1;
        // 当前页左侧导航数
        int leftCount = fenTwo / 2;
        // 当前页右侧导航数
        int rightCount = (fenTwo % 2 == 0) ? leftCount : (leftCount + 1);

        //---------------------------------------------------------------
        // 当前页<=(左边页数+1)
        if (currentPageNo <= (leftCount + 1)){
            return Pair.of(1, maxIndexPages); // 此时迭代结束为maxIndexPages
        }

        // 如果当前页+右边页>=总页数
        if (currentPageNo + rightCount >= allPageNo){
            return Pair.of(allPageNo - maxIndexPages + 1, allPageNo);// 此时迭代结束为allPageNo
        }

        return Pair.of(currentPageNo - leftCount, currentPageNo + rightCount);
    }

    /**
     * Builds the max navigation page numbers.
     *
     * @param currentPageNo
     *            the current page no
     * @param dynamicNavigationPageNumberConfig
     *            the dynamic navigation page number config
     * @return 如果 <code>dynamicNavigationPageNumberConfig</code>是null或者empty,直接返回 {@link PagerConstants#DEFAULT_NAVIGATION_PAGE_NUMBER}<br>
     *         如果 <code>dynamicNavigationPageNumberConfig</code>转换的map是null或者empty,返回 {@link PagerConstants#DEFAULT_NAVIGATION_PAGE_NUMBER}
     *         <br>
     *         如果 <code>currentPageNo</code> {@code >=} <code>dynamicNavigationPageNumberConfig</code>转换的map的key,返回 value <br>
     */
    private static Integer buildMaxNavigationPageNumbers(int currentPageNo,String dynamicNavigationPageNumberConfig){
        if (isNullOrEmpty(dynamicNavigationPageNumberConfig)){
            return DEFAULT_NAVIGATION_PAGE_NUMBER;
        }

        //按照key 倒序之后的map
        Map<Integer, Integer> navigationPageNumberMap = toNavigationPageNumberMap(dynamicNavigationPageNumberConfig);
        if (isNullOrEmpty(navigationPageNumberMap)){
            return DEFAULT_NAVIGATION_PAGE_NUMBER;
        }

        //---------------------------------------------------------------
        for (Map.Entry<Integer, Integer> entry : navigationPageNumberMap.entrySet()){
            Integer key = entry.getKey();
            Integer value = entry.getValue();

            if (currentPageNo >= key){
                return value;
            }
        }
        return DEFAULT_NAVIGATION_PAGE_NUMBER;
    }

    /**
     * 转成导航页码的map.
     *
     * @param dynamicNavigationPageNumberConfig
     *            the dynamic navigation page number config
     * @return the map
     */
    private static Map<Integer, Integer> toNavigationPageNumberMap(String dynamicNavigationPageNumberConfig){
        // 1 将字符串转成 map Map<String, String>
        Map<String, String> singleValueMap = ParamUtil.toSingleValueMap(dynamicNavigationPageNumberConfig, UTF8);

        // 2 转成 Map<Integer, Integer>
        Map<Integer, Integer> integerMap = toMap(singleValueMap, Integer.class, Integer.class);

        //3  排序
        return sortMapByKeyDesc(integerMap);
    }
}
