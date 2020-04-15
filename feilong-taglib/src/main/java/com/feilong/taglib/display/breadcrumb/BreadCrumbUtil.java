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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.URIUtil;
import com.feilong.core.net.URLUtil;
import com.feilong.core.util.AggregateUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbEntity;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbParams;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbVMParams;
import com.feilong.velocity.VelocityUtil;

/**
 * 面包屑渲染核心工具类.
 * 
 * <h3>关于 {@code currentPath} 逻辑:</h3>
 * 
 * <blockquote>
 * 在一堆 {@code List<BreadCrumbEntity<Object>>}中 ,
 * <ul>
 * <li>如果设置了 {@link BreadCrumbParams#setCurrentPath(String)}参数,那么{@code currentPath} 路径,然后渲染到当前节点;</li>
 * <li>如果设置了{@link BreadCrumbParams#setCurrentPath(String)}参数,如果没有找到{@code currentPath},什么都不会渲染;因为{@code currentPath}不在所有的面包屑中</li>
 * <li>如果没有传递{@code currentPath},那么渲染全部的{@code List<BreadCrumbEntity<Object>>},但是如果此时的{@code List<BreadCrumbEntity<Object>>}
 * 不是标准的面包屑树,即如果含有重复的parentId,那么会 throw {@link IllegalArgumentException}
 * </ul>
 * </blockquote>
 * 
 * <h3>关于 {@code BreadCrumbParams#getUrlPrefix()} 逻辑:</h3>
 * 
 * <blockquote>
 * <p>
 * 如果 {@link BreadCrumbEntity#getPath()} 是绝对路径,那么是不会拼接{@code BreadCrumbParams#getUrlPrefix()}的,<br>
 * 如果 {@link BreadCrumbEntity#getPath()} 不是绝对路径,那么会调用 {@link URLUtil#getUnionUrl(URL, String)} 进行union
 * </p>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.2
 */
public class BreadCrumbUtil{

    /** The Constant LOGGER. */
    private static final Logger       LOGGER            = LoggerFactory.getLogger(BreadCrumbUtil.class);

    /** The Constant VELOCITY_UTIL. */
    private static final VelocityUtil VELOCITY_UTIL     = VelocityUtil.INSTANCE;

    /** The Constant VM_KEY_BREADCRUMB. */
    private static final String       VM_KEY_BREADCRUMB = "breadCrumbVMParams";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private BreadCrumbUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 bread crumb content.
     *
     * @param breadCrumbParams
     *            the bread crumb params
     * @return
     *         <ul>
     *         <li>如果 isNullOrEmpty(breadCrumbParams) , throw {@link NullPointerException}</li>
     *         <li>如果 isNullOrEmpty(breadCrumbEntityList) , throw {@link NullPointerException}</li>
     *         <li>如果 isNullOrEmpty(currentBreadCrumbEntityTreeList) , throw {@link StringUtils#EMPTY}</li>
     *         </ul>
     */
    public static String getBreadCrumbContent(BreadCrumbParams breadCrumbParams){
        Validate.notNull(breadCrumbParams, "breadCrumbParams can't be null!");

        List<BreadCrumbEntity<Object>> breadCrumbEntityList = breadCrumbParams.getBreadCrumbEntityList();
        Validate.notEmpty(breadCrumbEntityList, "breadCrumbEntityList can't be null/empty!");
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("input breadCrumbParams info:[{}]", JsonUtil.format(breadCrumbParams));
        }

        //---------------------------------------------------------------
        List<BreadCrumbEntity<Object>> currentBreadCrumbEntityTreeList = lookUpCurrentBreadCrumbEntityTreeList(breadCrumbParams);

        if (isNullOrEmpty(currentBreadCrumbEntityTreeList)){
            return EMPTY;
        }

        //重构path地址
        currentBreadCrumbEntityTreeList = restructureBreadCrumbEntityTreeListPath(
                        currentBreadCrumbEntityTreeList,
                        breadCrumbParams.getUrlPrefix());

        //---------------------------------------------------------------

        BreadCrumbVMParams<Object> breadCrumbVMParams = new BreadCrumbVMParams<>();
        breadCrumbVMParams.setBreadCrumbEntityList(currentBreadCrumbEntityTreeList);
        breadCrumbVMParams.setConnector(breadCrumbParams.getConnector());

        //---------------------------------------------------------------
        Map<String, Object> contextKeyValues = newHashMap();
        contextKeyValues.put(VM_KEY_BREADCRUMB, breadCrumbVMParams);

        String siteMapString = VELOCITY_UTIL.parseTemplateWithClasspathResourceLoader(breadCrumbParams.getVmPath(), contextKeyValues);
        LOGGER.debug("siteMapString is:[{}]", siteMapString);
        return siteMapString;
    }

    //---------------------------------------------------------------

    /**
     * Restructure bread crumb entity tree list path.
     *
     * @param currentBreadCrumbEntityTreeList
     *            the current bread crumb entity tree list
     * @param urlPrefix
     *            the url prefix
     * @return the list< bread crumb entity< object>>
     * @since 1.2.2
     */
    private static List<BreadCrumbEntity<Object>> restructureBreadCrumbEntityTreeListPath(
                    List<BreadCrumbEntity<Object>> currentBreadCrumbEntityTreeList,
                    String urlPrefix){
        if (isNullOrEmpty(urlPrefix)){
            return currentBreadCrumbEntityTreeList;
        }

        for (BreadCrumbEntity<Object> breadCrumbEntity : currentBreadCrumbEntityTreeList){
            String path = breadCrumbEntity.getPath();

            //验证path是不是绝对路径.
            if (URIUtil.create(path).isAbsolute()){//(调用了 {@link java.net.URI#isAbsolute()},原理是 <code>url's scheme !=null</code>).
                //nothing to do 
            }else{
                breadCrumbEntity.setPath(URLUtil.getUnionUrl(URLUtil.toURL(urlPrefix), path));
            }
        }
        return currentBreadCrumbEntityTreeList;
    }

    /**
     * 按照父子关系排序好的 list.
     * 
     * <ol>
     * <li>如果没有传递{@code currentPath},那么渲染全部的{@code List<BreadCrumbEntity<Object>>}, <br>
     * 但是如果此时的{@code List<BreadCrumbEntity<Object>>} 不是标准的面包屑树,即如果含有重复的parentId,那么会 throw {@link IllegalArgumentException}<br>
     * </li>
     * <li>如果设置了{@link BreadCrumbParams#setCurrentPath(String)}参数,如果没有找到{@code currentPath},什么都不会渲染;因为{@code currentPath}不在所有的面包屑中<br>
     * </li>
     * <li>如果设置了 {@link BreadCrumbParams#setCurrentPath(String)}参数,那么{@code currentPath} 路径,然后渲染到当前节点;<br>
     * </li>
     * </ol>
     * 
     * @param <T>
     *            the generic type
     * @param breadCrumbParams
     *            the bread crumb params
     * @return the all parent site map entity list
     */
    private static <T> List<BreadCrumbEntity<T>> lookUpCurrentBreadCrumbEntityTreeList(BreadCrumbParams breadCrumbParams){
        String currentPath = breadCrumbParams.getCurrentPath();
        List<BreadCrumbEntity<T>> breadCrumbEntityList = breadCrumbParams.getBreadCrumbEntityList();

        //---------------------------------------------------------------
        if (isNullOrEmpty(currentPath)){
            //find all
            Map<T, Integer> groupCount = AggregateUtil.groupCount(breadCrumbEntityList, "parentId");
            for (Map.Entry<T, Integer> entry : groupCount.entrySet()){
                Integer value = entry.getValue();
                Validate.isTrue(value <= 1, "currentPath isNullOrEmpty,but breadCrumbEntityList has repeat parentId data!");
            }
            return sortOutAllParentBreadCrumbEntityList(breadCrumbEntityList);
        }

        //---------------------------------------------------------------
        BreadCrumbEntity<T> currentBreadCrumbEntity = getBreadCrumbEntityByPath(currentPath, breadCrumbEntityList);
        if (isNullOrEmpty(currentBreadCrumbEntity)){
            if (LOGGER.isWarnEnabled()){
                LOGGER.warn("when currentPath:{},in breadCrumbEntityList:[{}],can't find", currentPath, JsonUtil.format(breadCrumbParams));
            }

            return emptyList();
        }

        //---------------------------------------------------------------
        return sortOutAllParentBreadCrumbEntityList(currentBreadCrumbEntity, breadCrumbEntityList);
    }

    /**
     * 按照父子关系排序好的 list.
     *
     * @param <T>
     *            the generic type
     * @param breadCrumbEntityList
     *            the bread crumb entity list
     * @return the all parent bread crumb entity list
     */
    private static <T> List<BreadCrumbEntity<T>> sortOutAllParentBreadCrumbEntityList(List<BreadCrumbEntity<T>> breadCrumbEntityList){
        BreadCrumbEntity<T> currentBreadCrumbEntity = null;
        return sortOutAllParentBreadCrumbEntityList(currentBreadCrumbEntity, breadCrumbEntityList);
    }

    //---------------------------------------------------------------

    /**
     * 按照父子关系排序好的 list.
     *
     * @param <T>
     *            the generic type
     * @param currentBreadCrumbEntity
     *            the current bread crumb entity
     * @param breadCrumbEntityList
     *            the bread crumb entity list
     * @return the all parent bread crumb entity list
     */
    private static <T> List<BreadCrumbEntity<T>> sortOutAllParentBreadCrumbEntityList(
                    BreadCrumbEntity<T> currentBreadCrumbEntity,
                    List<BreadCrumbEntity<T>> breadCrumbEntityList){
        if (null == currentBreadCrumbEntity){
            //目前 原样返回, 将来可能支持自动排序
            //TODO 要点工作量
            return breadCrumbEntityList;
        }
        // 每次成一个新的
        List<BreadCrumbEntity<T>> allParentBreadCrumbEntityList = newArrayList();
        allParentBreadCrumbEntityList = constructParentBreadCrumbEntityList(
                        currentBreadCrumbEntity,
                        breadCrumbEntityList,
                        allParentBreadCrumbEntityList);

        LOGGER.info("before Collections.reverse,allParentBreadCrumbEntityList size:{}", allParentBreadCrumbEntityList.size());
        // 反转
        Collections.reverse(allParentBreadCrumbEntityList);
        return allParentBreadCrumbEntityList;
    }

    //---------------------------------------------------------------

    /**
     * 通过当前的BreadCrumbEntity,查找到所有的父节点.
     * 
     * <p style="color:red">
     * 递归生成
     * </p>
     *
     * @param <T>
     *            the generic type
     * @param breadCrumbEntity
     *            the site map entity_in
     * @param siteMapEntities
     *            the site map entities
     * @param allParentBreadCrumbEntityList
     *            the all parent site map entity list
     * @return the list< bread crumb entity< t>>
     */
    private static <T> List<BreadCrumbEntity<T>> constructParentBreadCrumbEntityList(
                    BreadCrumbEntity<T> breadCrumbEntity,
                    List<BreadCrumbEntity<T>> siteMapEntities,
                    List<BreadCrumbEntity<T>> allParentBreadCrumbEntityList){
        // 加入到链式表
        allParentBreadCrumbEntityList.add(breadCrumbEntity);
        T parentId = breadCrumbEntity.getParentId();

        //---------------------------------------------------------------

        for (BreadCrumbEntity<T> loopBreadCrumbEntity : siteMapEntities){
            // 当前的id和传入的breadCrumbEntity equals
            if (loopBreadCrumbEntity.getId().equals(parentId)){
                LOGGER.debug(
                                "loopBreadCrumbEntity.getId():{},breadCrumbEntity_in.getParentId():{}",
                                loopBreadCrumbEntity.getId(),
                                parentId);
                // 递归
                constructParentBreadCrumbEntityList(loopBreadCrumbEntity, siteMapEntities, allParentBreadCrumbEntityList);
                break;
            }
        }
        return allParentBreadCrumbEntityList;
    }

    //---------------------------------------------------------------

    /**
     * 匹配路径.
     *
     * @param <T>
     *            the generic type
     * @param currentPath
     *            the current path
     * @param breadCrumbEntityList
     *            the bread crumb entity list
     * @return the site map entity by path
     */
    private static <T> BreadCrumbEntity<T> getBreadCrumbEntityByPath(String currentPath,List<BreadCrumbEntity<T>> breadCrumbEntityList){
        for (BreadCrumbEntity<T> breadCrumbEntity : breadCrumbEntityList){
            if (breadCrumbEntity.getPath().equals(currentPath)){
                return breadCrumbEntity;
            }
        }
        LOGGER.warn("currentPath is :{},can't find match BreadCrumbEntity", currentPath);
        return null;
    }
}
